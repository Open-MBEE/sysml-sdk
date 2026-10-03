"""Package a built binding library as a release archive.

Usage: python tools/package_abi.py <target> [--out DIR]

<target> is a Rust target triple: the host triple the library in abi/target/release was built
for, or wasm32-unknown-unknown for the WebAssembly build. The archive holds one directory with
the library, LICENSE, NOTICE, the third-party notices of what the library compiles in
(tools/third_party_notices.py) and a short README, and is named sysmlv2_abi-<version>-<target>, as .zip
for Windows targets and .tar.gz for every other. The version is the SDK's (pyproject.toml); the
SysML Toolkit release comes from vendor/sysml-toolkit/PIN.json.
"""

from __future__ import annotations

import argparse
import json
import shutil
import tarfile
import tomllib
import zipfile
from pathlib import Path

import third_party_notices

ROOT = Path(__file__).resolve().parent.parent

README = """sysmlv2_abi {version} for {target}

The binding library of the SysML v2 SDK: the C interface through which every SDK language reads
a model with the SysML Toolkit. Built from sysml-sdk {version} against the SysML Toolkit
{repository} at {tag} (commit {commit}).

{usage}
License: Apache-2.0, see LICENSE and NOTICE. The library compiles in the SysML Toolkit, Rust
crates and the Rust standard library, each under its own license: see THIRD-PARTY-NOTICES.txt and
RUST-STANDARD-LIBRARY-NOTICES.html.
"""

USAGE_NATIVE = """Point SYSMLV2_ABI at the library file:

    {command}
"""

QUARANTINE = """
A library downloaded through a web browser is quarantined by macOS and will not load. Remove the
attribute once:

    xattr -d com.apple.quarantine {library}
"""

USAGE_WASM = """JavaScript loads this module in Node and in the browser. In Node, point SYSMLV2_ABI_WASM at it:

    export SYSMLV2_ABI_WASM=/path/to/{library}
"""


def usage(target: str, library: str) -> str:
    if target.startswith("wasm32"):
        return USAGE_WASM.format(library=library)
    if "windows" in target:
        text = USAGE_NATIVE.format(command="set SYSMLV2_ABI=C:" + "\\path\\to\\" + library)
    else:
        text = USAGE_NATIVE.format(command=f"export SYSMLV2_ABI=/path/to/{library}")
    if "apple-darwin" in target:
        text += QUARANTINE.format(library=library)
    return text


def library_path(target: str) -> Path:
    if target == "wasm32-unknown-unknown":
        return ROOT / "abi" / "target" / target / "release" / "sysmlv2_abi.wasm"
    if "windows" in target:
        name = "sysmlv2_abi.dll"
    elif "apple-darwin" in target:
        name = "libsysmlv2_abi.dylib"
    else:
        name = "libsysmlv2_abi.so"
    return ROOT / "abi" / "target" / "release" / name


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("target")
    ap.add_argument("--out", type=Path, default=ROOT / "dist")
    a = ap.parse_args()

    version = tomllib.loads((ROOT / "pyproject.toml").read_text(encoding="utf-8"))["project"]["version"]
    toolkit = json.loads((ROOT / "vendor" / "sysml-toolkit" / "PIN.json").read_text(encoding="utf-8"))["toolkit"]
    lib = library_path(a.target)
    if not lib.exists():
        raise SystemExit(f"no library at {lib}; build abi/ for {a.target} first")

    stem = f"sysmlv2_abi-{version}-{a.target}"
    staging = a.out / stem
    if staging.exists():
        shutil.rmtree(staging)
    staging.mkdir(parents=True)
    shutil.copy2(lib, staging / lib.name)
    shutil.copy2(ROOT / "LICENSE", staging / "LICENSE")
    shutil.copy2(ROOT / "NOTICE", staging / "NOTICE")
    third_party_notices.write(a.target, staging)
    (staging / "README.txt").write_text(
        README.format(version=version, target=a.target, usage=usage(a.target, lib.name),
                      repository=toolkit["repository"].removeprefix("https://github.com/"),
                      tag=toolkit["tag"], commit=toolkit["commit"][:12]),
        encoding="utf-8", newline="\n")

    if "windows" in a.target:
        archive = a.out / f"{stem}.zip"
        with zipfile.ZipFile(archive, "w", zipfile.ZIP_DEFLATED) as z:
            for f in sorted(staging.iterdir()):
                z.write(f, f"{stem}/{f.name}")
    else:
        archive = a.out / f"{stem}.tar.gz"
        with tarfile.open(archive, "w:gz") as t:
            t.add(staging, arcname=stem)
    shutil.rmtree(staging)
    print(archive)


if __name__ == "__main__":
    main()
