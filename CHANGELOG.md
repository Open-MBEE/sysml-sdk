# Changelog

## 0.2.0

On the SysML Toolkit `v0.12.0` (0.1.1 was on `v0.10.2`).

### Added

- The convenience contract as data, in preparation of the `find` members: `convenience.toml` gives
  the members by kind (features, and members through any membership) and by definition kind as three
  templates, and the others one by one, each with its signature and its semantics in OCL over
  specification properties and operations;
  `tools/extract_convenience.py` expands them into `convenience.json` (312 members), checks every name
  their OCL uses against `metamodel.json`, and spells each member in every language.
  `convenience-examples.toml` gives every member an example (456, over ten small models, with the
  answer); a test checks on each backend that the elements the examples name are in their models.
- The convenience members in every language, beside the specification's: `find_parts()`,
  `find_part_defs(scope)`, `find_upper_bound()` and the others (`findParts` in Java and JavaScript,
  `FindParts` in C#), with `Scope` (owned, inherited, all) and `VisibilityFilter` (any, protected,
  public) arguments and the contract's defaults. The SysML Toolkit answers them through the binding
  library, and the Pilot Implementation through the SDK's Pilot engine, each by the member's OCL over
  its own model; a payload backend does not answer them (`NotAnswered`). Where the contract gives no
  answer (the literal of an expression that is not one), a member raises `Refused`
  (`RefusedException` in Java and C#, `refused` in C++), a kind of `NotAnswered`. On the examples, the
  Pilot Implementation answers all 456; the SysML Toolkit answers 430 and does not answer 26, where it
  does not answer what the members read: `type` on action, state, calculation, case, allocation and
  flow usages and on enumeration values (15), a metadata usage's `metaclass` (4), the member lists of
  a definition with a public import in its body (6), and an operator expression's `ownedFeature` (1).
  Where an answer from the Pilot Implementation contradicts
  the specification, the examples list it as that backend's deviation (`deviates`), with the
  reason: four, where its `supertypes()` keeps implied specializations or gives a conjugated type
  none. The engine protocol has the `find` request and
  the `invalid` error for them.
  - A definition list (`find_part_defs(Scope.ALL)` and the others) gives each definition once, as
    the specification's `member` does, though an alias or a second import makes it a member again.
    Outside the owned scope a member or definition list is in the backend's order: it reads the
    specification's `membership` or `member`, whose order the specification does not define, and
    the backends differ (the SysML Toolkit gives the owned memberships first, the Pilot
    Implementation the imported ones).
  - A list of features with the inherited scope gives the inherited features whose owning type the
    type specializes (`specializes()`, which the SysML Toolkit does not answer yet): a feature that a
    supertype imports is inherited by name (the member lists have it) but is not a feature of the
    type, as the specification's `featureMembership` since KERML11-191. The all scope
    is the owned features followed by the inherited ones. A conjugated port definition's features
    are its original's.
  - Converting a value into another unit is not part of the members yet.
  - Name-keyed definition maps, one per definition kind: `find_part_defs_by_name(scope)`
    (`findPartDefsByName` in Java and JavaScript, `FindPartDefsByName` in C#) gives the part
    definitions under every name and short name they are members by, an alias's included, in the
    backend's order of `membership`: a `dict` in Python, a `Map` in JavaScript, a `java.util.Map` in Java, an
    `IReadOnlyDictionary` (a `NameMap<T>`) in C#, and a `NameMap<T>` in C++ (a vector of name and
    element pairs, with `find` and `contains`). A name that names two different definitions (a
    connection definition and a view definition with one name, which the backends load) is refused,
    naming both. The engine protocol carries a map as `[name, element]` pairs.
  - In Java, C# and C++, a literal's value (`findLiteral`) and the bounds (`findLowerBound`,
    `findUpperBound`) are a `Literal`: an integer, a real, a Boolean, a string, or the unbounded `*`
    (a sealed interface of records in Java, a record with nested cases in C#, a `std::variant` with
    `Unbounded` in C++), read by kind (`asLong()`, `AsLong()`) or by a switch over the cases; an
    absent bound is null. Java's `findLiteral(Long.class)` and C#'s `FindLiteral<long>()` read the
    value as the type given. Python and JavaScript answer their own values (`*` is `math.inf` and
    `Infinity`).
  - Every member carries its contract as a doc comment, so that editors show it on hover: Javadoc,
    C# XML documentation, JSDoc in `classes.d.ts`, docstrings in the Python stubs, and Doxygen. It
    says what the member answers, each parameter with its default, the shape of the answer, and when
    the member is refused: by its contract (`Refused`) where its definition can give no answer, and by
    the backend (`NotAnswered`) where the backend cannot answer what it reads.
- Convenience members over lists: every element list a convenience member returns carries the
  members of its elements, so calls chain (`pkg.find_part_defs(Scope.ALL).find_parts()`). The list's
  answer is the elements' answers in list order, list results concatenated and absent single results
  dropped; if the member refuses for one element, the call refuses. Members that answer a Boolean, a
  number or a literal stay on the elements. Python and JavaScript have `ElementList`, a list (an
  `Array`) subclass; Java a typed list per element class (`PartUsageList`, …, on `ElementList`); C#
  extension methods on `IEnumerable` of each member's class (`ElementLists`); C++ the template
  `ElementList<T>`, a `std::vector<T>`. Any list of elements lifts into one (`ElementList(xs)`,
  `ElementList.from(xs)`, `new TypeList(types)`). The backends answer a list together where they can:
  the engine protocol has the `findEach` request, one round trip per list.
- The convenience members in the documentation: each language's guide has a section on them, run
  against the release packages like the rest of the guide's code; the README introduces them; and the
  skills for coding agents teach them, and the Pilot backend, with what each backend answers. Each
  release carries the convenience reference, `sysml-sdk-reference-<version>.html`: every member with
  its signature in the five languages, its definition in OCL, its examples, and what each backend
  answers on them.
- `Model.find(qualifiedName, inherited)`: the element a qualified name names, whatever its visibility,
  the same on every backend: each segment a member of the namespace before it through its owned
  memberships (aliases included) and, with `inherited`, its inherited ones; quoted names with KerML's
  escapes. `Model.resolve(name, context)`: name resolution from a namespace, the specification's
  `Namespace::resolve`; without a context, `resolve` is the backend's resolution from the root, as
  before. `Model.split_qualified_name` (`splitQualifiedName`, `SplitQualifiedName`) splits a qualified
  name into its segments. The convenience members' internal entry point on the model is now
  `findMember` in Java, JavaScript and C#.
- A third backend in every language: the SysML v2 Pilot Implementation
  (`Systems-Modeling/SysML-v2-Pilot-Implementation`, release `2026-08`, version 0.62.0), run in
  Java. It reads `.sysml` and `.kerml` files and in-memory sources with its own standard library, adds
  the implied relationships and implicit elements to both (or not, with `elaborate` false; the
  Pilot's own elaboration leaves the library out, so the engine elaborates it, once per library
  loaded), and computes
  properties and operations with its own code, behind the same classes as the other backends:
  `Model.from_pilot` and `sysml.pilot.PilotBackend` in Python, `PilotBackend.open` in JavaScript
  (Node) and Java, `PilotBackend.Open` in C#, `PilotBackend::open` in C++ (`<sysml/pilot.hpp>`). On
  the specification's Annex A model it reads every property the SDK declares for each of the 30,220
  elements of the elaborated model without a failure.
- Where the Pilot runs: in the program's process in Java (in a class loader of its own), in Python
  through JPype (the `pilot` extra: `pip install "sysml[pilot]"`), and in C++ through JNI when
  compiled with `SYSML_PILOT_JNI`; otherwise in a Java process the backend starts and exchanges one
  JSON request and answer per line with. The SDK's Pilot engine that serves both,
  `sysml-pilot-server.jar`, is inside the Java SDK and carried by the Python, npm, NuGet and C++
  packages.
- The Pilot is not part of any package. `SYSML_PILOT_HOME` names one (a Pilot jar and its
  `sysml.library`); otherwise the release the SDK is pinned to is downloaded on the first use (about
  130 MB), checked against its pinned SHA-256, and kept in the user's cache, which all languages
  share. It needs a Java 21 or newer runtime.
- The Pilot reads its standard library once per Java runtime: a closed model's Pilot instance, with
  the model taken out of it, serves the next model opened with the same Pilot and library. Opening a
  small model then takes well under a second instead of about ten. This applies where models share a
  runtime (in Java, through JPype, and through JNI); with a Java process per backend, each process
  reads the library again. The answers are those of a fresh Pilot, except where the Pilot's
  elaboration of a model added an element inside a library feature, which later models then see
  (rare). `SYSML_PILOT_KEEP_LIBRARY=0` gives every model a fresh library.
- The Pilot engine can answer the convenience members through a plugin jar compiled against the
  Pilot (`SYSML_PILOT_PLUGIN`), whose bodies read the Pilot's own properties and utilities where they
  give a member's answer, or run the plugin's bodies beside its own and refuse a call, naming both
  answers, wherever the two differ (`SYSML_PILOT_CONVENIENCE=compare`). Without a plugin it answers
  as before. The engine protocol has the `diagnostics` request; the Pilot engine answers it with how
  it answers the members and, in compare mode, the counts per member, which it also writes to the
  file `SYSML_PILOT_DIAGNOSTICS` names.
- `BackendError` (`BackendException` in Java and C#, `backend_error` in C++): the engine behind a
  backend failed, or is not available.
- `EngineBackend` in every language: any engine that speaks the SDK's engine protocol (one JSON request
  and answer per line over its standard input and output, `docs/engine-protocol.md`) runs as a
  process behind the same classes. The Pilot backend is built on it.
- `NotAnswered` (`NotAnsweredException` in Java and C#, `not_answered` in C++): a member the backend
  does not answer for this element. `NotImplementedInToolkit` is now a kind of it, so one handler
  covers every backend's refusals; code that catches `NotImplementedInToolkit` is unchanged.

### Changed

- Built and tested against the SysML Toolkit's release `v0.12.0` (was `v0.10.2`). On the
  specification's Annex A model 3.6 % of all property reads are refused (12.8 % on `v0.10.2`): the
  inherited and imported member lists, `type`, connector ends, the `subjectParameter` of requirements
  and cases and the results of operator and invocation expressions are answered now. Every one of
  the 103 operation declarations has a body (33 before), among them `specializes`,
  `isCompatibleWith`, `allSupertypes` and `supertypes(...)`; a call still refuses where the SysML
  Toolkit lacks the evidence for its answer.
- Interchange-visible changes of the SysML Toolkit `v0.12.0` that reach the SDK's models: a usage that
  declares no multiplicity, where SysML gives it the default `[1..1]`, owns a `Multiplicity` member
  without bounds whose subsetting of `Base::exactlyOne` is implied (an `individual` definition one of
  `Base::zeroOrOne`), as the Pilot Implementation writes it; operator and invocation expressions own
  their result parameter, an implied relationship, and are typed by the invoked function; the trigger
  of `accept ... at`, `after` and `when` is the payload parameter's value. The query examples read
  such a default multiplicity member as no declaration, so their answers are unchanged.
- The specification's XMI files are no longer in the repository. `tools/extract_metamodel.py`
  downloads them from the OMG on its first run, checks them against the hashes in
  `vendor/sysml-toolkit/PIN.json`, and keeps them in `vendor/sysml-toolkit/spec-refs/`, which git
  ignores. `metamodel.json` and everything generated from it are unchanged.
- The SysML Toolkit backend keeps a library directory's standard library loaded for the rest of the
  process once a model has been opened with it, so opening models one after another no longer loads
  the library again each time: on small models an open takes about 0.01 s instead of 0.16 s, and the
  first read of a model's implied relationships about half as long. This needs the SysML Toolkit's
  library cache, which it keeps in `SYSMLV2_CACHE_DIR`, `$XDG_CACHE_HOME/sysmlv2` or
  `$HOME/.cache/sysmlv2`; on Windows, where `HOME` is usually not set, set `SYSMLV2_CACHE_DIR`. The
  library stays in memory (about 110 MB) until the process ends.
- The SysML Toolkit backend, in every language, keeps each answer it has read until the model is
  closed: a property read, or an operation call with the same arguments, asked again is answered from
  memory (a refusal is asked again). An open model does not change, so the answers stay right.
  Members that ask one question per element pay for each element once: the
  `find_..._defs_semantic(scope)` members call `specializes` on every type they consider, and on the
  specification's Annex A model `find_attribute_defs_semantic(Scope.ALL)` on `SimpleVehicleModel`
  takes about 2 s the first time and no measurable time when asked again.

### Fixed

- `resolve` without a context, on the SysML Toolkit and in every language, finds only the names the
  model gives. It found an unnamed element under the name of what it references (an unnamed
  `satisfy spec by v` as `v::spec`), through the toolkit's compatibility lookup; the binding library
  now uses the toolkit's semantic one.

## 0.1.1

On the SysML Toolkit `v0.10.2` (0.1.0 was on `v0.10.0`).

### Changed

- The SysML Toolkit backend runs on `Open-MBEE/sysml-toolkit` `v0.10.2`. Sessions on the standard
  library build and answer sooner, and the SysML Toolkit's fixes reach the SDK: an unnamed usage that
  only references another feature (`satisfy R by x;`, `assert c;`) no longer answers a simple name; a
  feature with no value of its own takes the value of a feature it subsets when the two must be
  equal; `satisfy requirement : R by x;` is checked against `R`'s constraints; a valued redefinition
  of a feature chain (`attribute :>> chassis.mass = 2.5 [kg];`) supplies its value when the chain is
  read through the redefining part. The answers and refusals the SDK reports for the specification's
  Annex A model are the same as with `v0.10.0`.

## 0.1.0

The first release.

### Added

- The SDK for Python, JavaScript/TypeScript, Java, C# and C++, generated from one table of the
  KerML and SysML metamodels (175 metaclasses, every property and every operation under its
  specification name).
- Two backends in every language: the OpenMBEE SysML Toolkit (`Open-MBEE/sysml-toolkit`
  `v0.10.0`), through the binding library, and full-form interchange JSON payloads.
- Properties are read through the SysML Toolkit's checked reader, by their specification names,
  owned or derived. It answers what it can derive completely and refuses the rest with its
  reason, raised as `NotImplementedInToolkit`; a value the SysML Toolkit cannot vouch for is
  never passed on.
- Specification operations are evaluated through the SysML Toolkit's operation entry point, by
  their declaration. Their parameters and results have the specification's types in every language
  (an element list, one element, a Boolean, text); `resolve` returns the Membership the
  specification defines.
- The binding library for Windows x64, Linux x64, macOS on Apple silicon and on Intel, and
  WebAssembly.
- A full-form JSON export of a model read by the SysML Toolkit, at the closure level (inherited and
  imported members included) or the owned-side level.
- Packages: a Python wheel per platform with the binding library inside, and an sdist; an npm
  package with the WebAssembly build inside; a jar for JDK 21 and later, a NuGet package and a C++
  header archive, each with the binding library of Windows x64, Linux x64 and macOS (Apple silicon
  and Intel) inside; the binding library per platform on its own. The jar copies its library out
  on first use, .NET finds the NuGet package's, and a C++ program the one beside its executable;
  `SYSMLV2_ABI` overrides them all. Every package that carries the binding library also carries the
  notices of what it compiles in: `THIRD-PARTY-NOTICES.txt` for the SysML Toolkit and the Rust
  crates, and `RUST-STANDARD-LIBRARY-NOTICES.html` for the Rust standard library.
- The same runtime surface in every language: the members of the metaclass `Element` on any element
  (`name`, `owner`, `qualifiedName`, ..., `effectiveName()`), a model's elements (`all()`), those of
  one metaclass, and its top-level namespaces; the elements of one model compare and hash by their
  element id. The SysML Toolkit backend reads the standard library from a directory in every
  language, and in JavaScript also from the library's files given as sources, in a browser as well.
  A model file or library directory that does not exist is named in the error.
- The standard library at hand in every language, as the first step of a program: every package
  carries its models (`standard_library()` in Python, `standardLibrary()` in JavaScript,
  `StandardLibrary.directory()` in Java, `StandardLibrary.Directory()` in C#, and in C++
  `sysml::standard_library()` for the archive's `sysml.library/` copied beside the program), and
  `standard_library_json()` and its counterparts download the library JSON from the release on their
  first call, check it against the SHA-256 GitHub states for it, and keep it in a cache all languages
  share (C++ reads the cache, and downloads nothing). `SYSML_LIBRARY_JSON`, `SYSML_LIBRARY_DIR` (C++)
  and `SYSML_CACHE_DIR` name other places. The packages that carry the models declare their license
  as Apache-2.0 AND EPL-2.0.
- The standard library, `sysml_library-<version>.zip`, in the two forms the backends read: the
  library models as the SysML v2 release has them (`sysml.library/`, for the SysML Toolkit), and the
  library as full-form JSON written by the SysML Toolkit under the element ids KerML 9.1 and
  SysML 9.1 prescribe for the library (`sysml.library.full.json`), with `PayloadLibrary` in every
  language: read it once and pass it with a payload model, and the model's references into the
  library resolve against it. Its elements are not listed as the model's. Both are licensed under
  the Eclipse Public License 2.0, as the library models are, not under the SDK's Apache-2.0; the
  archive's NOTICE names the source and the copyright holders.
- A guide per language (`examples/<language>/README.md`) with the code that reads a model through
  the SysML Toolkit and from a JSON export, ready to copy, and the first queries; the package checks
  run that code against the installed packages.
- A skill per language for coding agents (`skills/`), with tested helper functions that read what
  the model states where the SysML Toolkit refuses a derived property (types, inherited features,
  bound values, multiplicities, connector ends, subjects, initial and reachable states).
- Query examples (`examples/queries/`): reachable states and shortest trigger sequences, bills of
  materials, attribute roll-ups, connectivity and requirement checks over two example models and the
  specification's Annex A vehicle, in every language, each printing the same report. Through the
  SysML Toolkit a report stops at the first read the SysML Toolkit refuses and says so; through a
  payload it runs in full.
- Tests: where the SysML Toolkit does not answer a member yet, the test that needs it is an expected
  failure, listed with the member and the reason in `python/tests/toolkit_gaps.py`; it fails when it
  starts to pass, and when it stops on anything else.

### Known limitations

What the SysML Toolkit does not compute completely is refused with `NotImplementedInToolkit`;
nothing in this list returns an invented value through the SysML Toolkit backend. Where the
SysML Toolkit answers differently from the specification, it is listed here.

The SysML Toolkit backend:

- **Much of what builds on inheritance is refused.** The checked reader does not answer yet the
  inherited and computed member lists (`feature`, `inheritedFeature`, `inheritedMembership`,
  `featureMembership`, `member`, `membership`, `importedMembership`, `endFeature`, `input`, `output`,
  `usage` and the usage lists built on them), a feature's `type` and `featuringType` in most cases,
  `definition` and the kind-specific definitions (`partDefinition`, `itemDefinition`, ...),
  `parameter`, connector ends (`sourceFeature`, `targetFeature`, `connectorEnd`), the parameters of
  requirements and cases (`subjectParameter`, `actorParameter`, `stakeholderParameter`), and the
  `result` of expressions and functions. On the specification's Annex A
  model with the library, 12.7 % of all property reads are refused. The relationships these are
  derived from are answered: `ownedTyping`, `ownedSubclassification`, `ownedSubsetting`,
  `ownedRedefinition`, `ownedFeature` and `ownedMember` (the guides show how to read them).
- **A part of the specification operations is evaluated:** 33 of the 103 operation declarations,
  among them `resolve`, `resolveLocal`, `resolveGlobal`, `resolveVisible`, `effectiveName()`,
  `effectiveShortName()`, `inheritedMemberships(...)`, `supertypes(...)` and `evaluate(...)`. The
  others, such as `isCompatibleWith`, `specializes` and `allSupertypes`, raise
  `NotImplementedInToolkit`.
- **Some references do not resolve** that should, in the specification's own example model among
  others (19 references there): names that meet through recursive imports. Reading them raises
  `UnresolvedReference`.
- **`isVariable` and `mayTimeVary` of many features, and `isConstant` of some, are refused.**
- **Conditional operands are built as the expressions themselves.** The second operand of `and`,
  `or`, `implies` and `??`, and the branches of `if`, are the operand expressions, where the
  specification wraps each in a reference to it.

A payload, and the SysML Toolkit's JSON export:

- **A payload holds what its writer wrote.** The SysML Toolkit's export is written by its
  compatibility path, not by the checked reader: it writes a value for properties the checked
  reader refuses, and where the SysML Toolkit has no value, a stand-in the payload backend reads as
  the answer (null or an empty list). Only the SysML Toolkit backend can say that a member is not
  computed.
- **An alias hides the membership of the feature it names from inheritance:** in the export, a
  specialization of a type that has a feature `x` and `alias ax for x;` inherits the membership
  `ax`, but not the membership of `x` itself.
- **Exports made with the SysML Toolkit's command-line tool** are written at the owned-side level:
  the inherited and imported member lists are empty and the inheritance-aware properties list only
  what an element owns. The SDK's own export writes the closure level by default.
- **An unresolved reference in an export** is carried by elements the SysML Toolkit adds for it,
  which appear in the owner's `ownedRelationship`.
- **An export contains the model's own elements, not the standard library's.** Reading a reference
  into the library raises `UnresolvedReference` unless the payload model is given the library JSON.
  A model exported without the library loaded refers to library names by ids that resolve nowhere.
- **The library JSON holds each library element's own side.** Its inherited and imported member
  lists are empty, and properties such as `feature` list owned members only: the SysML Toolkit
  exports the library only as a compact array, and its full-form completion of that array writes the
  owned side. References inside the library that do not resolve carry the SysML Toolkit's recovery
  annotations.

Platforms and packages:

- Java needs `--enable-preview` on JDK 21; from JDK 22 on it does not.
- The Linux library is linked against glibc 2.28, so it loads on distributions with glibc 2.28 or
  later (for example RHEL 8, Debian 10, Ubuntu 20.04 and newer); CI tests it on Ubuntu 24.04.
- The C++ package is tested with GCC on Linux and with Apple Clang on macOS in CI, and with MinGW GCC
  on Windows by hand.
- On macOS a library downloaded through a web browser is quarantined; remove the attribute with
  `xattr -d com.apple.quarantine`.
- Python is on PyPI (`pip install sysml`); the other packages are release assets, not on their
  language registries yet.
