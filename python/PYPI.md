# sysml

Read SysML v2 and KerML models from Python. Every metaclass of the specification is a Python class,
and every property and operation keeps its specification name (`ownedFeature`, `qualifiedName`,
`effectiveName()`). A model is read through one of two backends, with the same classes over both:

- **the OpenMBEE SysML Toolkit** reads your `.sysml` and `.kerml` files, resolves names and computes
  derived properties;
- **a payload** is a model exported as full-form interchange JSON by any SysML v2 tool.

```sh
pip install sysml
```

Python 3.10 or newer. The wheels for Windows x64, Linux x64 (glibc 2.28 or later), and macOS on Apple
silicon (11 or later) and on Intel (10.12 or later) carry the SysML Toolkit's binding library and the
standard library models. On other platforms pip installs the source distribution, which reads
payloads only.

## First: the standard library

Most models refer to the KerML and SysML standard library (`ScalarValues::Real`, `ISQ::mass`, and
implicitly `Parts::parts` and the like). Get it once, at the start of your program:

```python
from sysml import standard_library, standard_library_json

library_dir = standard_library()          # the library models the wheel carries, for the SysML Toolkit
library_json = standard_library_json()    # the library as JSON, for payloads
```

`standard_library_json()` downloads the JSON (about 11 MB) from this version's GitHub release on its
first call, checks it against the SHA-256 GitHub states for the file, and keeps it in your user cache;
later calls read the cache. Where GitHub cannot be reached, download `sysml_library-<version>.zip`
from the release yourself and set `SYSML_LIBRARY_JSON` to the `sysml.library.full.json` it holds.

## Read a model through the SysML Toolkit

```python
from sysml import Model, standard_library

model = Model.from_toolkit("model.sysml", library_dir=standard_library())
vehicle = model.resolve("Vehicles::Vehicle")
print(vehicle.qualifiedName, [f.declaredName for f in vehicle.ownedFeature])
```

## Read a payload

```python
import json
from sysml import Model, PayloadLibrary, standard_library_json

with open(standard_library_json(), encoding="utf-8") as f:
    library = PayloadLibrary(json.load(f))      # read once, share it
with open("model.json", encoding="utf-8") as f:
    model = Model.from_full_json(json.load(f), library=library)
```

## More

- The Python guide: <https://github.com/Open-MBEE/sysml-sdk/blob/main/examples/python/README.md>
- The SDK, its other languages (JavaScript/TypeScript, Java, C#, C++) and its documentation:
  <https://github.com/Open-MBEE/sysml-sdk>
- What is new, and what the SysML Toolkit does not answer yet:
  <https://github.com/Open-MBEE/sysml-sdk/blob/main/CHANGELOG.md>

The SDK is licensed under the Apache License 2.0. The standard library models the wheels carry, and the
library JSON, are licensed under the Eclipse Public License 2.0: see `sysml/stdlib/LICENSE` and
`sysml/stdlib/NOTICE` in the installed package.
