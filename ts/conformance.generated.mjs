// Generated — do not edit. Source: metamodel.json via tools/gen_conformance.py.
//
// Anti-drift: TABLE_SHA256 pins the table this file was generated from;
// the checks verify meta.mjs + the Proxy runtime against the live table.
// Run: node conformance.generated.mjs

import { createHash } from "node:crypto";
import { readFileSync } from "node:fs";
import { Model, NotImplementedInToolkit, is } from "./runtime.mjs";
import { ABSTRACT, HIERARCHY, OPS, PROPS } from "./meta.mjs";

const TABLE_SHA256 = "17644f3a5d70189d76b5ecb95c2089aeda1f93b6705f8bce5eeae1faf023758b";
const raw = readFileSync(new URL("../metamodel.json", import.meta.url));
if (createHash("sha256").update(raw).digest("hex") !== TABLE_SHA256)
  throw new Error("metamodel.json changed since this harness was generated; " +
    "rerun tools/gen_conformance.py");
const MM = JSON.parse(raw.toString("utf-8")).classes;

const eq = (a, b, what) => {
  if (JSON.stringify(a) !== JSON.stringify(b))
    throw new Error(`${what}: ${JSON.stringify(a)} != ${JSON.stringify(b)}`);
};
const ancestors = (name, acc = new Set()) => {
  for (const b of MM[name].bases)
    if (!acc.has(b)) { acc.add(b); ancestors(b, acc); }
  return acc;
};

const names = Object.keys(MM).sort();
const concrete = names.filter((n) => !MM[n].abstract);
// every operation name a class declares or inherits
const flatOps = (n) => new Set([n, ...ancestors(n)].flatMap((c) => MM[c].ops.map((o) => o.name)));
// an operation that shares its name with a property of a class that has both
const colliding = new Set(names.flatMap((n) => [...flatOps(n)].filter((o) => o in MM[n].props)));
// keys the element Proxy answers itself (SPECIAL in runtime.mjs)
const RUNTIME_NAMES = new Set(["constructor", "then", "toJSON", "toString", "valueOf"]);
const member = (o) => (colliding.has(o) || RUNTIME_NAMES.has(o) ? o + "Op" : o);

// --- meta.mjs matches the table -------------------------------------------
eq(ABSTRACT, names.filter((n) => MM[n].abstract), "ABSTRACT");
for (const n of names) {
  eq(HIERARCHY[n], [...ancestors(n)].sort(), `HIERARCHY[${n}]`);
  const expected = Object.fromEntries(Object.entries(MM[n].props)
    .sort(([a], [b]) => (a < b ? -1 : 1))
    .map(([p, m]) => [p, [m.shape, m.derived ? 1 : 0]]));
  eq(PROPS[n] ?? {}, expected, `PROPS[${n}]`);
  const ops = Object.fromEntries(Object.entries(OPS[n] ?? {}).map(([m, [, o]]) => [m, o]));
  eq(ops, Object.fromEntries([...flatOps(n)].sort().map((o) => [member(o), o])
    .sort(([a], [b]) => (a < b ? -1 : 1))), `OPS[${n}]`);
}

// --- Proxy runtime honors the table ---------------------------------------
const payload = concrete.map((n) => ({ "@id": `e-${n}`, "@type": n }));
const model = Model.fromFullJson(payload);
for (const n of concrete) {
  const el = model.element(`e-${n}`);
  if (el.$metaclass !== n) throw new Error(`$metaclass ${n}`);
  if (el.$id !== `e-${n}`) throw new Error(`$id ${n}`);
  if (!is(el, n)) throw new Error(`is(self) ${n}`);
  for (const anc of ancestors(n))
    if (!is(el, anc)) throw new Error(`is(${n}, ${anc})`);
  for (const p of Object.keys(MM[n].props))
    el[p]; // a property read never second-guesses the backend
  for (const o of flatOps(n)) {
    const f = el[member(o)];
    if (typeof f !== "function") throw new Error(`${n}.${member(o)}: operation missing`);
    let threw = false;
    try { f(); } catch (e) { threw = e instanceof NotImplementedInToolkit; }
    if (!threw) throw new Error(`${n}.${member(o)}(): must throw NotImplementedInToolkit`);
  }
  let assigned = false;
  try { el.declaredName = "x"; assigned = true; } catch {}
  if (assigned) throw new Error(`${n}: assignment must throw`);
}

console.log(`conformance OK: ${names.length} classes, ` +
  `${concrete.length} concrete, table ${TABLE_SHA256.slice(0, 12)}`);
