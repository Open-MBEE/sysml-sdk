// Parametric queries over SysML v2 models, written against the SDK.
//
// Four families of queries, each a function of model elements and parameters:
//
// - state machines: which states a machine can reach, and the shortest sequence of triggers to
//   each, given which triggers can occur and what is known of the values its guards read;
// - structure: a bill of materials and the roll-up of an attribute over the part tree, with
//   multiplicities evaluated in context, redefined values, and inherited parts;
// - connectivity: the parts a source part reaches through connections and flows, optionally
//   through flows of one item definition only;
// - requirements: whether the constraints of each satisfied requirement hold for the part that
//   satisfies it.
//
// The queries read specification properties only (nestedState, usage, multiplicity,
// featureMembership, ...), so they run unchanged on either backend. Guards, multiplicity bounds,
// attribute values and constraints are the model's own expressions, evaluated by Evaluate() in
// three-valued logic: a value that the model does not determine and the caller did not bind is
// Unknown, and a guard that is Unknown may hold.
//
// What the queries simplify, on purpose:
//
// - A bound value holds for a whole exploration. Guard inputs such as a temperature vary over time
//   in SysML; binding one asks "what if it always had this value", and leaving it unbound lets every
//   guard over it go either way, independently of the others.
// - The regions of a parallel state are explored independently of each other.
// - A state that no transition or initial succession leads to is unreachable. That is the
//   conventional reading; the library's semantics do not state it formally.
// - A requirement is checked by its required constraints. Its assumptions (`assume constraint`) and
//   its nested requirements are not considered.
// - Connections and flows are followed where the model declares them; reaching a part means it may
//   be reached, at the level of the usages, not that every instance is.
//
// The same queries exist for every language of the SDK, function for function. Values are double,
// bool, string, an element, or Unknown.

using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using OpenMBEE.SysML;
using SpecType = OpenMBEE.SysML.Type; // the metaclass Type shadows System.Type

namespace QueryExamples;

/// <summary>The model does not give a query what it needs: a value, or an expression it can
/// evaluate.</summary>
public sealed class QueryError : Exception
{
    public QueryError(string message) : base(message) { }
}

/// <summary>A value that neither the model nor the caller determines.</summary>
public sealed class Unknown
{
    public static readonly Unknown Value = new();
    private Unknown() { }
    public override string ToString() => "unknown";
}

/// <summary>What an expression is evaluated against.</summary>
/// <param name="Context">The element whose features give values: a referenced feature has the value
/// of the feature that stands for it in the context (FeatureIn).</param>
/// <param name="Bindings">Values by feature name, which take precedence over the model's own.</param>
/// <param name="Derived">Attributes the caller computes, by name: a function of the element that has
/// the attribute (`vehicle.totalMass` is Derived["totalMass"](vehicle)).</param>
public sealed record Scope(SpecType? Context, IReadOnlyDictionary<string, object?> Bindings,
                           IReadOnlyDictionary<string, Func<SpecType, object?>> Derived)
{
    public Scope(SpecType? context)
        : this(context, new Dictionary<string, object?>(), new Dictionary<string, Func<SpecType, object?>>()) { }

    public Scope Within(SpecType context) => this with { Context = context };
}

/// <summary>A transition's trigger. The key is what must occur, as Explore selects triggers by it:
/// the name of the accepted signal's definition, or `at`, `after` or `when` for a time or change
/// event. The label says it in full (`at maintenanceTime`).</summary>
public sealed record Trigger(string Key, string Label);

/// <summary>A connection or flow between two parts, by their paths below the root
/// (`rotors.motor`). Kind is "connection" or "flow"; Item is what a flow carries, its payload's
/// definition, and null for a connection.</summary>
public sealed record Link(string Source, string Target, string Kind, string Name, string? Item)
{
    public string Describe()
    {
        var arrow = Kind == "flow" ? "->" : "--";
        var carries = Item != null ? $" of {Item}" : "";
        return $"{Source} {arrow} {Target}  ({Kind} {Name}{carries})";
    }
}

/// <summary>One required constraint of a satisfied requirement, evaluated for the satisfying
/// feature. Value is true, false or Unknown; Operands are the values of the constraint's operands,
/// for the report; Negated is `not satisfy`: the constraint is expected not to hold.</summary>
public sealed record Verdict(RequirementUsage Requirement, Feature? SatisfiedBy, ConstraintUsage Constraint,
                             object? Value, IReadOnlyList<object?> Operands, bool Negated)
{
    public string Outcome => Value is Unknown ? "unknown" : !Equals(Value, Negated) ? "holds" : "VIOLATED";
}

public static class Queries
{
    // ---------------------------------------------------------------- names

    /// <summary>The element's name. A redefinition declared without a name has the name of the
    /// feature it redefines: `attribute :>> mass` is named `mass`.</summary>
    public static string NameOf(IElement element) =>
        string.IsNullOrEmpty(element.name) ? "(unnamed)" : element.name;

    /// <summary>The names of `element` and its owners up to `top` (not included), outermost first:
    /// `operating::pumping::high` for a state two levels below the machine `top`.</summary>
    public static string PathBelow(IElement? element, IElement top)
    {
        var names = new List<string>();
        while (element != null && !element.Equals(top))
        {
            names.Add(NameOf(element));
            element = element.owner;
        }
        names.Reverse();
        return string.Join("::", names);
    }

    // ---------------------------------------------------------------- features and their values

    /// <summary>The usages of a definition or usage, owned and inherited, without those the
    /// standard library contributes (a part inherits `self`, `start`, `subparts`, ... from the
    /// library's `Part`).</summary>
    public static List<Usage> ModelUsages(SpecType element)
    {
        IReadOnlyList<Usage> usages = element switch
        {
            Definition d => d.usage,
            Usage u => u.usage,
            _ => Array.Empty<Usage>(),
        };
        return usages.Where(u => u.isLibraryElement != true).ToList();
    }

    /// <summary>The parts of a definition or usage, owned and inherited. A connection is a part too
    /// in SysML; it is not a component, so it is left out.</summary>
    public static List<PartUsage> PartUsages(SpecType element) =>
        ModelUsages(element).OfType<PartUsage>().Where(u => u is not ConnectionUsage).ToList();

    /// <summary>Whether `feature` redefines `other`, directly or through a chain of
    /// redefinitions.</summary>
    public static bool Redefines(Feature feature, Feature other)
    {
        var seen = new HashSet<Feature>();
        var todo = new Stack<Feature>(new[] { feature });
        while (todo.Count > 0)
        {
            foreach (var redefinition in todo.Pop().ownedRedefinition)
            {
                var redefined = redefinition.redefinedFeature;
                if (redefined != null && redefined.Equals(other)) return true;
                if (redefined != null && seen.Add(redefined)) todo.Push(redefined);
            }
        }
        return false;
    }

    /// <summary>The feature that stands for `feature` in `context`: among the context's features,
    /// owned or inherited, the one that is `feature` or redefines it. A type inherits only the most
    /// specific redefinition, so this is the one whose value holds in the context.</summary>
    public static Feature FeatureIn(SpecType context, Feature feature) =>
        context.feature.FirstOrDefault(c => c.Equals(feature) || Redefines(c, feature)) ?? feature;

    /// <summary>Whether a feature is one of the values an enumeration definition enumerates (`on` in
    /// `enum def IgnitionOnOff { on; off; }`). Those are owned through variant memberships, so they
    /// have an owning namespace but no owning type.</summary>
    public static bool IsEnumerationLiteral(IElement feature) =>
        feature is EnumerationUsage literal && literal.owningNamespace is EnumerationDefinition;

    /// <summary>The expression a feature is bound to by its declaration (`= 0.045`), or null.</summary>
    public static Expression? ValueExpression(Feature feature) =>
        feature.ownedMembership.OfType<FeatureValue>().FirstOrDefault()?.value;

    // ---------------------------------------------------------------- expressions

    /// <summary>The value of `expr`: a double, a bool, a string, an element, or Unknown.</summary>
    public static object? Evaluate(Expression expr, Scope scope) => expr switch
    {
        LiteralInteger literal => (double?)literal.value,
        LiteralRational literal => literal.value,
        LiteralBoolean literal => literal.value,
        LiteralString literal => literal.value,
        LiteralInfinity => double.PositiveInfinity,
        // before OperatorExpression, which it specializes
        FeatureChainExpression chain => ValueIn(Evaluate(chain.argument[0], scope), chain.targetFeature!, scope),
        FeatureReferenceExpression reference => ValueOf(reference.referent!, scope),
        OperatorExpression operation => ApplyOperator(operation.@operator!, operation.argument, scope),
        _ => throw new QueryError($"cannot evaluate a {expr.MetaclassName}"),
    };

    /// <summary>The value of a referenced feature: a binding of its name; an enumeration literal,
    /// which is its own value; else the value the model gives the feature that stands for it in the
    /// scope's context; else Unknown.</summary>
    public static object? ValueOf(Feature feature, Scope scope)
    {
        // The second operand of `and`, `or` and `implies` is a reference to an expression,
        // evaluated only when the operator needs it.
        if (feature is Expression expression) return Evaluate(expression, scope);
        var name = NameOf(feature);
        if (scope.Bindings.TryGetValue(name, out var bound)) return bound;
        if (IsEnumerationLiteral(feature)) return feature;
        if (scope.Context != null) feature = FeatureIn(scope.Context, feature);
        var expr = ValueExpression(feature);
        return expr == null ? Unknown.Value : Evaluate(expr, scope);
    }

    /// <summary>`source.feature`: a derived attribute that the caller computes, else the value of the
    /// feature in the context of `source`.</summary>
    public static object? ValueIn(object? source, Feature feature, Scope scope)
    {
        if (source is Unknown) return Unknown.Value;
        if (source is not SpecType context) throw new QueryError($"{Fmt(source)} has no feature {NameOf(feature)}");
        if (scope.Derived.TryGetValue(NameOf(feature), out var derive)) return derive(context);
        return ValueOf(feature, scope.Within(context));
    }

    /// <summary>An operator over its argument expressions. `and`, `or` and `implies` evaluate their
    /// second operand only when the first does not decide, as SysML's conditional operators do; all
    /// the logical operators follow three-valued logic. Any other operator on an Unknown is
    /// Unknown.</summary>
    public static object? ApplyOperator(string op, IReadOnlyList<Expression> args, Scope scope)
    {
        if (op is "and" or "or" or "implies") return Conditional(op, args, scope);
        var values = args.Select(a => Evaluate(a, scope)).ToList();
        if (op == "not" && values.Count == 1)
        {
            var v = Truth(values[0], op);
            return v is Unknown ? Unknown.Value : !(bool)v!;
        }
        if (op == "xor" && values.Count == 2)
        {
            var a = Truth(values[0], op);
            var b = Truth(values[1], op);
            return a is Unknown || b is Unknown ? Unknown.Value : !Equals(a, b);
        }
        if (values.Any(v => v is Unknown)) return Unknown.Value;
        if (op is "==" or "!=" && values.Count == 2)
        {
            var equal = Same(values[0], values[1]);
            return op == "==" ? equal : !equal;
        }
        var numbers = values.Select(v => Number(v, op)).ToList();
        if (numbers.Count == 1 && op is "-" or "+") return op == "-" ? -numbers[0] : numbers[0];
        if (numbers.Count == 2)
        {
            double x = numbers[0], y = numbers[1];
            object? result = op switch
            {
                "<" => x < y, "<=" => x <= y, ">" => x > y, ">=" => x >= y,
                "+" => x + y, "-" => x - y, "*" => x * y, "/" => x / y,
                _ => null,
            };
            if (result != null) return result;
        }
        throw new QueryError($"operator '{op}' on {args.Count} operand(s) is not supported");
    }

    private static object? Conditional(string op, IReadOnlyList<Expression> args, Scope scope)
    {
        var left = Truth(Evaluate(args[0], scope), op);
        if ((op == "and" && left is false) || (op == "or" && left is true)) return left;
        if (op == "implies" && left is false) return true;
        var right = Truth(Evaluate(args[1], scope), op);
        var unknown = left is Unknown || right is Unknown;
        if (op == "and") return right is false ? false : unknown ? Unknown.Value : true;
        return right is true ? true : unknown ? Unknown.Value : false; // or, implies
    }

    private static object? Truth(object? value, string op) =>
        value is Unknown or bool ? value : throw new QueryError($"'{op}' needs a Boolean, not {Fmt(value)}");

    private static double Number(object? value, string op) =>
        IsNumber(value) ? Convert.ToDouble(value, CultureInfo.InvariantCulture)
                        : throw new QueryError($"'{op}' needs a number, not {Fmt(value)}");

    /// <summary>Equality within a kind of value: numbers by value, elements by identity.</summary>
    private static bool Same(object? a, object? b)
    {
        if (IsNumber(a) && IsNumber(b)) return Number(a, "==") == Number(b, "==");
        return !IsNumber(a) && !IsNumber(b) && Equals(a, b);
    }

    public static bool IsNumber(object? value) => value is double or float or long or int;

    /// <summary>A value for a report: numbers with at most three decimals, elements by name.</summary>
    public static string Fmt(object? value)
    {
        switch (value)
        {
            case Unknown: return "unknown";
            case bool b: return b ? "true" : "false";
            case var n when IsNumber(n):
                var d = Number(n, "");
                if (double.IsInfinity(d)) return "*";
                var text = d.ToString("F3", CultureInfo.InvariantCulture).TrimEnd('0').TrimEnd('.');
                return text == "-0" ? "0" : text;
            case string s: return $"\"{s}\"";
            case IElement element: return NameOf(element);
            default: return "null";
        }
    }

    /// <summary>An expression in SysML notation, with every nested operation in parentheses.</summary>
    public static string Render(Expression expr)
    {
        switch (expr)
        {
            case LiteralInteger literal: return Fmt((double?)literal.value);
            case LiteralRational literal: return Fmt(literal.value);
            case LiteralBoolean literal: return literal.value == true ? "true" : "false";
            case LiteralString literal: return $"\"{literal.value}\"";
            case LiteralInfinity: return "*";
            case FeatureChainExpression chain: return $"{Render(chain.argument[0])}.{NameOf(chain.targetFeature!)}";
            case FeatureReferenceExpression reference:
                var referent = reference.referent!;
                if (referent is Expression expression) return Render(expression);
                if (IsEnumerationLiteral(referent)) return $"{NameOf(referent.owningNamespace!)}::{NameOf(referent)}";
                return NameOf(referent);
            case TriggerInvocationExpression trigger: return $"{trigger.kind} {Render(trigger.argument[0])}";
            case OperatorExpression operation:
                var operands = operation.argument.Select(Operand).ToList();
                var op = operation.@operator!;
                if (operands.Count == 1) return op.All(char.IsLetter) ? $"{op} {operands[0]}" : $"{op}{operands[0]}";
                return string.Join($" {op} ", operands);
            default: return $"<{expr.MetaclassName}>";
        }
    }

    private static string Operand(Expression expr)
    {
        if (expr is FeatureReferenceExpression { referent: Expression referent }) expr = referent;
        var text = Render(expr);
        var nested = expr is OperatorExpression and not FeatureChainExpression;
        return nested ? $"({text})" : text;
    }

    // ---------------------------------------------------------------- state machines

    /// <summary>Every state nested in `machine` at any depth, in the order the model declares them,
    /// each state before its substates.</summary>
    public static List<StateUsage> StatesOf(StateUsage machine)
    {
        var states = new List<StateUsage>();
        foreach (var state in machine.nestedState)
        {
            states.Add(state);
            states.AddRange(StatesOf(state));
        }
        return states;
    }

    /// <summary>Every transition declared in `machine` or in a state nested in it.</summary>
    public static List<TransitionUsage> TransitionsOf(StateUsage machine) =>
        new[] { machine }.Concat(StatesOf(machine)).SelectMany(s => s.nestedTransition).ToList();

    /// <summary>The states `state` starts in: the targets of the successions it owns
    /// (`first start then off;`); a transition is not a succession of this kind. Only the target is
    /// read: the source, the start of the state, is a feature of the standard library, which a model
    /// loaded without the library, or read from a payload, cannot resolve.</summary>
    public static List<StateUsage> InitialStates(StateUsage state) =>
        state.ownedFeature.OfType<SuccessionAsUsage>()
            .SelectMany(succession => succession.targetFeature.OfType<StateUsage>()).ToList();

    /// <summary>The transition's trigger, or null for a transition without one.</summary>
    public static Trigger? TriggerOf(TransitionUsage transition)
    {
        foreach (var accept in transition.triggerAction)
        {
            if (accept.payloadArgument is TriggerInvocationExpression argument)
                return new Trigger(argument.kind!, Render(argument));
            var types = accept.payloadParameter?.type ?? Array.Empty<SpecType>();
            var name = types.Count > 0 ? NameOf(types[0]) : "any";
            return new Trigger(name, name);
        }
        return null;
    }

    /// <summary>Whether the transition's guards hold: true if it has none, false if one is known to
    /// be false, else true or Unknown.</summary>
    public static object GuardOf(TransitionUsage transition, Scope scope)
    {
        object result = true;
        foreach (var guard in transition.guardExpression)
        {
            var value = Truth(Evaluate(guard, scope), "if");
            if (value is false) return false;
            if (value is Unknown) result = Unknown.Value;
        }
        return result;
    }

    /// <summary>`starting -> operating on SelfTestDone if selfTestPassed and (temperature &lt;
    /// maxTemperature)`</summary>
    public static string DescribeTransition(TransitionUsage transition, StateUsage machine)
    {
        var text = $"{PathBelow(transition.source, machine)} -> {PathBelow(transition.target, machine)}";
        var trigger = TriggerOf(transition);
        if (trigger != null) text += $" on {trigger.Label}";
        var guards = transition.guardExpression.Select(Render).ToList();
        if (guards.Count > 0) text += " if " + string.Join(" and ", guards);
        return text;
    }

    /// <summary>`initial state in operating: operating::idle`</summary>
    public static string DescribeInitial(StateUsage state, StateUsage initial, StateUsage machine)
    {
        var where = !state.Equals(machine) ? $" in {PathBelow(state, machine)}" : "";
        return $"initial state{where}: {PathBelow(initial, machine)}";
    }

    /// <summary>The states that enclose `state`, innermost first, below `machine`.</summary>
    public static List<StateUsage> EnclosingStates(StateUsage state, StateUsage machine)
    {
        var result = new List<StateUsage>();
        var owner = state.owner;
        while (owner != null && !owner.Equals(machine) && owner is StateUsage enclosing)
        {
            result.Add(enclosing);
            owner = enclosing.owner;
        }
        return result;
    }

    private sealed record Visit(StateUsage State, IReadOnlyList<string> Path, bool Enter);

    /// <summary>
    /// The states of `machine` reachable from its initial state, each mapped to a shortest list of
    /// trigger labels that reaches it (empty for a state entered without any trigger).
    ///
    /// `triggers`: the trigger keys that can occur (see TriggerOf); null for every trigger.
    /// `bindings`: values of the features the guards read, by name. A guard that is not known to be
    /// false may hold.
    ///
    /// Entering a state enters every region of a parallel state, or else its initial states
    /// (InitialStates) and the targets of the transitions from its entry action (the form
    /// `entry action initial; transition initial then off;`), and so on down. A transition from a
    /// state is taken from any of its substates too, and entering a nested target makes the states
    /// enclosing it active. Regions of a parallel state are explored independently of each other.
    ///
    /// This is a 0-1 breadth-first search: a triggered transition costs one and goes to the back of
    /// the queue, entering costs nothing and goes to the front. Where several shortest sequences
    /// exist, the queue's order decides which one is reported, and every language keeps that order.
    /// </summary>
    public static Dictionary<StateUsage, IReadOnlyList<string>> Explore(StateUsage machine,
        IReadOnlyCollection<string>? triggers = null, IReadOnlyDictionary<string, object?>? bindings = null)
    {
        var scope = new Scope(null, bindings ?? new Dictionary<string, object?>(), new Dictionary<string, Func<SpecType, object?>>());
        var outgoing = new Dictionary<IElement, List<TransitionUsage>>();
        foreach (var transition in TransitionsOf(machine))
        {
            var source = transition.source!;
            if (!outgoing.TryGetValue(source, out var list)) outgoing[source] = list = new List<TransitionUsage>();
            list.Add(transition);
        }

        var paths = new Dictionary<StateUsage, IReadOnlyList<string>>(); // state -> the shortest trigger labels
        var entered = new HashSet<StateUsage>(); // states whose initial substates have been entered
        var queue = new LinkedList<Visit>();
        queue.AddLast(new Visit(machine, Array.Empty<string>(), true));

        void Follow(TransitionUsage transition, IReadOnlyList<string> path)
        {
            var trigger = TriggerOf(transition);
            if (trigger != null && triggers != null && !triggers.Contains(trigger.Key)) return;
            if (GuardOf(transition, scope) is false) return;
            if (transition.target is not StateUsage target) throw new QueryError($"{NameOf(transition)} does not lead to a state");
            var arrivals = new List<(StateUsage State, bool Enter)> { (target, true) };
            arrivals.AddRange(EnclosingStates(target, machine).Select(s => (s, false)));
            foreach (var (state, enter) in arrivals)
            {
                if (trigger == null) queue.AddFirst(new Visit(state, path, enter));
                else queue.AddLast(new Visit(state, path.Append(trigger.Label).ToList(), enter));
            }
        }

        while (queue.Count > 0)
        {
            var visit = queue.First!.Value;
            queue.RemoveFirst();
            var state = visit.State;
            if (!paths.ContainsKey(state))
            {
                paths[state] = visit.Path;
                foreach (var transition in outgoing.GetValueOrDefault(state) ?? new List<TransitionUsage>())
                    Follow(transition, visit.Path);
            }
            if (visit.Enter && entered.Add(state))
            {
                if (state.isParallel == true)
                {
                    foreach (var region in state.nestedState) queue.AddFirst(new Visit(region, visit.Path, true));
                }
                else
                {
                    foreach (var initial in InitialStates(state)) queue.AddFirst(new Visit(initial, visit.Path, true));
                    if (state.entryAction is { } entry)
                        foreach (var transition in outgoing.GetValueOrDefault(entry) ?? new List<TransitionUsage>())
                            Follow(transition, visit.Path);
                }
            }
        }
        paths.Remove(machine);
        return paths;
    }

    // ---------------------------------------------------------------- structure

    private sealed class DefaultOneMarker
    {
        public override string ToString() => "[1..1]";
    }

    /// <summary>The multiplicity SysML implies for a usage that declares none (see
    /// MultiplicityOf).</summary>
    public static readonly object DefaultOne = new DefaultOneMarker();

    /// <summary>The features that `feature` subsets or redefines by a relationship written in the
    /// model, not an implied one (a part's implied subsetting of the library's `parts`).</summary>
    public static List<Feature> ExplicitSubsettings(Feature feature) =>
        feature.ownedSubsetting.Where(s => s.isImplied != true && s.subsettedFeature != null)
            .Select(s => s.subsettedFeature!).ToList();

    /// <summary>Whether SysML gives `usage` the default multiplicity [1..1] when it declares none and
    /// subsets or redefines nothing explicitly: an attribute, item, part or port usage (not a
    /// connection) owned by a definition or usage.</summary>
    public static bool HasDefaultMultiplicity(Feature usage)
    {
        var kind = (usage is AttributeUsage or ItemUsage or PortUsage) && usage is not ConnectionUsage;
        return kind && usage.owningType is Definition or Usage;
    }

    /// <summary>What says how many instances a usage stands for, by SysML's rule for usages: its own
    /// multiplicity; else that of the usages it explicitly subsets or redefines, nearest first (in a
    /// valid model a redefinition only narrows what it redefines, so the nearest is the tightest);
    /// else DefaultOne, for a usage that has the default [1..1]; else null: nothing constrains it
    /// ([0..*]). The result is a Multiplicity, DefaultOne or null.</summary>
    public static object? MultiplicityOf(Feature usage)
    {
        var seen = new HashSet<Feature> { usage };
        var todo = new Queue<Feature>(new[] { usage });
        while (todo.Count > 0)
        {
            var current = todo.Dequeue();
            if (current.multiplicity != null) return current.multiplicity;
            var subsetted = ExplicitSubsettings(current);
            if (subsetted.Count == 0) return HasDefaultMultiplicity(current) ? DefaultOne : null;
            foreach (var feature in subsetted)
                if (seen.Add(feature)) todo.Enqueue(feature);
        }
        return null;
    }

    /// <summary>How many instances `usage` stands for in `context`. Its multiplicity's bounds,
    /// evaluated in the context (a bound may be an expression: `Rotor[rotorCount]`), must be one
    /// whole number.</summary>
    public static int InstanceCount(Feature usage, SpecType context)
    {
        var multiplicity = MultiplicityOf(usage);
        if (ReferenceEquals(multiplicity, DefaultOne)) return 1;
        if (multiplicity == null)
            throw new QueryError($"{NameOf(usage)} in {NameOf(context)} has no fixed number of instances: [0..*]");
        if (multiplicity is not MultiplicityRange range)
            throw new QueryError($"{NameOf(usage)}: a {((IElement)multiplicity).MetaclassName} is not a range");
        var scope = new Scope(context);
        var upper = range.upperBound != null ? Evaluate(range.upperBound, scope) : Unknown.Value;
        var lower = range.lowerBound != null ? Evaluate(range.lowerBound, scope) : upper;
        if (!(upper is double u && lower is double l && l == u && !double.IsInfinity(u) && u == Math.Round(u)))
            throw new QueryError($"{NameOf(usage)} in {NameOf(context)} has no fixed number of instances: {Fmt(lower)}..{Fmt(upper)}");
        return (int)u;
    }

    /// <summary>The names of a part's definitions.</summary>
    public static string DefinitionName(PartUsage part)
    {
        var names = part.partDefinition.Select(d => NameOf(d)).ToList();
        return names.Count > 0 ? string.Join(", ", names) : "(untyped)";
    }

    /// <summary>How many parts of each definition `root` consists of, at every depth, by definition
    /// name in alphabetical order. A part occurs as often as its instance count times its
    /// owner's.</summary>
    public static SortedDictionary<string, int> BillOfMaterials(SpecType root)
    {
        var counts = new SortedDictionary<string, int>(StringComparer.Ordinal);
        void Walk(SpecType node, int factor)
        {
            foreach (var part in PartUsages(node))
            {
                var count = factor * InstanceCount(part, node);
                var key = DefinitionName(part);
                counts[key] = counts.GetValueOrDefault(key) + count;
                Walk(part, count);
            }
        }
        Walk(root, 1);
        return counts;
    }

    /// <summary>The sum of `attribute` over `node` and every part in it at any depth, each part
    /// counted as often as it occurs, each value the one that holds in its part's context (the most
    /// specific redefinition).</summary>
    public static double Rollup(SpecType node, Feature attribute)
    {
        var own = ValueOf(attribute, new Scope(node));
        if (!IsNumber(own)) throw new QueryError($"{NameOf(node)} has no value for {NameOf(attribute)}");
        double parts = 0;
        foreach (var part in PartUsages(node)) parts += InstanceCount(part, node) * Rollup(part, attribute);
        return Number(own, "+") + parts;
    }

    // ---------------------------------------------------------------- connectivity

    /// <summary>The part that a connector's related feature designates, as names relative to the
    /// connector's owner. The feature is a feature or a feature chain (`rotors.motor`,
    /// `battery.powerOut`); a port at the end of the chain belongs to the part before it.</summary>
    public static List<string> PartPath(Feature feature)
    {
        var chain = feature.chainingFeature.Count > 0 ? feature.chainingFeature.ToList() : new List<Feature> { feature };
        while (chain.Count > 1 && chain[^1] is PortUsage) chain.RemoveAt(chain.Count - 1);
        return chain.Select(f => NameOf(f)).ToList();
    }

    /// <summary>Every connection and flow of `root` and of the parts nested in it, owned or
    /// inherited, from each connector's source feature to each of its target features.</summary>
    public static List<Link> Links(SpecType root)
    {
        var found = new List<Link>();
        void Walk(SpecType node, List<string> prefix)
        {
            foreach (var connector in ModelUsages(node).OfType<ConnectorAsUsage>())
            {
                if (connector.sourceFeature is not { } sourceFeature) continue;
                var source = string.Join(".", prefix.Concat(PartPath(sourceFeature)));
                foreach (var feature in connector.targetFeature)
                {
                    var target = string.Join(".", prefix.Concat(PartPath(feature)));
                    if (connector is FlowUsage flow)
                    {
                        var item = string.Join(", ", flow.payloadType.Select(t => NameOf(t)));
                        found.Add(new Link(source, target, "flow", NameOf(flow), item.Length > 0 ? item : null));
                    }
                    else
                    {
                        found.Add(new Link(source, target, "connection", NameOf(connector), null));
                    }
                }
            }
            foreach (var part in PartUsages(node)) Walk(part, prefix.Append(NameOf(part)).ToList());
        }
        Walk(root, new List<string>());
        return found;
    }

    /// <summary>The parts that the part at path `source` reaches through the links of `root`, in
    /// alphabetical order: along a flow from its source to its target, along a connection either
    /// way. With `item`, through the flows of that item definition only.</summary>
    public static List<string> ReachableParts(SpecType root, string source, string? item = null)
    {
        var adjacent = new Dictionary<string, List<string>>();
        void Add(string from, string to)
        {
            if (!adjacent.TryGetValue(from, out var list)) adjacent[from] = list = new List<string>();
            list.Add(to);
        }
        foreach (var link in Links(root))
        {
            if (item != null && (link.Kind != "flow" || link.Item != item)) continue;
            Add(link.Source, link.Target);
            if (link.Kind == "connection") Add(link.Target, link.Source);
        }
        var seen = new HashSet<string> { source };
        var todo = new Stack<string>(new[] { source });
        while (todo.Count > 0)
            foreach (var next in adjacent.GetValueOrDefault(todo.Pop()) ?? new List<string>())
                if (seen.Add(next)) todo.Push(next);
        seen.Remove(source);
        return seen.OrderBy(s => s, StringComparer.Ordinal).ToList();
    }

    // ---------------------------------------------------------------- requirements

    /// <summary>The constraints a requirement requires, its own and the ones it inherits: those of
    /// its requirement constraint memberships of kind `requirement` (`require constraint`, as
    /// opposed to `assume constraint`). The derived property requiredConstraint has the owned ones
    /// only.</summary>
    public static List<ConstraintUsage> RequiredConstraints(RequirementUsage requirement) =>
        requirement.featureMembership.OfType<RequirementConstraintMembership>()
            .Where(m => m.kind == "requirement").Select(m => m.ownedConstraint!).ToList();

    /// <summary>The expression whose value is the constraint's result, or null.</summary>
    public static Expression? ResultExpression(ConstraintUsage constraint) =>
        constraint.ownedMembership.OfType<ResultExpressionMembership>().FirstOrDefault()?.ownedResultExpression;

    /// <summary>Every required constraint of every satisfied requirement in the model (not in the
    /// standard library), evaluated for the feature that satisfies it: the requirement's subject is
    /// that feature, the requirement's other features have the values the requirement gives them,
    /// and `derived` computes the attributes the model leaves to computation (a total mass is a
    /// roll-up).</summary>
    public static List<Verdict> CheckSatisfactions(Model model, IReadOnlyDictionary<string, Func<SpecType, object?>> derived)
    {
        var verdicts = new List<Verdict>();
        foreach (var satisfy in model.ElementsOfType<SatisfyRequirementUsage>())
        {
            if (satisfy.isLibraryElement == true) continue;
            var requirement = satisfy.satisfiedRequirement!;
            var by = satisfy.satisfyingFeature;
            var bindings = new Dictionary<string, object?>();
            if (requirement.subjectParameter is { } subject) bindings[NameOf(subject)] = by;
            var scope = new Scope(requirement, bindings, derived);
            foreach (var constraint in RequiredConstraints(requirement))
            {
                var expr = ResultExpression(constraint) ?? throw new QueryError($"{NameOf(constraint)} has no result expression");
                var value = Evaluate(expr, scope);
                var operands = expr is OperatorExpression operation
                    ? operation.argument.Select(a => Evaluate(a, scope)).ToList()
                    : new List<object?>();
                verdicts.Add(new Verdict(requirement, by, constraint, value, operands, satisfy.isNegated == true));
            }
        }
        return verdicts;
    }
}
