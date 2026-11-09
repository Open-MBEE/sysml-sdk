"""Generate the convenience members' reference: one self-contained HTML page.

Usage: python tools/gen_reference.py --out reference.html [--outcomes outcomes.json] [--review review.json]
       [--standalone]

Reads convenience.json (the members, their OCL, helpers, examples and models), the generated sources
(each member's signature as every language declares it), the backends' outcomes on the examples
(tools/convenience_outcomes.py, optional), and an optional review configuration (extra sections and
the page's introduction). Writes the page with its data embedded; the page renders itself. The page is
the body of an HTML document, for a host that supplies the document around it; --standalone writes a
complete document, to open as a file or serve as it is.
"""
from __future__ import annotations

import argparse
import html
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools"))
from reference_data import example_id  # noqa: E402

LANGS = ["python", "java", "ts", "cs", "cpp"]


def _strip_java(t: str) -> str:
    return t.replace("org.openmbee.sysml.classes.", "").replace("org.openmbee.sysml.", "").replace("java.util.", "")


def signatures() -> dict[str, list[str]]:
    """'lang|Class|spelling' -> the member's declarations in that language (overloads in order)."""
    sigs: dict[str, list[str]] = {}
    root_names = {"AnyElement": "Element", "IElement": "Element"}  # how TypeScript and C# name the root

    def add(lang: str, cls: str, name: str, sig: str) -> None:
        sigs.setdefault(f"{lang}|{root_names.get(cls, cls)}|{name}", []).append(sig)

    cls = None
    for line in (ROOT / "python" / "sysml" / "classes.pyi").read_text(encoding="utf-8").splitlines():
        if m := re.match(r"class (\w+)\(", line):
            cls = m.group(1)
        elif cls and (m := re.match(r"    def (find\w*)\(self(?:, )?(.*)\) -> (.*):(?: \.\.\.)?$", line)):
            add("python", cls, m.group(1), f"{m.group(1)}({m.group(2)}) -> {m.group(3)}")

    cls = None
    for line in (ROOT / "ts" / "classes.d.ts").read_text(encoding="utf-8").splitlines():
        if m := re.match(r"export interface (\w+)\b", line):
            cls = m.group(1)
        elif cls and (m := re.match(r"  (find\w*)\((.*)\): (.*);$", line)):
            add("ts", cls, m.group(1), f"{m.group(1)}({m.group(2)}): {m.group(3)}")

    cls = None
    for line in (ROOT / "csharp" / "OpenMBEE.SysML" / "Classes.g.cs").read_text(encoding="utf-8").splitlines():
        if m := re.match(r"public (?:partial )?interface (\w+)\b", line):
            cls = m.group(1)
        elif cls and (m := re.match(r"    (\S.*?) (Find\w*)\((.*?)\) =>", line)):
            add("cs", cls, m.group(2), f"{m.group(1)} {m.group(2)}({m.group(3)})")

    cls = None
    for line in (ROOT / "cpp" / "include" / "sysml" / "classes.g.hpp").read_text(encoding="utf-8").splitlines():
        if m := re.match(r"struct (\w+) [:{]", line):
            cls = m.group(1)
        elif line.startswith(("template", "class ")):
            cls = None  # ElementList<T>: the list forms, not the element's members
        elif cls and (m := re.match(r"    (.+?) (find_\w+)\((.*?)\) const;", line)):
            add("cpp", cls, m.group(2), f"{m.group(1)} {m.group(2)}({m.group(3)}) const")
    for line in (ROOT / "cpp" / "include" / "sysml" / "element_members.g.hpp").read_text(encoding="utf-8").splitlines():
        if m := re.match(r"(.+?) (find_\w+)\((.*?)\) const;", line):
            add("cpp", "Element", m.group(2), f"{m.group(1)} {m.group(2)}({m.group(3)}) const")

    java = ROOT / "java" / "src" / "org" / "openmbee" / "sysml"
    for path in [java / "Element.java", *sorted((java / "classes").glob("*.java"))]:
        cls = path.stem
        for line in path.read_text(encoding="utf-8").splitlines():
            if m := re.match(r"\s+default (.+?) (find\w*)\((.*?)\) \{", line):
                add("java", cls, m.group(2), _strip_java(f"{m.group(1)} {m.group(2)}({m.group(3)})"))
    return sigs


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--outcomes", help="the backends' outcomes (tools/convenience_outcomes.py)")
    ap.add_argument("--review", help="a review configuration: title, introduction, sections")
    ap.add_argument("--standalone", action="store_true", help="a complete HTML document, not the body of one")
    ap.add_argument("--out", required=True)
    args = ap.parse_args()

    table = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))
    sigs = signatures()
    outcomes = json.loads(Path(args.outcomes).read_text(encoding="utf-8")) if args.outcomes else {}
    review = json.loads(Path(args.review).read_text(encoding="utf-8")) if args.review else {}

    examples = []
    seen: set[str] = set()
    for e in table["examples"]:
        eid = example_id(e)
        if eid in seen:
            raise SystemExit(f"two examples share the id {eid}: {e}")
        seen.add(eid)
        row = dict(e, id=eid, outcomes={})
        for backend, data in outcomes.items():
            for r in data["results"]:
                if r["id"] == eid:
                    row["outcomes"][backend] = {"outcome": r["outcome"], "detail": r["detail"]}
        examples.append(row)

    members = []
    missing = []
    for m in table["members"]:
        rec = {k: m.get(k) for k in ("name", "on", "family", "variant", "kind", "params", "returns", "doc",
                                     "basis", "ocl", "prose", "spellings", "helpers", "uses", "reads",
                                     "refusable", "kindMetaclass", "libraryType", "exactMetaclass")}
        rec["sigs"] = {}
        for lang in LANGS:
            found = sigs.get(f"{lang}|{m['on']}|{m['spellings'][lang]}")
            if found:
                rec["sigs"][lang] = found
            else:
                missing.append(f"{lang} {m['on']}.{m['spellings'][lang]}")
        members.append(rec)
    if missing:
        raise SystemExit(f"{len(missing)} signatures not found in the generated sources, e.g. {missing[:5]}")

    data = {
        "counts": table["counts"],
        "specRefs": table.get("specRefs"),
        "enums": table["enums"],
        "helpers": table["helpers"],
        "members": members,
        "examples": examples,
        "models": table["models"],
        "backends": {k: v["label"] for k, v in outcomes.items()},
        "review": review,
    }
    template = (ROOT / "tools" / "reference_template.html").read_text(encoding="utf-8")
    payload = json.dumps(data, ensure_ascii=False, separators=(",", ":")).replace("</", "<\\/")
    title = html.escape(review.get("title", "SysML SDK Convenience Reference"))
    page = template.replace("{{TITLE}}", title).replace("{{DATA}}", payload)
    if args.standalone:
        page = ('<!doctype html>\n<html lang="en">\n<head>\n<meta charset="utf-8">\n'
                '<meta name="viewport" content="width=device-width, initial-scale=1">\n</head>\n<body>\n'
                + page + "\n</body>\n</html>\n")
    Path(args.out).write_bytes(page.encode("utf-8"))
    print(f"wrote {args.out}: {len(members)} member records, {len(examples)} examples, "
          f"{len(sigs)} signatures, {len(page) // 1024} KiB")


if __name__ == "__main__":
    main()
