// EngineBackend against a small engine written for the check (docs/engine-protocol.md): this program
// itself, started with --fake-engine. No Java and no engine install needed.
using System.Diagnostics;
using System.Text.Json;
using System.Text.Json.Nodes;
using OpenMBEE.SysML;

namespace OpenMBEE.SysML.Checks;

public static class EngineChecks
{
    private static void Check(bool cond, string what)
    {
        if (!cond) throw new Exception("FAILED: " + what);
    }

    /// <summary>The fake engine: a package P owning a part definition A, answered from a table.</summary>
    public static int FakeEngine()
    {
        var elements = new Dictionary<string, (string Mc, string Name, string? Qn)>
        {
            ["1"] = ("Package", "P", null),
            ["2"] = ("PartDefinition", "A", "P::A"),
        };
        var stdout = new StreamWriter(Console.OpenStandardOutput(), new System.Text.UTF8Encoding(false)) { AutoFlush = true };
        void Out(JsonNode n) => stdout.WriteLine(n.ToJsonString());
        Out(new JsonObject { ["ready"] = true, ["engine"] = "Test engine", ["version"] = "1.0", ["protocol"] = 1 });
        for (string? line; (line = Console.In.ReadLine()) != null;)
        {
            var req = JsonNode.Parse(line)!.AsObject();
            var id = req["id"]!.GetValue<int>();
            var cmd = req["cmd"]!.GetValue<string>();
            void Ok(JsonNode? v) => Out(new JsonObject { ["id"] = id, ["ok"] = v });
            void Err(string kind, string message) =>
                Out(new JsonObject { ["id"] = id, ["error"] = new JsonObject { ["kind"] = kind, ["message"] = message } });
            var h = req["h"]?.GetValue<string>();
            if (cmd == "open") { Ok(new JsonObject { ["sources"] = req["sources"]!.AsObject().Count, ["elaborate"] = req["elaborate"]!.GetValue<bool>() }); continue; }
            if (cmd == "close") { Ok(null); return 0; }
            if (cmd == "allHandles") { Ok(new JsonArray("1", "2")); continue; }
            if (cmd == "resolve")
            {
                var name = req["name"]!.GetValue<string>();
                Ok(name == "P" ? "1" : name == "P::A" ? "2" : null);
                continue;
            }
            if (h is null || !elements.ContainsKey(h)) { Err("gone", $"no element {h}"); continue; }
            var (mc, n, qn) = elements[h];
            if (cmd == "metaclass") { Ok(mc); continue; }
            if (cmd == "elementId") { Ok("id-" + h); continue; }
            if (cmd == "call")
            {
                if (req["op"]!.GetValue<string>() == "effectiveName") Ok(n);
                else Err("notApplicable", "no operation " + req["op"]);
                continue;
            }
            switch (req["prop"]!.GetValue<string>())
            {
                case "declaredName": Ok(n); break;
                case "qualifiedName": Ok(qn ?? n); break;
                case "owner": Ok(h == "2" ? new JsonObject { ["@id"] = "1" } : null); break;
                case "ownedMember": Ok(h == "1" ? new JsonArray(new JsonObject { ["@id"] = "2" }) : new JsonArray()); break;
                case "isAbstract": Err("refused", "isAbstract is not answered"); break;
                case "ownedRelationship": Err("unresolved", "a reference did not resolve"); break;
                case "boom": Err("failed", "the engine broke"); break;
                default: Err("notApplicable", mc + " has no property " + req["prop"]); break;
            }
        }
        return 0;
    }

    public static void Run()
    {
        var self = Environment.ProcessPath!;
        var command = self.EndsWith("dotnet") || self.EndsWith("dotnet.exe")
            ? new[] { self, typeof(EngineChecks).Assembly.Location, "--fake-engine" }
            : new[] { self, "--fake-engine" };
        using (var be = EngineBackend.Start(command, sources: new Dictionary<string, string> { ["a.sysml"] = "package P { part def A; }" }))
        {
            Check(be.Engine == "Test engine" && be.Version == "1.0", "the engine announces itself");
            Check(be.Stats.GetProperty("sources").GetInt32() == 1 && be.Stats.GetProperty("elaborate").GetBoolean(), "open answers");
            var m = new Model(be);
            var a = (PartDefinition)m.Resolve("P::A")!;
            Check(a.declaredName == "A" && a.qualifiedName == "P::A", "reads");
            Check(a.owner is Package p && ReferenceEquals(((Package)a.owner!).ownedMember[0], a), "references");
            Check(a.effectiveName() == "A", "operations");
            Check(m.All().Count() == 2 && m.Resolve("Q") is null, "all and resolve");
            var refused = false;
            try { _ = a.isAbstract; } catch (NotAnsweredException e) { refused = e.Message.StartsWith("Test engine: "); }
            Check(refused, "refused is NotAnsweredException");
            var unresolved = false;
            try { _ = a.ownedRelationship; } catch (UnresolvedReferenceException) { unresolved = true; }
            Check(unresolved, "unresolved is UnresolvedReferenceException");
            var failed = false;
            try { be.Get("2", "boom"); } catch (BackendException e) { failed = e.Message.Contains("the engine broke"); }
            Check(failed, "failed is BackendException");
            Check(be.Get("2", "noSuchProperty") is null, "notApplicable reads as absent");
            var gone = false;
            try { be.Metaclass("9"); } catch (GoneException) { gone = true; }
            Check(gone, "gone is GoneException");
        }
        var missing = false;
        try { EngineBackend.Start(new[] { Path.Combine(Path.GetTempPath(), "no-such-engine-" + Guid.NewGuid()) }); }
        catch (BackendException e) { missing = e.Message.Contains("cannot start"); }
        Check(missing, "a missing engine is a BackendException");
        Check(new NotImplementedInToolkitException("x") is NotAnsweredException, "the SysML Toolkit's refusals are NotAnswered");
        Console.WriteLine("ALL C# ENGINE CHECKS PASSED");
    }
}
