# Status

Release 0.2.0, a preview, on the OpenMBEE SysML Toolkit release `Open-MBEE/sysml-toolkit` `v0.12.0`
(0.1.1 was on `v0.10.2`), with the SysML v2 Pilot Implementation
(`Systems-Modeling/SysML-v2-Pilot-Implementation`, release `2026-08`, version 0.62.0) as a third
backend and the convenience members.

## What is there

- **Five languages, three backends.** Python, JavaScript/TypeScript, Java, C# and C++ each read a
  model through the SysML Toolkit, through the Pilot Implementation, or from a full-form interchange
  JSON payload, with the same generated surface over all three.
- **The convenience members.** 312 `find` members beside the specification's (features by kind,
  definitions, lookups by name, typing, values, multiplicities, behaviour), each defined in OCL over
  the specification in the contract `convenience.toml`, and checked on 456 examples on both the
  SysML Toolkit and the Pilot Implementation in every language. The Pilot Implementation answers
  every example (four of its answers are listed as deviations from the specification); the SysML
  Toolkit refuses 26, where it cannot derive what a member reads yet.
- **The whole metamodel.** 175 KerML and SysML metaclasses with their hierarchy; every property of
  every class (13,311 class-property pairs, inherited ones included) under its specification name
  and type; every operation (103 declarations) with typed parameters. Redefinitions are recorded,
  for properties and for operations. Table `metamodel.json`, from the specification's XMI of
  2025-02-01.
- **The SysML Toolkit backend**, through the binding library in `abi/`: one C function per member.
  Every property is read through the SysML Toolkit's checked reader by specification name, so what
  the SysML Toolkit can derive reaches every language without a change here, and what it cannot is
  refused with its reason. Every operation goes through the SysML Toolkit's operation entry point;
  the SysML Toolkit evaluates 103 of the 103 operation declarations, and refuses a call where it lacks the
  evidence for its answer.
- **Platforms.** Native libraries for Windows x64, Linux x64, macOS on Apple silicon and on Intel,
  built and tested by CI on each; a WebAssembly build for JavaScript in Node and browsers.
- **Packages** for every language, built and installed from scratch by CI before each release,
  with each language's guide run against them.

## How far the SysML Toolkit goes

On the specification's own example, the Annex A vehicle model with the standard library, every
property of every one of its 7,117 elements was read through the SysML Toolkit (402,285 reads):

| Outcome | Reads |
|---|---|
| a value | 173,532 |
| empty (no value in the model, or an empty list) | 214,069 |
| refused: the SysML Toolkit cannot derive the member completely yet | 14,601 |
| a reference that does not resolve | 83 |

The refusals are concentrated in a few derived properties: a feature's `featuringType` (on every
feature), `isImpliedIncluded`, `directedFeature` and `parameter`, `type` and `definition` on some
usages, `isModelLevelEvaluable`, an occurrence usage's `individualDefinition`, the owned lists of
operator and feature reference expressions, and the `result` of literals; the relationships those
derive from are answered.
These numbers are pinned in the test suite (`python/tests/toolkit_gaps.py`); a new SysML Toolkit
release that answers more changes them, and the table moves with the pin. What the SysML Toolkit
answers differently from the specification is listed under **Known limitations** in `CHANGELOG.md`.

## Testing

- Generated conformance checks per language: every class, property and operation of the table is
  present, typed and dispatched.
- SysML Toolkit checks per language, on every CI platform, which fail rather than skip in CI.
- The Annex A regression test: invariants of the specification that hold whatever the SysML Toolkit
  computes, and the SysML Toolkit's known gaps, each with its reason.
- A differential test: every property of every element through the SysML Toolkit against its own
  closure-level export read as a payload.
- Expected failures: a test that meets a refusal by the SysML Toolkit is listed with the refused
  member; it fails when it starts to pass.
- The convenience examples: each of the 456 on the SysML Toolkit and the Pilot Implementation, in
  every language, against the contract's answer; a known deviation of a backend is listed with its
  reason, and fails when the backend starts to agree.
- Package checks: each package installed somewhere empty, read through, and its language's guide
  run against it.

## Next

- Newer SysML Toolkit releases, as they answer more (the procedure is in `abi/README.md`).
- Newer Pilot Implementation releases (the pin is `RELEASE`, `VERSION` and `ASSET_SHA256` in
  `java/src/org/openmbee/sysml/pilot/Pilot.java`).
- Publication on the other languages' registries (Python is on PyPI).
