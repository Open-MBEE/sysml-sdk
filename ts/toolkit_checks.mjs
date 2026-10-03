// JavaScript reading directly from the SysML Toolkit through the WebAssembly build of the binding
// library. Skips itself when the module is not built, so the payload checks still run; with
// SYSML_REQUIRE_TOOLKIT=1 a missing module is a failure instead.
import { existsSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";
import { Model, NotImplementedInToolkit, is } from "./index.mjs";
import { ToolkitBackend } from "./toolkit.mjs";

const here = dirname(fileURLToPath(import.meta.url));
const wasm = process.env.SYSMLV2_ABI_WASM ||
  join(here, "..", "abi", "target", "wasm32-unknown-unknown", "release", "sysmlv2_abi.wasm");
if (!existsSync(wasm)) {
  if (process.env.SYSML_REQUIRE_TOOLKIT === "1") {
    console.error(`SYSML_REQUIRE_TOOLKIT=1 but the wasm build is not at ${wasm}`);
    process.exit(1);
  }
  console.log("SysML Toolkit checks skipped: wasm build of the binding library not present");
  process.exit(0);
}
function check(cond, what) { if (!cond) throw new Error(`FAILED: ${what}`); }

// The member a SysML Toolkit refusal names (`Type::inheritedMemberships()` gives
// `inheritedMemberships()`).
function refusedMember(message) {
  const i = message.indexOf(" is not answered by the SysML Toolkit");
  if (i < 0) return "";
  const head = message.slice(0, i).trim();
  const j = head.lastIndexOf("::");
  return j < 0 ? head : head.slice(j + 2);
}

// A check that meets the SysML Toolkit's refusal of `member` first (NotImplementedInToolkit
// naming it): an expected failure, reported as one. It fails when it passes (the SysML Toolkit
// answers now: remove the expectation) or when it stops on anything else.
function expectRefusal(member, reason, what, body) {
  try {
    body();
  } catch (e) {
    if (e instanceof NotImplementedInToolkit && refusedMember(e.message) === member) {
      console.log(`expected failure: ${what}: the SysML Toolkit refuses ${member} (${reason})`);
      return;
    }
    throw e;
  }
  throw new Error(`FAILED: ${what}: the SysML Toolkit answers ${member} now; remove the expected refusal`);
}

const source = "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }";
const be = await ToolkitBackend.open({ sources: { "inline.sysml": source } });
const m = new Model(be);

const car = m.resolve("Demo::Car");
check(is(car, "PartDefinition"), "Car is a PartDefinition");
check(car.qualifiedName === "Demo::Car", "qualifiedName");
check(is(car.owner, "Package"), "owner is the package");
const feats = car.ownedFeature;
check(feats.length === 2 && feats[0].declaredName === "wheels", "ownedFeature");
check(is(feats[0], "PartUsage"), "wheels is a PartUsage");
expectRefusal("type", "it waits on implied relationships", "type",
  () => check(feats[0].type.length === 1 && feats[0].type[0].declaredName === "Wheel", "type"));
check(feats[0].isComposite === true, "isComposite");
const sports = m.resolve("Demo::Sports");
expectRefusal("inheritedFeature", "it waits on implied relationships", "inheritedFeature",
  () => check(sports.inheritedFeature.length === 2, `inheritedFeature computed by the SysML Toolkit: ${sports.inheritedFeature.length}`));
check(car === m.resolve("Demo::Car"), "wrappers are cached by handle");
check(car.$id.length === 36, "elementId is a UUID");
// Operations, through the SysML Toolkit's operation entry point; it refuses what it cannot
// answer as specified.
check(car.effectiveName() === "Car", "effectiveName");
expectRefusal("connectorEnd", "it waits on the implied end redefinitions", "an unnamed connection end is named by the end it redefines", () => {
  const connection = m.resolve("Demo::Link").ownedFeature.find((f) => is(f, "ConnectionUsage"));
  const end = connection.connectorEnd[0];
  check(end.declaredName === null && end.effectiveName() === "source",
    `an unnamed connection end is named by the end it redefines: ${end.effectiveName()}`);
});
expectRefusal("inheritedMemberships()", "its evidence is incomplete", "inheritedMemberships", () => {
  const memberships = sports.inheritedMemberships([], [], false);
  check(memberships.length === 2 && memberships[0].memberName === "wheels", "inheritedMemberships");
});
let opRefused = false;
try { sports.isCompatibleWith(car); } catch (e) { opRefused = e instanceof NotImplementedInToolkit && e.message.includes("isCompatibleWith"); }
check(opRefused, "an operation the SysML Toolkit cannot answer as specified is refused");
check(be.fullJson().startsWith("["), "fullJson");
be.close();

// Text beyond ASCII, a requirement's text (a list of strings), the members every element has, and
// the top-level namespaces.
const sizes = new Model(await ToolkitBackend.open({ sources: { "größe.sysml":
  "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }" } }));
check(is(sizes.resolve("Sizes::Last"), "PartDefinition"), "a source with text beyond ASCII loads whole");
check(sizes.resolve("Sizes").documentation[0].body.includes("Größe und Wärme"), "text beyond ASCII comes back intact");
const light = sizes.resolve("Sizes::Light");
check(Array.isArray(light.text) && light.text.length === 1 && light.text[0].startsWith("Keep it light."),
  `a requirement's text is a list of strings: ${JSON.stringify(light.text)}`);
check(light.owner.name === "Sizes", "the members of Element, read on an element's owner");
check(sizes.roots().some((r) => r.ownedMember.some((m) => m.name === "Sizes")), "roots");

// A library given as sources loads as a library: the model resolves against it, and its elements
// are not the model's.
const withLibrary = new Model(await ToolkitBackend.open({
  sources: { "m.sysml": "package M { private import Lib::*; part def D :> Base; }" },
  librarySources: { "lib.sysml": "library package Lib { part def Base; }" },
}));
const base = withLibrary.resolve("Lib::Base");
check(base?.isLibraryElement === true, "a library element from sources reports isLibraryElement");
check(withLibrary.resolve("M::D").ownedSubclassification[0].superclassifier === base, "the model resolves against it");
check([...withLibrary.all()].every((e) => !e.isLibraryElement), "library elements are not among the model's");

// The standard library read from its directory has the ids the native build gives it, so every
// reference of an export resolves against the model or the library JSON (skipped without them).
const libraryDir = join(here, "..", "..", "sysml-toolkit", "spec-refs", "SysML-v2-Release", "sysml.library");
const libraryJson = join(here, "..", "examples", "sysml.library.full.json");
if (existsSync(libraryDir) && existsSync(libraryJson)) {
  const { readFileSync } = await import("node:fs");
  const vehicle = await ToolkitBackend.open({
    sources: { "vehicle.sysml": readFileSync(join(here, "..", "examples", "vehicle.sysml"), "utf8") }, libraryDir });
  const exported = JSON.parse(vehicle.fullJson());
  const known = new Set([...exported, ...JSON.parse(readFileSync(libraryJson, "utf8"))].map((e) => e["@id"]));
  const dangling = new Set();
  const visit = (v) => {
    if (Array.isArray(v)) v.forEach(visit);
    else if (v && typeof v === "object") {
      if (Object.keys(v).length === 1 && "@id" in v) { if (!known.has(v["@id"])) dangling.add(v["@id"]); }
      else Object.values(v).forEach(visit);
    }
  };
  exported.forEach((e) => Object.entries(e).forEach(([k, v]) => { if (k !== "@id") visit(v); }));
  check(dangling.size === 0, `the library read from its directory has the library JSON's ids (${dangling.size} references dangle)`);
  vehicle.close();
} else {
  console.log("note: the standard library directory or its JSON is absent; the library id check is skipped");
}
console.log("ALL JS SYSML TOOLKIT CHECKS PASSED");
