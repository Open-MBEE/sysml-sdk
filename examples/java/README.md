# Java

Read SysML v2 and KerML models from Java. Every metaclass of the specification is a Java
interface, so `instanceof` follows the specification's generalizations. Properties are read through
getters, `get` and the specification name (`getOwnedFeature()`, `getQualifiedName()`, and for
Booleans `getIsAbstract()`); operations keep their specification names (`effectiveName()`).

A model is read through one of two backends, with the same interfaces over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties, through the binding library (a native library, called with the
  foreign function API);
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain Java.

## Install

JDK 21 or newer; the jar has no dependencies. Download from the release:

- `sysml-sdk-0.1.0.jar`, to put on the class path. It carries the binding library
  for Windows x64, Linux x64, and macOS on Apple silicon and on Intel, and copies the one for your
  platform into the temporary directory on first use;
- `sysml_library-0.1.0.zip`, the standard library. Unpack it anywhere: it holds `sysml.library/`
  (the library's models, for the SysML Toolkit) and `sysml.library.full.json` (the same library as
  JSON, for payloads).

Compile and run against the jar. On JDK 21 the foreign function API is a preview, so add
`--enable-preview` (and `--release 21` to `javac`); from JDK 22 on, leave both out.
`--enable-native-access=ALL-UNNAMED` silences the JDK's warning about native access.

```sh
javac --enable-preview --release 21 -cp sysml-sdk-0.1.0.jar ReadModel.java
java --enable-preview --enable-native-access=ALL-UNNAMED -cp sysml-sdk-0.1.0.jar:. ReadModel    # ; instead of : on Windows
```

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
                "sysml.library")) {          // the standard library, from sysml_library-0.1.0.zip
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
- Without the standard library the model loads faster, but its references into it
  (`ScalarValues::Real`, `ISQ::mass`, and the implicit ones such as `Parts::parts`) do not resolve,
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
        PayloadLibrary library = PayloadLibrary.fromJson(Files.readString(Path.of("sysml.library.full.json")));   // read once, share it
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
