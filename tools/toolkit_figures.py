"""Measure what the pinned SysML Toolkit release answers, and write the figures where this
repository states them.

Usage: python tools/toolkit_figures.py [--toolkit DIR] [--write]

Measures, with the binding library built (abi/target/release) and the sysml-toolkit checkout
beside this repository (../sysml-toolkit, or --toolkit), whose SysML-v2-Release submodule holds the
specification's Annex A model and the standard library:

  - every property of every element of the Annex A model read through the SysML Toolkit, by
    outcome (value, empty, refused, unresolved), counted as python/tests/test_annex_a.py counts
    them, and among them the references that do not resolve (relationships whose `target` does
    not);
  - the operation declarations the SysML Toolkit runs: those its operation entry point has a body
    for (the declaration ids in the `handler` table of crates/sysmlv2-model/src/json/operations.rs),
    out of the declarations in metamodel.json.

Prints them, and what differs from the figures in python/tests/toolkit_gaps.py (the reads by
outcome), STATUS.md (elements, reads, the outcome table, the operations the SysML Toolkit runs)
and CHANGELOG.md (the share of reads refused, the operations the SysML Toolkit runs, the
references that do not resolve). With --write, rewrites those figures in place; nothing else in
the files changes. Run it after moving the SysML Toolkit pin
(abi/README.md, "Moving to a newer SysML Toolkit release").
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))
sys.path.insert(0, str(ROOT / "python"))

from naming import PYTHON_MODULE  # noqa: E402

OUTCOMES = ("value", "empty", "refused", "unresolved")


def reads(toolkit: Path) -> tuple[int, Counter, int]:
    """Elements of the Annex A model, its property reads by outcome, and its references that do not
    resolve (relationships whose target does not)."""
    import importlib

    sdk = importlib.import_module(PYTHON_MODULE)
    base = importlib.import_module(PYTHON_MODULE + ".base")
    release = toolkit / "spec-refs" / "SysML-v2-Release"
    model_file = release / "sysml" / "src" / "examples" / "Vehicle Example" / "SysML v2 Spec Annex A SimpleVehicleModel.sysml"
    model = sdk.Model.from_toolkit(sources={model_file.name: model_file.read_text(encoding="utf-8")},
                                  library_dir=str(release / "sysml.library"))
    elements = model.elements_of_type(base.Element)
    counts: Counter = Counter()
    unresolved_targets = 0
    for e in elements:
        for name in sorted({n for c in type(e).__mro__ for n, d in vars(c).items() if isinstance(d, base.P)}):
            try:
                v = getattr(e, name)
            except sdk.NotImplementedInToolkit:
                counts["refused"] += 1
                continue
            except sdk.UnresolvedReference:
                counts["unresolved"] += 1
                unresolved_targets += name == "target"
                continue
            counts["empty" if v in (None, [], "") else "value"] += 1
    return len(elements), counts, unresolved_targets


def operations(toolkit: Path) -> tuple[int, int]:
    """(declarations the SysML Toolkit has a body for, declarations in the metamodel table)."""
    src = (toolkit / "crates" / "sysmlv2-model" / "src" / "json" / "operations.rs").read_text(encoding="utf-8")
    start = src.index("\nfn handler(")
    body = src[start:src.index("\n}\n", start)]
    run = set(re.findall(r'"([A-Z][A-Za-z]*(?:-[A-Za-z]+)+-[A-Za-z]+_[^"]*)"', body))
    table = json.loads((ROOT / "metamodel.json").read_text(encoding="utf-8"))
    declared = sum(len(c.get("ops", [])) for c in table["classes"].values())
    return len(run), declared


def thousands(n: int) -> str:
    return f"{n:,}"


def rewrite(path: Path, edits: list[tuple[str, str, str]], write: bool) -> list[str]:
    """Apply (what, pattern, replacement) regex edits; each pattern must match exactly once."""
    text = path.read_text(encoding="utf-8")
    changed = []
    for what, pattern, replacement in edits:
        new, n = re.subn(pattern, replacement, text, flags=re.S)
        if n != 1:
            sys.exit(f"{path.name}: expected one match for {what}, found {n}")
        if new != text:
            changed.append(f"{path.relative_to(ROOT).as_posix()}: {what}")
        text = new
    if write and changed:
        path.write_bytes(text.encode("utf-8"))
    return changed


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--toolkit", type=Path, default=ROOT.parent / "sysml-toolkit")
    ap.add_argument("--write", action="store_true", help="rewrite the figures in place")
    a = ap.parse_args()

    elements, counts, references = reads(a.toolkit)
    total = sum(counts.values())
    run, declared = operations(a.toolkit)
    share = f"{100 * counts['refused'] / total:.1f}"
    print(f"Annex A: {elements} elements, {total} reads: "
          + ", ".join(f"{o} {counts[o]}" for o in OUTCOMES) + f" ({share} % refused)")
    print(f"references that do not resolve: {references}")
    print(f"operations: the SysML Toolkit runs {run} of the {declared} declarations")

    gaps = "{" + ", ".join(f'"{o}": {counts[o]}' for o in OUTCOMES) + "}"
    changed = rewrite(ROOT / "python" / "tests" / "toolkit_gaps.py", [
        ("the reads by outcome", r'(\("\*", "reads"\): \()\{[^}]*\}', r"\g<1>" + gaps),
    ], a.write)
    changed += rewrite(ROOT / "STATUS.md", [
        ("the elements and reads",
         r"every one of its [\d,]+ elements was read through the SysML Toolkit \([\d,]+ reads\)",
         f"every one of its {thousands(elements)} elements was read through the SysML Toolkit "
         f"({thousands(total)} reads)"),
        ("the values", r"(\| a value \| )[\d,]+( \|)", rf"\g<1>{thousands(counts['value'])}\g<2>"),
        ("the empty reads", r"(\| empty \(no value in the model, or an empty list\) \| )[\d,]+( \|)",
         rf"\g<1>{thousands(counts['empty'])}\g<2>"),
        ("the refusals", r"(\| refused: the SysML Toolkit cannot derive the member completely yet \| )[\d,]+( \|)",
         rf"\g<1>{thousands(counts['refused'])}\g<2>"),
        ("the unresolved references", r"(\| a reference that does not resolve \| )[\d,]+( \|)",
         rf"\g<1>{thousands(counts['unresolved'])}\g<2>"),
        ("the operations the SysML Toolkit runs", r"(the SysML Toolkit evaluates)(\s+)\d+(\s+of the)\s+\d+(\s+operation declarations)",
         rf"\g<1>\g<2>{run}\g<3> {declared}\g<4>"),
    ], a.write)
    changed += rewrite(ROOT / "CHANGELOG.md", [
        ("the share of reads refused", r"(model with the library, )[\d.]+( % of all property reads are refused)",
         rf"\g<1>{share}\g<2>"),
        ("the operations the SysML Toolkit runs", r"(\*\*A part of the specification operations is evaluated:\*\*)(\s+)\d+(\s+of the)\s+\d+(\s+operation declarations)",
         rf"\g<1>\g<2>{run}\g<3> {declared}\g<4>"),
        ("the references that do not resolve", r"(\*\*Some references do not resolve\*\*.*?\()\d+( references there\))",
         rf"\g<1>{references}\g<2>"),
    ], a.write)
    if changed:
        print(("rewrote:" if a.write else "differs (run with --write to rewrite):") + "\n  " + "\n  ".join(changed))
    else:
        print("every stated figure matches")


if __name__ == "__main__":
    main()
