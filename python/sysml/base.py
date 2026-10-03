"""Runtime for the generated SDK: element base class, property
descriptors, the Model entry point, and the payload backend.

Design contract: generated classes never see a backend directly —
every property access routes through Model._backend.get(). A property
read returns whatever the backend gave: the SysML Toolkit reports its
own unimplemented members, so the SDK never second-guesses a result.

Collision rule (Python backend): every public runtime member on Element
contains an underscore (element_id, backend_handle, metaclass_name,
get_raw). Spec property and operation names are strict lowerCamelCase
and can never contain one, so a generated descriptor can never shadow a
runtime member. tools/gen_classes.py enforces the invariant at
generation time.
"""

from __future__ import annotations

from typing import Any, Iterable, Optional


class SdkError(Exception):
    """Base of all SDK-specific errors."""


class NotComputed(SdkError):
    """A property the backend reports it does not compute.

    Reserved for a backend that says so itself; the SDK never infers it
    from an empty result.
    """


class NotImplementedInToolkit(SdkError):
    """A specification member the SysML Toolkit does not answer: raised
    when the SysML Toolkit reports the member as not implemented, and for
    every operation on a payload model."""


class ReadOnly(SdkError):
    """Edit attempted on a read-only backend (payload-backed model)."""


class Gone(SdkError):
    """The element no longer exists in the backend's current state."""


class UnresolvedReference(SdkError):
    """A reference that did not resolve — a SysML Toolkit `@ref`
    spelling passthrough (non-standard, best-effort) or a dangling `@id`.

    The SDK requires resolved models: typed navigation throws instead of
    yielding placeholder objects. The
    message carries everything known about the reference; `get_raw()`
    still exposes the raw value for forensics.
    """


def _is_id_ref(v: Any) -> bool:
    return isinstance(v, dict) and set(v.keys()) == {"@id"}


class P:
    """Descriptor for one spec property (generated classes declare these).

    shape: A=array, B=boolean, N=nullable, R=required ref, S/E=scalar.
    derived: the XMI derived flag (spec information, not a promise
    about what any particular backend computes).
    """

    __slots__ = ("name", "shape", "derived", "symbol", "code")

    def __init__(self, name: str, shape: str, derived: bool, symbol: str = "", code: str = ""):
        self.name = name
        self.shape = shape
        self.derived = derived
        # Static dispatch data, resolved at generation time for the declaring class:
        # the binding library function that answers this member and its result shape. A backend
        # that routes by name (the payload one) ignores them.
        self.symbol = symbol
        self.code = code

    def __set_name__(self, owner: type, attr: str) -> None:
        if attr != self.name:  # generated code always matches; guard anyway
            self.name = attr

    def __get__(self, obj: Optional["Element"], objtype: type | None = None):
        if obj is None:
            return self
        model = obj._model
        raw = model._backend.read(obj._handle, self)
        if raw is None and self.shape == "A":
            return []  # absent arrays normalize to empty (typed-surface rule)
        return _wrap(model, raw, obj, self.name)

    def __set__(self, obj: "Element", value: Any) -> None:
        raise ReadOnly(
            f"{type(obj).__name__}.{self.name}: property assignment requires an "
            f"edit transaction on a live backend (payload models are read-only)"
        )


def _wrap(model: "Model", raw: Any, owner: "Element" = None, prop: str = "?"):
    def _at() -> str:
        if owner is None:
            return prop
        return f"{type(owner).__name__}.{prop} (element {owner._handle})"

    if _is_id_ref(raw):
        try:
            return model.element(raw["@id"])
        except Gone:
            raise UnresolvedReference(
                f"dangling reference @id={raw['@id']!r} at {_at()}: target is "
                f"not in this model; the SDK requires resolved models"
            ) from None
    if isinstance(raw, dict) and "@ref" in raw:
        raise UnresolvedReference(
            f"unresolved reference {raw['@ref']!r} at {_at()} (the SysML Toolkit's "
            f"@ref passthrough is non-standard best-effort); the SDK requires "
            f"resolved models — use .get_raw({prop!r}) for the raw value"
        )
    if isinstance(raw, list):
        return [_wrap(model, v, owner, prop) for v in raw]
    return raw


class Op:
    """A spec operation. Carries the same static dispatch data as `P` plus the
    parameter list; the backend decides whether it can be called."""

    __slots__ = ("name", "params", "symbol", "code", "param_types")

    def __init__(self, name: str, params: list[str], symbol: str = "", code: str = "",
                 param_types: tuple = ()):
        self.name = name
        self.params = params
        self.symbol = symbol
        self.code = code
        self.param_types = param_types

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        op = self

        def _call(*args):
            model = obj._model
            raw = model._backend.call(obj._handle, op, args)
            if raw is None and op.code == "H":
                return []
            return _wrap(model, raw, obj, op.name)

        _call.__name__ = self.name
        return _call


class Element:
    """Base of every generated metaclass.

    A wrapper holds (model, backend handle); identity is by the
    backend's stable element id. Payload backends use the
    element id itself as the handle.

    Public runtime members must contain an underscore (collision rule —
    see the module docstring): the spec property `metaclass` on
    MetadataFeature/MetadataUsage is a generated descriptor like any
    other, so the runtime accessor is spelled `metaclass_name`.
    """

    __slots__ = ("_model", "_handle")

    def __init__(self, model: "Model", handle: Any):
        self._model = model
        self._handle = handle

    @property
    def element_id(self) -> str:
        """The element's stable id, via the backend's element_id(handle)."""
        return self._model._backend.element_id(self._handle)

    @property
    def backend_handle(self) -> Any:
        """The backend handle this wrapper holds (= id for payload backends)."""
        return self._handle

    @property
    def metaclass_name(self) -> str:
        """The element's metaclass, as the backend reports it."""
        return self._model._backend.metaclass(self._handle)

    def get_raw(self, prop: str) -> Any:
        """Escape hatch: the property's raw backend value, unwrapped."""
        return self._model._backend.get(self._handle, prop)

    def __eq__(self, other: object) -> bool:
        return (
            isinstance(other, Element)
            and other._model is self._model
            and other.element_id == self.element_id
        )

    def __hash__(self) -> int:
        return hash(self.element_id)

    def __repr__(self) -> str:
        qn = self._model._backend.get(self._handle, "qualifiedName")
        label = qn if isinstance(qn, str) and qn else self.element_id
        return f"<{type(self).__name__} {label}>"


def _index(elements: list[dict]) -> tuple[dict[str, dict], dict[str, str]]:
    """Elements by id (document order, the last of a repeated id) and ids by qualified name (the
    first)."""
    by_id: dict[str, dict] = {}
    by_qname: dict[str, str] = {}
    for e in elements:
        eid = e.get("@id")
        if not isinstance(eid, str):
            continue
        by_id[eid] = e
        qn = e.get("qualifiedName")
        if isinstance(qn, str) and qn:
            by_qname.setdefault(qn, eid)
    return by_id, by_qname


class PayloadLibrary:
    """A standard library read from a full-form interchange element array, such as the
    library JSON published with the SDK. Index it once and pass it to any number of payload
    models: their references into the library then resolve by the library's normative ids."""

    def __init__(self, elements: list[dict]):
        self._by_id, self._by_qname = _index(elements)

    def __len__(self) -> int:
        return len(self._by_id)


class PayloadBackend:
    """Read-only backend over a full-form interchange element array.

    The Backend protocol is defined over opaque per-backend
    element *handles*; an id-keyed backend like this one uses element
    ids as its handles.

    With a library, an id or qualified name the payload does not hold is looked up in the library;
    the library's elements are not the model's, so `all_handles` lists the payload's only.
    """

    def __init__(self, elements: list[dict], library: Optional[PayloadLibrary] = None):
        self._by_id, self._by_qname = _index(elements)
        self._library = library

    def _require(self, handle: str) -> dict:
        el = self._by_id.get(handle)
        if el is None and self._library is not None:
            el = self._library._by_id.get(handle)
        if el is None:
            raise Gone(f"no element {handle} in this payload")
        return el

    def get(self, handle: str, prop: str) -> Any:
        return self._require(handle).get(prop)

    def read(self, handle: str, prop: "P") -> Any:
        return self.get(handle, prop.name)

    def call(self, handle: str, op: "Op", args: tuple) -> Any:
        raise NotImplementedInToolkit(
            f"{op.name}(): operations are not available on payload models; "
            f"load the model through the SysML Toolkit backend"
        )

    def metaclass(self, handle: str) -> str:
        return self._require(handle)["@type"]

    def element_id(self, handle: str) -> str:
        self._require(handle)
        return handle

    def resolve(self, qualified_name: str) -> Optional[str]:
        handle = self._by_qname.get(qualified_name)
        if handle is None and self._library is not None:
            handle = self._library._by_qname.get(qualified_name)
        return handle

    def all_handles(self) -> Iterable[str]:
        return self._by_id.keys()


class Model:
    """Entry point: wraps a backend and mints typed wrappers by metaclass."""

    def __init__(self, backend):
        from . import classes  # registry import deferred to avoid a cycle

        self._backend = backend
        self._registry: dict[str, type] = classes.REGISTRY
        self._cache: dict[str, Element] = {}

    @classmethod
    def from_full_json(cls, elements: list[dict], library: Optional[PayloadLibrary] = None) -> "Model":
        """A model read from a full-form interchange element array. With `library`, references
        into the standard library resolve against it; its elements are not listed as the model's."""
        return cls(PayloadBackend(elements, library))

    @classmethod
    def from_toolkit(cls, *paths: "str | os.PathLike", sources: dict[str, str] | None = None,
                    library_dir: "str | os.PathLike | None" = None) -> "Model":
        """A model read directly from the SysML Toolkit through the binding library (abi/)."""
        from .toolkit import ToolkitBackend  # deferred: ctypes and the library load on demand

        return cls(ToolkitBackend.open(paths, sources=sources, library_dir=library_dir))

    def element(self, handle) -> Element:
        w = self._cache.get(handle)
        if w is None:
            mc = self._backend.metaclass(handle)  # raises Gone if absent
            klass = self._registry.get(mc, Element)
            w = klass(self, handle)
            self._cache[handle] = w
        return w

    def resolve(self, qualified_name: str) -> Optional[Element]:
        handle = self._backend.resolve(qualified_name)
        return None if handle is None else self.element(handle)

    def all(self) -> list[Element]:
        """Every element of the model (a payload's library elements are not the model's)."""
        return [self.element(h) for h in self._backend.all_handles()]

    def elements_of_type(self, klass, *, exact: bool = False) -> list[Element]:
        if isinstance(klass, str):
            klass = self._registry[klass]
        out = []
        for h in self._backend.all_handles():
            w = self.element(h)
            ok = type(w) is klass if exact else isinstance(w, klass)
            if ok:
                out.append(w)
        return out

    @property
    def roots(self) -> list[Element]:
        """Top-level namespaces. Ownerless alone is not enough: the
        SysML Toolkit serializes memberships with owner: null (as in
        data/bigger.full.json), so non-namespace ownerless elements are
        excluded."""
        from .classes import Namespace  # deferred: avoids import cycle

        return [
            w
            for h in self._backend.all_handles()
            if not self._backend.get(h, "owner")
            for w in [self.element(h)]
            if isinstance(w, Namespace)
        ]
