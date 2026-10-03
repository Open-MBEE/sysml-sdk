"""Parametric queries over two example models, and optionally over the vehicle model of SysML
Annex A, printed as a report. The program in every language prints the same report, which is kept
in examples/queries/expected/.

Run from the repository root:

    python examples/queries/python/demo.py                  reads the models through the SysML Toolkit
    python examples/queries/python/demo.py --payload        reads their JSON exports instead, with
                                                            the standard library as JSON
    python examples/queries/python/demo.py --library DIR    loads the standard library with them
    python examples/queries/python/demo.py --annex-a FILE --library DIR
                                                            reports on the Annex A vehicle instead

The JSON exports and the library JSON are generated, not committed: python tools/export_example.py.

The queries themselves are in queries.py; this file loads the models and asks the questions.
"""

from __future__ import annotations

import argparse
import functools
import json
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
MODELS = HERE.parent / "models"
LIBRARY_JSON = HERE.parents[1] / "sysml.library.full.json"
sys.path.insert(0, str(HERE.parents[2] / "python"))  # not needed once the SDK is installed

from sysml import Model, NotImplementedInToolkit, PayloadLibrary  # noqa: E402

import queries as q  # noqa: E402


@functools.cache
def payload_library() -> PayloadLibrary:
    """The standard library as JSON, read once and shared by every payload model."""
    return PayloadLibrary(json.loads(LIBRARY_JSON.read_text(encoding="utf-8")))


def load(name: str, *, payload: bool, library: str | None) -> Model:
    """A model from examples/queries/models: `name`.sysml through the SysML Toolkit, or
    `name`.full.json with the library JSON."""
    if payload:
        return Model.from_full_json(json.loads((MODELS / f"{name}.full.json").read_text(encoding="utf-8")),
                                    library=payload_library())
    return Model.from_toolkit(sources={f"{name}.sysml": (MODELS / f"{name}.sysml").read_text(encoding="utf-8")},
                             library_dir=library)


def heading(title: str) -> None:
    print(f"== {title} ==")


def section(report, *args) -> None:
    """Run one report. Where the SysML Toolkit cannot vouch for a value yet it refuses the read
    (NotImplementedInToolkit); the report stops there, says so, and the next one runs."""
    try:
        report(*args)
    except NotImplementedInToolkit as e:
        print(f"  report stopped: {e}")


def describe(triggers, bindings) -> str:
    """A scenario in words: the triggers that can occur and the values bound."""
    parts = []
    if triggers is not None:
        parts.append("only " + ", ".join(triggers))
    if bindings:
        parts.append(", ".join(f"{name} = {q.fmt(value)}" for name, value in bindings))
    return "; ".join(parts) if parts else "nothing bound, every trigger"


# ---------------------------------------------------------------- the reports

def report_states(machine, scenarios, paths_for) -> None:
    """Reachable states of `machine` under each scenario, then the shortest trigger sequences
    under the scenario `paths_for`. A scenario is (title, triggers or None, bindings)."""
    heading(f"Reachable states: {machine.qualifiedName}")
    states = q.states_of(machine)
    transitions = q.transitions_of(machine)
    print(f"{len(states)} states, {len(transitions)} transitions:")
    for state in [machine, *states]:
        for initial in q.initial_states(state):
            print(f"  {q.describe_initial(state, initial, machine)}")
    for transition in transitions:
        print(f"  {q.describe_transition(transition, machine)}")
    for title, triggers, bindings in scenarios:
        reached = q.explore(machine, triggers=triggers, bindings=dict(bindings))
        print(f"{title}: {describe(triggers, bindings)}")
        reachable = [q.path_below(s, machine) for s in states if s in reached]
        unreachable = [q.path_below(s, machine) for s in states if s not in reached]
        print(f"  reachable ({len(reachable)}): {', '.join(reachable)}")
        print(f"  unreachable ({len(unreachable)}): {', '.join(unreachable) or '-'}")
    triggers, bindings = paths_for
    reached = q.explore(machine, triggers=triggers, bindings=dict(bindings))
    print(f"shortest trigger sequences ({describe(triggers, bindings)}):")
    width = max(len(q.path_below(s, machine)) for s in states)
    for state in states:
        path = reached.get(state)
        text = "unreachable" if path is None else ", ".join(path) or "(no trigger)"
        print(f"  {q.path_below(state, machine).ljust(width)}  {text}")


def report_structure(roots, attributes) -> None:
    heading("Structure: bills of materials and roll-ups")
    for root in roots:
        print(root.qualifiedName)
        bom = q.bill_of_materials(root)
        print("  parts: " + ", ".join(f"{name} {count}" for name, count in bom.items()))
        print("  " + ", ".join(f"{q.name_of(a)} {q.fmt(q.rollup(root, a))}" for a in attributes))


def report_connectivity(root, questions) -> None:
    heading(f"Connectivity: {root.qualifiedName}")
    for link in q.links(root):
        print(f"  {link.describe()}")
    for source, item in questions:
        via = f", through {item} flows only" if item else ""
        reached = q.reachable_parts(root, source, item=item)
        print(f"from {source}{via}: {', '.join(reached) or '-'}")


def report_requirements(model, derived) -> None:
    heading("Requirements")
    for v in q.check_satisfactions(model, derived):
        definition = v.requirement.requirementDefinition
        kind = f" : {q.name_of(definition)}" if definition is not None else ""
        verb = "not satisfied" if v.negated else "satisfied"
        expr = q.result_expression(v.constraint)
        values = f" {expr.operator} ".join(q.fmt(o) for o in v.operands)
        print(f"{q.name_of(v.requirement)}{kind}, {verb} by {q.name_of(v.satisfied_by)}")
        print(f"  {q.name_of(v.constraint)}: {q.render(expr)}  [{values}]  {v.outcome}")


# ---------------------------------------------------------------- the questions

TEMPERATE = [("selfTestPassed", True), ("temperature", 25.0)]

PUMP_SCENARIOS = [
    ("A", None, []),
    ("B", None, TEMPERATE),
    ("C", None, [("selfTestPassed", False)]),
    ("D", None, [("selfTestPassed", True), ("temperature", 95.0)]),
    ("E", None, TEMPERATE + [("maxTemperature", 20.0)]),
    ("F", ["PowerOn", "SelfTestDone", "Start"], TEMPERATE),
]


def examples(payload: bool, library: str | None) -> None:
    pumps = load("pumps", payload=payload, library=library)
    section(report_states, pumps.resolve("Pumps::PumpController::modes"), PUMP_SCENARIOS, (None, TEMPERATE))
    print()

    drones = load("drones", payload=payload, library=library)
    mass, cost = drones.resolve("Drones::Component::mass"), drones.resolve("Drones::Component::cost")
    section(report_structure, [drones.resolve(f"Drones::{d}") for d in ("Quadcopter", "Hexacopter", "CargoHexacopter")],
            [mass, cost])
    print()
    section(report_connectivity, drones.resolve("Drones::Quadcopter"),
            [("battery", None), ("battery", "Power"), ("controller", None), ("controller", "Command"),
             ("rotors.propeller", None)])
    print()
    section(report_requirements, drones, {"totalMass": lambda part: q.rollup(part, mass),
                                          "totalCost": lambda part: q.rollup(part, cost)})


def annex_a_states(machine) -> None:
    keys = []
    for transition in q.transitions_of(machine):
        key, _ = q.trigger_of(transition)
        if key is not None and key != "at" and key not in keys:
            keys.append(key)
    report_states(machine, [("A", None, []), ("B", None, [("brakePedalDepressed", False)]), ("C", keys, [])],
                  (None, []))


def annex_a(path: str, library: str | None) -> None:
    text = Path(path).read_text(encoding="utf-8")
    vehicle = Model.from_toolkit(sources={Path(path).name: text}, library_dir=library)
    section(annex_a_states, vehicle.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates"))


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    parser.add_argument("--payload", action="store_true", help="read the models' JSON exports")
    parser.add_argument("--library", help="the standard library's directory, loaded with the models")
    parser.add_argument("--annex-a", help="the Annex A vehicle model: report on its states instead")
    args = parser.parse_args()
    if args.annex_a:
        annex_a(args.annex_a, args.library)
    else:
        examples(args.payload, args.library)


if __name__ == "__main__":
    main()
