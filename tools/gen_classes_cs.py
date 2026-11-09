"""C# backend of the SDK generator.

Hierarchy strategy: the metaclass hierarchy becomes
interfaces with *default interface members* (C# 8) — every property and
operation carries its body on the interface, so the runtime classes are
empty shells that only supply the IElement core. One generated sealed
class per concrete metaclass makes `el is Classifier` real C# type
narrowing (unlike TS, no separate guard function is needed).

Collision rule (C#): runtime members are PascalCase (ElementId,
MetaclassName, GetRaw, Model); spec names are strict lowerCamelCase, so
the first letter keeps the namespaces disjoint. Spec names that are C#
keywords are @-escaped (the reflected name is unaffected). Enforced by
check_collisions() below.

Member naming (tools/naming.py): properties use property syntax under the
specification name; operations are methods under theirs, except an
operation that shares its name with a property (`instantiatedType`), which
becomes `instantiatedTypeOp()`. Every interface declares its whole
catalogue, operations included.

Emits into csharp/<namespace>/Classes.g.cs: 175 interfaces, 167 sealed
element classes, ClassMap (TypeMap + Factories). The namespace comes from
tools/naming.py.
"""

import json
import re
import sys

from naming import (CS_NAMESPACE, CS_PROJECT, MM, ROOT, as_meta, convenience_doc, convenience_generic_doc,
                    convenience_runtime, flat_ops, op_member, spelling, wrap)

CONVENIENCE = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))["members"]

OUT = CS_PROJECT

CS_KEYWORDS = set(
    "abstract as base bool break byte case catch char checked class const continue "
    "decimal default delegate do double else enum event explicit extern false finally "
    "fixed float for foreach goto if implicit in int interface internal is lock long "
    "namespace new null object operator out override params private protected public "
    "readonly ref return sbyte sealed short sizeof stackalloc static string struct "
    "switch this throw true try typeof uint ulong unchecked unsafe ushort using "
    "virtual void volatile while".split()
)

# hand-written names in Runtime.cs the generated type names must not shadow
RUNTIME_TYPES = {
    "El", "IElement", "IBackend", "PayloadBackend", "Model", "ClassMap",
    "UnresolvedRef", "SdkException", "NotComputedException",
    "NotImplementedInToolkitException", "GoneException", "ReadOnlyException",
}


def esc(name: str) -> str:
    return f"@{name}" if name in CS_KEYWORDS else name


def cs_sig(meta):
    """Table row -> (return type, typed converter of Model) for typed getters
    (0.5.0). Enum values are lowercase spec strings, typed string."""
    k, t = meta["kind"], meta["type"]
    if k == "element":
        base = "IElement" if t == "Element" else t
        single = f"{base}?", f"AsRef<{base}>"
    elif t == "Boolean":
        base, single = "bool", ("bool?", "AsBool")
    elif t == "Integer":
        base, single = "long", ("long?", "AsInt")
    elif t == "Real":
        base, single = "double", ("double?", "AsReal")
    else:
        base, single = "string", ("string?", "AsString")
    if meta["shape"] == "A":
        return f"IReadOnlyList<{base}>", f"AsList<{base}>"
    return single


def check_collisions():
    bad = []
    for cls, info in MM.items():
        if cls in RUNTIME_TYPES or f"{cls}El" in MM:
            bad.append(f"class name {cls} shadows a runtime type or impl name")
        for nm in list(info["props"]) + [o["name"] for o in info["ops"] if o["name"]]:
            if not nm[0].islower():
                bad.append(
                    f"spec name {cls}.{nm} does not start lowercase — "
                    f"breaks the PascalCase runtime-member rule"
                )
        for _, op in flat_ops(cls):
            if op_member(op["name"], "cs") in info["props"]:
                bad.append(f"operation {cls}.{op['name']} collides with a property")
    if bad:
        sys.exit("collision rule violated:\n  " + "\n  ".join(bad))


def ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def member_lines(name, inherited):
    """The default members of a metaclass: its properties, then its operations. A member
    already declared by an ancestor interface is redeclared with `new`."""
    props, ops = [], []
    for p, meta in sorted(MM[name]["props"].items()):
        hide = "new " if p in inherited else ""
        rtype, call = cs_sig(meta)
        props.append(f'    {hide}{rtype} {esc(p)} => Model.{call}(this.Model.Read(this, "{p}"));')
    for decl, op in flat_ops(name):
        oname = op["name"]
        member = op_member(oname, "cs")
        hide = "new " if member in inherited else ""
        params = ", ".join(
            f"{cs_sig(as_meta(tr))[0]} {esc(a)}"
            for a, tr in zip(op["params"], op["paramTypes"])
        )
        args = (f'new object?[] {{ {", ".join(esc(a) for a in op["params"])} }}' if op["params"]
                else "System.Array.Empty<object?>()")
        call = f'this.Model.Call(this, "{oname}", {args})'
        if op["returns"]:
            rtype, conv = cs_sig(as_meta(op["returns"]))
            body = f"Model.{conv}({call})"
        else:
            rtype, body = "object?", call
        ops.append(f"    {hide}{rtype} {esc(member)}({params}) => {body};")
    return props, ops


def find_const(name: str) -> str:
    """The Finds field of a convenience member: FindParts for findParts."""
    return spelling(name, "cs")


def xmldoc(m: dict, over_list: bool = False) -> list[str]:
    """A convenience member's doc comment (tools/naming.py, convenience_doc) as C# XML documentation;
    over a list, the extension method's `elements` too."""
    return xmldoc_text(convenience_doc(m, "cs", over_list), over_list)


def xmldoc_text(d: dict, over_list: bool = False, typeparam: tuple[str, str] | None = None) -> list[str]:
    """A doc comment in convenience_doc's form as C# XML documentation."""

    def x(text: str) -> str:
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

    params = ([("elements", "The elements to ask.")] if over_list else []) + d["params"]
    parts = [f"<summary>{x(d['summary'])}</summary>"]
    if typeparam:
        parts += [f'<typeparam name="{typeparam[0]}">{x(typeparam[1])}</typeparam>']
    parts += [f'<param name="{n}">{x(t)}</param>' for n, t in params]
    parts += [f"<returns>{x(d['returns'])}</returns>"]
    parts += [f'<exception cref="{e}">{x(t)}</exception>' for e, t in d["throws"]]
    return [line for p in parts for line in wrap(p, 100, "    /// ", "    /// ")]


def find_lines(name):
    """The default members of the convenience members declared on a metaclass, with optional
    parameters for the defaults."""
    out = []
    for m in CONVENIENCE:
        if m["on"] != name:
            continue

        def ptype(t):
            if t["kind"] == "element":
                return "IElement" if t["type"] == "Element" else t["type"]
            if t["kind"] == "enum":
                return t["type"]
            return {"Boolean": "bool", "String": "string"}[t["type"]]

        def param(p):
            d = p["default"]
            if d is None:
                return f"{ptype(p['type'])} {p['name']}"
            if p["type"]["kind"] == "enum":
                return f"{ptype(p['type'])} {p['name']} = {p['type']['type']}.{d[0].upper() + d[1:]}"
            return f"{ptype(p['type'])} {p['name']} = {'true' if d is True else 'false'}"

        r = m["returns"]
        _, result = convenience_runtime(m)
        if result == "map":
            rtype, conv = f"IReadOnlyDictionary<string, {r['type']}>", f"Model.AsMap<{r['type']}>"
        elif r["kind"] == "element":
            base = "IElement" if r["type"] == "Element" else r["type"]
            rtype, conv = (f"IReadOnlyList<{base}>", f"Model.AsList<{base}>") if r["many"] else (f"{base}?", f"Model.AsRef<{base}>")
        else:
            rtype, conv = {"bool": ("bool", "Model.AsFlag"), "int": ("Literal?", "(Literal?)"),
                           "unlimited": ("Literal?", "(Literal?)"), "real": ("double?", "Model.AsReal"),
                           "strings": ("IReadOnlyList<string>", "Model.AsList<string>"),
                           "literal": ("Literal", "(Literal)")}[result]
        params = ", ".join(param(p) for p in m["params"])
        args = "".join(f", {p['name']}" for p in m["params"])
        call = f"this.Model.FindMember(this, Finds.{find_const(m['name'])}{args})"
        out += xmldoc(m)
        if result == "literal" and not r["optional"]:
            call += "!"  # never null: a literal member answers or throws
        out.append(f"    {rtype} {spelling(m['name'], 'cs')}({params}) => {conv}({call});")
        if result == "literal" and not r["optional"]:
            # The generic shortcut: the value read as the type given (Literal.As).
            if m["params"]:
                raise SystemExit(f"{m['name']}: a literal member with parameters has no generic overload yet")
            out += xmldoc_text(convenience_generic_doc(m, "cs"),
                               typeparam=("T", "The type to read the value as: long, int, double, bool or "
                                               "string, or a case of Literal."))
            out.append(f"    T {spelling(m['name'], 'cs')}<T>() => {spelling(m['name'], 'cs')}().As<T>();")
    return out


def list_extensions() -> list[str]:
    """ElementLists: the convenience members over lists, as extension methods on IEnumerable of each
    member's context (covariance applies them to lists of its subtypes), for element and string results:
    list results concatenated in list order, absent single results dropped."""
    out = ["/// <summary>The convenience members over lists of elements: <c>xs.FindPorts()</c> is the elements'",
           "/// <c>FindPorts()</c> in list order, list results concatenated (duplicates kept), absent single",
           "/// results dropped. If the member refuses for one element, the call refuses. Members that answer a",
           "/// Boolean, a number or a literal stay on the elements.</summary>",
           "public static class ElementLists", "{"]
    for m in CONVENIENCE:
        _, result = convenience_runtime(m)
        if result not in ("elements", "element", "strings"):
            continue

        def ptype(t):
            if t["kind"] == "element":
                return "IElement" if t["type"] == "Element" else t["type"]
            if t["kind"] == "enum":
                return t["type"]
            return {"Boolean": "bool", "String": "string"}[t["type"]]

        def param(p):
            d = p["default"]
            if d is None:
                return f"{ptype(p['type'])} {p['name']}"
            if p["type"]["kind"] == "enum":
                return f"{ptype(p['type'])} {p['name']} = {p['type']['type']}.{d[0].upper() + d[1:]}"
            return f"{ptype(p['type'])} {p['name']} = {'true' if d is True else 'false'}"

        r = m["returns"]
        item = "string" if result == "strings" else ("IElement" if r["type"] == "Element" else r["type"])
        ctx = "IElement" if m["on"] == "Element" else m["on"]
        params = "".join(f", {param(p)}" for p in m["params"])
        args = "".join(f", {p['name']}" for p in m["params"])
        out += xmldoc(m, over_list=True)
        out.append(f"    public static IReadOnlyList<{item}> {spelling(m['name'], 'cs')}(this IEnumerable<{ctx}> elements{params}) =>"
                   f" Model.FindAll<{item}>(elements, Finds.{find_const(m['name'])}{args});")
    out += ["}", ""]
    return out


def finds_class():
    seen = {}
    for m in CONVENIENCE:
        seen.setdefault(m["name"], m)
    out = ["/// <summary>The convenience members' descriptors (convenience.json), one per member name.</summary>",
           "public static class Finds", "{"]
    for name, m in sorted(seen.items()):
        params, result = convenience_runtime(m)
        pnames = ", ".join(json.dumps(p[0]) for p in params)
        kinds = ", ".join(json.dumps(p[1]) for p in params)
        out.append(f'    public static readonly Find {find_const(name)} = new("{name}", "{spelling(name, "python")}", '
                   f'new string[] {{ {pnames} }}, new string[] {{ {kinds} }}, "{result}");')
    out += ["}", ""]
    return out, len(seen)


def declared_names(name):
    return set(MM[name]["props"]) | {op_member(o["name"], "cs") for _, o in flat_ops(name)}


def main():
    check_collisions()
    lines = [
        "// Generated — do not edit. Source: metamodel.json via tools/gen_classes_cs.py.",
        "// Interfaces with default members; runtime core in Runtime.cs.",
        "#nullable enable",
        "",
        f"namespace {CS_NAMESPACE};",
        "",
    ]
    # The spec root merges into the runtime IElement: this is its generated half.
    props, ops = member_lines("Element", set())
    lines.append("/// <summary>The members of the metaclass Element, which every element has.</summary>")
    lines.append("public partial interface IElement")
    lines.append("{")
    lines += props + ops + find_lines("Element")
    lines.append("}")
    lines.append("")
    n_props, n_ops = len(props), len(ops)
    for name in sorted(MM):
        if name == "Element":
            continue
        info = MM[name]
        bases = [b if b != "Element" else "IElement" for b in info["bases"]] or [
            "IElement"
        ]
        # names declared by ancestor interfaces, IElement included — these need `new`
        inherited = set()
        for a in ancestors(name, set()) | {"Element"}:
            inherited |= declared_names(a)
        kind = "abstract" if info["abstract"] else "concrete"
        layer = {"kerml": "KerML", "sysml": "SysML"}[info["language"]]
        lines.append(f"/// <summary>{kind} — {layer} 20250201.</summary>")
        lines.append(f"public interface {name} : {', '.join(bases)}")
        lines.append("{")
        props, ops = member_lines(name, inherited)
        lines += props + ops + find_lines(name)
        n_props += len(props)
        n_ops += len(ops)
        lines.append("}")
        lines.append("")

    finds, n_finds = finds_class()
    lines += finds
    lines += list_extensions()
    concrete = sorted(n for n in MM if not MM[n]["abstract"])
    for name in concrete:
        lines.append(
            f"internal sealed class {name}El : El, {name}\n"
            f"{{\n    internal {name}El(Model model, string id) : "
            f"base(model, id) {{ }}\n}}"
        )
        lines.append("")

    lines.append("/// <summary>Generated maps: metaclass name to interface type")
    lines.append("/// (all classes) and to element factory (concrete only).</summary>")
    lines.append("public static class ClassMap")
    lines.append("{")
    # System.Type must be qualified: bare `Type` here is the spec interface
    lines.append("    public static readonly IReadOnlyDictionary<string, System.Type> TypeMap =")
    lines.append("        new Dictionary<string, System.Type>")
    lines.append("        {")
    lines.append('            ["Element"] = typeof(IElement),')
    for name in sorted(n for n in MM if n != "Element"):
        lines.append(f'            ["{name}"] = typeof({name}),')
    lines.append("        };")
    lines.append("")
    lines.append("    public static readonly")
    lines.append("        IReadOnlyDictionary<string, Func<Model, string, IElement>> Factories =")
    lines.append("        new Dictionary<string, Func<Model, string, IElement>>")
    lines.append("        {")
    for name in concrete:
        lines.append(f'            ["{name}"] = (m, id) => new {name}El(m, id),')
    lines.append("        };")
    lines.append("}")
    lines.append("")

    OUT.mkdir(parents=True, exist_ok=True)
    out = OUT / "Classes.g.cs"
    out.write_text("\n".join(lines), encoding="utf-8", newline="\n")
    print(
        f"cs backend: {len(MM) - 1} interfaces, {n_props} property members, "
        f"{n_ops} operation members, {n_finds} convenience members, {len(concrete)} element classes"
    )


if __name__ == "__main__":
    main()
