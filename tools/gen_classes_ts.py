"""TypeScript backend of the SDK generator.

Hierarchy strategy for single-inheritance languages: the metaclass
hierarchy becomes *interfaces* (TS interfaces support multiple
`extends`), all elements share one runtime representation (a Proxy),
and narrowing is done with one typed guard `is(el, "PartUsage")` whose
K→type mapping is the generated ClassMap.

Member naming (tools/naming.py): properties are read-only properties under
the specification name; operations are methods under theirs, except an
operation that shares its name with a property (`instantiatedType`) or with
a key the Proxy answers itself (`valueOf`), which becomes
`instantiatedTypeOp()` / `valueOfOp()`. Every interface declares its whole
catalogue, operations included.

Emits into ts/:
  meta.mjs      runtime metadata: PROPS, OPS, HIERARCHY (ancestor sets), ABSTRACT
  classes.d.ts  the interface hierarchy + ClassMap (compile-time surface)
"""

import json
import sys

from naming import MM, ROOT, TS_RUNTIME_NAMES, as_meta, flat_ops, op_member

OUT = ROOT / "ts"
OUT.mkdir(exist_ok=True)

def ts_type(meta):
    """Table row -> TS type (typed getters, table 0.5.0). Enum values are
    lowercase spec strings, typed as string for now."""
    if meta["kind"] == "element":
        base = meta["type"] if meta["type"] != "Element" else "AnyElement"
    elif meta["type"] == "Boolean":
        base = "boolean"
    elif meta["type"] in ("Integer", "Real"):
        base = "number"
    else:
        base = "string"
    return f"{base}[]" if meta["shape"] == "A" else f"{base} | null"

# Collision rule (TS backend): runtime members are $-prefixed
# ($id, $metaclass, $raw) and spec camelCase can never start with $. The
# Proxy additionally intercepts these keys before the member lookup —
# keep in sync with SPECIAL in ts/runtime.mjs.
PROXY_INTERCEPTED = set(TS_RUNTIME_NAMES)


def check_collisions():
    bad = []
    for cls, info in MM.items():
        for nm in list(info["props"]) + [o["name"] for o in info["ops"] if o["name"]]:
            if nm.startswith("$"):
                bad.append(f"spec name {cls}.{nm} starts with '$'")
        ops = {op_member(op["name"], "ts") for _, op in flat_ops(cls)}
        for nm in set(info["props"]) | ops:
            if nm in PROXY_INTERCEPTED:
                bad.append(f"spec member {cls}.{nm} is masked by the Proxy runtime")
        for nm in ops & set(info["props"]):
            bad.append(f"operation {cls}.{nm} collides with a property")
    if bad:
        sys.exit("collision rule violated:\n  " + "\n  ".join(bad))


def ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def main():
    check_collisions()
    # ---- meta.mjs ----------------------------------------------------------
    props = {}
    ops = {}
    hier = {}
    for name, info in MM.items():
        if info["props"]:
            props[name] = {
                p: [m["shape"], int(m["derived"])]
                for p, m in sorted(info["props"].items())
            }
        # member name -> [declaring class, specification name]
        flat = flat_ops(name)
        if flat:
            ops[name] = {op_member(op["name"], "ts"): [decl, op["name"]] for decl, op in flat}
        hier[name] = sorted(ancestors(name, set()))
    meta = [
        "// Generated — do not edit. Source: metamodel.json",
        f"export const PROPS = {json.dumps(props)};",
        f"export const OPS = {json.dumps(ops)};",
        f"export const HIERARCHY = {json.dumps(hier)};",
        f"export const ABSTRACT = {json.dumps(sorted(n for n, i in MM.items() if i['abstract']))};",
    ]
    (OUT / "meta.mjs").write_text("\n".join(meta), encoding="utf-8", newline="\n")

    # ---- classes.d.ts ------------------------------------------------------
    lines = [
        "// Generated — do not edit. Source: metamodel.json",
        "// The metaclass hierarchy as interfaces (multiple extends is legal",
        "// in TS); runtime representation is a Proxy — narrow with is().",
        "// Unresolved references THROW UnresolvedReference at access time",
        "// (the SDK requires resolved models), so reference types are clean.",
        "",
        "// Any element: the runtime members, and the members of the metaclass Element, which",
        "// every element has.",
        "export interface AnyElement {",
        "  readonly $id: string;      // runtime members are $-prefixed:",
        "  readonly $handle: string;  // spec camelCase can never collide",
        "  readonly $metaclass: string;",
        "  $raw(prop: string): unknown;",
    ]
    for p, m in sorted(MM["Element"]["props"].items()):
        lines.append(f"  readonly {p}: {ts_type(m)};")
    for _, op in flat_ops("Element"):
        args = ", ".join(f"{a}: {ts_type(as_meta(tr))}" for a, tr in zip(op["params"], op["paramTypes"]))
        ret = ts_type(as_meta(op["returns"])) if op["returns"] else "unknown"
        lines.append(f"  {op_member(op['name'], 'ts')}({args}): {ret};")
    lines += ["}", ""]
    n_props = 0
    for name, info in sorted(MM.items()):
        if name == "Element":
            continue
        bases = [b if b != "Element" else "AnyElement" for b in info["bases"]] or [
            "AnyElement"
        ]
        lines.append(f"export interface {name} extends {', '.join(bases)} {{")
        for p, m in sorted(info["props"].items()):
            # re-declaring inherited props identically is legal; keep flat
            lines.append(f"  readonly {p}: {ts_type(m)};")
            n_props += 1
        for _, op in flat_ops(name):
            args = ", ".join(f"{a}: {ts_type(as_meta(tr))}"
                             for a, tr in zip(op["params"], op["paramTypes"]))
            ret = ts_type(as_meta(op["returns"])) if op["returns"] else "unknown"
            lines.append(f"  {op_member(op['name'], 'ts')}({args}): {ret};")
        lines.append("}")
        lines.append("")
    conc = sorted(n for n, i in MM.items() if not i["abstract"])
    lines.append("// Concrete metaclasses only — the mintable @type values.")
    lines.append("export interface ClassMap {")
    for n in conc:
        lines.append(f"  {n}: {n};")
    lines.append("}")
    lines.append("")
    lines.append("// Every metaclass, abstract included — the narrowable kinds")
    lines.append("// (is(el, \"Classifier\") narrows to an abstract interface).")
    lines.append("export interface TypeMap {")
    for n in sorted(MM):
        lines.append(f"  {n}: {'AnyElement' if n == 'Element' else n};")
    lines.append("}")
    lines.append("")
    (OUT / "classes.d.ts").write_text("\n".join(lines), encoding="utf-8", newline="\n")
    print(f"ts backend: {len(MM) - 1} interfaces, {n_props} property rows, "
          f"{len(conc)} in ClassMap")


if __name__ == "__main__":
    main()
