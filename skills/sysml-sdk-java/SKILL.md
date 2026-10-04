---
name: sysml-sdk-java
description: Writes Java code that reads, navigates and queries SysML v2 and KerML models with the sysml-sdk jar (packages org.openmbee.sysml and .classes), through the OpenMBEE SysML Toolkit (a native binding library) or from a full-form interchange JSON payload. Use when a task involves loading .sysml/.kerml files or SysML v2 JSON in Java, walking parts, features, states, connections or requirements, evaluating a model's expressions, or answering questions about a SysML v2 model with code.
---

# SysML v2 SDK for Java

The SDK is the OMG metamodel as Java interfaces: every KerML and SysML metaclass, property and
operation, under its specification name, with the metaclass hierarchy as interface inheritance.
A model comes from one of two backends:

- **the SysML Toolkit**, through its native binding library (the foreign function API): reads SysML
  text, resolves names, computes derived properties and evaluates operations, and refuses what it
  cannot derive completely;
- **a payload**: a full-form interchange JSON export from any SysML v2 tool, pure Java.

The SDK never guesses. A member the SysML Toolkit does not answer throws
`NotImplementedInToolkitException` with the reason the SysML Toolkit gives; a reference that does
not resolve throws `UnresolvedReferenceException`. Never catch these to substitute a made-up
value; read around them as described below, or report them.

## Setup

- JDK 21 or later. On JDK 21 compile and run with `--enable-preview` (and `--release 21` for
  `javac`): the foreign function API is a preview there; from JDK 22 on leave both out. Add
  `--enable-native-access=ALL-UNNAMED` to silence the native-access warning.
- Put `sysml-sdk-0.1.0.jar` on the class path; no dependencies, no build tool (the class path
  separator is `;` on Windows, quoted in Git Bash, and `:` elsewhere):

  ```sh
  javac --enable-preview --release 21 -cp sysml-sdk-0.1.0.jar Main.java
  java --enable-preview --enable-native-access=ALL-UNNAMED -cp "sysml-sdk-0.1.0.jar;." Main   # Windows
  java --enable-preview --enable-native-access=ALL-UNNAMED -cp sysml-sdk-0.1.0.jar:. Main     # Linux, macOS
  ```
- The jar carries the binding library for Windows x64, Linux x64, and macOS on Apple
  silicon and on Intel, and copies the one for the running platform into the temporary directory on
  first use. To use another build, set `SYSMLV2_ABI` to it, or name it in code with
  `new ToolkitBackend.Library(Path.of(...))` in place of `ToolkitBackend.library()`.
- The jar also carries the standard library models.

**Initialize first: get the standard library.** Nearly every model refers to it (`ScalarValues::Real`,
`ISQ::mass`, and implicitly `Parts::parts`, `Items::items`, ...). Do this once, at the start of the
program, before loading any model:

```java
Path libraryDir = StandardLibrary.directory();   // the models the jar carries (copied out to the user cache once)
Path libraryJson = StandardLibrary.json();       // the library as JSON, for a payload's PayloadLibrary
```

`StandardLibrary.json()` downloads the JSON from the SDK's GitHub release on its first call (checked
against the SHA-256 GitHub states, kept in the user cache; later calls read the cache) and throws
`LibraryUnavailableException` with instructions when it cannot. Offline, `SYSML_LIBRARY_JSON` names a
copy (the `sysml.library.full.json` of the release's `sysml_library-0.1.0.zip`). Call only the one
the chosen backend needs.

Costs: opening a model with the standard library takes one to three seconds; the SysML Toolkit's
JSON export of a model takes a few seconds; the library JSON is about 160 MB and needs `-Xmx2g` to
be read (read it once). A model file or library directory that does not exist throws an
`SdkException` naming it; a model that does not load throws with a status, and the SysML Toolkit's
reason (a parse error) is printed on stderr.

## Loading a model

Through the SysML Toolkit:

```java
import java.nio.file.*;
import java.util.*;
import org.openmbee.sysml.*;              // Model, ToolkitBackend, Element, PayloadLibrary, the exceptions
import org.openmbee.sysml.classes.*;      // the metaclasses

try (ToolkitBackend toolkit = ToolkitBackend.open(ToolkitBackend.library(),   // the jar's binding library, or SYSMLV2_ABI's
        List.of("model.sysml"),                       // your files; List.of() with sources
        null,                                         // or in-memory sources: Map.of("model.sysml", text)
        libraryDir.toString())) {                     // the standard library, from the initialization
    Model model = new Model(toolkit);
    // ... your code here ...
}
```

From a payload (a JSON export you have; run with `-Xmx2g`):

```java
PayloadLibrary library = PayloadLibrary.fromJson(Files.readString(libraryJson));  // once, share it
Model model = Model.fromFullJson(Files.readString(Path.of("model.json")), library);
```

Load the standard library whenever the model uses it, which nearly every model does
(`ScalarValues::Real`, `ISQ::mass`, and implicitly `Parts::parts`). Without it, references into the
library stay unresolved: harmless until you read one, then `UnresolvedReferenceException`.
(`ToolkitBackend.open(List.of("a.sysml"))` and `ToolkitBackend.openSources(Map.of("m.sysml", text))`
open without it.) Relative paths are relative to the working directory. Element ids depend on the
file names given. With sources given, paths are ignored.

A payload refers to the standard library's elements without containing them; pass the library JSON
and those references resolve. Library elements are not the model's (`all()`, `roots()`,
`elementsOfType` skip them); a qualified name is looked up in the payload first. The library JSON
lists each library element's own members only, not what it inherits.

## The shape of the API

- **Find:** `model.resolve("Pkg::Part::feature")` returns an `Element` or `null`: cast it
  (`(PartDefinition) model.resolve(...)`). `model.elementsOfType(PartUsage.class)` (subclasses
  included), `model.all()`, `model.roots()` (top-level namespaces; the packages are their
  `getOwnedMember()`). `model.resolve` is the SysML Toolkit's own name lookup: it also finds
  inherited members and members of a feature's type (`"EBikes::CargoEBike::wheels"` is
  `Bike::wheels`, `"EBikes::CargoEBike::battery::mass"` is `Battery::mass`), and it answers where
  the specification's `resolve` operation is refused.
- **Properties** are getters, `get` + the specification name: `getQualifiedName()`,
  `getOwnedFeature()`, `getIsComposite()`. **Operations** keep their names: `effectiveName()`,
  `resolve("Name")` on a `Namespace`, `instantiatedType()`.
- **Metaclass tests:** `el instanceof PartUsage p` (the interfaces are the real hierarchy).
  `ConnectionUsage`, `InterfaceUsage` and `AllocationUsage` are `PartUsage`s too (`FlowUsage` is
  not); every connector, connections, flows, successions and bindings alike, is a
  `ConnectorAsUsage`. A definition's `getOwnedPart()` therefore lists its connections too.
  `el.$metaclass()` is the concrete metaclass name.
- **Every element** has the members of `Element`: `getName()`, `getDeclaredName()`,
  `getQualifiedName()`, `getOwner()`, `getOwningNamespace()`, `getDocumentation()`,
  `getIsLibraryElement()`, `effectiveName()`. A getter declared by a subclass needs a cast first.
- **Values:** multi-valued properties are `List<T>` (empty when absent); single-valued ones are the
  type or `null` (`getSourceFeature()` is one feature, `getTargetFeature()` a list). `Boolean`
  (nullable: test `Boolean.TRUE.equals(x)`), `Long` for Integer, `Double` for Real, `String` for
  enumeration values (lowercase: `"requirement"`). An unnamed element has `getName()` null.
- **Identity:** within one model, `equals`/`hashCode` go by element id (`$id()`), so elements work in
  sets and as map keys. The same element read through two models (the SysML Toolkit's and its
  export's) is not `equals`: compare their `$id()`s. Runtime members start with `$`: `$id()`,
  `$handle()`, `$metaclass()`, `$model()`, `$raw(prop)`.
- **Errors:** `NotImplementedInToolkitException`, `UnresolvedReferenceException`, `GoneException`,
  all `SdkException`s (unchecked). An operation argument that must be an element and is null throws
  `IllegalArgumentException`.
- **Operations** need the SysML Toolkit (a payload throws for every one) and take the
  specification's parameters in order, as the interfaces declare them
  (`inheritedMemberships(excludedNamespaces, excludedTypes, excludeImplied)`). The SysML Toolkit has
  a body for about a third of them and answers a call only where its evidence for the model is
  complete. Answered on a typical model: `effectiveName()` (not on every unnamed feature),
  `effectiveShortName()`, `resolveGlobal`, `supertypes(true)` (the argument is `excludeImplied`),
  `evaluate(target)` (target: the element to evaluate in, such as the part usage, never null; it
  returns elements, such as the literal a feature is bound to), `modelLevelEvaluable`. Refused
  there: `resolve`, `resolveLocal`, `resolveVisible`, `visibleMemberships`, `namesOf`,
  `inheritedMemberships`, `supertypes(false)`. No body at all: `isCompatibleWith`, `specializes`,
  `allSupertypes`, `directionOf`, ... Catch the refusal.
- **Name clashes:** `classes.*` has `Function`, `Class`, `Package`, `Type`, `Predicate`,
  `Expression`. Import `java.util.function.Function` by its single name (it then wins), and do not
  use bare `Class` or `Package` (ambiguous with `java.lang`): import the metaclass by its single
  name if you need it.

## When the SysML Toolkit refuses a read (read this before writing queries)

The SysML Toolkit's checked reader answers a property only where it can derive it completely. In
this release it refuses most of what builds on inheritance, so plan for it:

- **refused on nearly every element:** `getFeature()`, `getInheritedFeature()`,
  `getInheritedMembership()`, `getFeatureMembership()`, `getMember()`, `getMembership()`,
  `getImportedMembership()`, `getEndFeature()`, `getInput()`, `getOutput()`, `getUsage()`,
  `getDirectedUsage()`, `getDefinition()` and the kind-specific definitions
  (`getPartDefinition()`, `getPortDefinition()`, `getItemDefinition()`,
  `getRequirementDefinition()`, ...), `getParameter()` and `getFeaturingType()`;
- **refused on features, even with a written typing:** `getType()`. Assume it is refused and use
  `typesOf` below;
- **refused on connectors, accept actions, requirements and satisfy relations:**
  `getSourceFeature()`, `getTargetFeature()`, `getConnectorEnd()`, `getRelatedFeature()`,
  `getPayloadParameter()`, `getSubjectParameter()`, `getActorParameter()`,
  `getStakeholderParameter()`, `getSatisfyingFeature()`;
- **answered:** what the model states itself: `getOwnedMember()`, `getOwnedFeature()`,
  `getOwnedMembership()`, the owned lists (`getOwnedUsage()`, `getOwnedPart()`, `getNestedUsage()`,
  `getNestedPart()`, `getNestedAttribute()`, `getNestedState()`, `getNestedTransition()`, ...; owned
  members only, so an empty `getNestedPart()` does not mean a usage has no parts), `getOwner()`,
  `getOwningNamespace()`, `getOwningType()`, `getName()`, `getDeclaredName()`,
  `getQualifiedName()`, `getDocumentation()`, `getMultiplicity()`, `getDirection()`,
  `getIsComposite()`, `getIsEnd()`, `getIsImplied()`, `getEntryAction()`, `getIsParallel()`,
  `getTriggerAction()`, a transition's `getSource()` and `getTarget()`,
  `getSatisfiedRequirement()`, `getChainingFeature()`, and the specific owned relationships with
  their ends: `getOwnedTyping()` (`getType()`), `getOwnedSubclassification()`
  (`getSuperclassifier()`), `getOwnedSpecialization()` (`getGeneral()`), `getOwnedSubsetting()`
  (`getSubsettedFeature()`; it holds the redefinitions too), `getOwnedRedefinition()`
  (`getRedefinedFeature()`), `getOwnedReferenceSubsetting()` (`getReferencedFeature()`), and a port
  definition's `getConjugatedPortDefinition()` / `getOriginalPortDefinition()`.
  (`getOwnedMember()`, `getOwnedFeature()` and `getOwnedMembership()` are refused on a
  `FeatureReferenceExpression`.)

The refusal's message names the member and the SysML Toolkit's reason (`property has an incomplete
derivation`, `Type features are incomplete: IncompleteProvider`, `incomplete operation evidence`):
the SysML Toolkit lacks complete evidence for that element, which says nothing about whether the
model is right. Whether a read is refused depends on the property and the element; try one element
first rather than assume. Where it is refused, in this order:

**1. Read what the model states.** The helpers below derive what the common refused properties
give, from the owned relationships (verified through the SysML Toolkit and through the export alike;
copy them into your class as they are). `getIsImplied()` marks the relationships the SysML Toolkit
adds from the library, which the model does not write.

```java
/** The features a feature redefines or subsets as the model writes them (getOwnedSubsetting()
 *  holds the redefinitions too; getIsImplied() marks what the SysML Toolkit adds from the
 *  library). */
static List<Feature> writtenGenerals(Feature feature) {
    List<Feature> out = new ArrayList<>();
    for (Subsetting s : feature.getOwnedSubsetting())
        if (!Boolean.TRUE.equals(s.getIsImplied())) out.add(s.getSubsettedFeature());
    return out;
}

/** A definition's supertypes as the model writes them, without the library ones the SysML Toolkit
 *  implies (Parts::Part for a part definition). */
static List<Classifier> writtenSupertypes(Classifier definition) {
    List<Classifier> out = new ArrayList<>();
    for (Subclassification s : definition.getOwnedSubclassification())
        if (!Boolean.TRUE.equals(s.getIsImplied())) out.add(s.getSuperclassifier());
    return out;
}

/** A feature's types: getType() where the SysML Toolkit answers it; else its own typings; else
 *  those of the features it redefines or subsets as written (`part :>> battery[2];`). */
static List<Type> typesOf(Feature feature) {
    try { return feature.getType(); } catch (NotImplementedInToolkitException refused) { /* read around it */ }
    List<Type> own = new ArrayList<>();
    for (FeatureTyping t : feature.getOwnedTyping()) own.add(t.getType());
    if (!own.isEmpty()) return own;
    for (Feature general : writtenGenerals(feature)) {
        List<Type> found = typesOf(general);
        if (!found.isEmpty()) return found;
    }
    return List.of();
}

/** t's own features, then those it inherits along what the model writes (a definition's
 *  supertypes; a usage's types and the usages it redefines or subsets), without the ones redefined
 *  on the way. The model's own features, not the library's; not the specification's full
 *  derivation (no visibility, no imports). */
static List<Feature> featuresWithInherited(Type t) { return featuresWithInherited(t, new HashSet<>()); }

static List<Feature> featuresWithInherited(Type t, Set<Element> seen) {
    if (!seen.add(t)) return new ArrayList<>();
    List<Feature> own = new ArrayList<>(t.getOwnedFeature());
    Set<Feature> redefined = new HashSet<>();
    for (Feature f : own) for (Redefinition r : f.getOwnedRedefinition()) redefined.add(r.getRedefinedFeature());
    List<Type> bases = new ArrayList<>();
    if (t instanceof Classifier c) bases.addAll(writtenSupertypes(c));
    else if (t instanceof Feature f) { bases.addAll(typesOf(f)); bases.addAll(writtenGenerals(f)); }
    for (Type base : bases)
        for (Feature f : featuresWithInherited(base, seen))
            if (!redefined.contains(f) && !own.contains(f)) own.add(f);
    return own;
}

/** The feature that stands for `feature` in `context`: `feature` itself where the context inherits
 *  it, or the nearest one that redefines it, directly or through other redefinitions; null where the
 *  context has neither (pass the declaration, `Component::mass`, not a redefinition in some other
 *  definition). */
static Feature featureIn(Type context, Feature feature) {
    for (Feature f : featuresWithInherited(context))
        if (f.equals(feature) || redefines(f, feature, new HashSet<>(Set.of(f)))) return f;
    return null;
}

static boolean redefines(Feature f, Feature target, Set<Feature> seen) {
    for (Redefinition r : f.getOwnedRedefinition()) {
        Feature g = r.getRedefinedFeature();
        if (g.equals(target) || (seen.add(g) && redefines(g, target, seen))) return true;
    }
    return false;
}

/** The expression that gives `feature` its value in `context` (`attribute :>> mass = 2.5;` in the
 *  context's definition or body), or null. Initial values (`:=`) and defaults (`default =`) count
 *  too; the FeatureValue's getIsInitial() / getIsDefault() tell them apart. */
static Expression boundValue(Type context, Feature feature) {
    Feature f = featureIn(context, feature);
    if (f == null) return null;
    for (Membership m : f.getOwnedMembership())
        if (m instanceof FeatureValue v) return v.getValue();
    return null;
}

/** The feature a chain of written redefinitions starts from (`Frame::mass` -> `Unit::mass`): the
 *  declaration to pass to boundValue and featureIn. */
static Feature declarationOf(Feature feature) {
    Set<Feature> seen = new HashSet<>(Set.of(feature));
    while (true) {
        Feature next = null;
        for (Redefinition r : feature.getOwnedRedefinition())
            if (!Boolean.TRUE.equals(r.getIsImplied())) { next = r.getRedefinedFeature(); break; }
        if (next == null || !seen.add(next)) return feature;
        feature = next;
    }
}

/** {lower, upper} of a MultiplicityRange with literal bounds; upper null for `*`. `[4]` has only an
 *  upper bound: lower = upper then. A bound that is an expression (`[rotorCount]`) throws. */
static Long[] bounds(MultiplicityRange range) {
    Long upper = literal(range.getUpperBound());
    return new Long[] { range.getLowerBound() == null ? upper : literal(range.getLowerBound()), upper };
}

static Long literal(Expression e) {
    if (e instanceof LiteralInfinity) return null;
    if (e instanceof LiteralInteger i) return i.getValue();
    throw new IllegalArgumentException("a bound to evaluate in context: " + e.$metaclass());
}

/** {lower, upper} of the multiplicity that applies to a usage: its own; else, nearest first, that
 *  of the features it redefines or subsets as written; else {1, 1} where SysML gives the default
 *  (an attribute, item, part or port usage, not a connection, owned by a definition or usage);
 *  else {0, null}, unconstrained. */
static Long[] multiplicityOf(Feature usage) {
    Set<Feature> seen = new HashSet<>(Set.of(usage));
    Deque<Feature> todo = new ArrayDeque<>(List.of(usage));
    while (!todo.isEmpty()) {
        Feature u = todo.removeFirst();
        if (u.getMultiplicity() instanceof MultiplicityRange range) return bounds(range);
        List<Feature> generals = writtenGenerals(u);
        if (generals.isEmpty()) {
            boolean kind = (u instanceof AttributeUsage || u instanceof ItemUsage || u instanceof PortUsage)   // a PartUsage is an ItemUsage
                && !(u instanceof ConnectionUsage);
            boolean owned = u.getOwningType() instanceof Definition || u.getOwningType() instanceof Usage;
            return kind && owned ? new Long[] {1L, 1L} : new Long[] {0L, null};
        }
        for (Feature g : generals) if (seen.add(g)) todo.addLast(g);
    }
    return new Long[] {0L, null};
}

/** A connector's ends in order, the source first: the features or feature chains it connects. For
 *  connections, flows, successions and bindings alike. */
static List<Feature> ends(Connector connector) {
    List<Feature> out = new ArrayList<>();
    try {
        out.add(connector.getSourceFeature());
        out.addAll(connector.getTargetFeature());
        return out;
    } catch (NotImplementedInToolkitException refused) { out.clear(); }
    for (Feature e : connector.getOwnedFeature())
        if (Boolean.TRUE.equals(e.getIsEnd()) && e.getOwnedReferenceSubsetting() != null)
            out.add(e.getOwnedReferenceSubsetting().getReferencedFeature());
    return out;
}

/** A feature chain as its features ([battery, powerOut] for battery.powerOut); else [feature]. */
static List<Feature> path(Feature feature) {
    return feature.getChainingFeature().isEmpty() ? List.of(feature) : feature.getChainingFeature();
}

/** What an accept action accepts (a transition's getTriggerAction().get(0)): its payload's types. */
static List<Type> acceptedTypes(AcceptActionUsage accept) {
    try { return accept.getPayloadParameter().getType(); } catch (NotImplementedInToolkitException refused) { /* read around it */ }
    for (Membership m : accept.getOwnedMembership())
        if (m instanceof ParameterMembership p) return typesOf(p.getOwnedMemberParameter());
    return List.of();
}

/** What satisfies the requirement in `satisfy r by x;`: x. */
static Feature satisfyingFeature(SatisfyRequirementUsage satisfy) {
    try { return satisfy.getSatisfyingFeature(); } catch (NotImplementedInToolkitException refused) { /* read around it */ }
    for (Membership m : satisfy.getOwnedMembership())
        if (m instanceof SubjectMembership sm)
            for (Membership v : sm.getOwnedSubjectParameter().getOwnedMembership())
                if (v instanceof FeatureValue fv && fv.getValue() instanceof FeatureReferenceExpression ref) return ref.getReferent();
    return null;
}

/** The subject parameter of a requirement or requirement definition: its own `subject`; else,
 *  nearest first, that of its definitions or of what it specializes as written; else null. */
static Usage subjectOf(Type requirement) {
    try {
        if (requirement instanceof RequirementUsage r) return r.getSubjectParameter();
        if (requirement instanceof RequirementDefinition r) return r.getSubjectParameter();
    } catch (NotImplementedInToolkitException refused) { /* read around it */ }
    for (Membership m : requirement.getOwnedMembership())
        if (m instanceof SubjectMembership sm) return sm.getOwnedSubjectParameter();
    List<Type> bases = new ArrayList<>();
    if (requirement instanceof Classifier c) bases.addAll(writtenSupertypes(c));
    else if (requirement instanceof Feature f) { bases.addAll(typesOf(f)); bases.addAll(writtenGenerals(f)); }
    for (Type base : bases) {
        Usage found = subjectOf(base);
        if (found != null) return found;
    }
    return null;
}

/** The states a state machine (a StateUsage or StateDefinition) starts in: the targets of its
 *  successions from its entry action (`entry; then off;`) or from the library's `start`
 *  (`first start then off;`), and of its transitions from its entry action
 *  (`entry action initial; transition initial then off;`). */
static List<StateUsage> initialStates(Type machine) {
    ActionUsage entry = machine instanceof StateUsage su ? su.getEntryAction()
        : machine instanceof StateDefinition sd ? sd.getEntryAction() : null;
    List<StateUsage> out = new ArrayList<>();
    for (Feature f : machine.getOwnedFeature()) {
        if (f instanceof TransitionUsage t) {
            if (entry != null && entry.equals(t.getSource()) && t.getTarget() instanceof StateUsage target) out.add(target);
        } else if (f instanceof SuccessionAsUsage s) {
            List<Feature> e = ends(s);
            if (e.size() == 2 && e.get(1) instanceof StateUsage target && ((entry != null && entry.equals(e.get(0)))
                    || (Boolean.TRUE.equals(e.get(0).getIsLibraryElement()) && "start".equals(e.get(0).getName()))))
                out.add(target);
        }
    }
    return out;
}

/** The states of a state machine reachable from its initial states along its own transitions, in
 *  the order found (guards and triggers not considered; the transitions inside composite states not
 *  followed). */
static List<StateUsage> reachableStates(Type machine) {
    Map<Element, List<ActionUsage>> next = new HashMap<>();
    for (Feature f : machine.getOwnedFeature())
        if (f instanceof TransitionUsage t) next.computeIfAbsent(t.getSource(), k -> new ArrayList<>()).add(t.getTarget());
    List<StateUsage> reached = new ArrayList<>(initialStates(machine));
    for (int i = 0; i < reached.size(); i++)
        for (ActionUsage n : next.getOrDefault(reached.get(i), List.of()))
            if (n instanceof StateUsage s && !reached.contains(s)) reached.add(s);
    return reached;
}

/** The number an expression stands for when it is a literal, or a sign applied to one (`-40.0` is
 *  an OperatorExpression `-` whose operand is the literal 40.0). Anything else throws: evaluate it
 *  in context (the SysML Toolkit's evaluate refuses operator expressions). */
static double number(Expression expr) {
    if (expr instanceof LiteralInteger i) return i.getValue();
    if (expr instanceof LiteralRational r) return r.getValue();
    if (expr instanceof OperatorExpression op && ("-".equals(op.getOperator()) || "+".equals(op.getOperator()))) {
        List<Expression> operands = new ArrayList<>();
        for (Feature f : op.getOwnedFeature())
            for (Membership m : f.getOwnedMembership()) if (m instanceof FeatureValue v) operands.add(v.getValue());
        if (operands.size() == 1) return "-".equals(op.getOperator()) ? -number(operands.get(0)) : number(operands.get(0));
    }
    throw new IllegalArgumentException("not a literal number: " + expr.$metaclass());
}
```

**2. Query the SysML Toolkit's export as a payload** when a question needs the specification's full
derivation across a whole model (inherited members with visibility and imports, connector ends of
library connections). The export is written by the SysML Toolkit's compatibility path: it carries a
value for every property, including the ones the checked reader refuses, which the SysML Toolkit
does not vouch for. Say so in your answer when the result depends on them. Run with `-Xmx2g`.

```java
PayloadLibrary library = PayloadLibrary.fromJson(Files.readString(StandardLibrary.json()));  // once
Model exported = Model.fromFullJson(toolkit.fullJson(), library);   // same element ids as the SysML Toolkit's
```

**3. Never** substitute an empty list, a guess or a default for a refused read.

## Navigation recipes

With the helpers above; each works through the SysML Toolkit and on a payload.

```java
// d, part, port, usage, flow, machine and constraint stand for elements you have at hand (d: a
// definition or usage, as a Type); model is the Model.

// Parts of a definition or usage, owned and inherited, not connections; the model's own, not the
// library's (start, done, subparts, ...)
List<PartUsage> parts = new ArrayList<>();
for (Feature f : featuresWithInherited(d))
    if (f instanceof PartUsage p && !(f instanceof ConnectionUsage)) parts.add(p);

List<Type> definitions = typesOf(part);             // what a usage is typed by

// The value a feature has in a context (`attribute :>> mass = 2.5;` in the part's definition or body)
Feature massF = (Feature) model.resolve("Pkg::Component::mass");   // the declaration; declarationOf(f) finds it from a redefinition
Expression expr = boundValue(part, massF);           // or null
double mass = number(expr);                          // a literal, or a sign applied to one (-40.0)

Long[] m = multiplicityOf(usage);                    // {2, 2} for [2]; {1, 1} by default; upper null for *
String text = m[0].equals(m[1]) ? "[" + m[0] + "]" : "[" + m[0] + ".." + (m[1] == null ? "*" : m[1]) + "]";

// Roll-up: an attribute summed over a definition's direct parts (mass, cost, power); for a deeper
// tree, apply it to each part that has parts of its own (featuresWithInherited(p))
double total = 0;
List<String> missing = new ArrayList<>();
for (PartUsage p : parts) {
    Expression e = boundValue(p, massF);
    Long[] n = multiplicityOf(p);
    if (e == null || n[1] == null || !n[0].equals(n[1])) missing.add(p.getName());   // no value, or a range ([4..6], *): report, never guess
    else total += number(e) * n[0];
}

// Ports: the definition, and whether it is conjugated (`port p : ~PowerPort;`). getPortDefinition()
// (a list) is refused; getIsConjugated() is false on a `~` port: the conjugation is its type, the
// ConjugatedPortDefinition that PowerPort owns (name "~PowerPort", qualified name
// "Pkg::PowerPort::'~PowerPort'", quotes included)
for (Type t : typesOf(port))
    System.out.println(t instanceof ConjugatedPortDefinition c ? "~" + c.getOriginalPortDefinition().getQualifiedName() : t.getQualifiedName());

// Connections (ConnectionUsage: connect, interfaces, allocations): the ends, the source first. The
// ends of an inherited connection are the supertype's features (Drone::rotors, not a redefining
// `part :>> rotors[8];`): featureIn(d, chain.get(0)) is the one that stands for it in d
for (Feature f : featuresWithInherited(d))
    if (f instanceof ConnectionUsage c)
        for (Feature end : ends(c)) {
            List<Feature> chain = path(end);                  // [battery, powerOut], then the target
            List<Type> portTypes = typesOf(chain.get(chain.size() - 1));   // [PortDefinition or ConjugatedPortDefinition]
        }
// Flows (FlowUsage), bindings (BindingConnectorAsUsage) and successions are ConnectorAsUsage too,
// with the same ends()
List<Classifier> items = ((FlowUsage) flow).getPayloadType();     // the flowing item's definition

// States
StateUsage machine = (StateUsage) model.resolve("Pkg::Controller::modes");   // e.g. an ExhibitStateUsage
List<StateUsage> states = machine.getNestedState();   // its own states
List<StateUsage> initial = initialStates(machine);   // `entry; then off;`, `first start then off;`, ...
List<StateUsage> reachable = reachableStates(machine);   // from the initial states along the transitions
for (TransitionUsage t : machine.getNestedTransition()) {
    ActionUsage from = t.getSource(), to = t.getTarget();   // the states (cast to StateUsage); an initial
    // transition (`transition init then off;`) leaves the entry action, not a state: no cast then
    List<Type> signals = t.getTriggerAction().isEmpty() ? List.of() : acceptedTypes(t.getTriggerAction().get(0));
    if (!t.getTriggerAction().isEmpty())
        t.getTriggerAction().get(0).getPayloadArgument();   // TriggerInvocationExpression for at/when
}

// Requirements
for (SatisfyRequirementUsage s : model.elementsOfType(SatisfyRequirementUsage.class)) {
    RequirementUsage req = s.getSatisfiedRequirement();
    Feature by = satisfyingFeature(s);               // the part that satisfies it
    List<Type> definition = typesOf(req);            // [RequirementDefinition]
    Usage subject = subjectOf(req);                  // its subject parameter, or null; often the definition's
                                                     // (`subject bike : Bike;` in MassLimit: MassLimit::bike)
    Feature limitF = null;                           // the definition's maxMass
    for (Type t : typesOf(req)) for (Feature f : featuresWithInherited(t)) if ("maxMass".equals(f.getName())) limitF = f;
    Expression limit = boundValue(req, limitF);      // the requirement's value for it
    List<ConstraintUsage> constraints = new ArrayList<>();   // its own constraints
    for (Membership mm : req.getOwnedMembership())
        if (mm instanceof RequirementConstraintMembership rc && "requirement".equals(rc.getKind())) constraints.add(rc.getOwnedConstraint());
}
```

## Multiplicity

`getMultiplicity()` is only what the usage itself declares; `multiplicityOf` above applies SysML's
rule: a usage without one takes the multiplicity of the features it redefines or subsets as written
(nearest first; `part :>> battery[2];` declares its own), and one that redefines or subsets nothing
is `[1..1]` if it is an attribute, item, part or port usage owned by a definition or usage, and
unconstrained (`[0..*]`) otherwise. Implied subsettings (`getIsImplied()`, such as every part's
subsetting of the library's `parts`) do not count. A bound may be an expression to evaluate in
context (`Rotor[rotorCount]`); `bounds` throws for it.

## Evaluating the model's expressions

Expressions are elements: `OperatorExpression` (`getOperator()` such as `"<="`, `"and"`, `"not"`;
`getArgument()`), `FeatureReferenceExpression` (`getReferent()`), `FeatureChainExpression` (`a.b`:
argument 0 is `a`, `getTargetFeature()` is `b`; it is an OperatorExpression too, so test it first),
literals with `getValue()` (`LiteralInteger` Long, `LiteralRational` Double, `LiteralBoolean`,
`LiteralString`, `LiteralInfinity` for `*`), `TriggerInvocationExpression` (`getKind()`: `at`,
`after`, `when`). An enumeration literal is an `EnumerationUsage` whose `getOwningNamespace()` is the
`EnumerationDefinition` (its owning type is null, by the specification). Through the SysML Toolkit,
`getArgument()` is refused: an expression's operands are its `getOwnedFeature()`s, each bound by a
`FeatureValue` in its `getOwnedMembership()`.

The SysML Toolkit's `evaluate(target)` operation evaluates some expressions (it returns elements);
beyond that, the SDK does not evaluate. The SDK's source repository
(https://github.com/Open-MBEE/sysml-sdk, not the release packages) has, in
`examples/queries/java/Queries.java`, an evaluator in three-valued logic and complete queries:
reachable states with shortest trigger sequences, bill of materials, attribute roll-up,
connectivity, requirement checks. Where it is at hand, start from it for such questions; the recipes
above cover the simple cases.

## Other differences from the specification

- **With the library loaded**, `getUsage()`/`getFeature()` (in a payload) include library features:
  filter with `getIsLibraryElement()`. A flow's `getConnectorEnd()` also lists the library flow's
  ends; use `ends`.
- The second operand of `and`/`or`/`implies` is the operand expression itself, not the
  `FeatureReferenceExpression` to it that the specification builds. Accept both.
- An unnamed `satisfy` has no name, but a qualified-name lookup may still find it under the name of
  the requirement it satisfies. A feature named only through what it redefines has `getName()` from
  it and `getDeclaredName()` null (a satisfy's subject parameter is named `subj`, although it
  redefines the subject of the requirement's definition).
- `getMayTimeVary()`, `getIsVariable()` and `getIsConstant()` on usages throw
  `NotImplementedInToolkitException`.
- In the SysML Toolkit's export, an alias (`alias ax for x;`) hides the membership of the feature it
  names from what specializations inherit, and a conjugated port's `getPortDefinition()` is the
  `ConjugatedPortDefinition` (`~PowerPort`), as the specification says.
- A payload answers what its writer wrote: stand-ins where the writer had no value (null, `false`,
  an empty list), and `UnresolvedReferenceException` for references
  into the standard library unless it was given the library JSON.

## Verify

- Run the same query through the SysML Toolkit (with the helpers) and on its export as a payload
  (the plain derived properties), matching elements by `$id()`; differences point at the
  SysML Toolkit, the export, or your code.
- Format numbers with `Locale.ROOT` (or `BigDecimal`) so output does not depend on the machine's
  locale.
- For more examples: the guide and runnable tutorial (`examples/java/`) and the query examples
  (`examples/queries/java/`) in the SDK repository.
