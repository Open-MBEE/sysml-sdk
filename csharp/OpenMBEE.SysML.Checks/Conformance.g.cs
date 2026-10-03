// Generated — do not edit. Source: metamodel.json via tools/gen_conformance.py.
//
// Anti-drift: TableSha256 pins the table this file was generated from;
// the checks verify the generated interfaces + runtime against the
// live table via reflection. Invoked by Program.cs.
#nullable enable

using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using OpenMBEE.SysML;

namespace OpenMBEE.SysML.Checks;

public static class Conformance
{
    public const string TableSha256 = "17644f3a5d70189d76b5ecb95c2089aeda1f93b6705f8bce5eeae1faf023758b";

    private static void Check(bool cond, string what)
    {
        if (!cond)
            throw new Exception("conformance FAILED: " + what);
    }

    private static void Ancestors(JsonElement mm, string name, HashSet<string> acc)
    {
        foreach (var b in mm.GetProperty(name).GetProperty("bases").EnumerateArray())
        {
            var bn = b.GetString()!;
            if (acc.Add(bn))
                Ancestors(mm, bn, acc);
        }
    }

    // Every operation a class declares or inherits: name to parameter count.
    private static SortedDictionary<string, int> FlatOps(JsonElement mm, string name)
    {
        var classes = new HashSet<string> { name };
        Ancestors(mm, name, classes);
        var ops = new SortedDictionary<string, int>(StringComparer.Ordinal);
        foreach (var c in classes)
            foreach (var op in mm.GetProperty(c).GetProperty("ops").EnumerateArray())
                ops[op.GetProperty("name").GetString()!] = op.GetProperty("params").GetArrayLength();
        return ops;
    }

    // Invoke a generated default interface member; unwrap reflection's
    // TargetInvocationException so SDK exceptions stay visible.
    private static void Invoke(Func<object?> f)
    {
        try { f(); }
        catch (TargetInvocationException e) when (e.InnerException is not null)
        {
            throw e.InnerException;
        }
    }

    public static void Run(string metamodelPath)
    {
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
        {
            var info = mm.GetProperty(n);
            Check(ClassMap.TypeMap.ContainsKey(n), $"TypeMap[{n}] missing");
            var t = ClassMap.TypeMap[n];
            Check(ClassMap.Factories.ContainsKey(n) ==
                !info.GetProperty("abstract").GetBoolean(),
                $"factory presence for {n}");
            foreach (var b in info.GetProperty("bases").EnumerateArray())
                Check(ClassMap.TypeMap[b.GetString()!].IsAssignableFrom(t),
                    $"{n} !< {b.GetString()}");
            if (n == "Element")
                continue; // spec root merges into the runtime IElement
            var props = info.GetProperty("props");
            foreach (var p in props.EnumerateObject())
                Check(t.GetProperty(p.Name) is not null,
                    $"{n}.{p.Name}: member missing");
            foreach (var (o, arity) in FlatOps(mm, n))
            {
                var mi = t.GetMethod(Member(o));
                Check(mi is not null, $"{n}.{Member(o)}(): stub missing");
                Check(mi!.GetParameters().Length == arity,
                    $"{n}.{Member(o)}(): {mi.GetParameters().Length} parameters, table says {arity}");
            }
        }

        // --- runtime honors the table -------------------------------------
        var sb = new StringBuilder("[");
        for (var i = 0; i < concrete.Count; i++)
            sb.Append(i == 0 ? "" : ",")
              .Append($"{{\"@id\":\"e-{concrete[i]}\",\"@type\":\"{concrete[i]}\"}}");
        var payload = sb.Append(']').ToString();
        var model = Model.FromFullJson(payload);
        foreach (var n in concrete)
        {
            var el = model.Element($"e-{n}");
            var t = ClassMap.TypeMap[n];
            Check(el.MetaclassName == n, $"MetaclassName {n}");
            Check(el.ElementId == $"e-{n}", $"ElementId {n}");
            Check(t.IsInstanceOfType(el), $"{n} instance of own interface");
            var anc = new HashSet<string>();
            Ancestors(mm, n, anc);
            foreach (var a in anc)
                Check(ClassMap.TypeMap[a].IsInstanceOfType(el),
                    $"{n} not instance of {a}");
            var props = mm.GetProperty(n).GetProperty("props");
            foreach (var p in props.EnumerateObject())
            {
                var pi = t.GetProperty(p.Name)!;
                // a property read never second-guesses the backend
                Invoke(() => pi.GetValue(el));
            }
            foreach (var o in FlatOps(mm, n).Keys)
            {
                var mi = t.GetMethod(Member(o))!;
                var threw = false;
                try
                {
                    Invoke(() => mi.Invoke(el,
                        new object?[mi.GetParameters().Length]));
                }
                catch (NotImplementedInToolkitException) { threw = true; }
                Check(threw, $"{n}.{Member(o)}(): must throw NotImplementedInToolkit");
            }
        }

        Console.WriteLine($"conformance OK: {names.Count} classes, " +
            $"{concrete.Count} concrete, table {TableSha256[..12]}");
    }
}
