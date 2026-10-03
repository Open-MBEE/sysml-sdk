"""The query examples in examples/queries: what each query answers on the two example models, as
worked out by hand from the models, and the report that every language's demo prints.

Every test runs on the models' JSON exports with the library JSON when they have been generated
(python tools/export_example.py), and through the SysML Toolkit when the binding library is
built. The Annex A report needs the sysml-toolkit checkout beside this repository, as test_annex_a
does.
"""

from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

import pytest
from conftest import example_json, toolkit_library, payload_library

ROOT = Path(__file__).resolve().parents[2]
EXAMPLE = ROOT / "examples" / "queries"
sys.path.insert(0, str(EXAMPLE / "python"))

import queries as q  # noqa: E402

RELEASE = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release"
ANNEX_A = RELEASE / "sysml" / "src" / "examples" / "Vehicle Example" / "SysML v2 Spec Annex A SimpleVehicleModel.sysml"

needs_toolkit = pytest.mark.skipif(toolkit_library() is None, reason="binding library not built (abi/)")
needs_json = pytest.mark.skipif(not example_json(), reason="example JSON not generated (tools/export_example.py)")


@pytest.fixture(scope="module", params=[pytest.param("payload", marks=needs_json),
                                        pytest.param("toolkit", marks=needs_toolkit)])
def backend(request):
    return request.param


def load(name: str, backend: str):
    from sysml import Model

    if backend == "payload":
        return Model.from_full_json(json.loads((EXAMPLE / "models" / f"{name}.full.json").read_text(encoding="utf-8")),
                                    library=payload_library())
    return Model.from_toolkit(sources={f"{name}.sysml": (EXAMPLE / "models" / f"{name}.sysml").read_text(encoding="utf-8")})


@pytest.fixture(scope="module")
def pumps(backend):
    return load("pumps", backend)


@pytest.fixture(scope="module")
def drones(backend):
    return load("drones", backend)


# ---------------------------------------------------------------- state machines

ALL_STATES = ["off", "starting", "operating", "operating::idle", "operating::pumping", "operating::pumping::low",
              "operating::pumping::high", "fault", "calibration"]
OPERATING = ["operating", "operating::idle", "operating::pumping", "operating::pumping::low", "operating::pumping::high"]


@pytest.mark.parametrize("triggers, bindings, unreachable", [
    (None, {}, ["calibration"]),  # every guard unknown, so every guarded transition may be taken
    (None, {"selfTestPassed": True, "temperature": 25.0}, ["calibration"]),
    (None, {"selfTestPassed": False}, OPERATING + ["calibration"]),
    (None, {"selfTestPassed": True, "temperature": 95.0}, OPERATING + ["fault", "calibration"]),
    # A binding overrides the model's value: maxTemperature is 80 in the model.
    (None, {"selfTestPassed": True, "temperature": 25.0, "maxTemperature": 20.0}, OPERATING + ["fault", "calibration"]),
    ({"PowerOn", "SelfTestDone", "Start"}, {"selfTestPassed": True, "temperature": 25.0},
     ["operating::pumping::high", "fault", "calibration"]),
])
def test_reachable_states(pumps, triggers, bindings, unreachable):
    machine = pumps.resolve("Pumps::PumpController::modes")
    assert [q.path_below(s, machine) for s in q.states_of(machine)] == ALL_STATES
    reached = {q.path_below(s, machine) for s in q.explore(machine, triggers=triggers, bindings=bindings)}
    assert reached == set(ALL_STATES) - set(unreachable)


def test_shortest_trigger_sequences(pumps):
    machine = pumps.resolve("Pumps::PumpController::modes")
    paths = {q.path_below(s, machine): p
             for s, p in q.explore(machine, bindings={"selfTestPassed": True, "temperature": 25.0}).items()}
    assert paths["off"] == []  # entered through the initial transition
    assert paths["operating::idle"] == ["PowerOn", "SelfTestDone"]  # entered with operating
    assert paths["operating::pumping::high"] == ["PowerOn", "SelfTestDone", "Start", "Boost"]
    # A transition from the composite state `operating` is taken from any of its substates.
    assert paths["fault"] == ["PowerOn", "SelfTestDone", "Overheat"]
    assert "calibration" not in paths


def test_initial_states(pumps):
    machine = pumps.resolve("Pumps::PumpController::modes")
    operating = pumps.resolve("Pumps::PumpController::modes::operating")
    assert [q.name_of(s) for s in q.initial_states(machine)] == ["off"]  # first start then off;
    assert [q.name_of(s) for s in q.initial_states(operating)] == ["idle"]
    assert q.initial_states(pumps.resolve("Pumps::PumpController::modes::off")) == []


def test_guards_in_three_valued_logic(pumps):
    guard = pumps.resolve("Pumps::PumpController::modes::starting_to_operating").guardExpression[0]
    assert q.render(guard) == "selfTestPassed and (temperature < maxTemperature)"
    assert q.evaluate(guard, q.Scope()) is q.UNKNOWN
    assert q.evaluate(guard, q.Scope(bindings={"selfTestPassed": False})) is False  # decided by the first operand
    assert q.evaluate(guard, q.Scope(bindings={"selfTestPassed": True})) is q.UNKNOWN
    assert q.evaluate(guard, q.Scope(bindings={"selfTestPassed": True, "temperature": 79.0})) is True


# ---------------------------------------------------------------- structure

@pytest.mark.parametrize("root, bom, mass, cost", [
    ("Quadcopter", {"Battery": 1, "Cell": 6, "FlightController": 1, "Frame": 1, "Motor": 4, "Propeller": 4, "Rotor": 4},
     0.01 + 0.12 + (0.02 + 6 * 0.045) + 0.015 + 4 * (0.002 + 0.03 + 0.008), 8 + 20 + (5 + 6 * 4) + 30 + 4 * (0.5 + 12 + 1.5)),
    # Each drone fixes rotorCount, and the rotors' multiplicity is evaluated in its context.
    ("Hexacopter", {"Battery": 1, "Cell": 6, "FlightController": 1, "Frame": 1, "Motor": 6, "Propeller": 6, "Rotor": 6},
     0.01 + 0.12 + (0.02 + 6 * 0.045) + 0.015 + 6 * (0.002 + 0.03 + 0.008), 8 + 20 + (5 + 6 * 4) + 30 + 6 * (0.5 + 12 + 1.5)),
    # Its battery is redefined with eight cells; a payload bay with its own values is added.
    ("CargoHexacopter",
     {"Battery": 1, "Cell": 8, "FlightController": 1, "Frame": 1, "Motor": 6, "PayloadBay": 1, "Propeller": 6, "Rotor": 6},
     0.01 + 0.12 + (0.02 + 8 * 0.045) + 0.015 + 6 * (0.002 + 0.03 + 0.008) + 0.05,
     8 + 20 + (5 + 8 * 4) + 30 + 6 * (0.5 + 12 + 1.5) + 6),
])
def test_bill_of_materials_and_rollups(drones, root, bom, mass, cost):
    element = drones.resolve(f"Drones::{root}")
    assert q.bill_of_materials(element) == bom
    assert q.rollup(element, drones.resolve("Drones::Component::mass")) == pytest.approx(mass)
    assert q.rollup(element, drones.resolve("Drones::Component::cost")) == pytest.approx(cost)


# ---------------------------------------------------------------- connectivity

def test_links(drones):
    assert [link.describe() for link in q.links(drones.resolve("Drones::Quadcopter"))] == [
        "battery -- controller  (connection supply)",
        "battery -> rotors.motor  (flow powerFeed of Power)",
        "controller -> rotors.motor  (flow commands of Command)",
        "rotors.motor -- rotors.propeller  (connection drive)",
    ]


@pytest.mark.parametrize("source, item, reached", [
    ("battery", None, ["controller", "rotors.motor", "rotors.propeller"]),
    ("battery", "Power", ["rotors.motor"]),
    ("controller", None, ["battery", "rotors.motor", "rotors.propeller"]),  # a connection goes both ways
    ("rotors.propeller", None, ["rotors.motor"]),  # a flow goes one way
])
def test_reachable_parts(drones, source, item, reached):
    assert q.reachable_parts(drones.resolve("Drones::Quadcopter"), source, item=item) == reached


def test_links_are_inherited(drones):
    assert len(q.links(drones.resolve("Drones::CargoHexacopter"))) == 4


# ---------------------------------------------------------------- requirements

def test_requirements(drones):
    mass, cost = drones.resolve("Drones::Component::mass"), drones.resolve("Drones::Component::cost")
    verdicts = q.check_satisfactions(drones, {"totalMass": lambda p: q.rollup(p, mass),
                                              "totalCost": lambda p: q.rollup(p, cost)})
    assert [(q.name_of(v.requirement), q.name_of(v.satisfied_by), q.name_of(v.constraint), v.outcome)
            for v in verdicts] == [
        ("scoutMass", "scout", "massBound", "holds"),
        ("haulerMass", "hauler", "massBound", "holds"),
        ("haulerCost", "hauler", "costBound", "VIOLATED"),
    ]
    assert verdicts[0].operands == [pytest.approx(0.595), 0.6]
    assert verdicts[2].operands == [pytest.approx(185.0), 150.0]


# ---------------------------------------------------------------- the report

def run_demo(*args: str) -> str:
    out = subprocess.run([sys.executable, str(EXAMPLE / "python" / "demo.py"), *args],
                         capture_output=True, text=True, encoding="utf-8", check=True).stdout
    return out.replace("\r\n", "\n")


def expected(name: str) -> str:
    return (EXAMPLE / "expected" / name).read_text(encoding="utf-8")


def check_toolkit_report(expected_name: str, *args: str) -> None:
    """The demo's report through the SysML Toolkit, compared by tools/compare_report.py: every
    report equals the expected one, or stops at the SysML Toolkit refusal TOOLKIT_REPORT_REFUSALS
    (toolkit_gaps.py) lists for it, having printed what the expected report has up to there.
    Reports stopped that way make the test an expected failure."""
    sys.path.insert(0, str(Path(__file__).resolve().parents[2] / "tools"))
    from compare_report import check

    problems, stopped = check(run_demo(*args), EXAMPLE / "expected" / expected_name)
    assert not problems, "\n".join(problems)
    if stopped:
        pytest.xfail("; ".join(stopped))


@needs_json
def test_report_from_payload():
    assert run_demo("--payload") == expected("report.txt")


@needs_toolkit
def test_report_from_toolkit():
    check_toolkit_report("report.txt")


@needs_toolkit
@pytest.mark.skipif(not ANNEX_A.exists(), reason="sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository")
def test_report_on_annex_a():
    check_toolkit_report("annex-a.txt", "--annex-a", str(ANNEX_A), "--library", str(RELEASE / "sysml.library"))


# ---------------------------------------------------------------- multiplicities

def test_multiplicity_rules(drones):
    rotors = drones.resolve("Drones::Multicopter::rotors")
    assert q.multiplicity_of(rotors) is rotors.multiplicity  # declared: [rotorCount]
    assert q.multiplicity_of(drones.resolve("Drones::Multicopter::airframe")) is q.DEFAULT_ONE
    battery = next(f for f in drones.resolve("Drones::Quadcopter").ownedFeature if q.name_of(f) == "battery")
    # A redefinition without a multiplicity takes that of the usage it redefines: here the default.
    assert q.explicit_subsettings(battery) == [drones.resolve("Drones::Multicopter::battery")]
    assert q.multiplicity_of(battery) is q.DEFAULT_ONE
    assert q.instance_count(rotors, drones.resolve("Drones::Hexacopter")) == 6
    with pytest.raises(q.QueryError):  # the general multicopter leaves the number of rotors open
        q.instance_count(rotors, drones.resolve("Drones::Multicopter"))
