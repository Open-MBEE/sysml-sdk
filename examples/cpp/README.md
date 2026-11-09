# C++

Read SysML v2 and KerML models from C++17, header-only. Every metaclass of the specification is a
struct over a shared model; `is_a<T>()` follows the specification's generalizations and `as<T>()`
converts. Properties are read through getters, `get` and the specification name
(`getOwnedFeature()`, `getQualifiedName()`, and for Booleans `getIsAbstract()`); operations keep
their specification names (`effectiveName()`).

A model is read through one of three backends, with the same structs over all of them:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties, through the binding library (a native library, loaded at run time);
- **the SysML v2 Pilot Implementation** reads them too, in a Java process (or in your process
  through JNI), and computes every value with its own code;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain C++ (with the bundled nlohmann/json).

## Install

A C++17 compiler. Download from the release:

- `sysml-sdk-cpp-0.2.0.zip`, and put its `include/` on your include path. For the SysML Toolkit,
  its `lib/<target>/` holds the binding library for Windows x64, Linux x64, and macOS on Apple
  silicon and on Intel: copy the one for your platform (`sysmlv2_abi.dll`, `libsysmlv2_abi.so` or
  `libsysmlv2_abi.dylib`) beside your executable. Its `sysml.library/` holds the standard library
  models: copy that directory beside your executable too.

```sh
g++ -std=c++17 -I sysml-sdk-cpp-0.2.0/include read_model.cpp -o read_model    # add -ldl on Linux, -static with MinGW
```

On macOS on Intel, `sysml/library.hpp` (the standard library at hand) and `sysml/engine.hpp` (the Pilot
Implementation and other engines) use `std::filesystem`, which needs macOS 10.15 or later: compile with
`-mmacosx-version-min=10.15`. The SysML Toolkit backend alone builds for 10.12.

## First: the standard library

Most models refer to the KerML and SysML standard library: `ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like. Get it once, at the start of your program, in the form your
backend reads:

```cpp
#include <sysml/library.hpp>

std::string library_dir = sysml::standard_library();         // the library's models, for the SysML Toolkit
std::string library_json = sysml::standard_library_json();   // the library as JSON, for payloads
```

- `standard_library()` finds the `sysml.library` directory beside your executable, or the one the
  environment variable `SYSML_LIBRARY_DIR` names.
- `standard_library_json()` is the file `SYSML_LIBRARY_JSON` names, else the copy in your user cache
  that the other SDK languages download (`SYSML_CACHE_DIR` moves it). C++ downloads nothing: get it
  once with another SDK language, or download `sysml_library-0.2.0.zip` from the release, unpack it,
  and set `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds.
- The library is under the Eclipse Public License 2.0: see the LICENSE and NOTICE beside it.

## Read a model through the SysML Toolkit

Copy this, and put in your file names:

<!-- test: toolkit -->
```cpp
#include <iostream>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/library.hpp>
#include <sysml/toolkit.hpp>

using namespace sysml;

int main() {
    Model model = Model::from_backend(ToolkitBackend::open(
        {"model.sysml"},         // your model: one or more .sysml / .kerml files
        {},                      // or in-memory sources: {file name, text} pairs
        standard_library()));    // the standard library

    // ... your code here ...
    return 0;
}
```

- `ToolkitBackend::open({"a.sysml", "b.sysml"})` opens files without the standard library,
  `ToolkitBackend::open_sources({{"model.sysml", text}})` text you hold in memory.
- To load the binding library from elsewhere, point the environment variable `SYSMLV2_ABI` at it,
  or name it in code: pass `std::make_shared<ToolkitBackend::Library>(path)` as the last argument
  of `open`.
- Without the standard library the model loads faster, but its references into it do not resolve,
  and reading one throws `unresolved_reference`.

## Read a model from a JSON export

A payload is the full-form interchange JSON of a model: one array of elements, each an object with
its `@id`, its `@type` and its properties. The SysML Toolkit writes one with
`sysmlv2 convert model.sysml --to full-json --lib sysml.library > model.json`, and so can other SysML
v2 tools. Its references into the standard library resolve against the library JSON:

<!-- test: payload -->
```cpp
#include <fstream>
#include <iostream>
#include <sstream>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/library.hpp>

using namespace sysml;

static std::string read_file(const std::string& path) {
    std::ifstream in(path, std::ios::binary);
    if (!in) throw std::runtime_error("cannot open " + path);
    std::stringstream text;
    text << in.rdbuf();
    return text.str();
}

int main() {
    auto library = PayloadLibrary::parse(read_file(standard_library_json()));   // the standard library; read once, share it
    Model model = Model::from_full_json(read_file("model.json"), library);         // your export

    // ... your code here ...
    return 0;
}
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they throw `not_implemented_in_toolkit`.

## Read a model through the Pilot Implementation

The SysML v2 Pilot Implementation is the open-source implementation developed alongside the
specifications. It needs a Java 21 or newer runtime (`JAVA_HOME`, or `java` on the path) and the
SDK's Pilot engine: copy `sysml-pilot-server.jar` from the archive beside your program (or name it
in `SYSML_PILOT_SERVER`).

```cpp
#include <sysml/classes.g.hpp>
#include <sysml/pilot.hpp>

using namespace sysml;

int main() {
    Model model = Model::from_backend(PilotBackend::open({"model.sysml"}));    // your .sysml / .kerml files

    // ... your code here ...
}
```

The Pilot runs in a Java process. Compiled with `SYSML_PILOT_JNI` defined and the JDK's include
directories on the include path, it runs in your process instead, in the JVM of `JAVA_HOME`:

```sh
g++ -std=c++17 -DSYSML_PILOT_JNI -I include -I "$JAVA_HOME/include" -I "$JAVA_HOME/include/linux" main.cpp -ldl
```

(`win32` or `darwin` in place of `linux` on Windows and macOS.)

- The first use downloads the Pilot (about 130 MB) into your user cache, checked against its pinned
  SHA-256; later uses, from any SDK language, find it there. `SYSML_PILOT_HOME` names a Pilot you
  already have instead: a directory with the Pilot's jar and its `sysml.library`. The Pilot is
  under the Eclipse Public License 2.0; the SDK does not carry it.
- Opening takes seconds even for a small model: the Pilot loads its standard library (it always
  reads with its own) and adds the implied relationships, which `elaborate = false` skips.
- The Pilot answers rather than refuses, with its own reading of the specification: where it and
  the SysML Toolkit differ, each is one implementation's answer. A failure inside the Pilot, an
  operation it does not have, or a Pilot that cannot be found or started raises `backend_error`.
- Text in memory: `PilotBackend::open_sources({{"model.sysml", text}})`. Options: a
  `PilotOptions` with `heap = "8g"` (a large model needs a larger Java heap; 4 GB by default) or
  `elaborate = false`.

## Find and read elements

The snippets below go where `// ... your code here ...` is, with either backend. They read the
example model [`examples/vehicle.sysml`](../vehicle.sysml); put in your own qualified names.

<!-- test: queries -->
```cpp
// An element by its qualified name (an empty optional when there is none), as its metaclass.
auto vehicle = model.resolve("Vehicles::Vehicle").value().as<PartDefinition>();
std::cout << vehicle.getQualifiedName().value() << " is a " << vehicle.metaclass_name() << "\n";   // ... is a PartDefinition

// The top-level packages.
for (const Element& root : model.roots())
    for (const Element& member : root.as<Namespace>().getOwnedMember())
        std::cout << "top level: " << member.getQualifiedName().value_or("(unnamed)") << "\n";

// Every element of a metaclass, its subclasses included.
for (const PartUsage& part : model.elements_of_type<PartUsage>())
    std::cout << "part: " << part.getQualifiedName().value_or("(unnamed)") << " " << part.metaclass_name() << "\n";

// Properties keep their specification names behind get. A list property is a std::vector, a
// single one a std::optional of an element, a std::string, a bool, an integer or a double.
std::cout << vehicle.getDeclaredName().value_or("") << " abstract: " << vehicle.getIsAbstract().value_or(false) << "\n";
for (const Documentation& doc : vehicle.getDocumentation()) std::cout << "doc: " << doc.getBody().value_or("") << "\n";
for (const Feature& feature : vehicle.getOwnedFeature())
    std::cout << "  feature: " << feature.getDeclaredName().value_or("(unnamed)") << " " << feature.metaclass_name() << "\n";

// The structs follow the specification's generalization hierarchy.
std::cout << vehicle.is_a<Definition>() << vehicle.is_a<Type>() << vehicle.is_a<Usage>() << "\n";   // 110
```

## When the SysML Toolkit does not answer

The SysML Toolkit answers a property only where it can derive it completely. Where it cannot
yet, reading the property throws `not_implemented_in_toolkit` with the SysML Toolkit's reason,
instead of returning a value that might be wrong; nothing is silently empty. Catch it where your
code can go on.

The relationships a derived property is computed from are part of the model, and the SysML Toolkit
answers them: a feature's types are the `getType()` of its `getOwnedTyping()` relationships, a
definition's supertypes the `getSuperclassifier()` of its `getOwnedSubclassification()`s, what a
feature subsets the `getSubsettedFeature()` of its `getOwnedSubsetting()`s.

<!-- test: queries -->
```cpp
auto wheels = model.resolve("Vehicles::Vehicle::wheels").value().as<PartUsage>();
std::vector<Type> types;
try {
    types = wheels.getType();                                        // derived: the SysML Toolkit may refuse it
} catch (const not_implemented_in_toolkit& refusal) {
    std::cout << "not answered: " << refusal.what() << "\n";
    for (const FeatureTyping& typing : wheels.getOwnedTyping())      // what the model states
        types.push_back(typing.getType().value());
}
for (const Type& t : types) std::cout << "wheels: " << t.getQualifiedName().value_or("?") << "\n";   // Vehicles::Wheel

auto sports = model.resolve("Vehicles::SportsCar").value().as<PartDefinition>();
for (const Subclassification& s : sports.getOwnedSubclassification())
    std::cout << "SportsCar specializes " << s.getSuperclassifier().value().getQualifiedName().value_or("?") << "\n";
try {
    for (const Feature& f : sports.getInheritedFeature())
        if (f.getIsLibraryElement() != true) std::cout << "SportsCar inherits " << f.getDeclaredName().value_or("(unnamed)") << "\n";
} catch (const not_implemented_in_toolkit& refusal) {
    std::cout << "not answered: " << refusal.what() << "\n";
}
```

A reference that does not resolve throws `unresolved_reference`, and an element that is no longer
there `gone`. All derive from `sysml::sdk_error`, a `std::runtime_error`.

## Convenience members

Beside the specification's members, the structs have `find_` members for what takes several steps
in the specification: the parts of a definition, with the inherited ones or without, a member by
name, the definitions a package owns, a value as a number. The SDK defines each one over the
specification's properties and operations, and the SysML Toolkit and the Pilot Implementation
answer them alike; a payload does not (`not_answered`). Each member's definition is in its Doxygen
comment.

<!-- test: queries toolkit -->
```cpp
// vehicle, sports and wheels are the elements found above.
auto vehicles = model.resolve("Vehicles").value().as<Namespace>();
auto name_of = [](const Element& e) { return e.getName().value_or(e.metaclass_name()); };
auto print = [&](const auto& elements) {
    for (const auto& e : elements) std::cout << name_of(e) << " ";
    std::cout << "\n";
};

// The features of one kind that a type owns: parts here; attributes, ports, actions, states and the
// other kinds alike. A connection is a part too; the exact list leaves the subkinds out.
print(vehicle.find_parts());          // wheels engine tank ConnectionUsage
print(vehicle.find_parts_exact());    // wheels engine tank
std::cout << sports.find_part("spoiler")->getQualifiedName().value_or("") << "\n";   // Vehicles::SportsCar::spoiler

// The definitions a namespace owns, as a list, or by name (a NameMap: a vector of pairs, with find).
print(vehicles.find_part_defs());     // Wheel Engine FuelTank Vehicle SportsCar
std::cout << vehicles.find_part_defs_by_name().find("Engine")->getQualifiedName().value_or("") << "\n";   // Vehicles::Engine

// A list carries the same members, asked of each element in turn: the ports of every part definition.
print(vehicles.find_part_defs().find_ports());    // fuelIn fuelOut

// Types, multiplicities, values and documentation. A literal's value, and a bound, is a Literal: a
// std::variant of std::int64_t, double, bool, std::string and Unbounded (*).
print(wheels.find_declared_types());              // Wheel
std::cout << std::get<std::int64_t>(*wheels.find_lower_bound()) << " "
          << std::get<std::int64_t>(*wheels.find_upper_bound()) << "\n";    // 4 4
auto mass = model.resolve("Vehicles::myCar::mass").value().as<Feature>();
std::cout << std::get<std::int64_t>(mass.find_value()->find_literal()) << " "
          << mass.find_redefinition_root()->getQualifiedName().value_or("") << "\n";   // 1400 Vehicles::Vehicle::mass
for (const std::string& text : vehicle.find_doc_text()) std::cout << text << "\n";   // The general vehicle: four wheels, one engine, one tank.

// The inherited members too: Scope::Inherited, or Scope::All for both (Scope::Owned is the default).
try {
    print(sports.find_parts(Scope::All));     // spoiler wheels engine tank ConnectionUsage
} catch (const not_answered& refusal) {
    std::cout << "not answered: " << refusal.what() << "\n";   // where the backend cannot derive the inherited members
}
```

- Every list takes `scope`, `visibility` (`VisibilityFilter::Public`, `VisibilityFilter::Protected`
  for public and protected; `VisibilityFilter::Any` by default) and `library` (`true` keeps the
  features the standard library's types contribute, which are left out by default).
- A list of elements is an `ElementList<T>`, a `std::vector<T>` that carries the members its
  elements have, so calls chain.
- For each of 27 kinds of feature: `find_parts()`, `find_part(name)`, `find_parts_semantic()` (the
  features typed by a part definition, whatever their metaclass) and, for most,
  `find_parts_exact()`. On a namespace, for each kind of definition: `find_part_defs()`,
  `find_part_def(name)` and `find_part_defs_by_name()`, and the usages it has through any
  membership, `find_member_parts()`.
- One by one: the types and supertypes as written, what a feature subsets, redefines or references
  (`find_redefinition_root()` follows its redefinitions to the first), values and literals, the
  bounds of a multiplicity, the parameters, inputs and outputs of a behavior, what an accept action
  or a transition accepts, applied metadata, documentation.
- Where a member's definition gives no answer, it throws `refused`: `find_literal()` of an
  expression that is not a literal (`2 * x`, a value with a unit). Where the backend cannot answer
  something the member reads, it throws `not_answered`, naming what. Today the SysML Toolkit answers
  430 of the members' 456 examples; it does not answer `type` on action, state and other behavioral
  usages and on enumeration values (which the semantic lists of those kinds read), a metadata
  usage's `metaclass`, nor the member lists of a definition with a public import in its body.
- A member or definition list outside the owned scope is in the backend's order: the specification
  does not define the order of a namespace's memberships.
- Every member, with its definition, its signature in each language and its examples: the release's
  `sysml-sdk-reference-<version>.html`.

## Operations

The specification's operations are member functions under their own names, with the specification's
parameter and result types. The SysML Toolkit has a body for a part of them (`effectiveName()`,
`resolve`, `resolveGlobal`, `resolveLocal`, `visibleMemberships`, `supertypes`, `evaluate`, ...),
and answers a call only where its evidence for your model is complete; otherwise, and for the
operations it has no body for (`isCompatibleWith`, `specializes`, `allSupertypes`, ...), the call
raises `not_implemented_in_toolkit`, as a property read does. Whether `resolve` is answered, for
example, depends on the names in your model: catch the refusal where your code can go on.

<!-- test: queries toolkit -->
```cpp
std::cout << vehicle.effectiveName().value_or("") << "\n";                                   // Vehicle
auto membership = model.resolve("Vehicles").value().as<Namespace>().resolve("Vehicle").value();   // Namespace::resolve
std::cout << membership.metaclass_name() << " " << membership.getMemberElement().value().getQualifiedName().value_or("?") << "\n";
```

## Write a model out as JSON

The SysML Toolkit writes the model as full-form interchange JSON, with the inherited and imported
members filled in (`full_json(false)` writes each element's own side only, as the command-line
tool does). `full_json` belongs to the backend, so keep it before handing it to the model:

<!-- test: queries toolkit -->
```cpp
auto backend = ToolkitBackend::open({"model.sysml"}, {}, standard_library());
std::cout << backend->full_json().size() << " bytes of JSON\n";   // write it where you need it
Model exported = Model::from_backend(std::move(backend));          // the same session, as a model
```

## Next

- [tutorial.cpp](tutorial.cpp), a runnable walk through the example model with both backends; its
  header has the commands.
- [../queries/](../queries/README.md): parametric queries over whole models (reachable states,
  bills of materials, connectivity, requirements), the same in every language.
- The names of every class and member are the specification's; see
  [Names](../../README.md#names) for the rules.
- [skills/sysml-sdk-cpp](../../skills/sysml-sdk-cpp/SKILL.md) teaches a coding agent the same.
