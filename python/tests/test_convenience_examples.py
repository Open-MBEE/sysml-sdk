"""The convenience members' examples (convenience.json) against the models on the backends at hand.

Checks the examples, not the members (no backend answers those yet): every element an example names is
in its model as the backend reads it (the element asked, each step of its path, element arguments and
answers), and every element the families' rows of facts give a metaclass for has that metaclass. A
qualified name that does not resolve, such as a protected member's from outside, is looked up through
its owner's owned members by name.

The examples' models are read together, in one session per backend. The SysML Toolkit needs the binding
library and the sysml-toolkit checkout's release library beside this repository; the Pilot needs a Java
runtime and a Pilot (conftest.pilot_at_hand). Each skips without them.
"""

from __future__ import annotations

import json
import os
import re
from pathlib import Path

import pytest
from conftest import pilot_at_hand, pilot_server, toolkit_library

from sysml import Model

ROOT = Path(__file__).resolve().parents[2]
TABLE = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))
RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"
SOURCES = {f"{name}.sysml": m["text"] for name, m in TABLE["models"].items()}
RETURNS = {m["name"]: m["returns"] for m in TABLE["members"]}
PARAMS = {m["name"]: {p["name"]: p["type"] for p in m["params"]} for m in TABLE["members"]}
STEP = re.compile(r"(\w+)(?:\[(\w+)\])?")


def _toolkit():
    if toolkit_library() is None:
        pytest.skip("binding library not built (abi/)")
    if not RELEASE_LIBRARY.is_dir():
        pytest.skip("sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository")
    return Model.from_toolkit(sources=SOURCES, library_dir=str(RELEASE_LIBRARY))


def _pilot(tmp_path_factory):
    reason = pilot_at_hand()
    if reason is not None:
        pytest.skip(reason)
    from sysml.pilot import PilotBackend

    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    return Model(PilotBackend.open(sources=SOURCES, transport="process", heap="2g"))


@pytest.fixture(scope="module", params=["toolkit", "pilot"])
def model(request, tmp_path_factory):
    m = _toolkit() if request.param == "toolkit" else _pilot(tmp_path_factory)
    yield m
    close = getattr(m._backend, "close", None)
    if close:
        close()


def find(model, qualified_name: str):
    """The element by qualified name; else through its owner's owned members by name."""
    el = model.resolve(qualified_name)
    if el is not None:
        return el
    owner_name, sep, last = qualified_name.rpartition("::")
    if not sep:
        return None
    owner = find(model, owner_name)
    if owner is None:
        return None
    name = last[1:-1] if len(last) > 1 and last[0] == last[-1] == "'" else last
    return next((m for m in owner.ownedMember if name in (m.name, m.declaredName)), None)


def named_elements(value, t: dict):
    """The qualified names in an example's value of type `t` (elements of answers given by name, and the
    element-valued properties of those given as {"@type": ...})."""
    if t["kind"] != "element" or value is None:
        return []
    values = value if isinstance(value, list) else [value]
    out = []
    for v in values:
        if isinstance(v, str):
            out.append(v)
        elif isinstance(v, dict):
            out += [x for k, x in v.items() if k != "@type" and isinstance(x, str) and "::" in x]
    return out


def walk(model, el, via: list[str]):
    """Follow an example's path of specification properties; every step gives one element."""
    from sysml.classes import REGISTRY

    for step in via:
        prop, kind = STEP.fullmatch(step).groups()
        value = getattr(el, prop)
        values = value if isinstance(value, list) else [value]
        if kind:
            values = [v for v in values if v is not None and v.metaclass_name in _kinds(kind, REGISTRY)]
        if len(values) != 1:
            return None, f"{step} gives {len(values)} elements"
        el = values[0]
    return el, None


def _kinds(kind: str, registry) -> set[str]:
    base = registry.get(kind)
    return {name for name, cls in registry.items() if base is not None and issubclass(cls, base)}


def test_every_example_names_what_its_model_has(model):
    problems = []
    for e in TABLE["examples"]:
        where = f"{e['member']} at {e['at']}"
        el = find(model, e["at"])
        if el is None:
            problems.append(f"{where}: not found")
            continue
        if e.get("via"):
            _, why = walk(model, el, e["via"])
            if why:
                problems.append(f"{where} via {e['via']}: {why}")
        for name, value in e.get("args", {}).items():
            if PARAMS[e["member"]][name]["kind"] == "element" and find(model, value) is None:
                problems.append(f"{where}: argument {name} = {value} not found")
        for key in ("expect", "includes", "excludes"):
            for qn in named_elements(e.get(key), RETURNS[e["member"]]):
                if find(model, qn) is None:
                    problems.append(f"{where}: {key} names {qn}, not found")
    assert not problems, "\n".join(problems)


def test_the_metaclasses_the_facts_give(model):
    problems = []
    for name, m in TABLE["models"].items():
        for qn, mc in m["metaclasses"].items():
            el = find(model, qn)
            if el is None:
                problems.append(f"{qn}: not found")
            elif el.metaclass_name != mc:
                problems.append(f"{qn}: {el.metaclass_name}, the facts say {mc}")
    assert not problems, "\n".join(problems)
