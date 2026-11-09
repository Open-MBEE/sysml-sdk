// The convenience members' examples (convenience.json) through JavaScript, on the SysML Toolkit's
// WebAssembly build and on the Pilot Implementation (in a Java process): every example's member is
// called on its element and its answer compared with the example's, as
// python/tests/convenience_check.py does. Answers must agree; what a backend refuses is counted (the
// Python test pins the counts; the bodies are the binding library's and the Pilot engine's). Each
// backend skips itself when it is not at hand (the module and the release library; a Pilot and its
// engine, as ts/pilot_checks.mjs decides); SYSML_REQUIRE_TOOLKIT=1 makes the SysML Toolkit's skip a
// failure, SYSML_REQUIRE_PILOT=1 the Pilot's.
import { existsSync, readdirSync, readFileSync } from "node:fs";
import { homedir } from "node:os";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";
import { ElementList, FINDS, Model, NotAnswered, PilotBackend, Refused, is } from "./index.mjs";
import { ToolkitBackend, ToolkitLibrary } from "./toolkit.mjs";

const here = dirname(fileURLToPath(import.meta.url));
const root = join(here, "..");
const wasm = process.env.SYSMLV2_ABI_WASM || join(root, "abi", "target", "wasm32-unknown-unknown", "release", "sysmlv2_abi.wasm");
const libraryDir = join(root, "..", "sysml-toolkit", "spec-refs", "SysML-v2-Release", "sysml.library");

/** Why the SysML Toolkit's run skips itself, or null. */
function toolkitWhyNot() {
  for (const [path, what] of [[wasm, "the wasm build of the binding library"], [libraryDir, "the release library"]]) {
    if (existsSync(path)) continue;
    if (process.env.SYSML_REQUIRE_TOOLKIT === "1") {
      console.error(`SYSML_REQUIRE_TOOLKIT=1 but ${what} is not at ${path}`);
      process.exit(1);
    }
    return `${what} not present`;
  }
  return null;
}

function cacheRoot() {
  if (process.env.SYSML_CACHE_DIR) return process.env.SYSML_CACHE_DIR;
  if (process.platform === "win32") return join(process.env.LOCALAPPDATA || join(homedir(), "AppData", "Local"), "sysml", "Cache");
  if (process.platform === "darwin") return join(homedir(), "Library", "Caches", "sysml");
  return join(process.env.XDG_CACHE_HOME || join(homedir(), ".cache"), "sysml");
}

/** Why the Pilot's run skips itself, or null. */
function pilotWhyNot() {
  if (process.env.SYSML_REQUIRE_PILOT === "1") return null;
  const carried = join(here, "pilot", "sysml-pilot-server.jar");
  if (!process.env.SYSML_PILOT_SERVER && !existsSync(carried)) return "no Pilot engine (set SYSML_PILOT_SERVER)";
  if (process.env.SYSML_PILOT_HOME) return null;
  const cache = cacheRoot();
  if (existsSync(cache) && readdirSync(cache).some((d) => d.startsWith("pilot-") && existsSync(join(cache, d, ".complete"))))
    return null;
  return "no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)";
}

const table = JSON.parse(readFileSync(join(root, "convenience.json"), "utf8"));
const sources = Object.fromEntries(Object.entries(table.models).map(([name, m]) => [`${name}.sysml`, m.text]));
const records = Object.fromEntries(table.members.map((m) => [m.name, m]));

/** The element by qualified name, whatever its visibility (Model.find). */
function find(model, qn) {
  return model.find(qn);
}

function walk(el, via) {
  for (const step of via) {
    const [, prop, kind] = step.match(/^(\w+)(?:\[(\w+)\])?$/);
    const value = el[prop];
    let values = Array.isArray(value) ? value : [value];
    if (kind) values = values.filter((v) => v != null && is(v, kind));
    if (values.length !== 1) throw new Error(`${step} gives ${values.length} elements`);
    el = values[0];
  }
  return el;
}

function matches(model, expected, actual) {
  if (expected !== null && typeof expected === "object" && "@type" in expected) {
    if (!actual?.$metaclass || actual.$metaclass !== expected["@type"]) return false;
    return Object.entries(expected).every(([k, v]) => k === "@type" || matches(model, v, actual[k]));
  }
  if (actual?.$metaclass) return typeof expected === "string" && find(model, expected) === actual;
  if (typeof actual === "string") return expected === actual; // such as an operator: "*" is itself
  if (expected === "*") return actual === Infinity;
  if (typeof expected === "boolean" || typeof actual === "boolean") return expected === actual;
  if (typeof expected === "number" && typeof actual === "number") return expected === actual;
  return expected === actual;
}

function outcome(model, e) {
  const rec = records[e.member];
  let el = find(model, e.at);
  if (!el) return ["mismatch", `${e.at} not found`];
  if (e.via) el = walk(el, e.via);
  const args = rec.params.map((p) => {
    if (!e.args || !(p.name in e.args)) return undefined;
    return p.type.kind === "element" ? find(model, e.args[p.name]) : e.args[p.name];
  });
  while (args.length && args[args.length - 1] === undefined) args.pop();
  if (typeof el[e.member] !== "function") return ["mismatch", `${el.$metaclass} has no ${e.member}`];
  let answer;
  try {
    answer = el[e.member](...args);
  } catch (x) {
    if (x instanceof Refused) return e.refused ? ["agrees", "refused"] : ["mismatch", `refused: ${x.message}`];
    if (x instanceof NotAnswered) return ["refused by the backend", x.message];
    throw x;
  }
  if (e.refused) return ["mismatch", `answered ${String(answer)}, the contract refuses`];
  try {
    if (answer instanceof Map) { // a name-keyed map: the expected [name, element] pairs
      const got = [...answer.entries()];
      const want = e.expect;
      const pair = ([n, q], [k, v]) => k === n && matches(model, q, v);
      const ok = Array.isArray(want) && (e.unordered ? sameItems(want, got, pair)
        : want.length === got.length && want.every((w, i) => pair(w, got[i])));
      return ok ? ["agrees", ""] : ["mismatch", `expected ${JSON.stringify(want)}, got ${describe(answer)}`];
    }
    if ("includes" in e) {
      const missing = e.includes.filter((q) => !answer.some((a) => matches(model, q, a)));
      const present = (e.excludes ?? []).filter((q) => answer.some((a) => matches(model, q, a)));
      return missing.length || present.length ? ["mismatch", `missing ${missing}, unexpected ${present}`] : ["agrees", ""];
    }
    const want = e.expect;
    if (Array.isArray(want)) {
      const one = (w, a) => matches(model, w, a);
      const ok = Array.isArray(answer) && (e.unordered ? sameItems(want, answer, one)
        : want.length === answer.length && want.every((w, i) => one(w, answer[i])));
      return ok ? ["agrees", ""] : ["mismatch", `expected ${JSON.stringify(want)}, got ${describe(answer)}`];
    }
    if (want === null) return answer === null ? ["agrees", ""] : ["mismatch", `expected none, got ${describe(answer)}`];
    return answer !== null && matches(model, want, answer) ? ["agrees", ""] : ["mismatch", `expected ${JSON.stringify(want)}, got ${describe(answer)}`];
  } catch (x) {
    if (x instanceof NotAnswered) return ["refused by the backend", `the answer cannot be checked: ${x.message}`];
    throw x;
  }
}

function describe(a) {
  if (Array.isArray(a)) return `[${a.map(describe).join(", ")}]`;
  if (a instanceof Map) return `{${[...a].map(([k, v]) => `${k}: ${describe(v)}`).join(", ")}}`;
  if (a?.$metaclass) return a.qualifiedName ?? `<${a.$metaclass}>`;
  return JSON.stringify(a);
}

// Whether `got` holds the items of `want`, each matched once, in any order (an answer whose order the
// specification leaves open).
function sameItems(want, got, match) {
  if (want.length !== got.length) return false;
  const left = [...got];
  for (const w of want) {
    const i = left.findIndex((g) => match(w, g));
    if (i < 0) return false;
    left.splice(i, 1);
  }
  return true;
}

// An example the table lists as deviating on this backend (`deviates`, by backend key): its mismatch is
// a known deviation; if it agrees after all, the listing is stale.
function deviation(e, key, kind, detail) {
  const reason = e.deviates?.[key];
  if (reason === undefined) return [kind, detail];
  if (kind === "mismatch") return ["known deviation", `${reason} (${detail})`];
  if (kind === "agrees") return ["mismatch", `listed as deviating on ${key} (${reason}), but it agrees`];
  return [kind, detail];
}

function runOn(name, key, backend) {
  const model = new Model(backend);
  const counts = { agrees: 0, "refused by the backend": 0, "known deviation": 0, mismatch: 0 };
  const mismatches = [];
  for (const e of table.examples) {
    let kind, detail;
    try { [kind, detail] = outcome(model, e); } catch (x) { [kind, detail] = ["mismatch", `${x.name}: ${x.message}`]; }
    [kind, detail] = deviation(e, key, kind, detail);
    counts[kind]++;
    if (kind === "mismatch") mismatches.push(`${name}: ${e.member} at ${e.at} ${JSON.stringify(e.args ?? {})}: ${detail}`);
  }
  console.log(`convenience examples on ${name} (JavaScript): ${JSON.stringify(counts)}`);
  return [...mismatches, ...listChecks(name, model), ...findChecks(name, model)];
}

/** Model.find (whatever the visibility; through inheritance when asked; quoted names) and Model.resolve
 * from a context (the specification's Namespace::resolve, which the SysML Toolkit refuses in places). */
function findChecks(name, model) {
  const problems = [];
  const qn = (el) => el?.qualifiedName ?? null;
  const expect = (what, got, want) => { if (got !== want) problems.push(`${name}: ${what}: ${got}, not ${want}`); };
  expect("find Base::b", qn(model.find("Scopes::Base::b")), "Scopes::Base::b");
  expect("find Base::c", qn(model.find("Scopes::Base::c")), "Scopes::Base::c");
  expect("find Derived::b", qn(model.find("Scopes::Derived::b")), null);
  expect("find '~Plug'", model.find("Typing::Plug::'~Plug'")?.$metaclass ?? null, "ConjugatedPortDefinition");
  try {
    expect("find Derived::b, inherited", qn(model.find("Scopes::Derived::b", true)), "Scopes::Base::b");
    expect("find Derived::c, inherited", qn(model.find("Scopes::Derived::c", true)), null);
  } catch (x) { if (!(x instanceof NotAnswered)) throw x; }
  try {
    expect("resolve b from Base", qn(model.resolve("b", model.find("Scopes::Base"))), "Scopes::Base::b");
    expect("resolve Base::b from Derived", qn(model.resolve("Base::b", model.find("Scopes::Derived"))), null);
  } catch (x) { if (!(x instanceof NotAnswered)) throw x; }
  console.log(`find and resolve on ${name} (JavaScript): ${problems.length ? problems.length + " problems" : "as expected"}`);
  return problems;
}

// The members a list carries, by name (elements and strings), with each one's first example.
const LIST_MEMBERS = new Map();
for (const rows of Object.values(FINDS))
  for (const [member, row] of Object.entries(rows))
    if (["elements", "element", "strings"].includes(row[3]) && !LIST_MEMBERS.has(member)) LIST_MEMBERS.set(member, row);
const FIRST_EXAMPLE = new Map();
for (const e of table.examples) if (!FIRST_EXAMPLE.has(e.member)) FIRST_EXAMPLE.set(e.member, e);

/** Every member a list carries, on the elements of the examples' models it applies to: the list's answer
 * is the elements' answers flattened (list results concatenated, absent single results dropped), or the
 * list refuses as an element does; on all of them, and on those of them that answer. */
function listChecks(name, model) {
  const elements = [...model.all()];
  const counts = { "all compared": 0, "all refused": 0, "answering compared": 0 };
  const problems = [];
  const compare = (member, row, xs, answers, args) => {
    const refusal = answers.find((a) => a instanceof NotAnswered);
    let got;
    try {
      got = xs[member](...args);
    } catch (x) {
      if (!(x instanceof NotAnswered)) throw x;
      if (!refusal || x.constructor !== refusal.constructor) problems.push(`${name}: ${member}: the list refused (${x.message})`);
      return "refused";
    }
    if (refusal) { problems.push(`${name}: ${member}: an element refused (${refusal.message}), the list answered`); return "compared"; }
    const want = row[3] === "element" ? answers.filter((a) => a !== null) : answers.flat();
    if (got.length !== want.length || got.some((g, i) => g !== want[i]))
      problems.push(`${name}: ${member}: ${got.length} items from the list, ${want.length} from the elements`);
    else if (row[3] !== "strings" && !(got instanceof ElementList)) problems.push(`${name}: ${member}: not an ElementList`);
    return "compared";
  };
  for (const [member, row] of LIST_MEMBERS) {
    const applicable = ElementList.from(elements.filter((el) => FINDS[el.$metaclass]?.[member]));
    if (applicable.length === 0) continue;
    const e = FIRST_EXAMPLE.get(member) ?? {};
    const args = records[member].params.map((p) => {
      if (!e.args || !(p.name in e.args)) return undefined;
      return p.type.kind === "element" ? find(model, e.args[p.name]) : e.args[p.name];
    });
    while (args.length && args[args.length - 1] === undefined) args.pop();
    const answers = applicable.map((el) => { try { return el[member](...args); } catch (x) { if (x instanceof NotAnswered) return x; throw x; } });
    counts["all " + compare(member, row, applicable, answers, args)]++;
    const answering = applicable.filter((_, i) => !(answers[i] instanceof NotAnswered));
    if (answering.length && answering.length < applicable.length) {
      compare(member, row, ElementList.from(answering), answers.filter((a) => !(a instanceof NotAnswered)), args);
      counts["answering compared"]++;
    }
  }
  console.log(`convenience lists on ${name} (JavaScript): ${JSON.stringify(counts)}`);
  return problems;
}

const mismatches = [];
const noToolkit = toolkitWhyNot();
if (noToolkit) {
  console.log(`convenience checks on the SysML Toolkit skipped: ${noToolkit}`);
} else {
  const library = await ToolkitLibrary.load(wasm);
  const backend = await ToolkitBackend.open({ sources, libraryDir, library });
  try {
    mismatches.push(...runOn("the SysML Toolkit", "toolkit", backend));
  } finally {
    backend.close();
  }
}
const noPilot = pilotWhyNot();
if (noPilot) {
  console.log(`convenience checks on the Pilot Implementation skipped: ${noPilot}`);
} else {
  const backend = await PilotBackend.open({ sources, heap: "2g" });
  try {
    mismatches.push(...runOn("the Pilot Implementation", "pilot", backend));
  } finally {
    backend.close();
  }
}
if (mismatches.length) {
  console.error(mismatches.join("\n"));
  process.exit(1);
}
