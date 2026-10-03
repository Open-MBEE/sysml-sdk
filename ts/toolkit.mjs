// The SysML Toolkit backend for JavaScript: the binding library (abi/) compiled to WebAssembly,
// driven from Node or a browser. Implements the same Backend contract as PayloadBackend, so
// the generated interfaces and Model are untouched. Handles are the integers the library
// mints, carried as decimal strings to fit the contract; references come back as {"@id": h}
// so Model.wrap mints elements as it does for payloads.
//
// Dispatch: get(handle, prop) finds the declaring class of prop for the element's metaclass
// through toolkit_table.mjs and calls that export; call(handle, op, args) finds the operation's
// row the same way and passes each argument as the binding library's parameter C type says.
//
// The wasm module imports three wasm-bindgen glue functions that a SysML Toolkit dependency
// pulls in; no-op stubs satisfy them. Layouts below are wasm32 (4-byte pointers, 8-byte u64).

import { Gone, NotImplementedInToolkit, UnresolvedReference } from "./runtime.mjs";
import { BASES, FUNCS, NAMES_DIGEST } from "./toolkit_table.mjs";

const OK = 0, NOT_IMPLEMENTED = 1, NOT_APPLICABLE = 2, INVALID_HANDLE = 3, UNRESOLVED = 7;

// Struct sizes and offsets on wasm32 (ABI section 4).
const STR = { size: 16, ptr: 0, len: 4, present: 8, owner: 12 };
const HANDLES = { size: 12, items: 0, len: 4, owner: 8 };
const REF = { size: 16, present: 0, value: 8 };
const BOOL = { size: 2, present: 0, value: 1 };
const NUM = { size: 16, present: 0, value: 8 };
const OPTS = { size: 36, paths: 0, pathsLen: 4, names: 8, texts: 12, sourcesLen: 16, libraryDir: 20 };

// A SysML Toolkit dependency pulls in wasm-bindgen glue imports that the binding library never
// calls on its own paths (descriptors, externref table management, the random source for UUID v4).
// The set changes with SysML Toolkit versions, so the stubs are built from what the module
// declares: the table functions get harmless answers, anything else throws a clear error if
// ever called.
function stubImports(module) {
  const imports = {};
  for (const imp of WebAssembly.Module.imports(module)) {
    if (imp.kind !== "function") continue;
    const ns = (imports[imp.module] ??= {});
    if (imp.name === "__wbindgen_externref_table_grow") ns[imp.name] = () => 0;
    else if (imp.name === "__wbindgen_externref_table_set_null" || imp.name === "__wbindgen_describe") ns[imp.name] = () => {};
    else ns[imp.name] = () => { throw new ToolkitError(`unexpected call to host import ${imp.module}.${imp.name}`); };
  }
  return imports;
}

export class ToolkitError extends Error {}

/** One instantiated module, shared by every session created from it. */
export class ToolkitLibrary {
  /** @param {WebAssembly.Instance} instance */
  constructor(instance) {
    this.w = instance.exports;
    this.enc = new TextEncoder();
    this.dec = new TextDecoder();
    const p = this.w.sysmlv2_names_digest(), n = this.w.sysmlv2_names_digest_len();
    const have = this.dec.decode(new Uint8Array(this.w.memory.buffer, p, n));
    if (have !== NAMES_DIGEST) {
      throw new ToolkitError(
        `binding library was generated from naming table ${have.slice(0, 12)}, ` +
        `this SDK from ${NAMES_DIGEST.slice(0, 12)}; regenerate one side`);
    }
  }

  /** Load from bytes, a Response (browser fetch), or a file path (Node). */
  static async load(source) {
    let bytes = source;
    if (typeof source === "string") {
      const { readFileSync } = await import("node:fs");
      bytes = readFileSync(source);
    } else if (typeof Response !== "undefined" && source instanceof Response) {
      bytes = await source.arrayBuffer();
    }
    const module = await WebAssembly.compile(bytes);
    const instance = await WebAssembly.instantiate(module, stubImports(module));
    return new ToolkitLibrary(instance);
  }

  /** SYSMLV2_ABI_WASM; else the module the package carries; else the repository's build (Node only). */
  static async loadDefault() {
    const env = typeof process !== "undefined" && process.env.SYSMLV2_ABI_WASM;
    if (env) return ToolkitLibrary.load(env);
    const { dirname, join } = await import("node:path");
    const { existsSync } = await import("node:fs");
    const { fileURLToPath } = await import("node:url");
    const here = dirname(fileURLToPath(import.meta.url));
    const packaged = join(here, "sysmlv2_abi.wasm");
    if (existsSync(packaged)) return ToolkitLibrary.load(packaged);
    return ToolkitLibrary.load(join(here, "..", "abi", "target", "wasm32-unknown-unknown", "release", "sysmlv2_abi.wasm"));
  }

  view() { return new DataView(this.w.memory.buffer); }

  /** Write a JS string into module memory as a Str; returns the Str's address. Caller frees. */
  putStr(text) {
    const b = this.enc.encode(text);
    const buf = this.w.sysmlv2_alloc(Math.max(1, b.length));
    new Uint8Array(this.w.memory.buffer, buf, b.length).set(b);
    const a = this.w.sysmlv2_alloc(STR.size);
    const v = this.view();
    v.setUint32(a + STR.ptr, buf, true);
    v.setUint32(a + STR.len, b.length, true);
    v.setUint8(a + STR.present, 1);
    v.setUint32(a + STR.owner, 0, true);
    return { addr: a, buf, len: b.length };
  }

  dropStr(s) {
    this.w.sysmlv2_dealloc(s.buf, Math.max(1, s.len));
    this.w.sysmlv2_dealloc(s.addr, STR.size);
  }

  takeStr(a) {
    const v = this.view();
    const present = v.getUint8(a + STR.present) !== 0;
    const p = v.getUint32(a + STR.ptr, true), n = v.getUint32(a + STR.len, true);
    const s = present && p ? this.dec.decode(new Uint8Array(this.w.memory.buffer, p, n)) : null;
    this.w.sysmlv2_free(v.getUint32(a + STR.owner, true));
    return s;
  }

  takeHandles(a) {
    const v = this.view();
    const items = v.getUint32(a + HANDLES.items, true), n = v.getUint32(a + HANDLES.len, true);
    const out = [];
    for (let i = 0; i < n; i++) out.push(v.getBigUint64(items + 8 * i, true).toString());
    this.w.sysmlv2_free(v.getUint32(a + HANDLES.owner, true));
    return out;
  }
}

let defaultLibrary = null;

/** The `.sysml` and `.kerml` files below `dir`, as unit names to text, read as the SysML Toolkit
 * reads a library directory itself: in the order of their paths, compared by component, and each
 * named by its file name. The unnamed elements of the library take their ids from these names, so
 * a library read here has the ids the native build and the library JSON have. Node only. */
async function readLibraryDir(dir) {
  const { readdirSync, readFileSync } = await import("node:fs");
  const { join } = await import("node:path");
  const byComponent = (a, b) => {
    const x = a.split("/"), y = b.split("/");
    for (let i = 0; i < Math.min(x.length, y.length); i++) {
      if (x[i] !== y[i]) return x[i] < y[i] ? -1 : 1;
    }
    return x.length - y.length;
  };
  const files = readdirSync(dir, { recursive: true })
    .map((f) => String(f).replaceAll("\\", "/"))
    .filter((f) => f.endsWith(".sysml") || f.endsWith(".kerml"))
    .sort(byComponent);
  const units = {};
  for (const f of files) {
    const name = f.slice(f.lastIndexOf("/") + 1);
    if (name in units) throw new ToolkitError(`two library files are named ${name}; the SysML Toolkit names units by file name`);
    units[name] = readFileSync(join(dir, f), "utf8");
  }
  return units;
}

export class ToolkitBackend {
  /** @param {ToolkitLibrary} lib @param {number} session */
  constructor(lib, session) {
    this.lib = lib;
    this.s = session;
    this.mcCache = new Map();
    this.declCache = new Map();
  }

  /**
   * Open a session over `sources`: unit names to text (this build reads no files).
   *
   * The standard library, if the model needs it: `librarySources`, its `.sysml` and `.kerml`
   * files, each by its file name without the directories, to text (in a browser, fetch them), or
   * in Node `libraryDir`, the library's directory, whose files are read for you that way. Either way it is loaded as a library: its
   * elements are not among the model's (`all()`, `elementsOfType`) and report `isLibraryElement`.
   * `library` is the WebAssembly module to use (see ToolkitLibrary), by default the packaged one.
   */
  static async open({ sources, librarySources, libraryDir, library } = {}) {
    const lib = library ?? (defaultLibrary ??= await ToolkitLibrary.loadDefault());
    const w = lib.w;
    const names = Object.keys(sources ?? {});
    if (names.length === 0) throw new ToolkitError("open needs sources");
    const units = librarySources ?? (libraryDir != null ? await readLibraryDir(libraryDir) : {});
    const unitNames = Object.keys(units);
    if (unitNames.length > 0 && typeof w.sysmlv2_session_open_with_library_sources !== "function")
      throw new ToolkitError("this binding library predates library sources; use the one this package carries");
    const owned = []; // [address, size] of every allocation to release after the call
    const strs = []; // Str values to release
    const strArray = (texts) => {
      const size = Math.max(1, STR.size * texts.length);
      const arr = w.sysmlv2_alloc(size);
      owned.push([arr, size]);
      texts.forEach((text, i) => {
        const s = lib.putStr(text);
        strs.push(s);
        new Uint8Array(w.memory.buffer, arr + i * STR.size, STR.size).set(new Uint8Array(w.memory.buffer, s.addr, STR.size));
      });
      return arr;
    };
    try {
      const opts = w.sysmlv2_alloc(OPTS.size);
      owned.push([opts, OPTS.size]);
      new Uint8Array(w.memory.buffer, opts, OPTS.size).fill(0);
      const nameArr = strArray(names);
      const textArr = strArray(names.map((n) => sources[n]));
      const v = lib.view();
      v.setUint32(opts + OPTS.names, nameArr, true);
      v.setUint32(opts + OPTS.texts, textArr, true);
      v.setUint32(opts + OPTS.sourcesLen, names.length, true);
      const out = w.sysmlv2_alloc(4);
      owned.push([out, 4]);
      let st;
      if (unitNames.length > 0) {
        const libNames = strArray(unitNames);
        const libTexts = strArray(unitNames.map((n) => units[n]));
        st = w.sysmlv2_session_open_with_library_sources(opts, libNames, libTexts, unitNames.length, out);
      } else {
        st = w.sysmlv2_session_open(opts, out);
      }
      if (st !== OK) throw new ToolkitError(`the SysML Toolkit could not load the model: status ${st}`);
      return new ToolkitBackend(lib, lib.view().getUint32(out, true));
    } finally {
      for (const s of strs) lib.dropStr(s);
      for (const [addr, size] of owned) w.sysmlv2_dealloc(addr, size);
    }
  }

  close() {
    if (this.s) { this.lib.w.sysmlv2_session_close(this.s); this.s = 0; }
  }

  // -- errors --------------------------------------------------------------------------

  #lastError() {
    const a = this.lib.w.sysmlv2_alloc(STR.size);
    this.lib.w.sysmlv2_last_error(this.s, a);
    const msg = this.lib.takeStr(a) ?? "";
    this.lib.w.sysmlv2_dealloc(a, STR.size);
    return msg;
  }

  /** True when a value is present; false for NOT_APPLICABLE; throws otherwise. */
  #check(st, where) {
    if (st === OK) return true;
    if (st === NOT_APPLICABLE) return false;
    const msg = this.#lastError();
    if (st === NOT_IMPLEMENTED) throw new NotImplementedInToolkit(msg || `${where}: not implemented by the SysML Toolkit`);
    if (st === INVALID_HANDLE) throw new Gone(msg || `${where}: invalid handle`);
    if (st === UNRESOLVED) throw new UnresolvedReference(msg || `${where}: a reference did not resolve`);
    throw new ToolkitError(`${where}: status ${st}: ${msg}`);
  }

  #call(symbol, code, handle, where) {
    return this.#invoke(symbol, code, handle, [], where);
  }

  /** Call `symbol` with (session, handle, ...args, out) and unpack the result by `code`. */
  #invoke(symbol, code, handle, args, where) {
    const w = this.lib.w;
    const size = { H: HANDLES.size, S: STR.size, T: STR.size, B: BOOL.size }[code] ?? NUM.size;
    const out = w.sysmlv2_alloc(size);
    try {
      const st = w[symbol](this.s, BigInt(handle), ...args, out);
      if (!this.#check(st, where)) return code === "H" || code === "T" ? [] : null;
      const v = this.lib.view();
      switch (code) {
        case "H": return this.lib.takeHandles(out).map((h) => ({ "@id": h }));
        case "R": return v.getUint8(out + REF.present) ? { "@id": v.getBigUint64(out + REF.value, true).toString() } : null;
        case "S": return this.lib.takeStr(out);
        case "T": { // a list of strings (a requirement's `text`), as JSON text
          const text = this.lib.takeStr(out);
          return text == null ? [] : JSON.parse(text);
        }
        case "B": return v.getUint8(out + BOOL.present) ? v.getUint8(out + BOOL.value) !== 0 : null;
        case "I": return v.getUint8(out + NUM.present) ? Number(v.getBigInt64(out + NUM.value, true)) : null;
        default:  return v.getUint8(out + NUM.present) ? v.getFloat64(out + NUM.value, true) : null;
      }
    } finally {
      w.sysmlv2_dealloc(out, size);
    }
  }

  /**
   * A specification operation. Each argument is passed as the binding library's parameter C type
   * says: an element as its handle, an array of elements as a handle array, a boolean as a C
   * bool, anything else as text. The library answers or refuses.
   */
  call(handle, op, args) {
    const mc = this.metaclass(handle);
    const where = `${mc}.${op}()`;
    const e = this.#declaring(mc, `${op}()`);
    if (!e) throw new ToolkitError(`${where}: the binding library has no such operation`);
    const types = e[2] ? e[2].split(";") : [];
    const w = this.lib.w;
    const owned = [];  // [address, size] to release after the call
    const strs = [];
    try {
      const values = types.map((type, i) => {
        const a = args[i];
        // The binding library has no "no element": refuse it rather than pass a handle that names
        // some other element.
        if (type === "Handle") {
          if (a?.$handle == null) throw new TypeError(`${where}: argument ${i + 1} must be an element, not ${a}`);
          return BigInt(a.$handle);
        }
        if (type === "bool") return a ? 1 : 0;
        if (type === "Handles") {
          const items = Array.isArray(a) ? a : [];
          const arr = w.sysmlv2_alloc(Math.max(8, 8 * items.length));
          owned.push([arr, Math.max(8, 8 * items.length)]);
          const hs = w.sysmlv2_alloc(HANDLES.size);
          owned.push([hs, HANDLES.size]);
          const v = this.lib.view();
          items.forEach((x, k) => {
            if (x?.$handle == null) throw new TypeError(`${where}: argument ${i + 1} must hold elements only, not ${x}`);
            v.setBigUint64(arr + 8 * k, BigInt(x.$handle), true);
          });
          v.setUint32(hs + HANDLES.items, arr, true);
          v.setUint32(hs + HANDLES.len, items.length, true);
          v.setUint32(hs + HANDLES.owner, 0, true);
          return hs;
        }
        const s = this.lib.putStr(a == null ? "" : typeof a === "string" ? a : a.$id ?? String(a));
        strs.push(s);
        return s.addr;
      });
      return this.#invoke(e[0], e[1], handle, values, where);
    } finally {
      for (const s of strs) this.lib.dropStr(s);
      for (const [addr, size] of owned) w.sysmlv2_dealloc(addr, size);
    }
  }

  #declaring(mc, prop) {
    const key = `${mc}::${prop}`;
    if (this.declCache.has(key)) return this.declCache.get(key);
    const seen = new Set(), stack = [mc];
    let found = null;
    while (stack.length) {
      const c = stack.pop();
      if (seen.has(c)) continue;
      seen.add(c);
      const e = FUNCS[`${c}::${prop}`];
      if (e) { found = e; break; }
      stack.push(...(BASES[c] ?? []));
    }
    this.declCache.set(key, found);
    return found;
  }

  // -- the backend contract -------------------------------------------------------------

  get(handle, prop) {
    const mc = this.metaclass(handle);
    const e = this.#declaring(mc, prop);
    if (!e) return null;  // not a member of this metaclass: absent, as the payload backend answers
    return this.#call(e[0], e[1], handle, `${mc}.${prop}`);
  }

  metaclass(handle) {
    let mc = this.mcCache.get(handle);
    if (mc !== undefined) return mc;
    const a = this.lib.w.sysmlv2_alloc(STR.size);
    try {
      const st = this.lib.w.sysmlv2_metaclass(this.s, BigInt(handle), a);
      this.#check(st, `metaclass(${handle})`);
      mc = this.lib.takeStr(a) ?? "";
    } finally {
      this.lib.w.sysmlv2_dealloc(a, STR.size);
    }
    this.mcCache.set(handle, mc);
    return mc;
  }

  elementId(handle) {
    return this.#call("sysmlv2_element_element_id", "S", handle, "elementId") ?? "";
  }

  resolve(qualifiedName) {
    const w = this.lib.w;
    const arg = this.lib.putStr(qualifiedName);
    const out = w.sysmlv2_alloc(REF.size);
    try {
      const st = w.sysmlv2_session_resolve(this.s, arg.addr, out);
      if (!this.#check(st, `resolve(${qualifiedName})`)) return null;
      const v = this.lib.view();
      return v.getUint8(out + REF.present) ? v.getBigUint64(out + REF.value, true).toString() : null;
    } finally {
      this.lib.dropStr(arg);
      w.sysmlv2_dealloc(out, REF.size);
    }
  }

  allHandles() {
    return this.#handlesOf("sysmlv2_user_elements", "user_elements");
  }

  roots() {
    return this.#handlesOf("sysmlv2_session_roots", "roots");
  }

  /**
   * The model as full-form interchange JSON, from the SysML Toolkit's own emitter. With
   * `closures` (the default) the inheritance-aware properties carry the specification's values,
   * inherited and imported members included; without, the owned side only, as the SysML Toolkit's
   * command-line export writes them.
   */
  fullJson({ closures = true } = {}) {
    const a = this.lib.w.sysmlv2_alloc(STR.size);
    try {
      this.#check(this.lib.w.sysmlv2_session_full_json(this.s, closures ? 1 : 0, a), "full_json");
      return this.lib.takeStr(a) ?? "[]";
    } finally {
      this.lib.w.sysmlv2_dealloc(a, STR.size);
    }
  }

  #handlesOf(symbol, where) {
    const a = this.lib.w.sysmlv2_alloc(HANDLES.size);
    try {
      this.#check(this.lib.w[symbol](this.s, a), where);
      return this.lib.takeHandles(a);
    } finally {
      this.lib.w.sysmlv2_dealloc(a, HANDLES.size);
    }
  }
}
