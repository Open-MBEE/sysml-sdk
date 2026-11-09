"""Convenience members over lists (ElementList) on each backend, against the members on each element.

For every member a list carries (element and string results), on every element of the examples' models it
applies to: the list's answer is the elements' answers in list order, list results concatenated and absent
single results dropped; if an element refuses, the list refuses with the same kind of error. The arguments
are each member's first example's (convenience.json). Skips without the backend, as
test_convenience_answers.py does.
"""

from __future__ import annotations

import os

import pytest
from conftest import pilot_at_hand, pilot_server, toolkit_library
from convenience_check import RECORDS, ROOT, SOURCES, TABLE, find, snake

from sysml import Element, ElementList, Model, NotAnswered, Scope
from sysml.base import _list_members

RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"
FIRST_EXAMPLE = {}
for _e in TABLE["examples"]:
    FIRST_EXAMPLE.setdefault(_e["member"], _e)


@pytest.fixture(scope="module", params=["toolkit", "pilot"])
def model(request, tmp_path_factory):
    if request.param == "toolkit":
        if toolkit_library() is None:
            pytest.skip("binding library not built (abi/)")
        if not RELEASE_LIBRARY.is_dir():
            pytest.skip("sysml-toolkit checkout with spec-refs/SysML-v2-Release not beside this repository")
        yield Model.from_toolkit(sources=SOURCES, library_dir=str(RELEASE_LIBRARY))
        return
    reason = pilot_at_hand()
    if reason is not None:
        pytest.skip(reason)
    from sysml.pilot import PilotBackend

    os.environ.setdefault("SYSML_PILOT_SERVER", pilot_server(tmp_path_factory.mktemp("pilot-server")))
    backend = PilotBackend.open(sources=SOURCES, transport="process", heap="2g")
    try:
        yield Model(backend)
    finally:
        backend.close()


def arguments(model, name: str) -> dict:
    e = FIRST_EXAMPLE.get(name, {})
    out = {}
    for p in RECORDS[name]["params"]:
        if p["name"] in e.get("args", {}):
            v = e["args"][p["name"]]
            out[p["name"]] = find(model, v) if p["type"]["kind"] == "element" else v
    return out


def each_answer(member, elements, kwargs) -> list:
    """The answer on each element, or its refusal."""
    out = []
    for el in elements:
        try:
            out.append(getattr(el, member.spelled)(**kwargs))
        except NotAnswered as x:
            out.append(x)
    return out


def flattened(member, answers) -> list:
    out = []
    for v in answers:
        if member.result == "element":
            if v is not None:
                out.append(v)
        else:
            out.extend(v)
    return out


def compare(member, name, xs, answers, kwargs, problems) -> str:
    """The list's answer on xs against the elements' answers: "compared" or "refused"."""
    first_refusal = next((a for a in answers if isinstance(a, NotAnswered)), None)
    try:
        got = getattr(xs, name)(**kwargs)
    except NotAnswered as x:
        if first_refusal is None or type(x) is not type(first_refusal):
            problems.append(f"{name}: the list refused ({x}), the elements gave {first_refusal!r}")
        return "refused"
    want = None if first_refusal is not None else flattened(member, answers)
    if want is None:
        problems.append(f"{name}: an element refused ({first_refusal}), the list answered")
    elif list(got) != want:
        problems.append(f"{name}: {len(got)} items from the list, {len(want)} from the elements")
    elif member.result != "strings" and not isinstance(got, ElementList):
        problems.append(f"{name}: the result is a {type(got).__name__}, not an ElementList")
    return "compared"


def test_every_list_member_is_the_flattened_element_answers(model):
    """On all the elements a member applies to (a refusal by any refuses the list), and on those of
    them that answer (so that a backend that refuses some elements is compared too)."""
    elements = [el for el in model.all() if isinstance(el, Element)]
    counts = {"all compared": 0, "all refused": 0, "answering compared": 0}
    problems = []
    for name, member in sorted(_list_members().items()):
        applicable = ElementList(el for el in elements if hasattr(type(el), name))
        if not applicable:
            continue
        kwargs = arguments(model, RECORDS_BY_SPELLING[name])
        answers = each_answer(member, applicable, kwargs)
        counts["all " + compare(member, name, applicable, answers, kwargs, problems)] += 1
        answering = [(el, a) for el, a in zip(applicable, answers) if not isinstance(a, NotAnswered)]
        if answering and len(answering) < len(applicable):
            compare(member, name, ElementList(el for el, _ in answering), [a for _, a in answering], kwargs, problems)
            counts["answering compared"] += 1
    print(f"\n{type(model._backend).__name__}: {counts}")
    assert not problems, "\n".join(problems)
    applicable_members = [n for n in _list_members() if any(hasattr(type(el), n) for el in elements)]
    assert counts["all compared"] + counts["all refused"] == len(applicable_members)


RECORDS_BY_SPELLING = {snake(name): name for name in RECORDS}


def test_chains(model):
    """A chain over lists: the part definitions of a package, then their parts."""
    pkg = model.resolve("DefinitionLists::Imports")
    defs = pkg.find_part_defs(Scope.ALL)
    assert isinstance(defs, ElementList) and defs
    try:
        parts = defs.find_parts(Scope.ALL)
    except NotAnswered:
        pytest.skip("the backend does not answer findParts on these definitions")
    want = [p for d in defs for p in d.find_parts(Scope.ALL)]
    assert list(parts) == want


def test_lists_carry_no_scalar_members(model):
    xs = ElementList([model.resolve("Structure::Car")])
    with pytest.raises(AttributeError):
        xs.find_has_default_value  # noqa: B018 - a Boolean member stays on the elements
    assert xs.find_doc_text() == ["A car. "]


def test_a_member_that_does_not_apply_raises(model):
    pkg = model.resolve("Structure::Outer")
    with pytest.raises(TypeError):
        ElementList([pkg]).find_parts()


def test_empty_lists_answer_empty_lists(model):
    assert ElementList().find_parts() == []
    assert isinstance(ElementList().find_parts(), ElementList)
