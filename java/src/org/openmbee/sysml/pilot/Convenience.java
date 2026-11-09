package org.openmbee.sysml.pilot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The bodies of the SDK's convenience members (the {@code find} members of {@code convenience.json})
 * over the Pilot Implementation's model, read through the engine's EMF reflection.
 *
 * <p>Each body follows its member's OCL and reads the specification properties and operations that
 * OCL reads, as the Pilot computes them, so the answers are the Pilot's. Where the OCL gives
 * {@code invalid}, the member is refused with the protocol's {@code invalid}; where the OCL's
 * {@code and} or {@code or} decides on its left operand, the right one is not read, as OCL evaluates
 * it. One operation is computed here from its specification body instead of being called on the Pilot:
 * {@code Feature::allRedefinedFeatures()}, whose Pilot implementation fails on every call (its
 * generated code expects a list where the implementation gives a set). The generated dispatch
 * ({@link ConvenienceMembers}, from {@code tools/gen_pilot_convenience.py}) checks the element's
 * metaclass, converts the arguments and calls these.
 */
final class Convenience {
    /** The upper bound {@code *} as an integer: greater than every bound. */
    static final long UNBOUNDED = Long.MAX_VALUE;

    enum Scope { OWNED, INHERITED, ALL }

    enum Visibility { ANY, PROTECTED, PUBLIC }

    private final PilotEngine engine;

    Convenience(PilotEngine engine) {
        this.engine = engine;
    }

    // -- arguments, by parameter name as the protocol passes them ---------------------------------

    static Scope scope(Map<?, ?> args, String name, String fallback) {
        String v = literalArgument(args, name, fallback);
        switch (v) {
            case "owned": return Scope.OWNED;
            case "inherited": return Scope.INHERITED;
            case "all": return Scope.ALL;
            default: throw new PilotException("badRequest", name + " " + v + ": owned, inherited or all");
        }
    }

    static Visibility visibility(Map<?, ?> args, String name, String fallback) {
        String v = literalArgument(args, name, fallback);
        switch (v) {
            case "any": return Visibility.ANY;
            case "protected": return Visibility.PROTECTED;
            case "public": return Visibility.PUBLIC;
            default: throw new PilotException("badRequest", name + " " + v + ": any, protected or public");
        }
    }

    private static String literalArgument(Map<?, ?> args, String name, String fallback) {
        Object v = args.get(name);
        if (v == null) return fallback;
        if (v instanceof String s) return s;
        throw new PilotException("badRequest", name + " is a literal's name, not " + v);
    }

    static boolean bool(Map<?, ?> args, String name, boolean fallback) {
        Object v = args.get(name);
        if (v == null) return fallback;
        if (v instanceof Boolean b) return b;
        throw new PilotException("badRequest", name + " is a Boolean, not " + v);
    }

    static String str(Map<?, ?> args, String name) {
        Object v = args.get(name);
        if (v instanceof String s) return s;
        throw new PilotException("badRequest", v == null ? "the argument " + name + " is missing" : name + " is a string, not " + v);
    }

    Object element(Map<?, ?> args, String name) {
        Object v = args.get(name);
        if (v instanceof Map<?, ?> m && m.get("@id") != null) {
            Object h = m.get("@id");
            return engine.object(h instanceof Number n ? Long.toString(n.longValue()) : String.valueOf(h));
        }
        throw new PilotException("badRequest", v == null ? "the argument " + name + " is missing"
            : name + " is a reference to an element ({\"@id\": handle}), not " + v);
    }

    /** Whether the element's metaclass is one of the member's contexts. */
    void applicable(Object e, String member, String... contexts) throws ReflectiveOperationException {
        for (String c : contexts) if (isKind(e, c)) return;
        throw new PilotException("notApplicable", member + " does not apply to a " + engine.metaclassOf(e));
    }

    /** An UnlimitedNatural result as the protocol has it: {@code *} for unbounded. */
    static Object unlimited(Long v) {
        return v != null && v == UNBOUNDED ? "*" : v;
    }

    /** A literal's value, tagged so that every language tells an integer from a real and {@code *}
     *  from a string: {"integer": 4}, {"real": 0.5}, {"boolean": true}, {"string": "front"},
     *  {"unbounded": true}. */
    private static Map<String, Object> literal(String kind, Object value) {
        return Map.of(kind, value);
    }

    private static <T> T refuse(String why) {
        throw new PilotException("invalid", why);
    }

    // -- reads -----------------------------------------------------------------------------------

    private List<Object> elems(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        List<Object> out = new ArrayList<>();
        if (v instanceof Iterable<?> it) {
            for (Object x : it) out.add(resolved(x, e, prop));
        } else if (v != null) {
            out.add(resolved(v, e, prop));
        }
        return out;
    }

    private Object elem(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        if (v instanceof Iterable<?> it) {
            List<Object> xs = new ArrayList<>();
            for (Object x : it) xs.add(x);
            if (xs.size() > 1) throw new PilotException("failed", prop + ": one element was expected, the Pilot gives " + xs.size());
            v = xs.isEmpty() ? null : xs.get(0);
        }
        return v == null ? null : resolved(v, e, prop);
    }

    private Object resolved(Object x, Object owner, String prop) throws ReflectiveOperationException {
        if (engine.isProxy(x))
            throw new PilotException("unresolved", engine.metaclassOf(owner) + "." + prop + ": a reference did not resolve");
        return x;
    }

    private boolean flag(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        if (v == null) return false;
        if (v instanceof Boolean b) return b;
        throw new PilotException("failed", prop + ": a Boolean was expected, not " + v);
    }

    private String text(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        if (v == null || v instanceof String) return (String) v;
        if (engine.isEnumerator(v)) return engine.literalOf(v);
        throw new PilotException("failed", prop + ": a text was expected, not " + v);
    }

    private Long integer(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        if (v == null) return null;
        if (v instanceof java.math.BigInteger b && b.bitLength() >= 64)
            throw new PilotException("refused", prop + ": the integer " + b + " does not fit in 64 bits");
        if (v instanceof Number n) return n.longValue();
        throw new PilotException("failed", prop + ": an integer was expected, not " + v);
    }

    private Double number(Object e, String prop) throws ReflectiveOperationException {
        Object v = engine.read(e, prop);
        if (v == null) return null;
        if (v instanceof Number n) return n.doubleValue();
        throw new PilotException("failed", prop + ": a number was expected, not " + v);
    }

    /** {@code oclIsKindOf(metaclass)}. */
    private boolean isKind(Object e, String metaclass) {
        return engine.isKind(e, metaclass);
    }

    /** {@code oclIsTypeOf(metaclass)}. */
    private boolean isType(Object e, String metaclass) throws ReflectiveOperationException {
        return engine.metaclassOf(e).equals(metaclass);
    }

    private List<Object> kinds(List<Object> es, String metaclass) {
        List<Object> out = new ArrayList<>(es.size());
        for (Object e : es) if (isKind(e, metaclass)) out.add(e);
        return out;
    }

    private String nameOf(Object e) throws ReflectiveOperationException {
        return text(e, "name");
    }

    private static Set<Object> identitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private static List<Object> distinct(List<Object> es) {
        Set<Object> seen = identitySet();
        List<Object> out = new ArrayList<>(es.size());
        for (Object e : es) if (seen.add(e)) out.add(e);
        return out;
    }

    private static boolean containsSame(List<Object> es, Object x) {
        for (Object e : es) if (e == x) return true;
        return false;
    }

    // -- operations ------------------------------------------------------------------------------

    /** {@code Type::specializes(general)}. */
    private boolean specializes(Object t, Object general) throws ReflectiveOperationException {
        Object v = engine.invoke(t, "specializes", general);
        if (v == null) return false;
        if (v instanceof Boolean b) return b;
        throw new PilotException("failed", "specializes(): a Boolean was expected, not " + v);
    }

    /**
     * {@code Feature::allRedefinedFeatures()}, by its specification body (KerML, Feature operations):
     * {@code ownedRedefinition.redefinedFeature->closure(ownedRedefinition.redefinedFeature)
     * ->asOrderedSet()->prepend(self)}, the closure depth first.
     */
    private List<Object> allRedefinedFeatures(Object f) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        Set<Object> seen = identitySet();
        out.add(f);
        seen.add(f);
        Deque<Object> stack = new ArrayDeque<>();
        pushReversed(stack, directlyRedefined(f));
        while (!stack.isEmpty()) {
            Object g = stack.pop();
            if (!seen.add(g)) continue;
            out.add(g);
            pushReversed(stack, directlyRedefined(g));
        }
        return out;
    }

    /** {@code ownedRedefinition.redefinedFeature}, the implied redefinitions included. */
    private List<Object> directlyRedefined(Object f) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object r : elems(f, "ownedRedefinition")) {
            Object g = elem(r, "redefinedFeature");
            if (g != null) out.add(g);
        }
        return out;
    }

    private static void pushReversed(Deque<Object> stack, List<Object> es) {
        for (int i = es.size() - 1; i >= 0; i--) stack.push(es.get(i));
    }

    /** {@code Namespace::visibilityOf(m)}, lowercase. */
    private String visibilityOf(Object ns, Object m) throws ReflectiveOperationException {
        Object v = engine.invoke(ns, "visibilityOf", m);
        if (v == null || !engine.isEnumerator(v)) throw new PilotException("failed", "visibilityOf(): a visibility was expected, not " + v);
        return engine.literalOf(v).toLowerCase(java.util.Locale.ROOT);
    }

    /** The contract's {@code libraryType(qualifiedName)}: the type {@code resolveGlobal} finds, else none. */
    private Object libraryType(Object ns, String qualifiedName) throws ReflectiveOperationException {
        Object m = engine.invoke(ns, "resolveGlobal", qualifiedName);
        if (m == null) return null;
        Object t = elem(m, "memberElement");
        return t != null && isKind(t, "Type") ? t : null;
    }

    /** The library type of a semantic member, refused when the library does not have it. */
    private Object requiredLibraryType(Object ns, String qualifiedName) throws ReflectiveOperationException {
        Object t = libraryType(ns, qualifiedName);
        return t != null ? t : refuse("the library type " + qualifiedName + " is not in this model's library");
    }

    // -- helpers of the contract -----------------------------------------------------------------

    /** {@code isStandardLibraryElement()}: over {@code owningNamespace}, as the contract has it for now. */
    private boolean isStandardLibraryElement(Object e) throws ReflectiveOperationException {
        if (!flag(e, "isLibraryElement")) return false;
        Object ns = elem(e, "owningNamespace");
        if (ns == null) return false;
        return isKind(ns, "LibraryPackage") ? flag(ns, "isStandard") : isStandardLibraryElement(ns);
    }

    private static boolean keeps(Visibility visibility, String v) {
        switch (visibility) {
            case PUBLIC: return v.equals("public");
            case PROTECTED: return v.equals("public") || v.equals("protected");
            default: return true;
        }
    }

    /** The {@code reject} of the scope helpers: without {@code library}, the memberships of standard
     *  library namespaces leave, unless the element is itself in the standard library. */
    private List<Object> dropLibrary(Object e, List<Object> ms, boolean library) throws ReflectiveOperationException {
        if (library || isStandardLibraryElement(e)) return ms;
        List<Object> out = new ArrayList<>(ms.size());
        for (Object m : ms) {
            Object ns = elem(m, "membershipOwningNamespace");
            if (ns == null || !isStandardLibraryElement(ns)) out.add(m);
        }
        return out;
    }

    /** The inherited feature memberships whose owning type this type specializes
     *  ({@code deriveTypeFeatureMembership} since KERML11-191): a feature that a supertype imports is
     *  inherited by name only. */
    private List<Object> inheritedFeatureMemberships(Object e) throws ReflectiveOperationException {
        List<Object> ms = kinds(elems(e, "inheritedMembership"), "FeatureMembership");
        List<Object> out = new ArrayList<>(ms.size());
        for (Object m : ms) {
            Object owner = elem(m, "owningType");
            if (owner != null && specializes(e, owner)) out.add(m);
        }
        return out;
    }

    /** {@code scopedFeatureMemberships(scope, visibility, library)}. */
    private List<Object> scopedFeatureMemberships(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        List<Object> ms;
        switch (scope) {
            case OWNED: ms = elems(e, "ownedFeatureMembership"); break;
            case INHERITED: ms = inheritedFeatureMemberships(e); break;
            default:
                ms = elems(e, "ownedFeatureMembership");
                ms.addAll(inheritedFeatureMemberships(e));
                break;
        }
        List<Object> visible = new ArrayList<>(ms.size());
        for (Object m : ms) {
            if (vis == Visibility.ANY) {
                visible.add(m);
                continue;
            }
            String v = text(m, "visibility");
            if (keeps(vis, v == null ? "" : v.toLowerCase(java.util.Locale.ROOT))) visible.add(m);
        }
        return dropLibrary(e, visible, library);
    }

    private List<Object> scopedFeatures(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object m : scopedFeatureMemberships(e, scope, vis, library)) {
            Object f = elem(m, "ownedMemberFeature");
            if (f != null) out.add(f);
        }
        return out;
    }

    /** {@code scopedMemberships(scope, visibility, library)}: outside the owned scope, {@code membership}
     *  in the Pilot's order. */
    private List<Object> scopedMemberships(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        List<Object> ms;
        if (scope == Scope.OWNED) {
            ms = elems(e, "ownedMembership");
        } else if (scope == Scope.ALL) {
            ms = elems(e, "membership");
        } else {
            List<Object> owned = elems(e, "ownedMembership");
            ms = new ArrayList<>();
            for (Object m : elems(e, "membership")) if (!containsSame(owned, m)) ms.add(m);
        }
        List<Object> visible = new ArrayList<>(ms.size());
        for (Object m : ms) {
            if (vis == Visibility.ANY || keeps(vis, visibilityOf(e, m))) visible.add(m);
        }
        return dropLibrary(e, visible, library);
    }

    /** {@code scopedMembers(scope)}: outside the owned scope, {@code member} in the Pilot's order. */
    private List<Object> scopedMembers(Object e, Scope scope) throws ReflectiveOperationException {
        if (scope == Scope.OWNED) return elems(e, "ownedMember");
        // Each element once: member is a set.
        List<Object> all = distinct(elems(e, "member"));
        if (scope == Scope.ALL) return all;
        List<Object> owned = elems(e, "ownedMember");
        List<Object> out = new ArrayList<>(all.size());
        for (Object x : all) if (!containsSame(owned, x)) out.add(x);
        return out;
    }

    private boolean conformsTo(Object f, Object general) throws ReflectiveOperationException {
        for (Object t : elems(f, "type")) if (specializes(t, general)) return true;
        return false;
    }

    // -- the families ----------------------------------------------------------------------------

    /** Kind lists: {@code scopedFeatureMemberships(...).ownedMemberFeature->selectByKind(M)}. */
    List<Object> kindList(Object e, Scope scope, Visibility vis, boolean library, String metaclass) throws ReflectiveOperationException {
        return kinds(scopedFeatures(e, scope, vis, library), metaclass);
    }

    /** {@code ...->select(oclIsTypeOf(X))}. */
    List<Object> kindExact(Object e, Scope scope, Visibility vis, boolean library, String metaclass) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object f : scopedFeatures(e, scope, vis, library)) if (isType(f, metaclass)) out.add(f);
        return out;
    }

    /** The features whose type conforms to the library type; refused when the library has none. */
    List<Object> kindSemantic(Object e, Scope scope, Visibility vis, boolean library, String libType) throws ReflectiveOperationException {
        Object general = requiredLibraryType(e, libType);
        List<Object> fs = scopedFeatures(e, scope, vis, library);
        List<Object> out = new ArrayList<>();
        for (Object f : fs) if (conformsTo(f, general)) out.add(f);
        return out;
    }

    /** {@code type->exists(t | t.specializes(libraryType(L)))}. */
    boolean semanticTest(Object e, String libType) throws ReflectiveOperationException {
        return conformsTo(e, requiredLibraryType(e, libType));
    }

    Object kindByName(Object e, String name, Scope scope, Visibility vis, boolean library, String metaclass) throws ReflectiveOperationException {
        for (Object f : kindList(e, scope, vis, library, metaclass)) if (name.equals(nameOf(f))) return f;
        return null;
    }

    /** Member lists: {@code scopedMemberships(...).memberElement->selectByKind(M)}, each once. */
    List<Object> memberList(Object e, Scope scope, Visibility vis, boolean library, String metaclass) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object m : scopedMemberships(e, scope, vis, library)) {
            Object x = elem(m, "memberElement");
            if (x != null && isKind(x, metaclass)) out.add(x);
        }
        return distinct(out);
    }

    /** The member under {@code name} (a membership's {@code memberName}, an alias's included). */
    Object memberByName(Object e, String name, Scope scope, Visibility vis, boolean library, String metaclass) throws ReflectiveOperationException {
        for (Object m : scopedMemberships(e, scope, vis, library)) {
            if (!name.equals(text(m, "memberName"))) continue;
            Object x = elem(m, "memberElement");
            if (x != null && isKind(x, metaclass)) return x;
        }
        return null;
    }

    /** Definition lists: {@code scopedMembers(scope)->selectByKind(MD)}. */
    List<Object> defList(Object e, Scope scope, String metaclass) throws ReflectiveOperationException {
        return kinds(scopedMembers(e, scope), metaclass);
    }

    List<Object> defExact(Object e, Scope scope, String metaclass) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object x : scopedMembers(e, scope)) if (isType(x, metaclass)) out.add(x);
        return out;
    }

    List<Object> defSemantic(Object e, Scope scope, String libType) throws ReflectiveOperationException {
        Object general = requiredLibraryType(e, libType);
        List<Object> types = kinds(scopedMembers(e, scope), "Type");
        List<Object> out = new ArrayList<>();
        for (Object t : types) if (specializes(t, general)) out.add(t);
        return out;
    }

    Object defByName(Object e, String name, Scope scope, String metaclass) throws ReflectiveOperationException {
        for (Object d : defList(e, scope, metaclass)) if (name.equals(nameOf(d))) return d;
        return null;
    }

    /**
     * Name-keyed definition maps, {@code find{Ks}ByName(scope)}: the definitions of the list under every
     * name and short name of their memberships ({@code membership}, as {@code Namespace::namesOf} reads
     * it), in the Pilot's order, as pairs of a name and an element. Refused where one name names two
     * different elements, naming both.
     */
    List<Object> defNameMap(Object e, Scope scope, String metaclass) throws ReflectiveOperationException {
        List<Object> ds = defList(e, scope, metaclass);
        List<Object> ms = elems(e, "membership");
        Map<String, Object> byName = new LinkedHashMap<>();
        for (Object m : ms) {
            Object x = elem(m, "memberElement");
            if (x == null || !containsSame(ds, x)) continue;
            for (String prop : new String[] {"memberName", "memberShortName"}) {
                String name = text(m, prop);
                if (name == null) continue;
                Object y = byName.putIfAbsent(name, x);
                if (y != null && y != x)
                    return refuse("the name '" + name + "' names two elements: " + described(y) + " and " + described(x));
            }
        }
        List<Object> out = new ArrayList<>(byName.size());
        for (Map.Entry<String, Object> entry : byName.entrySet()) out.add(List.of(entry.getKey(), entry.getValue()));
        return out;
    }

    /** An element as a refusal names it: its name, its metaclass and the namespace that owns it (not its
     *  qualified name, which KerML leaves null for a member whose name an earlier member has). */
    private String described(Object x) throws ReflectiveOperationException {
        String name = text(x, "name");
        Object ns = elem(x, "owningNamespace");
        String owner = ns == null ? null : text(ns, "qualifiedName");
        return (name != null ? name : "an unnamed element") + " (" + engine.metaclassOf(x) + ") in "
            + (owner != null ? owner : "the root namespace");
    }

    // -- packages --------------------------------------------------------------------------------

    List<Object> findPackages(Object e) throws ReflectiveOperationException {
        return kinds(elems(e, "ownedMember"), "Package");
    }

    Object findPackage(Object e, String name) throws ReflectiveOperationException {
        for (Object p : findPackages(e)) if (name.equals(nameOf(p))) return p;
        return null;
    }

    // -- typing and specialization ---------------------------------------------------------------

    List<Object> findDeclaredTypes(Object e) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object t : elems(e, "ownedTyping")) {
            if (flag(t, "isImplied")) continue;
            Object x = elem(t, "type");
            if (x != null) out.add(x);
        }
        return out;
    }

    /** The generals of the type's owned specializations: all of them ({@code implied} null), or
     *  those whose {@code isImplied} is {@code implied}. */
    private List<Object> generals(Object e, Boolean implied) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object sp : elems(e, "ownedSpecialization")) {
            if (implied != null && flag(sp, "isImplied") != implied) continue;
            Object g = elem(sp, "general");
            if (g != null) out.add(g);
        }
        return out;
    }

    /** {@code Type::supertypes(excludeImplied)}, {@code Feature::supertypes} on a feature. */
    private List<Object> supertypes(Object e, boolean excludeImplied) throws ReflectiveOperationException {
        Object v = engine.invoke(e, "supertypes", excludeImplied);
        List<Object> out = new ArrayList<>();
        if (v instanceof Iterable<?> it) {
            for (Object x : it) out.add(resolved(x, e, "supertypes()"));
        } else if (v != null) {
            out.add(resolved(v, e, "supertypes()"));
        }
        return out;
    }

    List<Object> findDeclaredSupertypes(Object e) throws ReflectiveOperationException {
        return supertypes(e, true);
    }

    List<Object> findImpliedSupertypes(Object e) throws ReflectiveOperationException {
        List<Object> declared = supertypes(e, true);
        List<Object> out = new ArrayList<>();
        for (Object t : supertypes(e, false)) if (!containsSame(declared, t)) out.add(t);
        return out;
    }

    List<Object> findDeclaredAndImpliedSupertypes(Object e) throws ReflectiveOperationException {
        return supertypes(e, false);
    }

    List<Object> findSubsettedFeatures(Object e) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object sb : elems(e, "ownedSubsetting")) {
            if (flag(sb, "isImplied") || !isType(sb, "Subsetting")) continue;
            Object f = elem(sb, "subsettedFeature");
            if (f != null) out.add(f);
        }
        return out;
    }

    List<Object> findRedefinedFeatures(Object e) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object r : elems(e, "ownedRedefinition")) {
            if (flag(r, "isImplied")) continue;
            Object f = elem(r, "redefinedFeature");
            if (f != null) out.add(f);
        }
        return out;
    }

    Object findReferencedFeature(Object e) throws ReflectiveOperationException {
        Object r = elem(e, "ownedReferenceSubsetting");
        return r == null ? null : elem(r, "referencedFeature");
    }

    Object findRedefinitionRoot(Object e) throws ReflectiveOperationException {
        Object f = e;
        Set<Object> seen = identitySet();
        while (seen.add(f)) {
            List<Object> redefined = findRedefinedFeatures(f);
            if (redefined.isEmpty()) return f;
            f = redefined.get(0);
        }
        return refuse("the written redefinitions form a cycle");
    }

    Object findEffectiveFeature(Object e, Object feature) throws ReflectiveOperationException {
        for (Object f : elems(e, "feature"))
            if (f == feature || containsSame(allRedefinedFeatures(f), feature)) return f;
        return null;
    }

    // -- values ----------------------------------------------------------------------------------

    private List<Object> featureValues(Object e) throws ReflectiveOperationException {
        return kinds(elems(e, "ownedMembership"), "FeatureValue");
    }

    Object findValue(Object e) throws ReflectiveOperationException {
        List<Object> fvs = featureValues(e);
        return fvs.isEmpty() ? null : elem(fvs.get(0), "value");
    }

    boolean findHasDefaultValue(Object e) throws ReflectiveOperationException {
        for (Object fv : featureValues(e)) if (flag(fv, "isDefault")) return true;
        return false;
    }

    boolean findHasInitialValue(Object e) throws ReflectiveOperationException {
        for (Object fv : featureValues(e)) if (flag(fv, "isInitial")) return true;
        return false;
    }

    /** {@code operands()}: an operator expression's {@code argument}. */
    private List<Object> operands(Object x) throws ReflectiveOperationException {
        return elems(x, "argument");
    }

    private String operator(Object x) throws ReflectiveOperationException {
        return text(x, "operator");
    }

    /** {@code unsigned()}: a sign taken off. */
    private Object unsigned(Object x) throws ReflectiveOperationException {
        if (isKind(x, "OperatorExpression")) {
            String op = operator(x);
            if ("-".equals(op) || "+".equals(op)) {
                List<Object> args = operands(x);
                if (args.size() == 1) return args.get(0);
            }
        }
        return x;
    }

    private boolean isNumberLiteral(Object x) {
        return isKind(x, "LiteralInteger") || isKind(x, "LiteralRational");
    }

    boolean findHasUnit(Object x) throws ReflectiveOperationException {
        Object u = unsigned(x);
        return isKind(u, "OperatorExpression") && "[".equals(operator(u));
    }

    boolean findIsLiteral(Object x) throws ReflectiveOperationException {
        if (isKind(x, "LiteralExpression")) return true;
        Object u = unsigned(x);
        return u != x && isNumberLiteral(u);
    }

    Map<String, Object> findLiteral(Object x) throws ReflectiveOperationException {
        if (!findIsLiteral(x)) return refuse("the value is not a literal (a value with a unit, an expression or a reference)");
        if (isKind(x, "LiteralInteger")) {
            Long i = integer(x, "value");
            return i != null ? literal("integer", i) : refuse("the integer literal has no value");
        }
        if (isKind(x, "LiteralRational")) {
            Double r = number(x, "value");
            return r != null ? literal("real", r) : refuse("the rational literal has no value");
        }
        if (isKind(x, "LiteralBoolean")) return literal("boolean", flag(x, "value"));
        if (isKind(x, "LiteralString")) {
            String t = text(x, "value");
            return literal("string", t == null ? "" : t);
        }
        if (isKind(x, "LiteralInfinity")) return literal("unbounded", true);
        // A sign on a number literal.
        boolean negative = "-".equals(operator(x));
        Object a = operands(x).get(0);
        if (isKind(a, "LiteralInteger")) {
            Long i = integer(a, "value");
            return i != null ? literal("integer", negative ? -i : i) : refuse("the integer literal has no value");
        }
        Double r = number(a, "value");
        return r != null ? literal("real", negative ? -r : r) : refuse("the rational literal has no value");
    }

    /** The parts of {@code <number> [<unit reference>]} (a sign allowed): the number and the
     *  reference, or null. */
    private Object[] numberWithUnit(Object x) throws ReflectiveOperationException {
        if (!findHasUnit(x)) return null;
        List<Object> parts = operands(unsigned(x));
        if (parts.size() != 2) return null;
        Object first = unsigned(parts.get(0));
        if (isNumberLiteral(first) && isKind(parts.get(1), "FeatureReferenceExpression"))
            return new Object[] {parts.get(0), parts.get(1)};
        return null;
    }

    Object findUnit(Object x) throws ReflectiveOperationException {
        if (!findHasUnit(x)) {
            return isKind(unsigned(x), "LiteralExpression") ? null : refuse("the value is not a literal: its unit needs evaluation");
        }
        Object[] parts = numberWithUnit(x);
        if (parts == null) return refuse("the unit is not a single unit reference on a number: it needs evaluation");
        return elem(parts[1], "referent");
    }

    // -- multiplicity and order ------------------------------------------------------------------

    /** {@code boundsRange()}: a multiplicity's own range, else the first range among what it
     *  specializes (the OCL closure, depth first from the multiplicity's generals, not past a range). */
    private Object boundsRange(Object m) throws ReflectiveOperationException {
        if (isKind(m, "MultiplicityRange")) return m;
        Set<Object> seen = identitySet();
        Deque<Object> stack = new ArrayDeque<>();
        pushReversed(stack, generals(m, null));
        while (!stack.isEmpty()) {
            Object t = stack.pop();
            if (!seen.add(t)) continue;
            if (isKind(t, "MultiplicityRange")) return t;
            pushReversed(stack, generals(t, null));
        }
        return null;
    }

    private Object ownRange(Object t) throws ReflectiveOperationException {
        Object m = elem(t, "multiplicity");
        return m == null ? null : boundsRange(m);
    }

    private Object lowerBoundExpression(Object t) throws ReflectiveOperationException {
        Object r = ownRange(t);
        if (r == null) return null;
        Object l = elem(r, "lowerBound");
        if (l != null) return l;
        Object u = elem(r, "upperBound");
        return u != null && isKind(u, "LiteralInfinity") ? null : u;
    }

    private boolean hasMultiplicity(Object t) throws ReflectiveOperationException {
        return elem(t, "multiplicity") != null;
    }

    Long findLowerBound(Object t) throws ReflectiveOperationException {
        if (!hasMultiplicity(t)) return null;
        Object e = lowerBoundExpression(t);
        if (e == null) return 0L;
        Map<String, Object> l = findLiteral(e);
        return l.get("integer") instanceof Long i ? i : refuse("the lower bound is not an integer");
    }

    Long findUpperBound(Object t) throws ReflectiveOperationException {
        if (!hasMultiplicity(t)) return null;
        Object r = ownRange(t);
        if (r == null) return UNBOUNDED;
        Object u = elem(r, "upperBound");
        if (u == null) return refuse("the multiplicity range has no upper bound");
        Map<String, Object> l = findLiteral(u);
        if (l.get("integer") instanceof Long i) return i;
        if (l.containsKey("unbounded")) return UNBOUNDED;
        return refuse("the upper bound is not an integer or *");
    }

    Object findLowerBoundExpr(Object t) throws ReflectiveOperationException {
        return lowerBoundExpression(t);
    }

    Object findUpperBoundExpr(Object t) throws ReflectiveOperationException {
        Object r = ownRange(t);
        return r == null ? null : elem(r, "upperBound");
    }

    boolean findHasLiteralLowerBound(Object t) throws ReflectiveOperationException {
        if (!hasMultiplicity(t)) return false;
        Object e = lowerBoundExpression(t);
        return e == null || findIsLiteral(e);
    }

    boolean findHasLiteralUpperBound(Object t) throws ReflectiveOperationException {
        if (!hasMultiplicity(t)) return false;
        Object e = findUpperBoundExpr(t);
        return e == null || findIsLiteral(e);
    }

    boolean findIsEffectivelyOrdered(Object e) throws ReflectiveOperationException {
        Set<Object> seen = identitySet();
        Deque<Object> stack = new ArrayDeque<>();
        stack.push(e);
        while (!stack.isEmpty()) {
            Object f = stack.pop();
            if (!seen.add(f)) continue;
            if (flag(f, "isOrdered")) return true;
            for (Object sb : elems(f, "ownedSubsetting")) {
                Object g = elem(sb, "subsettedFeature");
                if (g != null) stack.push(g);
            }
        }
        return false;
    }

    // -- behaviour -------------------------------------------------------------------------------

    /** {@code Type::directionOf(feature)}: whether it is not null. */
    private boolean hasDirection(Object t, Object f) throws ReflectiveOperationException {
        return engine.invoke(t, "directionOf", f) != null;
    }

    List<Object> findParameters(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object f : scopedFeatures(e, scope, vis, library)) if (hasDirection(e, f)) out.add(f);
        return out;
    }

    /** The scoped features whose direction relative to {@code e} is {@code one} or inout. */
    private List<Object> directed(Object e, Scope scope, Visibility vis, boolean library, String one) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object f : scopedFeatures(e, scope, vis, library)) {
            Object d = engine.invoke(e, "directionOf", f);
            if (d == null) continue;
            if (!engine.isEnumerator(d)) throw new PilotException("failed", "directionOf(): a direction was expected, not " + d);
            String name = engine.literalOf(d);
            if (name.equals(one) || name.equals("inout")) out.add(f);
        }
        return out;
    }

    List<Object> findInputs(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        return directed(e, scope, vis, library, "in");
    }

    List<Object> findOutputs(Object e, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        return directed(e, scope, vis, library, "out");
    }

    List<Object> findSubsettingFeaturesOf(Object e, Object feature, Scope scope, Visibility vis, boolean library) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object f : scopedFeatures(e, scope, vis, library)) if (specializes(f, feature)) out.add(f);
        return out;
    }

    Object findResultExpression(Object e) throws ReflectiveOperationException {
        List<Object> ms = kinds(elems(e, "featureMembership"), "ResultExpressionMembership");
        return ms.isEmpty() ? null : elem(ms.get(0), "ownedResultExpression");
    }

    private static final List<String> TYPE_OPERATORS = List.of("istype", "hastype", "@", "@@", "as", "meta");

    Object findTypeArgument(Object e) throws ReflectiveOperationException {
        String op = operator(e);
        if (op == null || !TYPE_OPERATORS.contains(op)) return null;
        List<Object> fs = elems(e, "ownedFeature");
        if (fs.size() < 2) return null;
        List<Object> typings = elems(fs.get(1), "ownedTyping");
        return typings.isEmpty() ? null : elem(typings.get(0), "type");
    }

    private Object featureTarget(Object f) throws ReflectiveOperationException {
        return elem(f, "featureTarget");
    }

    List<Object> findInitialStates(Object e) throws ReflectiveOperationException {
        Object entry = elem(e, "entryAction");
        // The successions to exactly one state.
        List<Object[]> successions = new ArrayList<>();
        for (Object su : kinds(elems(e, "ownedFeature"), "SuccessionAsUsage")) {
            List<Object> targets = elems(su, "targetFeature");
            if (targets.size() != 1) continue;
            Object target = featureTarget(targets.get(0));
            if (target != null && isKind(target, "StateUsage")) successions.add(new Object[] {su, target});
        }
        // 1. `first start then off;`
        List<Object> fromStart = new ArrayList<>();
        for (Object[] s : successions) {
            Object source = elem(s[0], "sourceFeature");
            if (source == null) continue;
            source = featureTarget(source);
            if (source == null) continue;
            if (flag(source, "isLibraryElement") && "start".equals(nameOf(source))) fromStart.add(s[1]);
        }
        if (!fromStart.isEmpty()) return fromStart;
        // 2. `entry; then off;`
        List<Object> fromEntry = new ArrayList<>();
        if (entry == null) return fromEntry;
        for (Object[] s : successions) {
            Object source = elem(s[0], "sourceFeature");
            if (source != null && featureTarget(source) == entry) fromEntry.add(s[1]);
        }
        return fromEntry;
    }

    List<Object> findAcceptedTypes(Object e) throws ReflectiveOperationException {
        if (isKind(e, "AcceptActionUsage")) {
            Object p = elem(e, "payloadParameter");
            return p == null ? new ArrayList<>() : elems(p, "type");
        }
        List<Object> out = new ArrayList<>();
        for (Object a : elems(e, "triggerAction")) out.addAll(findAcceptedTypes(a));
        return out;
    }

    boolean findIsAcceptedType(Object e, Object type) throws ReflectiveOperationException {
        for (Object a : findAcceptedTypes(e)) if (specializes(type, a)) return true;
        return false;
    }

    // -- structure and annotations ---------------------------------------------------------------

    List<Object> findPath(Object e) throws ReflectiveOperationException {
        List<Object> chain = elems(e, "chainingFeature");
        if (chain.isEmpty()) chain.add(e);
        return chain;
    }

    Object findVariation(Object e) throws ReflectiveOperationException {
        Object m = elem(e, "owningMembership");
        if (m == null || !isKind(m, "VariantMembership")) return null;
        Object ns = elem(e, "owningNamespace");
        if (ns == null) return null;
        return isKind(ns, "Type") ? ns : refuse("the variant's owning namespace is not a type");
    }

    List<Object> findAppliedMetadata(Object e) throws ReflectiveOperationException {
        List<Object> out = new ArrayList<>();
        for (Object a : elems(e, "ownedAnnotation")) {
            Object x = elem(a, "annotatingElement");
            if (x != null && isKind(x, "MetadataFeature")) out.add(x);
        }
        if (isKind(e, "Namespace")) {
            for (Object f : kinds(elems(e, "ownedMember"), "MetadataFeature"))
                if (containsSame(elems(f, "annotatedElement"), e)) out.add(f);
        }
        return out;
    }

    List<Object> findMemberMetadata(Object e) throws ReflectiveOperationException {
        return kinds(elems(e, "ownedMember"), "MetadataUsage");
    }

    List<String> findDocText(Object e) throws ReflectiveOperationException {
        List<String> out = new ArrayList<>();
        for (Object d : elems(e, "documentation")) {
            String body = text(d, "body");
            out.add(body == null ? "" : body);
        }
        return out;
    }
}
