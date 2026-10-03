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

import sys

from naming import MM, ROOT, as_meta, flat_ops, getter, op_member

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
        lines += decls
        defs += struct_defs
        n_props += len(info["props"])
        n_ops += len(flat_ops(name))
        lines.append("};")
        lines.append("")
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
