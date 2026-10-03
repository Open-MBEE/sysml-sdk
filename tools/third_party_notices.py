"""Write the third-party notices for the binding library as built for one target.

Usage: python tools/third_party_notices.py <target> <out dir>

The binding library (abi/) compiles in the SysML Toolkit's crates, the crates they depend on and
the Rust standard library. Their licenses (MIT, Apache-2.0, BSD-3-Clause, Unlicense, ...) ask for
their notices in binary distributions, so every package that carries the library carries two files:

- THIRD-PARTY-NOTICES.txt: each compiled-in crate with its version, license and repository, and
  the license files the crate itself ships, each distinct text once with the crates it covers.
  The crates are the normal (not build or dev) dependencies of the library for `<target>`, from
  `cargo metadata --filter-platform`; proc-macro crates run at build time and are not included.
  The SysML Toolkit's crates carry the SysML Toolkit's LICENSE.
- RUST-STANDARD-LIBRARY-NOTICES.html: the Rust project's own notices for the standard library and
  its dependencies, as the toolchain that built the library ships them
  (`<sysroot>/share/doc/rust/COPYRIGHT-library.html`, part of the rustc component).

A crate without a license file, or a toolchain without the notice, stops the build.
"""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
ABI = ROOT / "abi"
LICENSE_PREFIXES = ("license", "licence", "copying", "copyright", "notice", "unlicense")


def metadata(target: str) -> dict:
    out = subprocess.run(
        ["cargo", "metadata", "--format-version", "1", "--locked", "--filter-platform", target],
        cwd=ABI, check=True, capture_output=True, text=True, encoding="utf-8",
    ).stdout
    return json.loads(out)


def shipped_packages(meta: dict) -> list[dict]:
    """The packages whose code is compiled into the library: the normal-dependency closure of
    the library's own package, without it. A proc-macro crate runs in the compiler; neither it
    nor its own dependencies end up in the library, so the walk stops there."""
    packages = {p["id"]: p for p in meta["packages"]}
    nodes = {n["id"]: n for n in meta["resolve"]["nodes"]}
    root = meta["resolve"]["root"]

    def proc_macro(pid: str) -> bool:
        return all("proc-macro" in t["kind"] for t in packages[pid]["targets"])

    seen, stack = set(), [root]
    while stack:
        pid = stack.pop()
        if pid in seen or proc_macro(pid):
            continue
        seen.add(pid)
        for dep in nodes[pid]["deps"]:
            if any(k["kind"] is None for k in dep["dep_kinds"]):
                stack.append(dep["pkg"])
    seen.discard(root)
    return sorted((packages[pid] for pid in seen), key=lambda p: (p["name"], p["version"]))


def license_files(p: dict) -> list[Path]:
    """The license files a crate ships in its own directory (and its `license-file`, if set)."""
    home = Path(p["manifest_path"]).parent
    files = {f for f in home.iterdir() if f.is_file() and f.name.lower().startswith(LICENSE_PREFIXES)}
    if p.get("license_file"):
        files.add(home / p["license_file"])
    return sorted(files)


def toolkit_root(p: dict) -> Path:
    """The sysml-toolkit checkout a path-dependency crate lives in (`<root>/crates/<crate>/Cargo.toml`)."""
    return Path(p["manifest_path"]).parents[2]


def text_of(path: Path) -> str:
    return path.read_text(encoding="utf-8", errors="replace").replace("\r\n", "\n").strip() + "\n"


def notices(target: str) -> str:
    meta = metadata(target)
    shipped = shipped_packages(meta)
    toolkit = [p for p in shipped if p["source"] is None]
    crates = [p for p in shipped if p["source"] is not None]

    texts: dict[str, list[str]] = {}  # license text -> what it covers
    lines = [
        f"Third-party software in the SysML v2 SDK's binding library ({target})",
        "",
        "The binding library compiles in the software below. Each part stays under its own license,",
        "whose text follows the list. The Rust standard library's notices are in",
        "RUST-STANDARD-LIBRARY-NOTICES.html beside this file.",
        "",
    ]
    if toolkit:
        root = toolkit_root(toolkit[0])
        names = ", ".join(f"{p['name']} {p['version']}" for p in toolkit)
        lines += ["The SysML Toolkit:", f"  {names}", f"  license: {toolkit[0].get('license') or 'see LICENSE'}", ""]
        for name in ("LICENSE", "NOTICE"):
            if (root / name).is_file():
                texts.setdefault(text_of(root / name), []).append(f"the SysML Toolkit ({name})")
    lines.append("Rust crates:")
    for p in crates:
        files = license_files(p)
        if not files:
            raise SystemExit(f"{p['name']} {p['version']}: no license file in {Path(p['manifest_path']).parent}")
        repo = p.get("repository") or ""
        lines.append(f"  {p['name']} {p['version']}  ({p.get('license') or 'see its license file'})  {repo}".rstrip())
        for f in files:
            texts.setdefault(text_of(f), []).append(f"{p['name']} {p['version']} ({f.name})")
    lines.append("")
    for text, covers in texts.items():
        lines += ["=" * 100, "Applies to: " + "; ".join(covers), "=" * 100, "", text]
    return "\n".join(lines)


def rust_std_notice() -> Path:
    sysroot = subprocess.run(["rustc", "--print", "sysroot"], cwd=ABI, check=True, capture_output=True,
                             text=True).stdout.strip()
    path = Path(sysroot) / "share" / "doc" / "rust" / "COPYRIGHT-library.html"
    if not path.is_file():
        raise SystemExit(f"the toolchain has no standard-library notice at {path}")
    return path


def write(target: str, out: Path) -> list[Path]:
    out.mkdir(parents=True, exist_ok=True)
    notice = out / "THIRD-PARTY-NOTICES.txt"
    notice.write_text(notices(target), encoding="utf-8", newline="\n")
    std = out / "RUST-STANDARD-LIBRARY-NOTICES.html"
    std.write_bytes(rust_std_notice().read_bytes())
    return [notice, std]


if __name__ == "__main__":
    if len(sys.argv) != 3:
        raise SystemExit("usage: python tools/third_party_notices.py <target> <out dir>")
    for f in write(sys.argv[1], Path(sys.argv[2])):
        print(f)
