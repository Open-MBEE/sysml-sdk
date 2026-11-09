"""Call every convenience member on every element of a model where it applies, on one backend, and sort
the calls: answered, refused by the contract (Refused), not answered by the backend (NotAnswered, with
its reason, by the receiver's metaclass), failed (any other exception). It checks that every call ends in an answer or a stated
reason, not that the answers are right (the examples in convenience.json do that).

Usage: python tools/convenience_sweep.py --backend toolkit|pilot --out sweep.json [--library DIR]
                                         [--pilot-jar JAR --pilot-library DIR] [--per-metaclass N]
                                         MODEL.sysml...

Each member is called with its defaults and, where it has a scope or a library flag, once more with
Scope.ALL and library=True. A name argument matches nothing; an element argument is the element's first
owned feature, else the element itself. With --per-metaclass N, each member is called on the first N
elements (in model order) of each metaclass it applies to. The SysML Toolkit needs the binding library
(SYSMLV2_ABI) and a release library (--library, by default the one beside this repository); the Pilot
Implementation needs a Java runtime, a Pilot (SYSML_PILOT_HOME, or --pilot-jar and --pilot-library, such as
a build from source) and the engine (SYSML_PILOT_SERVER).
"""
from __future__ import annotations

import argparse
import json
import sys
import time
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "python"))

from sysml import Model, NotAnswered, Refused, Scope  # noqa: E402
from sysml.classes import REGISTRY  # noqa: E402

RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"
TABLE = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))


def open_model(backend: str, models: list[str], library: str, pilot_jar: str | None = None,
               pilot_library: str | None = None) -> Model:
    if backend == "toolkit":
        return Model.from_toolkit(*models, library_dir=library)
    if backend == "pilot":
        from sysml.pilot import PilotBackend

        return Model(PilotBackend.open(models, transport="process", heap="3g", pilot_jar=pilot_jar,
                                       pilot_library=pilot_library))
    raise SystemExit(f"unknown backend {backend!r}: toolkit or pilot")


def applies(el, recs: list[dict]) -> bool:
    return any(isinstance(el, REGISTRY[r["on"]]) for r in recs if r["on"] in REGISTRY)


def element_argument(el):
    """The element's first owned feature, else the element itself (also where the backend does not
    answer ownedFeature: the sweep measures the members, not how it picks their arguments)."""
    try:
        fs = getattr(el, "ownedFeature", None) or []
    except NotAnswered:
        fs = []
    return fs[0] if fs else el


def arguments(el, rec: dict, wide: bool) -> dict:
    args = {}
    for p in rec["params"]:
        t, n = p["type"], p["name"]
        if t["kind"] == "element":
            args[n] = element_argument(el)
        elif t["type"] == "String":
            args[n] = "noSuchName"
        elif wide and t["type"] == "Scope":
            args[n] = Scope.ALL
        elif wide and t["type"] == "Boolean":
            args[n] = True
    return args


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--backend", required=True)
    ap.add_argument("--out", required=True)
    ap.add_argument("--library", default=str(RELEASE_LIBRARY))
    ap.add_argument("--pilot-jar")
    ap.add_argument("--pilot-library")
    ap.add_argument("--per-metaclass", type=int)
    ap.add_argument("models", nargs="+")
    a = ap.parse_args()

    t0 = time.time()
    model = open_model(a.backend, a.models, a.library, a.pilot_jar, a.pilot_library)
    elements = list(model.all())
    print(f"{a.backend}: opened in {time.time() - t0:.1f}s, {len(elements)} elements")
    members: dict[str, list[dict]] = defaultdict(list)
    for m in TABLE["members"]:
        members[m["name"]].append(m)
    pairs = [(el, name) for el in elements for name, recs in members.items() if applies(el, recs)]
    if a.per_metaclass:
        seen: Counter = Counter()
        sampled = []
        for el, name in pairs:
            if seen[(name, el.metaclass_name)] < a.per_metaclass:
                seen[(name, el.metaclass_name)] += 1
                sampled.append((el, name))
        pairs = sampled
    print(f"{len(pairs)} (element, member) pairs")

    outcomes: Counter = Counter()
    refusals: Counter = Counter()
    failures = []
    t0 = time.time()
    for i, (el, name) in enumerate(pairs):
        rec = members[name][0]
        spelled = rec["spellings"]["python"]
        widens = any(p["type"]["type"] in ("Scope", "Boolean") for p in rec["params"])
        for wide in ([False, True] if widens else [False]):
            args = arguments(el, rec, wide)
            try:
                getattr(el, spelled)(**args)
                outcomes["answered"] += 1
            except Refused as x:
                outcomes["refused by the contract"] += 1
                refusals[(name, "contract", str(x)[:200], el.metaclass_name)] += 1
            except NotAnswered as x:
                outcomes["not answered by the backend"] += 1
                refusals[(name, "backend", str(x)[:200], el.metaclass_name)] += 1
            except Exception as x:  # noqa: BLE001 - every failure is the point
                outcomes["failed"] += 1
                failures.append([name, "wide" if wide else "default", el.metaclass_name,
                                 el.qualifiedName, f"{type(x).__name__}: {x}"[:300]])
        if i and i % 20000 == 0:
            print(f"  {i} pairs, {time.time() - t0:.0f}s", flush=True)
    print(f"ran in {time.time() - t0:.0f}s: {dict(outcomes)}")
    Path(a.out).write_text(json.dumps({
        "backend": a.backend,
        "models": a.models,
        "elements": len(elements),
        "pairs": len(pairs),
        "outcomes": outcomes,
        "refusals": [[n, w, r, mc, c] for (n, w, r, mc), c in sorted(refusals.items(), key=lambda kv: -kv[1])],
        "failures": failures,
    }, indent=1, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"wrote {a.out}")


if __name__ == "__main__":
    main()
