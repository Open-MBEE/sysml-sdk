// The convenience members' examples (convenience.json) through Java, on the SysML Toolkit and on the
// Pilot Implementation (in this process): every example's member is called on its element and its
// answer compared with the example's, as python/tests/convenience_check.py does. Answers must agree;
// what a backend refuses is counted (the Python test pins the counts; the bodies are the binding
// library's and the Pilot engine's). Each backend skips itself when it is not at hand (the binding
// library and the release library; a Pilot, Pilot.find); SYSML_REQUIRE_TOOLKIT=1 makes the SysML
// Toolkit's skip a failure, SYSML_REQUIRE_PILOT=1 the Pilot's (the Pilot is then downloaded when absent).
package org.openmbee.sysml.checks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmbee.sysml.Backend;
import org.openmbee.sysml.Element;
import org.openmbee.sysml.Json;
import org.openmbee.sysml.Literal;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotAnsweredException;
import org.openmbee.sysml.PilotBackend;
import org.openmbee.sysml.RefusedException;
import org.openmbee.sysml.Scope;
import org.openmbee.sysml.SdkException;
import org.openmbee.sysml.ToolkitBackend;
import org.openmbee.sysml.VisibilityFilter;
import org.openmbee.sysml.classes.ClassMap;
import org.openmbee.sysml.pilot.Pilot;

public final class ConvenienceChecks {
    private ConvenienceChecks() { }

    private static Map<String, Object> table;
    private static final Map<String, Map<String, Object>> RECORDS = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Path root = Checks.findRepoRoot();
        table = (Map<String, Object>) Json.parse(Files.readString(root.resolve("convenience.json")));
        for (Object m : (List<Object>) table.get("members")) {
            Map<String, Object> rec = (Map<String, Object>) m;
            RECORDS.putIfAbsent((String) rec.get("name"), rec);
        }
        Map<String, String> sources = new LinkedHashMap<>();
        for (var e : ((Map<String, Object>) table.get("models")).entrySet())
            sources.put(e.getKey() + ".sysml", (String) ((Map<String, Object>) e.getValue()).get("text"));
        List<String> mismatches = new ArrayList<>();

        Path libraryDir = root.getParent().resolve("sysml-toolkit/spec-refs/SysML-v2-Release/sysml.library");
        Path libPath = null;
        try {
            libPath = ToolkitBackend.Library.defaultPath();
        } catch (SdkException e) {
            skipOrFail("the binding library: " + e.getMessage());
        }
        if (libPath != null && !Files.isDirectory(libraryDir)) {
            skipOrFail("the release library at " + libraryDir);
        } else if (libPath != null) {
            ToolkitBackend backend = ToolkitBackend.open(new ToolkitBackend.Library(libPath), List.of(), sources, libraryDir.toString());
            mismatches.addAll(runOn("the SysML Toolkit", "toolkit", backend));
        }

        Pilot pilot = "1".equals(System.getenv("SYSML_REQUIRE_PILOT")) ? Pilot.locate() : Pilot.find();
        if (pilot == null) {
            System.out.println("convenience checks on the Pilot Implementation skipped: no Pilot at hand"
                + " (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)");
        } else {
            try (PilotBackend backend = PilotBackend.open(pilot, List.of(), sources, true)) {
                mismatches.addAll(runOn("the Pilot Implementation", "pilot", backend));
            }
        }
        if (!mismatches.isEmpty()) throw new AssertionError("FAILED:\n" + String.join("\n", mismatches));
    }

    /** Whether {@code got} holds the items of {@code want}, each matched once: in order, or in any order
     *  where the specification leaves the order open. */
    private interface Match {
        boolean test(Object want, Object got) throws Exception;
    }

    private static boolean sameItems(List<?> want, List<?> got, Match match, boolean unordered) throws Exception {
        if (want.size() != got.size()) return false;
        if (!unordered) {
            for (int i = 0; i < want.size(); i++) if (!match.test(want.get(i), got.get(i))) return false;
            return true;
        }
        List<Object> left = new ArrayList<>(got);
        for (Object w : want) {
            int i = 0;
            while (i < left.size() && !match.test(w, left.get(i))) i++;
            if (i == left.size()) return false;
            left.remove(i);
        }
        return true;
    }

    /** An example the table lists as deviating on this backend ({@code deviates}, by backend key): its
     *  mismatch is a known deviation; if it agrees after all, the listing is stale. */
    @SuppressWarnings("unchecked")
    private static String[] deviation(Map<String, Object> e, String key, String[] result) {
        Object listed = e.get("deviates") instanceof Map<?, ?> m ? m.get(key) : null;
        if (listed == null) return result;
        if (result[0].equals("mismatch")) return new String[] {"known deviation", listed + " (" + result[1] + ")"};
        if (result[0].equals("agrees")) return new String[] {"mismatch", "listed as deviating on " + key + " (" + listed + "), but it agrees"};
        return result;
    }

    @SuppressWarnings("unchecked")
    private static List<String> runOn(String name, String key, Backend backend) throws Exception {
        Model model = new Model(backend);
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String k : List.of("agrees", "refused by the backend", "known deviation", "mismatch")) counts.put(k, 0);
        List<String> mismatches = new ArrayList<>();
        for (Object o : (List<Object>) table.get("examples")) {
            Map<String, Object> e = (Map<String, Object>) o;
            String[] result;
            try {
                result = outcome(model, e);
            } catch (Exception x) {
                result = new String[] {"mismatch", x.getClass().getSimpleName() + ": " + x.getMessage()};
            }
            result = deviation(e, key, result);
            counts.merge(result[0], 1, Integer::sum);
            if (result[0].equals("mismatch"))
                mismatches.add(name + ": " + e.get("member") + " at " + e.get("at") + " " + e.getOrDefault("args", "") + ": " + result[1]);
        }
        System.out.println("convenience examples on " + name + " (Java): " + counts);
        mismatches.addAll(listChecks(name, model));
        mismatches.addAll(findChecks(name, model));
        return mismatches;
    }

    /** Model.find (whatever the visibility; through inheritance when asked; quoted names) and
     *  Model.resolve from a context (the specification's Namespace::resolve, which the SysML Toolkit
     *  refuses in places). */
    private static List<String> findChecks(String name, Model model) {
        List<String> problems = new ArrayList<>();
        java.util.function.Function<Element, String> qn = el -> el == null ? null : el.getQualifiedName();
        java.util.function.BiConsumer<String, Object[]> expect = (what, gw) -> {
            if (!java.util.Objects.equals(gw[0], gw[1])) problems.add(name + ": " + what + ": " + gw[0] + ", not " + gw[1]);
        };
        expect.accept("find Base::b", new Object[] {qn.apply(model.find("Scopes::Base::b")), "Scopes::Base::b"});
        expect.accept("find Base::c", new Object[] {qn.apply(model.find("Scopes::Base::c")), "Scopes::Base::c"});
        expect.accept("find Derived::b", new Object[] {qn.apply(model.find("Scopes::Derived::b")), null});
        Element plug = model.find("Typing::Plug::'~Plug'");
        expect.accept("find '~Plug'", new Object[] {plug == null ? null : plug.$metaclass(), "ConjugatedPortDefinition"});
        try {
            expect.accept("find Derived::b, inherited", new Object[] {qn.apply(model.find("Scopes::Derived::b", true)), "Scopes::Base::b"});
            expect.accept("find Derived::c, inherited", new Object[] {qn.apply(model.find("Scopes::Derived::c", true)), null});
        } catch (NotAnsweredException x) {
            // the backend does not answer inheritedMembership here
        }
        try {
            var base = (org.openmbee.sysml.classes.Namespace) model.find("Scopes::Base");
            var derived = (org.openmbee.sysml.classes.Namespace) model.find("Scopes::Derived");
            expect.accept("resolve b from Base", new Object[] {qn.apply(model.resolve("b", base)), "Scopes::Base::b"});
            expect.accept("resolve Base::b from Derived", new Object[] {qn.apply(model.resolve("Base::b", derived)), null});
        } catch (NotAnsweredException x) {
            // the SysML Toolkit refuses Namespace::resolve in places
        }
        System.out.println("find and resolve on " + name + " (Java): " + (problems.isEmpty() ? "as expected" : problems.size() + " problems"));
        return problems;
    }

    /**
     * Every member a list carries (element and string results), on the elements of the examples' models
     * it applies to, through the typed list of each of its contexts: the list's answer is the elements'
     * answers flattened (list results concatenated, absent single results dropped), or the list refuses
     * as an element does; on all of them, and on those of them that answer. The arguments are each
     * member's first example's.
     */
    @SuppressWarnings("unchecked")
    private static List<String> listChecks(String name, Model model) throws Exception {
        List<Element> elements = new ArrayList<>();
        for (Element el : model.all()) elements.add(el);
        Map<String, Map<String, Object>> firstExample = new LinkedHashMap<>();
        for (Object o : (List<Object>) table.get("examples")) {
            Map<String, Object> e = (Map<String, Object>) o;
            firstExample.putIfAbsent((String) e.get("member"), e);
        }
        Map<String, Integer> counts = new LinkedHashMap<>(Map.of("all compared", 0, "all refused", 0, "answering compared", 0));
        List<String> problems = new ArrayList<>();
        for (Object o : (List<Object>) table.get("members")) {
            Map<String, Object> rec = (Map<String, Object>) o;
            Map<String, Object> returns = (Map<String, Object>) rec.get("returns");
            boolean elements_ = returns.get("kind").equals("element");
            boolean strings = returns.get("type").equals("String") && Boolean.TRUE.equals(returns.get("many"));
            if (!elements_ && !strings) continue;
            String member = (String) rec.get("name");
            String context = (String) rec.get("on");
            java.lang.Class<?> iface = context.equals("Element") ? Element.class : ClassMap.TYPE_MAP.get(context);
            List<Element> applicable = new ArrayList<>();
            for (Element el : elements) if (iface.isInstance(el)) applicable.add(el);
            if (applicable.isEmpty()) continue;
            Map<String, Object> e = firstExample.getOrDefault(member, Map.of());
            Object[][] tv = arguments(model, rec, (Map<String, Object>) e.getOrDefault("args", Map.of()));
            List<Object> answers = new ArrayList<>();
            for (Element el : applicable) {
                try {
                    answers.add(publicMethod(el, member, (java.lang.Class<?>[]) tv[0]).invoke(el, tv[1]));
                } catch (InvocationTargetException x) {
                    if (!(x.getCause() instanceof NotAnsweredException n)) throw x;
                    answers.add(n);
                }
            }
            boolean single = !Boolean.TRUE.equals(returns.get("many"));
            counts.merge("all " + compareList(name, member, context, applicable, answers, single, tv, problems), 1, Integer::sum);
            List<Element> answering = new ArrayList<>();
            List<Object> answered = new ArrayList<>();
            for (int i = 0; i < applicable.size(); i++) {
                if (answers.get(i) instanceof NotAnsweredException) continue;
                answering.add(applicable.get(i));
                answered.add(answers.get(i));
            }
            if (!answering.isEmpty() && answering.size() < applicable.size()) {
                compareList(name, member, context, answering, answered, single, tv, problems);
                counts.merge("answering compared", 1, Integer::sum);
            }
        }
        System.out.println("convenience lists on " + name + " (Java): " + counts);
        return problems;
    }

    /** The list of the context's typed list over {@code xs} against the elements' answers. */
    @SuppressWarnings("unchecked")
    private static String compareList(String name, String member, String context, List<Element> xs, List<Object> answers,
                                      boolean single, Object[][] tv, List<String> problems) throws Exception {
        Object list = context.equals("Element")
            ? new org.openmbee.sysml.ElementList<>(xs)
            : java.lang.Class.forName("org.openmbee.sysml.classes." + context + "List").getConstructor(java.util.Collection.class).newInstance(xs);
        NotAnsweredException refusal = null;
        for (Object a : answers) if (a instanceof NotAnsweredException n) { refusal = n; break; }
        Object got;
        try {
            got = list.getClass().getMethod(member, (java.lang.Class<?>[]) tv[0]).invoke(list, tv[1]);
        } catch (InvocationTargetException x) {
            if (!(x.getCause() instanceof NotAnsweredException n)) throw x;
            if (refusal == null || n.getClass() != refusal.getClass())
                problems.add(name + ": " + member + ": the list refused (" + n.getMessage() + ")");
            return "refused";
        }
        if (refusal != null) {
            problems.add(name + ": " + member + ": an element refused (" + refusal.getMessage() + "), the list answered");
            return "compared";
        }
        List<Object> want = new ArrayList<>();
        for (Object a : answers) {
            if (single) { if (a != null) want.add(a); }
            else want.addAll((List<Object>) a);
        }
        if (!want.equals(got))
            problems.add(name + ": " + member + ": " + ((List<?>) got).size() + " items from the list, " + want.size() + " from the elements");
        return "compared";
    }

    private static void skipOrFail(String what) {
        if ("1".equals(System.getenv("SYSML_REQUIRE_TOOLKIT")))
            throw new AssertionError("SYSML_REQUIRE_TOOLKIT=1 but " + what + " is missing");
        System.out.println("convenience checks on the SysML Toolkit skipped: " + what + " is missing");
    }

    /** The element by qualified name, whatever its visibility ({@link Model#find}). */
    static Element find(Model model, String qn) {
        return model.find(qn);
    }

    private static Object property(Element el, String prop) throws Exception {
        String getter = "get" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
        try {
            return publicMethod(el, getter).invoke(el);
        } catch (InvocationTargetException x) {
            throw x.getCause() instanceof Exception c ? c : x;
        }
    }

    /** A public method of the element's interfaces (the element classes are not public). */
    private static Method publicMethod(Element el, String name, java.lang.Class<?>... types) throws NoSuchMethodException {
        java.lang.Class<?> iface = ClassMap.TYPE_MAP.getOrDefault(el.$metaclass(), Element.class);
        return iface.getMethod(name, types);
    }

    private static Element walk(Element el, List<Object> via) throws Exception {
        for (Object o : via) {
            String step = (String) o;
            String prop = step, kind = null;
            int b = step.indexOf('[');
            if (b >= 0) { prop = step.substring(0, b); kind = step.substring(b + 1, step.length() - 1); }
            Object value = property(el, prop);
            List<?> values = value instanceof List<?> l ? l : value == null ? List.of() : List.of(value);
            List<Element> kept = new ArrayList<>();
            java.lang.Class<?> k = kind == null ? null : ClassMap.TYPE_MAP.get(kind);
            for (Object v : values)
                if (v instanceof Element x && (k == null || k.isInstance(x))) kept.add(x);
            if (kept.size() != 1) throw new IllegalStateException(step + " gives " + kept.size() + " elements");
            el = kept.get(0);
        }
        return el;
    }

    @SuppressWarnings("unchecked")
    private static boolean matches(Model model, Object expected, Object actual) throws Exception {
        if (expected instanceof Map<?, ?> m && m.containsKey("@type")) {
            if (!(actual instanceof Element el) || !el.$metaclass().equals(m.get("@type"))) return false;
            for (var e : ((Map<String, Object>) m).entrySet())
                if (!e.getKey().equals("@type") && !matches(model, e.getValue(), property(el, e.getKey()))) return false;
            return true;
        }
        if (actual instanceof Element el) return expected instanceof String s && el.equals(find(model, s));
        if (actual instanceof Literal lit) {
            return switch (lit) {
                case Literal.Unbounded u -> "*".equals(expected);
                case Literal.IntegerValue(long v) -> expected instanceof Number x && x.doubleValue() == v;
                case Literal.RealValue(double v) -> expected instanceof Number x && x.doubleValue() == v;
                case Literal.BooleanValue(boolean v) -> Boolean.valueOf(v).equals(expected);
                case Literal.StringValue(String v) -> v.equals(expected);
            };
        }
        if (actual instanceof String) return actual.equals(expected);  // such as an operator: "*" is itself
        if (expected instanceof Boolean || actual instanceof Boolean) return expected != null && expected.equals(actual);
        if (expected instanceof Number x && actual instanceof Number y) return x.doubleValue() == y.doubleValue();
        return expected != null && expected.equals(actual);
    }

    /** The parameter types and the argument values of a member, from an example's arguments (by name)
     *  and the defaults. */
    @SuppressWarnings("unchecked")
    private static Object[][] arguments(Model model, Map<String, Object> rec, Map<String, Object> given) throws Exception {
        List<Object> params = (List<Object>) rec.get("params");
        java.lang.Class<?>[] types = new java.lang.Class<?>[params.size()];
        Object[] values = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            Map<String, Object> p = (Map<String, Object>) params.get(i);
            Map<String, Object> t = (Map<String, Object>) p.get("type");
            String name = (String) p.get("name");
            Object v = given.containsKey(name) ? given.get(name) : p.get("default");
            switch ((String) t.get("kind")) {
                case "enum" -> {
                    if (t.get("type").equals("Scope")) { types[i] = Scope.class; values[i] = Scope.valueOf(((String) v).toUpperCase()); }
                    else { types[i] = VisibilityFilter.class; values[i] = VisibilityFilter.valueOf(((String) v).toUpperCase()); }
                }
                case "element" -> {
                    types[i] = t.get("type").equals("Element") ? Element.class : ClassMap.TYPE_MAP.get((String) t.get("type"));
                    values[i] = find(model, (String) v);
                }
                default -> {
                    if (t.get("type").equals("Boolean")) { types[i] = boolean.class; values[i] = Boolean.TRUE.equals(v); }
                    else { types[i] = String.class; values[i] = v; }
                }
            }
        }
        return new Object[][] {types, values};
    }

    @SuppressWarnings("unchecked")
    private static String[] outcome(Model model, Map<String, Object> e) throws Exception {
        Map<String, Object> rec = RECORDS.get((String) e.get("member"));
        Element el = find(model, (String) e.get("at"));
        if (el == null) return new String[] {"mismatch", e.get("at") + " not found"};
        if (e.containsKey("via")) el = walk(el, (List<Object>) e.get("via"));
        Map<String, Object> given = (Map<String, Object>) e.getOrDefault("args", Map.of());
        Object[][] tv = arguments(model, rec, given);
        java.lang.Class<?>[] types = (java.lang.Class<?>[]) tv[0];
        Object[] values = tv[1];
        Object answer;
        try {
            answer = publicMethod(el, (String) e.get("member"), types).invoke(el, values);
        } catch (NoSuchMethodException x) {
            return new String[] {"mismatch", el.$metaclass() + " has no " + e.get("member")};
        } catch (InvocationTargetException x) {
            Throwable c = x.getCause();
            if (c instanceof RefusedException)
                return Boolean.TRUE.equals(e.get("refused")) ? new String[] {"agrees", "refused"} : new String[] {"mismatch", "refused: " + c.getMessage()};
            if (c instanceof NotAnsweredException) return new String[] {"refused by the backend", c.getMessage()};
            throw c instanceof Exception ex ? ex : x;
        }
        if (Boolean.TRUE.equals(e.get("refused"))) return new String[] {"mismatch", "answered " + answer + ", the contract refuses"};
        try {
            boolean unordered = Boolean.TRUE.equals(e.get("unordered"));
            if (answer instanceof Map<?, ?> got) {  // a name-keyed map: the expected [name, element] pairs
                List<?> want = e.get("expect") instanceof List<?> l ? l : null;
                List<Object> entries = new ArrayList<>(got.entrySet());
                Match pair = (w, kv) -> ((List<?>) w).get(0).equals(((Map.Entry<?, ?>) kv).getKey())
                    && matches(model, ((List<?>) w).get(1), ((Map.Entry<?, ?>) kv).getValue());
                boolean ok = want != null && sameItems(want, entries, pair, unordered);
                return ok ? new String[] {"agrees", ""} : new String[] {"mismatch", "expected " + want + ", got " + answer};
            }
            if (e.containsKey("includes")) {
                for (Object q : (List<Object>) e.get("includes")) {
                    boolean any = false;
                    for (Object a : (List<Object>) answer) any |= matches(model, q, a);
                    if (!any) return new String[] {"mismatch", "missing " + q};
                }
                for (Object q : (List<Object>) e.getOrDefault("excludes", List.of()))
                    for (Object a : (List<Object>) answer)
                        if (matches(model, q, a)) return new String[] {"mismatch", "unexpected " + q};
                return new String[] {"agrees", ""};
            }
            Object want = e.get("expect");
            if (want instanceof List<?> w) {
                boolean ok = answer instanceof List<?> a && sameItems(w, a, (x, y) -> matches(model, x, y), unordered);
                return ok ? new String[] {"agrees", ""} : new String[] {"mismatch", "expected " + w + ", got " + answer};
            }
            if (want == null) return answer == null ? new String[] {"agrees", ""} : new String[] {"mismatch", "expected none, got " + answer};
            return answer != null && matches(model, want, answer) ? new String[] {"agrees", ""} : new String[] {"mismatch", "expected " + want + ", got " + answer};
        } catch (NotAnsweredException x) {
            return new String[] {"refused by the backend", "the answer cannot be checked: " + x.getMessage()};
        }
    }
}
