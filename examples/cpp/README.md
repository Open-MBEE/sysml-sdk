# C++

Read SysML v2 and KerML models from C++17, header-only. Every metaclass of the specification is a
struct over a shared model; `is_a<T>()` follows the specification's generalizations and `as<T>()`
converts. Properties are read through getters, `get` and the specification name
(`getOwnedFeature()`, `getQualifiedName()`, and for Booleans `getIsAbstract()`); operations keep
their specification names (`effectiveName()`).

A model is read through one of two backends, with the same structs over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties, through the binding library (a native library, loaded at run time);
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  plain C++ (with the bundled nlohmann/json).

## Install

A C++17 compiler. Download from the release:

- `sysml-sdk-cpp-0.1.0.zip`, and put its `include/` on your include path. For the SysML Toolkit,
  its `lib/<target>/` holds the binding library for Windows x64, Linux x64, and macOS on Apple
  silicon and on Intel: copy the one for your platform (`sysmlv2_abi.dll`, `libsysmlv2_abi.so` or
  `libsysmlv2_abi.dylib`) beside your executable;
- `sysml_library-0.1.0.zip`, the standard library. Unpack it anywhere: it holds `sysml.library/`
  (the library's models, for the SysML Toolkit) and `sysml.library.full.json` (the same library as
  JSON, for payloads).

```sh
g++ -std=c++17 -I sysml-sdk-cpp-0.1.0/include read_model.cpp -o read_model    # add -ldl on Linux, -static with MinGW
```

## Read a model through the SysML Toolkit

Copy this, and put in your file names:

<!-- test: toolkit -->
```cpp
#include <iostream>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/toolkit.hpp>

using namespace sysml;

int main() {
    Model model = Model::from_backend(ToolkitBackend::open(
        {"model.sysml"},     // your model: one or more .sysml / .kerml files
        {},                  // or in-memory sources: {file name, text} pairs
        "sysml.library"));   // the standard library, from sysml_library-0.1.0.zip

    // ... your code here ...
    return 0;
}
```

- `ToolkitBackend::open({"a.sysml", "b.sysml"})` opens files without the standard library,
  `ToolkitBackend::open_sources({{"model.sysml", text}})` text you hold in memory.
- To load the binding library from elsewhere, point the environment variable `SYSMLV2_ABI` at it,
  or name it in code: pass `std::make_shared<ToolkitBackend::Library>(path)` as the last argument
  of `open`.
- Without the standard library the model loads faster, but its references into it
  (`ScalarValues::Real`, `ISQ::mass`, and the implicit ones such as `Parts::parts`) do not resolve,
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

using namespace sysml;

static std::string read_file(const std::string& path) {
    std::ifstream in(path, std::ios::binary);
    if (!in) throw std::runtime_error("cannot open " + path);
    std::stringstream text;
    text << in.rdbuf();
    return text.str();
}

int main() {
    auto library = PayloadLibrary::parse(read_file("sysml.library.full.json"));   // read once, share it
    Model model = Model::from_full_json(read_file("model.json"), library);         // your export

    // ... your code here ...
    return 0;
}
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they throw `not_implemented_in_toolkit`.

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
auto backend = ToolkitBackend::open({"model.sysml"}, {}, "sysml.library");
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
