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
// The same queries exist for every language of the SDK, function for function. Elements are the
// SDK's Proxy objects; a model hands out one object per element, so `===` is element identity
// and elements can key a Map or a Set.

import { elementsOfType, is } from "../../../ts/index.mjs"; // `sysml` once published

/** The model does not give a query what it needs: a value, or an expression it can evaluate. */
export class QueryError extends Error {}

// ---------------------------------------------------------------- names

/** The element's name. A redefinition declared without a name has the name of the feature it
 * redefines: `attribute :>> mass` is named `mass`. */
export function nameOf(element) {
  return element.name || "(unnamed)";
}

/** The names of `element` and its owners up to `top` (not included), outermost first:
 * `operating::pumping::high` for a state two levels below the machine `top`. */
export function pathBelow(element, top) {
  const names = [];
  while (element != null && element !== top) {
    names.push(nameOf(element));
    element = element.owner;
  }
  return names.reverse().join("::");
}

// ---------------------------------------------------------------- features and their values

/** The usages of a definition or usage, owned and inherited, without those the standard library
 * contributes (a part inherits `self`, `start`, `subparts`, ... from the library's `Part`). */
export function modelUsages(element) {
  return element.usage.filter((u) => !u.isLibraryElement);
}

/** The parts of a definition or usage, owned and inherited. A connection is a part too in SysML;
 * it is not a component, so it is left out. */
export function partUsages(element) {
  return modelUsages(element).filter((u) => is(u, "PartUsage") && !is(u, "ConnectionUsage"));
}

/** Whether `feature` redefines `other`, directly or through a chain of redefinitions. */
export function redefines(feature, other) {
  const seen = new Set();
  const todo = [feature];
  while (todo.length > 0) {
    for (const redefinition of todo.pop().ownedRedefinition) {
      const redefined = redefinition.redefinedFeature;
      if (redefined === other) return true;
      if (redefined != null && !seen.has(redefined)) {
        seen.add(redefined);
        todo.push(redefined);
      }
    }
  }
  return false;
}

/** The feature that stands for `feature` in `context`: among the context's features, owned or
 * inherited, the one that is `feature` or redefines it. A type inherits only the most specific
 * redefinition, so this is the one whose value holds in the context. */
export function featureIn(context, feature) {
  return context.feature.find((candidate) => candidate === feature || redefines(candidate, feature)) ?? feature;
}

/** Whether a feature is one of the values an enumeration definition enumerates (`on` in
 * `enum def IgnitionOnOff { on; off; }`). Those are owned through variant memberships, so they
 * have an owning namespace but no owning type. */
export function isEnumerationLiteral(feature) {
  return is(feature, "EnumerationUsage") && is(feature.owningNamespace, "EnumerationDefinition");
}

/** The expression a feature is bound to by its declaration (`= 0.045`), or null. */
export function valueExpression(feature) {
  const membership = feature.ownedMembership.find((m) => is(m, "FeatureValue"));
  return membership ? membership.value : null;
}

// ---------------------------------------------------------------- expressions

/** A value that neither the model nor the caller determines. */
export const UNKNOWN = Symbol("unknown");

/**
 * What an expression is evaluated against.
 *
 * context:  the element whose features give values: a referenced feature has the value of the
 *           feature that stands for it in the context (featureIn);
 * bindings: values by feature name (a Map), which take precedence over the model's own;
 * derived:  attributes the caller computes, by name (a Map): a function of the element that has
 *           the attribute (`vehicle.totalMass` is derived.get("totalMass")(vehicle)).
 */
export class Scope {
  constructor(context = null, bindings = new Map(), derived = new Map()) {
    this.context = context;
    this.bindings = bindings;
    this.derived = derived;
  }

  within(context) {
    return new Scope(context, this.bindings, this.derived);
  }
}

/** Whether a value is a model element (an SDK element proxy). */
export function isElement(value) {
  return value !== null && typeof value === "object" && typeof value.$metaclass === "string";
}

/** The value of `expr`: a number, a Boolean, a string, an element, or UNKNOWN. */
export function evaluate(expr, scope) {
  if (is(expr, "LiteralInteger") || is(expr, "LiteralRational")) return Number(expr.value);
  if (is(expr, "LiteralBoolean")) return Boolean(expr.value);
  if (is(expr, "LiteralString")) return expr.value;
  if (is(expr, "LiteralInfinity")) return Infinity;
  if (is(expr, "FeatureChainExpression")) // before OperatorExpression, which it specializes
    return valueIn(evaluate(expr.argument[0], scope), expr.targetFeature, scope);
  if (is(expr, "FeatureReferenceExpression")) return valueOf(expr.referent, scope);
  if (is(expr, "OperatorExpression")) return applyOperator(expr.operator, expr.argument, scope);
  throw new QueryError(`cannot evaluate a ${expr.$metaclass}`);
}

/** The value of a referenced feature: a binding of its name; an enumeration literal, which is its
 * own value; else the value the model gives the feature that stands for it in the scope's context;
 * else UNKNOWN. */
export function valueOf(feature, scope) {
  // The second operand of `and`, `or` and `implies` is a reference to an expression, evaluated
  // only when the operator needs it.
  if (is(feature, "Expression")) return evaluate(feature, scope);
  const name = nameOf(feature);
  if (scope.bindings.has(name)) return scope.bindings.get(name);
  if (isEnumerationLiteral(feature)) return feature;
  if (scope.context != null) feature = featureIn(scope.context, feature);
  const expr = valueExpression(feature);
  return expr == null ? UNKNOWN : evaluate(expr, scope);
}

/** `source.feature`: a derived attribute that the caller computes, else the value of the feature
 * in the context of `source`. */
export function valueIn(source, feature, scope) {
  if (source === UNKNOWN) return UNKNOWN;
  if (!isElement(source)) throw new QueryError(`${fmt(source)} has no feature ${nameOf(feature)}`);
  const derive = scope.derived.get(nameOf(feature));
  if (derive !== undefined) return derive(source);
  return valueOf(feature, scope.within(source));
}

const BINARY = {
  "<": (a, b) => a < b, "<=": (a, b) => a <= b, ">": (a, b) => a > b, ">=": (a, b) => a >= b,
  "+": (a, b) => a + b, "-": (a, b) => a - b, "*": (a, b) => a * b, "/": (a, b) => a / b,
};

/** An operator over its argument expressions. `and`, `or` and `implies` evaluate their second
 * operand only when the first does not decide, as SysML's conditional operators do; all the
 * logical operators follow three-valued logic. Any other operator on an UNKNOWN is UNKNOWN. */
export function applyOperator(op, args, scope) {
  if (op === "and" || op === "or" || op === "implies") return conditional(op, args, scope);
  const values = args.map((a) => evaluate(a, scope));
  if (op === "not" && values.length === 1) {
    const v = truth(values[0], op);
    return v === UNKNOWN ? UNKNOWN : !v;
  }
  if (op === "xor" && values.length === 2) {
    const [a, b] = values.map((v) => truth(v, op));
    return a === UNKNOWN || b === UNKNOWN ? UNKNOWN : a !== b;
  }
  if (values.includes(UNKNOWN)) return UNKNOWN;
  if ((op === "==" || op === "!=") && values.length === 2) {
    const equal = same(values[0], values[1]);
    return op === "==" ? equal : !equal;
  }
  const numbers = values.map((v) => number(v, op));
  if (numbers.length === 1 && (op === "-" || op === "+")) return op === "-" ? -numbers[0] : numbers[0];
  if (numbers.length === 2 && op in BINARY) return BINARY[op](numbers[0], numbers[1]);
  throw new QueryError(`operator '${op}' on ${args.length} operand(s) is not supported`);
}

function conditional(op, args, scope) {
  const left = truth(evaluate(args[0], scope), op);
  if ((op === "and" && left === false) || (op === "or" && left === true)) return left;
  if (op === "implies" && left === false) return true;
  const right = truth(evaluate(args[1], scope), op);
  const unknown = left === UNKNOWN || right === UNKNOWN;
  if (op === "and") return right === false ? false : unknown ? UNKNOWN : true;
  return right === true ? true : unknown ? UNKNOWN : false; // or, implies
}

function truth(value, op) {
  if (value === UNKNOWN || typeof value === "boolean") return value;
  throw new QueryError(`'${op}' needs a Boolean, not ${fmt(value)}`);
}

function number(value, op) {
  if (isNumber(value)) return value;
  throw new QueryError(`'${op}' needs a number, not ${fmt(value)}`);
}

/** Equality within a kind of value: numbers by value, elements by identity. */
function same(a, b) {
  return typeof a === typeof b && a === b;
}

export function isNumber(value) {
  return typeof value === "number";
}

/** A value for a report: numbers with at most three decimals, elements by name. */
export function fmt(value) {
  if (value === UNKNOWN) return "unknown";
  if (typeof value === "boolean") return value ? "true" : "false";
  if (isNumber(value)) {
    if (value === Infinity || value === -Infinity) return "*";
    const text = value.toFixed(3).replace(/0+$/, "").replace(/\.$/, "");
    return text === "-0" ? "0" : text;
  }
  if (typeof value === "string") return `"${value}"`;
  return nameOf(value);
}

/** An expression in SysML notation, with every nested operation in parentheses. */
export function render(expr) {
  if (is(expr, "LiteralInteger") || is(expr, "LiteralRational")) return fmt(Number(expr.value));
  if (is(expr, "LiteralBoolean")) return expr.value ? "true" : "false";
  if (is(expr, "LiteralString")) return `"${expr.value}"`;
  if (is(expr, "LiteralInfinity")) return "*";
  if (is(expr, "FeatureChainExpression")) return `${render(expr.argument[0])}.${nameOf(expr.targetFeature)}`;
  if (is(expr, "FeatureReferenceExpression")) {
    const referent = expr.referent;
    if (is(referent, "Expression")) return render(referent);
    if (isEnumerationLiteral(referent)) return `${nameOf(referent.owningNamespace)}::${nameOf(referent)}`;
    return nameOf(referent);
  }
  if (is(expr, "TriggerInvocationExpression")) return `${expr.kind} ${render(expr.argument[0])}`;
  if (is(expr, "OperatorExpression")) {
    const operands = expr.argument.map(operand);
    if (operands.length === 1)
      return /^[A-Za-z]+$/.test(expr.operator) ? `${expr.operator} ${operands[0]}` : `${expr.operator}${operands[0]}`;
    return operands.join(` ${expr.operator} `);
  }
  return `<${expr.$metaclass}>`;
}

function operand(expr) {
  if (is(expr, "FeatureReferenceExpression") && is(expr.referent, "Expression")) expr = expr.referent;
  const text = render(expr);
  const nested = is(expr, "OperatorExpression") && !is(expr, "FeatureChainExpression");
  return nested ? `(${text})` : text;
}

// ---------------------------------------------------------------- state machines

/** Every state nested in `machine` at any depth, in the order the model declares them, each state
 * before its substates. */
export function statesOf(machine) {
  const states = [];
  for (const state of machine.nestedState) {
    states.push(state);
    states.push(...statesOf(state));
  }
  return states;
}

/** Every transition declared in `machine` or in a state nested in it. */
export function transitionsOf(machine) {
  return [machine, ...statesOf(machine)].flatMap((state) => state.nestedTransition);
}

/** The states `state` starts in: the targets of the successions it owns (`first start then off;`);
 * a transition is not a succession of this kind. Only the target is read: the source, the start of
 * the state, is a feature of the standard library, which a model loaded without the library, or
 * read from a payload, cannot resolve. */
export function initialStates(state) {
  return state.ownedFeature
    .filter((feature) => is(feature, "SuccessionAsUsage"))
    .flatMap((succession) => succession.targetFeature.filter((target) => is(target, "StateUsage")));
}

/** The transition's trigger as [key, label]. The key is what must occur, as explore() selects
 * triggers by it: the name of the accepted signal's definition, or `at`, `after` or `when` for a
 * time or change event. The label says it in full (`at maintenanceTime`). [null, null] for a
 * transition without a trigger. */
export function triggerOf(transition) {
  for (const accept of transition.triggerAction) {
    const argument = accept.payloadArgument;
    if (is(argument, "TriggerInvocationExpression")) return [argument.kind, render(argument)];
    const payload = accept.payloadParameter;
    const types = payload != null ? payload.type : [];
    const name = types.length > 0 ? nameOf(types[0]) : "any";
    return [name, name];
  }
  return [null, null];
}

/** Whether the transition's guards hold: true if it has none, false if one is known to be false,
 * else true or UNKNOWN. */
export function guardOf(transition, scope) {
  let result = true;
  for (const guard of transition.guardExpression) {
    const value = truth(evaluate(guard, scope), "if");
    if (value === false) return false;
    if (value === UNKNOWN) result = UNKNOWN;
  }
  return result;
}

/** `starting -> operating on SelfTestDone if selfTestPassed and (temperature < maxTemperature)` */
export function describeTransition(transition, machine) {
  let text = `${pathBelow(transition.source, machine)} -> ${pathBelow(transition.target, machine)}`;
  const [, label] = triggerOf(transition);
  if (label != null) text += ` on ${label}`;
  const guards = transition.guardExpression.map(render);
  if (guards.length > 0) text += ` if ${guards.join(" and ")}`;
  return text;
}

/** `initial state in operating: operating::idle` */
export function describeInitial(state, initial, machine) {
  const where = state !== machine ? ` in ${pathBelow(state, machine)}` : "";
  return `initial state${where}: ${pathBelow(initial, machine)}`;
}

/** The states that enclose `state`, innermost first, below `machine`. */
export function enclosingStates(state, machine) {
  const out = [];
  let owner = state.owner;
  while (owner != null && owner !== machine && is(owner, "StateUsage")) {
    out.push(owner);
    owner = owner.owner;
  }
  return out;
}

/**
 * The states of `machine` reachable from its initial state, as a Map from each to a shortest list
 * of trigger labels that reaches it (empty for a state entered without any trigger).
 *
 * triggers: the trigger keys that can occur (see triggerOf), an array or a Set; null for every
 *           trigger.
 * bindings: values of the features the guards read, by name (a Map or [name, value] pairs). A
 *           guard that is not known to be false may hold.
 *
 * Entering a state enters every region of a parallel state, or else its initial states
 * (initialStates) and the targets of the transitions from its entry action (the form
 * `entry action initial; transition initial then off;`), and so on down. A transition from a state
 * is taken from any of its substates too, and entering a nested target makes the states enclosing
 * it active. Regions of a parallel state are explored independently of each other.
 *
 * This is a 0-1 breadth-first search: a triggered transition costs one and goes to the back of the
 * queue, entering costs nothing and goes to the front. Where several shortest sequences exist, the
 * queue's order decides which one is reported, and every language keeps that order.
 */
export function explore(machine, { triggers = null, bindings = [] } = {}) {
  const allowed = triggers == null ? null : new Set(triggers);
  const scope = new Scope(null, new Map(bindings));
  const outgoing = new Map();
  for (const transition of transitionsOf(machine)) {
    if (!outgoing.has(transition.source)) outgoing.set(transition.source, []);
    outgoing.get(transition.source).push(transition);
  }

  const paths = new Map(); // state -> the shortest trigger labels that reach it
  const entered = new Set(); // states whose initial substates have been entered
  const queue = [[machine, [], true]]; // [state, trigger labels, whether it is being entered]

  const follow = (transition, path) => {
    const [key, label] = triggerOf(transition);
    if (key != null && allowed != null && !allowed.has(key)) return;
    if (guardOf(transition, scope) === false) return;
    const target = transition.target;
    const arrivals = [[target, true], ...enclosingStates(target, machine).map((s) => [s, false])];
    for (const [state, enter] of arrivals) {
      if (label == null) queue.unshift([state, path, enter]);
      else queue.push([state, [...path, label], enter]);
    }
  };

  while (queue.length > 0) {
    const [state, path, enter] = queue.shift();
    if (!paths.has(state)) {
      paths.set(state, path);
      for (const transition of outgoing.get(state) ?? []) follow(transition, path);
    }
    if (enter && !entered.has(state)) {
      entered.add(state);
      if (state.isParallel) {
        for (const region of state.nestedState) queue.unshift([region, path, true]);
      } else {
        for (const initial of initialStates(state)) queue.unshift([initial, path, true]);
        if (state.entryAction != null)
          for (const transition of outgoing.get(state.entryAction) ?? []) follow(transition, path);
      }
    }
  }
  paths.delete(machine);
  return paths;
}

// ---------------------------------------------------------------- structure

/** The multiplicity SysML implies for a usage that declares none (see multiplicityOf). */
export const DEFAULT_ONE = Symbol("[1..1]");

/** The features that `feature` subsets or redefines by a relationship written in the model, not an
 * implied one (a part's implied subsetting of the library's `parts`). */
export function explicitSubsettings(feature) {
  return feature.ownedSubsetting
    .filter((s) => !s.isImplied && s.subsettedFeature != null)
    .map((s) => s.subsettedFeature);
}

/** Whether SysML gives `usage` the default multiplicity [1..1] when it declares none and subsets or
 * redefines nothing explicitly: an attribute, item, part or port usage (not a connection) owned by
 * a definition or usage. */
export function hasDefaultMultiplicity(usage) {
  const kind = (is(usage, "AttributeUsage") || is(usage, "ItemUsage") || is(usage, "PortUsage")) &&
    !is(usage, "ConnectionUsage");
  return kind && (is(usage.owningType, "Definition") || is(usage.owningType, "Usage"));
}

/** What says how many instances a usage stands for, by SysML's rule for usages: its own
 * multiplicity; else that of the usages it explicitly subsets or redefines, nearest first (in a
 * valid model a redefinition only narrows what it redefines, so the nearest is the tightest); else
 * DEFAULT_ONE, for a usage that has the default [1..1]; else null: nothing constrains it ([0..*]). */
export function multiplicityOf(usage) {
  const seen = new Set([usage]);
  const todo = [usage];
  while (todo.length > 0) {
    const current = todo.shift();
    if (current.multiplicity != null) return current.multiplicity;
    const subsetted = explicitSubsettings(current);
    if (subsetted.length === 0) return hasDefaultMultiplicity(current) ? DEFAULT_ONE : null;
    for (const feature of subsetted) {
      if (!seen.has(feature)) {
        seen.add(feature);
        todo.push(feature);
      }
    }
  }
  return null;
}

/** How many instances `usage` stands for in `context`. Its multiplicity's bounds, evaluated in the
 * context (a bound may be an expression: `Rotor[rotorCount]`), must be one whole number. */
export function instanceCount(usage, context) {
  const multiplicity = multiplicityOf(usage);
  if (multiplicity === DEFAULT_ONE) return 1;
  if (multiplicity == null)
    throw new QueryError(`${nameOf(usage)} in ${nameOf(context)} has no fixed number of instances: [0..*]`);
  if (!is(multiplicity, "MultiplicityRange"))
    throw new QueryError(`${nameOf(usage)}: a ${multiplicity.$metaclass} is not a range`);
  const scope = new Scope(context);
  const upper = multiplicity.upperBound != null ? evaluate(multiplicity.upperBound, scope) : UNKNOWN;
  const lower = multiplicity.lowerBound != null ? evaluate(multiplicity.lowerBound, scope) : upper;
  if (!(isNumber(upper) && lower === upper && Number.isInteger(upper)))
    throw new QueryError(`${nameOf(usage)} in ${nameOf(context)} has no fixed number of instances: ${fmt(lower)}..${fmt(upper)}`);
  return upper;
}

/** The names of a part's definitions. */
export function definitionName(part) {
  return part.partDefinition.map(nameOf).join(", ") || "(untyped)";
}

/** How many parts of each definition `root` consists of, at every depth, as a Map by definition
 * name in alphabetical order. A part occurs as often as its instance count times its owner's. */
export function billOfMaterials(root) {
  const counts = new Map();
  const walk = (node, factor) => {
    for (const part of partUsages(node)) {
      const count = factor * instanceCount(part, node);
      const key = definitionName(part);
      counts.set(key, (counts.get(key) ?? 0) + count);
      walk(part, count);
    }
  };
  walk(root, 1);
  return new Map([...counts].sort(([a], [b]) => (a < b ? -1 : a > b ? 1 : 0)));
}

/** The sum of `attribute` over `node` and every part in it at any depth, each part counted as often
 * as it occurs, each value the one that holds in its part's context (the most specific
 * redefinition). */
export function rollup(node, attribute) {
  const own = valueOf(attribute, new Scope(node));
  if (!isNumber(own)) throw new QueryError(`${nameOf(node)} has no value for ${nameOf(attribute)}`);
  let parts = 0;
  for (const part of partUsages(node)) parts += instanceCount(part, node) * rollup(part, attribute);
  return own + parts;
}

// ---------------------------------------------------------------- connectivity

/** A connection or flow between two parts, by their paths below the root (`rotors.motor`). */
export class Link {
  constructor(source, target, kind, name, item = null) {
    this.source = source;
    this.target = target;
    this.kind = kind; // "connection" or "flow"
    this.name = name;
    this.item = item; // what a flow carries: its payload's definition
  }

  describe() {
    const arrow = this.kind === "flow" ? "->" : "--";
    const carries = this.item ? ` of ${this.item}` : "";
    return `${this.source} ${arrow} ${this.target}  (${this.kind} ${this.name}${carries})`;
  }
}

/** The part that a connector's related feature designates, as names relative to the connector's
 * owner. The feature is a feature or a feature chain (`rotors.motor`, `battery.powerOut`); a port
 * at the end of the chain belongs to the part before it. */
export function partPath(feature) {
  let chain = feature.chainingFeature.length > 0 ? feature.chainingFeature : [feature];
  while (chain.length > 1 && is(chain[chain.length - 1], "PortUsage")) chain = chain.slice(0, -1);
  return chain.map(nameOf);
}

/** Every connection and flow of `root` and of the parts nested in it, owned or inherited, from each
 * connector's source feature to each of its target features. */
export function links(root) {
  const found = [];
  const walk = (node, prefix) => {
    for (const connector of modelUsages(node)) {
      if (!is(connector, "ConnectorAsUsage") || connector.sourceFeature == null) continue;
      const source = [...prefix, ...partPath(connector.sourceFeature)].join(".");
      for (const feature of connector.targetFeature) {
        const target = [...prefix, ...partPath(feature)].join(".");
        if (is(connector, "FlowUsage")) {
          const item = connector.payloadType.map(nameOf).join(", ") || null;
          found.push(new Link(source, target, "flow", nameOf(connector), item));
        } else {
          found.push(new Link(source, target, "connection", nameOf(connector)));
        }
      }
    }
    for (const part of partUsages(node)) walk(part, [...prefix, nameOf(part)]);
  };
  walk(root, []);
  return found;
}

/** The parts that the part at path `source` reaches through the links of `root`, in alphabetical
 * order: along a flow from its source to its target, along a connection either way. With `item`,
 * through the flows of that item definition only. */
export function reachableParts(root, source, { item = null } = {}) {
  const adjacent = new Map();
  const add = (from, to) => {
    if (!adjacent.has(from)) adjacent.set(from, []);
    adjacent.get(from).push(to);
  };
  for (const link of links(root)) {
    if (item != null && (link.kind !== "flow" || link.item !== item)) continue;
    add(link.source, link.target);
    if (link.kind === "connection") add(link.target, link.source);
  }
  const seen = new Set([source]);
  const todo = [source];
  while (todo.length > 0) {
    for (const next of adjacent.get(todo.pop()) ?? []) {
      if (!seen.has(next)) {
        seen.add(next);
        todo.push(next);
      }
    }
  }
  seen.delete(source);
  return [...seen].sort();
}

// ---------------------------------------------------------------- requirements

/** One required constraint of a satisfied requirement, evaluated for the satisfying feature. */
export class Verdict {
  constructor(requirement, satisfiedBy, constraint, value, operands, negated) {
    this.requirement = requirement;
    this.satisfiedBy = satisfiedBy;
    this.constraint = constraint;
    this.value = value; // true, false or UNKNOWN
    this.operands = operands; // the values of the constraint's operands, for the report
    this.negated = negated; // `not satisfy`: the constraint is expected not to hold
  }

  get outcome() {
    if (this.value === UNKNOWN) return "unknown";
    return this.value !== this.negated ? "holds" : "VIOLATED";
  }
}

/** The constraints a requirement requires, its own and the ones it inherits: those of its
 * requirement constraint memberships of kind `requirement` (`require constraint`, as opposed to
 * `assume constraint`). The derived property requiredConstraint has the owned ones only. */
export function requiredConstraints(requirement) {
  return requirement.featureMembership
    .filter((m) => is(m, "RequirementConstraintMembership") && m.kind === "requirement")
    .map((m) => m.ownedConstraint);
}

/** The expression whose value is the constraint's result, or null. */
export function resultExpression(constraint) {
  const membership = constraint.ownedMembership.find((m) => is(m, "ResultExpressionMembership"));
  return membership ? membership.ownedResultExpression : null;
}

/** Every required constraint of every satisfied requirement in the model (not in the standard
 * library), evaluated for the feature that satisfies it: the requirement's subject is that
 * feature, the requirement's other features have the values the requirement gives them, and
 * `derived` (a Map by attribute name) computes the attributes the model leaves to computation (a
 * total mass is a roll-up). */
export function checkSatisfactions(model, derived) {
  const verdicts = [];
  for (const satisfy of elementsOfType(model, "SatisfyRequirementUsage")) {
    if (satisfy.isLibraryElement) continue;
    const requirement = satisfy.satisfiedRequirement;
    const by = satisfy.satisfyingFeature;
    const subject = requirement.subjectParameter;
    const scope = new Scope(requirement, new Map(subject != null ? [[nameOf(subject), by]] : []), derived);
    for (const constraint of requiredConstraints(requirement)) {
      const expr = resultExpression(constraint);
      if (expr == null) throw new QueryError(`${nameOf(constraint)} has no result expression`);
      const value = evaluate(expr, scope);
      const operands = is(expr, "OperatorExpression") ? expr.argument.map((a) => evaluate(a, scope)) : [];
      verdicts.push(new Verdict(requirement, by, constraint, value, operands, Boolean(satisfy.isNegated)));
    }
  }
  return verdicts;
}
