"""Run the convenience members' examples (convenience.json) on a model and sort the outcomes.

Each example's member is called on its element with its arguments, and the answer compared with the
example's: elements by identity (an expected element given by qualified name is looked up as the
example test does, one given as {"@type": metaclass, property: value} matches an element of exactly that
metaclass with those values), numbers, Booleans and strings by value, `*` as math.inf, a name-keyed map
as its [name, element] pairs in order. A member that
refuses (NotAnswered) agrees with an example that expects a refusal only when the refusal is the
contract's, `Refused` (the backend's own refusals, the other NotAnswered errors, say that it cannot answer,
not what the answer is).

Outcomes: "agrees"; "refused by the backend" (the backend cannot answer yet, or does not answer what the
comparison reads of its answer); "mismatch" (a wrong answer, or a refusal where an answer was expected
from the contract, or none where it refuses).
"""

from __future__ import annotations

import json
import math
from pathlib import Path

import re

from sysml import Element, NotAnswered, Refused

ROOT = Path(__file__).resolve().parents[2]
TABLE = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))
SOURCES = {f"{name}.sysml": m["text"] for name, m in TABLE["models"].items()}
RECORDS = {m["name"]: m for m in TABLE["members"]}


def snake(name: str) -> str:
    return re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()


def find(model, qualified_name: str):
    """The element by qualified name, whatever its visibility (Model.find)."""
    return model.find(qualified_name)


def walk(el, via: list[str]):
    from sysml.classes import REGISTRY

    for step in via:
        prop, _, kind = step.partition("[")
        kind = kind.rstrip("]")
        value = getattr(el, prop)
        values = value if isinstance(value, list) else [value]
        if kind:
            base = REGISTRY.get(kind)
            values = [v for v in values if v is not None and (base is None or isinstance(v, base) or
                                                            any(c.__name__ == kind for c in type(v).__mro__))]
        if len(values) != 1:
            raise LookupError(f"{step} gives {len(values)} elements")
        el = values[0]
    return el


def matches(model, expected, actual) -> bool:
    """Whether one expected value (an example's encoding) is the actual answer."""
    if isinstance(expected, dict) and "@type" in expected:
        if not isinstance(actual, Element) or actual.metaclass_name != expected["@type"]:
            return False
        for prop, want in expected.items():
            if prop == "@type":
                continue
            got = getattr(actual, prop)
            if not matches(model, want, got):
                return False
        return True
    if isinstance(actual, Element):
        return isinstance(expected, str) and find(model, expected) == actual
    if isinstance(actual, str):
        return expected == actual  # a string answer, such as an operator: "*" is itself
    if expected == "*":
        return actual == math.inf
    if isinstance(expected, bool) or isinstance(actual, bool):
        return type(expected) is type(actual) and expected == actual
    if isinstance(expected, (int, float)) and isinstance(actual, (int, float)):
        return float(expected) == float(actual)
    return expected == actual


def outcome(model, e: dict) -> tuple[str, str]:
    """(outcome, detail) of one example."""
    rec = RECORDS[e["member"]]
    el = find(model, e["at"])
    if el is None:
        return "mismatch", f"{e['at']} not found"
    if e.get("via"):
        el = walk(el, e["via"])
    args = {}
    for p in rec["params"]:
        if p["name"] in e.get("args", {}):
            v = e["args"][p["name"]]
            args[p["name"]] = find(model, v) if p["type"]["kind"] == "element" else v
    method = getattr(el, snake(e["member"]), None)
    if method is None:
        return "mismatch", f"{type(el).__name__} has no {e['member']}"
    try:
        answer = method(**args)
    except Refused as x:
        return ("agrees", "refused") if e.get("refused") else ("mismatch", f"refused: {x}")
    except NotAnswered as x:
        return "refused by the backend", str(x)
    if e.get("refused"):
        return "mismatch", f"answered {answer!r}, the contract refuses"
    try:
        return compare(model, e, answer)
    except NotAnswered as x:
        # The answer is there, but the backend does not answer what the comparison reads of it.
        return "refused by the backend", f"the answer cannot be checked: {x}"


def same_items(want: list, got: list, match) -> bool:
    """Whether `got` holds the items of `want`, each matched once, in any order."""
    if len(want) != len(got):
        return False
    left = list(got)
    for w in want:
        i = next((k for k, g in enumerate(left) if match(w, g)), None)
        if i is None:
            return False
        left.pop(i)
    return True


def compare(model, e: dict, answer) -> tuple[str, str]:
    unordered = e.get("unordered", False)
    if "includes" in e:
        missing = [q for q in e["includes"] if not any(matches(model, q, a) for a in answer)]
        present = [q for q in e.get("excludes", []) if any(matches(model, q, a) for a in answer)]
        if missing or present:
            return "mismatch", f"missing {missing}, unexpected {present}"
        return "agrees", ""
    want = e.get("expect")
    if isinstance(answer, dict):  # a name-keyed map: the expected [name, element] pairs, in order
        got = list(answer.items())
        pair = lambda w, kv: w[0] == kv[0] and matches(model, w[1], kv[1])  # noqa: E731
        if not isinstance(want, list) or not (
                same_items(want, got, pair) if unordered
                else len(want) == len(got) and all(pair(w, kv) for w, kv in zip(want, got))):
            return "mismatch", f"expected {want}, got {describe(answer)}"
        return "agrees", ""
    if isinstance(want, list):
        one = lambda w, a: matches(model, w, a)  # noqa: E731
        if not isinstance(answer, list) or not (
                same_items(want, answer, one) if unordered
                else len(want) == len(answer) and all(one(w, a) for w, a in zip(want, answer))):
            return "mismatch", f"expected {want}, got {describe(answer)}"
        return "agrees", ""
    if want is None:
        return ("agrees", "") if answer is None else ("mismatch", f"expected none, got {describe(answer)}")
    if answer is None or not matches(model, want, answer):
        return "mismatch", f"expected {want!r}, got {describe(answer)}"
    return "agrees", ""


def describe(answer) -> str:
    if isinstance(answer, list):
        return "[" + ", ".join(describe(a) for a in answer) + "]"
    if isinstance(answer, dict):
        return "{" + ", ".join(f"{k}: {describe(v)}" for k, v in answer.items()) + "}"
    if isinstance(answer, Element):
        return answer.qualifiedName or f"<{answer.metaclass_name}>"
    return repr(answer)


def deviation(e: dict, backend: str | None, kind: str, detail: str) -> tuple[str, str]:
    """An example the table lists as deviating on this backend (`deviates`, by backend): its mismatch is
    a known deviation, with the table's reason; if it agrees after all, the listing is stale."""
    reason = (e.get("deviates") or {}).get(backend) if backend else None
    if reason is None:
        return kind, detail
    if kind == "mismatch":
        return "known deviation", f"{reason} ({detail})"
    if kind == "agrees":
        return "mismatch", f"listed as deviating on {backend} ({reason}), but it agrees"
    return kind, detail


def run(model, backend: str | None = None) -> dict[str, list[tuple[dict, str]]]:
    """Every example on `model`; `backend` ("toolkit" or "pilot") names it for the examples' `deviates`."""
    out: dict[str, list] = {"agrees": [], "refused by the backend": [], "known deviation": [], "mismatch": []}
    for e in TABLE["examples"]:
        try:
            kind, detail = outcome(model, e)
        except Exception as x:  # noqa: BLE001 - every failure is reported with its example
            kind, detail = "mismatch", f"{type(x).__name__}: {x}"
        kind, detail = deviation(e, backend, kind, detail)
        out[kind].append((e, detail))
    return out
