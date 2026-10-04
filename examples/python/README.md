# Python

Read SysML v2 and KerML models from Python. Every metaclass of the specification is a Python
class, and every property and operation keeps its specification name: `ownedFeature`,
`qualifiedName`, `effectiveName()`.

A model is read through one of two backends, with the same classes over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and
  computes derived properties;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool, read in
  pure Python.

## Install

Python 3.10 or newer:

```sh
pip install sysml
```

The wheels (Windows x64, Linux x64 with glibc 2.28 or later, macOS on Apple silicon and on Intel)
carry the binding library and the standard library models; nothing else is needed. The release has
the same wheels to install from a file (`pip install sysml-0.1.0-py3-none-win_amd64.whl`, or
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
- Without access to GitHub, download `sysml_library-0.1.0.zip` from the release, unpack it, and set
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
