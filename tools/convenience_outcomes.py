"""Run the convenience members' examples on the backends at hand and write their outcomes as JSON.

Usage: python tools/convenience_outcomes.py --out outcomes.json [--backends toolkit,pilot]

Each example is run as the tests run it (python/tests/convenience_check.py) and recorded with its id
(tools/reference_data.example_id), its outcome ("agrees", "refused by the backend", "mismatch") and the
detail. The SysML Toolkit needs the binding library (SYSMLV2_ABI) and the release library beside this
repository; the Pilot Implementation needs a Java runtime and a Pilot (SYSML_PILOT_HOME) and the engine
(SYSML_PILOT_SERVER). tools/gen_reference.py reads the file.
"""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "python"))
sys.path.insert(0, str(ROOT / "python" / "tests"))
sys.path.insert(0, str(ROOT / "tools"))

from convenience_check import SOURCES, run  # noqa: E402
from reference_data import example_id  # noqa: E402
from sysml import Model  # noqa: E402

RELEASE_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"


def outcomes_of(model, backend: str) -> list[dict]:
    rows = []
    for outcome, cases in run(model, backend).items():
        for e, detail in cases:
            rows.append({"id": example_id(e), "outcome": outcome, "detail": detail})
    return sorted(rows, key=lambda r: r["id"])


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--out", required=True)
    ap.add_argument("--backends", default="toolkit,pilot")
    args = ap.parse_args()
    result = {}
    for name in args.backends.split(","):
        if name == "toolkit":
            model = Model.from_toolkit(sources=SOURCES, library_dir=str(RELEASE_LIBRARY))
            result["toolkit"] = {"label": "SysML Toolkit", "results": outcomes_of(model, "toolkit")}
        elif name == "pilot":
            from sysml.pilot import PilotBackend

            backend = PilotBackend.open(sources=SOURCES, transport="process", heap="2g")
            try:
                result["pilot"] = {"label": "Pilot Implementation", "results": outcomes_of(Model(backend), "pilot")}
            finally:
                backend.close()
        else:
            raise SystemExit(f"unknown backend {name!r}: toolkit or pilot")
    Path(args.out).write_text(json.dumps(result, indent=1, ensure_ascii=False) + "\n", encoding="utf-8")
    for name, r in result.items():
        counts = {}
        for row in r["results"]:
            counts[row["outcome"]] = counts.get(row["outcome"], 0) + 1
        print(f"{name}: {counts}")


if __name__ == "__main__":
    main()
