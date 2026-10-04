"""Acceptance checks for the release packages: install each one somewhere empty and read the
example model through it.

Usage: python tools/check_packages.py <what> --dist DIR [--java-home DIR] [--cxx COMPILER] [--library-dir DIR] [--bundled]

  python   install the platform wheel from DIR into a new virtual environment; read the payload,
           and read through the SysML Toolkit with the library the wheel carries
  npm      install the tarball into an empty project; read the payload, and read through the
           SysML Toolkit with the WebAssembly module the package carries
  java     compile and run the Java tutorial with only the jar on the class path, on the JDK in
           --java-home (default: the one on PATH), payload and SysML Toolkit
  csharp   restore the NuGet package into a new project from DIR alone, read the payload and the
           SysML Toolkit
  cpp      compile the C++ tutorial against the extracted header archive alone, payload and
           SysML Toolkit

The SysML Toolkit reads of java, csharp and cpp take the native library from SYSMLV2_ABI, else from
this repository's build. With --bundled they take the one the package carries for this machine,
with SYSMLV2_ABI unset, as a user of the release does: the jar's and the NuGet package's own, and for
C++ the archive's lib/<target>/ library copied beside each program. The payload reads need the
generated example JSON: the vehicle's export and the standard library as JSON (python
tools/export_example.py). Every check runs outside the repository.

Each check then runs its language's guide (examples/<language>/README.md) against the installed
package: the code block marked `<!-- test: toolkit -->` and the one marked `<!-- test: payload -->`
are complete programs, and every block marked `<!-- test: queries -->` (or `queries toolkit`,
`queries payload`) goes into them where the line `... your code here ...` is. The programs run in a
directory that holds the files the guides name: the example model as model.sysml, its export as
model.json, the library JSON as sysml.library.full.json, and the standard library as sysml.library
(from --library-dir, else from a sysml-toolkit checkout beside this repository, else empty).
"""

from __future__ import annotations

import argparse
import glob
import os
import platform
import re
import shutil
import subprocess
import sys
import tempfile
import textwrap
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from naming import CS_NAMESPACE, JAVA_PACKAGE, NPM_PACKAGE, PYTHON_MODULE, S  # noqa: E402

MODEL = (ROOT / "examples" / "vehicle.sysml").as_posix()
PAYLOAD = (ROOT / "examples" / "vehicle.full.json").as_posix()
LIBRARY_JSON = (ROOT / "examples" / "sysml.library.full.json").as_posix()
# What the checks read: answered by every SysML Toolkit release, and the same in a payload.
SPORTS_CAR = "spoiler, topSpeed | Vehicles::Vehicle"
FIRST_LINE = "Vehicles::Vehicle is a PartDefinition"  # the tutorials' first step
NATIVE = {"win32": "sysmlv2_abi.dll", "darwin": "libsysmlv2_abi.dylib"}.get(sys.platform, "libsysmlv2_abi.so")


def run(cmd: list, cwd: Path, env: dict | None = None) -> str:
    print("+", " ".join(str(c) for c in cmd), flush=True)
    r = subprocess.run([str(c) for c in cmd], cwd=cwd, env=env, capture_output=True, text=True)
    if r.returncode != 0:
        sys.exit(f"failed ({r.returncode}):\n{r.stdout}\n{r.stderr}")
    return r.stdout


def expect(output: str, needle: str, what: str) -> None:
    if needle not in output:
        sys.exit(f"{what}: expected {needle!r} in:\n{output}")
    print(f"ok: {what}")


BUNDLED = False  # --bundled: the package's own binding library, SYSMLV2_ABI unset


def platform_key() -> str:
    """This machine as the jar names its library directories: windows-x86_64, linux-x86_64,
    macos-aarch64, macos-x86_64."""
    arch = "aarch64" if platform.machine().lower() in ("arm64", "aarch64") else "x86_64"
    return {"win32": "windows", "darwin": "macos"}.get(sys.platform, "linux") + "-" + arch


RIDS = {"windows-x86_64": "win-x64", "linux-x86_64": "linux-x64", "macos-aarch64": "osx-arm64", "macos-x86_64": "osx-x64"}


def native_env() -> dict:
    if BUNDLED:
        return {k: v for k, v in os.environ.items() if k != "SYSMLV2_ABI"}
    env = dict(os.environ)
    if not env.get("SYSMLV2_ABI"):
        env["SYSMLV2_ABI"] = str(ROOT / "abi" / "target" / "release" / NATIVE)
    if not Path(env["SYSMLV2_ABI"]).exists():
        sys.exit(f"no binding library at {env['SYSMLV2_ABI']}; set SYSMLV2_ABI")
    return env


def one(dist: Path, pattern: str) -> Path:
    found = sorted(dist.glob(pattern))
    if len(found) != 1:
        sys.exit(f"expected one {pattern} in {dist}, found {[f.name for f in found]}")
    return found[0]


GUIDE_BLOCK = re.compile(r"<!-- test: (toolkit|payload|queries)(?: (toolkit|payload))? -->\n```[a-z]*\n(.*?)\n```", re.S)
PLACEHOLDER = re.compile(r"^([ \t]*)(?:#|//) \.\.\. your code here \.\.\.$", re.M)
DEFAULT_LIBRARY = ROOT.parent / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library"


def guide_programs(language: str) -> dict[str, str]:
    """The guide's SysML Toolkit and payload programs, each with the query blocks that apply to it
    in place of its placeholder line, indented as that line is."""
    text = (ROOT / "examples" / language / "README.md").read_text(encoding="utf-8").replace("\r\n", "\n")
    loaders, queries = {}, []
    for kind, only, code in GUIDE_BLOCK.findall(text):
        if kind == "queries":
            queries.append((only or None, code))
        else:
            loaders[kind] = code
    if set(loaders) != {"toolkit", "payload"} or not queries:
        sys.exit(f"examples/{language}/README.md: expected a SysML Toolkit and a payload program and query blocks")
    programs = {}
    for kind, code in loaders.items():
        m = PLACEHOLDER.search(code)
        if not m:
            sys.exit(f"examples/{language}/README.md: the {kind} program has no '... your code here ...' line")
        body = "\n\n".join(textwrap.indent(q, m.group(1)) for only, q in queries if only in (None, kind))
        programs[kind] = code[:m.start()] + body + code[m.end():] + "\n"
    return programs


def guide_files(work: Path, library_dir: Path | None) -> None:
    """The files the guides name, in the directory their programs run in."""
    shutil.copyfile(MODEL, work / "model.sysml")
    shutil.copyfile(PAYLOAD, work / "model.json")
    shutil.copyfile(LIBRARY_JSON, work / "sysml.library.full.json")
    if library_dir is None and DEFAULT_LIBRARY.is_dir():
        library_dir = DEFAULT_LIBRARY
    if library_dir is not None:
        shutil.copytree(library_dir, work / "sysml.library")
    else:
        (work / "sysml.library").mkdir()
        print("note: no standard library directory; the guides' SysML Toolkit programs read an empty one")


def guide_ran(language: str, kind: str, output: str) -> None:
    print(f"ok: the {language} guide's {kind} program\n" + textwrap.indent(output.rstrip(), "    | "))


def check_python(dist: Path, work: Path, library_dir: Path | None) -> None:
    run([sys.executable, "-m", "venv", work / "venv"], work)
    py = work / "venv" / ("Scripts/python.exe" if os.name == "nt" else "bin/python")
    run([py, "-m", "pip", "install", "--quiet", "--no-index", "--find-links", dist, S["python_dist"]], work)
    env = {k: v for k, v in os.environ.items() if k != "SYSMLV2_ABI"}  # the wheel's own library
    out = run([py, "-c", f"""
import json
from {PYTHON_MODULE} import Model, PayloadLibrary
from {PYTHON_MODULE}.toolkit import _default_library_path
assert "_native" in _default_library_path(), _default_library_path()
from pathlib import Path
for notice in ("THIRD-PARTY-NOTICES.txt", "RUST-STANDARD-LIBRARY-NOTICES.html"):
    assert (Path(_default_library_path()).parent / notice).is_file(), notice
library = PayloadLibrary(json.load(open({LIBRARY_JSON!r}, encoding="utf-8")))
p = Model.from_full_json(json.load(open({PAYLOAD!r}, encoding="utf-8")), library=library).resolve("Vehicles::SportsCar")
print("payload owns", ", ".join(f.declaredName for f in p.ownedFeature), "|", ", ".join(g.superclassifier.qualifiedName for g in p.ownedSubclassification))
s = Model.from_toolkit({MODEL!r}).resolve("Vehicles::SportsCar")
print("toolkit owns", ", ".join(f.declaredName for f in s.ownedFeature), "|", ", ".join(g.superclassifier.qualifiedName for g in s.ownedSubclassification), "|", s.effectiveName())
from {PYTHON_MODULE} import standard_library
from importlib.metadata import distribution
lib = standard_library()
assert (lib / "LICENSE").is_file() and (lib / "NOTICE").is_file(), lib
x = Model.from_toolkit(sources={{"x.sysml": "package P {{ attribute x : ScalarValues::Real; }}"}}, library_dir=lib).resolve("P::x")
print("bundled library types x as", x.ownedTyping[0].type.qualifiedName)
d = distribution({S["python_dist"]!r})
print("wheel:", d.read_text("WHEEL").split("Root-Is-Purelib: ")[1].split()[0], "|", d.metadata["License-Expression"])
"""], work, env)
    expect(out, f"payload owns {SPORTS_CAR}", "Python wheel, payload with the library JSON")
    expect(out, f"toolkit owns {SPORTS_CAR} | SportsCar", "Python wheel, SysML Toolkit read with the packaged library")
    expect(out, "bundled library types x as ScalarValues::Real", "Python wheel, the standard library models it carries")
    expect(out, "wheel: false | Apache-2.0 AND EPL-2.0", "Python wheel, a platform wheel with the library's license")
    guide_files(work, library_dir)
    for kind, program in guide_programs("python").items():
        (work / f"guide_{kind}.py").write_text(program, encoding="utf-8")
        guide_ran("Python", kind, run([py, f"guide_{kind}.py"], work, env))


def check_npm(dist: Path, work: Path, library_dir: Path | None) -> None:
    npm = "npm.cmd" if os.name == "nt" else "npm"
    tgz = one(dist, f"{S['npm_tarball_stem']}-[0-9]*.tgz")
    run([npm, "init", "-y"], work)
    run([npm, "install", "--silent", tgz], work)
    name = NPM_PACKAGE
    for notice in ("THIRD-PARTY-NOTICES.txt", "RUST-STANDARD-LIBRARY-NOTICES.html"):
        if not (work / "node_modules" / name / notice).is_file():
            sys.exit(f"npm package: no {notice}")
    (work / "check.mjs").write_text(f"""
import {{ readFileSync }} from "node:fs";
import {{ ToolkitBackend, Model, PayloadLibrary }} from "{name}";
const library = new PayloadLibrary(JSON.parse(readFileSync({LIBRARY_JSON!r}, "utf8")));
const p = Model.fromFullJson(JSON.parse(readFileSync({PAYLOAD!r}, "utf8")), {{ library }}).resolve("Vehicles::SportsCar");
console.log("payload owns", p.ownedFeature.map((f) => f.declaredName).join(", "), "|", p.ownedSubclassification.map((g) => g.superclassifier.qualifiedName).join(", "));
const text = readFileSync({MODEL!r}, "utf8");
const s = new Model(await ToolkitBackend.open({{ sources: {{ "vehicle.sysml": text }} }})).resolve("Vehicles::SportsCar");
console.log("toolkit owns", s.ownedFeature.map((f) => f.declaredName).join(", "), "|", s.ownedSubclassification.map((g) => g.superclassifier.qualifiedName).join(", "), "|", s.effectiveName());
const {{ standardLibrary }} = await import("{name}");
const lib = await standardLibrary();
const x = new Model(await ToolkitBackend.open({{ sources: {{ "x.sysml": "package P {{ attribute x : ScalarValues::Real; }}" }}, libraryDir: lib }})).resolve("P::x");
console.log("bundled library types x as", x.ownedTyping[0].type.qualifiedName);
const pkg = JSON.parse(readFileSync(new URL("./node_modules/{name}/package.json", import.meta.url), "utf8"));
console.log("package license:", pkg.license);
""", encoding="utf-8")
    env = {k: v for k, v in os.environ.items() if k != "SYSMLV2_ABI_WASM"}  # the package's own module
    out = run(["node", "check.mjs"], work, env)
    expect(out, f"payload owns {SPORTS_CAR}", "npm package, payload with the library JSON")
    expect(out, f"toolkit owns {SPORTS_CAR} | SportsCar", "npm package, SysML Toolkit read with the packaged module")
    expect(out, "bundled library types x as ScalarValues::Real", "npm package, the standard library models it carries")
    expect(out, "package license: (Apache-2.0 AND EPL-2.0)", "npm package, the library's license declared")
    for notice in ("LICENSE", "NOTICE"):
        if not (work / "node_modules" / name / "stdlib" / notice).is_file():
            sys.exit(f"npm package: no stdlib/{notice}")
    guide_files(work, library_dir)
    for kind, program in guide_programs("typescript").items():
        (work / f"guide_{kind}.mjs").write_text(program, encoding="utf-8")
        guide_ran("JavaScript", kind, run(["node", f"guide_{kind}.mjs"], work, env))


def check_java(dist: Path, work: Path, java_home: str | None, library_dir: Path | None) -> None:
    jar = one(dist, f"{S['product']}-[0-9]*[0-9].jar")
    if BUNDLED:
        entry = f"{JAVA_PACKAGE.replace('.', '/')}/native/{platform_key()}/{NATIVE}"
        with zipfile.ZipFile(jar) as z:
            if entry not in z.namelist():
                sys.exit(f"the jar carries no {entry}")
        print(f"ok: the jar carries {entry}")
    bin_ = (Path(java_home) / "bin") if java_home else None
    javac = str(bin_ / "javac") if bin_ else "javac"
    java = str(bin_ / "java") if bin_ else "java"
    version = subprocess.run([java, "-version"], capture_output=True, text=True).stderr
    preview = ['--enable-preview'] if '"21' in version else []
    release = "21" if preview else "22"
    out_dir = work / "classes"
    run([javac, *preview, "--release", release, "-cp", jar, "-d", out_dir, ROOT / "examples" / "java" / "Tutorial.java"], work)
    cp = os.pathsep.join([str(jar), str(out_dir)])
    # The tutorial reads examples/ relative to the working directory.
    payload = run([java, *preview, "-cp", cp, "Tutorial"], ROOT)
    expect(payload, FIRST_LINE, f"Java jar on {version.splitlines()[0]}, payload")
    toolkit = run([java, *preview, "--enable-native-access=ALL-UNNAMED", "-cp", cp, "Tutorial", "--toolkit"], ROOT, native_env())
    expect(toolkit, FIRST_LINE, f"Java jar on {version.splitlines()[0]}, SysML Toolkit")
    lib_src = work / "libcheck" / "LibCheck.java"
    lib_src.parent.mkdir()
    lib_src.write_text(f"""
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import {JAVA_PACKAGE}.*;
import {JAVA_PACKAGE}.classes.Feature;

public class LibCheck {{
    public static void main(String[] args) {{
        var lib = StandardLibrary.directory();
        if (!Files.isRegularFile(lib.resolve("LICENSE")) || !Files.isRegularFile(lib.resolve("NOTICE")))
            throw new AssertionError("no LICENSE or NOTICE in " + lib);
        Model m = new Model(ToolkitBackend.open(ToolkitBackend.library(), List.of(),
            Map.of("x.sysml", "package P {{ attribute x : ScalarValues::Real; }}"), lib.toString()));
        Feature x = (Feature) m.resolve("P::x");
        System.out.println("bundled library types x as " + x.getOwnedTyping().get(0).getType().getQualifiedName());
    }}
}}
""", encoding="utf-8")
    run([javac, *preview, "--release", release, "-cp", jar, "-d", lib_src.parent, lib_src], work)
    lib_out = run([java, *preview, "--enable-native-access=ALL-UNNAMED", "-cp",
                   os.pathsep.join([str(jar), str(lib_src.parent)]), "LibCheck"], work, native_env())
    expect(lib_out, "bundled library types x as ScalarValues::Real", "Java jar, the standard library models it carries")
    guide_files(work, library_dir)
    for kind, program in guide_programs("java").items():
        main_class = re.search(r"public class (\w+)", program).group(1)
        src = work / f"guide-{kind}" / f"{main_class}.java"
        src.parent.mkdir()
        src.write_text(program, encoding="utf-8")
        run([javac, *preview, "--release", release, "-cp", jar, "-d", src.parent, src], work)
        guide_cp = os.pathsep.join([str(jar), str(src.parent)])
        guide_ran(f"Java ({version.splitlines()[0]})", kind,
                  run([java, *preview, "--enable-native-access=ALL-UNNAMED", "-cp", guide_cp, main_class], work, native_env()))


def check_csharp(dist: Path, work: Path, library_dir: Path | None) -> None:
    nupkg = one(dist, "*.nupkg")
    if BUNDLED:
        entry = f"runtimes/{RIDS[platform_key()]}/native/{NATIVE}"
        with zipfile.ZipFile(nupkg) as z:
            if entry not in z.namelist():
                sys.exit(f"the NuGet package carries no {entry}")
        print(f"ok: the NuGet package carries {entry}")
    env = dict(native_env(), NUGET_PACKAGES=str(work / "nuget"))  # keep the user's cache untouched
    run(["dotnet", "new", "console", "--force", "-o", work / "app"], work, env)
    version = nupkg.name[len(CS_NAMESPACE) + 1:-len(".nupkg")]
    run(["dotnet", "add", "package", CS_NAMESPACE, "--version", version, "--source", dist], work / "app", env)
    (work / "app" / "Program.cs").write_text(f"""using {CS_NAMESPACE};
var library = PayloadLibrary.FromJson(File.ReadAllText(@"{LIBRARY_JSON}"));
var payload = Model.FromFullJson(File.ReadAllText(@"{PAYLOAD}"), library);
var fromPayload = (PartDefinition)payload.Resolve("Vehicles::SportsCar")!;
Console.WriteLine("payload owns " + string.Join(", ", fromPayload.ownedFeature.Select(f => f.declaredName)) + " | " + string.Join(", ", fromPayload.ownedSubclassification.Select(g => g.superclassifier!.qualifiedName)));
using var be = ToolkitBackend.Open(@"{MODEL}");
var fromToolkit = (PartDefinition)new Model(be).Resolve("Vehicles::SportsCar")!;
Console.WriteLine("toolkit owns " + string.Join(", ", fromToolkit.ownedFeature.Select(f => f.declaredName)) + " | " + string.Join(", ", fromToolkit.ownedSubclassification.Select(g => g.superclassifier!.qualifiedName)) + " | " + fromToolkit.effectiveName());
var lib = StandardLibrary.Directory();
if (!File.Exists(Path.Combine(lib, "LICENSE")) || !File.Exists(Path.Combine(lib, "NOTICE"))) throw new Exception("no LICENSE or NOTICE in " + lib);
using var withLibrary = ToolkitBackend.Open(ToolkitBackend.DefaultLibrary, Array.Empty<string>(),
    new Dictionary<string, string> {{ ["x.sysml"] = "package P {{ attribute x : ScalarValues::Real; }}" }}, lib);
var x = (Feature)new Model(withLibrary).Resolve("P::x")!;
Console.WriteLine("bundled library types x as " + x.ownedTyping[0].type!.qualifiedName);
""", encoding="utf-8")
    out = run(["dotnet", "run"], work / "app", env)
    expect(out, f"payload owns {SPORTS_CAR}", "NuGet package, payload")
    expect(out, f"toolkit owns {SPORTS_CAR} | SportsCar", "NuGet package, SysML Toolkit")
    expect(out, "bundled library types x as ScalarValues::Real", "NuGet package, the standard library models it carries")
    with zipfile.ZipFile(nupkg) as z:
        nuspec = z.read(next(n for n in z.namelist() if n.endswith(".nuspec"))).decode("utf-8")
    if "Apache-2.0 AND EPL-2.0" not in nuspec:
        sys.exit("the NuGet package does not declare the library's license (Apache-2.0 AND EPL-2.0)")
    print("ok: the NuGet package declares Apache-2.0 AND EPL-2.0")
    guide_files(work, library_dir)
    for kind, program in guide_programs("csharp").items():
        project = work / f"guide-{kind}"
        run(["dotnet", "new", "console", "--force", "-o", project], work, env)
        run(["dotnet", "add", "package", CS_NAMESPACE, "--version", version, "--source", dist], project, env)
        (project / "Program.cs").write_text(program, encoding="utf-8")
        guide_ran("C#", kind, run(["dotnet", "run", "--project", project], work, env))


def check_cpp(dist: Path, work: Path, cxx: str, library_dir: Path | None) -> None:
    archive = one(dist, f"{S['product']}-cpp-*.zip")
    with zipfile.ZipFile(archive) as z:
        z.extractall(work)
    [include] = glob.glob(str(work / "*" / "include"))
    carried = None
    if BUNDLED:
        key = platform_key()
        os_part = {"windows": "windows", "linux": "linux", "macos": "apple-darwin"}[key.split("-")[0]]
        found = [Path(d) / NATIVE for d in glob.glob(str(work / "*" / "lib" / "*"))
                 if os_part in Path(d).name and Path(d).name.startswith(key.split("-")[1])]
        if len(found) != 1 or not found[0].is_file():
            sys.exit(f"the C++ archive carries no library for {key}: {found}")
        carried = found[0]
        print(f"ok: the C++ archive carries {carried.relative_to(work).as_posix()}")
    exe = work / ("tutorial.exe" if os.name == "nt" else "tutorial")
    flags = ["-static"] if os.name == "nt" else (["-ldl"] if sys.platform.startswith("linux") else [])
    run([cxx, "-std=c++17", "-Wall", "-Werror", "-I", include, ROOT / "examples" / "cpp" / "tutorial.cpp", "-o", exe, *flags], work)
    if carried:
        shutil.copy2(carried, work / NATIVE)  # beside the programs, which are all built in work
    expect(run([exe], ROOT), FIRST_LINE, "C++ headers, payload")
    expect(run([exe, "--toolkit"], ROOT, native_env()), FIRST_LINE, "C++ headers, SysML Toolkit")
    # The archive's sysml.library, copied beside a program as its README says, found by standard_library().
    [archived] = glob.glob(str(work / "*" / "sysml.library"))
    for name in ("LICENSE", "NOTICE"):
        if not (Path(archived) / name).is_file():
            sys.exit(f"the C++ archive's sysml.library has no {name}")
    libcheck = work / "libcheck"
    shutil.copytree(archived, libcheck / "sysml.library")
    if carried:
        shutil.copy2(carried, libcheck / NATIVE)
    (libcheck / "libcheck.cpp").write_text("""#include <iostream>
#include <sysml/classes.g.hpp>
#include <sysml/library.hpp>
using namespace sysml;
int main() {
    Model model = Model::from_backend(ToolkitBackend::open({}, {{"x.sysml", "package P { attribute x : ScalarValues::Real; }"}},
                                                           standard_library()));
    auto x = model.resolve("P::x").value().as<Feature>();
    std::cout << "bundled library types x as " << x.getOwnedTyping().at(0).getType().value().getQualifiedName().value_or("?") << "\\n";
}
""", encoding="utf-8")
    libcheck_exe = libcheck / ("libcheck.exe" if os.name == "nt" else "libcheck")
    run([cxx, "-std=c++17", "-Wall", "-Werror", "-I", include, libcheck / "libcheck.cpp", "-o", libcheck_exe, *flags], work)
    expect(run([libcheck_exe], work, native_env()), "bundled library types x as ScalarValues::Real",
           "C++ archive, the standard library models it carries, beside the program")
    guide_files(work, library_dir)
    for kind, program in guide_programs("cpp").items():
        src = work / f"guide_{kind}.cpp"
        src.write_text(program, encoding="utf-8")
        guide_exe = work / (f"guide_{kind}.exe" if os.name == "nt" else f"guide_{kind}")
        run([cxx, "-std=c++17", "-Wall", "-Werror", "-I", include, src, "-o", guide_exe, *flags], work)
        guide_ran("C++", kind, run([guide_exe], work, native_env()))


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("what", choices=["python", "npm", "java", "csharp", "cpp"])
    ap.add_argument("--dist", type=Path, required=True)
    ap.add_argument("--java-home")
    ap.add_argument("--cxx", default="c++")
    ap.add_argument("--library-dir", type=Path, help="the standard library directory the guides read")
    ap.add_argument("--bundled", action="store_true",
                    help="java, csharp, cpp: the package's own binding library, SYSMLV2_ABI unset")
    a = ap.parse_args()
    global BUNDLED
    BUNDLED = a.bundled
    dist = a.dist.resolve()
    work = Path(tempfile.mkdtemp(prefix=f"sysml-check-{a.what}-"))
    # The standard library helpers: their cache inside the check (the models the jar and the NuGet
    # package copy out land there, not in the user's cache), and the library JSON the guides' payload
    # programs ask for, the copy the check puts beside them (no download).
    os.environ["SYSML_CACHE_DIR"] = str(work / "cache")
    os.environ["SYSML_LIBRARY_JSON"] = str(work / "sysml.library.full.json")
    os.environ.pop("SYSML_LIBRARY_DIR", None)
    try:
        if a.what == "python":
            check_python(dist, work, a.library_dir)
        elif a.what == "npm":
            check_npm(dist, work, a.library_dir)
        elif a.what == "java":
            check_java(dist, work, a.java_home, a.library_dir)
        elif a.what == "csharp":
            check_csharp(dist, work, a.library_dir)
        else:
            check_cpp(dist, work, a.cxx, a.library_dir)
    finally:
        shutil.rmtree(work, ignore_errors=True)


if __name__ == "__main__":
    main()
