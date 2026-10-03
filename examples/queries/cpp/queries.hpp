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
// unknown, and a guard that is unknown may hold.
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
// The same queries exist for every language of the SDK, function for function. Elements are
// values; the sets and maps here are keyed by element_id(), the identity elements compare by.
#pragma once

#include <algorithm>
#include <cmath>
#include <cstdio>
#include <deque>
#include <functional>
#include <limits>
#include <map>
#include <optional>
#include <set>
#include <stdexcept>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <variant>
#include <vector>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>

namespace queries {

using namespace sysml;

/// The model does not give a query what it needs: a value, or an expression it can evaluate.
class QueryError : public std::runtime_error {
public:
    using std::runtime_error::runtime_error;
};

// ---------------------------------------------------------------- names

/// The element's name. A redefinition declared without a name has the name of the feature it
/// redefines: `attribute :>> mass` is named `mass`.
inline std::string name_of(const Element& element) {
    std::optional<std::string> name = element.getName();
    return name && !name->empty() ? *name : "(unnamed)";
}

/// The names of `element` and its owners up to `top` (not included), outermost first:
/// `operating::pumping::high` for a state two levels below the machine `top`.
inline std::string path_below(const Element& element, const Element& top) {
    std::vector<std::string> names;
    for (std::optional<Element> e = element; e && *e != top; e = e->getOwner()) names.push_back(name_of(*e));
    std::string path;
    for (auto it = names.rbegin(); it != names.rend(); ++it) path += (path.empty() ? "" : "::") + *it;
    return path;
}

inline std::string join(const std::vector<std::string>& parts, const std::string& separator) {
    std::string out;
    for (std::size_t i = 0; i < parts.size(); ++i) out += (i ? separator : "") + parts[i];
    return out;
}

// ---------------------------------------------------------------- features and their values

/// The usages of a definition or usage, owned and inherited, without those the standard library
/// contributes (a part inherits `self`, `start`, `subparts`, ... from the library's `Part`).
inline std::vector<Usage> model_usages(const Type& element) {
    std::vector<Usage> usages;
    if (element.is_a<Definition>()) usages = element.as<Definition>().getUsage();
    else if (element.is_a<Usage>()) usages = element.as<Usage>().getUsage();
    std::vector<Usage> out;
    for (const Usage& u : usages)
        if (u.getIsLibraryElement() != true) out.push_back(u);
    return out;
}

/// The parts of a definition or usage, owned and inherited. A connection is a part too in SysML;
/// it is not a component, so it is left out.
inline std::vector<PartUsage> part_usages(const Type& element) {
    std::vector<PartUsage> parts;
    for (const Usage& u : model_usages(element))
        if (u.is_a<PartUsage>() && !u.is_a<ConnectionUsage>()) parts.push_back(u.as<PartUsage>());
    return parts;
}

/// Whether `feature` redefines `other`, directly or through a chain of redefinitions.
inline bool redefines(const Feature& feature, const Feature& other) {
    std::unordered_set<std::string> seen;
    std::vector<Feature> todo{feature};
    while (!todo.empty()) {
        Feature current = todo.back();
        todo.pop_back();
        for (const Redefinition& redefinition : current.getOwnedRedefinition()) {
            std::optional<Feature> redefined = redefinition.getRedefinedFeature();
            if (redefined && *redefined == other) return true;
            if (redefined && seen.insert(redefined->element_id()).second) todo.push_back(*redefined);
        }
    }
    return false;
}

/// The feature that stands for `feature` in `context`: among the context's features, owned or
/// inherited, the one that is `feature` or redefines it. A type inherits only the most specific
/// redefinition, so this is the one whose value holds in the context.
inline Feature feature_in(const Type& context, const Feature& feature) {
    for (const Feature& candidate : context.getFeature())
        if (candidate == feature || redefines(candidate, feature)) return candidate;
    return feature;
}

/// Whether a feature is one of the values an enumeration definition enumerates (`on` in
/// `enum def IgnitionOnOff { on; off; }`). Those are owned through variant memberships, so they
/// have an owning namespace but no owning type.
inline bool is_enumeration_literal(const Element& feature) {
    if (!feature.is_a<EnumerationUsage>()) return false;
    std::optional<Namespace> owner = feature.as<EnumerationUsage>().getOwningNamespace();
    return owner && owner->is_a<EnumerationDefinition>();
}

/// The expression a feature is bound to by its declaration (`= 0.045`), if any.
inline std::optional<Expression> value_expression(const Feature& feature) {
    for (const Membership& membership : feature.getOwnedMembership())
        if (membership.is_a<FeatureValue>()) return membership.as<FeatureValue>().getValue();
    return std::nullopt;
}

// ---------------------------------------------------------------- expressions

/// A value that neither the model nor the caller determines.
struct Unknown {
    friend bool operator==(Unknown, Unknown) { return true; }
    friend bool operator!=(Unknown, Unknown) { return false; }
};

/// A value of an expression: unknown, a Boolean, a number, a string, or an element.
using Value = std::variant<Unknown, bool, double, std::string, Element>;

inline bool is_unknown(const Value& v) { return std::holds_alternative<Unknown>(v); }
inline bool is_number(const Value& v) { return std::holds_alternative<double>(v); }
inline Value element_value(const Element& e) { return Value(std::in_place_type<Element>, e); }

/// What an expression is evaluated against.
struct Scope {
    /// The element whose features give values: a referenced feature has the value of the feature
    /// that stands for it in the context (feature_in).
    std::optional<Type> context;
    /// Values by feature name, which take precedence over the model's own.
    std::map<std::string, Value> bindings;
    /// Attributes the caller computes, by name: a function of the element that has the attribute
    /// (`vehicle.totalMass` is derived["totalMass"](vehicle)).
    std::map<std::string, std::function<Value(const Type&)>> derived;

    Scope within(const Type& element) const {
        Scope s = *this;
        s.context = element;
        return s;
    }
};

inline std::string fmt(const Value& value);
inline Value evaluate(const Expression& expr, const Scope& scope);

/// The value of a referenced feature: a binding of its name; an enumeration literal, which is its
/// own value; else the value the model gives the feature that stands for it in the scope's
/// context; else unknown.
inline Value value_of(const Feature& feature, const Scope& scope) {
    // The second operand of `and`, `or` and `implies` is a reference to an expression, evaluated
    // only when the operator needs it.
    if (feature.is_a<Expression>()) return evaluate(feature.as<Expression>(), scope);
    auto bound = scope.bindings.find(name_of(feature));
    if (bound != scope.bindings.end()) return bound->second;
    if (is_enumeration_literal(feature)) return element_value(feature);
    Feature standing = scope.context ? feature_in(*scope.context, feature) : feature;
    std::optional<Expression> expr = value_expression(standing);
    return expr ? evaluate(*expr, scope) : Value(Unknown{});
}

/// `source.feature`: a derived attribute that the caller computes, else the value of the feature
/// in the context of `source`.
inline Value value_in(const Value& source, const Feature& feature, const Scope& scope) {
    if (is_unknown(source)) return Unknown{};
    const Element* element = std::get_if<Element>(&source);
    if (!element || !element->is_a<Type>()) throw QueryError(fmt(source) + " has no feature " + name_of(feature));
    Type context = element->as<Type>();
    auto derive = scope.derived.find(name_of(feature));
    if (derive != scope.derived.end()) return derive->second(context);
    return value_of(feature, scope.within(context));
}

inline Value truth(const Value& value, const std::string& op) {
    if (is_unknown(value) || std::holds_alternative<bool>(value)) return value;
    throw QueryError("'" + op + "' needs a Boolean, not " + fmt(value));
}

inline double number(const Value& value, const std::string& op) {
    if (const double* d = std::get_if<double>(&value)) return *d;
    throw QueryError("'" + op + "' needs a number, not " + fmt(value));
}

/// Equality within a kind of value: numbers by value, elements by identity.
inline bool same(const Value& a, const Value& b) {
    if (a.index() != b.index()) return false;
    if (auto x = std::get_if<bool>(&a)) return *x == std::get<bool>(b);
    if (auto x = std::get_if<double>(&a)) return *x == std::get<double>(b);
    if (auto x = std::get_if<std::string>(&a)) return *x == std::get<std::string>(b);
    if (auto x = std::get_if<Element>(&a)) return *x == std::get<Element>(b);
    return false;
}

inline Value conditional(const std::string& op, const std::vector<Expression>& args, const Scope& scope) {
    Value left = truth(evaluate(args.at(0), scope), op);
    const bool* l = std::get_if<bool>(&left);
    if ((op == "and" && l && !*l) || (op == "or" && l && *l)) return left;
    if (op == "implies" && l && !*l) return true;
    Value right = truth(evaluate(args.at(1), scope), op);
    const bool* r = std::get_if<bool>(&right);
    const bool unknown = is_unknown(left) || is_unknown(right);
    if (op == "and") return r && !*r ? Value(false) : unknown ? Value(Unknown{}) : Value(true);
    return r && *r ? Value(true) : unknown ? Value(Unknown{}) : Value(false);  // or, implies
}

/// An operator over its argument expressions. `and`, `or` and `implies` evaluate their second
/// operand only when the first does not decide, as SysML's conditional operators do; all the
/// logical operators follow three-valued logic. Any other operator on an unknown is unknown.
inline Value apply_operator(const std::string& op, const std::vector<Expression>& args, const Scope& scope) {
    if (op == "and" || op == "or" || op == "implies") return conditional(op, args, scope);
    std::vector<Value> values;
    for (const Expression& arg : args) values.push_back(evaluate(arg, scope));
    if (op == "not" && values.size() == 1) {
        Value v = truth(values[0], op);
        return is_unknown(v) ? v : Value(!std::get<bool>(v));
    }
    if (op == "xor" && values.size() == 2) {
        Value a = truth(values[0], op), b = truth(values[1], op);
        return is_unknown(a) || is_unknown(b) ? Value(Unknown{}) : Value(std::get<bool>(a) != std::get<bool>(b));
    }
    for (const Value& v : values)
        if (is_unknown(v)) return Unknown{};
    if ((op == "==" || op == "!=") && values.size() == 2) {
        const bool equal = same(values[0], values[1]);
        return op == "==" ? equal : !equal;
    }
    std::vector<double> numbers;
    for (const Value& v : values) numbers.push_back(number(v, op));
    if (numbers.size() == 1 && (op == "-" || op == "+")) return op == "-" ? -numbers[0] : numbers[0];
    if (numbers.size() == 2) {
        const double a = numbers[0], b = numbers[1];
        if (op == "<") return a < b;
        if (op == "<=") return a <= b;
        if (op == ">") return a > b;
        if (op == ">=") return a >= b;
        if (op == "+") return a + b;
        if (op == "-") return a - b;
        if (op == "*") return a * b;
        if (op == "/") return a / b;
    }
    throw QueryError("operator '" + op + "' on " + std::to_string(args.size()) + " operand(s) is not supported");
}

/// The value of `expr`: a number, a Boolean, a string, an element, or unknown.
inline Value evaluate(const Expression& expr, const Scope& scope) {
    if (expr.is_a<LiteralInteger>()) return static_cast<double>(expr.as<LiteralInteger>().getValue().value());
    if (expr.is_a<LiteralRational>()) return expr.as<LiteralRational>().getValue().value();
    if (expr.is_a<LiteralBoolean>()) return expr.as<LiteralBoolean>().getValue().value();
    if (expr.is_a<LiteralString>()) return Value(std::in_place_type<std::string>, expr.as<LiteralString>().getValue().value());
    if (expr.is_a<LiteralInfinity>()) return std::numeric_limits<double>::infinity();
    if (expr.is_a<FeatureChainExpression>()) {  // before OperatorExpression, which it specializes
        auto chain = expr.as<FeatureChainExpression>();
        return value_in(evaluate(chain.getArgument().at(0), scope), chain.getTargetFeature().value(), scope);
    }
    if (expr.is_a<FeatureReferenceExpression>())
        return value_of(expr.as<FeatureReferenceExpression>().getReferent().value(), scope);
    if (expr.is_a<OperatorExpression>()) {
        auto operation = expr.as<OperatorExpression>();
        return apply_operator(operation.getOperator().value_or(""), operation.getArgument(), scope);
    }
    throw QueryError("cannot evaluate a " + expr.metaclass_name());
}

/// A value for a report: numbers with at most three decimals, elements by name.
inline std::string fmt(const Value& value) {
    if (is_unknown(value)) return "unknown";
    if (auto b = std::get_if<bool>(&value)) return *b ? "true" : "false";
    if (auto d = std::get_if<double>(&value)) {
        if (std::isinf(*d)) return "*";
        char buffer[64];
        std::snprintf(buffer, sizeof buffer, "%.3f", *d);
        std::string text = buffer;
        text.erase(text.find_last_not_of('0') + 1);
        if (!text.empty() && text.back() == '.') text.pop_back();
        return text == "-0" ? "0" : text;
    }
    if (auto s = std::get_if<std::string>(&value)) return "\"" + *s + "\"";
    return name_of(std::get<Element>(value));
}

inline std::string render(const Expression& expr);

inline std::string operand(Expression expr) {
    if (expr.is_a<FeatureReferenceExpression>()) {
        std::optional<Feature> referent = expr.as<FeatureReferenceExpression>().getReferent();
        if (referent && referent->is_a<Expression>()) expr = referent->as<Expression>();
    }
    std::string text = render(expr);
    const bool nested = expr.is_a<OperatorExpression>() && !expr.is_a<FeatureChainExpression>();
    return nested ? "(" + text + ")" : text;
}

/// An expression in SysML notation, with every nested operation in parentheses.
inline std::string render(const Expression& expr) {
    if (expr.is_a<LiteralInteger>()) return fmt(static_cast<double>(expr.as<LiteralInteger>().getValue().value()));
    if (expr.is_a<LiteralRational>()) return fmt(expr.as<LiteralRational>().getValue().value());
    if (expr.is_a<LiteralBoolean>()) return expr.as<LiteralBoolean>().getValue().value() ? "true" : "false";
    if (expr.is_a<LiteralString>()) return "\"" + expr.as<LiteralString>().getValue().value() + "\"";
    if (expr.is_a<LiteralInfinity>()) return "*";
    if (expr.is_a<FeatureChainExpression>()) {
        auto chain = expr.as<FeatureChainExpression>();
        return render(chain.getArgument().at(0)) + "." + name_of(chain.getTargetFeature().value());
    }
    if (expr.is_a<FeatureReferenceExpression>()) {
        Feature referent = expr.as<FeatureReferenceExpression>().getReferent().value();
        if (referent.is_a<Expression>()) return render(referent.as<Expression>());
        if (is_enumeration_literal(referent)) return name_of(referent.getOwningNamespace().value()) + "::" + name_of(referent);
        return name_of(referent);
    }
    if (expr.is_a<TriggerInvocationExpression>()) {
        auto trigger = expr.as<TriggerInvocationExpression>();
        return trigger.getKind().value_or("") + " " + render(trigger.getArgument().at(0));
    }
    if (expr.is_a<OperatorExpression>()) {
        auto operation = expr.as<OperatorExpression>();
        std::vector<std::string> operands;
        for (const Expression& arg : operation.getArgument()) operands.push_back(operand(arg));
        const std::string op = operation.getOperator().value_or("");
        if (operands.size() == 1) {
            const bool word = std::all_of(op.begin(), op.end(), [](unsigned char c) { return std::isalpha(c) != 0; });
            return word ? op + " " + operands[0] : op + operands[0];
        }
        return join(operands, " " + op + " ");
    }
    return "<" + expr.metaclass_name() + ">";
}

// ---------------------------------------------------------------- state machines

/// Every state nested in `machine` at any depth, in the order the model declares them, each state
/// before its substates.
inline std::vector<StateUsage> states_of(const StateUsage& machine) {
    std::vector<StateUsage> states;
    for (const StateUsage& state : machine.getNestedState()) {
        states.push_back(state);
        for (const StateUsage& nested : states_of(state)) states.push_back(nested);
    }
    return states;
}

/// Every transition declared in `machine` or in a state nested in it.
inline std::vector<TransitionUsage> transitions_of(const StateUsage& machine) {
    std::vector<StateUsage> states{machine};
    for (const StateUsage& s : states_of(machine)) states.push_back(s);
    std::vector<TransitionUsage> transitions;
    for (const StateUsage& state : states)
        for (const TransitionUsage& t : state.getNestedTransition()) transitions.push_back(t);
    return transitions;
}

/// The states `state` starts in: the targets of the successions it owns (`first start then off;`);
/// a transition is not a succession of this kind. Only the target is read: the source, the start of
/// the state, is a feature of the standard library, which a model loaded without the library, or
/// read from a payload, cannot resolve.
inline std::vector<StateUsage> initial_states(const StateUsage& state) {
    std::vector<StateUsage> initial;
    for (const Feature& feature : state.getOwnedFeature())
        if (feature.is_a<SuccessionAsUsage>())
            for (const Feature& target : feature.as<SuccessionAsUsage>().getTargetFeature())
                if (target.is_a<StateUsage>()) initial.push_back(target.as<StateUsage>());
    return initial;
}

/// A transition's trigger. The key is what must occur, as explore() selects triggers by it: the
/// name of the accepted signal's definition, or `at`, `after` or `when` for a time or change
/// event. The label says it in full (`at maintenanceTime`).
struct Trigger {
    std::string key;
    std::string label;
};

/// The transition's trigger, if it has one.
inline std::optional<Trigger> trigger_of(const TransitionUsage& transition) {
    for (const AcceptActionUsage& accept : transition.getTriggerAction()) {
        std::optional<Expression> argument = accept.getPayloadArgument();
        if (argument && argument->is_a<TriggerInvocationExpression>())
            return Trigger{argument->as<TriggerInvocationExpression>().getKind().value_or(""), render(*argument)};
        std::optional<ReferenceUsage> payload = accept.getPayloadParameter();
        std::vector<Type> types = payload ? payload->getType() : std::vector<Type>{};
        std::string name = types.empty() ? "any" : name_of(types.front());
        return Trigger{name, name};
    }
    return std::nullopt;
}

/// Whether the transition's guards hold: true if it has none, false if one is known to be false,
/// else true or unknown.
inline Value guard_of(const TransitionUsage& transition, const Scope& scope) {
    Value result = true;
    for (const Expression& guard : transition.getGuardExpression()) {
        Value value = truth(evaluate(guard, scope), "if");
        if (value == Value(false)) return false;
        if (is_unknown(value)) result = Unknown{};
    }
    return result;
}

/// `starting -> operating on SelfTestDone if selfTestPassed and (temperature < maxTemperature)`
inline std::string describe_transition(const TransitionUsage& transition, const StateUsage& machine) {
    std::string text = path_below(transition.getSource().value(), machine) + " -> " +
                       path_below(transition.getTarget().value(), machine);
    if (auto trigger = trigger_of(transition)) text += " on " + trigger->label;
    std::vector<std::string> guards;
    for (const Expression& guard : transition.getGuardExpression()) guards.push_back(render(guard));
    if (!guards.empty()) text += " if " + join(guards, " and ");
    return text;
}

/// `initial state in operating: operating::idle`
inline std::string describe_initial(const StateUsage& state, const StateUsage& initial, const StateUsage& machine) {
    std::string where = state != machine ? " in " + path_below(state, machine) : "";
    return "initial state" + where + ": " + path_below(initial, machine);
}

/// The states that enclose `state`, innermost first, below `machine`.
inline std::vector<StateUsage> enclosing_states(const StateUsage& state, const StateUsage& machine) {
    std::vector<StateUsage> out;
    for (std::optional<Element> owner = state.getOwner(); owner && *owner != machine && owner->is_a<StateUsage>();
         owner = owner->getOwner())
        out.push_back(owner->as<StateUsage>());
    return out;
}

/// The trigger labels of a shortest path to each reachable state, by the state's element id.
using Paths = std::unordered_map<std::string, std::vector<std::string>>;

/// The states of `machine` reachable from its initial state, each mapped (by element id) to a
/// shortest list of trigger labels that reaches it (empty for a state entered without any
/// trigger).
///
/// `triggers`: the trigger keys that can occur (see trigger_of); nullopt for every trigger.
/// `bindings`: values of the features the guards read, by name. A guard that is not known to be
/// false may hold.
///
/// Entering a state enters every region of a parallel state, or else its initial states
/// (initial_states) and the targets of the transitions from its entry action (the form
/// `entry action initial; transition initial then off;`), and so on down. A transition from a state
/// is taken from any of its substates too, and entering a nested target makes the states enclosing
/// it active. Regions of a parallel state are explored independently of each other.
///
/// This is a 0-1 breadth-first search: a triggered transition costs one and goes to the back of
/// the queue, entering costs nothing and goes to the front. Where several shortest sequences exist,
/// the queue's order decides which one is reported, and every language keeps that order.
inline Paths explore(const StateUsage& machine, const std::optional<std::vector<std::string>>& triggers,
                     const std::map<std::string, Value>& bindings) {
    Scope scope;
    scope.bindings = bindings;
    std::unordered_map<std::string, std::vector<TransitionUsage>> outgoing;  // by the source's element id
    for (const TransitionUsage& transition : transitions_of(machine))
        outgoing[transition.getSource().value().element_id()].push_back(transition);

    struct Visit {
        StateUsage state;
        std::vector<std::string> path;
        bool enter;
    };
    Paths paths;                               // state -> the shortest trigger labels that reach it
    std::unordered_set<std::string> entered;   // states whose initial substates have been entered
    std::deque<Visit> queue{Visit{machine, {}, true}};

    auto follow = [&](const TransitionUsage& transition, const std::vector<std::string>& path) {
        std::optional<Trigger> trigger = trigger_of(transition);
        if (trigger && triggers && std::find(triggers->begin(), triggers->end(), trigger->key) == triggers->end()) return;
        if (guard_of(transition, scope) == Value(false)) return;
        std::optional<ActionUsage> target = transition.getTarget();
        if (!target || !target->is_a<StateUsage>()) throw QueryError(name_of(transition) + " does not lead to a state");
        std::vector<Visit> arrivals{Visit{target->as<StateUsage>(), path, true}};
        for (const StateUsage& enclosing : enclosing_states(target->as<StateUsage>(), machine))
            arrivals.push_back(Visit{enclosing, path, false});
        for (Visit& arrival : arrivals) {
            if (!trigger) {
                queue.push_front(arrival);
            } else {
                arrival.path.push_back(trigger->label);
                queue.push_back(arrival);
            }
        }
    };

    while (!queue.empty()) {
        Visit visit = queue.front();
        queue.pop_front();
        const std::string id = visit.state.element_id();
        if (paths.count(id) == 0) {
            paths[id] = visit.path;
            for (const TransitionUsage& transition : outgoing[id]) follow(transition, visit.path);
        }
        if (visit.enter && entered.insert(id).second) {
            if (visit.state.getIsParallel() == true) {
                for (const StateUsage& region : visit.state.getNestedState())
                    queue.push_front(Visit{region, visit.path, true});
            } else {
                for (const StateUsage& initial : initial_states(visit.state))
                    queue.push_front(Visit{initial, visit.path, true});
                if (std::optional<ActionUsage> entry = visit.state.getEntryAction())
                    for (const TransitionUsage& transition : outgoing[entry->element_id()]) follow(transition, visit.path);
            }
        }
    }
    paths.erase(machine.element_id());
    return paths;
}

// ---------------------------------------------------------------- structure

/// The multiplicity SysML implies for a usage that declares none (see multiplicity_of).
struct DefaultOne {};

/// What multiplicity_of finds: nothing (std::monostate: [0..*]), the default [1..1], or a
/// multiplicity the model declares.
using FoundMultiplicity = std::variant<std::monostate, DefaultOne, Multiplicity>;

/// The features that `feature` subsets or redefines by a relationship written in the model, not an
/// implied one (a part's implied subsetting of the library's `parts`).
inline std::vector<Feature> explicit_subsettings(const Feature& feature) {
    std::vector<Feature> subsetted;
    for (const Subsetting& subsetting : feature.getOwnedSubsetting()) {
        std::optional<Feature> subsetted_feature = subsetting.getSubsettedFeature();
        if (subsetting.getIsImplied() != true && subsetted_feature) subsetted.push_back(*subsetted_feature);
    }
    return subsetted;
}

/// Whether SysML gives `usage` the default multiplicity [1..1] when it declares none and subsets or
/// redefines nothing explicitly: an attribute, item, part or port usage (not a connection) owned by
/// a definition or usage.
inline bool has_default_multiplicity(const Feature& usage) {
    const bool kind = (usage.is_a<AttributeUsage>() || usage.is_a<ItemUsage>() || usage.is_a<PortUsage>()) &&
                      !usage.is_a<ConnectionUsage>();
    std::optional<Type> owner = usage.getOwningType();
    return kind && owner && (owner->is_a<Definition>() || owner->is_a<Usage>());
}

/// What says how many instances a usage stands for, by SysML's rule for usages: its own
/// multiplicity; else that of the usages it explicitly subsets or redefines, nearest first (in a
/// valid model a redefinition only narrows what it redefines, so the nearest is the tightest); else
/// DefaultOne, for a usage that has the default [1..1]; else nothing: nothing constrains it ([0..*]).
inline FoundMultiplicity multiplicity_of(const Feature& usage) {
    std::unordered_set<std::string> seen{usage.element_id()};
    std::deque<Feature> todo{usage};
    while (!todo.empty()) {
        Feature current = todo.front();
        todo.pop_front();
        if (std::optional<Multiplicity> multiplicity = current.getMultiplicity()) return *multiplicity;
        std::vector<Feature> subsetted = explicit_subsettings(current);
        if (subsetted.empty()) {
            if (has_default_multiplicity(current)) return DefaultOne{};
            return std::monostate{};
        }
        for (const Feature& feature : subsetted)
            if (seen.insert(feature.element_id()).second) todo.push_back(feature);
    }
    return std::monostate{};
}

/// How many instances `usage` stands for in `context`. Its multiplicity's bounds, evaluated in the
/// context (a bound may be an expression: `Rotor[rotorCount]`), must be one whole number.
inline long instance_count(const Feature& usage, const Type& context) {
    FoundMultiplicity found = multiplicity_of(usage);
    if (std::holds_alternative<DefaultOne>(found)) return 1;
    if (std::holds_alternative<std::monostate>(found))
        throw QueryError(name_of(usage) + " in " + name_of(context) + " has no fixed number of instances: [0..*]");
    const Multiplicity& multiplicity = std::get<Multiplicity>(found);
    if (!multiplicity.is_a<MultiplicityRange>())
        throw QueryError(name_of(usage) + ": a " + multiplicity.metaclass_name() + " is not a range");
    auto range = multiplicity.as<MultiplicityRange>();
    Scope scope;
    scope.context = context;
    std::optional<Expression> upper_bound = range.getUpperBound(), lower_bound = range.getLowerBound();
    Value upper = upper_bound ? evaluate(*upper_bound, scope) : Value(Unknown{});
    Value lower = lower_bound ? evaluate(*lower_bound, scope) : upper;
    const double* u = std::get_if<double>(&upper);
    const double* l = std::get_if<double>(&lower);
    if (!(u && l && *l == *u && std::isfinite(*u) && *u == std::floor(*u)))
        throw QueryError(name_of(usage) + " in " + name_of(context) + " has no fixed number of instances: " +
                         fmt(lower) + ".." + fmt(upper));
    return static_cast<long>(*u);
}

/// The names of a part's definitions.
inline std::string definition_name(const PartUsage& part) {
    std::vector<std::string> names;
    for (const PartDefinition& definition : part.getPartDefinition()) names.push_back(name_of(definition));
    return names.empty() ? "(untyped)" : join(names, ", ");
}

inline void count_parts(const Type& node, long factor, std::map<std::string, long>& counts) {
    for (const PartUsage& part : part_usages(node)) {
        const long count = factor * instance_count(part, node);
        counts[definition_name(part)] += count;
        count_parts(part, count, counts);
    }
}

/// How many parts of each definition `root` consists of, at every depth, by definition name in
/// alphabetical order. A part occurs as often as its instance count times its owner's.
inline std::map<std::string, long> bill_of_materials(const Type& root) {
    std::map<std::string, long> counts;
    count_parts(root, 1, counts);
    return counts;
}

/// The sum of `attribute` over `node` and every part in it at any depth, each part counted as often
/// as it occurs, each value the one that holds in its part's context (the most specific
/// redefinition).
inline double rollup(const Type& node, const Feature& attribute) {
    Scope scope;
    scope.context = node;
    Value own = value_of(attribute, scope);
    if (!is_number(own)) throw QueryError(name_of(node) + " has no value for " + name_of(attribute));
    double parts = 0;
    for (const PartUsage& part : part_usages(node))
        parts += static_cast<double>(instance_count(part, node)) * rollup(part, attribute);
    return std::get<double>(own) + parts;
}

// ---------------------------------------------------------------- connectivity

/// A connection or flow between two parts, by their paths below the root (`rotors.motor`).
struct Link {
    std::string source;
    std::string target;
    std::string kind;  // "connection" or "flow"
    std::string name;
    std::optional<std::string> item;  // what a flow carries: its payload's definition

    std::string describe() const {
        const std::string arrow = kind == "flow" ? "->" : "--";
        const std::string carries = item ? " of " + *item : "";
        return source + " " + arrow + " " + target + "  (" + kind + " " + name + carries + ")";
    }
};

/// The part that a connector's related feature designates, as names relative to the connector's
/// owner. The feature is a feature or a feature chain (`rotors.motor`, `battery.powerOut`); a port
/// at the end of the chain belongs to the part before it.
inline std::vector<std::string> part_path(const Feature& feature) {
    std::vector<Feature> chain = feature.getChainingFeature();
    if (chain.empty()) chain.push_back(feature);
    while (chain.size() > 1 && chain.back().is_a<PortUsage>()) chain.pop_back();
    std::vector<std::string> names;
    for (const Feature& f : chain) names.push_back(name_of(f));
    return names;
}

inline std::string joined_path(std::vector<std::string> prefix, const std::vector<std::string>& path) {
    prefix.insert(prefix.end(), path.begin(), path.end());
    return join(prefix, ".");
}

inline void collect_links(const Type& node, const std::vector<std::string>& prefix, std::vector<Link>& found) {
    for (const Usage& usage : model_usages(node)) {
        if (!usage.is_a<ConnectorAsUsage>()) continue;
        auto connector = usage.as<ConnectorAsUsage>();
        std::optional<Feature> source_feature = connector.getSourceFeature();
        if (!source_feature) continue;
        const std::string source = joined_path(prefix, part_path(*source_feature));
        for (const Feature& feature : connector.getTargetFeature()) {
            const std::string target = joined_path(prefix, part_path(feature));
            if (connector.is_a<FlowUsage>()) {
                std::vector<std::string> items;
                for (const Classifier& type : connector.as<FlowUsage>().getPayloadType()) items.push_back(name_of(type));
                std::optional<std::string> item;
                if (!items.empty()) item = join(items, ", ");
                found.push_back(Link{source, target, "flow", name_of(connector), item});
            } else {
                found.push_back(Link{source, target, "connection", name_of(connector), std::nullopt});
            }
        }
    }
    for (const PartUsage& part : part_usages(node)) {
        std::vector<std::string> longer = prefix;
        longer.push_back(name_of(part));
        collect_links(part, longer, found);
    }
}

/// Every connection and flow of `root` and of the parts nested in it, owned or inherited, from each
/// connector's source feature to each of its target features.
inline std::vector<Link> links(const Type& root) {
    std::vector<Link> found;
    collect_links(root, {}, found);
    return found;
}

/// The parts that the part at path `source` reaches through the links of `root`, in alphabetical
/// order: along a flow from its source to its target, along a connection either way. With `item`,
/// through the flows of that item definition only.
inline std::vector<std::string> reachable_parts(const Type& root, const std::string& source,
                                                const std::optional<std::string>& item = std::nullopt) {
    std::map<std::string, std::vector<std::string>> adjacent;
    for (const Link& link : links(root)) {
        if (item && (link.kind != "flow" || link.item != item)) continue;
        adjacent[link.source].push_back(link.target);
        if (link.kind == "connection") adjacent[link.target].push_back(link.source);
    }
    std::set<std::string> seen{source};
    std::vector<std::string> todo{source};
    while (!todo.empty()) {
        std::string current = todo.back();
        todo.pop_back();
        for (const std::string& next : adjacent[current])
            if (seen.insert(next).second) todo.push_back(next);
    }
    seen.erase(source);
    return {seen.begin(), seen.end()};
}

// ---------------------------------------------------------------- requirements

/// One required constraint of a satisfied requirement, evaluated for the satisfying feature.
struct Verdict {
    RequirementUsage requirement;
    std::optional<Feature> satisfied_by;
    ConstraintUsage constraint;
    Value value;                  // true, false or unknown
    std::vector<Value> operands;  // the values of the constraint's operands, for the report
    bool negated;                 // `not satisfy`: the constraint is expected not to hold

    std::string outcome() const {
        if (is_unknown(value)) return "unknown";
        return value != Value(negated) ? "holds" : "VIOLATED";
    }
};

/// The constraints a requirement requires, its own and the ones it inherits: those of its
/// requirement constraint memberships of kind `requirement` (`require constraint`, as opposed to
/// `assume constraint`). The derived property requiredConstraint has the owned ones only.
inline std::vector<ConstraintUsage> required_constraints(const RequirementUsage& requirement) {
    std::vector<ConstraintUsage> constraints;
    for (const FeatureMembership& membership : requirement.getFeatureMembership()) {
        if (!membership.is_a<RequirementConstraintMembership>()) continue;
        auto required = membership.as<RequirementConstraintMembership>();
        if (required.getKind() == std::optional<std::string>("requirement"))
            constraints.push_back(required.getOwnedConstraint().value());
    }
    return constraints;
}

/// The expression whose value is the constraint's result, if any.
inline std::optional<Expression> result_expression(const ConstraintUsage& constraint) {
    for (const Membership& membership : constraint.getOwnedMembership())
        if (membership.is_a<ResultExpressionMembership>())
            return membership.as<ResultExpressionMembership>().getOwnedResultExpression();
    return std::nullopt;
}

/// Every required constraint of every satisfied requirement in the model (not in the standard
/// library), evaluated for the feature that satisfies it: the requirement's subject is that
/// feature, the requirement's other features have the values the requirement gives them, and
/// `derived` computes the attributes the model leaves to computation (a total mass is a roll-up).
inline std::vector<Verdict> check_satisfactions(const Model& model,
                                                const std::map<std::string, std::function<Value(const Type&)>>& derived) {
    std::vector<Verdict> verdicts;
    for (const SatisfyRequirementUsage& satisfy : model.elements_of_type<SatisfyRequirementUsage>()) {
        if (satisfy.getIsLibraryElement() == true) continue;
        RequirementUsage requirement = satisfy.getSatisfiedRequirement().value();
        std::optional<Feature> by = satisfy.getSatisfyingFeature();
        Scope scope;
        scope.context = requirement;
        scope.derived = derived;
        if (std::optional<Usage> subject = requirement.getSubjectParameter())
            scope.bindings[name_of(*subject)] = by ? element_value(*by) : Value(Unknown{});
        for (const ConstraintUsage& constraint : required_constraints(requirement)) {
            std::optional<Expression> expr = result_expression(constraint);
            if (!expr) throw QueryError(name_of(constraint) + " has no result expression");
            Value value = evaluate(*expr, scope);
            std::vector<Value> operands;
            if (expr->is_a<OperatorExpression>())
                for (const Expression& arg : expr->as<OperatorExpression>().getArgument()) operands.push_back(evaluate(arg, scope));
            verdicts.push_back(Verdict{requirement, by, constraint, value, operands, satisfy.getIsNegated() == true});
        }
    }
    return verdicts;
}

}  // namespace queries
