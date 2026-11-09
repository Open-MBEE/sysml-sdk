"""C++ backend of the SDK generator.

Hierarchy strategy: every metaclass is a struct that *virtually*
inherits its bases with the runtime Element as the single virtual root;
structs carry methods only, never data, so any element object is
{core, handle} regardless of static type and as<T>() re-views the same
state after a table check (no C++ RTTI).

Operations are answered by the model's backend (Element::call_): the
SysML Toolkit answers the ones it can and refuses the others; a payload
refuses them all.

Member naming (tools/naming.py): every property is read through a getter,
`get` + the specification name (`getQualifiedName()`); operations keep their
specification names. Every struct declares its whole catalogue, operations
included, so name lookup never reaches two virtual bases.

Collision rule (C++): runtime members are multi-word snake_case; spec
names are lowerCamelCase without underscores, and so are the getters.
C++ keywords among operation names would get a trailing underscore; the
guard hard-codes the allowed rename set (empty since properties became
getters: `operator` is a property, read as `getOperator()`) so any new
collision fails generation. Single-word runtime members that carry no
underscore (`as`) are reserved outright.

Emits cpp/include/sysml/classes.g.hpp: 174 structs, the spec
hierarchy table, and the out-of-line Element::is_kind definition.
"""

import json
import sys

from naming import MM, ROOT, as_meta, convenience_doc, convenience_runtime, flat_ops, getter, op_member, spelling, wrap

CONVENIENCE = json.loads((ROOT / "convenience.json").read_text(encoding="utf-8"))["members"]

OUT = ROOT / "cpp" / "include" / "sysml"

CPP_KEYWORDS = set(
    "alignas alignof and and_eq asm auto bitand bitor bool break case catch "
    "char char8_t char16_t char32_t class compl concept const consteval "
    "constexpr constinit const_cast continue co_await co_return co_yield "
    "decltype default delete do double dynamic_cast else enum explicit export "
    "extern false float for friend goto if inline int long mutable namespace "
    "new noexcept not not_eq nullptr operator or or_eq private protected "
    "public register reinterpret_cast requires return short signed sizeof "
    "static static_assert static_cast struct switch template this thread_local "
    "throw true try typedef typeid typename union unsigned using virtual void "
    "volatile wchar_t while xor xor_eq".split()
)
ALLOWED_RENAMES: set[str] = set()  # operation names emitted with a trailing underscore
RESERVED_SINGLE = {"as"}  # runtime members without an underscore


def esc(name: str) -> str:
    return f"{name}_" if name in CPP_KEYWORDS else name


def cpp_sig(cls, prop, meta):
    """Table row -> (return type, converter call) for typed getters
    (0.5.0). Enum values are lowercase spec strings, typed string."""
    k, t = meta["kind"], meta["type"]
    if meta["shape"] == "A":
        if k == "element":
            return f"std::vector<{t}>", f"read_list_<{t}>"
        if t != "String":
            sys.exit(f"{cls}.{prop}: unexpected primitive array of {t} — "
                     f"add a converter before generating")
        return "std::vector<std::string>", "read_string_list_"
    if k == "element":
        return f"std::optional<{t}>", f"read_ref_<{t}>"
    if t == "Boolean":
        return "std::optional<bool>", "read_bool_"
    if t == "Integer":
        return "std::optional<std::int64_t>", "read_int_"
    if t == "Real":
        return "std::optional<double>", "read_real_"
    return "std::optional<std::string>", "read_string_"


def ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def pruned_bases(name):
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


def check_collisions():
    bad = []
    for cls, info in MM.items():
        for nm in list(info["props"]) + [o["name"] for o in info["ops"] if o["name"]]:
            if "_" in nm:
                bad.append(f"spec name {cls}.{nm} contains an underscore")
        ops = {op_member(op["name"], "cpp") for _, op in flat_ops(cls)}
        getters = {getter(p) for p in info["props"]}
        for nm in ops:
            if nm in CPP_KEYWORDS and nm not in ALLOWED_RENAMES:
                bad.append(
                    f"operation {cls}.{nm} is a C++ keyword outside the allowed "
                    f"rename set — decide its rename consciously"
                )
        for nm in ops | getters:
            if nm in RESERVED_SINGLE:
                bad.append(f"member {cls}.{nm} collides with runtime member")
        for nm in ops & getters:
            bad.append(f"operation {cls}.{nm} collides with a property getter")
    if bad:
        sys.exit("collision rule violated:\n  " + "\n  ".join(bad))


def members(name):
    """A metaclass's getters and operations: (declarations for its struct body, inline
    definitions to follow once every struct is complete). Typed getters reference later-defined
    structs, so a struct only declares them (std::optional/vector member instantiation needs the
    complete type)."""
    decls, defs = [], []
    for p, meta in sorted(MM[name]["props"].items()):
        rtype, conv = cpp_sig(name, p, meta)
        decls.append(f"    {rtype} {getter(p)}() const;")
        defs.append(f'inline {rtype} {name}::{getter(p)}() const {{ return {conv}("{p}"); }}')
    for decl, op in flat_ops(name):
        oname = op["name"]
        member = esc(op_member(oname, "cpp"))
        params = ", ".join(
            f"const {cpp_sig(name, oname, as_meta(tr))[0]}& {esc(a)}"
            for a, tr in zip(op["params"], op["paramTypes"])
        )
        call = (f'call_("{oname}", {{{", ".join(f"detail::arg({esc(a)})" for a in op["params"])}}})'
                if op["params"] else f'call_("{oname}", {{}})')
        if op["returns"]:
            rtype, conv = cpp_sig(name, oname, as_meta(op["returns"]))
            body = f"return {conv.replace('read_', 'as_', 1)}({call});"
        else:
            rtype, body = "Value", f"return {call};"
        decls.append(f"    {rtype} {member}({params}) const;")
        defs.append(f"inline {rtype} {name}::{member}({params}) const {{ {body} }}")
    return decls, defs


def doxygen(m: dict, over_list: bool = False) -> list[str]:
    """A convenience member's doc comment (tools/naming.py, convenience_doc) as Doxygen."""
    d = convenience_doc(m, "cpp", over_list)
    out = wrap(d["summary"], 100, "    /// ", "    /// ")
    tags = [f"@param {n} {t}" for n, t in d["params"]] + [f"@return {d['returns']}"]
    tags += [f"@throws {e} {t}" for e, t in d["throws"]]
    for t in tags:
        out += wrap(t, 100, "    /// ", "    ///     ")
    return out


def finds(name):
    """The convenience members declared on a metaclass: (declarations with default arguments and their
    doc comments, inline definitions)."""
    decls, defs = [], []
    for m in CONVENIENCE:
        if m["on"] != name:
            continue

        def ptype(t):
            if t["kind"] == "element":
                return f"const {'Element' if t['type'] == 'Element' else t['type']}&"
            if t["kind"] == "enum":
                return t["type"]
            return {"Boolean": "bool", "String": "const std::string&"}[t["type"]]

        def default(p):
            d = p["default"]
            if d is None:
                return ""
            if p["type"]["kind"] == "enum":
                return f" = {p['type']['type']}::{d[0].upper() + d[1:]}"
            return " = true" if d is True else " = false"

        params, result = convenience_runtime(m)
        r = m["returns"]
        if result == "map":
            rtype, conv = f"NameMap<{r['type']}>", f"as_map_<{r['type']}>"
        elif r["kind"] == "element":
            base = "Element" if r["type"] == "Element" else r["type"]
            rtype, conv = (f"ElementList<{base}>", f"as_list_<{base}>") if r["many"] else (f"std::optional<{base}>", f"as_ref_<{base}>")
        else:
            rtype, conv = {"bool": ("bool", "as_bool_"), "int": ("std::optional<Literal>", "as_bound_"),
                           "unlimited": ("std::optional<Literal>", "as_bound_"), "real": ("std::optional<double>", "as_real_"),
                           "strings": ("std::vector<std::string>", "as_string_list_"), "literal": ("Literal", "as_literal_")}[result]
        member = spelling(m["name"], "cpp")
        decl_params = ", ".join(f"{ptype(p['type'])} {p['name']}{default(p)}" for p in m["params"])
        def_params = ", ".join(f"{ptype(p['type'])} {p['name']}" for p in m["params"])
        pnames = ", ".join(json.dumps(p[0]) for p in params)
        kinds = ", ".join(json.dumps(p[1]) for p in params)
        args = ", ".join(f"detail::arg({p['name']})" for p in m["params"])
        descriptor = (f'static const Find f{{"{m["name"]}", "{spelling(m["name"], "python")}", {{{pnames}}}, '
                      f'{{{kinds}}}, "{result}"}};')
        if result == "literal":
            body = f"return as_literal_(find_raw_(f, {{{args}}}));"
        elif result in ("int", "unlimited"):
            body = f"return as_bound_(find_raw_(f, {{{args}}}), {'true' if result == 'unlimited' else 'false'});"
        elif result == "bool":
            body = f"return as_bool_(find_(f, {{{args}}})).value_or(false);"
        else:
            body = f"return {conv}(find_(f, {{{args}}}));"
        decls += doxygen(m)
        decls.append(f"    {rtype} {member}({decl_params}) const;")
        defs.append(f"inline {rtype} {name}::{member}({def_params}) const {{\n    {descriptor}\n    {body}\n}}")
    return decls, defs


def element_list() -> list[str]:
    """The ElementList template: every member a list carries (element and string results) as a member
    template enabled when the elements' class derives from the member's context, so it can be called only
    on a list of a conforming type; a member with several contexts is a template per context."""
    out = [
        "/// A list of elements carrying the convenience members of its elements: `xs.find_ports()` is the",
        "/// elements' `find_ports()` in list order, list results concatenated (duplicates kept), absent single",
        "/// results dropped; if the member refuses for one element, the call refuses. Members that answer a",
        "/// Boolean, a number or a literal stay on the elements. A std::vector: any vector of elements",
        "/// converts to one.",
        "template <class T>",
        "class ElementList : public std::vector<T> {",
        "public:",
        "    ElementList() = default;",
        "    ElementList(std::vector<T> elements) : std::vector<T>(std::move(elements)) {}",
        "",
    ]
    for m in CONVENIENCE:
        params, result = convenience_runtime(m)
        if result not in ("elements", "element", "strings"):
            continue

        def ptype(t):
            if t["kind"] == "element":
                return f"const {'Element' if t['type'] == 'Element' else t['type']}&"
            if t["kind"] == "enum":
                return t["type"]
            return {"Boolean": "bool", "String": "const std::string&"}[t["type"]]

        def default(p):
            d = p["default"]
            if d is None:
                return ""
            if p["type"]["kind"] == "enum":
                return f" = {p['type']['type']}::{d[0].upper() + d[1:]}"
            return " = true" if d is True else " = false"

        r = m["returns"]
        member = spelling(m["name"], "cpp")
        decl_params = ", ".join(f"{ptype(p['type'])} {p['name']}{default(p)}" for p in m["params"])
        pnames = ", ".join(json.dumps(p[0]) for p in params)
        kinds = ", ".join(json.dumps(p[1]) for p in params)
        args = ", ".join(f"detail::arg({p['name']})" for p in m["params"])
        descriptor = (f'static const Find f{{"{m["name"]}", "{spelling(m["name"], "python")}", {{{pnames}}}, '
                      f'{{{kinds}}}, "{result}"}};')
        if result == "strings":
            rtype, body = "std::vector<std::string>", f"return Element::find_all_strings_(*this, f, {{{args}}});"
        else:
            item = "Element" if r["type"] == "Element" else r["type"]
            rtype, body = f"ElementList<{item}>", f"return Element::find_all_<{item}>(*this, f, {{{args}}});"
        ctx = m["on"]
        out += doxygen(m, over_list=True)
        out.append(f"    template <class U = T, std::enable_if_t<std::is_base_of_v<{ctx}, U>, int> = 0>")
        out.append(f"    {rtype} {member}({decl_params}) const {{")
        out.append(f"        {descriptor}")
        out.append(f"        {body}")
        out.append("    }")
    out += ["};", ""]
    return out


def write_fragment(name, comment, body):
    header = ["// Generated — do not edit. Source: metamodel.json via tools/gen_classes_cpp.py."]
    header += [f"// {line}" for line in comment]
    (OUT / name).write_text("\n".join(header + body) + "\n", encoding="utf-8", newline="\n")


def main():
    check_collisions()
    OUT.mkdir(parents=True, exist_ok=True)
    order = [n for n in topo_order() if n != "Element"]
    write_fragment("forward.g.hpp", [
        "Included by sdk.hpp inside namespace sysml, ahead of Element: the metaclass structs,",
        "declared, as Element's own members and the structs' getters name one another.",
    ], [f"struct {name};" for name in sorted(order)])
    # The spec root merges into the runtime Element: its members are declared inside that class
    # by a fragment, and defined below with everything else.
    element_decls, defs = members("Element")
    element_finds, element_find_defs = finds("Element")
    element_decls += element_finds
    defs += element_find_defs
    write_fragment("element_members.g.hpp", [
        "Included by sdk.hpp inside class Element: the members of the metaclass Element, which",
        "every element has. Defined in classes.g.hpp.",
    ], [line.strip() for line in element_decls])
    n_props, n_ops = len(MM["Element"]["props"]), len(flat_ops("Element"))

    lines = [
        "// Generated — do not edit. Source: metamodel.json via tools/gen_classes_cpp.py.",
        "// Virtual-inheritance metaclass hierarchy over the SDK runtime",
        "// (sdk.hpp): every struct is a view of the same element.",
        "#pragma once",
        "",
        '#include "sdk.hpp"',
        "",
        "namespace sysml {",
        "",
    ]
    for name in order:
        info = MM[name]
        bases = pruned_bases(name) or ["Element"]
        base_list = ", ".join(f"public virtual {b}" for b in bases)
        kind = "abstract" if info["abstract"] else "concrete"
        layer = {"kerml": "KerML", "sysml": "SysML"}[info["language"]]
        lines.append(f"/// {kind} — {layer} 20250201.")
        lines.append(f"struct {name} : {base_list} {{")
        lines.append(
            f'    static constexpr std::string_view kind_name = "{name}";'
        )
        # copy-only, like Element: a defaulted move-assign would move the
        # shared virtual base once per inheritance path (gcc
        # -Wvirtual-move-assign); copies are idempotent
        lines.append(f"    {name}() = default;")
        lines.append(f"    {name}(const {name}&) = default;")
        lines.append(f"    {name}& operator=(const {name}&) = default;")
        decls, struct_defs = members(name)
        find_decls, find_defs = finds(name)
        lines += decls + find_decls
        defs += struct_defs + find_defs
        n_props += len(info["props"])
        n_ops += len(flat_ops(name))
        lines.append("};")
        lines.append("")
    lines.extend(element_list())
    lines.append("// --- typed getter definitions (all structs complete) ---")
    lines.extend(defs)
    lines.append("")

    lines.append("namespace detail {")
    lines.append("")
    lines.append("/// Metaclass name -> all spec ancestors (Element included).")
    lines.append("inline const std::unordered_map<std::string_view,")
    lines.append("    std::vector<std::string_view>>& spec_hierarchy() {")
    lines.append("    static const std::unordered_map<std::string_view,")
    lines.append("        std::vector<std::string_view>> h = {")
    for name in sorted(MM):
        if name == "Element":
            continue
        anc = ", ".join(f'"{a}"' for a in sorted(ancestors(name, set())))
        lines.append(f'        {{"{name}", {{{anc}}}}},')
    lines.append("    };")
    lines.append("    return h;")
    lines.append("}")
    lines.append("")
    lines.append("}  // namespace detail")
    lines.append("")
    lines.append("inline bool Element::is_kind(std::string_view kind) const {")
    lines.append('    if (kind == "Element")')
    lines.append("        return true;")
    lines.append("    const std::string mc = metaclass_name();")
    lines.append("    if (mc == kind)")
    lines.append("        return true;")
    lines.append("    const auto& h = detail::spec_hierarchy();")
    lines.append("    auto it = h.find(std::string_view(mc));")
    lines.append("    if (it == h.end())")
    lines.append("        return false;")
    lines.append("    for (std::string_view a : it->second)")
    lines.append("        if (a == kind)")
    lines.append("            return true;")
    lines.append("    return false;")
    lines.append("}")
    lines.append("")
    lines.append("}  // namespace sysml")
    lines.append("")

    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / "classes.g.hpp").write_text("\n".join(lines), encoding="utf-8",
                                      newline="\n")
    print(
        f"cpp backend: {len(MM) - 1} structs, {n_props} property members, "
        f"{n_ops} operation members"
    )


if __name__ == "__main__":
    main()
