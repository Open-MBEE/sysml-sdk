"""Hand-written unit tests for the runtime (sysml/base.py).

Conformance of the generated surface against metamodel.json lives in
test_conformance_generated.py; this file covers runtime semantics the
table cannot express: identity, wrapping, read passthrough, and the
collision rule.
"""

import pytest

import sysml as f
from sysml.base import Element, P


PAYLOAD = [
    {"@id": "pkg", "@type": "Package", "qualifiedName": "P",
     "declaredName": "P", "ownedMember": [{"@id": "part"}]},
    {"@id": "part", "@type": "PartUsage", "qualifiedName": "P::x",
     "declaredName": "x", "owner": {"@id": "pkg"},
     "ownedRelationship": [{"@ref": "Base::things"}]},
    {"@id": "part2", "@type": "PartUsage", "qualifiedName": "P::y",
     "declaredName": "y", "owner": {"@id": "pkg"},
     "ownedRelationship": [{"@id": "nowhere"}]},
    {"@id": "mu", "@type": "MetadataUsage", "qualifiedName": "P::meta",
     "owner": {"@id": "pkg"}, "metaclass": None},
    {"@id": "mc", "@type": "Metaclass", "qualifiedName": "P::Safety",
     "owner": {"@id": "pkg"}},
    {"@id": "mu2", "@type": "MetadataUsage", "qualifiedName": "P::meta2",
     "owner": {"@id": "pkg"}, "metaclass": {"@id": "mc"}},
    {"@id": "alien", "@type": "NotARealMetaclass", "qualifiedName": "P::alien",
     "owner": {"@id": "pkg"}},
    # the SysML Toolkit serializes memberships ownerless (owner: null) — roots
    # must not report them (as in data/bigger.full.json; see Model.roots)
    {"@id": "om", "@type": "OwningMembership", "owner": None},
]


@pytest.fixture()
def model():
    return f.Model.from_full_json(PAYLOAD)


# --- identity ---------------------------------------------------------------

def test_wrappers_are_cached_and_equal_by_id(model):
    a = model.element("part")
    b = model.resolve("P::x")
    assert a is b
    assert a == b and hash(a) == hash(b)


def test_equality_is_model_scoped(model):
    other = f.Model.from_full_json(PAYLOAD)
    assert model.element("part") != other.element("part")


def test_repr_prefers_qualified_name(model):
    assert repr(model.element("part")) == "<PartUsage P::x>"


def test_gone_on_unknown_id(model):
    with pytest.raises(f.Gone):
        model.element("no-such-id")


# --- wrapping ---------------------------------------------------------------

def test_id_refs_wrap_to_typed_elements(model):
    part = model.element("part")
    assert type(part.owner).__name__ == "Package"
    assert part.owner == model.element("pkg")


def test_unresolved_ref_spelling_raises(model):
    # SysML Toolkit @ref passthrough: typed navigation refuses unresolved models
    with pytest.raises(f.UnresolvedReference) as ei:
        model.element("part").ownedRelationship
    assert "Base::things" in str(ei.value) and "PartUsage.ownedRelationship" in str(ei.value)


def test_dangling_id_ref_raises(model):
    with pytest.raises(f.UnresolvedReference) as ei:
        model.element("part2").ownedRelationship
    assert "nowhere" in str(ei.value)


def test_get_raw_still_exposes_unresolved(model):
    # forensics path: raw value untouched
    assert model.element("part").get_raw("ownedRelationship") == [
        {"@ref": "Base::things"}]


def test_unknown_metaclass_falls_back_to_base_element(model):
    alien = model.element("alien")
    assert type(alien) is Element
    assert alien.metaclass_name == "NotARealMetaclass"  # backend-routed


# --- read passthrough -------------------------------------------------------
# The SDK never second-guesses a backend result.
# An unimplemented derivation is reported by the SysML Toolkit, not inferred here.

def test_unimplemented_derivation_returns_the_backend_value(model):
    assert model.element("pkg").isLibraryElement is None


def test_absent_array_property_normalizes_to_empty(model):
    assert model.element("part").ownedMember == []


def test_derived_property_with_a_value_is_wrapped(model):
    assert model.element("mu2").metaclass == model.element("mc")


def test_get_raw_returns_the_unwrapped_value(model):
    assert model.element("pkg").get_raw("isLibraryElement") is None


def test_assignment_raises_readonly(model):
    with pytest.raises(f.ReadOnly):
        model.element("part").declaredName = "y"


def test_operation_stub_raises(model):
    with pytest.raises(f.NotImplementedInToolkit):
        model.element("pkg").visibleMemberships(None, False, False)


# --- collision rule ---------------------------------------------------------

def test_metadata_metaclass_is_the_spec_property(model):
    mu = model.element("mu")
    assert mu.metaclass_name == "MetadataUsage"  # runtime accessor
    assert mu.metaclass is None  # spec property: backend value (null here)
    assert isinstance(type(mu).metaclass, P)  # class access -> descriptor


def test_runtime_members_all_contain_underscore():
    """Runtime members carry an underscore so no spec name can shadow them. Spec
    operations of Element itself are attached to the runtime base by the generator
    and are descriptors, not runtime members, so they are excluded."""
    from sysml.base import Op, P

    public = [n for n in dir(Element) if not n.startswith("_")
              and not isinstance(getattr(Element, n), (Op, P))]
    assert public and all("_" in n for n in public)


# --- model queries ----------------------------------------------------------

def test_roots(model):
    assert model.roots == [model.element("pkg")]


def test_all_is_every_element(model):
    assert model.all() == model.elements_of_type(Element)
    assert model.element("pkg") in model.all()


def test_elements_of_type_exact_vs_subtype(model):
    from sysml.classes import Usage

    exact = model.elements_of_type("PartUsage", exact=True)
    assert [w.element_id for w in exact] == ["part", "part2"]
    assert {w.element_id for w in model.elements_of_type(Usage)} == {
        "part", "part2", "mu", "mu2"}


def test_resolve_miss_returns_none(model):
    assert model.resolve("No::Such::Name") is None


def test_payload_elements_come_in_document_order():
    """An id given twice keeps its first place and its last element, in every language."""
    m = f.Model.from_full_json([
        {"@id": "z", "@type": "Package", "declaredName": "first"},
        {"@id": "a", "@type": "Package", "declaredName": "A"},
        {"@id": "z", "@type": "Package", "declaredName": "second"},
    ])
    assert [e.element_id for e in m.elements_of_type(Element)] == ["z", "a"]
    assert m.element("z").declaredName == "second"


# --- a payload model with a library ------------------------------------------

LIBRARY = [
    {"@id": "lib", "@type": "LibraryPackage", "qualifiedName": "ScalarValues", "isLibraryElement": True},
    {"@id": "real", "@type": "DataType", "qualifiedName": "ScalarValues::Real", "declaredName": "Real",
     "owner": {"@id": "lib"}, "isLibraryElement": True},
    # an id the model also has: the model's element wins
    {"@id": "clash", "@type": "Package", "declaredName": "from the library"},
    # a qualified name the model also has: the model's element wins
    {"@id": "libP", "@type": "Package", "qualifiedName": "P"},
]
WITH_LIBRARY = [
    {"@id": "pkg", "@type": "Package", "qualifiedName": "P", "declaredName": "P"},
    {"@id": "mass", "@type": "AttributeUsage", "qualifiedName": "P::mass", "owner": {"@id": "pkg"},
     "type": [{"@id": "real"}], "definition": [{"@id": "gone"}]},
    {"@id": "clash", "@type": "Package", "declaredName": "from the model"},
]


def test_library_resolves_references_the_model_does_not_hold():
    m = f.Model.from_full_json(WITH_LIBRARY, library=f.PayloadLibrary(LIBRARY))
    real = m.element("mass").type[0]
    assert real.declaredName == "Real" and real.isLibraryElement is True
    assert real.owner.qualifiedName == "ScalarValues"
    assert m.resolve("ScalarValues::Real") == real


def test_library_elements_are_not_the_models():
    m = f.Model.from_full_json(WITH_LIBRARY, library=f.PayloadLibrary(LIBRARY))
    assert [e.element_id for e in m.elements_of_type(Element)] == ["pkg", "mass", "clash"]
    assert [e.element_id for e in m.roots] == ["pkg", "clash"]


def test_the_model_wins_over_the_library():
    m = f.Model.from_full_json(WITH_LIBRARY, library=f.PayloadLibrary(LIBRARY))
    assert m.element("clash").declaredName == "from the model"
    assert m.resolve("P").element_id == "pkg"


def test_a_reference_in_neither_still_raises():
    m = f.Model.from_full_json(WITH_LIBRARY, library=f.PayloadLibrary(LIBRARY))
    with pytest.raises(f.UnresolvedReference):
        m.element("mass").definition


def test_without_the_library_its_references_do_not_resolve():
    m = f.Model.from_full_json(WITH_LIBRARY)
    with pytest.raises(f.UnresolvedReference):
        m.element("mass").type
    assert m.resolve("ScalarValues::Real") is None
