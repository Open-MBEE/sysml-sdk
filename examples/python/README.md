# Python

Read SysML v2 and KerML models from Python. Every metaclass of the specification is a Python
class, and every property and operation keeps its specification name: `ownedFeature`,
`qualifiedName`, `effectiveName()`.

A model is read through one of three backends, with the same classes over all of them:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties;
- **the SysML v2 Pilot Implementation** reads them too, in Java (in your Python process through
  JPype, or in a Java process), and computes every value with its own code;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  pure Python.

## Install

Python 3.10 or newer:

```sh
pip install sysml
```

The wheels (Windows x64, Linux x64 with glibc 2.28 or later, macOS on Apple silicon and on Intel)
carry the binding library and the standard library models; nothing else is needed. The release has
the same wheels to install from a file (`pip install sysml-0.2.0-py3-none-win_amd64.whl`, or
`manylinux_2_28_x86_64`, `macosx_11_0_arm64`, `macosx_10_12_x86_64`). On other platforms pip installs
the source distribution, which reads payloads only.

## First: the standard library

Most models refer to the KerML and SysML standard library: `ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like. Get it once, at the start of your program, in the form your
backend reads:

```python
from sysml import standard_library, standard_library_json

library_dir = standard_library()          # the library's models the wheel carries, for the SysML Toolkit
library_json = standard_library_json()    # the library as JSON, for payloads
```

- `standard_library_json()` downloads the JSON (about 11 MB) from this version's GitHub release on
  its first call, checks it against the SHA-256 GitHub states for the file, and keeps it in your
  user cache (`SYSML_CACHE_DIR` moves it); later calls, from any SDK language, read the cache.
  Nothing is downloaded unless you call it.
- Without access to GitHub, download `sysml_library-0.2.0.zip` from the release, unpack it, and set
  `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds. Its `sysml.library/` directory is
  the same models the wheel carries.
- The library is under the Eclipse Public License 2.0: see the LICENSE and NOTICE beside it.

## Read a model through the SysML Toolkit

Copy this, and put in your file names:

<!-- test: toolkit -->
```python
from sysml import Model, NotImplementedInToolkit, UnresolvedReference, standard_library
from sysml.classes import *

model = Model.from_toolkit(
    "model.sysml",                      # your model: one or more .sysml / .kerml files
    library_dir=standard_library(),     # the standard library
)

# ... your code here ...
```

- A model in several files: pass them all, `Model.from_toolkit("a.sysml", "b.sysml", library_dir=...)`.
- Text you already hold in memory: `Model.from_toolkit(sources={"model.sysml": text}, library_dir=...)`.
- Without `library_dir` the model loads faster, but its references into the standard library
  do not resolve, and reading one raises `UnresolvedReference`.
- `SYSMLV2_ABI=<path to sysmlv2_abi library>` makes the SDK use another build of the binding
  library than the wheel's.

## Read a model from a JSON export

A payload is the full-form interchange JSON of a model: one array of elements, each an object with
its `@id`, its `@type` and its properties. The SysML Toolkit writes one with
`sysmlv2 convert model.sysml --to full-json --lib sysml.library > model.json`, and so can other SysML
v2 tools. Its references into the standard library resolve against the library JSON:

<!-- test: payload -->
```python
import json

from sysml import Model, NotImplementedInToolkit, PayloadLibrary, UnresolvedReference, standard_library_json
from sysml.classes import *

with open(standard_library_json(), encoding="utf-8") as f:
    library = PayloadLibrary(json.load(f))    # the standard library; read once, share it
with open("model.json", encoding="utf-8") as f:
    model = Model.from_full_json(json.load(f), library=library)    # your export

# ... your code here ...
```

A payload answers with what its writer wrote and nothing else. Operations need the SysML Toolkit:
on a payload they raise `NotImplementedInToolkit`.

## Read a model through the Pilot Implementation

The SysML v2 Pilot Implementation is the open-source implementation developed alongside the
specifications. It needs a Java 21 or newer runtime (`JAVA_HOME`, or `java` on the path). With
JPype installed it runs in your Python process, otherwise in a Java process the SDK starts:

```sh
pip install "sysml[pilot]"     # optional: JPype, for the Pilot in this process
```

```python
from sysml import BackendError, Model
from sysml.classes import *

model = Model.from_pilot("model.sysml")    # your model: one or more .sysml / .kerml files

# ... your code here ...
```

- The first use downloads the Pilot (about 130 MB) into your user cache, checked against its pinned
  SHA-256; later uses, from any SDK language, find it there. `SYSML_PILOT_HOME` names a Pilot you
  already have instead: a directory with the Pilot's jar and its `sysml.library`. The Pilot is
  under the Eclipse Public License 2.0; the SDK does not carry it.
- Opening takes seconds even for a small model: the Pilot loads its standard library (it always
  reads with its own) and adds the implied relationships, which `elaborate=False` skips.
- The Pilot answers rather than refuses, with its own reading of the specification: where it and
  the SysML Toolkit differ, each is one implementation's answer. A failure inside the Pilot, an
  operation it does not have, or a Pilot that cannot be found or started raises `BackendError`.
- Text in memory: `Model.from_pilot(sources={"model.sysml": text})`. A large model needs a larger
  Java heap: `heap="8g"` (4 GB by default). `transport="process"` runs the Pilot in a Java process
  even with JPype installed.
- To stop the Pilot before your program ends, open the backend yourself:
  `with PilotBackend.open(["model.sysml"]) as pilot: model = Model(pilot)` (`from sysml.pilot
  import PilotBackend`).

## Find and read elements

The snippets below go where `# ... your code here ...` is, with either backend. They read the
example model [`examples/vehicle.sysml`](../vehicle.sysml); put in your own qualified names.

<!-- test: queries -->
```python
# An element by its qualified name (None when there is none).
vehicle = model.resolve("Vehicles::Vehicle")
print(vehicle.qualifiedName, "is a", type(vehicle).__name__)     # Vehicles::Vehicle is a PartDefinition

# The top-level packages.
for root in model.roots:
    for member in root.ownedMember:
        print("top level:", member.qualifiedName)

# Every element of a metaclass, its subclasses included (exact=True for that metaclass only).
for part in model.elements_of_type(PartUsage):
    print("part:", part.qualifiedName or "(unnamed)", type(part).__name__)

# Properties keep their specification names. A list property is a Python list, a single one an
# element, a str, a bool, a number or None.
print(vehicle.declaredName, vehicle.isAbstract, [doc.body.strip() for doc in vehicle.documentation])
for feature in vehicle.ownedFeature:
    print("  feature:", feature.declaredName, type(feature).__name__)

# The classes form the specification's generalization hierarchy.
print(isinstance(vehicle, Definition), isinstance(vehicle, Type), isinstance(vehicle, Usage))
```

## When the SysML Toolkit does not answer

The SysML Toolkit answers a property only where it can derive it completely. Where it cannot
yet, reading the property raises `NotImplementedInToolkit` with the SysML Toolkit's reason,
instead of returning a value that might be wrong; nothing is silently empty. Catch it where your
code can go on.

The relationships a derived property is computed from are part of the model, and the SysML Toolkit
answers them: a feature's types are the `type` of its `ownedTyping` relationships, a definition's
supertypes the `superclassifier` of its `ownedSubclassification`s, what a feature subsets the
`subsettedFeature` of its `ownedSubsetting`s.

<!-- test: queries -->
```python
wheels = model.resolve("Vehicles::Vehicle::wheels")
try:
    types = wheels.type                                          # derived: the SysML Toolkit may refuse it
except NotImplementedInToolkit as refusal:
    print("not answered:", refusal)
    types = [typing.type for typing in wheels.ownedTyping]      # what the model states
print("wheels:", [t.qualifiedName for t in types])              # ['Vehicles::Wheel']

sports = model.resolve("Vehicles::SportsCar")
print("SportsCar specializes", [s.superclassifier.qualifiedName for s in sports.ownedSubclassification])
try:
    print("SportsCar inherits", [f.declaredName for f in sports.inheritedFeature if not f.isLibraryElement])
except NotImplementedInToolkit as refusal:
    print("not answered:", refusal)
```

A reference that does not resolve raises `UnresolvedReference`, and an element that is no longer
there `Gone`. All three errors derive from `sysml.SdkError`.

## Convenience members

Beside the specification's members, the classes have `find_` members for what takes several steps
in the specification: the parts of a definition, with the inherited ones or without, a member by
name, the definitions a package owns, a value as a number. The SDK defines each one over the
specification's properties and operations, and the SysML Toolkit and the Pilot Implementation
answer them alike; a payload does not (`NotAnswered`). Your editor shows each member's definition
on hover.

<!-- test: queries toolkit -->
```python
from sysml import NotAnswered, Scope

vehicles = model.resolve("Vehicles")    # vehicle, sports and wheels are the elements found above

# The features of one kind that a type owns: parts here; attributes, ports, actions, states and the
# other kinds alike. A connection is a part too; the exact list leaves the subkinds out.
print([p.name or type(p).__name__ for p in vehicle.find_parts()])    # ['wheels', 'engine', 'tank', 'ConnectionUsage']
print([p.name for p in vehicle.find_parts_exact()])                  # ['wheels', 'engine', 'tank']
print(sports.find_part("spoiler").qualifiedName)                     # Vehicles::SportsCar::spoiler

# The definitions a namespace owns, as a list, or by name.
print([d.name for d in vehicles.find_part_defs()])    # ['Wheel', 'Engine', 'FuelTank', 'Vehicle', 'SportsCar']
print(vehicles.find_part_defs_by_name()["Engine"].qualifiedName)    # Vehicles::Engine

# A list carries the same members, asked of each element in turn: the ports of every part definition.
print([p.qualifiedName for p in vehicles.find_part_defs().find_ports()])
# ['Vehicles::Engine::fuelIn', 'Vehicles::FuelTank::fuelOut']

# Types, multiplicities, values and documentation.
print([t.name for t in wheels.find_declared_types()], wheels.find_lower_bound(), wheels.find_upper_bound())    # ['Wheel'] 4 4
mass = model.resolve("Vehicles::myCar::mass")
print(mass.find_value().find_literal(), mass.find_redefinition_root().qualifiedName)    # 1400 Vehicles::Vehicle::mass
print([text.strip() for text in vehicle.find_doc_text()])    # ['The general vehicle: four wheels, one engine, one tank.']

# The inherited members too: Scope.INHERITED, or Scope.ALL for both (Scope.OWNED is the default).
try:
    print([p.name or type(p).__name__ for p in sports.find_parts(Scope.ALL)])
    # ['spoiler', 'wheels', 'engine', 'tank', 'ConnectionUsage']
except NotAnswered as refusal:
    print("not answered:", refusal)    # where the backend cannot derive the inherited members
```

- Every list takes `scope`, `visibility` (`VisibilityFilter.PUBLIC`, `VisibilityFilter.PROTECTED`
  for public and protected; `VisibilityFilter.ANY` by default) and `library` (`True` keeps the
  features the standard library's types contribute, which are left out by default).
- For each of 27 kinds of feature: `find_parts()`, `find_part(name)`, `find_parts_semantic()` (the
  features typed by a part definition, whatever their metaclass) and, for most, `find_parts_exact()`.
  On a namespace, for each kind of definition: `find_part_defs()`, `find_part_def(name)` and
  `find_part_defs_by_name()`, and the usages it has through any membership, `find_member_parts()`.
- One by one: the types and supertypes as written, what a feature subsets, redefines or references
  (`find_redefinition_root()` follows its redefinitions to the first), values and literals, the
  bounds of a multiplicity, the parameters, inputs and outputs of a behavior, what an accept action
  or a transition accepts, applied metadata, documentation.
- Where a member's definition gives no answer, it raises `Refused`: `find_literal()` of an
  expression that is not a literal (`2 * x`, a value with a unit). Where the backend cannot answer
  something the member reads, it raises `NotAnswered`, naming what. Today the SysML Toolkit answers
  430 of the members' 456 examples; it does not answer `type` on action, state and other behavioral
  usages and on enumeration values (which the semantic lists of those kinds read), a metadata
  usage's `metaclass`, nor the member lists of a definition with a public import in its body.
- A member or definition list outside the owned scope is in the backend's order: the specification
  does not define the order of a namespace's memberships.
- Every member, with its definition, its signature in each language and its examples: the release's
  `sysml-sdk-reference-<version>.html`.

## Operations

The specification's operations are methods under their own names, with the specification's
parameters. The SysML Toolkit has a body for a part of them (`effectiveName()`, `resolve`,
`resolveGlobal`, `resolveLocal`, `visibleMemberships`, `supertypes`, `evaluate`, ...), and answers
a call only where its evidence for your model is complete; otherwise, and for the operations it
has no body for (`isCompatibleWith`, `specializes`, `allSupertypes`, ...), the call raises
`NotImplementedInToolkit`, as a property read does. Whether `resolve` is answered, for example,
depends on the names in your model: catch the refusal where your code can go on.

<!-- test: queries toolkit -->
```python
print(vehicle.effectiveName())                                   # Vehicle
membership = model.resolve("Vehicles").resolve("Vehicle")       # Namespace::resolve: a Membership
print(type(membership).__name__, membership.memberElement.qualifiedName)
```

## Write a model out as JSON

The SysML Toolkit writes the model as full-form interchange JSON, with the inherited and imported
members filled in (`closures=False` writes each element's own side only, as the command-line
tool does):

<!-- test: queries toolkit -->
```python
from sysml.toolkit import ToolkitBackend

backend = ToolkitBackend.open(["model.sysml"], library_dir=standard_library())
with open("model.full.json", "w", encoding="utf-8") as f:
    f.write(backend.full_json())
exported = Model(backend)                                      # the same session, as a model
```

## Next

- [tutorial.py](tutorial.py), a runnable walk through the example model with both backends:
  `python examples/python/tutorial.py` (SysML Toolkit) or `--payload`.
- [../queries/](../queries/README.md): parametric queries over whole models (reachable states,
  bills of materials, connectivity, requirements), the same in every language.
- The names of every class and member are the specification's; see
  [Names](../../README.md#names) for the rules.
- [skills/sysml-sdk-python](../../skills/sysml-sdk-python/SKILL.md) teaches a coding agent the
  same.
