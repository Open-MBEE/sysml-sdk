"""Java backend of the SDK generator.

Hierarchy strategy: interfaces with *default methods* —
every property and operation carries its body on the interface; one
package-private empty class per concrete metaclass (all in Impls.java —
one file may hold many non-public top-level classes) makes
`el instanceof Classifier` real Java narrowing.

Collision rule (Java): runtime members are $-prefixed ($id, $handle,
$metaclass, $model, $raw) — legal in mechanically generated Java source,
and spec camelCase can never start with $. Enforced by check_collisions()
below, together with the flattened-catalog superset invariant that Java's
default-method diamond rule requires (a class inheriting the same default
from two unrelated interfaces is a compile error; a full own catalog on
every interface makes the own declaration most specific everywhere).

Operations are answered by the model's backend (Model.call): the
SysML Toolkit answers the ones it can and refuses the others; a payload
refuses them all.

Member naming (tools/naming.py): every property is read through a getter,
`get` + the specification name (`getQualifiedName()`); operations keep their
specification names, so the two never collide. Every interface declares its
whole catalogue, operations included, which also keeps the default-method
diamond rule satisfied for operations.

Emits into java/src/<package>/classes/ (wiped first — everything in that
package is generated): one interface file per metaclass, Impls.java,
ClassMap.java. The package comes from tools/naming.py.
"""

import sys

from naming import JAVA_PACKAGE, JAVA_SRC, MM, as_meta, flat_ops, getter, op_member

OUT = JAVA_SRC / "classes"

JAVA_KEYWORDS = set(
    "abstract assert boolean break byte case catch char class const continue "
    "default do double else enum extends final finally float for goto if "
    "implements import instanceof int interface long native new package private "
    "protected public return short static strictfp super switch synchronized "
    "this throw throws transient try void volatile while true false null "
    "var yield record sealed permits".split()
)

RUNTIME_TYPES = {"Element", "El", "Backend", "PayloadBackend", "Model", "Json",
                 "UnresolvedRef", "ClassMap", "Impls"}

# Methods every Java object has; no generated member may take one of these names.
OBJECT_METHODS = {"getClass", "hashCode", "equals", "toString", "notify", "notifyAll",
                  "wait", "clone", "finalize"}

HEADER = "// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py."


def java_sig(meta, qualifier=""):
    """Table row -> (return type, $-converter) for typed getters (0.5.0).
    Enum values are lowercase spec strings, typed String. `qualifier` prefixes
    metaclass types, for code outside the classes package."""
    k, t = meta["kind"], meta["type"]
    if k == "element":
        base, helper = (f"{JAVA_PACKAGE}.Element" if t == "Element" else qualifier + t), "$ref"
    elif t == "Boolean":
        base, helper = "Boolean", "$bool"
    elif t == "Integer":
        base, helper = "Long", "$int"
    elif t == "Real":
        base, helper = "Double", "$real"
    else:
        base, helper = "String", "$string"
    if meta["shape"] == "A":
        return f"java.util.List<{base}>", "$list"
    return base, helper


def ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def check_collisions():
    bad = []
    for cls, info in MM.items():
        if cls != "Element" and (cls in RUNTIME_TYPES or f"{cls}El" in MM):
            bad.append(f"class name {cls} shadows a runtime type or impl name")
        for nm in list(info["props"]) + [o["name"] for o in info["ops"] if o["name"]]:
            if nm.startswith("$"):
                bad.append(f"spec name {cls}.{nm} starts with '$'")
        ops = {op_member(op["name"], "java") for _, op in flat_ops(cls)}
        getters = {getter(p) for p in info["props"]}
        for nm in ops:
            if nm in JAVA_KEYWORDS:
                bad.append(f"operation {cls}.{nm} is a Java keyword (no escape exists)")
        for nm in (ops | getters) & OBJECT_METHODS:
            bad.append(f"member {cls}.{nm} would override a java.lang.Object method")
        for nm in ops & getters:
            bad.append(f"operation {cls}.{nm} collides with a property getter")
        # Java diamond rule: the own catalog must cover every ancestor's
        own = set(info["props"])
        for a in ancestors(cls, set()):
            if a == "Element":
                continue  # spec root merges into the runtime Element interface
            missing = set(MM[a]["props"]) - own
            if missing:
                bad.append(
                    f"{cls} lacks {sorted(missing)} declared by ancestor {a} — "
                    f"default-method diamonds become possible"
                )
    if bad:
        sys.exit("collision rule violated:\n  " + "\n  ".join(bad))


def member_lines(name, qualifier=""):
    """The default methods of a metaclass: a getter per property, then its operations."""
    props, ops = [], []
    for p, meta in sorted(MM[name]["props"].items()):
        rtype, conv = java_sig(meta, qualifier)
        props.append(f'    default {rtype} {getter(p)}() {{ return {conv}($model().read(this, "{p}")); }}')
    for decl, op in flat_ops(name):
        oname = op["name"]
        names = [a + "_" if a in JAVA_KEYWORDS else a for a in op["params"]]
        params = ", ".join(
            f"{java_sig(as_meta(tr), qualifier)[0]} {a}" for a, tr in zip(names, op["paramTypes"])
        )
        call = f'$model().call(this, "{oname}", new Object[] {{{", ".join(names)}}})'
        if op["returns"]:
            rtype, conv = java_sig(as_meta(op["returns"]), qualifier)
            body = f"return {conv}({call});"
        else:
            rtype, body = "Object", f"return {call};"
        ops.append(f"    default {rtype} {op_member(oname, 'java')}({params}) {{ {body} }}")
    return props, ops


def write_runtime_element():
    """The members of the metaclass Element, written into the runtime Element interface between
    its marker lines: every element has them, whatever its class."""
    path = JAVA_SRC / "Element.java"
    text = path.read_text(encoding="utf-8")
    begin, end = "    // BEGIN GENERATED\n", "    // END GENERATED\n"
    head, rest = text.split(begin)
    _, tail = rest.split(end)
    props, ops = member_lines("Element", f"{JAVA_PACKAGE}.classes.")
    body = "".join(line + "\n" for line in props + ops)
    path.write_text(head + begin + body + end + tail, encoding="utf-8", newline="\n")
    return len(props), len(ops)


def main():
    check_collisions()
    OUT.mkdir(parents=True, exist_ok=True)
    for old in OUT.glob("*.java"):
        old.unlink()

    n_props, n_ops = write_runtime_element()
    for name in sorted(MM):
        if name == "Element":
            continue  # the spec root merges into the runtime Element interface
        info = MM[name]
        bases = info["bases"] or ["Element"]
        base_list = ", ".join(f"{JAVA_PACKAGE}.Element" if b == "Element" else b for b in bases)
        kind = "abstract" if info["abstract"] else "concrete"
        layer = {"kerml": "KerML", "sysml": "SysML"}[info["language"]]
        lines = [
            HEADER,
            f"package {JAVA_PACKAGE}.classes;",
            "",
        ]
        lines.append(f"/** {kind} — {layer} 20250201. */")
        lines.append(f"public interface {name} extends {base_list} {{")
        props, ops = member_lines(name)
        lines += props + ops
        n_props += len(props)
        n_ops += len(ops)
        lines.append("}")
        lines.append("")
        (OUT / f"{name}.java").write_text("\n".join(lines), encoding="utf-8",
                                          newline="\n")

    concrete = sorted(n for n in MM if not MM[n]["abstract"])
    impls = [
        HEADER,
        "// One file may hold many non-public top-level classes; every impl",
        "// is an empty shell — behavior lives on the interfaces.",
        f"package {JAVA_PACKAGE}.classes;",
        "",
        f"import {JAVA_PACKAGE}.El;",
        f"import {JAVA_PACKAGE}.Model;",
        "",
    ]
    for name in concrete:
        impls.append(f"final class {name}El extends El implements {name} {{")
        impls.append(f"    {name}El(Model model, String handle) {{ super(model, handle); }}")
        impls.append("}")
        impls.append("")
    (OUT / "Impls.java").write_text("\n".join(impls), encoding="utf-8", newline="\n")

    cm = [
        HEADER,
        f"package {JAVA_PACKAGE}.classes;",
        "",
        "import java.util.Collections;",
        "import java.util.LinkedHashMap;",
        "import java.util.Map;",
        "import java.util.function.BiFunction;",
        "",
        f"import {JAVA_PACKAGE}.Element;",
        f"import {JAVA_PACKAGE}.Model;",
        "",
        "/** Generated maps: metaclass name to interface (all classes) and to",
        " * element factory (concrete only). java.lang.Class is qualified",
        " * throughout — bare Class here is the KerML metaclass interface. */",
        "public final class ClassMap {",
        "    private ClassMap() { }",
        "",
        "    public static final Map<String, java.lang.Class<?>> TYPE_MAP = typeMap();",
        "",
        "    public static final Map<String, BiFunction<Model, String, Element>>",
        "        FACTORIES = factories();",
        "",
        "    private static Map<String, java.lang.Class<?>> typeMap() {",
        "        Map<String, java.lang.Class<?>> m = new LinkedHashMap<>();",
        '        m.put("Element", Element.class);',
    ]
    for name in sorted(n for n in MM if n != "Element"):
        cm.append(f'        m.put("{name}", {name}.class);')
    cm += [
        "        return Collections.unmodifiableMap(m);",
        "    }",
        "",
        "    private static Map<String, BiFunction<Model, String, Element>> factories() {",
        "        Map<String, BiFunction<Model, String, Element>> m = new LinkedHashMap<>();",
    ]
    for name in concrete:
        cm.append(f'        m.put("{name}", {name}El::new);')
    cm += [
        "        return Collections.unmodifiableMap(m);",
        "    }",
        "}",
        "",
    ]
    (OUT / "ClassMap.java").write_text("\n".join(cm), encoding="utf-8", newline="\n")

    print(
        f"java backend: {len(MM) - 1} interfaces, {n_props} property members, "
        f"{n_ops} operation members, {len(concrete)} element classes"
    )


if __name__ == "__main__":
    main()
