"""Extract the language-neutral metamodel table (metamodel.json).

Inputs (all read-only, vendored under vendor/sysml-toolkit/ — see PIN.json
for provenance; set SYSML_TOOLKIT_REPO to read a live sysml-toolkit
checkout instead when re-vendoring):
  spec-refs/KerML.xmi, spec-refs/SysML.xmi   -> class hierarchy, abstract, operations
  crates/sysmlv2-model/src/schema_props.rs   -> concrete-class property catalog + shapes
  crates/sysmlv2-testkit/src/xmi_props.rs    -> per-property derived flag

Output: metamodel.json
  { "classes": { name: { "abstract": bool, "bases": [name...],
                          "props": { pname: {"shape": "A|B|N|R|S|E",
                                              "derived": bool,
                                              "type": name, "kind": ...,
                                              "redefines": ["Class::prop", ...]} },
                          "ops": [ {"name": ..., "params": [name, ...],
                                    "paramTypes": [{"type", "kind", "many"}, ...],
                                    "returns": {"type", "kind", "many"} | null,
                                    "redefines": ["Class::op", ...]} ] } } }

Table 0.6.0 drops the per-property
"reliability" column: the SysML Toolkit reports its own unimplemented
members, so the SDK no longer second-guesses results. The "derived"
boolean stays — it is genuine XMI spec information.

Table 0.7.0 adds redefinitions, of properties and of operations, and
parameter and return types, and leaves the return parameter out of
"params". A property's "redefines" lists what its nearest XMI declaration
along the class's ancestry redefines; a redefinition whose target is not a
class attribute (an association end) is skipped and counted.

The published XMI carries no enumeration-valued attribute at all: no
parameter `direction`, no `aggregation`, no `visibility` (the Boolean flags
are all there). The source model has them: the Pilot Implementation's
Eclipse UML export of it (org.omg.sysml/model/SysML.uml, as of 2025-06-02)
marks one `direction="return"` parameter on every operation. Here the
return parameter is identified by convention: the one unnamed parameter,
else the one named `result`, else none. The convention picks the parameter
the Pilot's export marks on all 103 operations, agrees with every
operation's xmi:id (which lists its input types, e.g. `evaluate_Element`),
and with every operation signature of the KerML and SysML documents
(checked 2026-09-27).

Table 0.7.1 corrects the XMI where it contradicts the specification
document, listed in XMI_ERRATA with the source of each correction.
"""

import json
import os
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

HERE = Path(__file__).resolve().parent.parent  # repository root
REPO = Path(os.environ.get("SYSML_TOOLKIT_REPO", HERE / "vendor" / "sysml-toolkit"))

# metamodel.json is a published, versioned artifact: bump
# TABLE_VERSION on any change to its shape or semantics. Provenance is
# always taken from the vendored PIN.json — when SYSML_TOOLKIT_REPO
# overrides the inputs, re-vendor and update the pin first.
TABLE_VERSION = "0.7.1"
SPEC_REFS = "20250201"
XMI_NS = "{http://www.omg.org/spec/XMI/20161101}"

# Corrections to the published XMI, each with its source. Extraction fails when the XMI already
# carries what a correction supplies, so a correction never outlives its reason.
_BOOLEAN = {"type": "Boolean", "kind": "primitive", "many": False}
XMI_ERRATA = {
    # The XMI gives isCompatibleWith no return parameter. KerML 20250201 gives
    # `isCompatibleWith(otherType : Type) : Boolean` for Type and for Feature; the Pilot
    # Implementation's metamodel added the Boolean return on 2025-06-02 (ST6RI-854).
    ("Type", "isCompatibleWith"): {"returns": _BOOLEAN},
    ("Feature", "isCompatibleWith"): {"returns": _BOOLEAN},
}


def raw_ref(el, file_key: str):
    """A child element's reference: ("idref", file_key, id) | ("href", href_string) | None."""
    if el is None:
        return None
    idref = el.get(f"{XMI_NS}idref") or el.get("idref")
    if idref:
        return ("idref", file_key, idref)
    href = el.get("href")
    return ("href", href) if href else None


def ref_key(ref):
    """(file_key, xmi_id) of a raw reference."""
    if ref[0] == "idref":
        return ref[1], ref[2]
    doc, _, frag = ref[1].partition("#")
    return ("KerML" if "KerML" in doc else "SysML"), frag


def is_many(el) -> bool:
    """True when a typed element's upper bound exceeds one (UML default: one)."""
    up = el.find("upperValue")
    if up is None:
        return False
    v = up.get("value", "1")
    return v in ("*", "-1") or (v.isdigit() and int(v) > 1)


def return_index(params) -> int | None:
    """Index of the return parameter among an operation's ownedParameter elements (see the
    module docstring): the one unnamed parameter, else the one named `result`, else none."""
    unnamed = [i for i, p in enumerate(params) if not p.get("name")]
    if len(unnamed) == 1:
        return unnamed[0]
    if unnamed:
        sys.exit(f"operation with {len(unnamed)} unnamed parameters: {params[0].get(f'{XMI_NS}id')}")
    named_result = [i for i, p in enumerate(params) if p.get("name") == "result"]
    return named_result[0] if len(named_result) == 1 else None


def parse_xmi_classes(path: Path, file_key: str):
    """-> full_ids {xmi_id: name} (every named packagedElement),
       enum_ids (uml:Enumeration ids),
       member_ids {xmi_id: (class, member)} (every class attribute and operation),
       classes {name: {"abstract", "bases", "ops", "attrs", "attr_redefs"}} for uml:Class.

    attrs: {attr_name: ("idref", file_key, id) | ("href", href_string)} —
    raw type references, resolved after both files are parsed; likewise the
    raw references in attr_redefs {attr_name: [ref, ...]} and in each op's
    "redefs", "param_refs" and "return_ref"."""
    root = ET.parse(path).getroot()
    full_ids, enum_ids, member_ids, classes = {}, set(), {}, {}
    for el in root.iter():
        if el.tag != "packagedElement":
            continue
        name, xid = el.get("name"), el.get(f"{XMI_NS}id")
        if name and xid:
            full_ids[xid] = name
        if el.get(f"{XMI_NS}type") == "uml:Enumeration" and xid:
            enum_ids.add(xid)
        if el.get(f"{XMI_NS}type") != "uml:Class" or not name or not xid:
            continue
        bases = []
        for g in el.findall("generalization"):
            ge = g.find("general")
            if ge is None:
                continue
            idref = ge.get(f"{XMI_NS}idref") or ge.get("idref")
            if idref:
                bases.append((file_key, idref))
                continue
            href = ge.get("href")
            if href:
                ref_file, _, frag = href.partition("#")
                key = "KerML" if "KerML" in ref_file else "SysML"
                bases.append((key, frag))
        ops = []
        for o in el.findall("ownedOperation"):
            member_ids[o.get(f"{XMI_NS}id")] = (name, o.get("name"))
            plist = o.findall("ownedParameter")
            ret = return_index(plist)
            params, param_refs = [], []
            for i, p in enumerate(plist):
                if i == ret:
                    continue
                params.append(p.get("name"))
                param_refs.append((raw_ref(p.find("type"), file_key), is_many(p)))
            return_ref = None if ret is None else (raw_ref(plist[ret].find("type"), file_key), is_many(plist[ret]))
            redefs = [raw_ref(r, file_key) for r in o.findall("redefinedOperation")]
            ops.append({"name": o.get("name"), "params": params, "param_refs": param_refs,
                        "return_ref": return_ref, "redefs": redefs})
        attrs, attr_redefs = {}, {}
        for a in el.findall("ownedAttribute"):
            aname = a.get("name")
            if a.get(f"{XMI_NS}id") and aname:
                member_ids[a.get(f"{XMI_NS}id")] = (name, aname)
            ref = raw_ref(a.find("type"), file_key)
            if not aname or ref is None:
                continue
            attrs[aname] = ref
            attr_redefs[aname] = [raw_ref(r, file_key) for r in a.findall("redefinedProperty")]
        classes[name] = {"abstract": el.get("isAbstract") == "true",
                        "bases": bases, "ops": ops, "attrs": attrs, "attr_redefs": attr_redefs}
    return full_ids, enum_ids, member_ids, classes


def parse_schema_props(path: Path):
    """schema_props.rs -> {class: {prop: shape_char}} (concrete classes only)."""
    src = path.read_text(encoding="utf-8")
    out = {}
    for m in re.finditer(r'\(\s*"(\w+)",\s*&\[(.*?)\]\s*,\s*\)', src, re.S):
        cls, body = m.group(1), m.group(2)
        out[cls] = {p: s for p, s in re.findall(r"\(\"(\w+)\", b'(.)'\)", body)}
    return out


def parse_xmi_props(path: Path):
    """xmi_props.rs -> {class: {prop: derived_bool}}, {class: abstract_bool}."""
    src = path.read_text(encoding="utf-8")
    derived, abstract = {}, {}
    for m in re.finditer(
        r'\(\s*"(\w+)",\s*(true|false),\s*&\[(.*?)\n        \],\s*\n    \),', src, re.S
    ):
        cls, ab, body = m.group(1), m.group(2), m.group(3)
        abstract[cls] = ab == "true"
        derived[cls] = {
            p: (int(f) & 1) != 0 for p, f in re.findall(r'\("(\w+)",\s*(\d+),', body)
        }
    return derived, abstract


PRIMITIVES = {"Boolean", "Integer", "Real", "String", "UnlimitedNatural"}


def main():
    k_ids, k_enums, k_members, k_classes = parse_xmi_classes(REPO / "spec-refs/KerML.xmi", "KerML")
    s_ids, s_enums, s_members, s_classes = parse_xmi_classes(REPO / "spec-refs/SysML.xmi", "SysML")
    # Language provenance relies on the two declaration sets being a
    # partition (measured for 20250201: 82 + 93, no overlap; SysML.xmi
    # references KerML classes by cross-document href, never by
    # re-declaring them). If a future spec release breaks that, decide
    # the policy consciously instead of inheriting a silent tie-break.
    overlap = sorted(set(k_classes) & set(s_classes))
    if overlap:
        sys.exit(f"language provenance ambiguous — declared in BOTH XMIs: {overlap}")
    id_maps = {"KerML": k_ids, "SysML": s_ids}
    member_maps = {"KerML": k_members, "SysML": s_members}
    enum_ids = k_enums | s_enums
    merged = {}
    for src in (k_classes, s_classes):
        for name, info in src.items():
            merged.setdefault(name, info)  # SysML redefinitions keep KerML entry order

    # resolve base references to class names
    def base_names(info):
        out = []
        for key, ref in info["bases"]:
            nm = id_maps[key].get(ref) or id_maps["KerML"].get(ref) or id_maps["SysML"].get(ref)
            if nm and nm not in out:
                out.append(nm)
        return out

    # --- property types (nearest XMI declaration wins) ---------------------
    def resolve_type(ref):
        """raw attr type ref -> (type_name, kind) with kind in
        element | enum | primitive, or (None, None) if unresolvable."""
        if ref[0] == "idref":
            _, fkey, xid = ref
        else:
            _, href = ref
            _, _, xid = href.partition("#")
            fkey = "KerML" if "KerML" in href.partition("#")[0] else "SysML"
        if xid in PRIMITIVES:
            return xid, "primitive"
        name = id_maps[fkey].get(xid) or id_maps["KerML"].get(xid) or id_maps["SysML"].get(xid)
        if name is None:
            return None, None
        if name in merged:
            return name, "element"
        if xid in enum_ids:
            return name, "enum"
        return name, None

    bases_by_name = {name: base_names(info) for name, info in merged.items()}

    def ancestors(n, acc):
        for b in bases_by_name[n]:
            if b not in acc:
                acc.add(b)
                ancestors(b, acc)
        return acc

    topo, seen = [], set()

    def visit(n):
        if n in seen:
            return
        seen.add(n)
        for b in bases_by_name[n]:
            visit(b)
        topo.append(n)

    for n in sorted(merged):
        visit(n)
    topo_index = {n: i for i, n in enumerate(topo)}

    def nearest_declaration(cls, prop):
        """The class holding the most-derived XMI declaration of prop along
        cls's ancestry — honors redefinition narrowing — or None."""
        candidates = [cls] + sorted(ancestors(cls, set()),
                                    key=lambda a: -topo_index[a])
        for c in candidates:
            if merged[c]["attrs"].get(prop) is not None:
                return c
        return None

    def nearest_type(cls, prop):
        c = nearest_declaration(cls, prop)
        return resolve_type(merged[c]["attrs"][prop]) if c else (None, None)

    skipped_redefs = set()

    def members(refs):
        """Raw redefinition references -> sorted "Class::member" names; a
        reference to anything but a class member (an association end) is
        skipped and counted."""
        out = set()
        for ref in refs:
            fkey, xid = ref_key(ref)
            hit = member_maps[fkey].get(xid)
            if hit is None:
                skipped_redefs.add(xid)
            else:
                out.add(f"{hit[0]}::{hit[1]}")
        return sorted(out)

    applied_errata = []

    def returns_of(cls, op):
        """The operation's return type: from the XMI, or from XMI_ERRATA where the XMI has none."""
        fix = XMI_ERRATA.get((cls, op["name"]), {}).get("returns")
        if fix and op["return_ref"]:
            sys.exit(f"{cls}::{op['name']}: the XMI now has a return parameter; drop its XMI_ERRATA row")
        if fix:
            applied_errata.append(f"{cls}::{op['name']}")
            return dict(fix)
        return typed(op["return_ref"]) if op["return_ref"] else None

    def typed(ref_many):
        ref, many = ref_many
        tname, tkind = resolve_type(ref) if ref else (None, None)
        return {"type": tname, "kind": tkind, "many": many}

    shapes = parse_schema_props(REPO / "crates/sysmlv2-model/src/schema_props.rs")
    derived, xmi_abstract = parse_xmi_props(
        REPO / "crates/sysmlv2-testkit/src/xmi_props.rs"
    )
    classes = {}
    untyped, shape_mismatch = [], []
    for name, info in merged.items():
        props = {}
        for p, shape in shapes.get(name, {}).items():
            is_derived = derived.get(name, {}).get(p, False)
            tname, tkind = nearest_type(name, p)
            if tname is None or tkind is None:
                untyped.append(f"{name}.{p}")
            elif shape == "B" and tname != "Boolean":
                shape_mismatch.append(f"{name}.{p}: shape B but type {tname}")
            decl = nearest_declaration(name, p)
            props[p] = {
                "shape": shape,
                "derived": is_derived,
                "type": tname,
                "kind": tkind,
                "redefines": members(merged[decl]["attr_redefs"].get(p, [])) if decl else [],
            }
        classes[name] = {
            "abstract": xmi_abstract.get(name, info["abstract"]),
            "bases": base_names(info),
            # defining spec layer = the XMI that declares the uml:Class
            # (the sets are disjoint — guarded above)
            "language": "kerml" if name in k_classes else "sysml",
            "props": props,
            "ops": [
                {
                    "name": op["name"],
                    "params": op["params"],
                    "paramTypes": [typed(r) for r in op["param_refs"]],
                    "returns": returns_of(name, op),
                    "redefines": members(op["redefs"]),
                }
                for op in info["ops"]
            ],
        }
    if untyped:
        sys.exit(
            f"type column incomplete — {len(untyped)} rows with no XMI "
            f"declaration found: {untyped[:20]}"
        )
    if shape_mismatch:
        sys.exit("shape/type inconsistency:\n  " + "\n  ".join(shape_mismatch[:20]))

    pin = json.loads(
        (HERE / "vendor" / "sysml-toolkit" / "PIN.json").read_text(encoding="utf-8")
    )
    missing = sorted(f"{c}::{o}" for c, o in XMI_ERRATA if f"{c}::{o}" not in applied_errata)
    if missing:
        sys.exit(f"XMI_ERRATA rows that match no operation: {missing}")
    meta = {
        "table": TABLE_VERSION,
        "xmiErrata": sorted(applied_errata),
        "specRefs": SPEC_REFS,
        "toolkitCommit": pin["sourceCommit"],
    }
    out = HERE / "metamodel.json"
    out.write_text(
        json.dumps({"classes": classes, "meta": meta}, indent=1, sort_keys=True),
        encoding="utf-8", newline="\n",
    )
    n_conc = sum(1 for c in classes.values() if not c["abstract"])
    n_props = sum(len(c["props"]) for c in classes.values())
    print(f"classes: {len(classes)} ({n_conc} concrete), property rows: {n_props}")
    n_redef = sum(1 for c in classes.values() for m in c["props"].values() if m["redefines"])
    print(f"properties with redefinitions: {n_redef}; redefinition targets skipped "
          f"(not class members): {len(skipped_redefs)}")
    roots = [n for n, c in classes.items() if not c["bases"]]
    print("hierarchy roots:", roots)
    missing_bases = sorted(
        {b for c in classes.values() for b in c["bases"] if b not in classes}
    )
    if missing_bases:
        print("WARNING unresolved bases:", missing_bases, file=sys.stderr)


if __name__ == "__main__":
    main()
