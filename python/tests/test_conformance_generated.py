"""Generated — do not edit. Source: metamodel.json via tools/gen_conformance.py.

Anti-drift: TABLE_SHA256 pins the table this file was generated from;
the reflection tests verify the generated Python surface against the
live table. See tools/gen_conformance.py for the mechanism.
"""

import hashlib
import json
from pathlib import Path

import pytest

from sysml import REGISTRY, Model, NotImplementedInToolkit
from sysml import classes as C
from sysml.base import Element, Op, P

TABLE_SHA256 = "17644f3a5d70189d76b5ecb95c2089aeda1f93b6705f8bce5eeae1faf023758b"
_RAW = (Path(__file__).resolve().parents[2] / "metamodel.json").read_bytes()
MM = json.loads(_RAW)["classes"]

# names the Python generator skips (mirror of tools/gen_classes.py)
_SKIP = {"import", "class", "def", "return", "in", "for", "if", "else"}


def _props(name):
    for p, meta in MM[name]["props"].items():
        if p not in _SKIP and p.isidentifier():
            yield p, meta


def _ancestors(name, acc=None):
    acc = set() if acc is None else acc
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            _ancestors(b, acc)
    return acc


def _flat_ops(name):
    """Every operation name `name` declares or inherits."""
    return {op["name"] for c in {name} | _ancestors(name) for op in MM[c]["ops"]}


# an operation that shares its name with a property of a class that has both
_COLLIDING = {o for n in MM for o in _flat_ops(n) if o in MM[n]["props"]}


def _member(o):
    return o + "_op" if o in _COLLIDING else o


def _ops(name):
    """(spec name, member name, parameter names) of the operations `name` itself declares."""
    for op in MM[name]["ops"]:
        o = op["name"]
        if o and o.isidentifier():
            yield o, _member(o), op["params"]


CLASSES = sorted(n for n in MM if n != "Element")
CONCRETE = [n for n in CLASSES if not MM[n]["abstract"]]
MODEL = Model.from_full_json([{"@id": f"e-{n}", "@type": n} for n in CONCRETE])


def test_table_digest_matches_generated_harness():
    assert hashlib.sha256(_RAW).hexdigest() == TABLE_SHA256, (
        "metamodel.json changed since this harness was generated; "
        "rerun tools/gen_conformance.py"
    )


def test_registry_is_exactly_the_concrete_classes():
    assert sorted(REGISTRY) == CONCRETE


@pytest.mark.parametrize("name", CLASSES)
def test_class_matches_table(name):
    klass = getattr(C, name)
    info = MM[name]
    if not info["abstract"]:
        assert REGISTRY[name] is klass
    for b in info["bases"]:
        base = Element if b == "Element" else getattr(C, b)
        assert issubclass(klass, base), f"{name} !< {b}"
    own = vars(klass)
    for p, meta in _props(name):
        d = own.get(p)
        assert isinstance(d, P), f"{name}.{p}: descriptor missing"
        assert (d.shape, d.derived) == (meta["shape"], meta["derived"]), (
            f"{name}.{p}: metadata drift"
        )
    for o, m, params in _ops(name):
        assert isinstance(own.get(m), Op), f"{name}.{m}: op stub missing"
        assert own[m].name == o, f"{name}.{m}: names {own[m].name}"
        assert own[m].params == params, f"{name}.{m}: parameters {own[m].params}"


def test_element_operations_on_the_runtime_base():
    for o, m, _params in _ops("Element"):
        assert isinstance(vars(Element).get(m), Op), f"Element.{m}: op stub missing"


@pytest.mark.parametrize("name", CONCRETE)
def test_runtime_honors_table(name):
    w = MODEL.element(f"e-{name}")
    assert type(w) is REGISTRY[name]
    assert w.metaclass_name == name
    for anc in _ancestors(name):
        base = Element if anc == "Element" else getattr(C, anc)
        assert isinstance(w, base), f"{name} not instance of {anc}"
    for p, _meta in _props(name):
        getattr(w, p)  # a property read never second-guesses the backend
    for o in sorted(_flat_ops(name)):
        with pytest.raises(NotImplementedInToolkit):
            getattr(w, _member(o))()
