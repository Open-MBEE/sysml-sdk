// Hand-written typings for the runtime (runtime.mjs / index.mjs).
// The generated interface surface lives in classes.d.ts.
import type { AnyElement, ClassMap, TypeMap } from "./classes.d.ts";

export type { AnyElement, ClassMap, TypeMap } from "./classes.d.ts";

export class NotComputed extends Error {}
/** A specification member the backend does not answer for this element: the SysML Toolkit's
 * refusals (NotImplementedInToolkit), and an engine's. */
export class NotAnswered extends Error {}
export class NotImplementedInToolkit extends NotAnswered {}
/** A convenience member that gives no answer for this element by its contract (`invalid` in its OCL). */
export class Refused extends NotAnswered {}
/** The scopes of the convenience lists (a literal type and its constants). */
export type Scope = import("./classes.d.ts").Scope;
export declare const Scope: { readonly OWNED: "owned"; readonly INHERITED: "inherited"; readonly ALL: "all" };
/** The visibility filters of the convenience lists (a literal type and its constants). */
export type VisibilityFilter = import("./classes.d.ts").VisibilityFilter;
export declare const VisibilityFilter: { readonly ANY: "any"; readonly PROTECTED: "protected"; readonly PUBLIC: "public" };
/** A literal's value from its tagged JSON; `*` is Infinity. */
export function decodeLiteral(raw: unknown): number | boolean | string;
/** A qualified name's segments as [written, name] pairs: separated by `::`, quoted names unescaped. */
export function splitQualifiedName(qualifiedName: string): [string, string][];
export class Gone extends Error {}
/** The engine behind a backend could not answer: it failed while computing a value, or it is not
 * available (not installed, not downloadable, or no suitable runtime). */
export class BackendError extends Error {}
/** Unresolved reference (SysML Toolkit @ref passthrough or dangling @id):
 * typed navigation throws — the SDK requires resolved models. */
export class UnresolvedReference extends Error {}

/**
 * The backend contract: everything the generated surface needs.
 * The protocol is defined over opaque per-backend element handles; an
 * id-keyed backend (payload, REST) uses element ids as its handles,
 * which is why this payload-backed runtime types them as string.
 */
export interface Backend {
  get(handle: string, prop: string): unknown;
  metaclass(handle: string): string;
  elementId(handle: string): string;
  resolve(qualifiedName: string): string | null | undefined;
  allHandles(): Iterable<string>;
  /** A specification operation by name, with its arguments; throws NotImplementedInToolkit when
   * the backend cannot evaluate it. */
  call(handle: string, op: string, args: readonly unknown[]): unknown;
  /** A convenience member (`member` is its FINDS row) with its arguments, defaults filled in;
   * throws NotAnswered when the backend cannot answer it, Refused when its contract gives none. */
  find(handle: string, member: readonly unknown[], args: readonly unknown[]): unknown;
  /** `find` on each element, a result per handle in order, for the members of an ElementList; a
   * backend without it is called once per element. */
  findEach?(handles: readonly string[], member: readonly unknown[], args: readonly unknown[]): unknown[];
}

/** A list of elements carrying the convenience members of its elements (generated in classes.d.ts):
 * `xs.findPorts()` is the elements' `findPorts()` in list order, list results concatenated (duplicates
 * kept), absent single results dropped. Every element list a convenience member returns is one; lift
 * any array with `ElementList.from(xs)`. `map` and `filter` give plain arrays. */
export type ElementList<T extends AnyElement = AnyElement> = import("./classes.d.ts").ElementList<T>;
export declare const ElementList: {
  new <T extends AnyElement = AnyElement>(...items: T[]): ElementList<T>;
  from<T extends AnyElement>(items: Iterable<T> | ArrayLike<T>): ElementList<T>;
  readonly prototype: ElementList;
};

/** A standard library read from a full-form interchange element array, such as the library JSON
 * published with the SDK. Index it once and pass it to any number of payload models: their
 * references into the library then resolve by the library's normative ids. */
export class PayloadLibrary {
  constructor(elements: ReadonlyArray<Record<string, unknown>>);
  /** The number of library elements. */
  readonly size: number;
}

/** With a library, an id or qualified name the payload does not hold is looked up in the library;
 * the library's elements are not the model's, so `allHandles` lists the payload's only. */
export class PayloadBackend implements Backend {
  constructor(elements: ReadonlyArray<Record<string, unknown>>, library?: PayloadLibrary | null);
  get(handle: string, prop: string): unknown;
  metaclass(handle: string): string;
  elementId(handle: string): string;
  resolve(qualifiedName: string): string | null;
  allHandles(): Iterable<string>;
  call(handle: string, op: string, args: readonly unknown[]): never;
  /** A payload answers no convenience member: throws NotAnswered. */
  find(handle: string, member: readonly unknown[], args: readonly unknown[]): never;
  findEach(handles: readonly string[], member: readonly unknown[], args: readonly unknown[]): never;
}

/** The SysML Toolkit, reached through the WebAssembly build of the binding library (abi/). */
export class ToolkitLibrary {
  /** Bytes, a fetch Response (browser), or a file path (Node). */
  static load(source: BufferSource | Response | string): Promise<ToolkitLibrary>;
  /** SYSMLV2_ABI_WASM, else the module the package carries, else the repository's build. Node only. */
  static loadDefault(): Promise<ToolkitLibrary>;
}

export class ToolkitError extends Error {}

export class ToolkitBackend implements Backend {
  /** Open a session over in-memory sources: unit name to text. The standard library, if the model
   * needs it: `librarySources` (its files, file name without directories to text) or, in Node, `libraryDir` (its
   * directory). It loads as a library: its elements are not among the model's and report
   * `isLibraryElement`. `library` is the WebAssembly module, by default the packaged one. */
  static open(options: {
    sources: Record<string, string>;
    librarySources?: Record<string, string>;
    libraryDir?: string;
    library?: ToolkitLibrary;
  }): Promise<ToolkitBackend>;
  close(): void;
  /** The model as full-form interchange JSON, from the SysML Toolkit's own emitter. With
   * `closures` (the default) the inheritance-aware properties carry the specification's values;
   * without, the owned side only, as the SysML Toolkit's command-line export writes them. */
  fullJson(options?: { closures?: boolean }): string;
  roots(): string[];
  get(handle: string, prop: string): unknown;
  metaclass(handle: string): string;
  elementId(handle: string): string;
  resolve(qualifiedName: string): string | null;
  allHandles(): Iterable<string>;
  call(handle: string, op: string, args: readonly unknown[]): unknown;
  find(handle: string, member: readonly unknown[], args: readonly unknown[]): unknown;
  findEach(handles: readonly string[], member: readonly unknown[], args: readonly unknown[]): unknown[];
}

/** An engine that speaks the SDK's engine protocol (docs/engine-protocol.md), in a process (Node
 * only). Errors: a property the element's metaclass lacks reads as absent; a member the engine does
 * not answer throws NotAnswered; a reference it could not resolve throws UnresolvedReference; an
 * unknown element throws Gone; a failure inside the engine, an operation the metaclass does not
 * have, or an engine that cannot start throws BackendError. */
export class EngineBackend implements Backend {
  /** Start the engine `command` (the program and its arguments) and read a model with it: files and
   * directories (`paths`) and in-memory `sources` (unit name to text). `elaborate` asks for the
   * implied relationships; `library` names a standard library directory (default: the engine's). */
  static start(command: readonly string[], options?: {
    paths?: readonly string[];
    sources?: Record<string, string>;
    elaborate?: boolean;
    library?: string | null;
  }): Promise<EngineBackend>;
  /** The engine's name and version, as it announced them. */
  readonly engine: string;
  readonly version: string;
  /** What the engine said about opening the model (engine-specific). */
  readonly stats: Record<string, unknown>;
  /** Stop the engine's process. */
  close(): void;
  get(handle: string, prop: string): unknown;
  metaclass(handle: string): string;
  elementId(handle: string): string;
  resolve(qualifiedName: string): string | null;
  allHandles(): Iterable<string>;
  call(handle: string, op: string, args: readonly unknown[]): unknown;
  find(handle: string, member: readonly unknown[], args: readonly unknown[]): unknown;
  findEach(handles: readonly string[], member: readonly unknown[], args: readonly unknown[]): unknown[];
}

/** The SysML v2 Pilot Implementation, in a Java process (Node only): an EngineBackend. Its answers
 * are the Pilot's: the two implementations differ in places. */
export class PilotBackend extends EngineBackend {
  /** Read a model: files and directories of .sysml and .kerml files (`paths`), and in-memory
   * `sources` (unit name to text). With `elaborate` (the default), the Pilot adds the implied
   * relationships and implicit elements. The Pilot is SYSML_PILOT_HOME's, else the copy in the
   * user's cache, else the pinned release, downloaded there on the first use (about 130 MB);
   * `pilotHome`, or `pilotJar` and `pilotLibrary` together, name another. A Java 21 or newer
   * runtime runs it: `java`, else JAVA_HOME's, else the PATH's; `heap` is its maximum heap.
   * `server` names the SDK's Pilot engine jar (default: SYSML_PILOT_SERVER, else the package's). */
  static open(options?: {
    paths?: readonly string[];
    sources?: Record<string, string>;
    elaborate?: boolean;
    java?: string;
    heap?: string;
    pilotHome?: string;
    pilotJar?: string;
    pilotLibrary?: string;
    server?: string;
  }): Promise<PilotBackend>;
  /** The version of the Pilot that reads the model. */
  readonly pilotVersion: string;
  /** What opening took: the Pilot's version (`pilot`), `libraryMs`, `modelMs`, `resources`. */
  readonly stats: { pilot: string; libraryMs: number; modelMs: number; resources: number };
}

/** The standard library is not at hand: this copy carries no models, or the JSON could not be
 * downloaded and checked. */
export class LibraryUnavailable extends Error {}

/** The directory of the standard library models the npm package carries, for `libraryDir`. Node only. */
export function standardLibrary(): Promise<string>;

/** The standard library as full-form JSON, for PayloadLibrary: the file SYSML_LIBRARY_JSON names,
 * else the cached copy, downloaded from this version's GitHub release on the first call and checked
 * against the SHA-256 GitHub states for it. Resolves to the file's path. Node only. */
export function standardLibraryJson(options?: {
  version?: string;
  cacheDir?: string;
  fetch?: typeof globalThis.fetch;
}): Promise<string>;

export class Model {
  constructor(backend: Backend);
  /** A model read from a full-form interchange element array. With `library`, references into the
   * standard library resolve against it; its elements are not listed as the model's. */
  static fromFullJson(
    elements: ReadonlyArray<Record<string, unknown>>,
    options?: { library?: PayloadLibrary | null },
  ): Model;
  readonly backend: Backend;
  element(id: string): AnyElement;
  /** Name resolution with visibility: from `context` (a namespace) by the specification's
   * Namespace::resolve, else the backend's resolution from the root. */
  resolve(name: string, context?: AnyElement | null): AnyElement | null;
  wrap(raw: unknown, at?: string): unknown;
  call(handle: string, metaclass: string, op: string, args: readonly unknown[]): unknown;
  /** A convenience member (`member` is its FINDS row), answered by the backend. */
  findMember(handle: string, metaclass: string, member: readonly unknown[], args: readonly unknown[]): unknown;
  /** The element a qualified name names, whatever its visibility, the same on every backend; with
   * `inherited`, also through inherited memberships. */
  find(qualifiedName: string, inherited?: boolean): AnyElement | null;
  /** A convenience member's arguments, checked, with their defaults filled in. */
  bind(member: readonly unknown[], args: readonly unknown[]): unknown[];
  /** A convenience member's answer from the backend's (element lists as ElementLists). */
  convert(member: readonly unknown[], raw: unknown, at: string): unknown;
  all(): IterableIterator<AnyElement>;
  /** The top-level namespaces: the elements without an owner that are namespaces. */
  roots(): AnyElement[];
}

/** Typed narrowing over the generated hierarchy: metaclass or ancestor. */
export function is<K extends keyof TypeMap>(
  el: unknown,
  kind: K,
): el is TypeMap[K];

export function elementsOfType<K extends keyof TypeMap>(
  model: Model,
  kind: K,
): TypeMap[K][];

export declare const PROPS: Record<
  string,
  Record<string, [shape: string, derived: 0 | 1]>
>;
/** Per metaclass: operation member name to [declaring class, specification name]. */
export declare const OPS: Record<
  string,
  Record<string, [declaringClass: string, specName: string]>
>;
export declare const HIERARCHY: Record<string, string[]>;
/** Per metaclass: convenience member name to [name, binding symbol suffix, params, result]. */
export declare const FINDS: Record<string, Record<string, [string, string, [string, string, unknown][], string]>>;
export declare const ABSTRACT: string[];
