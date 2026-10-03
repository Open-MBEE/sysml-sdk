// Node smoke test for the TS/JS SDK backend — mirrors ../demo.py.
import { readFileSync } from "node:fs";
import { Model, PayloadLibrary, UnresolvedReference, elementsOfType, is } from "./runtime.mjs";

const payload = JSON.parse(
  readFileSync(new URL("../data/bigger.full.json", import.meta.url), "utf-8"));
const m = Model.fromFullJson(payload);

const vehicle = m.resolve("Demo::Vehicle");
console.log("resolve:", `${vehicle}`);
if (vehicle.$metaclass !== "PartDefinition") throw new Error("metaclass");
for (const k of ["Definition", "Classifier", "Type", "Namespace"])
  if (!is(vehicle, k)) throw new Error(`is(${k})`);
if (is(vehicle, "Feature")) throw new Error("!Feature");
console.log("is() chain: PartDefinition < Definition < Classifier < Type < Namespace OK");
for (const k of ["$id", "$metaclass", "qualifiedName", "ownedFeature"])
  if (!(k in vehicle)) throw new Error(`${k} in element`);
if ("isComposite" in vehicle || "ownedParts" in vehicle) throw new Error("not a member of PartDefinition");
console.log("`in`: runtime members and the metaclass's members OK");

const feats = vehicle.ownedFeature;
console.log("ownedFeature:", feats.map((w) => `${w.$metaclass}:${w.declaredName}`).join(", "));
if (feats.length !== 7) throw new Error("7 features");
const wheel = feats.find((w) => w.declaredName === "wheel");
if (wheel.owner.$id !== vehicle.$id) throw new Error("owner");
if (wheel.isComposite !== true) throw new Error("isComposite");

const vUsage = m.resolve("Demo::v");
if (vUsage.definition[0].$id !== vehicle.$id) throw new Error("definition");
console.log("v.definition -> Vehicle OK");

// Reads pass the backend value through unchanged: no client-side check.
for (const p of ["ownedPart", "inheritedFeature"]) {
  const v = vehicle[p];
  if (!Array.isArray(v) || v.length !== 0) throw new Error(`${p} != []`);
}
console.log("unimplemented derivations -> [] (backend value, no second-guessing)");

try { vehicle.declaredName = "X"; throw new Error("expected throw"); }
catch (e) { if (!(e instanceof TypeError)) throw e; }
console.log("assignment -> TypeError (read-only, as designed)");

const parts = elementsOfType(m, "PartUsage");
const usages = elementsOfType(m, "Usage");
console.log(`PartUsage: ${parts.length}, all Usage subtypes: ${usages.length}`);
if (usages.length <= parts.length) throw new Error("subtype counts");

const calc = m.resolve("Demo::Vehicle::K");
if (calc.$metaclass !== "CalculationDefinition") throw new Error("K");
if (calc.input.map((w) => w.declaredName).join() !== "x") throw new Error("K.input");
console.log("K.input -> [x] OK");

// Elements come in document order; an id given twice keeps its first place and its last element.
const ordered = Model.fromFullJson([
  { "@id": "z", "@type": "Package", declaredName: "first" },
  { "@id": "a", "@type": "Package", declaredName: "A" },
  { "@id": "z", "@type": "Package", declaredName: "second" },
]);
if ([...ordered.all()].map((e) => e.$id).join() !== "z,a") throw new Error("document order");
if (ordered.element("z").declaredName !== "second") throw new Error("the last element of an id");
console.log("payload elements in document order OK");

// A payload model with a library: references the model does not hold resolve in the library, the
// library's elements are not the model's, the model wins an id or name both have, and a reference
// in neither still throws. The same checks run in every language.
const library = new PayloadLibrary([
  { "@id": "lib", "@type": "LibraryPackage", qualifiedName: "ScalarValues", isLibraryElement: true },
  { "@id": "real", "@type": "DataType", qualifiedName: "ScalarValues::Real", declaredName: "Real",
    owner: { "@id": "lib" }, isLibraryElement: true },
  { "@id": "clash", "@type": "Package", declaredName: "from the library" },
  { "@id": "libP", "@type": "Package", qualifiedName: "P" },
]);
const withLibraryElements = [
  { "@id": "pkg", "@type": "Package", qualifiedName: "P", declaredName: "P" },
  { "@id": "mass", "@type": "AttributeUsage", qualifiedName: "P::mass", owner: { "@id": "pkg" },
    type: [{ "@id": "real" }], definition: [{ "@id": "gone" }] },
  { "@id": "clash", "@type": "Package", declaredName: "from the model" },
];
const withLibrary = Model.fromFullJson(withLibraryElements, { library });
const real = withLibrary.element("mass").type[0];
if (real.declaredName !== "Real" || real.isLibraryElement !== true) throw new Error("library element");
if (real.owner.qualifiedName !== "ScalarValues") throw new Error("library owner");
if (withLibrary.resolve("ScalarValues::Real") !== real) throw new Error("resolve into the library");
if ([...withLibrary.all()].map((e) => e.$id).join() !== "pkg,mass,clash") throw new Error("all() lists the library");
if (withLibrary.roots().map((e) => e.$id).join() !== "pkg,clash") throw new Error("roots() lists the library");
if (withLibrary.element("clash").declaredName !== "from the model") throw new Error("the model's id wins");
if (withLibrary.resolve("P").$id !== "pkg") throw new Error("the model's name wins");
const throwsUnresolved = (f) => { try { f(); } catch (e) { return e instanceof UnresolvedReference; } return false; };
if (!throwsUnresolved(() => withLibrary.element("mass").definition)) throw new Error("a reference in neither");
const withoutLibrary = Model.fromFullJson(withLibraryElements);
if (!throwsUnresolved(() => withoutLibrary.element("mass").type)) throw new Error("no library, no resolution");
if (withoutLibrary.resolve("ScalarValues::Real") !== null) throw new Error("no library, no name");
console.log("payload model with a library OK");

console.log("\nALL TS-BACKEND SMOKE TESTS PASSED");
