# JavaScript and TypeScript

Read SysML v2 and KerML models from JavaScript or TypeScript, in Node or in a browser. Every
metaclass of the specification has its TypeScript type, and every property and operation keeps its
specification name: `ownedFeature`, `qualifiedName`, `effectiveName()`.

A model is read through one of two backends, with the same elements over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` text, resolves names and
  computes derived properties. It runs as WebAssembly, the same in Node and in a browser;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain JavaScript.

## Install

Node 20 or newer, or a browser. Download the package from the release and install it:

```sh
npm install ./sysml-0.1.0.tgz
```

The package carries the SysML Toolkit as WebAssembly and the standard library models; nothing else
is needed. Type declarations are included. The code below is plain JavaScript (`.mjs`); in
TypeScript, narrow what `resolve` returns (an element or `null`) before using it, with
`is(el, "PartDefinition")` or a non-null assertion.

## First: the standard library

Most models refer to the KerML and SysML standard library: `ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like. Get it once, at the start of your program, in the form your
backend reads (Node):

```js
import { standardLibrary, standardLibraryJson } from "sysml";

const libraryDir = await standardLibrary();        // the library's models the package carries, for the SysML Toolkit
const libraryJson = await standardLibraryJson();   // the library as JSON, for payloads
```

- `standardLibraryJson()` downloads the JSON (about 11 MB) from this version's GitHub release on its
  first call, checks it against the SHA-256 GitHub states for the file, and keeps it in your user
  cache (`SYSML_CACHE_DIR` moves it); later calls, from any SDK language, read the cache. Nothing is
  downloaded unless you call it.
- Without access to GitHub, download `sysml_library-0.1.0.zip` from the release, unpack it, and set
  `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds. Its `sysml.library/` directory is
  the same models the package carries.
- In a browser there is no file system: serve the library's files and pass them as
  `librarySources`, and a library JSON you host yourself to `PayloadLibrary`.
- The library is under the Eclipse Public License 2.0: see the LICENSE and NOTICE beside it.

## Read a model through the SysML Toolkit

Copy this, and put in your file names:

<!-- test: toolkit -->
```js
import { readFileSync, writeFileSync } from "node:fs";
import { ToolkitBackend, Model, NotImplementedInToolkit, UnresolvedReference, elementsOfType, is, standardLibrary } from "sysml";

const files = ["model.sysml"];                 // your model: one or more .sysml / .kerml files
const model = new Model(await ToolkitBackend.open({
  sources: Object.fromEntries(files.map((f) => [f, readFileSync(f, "utf8")])),
  libraryDir: await standardLibrary(),         // the standard library
}));

// ... your code here ...
```

- `sources` maps a file name to its text, so text you hold in memory works the same way.
- In a browser, pass the library's files as `librarySources` (each file's name, without its
  directories, to its text) instead of `libraryDir`, and the WebAssembly module as `library: await ToolkitLibrary.load(await fetch(url))`,
  where `url` serves `sysmlv2_abi.wasm` from the package.
- Without the library the model loads faster, but its references into the standard library
  do not resolve, and reading one throws `UnresolvedReference`.

## Read a model from a JSON export

A payload is the full-form interchange JSON of a model: one array of elements, each an object with
its `@id`, its `@type` and its properties. The SysML Toolkit writes one with
`sysmlv2 convert model.sysml --to full-json --lib sysml.library > model.json`, and so can other SysML
v2 tools. Its references into the standard library resolve against the library JSON:

<!-- test: payload -->
```js
import { readFileSync } from "node:fs";
import { Model, NotImplementedInToolkit, PayloadLibrary, UnresolvedReference, elementsOfType, is, standardLibraryJson } from "sysml";

const readJson = (path) => JSON.parse(readFileSync(path, "utf8"));
const library = new PayloadLibrary(readJson(await standardLibraryJson()));   // the standard library; read once, share it
const model = Model.fromFullJson(readJson("model.json"), { library });     // your export

// ... your code here ...
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they throw `NotImplementedInToolkit`.

## Find and read elements

The snippets below go where `// ... your code here ...` is, with either backend. They read the
example model [`examples/vehicle.sysml`](../vehicle.sysml); put in your own qualified names.

<!-- test: queries -->
```js
// An element by its qualified name (null when there is none).
const vehicle = model.resolve("Vehicles::Vehicle");
console.log(vehicle.qualifiedName, "is a", vehicle.$metaclass);            // Vehicles::Vehicle is a PartDefinition

// The top-level packages.
for (const root of model.roots()) {
  for (const member of root.ownedMember) console.log("top level:", member.qualifiedName);
}

// Every element of a metaclass, its subclasses included.
for (const part of elementsOfType(model, "PartUsage")) {
  console.log("part:", part.qualifiedName ?? "(unnamed)", part.$metaclass);
}

// Properties keep their specification names. A list property is an array, a single one an
// element, a string, a boolean, a number or null.
console.log(vehicle.declaredName, vehicle.isAbstract, vehicle.documentation.map((doc) => doc.body.trim()));
for (const feature of vehicle.ownedFeature) console.log("  feature:", feature.declaredName, feature.$metaclass);

// is() follows the specification's generalizations, and narrows the type in TypeScript.
console.log(is(vehicle, "Definition"), is(vehicle, "Type"), is(vehicle, "Usage"));
```

## When the SysML Toolkit does not answer

The SysML Toolkit answers a property only where it can derive it completely. Where it cannot
yet, reading the property throws `NotImplementedInToolkit` with the SysML Toolkit's reason,
instead of returning a value that might be wrong; nothing is silently empty. Catch it where your
code can go on.

The relationships a derived property is computed from are part of the model, and the SysML Toolkit
answers them: a feature's types are the `type` of its `ownedTyping` relationships, a definition's
supertypes the `superclassifier` of its `ownedSubclassification`s, what a feature subsets the
`subsettedFeature` of its `ownedSubsetting`s.

<!-- test: queries -->
```js
const wheels = model.resolve("Vehicles::Vehicle::wheels");
let types;
try {
  types = wheels.type;                                          // derived: the SysML Toolkit may refuse it
} catch (refusal) {
  if (!(refusal instanceof NotImplementedInToolkit)) throw refusal;
  console.log("not answered:", refusal.message);
  types = wheels.ownedTyping.map((typing) => typing.type);     // what the model states
}
console.log("wheels:", types.map((t) => t.qualifiedName));    // [ 'Vehicles::Wheel' ]

const sports = model.resolve("Vehicles::SportsCar");
console.log("SportsCar specializes", sports.ownedSubclassification.map((s) => s.superclassifier.qualifiedName));
try {
  console.log("SportsCar inherits", sports.inheritedFeature.filter((f) => !f.isLibraryElement).map((f) => f.declaredName));
} catch (refusal) {
  if (!(refusal instanceof NotImplementedInToolkit)) throw refusal;
  console.log("not answered:", refusal.message);
}
```

A reference that does not resolve throws `UnresolvedReference`, and an element that is no longer
there `Gone`.

## Operations

The specification's operations are methods under their own names, with the specification's
parameters. The SysML Toolkit has a body for a part of them (`effectiveName()`, `resolve`,
`resolveGlobal`, `resolveLocal`, `visibleMemberships`, `supertypes`, `evaluate`, ...), and answers
a call only where its evidence for your model is complete; otherwise, and for the operations it
has no body for (`isCompatibleWith`, `specializes`, `allSupertypes`, ...), the call raises
`NotImplementedInToolkit`, as a property read does. Whether `resolve` is answered, for example,
depends on the names in your model: catch the refusal where your code can go on. Two operation
names are suffixed so they cannot collide: `instantiatedTypeOp()` and
`MultiplicityRange.valueOfOp()`.

<!-- test: queries toolkit -->
```js
console.log(vehicle.effectiveName());                           // Vehicle
const membership = model.resolve("Vehicles").resolve("Vehicle");   // Namespace::resolve: a Membership
console.log(membership.$metaclass, membership.memberElement.qualifiedName);
```

## Write a model out as JSON

The SysML Toolkit writes the model as full-form interchange JSON, with the inherited and imported
members filled in (`{ closures: false }` writes each element's own side only, as the command-line
tool does):

<!-- test: queries toolkit -->
```js
const backend = await ToolkitBackend.open({ sources: { "model.sysml": readFileSync("model.sysml", "utf8") }, libraryDir: await standardLibrary() });
writeFileSync("model.full.json", backend.fullJson());
const exported = new Model(backend);                            // the same session, as a model
```

## Next

- [tutorial.mjs](tutorial.mjs), a runnable walk through the example model with both backends:
  `node examples/typescript/tutorial.mjs` (payload) or `--toolkit`.
- [../queries/](../queries/README.md): parametric queries over whole models (reachable states,
  bills of materials, connectivity, requirements), the same in every language.
- The names of every class and member are the specification's; see
  [Names](../../README.md#names) for the rules.
- [skills/sysml-sdk-typescript](../../skills/sysml-sdk-typescript/SKILL.md) teaches a coding
  agent the same.
