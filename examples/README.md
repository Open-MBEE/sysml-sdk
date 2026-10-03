# Guides, tutorials and query examples

Start with the guide of your language: how to install the package, the code that reads a model
through the SysML Toolkit and the code that reads a JSON export, ready to copy with your file names,
and the first queries.

| Language | Guide | Runnable tutorial | Run from the repository root |
|---|---|---|---|
| Python | [python/README.md](python/README.md) | [tutorial.py](python/tutorial.py) | `python examples/python/tutorial.py` (SysML Toolkit), `--payload` |
| JavaScript / TypeScript | [typescript/README.md](typescript/README.md) | [tutorial.mjs](typescript/tutorial.mjs) | `node examples/typescript/tutorial.mjs` (payload), `--toolkit` |
| Java | [java/README.md](java/README.md) | [Tutorial.java](java/Tutorial.java) | see the header of `Tutorial.java` |
| C# | [csharp/README.md](csharp/README.md) | [Tutorial.cs](csharp/Tutorial.cs) | `dotnet run --project examples/csharp` (payload), `-- --toolkit` |
| C++ | [cpp/README.md](cpp/README.md) | [tutorial.cpp](cpp/tutorial.cpp) | see the header of `tutorial.cpp` |

Each tutorial is a single runnable file with the steps numbered in comments: find an element by
qualified name, ask what it is, walk its features and their types, read its documentation, follow
a usage to its definition, and see what happens when the SysML Toolkit does not answer a member.

The model is `vehicle.sysml`; `vehicle.full.json` is the same model exported by the SysML Toolkit
as a full-form interchange payload, which is what any SysML v2 tool can hand you. Its references
into the standard library resolve against `sysml.library.full.json`, the library as JSON that each
SDK release publishes. Neither JSON file is committed: generate both once with
`python tools/export_example.py --library <sysml.library>`, which needs the binding library and the
standard library's directory.

[queries/](queries/README.md) goes further: parametric queries over whole models (reachable
states, bills of materials, roll-ups, connectivity, requirement checks) in every language, each
printing the same report.

## The two ways in, and why they differ

Through the **SysML Toolkit** the SDK asks a live engine. It reads each property through the
SysML Toolkit's checked reader, which answers only what it can derive completely: derived members
the SysML Toolkit cannot derive yet (in this release, most of what builds on inheritance, such as
`inheritedFeature`, and a feature's `type`) raise `NotImplementedInToolkit` with the reason the
SysML Toolkit gives. The relationships they derive from are answered: the guides show how to read a
feature's types from its `ownedTyping`, and a definition's supertypes from its
`ownedSubclassification`. Specification operations are evaluated by the SysML Toolkit
(`effectiveName()`, `resolve`, ...). This path is available in all five languages through the same
binding library.

Through a **payload** the SDK reads what the exporting tool wrote, and nothing else. This file was
written by the SysML Toolkit at the closure level, so its inherited features are there. An export
by the SysML Toolkit's command-line tool is written at the owned-side level instead: the inherited
and imported member lists are empty, and the other inheritance-aware properties list only what an
element owns. A payload also cannot say what its writer did not compute. Where the SysML Toolkit
has no value the file holds a stand-in, such as null for a feature's `isVariable`. The payload path
reads such a stand-in as the answer; only the SysML Toolkit path can say that it does not answer a
member, and the tutorials show where it does (`type`, `inheritedFeature`, `partDefinition`, ...).
Operations need the SysML Toolkit: on a payload they raise `NotImplementedInToolkit`.

The SDK never guesses: it answers what the engine or the file says, or raises.

## Building the SysML Toolkit path

The SysML Toolkit paths need the binding library, and the JavaScript one its WebAssembly build: see
[abi/README.md](../abi/README.md), or use a release's packages, which carry them. The payload
paths need only the language's own toolchain.
