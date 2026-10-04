// Hand-written typings for the runtime (runtime.mjs / index.mjs).
// The generated interface surface lives in classes.d.ts.
import type { AnyElement, ClassMap, TypeMap } from "./classes.d.ts";

export type { AnyElement, ClassMap, TypeMap } from "./classes.d.ts";

export class NotComputed extends Error {}
export class NotImplementedInToolkit extends Error {}
export class Gone extends Error {}
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
}

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
  resolve(qualifiedName: string): AnyElement | null;
  wrap(raw: unknown, at?: string): unknown;
  call(handle: string, metaclass: string, op: string, args: readonly unknown[]): unknown;
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
export declare const ABSTRACT: string[];
