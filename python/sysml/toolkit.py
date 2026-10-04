"""The SysML Toolkit backend: the Python core over the binding library (abi/).

Implements the same five-method backend contract as PayloadBackend, so the generated classes
and `Model` are untouched. Handles are the integers the library mints; references come back
as ``{"@id": handle}`` so that `_wrap` turns them into typed elements exactly as it does for
payload models.

Dispatch is static: each generated descriptor carries the C symbol and result shape resolved
at generation time for its declaring class, and ``read`` calls that symbol directly. No
lookup, no base walk. ``get(handle, prop)`` remains as the name-based fallback the shared
`Model` helpers use.

Errors follow the ABI specification: NOT_IMPLEMENTED raises NotImplementedInToolkit,
NOT_APPLICABLE is an absent value, INVALID_HANDLE raises Gone.
"""

from __future__ import annotations

import ctypes as C
import errno
import json
import os
import sys
from typing import Any, Iterable, Optional

from .base import SdkError, Gone, NotImplementedInToolkit, UnresolvedReference
from . import toolkit_table as T

# ---------------------------------------------------------------- C structures

class Str(C.Structure):
    _fields_ = [("ptr", C.c_void_p), ("len", C.c_size_t), ("present", C.c_bool), ("owner", C.c_void_p)]

class Handles(C.Structure):
    _fields_ = [("items", C.POINTER(C.c_uint64)), ("len", C.c_size_t), ("owner", C.c_void_p)]

class Ref(C.Structure):
    _fields_ = [("present", C.c_bool), ("value", C.c_uint64)]

class Bool(C.Structure):
    _fields_ = [("present", C.c_bool), ("value", C.c_bool)]

class Int(C.Structure):
    _fields_ = [("present", C.c_bool), ("value", C.c_int64)]

class Real(C.Structure):
    _fields_ = [("present", C.c_bool), ("value", C.c_double)]

class LoadOptions(C.Structure):
    _fields_ = [
        ("paths", C.POINTER(Str)), ("paths_len", C.c_size_t),
        ("source_names", C.POINTER(Str)), ("source_texts", C.POINTER(Str)), ("sources_len", C.c_size_t),
        ("library_dir", Str),
    ]

# "T", a list of strings, crosses the ABI as JSON text in a Str.
OUT_TYPES = {"H": Handles, "R": Ref, "S": Str, "T": Str, "B": Bool, "I": Int, "F": Real}
OK, NOT_IMPLEMENTED, NOT_APPLICABLE, INVALID_HANDLE, INVALID_ARG, LOAD_FAILED, INTERNAL, UNRESOLVED = range(8)


class ToolkitError(SdkError):
    """A status the ABI specification classes as an error other than the two typed ones."""


# ---------------------------------------------------------------- library loading

def _default_library_path() -> str:
    """SYSMLV2_ABI; else the library a platform wheel carries; else the repository's build."""
    env = os.environ.get("SYSMLV2_ABI")
    if env:
        return env
    name = {"win32": "sysmlv2_abi.dll", "darwin": "libsysmlv2_abi.dylib"}.get(sys.platform, "libsysmlv2_abi.so")
    here = os.path.dirname(os.path.abspath(__file__))  # python/sysml
    packaged = os.path.join(here, "_native", name)
    if os.path.exists(packaged):
        return packaged
    repo = os.path.dirname(os.path.dirname(here))
    return os.path.join(repo, "abi", "target", "release", name)


class Library:
    """One loaded binding library, shared by every session created from it."""

    def __init__(self, path: Optional[str] = None):
        self.path = path or _default_library_path()
        if path is None and not os.path.exists(self.path):
            # The source distribution, on a platform no wheel is built for, carries no library.
            raise FileNotFoundError(
                errno.ENOENT,
                "no binding library for the SysML Toolkit backend: this installation of the SDK "
                f"carries none for this platform ({sys.platform}). Platform wheels carry one for Windows "
                "x64, Linux x64 (glibc 2.28 or later), and macOS on Apple silicon (11 or later) and on "
                "Intel (10.12 or later). Elsewhere, set SYSMLV2_ABI to a sysmlv2_abi library built for "
                "this platform (abi/README.md). Reading interchange JSON (Model.from_full_json) needs "
                "no binding library")
        self.lib = C.CDLL(self.path)
        lib = self.lib
        lib.sysmlv2_free.argtypes = [C.c_void_p]
        lib.sysmlv2_free.restype = None
        lib.sysmlv2_abi_version.restype = C.c_uint32
        lib.sysmlv2_names_digest.restype = C.c_void_p
        lib.sysmlv2_names_digest_len.restype = C.c_size_t
        lib.sysmlv2_session_open.argtypes = [C.POINTER(LoadOptions), C.POINTER(C.c_void_p)]
        lib.sysmlv2_session_close.argtypes = [C.c_void_p]
        lib.sysmlv2_session_roots.argtypes = [C.c_void_p, C.POINTER(Handles)]
        lib.sysmlv2_last_error.argtypes = [C.c_void_p, C.POINTER(Str)]
        lib.sysmlv2_metaclass.argtypes = [C.c_void_p, C.c_uint64, C.POINTER(Str)]
        lib.sysmlv2_user_elements.argtypes = [C.c_void_p, C.POINTER(Handles)]
        lib.sysmlv2_session_full_json.argtypes = [C.c_void_p, C.c_uint32, C.POINTER(Str)]
        digest = C.string_at(lib.sysmlv2_names_digest(), lib.sysmlv2_names_digest_len()).decode()
        if digest != T.NAMES_DIGEST:
            raise ToolkitError(
                f"binding library {self.path} was generated from naming table {digest[:12]}, "
                f"this SDK from {T.NAMES_DIGEST[:12]}; regenerate one side"
            )
        self._fn_cache: dict[str, Any] = {}

    def fn(self, symbol: str, out_type):
        f = self._fn_cache.get(symbol)
        if f is None:
            f = getattr(self.lib, symbol)
            f.argtypes = [C.c_void_p, C.c_uint64, C.POINTER(out_type)]
            f.restype = C.c_int
            self._fn_cache[symbol] = f
        return f

    def take_str(self, s: Str) -> Optional[str]:
        v = C.string_at(s.ptr, s.len).decode() if (s.present and s.ptr) else None
        self.lib.sysmlv2_free(s.owner)
        return v

    def take_handles(self, h: Handles) -> list[int]:
        v = [h.items[i] for i in range(h.len)]
        self.lib.sysmlv2_free(h.owner)
        return v


_DEFAULT: Optional[Library] = None


def library(path: Optional[str] = None) -> Library:
    global _DEFAULT
    if path is not None:
        return Library(path)
    if _DEFAULT is None:
        _DEFAULT = Library()
    return _DEFAULT


# ---------------------------------------------------------------- backend

def _c_str(b: bytes, keep: list) -> Str:
    buf = C.create_string_buffer(b, len(b))
    keep.append(buf)
    return Str(C.cast(buf, C.c_void_p), len(b), True, None)


class ToolkitBackend:
    """A read-only session over the SysML Toolkit, through the binding library."""

    def __init__(self, session: C.c_void_p, lib: Library):
        self._s = session
        self._L = lib
        self._mc: dict[int, str] = {}
        self._declaring: dict[tuple[str, str], tuple[str, str] | None] = {}

    @classmethod
    def open(cls, paths: Iterable[str | os.PathLike] = (), *, sources: dict[str, str] | None = None,
             library_dir: str | os.PathLike | None = None, lib: Optional[Library] = None) -> "ToolkitBackend":
        L = lib or library()
        keep: list = []
        paths = [os.fspath(p) for p in paths]
        library_dir = os.fspath(library_dir) if library_dir is not None else None
        opts = LoadOptions()
        if sources:
            names = (Str * len(sources))(*[_c_str(k.encode(), keep) for k in sources])
            texts = (Str * len(sources))(*[_c_str(v.encode(), keep) for v in sources.values()])
            opts.source_names, opts.source_texts, opts.sources_len = names, texts, len(sources)
        elif paths:
            arr = (Str * len(paths))(*[_c_str(p.encode(), keep) for p in paths])
            opts.paths, opts.paths_len = arr, len(paths)
        else:
            raise ToolkitError("open needs paths or sources")
        for p in ([] if sources else paths):
            if not os.path.isfile(p):
                raise FileNotFoundError(errno.ENOENT, "model file not found", os.path.abspath(p))
        if library_dir and not os.path.isdir(library_dir):
            raise FileNotFoundError(errno.ENOENT, "library directory not found", os.path.abspath(library_dir))
        opts.library_dir = _c_str(library_dir.encode(), keep) if library_dir else Str(None, 0, False, None)
        sess = C.c_void_p()
        st = L.lib.sysmlv2_session_open(C.byref(opts), C.byref(sess))
        if st != OK:
            raise ToolkitError(f"the SysML Toolkit could not load {paths or list(sources)}: status {st} (the SysML Toolkit's reason is on stderr)")
        return cls(sess, L)

    def close(self) -> None:
        if self._s:
            self._L.lib.sysmlv2_session_close(self._s)
            self._s = None

    def __del__(self):
        try:
            self.close()
        except Exception:
            pass

    # -- errors --------------------------------------------------------------

    def _error(self) -> str:
        out = Str()
        self._L.lib.sysmlv2_last_error(self._s, C.byref(out))
        return self._L.take_str(out) or ""

    def _check(self, st: int, where: str) -> bool:
        """True when a value is present; False for NOT_APPLICABLE; raises otherwise."""
        if st == OK:
            return True
        if st == NOT_APPLICABLE:
            return False
        msg = self._error()
        if st == NOT_IMPLEMENTED:
            raise NotImplementedInToolkit(msg or f"{where}: not implemented by the SysML Toolkit")
        if st == INVALID_HANDLE:
            raise Gone(msg or f"{where}: invalid handle")
        if st == UNRESOLVED:
            raise UnresolvedReference(msg or f"{where}: a reference did not resolve")
        raise ToolkitError(f"{where}: status {st}: {msg}")

    # -- dispatch ------------------------------------------------------------

    def _declaring_entry(self, mc: str, prop: str):
        key = (mc, prop)
        if key in self._declaring:
            return self._declaring[key]
        seen, stack, found = set(), [mc], None
        while stack:
            c = stack.pop()
            if c in seen:
                continue
            seen.add(c)
            e = T.FUNCS.get((c, prop))
            if e is not None:
                found = e
                break
            stack.extend(T.BASES.get(c, []))
        self._declaring[key] = found
        return found

    def _call(self, symbol: str, code: str, handle: int, where: str) -> Any:
        out_t = OUT_TYPES[code]
        out = out_t()
        st = self._L.fn(symbol, out_t)(self._s, C.c_uint64(handle), C.byref(out))
        if not self._check(st, where):
            return [] if code in ("H", "T") else None
        return self._unpack(code, out)

    def _unpack(self, code: str, out) -> Any:
        if code == "H":
            return [{"@id": h} for h in self._L.take_handles(out)]
        if code == "R":
            return {"@id": out.value} if out.present else None
        if code == "S":
            return self._L.take_str(out)
        if code == "T":  # a list of strings (a requirement's `text`), as JSON text
            text = self._L.take_str(out)
            return [] if text is None else json.loads(text)
        return out.value if out.present else None

    # -- the backend contract --------------------------------------------------

    def read(self, handle: int, prop) -> Any:
        """Static dispatch: the descriptor already names the binding library function."""
        return self._call(prop.symbol, prop.code, handle, f"{self.metaclass(handle)}.{prop.name}")

    _PARAM_CTYPES = {"Handle": C.c_uint64, "bool": C.c_bool, "Str": C.POINTER(Str), "Handles": C.POINTER(Handles)}

    def call(self, handle: int, op, args: tuple) -> Any:
        """Invoke a specification operation with its declared parameters."""
        if len(args) != len(op.param_types):
            raise TypeError(f"{op.name}() takes {len(op.param_types)} argument(s), {len(args)} given")
        keep: list = []
        cargs = []

        def handle_of(value, what: str) -> int:
            # The binding library has no "no element": a missing element is refused here, never
            # passed as a handle that names some other element.
            if hasattr(value, "_handle"):
                return value._handle
            if isinstance(value, int) and not isinstance(value, bool):
                return value
            raise TypeError(f"{self.metaclass(handle)}.{op.name}(): {what} must be an element, not {value!r}")

        for (pname, ptype), value in zip(op.param_types, args):
            if ptype == "Handle":
                cargs.append(C.c_uint64(handle_of(value, pname)))
            elif ptype == "bool":
                cargs.append(C.c_bool(bool(value)))
            elif ptype == "Str":  # an element as its element id, as every binding passes it
                text = value.element_id if hasattr(value, "element_id") else "" if value is None else str(value)
                cargs.append(C.byref(_c_str(text.encode(), keep)))
            else:  # Handles
                hs = [handle_of(v, f"each of {pname}") for v in (value or [])]
                arr = (C.c_uint64 * max(1, len(hs)))(*hs)
                keep.append(arr)
                keep.append(Handles(C.cast(arr, C.POINTER(C.c_uint64)), len(hs), None))
                cargs.append(C.byref(keep[-1]))
        out_t = OUT_TYPES[op.code]
        f = getattr(self._L.lib, op.symbol)
        f.argtypes = [C.c_void_p, C.c_uint64] + [self._PARAM_CTYPES[t] for _, t in op.param_types] + [C.POINTER(out_t)]
        f.restype = C.c_int
        out = out_t()
        st = f(self._s, C.c_uint64(handle), *cargs, C.byref(out))
        where = f"{self.metaclass(handle)}.{op.name}()"
        if not self._check(st, where):
            return [] if op.code == "H" else None
        return self._unpack(op.code, out)

    def full_json(self, closures: bool = True) -> str:
        """The model as full-form interchange JSON, from the SysML Toolkit's own emitter.

        With `closures` (the default) the inheritance-aware properties carry the specification's
        values, inherited and imported members included. Without, they carry the owned side only,
        as the SysML Toolkit's command-line export writes them.
        """
        out = Str()
        st = self._L.lib.sysmlv2_session_full_json(self._s, 1 if closures else 0, C.byref(out))
        self._check(st, "full_json")
        return self._L.take_str(out) or "[]"

    def get(self, handle: int, prop: str) -> Any:
        mc = self.metaclass(handle)
        entry = self._declaring_entry(mc, prop)
        if entry is None:
            return None  # not a member of this metaclass: absent, as the payload backend answers
        symbol, code, _ = entry
        return self._call(symbol, code, handle, f"{mc}.{prop}")

    def metaclass(self, handle: int) -> str:
        mc = self._mc.get(handle)
        if mc is None:
            out = Str()
            st = self._L.lib.sysmlv2_metaclass(self._s, C.c_uint64(handle), C.byref(out))
            self._check(st, f"metaclass({handle})")
            mc = self._L.take_str(out) or ""
            self._mc[handle] = mc
        return mc

    def element_id(self, handle: int) -> str:
        return self._call("sysmlv2_element_element_id", "S", handle, "elementId") or ""

    def resolve(self, qualified_name: str) -> Optional[int]:
        keep: list = []
        arg = _c_str(qualified_name.encode(), keep)
        f = self._L.lib.sysmlv2_session_resolve
        f.argtypes = [C.c_void_p, C.POINTER(Str), C.POINTER(Ref)]
        f.restype = C.c_int
        out = Ref()
        st = f(self._s, C.byref(arg), C.byref(out))
        if not self._check(st, f"resolve({qualified_name!r})"):
            return None
        return out.value if out.present else None

    def all_handles(self) -> Iterable[int]:
        out = Handles()
        st = self._L.lib.sysmlv2_user_elements(self._s, C.byref(out))
        self._check(st, "user_elements")
        return self._L.take_handles(out)

    def roots(self) -> list[int]:
        out = Handles()
        st = self._L.lib.sysmlv2_session_roots(self._s, C.byref(out))
        self._check(st, "roots")
        return self._L.take_handles(out)
