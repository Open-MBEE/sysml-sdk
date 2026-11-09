# Java

Read SysML v2 and KerML models from Java. Every metaclass of the specification is a Java
interface, so `instanceof` follows the specification's generalizations. Properties are read through
getters, `get` and the specification name (`getOwnedFeature()`, `getQualifiedName()`, and for
Booleans `getIsAbstract()`); operations keep their specification names (`effectiveName()`).

A model is read through one of three backends, with the same interfaces over all of them:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties, through the binding library (a native library, called with the
  foreign function API);
- **the SysML v2 Pilot Implementation** reads them too, in your Java process, and computes every
  value with its own code;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain Java.

## Install

JDK 21 or newer; the jar has no dependencies. Download from the release:

- `sysml-sdk-0.2.0.jar`, to put on the class path. It carries the binding library
  for Windows x64, Linux x64, and macOS on Apple silicon and on Intel, and copies the one for your
  platform into the temporary directory on first use. It also carries the standard library models.

Compile and run against the jar. On JDK 21 the foreign function API is a preview, so add
`--enable-preview` (and `--release 21` to `javac`); from JDK 22 on, leave both out.
`--enable-native-access=ALL-UNNAMED` silences the JDK's warning about native access.

```sh
javac --enable-preview --release 21 -cp sysml-sdk-0.2.0.jar ReadModel.java
java --enable-preview --enable-native-access=ALL-UNNAMED -cp sysml-sdk-0.2.0.jar:. ReadModel    # ; instead of : on Windows
```

## First: the standard library

Most models refer to the KerML and SysML standard library: `ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like. Get it once, at the start of your program, in the form your
backend reads:

```java
Path libraryDir = StandardLibrary.directory();   // the library's models the jar carries, for the SysML Toolkit
Path libraryJson = StandardLibrary.json();       // the library as JSON, for payloads
```

- `StandardLibrary.directory()` copies the models out of the jar into your user cache on its first
  call (a session reads a directory).
- `StandardLibrary.json()` downloads the JSON (about 11 MB) from this version's GitHub release on its
  first call, checks it against the SHA-256 GitHub states for the file, and keeps it in your user
  cache (`SYSML_CACHE_DIR` moves it); later calls, from any SDK language, read the cache. Nothing is
  downloaded unless you call it.
- Without access to GitHub, download `sysml_library-0.2.0.zip` from the release, unpack it, and set
  `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds. Its `sysml.library/` directory is
  the same models the jar carries.
- The library is under the Eclipse Public License 2.0: see the LICENSE and NOTICE beside it.

## Read a model through the SysML Toolkit

Copy this, and put in your file names:

<!-- test: toolkit -->
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.openmbee.sysml.*;
import org.openmbee.sysml.classes.*;

public class ReadModel {
    public static void main(String[] args) throws Exception {
        try (ToolkitBackend toolkit = ToolkitBackend.open(ToolkitBackend.library(),   // the jar's binding library, or SYSMLV2_ABI's
                List.of("model.sysml"),      // your model: one or more .sysml / .kerml files
                null,                        // or in-memory sources: a Map from file name to text
                StandardLibrary.directory().toString())) {   // the standard library
            Model model = new Model(toolkit);

            // ... your code here ...
        }
    }
}
```

- `ToolkitBackend.open(List.of("a.sysml", "b.sysml"))` opens files without the standard library,
  `ToolkitBackend.openSources(Map.of("model.sysml", text))` text you hold in memory.
- To use another build of the binding library, point the environment variable `SYSMLV2_ABI` at
  it, or name it in code: `new ToolkitBackend.Library(Path.of(...))` in place of
  `ToolkitBackend.library()`.
- Without the standard library the model loads faster, but its references into it do not resolve,
  and reading one throws `UnresolvedReferenceException`.

## Read a model from a JSON export

A payload is the full-form interchange JSON of a model: one array of elements, each an object with
its `@id`, its `@type` and its properties. The SysML Toolkit writes one with
`sysmlv2 convert model.sysml --to full-json --lib sysml.library > model.json`, and so can other SysML
v2 tools. Its references into the standard library resolve against the library JSON:

<!-- test: payload -->
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.openmbee.sysml.*;
import org.openmbee.sysml.classes.*;

public class ReadExport {
    public static void main(String[] args) throws Exception {
        PayloadLibrary library = PayloadLibrary.fromJson(Files.readString(StandardLibrary.json()));   // the standard library; read once, share it
        Model model = Model.fromFullJson(Files.readString(Path.of("model.json")), library);                    // your export

        // ... your code here ...
    }
}
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they throw `NotImplementedInToolkitException`.

`org.openmbee.sysml.classes` has a `Class` and a `Package`, as `java.lang` does: to use
the metaclasses, import them by name (`import org.openmbee.sysml.classes.Package;`),
which takes precedence over `java.lang`.

## Read a model through the Pilot Implementation

The SysML v2 Pilot Implementation is the open-source implementation developed alongside the
specifications. It runs in your program's Java process, in a class loader of its own (its
libraries never meet yours), and needs Java 21 or newer:

```java
import java.util.List;

import org.openmbee.sysml.*;
import org.openmbee.sysml.classes.*;

public class Main {
    public static void main(String[] args) {
        try (PilotBackend pilot = PilotBackend.open(List.of("model.sysml"))) {  // your .sysml / .kerml files
            Model model = new Model(pilot);

            // ... your code here ...
        }
    }
}
```

- The first use downloads the Pilot (about 130 MB) into your user cache, checked against its pinned
  SHA-256; later uses, from any SDK language, find it there. `SYSML_PILOT_HOME` names a Pilot you
  already have instead: a directory with the Pilot's jar and its `sysml.library`. The Pilot is
  under the Eclipse Public License 2.0; the SDK does not carry it.
- Opening takes seconds even for a small model: the Pilot loads its standard library (it always
  reads with its own) and adds the implied relationships, which `elaborate` false skips.
- The Pilot answers rather than refuses, with its own reading of the specification: where it and
  the SysML Toolkit differ, each is one implementation's answer. A failure inside the Pilot, an
  operation it does not have, or a Pilot that cannot be found or started raises `BackendException`.
- Text in memory: `PilotBackend.openSources(Map.of("model.sysml", text))`; a Pilot of your own,
  or no elaboration: `PilotBackend.open(Pilot.at(Path.of(jar), Path.of(library)), paths, sources,
  elaborate)` (`Pilot` is `org.openmbee.sysml.pilot.Pilot`; `null` for the Pilot the SDK finds). A large
  model needs a larger heap: run with `-Xmx4g` or more.

## Find and read elements

The snippets below go where `// ... your code here ...` is, with either backend. They read the
example model [`examples/vehicle.sysml`](../vehicle.sysml); put in your own qualified names.

<!-- test: queries -->
```java
// An element by its qualified name (null when there is none), cast to its metaclass.
PartDefinition vehicle = (PartDefinition) model.resolve("Vehicles::Vehicle");
System.out.println(vehicle.getQualifiedName() + " is a " + vehicle.$metaclass());   // Vehicles::Vehicle is a PartDefinition

// The top-level packages.
for (Element root : model.roots()) {
    for (Element member : ((Namespace) root).getOwnedMember()) System.out.println("top level: " + member.getQualifiedName());
}

// Every element of a metaclass, its subclasses included.
for (PartUsage part : model.elementsOfType(PartUsage.class)) {
    System.out.println("part: " + part.getQualifiedName() + " " + part.$metaclass());
}

// Properties keep their specification names behind get. A list property is a List, a single one
// an element, a String, a Boolean, a Long, a Double or null.
System.out.println(vehicle.getDeclaredName() + " " + vehicle.getIsAbstract()
    + " " + vehicle.getDocumentation().stream().map(doc -> doc.getBody().trim()).toList());
for (Feature feature : vehicle.getOwnedFeature()) {
    System.out.println("  feature: " + feature.getDeclaredName() + " " + feature.$metaclass());
}

// The interfaces form the specification's generalization hierarchy.
System.out.println((vehicle instanceof Definition) + " " + (vehicle instanceof Type) + " " + (model.resolve("Vehicles::myCar") instanceof Usage));
```

## When the SysML Toolkit does not answer

The SysML Toolkit answers a property only where it can derive it completely. Where it cannot yet,
reading the property throws `NotImplementedInToolkitException` with the SysML Toolkit's reason,
instead of returning a value that might be wrong; nothing is silently empty. Catch it where your
code can go on.

The relationships a derived property is computed from are part of the model, and the SysML Toolkit
answers them: a feature's types are the `getType()` of its `getOwnedTyping()` relationships, a
definition's supertypes the `getSuperclassifier()` of its `getOwnedSubclassification()`s, what a
feature subsets the `getSubsettedFeature()` of its `getOwnedSubsetting()`s.

<!-- test: queries -->
```java
PartUsage wheels = (PartUsage) model.resolve("Vehicles::Vehicle::wheels");
List<Type> types;
try {
    types = wheels.getType();                                                   // derived: the SysML Toolkit may refuse it
} catch (NotImplementedInToolkitException refusal) {
    System.out.println("not answered: " + refusal.getMessage());
    types = wheels.getOwnedTyping().stream().map(FeatureTyping::getType).toList();   // what the model states
}
System.out.println("wheels: " + types.stream().map(Type::getQualifiedName).toList());   // [Vehicles::Wheel]

PartDefinition sports = (PartDefinition) model.resolve("Vehicles::SportsCar");
System.out.println("SportsCar specializes "
    + sports.getOwnedSubclassification().stream().map(s -> s.getSuperclassifier().getQualifiedName()).toList());
try {
    System.out.println("SportsCar inherits " + sports.getInheritedFeature().stream()
        .filter(f -> !Boolean.TRUE.equals(f.getIsLibraryElement())).map(Feature::getDeclaredName).toList());
} catch (NotImplementedInToolkitException refusal) {
    System.out.println("not answered: " + refusal.getMessage());
}
```

A reference that does not resolve throws `UnresolvedReferenceException`, and an element that is no
longer there `GoneException`. All three derive from `SdkException`, an unchecked exception.

## Convenience members

Beside the specification's members, the interfaces have `find` members for what takes several
steps in the specification: the parts of a definition, with the inherited ones or without, a member
by name, the definitions a package owns, a value as a number. The SDK defines each one over the
specification's properties and operations, and the SysML Toolkit and the Pilot Implementation
answer them alike; a payload does not (`NotAnsweredException`). Your editor shows each member's
definition in its Javadoc.

<!-- test: queries toolkit -->
```java
// vehicle, sports and wheels are the elements found above.
Namespace vehicles = (Namespace) model.resolve("Vehicles");
java.util.function.Function<Element, String> nameOf = e -> e.getName() != null ? e.getName() : e.$metaclass();

// The features of one kind that a type owns: parts here; attributes, ports, actions, states and the
// other kinds alike. A connection is a part too; the exact list leaves the subkinds out.
System.out.println(vehicle.findParts().stream().map(nameOf).toList());        // [wheels, engine, tank, ConnectionUsage]
System.out.println(vehicle.findPartsExact().stream().map(nameOf).toList());   // [wheels, engine, tank]
System.out.println(sports.findPart("spoiler").getQualifiedName());            // Vehicles::SportsCar::spoiler

// The definitions a namespace owns, as a list, or by name (a Map).
System.out.println(vehicles.findPartDefs().stream().map(nameOf).toList());    // [Wheel, Engine, FuelTank, Vehicle, SportsCar]
System.out.println(vehicles.findPartDefsByName().get("Engine").getQualifiedName());   // Vehicles::Engine

// A list carries the same members, asked of each element in turn: the ports of every part definition.
System.out.println(vehicles.findPartDefs().findPorts().stream().map(PortUsage::getQualifiedName).toList());
// [Vehicles::Engine::fuelIn, Vehicles::FuelTank::fuelOut]

// Types, multiplicities, values and documentation. A literal's value, and a bound, is a Literal: an
// integer, a real, a Boolean, a string, or unbounded (*); asLong() and the like read it, and a
// switch over its cases tells them apart.
System.out.println(wheels.findDeclaredTypes().stream().map(nameOf).toList()
    + " " + wheels.findLowerBound().asLong() + " " + wheels.findUpperBound().asLong());   // [Wheel] 4 4
Feature mass = (Feature) model.resolve("Vehicles::myCar::mass");
long kilograms = mass.findValue().findLiteral(Long.class);                    // or findLiteral().asLong()
System.out.println(kilograms + " " + mass.findRedefinitionRoot().getQualifiedName());   // 1400 Vehicles::Vehicle::mass
System.out.println(vehicle.findDocText().stream().map(String::strip).toList());   // [The general vehicle: four wheels, one engine, one tank.]

// The inherited members too: Scope.INHERITED, or Scope.ALL for both (Scope.OWNED is the default).
try {
    System.out.println(sports.findParts(Scope.ALL).stream().map(nameOf).toList());
    // [spoiler, wheels, engine, tank, ConnectionUsage]
} catch (NotAnsweredException refusal) {
    System.out.println("not answered: " + refusal.getMessage());   // where the backend cannot derive the inherited members
}
```

- Every list takes `scope`, `visibility` (`VisibilityFilter.PUBLIC`, `VisibilityFilter.PROTECTED`
  for public and protected; `VisibilityFilter.ANY` by default) and `library` (`true` keeps the
  features the standard library's types contribute, which are left out by default); the shorter
  overloads leave the rest at their defaults.
- A list of elements is a typed list (`PartUsageList`, a `List<PartUsage>`) that carries the members
  its elements have, so calls chain and your IDE offers only those.
- For each of 27 kinds of feature: `findParts()`, `findPart(name)`, `findPartsSemantic()` (the
  features typed by a part definition, whatever their metaclass) and, for most, `findPartsExact()`.
  On a namespace, for each kind of definition: `findPartDefs()`, `findPartDef(name)` and
  `findPartDefsByName()`, and the usages it has through any membership, `findMemberParts()`.
- One by one: the types and supertypes as written, what a feature subsets, redefines or references
  (`findRedefinitionRoot()` follows its redefinitions to the first), values and literals, the bounds
  of a multiplicity, the parameters, inputs and outputs of a behavior, what an accept action or a
  transition accepts, applied metadata, documentation.
- Where a member's definition gives no answer, it throws `RefusedException`: `findLiteral()` of an
  expression that is not a literal (`2 * x`, a value with a unit). Where the backend cannot answer
  something the member reads, it throws `NotAnsweredException`, naming what. Today the SysML Toolkit
  answers 430 of the members' 456 examples; it does not answer `type` on action, state and other
  behavioral usages and on enumeration values (which the semantic lists of those kinds read), a
  metadata usage's `metaclass`, nor the member lists of a definition with a public import in its
  body.
- A member or definition list outside the owned scope is in the backend's order: the specification
  does not define the order of a namespace's memberships.
- Every member, with its definition, its signature in each language and its examples: the release's
  `sysml-sdk-reference-<version>.html`.

## Operations

The specification's operations are methods under their own names, with the specification's
parameter and result types. The SysML Toolkit has a body for a part of them (`effectiveName()`,
`resolve`, `resolveGlobal`, `resolveLocal`, `visibleMemberships`, `supertypes`, `evaluate`, ...),
and answers a call only where its evidence for your model is complete; otherwise, and for the
operations it has no body for (`isCompatibleWith`, `specializes`, `allSupertypes`, ...), the call
raises `NotImplementedInToolkitException`, as a property read does. Whether `resolve` is answered,
for example, depends on the names in your model: catch the refusal where your code can go on.

<!-- test: queries toolkit -->
```java
System.out.println(vehicle.effectiveName());                                            // Vehicle
Membership membership = ((Namespace) model.resolve("Vehicles")).resolve("Vehicle");     // Namespace::resolve
System.out.println(membership.$metaclass() + " " + membership.getMemberElement().getQualifiedName());
```

## Write a model out as JSON

The SysML Toolkit writes the model as full-form interchange JSON, with the inherited and imported
members filled in (`fullJson(false)` writes each element's own side only, as the command-line
tool does):

<!-- test: queries toolkit -->
```java
Files.writeString(Path.of("model.full.json"), toolkit.fullJson());
```

## Next

- [Tutorial.java](Tutorial.java), a runnable walk through the example model with both backends; its
  header has the commands.
- [../queries/](../queries/README.md): parametric queries over whole models (reachable states,
  bills of materials, connectivity, requirements), the same in every language.
- The names of every class and member are the specification's; see
  [Names](../../README.md#names) for the rules.
- [skills/sysml-sdk-java](../../skills/sysml-sdk-java/SKILL.md) teaches a coding agent the
  same.
