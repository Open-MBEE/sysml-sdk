"""Which property reads the SDK's own examples need, and what the SysML Toolkit's checked reader
answers for exactly those reads.

1. Runs the Python tutorial and the query demos on the payload path (JSON exports of the models,
   made by the SysML Toolkit with the library loaded), logging every (element id, property) they
   read. For Annex A it first exports the model with the SysML Toolkit, then runs the demo's
   Annex A report on that payload.
2. Opens each model through the SysML Toolkit backend (the binding library as built, i.e. with the
   checked reader), loaded by file name with the library, so element ids equal the payload's, and
   reads the same properties of the same elements.
3. Prints, per property and outcome, how many reads and which programs; reads on library elements
   are counted apart (the SysML Toolkit backend lists the model's own elements only).

Measurement only: nothing here changes what the SDK answers.
Usage: python tools/toolkit_needs.py <sdk root> <sysml.library dir> <Annex A .sysml> <out.json>
(run from the SDK root after `python tools/export_example.py --library <dir>`)
"""
import collections
import json
import runpy
import sys
from pathlib import Path

ROOT, LIB, ANNEX, OUT = Path(sys.argv[1]), sys.argv[2], Path(sys.argv[3]), Path(sys.argv[4])
sys.path.insert(0, str(ROOT / "python"))
sys.path.insert(0, str(ROOT / "examples" / "queries" / "python"))

from sysml import Model, NotImplementedInToolkit, PayloadLibrary, base  # noqa: E402
from sysml.base import UnresolvedReference  # noqa: E402

reads = collections.OrderedDict()  # (model, id, prop) -> {metaclass, library, programs}
current = {"program": None, "model": None, "ids": {}}
orig_get = base.PayloadBackend.get


def logging_get(self, handle, prop):
    el = self._require(handle)
    model = current["ids"].get(id(self))
    entry = reads.setdefault((model, handle, prop), {"metaclass": el["@type"], "library": handle not in self._by_id,
                                                     "programs": set()})
    entry["programs"].add(current["program"])
    return orig_get(self, handle, prop)


base.PayloadBackend.get = logging_get
orig_init = base.PayloadBackend.__init__


def tagging_init(self, elements, library=None):
    orig_init(self, elements, library)
    current["ids"][id(self)] = current["model"]


base.PayloadBackend.__init__ = tagging_init

library = PayloadLibrary(json.loads((ROOT / "examples" / "sysml.library.full.json").read_text(encoding="utf-8")))


def run(program, model_hint, argv):
    current["program"], current["model"] = program, model_hint
    sys.argv = argv
    try:
        runpy.run_path(argv[0], run_name="__main__")
    except SystemExit:
        pass


# The tutorial (vehicle) and the query demos (pumps, drones), on the payload path. The demo loads
# pumps first, then drones; the model tag follows the file the backend was built from.
run("tutorial", "vehicle.sysml", [str(ROOT / "examples/python/tutorial.py"), "--payload"])
import importlib.util  # noqa: E402

_spec = importlib.util.spec_from_file_location("query_demo", ROOT / "examples/queries/python/demo.py")
demo = importlib.util.module_from_spec(_spec)  # the query demo module, for its loaders and Annex A report
_spec.loader.exec_module(demo)

orig_load = demo.load


def tagged_load(name, *, payload, library):
    current["model"] = f"{name}.sysml"
    return orig_load(name, payload=payload, library=library)


demo.load = tagged_load
current["program"] = "queries"
demo.examples(True, None)

# Annex A: the SysML Toolkit's closure export as the payload, then the demo's Annex A report on it.
text = ANNEX.read_text(encoding="utf-8")
toolkit_annex = Model.from_toolkit(sources={ANNEX.name: text}, library_dir=LIB)
payload = json.loads(toolkit_annex._backend.full_json(closures=True))
current["program"], current["model"] = "queries (Annex A)", ANNEX.name
vehicle = Model.from_full_json(payload, library=library)
machine = vehicle.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates")
keys = []
q = demo.q
for transition in q.transitions_of(machine):
    key, _ = q.trigger_of(transition)
    if key is not None and key != "at" and key not in keys:
        keys.append(key)
demo.report_states(machine, [("A", None, []), ("B", None, [("brakePedalDepressed", False)]), ("C", keys, [])],
                   (None, []))

# The same reads through the SysML Toolkit backend.
sources = {"vehicle.sysml": ROOT / "examples/vehicle.sysml", "pumps.sysml": ROOT / "examples/queries/models/pumps.sysml",
           "drones.sysml": ROOT / "examples/queries/models/drones.sysml", ANNEX.name: ANNEX}
toolkit_models = {}
for name, path in sources.items():
    m = Model.from_toolkit(sources={name: path.read_text(encoding="utf-8")}, library_dir=LIB)
    by_id = {m._backend.element_id(h): h for h in m._backend.all_handles()}
    toolkit_models[name] = (m, by_id)

rows = collections.defaultdict(lambda: {"reads": 0, "programs": set(), "example": None})
library_reads = collections.Counter()
missing = 0
for (model, eid, prop), info in reads.items():
    if info["library"]:
        library_reads[prop] += 1
        continue
    m, by_id = toolkit_models[model]
    h = by_id.get(eid)
    if h is None:
        missing += 1
        continue
    try:
        m._backend.get(h, prop)
        outcome = "answered"
    except NotImplementedInToolkit as e:
        outcome = "refused: " + str(e).split(": ", 1)[-1]
    except UnresolvedReference as e:
        outcome = "unresolved"
    row = rows[(prop, outcome)]
    row["reads"] += 1
    row["programs"] |= info["programs"]
    row["metaclasses"] = row.get("metaclasses", set()) | {info["metaclass"]}
    row["example"] = row["example"] or f"{model} {eid[:8]} {info['metaclass']}"

total = sum(r["reads"] for r in rows.values())
answered = sum(r["reads"] for (p, o), r in rows.items() if o == "answered")
print(f"distinct reads on the models' own elements: {total} (answered {answered}); on library elements: "
      f"{sum(library_reads.values())}; ids not found in the SysML Toolkit model: {missing}")
out = []
for (prop, outcome), r in sorted(rows.items(), key=lambda kv: (kv[0][1] == "answered", -kv[1]["reads"], kv[0][0])):
    if outcome != "answered":
        print(f"  {prop:28} {r['reads']:5}  {outcome}  [{', '.join(sorted(r['programs']))}]  "
              f"on {', '.join(sorted(r['metaclasses']))[:80]}; e.g. {r['example']}")
    out.append({"property": prop, "outcome": outcome, "reads": r["reads"], "programs": sorted(r["programs"]),
                "metaclasses": sorted(r["metaclasses"]), "example": r["example"]})
print("library element reads by property:", dict(library_reads.most_common(12)))
OUT.write_text(json.dumps(out, indent=1), encoding="utf-8")
