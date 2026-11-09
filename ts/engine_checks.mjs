// EngineBackend against a small engine written for the check (docs/engine-protocol.md), run as a
// Node process: no Java and no engine install needed.
import { mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { BackendError, EngineBackend, Gone, Model, NotAnswered, UnresolvedReference, is } from "./index.mjs";

function check(cond, what) { if (!cond) throw new Error(`FAILED: ${what}`); }

const ENGINE = `
const readline = require("node:readline");
const ELEMENTS = { "1": ["Package", "P", null], "2": ["PartDefinition", "A", "P::A"] };
const out = (o) => process.stdout.write(JSON.stringify(o) + "\\n");
out({ ready: true, engine: "Test engine", version: "1.0", protocol: 1 });
readline.createInterface({ input: process.stdin }).on("line", (line) => {
  const req = JSON.parse(line), id = req.id, h = req.h;
  const ok = (v) => out({ id, ok: v }), err = (kind, message) => out({ id, error: { kind, message } });
  if (req.cmd === "open") return ok({ sources: Object.keys(req.sources), elaborate: req.elaborate });
  if (req.cmd === "close") { ok(null); process.exit(0); }
  if (req.cmd === "allHandles") return ok(["1", "2"]);
  if (req.cmd === "resolve") return ok({ "P": "1", "P::A": "2" }[req.name] ?? null);
  if (!(h in ELEMENTS)) return err("gone", "no element " + h);
  const [mc, name, qn] = ELEMENTS[h];
  if (req.cmd === "metaclass") return ok(mc);
  if (req.cmd === "elementId") return ok("id-" + h);
  if (req.cmd === "call") return req.op === "effectiveName" ? ok(name) : err("notApplicable", "no operation " + req.op);
  switch (req.prop) {
    case "declaredName": return ok(name);
    case "qualifiedName": return ok(qn ?? name);
    case "owner": return ok(h === "2" ? { "@id": "1" } : null);
    case "ownedMember": return ok(h === "1" ? [{ "@id": "2" }] : []);
    case "isAbstract": return err("refused", "isAbstract is not answered");
    case "ownedRelationship": return err("unresolved", "a reference did not resolve");
    case "boom": return err("failed", "the engine broke");
    default: return err("notApplicable", mc + " has no property " + req.prop);
  }
});
`;

const dir = mkdtempSync(join(tmpdir(), "engine-check-"));
const script = join(dir, "engine.cjs");
writeFileSync(script, ENGINE);
try {
  const be = await EngineBackend.start([process.execPath, script], { sources: { "a.sysml": "package P { part def A; }" } });
  try {
    check(be.engine === "Test engine" && be.version === "1.0", "the engine announces itself");
    check(be.stats.sources[0] === "a.sysml" && be.stats.elaborate === true, "open answers: " + JSON.stringify(be.stats));
    const m = new Model(be);
    const a = m.resolve("P::A");
    check(is(a, "PartDefinition") && a.declaredName === "A" && a.qualifiedName === "P::A", "reads");
    check(is(a.owner, "Package") && a.owner.ownedMember[0] === a, "references");
    check(a.effectiveName() === "A", "operations");
    check([...m.all()].length === 2 && m.resolve("Q") === null, "all and resolve");
    let refused = false;
    try { a.isAbstract; } catch (e) { refused = e instanceof NotAnswered && e.message.startsWith("Test engine: "); }
    check(refused, "refused is NotAnswered");
    let unresolved = false;
    try { a.ownedRelationship; } catch (e) { unresolved = e instanceof UnresolvedReference; }
    check(unresolved, "unresolved is UnresolvedReference");
    let failed = false;
    try { be.get("2", "boom"); } catch (e) { failed = e instanceof BackendError && e.message.includes("the engine broke"); }
    check(failed, "failed is BackendError");
    check(be.get("2", "noSuchProperty") === null, "notApplicable reads as absent");
    let gone = false;
    try { be.metaclass("9"); } catch (e) { gone = e instanceof Gone; }
    check(gone, "gone is Gone");
  } finally {
    be.close();
  }
  let missing = false;
  try { await EngineBackend.start([join(dir, "no-such-engine")]); } catch (e) { missing = e instanceof BackendError && e.message.includes("cannot start"); }
  check(missing, "a missing engine is a BackendError");
  console.log("ALL JAVASCRIPT ENGINE CHECKS PASSED");
} finally {
  rmSync(dir, { recursive: true, force: true });
}
