// Parametric queries over two example models, and optionally over the vehicle model of SysML
// Annex A, printed as a report. The program in every language prints the same report, which is
// kept in examples/queries/expected/.
//
// Run from the repository root:
//
//     node examples/queries/typescript/demo.mjs                  reads the models through the SysML Toolkit
//     node examples/queries/typescript/demo.mjs --payload        reads their JSON exports instead, with
//                                                                the standard library as JSON
//     node examples/queries/typescript/demo.mjs --library DIR    loads the standard library with them
//     node examples/queries/typescript/demo.mjs --annex-a FILE --library DIR
//                                                                reports on the Annex A vehicle instead
//
// The SysML Toolkit runs in the WebAssembly build of the binding library, which reads no files;
// in Node `libraryDir` reads the standard library's files for it. The JSON exports and the library
// JSON are generated, not committed: python tools/export_example.py. The queries themselves are in
// queries.mjs; this file loads the models and asks the questions.

import { readFileSync } from "node:fs";
import { basename, dirname, join } from "node:path";
import { fileURLToPath } from "node:url";
import { ToolkitBackend, Model, NotImplementedInToolkit, PayloadLibrary } from "../../../ts/index.mjs"; // `sysml` once published
import * as q from "./queries.mjs";

const MODELS = join(dirname(fileURLToPath(import.meta.url)), "..", "models");
const LIBRARY_JSON = join(MODELS, "..", "..", "sysml.library.full.json");

let payloadLibrary = null;

/** The standard library as JSON, read once and shared by every payload model. */
function readPayloadLibrary() {
  payloadLibrary ??= new PayloadLibrary(JSON.parse(readFileSync(LIBRARY_JSON, "utf8")));
  return payloadLibrary;
}

/** A model from examples/queries/models: `name`.sysml through the SysML Toolkit, or
 * `name`.full.json with the library JSON. */
async function load(name, { payload, library }) {
  if (payload)
    return Model.fromFullJson(JSON.parse(readFileSync(join(MODELS, `${name}.full.json`), "utf8")),
      { library: readPayloadLibrary() });
  const sources = { [`${name}.sysml`]: readFileSync(join(MODELS, `${name}.sysml`), "utf8") };
  return new Model(await ToolkitBackend.open({ sources, libraryDir: library ?? undefined }));
}

function heading(title) {
  console.log(`== ${title} ==`);
}

/** Run one report. Where the SysML Toolkit cannot vouch for a value yet it refuses the read
 * (NotImplementedInToolkit); the report stops there, says so, and the next one runs. */
function section(report) {
  try {
    report();
  } catch (e) {
    if (!(e instanceof NotImplementedInToolkit)) throw e;
    console.log(`  report stopped: ${e.message}`);
  }
}

/** A scenario in words: the triggers that can occur and the values bound. */
function describe(triggers, bindings) {
  const parts = [];
  if (triggers != null) parts.push(`only ${triggers.join(", ")}`);
  if (bindings.length > 0) parts.push(bindings.map(([name, value]) => `${name} = ${q.fmt(value)}`).join(", "));
  return parts.length > 0 ? parts.join("; ") : "nothing bound, every trigger";
}

// ---------------------------------------------------------------- the reports

/** Reachable states of `machine` under each scenario, then the shortest trigger sequences under
 * the scenario `pathsFor`. A scenario is [title, triggers or null, bindings]. */
function reportStates(machine, scenarios, pathsFor) {
  heading(`Reachable states: ${machine.qualifiedName}`);
  const states = q.statesOf(machine);
  const transitions = q.transitionsOf(machine);
  console.log(`${states.length} states, ${transitions.length} transitions:`);
  for (const state of [machine, ...states])
    for (const initial of q.initialStates(state)) console.log(`  ${q.describeInitial(state, initial, machine)}`);
  for (const transition of transitions) console.log(`  ${q.describeTransition(transition, machine)}`);
  for (const [title, triggers, bindings] of scenarios) {
    const reached = q.explore(machine, { triggers, bindings });
    console.log(`${title}: ${describe(triggers, bindings)}`);
    const reachable = states.filter((s) => reached.has(s)).map((s) => q.pathBelow(s, machine));
    const unreachable = states.filter((s) => !reached.has(s)).map((s) => q.pathBelow(s, machine));
    console.log(`  reachable (${reachable.length}): ${reachable.join(", ")}`);
    console.log(`  unreachable (${unreachable.length}): ${unreachable.join(", ") || "-"}`);
  }
  const [triggers, bindings] = pathsFor;
  const reached = q.explore(machine, { triggers, bindings });
  console.log(`shortest trigger sequences (${describe(triggers, bindings)}):`);
  const width = Math.max(...states.map((s) => q.pathBelow(s, machine).length));
  for (const state of states) {
    const path = reached.get(state);
    const text = path === undefined ? "unreachable" : path.join(", ") || "(no trigger)";
    console.log(`  ${q.pathBelow(state, machine).padEnd(width)}  ${text}`);
  }
}

function reportStructure(roots, attributes) {
  heading("Structure: bills of materials and roll-ups");
  for (const root of roots) {
    console.log(root.qualifiedName);
    const bom = q.billOfMaterials(root);
    console.log(`  parts: ${[...bom].map(([name, count]) => `${name} ${count}`).join(", ")}`);
    console.log(`  ${attributes.map((a) => `${q.nameOf(a)} ${q.fmt(q.rollup(root, a))}`).join(", ")}`);
  }
}

function reportConnectivity(root, questions) {
  heading(`Connectivity: ${root.qualifiedName}`);
  for (const link of q.links(root)) console.log(`  ${link.describe()}`);
  for (const [source, item] of questions) {
    const via = item ? `, through ${item} flows only` : "";
    const reached = q.reachableParts(root, source, { item });
    console.log(`from ${source}${via}: ${reached.join(", ") || "-"}`);
  }
}

function reportRequirements(model, derived) {
  heading("Requirements");
  for (const v of q.checkSatisfactions(model, derived)) {
    const definition = v.requirement.requirementDefinition;
    const kind = definition != null ? ` : ${q.nameOf(definition)}` : "";
    const verb = v.negated ? "not satisfied" : "satisfied";
    const expr = q.resultExpression(v.constraint);
    const values = v.operands.map(q.fmt).join(` ${expr.operator} `);
    console.log(`${q.nameOf(v.requirement)}${kind}, ${verb} by ${q.nameOf(v.satisfiedBy)}`);
    console.log(`  ${q.nameOf(v.constraint)}: ${q.render(expr)}  [${values}]  ${v.outcome}`);
  }
}

// ---------------------------------------------------------------- the questions

const TEMPERATE = [["selfTestPassed", true], ["temperature", 25]];

const PUMP_SCENARIOS = [
  ["A", null, []],
  ["B", null, TEMPERATE],
  ["C", null, [["selfTestPassed", false]]],
  ["D", null, [["selfTestPassed", true], ["temperature", 95]]],
  ["E", null, [...TEMPERATE, ["maxTemperature", 20]]],
  ["F", ["PowerOn", "SelfTestDone", "Start"], TEMPERATE],
];

async function examples(options) {
  const pumps = await load("pumps", options);
  section(() => reportStates(pumps.resolve("Pumps::PumpController::modes"), PUMP_SCENARIOS, [null, TEMPERATE]));
  console.log();

  const drones = await load("drones", options);
  const mass = drones.resolve("Drones::Component::mass");
  const cost = drones.resolve("Drones::Component::cost");
  section(() => reportStructure(["Quadcopter", "Hexacopter", "CargoHexacopter"].map((d) => drones.resolve(`Drones::${d}`)), [mass, cost]));
  console.log();
  section(() => reportConnectivity(drones.resolve("Drones::Quadcopter"), [
    ["battery", null], ["battery", "Power"], ["controller", null], ["controller", "Command"], ["rotors.propeller", null],
  ]));
  console.log();
  section(() => reportRequirements(drones, new Map([
    ["totalMass", (part) => q.rollup(part, mass)],
    ["totalCost", (part) => q.rollup(part, cost)],
  ])));
}

async function annexA(path, library) {
  const sources = { [basename(path)]: readFileSync(path, "utf8") };
  const vehicle = new Model(await ToolkitBackend.open({ sources, libraryDir: library ?? undefined }));
  const machine = vehicle.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates");
  section(() => {
    const keys = [];
    for (const transition of q.transitionsOf(machine)) {
      const [key] = q.triggerOf(transition);
      if (key != null && key !== "at" && !keys.includes(key)) keys.push(key);
    }
    reportStates(machine, [["A", null, []], ["B", null, [["brakePedalDepressed", false]]], ["C", keys, []]], [null, []]);
  });
}

function option(args, name) {
  const i = args.indexOf(name);
  return i >= 0 ? args[i + 1] : null;
}

const args = process.argv.slice(2);
const library = option(args, "--library");
const annex = option(args, "--annex-a");
if (annex) await annexA(annex, library);
else await examples({ payload: args.includes("--payload"), library });
