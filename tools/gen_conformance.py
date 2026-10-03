"""Generate per-language conformance tests from metamodel.json.

The anti-drift mechanism shared by all language backends:
each generated harness pins the SHA-256 of the metamodel.json it was
generated from and then verifies the language's generated surface and
runtime against the live table, so

  - table changed, harness not regenerated  -> digest test fails;
  - surface regenerated or hand-edited out of sync with the table
    (guardrail: no hand edits to generated files) -> reflection fails.

Python, TS, C#, and Java harnesses read the table at runtime and loop
(all four reflect cheaply); C++ has no reflection, so its harness is
*unrolled* — explicit per-class assertions generated from the table
(the digest pin plays the same role in all five).

Every harness checks the member-naming rule of tools/naming.py from the
table itself, independently of the generator: each class has every property
under its reader (the specification name, or `get` + it in Java and C++) and
every operation it declares or inherits under its member name (the
specification name, with `_op` / `Op` where a property of the same name
exists, in Python, C# and TypeScript).

Emits: python/tests/test_conformance_generated.py, ts/conformance.generated.mjs,
Conformance.g.cs in the C# checks project, checks/Conformance.java in the Java
package, cpp/checks/conformance.g.cpp
"""

import hashlib
import json

from naming import (CS_CHECKS_PROJECT, CS_NAMESPACE, JAVA_PACKAGE, JAVA_SRC, MM, ROOT,
                    TS_RUNTIME_NAMES, flat_ops, getter, op_member)

HERE = ROOT
RAW = (HERE / "metamodel.json").read_bytes()
DIGEST = hashlib.sha256(RAW).hexdigest()

# mirror of tools/gen_classes_cpp.py naming (operation keyword rename set)
_CPP_RENAMES: set[str] = set()


def _cpp_esc(name: str) -> str:
    return f"{name}_" if name in _CPP_RENAMES else name


def _ancestors(name, acc):
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            _ancestors(b, acc)
    return acc


def gen_cpp() -> str:
    out = [
        "// " + HEADER,
        "//",
        "// Anti-drift: TABLE_SHA256 pins the table this file was generated",
        "// from; the checks are UNROLLED per-class assertions (C++ has no",
        "// reflection). Invoked by checks.cpp.",
        "#include <fstream>",
        "#include <iostream>",
        "#include <sstream>",
        "#include <stdexcept>",
        "#include <string>",
        "",
        "#include <sysml/classes.g.hpp>",
        "",
        '#include "sha256.hpp"',
        "",
        "namespace checks {",
        "",
        "using namespace sysml;",
        "",
        f'static const char* const TABLE_SHA256 = "{DIGEST}";',
        "",
        "static void require(bool cond, const std::string& what) {",
        "    if (!cond)",
        '        throw std::runtime_error("conformance FAILED: " + what);',
        "}",
        "",
        "#define EXPECT_NIK(expr, what)                                        \\",
        "    do {                                                              \\",
        "        bool threw_ = false;                                          \\",
        "        try {                                                         \\",
        "            (void)(expr);                                             \\",
        "        } catch (const ::sysml::not_implemented_in_toolkit&) { \\",
        "            threw_ = true;                                            \\",
        '        }                                                             \\',
        '        require(threw_, std::string(what) + ": op must throw");       \\',
        "    } while (0)",
        "",
    ]
    concrete = sorted(n for n, i in MM.items() if not i["abstract"])
    for name in concrete:
        info = MM[name]
        out.append(f"static void check_{name}(const Model& m) {{")
        out.append(f'    Element e = m.element("e-{name}");')
        out.append(f'    require(e.metaclass_name() == "{name}", "{name}: metaclass");')
        out.append(f'    require(e.element_id() == "e-{name}", "{name}: element_id");')
        for anc in sorted(_ancestors(name, set())):
            out.append(f'    require(e.is_kind("{anc}"), "{name} !< {anc}");')
        out.append(f"    auto t = e.as<{name}>();")
        for p in sorted(info["props"]):
            out.append(f"    (void)t.{getter(p)}();")
        for _, op in flat_ops(name):
            oname = op["name"]
            args = ", ".join("{}" for _ in op["params"])
            out.append(
                f'    EXPECT_NIK(t.{_cpp_esc(op_member(oname, "cpp"))}({args}), "{name}.{oname}()");'
            )
        out.append("}")
        out.append("")

    payload = ",".join(
        f'{{\\"@id\\":\\"e-{n}\\",\\"@type\\":\\"{n}\\"}}' for n in concrete
    )
    out.append("void run_conformance(const std::string& metamodel_path) {")
    out.append("    std::ifstream in(metamodel_path, std::ios::binary);")
    out.append('    require(in.good(), "cannot open " + metamodel_path);')
    out.append("    std::ostringstream buf;")
    out.append("    buf << in.rdbuf();")
    out.append("    require(sha256_hex(buf.str()) == TABLE_SHA256,")
    out.append('        "metamodel.json changed since this harness was generated; "')
    out.append('        "rerun tools/gen_conformance.py");')
    out.append(f'    const std::string payload = "[{payload}]";')
    out.append("    Model m = Model::from_full_json(payload);")
    for name in concrete:
        out.append(f"    check_{name}(m);")
    out.append(
        f'    std::cout << "conformance OK: {len(MM)} classes, '
        f'{len(concrete)} concrete, table " << std::string(TABLE_SHA256).substr(0, 12)'
    )
    out.append('              << "\\n";')
    out.append("}")
    out.append("")
    out.append("}  // namespace checks")
    out.append("")
    return "\n".join(out)

HEADER = (
    "Generated — do not edit. Source: metamodel.json via "
    "tools/gen_conformance.py."
)

PY = '''"""{header}

Anti-drift: TABLE_SHA256 pins the table this file was generated from;
the reflection tests verify the generated Python surface against the
live table. See tools/gen_conformance.py for the mechanism.
"""

import hashlib
import json
from pathlib import Path

import pytest

from sysml import REGISTRY, Model, NotImplementedInToolkit
from sysml import classes as C
from sysml.base import Element, Op, P

TABLE_SHA256 = "{digest}"
_RAW = (Path(__file__).resolve().parents[2] / "metamodel.json").read_bytes()
MM = json.loads(_RAW)["classes"]

# names the Python generator skips (mirror of tools/gen_classes.py)
_SKIP = {{"import", "class", "def", "return", "in", "for", "if", "else"}}


def _props(name):
    for p, meta in MM[name]["props"].items():
        if p not in _SKIP and p.isidentifier():
            yield p, meta


def _ancestors(name, acc=None):
    acc = set() if acc is None else acc
    for b in MM[name]["bases"]:
        if b not in acc:
            acc.add(b)
            _ancestors(b, acc)
    return acc


def _flat_ops(name):
    """Every operation name `name` declares or inherits."""
    return {{op["name"] for c in {{name}} | _ancestors(name) for op in MM[c]["ops"]}}


# an operation that shares its name with a property of a class that has both
_COLLIDING = {{o for n in MM for o in _flat_ops(n) if o in MM[n]["props"]}}


def _member(o):
    return o + "_op" if o in _COLLIDING else o


def _ops(name):
    """(spec name, member name, parameter names) of the operations `name` itself declares."""
    for op in MM[name]["ops"]:
        o = op["name"]
        if o and o.isidentifier():
            yield o, _member(o), op["params"]


CLASSES = sorted(n for n in MM if n != "Element")
CONCRETE = [n for n in CLASSES if not MM[n]["abstract"]]
MODEL = Model.from_full_json([{{"@id": f"e-{{n}}", "@type": n}} for n in CONCRETE])


def test_table_digest_matches_generated_harness():
    assert hashlib.sha256(_RAW).hexdigest() == TABLE_SHA256, (
        "metamodel.json changed since this harness was generated; "
        "rerun tools/gen_conformance.py"
    )


def test_registry_is_exactly_the_concrete_classes():
    assert sorted(REGISTRY) == CONCRETE


@pytest.mark.parametrize("name", CLASSES)
def test_class_matches_table(name):
    klass = getattr(C, name)
    info = MM[name]
    if not info["abstract"]:
        assert REGISTRY[name] is klass
    for b in info["bases"]:
        base = Element if b == "Element" else getattr(C, b)
        assert issubclass(klass, base), f"{{name}} !< {{b}}"
    own = vars(klass)
    for p, meta in _props(name):
        d = own.get(p)
        assert isinstance(d, P), f"{{name}}.{{p}}: descriptor missing"
        assert (d.shape, d.derived) == (meta["shape"], meta["derived"]), (
            f"{{name}}.{{p}}: metadata drift"
        )
    for o, m, params in _ops(name):
        assert isinstance(own.get(m), Op), f"{{name}}.{{m}}: op stub missing"
        assert own[m].name == o, f"{{name}}.{{m}}: names {{own[m].name}}"
        assert own[m].params == params, f"{{name}}.{{m}}: parameters {{own[m].params}}"


def test_element_operations_on_the_runtime_base():
    for o, m, _params in _ops("Element"):
        assert isinstance(vars(Element).get(m), Op), f"Element.{{m}}: op stub missing"


@pytest.mark.parametrize("name", CONCRETE)
def test_runtime_honors_table(name):
    w = MODEL.element(f"e-{{name}}")
    assert type(w) is REGISTRY[name]
    assert w.metaclass_name == name
    for anc in _ancestors(name):
        base = Element if anc == "Element" else getattr(C, anc)
        assert isinstance(w, base), f"{{name}} not instance of {{anc}}"
    for p, _meta in _props(name):
        getattr(w, p)  # a property read never second-guesses the backend
    for o in sorted(_flat_ops(name)):
        with pytest.raises(NotImplementedInToolkit):
            getattr(w, _member(o))()
'''

TS = '''// {header}
//
// Anti-drift: TABLE_SHA256 pins the table this file was generated from;
// the checks verify meta.mjs + the Proxy runtime against the live table.
// Run: node conformance.generated.mjs

import {{ createHash }} from "node:crypto";
import {{ readFileSync }} from "node:fs";
import {{ Model, NotImplementedInToolkit, is }} from "./runtime.mjs";
import {{ ABSTRACT, HIERARCHY, OPS, PROPS }} from "./meta.mjs";

const TABLE_SHA256 = "{digest}";
const raw = readFileSync(new URL("../metamodel.json", import.meta.url));
if (createHash("sha256").update(raw).digest("hex") !== TABLE_SHA256)
  throw new Error("metamodel.json changed since this harness was generated; " +
    "rerun tools/gen_conformance.py");
const MM = JSON.parse(raw.toString("utf-8")).classes;

const eq = (a, b, what) => {{
  if (JSON.stringify(a) !== JSON.stringify(b))
    throw new Error(`${{what}}: ${{JSON.stringify(a)}} != ${{JSON.stringify(b)}}`);
}};
const ancestors = (name, acc = new Set()) => {{
  for (const b of MM[name].bases)
    if (!acc.has(b)) {{ acc.add(b); ancestors(b, acc); }}
  return acc;
}};

const names = Object.keys(MM).sort();
const concrete = names.filter((n) => !MM[n].abstract);
// every operation name a class declares or inherits
const flatOps = (n) => new Set([n, ...ancestors(n)].flatMap((c) => MM[c].ops.map((o) => o.name)));
// an operation that shares its name with a property of a class that has both
const colliding = new Set(names.flatMap((n) => [...flatOps(n)].filter((o) => o in MM[n].props)));
// keys the element Proxy answers itself (SPECIAL in runtime.mjs)
const RUNTIME_NAMES = new Set({runtime_names});
const member = (o) => (colliding.has(o) || RUNTIME_NAMES.has(o) ? o + "Op" : o);

// --- meta.mjs matches the table -------------------------------------------
eq(ABSTRACT, names.filter((n) => MM[n].abstract), "ABSTRACT");
for (const n of names) {{
  eq(HIERARCHY[n], [...ancestors(n)].sort(), `HIERARCHY[${{n}}]`);
  const expected = Object.fromEntries(Object.entries(MM[n].props)
    .sort(([a], [b]) => (a < b ? -1 : 1))
    .map(([p, m]) => [p, [m.shape, m.derived ? 1 : 0]]));
  eq(PROPS[n] ?? {{}}, expected, `PROPS[${{n}}]`);
  const ops = Object.fromEntries(Object.entries(OPS[n] ?? {{}}).map(([m, [, o]]) => [m, o]));
  eq(ops, Object.fromEntries([...flatOps(n)].sort().map((o) => [member(o), o])
    .sort(([a], [b]) => (a < b ? -1 : 1))), `OPS[${{n}}]`);
}}

// --- Proxy runtime honors the table ---------------------------------------
const payload = concrete.map((n) => ({{ "@id": `e-${{n}}`, "@type": n }}));
const model = Model.fromFullJson(payload);
for (const n of concrete) {{
  const el = model.element(`e-${{n}}`);
  if (el.$metaclass !== n) throw new Error(`$metaclass ${{n}}`);
  if (el.$id !== `e-${{n}}`) throw new Error(`$id ${{n}}`);
  if (!is(el, n)) throw new Error(`is(self) ${{n}}`);
  for (const anc of ancestors(n))
    if (!is(el, anc)) throw new Error(`is(${{n}}, ${{anc}})`);
  for (const p of Object.keys(MM[n].props))
    el[p]; // a property read never second-guesses the backend
  for (const o of flatOps(n)) {{
    const f = el[member(o)];
    if (typeof f !== "function") throw new Error(`${{n}}.${{member(o)}}: operation missing`);
    let threw = false;
    try {{ f(); }} catch (e) {{ threw = e instanceof NotImplementedInToolkit; }}
    if (!threw) throw new Error(`${{n}}.${{member(o)}}(): must throw NotImplementedInToolkit`);
  }}
  let assigned = false;
  try {{ el.declaredName = "x"; assigned = true; }} catch {{}}
  if (assigned) throw new Error(`${{n}}: assignment must throw`);
}}

console.log(`conformance OK: ${{names.length}} classes, ` +
  `${{concrete.length}} concrete, table ${{TABLE_SHA256.slice(0, 12)}}`);
'''


CS = '''// {header}
//
// Anti-drift: TableSha256 pins the table this file was generated from;
// the checks verify the generated interfaces + runtime against the
// live table via reflection. Invoked by Program.cs.
#nullable enable

using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using __CSNS__;

namespace __CSNS__.Checks;

public static class Conformance
{{
    public const string TableSha256 = "{digest}";

    private static void Check(bool cond, string what)
    {{
        if (!cond)
            throw new Exception("conformance FAILED: " + what);
    }}

    private static void Ancestors(JsonElement mm, string name, HashSet<string> acc)
    {{
        foreach (var b in mm.GetProperty(name).GetProperty("bases").EnumerateArray())
        {{
            var bn = b.GetString()!;
            if (acc.Add(bn))
                Ancestors(mm, bn, acc);
        }}
    }}

    // Every operation a class declares or inherits: name to parameter count.
    private static SortedDictionary<string, int> FlatOps(JsonElement mm, string name)
    {{
        var classes = new HashSet<string> {{ name }};
        Ancestors(mm, name, classes);
        var ops = new SortedDictionary<string, int>(StringComparer.Ordinal);
        foreach (var c in classes)
            foreach (var op in mm.GetProperty(c).GetProperty("ops").EnumerateArray())
                ops[op.GetProperty("name").GetString()!] = op.GetProperty("params").GetArrayLength();
        return ops;
    }}

    // Invoke a generated default interface member; unwrap reflection's
    // TargetInvocationException so SDK exceptions stay visible.
    private static void Invoke(Func<object?> f)
    {{
        try {{ f(); }}
        catch (TargetInvocationException e) when (e.InnerException is not null)
        {{
            throw e.InnerException;
        }}
    }}

    public static void Run(string metamodelPath)
    {{
        var raw = File.ReadAllBytes(metamodelPath);
        var digest = Convert.ToHexString(SHA256.HashData(raw)).ToLowerInvariant();
        Check(digest == TableSha256,
            "metamodel.json changed since this harness was generated; " +
            "rerun tools/gen_conformance.py");
        using var doc = JsonDocument.Parse(raw);
        var mm = doc.RootElement.GetProperty("classes");

        var names = new List<string>();
        foreach (var c in mm.EnumerateObject())
            names.Add(c.Name);
        names.Sort(StringComparer.Ordinal);
        var concrete = names
            .Where(n => !mm.GetProperty(n).GetProperty("abstract").GetBoolean())
            .ToList();
        // an operation that shares its name with a property of a class that has both
        var colliding = new HashSet<string>();
        foreach (var n in names)
            foreach (var o in FlatOps(mm, n).Keys)
                if (mm.GetProperty(n).GetProperty("props").TryGetProperty(o, out _))
                    colliding.Add(o);
        string Member(string o) => colliding.Contains(o) ? o + "Op" : o;

        // --- generated surface matches the table --------------------------
        foreach (var n in names)
        {{
            var info = mm.GetProperty(n);
            Check(ClassMap.TypeMap.ContainsKey(n), $"TypeMap[{{n}}] missing");
            var t = ClassMap.TypeMap[n];
            Check(ClassMap.Factories.ContainsKey(n) ==
                !info.GetProperty("abstract").GetBoolean(),
                $"factory presence for {{n}}");
            foreach (var b in info.GetProperty("bases").EnumerateArray())
                Check(ClassMap.TypeMap[b.GetString()!].IsAssignableFrom(t),
                    $"{{n}} !< {{b.GetString()}}");
            if (n == "Element")
                continue; // spec root merges into the runtime IElement
            var props = info.GetProperty("props");
            foreach (var p in props.EnumerateObject())
                Check(t.GetProperty(p.Name) is not null,
                    $"{{n}}.{{p.Name}}: member missing");
            foreach (var (o, arity) in FlatOps(mm, n))
            {{
                var mi = t.GetMethod(Member(o));
                Check(mi is not null, $"{{n}}.{{Member(o)}}(): stub missing");
                Check(mi!.GetParameters().Length == arity,
                    $"{{n}}.{{Member(o)}}(): {{mi.GetParameters().Length}} parameters, table says {{arity}}");
            }}
        }}

        // --- runtime honors the table -------------------------------------
        var sb = new StringBuilder("[");
        for (var i = 0; i < concrete.Count; i++)
            sb.Append(i == 0 ? "" : ",")
              .Append($"{{{{\\"@id\\":\\"e-{{concrete[i]}}\\",\\"@type\\":\\"{{concrete[i]}}\\"}}}}");
        var payload = sb.Append(']').ToString();
        var model = Model.FromFullJson(payload);
        foreach (var n in concrete)
        {{
            var el = model.Element($"e-{{n}}");
            var t = ClassMap.TypeMap[n];
            Check(el.MetaclassName == n, $"MetaclassName {{n}}");
            Check(el.ElementId == $"e-{{n}}", $"ElementId {{n}}");
            Check(t.IsInstanceOfType(el), $"{{n}} instance of own interface");
            var anc = new HashSet<string>();
            Ancestors(mm, n, anc);
            foreach (var a in anc)
                Check(ClassMap.TypeMap[a].IsInstanceOfType(el),
                    $"{{n}} not instance of {{a}}");
            var props = mm.GetProperty(n).GetProperty("props");
            foreach (var p in props.EnumerateObject())
            {{
                var pi = t.GetProperty(p.Name)!;
                // a property read never second-guesses the backend
                Invoke(() => pi.GetValue(el));
            }}
            foreach (var o in FlatOps(mm, n).Keys)
            {{
                var mi = t.GetMethod(Member(o))!;
                var threw = false;
                try
                {{
                    Invoke(() => mi.Invoke(el,
                        new object?[mi.GetParameters().Length]));
                }}
                catch (NotImplementedInToolkitException) {{ threw = true; }}
                Check(threw, $"{{n}}.{{Member(o)}}(): must throw NotImplementedInToolkit");
            }}
        }}

        Console.WriteLine($"conformance OK: {{names.Count}} classes, " +
            $"{{concrete.Count}} concrete, table {{TableSha256[..12]}}");
    }}
}}
'''


JAVA = '''// __HEADER__
//
// Anti-drift: TABLE_SHA256 pins the table this file was generated from;
// the checks verify the generated interfaces + runtime against the live
// table via reflection. Invoked by Checks.java.
package __JAVAPKG__.checks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.TreeMap;

import __JAVAPKG__.Element;
import __JAVAPKG__.Json;
import __JAVAPKG__.Model;
import __JAVAPKG__.NotImplementedInToolkitException;
import __JAVAPKG__.classes.ClassMap;

public final class Conformance {
    public static final String TABLE_SHA256 = "__DIGEST__";

    private Conformance() { }

    private static void check(boolean cond, String what) {
        if (!cond)
            throw new AssertionError("conformance FAILED: " + what);
    }

    private static void ancestors(Map<?, ?> mm, String name, Set<String> acc) {
        for (Object b : (List<?>) ((Map<?, ?>) mm.get(name)).get("bases"))
            if (acc.add((String) b))
                ancestors(mm, (String) b, acc);
    }

    // Every operation a class declares or inherits: name to parameter count.
    private static Map<String, Integer> flatOps(Map<?, ?> mm, String name) {
        Set<String> classes = new HashSet<>(List.of(name));
        ancestors(mm, name, classes);
        Map<String, Integer> ops = new TreeMap<>();
        for (String c : classes)
            for (Object opo : (List<?>) ((Map<?, ?>) mm.get(c)).get("ops"))
                ops.put((String) ((Map<?, ?>) opo).get("name"),
                    ((List<?>) ((Map<?, ?>) opo).get("params")).size());
        return ops;
    }

    // Properties are read through `get` + the specification name.
    private static String getter(String prop) {
        return "get" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
    }

    // Invoke a generated default method; unwrap reflection's
    // InvocationTargetException so SDK exceptions stay visible.
    private static void invoke(Method m, Object target, Object[] args)
            throws Exception {
        try {
            m.invoke(target, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception cause)
                throw cause;
            throw e;
        }
    }

    public static void run(Path metamodelPath) throws Exception {
        byte[] raw = Files.readAllBytes(metamodelPath);
        String digest = HexFormat.of()
            .formatHex(MessageDigest.getInstance("SHA-256").digest(raw));
        check(digest.equals(TABLE_SHA256),
            "metamodel.json changed since this harness was generated; "
            + "rerun tools/gen_conformance.py");
        Map<?, ?> mm = (Map<?, ?>) ((Map<?, ?>) Json
            .parse(new String(raw, StandardCharsets.UTF_8))).get("classes");

        List<String> names = new ArrayList<>();
        for (Object k : mm.keySet())
            names.add((String) k);
        names.sort(null);
        List<String> concrete = new ArrayList<>();
        for (String n : names)
            if (!(Boolean) ((Map<?, ?>) mm.get(n)).get("abstract"))
                concrete.add(n);

        // --- generated surface matches the table --------------------------
        for (String n : names) {
            Map<?, ?> info = (Map<?, ?>) mm.get(n);
            var t = ClassMap.TYPE_MAP.get(n);
            check(t != null, "TYPE_MAP[" + n + "] missing");
            check(ClassMap.FACTORIES.containsKey(n)
                == !(Boolean) info.get("abstract"), "factory presence for " + n);
            for (Object b : (List<?>) info.get("bases"))
                check(ClassMap.TYPE_MAP.get((String) b).isAssignableFrom(t),
                    n + " !< " + b);
            if (n.equals("Element"))
                continue; // spec root merges into the runtime Element
            Set<String> declared = new HashSet<>();
            for (Method m : t.getDeclaredMethods())
                declared.add(m.getName());
            Map<?, ?> props = (Map<?, ?>) info.get("props");
            for (Object p : props.keySet())
                check(declared.contains(getter((String) p)),
                    n + "." + getter((String) p) + "(): property getter missing");
            for (String o : flatOps(mm, n).keySet())
                check(declared.contains(o), n + "." + o + "(): operation missing");
        }

        // --- runtime honors the table -------------------------------------
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < concrete.size(); i++)
            sb.append(i == 0 ? "" : ",")
              .append("{\\"@id\\":\\"e-").append(concrete.get(i))
              .append("\\",\\"@type\\":\\"").append(concrete.get(i)).append("\\"}");
        String payload = sb.append(']').toString();
        Model model = Model.fromFullJson(payload);
        for (String n : concrete) {
            Element el = model.element("e-" + n);
            var t = ClassMap.TYPE_MAP.get(n);
            check(el.$metaclass().equals(n), "$metaclass " + n);
            check(el.$id().equals("e-" + n), "$id " + n);
            check(t.isInstance(el), n + " instance of own interface");
            Set<String> anc = new HashSet<>();
            ancestors(mm, n, anc);
            for (String a : anc)
                check(ClassMap.TYPE_MAP.get(a).isInstance(el),
                    n + " not instance of " + a);
            Map<?, ?> props = (Map<?, ?>) ((Map<?, ?>) mm.get(n)).get("props");
            for (Object po : props.keySet()) {
                Method read = t.getMethod(getter((String) po));
                // a property read never second-guesses the backend
                invoke(read, el, new Object[0]);
            }
            for (Map.Entry<String, Integer> entry : flatOps(mm, n).entrySet()) {
                String o = entry.getKey();
                int arity = entry.getValue();
                Method op = null;
                for (Method m : t.getMethods())
                    if (m.getName().equals(o) && m.getParameterCount() == arity) {
                        op = m;
                        break;
                    }
                check(op != null, n + "." + o + "(): not found");
                boolean threw = false;
                try {
                    invoke(op, el, new Object[arity]);
                } catch (NotImplementedInToolkitException e) {
                    threw = true;
                }
                check(threw, n + "." + o + "(): must throw NotImplementedInToolkit");
            }
        }

        System.out.println("conformance OK: " + names.size() + " classes, "
            + concrete.size() + " concrete, table " + TABLE_SHA256.substring(0, 12));
    }
}
'''


def main():
    py = PY.format(header=HEADER, digest=DIGEST)
    ts = TS.format(header=HEADER, digest=DIGEST, runtime_names=json.dumps(TS_RUNTIME_NAMES))
    cs = CS.format(header=HEADER, digest=DIGEST).replace("__CSNS__", CS_NAMESPACE)
    java = (JAVA.replace("__HEADER__", HEADER).replace("__DIGEST__", DIGEST)
            .replace("__JAVAPKG__", JAVA_PACKAGE))
    (HERE / "python" / "tests" / "test_conformance_generated.py").write_text(
        py, encoding="utf-8", newline="\n")
    (HERE / "ts" / "conformance.generated.mjs").write_text(
        ts, encoding="utf-8", newline="\n")
    (CS_CHECKS_PROJECT / "Conformance.g.cs").write_text(cs, encoding="utf-8", newline="\n")
    (JAVA_SRC / "checks" / "Conformance.java").write_text(java, encoding="utf-8", newline="\n")
    (HERE / "cpp" / "checks" / "conformance.g.cpp").write_text(
        gen_cpp(), encoding="utf-8", newline="\n")
    print(f"wrote conformance harnesses (table sha256 {DIGEST[:12]}…)")


if __name__ == "__main__":
    main()
