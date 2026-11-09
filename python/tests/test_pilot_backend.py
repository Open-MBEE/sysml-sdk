"""The Python SDK reading through the SysML v2 Pilot Implementation.

The protocol tests run everywhere, against a stand-in for the engine. The live tests run the Pilot
in a Java process and, when JPype is installed, in this process; they skip when no Pilot is at hand
(conftest.pilot_at_hand).
"""

from __future__ import annotations

import os

import pytest
from conftest import pilot_at_hand, pilot_server

from sysml import BackendError, Gone, Model, NotAnswered, SdkError, UnresolvedReference
from sysml.pilot import PilotBackend

# ---------------------------------------------------------------- the protocol, without a Pilot


class FakeTransport:
    """Answers requests from a table: (cmd, key) -> answer."""

    engine = "Pilot Implementation"
    version = "0.0.0"

    def __init__(self, answers):
        self.answers = answers
        self.requests = []
        self.closed = False

    def request(self, req):
        self.requests.append(req)
        key = req.get("prop") or req.get("op") or req.get("name") or req.get("h")
        return {"id": req["id"], **self.answers[(req["cmd"], key)]}

    def close(self):
        self.closed = True


def test_errors_map_to_the_sdks():
    t = FakeTransport({
        ("get", "noSuch"): {"error": {"kind": "notApplicable", "message": "PartUsage has no property noSuch"}},
        ("get", "boom"): {"error": {"kind": "failed", "message": "the Pilot raised NullPointerException"}},
        ("metaclass", "9"): {"error": {"kind": "gone", "message": "no element has the handle 9"}},
        ("call", "noOp"): {"error": {"kind": "notApplicable", "message": "PartUsage has no operation noOp"}},
        ("resolve", "x"): {"error": {"kind": "badRequest", "message": "no model is open"}},
    })
    b = PilotBackend(t)
    assert b.get("1", "noSuch") is None  # not a member of this metaclass: absent
    with pytest.raises(BackendError, match="NullPointerException"):
        b.get("1", "boom")
    with pytest.raises(Gone):
        b.metaclass("9")

    class Op:
        name = "noOp"

    with pytest.raises(BackendError, match="no operation noOp"):
        b.call("1", Op, ())
    with pytest.raises(SdkError, match="no model is open"):
        b.resolve("x")
    t.answers[("get", "refusedHere")] = {"error": {"kind": "refused", "message": "not answered"}}
    with pytest.raises(NotAnswered):
        b.get("1", "refusedHere")


def test_arguments_and_values_cross_as_the_protocol_says():
    t = FakeTransport({
        ("metaclass", "1"): {"ok": "PartDefinition"},
        ("metaclass", "2"): {"ok": "PartDefinition"},
        ("call", "isCompatibleWith"): {"ok": True},
        ("get", "ownedFeature"): {"ok": [{"@id": "2"}]},
        ("elementId", "1"): {"ok": None},
    })
    m = Model(PilotBackend(t))
    a, b = m.element("1"), m.element("2")
    assert a.isCompatibleWith(b) is True
    assert t.requests[-1]["args"] == [{"@id": "2"}]
    assert a.ownedFeature == [b]
    assert m._backend.element_id("1") == ""
    m._backend.close()
    assert t.closed
    with pytest.raises(SdkError, match="closed"):
        m._backend.metaclass("1")


def test_a_bad_transport_name_is_refused():
    with pytest.raises(SdkError, match="transport"):
        PilotBackend.open(sources={"a.sysml": "package A;"}, transport="carrier pigeon")


# ---------------------------------------------------------------- live, with the Pilot

SKIP = pilot_at_hand()
live = pytest.mark.skipif(SKIP is not None, reason=SKIP or "")

SOURCES = {
    "demo.sysml": "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } "
                  "part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }",
    "größe.sysml": "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } "
                   "part def Last; }",
    "bad.sysml": "package Bad { part p : Missing; }",
}


def _transports():
    out = ["process"]
    try:
        import jpype  # noqa: F401
        out.append("jpype")
    except ImportError:
        pass
    return out


@pytest.fixture(scope="module", params=_transports())
def model(request, tmp_path_factory):
    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    if request.param == "jpype":
        # The JVM handles access violations itself (safepoints, null checks); pytest's
        # faulthandler would report each one as fatal.
        import faulthandler

        faulthandler.disable()
    be = PilotBackend.open(sources=SOURCES, transport=request.param, heap="2g")
    yield Model(be)
    be.close()


@live
def test_reads_and_derived_properties(model):
    from sysml.classes import Package, PartDefinition, PartUsage

    car = model.resolve("Demo::Car")
    assert isinstance(car, PartDefinition) and car.qualifiedName == "Demo::Car"
    assert isinstance(car.owner, Package)
    wheels, mass = car.ownedFeature
    assert isinstance(wheels, PartUsage) and wheels.declaredName == "wheels" and wheels.isComposite
    assert [t.declaredName for t in wheels.type] == ["Wheel"]
    sports = model.resolve("Demo::Sports")
    assert "wheels" in [f.declaredName for f in sports.inheritedFeature]
    assert car is model.resolve("Demo::Car")
    assert len(car.element_id) == 36
    assert model.resolve("Demo::Nothing") is None
    assert model.resolve("ScalarValues::Real") is not None


@live
def test_operations(model):
    car, sports = model.resolve("Demo::Car"), model.resolve("Demo::Sports")
    assert car.effectiveName() == "Car"
    assert sports.specializes(car) is True
    assert sports.isCompatibleWith(car) is True
    assert "wheels" in [ms.memberName for ms in sports.inheritedMemberships([], [], False)]
    [connection] = [f for f in model.resolve("Demo::Link").ownedFeature if type(f).__name__ == "ConnectionUsage"]
    end = connection.connectorEnd[0]
    assert end.declaredName is None and end.effectiveName() == "source"


@live
def test_text_requirements_and_roots(model):
    assert "Größe und Wärme" in model.resolve("Sizes").documentation[0].body
    light = model.resolve("Sizes::Light")
    assert len(light.text) == 1 and light.text[0].startswith("Keep it light.")
    names = {m.name for r in model.roots for m in r.ownedMember}
    assert {"Demo", "Sizes", "Bad"} <= names


@live
def test_unresolved_references_and_errors(model):
    p = model.resolve("Bad::p")
    with pytest.raises(UnresolvedReference):
        p.ownedTyping[0].type
    backend = model._backend
    with pytest.raises(Gone):
        backend.metaclass("999999999")
    assert backend.get(p.backend_handle, "noSuchProperty") is None

    class Op:
        name = "noSuchOperation"

    with pytest.raises(BackendError, match="noSuchOperation"):
        backend.call(p.backend_handle, Op, ())
    assert backend.pilot_version and backend.stats["pilot"] == backend.pilot_version


@live
def test_without_elaboration(tmp_path_factory):
    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    with PilotBackend.open(sources={"demo.sysml": SOURCES["demo.sysml"]}, elaborate=False,
                           transport="process", heap="1g") as be:
        car = Model(be).resolve("Demo::Car")
        assert car.ownedSpecialization == []


@live
def test_a_missing_pilot_is_a_backend_error(tmp_path, tmp_path_factory):
    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    with pytest.raises(BackendError, match="holds no Pilot"):
        PilotBackend.open(sources={"a.sysml": "package A;"}, transport="process", pilot_home=tmp_path)


@live
def test_a_convenience_mode_needs_its_plugin(tmp_path_factory, monkeypatch):
    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    monkeypatch.delenv("SYSML_PILOT_PLUGIN", raising=False)
    monkeypatch.setenv("SYSML_PILOT_CONVENIENCE", "compare")
    with pytest.raises(BackendError, match="needs the plugin jar"):
        PilotBackend.open(sources={"a.sysml": "package A;"}, transport="process")
    monkeypatch.setenv("SYSML_PILOT_CONVENIENCE", "fastest")
    with pytest.raises(BackendError, match="reference, shortcut or compare"):
        PilotBackend.open(sources={"a.sysml": "package A;"}, transport="process")
