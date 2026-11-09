// The convenience members' examples (convenience.json) through C#, on the SysML Toolkit and on the
// Pilot Implementation: every example's member is called on its element and its answer compared with
// the example's, as python/tests/convenience_check.py does. Answers must agree; what a backend refuses
// is counted (the Python test pins the counts; the bodies are the binding library's and the Pilot
// engine's). Each backend skips itself when it is not at hand (the binding library and the release
// library; a Pilot and its engine, PilotChecks.WhyNot); SYSML_REQUIRE_TOOLKIT=1 makes the SysML
// Toolkit's skip a failure, SYSML_REQUIRE_PILOT=1 the Pilot's.
using System.Reflection;
using System.Text.Json;
using OpenMBEE.SysML;

namespace OpenMBEE.SysML.Checks;

public static class ConvenienceChecks
{
    public static void RunIfAvailable(string root)
    {
        using var doc = JsonDocument.Parse(File.ReadAllText(Path.Combine(root, "convenience.json")));
        var table = doc.RootElement;
        var records = new Dictionary<string, JsonElement>();
        foreach (var m in table.GetProperty("members").EnumerateArray())
            records.TryAdd(m.GetProperty("name").GetString()!, m);
        var sources = new Dictionary<string, string>();
        foreach (var m in table.GetProperty("models").EnumerateObject())
            sources[m.Name + ".sysml"] = m.Value.GetProperty("text").GetString()!;
        var mismatches = new List<string>();

        if (ToolkitAtHand(root) is { } toolkit)
        {
            var be = ToolkitBackend.Open(new ToolkitBackend.Library(toolkit.Library), Array.Empty<string>(), sources, toolkit.LibraryDir);
            mismatches.AddRange(RunOn("the SysML Toolkit", "toolkit", new Model(be), records, table));
        }
        if (PilotChecks.WhyNot() is { } why)
        {
            Console.WriteLine("convenience checks on the Pilot Implementation skipped: " + why);
        }
        else
        {
            using var be = PilotBackend.OpenSources(sources, new PilotOptions { Heap = "2g" });
            mismatches.AddRange(RunOn("the Pilot Implementation", "pilot", new Model(be), records, table));
        }
        if (mismatches.Count > 0) throw new Exception("FAILED:\n" + string.Join("\n", mismatches));
    }

    /// <summary>Whether <paramref name="got"/> holds the items of <paramref name="want"/>, each matched once:
    /// in order, or in any order where the specification leaves the order open.</summary>
    private static bool SameItems(List<JsonElement> want, List<object?> got, Func<JsonElement, object?, bool> match, bool unordered)
    {
        if (want.Count != got.Count) return false;
        if (!unordered) return want.Select((w, i) => match(w, got[i])).All(b => b);
        var left = new List<object?>(got);
        foreach (var w in want)
        {
            var i = left.FindIndex(x => match(w, x));
            if (i < 0) return false;
            left.RemoveAt(i);
        }
        return true;
    }

    /// <summary>An example the table lists as deviating on this backend (<c>deviates</c>, by backend key): its
    /// mismatch is a known deviation; if it agrees after all, the listing is stale.</summary>
    private static (string Kind, string Detail) Deviation(JsonElement e, string key, (string Kind, string Detail) r)
    {
        if (!e.TryGetProperty("deviates", out var d) || !d.TryGetProperty(key, out var reason)) return r;
        if (r.Kind == "mismatch") return ("known deviation", $"{reason.GetString()} ({r.Detail})");
        if (r.Kind == "agrees") return ("mismatch", $"listed as deviating on {key} ({reason.GetString()}), but it agrees");
        return r;
    }

    private static List<string> RunOn(string backend, string key, Model model, Dictionary<string, JsonElement> records, JsonElement table)
    {
        var counts = new Dictionary<string, int> { ["agrees"] = 0, ["refused by the backend"] = 0, ["known deviation"] = 0, ["mismatch"] = 0 };
        var mismatches = new List<string>();
        foreach (var e in table.GetProperty("examples").EnumerateArray())
        {
            (string Kind, string Detail) r;
            try { r = Outcome(model, records, e); }
            catch (Exception x) { r = ("mismatch", $"{x.GetType().Name}: {x.Message} {x.InnerException?.Message}"); }
            r = Deviation(e, key, r);
            counts[r.Kind]++;
            if (r.Kind == "mismatch")
                mismatches.Add($"{backend}: {e.GetProperty("member")} at {e.GetProperty("at")}: {r.Detail}");
        }
        Console.WriteLine($"convenience examples on {backend} (C#): " + string.Join(", ", counts.Select(kv => $"{kv.Key}={kv.Value}")));
        mismatches.AddRange(ListChecks(backend, model, table));
        mismatches.AddRange(FindChecks(backend, model));
        return mismatches;
    }

    /// <summary>Model.Find (whatever the visibility; through inheritance when asked; quoted names) and
    /// Model.Resolve from a context (the specification's Namespace::resolve, which the SysML Toolkit
    /// refuses in places).</summary>
    private static List<string> FindChecks(string backend, Model model)
    {
        var problems = new List<string>();
        string? Qn(IElement? el) => el?.qualifiedName;
        void Expect(string what, string? got, string? want)
        {
            if (got != want) problems.Add($"{backend}: {what}: {got}, not {want}");
        }
        Expect("find Base::b", Qn(model.Find("Scopes::Base::b")), "Scopes::Base::b");
        Expect("find Base::c", Qn(model.Find("Scopes::Base::c")), "Scopes::Base::c");
        Expect("find Derived::b", Qn(model.Find("Scopes::Derived::b")), null);
        Expect("find '~Plug'", model.Find("Typing::Plug::'~Plug'")?.MetaclassName, "ConjugatedPortDefinition");
        try
        {
            Expect("find Derived::b, inherited", Qn(model.Find("Scopes::Derived::b", inherited: true)), "Scopes::Base::b");
            Expect("find Derived::c, inherited", Qn(model.Find("Scopes::Derived::c", inherited: true)), null);
        }
        catch (NotAnsweredException)
        {
            // the backend does not answer inheritedMembership here
        }
        try
        {
            Expect("resolve b from Base", Qn(model.Resolve("b", (Namespace)model.Find("Scopes::Base")!)), "Scopes::Base::b");
            Expect("resolve Base::b from Derived", Qn(model.Resolve("Base::b", (Namespace)model.Find("Scopes::Derived")!)), null);
        }
        catch (NotAnsweredException)
        {
            // the SysML Toolkit refuses Namespace::resolve in places
        }
        Console.WriteLine($"find and resolve on {backend} (C#): " + (problems.Count == 0 ? "as expected" : $"{problems.Count} problems"));
        return problems;
    }

    /// <summary>Every member a list carries (element and string results), on the elements of the
    /// examples' models it applies to, through its extension method on IEnumerable of its context: the
    /// list's answer is the elements' answers flattened (list results concatenated, absent single results
    /// dropped), or the list refuses as an element does; on all of them, and on those of them that answer.
    /// The arguments are each member's first example's.</summary>
    private static List<string> ListChecks(string backend, Model model, JsonElement table)
    {
        var elements = model.All().ToList();
        var firstExample = new Dictionary<string, JsonElement>();
        foreach (var e in table.GetProperty("examples").EnumerateArray())
            firstExample.TryAdd(e.GetProperty("member").GetString()!, e);
        var counts = new Dictionary<string, int> { ["all compared"] = 0, ["all refused"] = 0, ["answering compared"] = 0 };
        var problems = new List<string>();
        foreach (var rec in table.GetProperty("members").EnumerateArray())
        {
            var returns = rec.GetProperty("returns");
            var single = !returns.GetProperty("many").GetBoolean();
            var strings = returns.GetProperty("type").GetString() == "String" && !single;
            if (returns.GetProperty("kind").GetString() != "element" && !strings) continue;
            var member = rec.GetProperty("name").GetString()!;
            var context = rec.GetProperty("on").GetString()!;
            var iface = context == "Element" ? typeof(IElement) : ClassMap.TypeMap[context];
            var applicable = elements.Where(iface.IsInstanceOfType).ToList();
            if (applicable.Count == 0) continue;
            var given = firstExample.TryGetValue(member, out var ex) && ex.TryGetProperty("args", out var a) ? a : default;
            var (types, values) = Arguments(model, rec, given);
            var spelled = char.ToUpperInvariant(member[0]) + member[1..];
            var elementMethod = (IElement el) => new[] { Iface(el) }.Concat(Iface(el).GetInterfaces())
                .Select(i => i.GetMethod(spelled, 0, types)).First(m => m is not null)!;
            var answers = new List<object?>();
            foreach (var el in applicable)
            {
                try { answers.Add(elementMethod(el).Invoke(el, values)); }
                catch (TargetInvocationException x) when (x.InnerException is NotAnsweredException n) { answers.Add(n); }
            }
            var ext = typeof(ElementLists).GetMethods()
                .First(m => m.Name == spelled && m.GetParameters()[0].ParameterType == typeof(IEnumerable<>).MakeGenericType(iface));
            counts["all " + CompareList(backend, member, ext, iface, applicable, answers, single, values, problems)]++;
            var answering = applicable.Where((_, i) => answers[i] is not NotAnsweredException).ToList();
            if (answering.Count > 0 && answering.Count < applicable.Count)
            {
                CompareList(backend, member, ext, iface, answering, answers.Where(x => x is not NotAnsweredException).ToList(), single, values, problems);
                counts["answering compared"]++;
            }
        }
        Console.WriteLine($"convenience lists on {backend} (C#): " + string.Join(", ", counts.Select(kv => $"{kv.Key}={kv.Value}")));
        return problems;
    }

    private static string CompareList(string backend, string member, MethodInfo ext, System.Type iface, List<IElement> xs,
                                      List<object?> answers, bool single, object?[] values, List<string> problems)
    {
        var typed = typeof(Enumerable).GetMethod(nameof(Enumerable.Cast))!.MakeGenericMethod(iface).Invoke(null, new object[] { xs });
        var refusal = answers.OfType<NotAnsweredException>().FirstOrDefault();
        object? got;
        try
        {
            got = ext.Invoke(null, new[] { typed }.Concat(values).ToArray());
        }
        catch (TargetInvocationException x) when (x.InnerException is NotAnsweredException n)
        {
            if (refusal is null || n.GetType() != refusal.GetType())
                problems.Add($"{backend}: {member}: the list refused ({n.Message})");
            return "refused";
        }
        if (refusal is not null)
        {
            problems.Add($"{backend}: {member}: an element refused ({refusal.Message}), the list answered");
            return "compared";
        }
        var want = new List<object?>();
        foreach (var answer in answers)
        {
            if (single) { if (answer is not null) want.Add(answer); }
            else want.AddRange(((System.Collections.IEnumerable)answer!).Cast<object?>());
        }
        var have = ((System.Collections.IEnumerable)got!).Cast<object?>().ToList();
        if (!have.SequenceEqual(want))
            problems.Add($"{backend}: {member}: {have.Count} items from the list, {want.Count} from the elements");
        return "compared";
    }

    /// <summary>The parameter types and the argument values of a member, from an example's arguments
    /// (by name) and the defaults.</summary>
    private static (System.Type[], object?[]) Arguments(Model model, JsonElement rec, JsonElement given)
    {
        var ps = rec.GetProperty("params").EnumerateArray().ToList();
        var types = new System.Type[ps.Count];
        var values = new object?[ps.Count];
        for (var i = 0; i < ps.Count; i++)
        {
            var name = ps[i].GetProperty("name").GetString()!;
            var t = ps[i].GetProperty("type");
            var v = given.ValueKind == JsonValueKind.Object && given.TryGetProperty(name, out var gv) ? gv : ps[i].GetProperty("default");
            switch (t.GetProperty("kind").GetString())
            {
                case "enum":
                    var literal = v.GetString()!;
                    literal = char.ToUpperInvariant(literal[0]) + literal[1..];
                    if (t.GetProperty("type").GetString() == "Scope") { types[i] = typeof(Scope); values[i] = Enum.Parse<Scope>(literal); }
                    else { types[i] = typeof(VisibilityFilter); values[i] = Enum.Parse<VisibilityFilter>(literal); }
                    break;
                case "element":
                    var tn = t.GetProperty("type").GetString()!;
                    types[i] = tn == "Element" ? typeof(IElement) : ClassMap.TypeMap[tn];
                    values[i] = Find(model, v.GetString()!);
                    break;
                default:
                    if (t.GetProperty("type").GetString() == "Boolean") { types[i] = typeof(bool); values[i] = v.ValueKind == JsonValueKind.True; }
                    else { types[i] = typeof(string); values[i] = v.GetString(); }
                    break;
            }
        }
        return (types, values);
    }

    /// <summary>The binding library and the release library; null, noted, when one is missing.</summary>
    private static (string Library, string LibraryDir)? ToolkitAtHand(string root)
    {
        var libraryDir = Path.Combine(Path.GetDirectoryName(root)!, "sysml-toolkit", "spec-refs", "SysML-v2-Release", "sysml.library");
        string libPath;
        try
        {
            libPath = ToolkitBackend.Library.DefaultPath();
        }
        catch (SdkException e)
        {
            SkipOrFail("the binding library: " + e.Message);
            return null;
        }
        if (!File.Exists(libPath)) { SkipOrFail($"the binding library at {libPath}"); return null; }
        if (!Directory.Exists(libraryDir)) { SkipOrFail($"the release library at {libraryDir}"); return null; }
        return (libPath, libraryDir);
    }

    private static void SkipOrFail(string what)
    {
        if (Environment.GetEnvironmentVariable("SYSML_REQUIRE_TOOLKIT") == "1")
            throw new Exception($"SYSML_REQUIRE_TOOLKIT=1 but {what} is missing");
        Console.WriteLine($"convenience checks on the SysML Toolkit skipped: {what} is missing");
    }

    /// <summary>The element by qualified name, whatever its visibility (<c>Model.Find</c>).</summary>
    private static IElement? Find(Model model, string qn) => model.Find(qn);

    private static System.Type Iface(IElement el) =>
        ClassMap.TypeMap.TryGetValue(el.MetaclassName, out var t) ? t : typeof(IElement);

    private static object? Property(IElement el, string prop)
    {
        var p = Iface(el).GetProperty(prop) ?? typeof(IElement).GetProperty(prop)
            ?? throw new MissingMemberException(el.MetaclassName, prop);
        try { return p.GetValue(el); }
        catch (TargetInvocationException x) when (x.InnerException is not null) { throw x.InnerException; }
    }

    private static IElement Walk(IElement el, JsonElement via)
    {
        foreach (var s in via.EnumerateArray())
        {
            var step = s.GetString()!;
            var b = step.IndexOf('[');
            var prop = b < 0 ? step : step[..b];
            var kind = b < 0 ? null : step[(b + 1)..^1];
            var value = Property(el, prop);
            var values = value is System.Collections.IEnumerable list and not string ? list.Cast<object?>().ToList() : new List<object?> { value };
            var kept = values.OfType<IElement>().Where(x => kind is null || ClassMap.TypeMap[kind].IsInstanceOfType(x)).ToList();
            if (kept.Count != 1) throw new Exception($"{step} gives {kept.Count} elements");
            el = kept[0];
        }
        return el;
    }

    private static object? Plain(JsonElement v) => v.ValueKind switch
    {
        JsonValueKind.String => v.GetString(),
        JsonValueKind.True => true,
        JsonValueKind.False => false,
        JsonValueKind.Number => v.TryGetInt64(out var l) ? l : v.GetDouble(),
        JsonValueKind.Null => null,
        _ => v,
    };

    private static bool Matches(Model model, JsonElement expected, object? actual)
    {
        if (expected.ValueKind == JsonValueKind.Object && expected.TryGetProperty("@type", out var type))
        {
            if (actual is not IElement el || el.MetaclassName != type.GetString()) return false;
            foreach (var p in expected.EnumerateObject())
                if (p.Name != "@type" && !Matches(model, p.Value, Property(el, p.Name))) return false;
            return true;
        }
        if (actual is IElement a) return expected.ValueKind == JsonValueKind.String && a.Equals(Find(model, expected.GetString()!));
        var want = Plain(expected);
        if (actual is Literal lit)
            return lit switch
            {
                Literal.Unbounded => want is "*",
                Literal.IntegerValue(var v) => want is long or double && Convert.ToDouble(want) == v,
                Literal.RealValue(var v) => want is long or double && Convert.ToDouble(want) == v,
                Literal.BooleanValue(var v) => Equals(want, v),
                Literal.StringValue(var v) => Equals(want, v),
                _ => false,
            };
        if (actual is string s) return Equals(want, s); // such as an operator: "*" is itself
        if (want is bool || actual is bool) return Equals(want, actual);
        if (want is long or double && actual is long or double) return Convert.ToDouble(want) == Convert.ToDouble(actual);
        return Equals(want, actual);
    }

    private static (string, string) Outcome(Model model, Dictionary<string, JsonElement> records, JsonElement e)
    {
        var rec = records[e.GetProperty("member").GetString()!];
        var el = Find(model, e.GetProperty("at").GetString()!);
        if (el is null) return ("mismatch", $"{e.GetProperty("at")} not found");
        if (e.TryGetProperty("via", out var via)) el = Walk(el, via);
        var given = e.TryGetProperty("args", out var a) ? a : default;
        var (types, values) = Arguments(model, rec, given);
        var member = e.GetProperty("member").GetString()!;
        // An interface's own methods only: its bases' are searched one by one.
        var spelled = char.ToUpperInvariant(member[0]) + member[1..];
        var method = new[] { Iface(el) }.Concat(Iface(el).GetInterfaces())
            .Select(i => i.GetMethod(spelled, 0, types)).FirstOrDefault(m => m is not null);
        if (method is null) return ("mismatch", $"{el.MetaclassName} has no {member}");
        object? answer;
        try
        {
            answer = method.Invoke(el, values);
        }
        catch (TargetInvocationException x) when (x.InnerException is RefusedException r)
        {
            return e.TryGetProperty("refused", out _) ? ("agrees", "refused") : ("mismatch", "refused: " + r.Message);
        }
        catch (TargetInvocationException x) when (x.InnerException is NotAnsweredException n)
        {
            return ("refused by the backend", n.Message);
        }
        if (e.TryGetProperty("refused", out _)) return ("mismatch", $"answered {answer}, the contract refuses");
        try
        {
            if (e.TryGetProperty("includes", out var includes))
            {
                var list = ((System.Collections.IEnumerable)answer!).Cast<object?>().ToList();
                foreach (var q in includes.EnumerateArray())
                    if (!list.Any(x => Matches(model, q, x))) return ("mismatch", $"missing {q}");
                if (e.TryGetProperty("excludes", out var excludes))
                    foreach (var q in excludes.EnumerateArray())
                        if (list.Any(x => Matches(model, q, x))) return ("mismatch", $"unexpected {q}");
                return ("agrees", "");
            }
            var want = e.GetProperty("expect");
            var unordered = e.TryGetProperty("unordered", out var u) && u.ValueKind == JsonValueKind.True;
            if (answer is not null && answer.GetType().IsGenericType && answer.GetType().GetGenericTypeDefinition() == typeof(NameMap<>))
            {
                // A name-keyed map: the expected [name, element] pairs.
                var pairs = ((System.Collections.IEnumerable)answer).Cast<object>()
                    .Select(kv => (object?)(Key: (string)kv.GetType().GetProperty("Key")!.GetValue(kv)!, Value: kv.GetType().GetProperty("Value")!.GetValue(kv)))
                    .ToList();
                var ws = want.ValueKind == JsonValueKind.Array ? want.EnumerateArray().ToList() : new List<JsonElement>();
                var ok = SameItems(ws, pairs, (w, kv) =>
                {
                    var (key, value) = ((string Key, object? Value))kv!;
                    return w[0].GetString() == key && Matches(model, w[1], value);
                }, unordered);
                return ok ? ("agrees", "") : ("mismatch", $"expected {want}, got {string.Join(", ", pairs.Select(p => (((string Key, object? Value))p!).Key))}");
            }
            if (want.ValueKind == JsonValueKind.Array)
            {
                if (answer is not System.Collections.IEnumerable got || answer is string) return ("mismatch", $"expected {want}, got {answer}");
                var items = got.Cast<object?>().ToList();
                var ws = want.EnumerateArray().ToList();
                if (items.Count != ws.Count) return ("mismatch", $"expected {want}, got {items.Count} items");
                if (!SameItems(ws, items, (w, x) => Matches(model, w, x), unordered))
                    return ("mismatch", $"expected {want}, got {string.Join(", ", items)}");
                return ("agrees", "");
            }
            if (want.ValueKind == JsonValueKind.Null) return answer is null ? ("agrees", "") : ("mismatch", $"expected none, got {answer}");
            return answer is not null && Matches(model, want, answer) ? ("agrees", "") : ("mismatch", $"expected {want}, got {answer}");
        }
        catch (NotAnsweredException x)
        {
            return ("refused by the backend", "the answer cannot be checked: " + x.Message);
        }
    }
}
