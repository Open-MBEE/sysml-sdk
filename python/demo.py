"""SDK demo + smoke assertions over real SysML Toolkit full-form payloads."""

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))
import sysml as f

payload = json.loads(
    (Path(__file__).parent.parent / "data" / "bigger.full.json").read_text(encoding="utf-8")
)
m = f.Model.from_full_json(payload)

# --- typed resolution and hierarchy ---------------------------------------
vehicle = m.resolve("Demo::Vehicle")
print("resolve:", repr(vehicle))
assert type(vehicle).__name__ == "PartDefinition"
assert isinstance(vehicle, f.REGISTRY["PartDefinition"])
from sysml.classes import Classifier, Definition, Feature, Namespace, Type

assert isinstance(vehicle, Classifier) and isinstance(vehicle, Type)
assert isinstance(vehicle, Namespace) and isinstance(vehicle, Definition)
assert not isinstance(vehicle, Feature)
print("isinstance chain: PartDefinition < Definition < Classifier < Type < Namespace OK")

# --- navigation: computed properties return typed wrappers ----------------
feats = vehicle.ownedFeature
print("ownedFeature:", [(w.metaclass_name, w.declaredName) for w in feats])
assert len(feats) == 7
wheel = next(w for w in feats if w.declaredName == "wheel")
assert wheel.metaclass_name == "PartUsage"
assert wheel.owner == vehicle
assert wheel.isComposite is True
assert vehicle.qualifiedName == "Demo::Vehicle"

base = m.resolve("Demo::Base")
subclass = vehicle.ownedSubclassification
print("ownedSubclassification:", subclass)
assert len(subclass) == 1 and subclass[0].superclassifier == base

# definition/typing across the graph
v_usage = m.resolve("Demo::v")
assert v_usage.definition == [vehicle]
print("v.definition -> Vehicle OK")

# --- reads pass the backend value through unchanged ------------------------
# (no client-side check: a property the SysML Toolkit does not
# implement is reported by the SysML Toolkit itself.)
assert vehicle.ownedPart == []
assert vehicle.inheritedFeature == []
print("unimplemented derivations -> [] (backend value, no second-guessing)")

# --- operation stubs are honest -------------------------------------------
try:
    vehicle.specializes(base)
    raise AssertionError("expected NotImplementedInToolkit")
except f.NotImplementedInToolkit:
    print("Type.specializes() -> NotImplementedInToolkit (as designed)")

# --- read-only guard -------------------------------------------------------
try:
    vehicle.declaredName = "X"
    raise AssertionError("expected ReadOnly")
except f.ReadOnly:
    print("assignment -> ReadOnly (payload backend, as designed)")

# --- model-wide queries ----------------------------------------------------
parts = m.elements_of_type("PartUsage")
usages = m.elements_of_type(f.REGISTRY["Usage"]) if "Usage" in f.REGISTRY else None
from sysml.classes import Usage

all_usages = m.elements_of_type(Usage)  # abstract classes work via isinstance
print(f"PartUsage: {len(parts)}, all Usage subtypes: {len(all_usages)}")
assert len(all_usages) > len(parts)

calc = m.resolve("Demo::Vehicle::K")
assert calc.metaclass_name == "CalculationDefinition"
inp = calc.input
print("K.input:", [(w.metaclass_name, w.declaredName) for w in inp])
assert [w.declaredName for w in inp] == ["x"]

# --- collision rule: MetadataUsage.metaclass is the SPEC property ----------
meta_payload = [
    {"@id": "mc1", "@type": "Metaclass", "declaredName": "Safety",
     "qualifiedName": "M::Safety"},
    {"@id": "m1", "@type": "MetadataUsage", "qualifiedName": "M::stubbed",
     "metaclass": None},
    {"@id": "m2", "@type": "MetadataUsage", "qualifiedName": "M::filled",
     "metaclass": {"@id": "mc1"}},
]
mm = f.Model.from_full_json(meta_payload)
mu = mm.element("m1")
assert mu.metaclass_name == "MetadataUsage"  # runtime accessor, underscore rule
assert mu.metaclass is None  # the spec property: backend value, passed through
filled = mm.element("m2").metaclass
assert filled == mm.element("mc1") and filled.metaclass_name == "Metaclass"
assert type(mu).metaclass.derived is True  # class access -> the P descriptor
print("MetadataUsage: .metaclass_name (runtime) vs .metaclass (spec) OK")

print("\nALL PYTHON SDK SMOKE TESTS PASSED")
