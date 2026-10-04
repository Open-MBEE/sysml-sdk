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

ROOT = Path(__file__).absolute().parent.parent
sys.path.insert(0, str(Path(__file__).absolute().parent))

from naming import JAVA_PACKAGE, JAVA_SRC  # noqa: E402
from package_abi import library_path  # noqa: E402
import package_library  # noqa: E402
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


def wheel(target: str, out: Path, library_dir: Path | None) -> Path:
    lib = library_path(target)
    if not lib.exists():
        raise SystemExit(f"no library at {lib}; build abi/ for {target} first")
    if library_dir is None:
        print("note: no --library-dir; the wheel carries no standard library models")
    native = ROOT / "python" / "sysml" / "_native"
    stdlib = ROOT / "python" / "sysml" / "stdlib"
    staging = out / "wheel-staging"
    shutil.rmtree(staging, ignore_errors=True)
    shutil.rmtree(stdlib, ignore_errors=True)
    native.mkdir(exist_ok=True)
    try:
        shutil.copy2(lib, native / lib.name)
        third_party_notices.write(target, native)
        if library_dir is not None:
            package_library.write_models(library_dir, stdlib)
        run(sys.executable, "-m", "build", "--wheel", "--outdir", staging)
    finally:
        shutil.rmtree(native, ignore_errors=True)
        shutil.rmtree(stdlib, ignore_errors=True)
        shutil.rmtree(ROOT / "build", ignore_errors=True)
    [built] = staging.glob("*.whl")
    run(sys.executable, "-m", "wheel", "tags", "--python-tag", "py3", "--abi-tag", "none",
        "--platform-tag", PLATFORM_TAGS[target], "--remove", built)
    [tagged] = staging.glob("*.whl")
    platform_wheel(tagged, library_dir is not None)
    [tagged] = staging.glob("*.whl")
    dest = out / tagged.name
    shutil.move(tagged, dest)
    shutil.rmtree(staging)
    return dest


def platform_wheel(whl: Path, carries_library: bool) -> None:
    """Rewrite the wheel setuptools made (it sees no extension module, so it calls the wheel pure) as
    the platform wheel it is: `Root-Is-Purelib: false`. When it carries the standard library models,
    its license is `Apache-2.0 AND EPL-2.0`, and their LICENSE and NOTICE are license files of the
    distribution too (under stdlib/ in its .dist-info/licenses)."""
    unpacked = whl.parent / "unpacked"
    shutil.rmtree(unpacked, ignore_errors=True)
    run(sys.executable, "-m", "wheel", "unpack", "--dest", unpacked, whl)
    [root] = unpacked.iterdir()
    [info] = root.glob("*.dist-info")
    wheel_file = info / "WHEEL"
    text = wheel_file.read_text(encoding="utf-8")
    if "Root-Is-Purelib: true" not in text:
        raise SystemExit(f"{whl.name}: no 'Root-Is-Purelib: true' in WHEEL to correct")
    wheel_file.write_text(text.replace("Root-Is-Purelib: true", "Root-Is-Purelib: false"), encoding="utf-8",
                          newline="\n")
    if carries_library:
        metadata = info / "METADATA"
        head, sep, body = metadata.read_text(encoding="utf-8").partition("\n\n")
        if "\nLicense-Expression: Apache-2.0\n" not in head + "\n":
            raise SystemExit(f"{whl.name}: METADATA does not say License-Expression: Apache-2.0")
        head = head.replace("License-Expression: Apache-2.0", "License-Expression: Apache-2.0 AND EPL-2.0")
        licenses = info / "licenses" / "stdlib"
        licenses.mkdir(parents=True, exist_ok=True)
        for name in ("LICENSE", "NOTICE"):
            shutil.copy2(root / "sysml" / "stdlib" / name, licenses / name)
            head += f"\nLicense-File: stdlib/{name}"
        metadata.write_text(head + sep + body, encoding="utf-8", newline="\n")
    whl.unlink()
    run(sys.executable, "-m", "wheel", "pack", "--dest-dir", whl.parent, root)
    shutil.rmtree(unpacked)


def sdist(out: Path) -> Path:
    staging = out / "sdist-staging"
    shutil.rmtree(staging, ignore_errors=True)
    run(sys.executable, "-m", "build", "--sdist", "--outdir", staging)
    [built] = staging.glob("*.tar.gz")
    dest = out / built.name
    shutil.move(built, dest)
    shutil.rmtree(staging)
    return dest


def npm(out: Path, library_dir: Path | None) -> Path:
    wasm = library_path("wasm32-unknown-unknown")
    if not wasm.exists():
        raise SystemExit(f"no WebAssembly build at {wasm}; build abi/ for wasm32-unknown-unknown first")
    if library_dir is None:
        print("note: no --library-dir; the npm package carries no standard library models")
    ts = ROOT / "ts"
    stdlib = ts / "stdlib"
    copies = [ts / "sysmlv2_abi.wasm", ts / "LICENSE", ts / "NOTICE"]
    shutil.rmtree(stdlib, ignore_errors=True)
    try:
        shutil.copy2(wasm, copies[0])
        shutil.copy2(ROOT / "LICENSE", copies[1])
        shutil.copy2(ROOT / "NOTICE", copies[2])
        copies += third_party_notices.write("wasm32-unknown-unknown", ts)
        if library_dir is not None:
            package_library.write_models(library_dir, stdlib)
        before = set(out.glob("*.tgz"))
        npm_cmd = "npm.cmd" if os.name == "nt" else "npm"
        run(npm_cmd, "pack", "--pack-destination", out.resolve(), cwd=ts)
    finally:
        for c in copies:
            c.unlink(missing_ok=True)
        shutil.rmtree(stdlib, ignore_errors=True)
    [made] = set(out.glob("*.tgz")) - before
    return made


def _zip_tree(z: zipfile.ZipFile, src: Path, prefix: str) -> None:
    for f in sorted(src.rglob("*")):
        if f.is_file():
            z.write(f, prefix + f.relative_to(src).as_posix())


def library_models(library_dir: Path | None, staging: Path, what: str) -> list[tuple[str, bytes]]:
    """The standard library models a package carries, as (path relative to the library, bytes), with
    their LICENSE and NOTICE and an INDEX listing every other file (for the languages that copy the
    models out of the package); none without `library_dir`."""
    if library_dir is None:
        print(f"note: no --library-dir; {what} carries no standard library models")
        return []
    shutil.rmtree(staging, ignore_errors=True)
    package_library.write_models(library_dir, staging)
    files = sorted(f.relative_to(staging).as_posix() for f in staging.rglob("*") if f.is_file())
    out = [(rel, (staging / rel).read_bytes()) for rel in files]
    shutil.rmtree(staging)
    return out + [("INDEX", "".join(f"{rel}\n" for rel in files).encode("utf-8"))]


def java(out: Path, javac22: str | None, archives: Path | None = None, library_dir: Path | None = None) -> list[Path]:
    src_root = JAVA_SRC.parents[JAVA_PACKAGE.count(".")]  # java/src, as naming spells it
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
        # The standard library models, with an index StandardLibrary reads to copy them out.
        for rel, data in library_models(library_dir, work / "stdlib", "the jar"):
            z.writestr(f"{package_path}/stdlib/{rel}", data)
    sources_jar = out / f"sysml-sdk-{VERSION}-sources.jar"
    with zipfile.ZipFile(sources_jar, "w", zipfile.ZIP_DEFLATED) as z:
        z.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\r\n")
        z.write(ROOT / "LICENSE", "META-INF/LICENSE")
        z.write(ROOT / "NOTICE", "META-INF/NOTICE")
        for f in sources:
            z.write(f, f.relative_to(src_root).as_posix())
    shutil.rmtree(work)
    return [jar, sources_jar]


def csharp(out: Path, archives: Path | None = None, library_dir: Path | None = None) -> Path:
    projects = [p for p in (ROOT / "csharp").glob("*/*.csproj") if not p.stem.endswith(".Checks")]
    [project] = projects
    # Staged beside the project, which packs runtimes/ and THIRD-PARTY/ and embeds stdlib/ when they exist.
    staged = [project.parent / "runtimes", project.parent / "THIRD-PARTY", project.parent / "stdlib"]
    for d in staged:
        shutil.rmtree(d, ignore_errors=True)
    try:
        if library_dir is not None:
            package_library.write_models(library_dir, staged[2])
        else:
            print("note: no --library-dir; the NuGet package carries no standard library models")
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
    #include <sysml/library.hpp>       the standard library: sysml::standard_library(), standard_library_json()

(<sysml/sdk.hpp> alone is the runtime and the payload backend, without the metaclass structs.)

The SysML Toolkit backend needs the binding library of the same release. lib/<target>/ holds it for
each platform: copy the one for yours beside your executable, or name it in SYSMLV2_ABI. On Linux,
link with -ldl.

The standard library: sysml.library/ holds its models. Copy that directory beside your executable
too (or name it in SYSML_LIBRARY_DIR); sysml::standard_library() from <sysml/library.hpp> finds it,
for the library directory of ToolkitBackend::open. sysml::standard_library_json() is the library as
JSON, for payloads: the file SYSML_LIBRARY_JSON names, or the copy another SDK language downloaded
into the user's cache (C++ downloads nothing; sysml_library-{version}.zip on the release has it).

License: Apache-2.0, see LICENSE and NOTICE. include/nlohmann/json.hpp is JSON for Modern C++ by Niels
Lohmann, MIT license, see THIRD-PARTY/nlohmann-json-LICENSE.MIT. sysml.library/ is the SysML v2
release's standard library, Eclipse Public License 2.0, see sysml.library/LICENSE and NOTICE.
"""


def cpp(out: Path, archives: Path | None = None, library_dir: Path | None = None) -> Path:
    stem = f"sysml-sdk-cpp-{VERSION}"
    dest = out / f"{stem}.zip"
    models = library_models(library_dir, out / "cpp-stdlib-staging", "the C++ archive")
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
        for rel, data in models:
            if rel != "INDEX":  # a directory to copy, not to read out of a package
                z.writestr(f"{stem}/sysml.library/{rel}", data)
    return dest


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("what", choices=["python", "sdist", "npm", "java", "csharp", "cpp"])
    ap.add_argument("--target", help="Rust target triple of the native library (python)")
    ap.add_argument("--out", type=Path, default=ROOT / "dist")
    ap.add_argument("--javac22", help="a JDK 22 or newer javac, for the multi-release jar (java)")
    ap.add_argument("--abi-archives", type=Path,
                    help="the binding library archives to carry (java, csharp, cpp)")
    ap.add_argument("--library-dir", type=Path,
                    help="the standard library's sysml.library directory, in a git checkout of the SysML v2 "
                         "release, whose models the package carries (python, npm, java, csharp, cpp)")
    a = ap.parse_args()
    a.out.mkdir(parents=True, exist_ok=True)
    if a.what == "python":
        if not a.target:
            raise SystemExit("python needs --target")
        made = [wheel(a.target, a.out, a.library_dir)]
    elif a.what == "sdist":
        made = [sdist(a.out)]
    elif a.what == "npm":
        made = [npm(a.out, a.library_dir)]
    elif a.what == "java":
        made = java(a.out, a.javac22, a.abi_archives, a.library_dir)
    elif a.what == "csharp":
        made = [csharp(a.out, a.abi_archives, a.library_dir)]
    else:
        made = [cpp(a.out, a.abi_archives, a.library_dir)]
    for m in made:
        print(m)


if __name__ == "__main__":
    main()
