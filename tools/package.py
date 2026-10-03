"""Build the release packages of the SDK into one directory.

Usage: python tools/package.py <what> [--target TRIPLE] [--out DIR] [--javac22 PATH]

  python   a platform wheel carrying the binding library built for TARGET (abi/target/release)
  sdist    the Python source distribution, without any native library
  npm      the npm tarball, carrying the WebAssembly build (abi/target/wasm32-unknown-unknown)
  java     sysml-sdk-<version>.jar and its sources jar; with --javac22, a multi-release jar
  csharp   the NuGet package
  cpp      sysml-sdk-cpp-<version>.zip: the headers, the vendored JSON header, the licenses

With --abi-archives DIR, the jar, the NuGet package and the C++ archive also carry the binding
library of every platform whose archive (sysmlv2_abi-<version>-<target>.zip or .tar.gz, as
tools/package_abi.py writes it) is in DIR, each with the notices of what it compiles in: the jar
under <package path>/native/<os>-<arch>/, which the SysML Toolkit backend copies out and loads;
the NuGet package under runtimes/<rid>/native/, where .NET finds it; the C++ archive under
lib/<target>/, to copy beside the executable.

Every package carries LICENSE and NOTICE. The version is pyproject.toml's; the Java package name and the npm
package name come from their own sources, so tools/rename.py keeps them in step.

Java: the foreign function API the SysML Toolkit backend uses is a preview API in JDK 21 and final
from JDK 22, and a class compiled with preview features runs only on the JDK that compiled it. The
jar is therefore built with JDK 21 and --enable-preview, and, given a JDK 22 or newer compiler
(--javac22), also carries the SysML Toolkit backend compiled for JDK 22 under
META-INF/versions/22, so one jar runs on JDK 21 (with --enable-preview) and on every later JDK.
"""

from __future__ import annotations

import argparse
import json
import os
import re
import shutil
import subprocess
import sys
import tarfile
import tomllib
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from naming import JAVA_PACKAGE, JAVA_SRC  # noqa: E402
from package_abi import library_path  # noqa: E402
import third_party_notices  # noqa: E402

VERSION = tomllib.loads((ROOT / "pyproject.toml").read_text(encoding="utf-8"))["project"]["version"]

# Wheel platform tags. The macOS ones match the deployment targets the libraries are built for
# (MACOSX_DEPLOYMENT_TARGET in CI, which are also Rust's defaults for these targets); the x86-64
# Linux one matches the glibc the library is linked against in CI (2.28).
PLATFORM_TAGS = {
    "x86_64-unknown-linux-gnu": "manylinux_2_28_x86_64",
    "aarch64-unknown-linux-gnu": "linux_aarch64",
    "aarch64-apple-darwin": "macosx_11_0_arm64",
    "x86_64-apple-darwin": "macosx_10_12_x86_64",
    "x86_64-pc-windows-msvc": "win_amd64",
    "x86_64-pc-windows-gnu": "win_amd64",
}


# The platforms whose binding library the jar, the NuGet package and the C++ archive carry: the
# directory in the jar (os-arch, as the Java SysML Toolkit backend names the running platform) and
# the .NET runtime identifier. The first target of a platform present in --abi-archives is the one
# carried.
NATIVE_TARGETS = {
    "x86_64-pc-windows-msvc": ("windows-x86_64", "win-x64"),
    "x86_64-pc-windows-gnu": ("windows-x86_64", "win-x64"),
    "x86_64-unknown-linux-gnu": ("linux-x86_64", "linux-x64"),
    "aarch64-apple-darwin": ("macos-aarch64", "osx-arm64"),
    "x86_64-apple-darwin": ("macos-x86_64", "osx-x64"),
}
NOTICES = ("THIRD-PARTY-NOTICES.txt", "RUST-STANDARD-LIBRARY-NOTICES.html")


def natives(archives: Path | None) -> dict[str, dict[str, bytes]]:
    """Target -> {file name: bytes} for the library and its notices, read from the binding library
    archives in `archives`; one target per platform."""
    if archives is None:
        return {}
    found: dict[str, dict[str, bytes]] = {}
    platforms = set()
    for target, (platform, _) in NATIVE_TARGETS.items():
        stem = f"sysmlv2_abi-{VERSION}-{target}"
        wanted = [library_path(target).name, *NOTICES]
        files: dict[str, bytes] = {}
        if (archives / f"{stem}.zip").exists():
            with zipfile.ZipFile(archives / f"{stem}.zip") as z:
                files = {n: z.read(f"{stem}/{n}") for n in wanted}
        elif (archives / f"{stem}.tar.gz").exists():
            with tarfile.open(archives / f"{stem}.tar.gz") as t:
                files = {n: t.extractfile(f"{stem}/{n}").read() for n in wanted}
        if files and platform not in platforms:
            platforms.add(platform)
            found[target] = files
    if not found:
        raise SystemExit(f"no binding library archive of version {VERSION} in {archives}")
    print("carrying the binding library for", ", ".join(found))
    return found


def run(*cmd: str | os.PathLike, cwd: Path = ROOT) -> None:
    print("+", " ".join(str(c) for c in cmd))
    subprocess.run([str(c) for c in cmd], cwd=cwd, check=True)


def wheel(target: str, out: Path) -> Path:
    lib = library_path(target)
    if not lib.exists():
        raise SystemExit(f"no library at {lib}; build abi/ for {target} first")
    native = ROOT / "python" / "sysml" / "_native"
    staging = out / "wheel-staging"
    shutil.rmtree(staging, ignore_errors=True)
    native.mkdir(exist_ok=True)
    try:
        shutil.copy2(lib, native / lib.name)
        third_party_notices.write(target, native)
        run(sys.executable, "-m", "build", "--wheel", "--outdir", staging)
    finally:
        shutil.rmtree(native, ignore_errors=True)
        shutil.rmtree(ROOT / "build", ignore_errors=True)
    [built] = staging.glob("*.whl")
    run(sys.executable, "-m", "wheel", "tags", "--python-tag", "py3", "--abi-tag", "none",
        "--platform-tag", PLATFORM_TAGS[target], "--remove", built)
    [tagged] = staging.glob("*.whl")
    dest = out / tagged.name
    shutil.move(tagged, dest)
    shutil.rmtree(staging)
    return dest


def sdist(out: Path) -> Path:
    staging = out / "sdist-staging"
    shutil.rmtree(staging, ignore_errors=True)
    run(sys.executable, "-m", "build", "--sdist", "--outdir", staging)
    [built] = staging.glob("*.tar.gz")
    dest = out / built.name
    shutil.move(built, dest)
    shutil.rmtree(staging)
    return dest


def npm(out: Path) -> Path:
    wasm = library_path("wasm32-unknown-unknown")
    if not wasm.exists():
        raise SystemExit(f"no WebAssembly build at {wasm}; build abi/ for wasm32-unknown-unknown first")
    ts = ROOT / "ts"
    copies = [ts / "sysmlv2_abi.wasm", ts / "LICENSE", ts / "NOTICE"]
    try:
        shutil.copy2(wasm, copies[0])
        shutil.copy2(ROOT / "LICENSE", copies[1])
        shutil.copy2(ROOT / "NOTICE", copies[2])
        copies += third_party_notices.write("wasm32-unknown-unknown", ts)
        before = set(out.glob("*.tgz"))
        npm_cmd = "npm.cmd" if os.name == "nt" else "npm"
        run(npm_cmd, "pack", "--pack-destination", out.resolve(), cwd=ts)
    finally:
        for c in copies:
            c.unlink(missing_ok=True)
    [made] = set(out.glob("*.tgz")) - before
    return made


def _zip_tree(z: zipfile.ZipFile, src: Path, prefix: str) -> None:
    for f in sorted(src.rglob("*")):
        if f.is_file():
            z.write(f, prefix + f.relative_to(src).as_posix())


def java(out: Path, javac22: str | None, archives: Path | None = None) -> list[Path]:
    src_root = ROOT / "java" / "src"
    sources = sorted(p for p in JAVA_SRC.rglob("*.java") if "checks" not in p.relative_to(JAVA_SRC).parts)
    work = out / "java-staging"
    shutil.rmtree(work, ignore_errors=True)
    classes, classes22 = work / "classes", work / "classes22"
    classes.mkdir(parents=True)
    argfile = work / "sources.txt"
    argfile.write_text("\n".join(f'"{p.as_posix()}"' for p in sources), encoding="utf-8")
    run("javac", "--enable-preview", "--release", "21", "-d", classes, f"@{argfile}")
    toolkit_backend = JAVA_SRC / "ToolkitBackend.java"
    if javac22:
        classes22.mkdir()
        run(javac22, "--release", "22", "-cp", classes, "-d", classes22, toolkit_backend)
    manifest = [
        "Manifest-Version: 1.0",
        f"Automatic-Module-Name: {JAVA_PACKAGE}",
        f"Implementation-Title: {JAVA_PACKAGE}",
        f"Implementation-Version: {VERSION}",
    ]
    if javac22:
        manifest.append("Multi-Release: true")
    jar = out / f"sysml-sdk-{VERSION}.jar"
    with zipfile.ZipFile(jar, "w", zipfile.ZIP_DEFLATED) as z:
        z.writestr("META-INF/MANIFEST.MF", "\r\n".join(manifest) + "\r\n")
        z.write(ROOT / "LICENSE", "META-INF/LICENSE")
        z.write(ROOT / "NOTICE", "META-INF/NOTICE")
        _zip_tree(z, classes, "")
        if javac22:
            # Only the SysML Toolkit backend differs; the rest of the jar serves every JDK.
            pkg_dir = Path(JAVA_PACKAGE.replace(".", "/"))
            for f in sorted((classes22 / pkg_dir).glob("ToolkitBackend*.class")):
                z.write(f, "META-INF/versions/22/" + f.relative_to(classes22).as_posix())
        # Beside the SysML Toolkit backend's class, which reads them as resources relative to itself.
        package_path = JAVA_PACKAGE.replace(".", "/")
        for target, files in natives(archives).items():
            platform = NATIVE_TARGETS[target][0]
            for name, data in files.items():
                z.writestr(f"{package_path}/native/{platform}/{name}", data)
    sources_jar = out / f"sysml-sdk-{VERSION}-sources.jar"
    with zipfile.ZipFile(sources_jar, "w", zipfile.ZIP_DEFLATED) as z:
        z.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\r\n")
        z.write(ROOT / "LICENSE", "META-INF/LICENSE")
        z.write(ROOT / "NOTICE", "META-INF/NOTICE")
        for f in sources:
            z.write(f, f.relative_to(src_root).as_posix())
    shutil.rmtree(work)
    return [jar, sources_jar]


def csharp(out: Path, archives: Path | None = None) -> Path:
    projects = [p for p in (ROOT / "csharp").glob("*/*.csproj") if not p.stem.endswith(".Checks")]
    [project] = projects
    # Staged beside the project, which packs runtimes/ and THIRD-PARTY/ when they exist.
    staged = [project.parent / "runtimes", project.parent / "THIRD-PARTY"]
    for d in staged:
        shutil.rmtree(d, ignore_errors=True)
    try:
        for target, files in natives(archives).items():
            rid = NATIVE_TARGETS[target][1]
            for name, data in files.items():
                dest = (staged[0] / rid / "native" if name not in NOTICES else staged[1] / rid) / name
                dest.parent.mkdir(parents=True, exist_ok=True)
                dest.write_bytes(data)
        before = set(out.glob("*.nupkg"))
        run("dotnet", "pack", project, "-c", "Release", "-o", out.resolve())
        [made] = set(out.glob("*.nupkg")) - before
    finally:
        for d in staged:
            shutil.rmtree(d, ignore_errors=True)
    return made


CPP_README = """sysml-sdk for C++ {version}

Header-only, C++17. Put include/ on the include path:

    g++ -std=c++17 -I include your_program.cpp

    #include <sysml/classes.g.hpp>    the metaclass structs, with the runtime and the payload backend
    #include <sysml/toolkit.hpp>       the SysML Toolkit backend (loads the binding library at run time)

(<sysml/sdk.hpp> alone is the runtime and the payload backend, without the metaclass structs.)

The SysML Toolkit backend needs the binding library of the same release. lib/<target>/ holds it for
each platform: copy the one for yours beside your executable, or name it in SYSMLV2_ABI. On Linux,
link with -ldl.

License: Apache-2.0, see LICENSE and NOTICE. include/nlohmann/json.hpp is JSON for Modern C++ by Niels
Lohmann, MIT license, see THIRD-PARTY/nlohmann-json-LICENSE.MIT.
"""


def cpp(out: Path, archives: Path | None = None) -> Path:
    stem = f"sysml-sdk-cpp-{VERSION}"
    dest = out / f"{stem}.zip"
    with zipfile.ZipFile(dest, "w", zipfile.ZIP_DEFLATED) as z:
        for f in sorted((ROOT / "cpp" / "include" / "sysml").glob("*.hpp")):
            z.write(f, f"{stem}/include/sysml/{f.name}")
        z.write(ROOT / "vendor" / "nlohmann" / "json.hpp", f"{stem}/include/nlohmann/json.hpp")
        z.write(ROOT / "LICENSE", f"{stem}/LICENSE")
        z.write(ROOT / "NOTICE", f"{stem}/NOTICE")
        z.write(ROOT / "vendor" / "nlohmann" / "LICENSE.MIT", f"{stem}/THIRD-PARTY/nlohmann-json-LICENSE.MIT")
        z.writestr(f"{stem}/README.txt", CPP_README.format(version=VERSION))
        for target, files in natives(archives).items():
            for name, data in files.items():
                z.writestr(f"{stem}/lib/{target}/{name}", data)
    return dest


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("what", choices=["python", "sdist", "npm", "java", "csharp", "cpp"])
    ap.add_argument("--target", help="Rust target triple of the native library (python)")
    ap.add_argument("--out", type=Path, default=ROOT / "dist")
    ap.add_argument("--javac22", help="a JDK 22 or newer javac, for the multi-release jar (java)")
    ap.add_argument("--abi-archives", type=Path,
                    help="the binding library archives to carry (java, csharp, cpp)")
    a = ap.parse_args()
    a.out.mkdir(parents=True, exist_ok=True)
    if a.what == "python":
        if not a.target:
            raise SystemExit("python needs --target")
        made = [wheel(a.target, a.out)]
    elif a.what == "sdist":
        made = [sdist(a.out)]
    elif a.what == "npm":
        made = [npm(a.out)]
    elif a.what == "java":
        made = java(a.out, a.javac22, a.abi_archives)
    elif a.what == "csharp":
        made = [csharp(a.out, a.abi_archives)]
    else:
        made = [cpp(a.out, a.abi_archives)]
    for m in made:
        print(m)


if __name__ == "__main__":
    main()
