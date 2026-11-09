"""EngineBackend: any engine that speaks the engine protocol (docs/engine-protocol.md), here a small
engine written for the test, run as a process."""

from __future__ import annotations

import sys
import textwrap

import pytest

from sysml import BackendError, Gone, Model, NotAnswered, SdkError, UnresolvedReference
from sysml.engine import EngineBackend

# A one-model engine: a package P owning a part definition A. Answers from a table; `A.ownedRelationship`
# refers to an element it cannot resolve, `A.isAbstract` is refused, and `effectiveName()` is answered.
ENGINE = textwrap.dedent('''
    import json, sys
    ELEMENTS = {"1": ("Package", "P", None), "2": ("PartDefinition", "A", "P::A")}
    print(json.dumps({"ready": True, "engine": "Test engine", "version": "1.0", "protocol": 1}), flush=True)
    for line in sys.stdin:
        req = json.loads(line)
        cmd, rid = req["cmd"], req["id"]
        def ok(v):
            print(json.dumps({"id": rid, "ok": v}), flush=True)
        def err(kind, msg):
            print(json.dumps({"id": rid, "error": {"kind": kind, "message": msg}}), flush=True)
        h = req.get("h")
        if cmd == "open":
            ok({"sources": sorted(req["sources"]), "elaborate": req["elaborate"]})
        elif cmd == "close":
            ok(None)
            break
        elif cmd == "allHandles":
            ok(["1", "2"])
        elif cmd == "resolve":
            ok({"P": "1", "P::A": "2"}.get(req["name"]))
        elif h not in ELEMENTS:
            err("gone", "no element " + str(h))
        elif cmd == "metaclass":
            ok(ELEMENTS[h][0])
        elif cmd == "elementId":
            ok("id-" + h)
        elif cmd == "get":
            prop = req["prop"]
            if prop == "declaredName":
                ok(ELEMENTS[h][1])
            elif prop == "qualifiedName":
                ok(ELEMENTS[h][2] or ELEMENTS[h][1])
            elif prop == "owner":
                ok({"@id": "1"} if h == "2" else None)
            elif prop == "ownedMember":
                ok([{"@id": "2"}] if h == "1" else [])
            elif prop == "isAbstract":
                err("refused", "isAbstract is not answered")
            elif prop == "ownedRelationship":
                err("unresolved", "a reference did not resolve")
            elif prop == "boom":
                err("failed", "the engine broke")
            else:
                err("notApplicable", ELEMENTS[h][0] + " has no property " + prop)
        elif cmd == "call":
            if req["op"] == "effectiveName":
                ok(ELEMENTS[h][1])
            else:
                err("notApplicable", "no operation " + req["op"])
        else:
            err("badRequest", "unknown command " + cmd)
''')


@pytest.fixture(scope="module")
def engine(tmp_path_factory):
    script = tmp_path_factory.mktemp("engine") / "engine.py"
    script.write_text(ENGINE, encoding="utf-8")
    be = EngineBackend.start([sys.executable, str(script)], sources={"a.sysml": "package P { part def A; }"})
    yield be
    be.close()


def test_the_engine_announces_itself_and_opens(engine):
    assert (engine.engine, engine.version) == ("Test engine", "1.0")
    assert engine.stats == {"sources": ["a.sysml"], "elaborate": True}


def test_reads_through_the_generated_classes(engine):
    from sysml.classes import Package, PartDefinition

    m = Model(engine)
    a = m.resolve("P::A")
    assert isinstance(a, PartDefinition) and a.declaredName == "A" and a.qualifiedName == "P::A"
    assert isinstance(a.owner, Package) and a.owner.ownedMember == [a]
    assert a.effectiveName() == "A"
    assert [e.declaredName for e in m.all()] == ["P", "A"]
    assert m.resolve("Q") is None


def test_errors_map_as_the_protocol_says(engine):
    m = Model(engine)
    a = m.resolve("P::A")
    with pytest.raises(NotAnswered, match="Test engine: isAbstract"):
        a.isAbstract
    with pytest.raises(UnresolvedReference):
        a.ownedRelationship
    with pytest.raises(BackendError, match="the engine broke"):
        engine.get("2", "boom")
    assert engine.get("2", "noSuchProperty") is None
    with pytest.raises(Gone):
        engine.metaclass("9")

    class Op:
        name = "noSuchOperation"

    with pytest.raises(BackendError, match="noSuchOperation"):
        engine.call("2", Op, ())


def test_an_engine_that_is_not_ready(tmp_path):
    script = tmp_path / "not_ready.py"
    script.write_text('import json; print(json.dumps({"ready": False, "error": {"kind": "unavailable", '
                      '"message": "no licence"}}))\nraise SystemExit(1)\n', encoding="utf-8")
    with pytest.raises(BackendError, match="no licence"):
        EngineBackend.start([sys.executable, str(script)])


def test_a_missing_engine(tmp_path):
    with pytest.raises(BackendError, match="cannot start"):
        EngineBackend.start([str(tmp_path / "no-such-engine")])


def test_a_closed_backend(tmp_path_factory):
    script = tmp_path_factory.mktemp("engine2") / "engine.py"
    script.write_text(ENGINE, encoding="utf-8")
    be = EngineBackend.start([sys.executable, str(script)])
    be.close()
    with pytest.raises(SdkError, match="closed"):
        be.metaclass("1")
