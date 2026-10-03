"""Generate sysml/classes.py from metamodel.json.

Emits the full metaclass hierarchy as Python classes (real multiple
inheritance, redundant bases pruned for C3), property descriptors on
concrete classes, and operation stubs. This is the Python backend of
the (future multi-language) generator.

Member naming (tools/naming.py): properties are descriptors under the
specification name; operations are methods under theirs, except an
operation that shares its name with a property (`instantiatedType`), which
becomes `instantiatedType_op()`.
"""

import sys

from naming import MM, ROOT, as_meta, op_key, op_member

HERE = ROOT  # repository root

PY_KEYWORDS = {"import", "class", "def", "return", "in", "for", "if", "else"}


def check_collisions():
    """Enforce the Python collision rule.

    Rule: every public runtime member on base.Element contains an
    underscore; spec property/operation names are lowerCamelCase and
    never do. Both halves are asserted here so a future metamodel or
    runtime change that breaks the invariant fails the build instead of
    silently shadowing (the way `metaclass` once shadowed the runtime
    accessor on MetadataFeature/MetadataUsage).
    """
    sys.path.insert(0, str(HERE / "python"))
    from sysml.base import Element as RuntimeElement, Op, P

    # Spec operations of Element itself are attached to the runtime base by this
    # generator; they are descriptors, not runtime members, and are excluded here.
    runtime_public = {n for n in dir(RuntimeElement) if not n.startswith("_")
                      and not isinstance(getattr(RuntimeElement, n), (Op, P))}
    bad = [f"runtime member Element.{n} lacks an underscore"
           for n in runtime_public if "_" not in n]
    for cls, info in MM.items():
        for nm in list(info["props"]) + [o["name"] for o in info["ops"] if o["name"]]:
            if "_" in nm:
                bad.append(f"spec name {cls}.{nm} contains an underscore")
            if nm in runtime_public:
                bad.append(f"spec name {cls}.{nm} collides with runtime Element.{nm}")
        for o in info["ops"]:
            member = op_member(o["name"], "python")
            if member != o["name"] and member in runtime_public:
                bad.append(f"operation {cls}.{member} collides with runtime Element.{member}")
            if member in info["props"]:
                bad.append(f"operation {cls}.{member} collides with a property")
    if bad:
        sys.exit("collision rule violated:\n  " + "\n  ".join(bad))


def ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def pruned_bases(name):
    """Drop bases that are already ancestors of other bases (C3 hygiene)."""
    bases = MM[name]["bases"]
    keep = []
    for b in bases:
        others = set()
        for o in bases:
            if o != b:
                ancestors(o, others)
        if b not in others:
            keep.append(b)
    return keep


def topo_order():
    seen, order = set(), []

    def visit(n):
        if n in seen:
            return
        seen.add(n)
        for b in MM[n]["bases"]:
            visit(b)
        order.append(n)

    for n in sorted(MM):
        visit(n)
    return order


def load_toolkit_table():
    """The generated dispatch data (tools/gen_toolkit_table.py), loaded by path so that
    regeneration never imports the package and its possibly stale classes.py."""
    import importlib.util
    path = HERE / "python" / "sysml" / "toolkit_table.py"
    spec = importlib.util.spec_from_file_location("toolkit_table", path)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod


def resolve_symbol(KT, cls, member):
    """(c_symbol, result_code, params) for `member` as seen from `cls`, found on the
    declaring class by walking the bases. Missing rows are a generation failure: the
    naming table must cover every member the metamodel declares."""
    seen, stack = set(), [cls]
    while stack:
        c = stack.pop()
        if c in seen:
            continue
        seen.add(c)
        e = KT.FUNCS.get((c, member))
        if e is not None:
            return e
        stack.extend(MM[c]["bases"])
    raise SystemExit(f"naming table has no row for {cls}::{member} (nor any base)")


def main():
    check_collisions()
    KT = load_toolkit_table()
    lines = [
        '"""Generated metaclass hierarchy — do not edit.',
        "",
        "Source: metamodel.json (XMI 20250201 hierarchy, schema property",
        "catalog, XMI derived flags).",
        '"""',
        "",
        "from .base import Element, Op, P",
        "",
    ]
    n_props = n_ops = 0
    order = topo_order()
    topo_index = {n: i for i, n in enumerate(order)}
    for name in order:
        info = MM[name]
        bases = pruned_bases(name) or (["Element"] if name != "Element" else [])
        # Sort every base list by one global order (most-derived first):
        # all local precedence constraints then embed in a single total
        # order, so C3 linearization cannot fail. isinstance semantics
        # are unaffected; only MRO tie-breaking order changes.
        bases.sort(key=lambda b: -topo_index[b])
        if name == "Element":
            # the spec root merges into the runtime base class; its properties and
            # operations are attached to that class below, after every subclass exists
            lines.append("# 'Element' is the runtime base (base.Element); its properties and")
            lines.append("# operations are attached at the end of this module.")
            lines.append("")
            continue
        base_list = ", ".join(b if b != "Element" else "Element" for b in bases)
        kind = "abstract" if info["abstract"] else "concrete"
        layer = {"kerml": "KerML", "sysml": "SysML"}[info["language"]]
        lines.append(f"class {name}({base_list}):")
        lines.append(f'    """{kind} — {layer} 20250201."""')
        lines.append("    __slots__ = ()")
        for p, meta in sorted(info["props"].items()):
            if p in PY_KEYWORDS or not p.isidentifier():
                continue  # none expected; guard for exotic names
            sym, code, _ = resolve_symbol(KT, name, p)
            lines.append(
                f'    {p} = P("{p}", "{meta["shape"]}", {meta["derived"]}, "{sym}", "{code}")'
            )
            n_props += 1
        for op in info["ops"]:
            oname = op["name"]
            if not oname or not oname.isidentifier():
                continue
            sym, code, params = resolve_symbol(KT, name, op_key(oname))
            member = op_member(oname, "python")
            lines.append(f'    {member} = Op("{oname}", {op["params"]!r}, "{sym}", "{code}", {params!r})')
            n_ops += 1
        lines.append("")

    # Every element has the members of the metaclass Element, whatever its class.
    for p, meta in sorted(MM["Element"]["props"].items()):
        sym, code, _ = resolve_symbol(KT, "Element", p)
        lines.append(f'Element.{p} = P("{p}", "{meta["shape"]}", {meta["derived"]}, "{sym}", "{code}")')
        n_props += 1
    for op in MM["Element"]["ops"]:
        oname = op["name"]
        if not oname or not oname.isidentifier():
            continue
        sym, code, params = resolve_symbol(KT, "Element", op_key(oname))
        member = op_member(oname, "python")
        lines.append(f'Element.{member} = Op("{oname}", {op["params"]!r}, "{sym}", "{code}", {params!r})')
        n_ops += 1
    lines.append("")

    concrete = [n for n in MM if not MM[n]["abstract"]]
    lines.append("REGISTRY = {")
    for n in sorted(concrete):
        lines.append(f'    "{n}": {n},')
    lines.append("}")
    lines.append("")

    out = HERE / "python" / "sysml" / "classes.py"
    out.write_text("\n".join(lines), encoding="utf-8", newline="\n")
    write_stub(order, topo_index, concrete)
    print(
        f"wrote {out.name} (+ .pyi): {len(MM) - 1} classes, {n_props} property "
        f"descriptors, {n_ops} operation stubs, {len(concrete)} in registry"
    )


def py_type(meta):
    """Table row -> Python type annotation (typed getters, table 0.5.0)."""
    if meta["kind"] == "element":
        base = meta["type"]
    elif meta["type"] == "Boolean":
        base = "bool"
    elif meta["type"] == "Integer":
        base = "int"
    elif meta["type"] == "Real":
        base = "float"
    else:  # String + enums (enum convention: lowercase spec strings)
        base = "str"
    return f"list[{base}]" if meta["shape"] == "A" else f"{base} | None"


def write_stub(order, topo_index, concrete):
    """classes.pyi: the typed surface for IDEs and type checkers. The
    runtime stays descriptor-based (dynamic); unresolved references
    raise UnresolvedReference instead of yielding placeholders."""
    lines = [
        '"""Generated type stubs — do not edit. Source: metamodel.json."""',
        "",
        "from .base import Element as _RuntimeElement",
        "",
    ]
    for name in order:
        info = MM[name]
        if name == "Element":
            # The runtime base, with the members every element has.
            lines.append("class Element(_RuntimeElement):")
        else:
            bases = pruned_bases(name) or ["Element"]
            bases.sort(key=lambda b: -topo_index[b])
            lines.append(f"class {name}({', '.join(bases)}):")
        body = []
        for p, meta in sorted(info["props"].items()):
            if p in PY_KEYWORDS or not p.isidentifier():
                continue
            body.append(f"    {p}: {py_type(meta)}")
        for op in info["ops"]:
            oname = op["name"]
            if not oname or not oname.isidentifier():
                continue
            args = "".join(f", {a}: {py_type(as_meta(tr))}"
                           for a, tr in zip(op["params"], op["paramTypes"]))
            ret = py_type(as_meta(op["returns"])) if op["returns"] else "object"
            body.append(f"    def {op_member(oname, 'python')}(self{args}) -> {ret}: ...")
        lines.extend(body if body else ["    ..."])
        lines.append("")
    lines.append("REGISTRY: dict[str, type[Element]]")
    lines.append("")
    (HERE / "python" / "sysml" / "classes.pyi").write_text(
        "\n".join(lines), encoding="utf-8", newline="\n")


if __name__ == "__main__":
    main()
