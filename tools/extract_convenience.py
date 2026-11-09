"""Write convenience.json, the convenience contract as data, from convenience.toml and metamodel.json.

The source (`convenience.toml`) gives the shared arguments, the helpers, the two families as templates
with their kind tables, and the individual members. This tool expands the families into their members
and writes one record per member and context class, with what the generators and checks need:

  { "tableFormat": ..., "specRefs": ...,
    "enums": { name: {"literals": [...], "doc"} },
    "helpers": [ {"name", "on", "params", "returns", "ocl", "doc", "basis", "helpers", "reads"} ],
    "members": [ {"name", "on", "family", "variant", "kind", "params": [{"name", "type", "default", "doc"}],
                  "returns", "doc", "basis", "ocl" | "prose", "helpers", "uses", "reads", "refusable",
                  "specEquivalents", "spellings": {lang: member name}, "examples": [...]} ],
    "counts": {...} }

A type is {"type", "kind": element | enum | primitive | literal | map, "many", "ordered", "optional"}; a
map's `type` is its elements' metaclass (its keys are names).
`helpers`, `uses` and `reads` are what the body calls, directly or through the helpers and members it
calls: contract helpers, other contract members, and specification properties and operations (by
name; the class a name is read on is not inferred).

Checks, each a rule over the data (generation fails on any):
  - every type names a metaclass, an enumeration, a primitive or OclAny;
  - every identifier of every OCL body is a keyword, a variable or parameter, a type, an enumeration
    literal of its enumeration, an OCL operation, or a specification property or operation, a helper
    or a member: on the context class where the receiver is `self` (written or implied), on some class
    otherwise. Receivers of other expressions are not typed, so a name that exists on some other class
    passes;
  - a family list with the owned scope matches the specification's owned{K} on Definition and
    nested{K} on Usage where those exist (same type);
  - member names are unique per class unless one context specializes the other with the same
    signature, and every language's spelling is unique and differs from every specification member's
    and runtime member's spelling.

`tableFormat` versions the shape of this file only; the contract itself follows the SDK package's
version.
"""

from __future__ import annotations

import json
import re
import sys
import tomllib
import xml.etree.ElementTree as ET

from naming import MM, ROOT, ancestors, flat_ops, getter, op_member, spelling

SOURCE = ROOT / "convenience.toml"
EXAMPLES = ROOT / "convenience-examples.toml"
OUT = ROOT / "convenience.json"
TABLE_FORMAT = "0.2.0"  # 0.2.0: parameter docs, `refusable`, the map type
LANGS = ("python", "ts", "java", "cs", "cpp")

PRIMITIVES = {"Boolean", "Integer", "Real", "String", "UnlimitedNatural"}
COLLECTIONS = {"Sequence": True, "OrderedSet": True, "Set": False, "Bag": False}

OCL_KEYWORDS = {"let", "in", "if", "then", "else", "endif", "and", "or", "not", "xor", "implies",
                "true", "false", "null", "invalid", "self"}
OCL_OBJECT_OPS = {"oclIsKindOf", "oclIsTypeOf", "oclAsType", "oclIsUndefined", "oclIsInvalid", "oclType"}
OCL_ITERATORS = {"select", "reject", "collect", "collectNested", "closure", "any", "exists", "forAll",
                 "one", "isUnique", "sortedBy", "iterate"}
OCL_COLLECTION_OPS = OCL_ITERATORS | {
    "isEmpty", "notEmpty", "size", "first", "last", "at", "includes", "excludes", "includesAll",
    "excludesAll", "union", "intersection", "including", "excluding", "selectByKind", "selectByType",
    "asOrderedSet", "asSequence", "asSet", "asBag", "flatten", "count", "sum", "indexOf", "append",
    "prepend", "reverse"}


def fail(errors: list[str]) -> None:
    if errors:
        sys.exit(f"{SOURCE.name}: {len(errors)} problem(s):\n  " + "\n  ".join(errors))


# ---------------------------------------------------------------- the metamodel's enumerations

def spec_enums() -> dict[str, list[str]]:
    """Enumeration literals from the specification's XMI (the metamodel table carries the enumeration
    types but not their literals); the files are fetched and hash-checked as the metamodel extractor
    does."""
    import extract_metamodel as X

    enums = {}
    for rel in ("spec-refs/KerML.xmi", "spec-refs/SysML.xmi"):
        for el in ET.parse(X.spec_file(rel)).getroot().iter():
            if el.get(f"{X.XMI_NS}type") == "uml:Enumeration":
                enums[el.get("name")] = [lit.get("name") for lit in el.findall("ownedLiteral")]
    return enums


# ---------------------------------------------------------------- types

class Types:
    def __init__(self, contract_enums: dict, spec_enum_literals: dict):
        self.contract_enums = contract_enums
        self.enums = {**spec_enum_literals, **{n: e["literals"] for n, e in contract_enums.items()}}

    def kind_of(self, name: str) -> str | None:
        if name in MM:
            return "element"
        if name in self.enums:
            return "enum"
        if name in PRIMITIVES:
            return "primitive"
        if name == "OclAny":
            return "literal"
        return None

    def parse(self, text: str, where: str, errors: list[str]) -> dict:
        text = text.strip()
        many = ordered = optional = False
        m = re.fullmatch(r"Map\((\w+),\s*(\w+)\)", text)
        if m:  # names to elements, in order: kind "map", `type` the elements'
            if m.group(1) != "String":
                errors.append(f"{where}: a map's keys are Strings, not {m.group(1)}")
            if m.group(2) not in MM:
                errors.append(f"{where}: a map's values are elements, not {m.group(2)}")
            return {"type": m.group(2), "kind": "map", "many": True, "ordered": True, "optional": False}
        m = re.fullmatch(r"(\w+)\((\w+)\)", text)
        if m:
            if m.group(1) not in COLLECTIONS:
                errors.append(f"{where}: {m.group(1)} is not a collection type")
            many, ordered, name = True, COLLECTIONS.get(m.group(1), False), m.group(2)
        else:
            m = re.fullmatch(r"(\w+)\[0\.\.1\]", text)
            if m:
                optional, name = True, m.group(1)
            elif re.fullmatch(r"\w+", text):
                name = text
            else:
                errors.append(f"{where}: cannot read the type {text!r}")
                name = text
        kind = self.kind_of(name)
        if kind is None:
            errors.append(f"{where}: {name} is not a metaclass, enumeration, primitive or OclAny")
        return {"type": name, "kind": kind, "many": many, "ordered": ordered, "optional": optional}


def params_of(raw: list, arguments: dict, types: Types, where: str, errors: list[str],
              documented: bool = False) -> list[dict]:
    """The parameters, each with its doc: a shared argument's own, else its enumeration's; a member's
    parameters must all have one (`documented`), as the generated doc comments describe each."""
    out = []
    for p in raw or []:
        if isinstance(p, str):
            if p not in arguments:
                errors.append(f"{where}: no shared argument {p!r}")
                continue
            a = arguments[p]
            t = types.parse(a["type"], f"{where}({p})", errors)
            doc = a.get("doc") or types.contract_enums.get(t["type"], {}).get("doc")
            out.append({"name": p, "type": t, "default": a.get("default"), "doc": doc})
        else:
            out.append({"name": p["name"], "type": types.parse(p["type"], f"{where}({p['name']})", errors),
                        "default": p.get("default"), "doc": p.get("doc")})
    for p in out:
        d = p["default"]
        t = p["type"]
        if d is not None and t["kind"] == "enum" and d not in types.enums[t["type"]]:
            errors.append(f"{where}: default {d!r} is not a literal of {t['type']}")
        if documented and not p["doc"]:
            errors.append(f"{where}: parameter {p['name']} has no doc")
    return out


# ---------------------------------------------------------------- family expansion

def substitute(text: str, kind: dict) -> str:
    cap = lambda s: s[0].upper() + s[1:]
    values = {"ks": kind["ks"], "k": kind["k"], "Ks": cap(kind["ks"]), "K": cap(kind["k"]),
              "M": kind["metaclass"], "X": exact_metaclass(kind), "L": kind.get("library", "")}
    return re.sub(r"\{(ks|k|Ks|K|M|X|L)\}", lambda m: values[m.group(1)], text)


def exact_metaclass(kind: dict) -> str:
    """The metaclass of a kind's exact variant: `exact` where it names one, else the kind's."""
    return kind["exact"] if isinstance(kind.get("exact"), str) else kind["metaclass"]


def kinds_of(src: dict, fam: dict) -> list[dict]:
    """A family's kind table: its own, or another family's (`kindsFrom`)."""
    return src["family"][fam["kindsFrom"]]["kinds"] if "kindsFrom" in fam else fam["kinds"]


def check_kinds(src: dict, errors: list[str]) -> None:
    """An exact variant needs a concrete metaclass (oclIsTypeOf of an abstract one never holds), the
    kind's or one of its subtypes, that has subtypes itself (else it is the plain list)."""
    for fname, fam in src["family"].items():
        for kind in kinds_of(src, fam):
            mc = kind["metaclass"]
            if mc not in MM:
                errors.append(f"{fname}/{kind['ks']}: no metaclass {mc}")
                continue
            if not kind.get("exact"):
                continue
            x = exact_metaclass(kind)
            if x not in MM or not (x == mc or mc in ancestors(x)):
                errors.append(f"{fname}/{kind['ks']}: the exact metaclass {x} is not {mc} or a subtype of it")
                continue
            if MM[x]["abstract"]:
                errors.append(f"{fname}/{kind['ks']}: an exact variant of the abstract {x} is always empty")
            if not any(x in ancestors(c) for c in MM):
                errors.append(f"{fname}/{kind['ks']}: {x} has no subtypes, so the exact variant is the list")


def expand(src: dict) -> list[dict]:
    """Raw member entries: the families' members, then the individual ones, each with one context."""
    out = []
    for fname, fam in src["family"].items():
        for kind in kinds_of(src, fam):
            for vname, var in fam["variants"].items():
                when = var.get("when")
                if when == "exact" and not kind.get("exact"):
                    continue
                if when == "library" and not kind.get("library"):
                    continue
                entry = {key: (substitute(val, kind) if isinstance(val, str) else val)
                         for key, val in var.items() if key != "when"}
                entry["on"] = entry.get("on", fam["on"])
                entry["params"] = [substitute(p, kind) if isinstance(p, str) else p
                                   for p in var.get("params", [])]
                entry.update(family=fname, variant=vname, kind=kind["ks"], k=kind["k"],
                             metaclass=kind["metaclass"], library=kind.get("library"))
                out.append(entry)
    for m in src["member"]:
        ons = m["on"] if isinstance(m["on"], list) else [m["on"]]
        for on in ons:
            out.append({**m, "on": on, "family": None, "variant": None, "kind": None})
    return out


# ---------------------------------------------------------------- OCL names

TOKEN = re.compile(r"""
    (?P<ws>\s+) | (?P<comment>--[^\n]*) | (?P<string>'(?:[^'\\]|\\.)*') | (?P<qid>_'[^']*')
  | (?P<id>[A-Za-z_][A-Za-z0-9_]*) | (?P<num>\d+(?:\.\d+)?)
  | (?P<op>->|::|<>|<=|>=|\.\.|[.()\[\]{},|:=<>+\-*/@])
""", re.VERBOSE)


def tokens(text: str, where: str, errors: list[str]) -> list[tuple[str, str]]:
    out, pos = [], 0
    while pos < len(text):
        m = TOKEN.match(text, pos)
        if not m:
            errors.append(f"{where}: cannot read the OCL at {text[pos:pos + 20]!r}")
            return out
        pos = m.end()
        if m.lastgroup not in ("ws", "comment"):
            out.append((m.lastgroup, m.group()))
    return out


def class_has(cls: str, name: str) -> bool:
    return name in MM[cls]["props"] or any(op["name"] == name for _, op in flat_ops(cls))


SPEC_NAMES = {n for c in MM.values() for n in c["props"]} | {o["name"] for c in MM.values() for o in c["ops"]}


def is_a(cls: str, on: str) -> bool:
    return cls == on or on in ancestors(cls)


class Names:
    """Classifies the identifiers of one OCL body and collects what it calls."""

    def __init__(self, types: Types, helpers: dict[str, list[str]], members: dict[str, list[str]]):
        self.types = types
        self.helpers = helpers  # helper name -> classes it is defined on
        self.members = members  # member name -> classes it is defined on

    def on_class(self, cls: str, name: str, call: bool) -> str | None:
        """What `name` is on `cls` (self): 'spec', 'helper', 'member', 'ocl', or None."""
        if call and name in OCL_OBJECT_OPS:
            return "ocl"
        if class_has(cls, name):
            return "spec"
        if call and any(is_a(cls, c) for c in self.helpers.get(name, [])):
            return "helper"
        if call and any(is_a(cls, c) for c in self.members.get(name, [])):
            return "member"
        return None

    def anywhere(self, name: str, call: bool) -> str | None:
        if call and name in OCL_OBJECT_OPS:
            return "ocl"
        if name in SPEC_NAMES:
            return "spec"
        if call and name in self.helpers:
            return "helper"
        if call and name in self.members:
            return "member"
        return None

    @staticmethod
    def tuple_parts(toks: list[tuple[str, str]]) -> set[str]:
        """The part names of the body's tuple types and literals (`Tuple(key : String, ...)`,
        `Tuple{key = n, ...}`): an identifier followed by `:` or `=` directly inside one."""
        parts = set()
        for i, (kind, text) in enumerate(toks):
            if text != "Tuple" or i + 1 >= len(toks) or toks[i + 1][1] not in ("(", "{"):
                continue
            depth = 0
            for j in range(i + 1, len(toks)):
                t = toks[j][1]
                if t in ("(", "{"):
                    depth += 1
                elif t in (")", "}"):
                    depth -= 1
                    if depth == 0:
                        break
                elif depth == 1 and toks[j][0] == "id" and j + 1 < len(toks) and toks[j + 1][1] in (":", "="):
                    parts.add(t)
        return parts

    def check(self, ocl: str, context: str, params: list[str], where: str, errors: list[str]) -> dict:
        toks = tokens(ocl, where, errors)
        found = {"helper": set(), "member": set(), "spec": set()}
        parts = self.tuple_parts(toks)
        # Variables: let-bound names and declared iterator variables, anywhere in the body.
        variables = set(params)
        for i, (kind, text) in enumerate(toks):
            nxt = toks[i + 1][1] if i + 1 < len(toks) else ""
            if kind == "id" and i > 0 and toks[i - 1][1] == "let":
                variables.add(text)
            if kind == "id" and i > 0 and toks[i - 1][1] == "(" and (nxt == "|" or (
                    nxt == ":" and i + 3 < len(toks) and toks[i + 3][1] == "|")):
                variables.add(text)
        frames: list[str] = []  # per open parenthesis: 'implicit' / 'declared' iterator body, or 'plain'
        for i, (kind, text) in enumerate(toks):
            prev = toks[i - 1][1] if i > 0 else ""
            prev2 = toks[i - 2][1] if i > 1 else ""
            nxt = toks[i + 1][1] if i + 1 < len(toks) else ""
            if kind == "op" and text == "(":
                frame = "plain"
                if prev in OCL_ITERATORS and prev2 == "->":
                    a = toks[i + 1][1] if i + 1 < len(toks) else ""
                    b = toks[i + 2][1] if i + 2 < len(toks) else ""
                    d = toks[i + 4][1] if i + 4 < len(toks) else ""
                    frame = "declared" if b == "|" or (b == ":" and d == "|") else "implicit"
                frames.append(frame)
                continue
            if kind == "op" and text == ")":
                if frames:
                    frames.pop()
                continue
            if kind == "qid":
                if prev != "::":
                    errors.append(f"{where}: {text} outside an enumeration literal")
                continue
            if kind != "id":
                continue
            call = nxt == "("
            if prev == "->":
                if text not in OCL_COLLECTION_OPS:
                    errors.append(f"{where}: ->{text} is not an OCL collection operation")
                continue
            if prev == "::":
                continue  # an enumeration literal: checked with its type below
            if nxt == "::":
                literal = toks[i + 2][1] if i + 2 < len(toks) else ""
                literal = literal[2:-1] if literal.startswith("_'") else literal
                if text not in self.types.enums:
                    errors.append(f"{where}: {text} is not an enumeration")
                elif literal not in self.types.enums[text]:
                    errors.append(f"{where}: {literal} is not a literal of {text}")
                continue
            if text in OCL_KEYWORDS:
                continue
            if text in parts and (prev == "." or nxt in (":", "=")):
                continue  # a tuple part: declared, or read after a dot
            if text[0].isupper():
                if text in COLLECTIONS and nxt in ("{", "("):
                    continue
                if text == "Tuple" and nxt in ("{", "("):
                    continue
                if self.types.kind_of(text) in ("element", "enum", "primitive"):
                    continue
                errors.append(f"{where}: {text} is not a type")
                continue
            if prev == ".":
                what = (self.on_class(context, text, call) if prev2 == "self"
                        else self.anywhere(text, call))
                if what is None:
                    on = f"on {context}" if prev2 == "self" else "on any class"
                    errors.append(f"{where}: .{text}{'()' if call else ''} is not a member {on}")
                elif what != "ocl":
                    found[what].add(text)
                continue
            if text in variables and not call:
                continue
            iterator = next((f for f in reversed(frames) if f != "plain"), None)
            what = self.anywhere(text, call) if iterator == "implicit" else self.on_class(context, text, call)
            if what is None:
                on = "on the iterated elements" if iterator == "implicit" else f"on {context}"
                errors.append(f"{where}: {text}{'()' if call else ''} is not a variable, nor a member {on}")
            elif what != "ocl":
                found[what].add(text)
        return found


# ---------------------------------------------------------------- spellings

def spec_spellings(cls: str, lang: str) -> set[str]:
    props = MM[cls]["props"]
    ops = {op["name"] for _, op in flat_ops(cls)}
    if lang in ("java", "cpp"):
        return {getter(p) for p in props} | ops
    return set(props) | {op_member(o, lang) for o in ops}


def python_runtime_members() -> set[str]:
    sys.path.insert(0, str(ROOT / "python"))
    from sysml.base import Element, Find, Op, P

    return {n for n in dir(Element) if not n.startswith("_") and not isinstance(getattr(Element, n), (Find, Op, P))}


# ---------------------------------------------------------------- main

def main() -> None:
    src = tomllib.loads(SOURCE.read_text(encoding="utf-8"))
    errors: list[str] = []
    types = Types(src["enums"], spec_enums())
    arguments = src["arguments"]
    for n, a in arguments.items():
        types.parse(a["type"], f"argument {n}", errors)

    check_kinds(src, errors)
    fail(errors)
    raw_members = expand(src)
    helper_classes: dict[str, list[str]] = {}
    for h in src["helper"]:
        helper_classes.setdefault(h["name"], []).append(h["on"])
    member_classes: dict[str, list[str]] = {}
    for m in raw_members:
        member_classes.setdefault(m["name"], []).append(m["on"])
    for name, classes in {**helper_classes, **member_classes}.items():
        for c in classes:
            if c not in MM:
                errors.append(f"{name}: no metaclass {c}")
    fail(errors)
    names = Names(types, helper_classes, member_classes)

    def body(entry: dict, where: str, params: list[dict]) -> dict:
        ocl = entry.get("ocl")
        if ocl is None:
            if not entry.get("prose"):
                errors.append(f"{where}: neither OCL nor prose")
            return {"helper": set(), "member": set(), "spec": set()}
        return names.check(ocl, entry["on"], [p["name"] for p in params], where, errors)

    helpers = []
    for h in src["helper"]:
        where = f"helper {h['on']}::{h['name']}"
        params = params_of(h.get("params"), arguments, types, where, errors)
        found = body(h, where, params)
        helpers.append({"name": h["name"], "on": h["on"], "params": params,
                        "returns": types.parse(h["returns"], where, errors),
                        "ocl": h["ocl"].strip(), "doc": h.get("doc"), "basis": h.get("basis"),
                        "_found": found})

    members = []
    for m in raw_members:
        where = f"{m['on']}::{m['name']}"
        params = params_of(m.get("params"), arguments, types, where, errors, documented=True)
        if not m.get("doc"):
            errors.append(f"{where}: no doc")
        found = body(m, where, params)
        rec = {"name": m["name"], "on": m["on"], "family": m["family"], "variant": m["variant"],
               "kind": m["kind"], "params": params, "returns": types.parse(m["returns"], where, errors),
               "doc": m.get("doc"), "basis": m.get("basis")}
        if m["family"]:
            rec["kindMetaclass"] = m["metaclass"]
            rec["libraryType"] = m.get("library")
        if m.get("ocl") is not None:
            rec["ocl"] = m["ocl"].strip()
        else:
            rec["prose"] = " ".join(m["prose"].split())
        if m["family"] == "kindLists" and m["variant"] == "list":
            rec["specEquivalents"] = spec_equivalents(m, errors)
        rec["spellings"] = {lang: spelling(m["name"], lang) for lang in LANGS}
        rec["_found"] = found
        members.append(rec)
    fail(errors)

    close_calls(helpers, members)
    mark_refusable(helpers, members, errors)
    check_classes(members, errors)
    check_spellings(members, errors)
    fail(errors)

    ex_src = tomllib.loads(EXAMPLES.read_text(encoding="utf-8"))
    models, examples = load_examples(ex_src, src, members, types, errors)
    fail(errors)

    counts = count(members, examples)
    for r in helpers + members:
        del r["_found"]
    table = {"tableFormat": TABLE_FORMAT, "specRefs": src["meta"]["specRefs"],
             "metamodelTable": json.loads((ROOT / "metamodel.json").read_text(encoding="utf-8"))["meta"]["table"],
             "enums": src["enums"], "helpers": helpers, "members": members, "models": models,
             "examples": examples, "counts": counts}
    OUT.write_text(json.dumps(table, indent=1, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")
    print(f"wrote {OUT.name}: {counts['members']} members ({counts['records']} records over "
          f"{counts['contexts']} context classes), {len(helpers)} helpers; by family: {counts['byFamily']}; "
          f"{counts['examples']} examples over {len(models)} models")


# ---------------------------------------------------------------- examples

# What each family variant's example takes from a kind's row of facts: the answer's key (the
# semantic test takes two, semanticYes and semanticNo, asked on those features).
DEMO_VARIANTS = {"list": "list", "exact": "exact", "semantic": "semantic", "byName": "lookup", "nameMap": "list"}


def load_examples(ex: dict, src: dict, members: list[dict], types: Types, errors: list[str]) -> tuple[dict, list]:
    """The models and the examples, the families' expanded from their rows of facts; every example
    checked against its member's signature, every member given one at least."""
    by_name: dict[str, list[dict]] = {}
    for m in members:
        by_name.setdefault(m["name"], []).append(m)
    models = {name: {"doc": mdl.get("doc"), "text": mdl["text"].lstrip("\n"), "metaclasses": {}}
              for name, mdl in ex["models"].items()}
    examples = []

    for fname, demo in ex["demos"].items():
        # Rows of another family's demos (`from`), with this family's own rows besides.
        base = ex["demos"][demo["from"]] if "from" in demo else demo
        model = base["model"]
        rows = {**base.get("kinds", {}), **demo.get("kinds", {})}
        fam = src["family"][fname]
        for kind in kinds_of(src, fam):
            ks = kind["ks"]
            facts = rows.get(ks)
            where = f"demo {fname}/{ks}"
            if facts is None:
                errors.append(f"{where}: no row of facts")
                continue
            at, mcs = facts["at"], facts["metaclasses"]
            for n, mc in mcs.items():
                if mc not in MM:
                    errors.append(f"{where}: {n} has no metaclass {mc}")
                    continue
                models[model]["metaclasses"][f"{at}::{n}"] = mc
            named = [n for key in ("list", "exact", "semantic") for n in facts.get(key, [])]
            named += [facts[key] for key in ("lookup", "semanticYes", "semanticNo") if key in facts]
            for n in named:
                if n not in mcs:
                    errors.append(f"{where}: {n} is named but given no metaclass")
            # The list and exact answers follow from the metaclasses: a check of the hand-written ones.
            def kind_of(n: str, mc: str) -> bool:
                return mcs.get(n) in MM and (mcs[n] == mc or mc in ancestors(mcs[n]))
            want = [n for n in mcs if kind_of(n, kind["metaclass"])]
            if facts.get("list") != want:
                errors.append(f"{where}: list {facts.get('list')}, but the metaclasses give {want}")
            if kind.get("exact") and "exact" in fam["variants"]:
                want_x = [n for n in mcs if mcs[n] == exact_metaclass(kind)]
                if facts.get("exact") != want_x:
                    errors.append(f"{where}: exact {facts.get('exact')}, but the metaclasses give {want_x}")
            for rec in members:
                if rec["family"] != fname or rec["kind"] != ks:
                    continue
                base = {"member": rec["name"], "model": model, "from": f"{fname}/{ks}"}
                if rec["variant"] == "semanticTest":
                    for key, answer in (("semanticYes", True), ("semanticNo", False)):
                        if key not in facts:
                            errors.append(f"{where}: no {key} for {rec['name']}")
                        else:
                            examples.append({**base, "at": f"{at}::{facts[key]}", "expect": answer})
                    continue
                key = DEMO_VARIANTS[rec["variant"]]
                if key not in facts:
                    errors.append(f"{where}: no {key} for {rec['name']}")
                elif rec["variant"] == "byName":
                    examples.append({**base, "at": at, "args": {"name": facts[key]},
                                     "expect": f"{at}::{facts[key]}"})
                elif rec["variant"] == "nameMap":
                    # The holders give their members one name each (no alias, no short name).
                    examples.append({**base, "at": at, "expect": [[n, f"{at}::{n}"] for n in facts[key]]})
                else:
                    examples.append({**base, "at": at, "expect": [f"{at}::{n}" for n in facts[key]]})

    for i, e in enumerate(ex.get("example", [])):
        where = f"example {i + 1} ({e.get('member')})"
        if e.get("member") not in by_name:
            errors.append(f"{where}: no member {e.get('member')}")
            continue
        if e.get("model") not in models:
            errors.append(f"{where}: no model {e.get('model')}")
            continue
        if not e.get("at"):
            errors.append(f"{where}: no element asked (at)")
        answers = [k for k in ("expect", "none", "refused", "includes") if k in e]
        if len(answers) != 1 or ("excludes" in e and "includes" not in e):
            errors.append(f"{where}: give one of expect, none, refused, includes (with excludes)")
        e = dict(e)
        if e.pop("none", None) is True:
            e["expect"] = None  # TOML has no null
        examples.append(e)

    for e in examples:
        check_example(e, by_name[e["member"]], types, errors)
        if unordered(e, by_name[e["member"]][0]):
            e["unordered"] = True
    missing = sorted({m["name"] for m in members} - {e["member"] for e in examples})
    if missing:
        errors.append(f"{len(missing)} member(s) without an example: {', '.join(missing)}")
    return models, examples


VIA_STEP = re.compile(r"(\w+)(?:\[(\w+)\])?")


def unordered(e: dict, rec: dict) -> bool:
    """Whether an example's answer has no guaranteed order, so that it is compared as a set: a member or
    definition list outside the owned scope, and every name-keyed map, all of which follow the order of
    membership, which the specification does not define."""
    if rec["family"] not in ("memberLists", "definitionLists"):
        return False
    if rec["variant"] == "nameMap":
        return True
    if rec["variant"] not in ("list", "exact", "semantic"):
        return False
    scope = next((p["default"] for p in rec["params"] if p["name"] == "scope"), "owned")
    return (e.get("args") or {}).get("scope", scope) != "owned"


def check_example(e: dict, recs: list[dict], types: Types, errors: list[str]) -> None:
    rec = recs[0]
    where = f"example of {rec['name']} at {e.get('at')}"
    if any(signature(r) != signature(rec) for r in recs):
        errors.append(f"{where}: the member's contexts differ in signature")
    for step in e.get("via", []):
        m = VIA_STEP.fullmatch(step)
        if not m or m.group(1) not in SPEC_NAMES or (m.group(2) and m.group(2) not in MM):
            errors.append(f"{where}: via step {step!r} is not prop or prop[Metaclass]")
    params = {p["name"]: p for p in rec["params"]}
    args = e.get("args", {})
    for name, value in args.items():
        if name not in params:
            errors.append(f"{where}: no parameter {name}")
        elif not value_fits(value, {**params[name]["type"], "optional": False}, types):
            errors.append(f"{where}: argument {name} = {value!r} is not a {params[name]['type']['type']}")
    for name, p in params.items():
        if p["default"] is None and name not in args:
            errors.append(f"{where}: no argument for {name}, which has no default")
    ret = rec["returns"]
    if "expect" in e and not value_fits(e["expect"], ret, types):
        errors.append(f"{where}: {e['expect']!r} is not a {ret['type']}{' list' if ret['many'] else ''}"
                      f"{' or null' if ret['optional'] else ''}")
    for key in ("includes", "excludes"):
        if key in e and not (ret["many"] and ret["kind"] == "element" and value_fits(e[key], ret, types)):
            errors.append(f"{where}: {key} needs a list of elements, of a member that returns one")
    if "refused" in e and e["refused"] is not True:
        errors.append(f"{where}: refused is true or absent")
    deviates = e.get("deviates", {})
    if not isinstance(deviates, dict) or any(
            k not in ("toolkit", "pilot") or not isinstance(v, str) or not v.strip() for k, v in deviates.items()):
        errors.append(f"{where}: deviates maps a backend (toolkit, pilot) to the reason its answer differs")


def value_fits(value, t: dict, types: Types) -> bool:
    """Whether an example's value has the shape of type `t` (an element by qualified name, or as
    {"@type": metaclass, ...} of the type or a subtype); a map's as a list of [name, element] pairs."""
    if t["kind"] == "map":
        one = {**t, "kind": "element", "many": False, "ordered": False, "optional": False}
        return isinstance(value, list) and all(
            isinstance(p, list) and len(p) == 2 and isinstance(p[0], str) and value_fits(p[1], one, types)
            for p in value)
    if t["many"]:
        one = {**t, "many": False, "optional": False}
        return isinstance(value, list) and all(value_fits(v, one, types) for v in value)
    if value is None:
        return t["optional"]
    kind, ty = t["kind"], t["type"]
    if kind == "element":
        if isinstance(value, str):
            return bool(value)
        return (isinstance(value, dict) and value.get("@type") in MM
                and (value["@type"] == ty or ty in ancestors(value["@type"])))
    if kind == "enum":
        return value in types.enums[ty]
    if kind == "literal":
        return isinstance(value, (bool, int, float, str))
    if ty == "Boolean":
        return isinstance(value, bool)
    if ty == "Integer":
        return isinstance(value, int) and not isinstance(value, bool)
    if ty == "UnlimitedNatural":
        return value == "*" or (isinstance(value, int) and not isinstance(value, bool) and value >= 0)
    if ty == "Real":
        return isinstance(value, (int, float)) and not isinstance(value, bool)
    return isinstance(value, str)


def spec_equivalents(m: dict, errors: list[str]) -> list[str]:
    """The specification's owned{K} on Definition and nested{K} on Usage that a kind list with the
    owned scope equals, where they exist; their type must be the list's."""
    K = m["k"][0].upper() + m["k"][1:]
    out = []
    for cls, prop in (("Definition", "owned" + K), ("Usage", "nested" + K)):
        row = MM[cls]["props"].get(prop)
        if row is None:
            continue
        if row["type"] != m["metaclass"]:
            errors.append(f"{m['name']}: {cls}::{prop} is typed {row['type']}, the list {m['metaclass']}")
        out.append(f"{cls}::{prop}")
    return out


def close_calls(helpers: list[dict], members: list[dict]) -> None:
    """helpers / uses / reads, closed over the helpers and members a body calls."""
    by_helper = {h["name"]: [x for x in helpers if x["name"] == h["name"]] for h in helpers}
    by_member: dict[str, list[dict]] = {}
    for m in members:
        by_member.setdefault(m["name"], []).append(m)

    def closure(found: dict) -> tuple[set, set, set]:
        hs, ms, spec = set(), set(), set(found["spec"])
        todo = [("helper", n) for n in found["helper"]] + [("member", n) for n in found["member"]]
        while todo:
            what, n = todo.pop()
            seen = hs if what == "helper" else ms
            if n in seen:
                continue
            seen.add(n)
            for r in (by_helper if what == "helper" else by_member)[n]:
                spec |= r["_found"]["spec"]
                todo += [("helper", x) for x in r["_found"]["helper"]] + [("member", x) for x in r["_found"]["member"]]
        return hs, ms, spec

    for h in helpers:
        hs, _, spec = closure(h["_found"])
        h["helpers"], h["reads"] = sorted(hs), sorted(spec)
    for m in members:
        hs, ms, spec = closure(m["_found"])
        m["helpers"], m["uses"], m["reads"] = sorted(hs), sorted(ms), sorted(spec)


def mark_refusable(helpers: list[dict], members: list[dict], errors: list[str]) -> None:
    """`refusable`: whether the contract can refuse the member (its body, or a helper or member it calls,
    yields `invalid`). Its doc must then say when, as the generated doc comments refer to it."""
    def gives_invalid(r: dict) -> bool:
        return bool(re.search(r"\binvalid\b", r.get("ocl") or ""))

    helper_invalid = {h["name"] for h in helpers if gives_invalid(h)}
    member_invalid = {m["name"] for m in members if gives_invalid(m)}
    for m in members:
        m["refusable"] = (gives_invalid(m) or bool(helper_invalid & set(m["helpers"]))
                          or bool(member_invalid & set(m["uses"])))
        if m["refusable"] and not re.search(r"[Rr]efuse", m["doc"] or ""):
            errors.append(f"{m['on']}::{m['name']}: the contract can refuse it, but its doc does not say when")


def signature(m: dict) -> tuple:
    return (tuple((p["name"], json.dumps(p["type"], sort_keys=True), json.dumps(p["default"])) for p in m["params"]),
            json.dumps(m["returns"], sort_keys=True))


def check_classes(members: list[dict], errors: list[str]) -> None:
    """On every metaclass, one member per name: several records may apply only where one context
    specializes all the others, with the same signature. Reported once per conflict, with the number
    of metaclasses it reaches."""
    conflicts: dict[str, list[str]] = {}
    for cls in sorted(MM):
        by_name: dict[str, list[dict]] = {}
        for m in members:
            if is_a(cls, m["on"]):
                by_name.setdefault(m["name"], []).append(m)
        for name, recs in by_name.items():
            if len(recs) == 1:
                continue
            ons = {r["on"] for r in recs}
            most = [o for o in ons if all(o == p or p in ancestors(o) for p in ons)]
            if not most:
                conflicts.setdefault(f"{name} comes from unrelated contexts {sorted(ons)}", []).append(cls)
            if len({signature(r) for r in recs}) > 1:
                what = ", ".join(f"{r['on']} ({r['family'] or 'individual'}/{r['variant'] or '-'})" for r in recs)
                conflicts.setdefault(f"{name} has different signatures on {what}", []).append(cls)
    for what, classes in conflicts.items():
        errors.append(f"{what}: on {len(classes)} metaclasses ({classes[0]}, ...)")


def check_spellings(members: list[dict], errors: list[str]) -> None:
    names = sorted({m["name"] for m in members})
    runtime = {"python": python_runtime_members()}
    for lang in LANGS:
        seen: dict[str, str] = {}
        for n in names:
            s = spelling(n, lang)
            if s in seen:
                errors.append(f"{lang}: {n} and {seen[s]} are both spelled {s}")
            seen[s] = n
            if s in runtime.get(lang, set()):
                errors.append(f"{lang}: {n} is spelled {s}, a runtime member")
        for cls in MM:
            clash = spec_spellings(cls, lang) & {spelling(m["name"], lang) for m in members if is_a(cls, m["on"])}
            for s in sorted(clash):
                errors.append(f"{lang}: {cls}.{s} is also a specification member")


def count(members: list[dict], examples: list[dict]) -> dict:
    names = {m["name"] for m in members}
    by_family: dict[str, dict[str, int]] = {}
    for fam in ("kindLists", "memberLists", "definitionLists", None):
        sub = {}
        for m in members:
            if m["family"] == fam:
                sub.setdefault(m["variant"] or "individual", set()).add(m["name"])
        by_family[fam or "individual"] = {v: len(s) for v, s in sub.items()}
    return {"members": len(names), "records": len(members), "contexts": len({m["on"] for m in members}),
            "byFamily": by_family, "examples": len(examples)}


if __name__ == "__main__":
    main()
