# sysml-sdk

The SysML v2 SDK: read SysML v2 and KerML models from Python, JavaScript/TypeScript, Java, C# and
C++, through an API that has the shape of the OMG specification. Every class, property and
operation of the KerML and SysML metamodels is there under its specification name
(`ownedFeature`, `qualifiedName`, `effectiveName()`), and the generated classes form the metaclass
hierarchy, so `isinstance`, `instanceof` and their equivalents follow the specification's
generalizations.

A model is read through one of two backends, with the same classes over both:

- **the SysML Toolkit**: the OpenMBEE SysML Toolkit
  ([Open-MBEE/sysml-toolkit](https://github.com/Open-MBEE/sysml-toolkit), release `v0.10.0`),
  reached through this SDK's binding library. It reads `.sysml` and `.kerml` text, resolves names,
  computes derived properties and evaluates specification operations;
- **a payload**: a model exported as full-form interchange JSON by any SysML v2 tool, read with no
  native code at all.

The SDK never guesses. A property read returns what the SysML Toolkit computed or what the payload
holds. A member the SysML Toolkit does not answer raises `NotImplementedInToolkit` with the
SysML Toolkit's reason, and a reference that does not resolve raises `UnresolvedReference`; nothing
is silently empty.

This is release 0.1.0, a preview. The payload backend is the dependable path; the SysML Toolkit
backend is a preview of the SysML Toolkit's own coverage. What the SysML Toolkit does not answer yet
is listed under **Known limitations** in [CHANGELOG.md](CHANGELOG.md); [STATUS.md](STATUS.md) has
the numbers.

## Get started

Each guide shows how to install the package, the code that reads a model through the SysML Toolkit
and the code that reads a JSON export, ready to copy with your file names, and the first queries.
The code in the guides is run against the release packages before every release.

| Language | Guide | Runnable tutorial | Skill for coding agents |
|---|---|---|---|
| Python | [examples/python](examples/python/README.md) | [tutorial.py](examples/python/tutorial.py) | [sysml-sdk-python](skills/sysml-sdk-python/SKILL.md) |
| JavaScript / TypeScript | [examples/typescript](examples/typescript/README.md) | [tutorial.mjs](examples/typescript/tutorial.mjs) | [sysml-sdk-typescript](skills/sysml-sdk-typescript/SKILL.md) |
| Java | [examples/java](examples/java/README.md) | [Tutorial.java](examples/java/Tutorial.java) | [sysml-sdk-java](skills/sysml-sdk-java/SKILL.md) |
| C# | [examples/csharp](examples/csharp/README.md) | [Tutorial.cs](examples/csharp/Tutorial.cs) | [sysml-sdk-csharp](skills/sysml-sdk-csharp/SKILL.md) |
| C++ | [examples/cpp](examples/cpp/README.md) | [tutorial.cpp](examples/cpp/tutorial.cpp) | [sysml-sdk-cpp](skills/sysml-sdk-cpp/SKILL.md) |

In Python, the whole thing is:

```python
from sysml import Model

model = Model.from_toolkit("model.sysml", library_dir="sysml.library")
vehicle = model.resolve("Vehicles::Vehicle")
print([feature.declaredName for feature in vehicle.ownedFeature])
```

[examples/queries/](examples/queries/README.md) goes further: parametric queries over whole models
(reachable states with trigger sequences, bills of materials, roll-ups, connectivity, requirement
checks), the same in every language and printing the same report.

## Install

The release assets of each version carry one package per language, the binding library for each
platform, and the standard library. The packages are not on the public registries yet.

| Language | Asset | Install | SysML Toolkit backend |
|---|---|---|---|
| Python 3.10+ | `sysml-0.1.0-py3-none-<platform>.whl` | `pip install sysml-0.1.0-py3-none-<platform>.whl` | included in the wheel |
| JavaScript / TypeScript (Node 20+, browsers) | `sysml-0.1.0.tgz` | `npm install ./sysml-0.1.0.tgz` | included, as WebAssembly |
| Java 21+ | `sysml-sdk-0.1.0.jar` | put it on the class path | included in the jar |
| C# (.NET 8) | `OpenMBEE.SysML.0.1.0.nupkg` | `dotnet add package OpenMBEE.SysML --version 0.1.0 --source <folder with the .nupkg>` | included in the package |
| C++17 | `sysml-sdk-cpp-0.1.0.zip` | put its `include/` on the include path (header-only) | included in the archive (`lib/<target>/`): copy it beside your program |

- **The standard library**, `sysml_library-0.1.0.zip`: `sysml.library/` holds the library's
  models, for the SysML Toolkit (pass the directory when a session opens), and
  `sysml.library.full.json` the same library as JSON, for payloads. Most models refer to it
  (`ScalarValues::Real`, `ISQ::mass`, and implicitly `Parts::parts` and the like). It is the
  SysML v2 release's library, under the Eclipse Public License 2.0.
- **The binding library** comes with every package: the wheel carries the one for its platform,
  the npm package the WebAssembly build, and the jar, the NuGet package and the C++ archive one for
  each of Windows x64, Linux x64, and macOS on Apple silicon and on Intel. The jar copies its library out on first use and loads it; .NET finds the
  NuGet package's; a C++ program finds the one beside its executable. `SYSMLV2_ABI` (and
  `SYSMLV2_ABI_WASM` for JavaScript) overrides it with another build. The release also has each
  platform's library on its own, `sysmlv2_abi-0.1.0-<target>.zip` (Windows) and `.tar.gz` (others),
  each with a README.

| Platform | Target | Wheel tag |
|---|---|---|
| Windows x64 | `x86_64-pc-windows-msvc` | `win_amd64` |
| Linux x64 (glibc; built on Ubuntu 24.04) | `x86_64-unknown-linux-gnu` | `linux_x86_64` |
| macOS, Apple silicon (11 or later) | `aarch64-apple-darwin` | `macosx_11_0_arm64` |
| macOS, Intel (10.12 or later) | `x86_64-apple-darwin` | `macosx_10_12_x86_64` |
| Anywhere JavaScript runs | `wasm32-unknown-unknown` | (in the npm package) |

Java: on JDK 21 run with `--enable-preview`, because the foreign function API the SysML Toolkit
backend uses is a preview there; from JDK 22 on nothing is needed. The jar serves both. Add
`--enable-native-access=ALL-UNNAMED` to silence the JDK's warning about native access. On macOS, a
library downloaded through a web browser is quarantined and will not load until the attribute is
removed: `xattr -d com.apple.quarantine libsysmlv2_abi.dylib`.

## The two backends

**The SysML Toolkit** answers from the model itself, through its checked reader: a property is
read by its specification name, owned or derived, and the SysML Toolkit answers only what it can
derive completely. Where it cannot yet, it refuses, and the SDK raises `NotImplementedInToolkit`
with the SysML Toolkit's reason rather than pass on a value that might be wrong. The relationships
a derived property is computed from belong to the model and are answered, so the guides show both:
catching a refusal, and reading what the model states (a feature's `ownedTyping` for its `type`, a
definition's `ownedSubclassification` for its supertypes).

Specification operations go through the SysML Toolkit's operation entry point by their
declaration, with the specification's parameter and result types. The SysML Toolkit has a body for
a part of them (`effectiveName()`, `resolve`, `resolveGlobal`, `visibleMemberships(...)`,
`supertypes(...)`, `evaluate(...)`, ...) and answers a call only where its evidence for the model
at hand is complete; it refuses the others, like a property read.

The SysML Toolkit reads the standard library from a directory given when the session opens:
`library_dir` in Python, the last argument of
`ToolkitBackend.open(library, paths, sources, libraryDir)` in Java and of `ToolkitBackend.Open(...)`
in C#, and of `ToolkitBackend::open(paths, sources, library_dir)` in C++. The WebAssembly build
reads no files itself: in JavaScript pass `libraryDir` in Node, or the library's files as
`librarySources` (each file's name, without its directories, to its text), which works in a
browser too. The library loads as a library: its elements are not listed as the model's, and they
report `isLibraryElement`.

The SysML Toolkit also writes the model back out as full-form JSON: `full_json()` in Python,
`fullJson()` in JavaScript and Java, `FullJson()` in C#, `full_json()` in C++. By default the
export is written at the closure level, with inherited and imported members filled in; `closures`
false writes the owned side only, as the SysML Toolkit's command-line tool does.

**A payload** answers with what its writer wrote, and nothing else: it needs no native code, and it
cannot tell a computed value from a stand-in its writer put where it had none. Operations cannot be
evaluated on a payload and raise `NotImplementedInToolkit`. A reference to an element the payload
does not contain raises `UnresolvedReference`.

A payload refers to the standard library's elements without containing them, by the element ids
KerML 9.1 and SysML 9.1 prescribe for the library. Read the release's library JSON once as a
`PayloadLibrary` and pass it with each payload model: the model's references into the library then
resolve against it. The library's elements are not the model's, so `all`, `roots` and
`elements_of_type` list the payload's own, as the SysML Toolkit does with its library, and a
qualified name is looked up in the payload first. The library JSON holds each library element's own
side: its inherited members are not listed. A model exported without the library loaded refers to
library names by ids that resolve nowhere; export with the library.

Every language has the same three errors: `NotImplementedInToolkit` (a member not answered),
`UnresolvedReference` (a reference that does not resolve), `Gone` (an element that is no longer
there); in Java and C# they end in `Exception`, in C++ they are `not_implemented_in_toolkit`,
`unresolved_reference` and `gone`.

## Names

Each language follows its own conventions for package names.

| Language | Package | In your code |
|---|---|---|
| Python | `sysml` | `import sysml` |
| JavaScript / TypeScript | `sysml` | `import { Model } from "sysml";` |
| Java | `sysml-sdk` jar | `org.openmbee.sysml` and `org.openmbee.sysml.classes` |
| C# | `OpenMBEE.SysML` | `using OpenMBEE.SysML;` |
| C++ | header-only | namespace `sysml` |

In the specification, properties and operations are separate members of a class, and every
language shows at the call site which one it uses.

- Java and C++ read every property through a getter: `get` followed by the specification name
  with its first letter capitalized (`getQualifiedName()`, `getOwnedFeature()`, and for Booleans
  `getIsAbstract()`). Operations keep their specification names (`effectiveName()`,
  `specializes(...)`).
- Python, C# and TypeScript read properties with property syntax under the specification name
  (`el.qualifiedName`) and call operations by theirs (`el.effectiveName()`). Where an operation
  shares its name with a property of the same class, the operation takes a suffix:
  `instantiatedType` is a property of `InstantiationExpression` and its subclasses, and the
  operation of that name is `instantiatedType_op()` in Python and `instantiatedTypeOp()` in C#
  and TypeScript. TypeScript also suffixes the one operation whose name JavaScript reserves for
  its own protocols: `MultiplicityRange.valueOf` is `valueOfOp()`.

Every class carries every member it has, whether it declares it or inherits it. The members of the
metaclass `Element` itself (`name`, `owner`, `qualifiedName`, `documentation`, `isLibraryElement`,
`effectiveName()`, ...) are on the runtime element type too, so an element held without its class
(what `owner` returns) answers them without being narrowed first. Runtime members that are not in the
specification are spelled so they can never collide with it: `element_id` and `metaclass_name` in
Python, `$id` and `$metaclass` in Java and TypeScript, `ElementId` and `MetaclassName` in C#,
`element_id()` and `metaclass_name()` in C++.

Every model offers the same helpers: its elements (`all()`, `All()`), those of one metaclass
(`elements_of_type`, `elementsOfType`, `ElementsOfType<T>()`), and its top-level namespaces
(`roots`). Elements compare, and hash, by their element id.

## Skills for coding agents

[skills/](skills/) holds one skill per language (`sysml-sdk-python`, `sysml-sdk-typescript`,
`sysml-sdk-java`, `sysml-sdk-csharp`, `sysml-sdk-cpp`), in the Agent Skills format. A skill
gives a coding agent what it needs to write working code against the SDK from a one-line request
("which parts does this model's vehicle have?"): how to load a model with either backend, the shape
of the API, navigation recipes for common SysML questions (parts, typing, connections and ports,
states, requirements, multiplicities, attribute roll-ups, expressions), tested helper functions for
the reads the SysML Toolkit refuses, and how to check the result.

To use one, copy its folder into your agent's skills directory; for Claude Code that is
`.claude/skills/` in a project, or `~/.claude/skills/` for all projects:

```sh
cp -r skills/sysml-sdk-python ~/.claude/skills/
```

## Building from source

The binding library (`abi/`) is a Rust crate that depends on the SysML Toolkit by path: clone it
beside this repository, under its own name `sysml-toolkit`, at the tag
`vendor/sysml-toolkit/PIN.json` names under `toolkit` (`v0.10.0`). [abi/README.md](abi/README.md)
has the build, the WebAssembly build, and how to move to a newer SysML Toolkit release.

```sh
git clone --branch v0.10.0 https://github.com/Open-MBEE/sysml-toolkit
git -C sysml-toolkit submodule update --init --depth 1 spec-refs/SysML-v2-Release   # the standard library, for the tests
cd sysml-sdk/abi && cargo build --release                # the native library, into abi/target/release
cargo build --release --target wasm32-unknown-unknown       # the WebAssembly build
```

The payload backend needs nothing but each language's own toolchain.

### Layout

| Path | What |
|---|---|
| `metamodel.json` | The table every language is generated from: the metaclass hierarchy, every class's properties with their types and redefinitions, and its operations with their parameters. |
| `python/sysml/` | Python: the runtime (`base.py`), the SysML Toolkit backend over `ctypes` (`toolkit.py`), the generated classes. |
| `ts/` | JavaScript and TypeScript: the runtime, the SysML Toolkit backend over the WebAssembly build (`toolkit.mjs`, Node and browser alike), the generated declarations and metadata. |
| `java/` | Java, with no dependencies and no build tool: the runtime, the SysML Toolkit backend over the foreign function API, the generated interfaces, and checks. |
| `csharp/` | C#: the library (runtime, SysML Toolkit backend over P/Invoke, generated interfaces) and a checks project. |
| `cpp/` | C++17, header-only: the runtime, the SysML Toolkit backend loading the library at run time, the generated structs, and checks. JSON through the vendored nlohmann/json. |
| `abi/` | The binding library: the C interface over the SysML Toolkit that every language uses. |
| `tools/` | The extractor, the generators, the packaging and package-check scripts, and the naming tools. |
| `vendor/` | Pinned inputs: the specification's XMI and the SysML Toolkit's property tables, the naming table of the binding library, nlohmann/json. Each `PIN.json` records provenance and hashes. |
| `examples/` | The guides and tutorials, one per language, and the query examples (`examples/queries/`): the same parametric queries in every language, printing the same report. |
| `skills/` | One skill per language for coding agents that write code against the SDK. |
| `data/`, `python/tests/`, `*/checks` | Test payloads, tests, and the generated conformance checks per language. |

### Regenerate

```sh
python tools/extract_metamodel.py     # vendored XMI and property tables -> metamodel.json
python tools/gen_toolkit_table.py     # the dispatch table of each SysML Toolkit backend (also
                                      #   _java, _cs, _cpp, _js), from vendor/naming-table/
python tools/gen_classes.py           # the Python classes (also _ts, _cs, _java, _cpp)
python tools/gen_conformance.py       # the conformance checks of every language
python tools/gen_abi.py               # abi/src/generated.rs
python tools/export_example.py --library <sysml.library>
                                      # the example JSON, through the SysML Toolkit: the payloads
                                      #   (examples/vehicle.full.json, examples/queries/models/*.full.json)
                                      #   and the standard library (examples/sysml.library.full.json)
```

Generated files are never edited by hand; CI regenerates them and fails on any difference. The
example JSON is not committed at all: CI generates it once per SysML Toolkit pin and every job that
reads payloads downloads it, and `tools/package_library.py` packages the library archive as a
release asset. `tools/naming.py` holds every naming rule and the package names; `tools/rename.py`
changes those names across the repository.

### Test

```sh
python -m pytest -q                   # runtime, conformance, the SysML Toolkit (Annex A needs the
                                      #   sysml-toolkit checkout's spec-refs/SysML-v2-Release)
python python/demo.py
node ts/demo.mjs && node ts/conformance.generated.mjs && node ts/toolkit_checks.mjs
dotnet run --project csharp/OpenMBEE.SysML.Checks -c Release
javac --enable-preview --release 21 -d java/out $(find java/src -name '*.java')
java --enable-preview -cp java/out org.openmbee.sysml.checks.Checks
java --enable-preview -cp java/out org.openmbee.sysml.checks.ToolkitChecks
g++ -std=c++17 -Wall -Werror -O0 -I cpp/include -I vendor cpp/checks/checks.cpp cpp/checks/conformance.g.cpp -o cpp/out/checks && cpp/out/checks
g++ -std=c++17 -Wall -Werror -I cpp/include -I vendor cpp/checks/toolkit_checks.cpp -o cpp/out/toolkit_checks && cpp/out/toolkit_checks
cd abi && cargo test --release --lib && python smoke.py ../examples/vehicle.sysml
```

The query examples print one report in every language; each run is compared with
`examples/queries/expected/` (see [examples/queries/README.md](examples/queries/README.md)).

Where the SysML Toolkit does not answer a member yet, the tests that need it are expected failures,
not passes and not failures. `python/tests/toolkit_gaps.py` lists each with the member the
SysML Toolkit refuses and why; pytest reports them as `xfailed`, a listed test that starts to pass
fails until its row is removed, and one that stops on anything else fails. The SysML Toolkit check
programs of the other languages mark theirs the same way (`expected failure: ...`). A query report
read through the SysML Toolkit stops where the SysML Toolkit refuses a read;
`tools/compare_report.py` accepts it only at a refusal the same module lists, and only when what the
report printed up to there equals the expected report:

```sh
python examples/queries/python/demo.py | python tools/compare_report.py examples/queries/expected/report.txt
```

The SysML Toolkit checks skip themselves when the binding library is not built; with
`SYSML_REQUIRE_TOOLKIT=1` a skip is a failure, which is how CI runs them. On Windows with MinGW,
add `-static` to the C++ commands, and `-Wa,-mbig-obj` for the conformance file. On Linux, add
`-ldl` to the SysML Toolkit checks.

The packages are built with `python tools/package.py <python|sdist|npm|java|csharp|cpp>` and
checked with `python tools/check_packages.py <python|npm|java|csharp|cpp> --dist <folder>`, which
installs each one somewhere empty, reads the example model through it, and runs the language's
guide against it.

## License

Copyright 2026 Planetary Utilities. Apache-2.0, see [LICENSE](LICENSE); the contributors are listed
in [NOTICE](NOTICE), which every package carries. The C++ package includes nlohmann/json, under the
MIT license (`vendor/nlohmann/LICENSE.MIT`). The packages that carry the binding library also carry
the notices of the Rust crates compiled into it (`THIRD-PARTY-NOTICES.txt`,
`RUST-STANDARD-LIBRARY-NOTICES.html`).

The standard library archive (`sysml_library-<version>.zip`) is not covered by the SDK's license.
It holds the SysML v2 release's library models, and a JSON form made from them, both under the
Eclipse Public License 2.0; the archive carries that license and a NOTICE with the models' source
and copyright holders.
