// Parametric queries over two example models, and optionally over the vehicle model of SysML
// Annex A, printed as a report. The program in every language prints the same report, which is
// kept in examples/queries/expected/.
//
// Run from the repository root:
//
//     dotnet run --project examples/queries/csharp                   reads the models through the SysML Toolkit
//     dotnet run --project examples/queries/csharp -- --payload      reads their JSON exports instead,
//                                                                    with the standard library as JSON
//     dotnet run --project examples/queries/csharp -- --library DIR  loads the standard library with them
//     dotnet run --project examples/queries/csharp -- --annex-a FILE --library DIR
//                                                                    reports on the Annex A vehicle instead
//
// The JSON exports and the library JSON are generated, not committed: python tools/export_example.py.
// The queries themselves are in Queries.cs; this file loads the models and asks the questions.

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using OpenMBEE.SysML;
using QueryExamples;
using SpecType = OpenMBEE.SysML.Type; // the metaclass Type shadows System.Type

var models = Path.Combine(FindRepoRoot(), "examples", "queries", "models");
var libraryJson = Path.Combine(FindRepoRoot(), "examples", "sysml.library.full.json");
PayloadLibrary? payloadLibrary = null;
var library = Option("--library");
var annex = Option("--annex-a");
if (annex != null) AnnexA(annex);
else Examples(args.Contains("--payload"));

// A model from examples/queries/models: `name`.sysml through the SysML Toolkit, or
// `name`.full.json with the library JSON, read once and shared by every payload model.
Model Load(string name, bool payload)
{
    if (payload)
    {
        payloadLibrary ??= PayloadLibrary.FromJson(File.ReadAllText(libraryJson));
        return Model.FromFullJson(File.ReadAllText(Path.Combine(models, $"{name}.full.json")), payloadLibrary);
    }
    var sources = new Dictionary<string, string> { [$"{name}.sysml"] = File.ReadAllText(Path.Combine(models, $"{name}.sysml")) };
    return new Model(ToolkitBackend.Open(ToolkitBackend.DefaultLibrary, Array.Empty<string>(), sources, library));
}

void Heading(string title) => Console.WriteLine($"== {title} ==");

// Run one report. Where the SysML Toolkit cannot vouch for a value yet it refuses the read
// (NotImplementedInToolkitException); the report stops there, says so, and the next one runs.
void Section(Action report)
{
    try
    {
        report();
    }
    catch (NotImplementedInToolkitException e)
    {
        Console.WriteLine($"  report stopped: {e.Message}");
    }
}

// A scenario in words: the triggers that can occur and the values bound.
string Describe(IReadOnlyList<string>? triggers, IReadOnlyList<(string Name, object Value)> bindings)
{
    var parts = new List<string>();
    if (triggers != null) parts.Add("only " + string.Join(", ", triggers));
    if (bindings.Count > 0) parts.Add(string.Join(", ", bindings.Select(b => $"{b.Name} = {Queries.Fmt(b.Value)}")));
    return parts.Count > 0 ? string.Join("; ", parts) : "nothing bound, every trigger";
}

Dictionary<string, object?> AsBindings(IReadOnlyList<(string Name, object Value)> bindings) =>
    bindings.ToDictionary(b => b.Name, b => (object?)b.Value);

// ---------------------------------------------------------------- the reports

// Reachable states of `machine` under each scenario, then the shortest trigger sequences under the
// scenario `pathsFor`.
void ReportStates(StateUsage machine, IReadOnlyList<Scenario> scenarios, Scenario pathsFor)
{
    Heading($"Reachable states: {machine.qualifiedName}");
    var states = Queries.StatesOf(machine);
    var transitions = Queries.TransitionsOf(machine);
    Console.WriteLine($"{states.Count} states, {transitions.Count} transitions:");
    foreach (var state in new[] { machine }.Concat(states))
        foreach (var initial in Queries.InitialStates(state))
            Console.WriteLine($"  {Queries.DescribeInitial(state, initial, machine)}");
    foreach (var transition in transitions) Console.WriteLine($"  {Queries.DescribeTransition(transition, machine)}");
    foreach (var scenario in scenarios)
    {
        var reached = Queries.Explore(machine, scenario.Triggers, AsBindings(scenario.Bindings));
        Console.WriteLine($"{scenario.Title}: {Describe(scenario.Triggers, scenario.Bindings)}");
        var reachable = states.Where(s => reached.ContainsKey(s)).Select(s => Queries.PathBelow(s, machine)).ToList();
        var unreachable = states.Where(s => !reached.ContainsKey(s)).Select(s => Queries.PathBelow(s, machine)).ToList();
        Console.WriteLine($"  reachable ({reachable.Count}): {string.Join(", ", reachable)}");
        Console.WriteLine($"  unreachable ({unreachable.Count}): {(unreachable.Count > 0 ? string.Join(", ", unreachable) : "-")}");
    }
    var paths = Queries.Explore(machine, pathsFor.Triggers, AsBindings(pathsFor.Bindings));
    Console.WriteLine($"shortest trigger sequences ({Describe(pathsFor.Triggers, pathsFor.Bindings)}):");
    var width = states.Max(s => Queries.PathBelow(s, machine).Length);
    foreach (var state in states)
    {
        var text = !paths.TryGetValue(state, out var path) ? "unreachable"
            : path.Count == 0 ? "(no trigger)" : string.Join(", ", path);
        Console.WriteLine($"  {Queries.PathBelow(state, machine).PadRight(width)}  {text}");
    }
}

void ReportStructure(IEnumerable<SpecType> roots, IReadOnlyList<Feature> attributes)
{
    Heading("Structure: bills of materials and roll-ups");
    foreach (var root in roots)
    {
        Console.WriteLine(root.qualifiedName);
        Console.WriteLine("  parts: " + string.Join(", ", Queries.BillOfMaterials(root).Select(e => $"{e.Key} {e.Value}")));
        Console.WriteLine("  " + string.Join(", ", attributes.Select(a => $"{Queries.NameOf(a)} {Queries.Fmt(Queries.Rollup(root, a))}")));
    }
}

void ReportConnectivity(SpecType root, IReadOnlyList<(string Source, string? Item)> questions)
{
    Heading($"Connectivity: {root.qualifiedName}");
    foreach (var link in Queries.Links(root)) Console.WriteLine($"  {link.Describe()}");
    foreach (var (source, item) in questions)
    {
        var via = item != null ? $", through {item} flows only" : "";
        var reached = Queries.ReachableParts(root, source, item);
        Console.WriteLine($"from {source}{via}: {(reached.Count > 0 ? string.Join(", ", reached) : "-")}");
    }
}

void ReportRequirements(Model model, IReadOnlyDictionary<string, Func<SpecType, object?>> derived)
{
    Heading("Requirements");
    foreach (var v in Queries.CheckSatisfactions(model, derived))
    {
        var definition = v.Requirement.requirementDefinition;
        var kind = definition != null ? $" : {Queries.NameOf(definition)}" : "";
        var verb = v.Negated ? "not satisfied" : "satisfied";
        var expr = Queries.ResultExpression(v.Constraint)!;
        var op = expr is OperatorExpression operation ? operation.@operator : "";
        var values = string.Join($" {op} ", v.Operands.Select(Queries.Fmt));
        var by = v.SatisfiedBy != null ? Queries.NameOf(v.SatisfiedBy) : "(nothing)";
        Console.WriteLine($"{Queries.NameOf(v.Requirement)}{kind}, {verb} by {by}");
        Console.WriteLine($"  {Queries.NameOf(v.Constraint)}: {Queries.Render(expr)}  [{values}]  {v.Outcome}");
    }
}

// ---------------------------------------------------------------- the questions

void Examples(bool payload)
{
    var temperate = new List<(string, object)> { ("selfTestPassed", true), ("temperature", 25.0) };
    var pumps = Load("pumps", payload);
    Section(() => ReportStates((StateUsage)pumps.Resolve("Pumps::PumpController::modes")!, new List<Scenario>
    {
        new("A", null, new List<(string, object)>()),
        new("B", null, temperate),
        new("C", null, new List<(string, object)> { ("selfTestPassed", false) }),
        new("D", null, new List<(string, object)> { ("selfTestPassed", true), ("temperature", 95.0) }),
        new("E", null, temperate.Append(("maxTemperature", 20.0)).ToList()),
        new("F", new[] { "PowerOn", "SelfTestDone", "Start" }, temperate),
    }, new Scenario("", null, temperate)));
    Console.WriteLine();

    var drones = Load("drones", payload);
    var mass = (Feature)drones.Resolve("Drones::Component::mass")!;
    var cost = (Feature)drones.Resolve("Drones::Component::cost")!;
    Section(() => ReportStructure(new[] { "Quadcopter", "Hexacopter", "CargoHexacopter" }.Select(d => (SpecType)drones.Resolve($"Drones::{d}")!),
        new[] { mass, cost }));
    Console.WriteLine();
    Section(() => ReportConnectivity((SpecType)drones.Resolve("Drones::Quadcopter")!, new List<(string, string?)>
    {
        ("battery", null), ("battery", "Power"), ("controller", null), ("controller", "Command"), ("rotors.propeller", null),
    }));
    Console.WriteLine();
    Section(() => ReportRequirements(drones, new Dictionary<string, Func<SpecType, object?>>
    {
        ["totalMass"] = part => Queries.Rollup(part, mass),
        ["totalCost"] = part => Queries.Rollup(part, cost),
    }));
}

void AnnexA(string path)
{
    var sources = new Dictionary<string, string> { [Path.GetFileName(path)] = File.ReadAllText(path) };
    var vehicle = new Model(ToolkitBackend.Open(ToolkitBackend.DefaultLibrary, Array.Empty<string>(), sources, library));
    var machine = (StateUsage)vehicle.Resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates")!;
    Section(() =>
    {
        var keys = new List<string>();
        foreach (var transition in Queries.TransitionsOf(machine))
        {
            var trigger = Queries.TriggerOf(transition);
            if (trigger != null && trigger.Key != "at" && !keys.Contains(trigger.Key)) keys.Add(trigger.Key);
        }
        var none = new List<(string, object)>();
        ReportStates(machine, new List<Scenario>
        {
            new("A", null, none),
            new("B", null, new List<(string, object)> { ("brakePedalDepressed", false) }),
            new("C", keys, none),
        }, new Scenario("", null, none));
    });
}

string? Option(string name)
{
    var i = Array.IndexOf(args, name);
    return i >= 0 && i + 1 < args.Length ? args[i + 1] : null;
}

static string FindRepoRoot()
{
    var d = new DirectoryInfo(AppContext.BaseDirectory);
    while (d != null && !File.Exists(Path.Combine(d.FullName, "metamodel.json"))) d = d.Parent;
    return d?.FullName ?? throw new InvalidOperationException("run from inside the sysml-sdk repository");
}

/// <summary>A scenario: its title, the triggers that can occur (null for all), and the values bound, in
/// order.</summary>
record Scenario(string Title, IReadOnlyList<string>? Triggers, IReadOnlyList<(string Name, object Value)> Bindings);
