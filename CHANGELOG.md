# Changelog

## 0.1.0, preview

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
  model with the library, 12.8 % of all property reads are refused. The relationships these are
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
- The Linux library is built on Ubuntu 24.04; older glibc versions are not tested.
- The C++ package is tested with GCC on Linux and with Apple Clang on macOS in CI, and with MinGW GCC
  on Windows by hand.
- On macOS a library downloaded through a web browser is quarantined; remove the attribute with
  `xattr -d com.apple.quarantine`.
- The packages are release assets only; they are not on the language registries yet.
