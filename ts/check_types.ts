// Compile-time gate: the generated interface hierarchy must type-check,
// including every multiple-inheritance diamond, and the ClassMap-keyed
// guard must narrow. Run: npx -p typescript tsc --noEmit --strict check_types.ts
import type {
  ActionUsage, AnyElement, Classifier, Feature, FlowUsage, Namespace,
  PartDefinition, PartUsage, TransitionUsage, Type, TypeMap, Usage,
} from "./classes.d.ts";

declare function is<K extends keyof TypeMap>(el: AnyElement, kind: K): el is TypeMap[K];
declare const el: AnyElement;

if (is(el, "PartDefinition")) {
  const d: PartDefinition = el;                 // narrowing works
  const c: Classifier = d;                      // upcast through the hierarchy
  const t: Type = d;
  const n: Namespace = d;
  const feats: Feature[] = d.ownedFeature;      // element-typed collection
  const name: string | null = d.declaredName;   // typed nullable scalar
  const abstract: boolean | null = d.isAbstract;
  void [c, t, n, feats, name, abstract];
}

if (is(el, "FlowUsage")) {
  // the diamond that broke Python C3: Connector × Step × Usage
  const u: Usage = el;
  const f: FlowUsage = el;
  void [u, f];
}

if (is(el, "TransitionUsage")) {
  const src: ActionUsage | null = el.source;    // element-typed reference
  void src;
}

if (is(el, "Classifier")) {
  const c: Classifier = el;                     // abstract kinds narrow too
  const t: Type = c;
  void t;
}

declare const pu: PartUsage;
const asUsage: Usage = pu;                       // interface extends chain
// @ts-expect-error — Definition is not assignable from PartUsage
const notDef: PartDefinition = pu;
void [asUsage, notDef];

// A payload model with the standard library as JSON: the library is optional and shared.
import type { Model, PayloadLibrary } from "./index.d.ts";
declare const ModelClass: typeof Model;
declare const LibraryClass: typeof PayloadLibrary;
declare const elements: Record<string, unknown>[];
const library: PayloadLibrary = new LibraryClass(elements);
const libraryCount: number = library.size;
const withLibrary: Model = ModelClass.fromFullJson(elements, { library });
const withoutLibrary: Model = ModelClass.fromFullJson(elements);
// @ts-expect-error — the library is a PayloadLibrary, not an element array
ModelClass.fromFullJson(elements, { library: elements });
void [libraryCount, withLibrary, withoutLibrary];
