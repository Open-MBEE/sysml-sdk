"""Compare what the SysML Toolkit and the Pilot Implementation answer for one model.

Usage: python tools/compare_backends.py <model.sysml> <out.json> [--library DIR] [--transport process|jpype]

The SysML Toolkit reads the model with the standard library in DIR (default: the one this package
carries); the Pilot with its own. Nothing here decides which answer is right: the result counts
where the two agree and where they differ, with examples of each difference.

Elements are paired by a content key: the qualified name; for an explicit relationship, its
owner's key and its position among the owner's *explicit* relationships (so implied relationships
one side adds do not shift it); for an implied relationship, its metaclass, sources and targets; for
any other unnamed element, its owning relationship's key and its position among that relationship's
owned related elements. Without implied relationships this is the specification's `path()`.

Every property the SDK declares is read through both on every paired element of the same metaclass,
and lists are compared twice, in order and as multisets. Outcomes:

  agree           the same value
  orderOnly       the same values in another order (an ordering difference)
  impliedOnly     the values differ only by implied relationships present on one side
  differ          any other difference
  bothEmpty       neither has a value
  refused...      the SysML Toolkit refused the read (split by whether the Pilot has a value)
  pilotError      the Pilot failed
  ids             element ids, set aside (each implementation mints its own)

For every paired explicit relationship the specification's `path()` (its position, implied
relationships counted) is compared too; a position shifted only by implied relationships is
reported apart from explicit relationships in another order.
"""
from __future__ import annotations

import argparse
import collections
import json
import sys
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT / "python"))

from sysml import BackendError, Model, NotImplementedInToolkit, SdkError, standard_library  # noqa: E402
from sysml.base import P  # noqa: E402
from sysml.pilot import PilotBackend  # noqa: E402
from sysml.toolkit import ToolkitBackend  # noqa: E402

IDS = {"elementId", "memberElementId", "ownedMemberElementId"}


def props_of(klass) -> dict[str, P]:
    seen: dict[str, P] = {}
    for c in klass.__mro__:
        for name, v in vars(c).items():
            if isinstance(v, P) and name not in seen:
                seen[name] = v
    return seen


class Keys:
    """Content keys (and the specification's path()) of one backend's elements."""

    def __init__(self, model: Model):
        self.m, self.b = model, model._backend
        self.key_cache: dict = {}
        self.path_cache: dict = {}
        self.busy: set = set()

    def get(self, h, prop):
        try:
            return self.b.get(h, prop)
        except SdkError:
            return None

    @staticmethod
    def ids(v):
        return [x["@id"] for x in (v or []) if isinstance(x, dict) and "@id" in x]

    def is_rel(self, h):
        return any(c.__name__ == "Relationship" for c in type(self.m.element(h)).__mro__)

    def implied(self, h):
        return bool(self.get(h, "isImplied"))

    def key(self, h):
        if h in self.key_cache:
            return self.key_cache[h]
        if h in self.busy:
            return f"<cycle {self.b.metaclass(h)}>"
        self.busy.add(h)
        try:
            k = self._key(h)
        finally:
            self.busy.discard(h)
        self.key_cache[h] = k
        return k

    def _key(self, h):
        qn = self.get(h, "qualifiedName")
        if qn:
            return qn
        mc = self.b.metaclass(h)
        if self.is_rel(h):
            owned = self.ids(self.get(h, "ownedRelatedElement"))
            if mc == "OwningMembership" and owned:
                oqn = self.get(owned[0], "qualifiedName")
                if oqn:
                    return oqn + "/owningMembership"
            if self.implied(h):
                def end(x):
                    if x in owned:
                        return f"{self.b.metaclass(x)}:{self.get(x, 'declaredName') or ''}"
                    return self.key(x)
                src = ",".join(end(x) for x in self.ids(self.get(h, "source")))
                tgt = ",".join(end(x) for x in self.ids(self.get(h, "target")))
                return f"implied {mc}({src} -> {tgt})"
            if self.get(h, "owningRelationship") is None:
                owner = self.get(h, "owningRelatedElement")
                if owner is not None:
                    owner = owner["@id"]
                    explicit = [r for r in self.ids(self.get(owner, "ownedRelationship")) if not self.implied(r)]
                    if h in explicit:
                        return f"{self.key(owner)}/{explicit.index(h) + 1}"
                    return None
        rel = self.get(h, "owningRelationship")
        if rel is None:
            return ""
        rel = rel["@id"]
        owned = self.ids(self.get(rel, "ownedRelatedElement"))
        if h not in owned:
            return None
        return f"{self.key(rel)}/{owned.index(h) + 1}"

    def path(self, h):
        """The specification's path(): positions count every relationship, implied ones included."""
        if h in self.path_cache:
            return self.path_cache[h]
        qn = self.get(h, "qualifiedName")
        p = None
        if qn:
            p = qn
        elif self.is_rel(h) and self.get(h, "owningRelationship") is None and self.get(h, "owningRelatedElement"):
            owner = self.get(h, "owningRelatedElement")["@id"]
            rels = self.ids(self.get(owner, "ownedRelationship"))
            p = f"{self.path(owner)}/{rels.index(h) + 1}" if h in rels else None
        else:
            rel = self.get(h, "owningRelationship")
            if rel is not None:
                owned = self.ids(self.get(rel["@id"], "ownedRelatedElement"))
                p = f"{self.path(rel['@id'])}/{owned.index(h) + 1}" if h in owned else None
            else:
                p = ""
        self.path_cache[h] = p
        return p


def norm(keys: Keys, raw):
    if isinstance(raw, dict) and set(raw) == {"@id"}:
        return keys.key(raw["@id"])
    if isinstance(raw, list):
        return [norm(keys, x) for x in raw]
    return raw


def empty(v):
    return v is None or v == [] or v == ""


def main() -> None:
    ap = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    ap.add_argument("model")
    ap.add_argument("out")
    ap.add_argument("--library", help="the standard library directory the SysML Toolkit reads (default: the package's)")
    ap.add_argument("--transport", default=None, help="how the Pilot runs: process or jpype (default: jpype when installed)")
    a = ap.parse_args()

    t0 = time.time()
    tk = ToolkitBackend.open([a.model], library_dir=a.library or standard_library())
    pl = PilotBackend.open([a.model], transport=a.transport)
    t1 = time.time()
    mt, mp = Model(tk), Model(pl)
    kt, kp = Keys(mt), Keys(mp)
    tk_by_key: dict = {}
    for h in tk.all_handles():
        k = kt.key(h)
        if k:
            tk_by_key.setdefault(k, h)
    pl_by_key: dict = {}
    for h in pl.all_handles():
        k = kp.key(h)
        if k:
            pl_by_key.setdefault(k, h)
    common = sorted(set(tk_by_key) & set(pl_by_key))
    t2 = time.time()

    outcomes = collections.defaultdict(collections.Counter)
    examples = collections.defaultdict(list)
    order_examples = collections.defaultdict(list)
    metaclass_differ = []
    positions = collections.Counter()
    position_examples = collections.defaultdict(list)
    for k in common:
        ht, hp = tk_by_key[k], pl_by_key[k]
        mct, mcp = tk.metaclass(ht), pl.metaclass(hp)
        if mct != mcp:
            metaclass_differ.append((k, mct, mcp))
            continue
        if kt.is_rel(ht) and not kt.implied(ht):
            pa, pb = kt.path(ht), kp.path(hp)
            if pa == pb:
                positions["same"] += 1
            else:
                owner_t, owner_p = kt.get(ht, "owningRelatedElement"), kp.get(hp, "owningRelatedElement")
                kind = "shiftedByImplied"
                if owner_t and owner_p:
                    ex_t = [kt.key(r) for r in kt.ids(kt.get(owner_t["@id"], "ownedRelationship")) if not kt.implied(r)]
                    ex_p = [kp.key(r) for r in kp.ids(kp.get(owner_p["@id"], "ownedRelationship")) if not kp.implied(r)]
                    kind = "shiftedByImplied" if ex_t == ex_p else "explicitOrderDiffers"
                positions[kind] += 1
                if len(position_examples[kind]) < 5:
                    position_examples[kind].append({"relationship": k, "metaclass": mct, "toolkitPath": pa, "pilotPath": pb})
        for name in props_of(type(mt.element(ht))):
            if name in IDS:
                outcomes[name]["ids"] += 1
                continue
            try:
                vt, refused = norm(kt, tk.get(ht, name)), False
            except (NotImplementedInToolkit, SdkError):
                vt, refused = None, True
            try:
                vp = norm(kp, pl.get(hp, name))
            except (BackendError, SdkError):
                outcomes[name]["pilotError"] += 1
                continue
            if refused:
                o = "refusedPilotEmpty" if empty(vp) else "refusedPilotValue"
            elif empty(vt) and empty(vp):
                o = "bothEmpty"
            elif vt == vp:
                o = "agree"
            elif isinstance(vt, list) and isinstance(vp, list) and sorted(map(repr, vt)) == sorted(map(repr, vp)):
                o = "orderOnly"
                if len(order_examples[name]) < 3:
                    order_examples[name].append({"element": k, "metaclass": mct, "toolkit": vt, "pilot": vp})
            else:
                lt = vt if isinstance(vt, list) else [vt]
                lp = vp if isinstance(vp, list) else [vp]
                ct, cp = collections.Counter(map(repr, lt)), collections.Counter(map(repr, lp))
                only = list((ct - cp).elements()) + list((cp - ct).elements())
                o = "impliedOnly" if only and all(str(x).startswith("'implied ") for x in only) else "differ"
                if len(examples[(name, o)]) < 3:
                    examples[(name, o)].append({"element": k, "metaclass": mct, "toolkit": vt, "pilot": vp})
            outcomes[name][o] += 1
    t3 = time.time()
    pilot_version = pl.pilot_version
    pl.close()
    tk.close()

    implied_one_side = {"pilotOnly": collections.Counter(), "toolkitOnly": collections.Counter()}
    for side, mine, other in (("pilotOnly", pl_by_key, tk_by_key), ("toolkitOnly", tk_by_key, pl_by_key)):
        for k in sorted(set(mine) - set(other)):  # sorted: ties at the cut-off list the same way every run
            if k.startswith("implied "):
                implied_one_side[side][k.split("(")[0] + " -> " + k.rsplit("-> ", 1)[-1][:-1]] += 1

    totals = collections.Counter()
    for c in outcomes.values():
        totals.update(c)
    result = {
        "model": a.model,
        "pilot": pilot_version,
        "elements": {"toolkit": len(tk_by_key), "pilot": len(pl_by_key), "paired": len(common),
                     "toolkitOnly": len(set(tk_by_key) - set(pl_by_key)),
                     "pilotOnly": len(set(pl_by_key) - set(tk_by_key)), "metaclassDiffer": len(metaclass_differ)},
        "totals": dict(totals),
        "relationshipPositions": dict(positions),
        "seconds": {"open": round(t1 - t0, 1), "pairing": round(t2 - t1, 1), "compare": round(t3 - t2, 1)},
        "perProperty": {k: dict(v) for k, v in sorted(outcomes.items())},
        "examples": {f"{p}|{o}": v for (p, o), v in examples.items()},
        "orderExamples": order_examples,
        "positionExamples": position_examples,
        "impliedOneSide": {s: c.most_common(25) for s, c in implied_one_side.items()},
        "metaclassDifferExamples": metaclass_differ[:30],
        "toolkitOnlyExamples": sorted(set(tk_by_key) - set(pl_by_key))[:40],
    }
    Path(a.out).write_text(json.dumps(result, indent=1, default=str), encoding="utf-8")
    print(json.dumps({k: result[k] for k in ("elements", "totals", "relationshipPositions", "seconds")}, indent=1))
    for o in ("differ", "orderOnly", "impliedOnly"):
        worst = sorted(((v.get(o, 0), k) for k, v in outcomes.items() if v.get(o)), reverse=True)[:12]
        print(f"most {o}:", worst)


if __name__ == "__main__":
    main()
