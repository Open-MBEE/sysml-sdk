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

import enum
import json
import math
from typing import Any, Iterable, Optional


class SdkError(Exception):
    """Base of all SDK-specific errors."""


class NotComputed(SdkError):
    """A property the backend reports it does not compute.

    Reserved for a backend that says so itself; the SDK never infers it
    from an empty result.
    """


class NotAnswered(SdkError):
    """A specification member the backend does not answer for this element: the SysML Toolkit's
    refusals (`NotImplementedInToolkit`), and an engine's (docs/engine-protocol.md, kind
    ``refused``). Catch it to handle every backend's refusals alike."""


class Refused(NotAnswered):
    """A convenience member that gives no answer for this element by its contract (`invalid` in its
    OCL): the literal of an expression that is not a literal, a bound that is an expression. Unlike the
    other NotAnswered errors, it says what the answer is, not that the backend cannot give it."""


class NotImplementedInToolkit(NotAnswered):
    """A specification member the SysML Toolkit does not answer: raised
    when the SysML Toolkit reports the member as not implemented, and for
    every operation on a payload model."""


class BackendError(SdkError):
    """The engine behind a backend could not answer: it failed while computing a value, or it is
    not available (not installed, not downloadable, or no suitable runtime). The message says
    which engine and why."""


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


class Scope(str, enum.Enum):
    """Which members a convenience list reads: the element's own, those it inherits (for a
    namespace's members, imported ones too), or both. A list of features gives its own first; a list
    of members or definitions, outside the owned scope, is in the backend's order."""

    OWNED = "owned"
    INHERITED = "inherited"
    ALL = "all"


class VisibilityFilter(str, enum.Enum):
    """Which visibilities a convenience list keeps: every one, public and protected, or public."""

    ANY = "any"
    PROTECTED = "protected"
    PUBLIC = "public"


_ENUMS = {"scope": Scope, "visibility": VisibilityFilter}
_UNBOUNDED_INT = 2**63 - 1  # the binding library's `*`


def decode_literal(raw: Any) -> Any:
    """A literal's value as tagged JSON ({"integer": 4}, {"real": 0.5}, {"boolean": true},
    {"string": "a"}, {"unbounded": true}) to a Python value; `*` is math.inf."""
    if isinstance(raw, str):
        raw = json.loads(raw)
    if not isinstance(raw, dict) or len(raw) != 1:
        raise SdkError(f"a literal value was expected, not {raw!r}")
    (kind, value), = raw.items()
    if kind == "unbounded":
        return math.inf
    if kind in ("integer", "real", "boolean", "string"):
        return value
    raise SdkError(f"a literal value was expected, not {raw!r}")


class Find:
    """A convenience member (convenience.json), answered by the backend; generated classes
    declare these under their Python spelling (`find_parts`).

    params: (name, kind, default) per parameter, kind one of scope, visibility, bool, str,
    element; a default of None means the argument is required. result: elements, element,
    bool, int, unlimited (an UnlimitedNatural: `*` is math.inf), real, strings, literal, map
    (names to elements, in order: a dict).
    """

    __slots__ = ("name", "spelled", "params", "result")

    def __init__(self, name: str, spelled: str, params: tuple, result: str):
        self.name = name
        self.spelled = spelled
        self.params = params
        self.result = result

    def bind(self, args: tuple, kwargs: dict) -> list:
        if len(args) > len(self.params):
            raise TypeError(f"{self.spelled}() takes {len(self.params)} argument(s), {len(args)} given")
        values = list(args)
        for pname, kind, default in self.params[len(args):]:
            if pname in kwargs:
                values.append(kwargs.pop(pname))
            elif default is None:
                raise TypeError(f"{self.spelled}() needs the argument {pname}")
            else:
                values.append(_ENUMS[kind](default) if kind in _ENUMS else default)
        if kwargs:
            raise TypeError(f"{self.spelled}() has no parameter {', '.join(kwargs)}")
        out = []
        for (pname, kind, _), v in zip(self.params, values):
            if kind in _ENUMS:
                v = _ENUMS[kind](v)
            elif kind == "bool":
                v = bool(v)
            elif kind == "str" and not isinstance(v, str):
                raise TypeError(f"{self.spelled}(): {pname} must be a string, not {v!r}")
            elif kind == "element" and not isinstance(v, Element):
                raise TypeError(f"{self.spelled}(): {pname} must be an element, not {v!r}")
            out.append(v)
        return out

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        find = self

        def _call(*args, **kwargs):
            model = obj._model
            raw = model._backend.find(obj._handle, find, find.bind(args, kwargs))
            return find.convert(model, raw, obj)

        _call.__name__ = self.spelled
        return _call

    def each(self, elements: list, values: list):
        """This member over a list of elements (see ElementList): list results concatenated in list
        order, single results with the absent ones dropped. One backend call per model."""
        out: list = [] if self.result == "strings" else ElementList()
        if not elements:
            return out
        for el in elements:
            if not isinstance(el, Element) or not isinstance(getattr(type(el), self.spelled, None), Find):
                what = el.metaclass_name if isinstance(el, Element) else type(el).__name__
                raise TypeError(f"{self.spelled}() does not apply to a {what}")
        model = elements[0]._model
        if all(el._model is model for el in elements):
            raws = model._backend.find_each([el._handle for el in elements], self, values)
        else:
            raws = [el._model._backend.find(el._handle, self, values) for el in elements]
        for el, raw in zip(elements, raws):
            v = self.convert(el._model, raw, el)
            if self.result == "element":
                if v is not None:
                    out.append(v)
            else:
                out.extend(v)
        return out

    def convert(self, model: "Model", raw: Any, owner: "Element"):
        r = self.result
        if r == "elements":
            return ElementList() if raw is None else ElementList(_wrap(model, raw, owner, self.name))
        if r == "element":
            return None if raw is None else _wrap(model, raw, owner, self.name)
        if r == "map":  # pairs of a name and an element, in order
            return {name: _wrap(model, ref, owner, self.name) for name, ref in (raw or [])}
        if raw is None:
            return [] if r == "strings" else None
        if r == "unlimited":
            return math.inf if raw in ("*", _UNBOUNDED_INT) else raw
        if r == "literal":
            return decode_literal(raw)
        if r == "real":
            return float(raw)
        return raw


# The results a list carries a member for: elements and strings (Booleans, numbers and literals stay
# on the elements).
_LIST_RESULTS = ("elements", "element", "strings")
_LIST_MEMBERS: Optional[dict] = None


def _list_members() -> dict:
    """The convenience members a list can carry, by spelling (from the generated classes)."""
    global _LIST_MEMBERS
    if _LIST_MEMBERS is None:
        from . import classes

        found: dict = {}
        for cls in [Element, *(c for c in vars(classes).values() if isinstance(c, type) and issubclass(c, Element))]:
            for v in vars(cls).values():
                if isinstance(v, Find) and v.result in _LIST_RESULTS:
                    found.setdefault(v.spelled, v)
        _LIST_MEMBERS = found
    return _LIST_MEMBERS


class ElementList(list):
    """A list of elements that carries the convenience members of its elements: `xs.find_ports()`
    is the elements' `find_ports()` in list order, list results concatenated (duplicates kept) and
    single results with the absent ones dropped; the result is again an ElementList (a list of
    strings for `find_doc_text`). If the member refuses for one element, the call refuses. Members
    that answer a Boolean, a number or a literal stay on the elements. Every element list a
    convenience member returns is one; any list of elements can be lifted: `ElementList(xs)`."""

    __slots__ = ()

    def __getattr__(self, name: str):
        if name.startswith("_"):
            raise AttributeError(name)
        member = _list_members().get(name)
        if member is None:
            raise AttributeError(f"'ElementList' object has no attribute {name!r}")

        def _call(*args, **kwargs):
            return member.each(self, member.bind(args, kwargs))

        _call.__name__ = name
        return _call


_ESCAPES = {"'": "'", '"': '"', "b": "\b", "f": "\f", "t": "\t", "n": "\n", "\\": "\\"}


def split_qualified_name(qualified_name: str) -> list:
    """A qualified name's segments as (written, name) pairs (KerML 8.2.3.4.1): separated by `::`; an
    unrestricted name in single quotes, its escape sequences resolved (KerML 8.2.2.3); a leading global
    scope qualifier `$` dropped."""
    qn, out, i = qualified_name, [], 0
    while i < len(qn):
        if qn[i] == "'":
            j, name = i + 1, []
            while j < len(qn) and qn[j] != "'":
                if qn[j] == "\\" and j + 1 < len(qn):
                    name.append(_ESCAPES.get(qn[j + 1], qn[j + 1]))
                    j += 2
                else:
                    name.append(qn[j])
                    j += 1
            if j >= len(qn):
                raise SdkError(f"an unterminated quoted name in {qualified_name!r}")
            out.append((qn[i:j + 1], "".join(name)))
            i = j + 1
        else:
            j = qn.find("::", i)
            j = len(qn) if j < 0 else j
            out.append((qn[i:j], qn[i:j]))
            i = j
        if i < len(qn):
            if not qn.startswith("::", i):
                raise SdkError(f"a name in {qualified_name!r} is not followed by '::'")
            i += 2
    if out and out[0][1] == "$":
        out = out[1:]
    return out


def _member_named(memberships, name: str):
    """The member element of the first membership with this member name or short name."""
    for m in memberships:
        if m.memberName == name or m.memberShortName == name:
            return m.memberElement
    return None


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

    def find(self, handle: str, member: "Find", args: list) -> Any:
        raise NotAnswered(
            f"{member.name}: the convenience members are not available on payload models; "
            f"load the model through the SysML Toolkit or the Pilot Implementation"
        )

    def find_each(self, handles: list, member: "Find", args: list) -> list:
        return [self.find(h, member, args) for h in handles]

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

    @classmethod
    def from_pilot(cls, *paths: "str | os.PathLike", sources: dict[str, str] | None = None,
                   **options) -> "Model":
        """A model read by the SysML v2 Pilot Implementation (a Java runtime runs it; see
        `sysml.pilot.PilotBackend.open` for the options)."""
        from .pilot import PilotBackend  # deferred: the Pilot starts on demand

        return cls(PilotBackend.open(paths, sources=sources, **options))

    def element(self, handle) -> Element:
        w = self._cache.get(handle)
        if w is None:
            mc = self._backend.metaclass(handle)  # raises Gone if absent
            klass = self._registry.get(mc, Element)
            w = klass(self, handle)
            self._cache[handle] = w
        return w

    def resolve(self, name: str, context: Optional[Element] = None) -> Optional[Element]:
        """Name resolution, with visibility (KerML 8.2.3.5). With a `context` namespace: the
        specification's `Namespace::resolve` on it (its member element), as the backend answers it.
        Without one: the backend's resolution from the root. A payload model resolves its elements' own
        qualified names; the SysML Toolkit's lookup does not consider visibility yet. Only names the model
        gives count: an unnamed element is not found under the name of what it references. To get an
        element by its qualified name whatever its visibility, use `find`."""
        if context is None:
            handle = self._backend.resolve(name)
            return None if handle is None else self.element(handle)
        resolve = getattr(context, "resolve", None)
        if resolve is None:
            raise TypeError(f"resolve(): the context must be a namespace, not a {context.metaclass_name}")
        m = resolve(name)
        return None if m is None else m.memberElement

    def find(self, qualified_name: str, inherited: bool = False) -> Optional[Element]:
        """The element a qualified name names, whatever its visibility, the same on every backend: the
        first segment is a top-level name; each next one a member of the namespace before it, through
        one of its owned memberships (an alias's name included) or, with `inherited`, also through its
        inherited memberships (its supertypes' non-private ones). None if a segment names nothing.
        Segments may be quoted, with KerML's escapes (`'~Plug'`)."""
        from .classes import Namespace, Type  # deferred: avoids import cycle

        handle = self._backend.resolve(qualified_name)
        if handle is not None:
            el = self.element(handle)
            if el.qualifiedName == qualified_name:
                return el
        segments = split_qualified_name(qualified_name)
        if not segments:
            return None
        handle = self._backend.resolve(segments[0][0])
        current = self.element(handle) if handle is not None else None
        if current is None:
            for root in self.roots:
                current = _member_named(root.ownedMembership, segments[0][1])
                if current is not None:
                    break
        for _, name in segments[1:]:
            if not isinstance(current, Namespace):
                return None
            found = _member_named(current.ownedMembership, name)
            if found is None and inherited and isinstance(current, Type):
                found = _member_named(current.inheritedMembership, name)
            current = found
        return current

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
