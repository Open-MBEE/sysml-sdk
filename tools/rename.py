"""Rename what users type to install or import the SDK.

Usage: python tools/rename.py [--java PACKAGE] [--cs NAMESPACE] [--npm NAME] [--python DIST]
                              [--product NAME] [--tables DIR]

Each option replaces one entry of NAMES in tools/naming.py; a name not given stays as it is:

  --java      the Java package (org.example.sysml); also the jar's module name
  --cs        the C# namespace (Example.SysML); also the NuGet id and the project folder names
  --npm       the npm package (@example/sysml-sdk, or unscoped)
  --python    the Python distribution (what `pip install` takes); the import name is the same with
              "_" for "-", and so is the wheel's file name
  --product   the product name in file names: the jar, the C++ archive, the skills

The script

  1. rewrites every spelling of each name in the tracked text files (vendored inputs excepted, whose
     bytes are pinned): the Java package and its source path, the C# namespace, the npm package and
     the file name `npm pack` gives it, the Python module, the Python distribution where
     `pyproject.toml` names it and in the `| Python |` row of a Markdown table, and then the
     product name wherever it is left. Every other name is set aside first, so the product name
     inside the npm package (or the distribution) is never rewritten as the product;
  2. moves the Java source tree, the two C# project folders and their project files, the Python
     module folder and the skill folders, with `git mv` (the .NET build server is stopped first; it
     holds the folders open on Windows);
  3. rewrites NAMES in tools/naming.py and reruns every generator;
  4. lists every remaining occurrence of a replaced name, for a person to judge.

A name is replaced only where it stands whole, and nothing is written when an old name also
occurs inside other words (`sysml` inside `sysml-toolkit`) or two old names are spelled alike (an
npm package and a Python module both named `sysml`): renaming from such names cannot tell the
occurrences apart. Undo a rename by reverting its commit, and rename again from there.

Running it with the current names changes nothing, which is its own test; running it back with
distinctive old names restores the tree. Repository names (`Open-MBEE/sysml-sdk`), C++ names
(namespace `sysml`) and the binding library's file names (`sysmlv2_abi`) are not among the names.
Commit the result like any change.
"""

from __future__ import annotations

import argparse
import re
import shutil
import subprocess
import sys
from pathlib import Path

import naming

ROOT = naming.ROOT
GENERATORS = [
    "extract_metamodel", "gen_toolkit_table", "gen_toolkit_table_java", "gen_toolkit_table_cs",
    "gen_toolkit_table_cpp", "gen_toolkit_table_js", "gen_abi", "gen_classes", "gen_classes_ts",
    "gen_classes_cs", "gen_classes_java", "gen_classes_cpp", "gen_conformance",
]
TABLE_GENERATORS = {g for g in GENERATORS if g.startswith("gen_toolkit_table")}
# The name table and this script spell the names on purpose, the export script names the public
# repository (not a package), and vendored inputs keep pinned bytes.
SKIP_FILES = {"tools/naming.py", "tools/rename.py", "tools/export-public.sh"}
SKIP_PREFIXES = ("vendor/",)
# Repository names are not package names, even where a product name spells the same.
REPOSITORIES = ("Open-MBEE/sysml-sdk", "Open-MBEE/sysml-toolkit")
SKILL_LANGUAGES = ("python", "typescript", "java", "csharp", "cpp")


def git(*args: str) -> str:
    return subprocess.run(["git", *args], cwd=ROOT, check=True, capture_output=True,
                          text=True).stdout


# How each spelling may continue after it and still be the name (a Java package goes on with
# `.Class`, a wheel's file name with `-0.1.0`, the product with `-cpp`); before it, never a letter,
# a digit, `_`, `@`, `.` or `-`. Most specific first: each is set aside under a placeholder, so no
# later one rewrites a part of it.
BEFORE = r"(?<![A-Za-z0-9_@.-])"
KINDS = [
    ("java_package", "", r"(?![A-Za-z0-9_])"),
    ("java_dir", "", r"(?![A-Za-z0-9_])"),
    ("cs_namespace", "", r"(?![A-Za-z0-9_])"),
    ("npm_package", "", r"(?![A-Za-z0-9_.-])"),
    ("npm_tarball_stem", "-", r"(?=[0-9])"),
    ("python_module", "", r"(?![A-Za-z0-9_]|-(?![0-9]))"),
]


class Ambiguous(Exception):
    pass


def rewrite_text(text: str, so: dict[str, str], sn: dict[str, str]) -> str:
    """Every spelling of the old names in `text`, rewritten to the new ones. Raises Ambiguous where
    an old name also occurs inside another word, since a plain replacement would rewrite that word."""
    held: list[str] = []

    def hold(new: str) -> str:
        held.append(new)
        return f"\x00{len(held) - 1}\x00"

    def replace(old: str, new: str, after: str, text: str) -> str:
        text = re.sub(BEFORE + re.escape(old) + after, lambda m: hold(new), text)
        if old in text:
            i = text.index(old)
            raise Ambiguous(f"{old!r} also occurs inside another word: "
                            f"...{text[max(0, i - 30):i + len(old) + 30]!r}...")
        return text

    for repository in REPOSITORIES:
        text = text.replace(repository, hold(repository))
    for key, suffix, after in KINDS:
        if so[key] != sn[key]:
            text = replace(so[key] + suffix, sn[key] + suffix, after, text)
        else:  # unchanged, but set aside all the same, so the product inside it stays
            text = re.sub(BEFORE + re.escape(so[key] + suffix) + after, lambda m: hold(m.group(0)), text)
    dist_old, dist_new = re.escape(so["python_dist"]), sn["python_dist"]
    text = re.sub(rf'^(name = "){dist_old}(")', lambda m: m.group(1) + hold(dist_new) + m.group(2),
                  text, flags=re.M)
    text = re.sub(rf"^(\|\s*Python[^|\n]*\|\s*`){dist_old}(`)",
                  lambda m: m.group(1) + hold(dist_new) + m.group(2), text, flags=re.M)
    if so["product"] != sn["product"]:
        text = replace(so["product"], sn["product"], r"(?![A-Za-z0-9_])", text)
    return re.sub("\x00([0-9]+)\x00", lambda m: held[int(m.group(1))], text)


def rewrite_files(files: list[str], so: dict[str, str], sn: dict[str, str]) -> list[str]:
    """Rewrite every file, or none: all new texts are made first, and one ambiguity stops it."""
    same = [(a, b) for a in so for b in so if a < b and so[a] == so[b] and (so[a] != sn[a] or so[b] != sn[b])
            and {a, b} != {"python_dist", "product"}]
    if same:
        sys.exit(f"the old names {same} are spelled the same and cannot be told apart; revert the "
                 "commit that made them so, and rename from there")
    new_texts = {}
    problems = []
    for rel in files:
        if rel in SKIP_FILES or rel.startswith(SKIP_PREFIXES):
            continue
        p = ROOT / rel
        try:
            text = p.read_bytes().decode("utf-8")
        except (UnicodeDecodeError, FileNotFoundError, IsADirectoryError):
            continue
        try:
            new = rewrite_text(text, so, sn)
        except Ambiguous as e:
            problems.append(f"{rel}: {e}")
            continue
        if new != text:
            new_texts[rel] = new
    if problems:
        sys.exit("nothing written: an old name is not distinctive enough to replace by its spelling "
                 "(rename from the original names instead, after reverting the rename that "
                 "introduced it):\n  " + "\n  ".join(problems[:20]))
    for rel, new in new_texts.items():
        (ROOT / rel).write_bytes(new.encode("utf-8"))
    return sorted(new_texts)


def moves(so: dict[str, str], sn: dict[str, str]) -> list[tuple[Path, Path]]:
    """The folders and project files to move, checked before anything is written."""
    pairs = [(ROOT / "java" / "src" / so["java_dir"], ROOT / "java" / "src" / sn["java_dir"])]
    for suffix in ("", ".Checks"):
        src = ROOT / "csharp" / f"{so['cs_namespace']}{suffix}"
        dst = ROOT / "csharp" / f"{sn['cs_namespace']}{suffix}"
        pairs += [(src, dst), (dst / f"{so['cs_namespace']}{suffix}.csproj",
                               dst / f"{sn['cs_namespace']}{suffix}.csproj")]
    pairs.append((ROOT / "python" / so["python_module"], ROOT / "python" / sn["python_module"]))
    for lang in SKILL_LANGUAGES:
        pairs.append((ROOT / "skills" / f"{so['product']}-{lang}", ROOT / "skills" / f"{sn['product']}-{lang}"))
    pairs = [(s, d) for s, d in pairs if s != d]
    for s, d in pairs:
        if d.exists() and not s.name.endswith(".csproj"):
            rel = d.relative_to(ROOT).as_posix()
            tracked = git("ls-files", "--", rel).strip()
            hint = "" if tracked else f" (only untracked files: `git clean -fdX {rel}` removes the ignored ones)"
            sys.exit(f"nothing written: cannot move {s.relative_to(ROOT).as_posix()} to {rel}, which exists{hint}")
    return pairs


def move(src: Path, dst: Path) -> None:
    if src == dst or not src.exists():
        return
    dst.parent.mkdir(parents=True, exist_ok=True)
    git("mv", str(src.relative_to(ROOT)), str(dst.relative_to(ROOT)))
    # Remove the directories the move left empty.
    d = src.parent
    while d != ROOT and d.exists() and not any(d.iterdir()):
        d.rmdir()
        d = d.parent


def main() -> None:
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    for key in naming.NAMES:
        ap.add_argument(f"--{key}", help=f"now {naming.NAMES[key]!r}")
    ap.add_argument("--tables", help="naming-table directory for the dispatch-table generators")
    a = ap.parse_args()
    old = dict(naming.NAMES)
    new = {k: (getattr(a, k) or v) for k, v in old.items()}
    so, sn = naming.spellings(old), naming.spellings(new)

    if git("status", "--porcelain", "--untracked-files=no").strip():
        sys.exit("the working tree has uncommitted changes; commit or stash them first")

    planned = moves(so, sn)
    files = git("ls-files").splitlines()
    changed = rewrite_files(files, so, sn)
    print(f"rewrote {len(changed)} files")

    if new != old:
        if so["cs_namespace"] != sn["cs_namespace"] and shutil.which("dotnet"):
            subprocess.run(["dotnet", "build-server", "shutdown"], cwd=ROOT, capture_output=True)
        for src, dst in planned:
            move(src, dst)
        cfg = ROOT / "tools" / "naming.py"
        text = cfg.read_text(encoding="utf-8")
        for key, value in new.items():
            text, n = re.subn(rf'^(    "{key}": )"[^"]*"', lambda m: f'{m.group(1)}"{value}"', text, flags=re.M)
            if n != 1:
                sys.exit(f"tools/naming.py: no single NAMES entry {key!r} to update")
        cfg.write_bytes(text.encode("utf-8"))
        print("moved the folders; NAMES:", {k: v for k, v in new.items() if v != old[k]})

    for g in GENERATORS:
        cmd = [sys.executable, str(ROOT / "tools" / f"{g}.py")]
        if a.tables and g in TABLE_GENERATORS:
            cmd += ["--tables", a.tables]
        r = subprocess.run(cmd, cwd=ROOT, capture_output=True, text=True)
        if r.returncode != 0:
            sys.exit(f"{g} failed:\n{r.stdout}{r.stderr}")
    print(f"regenerated ({len(GENERATORS)} generators)")

    # A replaced name that is still the spelling of another name (the product, when only the
    # distribution changed) is expected to remain.
    replaced = sorted({so[k] for k in so if so[k] != sn[k] and k != "java_dir"} - set(sn.values()),
                      key=len, reverse=True)
    for name in replaced:
        # git grep exits 1 when nothing matches.
        r = subprocess.run(["git", "grep", "-n", "-F", name, "--", ".", *(f":!{s}" for s in SKIP_FILES)],
                           cwd=ROOT, capture_output=True, text=True)
        if r.stdout.strip():
            print(f"\nremaining occurrences of {name!r} (judge each):")
            print(r.stdout.rstrip())


if __name__ == "__main__":
    main()
