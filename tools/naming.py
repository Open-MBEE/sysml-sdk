"""Every name the generators emit that is a convention or a decision, in one place.

Package names. The Java package, the C# namespace and project folders, the npm package, the Python
distribution and module, and the product name in file names are `NAMES` below and nowhere else in
the generators; the hand-written files spell them out and `tools/rename.py` rewrites them. C++
names (namespace `sysml`) are not among them.

Member naming. Properties and operations are separate members, and every language shows which
one a call site uses:

  - Java and C++ read every property through a getter, `get` + the specification name with its
    first letter capitalized (`getQualifiedName()`, Booleans too: `getIsAbstract()`). Operations
    keep their specification names (`effectiveName()`), so the two can never collide.
  - Python, C# and TypeScript read properties with property syntax under the specification name
    (`el.qualifiedName`) and call operations by their specification name, except an operation
    whose name is also a property of a class that has both. Only `instantiatedType` is such a
    name; the operation becomes `instantiatedType_op()` in Python and `instantiatedTypeOp()` in
    C# and TypeScript, on every class.
  - TypeScript also suffixes an operation whose name the element Proxy must answer itself for
    JavaScript's own protocols (`valueOf`, `toString`, ...). Only `valueOf` is such a name: it is
    `valueOfOp()` on `MultiplicityRange`.

Operation catalogue. The metamodel lists each operation on the class that declares or redefines
it, while properties are listed on every class that has them. `flat_ops` gives operations the
same shape: every operation a class has, from its most specific declaration.
"""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

# ---------------------------------------------------------------- the names users type

# Every name a user types to install or import the SDK. `tools/rename.py` changes them across the
# repository and rewrites this table; the generators and packaging tools read them from here.
NAMES = {
    "java": "org.openmbee.sysml",  # Java package; also the jar's module name
    "cs": "OpenMBEE.SysML",  # C# namespace, NuGet id, project folders
    "npm": "sysml",  # npm package
    "python": "sysml",  # Python distribution; the import name is this with "_" for "-"
    "product": "sysml-sdk",  # file names of the jar and the C++ archive, the skills' names
}


def spellings(names: dict[str, str] = NAMES) -> dict[str, str]:
    """Every derived spelling of the names."""
    npm = names["npm"]
    return {
        "java_package": names["java"],
        "java_dir": names["java"].replace(".", "/"),  # the package as a source path
        "cs_namespace": names["cs"],
        "npm_package": npm,
        # `npm pack` names a scoped package's tarball <scope>-<name>-<version>.tgz
        "npm_tarball_stem": npm[1:].replace("/", "-") if npm.startswith("@") else npm,
        "python_dist": names["python"],
        "python_module": names["python"].replace("-", "_"),
        "product": names["product"],
    }


S = spellings()
JAVA_PACKAGE = S["java_package"]
JAVA_SRC = ROOT / "java" / "src" / Path(S["java_dir"])
CS_NAMESPACE = S["cs_namespace"]
CS_PROJECT = ROOT / "csharp" / CS_NAMESPACE
CS_CHECKS_PROJECT = ROOT / "csharp" / f"{CS_NAMESPACE}.Checks"
NPM_PACKAGE = S["npm_package"]
PYTHON_MODULE = S["python_module"]

# ---------------------------------------------------------------- the metamodel

MM = json.loads((ROOT / "metamodel.json").read_text(encoding="utf-8"))["classes"]


def ancestors(name: str, acc: set[str] | None = None) -> set[str]:
    acc = set() if acc is None else acc
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            ancestors(b, acc)
    return acc


def flat_ops(name: str) -> list[tuple[str, dict]]:
    """(declaring class, operation) for every operation `name` has, sorted by operation name.

    The declaration is the most specific one: the class's own, else the one no other declaring
    ancestor redefines. The metamodel has no class that inherits two unrelated redefinitions of
    one operation; generation fails if one appears.
    """
    anc = ancestors(name)
    by_name: dict[str, list[str]] = {}
    for c in [name, *sorted(anc)]:
        for op in MM[c]["ops"]:
            by_name.setdefault(op["name"], []).append(c)
    out = []
    for op_name, decls in sorted(by_name.items()):
        most = [c for c in decls if not any(c in ancestors(d) for d in decls if d != c)]
        if len(most) != 1:
            raise SystemExit(f"{name}.{op_name}: inherited from unrelated redefinitions {most}")
        decl = most[0]
        op = next(o for o in MM[decl]["ops"] if o["name"] == op_name)
        out.append((decl, op))
    return out


# ---------------------------------------------------------------- member naming

# Operation names that are also a property name on some class that has both.
COLLIDING_OPS = sorted(
    {op["name"] for n in MM for _, op in flat_ops(n) if op["name"] in MM[n]["props"]}
)

# Keys the TypeScript element Proxy answers itself; keep in sync with SPECIAL in ts/runtime.mjs.
TS_RUNTIME_NAMES = ["constructor", "then", "toJSON", "toString", "valueOf"]


def getter(prop: str) -> str:
    """Java and C++ property reader: `get` + the specification name, first letter capitalized."""
    return "get" + prop[0].upper() + prop[1:]


def op_member(op_name: str, lang: str) -> str:
    """The member name of a specification operation in `lang` (python, cs, ts, java, cpp)."""
    if lang in ("java", "cpp"):
        return op_name
    if op_name in COLLIDING_OPS or (lang == "ts" and op_name in TS_RUNTIME_NAMES):
        return op_name + ("_op" if lang == "python" else "Op")
    return op_name


def as_meta(typed: dict) -> dict:
    """An operation's parameter or return type in the shape of a property row, so each language
    maps both with one function. UML's UnlimitedNatural is typed as an integer; its `*` has no
    integer value, which matters only once the SysML Toolkit answers the operations that take
    one."""
    t = "Integer" if typed["type"] == "UnlimitedNatural" else typed["type"]
    return {"kind": typed["kind"], "type": t, "shape": "A" if typed["many"] else "R"}


def op_decl(row: dict) -> dict:
    """The metamodel's declaration of a naming-table operation row."""
    for op in MM[row["declaring_class"]]["ops"]:
        if op["name"] == row["member"]:
            return op
    raise SystemExit(f"{row['declaring_class']}::{row['member']}: no such operation in metamodel.json")


def abi_param_type(typed: dict) -> str:
    """The C type of an operation parameter of the binding library, from its metamodel type: an
    element list is Handles, one element a Handle, a single Boolean a bool; anything else (text,
    enumeration literals, numbers) is a Str."""
    if typed["kind"] == "element":
        return "Handles" if typed["many"] else "Handle"
    if typed["type"] == "Boolean" and not typed["many"]:
        return "bool"
    return "Str"


def abi_params(row: dict) -> list[str]:
    """The C types of a naming-table row's parameters, in order (none for a property)."""
    if row["kind"] != "operation":
        return []
    return [abi_param_type(t) for t in op_decl(row)["paramTypes"]]


def op_key(op_name: str) -> str:
    """Dispatch-table key of an operation. Properties are keyed by their plain name; operations
    carry `()` so that a property and an operation of the same name both keep their row."""
    return op_name + "()"


# ---------------------------------------------------------------- convenience members

def spelling(name: str, lang: str) -> str:
    """A convenience member's name in `lang`: snake_case in Python and C++, camelCase in Java and
    TypeScript, PascalCase in C#."""
    import re

    if lang in ("python", "cpp"):
        return re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()
    if lang == "cs":
        return name[0].upper() + name[1:]
    return name


def convenience_symbol(name: str) -> str:
    """The binding library's function of a convenience member."""
    return "sysmlv2_conv_" + spelling(name, "python")


def convenience_abi(member: dict) -> tuple[list[str], str]:
    """(parameter kinds, result code) of a convenience member at the binding library. Parameters:
    "enum" (a Scope or VisibilityFilter, as the literal's index, uint32_t), "Boolean", "String",
    "element" (a Handle). Results, as `result_code`'s letters, plus L for a literal (tagged JSON in a
    Str), M for a name-keyed map (JSON pairs of a name and a handle in a Str): H, R, B, I (an
    UnlimitedNatural too, `*` being INT64_MAX), F, T, L, M."""
    kinds = []
    for p in member["params"]:
        t = p["type"]
        if t["kind"] in ("enum", "element"):
            kinds.append(t["kind"])
        elif t["type"] in ("Boolean", "String") and not t["many"]:
            kinds.append(t["type"])
        else:
            raise SystemExit(f"{member['name']}: no binding for parameter {p['name']} of type {t['type']}")
    r = member["returns"]
    if r["kind"] == "map":
        code = "M"
    elif r["kind"] == "element":
        code = "H" if r["many"] else "R"
    elif r["kind"] == "literal":
        code = "L"
    elif r["type"] == "String" and r["many"]:
        code = "T"
    elif not r["many"] and r["type"] in ("Boolean", "Integer", "UnlimitedNatural", "Real"):
        code = {"Boolean": "B", "Integer": "I", "UnlimitedNatural": "I", "Real": "F"}[r["type"]]
    else:
        raise SystemExit(f"{member['name']}: no binding for a result of type {r['type']}")
    return kinds, code


RUNTIME_PARAM_KIND = {"Scope": "scope", "VisibilityFilter": "visibility", "Boolean": "bool", "String": "str"}


def convenience_runtime(member: dict) -> tuple[list, str]:
    """A convenience member as every language's runtime describes it: its parameters as
    [name, kind, default] (kind scope, visibility, bool, str or element; a default of None means
    required) and its result: elements, element, bool, int, unlimited (an UnlimitedNatural, `*`
    unbounded), real, strings, literal or map (names to elements, in order)."""
    params = [[p["name"], "element" if p["type"]["kind"] == "element" else RUNTIME_PARAM_KIND[p["type"]["type"]],
               p["default"]] for p in member["params"]]
    r = member["returns"]
    if r["kind"] == "map":
        result = "map"
    elif r["kind"] == "element":
        result = "elements" if r["many"] else "element"
    elif r["kind"] == "literal":
        result = "literal"
    elif r["type"] == "String" and r["many"]:
        result = "strings"
    else:
        result = {"Boolean": "bool", "Integer": "int", "UnlimitedNatural": "unlimited", "Real": "real"}[r["type"]]
    return params, result


# The languages whose literal answers (findLiteral, and the bounds' numbers) are a Literal value type
# rather than the language's own values: an integer, a real, a Boolean, a string, or unbounded.
LITERAL_TYPED = ("java", "cs", "cpp")

# How a convenience member's doc comment spells, per language, an absent answer, the unbounded `*`, a
# literal's value, the Booleans and the two refusals (the contract's, and the backend's of which it is one).
DOC_SPELLING = {
    "python": {"none": "None", "inf": "math.inf", "true": "True", "false": "False",
               "literal": "an int, float, bool or str", "refused": "Refused", "not_answered": "NotAnswered"},
    "java": {"none": "null", "inf": "Long.MAX_VALUE", "true": "true", "false": "false",
             "literal": "a Long, Double, Boolean or String", "refused": "RefusedException",
             "not_answered": "NotAnsweredException"},
    "cs": {"none": "null", "inf": "long.MaxValue", "true": "true", "false": "false",
           "literal": "a long, double, bool or string", "refused": "RefusedException",
           "not_answered": "NotAnsweredException"},
    "ts": {"none": "null", "inf": "Infinity", "true": "true", "false": "false",
           "literal": "a number, boolean or string", "refused": "Refused", "not_answered": "NotAnswered"},
    "cpp": {"none": "std::nullopt", "inf": "INT64_MAX", "true": "true", "false": "false",
            "literal": "a Literal: an integer, a real, a Boolean or a string", "refused": "refused",
            "not_answered": "not_answered"},
}


def convenience_default(p: dict, lang: str) -> str:
    """A parameter's default as `lang` writes it (Scope.OWNED, Scope::Owned, "owned", false)."""
    d = p["default"]
    if p["type"]["kind"] != "enum":
        return DOC_SPELLING[lang]["true" if d is True else "false"]
    enum = p["type"]["type"]
    if lang in ("python", "java"):
        return f"{enum}.{d.upper()}"
    if lang == "ts":
        return f'"{d}"'
    return f"{enum}{'::' if lang == 'cpp' else '.'}{d[0].upper() + d[1:]}"


def convenience_doc(member: dict, lang: str, over_list: bool = False, params: list | None = None) -> dict:
    """A convenience member's doc comment as text, for each generator to set in its language's form:
    {"summary", "params": [(name, text)], "returns", "throws": [(exception, text)]}. The summary is the
    contract's doc; each parameter's text is its doc and default; the answer's text follows its shape;
    the contract's refusal is listed where the definition can give no answer (`refusable`), the backend's
    always. `over_list` gives the doc of the member over a list of elements; `params` the parameters an
    overload takes, where the others are left at their defaults."""
    s = DOC_SPELLING[lang]
    summary = member["doc"]
    if over_list:
        summary += " Over the list's elements, in order: list answers concatenated, absent single answers dropped."
    taken = member["params"] if params is None else params
    left = [p for p in member["params"] if p not in taken]
    if left:
        names = [p["name"] for p in left]
        values = [convenience_default(p, lang) for p in left]
        if len(left) == 1:
            summary += f" Leaves {names[0]} at its default, {values[0]}."
        else:
            summary += (f" Leaves {', '.join(names[:-1])} and {names[-1]} at their defaults: "
                        f"{', '.join(values[:-1])} and {values[-1]}.")
    texts = []
    for p in taken:
        text = p["doc"]
        # Java has no default arguments: its shorter overloads say which defaults they leave.
        if p["default"] is not None and lang != "java":
            text += f" Default: {convenience_default(p, lang)}."
        texts.append((p["name"], text))
    _, result = convenience_runtime(member)
    if over_list:
        returns = "The texts, in order." if result == "strings" else "The elements, in order."
    elif lang in LITERAL_TYPED and result in ("int", "unlimited", "literal"):
        returns = {
            "int": f"The number as a Literal, an integer; {s['none']} where there is none.",
            "unlimited": f"The number as a Literal, an integer or unbounded (*); {s['none']} where there is none.",
            "literal": "The value as a Literal: an integer, a real, a Boolean, a string, or unbounded (*).",
        }[result]
    else:
        returns = {
            "elements": "The elements, in order; an empty list where there are none.",
            "element": f"The element, or {s['none']} where there is none.",
            "bool": f"{s['true']} or {s['false']}.",
            "int": f"The number, or {s['none']} where there is none.",
            "real": f"The number, or {s['none']} where there is none.",
            "unlimited": f"The number ({s['inf']} for *, unbounded), or {s['none']} where there is none.",
            "strings": "The texts, in order; an empty list where there are none.",
            "literal": f"The value: {s['literal']} ({s['inf']} for *, unbounded).",
            "map": "The names and the element each names, in membership order; an empty map where there are none.",
        }[result]
    throws = []
    if member["refusable"]:
        throws.append((s["refused"], "Where the definition gives no answer for this element, as described above."))
    throws.append((s["not_answered"], "Where the backend cannot answer a property or operation this member reads; "
                   "the message names it." + (" If the member refuses for one element, the call refuses." if over_list else "")))
    return {"summary": summary, "params": texts, "returns": returns, "throws": throws}


def convenience_generic_doc(member: dict, lang: str) -> dict:
    """The doc comment of a literal member's generic overload (Java's `<T> T findLiteral(Class<T> type)`,
    C#'s `T FindLiteral<T>()`), in the form of convenience_doc."""
    d = convenience_doc(member, lang)
    if lang == "java":
        kinds = "Long, Integer, Double, Boolean or String, or a case of Literal"
        d["params"] = [("type", f"The class to read the value as: {kinds}.")]
        wrong = ("java.lang.IllegalStateException", "Where the literal is of another kind (an integer read as a String; "
                 "unbounded read as a number).")
    else:
        kinds = "long, int, double, bool or string, or a case of Literal"
        d["params"] = []
        wrong = ("InvalidOperationException", "Where the literal is of another kind (an integer read as a string; "
                 "unbounded read as a number).")
    d["summary"] += f" Read as the type given: {kinds}."
    d["returns"] = "The value as the type given."
    d["throws"] = d["throws"] + [wrong]
    return d


def wrap(text: str, width: int, first: str, rest: str) -> list[str]:
    """`text` as lines of at most `width` columns, the first prefixed `first`, the others `rest`. A word
    that starts with @ or * never begins a line: doc comment tools would read a tag (`@M`), or drop the
    asterisk as the comment's own."""
    import re
    import textwrap

    glued = re.sub(r" (?=[@*])", "\x01", text)
    lines = textwrap.wrap(glued, width=width, initial_indent=first, subsequent_indent=rest,
                          break_long_words=False, break_on_hyphens=False)
    return [line.replace("\x01", " ") for line in lines] or [first.rstrip()]


def convenience_digest(table: dict) -> str:
    """SHA-256 over the convenience members' binding signatures, one line per member name."""
    import hashlib

    seen = {}
    for m in table["members"]:
        seen.setdefault(m["name"], m)
    lines = []
    for name in sorted(seen):
        kinds, code = convenience_abi(seen[name])
        lines.append(f"{convenience_symbol(name)}({','.join(kinds)})->{code}")
    return hashlib.sha256("\n".join(lines).encode("utf-8")).hexdigest()


def result_code(row: dict) -> str:
    """How a binding unpacks the result of a naming-table row's function, one letter per C result
    type of tools/gen_abi.py: H = handle array (Handles), R = one handle (Ref), S = a string (Str),
    T = a list of strings, which the library returns as JSON text in a Str, B = Bool, I = Int,
    F = Real. Every language's SysML Toolkit table is generated with this function."""
    if row["kind"] == "operation":
        returns = op_decl(row)["returns"]
        if returns is None:
            return "S"
        m = as_meta(returns)
        kind, shape, ty = m["kind"], m["shape"], m["type"]
    else:
        kind, shape, ty = row["result_kind"], row["result_shape"], row["result_type"]
    if kind == "element":
        return "H" if shape == "A" else "R"
    if shape == "A":
        return "T"
    return {"Boolean": "B", "Integer": "I", "Real": "F", "Rational": "F"}.get(ty, "S")
