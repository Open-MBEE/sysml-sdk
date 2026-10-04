// Smoke test + conformance runner for the C# SDK backend.
// Mirrors ../../demo.py; any failure throws and exits non-zero.

using OpenMBEE.SysML;
using OpenMBEE.SysML.Checks;
using SpecType = OpenMBEE.SysML.Type;

var root = FindRepoRoot();
Conformance.Run(Path.Combine(root, "metamodel.json"));
RunSmoke(Path.Combine(root, "data", "bigger.full.json"));
CheckPayloadLibrary();
LibraryChecks.Run();
Console.WriteLine();
ToolkitChecks.RunIfAvailable();
Console.WriteLine("ALL C#-BACKEND SMOKE TESTS PASSED");
return 0;

static string FindRepoRoot()
{
    for (var d = new DirectoryInfo(AppContext.BaseDirectory); d is not null; d = d.Parent)
        if (File.Exists(Path.Combine(d.FullName, "metamodel.json")))
            return d.FullName;
    throw new InvalidOperationException("metamodel.json not found above bin dir");
}

static void Check(bool cond, string what)
{
    if (!cond)
        throw new Exception("FAILED: " + what);
}

static void RunSmoke(string payloadPath)
{
    var payload = File.ReadAllText(payloadPath);
    var m = Model.FromFullJson(payload);

    // --- typed resolution and hierarchy -----------------------------------
    var vehicle = m.Resolve("Demo::Vehicle")!;
    Console.WriteLine($"resolve: {vehicle}");
    Check(vehicle is PartDefinition, "PartDefinition");
    Check(vehicle is Definition and Classifier and SpecType and Namespace,
        "hierarchy chain");
    Check(vehicle is not Feature, "not Feature");
    Console.WriteLine(
        "is chain: PartDefinition < Definition < Classifier < Type < Namespace OK");

    // --- navigation: typed getters (table 0.5.0) --------------------------
    IReadOnlyList<Feature> feats = ((PartDefinition)vehicle).ownedFeature;
    Console.WriteLine("ownedFeature: " + string.Join(", ",
        feats.Select(f => $"{f.MetaclassName}:{f.declaredName}")));
    Check(feats.Count == 7, "7 features");
    var wheel = feats.First(f => f.declaredName == "wheel");
    Check(wheel.MetaclassName == "PartUsage", "wheel is PartUsage");
    Check(ReferenceEquals(wheel.owner, vehicle), "owner minted from cache");
    Check(wheel.isComposite == true, "isComposite");
    Check(((PartDefinition)vehicle).qualifiedName == "Demo::Vehicle",
        "qualifiedName");

    var baseDef = m.Resolve("Demo::Base")!;
    IReadOnlyList<Subclassification> subclass =
        ((Classifier)vehicle).ownedSubclassification;
    Check(subclass.Count == 1 &&
        ReferenceEquals(subclass[0].superclassifier, baseDef),
        "ownedSubclassification -> Base");

    var vUsage = (Usage)m.Resolve("Demo::v")!;
    IReadOnlyList<Classifier> defs = vUsage.definition;
    Check(defs.Count == 1 && ReferenceEquals(defs[0], vehicle), "v.definition");
    Console.WriteLine("v.definition -> Vehicle OK");

    // --- reads pass the backend value through unchanged -------------------
    // (no client-side check: a member the SysML Toolkit does not implement says so itself)
    Check(((PartDefinition)vehicle).ownedPart.Count == 0, "ownedPart == []");
    Check(((SpecType)vehicle).inheritedFeature.Count == 0, "inheritedFeature == []");
    Console.WriteLine(
        "unimplemented derivations -> [] (backend value, no second-guessing)");

    // --- operation stubs are honest ---------------------------------------
    try
    {
        _ = ((SpecType)vehicle).specializes((SpecType?)baseDef);
        throw new Exception("expected NotImplementedInToolkitException");
    }
    catch (NotImplementedInToolkitException)
    {
        Console.WriteLine("Type.specializes() -> NotImplementedInToolkit (as designed)");
    }

    // (read-only guard is compile-time in C#: generated members have no setters)

    // --- model-wide queries -----------------------------------------------
    var parts = m.ElementsOfType<PartUsage>().Count();
    var usages = m.ElementsOfType<Usage>().Count();
    Console.WriteLine($"PartUsage: {parts}, all Usage subtypes: {usages}");
    Check(usages > parts, "subtype counts");

    var calc = m.Resolve("Demo::Vehicle::K")!;
    Check(calc is CalculationDefinition, "K is CalculationDefinition");
    IReadOnlyList<Feature> inputs = ((CalculationDefinition)calc).input;
    Check(inputs.Count == 1 && inputs[0].declaredName == "x", "K.input");
    Console.WriteLine("K.input -> [x] OK");

    // --- collision rule: MetadataUsage.metaclass is the SPEC member -------
    const string metaPayload = """
        [
          {"@id": "mc1", "@type": "Metaclass", "declaredName": "Safety",
           "qualifiedName": "M::Safety"},
          {"@id": "m1", "@type": "MetadataUsage", "qualifiedName": "M::stubbed",
           "metaclass": null},
          {"@id": "m2", "@type": "MetadataUsage", "qualifiedName": "M::filled",
           "metaclass": {"@id": "mc1"}}
        ]
        """;
    var mm = Model.FromFullJson(metaPayload);
    var mu = (MetadataUsage)mm.Element("m1");
    Check(mu.MetaclassName == "MetadataUsage", "runtime accessor (PascalCase)");
    Check(mu.metaclass is null, "spec member: backend value passed through");
    var filled = ((MetadataUsage)mm.Element("m2")).metaclass;
    Check(ReferenceEquals(filled, mm.Element("mc1")), "filled metaclass re-check");
    Console.WriteLine("MetadataUsage: .MetaclassName (runtime) vs .metaclass (spec) OK");

    // Elements come in document order; an id given twice keeps its first place and its last
    // element.
    var ordered = Model.FromFullJson("""
        [
          {"@id": "z", "@type": "Package", "declaredName": "first"},
          {"@id": "a", "@type": "Package", "declaredName": "A"},
          {"@id": "z", "@type": "Package", "declaredName": "second"}
        ]
        """);
    var ids = string.Join(",", ordered.All().Select(e => e.ElementId));
    Check(ids == "z,a", $"document order: {ids}");
    Check(ordered.Element("z").declaredName == "second", "the last element of an id");
    Console.WriteLine("payload elements in document order OK");
}

// A payload model with a library: references the model does not hold resolve in the library, the
// library's elements are not the model's, the model wins an id or name both have, and a reference
// in neither still throws. The same checks run in every language.
static void CheckPayloadLibrary()
{
    var library = PayloadLibrary.FromJson("""
        [
          {"@id": "lib", "@type": "LibraryPackage", "qualifiedName": "ScalarValues", "isLibraryElement": true},
          {"@id": "real", "@type": "DataType", "qualifiedName": "ScalarValues::Real", "declaredName": "Real",
           "owner": {"@id": "lib"}, "isLibraryElement": true},
          {"@id": "clash", "@type": "Package", "declaredName": "from the library"},
          {"@id": "libP", "@type": "Package", "qualifiedName": "P"}
        ]
        """);
    var model = """
        [
          {"@id": "pkg", "@type": "Package", "qualifiedName": "P", "declaredName": "P"},
          {"@id": "mass", "@type": "AttributeUsage", "qualifiedName": "P::mass", "owner": {"@id": "pkg"},
           "type": [{"@id": "real"}], "definition": [{"@id": "gone"}]},
          {"@id": "clash", "@type": "Package", "declaredName": "from the model"}
        ]
        """;
    var withLibrary = Model.FromFullJson(model, library);
    var mass = (AttributeUsage)withLibrary.Element("mass");
    SpecType real = mass.type[0];
    Check(real.declaredName == "Real" && real.isLibraryElement == true, "library element");
    Check(real.owner?.qualifiedName == "ScalarValues", "library owner");
    Check(real.Equals(withLibrary.Resolve("ScalarValues::Real")), "resolve into the library");
    var all = string.Join(",", withLibrary.All().Select(e => e.ElementId));
    Check(all == "pkg,mass,clash", $"All() lists the library: {all}");
    var roots = string.Join(",", withLibrary.Roots().Select(e => e.ElementId));
    Check(roots == "pkg,clash", $"Roots() lists the library: {roots}");
    Check(withLibrary.Element("clash").declaredName == "from the model", "the model's id wins");
    Check(withLibrary.Resolve("P")?.ElementId == "pkg", "the model's name wins");
    Check(ThrowsUnresolved(() => _ = mass.definition), "a reference in neither");
    var withoutLibrary = Model.FromFullJson(model);
    Check(ThrowsUnresolved(() => _ = ((AttributeUsage)withoutLibrary.Element("mass")).type), "no library, no resolution");
    Check(withoutLibrary.Resolve("ScalarValues::Real") is null, "no library, no name");
    Console.WriteLine("payload model with a library OK");
}

static bool ThrowsUnresolved(Action read)
{
    try
    {
        read();
    }
    catch (UnresolvedReferenceException)
    {
        return true;
    }
    return false;
}
