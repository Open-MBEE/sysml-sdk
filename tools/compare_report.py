"""Compare a query report read through the SysML Toolkit with the expected report.

Usage: <query demo> | python tools/compare_report.py examples/queries/expected/<report>

Every section (a report under its `== heading ==`) must equal the expected section, unless the
SysML Toolkit refused a read in it: the demo then stops that report with the line
`  report stopped: <the SysML Toolkit's refusal>`. A stopped section must equal the expected one up
to that line, and the refused member must be one TOOLKIT_REPORT_REFUSALS in
python/tests/toolkit_gaps.py lists for the section. A listed section that completes, or stops on
a member not listed, fails: the SysML Toolkit changed, so the table changes with it. The stopped
sections are printed as expected failures; the exit status is 1 when anything else differs.
"""
from __future__ import annotations

import difflib
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT / "python" / "tests"))

from toolkit_gaps import TOOLKIT_REPORT_REFUSALS  # noqa: E402

STOP = "report stopped: "
REFUSAL = " is not answered by the SysML Toolkit"


def sections(text: str) -> list[list[str]]:
    """The report's sections: each starts at a heading and ends before the next one, or at a stop
    line. Blank lines at the edges of a section are dropped."""
    out: list[list[str]] = []
    current: list[str] | None = None
    for line in text.replace("\r\n", "\n").split("\n"):
        if line.startswith("== "):
            if current is not None:
                out.append(current)
            current = [line]
        elif line.strip().startswith(STOP):
            out.append((current or []) + [line])
            current = None
        elif current is not None or line.strip():
            current = (current or []) + [line]
    if current is not None:
        out.append(current)
    trimmed = []
    for s in out:
        while s and not s[-1].strip():
            s = s[:-1]
        trimmed.append(s)
    return trimmed


def accepted(row) -> dict[str, str]:
    """A table row's accepted refusals, member -> why: one (member, why) pair, or a list of them
    where the report stops at a different read depending on how the model was loaded."""
    return dict(row if isinstance(row, list) else [row])


def check(actual: str, expected_path: Path) -> tuple[list[str], list[str]]:
    """(problems, expected failures) of `actual` against the expected report at `expected_path`."""
    listed = TOOLKIT_REPORT_REFUSALS.get(expected_path.name, {})
    expected = sections(expected_path.read_text(encoding="utf-8"))
    got = sections(actual)
    problems, stopped = [], []
    if len(got) != len(expected):
        problems.append(f"{len(got)} sections, {len(expected)} expected")
    for exp, act in zip(expected, got):
        title = exp[0][3:-3] if exp and exp[0].startswith("== ") else "(untitled)"
        row = listed.get(title)
        if act and act[-1].strip().startswith(STOP):
            message = act[-1].strip()[len(STOP):]
            member = message.split(REFUSAL)[0].split("::")[-1] if REFUSAL in message else None
            printed = act[:-1]
            if printed != exp[:len(printed)]:
                problems.append(f"{title}: what the report printed before the SysML Toolkit's refusal differs "
                                f"from the expected report")
            elif row is None:
                problems.append(f"{title}: stopped by the SysML Toolkit ({message}); not listed in "
                                f"TOOLKIT_REPORT_REFUSALS")
            elif member not in accepted(row):
                problems.append(f"{title}: the SysML Toolkit refused {member}; TOOLKIT_REPORT_REFUSALS lists "
                                f"{' or '.join(accepted(row))}")
            else:
                stopped.append(f"{title}: the SysML Toolkit refuses {member} ({accepted(row)[member]})")
        elif act != exp:
            diff = "\n".join(difflib.unified_diff(exp, act, "expected", "actual", lineterm=""))
            problems.append(f"{title}: differs from the expected report\n{diff}")
        elif row is not None:
            problems.append(f"{title}: complete now, but TOOLKIT_REPORT_REFUSALS lists a refusal of "
                            f"{' or '.join(accepted(row))}; "
                            f"remove the row")
    return problems, stopped


def main() -> None:
    if len(sys.argv) != 2:
        raise SystemExit("usage: <query demo> | python tools/compare_report.py <expected report>")
    problems, stopped = check(sys.stdin.read(), Path(sys.argv[1]))
    for s in stopped:
        print(f"expected failure: {s}")
    for p in problems:
        print(f"FAIL {p}")
    sys.exit(1 if problems else 0)


if __name__ == "__main__":
    main()
