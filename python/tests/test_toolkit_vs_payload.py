"""Differential test: the same model through the SysML Toolkit backend and through the
SysML Toolkit's own closure-level full-form JSON export read by the payload backend. Every property
of every element must agree, except where the export writes something the SysML Toolkit does not
hold (EXPORT_DIFFERENCES in toolkit_gaps.py, each with its reason).

Disagreements are findings in the SysML Toolkit, never the model's fault: either the
SysML Toolkit's answer or its emitter is wrong, and the failure message names the member so it can
be reported.

Skips when the binding library is not built.
"""

from __future__ import annotations

import json

import pytest
from conftest import toolkit_library
from toolkit_gaps import EXPORT_DIFFERENCES

pytestmark = pytest.mark.skipif(toolkit_library() is None, reason="binding library not built (abi/)")

SOURCE = """
package Vehicles {
    part def Wheel { attribute radius : Real; }
    part def Engine;
    part def Car {
        part wheels : Wheel[4];
        part engine : Engine;
        attribute mass : Real;
        port fuelPort;
    }
    part def SportsCar :> Car { part spoiler; }
    part myCar : SportsCar;
}
"""


@pytest.fixture(scope="module")
def pair():
    from sysml.base import Model
    from sysml.toolkit import ToolkitBackend

    be = ToolkitBackend.open(sources={"vehicles.sysml": SOURCE})
    toolkit = Model(be)
    payload = Model.from_full_json(json.loads(be.full_json()))
    yield toolkit, payload
    be.close()


def _norm(v):
    if isinstance(v, list):
        return sorted(_norm(x) for x in v)
    if hasattr(v, "element_id"):
        return v.element_id
    return v


def _read(element, prop):
    from sysml import NotImplementedInToolkit, UnresolvedReference

    try:
        return "value", _norm(getattr(element, prop))
    except NotImplementedInToolkit:
        return "refused", None
    except UnresolvedReference:
        return "unresolved", None


def test_every_property_agrees_with_the_closure_level_export(pair):
    from sysml.base import Element, P

    toolkit, payload = pair
    by_id = {e.element_id: e for e in payload.elements_of_type(Element)}
    disagreements, compared, observed, answered = [], 0, set(), set()
    for k in toolkit.elements_of_type(Element):
        p = by_id.get(k.element_id)
        assert p is not None, f"SysML Toolkit element {k.element_id} ({type(k).__name__}) missing from its own JSON export"
        assert type(p).__name__ == type(k).__name__, f"metaclass differs for {k.element_id}"
        for m in sorted({n for c in type(k).__mro__ for n, d in vars(c).items() if isinstance(d, P)}):
            kv = _read(k, m)
            if kv[0] == "refused":
                continue  # the SysML Toolkit says it has no answer; the export's fill-in is listed in EXPORT_DIFFERENCES
            pv = _read(p, m)
            compared += 1
            answered.add(m)
            if kv != pv:
                if m in EXPORT_DIFFERENCES:
                    observed.add(m)
                    continue
                disagreements.append(
                    f"{type(k).__name__}({k.qualifiedName or k.element_id}).{m}: toolkit={kv!r} payload={pv!r}"
                )
    assert compared > 1500, "the model is too small to mean anything"
    assert not disagreements, "NEW findings in the SysML Toolkit, report them:\n  " + "\n  ".join(disagreements)
    # A known difference is gone only where the SysML Toolkit answered the property and agreed;
    # where it refuses the property everywhere, there is nothing to compare.
    healed = (set(EXPORT_DIFFERENCES) & answered) - observed
    assert not healed, (
        "these known differences no longer show; the SysML Toolkit may have fixed them, "
        f"remove them from EXPORT_DIFFERENCES: {sorted(healed)}"
    )


def test_the_owned_side_export_leaves_the_closures_empty(pair):
    from sysml.base import Model

    toolkit, _ = pair
    owned = Model.from_full_json(json.loads(toolkit._backend.full_json(closures=False)))
    sports = owned.resolve("Vehicles::SportsCar")
    assert sports.inheritedFeature == []
    assert [f.declaredName for f in toolkit.resolve("Vehicles::SportsCar").inheritedFeature] == [
        "wheels", "engine", "mass", "fuelPort"]


def test_operations_the_toolkit_answers(pair):
    from sysml.base import NotImplementedInToolkit

    toolkit, _ = pair
    car = toolkit.resolve("Vehicles::Car")
    sports = toolkit.resolve("Vehicles::SportsCar")
    # The specification defines the derived names as these operations.
    assert car.effectiveName() == car.name == "Car"
    assert car.effectiveShortName() is None
    # Inherited memberships are Membership relationships; memberName on an OwningMembership is
    # its ownedMemberName, which it redefines.
    inherited = sports.inheritedMemberships([], [], False)
    assert all(m.metaclass_name.endswith("Membership") for m in inherited)
    names = sorted(m.memberName for m in inherited)
    assert "wheels" in names and "engine" in names
    # The SysML Toolkit does not take the excluded lists: a non-empty one is refused, never
    # ignored.
    with pytest.raises(NotImplementedInToolkit):
        sports.inheritedMemberships([car], [], False)


def test_operations_the_toolkit_answers_differently_are_refused(pair):
    from sysml.base import NotImplementedInToolkit

    toolkit, _ = pair
    car = toolkit.resolve("Vehicles::Car")
    sports = toolkit.resolve("Vehicles::SportsCar")
    for name, call in [
        ("isCompatibleWith", lambda: sports.isCompatibleWith(car)),
        ("supertypes", lambda: sports.supertypes(False)),
        ("resolve", lambda: car.resolve("wheels")),
        ("resolveLocal", lambda: car.resolveLocal("wheels")),
    ]:
        with pytest.raises(NotImplementedInToolkit, match=name):
            call()


def test_operations_refuse_on_payload(pair):
    from sysml.base import NotImplementedInToolkit

    _, payload = pair
    car = payload.resolve("Vehicles::Car")
    with pytest.raises(NotImplementedInToolkit):
        car.effectiveName()
