// SDK runtime (TS/JS backend): Model, PayloadBackend, Proxy elements.
// Mirrors python/sysml/base.py — same read semantics, same identity
// rules. A property read returns whatever the backend gave: the
// SysML Toolkit reports its own unimplemented members, so the SDK
// never second-guesses a result.

import { HIERARCHY, OPS, PROPS } from "./meta.mjs";

export class NotComputed extends Error {}
export class NotImplementedInToolkit extends Error {}
export class Gone extends Error {}
// The SDK requires resolved models: typed navigation throws on
// SysML Toolkit @ref passthroughs and dangling @ids.
export class UnresolvedReference extends Error {}

const isIdRef = (v) =>
  v !== null && typeof v === "object" && !Array.isArray(v) &&
  Object.keys(v).length === 1 && typeof v["@id"] === "string";

/** Elements by id (document order, the last of a repeated id) and ids by qualified name (the first). */
function index(elements) {
  const byId = new Map();
  const byQname = new Map();
  for (const e of elements) {
    if (typeof e["@id"] !== "string") continue;
    byId.set(e["@id"], e);
    const qn = e["qualifiedName"];
    if (typeof qn === "string" && qn && !byQname.has(qn)) byQname.set(qn, e["@id"]);
  }
  return { byId, byQname };
}

/** A standard library read from a full-form interchange element array, such as the library JSON
 * published with the SDK. Index it once and pass it to any number of payload models: their
 * references into the library then resolve by the library's normative ids. */
export class PayloadLibrary {
  constructor(elements) {
    const { byId, byQname } = index(elements);
    this.byId = byId;
    this.byQname = byQname;
  }
  get size() { return this.byId.size; }
}

// The Backend protocol is defined over opaque per-backend
// element *handles*; an id-keyed backend like this one uses element ids
// as its handles. With a library, an id or qualified name the payload does not
// hold is looked up in the library; the library's elements are not the model's,
// so allHandles lists the payload's only.
export class PayloadBackend {
  constructor(elements, library = null) {
    const { byId, byQname } = index(elements);
    this.byId = byId;
    this.byQname = byQname;
    this.library = library;
  }
  #require(handle) {
    const el = this.byId.get(handle) ?? this.library?.byId.get(handle);
    if (!el) throw new Gone(`no element ${handle} in this payload`);
    return el;
  }
  get(handle, prop) {
    const el = this.#require(handle);
    return prop in el ? el[prop] : undefined;
  }
  metaclass(handle) { return this.#require(handle)["@type"]; }
  elementId(handle) { this.#require(handle); return handle; }
  resolve(qname) { return this.byQname.get(qname) ?? this.library?.byQname.get(qname) ?? null; }
  allHandles() { return this.byId.keys(); }
  /** A payload holds property values only; there is nothing to evaluate an operation with. */
  call(handle, op) {
    throw new NotImplementedInToolkit(
      `${op}(): operations are not available on payload models; load the model through the SysML Toolkit backend`);
  }
}

// Keys the Proxy intercepts before spec-property lookup. The non-$ names
// must never be spec property names — enforced at generation time
// (PROXY_INTERCEPTED in tools/gen_classes_ts.py; keep the two in sync).
const SPECIAL = new Set(["$id", "$handle", "$metaclass", "$raw", "then",
  "toJSON", "constructor", "valueOf", "toString"]);
// The runtime members `in` reports, beside the metaclass's properties and operations.
const RUNTIME_MEMBERS = new Set(["$id", "$handle", "$metaclass", "$raw"]);

export class Model {
  constructor(backend) {
    this.backend = backend;
    this.cache = new Map();
  }
  /** A model read from a full-form interchange element array. With `library` (a PayloadLibrary),
   * references into the standard library resolve against it; its elements are not the model's. */
  static fromFullJson(elements, { library = null } = {}) {
    return new Model(new PayloadBackend(elements, library));
  }
  wrap(raw, at = "?") {
    if (isIdRef(raw)) {
      try { return this.element(raw["@id"]); }
      catch {
        throw new UnresolvedReference(
          `dangling reference @id='${raw["@id"]}' at ${at}: target is not ` +
          `in this model; the SDK requires resolved models`);
      }
    }
    if (raw !== null && typeof raw === "object" && !Array.isArray(raw) && "@ref" in raw)
      throw new UnresolvedReference(
        `unresolved reference '${raw["@ref"]}' at ${at} (the SysML Toolkit's @ref ` +
        `passthrough is non-standard best-effort); the SDK requires resolved ` +
        `models — use $raw() for the raw value`);
    if (Array.isArray(raw)) return raw.map((v) => this.wrap(v, at));
    return raw === undefined ? null : raw;
  }
  element(id) {
    let w = this.cache.get(id);
    if (w) return w;
    const mc = this.backend.metaclass(id); // throws Gone if absent
    const model = this;
    const props = PROPS[mc] ?? {};
    const ops = OPS[mc] ?? {};
    w = new Proxy({ id, mc }, {
      get(t, key) {
        if (typeof key !== "string" || SPECIAL.has(key)) {
          if (key === "$id") return model.backend.elementId(t.id);
          if (key === "$handle") return t.id;
          if (key === "$metaclass") return t.mc;
          if (key === "$raw") return (p) => model.backend.get(t.id, p);
          if (key === "toString" || key === "toJSON")
            return () => `<${t.mc} ${model.backend.get(t.id, "qualifiedName") ?? t.id}>`;
          return undefined;
        }
        const meta = props[key];
        if (!meta) {
          const op = ops[key];
          if (!op) return undefined; // not a spec member of this metaclass
          const spec = op[1];
          return (...args) => model.call(t.id, t.mc, spec, args);
        }
        const rawVal = model.backend.get(t.id, key);
        if (rawVal == null && meta[0] === "A")
          return []; // absent arrays normalize to empty (typed-surface rule)
        return model.wrap(rawVal, `${t.mc}.${key} (element ${t.id})`);
      },
      has(t, key) { return typeof key === "string" && (key in props || key in ops || RUNTIME_MEMBERS.has(key)); },
      set() { throw new TypeError("payload models are read-only"); },
    });
    this.cache.set(id, w);
    return w;
  }
  /**
   * Every operation routes through here: the backend evaluates it (the SysML Toolkit answers or
   * refuses; a payload refuses), and the result is wrapped like a property value.
   */
  call(handle, mc, op, args) {
    const raw = this.backend.call(handle, op, args);
    return this.wrap(raw, `${mc}.${op}() (element ${handle})`);
  }
  resolve(qname) {
    const handle = this.backend.resolve(qname);
    return handle == null ? null : this.element(handle);
  }
  *all() { for (const h of this.backend.allHandles()) yield this.element(h); }
  /** The top-level namespaces: the elements without an owner that are namespaces (a membership
   * may have no owner either). */
  roots() { return [...this.all()].filter((el) => is(el, "Namespace") && el.owner == null); }
}

/** Typed narrowing: is(el, "PartUsage") — metaclass or any ancestor. */
export function is(el, kind) {
  if (!el || typeof el.$metaclass !== "string") return false;
  const mc = el.$metaclass;
  return mc === kind || (HIERARCHY[mc] ?? []).includes(kind);
}

export function elementsOfType(model, kind) {
  const out = [];
  for (const el of model.all()) if (is(el, kind)) out.push(el);
  return out;
}
