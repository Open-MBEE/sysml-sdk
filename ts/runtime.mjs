// SDK runtime (TS/JS backend): Model, PayloadBackend, Proxy elements.
// Mirrors python/sysml/base.py — same read semantics, same identity
// rules. A property read returns whatever the backend gave: the
// SysML Toolkit reports its own unimplemented members, so the SDK
// never second-guesses a result.

import { FINDS, HIERARCHY, OPS, PROPS } from "./meta.mjs";

export class NotComputed extends Error {}
/** A specification member the backend does not answer for this element: the SysML Toolkit's
 * refusals (NotImplementedInToolkit), and an engine's. */
export class NotAnswered extends Error {}
export class NotImplementedInToolkit extends NotAnswered {}
/** A convenience member that gives no answer for this element by its contract (`invalid` in its OCL):
 * the literal of an expression that is not a literal, a bound that is an expression. Unlike the other
 * NotAnswered errors, it says what the answer is, not that the backend cannot give it. */
export class Refused extends NotAnswered {}
export class Gone extends Error {}
/** The engine behind a backend could not answer: it failed while computing a value, or it is not
 * available (not installed, not downloadable, or no suitable runtime). */
export class BackendError extends Error {}
// The SDK requires resolved models: typed navigation throws on
// SysML Toolkit @ref passthroughs and dangling @ids.
export class UnresolvedReference extends Error {}

/** The scopes of the convenience lists. */
export const Scope = Object.freeze({ OWNED: "owned", INHERITED: "inherited", ALL: "all" });
/** The visibility filters of the convenience lists. */
export const VisibilityFilter = Object.freeze({ ANY: "any", PROTECTED: "protected", PUBLIC: "public" });
const ENUM_LITERALS = { scope: Object.values(Scope), visibility: Object.values(VisibilityFilter) };

/** A literal's value as tagged JSON ({"integer": 4}, {"real": 0.5}, {"boolean": true}, {"string": "a"},
 * {"unbounded": true}) to a JavaScript value; `*` is Infinity. */
export function decodeLiteral(raw) {
  const v = typeof raw === "string" ? JSON.parse(raw) : raw;
  const keys = v && typeof v === "object" ? Object.keys(v) : [];
  if (keys.length === 1) {
    const [k] = keys;
    if (k === "unbounded") return Infinity;
    if (k === "integer" || k === "real" || k === "boolean" || k === "string") return v[k];
  }
  throw new Error(`a literal value was expected, not ${JSON.stringify(raw)}`);
}

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
  /** Nor a convenience member. */
  find(handle, member) {
    throw new NotAnswered(
      `${member[0]}: the convenience members are not available on payload models; load the model through ` +
      `the SysML Toolkit or the Pilot Implementation`);
  }
  findEach(handles, member, args) { return handles.map((h) => this.find(h, member, args)); }
}

// The results a list carries a member for: elements and strings (Booleans, numbers and literals stay on
// the elements); by member name, its FINDS row (the same in every metaclass that has the member).
const LIST_RESULTS = new Set(["elements", "element", "strings"]);
const LIST_FINDS = new Map();
for (const rows of Object.values(FINDS))
  for (const [name, row] of Object.entries(rows))
    if (LIST_RESULTS.has(row[3]) && !LIST_FINDS.has(name)) LIST_FINDS.set(name, row);

/**
 * A list of elements that carries the convenience members of its elements: `xs.findPorts()` is the
 * elements' `findPorts()` in list order, list results concatenated (duplicates kept) and single results
 * with the absent ones dropped; the result is again an ElementList (an array of strings for
 * `findDocText`). If the member refuses for one element, the call refuses. Members that answer a Boolean,
 * a number or a literal stay on the elements. Every element list a convenience member returns is one;
 * any array of elements can be lifted: `ElementList.from(xs)`. `map` and `filter` give plain arrays.
 */
export class ElementList extends Array {
  static get [Symbol.species]() { return Array; }
}
for (const [name, row] of LIST_FINDS) {
  if (name in Array.prototype) throw new Error(`the convenience member ${name} would hide Array.prototype.${name}`);
  Object.defineProperty(ElementList.prototype, name, {
    value(...args) { return findEach(this, name, row, args); },
    writable: true, configurable: true,
  });
}

function findEach(list, name, row, args) {
  const out = row[3] === "strings" ? [] : new ElementList();
  if (list.length === 0) return out;
  for (const el of list)
    if (!FINDS[el?.$metaclass]?.[name]) throw new TypeError(`${name}() does not apply to a ${el?.$metaclass ?? typeof el}`);
  const model = list[0].$model;
  const values = model.bind(row, args);
  const raws = list.every((el) => el.$model === model) && typeof model.backend.findEach === "function"
    ? model.backend.findEach(list.map((el) => el.$handle), row, values)
    : list.map((el) => el.$model.backend.find(el.$handle, row, values));
  list.forEach((el, i) => {
    const v = el.$model.convert(row, raws[i], `${el.$metaclass}.${name}() (element ${el.$handle})`);
    if (row[3] === "element") { if (v !== null) out.push(v); } else out.push(...v);
  });
  return out;
}

// Keys the Proxy intercepts before spec-property lookup. The non-$ names
// must never be spec property names — enforced at generation time
// (PROXY_INTERCEPTED in tools/gen_classes_ts.py; keep the two in sync).
const SPECIAL = new Set(["$id", "$handle", "$metaclass", "$model", "$raw", "then",
  "toJSON", "constructor", "valueOf", "toString"]);
// The runtime members `in` reports, beside the metaclass's properties and operations.
const RUNTIME_MEMBERS = new Set(["$id", "$handle", "$metaclass", "$model", "$raw"]);

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
    const finds = FINDS[mc] ?? {};
    w = new Proxy({ id, mc }, {
      get(t, key) {
        if (typeof key !== "string" || SPECIAL.has(key)) {
          if (key === "$id") return model.backend.elementId(t.id);
          if (key === "$handle") return t.id;
          if (key === "$metaclass") return t.mc;
          if (key === "$model") return model;
          if (key === "$raw") return (p) => model.backend.get(t.id, p);
          if (key === "toString" || key === "toJSON")
            return () => `<${t.mc} ${model.backend.get(t.id, "qualifiedName") ?? t.id}>`;
          return undefined;
        }
        const meta = props[key];
        if (!meta) {
          const op = ops[key];
          if (!op) {
            const find = finds[key];
            if (find) return (...args) => model.findMember(t.id, t.mc, find, args);
            return undefined; // not a member of this metaclass
          }
          const spec = op[1];
          return (...args) => model.call(t.id, t.mc, spec, args);
        }
        const rawVal = model.backend.get(t.id, key);
        if (rawVal == null && meta[0] === "A")
          return []; // absent arrays normalize to empty (typed-surface rule)
        return model.wrap(rawVal, `${t.mc}.${key} (element ${t.id})`);
      },
      has(t, key) { return typeof key === "string" && (key in props || key in ops || key in finds || RUNTIME_MEMBERS.has(key)); },
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
  /**
   * Every convenience member routes through here: the arguments are checked and their defaults filled
   * in, the backend answers or refuses, and the result is wrapped. `member` is its FINDS row.
   */
  findMember(handle, mc, member, args) {
    const raw = this.backend.find(handle, member, this.bind(member, args));
    return this.convert(member, raw, `${mc}.${member[0]}() (element ${handle})`);
  }
  /** A convenience member's arguments, checked, with their defaults filled in. */
  bind(member, args) {
    const [name, , params] = member;
    if (args.length > params.length) throw new TypeError(`${name}() takes ${params.length} argument(s), ${args.length} given`);
    return params.map(([pname, kind, dflt], i) => {
      const v = args[i] === undefined ? dflt : args[i];
      if (v === null || v === undefined) throw new TypeError(`${name}() needs the argument ${pname}`);
      if (kind in ENUM_LITERALS && !ENUM_LITERALS[kind].includes(v))
        throw new TypeError(`${name}(): ${pname} is one of ${ENUM_LITERALS[kind].join(", ")}, not ${v}`);
      if (kind === "bool" && typeof v !== "boolean") throw new TypeError(`${name}(): ${pname} must be a boolean, not ${v}`);
      if (kind === "str" && typeof v !== "string") throw new TypeError(`${name}(): ${pname} must be a string, not ${v}`);
      if (kind === "element" && v?.$handle == null) throw new TypeError(`${name}(): ${pname} must be an element, not ${v}`);
      return v;
    });
  }
  /** A convenience member's answer from the backend's: element lists as ElementLists. */
  convert(member, raw, at) {
    switch (member[3]) {
      case "elements": return raw == null ? new ElementList() : ElementList.from(this.wrap(raw, at));
      case "element": return raw == null ? null : this.wrap(raw, at);
      case "strings": return raw ?? [];
      case "unlimited": return raw === "*" ? Infinity : raw ?? null;
      case "literal": return decodeLiteral(raw);
      case "map": return new Map((raw ?? []).map(([name, ref]) => [name, this.wrap(ref, at)]));
      default: return raw ?? null;
    }
  }
  /**
   * Name resolution, with visibility (KerML 8.2.3.5). With a `context` namespace: the specification's
   * `Namespace::resolve` on it (its member element), as the backend answers it. Without one: the
   * backend's resolution from the root. A payload model resolves its elements' own qualified names; the
   * SysML Toolkit's lookup does not consider visibility yet. Only names the model gives count: an unnamed
   * element is not found under the name of what it references. To get an element by its qualified name
   * whatever its visibility, use `find`.
   */
  resolve(name, context = null) {
    if (context == null) {
      const handle = this.backend.resolve(name);
      return handle == null ? null : this.element(handle);
    }
    if (!is(context, "Namespace")) throw new TypeError(`resolve(): the context must be a namespace, not a ${context.$metaclass}`);
    const m = context.resolve(name);
    return m == null ? null : m.memberElement;
  }
  /**
   * The element a qualified name names, whatever its visibility, the same on every backend: the first
   * segment is a top-level name; each next one a member of the namespace before it, through one of its
   * owned memberships (an alias's name included) or, with `inherited`, also through its inherited
   * memberships (its supertypes' non-private ones). Null if a segment names nothing. Segments may be
   * quoted, with KerML's escapes (`'~Plug'`).
   */
  find(qualifiedName, inherited = false) {
    let handle = this.backend.resolve(qualifiedName);
    if (handle != null) {
      const el = this.element(handle);
      if (el.qualifiedName === qualifiedName) return el;
    }
    const segments = splitQualifiedName(qualifiedName);
    if (segments.length === 0) return null;
    handle = this.backend.resolve(segments[0][0]);
    let current = handle != null ? this.element(handle) : null;
    if (current == null)
      for (const root of this.roots()) {
        current = memberNamed(root.ownedMembership, segments[0][1]);
        if (current != null) break;
      }
    for (const [, name] of segments.slice(1)) {
      if (!is(current, "Namespace")) return null;
      let found = memberNamed(current.ownedMembership, name);
      if (found == null && inherited && is(current, "Type")) found = memberNamed(current.inheritedMembership, name);
      current = found;
    }
    return current;
  }
  *all() { for (const h of this.backend.allHandles()) yield this.element(h); }
  /** The top-level namespaces: the elements without an owner that are namespaces (a membership
   * may have no owner either). */
  roots() { return [...this.all()].filter((el) => is(el, "Namespace") && el.owner == null); }
}

const ESCAPES = { "'": "'", '"': '"', b: "\b", f: "\f", t: "\t", n: "\n", "\\": "\\" };

/**
 * A qualified name's segments as [written, name] pairs (KerML 8.2.3.4.1): separated by `::`; an
 * unrestricted name in single quotes, its escape sequences resolved (KerML 8.2.2.3); a leading global
 * scope qualifier `$` dropped.
 */
export function splitQualifiedName(qualifiedName) {
  const qn = qualifiedName;
  const out = [];
  let i = 0;
  while (i < qn.length) {
    if (qn[i] === "'") {
      let j = i + 1;
      let name = "";
      while (j < qn.length && qn[j] !== "'") {
        if (qn[j] === "\\" && j + 1 < qn.length) { name += ESCAPES[qn[j + 1]] ?? qn[j + 1]; j += 2; }
        else { name += qn[j]; j += 1; }
      }
      if (j >= qn.length) throw new Error(`an unterminated quoted name in ${JSON.stringify(qualifiedName)}`);
      out.push([qn.slice(i, j + 1), name]);
      i = j + 1;
    } else {
      let j = qn.indexOf("::", i);
      if (j < 0) j = qn.length;
      out.push([qn.slice(i, j), qn.slice(i, j)]);
      i = j;
    }
    if (i < qn.length) {
      if (!qn.startsWith("::", i)) throw new Error(`a name in ${JSON.stringify(qualifiedName)} is not followed by '::'`);
      i += 2;
    }
  }
  return out.length && out[0][1] === "$" ? out.slice(1) : out;
}

/** The member element of the first membership with this member name or short name. */
function memberNamed(memberships, name) {
  for (const m of memberships) if (m.memberName === name || m.memberShortName === name) return m.memberElement;
  return null;
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
