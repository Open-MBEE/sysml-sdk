"""Regression test on the specification's own example: the simple vehicle model of SysML Annex A,
read through the SysML Toolkit with the standard library.

Needs the sysml-toolkit repository beside this one, with its `spec-refs/SysML-v2-Release` checkout,
and the binding library; skips when either is absent.

Two kinds of assertion:

- Invariants of the specification, which hold whatever the SysML Toolkit computes: a property
  redefined under another name answers what the redefining property answers, a non-owning
  Membership owns nothing, an owned element's owning relationship is never answered with a null it
  does not have, the operations the SDK answers agree with the derived properties the
  specification defines by them, and the operations it cannot answer as specified are refused.
- TOOLKIT_GAPS (toolkit_gaps.py): what the SysML Toolkit does not yet answer as the specification
  defines it, each row saying what the SysML Toolkit does not do yet. The totals of reads by
  outcome are in it too, so a change that starts answering where the SysML Toolkit had no answer
  is noticed.

The model is loaded under its file name, so element ids are the same on every machine.
"""

from __future__ import annotations

import json
from collections import Counter
from pathlib import Path

import pytest
from conftest import toolkit_library
from toolkit_gaps import TOOLKIT_GAPS, REFUSED

ROOT = Path(__file__).resolve().parents[2]
RELEASE = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release"
MODEL = RELEASE / "sysml" / "src" / "examples" / "Vehicle Example" / "SysML v2 Spec Annex A SimpleVehicleModel.sysml"
LIBRARY = RELEASE / "sysml.library"

pytestmark = [
    pytest.mark.skipif(toolkit_library() is None, reason="binding library not built (abi/)"),
    pytest.mark.skipif(not MODEL.exists(), reason="sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository"),
]

@pytest.fixture(scope="module")
def model():
    from sysml import Model

    return Model.from_toolkit(sources={MODEL.name: MODEL.read_text(encoding="utf-8")}, library_dir=str(LIBRARY))


@pytest.fixture(scope="module")
def elements(model):
    from sysml.base import Element

    return model.elements_of_type(Element)


@pytest.fixture(scope="module")
def by_id(elements):
    return {e.element_id[:8]: e for e in elements}


def _answer(element, member):
    from sysml import NotImplementedInToolkit, UnresolvedReference

    try:
        if member.endswith("()"):
            return getattr(element, member[:-2])()
        return getattr(element, member)
    except NotImplementedInToolkit:
        return REFUSED
    except UnresolvedReference:
        return "unresolved"


def _outcome(element, prop):
    v = _answer(element, prop)
    if v in (REFUSED, "unresolved"):
        return v
    return "empty" if v in (None, [], "") else "value"


# ---------------------------------------------------------------- invariants


def test_a_property_redefined_under_another_name_answers_through_the_redefining_one(elements):
    typings = [e for e in elements if type(e).__name__ == "FeatureTyping"]
    owning = [e for e in elements if hasattr(type(e), "ownedMemberElement")]
    assert typings and owning
    assert all(t.general == t.type for t in typings)
    for m in owning:
        assert m.memberName == m.ownedMemberName, m.element_id
        assert m.memberElement == m.ownedMemberElement, m.element_id


def test_a_non_owning_membership_owns_nothing_and_keeps_only_its_own_name(elements):
    plain = [e for e in elements if type(e).__name__ == "Membership"]
    assert len(plain) == 280
    assert all(m.ownedRelatedElement == [] for m in plain)
    # The one alias of the model; the others are the memberships through which expressions,
    # transitions and use cases refer to elements, which the model does not name.
    assert [m.memberName for m in plain if m.memberName is not None] == ["Torque"]


def test_an_owning_membership_owns_its_member(elements):
    for m in (e for e in elements if hasattr(type(e), "ownedMemberElement")):
        assert m.ownedRelatedElement == [m.ownedMemberElement], m.element_id


def test_owned_relationships_include_the_implied_ones(model):
    from sysml.classes import Subclassification

    vehicle = model.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle")
    implied = [r for r in vehicle.ownedRelationship if isinstance(r, Subclassification)]
    assert [r.general.qualifiedName for r in implied] == ["Parts::Part"]


def test_an_owned_element_never_answers_a_null_owning_relationship_it_does_not_have(elements):
    for e in elements:
        if e.owner is None:
            continue
        r = _answer(e, "owningRelationship")
        if r is None:  # only a relationship its owner lists directly has none
            assert hasattr(type(e), "source") and e in e.owner.ownedRelationship, e.element_id


def test_the_answered_operations_agree_with_the_derived_properties(elements):
    for e in elements:
        assert e.effectiveName() == e.name, e.element_id
        assert e.effectiveShortName() == e.shortName, e.element_id


def test_operations_the_toolkit_answers_differently_are_refused(model):
    from sysml import NotImplementedInToolkit

    vehicle = model.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle")
    for call in (lambda: vehicle.resolve("mass"), lambda: vehicle.resolveLocal("mass"),
                 lambda: vehicle.supertypes(False), lambda: vehicle.isCompatibleWith(vehicle),
                 lambda: vehicle.inheritedMemberships([vehicle], [], False)):
        with pytest.raises(NotImplementedInToolkit):
            call()


def test_the_export_is_the_same_model(model, elements):
    exported = {e["@id"] for e in json.loads(model._backend.full_json())}
    assert {e.element_id for e in elements} <= exported


# ---------------------------------------------------------------- gaps in the SysML Toolkit


@pytest.mark.parametrize("key", [k for k in TOOLKIT_GAPS if k[0] != "*"], ids=lambda k: f"{k[0]}.{k[1]}")
def test_toolkit_gap(by_id, key):
    prefix, member = key
    expected, reason = TOOLKIT_GAPS[key]
    got = _answer(by_id[prefix], member)
    if isinstance(expected, int) and isinstance(got, list):
        got = len(got)
    assert got == expected, f"{reason}: now {got!r}; if the SysML Toolkit changed, update TOOLKIT_GAPS"


def test_reads_by_outcome(elements):
    from sysml.base import P

    counts = Counter()
    for e in elements:
        for name in sorted({n for c in type(e).__mro__ for n, d in vars(c).items() if isinstance(d, P)}):
            counts[_outcome(e, name)] += 1
    expected, reason = TOOLKIT_GAPS[("*", "reads")]
    assert dict(counts) == expected, f"{reason}: now {dict(counts)}; if the SysML Toolkit changed, update TOOLKIT_GAPS"
