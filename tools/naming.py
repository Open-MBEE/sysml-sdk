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
