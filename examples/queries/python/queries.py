"""Parametric queries over SysML v2 models, written against the SDK.

Four families of queries, each a function of model elements and parameters:

- state machines: which states a machine can reach, and the shortest sequence of triggers to each,
  given which triggers can occur and what is known of the values its guards read;
- structure: a bill of materials and the roll-up of an attribute over the part tree, with
  multiplicities evaluated in context, redefined values, and inherited parts;
- connectivity: the parts a source part reaches through connections and flows, optionally through
  flows of one item definition only;
- requirements: whether the constraints of each satisfied requirement hold for the part that
  satisfies it.

The queries read specification properties only (`nestedState`, `usage`, `multiplicity`,
`featureMembership`, ...), so they run unchanged on either backend. Guards, multiplicity bounds,
attribute values and constraints are the model's own expressions, evaluated by `evaluate` in
three-valued logic: a value that the model does not determine and the caller did not bind is
UNKNOWN, and a guard that is UNKNOWN may hold.

What the queries simplify, on purpose:

- A bound value holds for a whole exploration. Guard inputs such as a temperature vary over time
  in SysML; binding one asks "what if it always had this value", and leaving it unbound lets every
  guard over it go either way, independently of the others.
- The regions of a parallel state are explored independently of each other.
- A state that no transition or initial succession leads to is unreachable. That is the
  conventional reading; the library's semantics do not state it formally.
- A requirement is checked by its required constraints. Its assumptions (`assume constraint`) and
  its nested requirements are not considered.
- Connections and flows are followed where the model declares them; reaching a part means it may
  be reached, at the level of the usages, not that every instance is.

The same queries exist for every language of the SDK, function for function.
"""

from __future__ import annotations

import math
from collections import deque
from dataclasses import dataclass, field
from typing import Any, Callable, Optional

from sysml.classes import (
    AttributeUsage,
    ConnectionUsage,
    ConnectorAsUsage,
    Definition,
    Element,
    EnumerationDefinition,
    EnumerationUsage,
    Expression,
    FeatureChainExpression,
    FeatureReferenceExpression,
    FeatureValue,
    FlowUsage,
    ItemUsage,
    LiteralBoolean,
    LiteralInfinity,
    LiteralInteger,
    LiteralRational,
    LiteralString,
    MultiplicityRange,
    OperatorExpression,
    PartUsage,
    PortUsage,
    RequirementConstraintMembership,
    ResultExpressionMembership,
    SatisfyRequirementUsage,
    StateUsage,
    SuccessionAsUsage,
    TriggerInvocationExpression,
    Usage,
)


class QueryError(Exception):
    """The model does not give a query what it needs: a value, or an expression it can evaluate."""


# ---------------------------------------------------------------- names

def name_of(element) -> str:
    """The element's name. A redefinition declared without a name has the name of the feature it
    redefines: `attribute :>> mass` is named `mass`."""
    return element.name or "(unnamed)"


def path_below(element, top) -> str:
    """The names of `element` and its owners up to `top` (not included), outermost first:
    `operating::pumping::high` for a state two levels below the machine `top`."""
    names = []
    while element is not None and element != top:
        names.append(name_of(element))
        element = element.owner
    return "::".join(reversed(names))


# ---------------------------------------------------------------- features and their values

def model_usages(element) -> list:
    """The usages of a definition or usage, owned and inherited, without those the standard library
    contributes (a part inherits `self`, `start`, `subparts`, ... from the library's `Part`)."""
    return [u for u in element.usage if not u.isLibraryElement]


def part_usages(element) -> list:
    """The parts of a definition or usage, owned and inherited. A connection is a part too in
    SysML; it is not a component, so it is left out."""
    return [u for u in model_usages(element) if isinstance(u, PartUsage) and not isinstance(u, ConnectionUsage)]


def redefines(feature, other) -> bool:
    """Whether `feature` redefines `other`, directly or through a chain of redefinitions."""
    seen, todo = set(), [feature]
    while todo:
        for redefinition in todo.pop().ownedRedefinition:
            redefined = redefinition.redefinedFeature
            if redefined == other:
                return True
            if redefined is not None and redefined not in seen:
                seen.add(redefined)
                todo.append(redefined)
    return False


def feature_in(context, feature):
    """The feature that stands for `feature` in `context`: among the context's features, owned or
    inherited, the one that is `feature` or redefines it. A type inherits only the most specific
    redefinition, so this is the one whose value holds in the context."""
    for candidate in context.feature:
        if candidate == feature or redefines(candidate, feature):
            return candidate
    return feature


def is_enumeration_literal(feature) -> bool:
    """Whether a feature is one of the values an enumeration definition enumerates (`on` in
    `enum def IgnitionOnOff { on; off; }`). Those are owned through variant memberships, so they
    have an owning namespace but no owning type."""
    return isinstance(feature, EnumerationUsage) and isinstance(feature.owningNamespace, EnumerationDefinition)


def value_expression(feature):
    """The expression a feature is bound to by its declaration (`= 0.045`), or None."""
    for membership in feature.ownedMembership:
        if isinstance(membership, FeatureValue):
            return membership.value
    return None


# ---------------------------------------------------------------- expressions

class _Unknown:
    def __repr__(self) -> str:
        return "unknown"


UNKNOWN = _Unknown()
"""A value that neither the model nor the caller determines."""


@dataclass
class Scope:
    """What an expression is evaluated against.

    context:  the element whose features give values: a referenced feature has the value of the
              feature that stands for it in the context (`feature_in`);
    bindings: values by feature name, which take precedence over the model's own;
    derived:  attributes the caller computes, by name: a function of the element that has the
              attribute (`vehicle.totalMass` is derived["totalMass"](vehicle)).
    """

    context: Any = None
    bindings: dict = field(default_factory=dict)
    derived: dict = field(default_factory=dict)

    def within(self, context) -> "Scope":
        return Scope(context, self.bindings, self.derived)


def evaluate(expr, scope: Scope):
    """The value of `expr`: a number (float), a Boolean, a string, an element, or UNKNOWN."""
    if isinstance(expr, (LiteralInteger, LiteralRational)):
        return float(expr.value)
    if isinstance(expr, LiteralBoolean):
        return bool(expr.value)
    if isinstance(expr, LiteralString):
        return expr.value
    if isinstance(expr, LiteralInfinity):
        return math.inf
    if isinstance(expr, FeatureChainExpression):  # before OperatorExpression, which it specializes
        return value_in(evaluate(expr.argument[0], scope), expr.targetFeature, scope)
    if isinstance(expr, FeatureReferenceExpression):
        return value_of(expr.referent, scope)
    if isinstance(expr, OperatorExpression):
        return apply_operator(expr.operator, expr.argument, scope)
    raise QueryError(f"cannot evaluate a {expr.metaclass_name}")


def value_of(feature, scope: Scope):
    """The value of a referenced feature: a binding of its name; an enumeration literal, which is
    its own value; else the value the model gives the feature that stands for it in the scope's
    context; else UNKNOWN."""
    if isinstance(feature, Expression):
        # The second operand of `and`, `or` and `implies` is a reference to an expression,
        # evaluated only when the operator needs it.
        return evaluate(feature, scope)
    name = name_of(feature)
    if name in scope.bindings:
        return scope.bindings[name]
    if is_enumeration_literal(feature):
        return feature
    if scope.context is not None:
        feature = feature_in(scope.context, feature)
    expr = value_expression(feature)
    return UNKNOWN if expr is None else evaluate(expr, scope)


def value_in(source, feature, scope: Scope):
    """`source.feature`: a derived attribute that the caller computes, else the value of the feature
    in the context of `source`."""
    if source is UNKNOWN:
        return UNKNOWN
    if not isinstance(source, Element):
        raise QueryError(f"{fmt(source)} has no feature {name_of(feature)}")
    derive = scope.derived.get(name_of(feature))
    if derive is not None:
        return derive(source)
    return value_of(feature, scope.within(source))


def apply_operator(op: str, args: list, scope: Scope):
    """An operator over its argument expressions. `and`, `or` and `implies` evaluate their second
    operand only when the first does not decide, as SysML's conditional operators do; all the
    logical operators follow three-valued logic. Any other operator on an UNKNOWN is UNKNOWN."""
    if op in ("and", "or", "implies"):
        return _conditional(op, args, scope)
    values = [evaluate(a, scope) for a in args]
    if op == "not" and len(values) == 1:
        v = _truth(values[0], op)
        return UNKNOWN if v is UNKNOWN else not v
    if op == "xor" and len(values) == 2:
        a, b = (_truth(v, op) for v in values)
        return UNKNOWN if UNKNOWN in (a, b) else a != b
    if any(v is UNKNOWN for v in values):
        return UNKNOWN
    if op in ("==", "!=") and len(values) == 2:
        same = _same(values[0], values[1])
        return same if op == "==" else not same
    numbers = [_number(v, op) for v in values]
    if len(numbers) == 1 and op in ("-", "+"):
        return -numbers[0] if op == "-" else numbers[0]
    if len(numbers) == 2:
        a, b = numbers
        binary = {
            "<": lambda: a < b, "<=": lambda: a <= b, ">": lambda: a > b, ">=": lambda: a >= b,
            "+": lambda: a + b, "-": lambda: a - b, "*": lambda: a * b, "/": lambda: a / b,
        }.get(op)
        if binary is not None:
            return binary()
    raise QueryError(f"operator {op!r} on {len(args)} operand(s) is not supported")


def _conditional(op: str, args: list, scope: Scope):
    left = _truth(evaluate(args[0], scope), op)
    if (op == "and" and left is False) or (op == "or" and left is True):
        return left
    if op == "implies" and left is False:
        return True
    right = _truth(evaluate(args[1], scope), op)
    if op == "and":
        return False if right is False else (UNKNOWN if UNKNOWN in (left, right) else True)
    if op == "or":
        return True if right is True else (UNKNOWN if UNKNOWN in (left, right) else False)
    return True if right is True else (UNKNOWN if UNKNOWN in (left, right) else False)  # implies


def _truth(value, op: str):
    if value is UNKNOWN or isinstance(value, bool):
        return value
    raise QueryError(f"{op!r} needs a Boolean, not {fmt(value)}")


def _number(value, op: str) -> float:
    if is_number(value):
        return float(value)
    raise QueryError(f"{op!r} needs a number, not {fmt(value)}")


def _same(a, b) -> bool:
    """Equality within a kind of value: numbers by value, elements by identity."""
    if is_number(a) and is_number(b):
        return float(a) == float(b)
    if isinstance(a, bool) or isinstance(b, bool):
        return isinstance(a, bool) and isinstance(b, bool) and a == b
    if isinstance(a, Element) or isinstance(b, Element):
        return isinstance(a, Element) and isinstance(b, Element) and a == b
    return isinstance(a, str) and isinstance(b, str) and a == b


def is_number(value) -> bool:
    return isinstance(value, (int, float)) and not isinstance(value, bool)


def fmt(value) -> str:
    """A value for a report: numbers with at most three decimals, elements by name."""
    if value is UNKNOWN:
        return "unknown"
    if isinstance(value, bool):
        return "true" if value else "false"
    if is_number(value):
        value = float(value)
        if math.isinf(value):
            return "*"
        text = f"{value:.3f}".rstrip("0").rstrip(".")
        return "0" if text == "-0" else text
    if isinstance(value, str):
        return f'"{value}"'
    return name_of(value)


def render(expr) -> str:
    """An expression in SysML notation, with every nested operation in parentheses."""
    if isinstance(expr, (LiteralInteger, LiteralRational)):
        return fmt(float(expr.value))
    if isinstance(expr, LiteralBoolean):
        return "true" if expr.value else "false"
    if isinstance(expr, LiteralString):
        return f'"{expr.value}"'
    if isinstance(expr, LiteralInfinity):
        return "*"
    if isinstance(expr, FeatureChainExpression):
        return f"{render(expr.argument[0])}.{name_of(expr.targetFeature)}"
    if isinstance(expr, FeatureReferenceExpression):
        referent = expr.referent
        if isinstance(referent, Expression):
            return render(referent)
        if is_enumeration_literal(referent):
            return f"{name_of(referent.owningNamespace)}::{name_of(referent)}"
        return name_of(referent)
    if isinstance(expr, TriggerInvocationExpression):
        return f"{expr.kind} {render(expr.argument[0])}"
    if isinstance(expr, OperatorExpression):
        operands = [_operand(a) for a in expr.argument]
        if len(operands) == 1:
            return f"{expr.operator} {operands[0]}" if expr.operator.isalpha() else f"{expr.operator}{operands[0]}"
        return f" {expr.operator} ".join(operands)
    return f"<{expr.metaclass_name}>"


def _operand(expr) -> str:
    if isinstance(expr, FeatureReferenceExpression) and isinstance(expr.referent, Expression):
        expr = expr.referent
    text = render(expr)
    nested = isinstance(expr, OperatorExpression) and not isinstance(expr, FeatureChainExpression)
    return f"({text})" if nested else text


# ---------------------------------------------------------------- state machines

def states_of(machine) -> list:
    """Every state nested in `machine` at any depth, in the order the model declares them, each
    state before its substates."""
    states = []
    for state in machine.nestedState:
        states.append(state)
        states.extend(states_of(state))
    return states


def transitions_of(machine) -> list:
    """Every transition declared in `machine` or in a state nested in it."""
    return [t for state in [machine, *states_of(machine)] for t in state.nestedTransition]


def initial_states(state) -> list:
    """The states `state` starts in: the targets of the successions it owns
    (`first start then off;`); a transition is not a succession of this kind. Only the target is
    read: the source, the start of the state, is a feature of the standard library, which a model
    loaded without the library, or read from a payload, cannot resolve."""
    return [target for feature in state.ownedFeature if isinstance(feature, SuccessionAsUsage)
            for target in feature.targetFeature if isinstance(target, StateUsage)]


def trigger_of(transition) -> tuple[Optional[str], Optional[str]]:
    """The transition's trigger as (key, label). The key is what must occur, as `explore` selects
    triggers by it: the name of the accepted signal's definition, or `at`, `after` or `when` for a
    time or change event. The label says it in full (`at maintenanceTime`). (None, None) for a
    transition without a trigger."""
    for accept in transition.triggerAction:
        argument = accept.payloadArgument
        if isinstance(argument, TriggerInvocationExpression):
            return argument.kind, render(argument)
        payload = accept.payloadParameter
        types = payload.type if payload is not None else []
        name = name_of(types[0]) if types else "any"
        return name, name
    return None, None


def guard_of(transition, scope: Scope):
    """Whether the transition's guards hold: True if it has none, False if one is known to be
    false, else True or UNKNOWN."""
    result = True
    for guard in transition.guardExpression:
        value = _truth(evaluate(guard, scope), "if")
        if value is False:
            return False
        if value is UNKNOWN:
            result = UNKNOWN
    return result


def describe_transition(transition, machine) -> str:
    """`starting -> operating on SelfTestDone if selfTestPassed and (temperature < maxTemperature)`"""
    text = f"{path_below(transition.source, machine)} -> {path_below(transition.target, machine)}"
    _, label = trigger_of(transition)
    if label is not None:
        text += f" on {label}"
    guards = [render(g) for g in transition.guardExpression]
    if guards:
        text += " if " + " and ".join(guards)
    return text


def describe_initial(state, initial, machine) -> str:
    """`initial state in operating: operating::idle`"""
    where = f" in {path_below(state, machine)}" if state != machine else ""
    return f"initial state{where}: {path_below(initial, machine)}"


def enclosing_states(state, machine) -> list:
    """The states that enclose `state`, innermost first, below `machine`."""
    out = []
    owner = state.owner
    while owner is not None and owner != machine and isinstance(owner, StateUsage):
        out.append(owner)
        owner = owner.owner
    return out


def explore(machine, *, triggers=None, bindings=None) -> dict:
    """The states of `machine` reachable from its initial state, each mapped to a shortest list of
    trigger labels that reaches it (empty for a state entered without any trigger).

    triggers: the trigger keys that can occur (see `trigger_of`); None for every trigger.
    bindings: values of the features the guards read, by name. A guard that is not known to be
              false may hold.

    Entering a state enters every region of a parallel state, or else its initial states
    (`initial_states`) and the targets of the transitions from its entry action (the form
    `entry action initial; transition initial then off;`), and so on down. A transition from a
    state is taken from any of its substates too, and entering a nested target makes the states
    enclosing it active. Regions of a parallel state are explored independently of each other.

    This is a 0-1 breadth-first search: a triggered transition costs one and goes to the back of
    the queue, entering costs nothing and goes to the front. Where several shortest sequences
    exist, the queue's order decides which one is reported, and every language keeps that order.
    """
    scope = Scope(bindings=dict(bindings or {}))
    outgoing: dict = {}
    for transition in transitions_of(machine):
        outgoing.setdefault(transition.source, []).append(transition)

    paths: dict = {}  # state -> the shortest trigger labels that reach it
    entered: set = set()  # states whose initial substates have been entered
    queue = deque([(machine, [], True)])  # (state, trigger labels, whether it is being entered)

    def follow(transition, path):
        key, label = trigger_of(transition)
        if key is not None and triggers is not None and key not in triggers:
            return
        if guard_of(transition, scope) is False:
            return
        target = transition.target
        arrivals = [(target, True)] + [(s, False) for s in enclosing_states(target, machine)]
        for state, enter in arrivals:
            if label is None:
                queue.appendleft((state, path, enter))
            else:
                queue.append((state, path + [label], enter))

    while queue:
        state, path, enter = queue.popleft()
        if state not in paths:
            paths[state] = path
            for transition in outgoing.get(state, []):
                follow(transition, path)
        if enter and state not in entered:
            entered.add(state)
            if state.isParallel:
                for region in state.nestedState:
                    queue.appendleft((region, path, True))
            else:
                for initial in initial_states(state):
                    queue.appendleft((initial, path, True))
                if state.entryAction is not None:
                    for transition in outgoing.get(state.entryAction, []):
                        follow(transition, path)
    del paths[machine]
    return paths


# ---------------------------------------------------------------- structure

class _DefaultOne:
    def __repr__(self) -> str:
        return "[1..1]"


DEFAULT_ONE = _DefaultOne()
"""The multiplicity SysML implies for a usage that declares none (see `multiplicity_of`)."""


def explicit_subsettings(feature) -> list:
    """The features that `feature` subsets or redefines by a relationship written in the model,
    not an implied one (a part's implied subsetting of the library's `parts`)."""
    return [s.subsettedFeature for s in feature.ownedSubsetting
            if not s.isImplied and s.subsettedFeature is not None]


def has_default_multiplicity(usage) -> bool:
    """Whether SysML gives `usage` the default multiplicity [1..1] when it declares none and
    subsets or redefines nothing explicitly: an attribute, item, part or port usage (not a
    connection) owned by a definition or usage."""
    kind = isinstance(usage, (AttributeUsage, ItemUsage, PortUsage)) and not isinstance(usage, ConnectionUsage)
    return kind and isinstance(usage.owningType, (Definition, Usage))


def multiplicity_of(usage):
    """What says how many instances a usage stands for, by SysML's rule for usages: its own
    multiplicity; else that of the usages it explicitly subsets or redefines, nearest first (in a
    valid model a redefinition only narrows what it redefines, so the nearest is the tightest); else
    DEFAULT_ONE, for a usage that has the default [1..1]; else None: nothing constrains it ([0..*])."""
    seen, todo = {usage}, deque([usage])
    while todo:
        current = todo.popleft()
        if current.multiplicity is not None:
            return current.multiplicity
        subsetted = explicit_subsettings(current)
        if not subsetted:
            return DEFAULT_ONE if has_default_multiplicity(current) else None
        for feature in subsetted:
            if feature not in seen:
                seen.add(feature)
                todo.append(feature)
    return None


def instance_count(usage, context) -> int:
    """How many instances `usage` stands for in `context`. Its multiplicity's bounds, evaluated in
    the context (a bound may be an expression: `Rotor[rotorCount]`), must be one whole number."""
    multiplicity = multiplicity_of(usage)
    if multiplicity is DEFAULT_ONE:
        return 1
    if multiplicity is None:
        raise QueryError(f"{name_of(usage)} in {name_of(context)} has no fixed number of instances: [0..*]")
    if not isinstance(multiplicity, MultiplicityRange):
        raise QueryError(f"{name_of(usage)}: a {multiplicity.metaclass_name} is not a range")
    scope = Scope(context)
    upper = evaluate(multiplicity.upperBound, scope) if multiplicity.upperBound is not None else UNKNOWN
    lower = evaluate(multiplicity.lowerBound, scope) if multiplicity.lowerBound is not None else upper
    if not (is_number(upper) and lower == upper and math.isfinite(upper) and upper == int(upper)):
        raise QueryError(
            f"{name_of(usage)} in {name_of(context)} has no fixed number of instances: {fmt(lower)}..{fmt(upper)}")
    return int(upper)


def definition_name(part) -> str:
    """The names of a part's definitions."""
    return ", ".join(name_of(d) for d in part.partDefinition) or "(untyped)"


def bill_of_materials(root) -> dict[str, int]:
    """How many parts of each definition `root` consists of, at every depth, by definition name
    in alphabetical order. A part occurs as often as its instance count times its owner's."""
    counts: dict[str, int] = {}

    def walk(node, factor):
        for part in part_usages(node):
            count = factor * instance_count(part, node)
            key = definition_name(part)
            counts[key] = counts.get(key, 0) + count
            walk(part, count)

    walk(root, 1)
    return dict(sorted(counts.items()))


def rollup(node, attribute) -> float:
    """The sum of `attribute` over `node` and every part in it at any depth, each part counted as
    often as it occurs, each value the one that holds in its part's context (the most specific
    redefinition)."""
    own = value_of(attribute, Scope(node))
    if not is_number(own):
        raise QueryError(f"{name_of(node)} has no value for {name_of(attribute)}")
    return own + sum(instance_count(part, node) * rollup(part, attribute) for part in part_usages(node))


# ---------------------------------------------------------------- connectivity

@dataclass(frozen=True)
class Link:
    """A connection or flow between two parts, by their paths below the root (`rotors.motor`)."""

    source: str
    target: str
    kind: str  # "connection" or "flow"
    name: str
    item: Optional[str] = None  # what a flow carries: its payload's definition

    def describe(self) -> str:
        arrow = "->" if self.kind == "flow" else "--"
        carries = f" of {self.item}" if self.item else ""
        return f"{self.source} {arrow} {self.target}  ({self.kind} {self.name}{carries})"


def part_path(feature) -> list[str]:
    """The part that a connector's related feature designates, as names relative to the
    connector's owner. The feature is a feature or a feature chain (`rotors.motor`,
    `battery.powerOut`); a port at the end of the chain belongs to the part before it."""
    chain = feature.chainingFeature or [feature]
    while len(chain) > 1 and isinstance(chain[-1], PortUsage):
        chain = chain[:-1]
    return [name_of(f) for f in chain]


def links(root) -> list[Link]:
    """Every connection and flow of `root` and of the parts nested in it, owned or inherited, from
    each connector's source feature to each of its target features."""
    found: list[Link] = []

    def walk(node, prefix):
        for connector in model_usages(node):
            if not isinstance(connector, ConnectorAsUsage) or connector.sourceFeature is None:
                continue
            source = ".".join(prefix + part_path(connector.sourceFeature))
            for target in (".".join(prefix + part_path(f)) for f in connector.targetFeature):
                if isinstance(connector, FlowUsage):
                    item = ", ".join(name_of(t) for t in connector.payloadType) or None
                    found.append(Link(source, target, "flow", name_of(connector), item))
                else:
                    found.append(Link(source, target, "connection", name_of(connector)))
        for part in part_usages(node):
            walk(part, prefix + [name_of(part)])

    walk(root, [])
    return found


def reachable_parts(root, source: str, *, item: Optional[str] = None) -> list[str]:
    """The parts that the part at path `source` reaches through the links of `root`, in
    alphabetical order: along a flow from its source to its target, along a connection either way.
    With `item`, through the flows of that item definition only."""
    adjacent: dict[str, list[str]] = {}
    for link in links(root):
        if item is not None and (link.kind != "flow" or link.item != item):
            continue
        adjacent.setdefault(link.source, []).append(link.target)
        if link.kind == "connection":
            adjacent.setdefault(link.target, []).append(link.source)
    seen, todo = {source}, [source]
    while todo:
        for nxt in adjacent.get(todo.pop(), []):
            if nxt not in seen:
                seen.add(nxt)
                todo.append(nxt)
    return sorted(seen - {source})


# ---------------------------------------------------------------- requirements

@dataclass(frozen=True)
class Verdict:
    """One required constraint of a satisfied requirement, evaluated for the satisfying feature."""

    requirement: Any
    satisfied_by: Any
    constraint: Any
    value: Any  # True, False or UNKNOWN
    operands: list  # the values of the constraint's operands, for the report
    negated: bool  # `not satisfy`: the constraint is expected not to hold

    @property
    def outcome(self) -> str:
        if self.value is UNKNOWN:
            return "unknown"
        return "holds" if self.value != self.negated else "VIOLATED"


def required_constraints(requirement) -> list:
    """The constraints a requirement requires, its own and the ones it inherits: those of its
    requirement constraint memberships of kind `requirement` (`require constraint`, as opposed
    to `assume constraint`). The derived property `requiredConstraint` has the owned ones only."""
    return [
        m.ownedConstraint
        for m in requirement.featureMembership
        if isinstance(m, RequirementConstraintMembership) and m.kind == "requirement"
    ]


def result_expression(constraint):
    """The expression whose value is the constraint's result, or None."""
    for membership in constraint.ownedMembership:
        if isinstance(membership, ResultExpressionMembership):
            return membership.ownedResultExpression
    return None


def check_satisfactions(model, derived: dict[str, Callable]) -> list[Verdict]:
    """Every required constraint of every satisfied requirement in the model (not in the standard
    library), evaluated for the feature that satisfies it: the requirement's subject is that
    feature, the requirement's other features have the values the requirement gives them, and
    `derived` computes the attributes the model leaves to computation (a total mass is a roll-up)."""
    verdicts = []
    for satisfy in model.elements_of_type(SatisfyRequirementUsage):
        if satisfy.isLibraryElement:
            continue
        requirement, by = satisfy.satisfiedRequirement, satisfy.satisfyingFeature
        subject = requirement.subjectParameter
        scope = Scope(requirement, {name_of(subject): by} if subject is not None else {}, derived)
        for constraint in required_constraints(requirement):
            expr = result_expression(constraint)
            if expr is None:
                raise QueryError(f"{name_of(constraint)} has no result expression")
            value = evaluate(expr, scope)
            operands = [evaluate(a, scope) for a in expr.argument] if isinstance(expr, OperatorExpression) else []
            verdicts.append(Verdict(requirement, by, constraint, value, operands, bool(satisfy.isNegated)))
    return verdicts
