// C# reading directly from the SysML Toolkit through the binding library. Skips itself when the
// library is not built, so the payload checks still run everywhere; with
// SYSML_REQUIRE_TOOLKIT=1 a missing library is a failure instead.
using OpenMBEE.SysML;
using SpecType = OpenMBEE.SysML.Type; // the metaclass Type shadows System.Type

namespace OpenMBEE.SysML.Checks;

public static class ToolkitChecks
{
    private static void Check(bool cond, string what)
    {
        if (!cond) throw new Exception("FAILED: " + what);
    }

    // A check that meets the SysML Toolkit's refusal of `member` first
    // (NotImplementedInToolkitException naming it): an expected failure, reported as one. It
    // fails when it passes (the SysML Toolkit answers now: remove the expectation) or when it
    // stops on anything else.
    private static void ExpectRefusal(string member, string reason, string what, Action body)
    {
        try
        {
            body();
        }
        catch (NotImplementedInToolkitException e) when (RefusedMember(e.Message) == member)
        {
            Console.WriteLine($"expected failure: {what}: the SysML Toolkit refuses {member} ({reason})");
            return;
        }
        throw new Exception($"FAILED: {what}: the SysML Toolkit answers {member} now; remove the expected refusal");
    }

    // The member a SysML Toolkit refusal names (`Type::inheritedMemberships()` gives
    // `inheritedMemberships()`).
    private static string RefusedMember(string message)
    {
        var i = message.IndexOf(" is not answered by the SysML Toolkit", StringComparison.Ordinal);
        if (i < 0) return "";
        var head = message[..i].Trim();
        var j = head.LastIndexOf("::", StringComparison.Ordinal);
        return j < 0 ? head : head[(j + 2)..];
    }

    public static void RunIfAvailable()
    {
        string path;
        try { path = ToolkitBackend.Library.DefaultPath(); }
        catch (SdkException e)
        {
            if (Environment.GetEnvironmentVariable("SYSML_REQUIRE_TOOLKIT") == "1")
                throw new Exception("SYSML_REQUIRE_TOOLKIT=1 but " + e.Message);
            Console.WriteLine("SysML Toolkit checks skipped: binding library not built");
            return;
        }

        var source = "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }";
        using var be = ToolkitBackend.OpenSources(new Dictionary<string, string> { ["inline.sysml"] = source });
        var m = new Model(be);

        var car = m.Resolve("Demo::Car");
        Check(car is PartDefinition, "Car is a PartDefinition");
        var carDef = (PartDefinition)car!;
        Check(carDef.qualifiedName == "Demo::Car", "qualifiedName");
        Check(carDef.owner is Package, "owner is the package");
        var feats = carDef.ownedFeature;
        Check(feats.Count == 2 && feats[0].declaredName == "wheels", "ownedFeature");
        Check(feats[0] is PartUsage, "wheels is a PartUsage");
        ExpectRefusal("type", "it waits on implied relationships", "type",
            () => Check(feats[0].type.Count == 1 && feats[0].type[0].declaredName == "Wheel", "type"));
        Check(feats[0].isComposite == true, "isComposite");
        var sports = (PartDefinition)m.Resolve("Demo::Sports")!;
        ExpectRefusal("inheritedFeature", "it waits on implied relationships", "inheritedFeature",
            () => Check(sports.inheritedFeature.Count == 2, $"inheritedFeature computed by the SysML Toolkit: {sports.inheritedFeature.Count}"));
        Check(ReferenceEquals(car, m.Resolve("Demo::Car")), "wrappers are cached by handle");
        Check(carDef.ElementId.Length == 36, "elementId is a UUID");
        // Operations, through the SysML Toolkit's operation entry point; it refuses what it cannot
        // answer as specified.
        Check(carDef.effectiveName() == "Car", "effectiveName");
        ExpectRefusal("connectorEnd", "it waits on the implied end redefinitions", "an unnamed connection end is named by the end it redefines", () =>
        {
            var connection = ((PartDefinition)m.Resolve("Demo::Link")!).ownedFeature.OfType<ConnectionUsage>().Single();
            var end = connection.connectorEnd[0];
            Check(end.declaredName is null && end.effectiveName() == "source",
                $"an unnamed connection end is named by the end it redefines: {end.effectiveName()}");
        });
        ExpectRefusal("inheritedMemberships()", "its evidence is incomplete", "inheritedMemberships", () =>
        {
            var memberships = sports.inheritedMemberships(Array.Empty<Namespace>(), Array.Empty<SpecType>(), false);
            Check(memberships.Count == 2 && memberships[0].memberName == "wheels", "inheritedMemberships");
        });
        var opRefused = false;
        try { _ = sports.isCompatibleWith(carDef); }
        catch (NotImplementedInToolkitException e) { opRefused = e.Message.Contains("isCompatibleWith"); }
        Check(opRefused, "an operation the SysML Toolkit cannot answer as specified is refused");
        Check(be.FullJson().StartsWith("["), "FullJson");
        // Text beyond ASCII crosses to the SysML Toolkit as UTF-8, measured in bytes, and comes
        // back intact; a requirement's text is a list of strings; every element has the members
        // of Element; the top-level namespaces.
        using var accented = ToolkitBackend.OpenSources(new Dictionary<string, string>
        {
            ["größe.sysml"] = "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }",
        });
        var sizes = new Model(accented);
        Check(sizes.Resolve("Sizes::Last") is PartDefinition, "a source with text beyond ASCII loads whole");
        Check(sizes.Resolve("Sizes")!.documentation.Single().body?.Contains("Größe und Wärme") == true,
            "text beyond ASCII comes back intact");
        var light = (RequirementDefinition)sizes.Resolve("Sizes::Light")!;
        Check(light.text.Count == 1 && light.text[0].StartsWith("Keep it light."),
            $"a requirement's text is a list of strings: {string.Join("|", light.text)}");
        IElement owner = light.owner!;
        Check(owner.name == "Sizes", "the members of Element, read on a plain IElement");
        Check(sizes.Roots().OfType<Namespace>().Any(r => r.ownedMember.Any(m => m.name == "Sizes")), "roots");
        Console.WriteLine("ALL C# SYSML TOOLKIT CHECKS PASSED");
    }
}
