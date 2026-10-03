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
// attribute values and constraints are the model's own expressions, evaluated by evaluate() in
// three-valued logic: a value that the model does not determine and the caller did not bind is
// UNKNOWN, and a guard that is UNKNOWN may hold.
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
// The same queries exist for every language of the SDK, function for function. Values are Double,
// Boolean, String, an element, or UNKNOWN.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import org.openmbee.sysml.Element;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.classes.*;

public final class Queries {
    private Queries() {
    }

    /** The model does not give a query what it needs: a value, or an expression it can evaluate. */
    public static final class QueryError extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public QueryError(String message) {
            super(message);
        }
    }

    // ---------------------------------------------------------------- names

    /** The element's name. A redefinition declared without a name has the name of the feature it
     * redefines: `attribute :>> mass` is named `mass`. */
    public static String nameOf(Element element) {
        String name = element.getName();
        return name == null || name.isEmpty() ? "(unnamed)" : name;
    }

    /** The names of `element` and its owners up to `top` (not included), outermost first:
     * `operating::pumping::high` for a state two levels below the machine `top`. */
    public static String pathBelow(Element element, Element top) {
        List<String> names = new ArrayList<>();
        while (element != null && !element.equals(top)) {
            names.add(nameOf(element));
            element = element.getOwner();
        }
        Collections.reverse(names);
        return String.join("::", names);
    }

    // ---------------------------------------------------------------- features and their values

    /** The usages of a definition or usage, owned and inherited, without those the standard library
     * contributes (a part inherits `self`, `start`, `subparts`, ... from the library's `Part`). */
    public static List<Usage> modelUsages(Type element) {
        List<Usage> usages = switch (element) {
            case Definition d -> d.getUsage();
            case Usage u -> u.getUsage();
            default -> List.of();
        };
        List<Usage> out = new ArrayList<>();
        for (Usage u : usages)
            if (!Boolean.TRUE.equals(u.getIsLibraryElement()))
                out.add(u);
        return out;
    }

    /** The parts of a definition or usage, owned and inherited. A connection is a part too in SysML;
     * it is not a component, so it is left out. */
    public static List<PartUsage> partUsages(Type element) {
        List<PartUsage> parts = new ArrayList<>();
        for (Usage u : modelUsages(element))
            if (u instanceof PartUsage part && !(u instanceof ConnectionUsage))
                parts.add(part);
        return parts;
    }

    /** Whether `feature` redefines `other`, directly or through a chain of redefinitions. */
    public static boolean redefines(Feature feature, Feature other) {
        Set<Feature> seen = new HashSet<>();
        Deque<Feature> todo = new ArrayDeque<>(List.of(feature));
        while (!todo.isEmpty()) {
            for (Redefinition redefinition : todo.pop().getOwnedRedefinition()) {
                Feature redefined = redefinition.getRedefinedFeature();
                if (redefined != null && redefined.equals(other))
                    return true;
                if (redefined != null && seen.add(redefined))
                    todo.push(redefined);
            }
        }
        return false;
    }

    /** The feature that stands for `feature` in `context`: among the context's features, owned or
     * inherited, the one that is `feature` or redefines it. A type inherits only the most specific
     * redefinition, so this is the one whose value holds in the context. */
    public static Feature featureIn(Type context, Feature feature) {
        for (Feature candidate : context.getFeature())
            if (candidate.equals(feature) || redefines(candidate, feature))
                return candidate;
        return feature;
    }

    /** Whether a feature is one of the values an enumeration definition enumerates (`on` in
     * `enum def IgnitionOnOff { on; off; }`). Those are owned through variant memberships, so they
     * have an owning namespace but no owning type. */
    public static boolean isEnumerationLiteral(Element feature) {
        return feature instanceof EnumerationUsage literal
            && literal.getOwningNamespace() instanceof EnumerationDefinition;
    }

    /** The expression a feature is bound to by its declaration (`= 0.045`), or null. */
    public static Expression valueExpression(Feature feature) {
        for (Membership membership : feature.getOwnedMembership())
            if (membership instanceof FeatureValue value)
                return value.getValue();
        return null;
    }

    // ---------------------------------------------------------------- expressions

    /** A value that neither the model nor the caller determines. */
    public static final Object UNKNOWN = new Object() {
        @Override
        public String toString() {
            return "unknown";
        }
    };

    /**
     * What an expression is evaluated against.
     *
     * @param context  the element whose features give values: a referenced feature has the value of
     *                 the feature that stands for it in the context (featureIn)
     * @param bindings values by feature name, which take precedence over the model's own
     * @param derived  attributes the caller computes, by name: a function of the element that has
     *                 the attribute (`vehicle.totalMass` is derived.get("totalMass").apply(vehicle))
     */
    public record Scope(Type context, Map<String, Object> bindings, Map<String, Function<Type, Object>> derived) {
        public Scope(Type context) {
            this(context, Map.of(), Map.of());
        }

        public Scope within(Type context) {
            return new Scope(context, bindings, derived);
        }
    }

    /** The value of `expr`: a Double, a Boolean, a String, an element, or UNKNOWN. */
    public static Object evaluate(Expression expr, Scope scope) {
        if (expr instanceof LiteralInteger literal)
            return literal.getValue().doubleValue();
        if (expr instanceof LiteralRational literal)
            return literal.getValue();
        if (expr instanceof LiteralBoolean literal)
            return literal.getValue();
        if (expr instanceof LiteralString literal)
            return literal.getValue();
        if (expr instanceof LiteralInfinity)
            return Double.POSITIVE_INFINITY;
        if (expr instanceof FeatureChainExpression chain) // before OperatorExpression, which it specializes
            return valueIn(evaluate(chain.getArgument().get(0), scope), chain.getTargetFeature(), scope);
        if (expr instanceof FeatureReferenceExpression reference)
            return valueOf(reference.getReferent(), scope);
        if (expr instanceof OperatorExpression operation)
            return applyOperator(operation.getOperator(), operation.getArgument(), scope);
        throw new QueryError("cannot evaluate a " + expr.$metaclass());
    }

    /** The value of a referenced feature: a binding of its name; an enumeration literal, which is its
     * own value; else the value the model gives the feature that stands for it in the scope's
     * context; else UNKNOWN. */
    public static Object valueOf(Feature feature, Scope scope) {
        // The second operand of `and`, `or` and `implies` is a reference to an expression, evaluated
        // only when the operator needs it.
        if (feature instanceof Expression expression)
            return evaluate(expression, scope);
        String name = nameOf(feature);
        if (scope.bindings().containsKey(name))
            return scope.bindings().get(name);
        if (isEnumerationLiteral(feature))
            return feature;
        if (scope.context() != null)
            feature = featureIn(scope.context(), feature);
        Expression expr = valueExpression(feature);
        return expr == null ? UNKNOWN : evaluate(expr, scope);
    }

    /** `source.feature`: a derived attribute that the caller computes, else the value of the feature
     * in the context of `source`. */
    public static Object valueIn(Object source, Feature feature, Scope scope) {
        if (source == UNKNOWN)
            return UNKNOWN;
        if (!(source instanceof Type context))
            throw new QueryError(fmt(source) + " has no feature " + nameOf(feature));
        Function<Type, Object> derive = scope.derived().get(nameOf(feature));
        if (derive != null)
            return derive.apply(context);
        return valueOf(feature, scope.within(context));
    }

    /** An operator over its argument expressions. `and`, `or` and `implies` evaluate their second
     * operand only when the first does not decide, as SysML's conditional operators do; all the
     * logical operators follow three-valued logic. Any other operator on an UNKNOWN is UNKNOWN. */
    public static Object applyOperator(String op, List<Expression> args, Scope scope) {
        if (op.equals("and") || op.equals("or") || op.equals("implies"))
            return conditional(op, args, scope);
        List<Object> values = new ArrayList<>();
        for (Expression arg : args)
            values.add(evaluate(arg, scope));
        if (op.equals("not") && values.size() == 1) {
            Object v = truth(values.get(0), op);
            return v == UNKNOWN ? UNKNOWN : !(Boolean) v;
        }
        if (op.equals("xor") && values.size() == 2) {
            Object a = truth(values.get(0), op), b = truth(values.get(1), op);
            return a == UNKNOWN || b == UNKNOWN ? UNKNOWN : !a.equals(b);
        }
        if (values.contains(UNKNOWN))
            return UNKNOWN;
        if ((op.equals("==") || op.equals("!=")) && values.size() == 2) {
            boolean equal = same(values.get(0), values.get(1));
            return op.equals("==") ? equal : !equal;
        }
        List<Double> numbers = new ArrayList<>();
        for (Object v : values)
            numbers.add(number(v, op));
        if (numbers.size() == 1 && (op.equals("-") || op.equals("+")))
            return op.equals("-") ? -numbers.get(0) : numbers.get(0);
        if (numbers.size() == 2) {
            double a = numbers.get(0), b = numbers.get(1);
            switch (op) {
                case "<": return a < b;
                case "<=": return a <= b;
                case ">": return a > b;
                case ">=": return a >= b;
                case "+": return a + b;
                case "-": return a - b;
                case "*": return a * b;
                case "/": return a / b;
                default: break;
            }
        }
        throw new QueryError("operator '" + op + "' on " + args.size() + " operand(s) is not supported");
    }

    private static Object conditional(String op, List<Expression> args, Scope scope) {
        Object left = truth(evaluate(args.get(0), scope), op);
        if ((op.equals("and") && Boolean.FALSE.equals(left)) || (op.equals("or") && Boolean.TRUE.equals(left)))
            return left;
        if (op.equals("implies") && Boolean.FALSE.equals(left))
            return true;
        Object right = truth(evaluate(args.get(1), scope), op);
        boolean unknown = left == UNKNOWN || right == UNKNOWN;
        if (op.equals("and"))
            return Boolean.FALSE.equals(right) ? (Object) false : unknown ? UNKNOWN : true;
        return Boolean.TRUE.equals(right) ? (Object) true : unknown ? UNKNOWN : false; // or, implies
    }

    private static Object truth(Object value, String op) {
        if (value == UNKNOWN || value instanceof Boolean)
            return value;
        throw new QueryError("'" + op + "' needs a Boolean, not " + fmt(value));
    }

    private static double number(Object value, String op) {
        if (value instanceof Number n)
            return n.doubleValue();
        throw new QueryError("'" + op + "' needs a number, not " + fmt(value));
    }

    /** Equality within a kind of value: numbers by value, elements by identity. */
    private static boolean same(Object a, Object b) {
        if (a instanceof Number x && b instanceof Number y)
            return x.doubleValue() == y.doubleValue();
        return !(a instanceof Number) && !(b instanceof Number) && a.equals(b);
    }

    public static boolean isNumber(Object value) {
        return value instanceof Number;
    }

    /** A value for a report: numbers with at most three decimals, elements by name. */
    public static String fmt(Object value) {
        if (value == UNKNOWN)
            return "unknown";
        if (value instanceof Boolean b)
            return b ? "true" : "false";
        if (value instanceof Number n) {
            double d = n.doubleValue();
            if (Double.isInfinite(d))
                return "*";
            // Rounded from the exact binary value, as printf does; String.format would round the
            // shortest decimal form instead.
            String text = new BigDecimal(d).setScale(3, RoundingMode.HALF_EVEN).toPlainString()
                .replaceAll("0+$", "").replaceAll("\\.$", "");
            return text.equals("-0") ? "0" : text;
        }
        if (value instanceof String s)
            return "\"" + s + "\"";
        return nameOf((Element) value);
    }

    /** An expression in SysML notation, with every nested operation in parentheses. */
    public static String render(Expression expr) {
        if (expr instanceof LiteralInteger literal)
            return fmt(literal.getValue().doubleValue());
        if (expr instanceof LiteralRational literal)
            return fmt(literal.getValue());
        if (expr instanceof LiteralBoolean literal)
            return literal.getValue() ? "true" : "false";
        if (expr instanceof LiteralString literal)
            return "\"" + literal.getValue() + "\"";
        if (expr instanceof LiteralInfinity)
            return "*";
        if (expr instanceof FeatureChainExpression chain)
            return render(chain.getArgument().get(0)) + "." + nameOf(chain.getTargetFeature());
        if (expr instanceof FeatureReferenceExpression reference) {
            Feature referent = reference.getReferent();
            if (referent instanceof Expression expression)
                return render(expression);
            if (isEnumerationLiteral(referent))
                return nameOf(referent.getOwningNamespace()) + "::" + nameOf(referent);
            return nameOf(referent);
        }
        if (expr instanceof TriggerInvocationExpression trigger)
            return trigger.getKind() + " " + render(trigger.getArgument().get(0));
        if (expr instanceof OperatorExpression operation) {
            List<String> operands = new ArrayList<>();
            for (Expression arg : operation.getArgument())
                operands.add(operand(arg));
            String op = operation.getOperator();
            if (operands.size() == 1)
                return op.chars().allMatch(Character::isLetter) ? op + " " + operands.get(0) : op + operands.get(0);
            return String.join(" " + op + " ", operands);
        }
        return "<" + expr.$metaclass() + ">";
    }

    private static String operand(Expression expr) {
        if (expr instanceof FeatureReferenceExpression reference && reference.getReferent() instanceof Expression referent)
            expr = referent;
        String text = render(expr);
        boolean nested = expr instanceof OperatorExpression && !(expr instanceof FeatureChainExpression);
        return nested ? "(" + text + ")" : text;
    }

    // ---------------------------------------------------------------- state machines

    /** Every state nested in `machine` at any depth, in the order the model declares them, each state
     * before its substates. */
    public static List<StateUsage> statesOf(StateUsage machine) {
        List<StateUsage> states = new ArrayList<>();
        for (StateUsage state : machine.getNestedState()) {
            states.add(state);
            states.addAll(statesOf(state));
        }
        return states;
    }

    /** Every transition declared in `machine` or in a state nested in it. */
    public static List<TransitionUsage> transitionsOf(StateUsage machine) {
        List<StateUsage> states = new ArrayList<>(List.of(machine));
        states.addAll(statesOf(machine));
        List<TransitionUsage> transitions = new ArrayList<>();
        for (StateUsage state : states)
            transitions.addAll(state.getNestedTransition());
        return transitions;
    }

    /** The states `state` starts in: the targets of the successions it owns (`first start then off;`);
     * a transition is not a succession of this kind. Only the target is read: the source, the start
     * of the state, is a feature of the standard library, which a model loaded without the library,
     * or read from a payload, cannot resolve. */
    public static List<StateUsage> initialStates(StateUsage state) {
        List<StateUsage> initial = new ArrayList<>();
        for (Feature feature : state.getOwnedFeature())
            if (feature instanceof SuccessionAsUsage succession)
                for (Feature target : succession.getTargetFeature())
                    if (target instanceof StateUsage targetState)
                        initial.add(targetState);
        return initial;
    }

    /** A transition's trigger. The key is what must occur, as explore() selects triggers by it: the
     * name of the accepted signal's definition, or `at`, `after` or `when` for a time or change
     * event. The label says it in full (`at maintenanceTime`). */
    public record Trigger(String key, String label) {
    }

    /** The transition's trigger, or null for a transition without one. */
    public static Trigger triggerOf(TransitionUsage transition) {
        for (AcceptActionUsage accept : transition.getTriggerAction()) {
            if (accept.getPayloadArgument() instanceof TriggerInvocationExpression argument)
                return new Trigger(argument.getKind(), render(argument));
            ReferenceUsage payload = accept.getPayloadParameter();
            List<Type> types = payload != null ? payload.getType() : List.of();
            String name = types.isEmpty() ? "any" : nameOf(types.get(0));
            return new Trigger(name, name);
        }
        return null;
    }

    /** Whether the transition's guards hold: true if it has none, false if one is known to be false,
     * else true or UNKNOWN. */
    public static Object guardOf(TransitionUsage transition, Scope scope) {
        Object result = true;
        for (Expression guard : transition.getGuardExpression()) {
            Object value = truth(evaluate(guard, scope), "if");
            if (Boolean.FALSE.equals(value))
                return false;
            if (value == UNKNOWN)
                result = UNKNOWN;
        }
        return result;
    }

    /** `starting -> operating on SelfTestDone if selfTestPassed and (temperature < maxTemperature)` */
    public static String describeTransition(TransitionUsage transition, StateUsage machine) {
        String text = pathBelow(transition.getSource(), machine) + " -> " + pathBelow(transition.getTarget(), machine);
        Trigger trigger = triggerOf(transition);
        if (trigger != null)
            text += " on " + trigger.label();
        List<String> guards = new ArrayList<>();
        for (Expression guard : transition.getGuardExpression())
            guards.add(render(guard));
        if (!guards.isEmpty())
            text += " if " + String.join(" and ", guards);
        return text;
    }

    /** `initial state in operating: operating::idle` */
    public static String describeInitial(StateUsage state, StateUsage initial, StateUsage machine) {
        String where = !state.equals(machine) ? " in " + pathBelow(state, machine) : "";
        return "initial state" + where + ": " + pathBelow(initial, machine);
    }

    /** The states that enclose `state`, innermost first, below `machine`. */
    public static List<StateUsage> enclosingStates(StateUsage state, StateUsage machine) {
        List<StateUsage> out = new ArrayList<>();
        Element owner = state.getOwner();
        while (owner != null && !owner.equals(machine) && owner instanceof StateUsage enclosing) {
            out.add(enclosing);
            owner = enclosing.getOwner();
        }
        return out;
    }

    private record Visit(StateUsage state, List<String> path, boolean enter) {
    }

    /**
     * The states of `machine` reachable from its initial state, each mapped to a shortest list of
     * trigger labels that reaches it (empty for a state entered without any trigger).
     *
     * @param triggers the trigger keys that can occur (see triggerOf); null for every trigger
     * @param bindings values of the features the guards read, by name. A guard that is not known to
     *                 be false may hold.
     *
     * Entering a state enters every region of a parallel state, or else its initial states
     * (initialStates) and the targets of the transitions from its entry action (the form
     * `entry action initial; transition initial then off;`), and so on down. A transition from a
     * state is taken from any of its substates too, and entering a nested target makes the states
     * enclosing it active. Regions of a parallel state are explored independently of each other.
     *
     * This is a 0-1 breadth-first search: a triggered transition costs one and goes to the back of
     * the queue, entering costs nothing and goes to the front. Where several shortest sequences
     * exist, the queue's order decides which one is reported, and every language keeps that order.
     */
    public static Map<StateUsage, List<String>> explore(StateUsage machine, Collection<String> triggers,
                                                        Map<String, Object> bindings) {
        Scope scope = new Scope(null, bindings != null ? bindings : Map.of(), Map.of());
        Map<Element, List<TransitionUsage>> outgoing = new HashMap<>();
        for (TransitionUsage transition : transitionsOf(machine))
            outgoing.computeIfAbsent(transition.getSource(), k -> new ArrayList<>()).add(transition);

        Map<StateUsage, List<String>> paths = new LinkedHashMap<>(); // state -> the shortest trigger labels
        Set<StateUsage> entered = new HashSet<>(); // states whose initial substates have been entered
        Deque<Visit> queue = new ArrayDeque<>(List.of(new Visit(machine, List.of(), true)));

        while (!queue.isEmpty()) {
            Visit visit = queue.pollFirst();
            StateUsage state = visit.state();
            if (!paths.containsKey(state)) {
                paths.put(state, visit.path());
                for (TransitionUsage transition : outgoing.getOrDefault(state, List.of()))
                    follow(transition, visit.path(), machine, triggers, scope, queue);
            }
            if (visit.enter() && entered.add(state)) {
                if (Boolean.TRUE.equals(state.getIsParallel())) {
                    for (StateUsage region : state.getNestedState())
                        queue.addFirst(new Visit(region, visit.path(), true));
                } else {
                    for (StateUsage initial : initialStates(state))
                        queue.addFirst(new Visit(initial, visit.path(), true));
                    if (state.getEntryAction() != null)
                        for (TransitionUsage transition : outgoing.getOrDefault(state.getEntryAction(), List.of()))
                            follow(transition, visit.path(), machine, triggers, scope, queue);
                }
            }
        }
        paths.remove(machine);
        return paths;
    }

    private static void follow(TransitionUsage transition, List<String> path, StateUsage machine,
                               Collection<String> triggers, Scope scope, Deque<Visit> queue) {
        Trigger trigger = triggerOf(transition);
        if (trigger != null && triggers != null && !triggers.contains(trigger.key()))
            return;
        if (Boolean.FALSE.equals(guardOf(transition, scope)))
            return;
        if (!(transition.getTarget() instanceof StateUsage target))
            throw new QueryError(nameOf(transition) + " does not lead to a state");
        List<Visit> arrivals = new ArrayList<>(List.of(new Visit(target, path, true)));
        for (StateUsage enclosing : enclosingStates(target, machine))
            arrivals.add(new Visit(enclosing, path, false));
        for (Visit arrival : arrivals) {
            if (trigger == null) {
                queue.addFirst(arrival);
            } else {
                List<String> longer = new ArrayList<>(path);
                longer.add(trigger.label());
                queue.addLast(new Visit(arrival.state(), longer, arrival.enter()));
            }
        }
    }

    // ---------------------------------------------------------------- structure

    /** The multiplicity SysML implies for a usage that declares none (see multiplicityOf). */
    public static final Object DEFAULT_ONE = new Object() {
        @Override
        public String toString() {
            return "[1..1]";
        }
    };

    /** The features that `feature` subsets or redefines by a relationship written in the model, not
     * an implied one (a part's implied subsetting of the library's `parts`). */
    public static List<Feature> explicitSubsettings(Feature feature) {
        List<Feature> subsetted = new ArrayList<>();
        for (Subsetting subsetting : feature.getOwnedSubsetting())
            if (!Boolean.TRUE.equals(subsetting.getIsImplied()) && subsetting.getSubsettedFeature() != null)
                subsetted.add(subsetting.getSubsettedFeature());
        return subsetted;
    }

    /** Whether SysML gives `usage` the default multiplicity [1..1] when it declares none and subsets
     * or redefines nothing explicitly: an attribute, item, part or port usage (not a connection)
     * owned by a definition or usage. */
    public static boolean hasDefaultMultiplicity(Feature usage) {
        boolean kind = (usage instanceof AttributeUsage || usage instanceof ItemUsage || usage instanceof PortUsage)
            && !(usage instanceof ConnectionUsage);
        return kind && (usage.getOwningType() instanceof Definition || usage.getOwningType() instanceof Usage);
    }

    /** What says how many instances a usage stands for, by SysML's rule for usages: its own
     * multiplicity; else that of the usages it explicitly subsets or redefines, nearest first (in a
     * valid model a redefinition only narrows what it redefines, so the nearest is the tightest);
     * else DEFAULT_ONE, for a usage that has the default [1..1]; else null: nothing constrains it
     * ([0..*]). The result is a Multiplicity, DEFAULT_ONE or null. */
    public static Object multiplicityOf(Feature usage) {
        Set<Feature> seen = new HashSet<>(List.of(usage));
        Deque<Feature> todo = new ArrayDeque<>(List.of(usage));
        while (!todo.isEmpty()) {
            Feature current = todo.pollFirst();
            if (current.getMultiplicity() != null)
                return current.getMultiplicity();
            List<Feature> subsetted = explicitSubsettings(current);
            if (subsetted.isEmpty())
                return hasDefaultMultiplicity(current) ? DEFAULT_ONE : null;
            for (Feature feature : subsetted)
                if (seen.add(feature))
                    todo.addLast(feature);
        }
        return null;
    }

    /** How many instances `usage` stands for in `context`. Its multiplicity's bounds, evaluated in the
     * context (a bound may be an expression: `Rotor[rotorCount]`), must be one whole number. */
    public static int instanceCount(Feature usage, Type context) {
        Object multiplicity = multiplicityOf(usage);
        if (multiplicity == DEFAULT_ONE)
            return 1;
        if (multiplicity == null)
            throw new QueryError(nameOf(usage) + " in " + nameOf(context) + " has no fixed number of instances: [0..*]");
        if (!(multiplicity instanceof MultiplicityRange range))
            throw new QueryError(nameOf(usage) + ": a " + ((Element) multiplicity).$metaclass() + " is not a range");
        Scope scope = new Scope(context);
        Object upper = range.getUpperBound() != null ? evaluate(range.getUpperBound(), scope) : UNKNOWN;
        Object lower = range.getLowerBound() != null ? evaluate(range.getLowerBound(), scope) : upper;
        if (!(upper instanceof Double u && lower instanceof Double l && l.equals(u) && !u.isInfinite() && u == Math.rint(u)))
            throw new QueryError(nameOf(usage) + " in " + nameOf(context) + " has no fixed number of instances: "
                + fmt(lower) + ".." + fmt(upper));
        return (int) (double) u;
    }

    /** The names of a part's definitions. */
    public static String definitionName(PartUsage part) {
        List<String> names = new ArrayList<>();
        for (PartDefinition definition : part.getPartDefinition())
            names.add(nameOf(definition));
        return names.isEmpty() ? "(untyped)" : String.join(", ", names);
    }

    /** How many parts of each definition `root` consists of, at every depth, by definition name in
     * alphabetical order. A part occurs as often as its instance count times its owner's. */
    public static Map<String, Integer> billOfMaterials(Type root) {
        Map<String, Integer> counts = new TreeMap<>();
        countParts(root, 1, counts);
        return counts;
    }

    private static void countParts(Type node, int factor, Map<String, Integer> counts) {
        for (PartUsage part : partUsages(node)) {
            int count = factor * instanceCount(part, node);
            counts.merge(definitionName(part), count, Integer::sum);
            countParts(part, count, counts);
        }
    }

    /** The sum of `attribute` over `node` and every part in it at any depth, each part counted as often
     * as it occurs, each value the one that holds in its part's context (the most specific
     * redefinition). */
    public static double rollup(Type node, Feature attribute) {
        Object own = valueOf(attribute, new Scope(node));
        if (!(own instanceof Number n))
            throw new QueryError(nameOf(node) + " has no value for " + nameOf(attribute));
        double parts = 0;
        for (PartUsage part : partUsages(node))
            parts += instanceCount(part, node) * rollup(part, attribute);
        return n.doubleValue() + parts;
    }

    // ---------------------------------------------------------------- connectivity

    /** A connection or flow between two parts, by their paths below the root (`rotors.motor`).
     *
     * @param kind "connection" or "flow"
     * @param item what a flow carries: its payload's definition; null for a connection */
    public record Link(String source, String target, String kind, String name, String item) {
        public String describe() {
            String arrow = kind.equals("flow") ? "->" : "--";
            String carries = item != null ? " of " + item : "";
            return source + " " + arrow + " " + target + "  (" + kind + " " + name + carries + ")";
        }
    }

    /** The part that a connector's related feature designates, as names relative to the connector's
     * owner. The feature is a feature or a feature chain (`rotors.motor`, `battery.powerOut`); a port
     * at the end of the chain belongs to the part before it. */
    public static List<String> partPath(Feature feature) {
        List<Feature> chain = new ArrayList<>(feature.getChainingFeature());
        if (chain.isEmpty())
            chain.add(feature);
        while (chain.size() > 1 && chain.get(chain.size() - 1) instanceof PortUsage)
            chain.remove(chain.size() - 1);
        List<String> names = new ArrayList<>();
        for (Feature f : chain)
            names.add(nameOf(f));
        return names;
    }

    /** Every connection and flow of `root` and of the parts nested in it, owned or inherited, from each
     * connector's source feature to each of its target features. */
    public static List<Link> links(Type root) {
        List<Link> found = new ArrayList<>();
        collectLinks(root, List.of(), found);
        return found;
    }

    private static void collectLinks(Type node, List<String> prefix, List<Link> found) {
        for (Usage usage : modelUsages(node)) {
            if (!(usage instanceof ConnectorAsUsage connector) || connector.getSourceFeature() == null)
                continue;
            String source = joined(prefix, partPath(connector.getSourceFeature()));
            for (Feature feature : connector.getTargetFeature()) {
                String target = joined(prefix, partPath(feature));
                if (connector instanceof FlowUsage flow) {
                    List<String> items = new ArrayList<>();
                    for (Classifier type : flow.getPayloadType())
                        items.add(nameOf(type));
                    found.add(new Link(source, target, "flow", nameOf(flow), items.isEmpty() ? null : String.join(", ", items)));
                } else {
                    found.add(new Link(source, target, "connection", nameOf(connector), null));
                }
            }
        }
        for (PartUsage part : partUsages(node)) {
            List<String> longer = new ArrayList<>(prefix);
            longer.add(nameOf(part));
            collectLinks(part, longer, found);
        }
    }

    private static String joined(List<String> prefix, List<String> path) {
        List<String> all = new ArrayList<>(prefix);
        all.addAll(path);
        return String.join(".", all);
    }

    /** The parts that the part at path `source` reaches through the links of `root`, in alphabetical
     * order: along a flow from its source to its target, along a connection either way. With `item`,
     * through the flows of that item definition only (null for every link). */
    public static List<String> reachableParts(Type root, String source, String item) {
        Map<String, List<String>> adjacent = new HashMap<>();
        for (Link link : links(root)) {
            if (item != null && (!link.kind().equals("flow") || !item.equals(link.item())))
                continue;
            adjacent.computeIfAbsent(link.source(), k -> new ArrayList<>()).add(link.target());
            if (link.kind().equals("connection"))
                adjacent.computeIfAbsent(link.target(), k -> new ArrayList<>()).add(link.source());
        }
        Set<String> seen = new HashSet<>(List.of(source));
        Deque<String> todo = new ArrayDeque<>(List.of(source));
        while (!todo.isEmpty()) {
            for (String next : adjacent.getOrDefault(todo.pop(), List.of()))
                if (seen.add(next))
                    todo.push(next);
        }
        seen.remove(source);
        return new ArrayList<>(new TreeSet<>(seen));
    }

    // ---------------------------------------------------------------- requirements

    /** One required constraint of a satisfied requirement, evaluated for the satisfying feature.
     *
     * @param value    true, false or UNKNOWN
     * @param operands the values of the constraint's operands, for the report
     * @param negated  `not satisfy`: the constraint is expected not to hold */
    public record Verdict(RequirementUsage requirement, Feature satisfiedBy, ConstraintUsage constraint,
                          Object value, List<Object> operands, boolean negated) {
        public String outcome() {
            if (value == UNKNOWN)
                return "unknown";
            return !value.equals(negated) ? "holds" : "VIOLATED";
        }
    }

    /** The constraints a requirement requires, its own and the ones it inherits: those of its
     * requirement constraint memberships of kind `requirement` (`require constraint`, as opposed to
     * `assume constraint`). The derived property requiredConstraint has the owned ones only. */
    public static List<ConstraintUsage> requiredConstraints(RequirementUsage requirement) {
        List<ConstraintUsage> constraints = new ArrayList<>();
        for (FeatureMembership membership : requirement.getFeatureMembership())
            if (membership instanceof RequirementConstraintMembership required && "requirement".equals(required.getKind()))
                constraints.add(required.getOwnedConstraint());
        return constraints;
    }

    /** The expression whose value is the constraint's result, or null. */
    public static Expression resultExpression(ConstraintUsage constraint) {
        for (Membership membership : constraint.getOwnedMembership())
            if (membership instanceof ResultExpressionMembership result)
                return result.getOwnedResultExpression();
        return null;
    }

    /** Every required constraint of every satisfied requirement in the model (not in the standard
     * library), evaluated for the feature that satisfies it: the requirement's subject is that
     * feature, the requirement's other features have the values the requirement gives them, and
     * `derived` computes the attributes the model leaves to computation (a total mass is a roll-up). */
    public static List<Verdict> checkSatisfactions(Model model, Map<String, Function<Type, Object>> derived) {
        List<Verdict> verdicts = new ArrayList<>();
        for (SatisfyRequirementUsage satisfy : model.elementsOfType(SatisfyRequirementUsage.class)) {
            if (Boolean.TRUE.equals(satisfy.getIsLibraryElement()))
                continue;
            RequirementUsage requirement = satisfy.getSatisfiedRequirement();
            Feature by = satisfy.getSatisfyingFeature();
            Usage subject = requirement.getSubjectParameter();
            Map<String, Object> bindings = new HashMap<>();
            if (subject != null)
                bindings.put(nameOf(subject), by);
            Scope scope = new Scope(requirement, bindings, derived);
            for (ConstraintUsage constraint : requiredConstraints(requirement)) {
                Expression expr = resultExpression(constraint);
                if (expr == null)
                    throw new QueryError(nameOf(constraint) + " has no result expression");
                Object value = evaluate(expr, scope);
                List<Object> operands = new ArrayList<>();
                if (expr instanceof OperatorExpression operation)
                    for (Expression arg : operation.getArgument())
                        operands.add(evaluate(arg, scope));
                verdicts.add(new Verdict(requirement, by, constraint, value, operands, Boolean.TRUE.equals(satisfy.getIsNegated())));
            }
        }
        return verdicts;
    }
}
