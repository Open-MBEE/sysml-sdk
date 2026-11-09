// A backend over an engine that speaks the SDK's engine protocol (docs/engine-protocol.md), for
// JavaScript in Node: the engine runs as a process and exchanges one JSON request and one JSON
// answer per line over its standard input and output. The Backend contract is synchronous, so the
// process lives in a worker thread and each request waits for its answer (Atomics.wait); starting
// the engine and reading the model are waited for without blocking the event loop.
//
// Errors: a property the element's metaclass does not have reads as absent; a member the engine does
// not answer throws NotAnswered; a reference it could not resolve throws UnresolvedReference; an
// element no longer known throws Gone; a failure inside the engine, an operation the element's
// metaclass does not have, or an engine that cannot start throws BackendError.

import { BackendError, Gone, NotAnswered, Refused, UnresolvedReference } from "./runtime.mjs";

export const PROTOCOL = 1;

// Node's modules, imported on the first start: the package also serves browsers, which have none.
let node = null;
let receiveMessageOnPort = null;
export async function nodeModules() {
  if (!node) {
    const [fs, path, url, threads] = await Promise.all(
      ["node:fs", "node:path", "node:url", "node:worker_threads"].map((m) => import(m)));
    node = { existsSync: fs.existsSync, join: path.join, dirname: path.dirname, resolve: path.resolve,
             fileURLToPath: url.fileURLToPath, MessageChannel: threads.MessageChannel, Worker: threads.Worker };
    receiveMessageOnPort = threads.receiveMessageOnPort;
  }
  return node;
}

class NotApplicable extends Error {}

function argument(a) {
  if (a !== null && typeof a === "object" && typeof a.$handle === "string") return { "@id": a.$handle };
  if (Array.isArray(a)) return a.map(argument);
  return a ?? null;
}

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

export class EngineBackend {
  #worker;
  #port;
  #counter;
  #next = 0;
  #closed = false;

  constructor(worker, port, counter, ready) {
    this.#worker = worker;
    this.#port = port;
    this.#counter = counter;
    /** The engine's name and version, as it announced them. */
    this.engine = ready.engine ?? "the engine";
    this.version = ready.version ?? "";
    /** What the engine said about opening the model (engine-specific). */
    this.stats = null;
  }

  /**
   * Start the engine `command` (the program and its arguments) and read a model with it: files and
   * directories of .sysml and .kerml files (`paths`), and in-memory `sources` (unit name to text).
   * `elaborate` asks for the implied relationships; `library` names a standard library directory
   * (default: the engine's own).
   */
  static async start(command, { paths = [], sources = {}, elaborate = true, library = null } = {}) {
    const n = await nodeModules();
    const [program, ...args] = command;
    const { port1, port2 } = new n.MessageChannel();
    const signal = new SharedArrayBuffer(4);
    const worker = new n.Worker(new URL("./engine_worker.mjs", import.meta.url), {
      workerData: { command: program, args, port: port2, signal },
      transferList: [port2],
    });
    worker.unref();
    const counter = new Int32Array(signal);
    let first;
    while (!(first = receiveMessageOnPort(port1))) await sleep(20);
    first = first.message;
    const fail = async (message) => {
      await worker.terminate();
      throw new BackendError(message);
    };
    if (first.failed) return fail(`cannot start the engine ${program}: ${first.failed}`);
    if (first.exit !== undefined) return fail(`the engine ${program} ended before it was ready (status ${first.exit})`);
    let ready;
    try { ready = JSON.parse(first.line); } catch {
      return fail(`the engine ${program} did not start (it said '${first.line}')`);
    }
    if (!ready.ready) return fail(`the engine is not available: ${ready.error?.message ?? first.line}`);
    if (ready.protocol !== PROTOCOL) return fail(`the engine speaks protocol ${ready.protocol}, this SDK ${PROTOCOL}`);
    const backend = new this(worker, port1, counter, ready);
    try {
      backend.#send("open", { paths: paths.map((p) => n.resolve(p)), sources, elaborate, library });
      let m;
      while (!(m = receiveMessageOnPort(port1))) await sleep(20);
      backend.stats = backend.#answer(m.message);
    } catch (e) {
      backend.close();
      throw e;
    }
    return backend;
  }

  /** The next message from the worker, waiting for it. */
  #receive() {
    for (;;) {
      const seen = Atomics.load(this.#counter, 0);
      const m = receiveMessageOnPort(this.#port);
      if (m) return m.message;
      Atomics.wait(this.#counter, 0, seen, 60_000);
    }
  }

  #send(cmd, fields = {}) {
    if (this.#closed) throw new Error(`this ${this.engine} backend is closed`);
    this.#worker.postMessage(JSON.stringify({ id: ++this.#next, cmd, ...fields }));
  }

  #ask(cmd, fields = {}) {
    this.#send(cmd, fields);
    return this.#answer(this.#receive());
  }

  #answer(m) {
    if (m.line === undefined) {
      this.#closed = true;
      throw new BackendError(`the ${this.engine} process ended while answering (${m.exit !== undefined ? `status ${m.exit}` : m.failed})`);
    }
    const answer = JSON.parse(m.line);
    const error = answer.error;
    if (!error) return answer.ok ?? null;
    if (error.kind === "gone") throw new Gone(error.message);
    if (error.kind === "notApplicable") throw new NotApplicable(error.message);
    if (error.kind === "refused") throw new NotAnswered(`${this.engine}: ${error.message}`);
    if (error.kind === "invalid") throw new Refused(`${this.engine}: ${error.message}`);
    if (error.kind === "unresolved") throw new UnresolvedReference(`${this.engine}: ${error.message}`);
    if (error.kind === "badRequest") throw new Error(error.message);
    throw new BackendError(`${this.engine}: ${error.message}`);
  }

  /** Stop the engine's process. */
  close() {
    if (this.#closed) return;
    try { this.#ask("close"); } catch { /* ended already */ }
    this.#closed = true;
    this.#worker.postMessage(null);
    // Its exit, so that the process is gone before the worker is.
    const deadline = Date.now() + 30_000;
    for (;;) {
      const seen = Atomics.load(this.#counter, 0);
      const m = receiveMessageOnPort(this.#port);
      if (m?.message.exit !== undefined || Date.now() > deadline) break;
      if (!m) Atomics.wait(this.#counter, 0, seen, 1_000);
    }
    this.#worker.terminate();
  }

  // -- the backend contract -----------------------------------------------------------------

  get(handle, prop) {
    try {
      return this.#ask("get", { h: handle, prop });
    } catch (e) {
      if (e instanceof NotApplicable) return null;  // not a member of this metaclass: absent
      throw e;
    }
  }

  metaclass(handle) { return this.#ask("metaclass", { h: handle }); }

  elementId(handle) { return this.#ask("elementId", { h: handle }) ?? ""; }

  resolve(qualifiedName) { return this.#ask("resolve", { name: qualifiedName }); }

  allHandles() { return this.#ask("allHandles"); }

  /** A convenience member, by the protocol's `find`: its name and its arguments by name. */
  find(handle, member, values) {
    const [name, , params] = member;
    const args = Object.fromEntries(params.map(([pname], i) => [pname, argument(values[i])]));
    try {
      return this.#ask("find", { h: handle, member: name, args });
    } catch (e) {
      if (e instanceof NotApplicable) throw new BackendError(`${this.engine}: ${e.message}`);
      throw e;
    }
  }

  /** A convenience member on each element, by the protocol's `findEach`: one request. */
  findEach(handles, member, values) {
    const [name, , params] = member;
    const args = Object.fromEntries(params.map(([pname], i) => [pname, argument(values[i])]));
    try {
      return this.#ask("findEach", { hs: [...handles], member: name, args });
    } catch (e) {
      if (e instanceof NotApplicable) throw new BackendError(`${this.engine}: ${e.message}`);
      throw e;
    }
  }

  /** A specification operation, evaluated by the engine. Elements are passed as references, arrays
   * element by element. */
  call(handle, op, args) {
    try {
      return this.#ask("call", { h: handle, op, args: args.map(argument) });
    } catch (e) {
      if (e instanceof NotApplicable) throw new BackendError(`${this.engine}: ${e.message}`);
      throw e;
    }
  }
}
