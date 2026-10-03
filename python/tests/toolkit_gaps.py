"""What the pinned SysML Toolkit release does not yet answer as the specification defines it.

This module is the only place a test records a gap in the SysML Toolkit; every row says what the
SysML Toolkit does not do yet. When the SysML Toolkit pin moves, rows that change are expected:
update them here (see abi/README.md, "Moving to a newer SysML Toolkit release"). Tests that meet a
gap not listed here fail, and so do tests whose listed gap no longer shows, so a change in the
SysML Toolkit is always noticed.
"""

REFUSED = "refused"  # the read raises NotImplementedInToolkit

# The specification's Annex A model with the standard library (test_annex_a.py).
# (element id prefix, member) -> (expected answer, why). A member is a
# property; a member ending in "()" is an operation called without arguments.
TOOLKIT_GAPS = {
    ("7ed4db60", "connectorEnd"): (REFUSED, "connector ends wait on the implied end redefinitions"),
    ("be8ea388", "isVariable"): (REFUSED, "redefined by mayTimeVary, whose variability evidence is incomplete"),
    # Reads of every property of every user element, by outcome.
    ("*", "reads"): ({"value": 134104, "empty": 182917, "refused": 46479, "unresolved": 83},
                     "the properties the SysML Toolkit's checked reader does not answer yet"),
}

# Tests of what the SDK should answer that meet a SysML Toolkit refusal first: test id (relative
# to python/tests; a trailing * matches every parametrization) -> (the refused member, why).
# Such a test is reported as an expected failure (xfail) when it stops with exactly that
# refusal. It fails when it passes (the SysML Toolkit answers now: remove the row), and when it
# stops on anything else (conftest.py applies this table).
TOOLKIT_REFUSALS = {
    "test_annex_a.py::test_the_answered_operations_agree_with_the_derived_properties":
        ("effectiveName()", "incomplete evidence for Feature::effectiveName"),
    "test_toolkit_backend.py::test_ownership_spine": ("type", "the type projection waits on implied relationships"),
    "test_toolkit_vs_payload.py::test_the_owned_side_export_leaves_the_closures_empty":
        ("inheritedFeature", "inherited features wait on implied relationships"),
    "test_toolkit_vs_payload.py::test_operations_the_toolkit_answers":
        ("inheritedMemberships()", "incomplete evidence for Type::inheritedMemberships"),
    "test_queries.py::test_reachable_states[toolkit-*": ("targetFeature", "connector ends"),
    "test_queries.py::test_shortest_trigger_sequences[toolkit]": ("targetFeature", "connector ends"),
    "test_queries.py::test_initial_states[toolkit]": ("targetFeature", "connector ends"),
    "test_queries.py::test_guards_in_three_valued_logic[toolkit]": ("argument", "invocation arguments"),
    "test_queries.py::test_bill_of_materials_and_rollups[toolkit-*": ("usage", "inherited features"),
    "test_queries.py::test_links[toolkit]": ("usage", "inherited features"),
    "test_queries.py::test_reachable_parts[toolkit-*": ("usage", "inherited features"),
    "test_queries.py::test_links_are_inherited[toolkit]": ("usage", "inherited features"),
    "test_queries.py::test_requirements[toolkit]": ("satisfyingFeature", "requirement parameters"),
    "test_queries.py::test_multiplicity_rules[toolkit]": ("feature", "inherited features"),
}

# The query reports through the SysML Toolkit (examples/queries/expected), compared by
# tools/compare_report.py: expected report -> {section heading: (the member the SysML Toolkit
# refuses in that report, why)}, or a list of such pairs where the report stops at a different read
# depending on how the model was loaded: with the standard library the SysML Toolkit answers more,
# so a report can stop later. A report the SysML Toolkit stops must equal the expected one up to
# its refusal; a listed report that completes fails.
TOOLKIT_REPORT_REFUSALS = {
    "report.txt": {
        "Reachable states: Pumps::PumpController::modes": ("targetFeature", "connector ends"),
        "Structure: bills of materials and roll-ups": ("usage", "inherited features"),
        "Connectivity: Drones::Quadcopter": [
            ("usage", "inherited features"),
            ("sourceFeature", "connector ends, where the usages are answered with the standard library"),
        ],
        "Requirements": ("satisfyingFeature", "requirement parameters"),
    },
    "annex-a.txt": {
        "Reachable states: SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates":
            ("payloadParameter", "accept action parameters"),
    },
}

# The small model of test_toolkit_vs_payload.py, read through the SysML Toolkit and through the
# SysML Toolkit's own closure-level export: the properties whose answers differ, and why.
_DEFAULT_AS_NULL = ("the export writes null for this [1] property at its default; the SysML Toolkit answers "
                    "the default")
EXPORT_DIFFERENCES = {
    "result": "the export writes the result parameter an expression inherits from the library (a literal's is "
              "LiteralIntegerEvaluation::result, for instance), where the SysML Toolkit refuses result",
    "ownedRelationship": "the export invents elements to carry the unresolved reference to Real",
    "ownedElement": "as ownedRelationship",
    "textualRepresentation": "as ownedRelationship",
    **{name: _DEFAULT_AS_NULL for name in (
        "isVariable", "mayTimeVary")},
}
