"""The convenience members' answers on each backend, against their examples (convenience.json).

Every example is run (python/tests/convenience_check.py). The answers a backend gives must be the
examples'; what it refuses is counted, and the count is pinned per backend, so a backend that starts
answering more, or less, is noticed. Skips without the backend (the binding library and the release
library beside this repository for the SysML Toolkit; a Java runtime and a Pilot for the Pilot
Implementation, conftest.pilot_at_hand).
"""

from __future__ import annotations

import os
from collections import Counter

import pytest
from conftest import pilot_at_hand, pilot_server, toolkit_library
from convenience_check import ROOT, SOURCES, run

from sysml import Model

RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"

# Examples the SysML Toolkit refuses (v0.12.0): `type` on action, state, calculation, case,
# allocation and flow usages and on enumeration values (15), a metadata usage's `metaclass` (4), the
# member lists of a definition with a public import (6), and an operator expression's `ownedFeature`
# (1).
TOOLKIT_REFUSED = 26

# Examples the Pilot Implementation backend refuses: none.
PILOT_REFUSED = 0

# Examples where a backend's answer contradicts the specification, as the examples table lists them
# (`deviates`): the Pilot Implementation's supertypes() keeps implied specializations and gives a
# conjugated type none.
PILOT_DEVIATES = 4
TOOLKIT_DEVIATES = 0


@pytest.fixture(scope="module")
def toolkit_outcomes():
    if toolkit_library() is None:
        pytest.skip("binding library not built (abi/)")
    if not RELEASE_LIBRARY.is_dir():
        pytest.skip("sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository")
    return run(Model.from_toolkit(sources=SOURCES, library_dir=str(RELEASE_LIBRARY)), "toolkit")


@pytest.fixture(scope="module")
def pilot_outcomes(tmp_path_factory):
    reason = pilot_at_hand()
    if reason is not None:
        pytest.skip(reason)
    from sysml.pilot import PilotBackend

    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    backend = PilotBackend.open(sources=SOURCES, transport="process", heap="2g")
    try:
        yield run(Model(backend), "pilot")
    finally:
        backend.close()


def report(outcomes) -> str:
    lines = [f"{e['member']} at {e['at']} {e.get('args', '')}: {detail}" for e, detail in outcomes]
    return "\n".join(lines)


def test_toolkit_answers_agree_with_the_examples(toolkit_outcomes):
    assert not toolkit_outcomes["mismatch"], report(toolkit_outcomes["mismatch"])


def test_toolkit_refusals_are_the_known_ones(toolkit_outcomes):
    refused = toolkit_outcomes["refused by the backend"]
    reasons = Counter(detail.split(":")[0] if ":" in detail else detail for _, detail in refused)
    assert len(refused) == TOOLKIT_REFUSED, (
        f"{len(refused)} examples refused by the SysML Toolkit, {TOOLKIT_REFUSED} pinned; by member: {dict(reasons)}")


def test_pilot_answers_agree_with_the_examples(pilot_outcomes):
    assert not pilot_outcomes["mismatch"], report(pilot_outcomes["mismatch"])


def test_toolkit_deviations_are_the_listed_ones(toolkit_outcomes):
    deviates = toolkit_outcomes["known deviation"]
    assert len(deviates) == TOOLKIT_DEVIATES, report(deviates)


def test_pilot_deviations_are_the_listed_ones(pilot_outcomes):
    deviates = pilot_outcomes["known deviation"]
    assert len(deviates) == PILOT_DEVIATES, report(deviates)


def test_pilot_refusals_are_the_known_ones(pilot_outcomes):
    refused = pilot_outcomes["refused by the backend"]
    assert len(refused) == PILOT_REFUSED, (
        f"{len(refused)} examples refused by the Pilot Implementation, {PILOT_REFUSED} pinned:\n{report(refused)}")
