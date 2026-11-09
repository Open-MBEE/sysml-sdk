"""Model.find (an element by its qualified name, whatever its visibility, the same on every backend) and
Model.resolve (name resolution with visibility, from the root or a context namespace).

find runs on a payload model (the SysML Toolkit's export of the same text), the SysML Toolkit and the
Pilot Implementation; each backend skips without what it needs. resolve with a context is the
specification's Namespace::resolve: the Pilot's answers are checked, the SysML Toolkit may refuse.
"""

from __future__ import annotations

import json
import os

import pytest
from conftest import pilot_at_hand, pilot_server, toolkit_library

from sysml import Model, NotAnswered, SdkError, UnresolvedReference
from sysml.base import split_qualified_name

from convenience_check import ROOT

RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"
TEXT = """package Scopes {
    part def P;
    part def Base { part a : P; protected part b : P; private part c : P; }
    part def Derived :> Base { part d : P; }
    package Inner { public import Scopes::Base::*; part x : P; }
    package Hidden { private part h : P; }
    alias Alias for Base;
    part def 'odd name' { part 'it\\'s' : P; }
    requirement def Spec;
    package Reqs { requirement spec : Spec; }
    part v { satisfy Reqs::spec by v; }
}"""


def test_split_qualified_name():
    assert split_qualified_name("A::B::C") == [("A", "A"), ("B", "B"), ("C", "C")]
    assert split_qualified_name("Typing::Plug::'~Plug'") == [("Typing", "Typing"), ("Plug", "Plug"), ("'~Plug'", "~Plug")]
    assert split_qualified_name("'a::b'::c") == [("'a::b'", "a::b"), ("c", "c")]
    assert split_qualified_name("'it\\'s'") == [("'it\\'s'", "it's")]
    assert split_qualified_name("A::'x\\\\y\\ty'") == [("A", "A"), ("'x\\\\y\\ty'", "x\\y\ty")]
    assert split_qualified_name("$::A::B") == [("A", "A"), ("B", "B")]
    with pytest.raises(SdkError):
        split_qualified_name("'open")


@pytest.fixture(scope="module", params=["payload", "toolkit", "pilot"])
def model(request, tmp_path_factory):
    if request.param in ("payload", "toolkit"):
        if toolkit_library() is None:
            pytest.skip("binding library not built (abi/)")
        if not RELEASE_LIBRARY.is_dir():
            pytest.skip("sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository")
        m = Model.from_toolkit(sources={"s.sysml": TEXT}, library_dir=str(RELEASE_LIBRARY))
        yield m if request.param == "toolkit" else Model.from_full_json(json.loads(m._backend.full_json()))
        return
    reason = pilot_at_hand()
    if reason is not None:
        pytest.skip(reason)
    from sysml.pilot import PilotBackend

    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    backend = PilotBackend.open(sources={"s.sysml": TEXT}, transport="process", heap="1g")
    try:
        yield Model(backend)
    finally:
        backend.close()


def qn(el):
    return None if el is None else el.qualifiedName


def test_find_whatever_the_visibility(model):
    assert qn(model.find("Scopes::Base::a")) == "Scopes::Base::a"
    assert qn(model.find("Scopes::Base::b")) == "Scopes::Base::b"
    assert qn(model.find("Scopes::Base::c")) == "Scopes::Base::c"
    assert qn(model.find("Scopes::Hidden::h")) == "Scopes::Hidden::h"
    assert model.find("Scopes::Base::nothing") is None
    assert model.find("Nothing::At::All") is None


def test_find_through_inheritance_only_when_asked(model):
    assert model.find("Scopes::Derived::b") is None
    try:
        assert qn(model.find("Scopes::Derived::b", inherited=True)) == "Scopes::Base::b"
        assert qn(model.find("Scopes::Derived::a", inherited=True)) == "Scopes::Base::a"
        assert model.find("Scopes::Derived::c", inherited=True) is None  # private: not inherited
    except NotAnswered:
        pytest.skip("the backend does not answer inheritedMembership here")
    except UnresolvedReference:
        # A payload without its library: the inherited memberships include the library's.
        assert model._backend.__class__.__name__ == "PayloadBackend"
        pytest.skip("the payload model has no library to resolve its inherited memberships against")


def test_find_aliases_and_quoted_names(model):
    assert qn(model.find("Scopes::Alias")) == "Scopes::Base"
    assert model.find("Scopes::Alias::a") is not None
    assert qn(model.find("Scopes::'odd name'")) == "Scopes::'odd name'"
    odd = model.find("Scopes::'odd name'::'it\\'s'")
    assert odd is not None and odd.name == "it's"
    with pytest.raises(SdkError):
        model.find("Scopes::'odd name'::'it''s'")  # SysML has no doubled-quote escape


def test_no_name_the_model_does_not_give(model):
    # The satisfy usage in `v` has no name: neither lookup finds it as `v::spec`.
    assert model.find("Scopes::v::spec") is None
    assert model.resolve("Scopes::v::spec") is None
    assert qn(model.resolve("Scopes::Reqs::spec")) == "Scopes::Reqs::spec"


def test_resolve_from_a_context(model):
    if model._backend.__class__.__name__ == "PayloadBackend":
        with pytest.raises(NotAnswered):
            model.resolve("b", model.find("Scopes::Base"))
        return
    base = model.find("Scopes::Base")
    derived = model.find("Scopes::Derived")
    cases = [(base, "b", "Scopes::Base::b"), (base, "c", "Scopes::Base::c"), (base, "P", "Scopes::P"),
             (derived, "b", "Scopes::Base::b"), (derived, "c", None), (derived, "Base::b", None)]
    for ctx, name, want in cases:
        try:
            got = qn(model.resolve(name, ctx))
        except NotAnswered:
            if model._backend.__class__.__name__ == "ToolkitBackend":
                continue  # the SysML Toolkit refuses Namespace::resolve in places
            raise
        assert got == want, f"resolve({name!r}) from {qn(ctx)}"


def test_resolve_needs_a_namespace(model):
    with pytest.raises(TypeError):
        model.resolve("x", model.find("Scopes::Base::a::nothing") or model.find("Scopes::Base").ownedMembership[0])
