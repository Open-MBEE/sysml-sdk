// C# reading through the SysML v2 Pilot Implementation, in a Java process. Skips itself when no
// Pilot is at hand (SYSML_PILOT_HOME unset and none in the user's cache) or no Pilot engine
// (SYSML_PILOT_SERVER unset and none in this build), so the other checks run everywhere without
// downloading the Pilot; with SYSML_REQUIRE_PILOT=1 nothing is a reason to skip.
using OpenMBEE.SysML;

namespace OpenMBEE.SysML.Checks;

public static class PilotChecks
{
    private static void Check(bool cond, string what)
    {
        if (!cond) throw new Exception("FAILED: " + what);
    }

    /// <summary>Why the Pilot checks skip themselves, or null when they run.</summary>
    internal static string? WhyNot()
    {
        if (Environment.GetEnvironmentVariable("SYSML_REQUIRE_PILOT") == "1") return null;
        try
        {
            PilotBackend.ServerJar();
            PilotBackend.JavaExecutable();
        }
        catch (BackendException e)
        {
            return e.Message;
        }
        if (!string.IsNullOrEmpty(Environment.GetEnvironmentVariable("SYSML_PILOT_HOME"))) return null;
        // The engine's cache: SYSML_CACHE_DIR, else the platform's per-user cache directory.
        var cache = Environment.GetEnvironmentVariable("SYSML_CACHE_DIR");
        if (string.IsNullOrEmpty(cache))
        {
            var home = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
            var local = Environment.GetEnvironmentVariable("LOCALAPPDATA");
            var xdg = Environment.GetEnvironmentVariable("XDG_CACHE_HOME");
            cache = OperatingSystem.IsWindows() ? Path.Combine(string.IsNullOrEmpty(local) ? Path.Combine(home, "AppData", "Local") : local, "sysml", "Cache")
                : OperatingSystem.IsMacOS() ? Path.Combine(home, "Library", "Caches", "sysml")
                : Path.Combine(string.IsNullOrEmpty(xdg) ? Path.Combine(home, ".cache") : xdg, "sysml");
        }
        if (Directory.Exists(cache) && Directory.EnumerateDirectories(cache, "pilot-*").Any(d => File.Exists(Path.Combine(d, ".complete"))))
            return null;
        return "no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)";
    }

    public static void RunIfAvailable()
    {
        if (WhyNot() is { } why)
        {
            Console.WriteLine("Pilot checks skipped: " + why);
            return;
        }
        var sources = new Dictionary<string, string>
        {
            ["demo.sysml"] = "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }",
            ["größe.sysml"] = "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }",
            ["bad.sysml"] = "package Bad { part p : Missing; }",
        };
        using (var be = PilotBackend.OpenSources(sources, new PilotOptions { Heap = "2g" }))
        {
            var m = new Model(be);
            Console.WriteLine($"checking with the Pilot {be.PilotVersion}");
            Check(be.Stats.GetProperty("pilot").GetString() == be.PilotVersion, "stats name the Pilot's version");

            var carDef = (PartDefinition)m.Resolve("Demo::Car")!;
            Check(carDef.qualifiedName == "Demo::Car", "qualifiedName");
            Check(carDef.owner is Package, "owner is the package");
            var feats = carDef.ownedFeature;
            Check(feats.Count == 2 && feats[0].declaredName == "wheels" && feats[0] is PartUsage, "ownedFeature");
            Check(feats[0].type.Count == 1 && feats[0].type[0].declaredName == "Wheel", "type");
            Check(feats[0].isComposite == true, "isComposite");
            var sports = (PartDefinition)m.Resolve("Demo::Sports")!;
            Check(sports.inheritedFeature.Any(f => f.declaredName == "wheels"), "inheritedFeature includes Car's wheels");
            Check(ReferenceEquals(carDef, m.Resolve("Demo::Car")), "wrappers are cached by handle");
            Check(carDef.ElementId.Length == 36, "elementId is a UUID");
            Check(m.Resolve("Demo::Nothing") is null, "an unknown name resolves to null");
            Check(m.Resolve("ScalarValues::Real") is not null, "library names resolve");

            Check(carDef.effectiveName() == "Car", "effectiveName");
            Check(sports.specializes(carDef) == true, "specializes");
            Check(sports.isCompatibleWith(carDef) == true, "isCompatibleWith");
            Check(sports.inheritedMemberships(Array.Empty<Namespace>(), Array.Empty<OpenMBEE.SysML.Type>(), false).Any(ms => ms.memberName == "wheels"),
                "inheritedMemberships");
            var connection = ((PartDefinition)m.Resolve("Demo::Link")!).ownedFeature.OfType<ConnectionUsage>().Single();
            var end = connection.connectorEnd[0];
            Check(end.declaredName is null && end.effectiveName() == "source",
                $"an unnamed connection end is named by the end it redefines: {end.effectiveName()}");

            Check(((IElement)m.Resolve("Sizes")!).documentation[0].body!.Contains("Größe und Wärme"), "text beyond ASCII comes back intact");
            var light = (RequirementDefinition)m.Resolve("Sizes::Light")!;
            Check(light.text.Count == 1 && light.text[0].StartsWith("Keep it light."), "a requirement's text is a list of strings");
            Check(m.Roots().SelectMany(r => ((Namespace)r).ownedMember).Any(x => x.name == "Sizes"), "roots");

            var p = (PartUsage)m.Resolve("Bad::p")!;
            var unresolved = false;
            try { _ = p.ownedTyping[0].type; }
            catch (UnresolvedReferenceException) { unresolved = true; }
            Check(unresolved, "an unresolved reference throws UnresolvedReferenceException");
            var gone = false;
            try { be.Metaclass("999999999"); }
            catch (GoneException) { gone = true; }
            Check(gone, "an unknown handle is gone");
            Check(be.Get(p.Handle, "noSuchProperty") is null, "a property the metaclass lacks reads as absent");
            var refused = false;
            try { be.Call(p.Handle, "noSuchOperation", Array.Empty<object?>()); }
            catch (BackendException e) { refused = e.Message.Contains("noSuchOperation"); }
            Check(refused, "an operation the Pilot does not have throws BackendException");
        }
        var missing = false;
        try
        {
            using var _ = PilotBackend.OpenSources(new Dictionary<string, string> { ["a.sysml"] = "package A;" },
                new PilotOptions { PilotHome = Path.GetTempPath(), Heap = "1g" });
        }
        catch (BackendException e) { missing = e.Message.Contains("holds no Pilot"); }
        Check(missing, "a missing Pilot throws BackendException");
        Console.WriteLine("ALL C# PILOT CHECKS PASSED");
    }
}
