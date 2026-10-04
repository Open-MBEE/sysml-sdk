# C#

Read SysML v2 and KerML models from C# (.NET 8). Every metaclass of the specification is a C#
interface, so `is` and casts follow the specification's generalizations, and every property and
operation keeps its specification name: `ownedFeature`, `qualifiedName`, `effectiveName()`.

A model is read through one of two backends, with the same interfaces over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties, through the binding library (a native library, called with P/Invoke);
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain C#.

## Install

.NET 8 or newer. Download from the release:

- `OpenMBEE.SysML.0.1.0.nupkg`, and add it to your project from the folder that holds it:
  `dotnet add package OpenMBEE.SysML --version 0.1.0 --source <that folder>`. It carries the
  binding library for Windows x64, Linux x64, and macOS on Apple silicon and on
  Intel, and .NET puts the one for your platform beside your program. It also carries the standard
  library models.

## First: the standard library

Most models refer to the KerML and SysML standard library: `ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like. Get it once, at the start of your program, in the form your
backend reads:

```csharp
var libraryDir = StandardLibrary.Directory();   // the library's models the package carries, for the SysML Toolkit
var libraryJson = StandardLibrary.Json();       // the library as JSON, for payloads
```

- `StandardLibrary.Directory()` copies the models out of the package into your user cache on its
  first call (a session reads a directory).
- `StandardLibrary.Json()` downloads the JSON (about 11 MB) from this version's GitHub release on its
  first call, checks it against the SHA-256 GitHub states for the file, and keeps it in your user
  cache (`SYSML_CACHE_DIR` moves it); later calls, from any SDK language, read the cache. Nothing is
  downloaded unless you call it.
- Without access to GitHub, download `sysml_library-0.1.0.zip` from the release, unpack it, and set
  `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds. Its `sysml.library/` directory is
  the same models the package carries.
- The library is under the Eclipse Public License 2.0: see the LICENSE and NOTICE beside it.

## Read a model through the SysML Toolkit

Copy this into `Program.cs`, and put in your file names:

<!-- test: toolkit -->
```csharp
using OpenMBEE.SysML;
using SpecType = OpenMBEE.SysML.Type;     // the metaclass Type, beside System.Type

using var toolkit = ToolkitBackend.Open(ToolkitBackend.DefaultLibrary,   // the package's binding library, or SYSMLV2_ABI's
    new[] { "model.sysml" },     // your model: one or more .sysml / .kerml files
    null,                        // or in-memory sources: file name to text
    StandardLibrary.Directory());   // the standard library
var model = new Model(toolkit);

// ... your code here ...
```

- `ToolkitBackend.Open("a.sysml", "b.sysml")` opens files without the standard library,
  `ToolkitBackend.OpenSources(new Dictionary<string, string> { ["model.sysml"] = text })` text you
  hold in memory.
- To use another build of the binding library, point the environment variable `SYSMLV2_ABI` at
  it, or name it in code: `new ToolkitBackend.Library(path)` in place of
  `ToolkitBackend.DefaultLibrary`.
- Without the standard library the model loads faster, but its references into it do not resolve,
  and reading one throws `UnresolvedReferenceException`.

## Read a model from a JSON export

A payload is the full-form interchange JSON of a model: one array of elements, each an object with
its `@id`, its `@type` and its properties. The SysML Toolkit writes one with
`sysmlv2 convert model.sysml --to full-json --lib sysml.library > model.json`, and so can other SysML
v2 tools. Its references into the standard library resolve against the library JSON:

<!-- test: payload -->
```csharp
using OpenMBEE.SysML;
using SpecType = OpenMBEE.SysML.Type;     // the metaclass Type, beside System.Type

var library = PayloadLibrary.FromJson(File.ReadAllText(StandardLibrary.Json()));   // the standard library; read once, share it
var model = Model.FromFullJson(File.ReadAllText("model.json"), library);             // your export

// ... your code here ...
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they throw `NotImplementedInToolkitException`.

The snippets assume the project's implicit usings (`System`, `System.IO`, `System.Linq`, ...), which
`dotnet new console` turns on.

## Find and read elements

The snippets below go where `// ... your code here ...` is, with either backend. They read the
example model [`examples/vehicle.sysml`](../vehicle.sysml); put in your own qualified names.

<!-- test: queries -->
```csharp
// An element by its qualified name (null when there is none), cast to its metaclass.
var vehicle = (PartDefinition)model.Resolve("Vehicles::Vehicle")!;
Console.WriteLine($"{vehicle.qualifiedName} is a {vehicle.MetaclassName}");     // Vehicles::Vehicle is a PartDefinition

// The top-level packages.
foreach (var root in model.Roots())
    foreach (var member in ((Namespace)root).ownedMember) Console.WriteLine($"top level: {member.qualifiedName}");

// Every element of a metaclass, its subclasses included.
foreach (var part in model.ElementsOfType<PartUsage>())
    Console.WriteLine($"part: {part.qualifiedName ?? "(unnamed)"} {part.MetaclassName}");

// Properties keep their specification names. A list property is an IReadOnlyList, a single one an
// element, a string, a bool?, a long?, a double? or null.
Console.WriteLine($"{vehicle.declaredName} {vehicle.isAbstract} {string.Join("; ", vehicle.documentation.Select(doc => doc.body?.Trim()))}");
foreach (var feature in vehicle.ownedFeature) Console.WriteLine($"  feature: {feature.declaredName} {feature.MetaclassName}");

// The interfaces form the specification's generalization hierarchy.
Console.WriteLine($"{vehicle is Definition} {vehicle is SpecType} {model.Resolve("Vehicles::myCar") is Usage}");
```

## When the SysML Toolkit does not answer

The SysML Toolkit answers a property only where it can derive it completely. Where it cannot yet,
reading the property throws `NotImplementedInToolkitException` with the SysML Toolkit's reason,
instead of returning a value that might be wrong; nothing is silently empty. Catch it where your
code can go on.

The relationships a derived property is computed from are part of the model, and the SysML Toolkit
answers them: a feature's types are the `type` of its `ownedTyping` relationships, a definition's
supertypes the `superclassifier` of its `ownedSubclassification`s, what a feature subsets the
`subsettedFeature` of its `ownedSubsetting`s.

<!-- test: queries -->
```csharp
var wheels = (PartUsage)model.Resolve("Vehicles::Vehicle::wheels")!;
IEnumerable<SpecType> types;
try
{
    types = wheels.type.ToList();                                   // derived: the SysML Toolkit may refuse it
}
catch (NotImplementedInToolkitException refusal)
{
    Console.WriteLine($"not answered: {refusal.Message}");
    types = wheels.ownedTyping.Select(typing => typing.type!);      // what the model states
}
Console.WriteLine($"wheels: {string.Join(", ", types.Select(t => t.qualifiedName))}");   // Vehicles::Wheel

var sports = (PartDefinition)model.Resolve("Vehicles::SportsCar")!;
Console.WriteLine($"SportsCar specializes {string.Join(", ", sports.ownedSubclassification.Select(s => s.superclassifier!.qualifiedName))}");
try
{
    Console.WriteLine($"SportsCar inherits {string.Join(", ", sports.inheritedFeature.Where(f => f.isLibraryElement != true).Select(f => f.declaredName))}");
}
catch (NotImplementedInToolkitException refusal)
{
    Console.WriteLine($"not answered: {refusal.Message}");
}
```

A reference that does not resolve throws `UnresolvedReferenceException`, and an element that is no
longer there `GoneException`. All three derive from `SdkException`.

## Operations

The specification's operations are methods under their own names, with the specification's
parameter and result types. The SysML Toolkit has a body for a part of them (`effectiveName()`,
`resolve`, `resolveGlobal`, `resolveLocal`, `visibleMemberships`, `supertypes`, `evaluate`, ...),
and answers a call only where its evidence for your model is complete; otherwise, and for the
operations it has no body for (`isCompatibleWith`, `specializes`, `allSupertypes`, ...), the call
raises `NotImplementedInToolkitException`, as a property read does. Whether `resolve` is answered,
for example, depends on the names in your model: catch the refusal where your code can go on. One
operation name is suffixed so it cannot collide with a property: `instantiatedTypeOp()`.

<!-- test: queries toolkit -->
```csharp
Console.WriteLine(vehicle.effectiveName());                                          // Vehicle
var membership = ((Namespace)model.Resolve("Vehicles")!).resolve("Vehicle")!;       // Namespace::resolve
Console.WriteLine($"{membership.MetaclassName} {membership.memberElement!.qualifiedName}");
```

## Write a model out as JSON

The SysML Toolkit writes the model as full-form interchange JSON, with the inherited and imported
members filled in (`FullJson(false)` writes each element's own side only, as the command-line
tool does):

<!-- test: queries toolkit -->
```csharp
File.WriteAllText("model.full.json", toolkit.FullJson());
```

## Next

- [Tutorial.cs](Tutorial.cs), a runnable walk through the example model with both backends:
  `dotnet run --project examples/csharp` (payload) or `-- --toolkit`.
- [../queries/](../queries/README.md): parametric queries over whole models (reachable states,
  bills of materials, connectivity, requirements), the same in every language.
- The names of every class and member are the specification's; see
  [Names](../../README.md#names) for the rules.
- [skills/sysml-sdk-csharp](../../skills/sysml-sdk-csharp/SKILL.md) teaches a coding agent the
  same.
