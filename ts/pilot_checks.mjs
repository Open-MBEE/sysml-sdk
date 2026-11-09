// JavaScript reading through the SysML v2 Pilot Implementation, in a Java process. Skips itself
// when no Pilot is at hand (SYSML_PILOT_HOME unset and none in the user's cache) or no Pilot engine
// (SYSML_PILOT_SERVER unset and none in the package), so the other checks run everywhere without
// downloading the Pilot; with SYSML_REQUIRE_PILOT=1 nothing is a reason to skip.
import { existsSync, readdirSync } from "node:fs";
import { homedir, tmpdir } from "node:os";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";
import { BackendError, Gone, Model, PilotBackend, UnresolvedReference, is } from "./index.mjs";

function check(cond, what) { if (!cond) throw new Error(`FAILED: ${what}`); }

function cacheRoot() {
  if (process.env.SYSML_CACHE_DIR) return process.env.SYSML_CACHE_DIR;
  if (process.platform === "win32") return join(process.env.LOCALAPPDATA || join(homedir(), "AppData", "Local"), "sysml", "Cache");
  if (process.platform === "darwin") return join(homedir(), "Library", "Caches", "sysml");
  return join(process.env.XDG_CACHE_HOME || join(homedir(), ".cache"), "sysml");
}

function whyNot() {
  if (process.env.SYSML_REQUIRE_PILOT === "1") return null;
  const carried = join(dirname(fileURLToPath(import.meta.url)), "pilot", "sysml-pilot-server.jar");
  if (!process.env.SYSML_PILOT_SERVER && !existsSync(carried)) return "no Pilot engine (set SYSML_PILOT_SERVER)";
  if (process.env.SYSML_PILOT_HOME) return null;
  const cache = cacheRoot();
  if (existsSync(cache) && readdirSync(cache).some((d) => d.startsWith("pilot-") && existsSync(join(cache, d, ".complete"))))
    return null;
  return "no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)";
}

const why = whyNot();
if (why) {
  console.log(`Pilot checks skipped: ${why}`);
  process.exit(0);
}

const sources = {
  "demo.sysml": "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }",
  "größe.sysml": "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }",
  "bad.sysml": "package Bad { part p : Missing; }",
};
const be = await PilotBackend.open({ sources, heap: "2g" });
try {
  const m = new Model(be);
  console.log(`checking with the Pilot ${be.pilotVersion}`);
  check(be.stats.pilot === be.pilotVersion, "stats name the Pilot's version");

  const car = m.resolve("Demo::Car");
  check(is(car, "PartDefinition") && car.qualifiedName === "Demo::Car", "qualifiedName");
  check(is(car.owner, "Package"), "owner is the package");
  const feats = car.ownedFeature;
  check(feats.length === 2 && feats[0].declaredName === "wheels" && is(feats[0], "PartUsage"), "ownedFeature");
  check(feats[0].type.length === 1 && feats[0].type[0].declaredName === "Wheel", "type");
  check(feats[0].isComposite === true, "isComposite");
  const sports = m.resolve("Demo::Sports");
  check(sports.inheritedFeature.some((f) => f.declaredName === "wheels"), "inheritedFeature includes Car's wheels");
  check(car === m.resolve("Demo::Car"), "wrappers are cached by handle");
  check(car.$id.length === 36, "elementId is a UUID");
  check(m.resolve("Demo::Nothing") === null, "an unknown name resolves to null");
  check(m.resolve("ScalarValues::Real") !== null, "library names resolve");

  check(car.effectiveName() === "Car", "effectiveName");
  check(sports.specializes(car) === true, "specializes");
  check(sports.isCompatibleWith(car) === true, "isCompatibleWith");
  check(sports.inheritedMemberships([], [], false).some((ms) => ms.memberName === "wheels"), "inheritedMemberships");
  const connection = m.resolve("Demo::Link").ownedFeature.find((f) => is(f, "ConnectionUsage"));
  const end = connection.connectorEnd[0];
  check(end.declaredName == null && end.effectiveName() === "source",
    `an unnamed connection end is named by the end it redefines: ${end.effectiveName()}`);

  check(m.resolve("Sizes").documentation[0].body.includes("Größe und Wärme"), "text beyond ASCII comes back intact");
  const light = m.resolve("Sizes::Light");
  check(light.text.length === 1 && light.text[0].startsWith("Keep it light."), "a requirement's text is a list of strings");
  check(m.roots().flatMap((r) => r.ownedMember).some((x) => x.name === "Sizes"), "roots");

  const p = m.resolve("Bad::p");
  let unresolved = false;
  try { p.ownedTyping[0].type; } catch (e) { unresolved = e instanceof UnresolvedReference; }
  check(unresolved, "an unresolved reference throws UnresolvedReference");
  let gone = false;
  try { be.metaclass("999999999"); } catch (e) { gone = e instanceof Gone; }
  check(gone, "an unknown handle is gone");
  check(be.get(p.$handle, "noSuchProperty") === null, "a property the metaclass lacks reads as absent");
  let refused = false;
  try { be.call(p.$handle, "noSuchOperation", []); } catch (e) { refused = e instanceof BackendError && e.message.includes("noSuchOperation"); }
  check(refused, "an operation the Pilot does not have throws BackendError");
} finally {
  be.close();
}
let missing = false;
try {
  (await PilotBackend.open({ sources: { "a.sysml": "package A;" }, pilotHome: tmpdir(), heap: "1g" })).close();
} catch (e) { missing = e instanceof BackendError && e.message.includes("holds no Pilot"); }
check(missing, "a missing Pilot throws BackendError");
console.log("ALL JAVASCRIPT PILOT CHECKS PASSED");
