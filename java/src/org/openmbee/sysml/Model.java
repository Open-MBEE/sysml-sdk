package org.openmbee.sysml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmbee.sysml.classes.ClassMap;

/** Entry point: wraps a backend and mints typed wrappers by metaclass. */
public final class Model {
    private final Backend backend;
    private final Map<String, Element> cache = new HashMap<>();

    public Model(Backend backend) {
        this.backend = backend;
    }

    public static Model fromFullJson(String json) {
        return new Model(PayloadBackend.fromJson(json));
    }

    /** A model read from a full-form interchange element array. With {@code library}, references
     * into the standard library resolve against it; its elements are not the model's. */
    public static Model fromFullJson(String json, PayloadLibrary library) {
        return new Model(PayloadBackend.fromJson(json, library));
    }

    public Backend backend() {
        return backend;
    }

    public Element element(String handle) {
        Element w = cache.get(handle);
        if (w != null)
            return w;
        String mc = backend.metaclass(handle); // throws Gone if absent
        var make = ClassMap.FACTORIES.get(mc);
        w = make != null ? make.apply(this, handle) : new UnknownEl(this, handle);
        cache.put(handle, w);
        return w;
    }

    /**
     * Name resolution, with visibility (KerML 8.2.3.5), from the root: the backend's resolution. A
     * payload model resolves its elements' own qualified names; the SysML Toolkit's lookup does not
     * consider visibility yet. Only names the model gives count: an unnamed element is not found under
     * the name of what it references. To get an element by its qualified name whatever its visibility,
     * use {@link #find}.
     */
    public Element resolve(String qualifiedName) {
        String handle = backend.resolve(qualifiedName);
        return handle == null ? null : element(handle);
    }

    /** Name resolution, with visibility, from a namespace: the specification's
     *  {@code Namespace::resolve} on {@code context} (its member element), as the backend answers it. */
    public Element resolve(String name, org.openmbee.sysml.classes.Namespace context) {
        org.openmbee.sysml.classes.Membership m = context.resolve(name);
        return m == null ? null : m.getMemberElement();
    }

    /** {@link #find(String, boolean)} without inherited memberships. */
    public Element find(String qualifiedName) {
        return find(qualifiedName, false);
    }

    /**
     * The element a qualified name names, whatever its visibility, the same on every backend: the first
     * segment is a top-level name; each next one a member of the namespace before it, through one of its
     * owned memberships (an alias's name included) or, with {@code inherited}, also through its inherited
     * memberships (its supertypes' non-private ones). Null if a segment names nothing. Segments may be
     * quoted, with KerML's escapes ({@code '~Plug'}).
     */
    public Element find(String qualifiedName, boolean inherited) {
        String handle = backend.resolve(qualifiedName);
        if (handle != null) {
            Element el = element(handle);
            if (qualifiedName.equals(el.getQualifiedName())) return el;
        }
        List<String[]> segments = splitQualifiedName(qualifiedName);
        if (segments.isEmpty()) return null;
        handle = backend.resolve(segments.get(0)[0]);
        Element current = handle != null ? element(handle) : null;
        if (current == null) {
            for (Element root : roots()) {
                current = memberNamed(((org.openmbee.sysml.classes.Namespace) root).getOwnedMembership(), segments.get(0)[1]);
                if (current != null) break;
            }
        }
        for (String[] segment : segments.subList(1, segments.size())) {
            if (!(current instanceof org.openmbee.sysml.classes.Namespace ns)) return null;
            Element found = memberNamed(ns.getOwnedMembership(), segment[1]);
            if (found == null && inherited && current instanceof org.openmbee.sysml.classes.Type t)
                found = memberNamed(t.getInheritedMembership(), segment[1]);
            current = found;
        }
        return current;
    }

    private static Element memberNamed(List<org.openmbee.sysml.classes.Membership> memberships, String name) {
        for (org.openmbee.sysml.classes.Membership m : memberships)
            if (name.equals(m.getMemberName()) || name.equals(m.getMemberShortName())) return m.getMemberElement();
        return null;
    }

    private static final Map<Character, Character> ESCAPES = Map.of(
        '\'', '\'', '"', '"', 'b', '\b', 'f', '\f', 't', '\t', 'n', '\n', '\\', '\\');

    /**
     * A qualified name's segments as {written, name} pairs (KerML 8.2.3.4.1): separated by {@code ::};
     * an unrestricted name in single quotes, its escape sequences resolved (KerML 8.2.2.3); a leading
     * global scope qualifier {@code $} dropped.
     */
    public static List<String[]> splitQualifiedName(String qualifiedName) {
        String qn = qualifiedName;
        List<String[]> out = new ArrayList<>();
        int i = 0;
        while (i < qn.length()) {
            if (qn.charAt(i) == '\'') {
                int j = i + 1;
                StringBuilder name = new StringBuilder();
                while (j < qn.length() && qn.charAt(j) != '\'') {
                    if (qn.charAt(j) == '\\' && j + 1 < qn.length()) {
                        char c = qn.charAt(j + 1);
                        name.append(ESCAPES.getOrDefault(c, c));
                        j += 2;
                    } else {
                        name.append(qn.charAt(j++));
                    }
                }
                if (j >= qn.length()) throw new SdkException("an unterminated quoted name in " + qualifiedName);
                out.add(new String[] {qn.substring(i, j + 1), name.toString()});
                i = j + 1;
            } else {
                int j = qn.indexOf("::", i);
                if (j < 0) j = qn.length();
                out.add(new String[] {qn.substring(i, j), qn.substring(i, j)});
                i = j;
            }
            if (i < qn.length()) {
                if (!qn.startsWith("::", i)) throw new SdkException("a name in " + qualifiedName + " is not followed by '::'");
                i += 2;
            }
        }
        if (!out.isEmpty() && out.get(0)[1].equals("$")) out.remove(0);
        return out;
    }

    public List<Element> all() {
        List<Element> out = new ArrayList<>();
        for (String handle : backend.allHandles())
            out.add(element(handle));
        return out;
    }

    /** The top-level namespaces: the elements without an owner that are namespaces (a
     * membership may have no owner either). */
    public List<Element> roots() {
        List<Element> out = new ArrayList<>();
        for (Element el : all())
            if (el instanceof org.openmbee.sysml.classes.Namespace && el.getOwner() == null)
                out.add(el);
        return out;
    }

    public <T extends Element> List<T> elementsOfType(java.lang.Class<T> type) {
        List<T> out = new ArrayList<>();
        for (Element el : all())
            if (type.isInstance(el))
                out.add(type.cast(el));
        return out;
    }

    /**
     * Every generated member routes through here: the backend value,
     * wrapped. A read never second-guesses the backend. Mirrors P.__get__
     * in the Python runtime.
     */
    public Object read(Element el, String prop) {
        Object raw = backend.get(el.$handle(), prop);
        return wrap(raw, el.$metaclass() + "." + prop + " (element " + el.$handle() + ")");
    }

    /**
     * Every generated operation routes through here: the backend evaluates it (the SysML Toolkit
     * answers or refuses; a payload refuses), and the result is wrapped like a property value.
     */
    public Object call(Element el, String op, Object[] args) {
        Object raw = backend.call(el.$handle(), op, java.util.Arrays.asList(args));
        return wrap(raw, el.$metaclass() + "." + op + "() (element " + el.$handle() + ")");
    }

    /**
     * Every convenience member routes through here: the backend answers or refuses, and the result
     * is wrapped and converted to its Java shape (a literal, and the bounds' numbers, a {@link Literal}).
     */
    public Object findMember(Element el, Find member, Object... args) {
        return convert(el, member, backend.find(el.$handle(), member, java.util.Arrays.asList(args)));
    }

    /**
     * A convenience member over a list of elements (the members of {@link ElementList}): list results
     * concatenated in list order, single results with the absent ones dropped; one backend call per
     * model. If the member refuses for one element, the call refuses.
     */
    @SuppressWarnings("unchecked")
    public static <R> List<R> findAll(List<? extends Element> elements, Find member, Object... args) {
        List<Object> out = new ArrayList<>();
        if (elements.isEmpty()) return (List<R>) out;
        Model model = elements.get(0).$model();
        List<Object> given = java.util.Arrays.asList(args);
        List<Object> raws;
        if (elements.stream().allMatch(el -> el.$model() == model)) {
            List<String> handles = new ArrayList<>(elements.size());
            for (Element el : elements) handles.add(el.$handle());
            raws = model.backend.findEach(handles, member, given);
        } else {
            raws = new ArrayList<>(elements.size());
            for (Element el : elements) raws.add(el.$model().backend.find(el.$handle(), member, given));
        }
        for (int i = 0; i < elements.size(); i++) {
            Element el = elements.get(i);
            Object v = el.$model().convert(el, member, raws.get(i));
            if (member.result().equals("element")) {
                if (v != null) out.add(v);
            } else {
                out.addAll((List<Object>) v);
            }
        }
        return (List<R>) out;
    }

    /** A convenience member's answer from the backend's, in its Java shape. */
    Object convert(Element el, Find member, Object raw) {
        String at = el.$metaclass() + "." + member.name() + "() (element " + el.$handle() + ")";
        return switch (member.result()) {
            case "elements" -> raw == null ? List.of() : wrap(raw, at);
            case "element" -> raw == null ? null : wrap(raw, at);
            case "strings" -> raw == null ? List.of() : raw;
            case "unlimited" -> raw == null ? null
                    : "*".equals(raw) || raw instanceof Number n && n.longValue() == Long.MAX_VALUE ? new Literal.Unbounded()
                    : new Literal.IntegerValue(((Number) raw).longValue());
            case "literal" -> decodeLiteral(raw);
            case "real" -> raw instanceof Number n ? Double.valueOf(n.doubleValue()) : raw;
            case "int" -> raw == null ? null : new Literal.IntegerValue(((Number) raw).longValue());
            case "map" -> nameMap(raw, at);
            default -> raw;
        };
    }

    /** A name-keyed map from the backend's pairs of a name and an element, in their order. */
    private Map<String, Object> nameMap(Object raw, String at) {
        Map<String, Object> out = new java.util.LinkedHashMap<>();
        if (raw != null) {
            for (Object pair : (List<?>) raw) {
                List<?> p = (List<?>) pair;
                out.put(String.valueOf(p.get(0)), wrap(p.get(1), at));
            }
        }
        return java.util.Collections.unmodifiableMap(out);
    }

    /** A literal's value from its tagged JSON ({"integer": 4}, {"real": 0.5}, {"boolean": true},
     * {"string": "a"}, {"unbounded": true}). */
    public static Literal decodeLiteral(Object raw) {
        Object v = raw instanceof String s ? Json.parse(s) : raw;
        if (v instanceof Map<?, ?> m && m.size() == 1) {
            var e = m.entrySet().iterator().next();
            Object x = e.getValue();
            switch (String.valueOf(e.getKey())) {
                case "unbounded": return new Literal.Unbounded();
                case "integer": if (x instanceof Number n) return new Literal.IntegerValue(n.longValue()); break;
                case "real": if (x instanceof Number n) return new Literal.RealValue(n.doubleValue()); break;
                case "boolean": if (x instanceof Boolean b) return new Literal.BooleanValue(b); break;
                case "string": if (x instanceof String str) return new Literal.StringValue(str); break;
                default: break;
            }
        }
        throw new SdkException("a literal value was expected, not " + raw);
    }

    private static String idRef(Object v) {
        return v instanceof Map<?, ?> m && m.size() == 1
            && m.get("@id") instanceof String id ? id : null;
    }

    public Object wrap(Object raw, String at) {
        String id = idRef(raw);
        if (id != null) {
            try {
                return element(id);
            } catch (GoneException e) {
                throw new UnresolvedReferenceException(
                    "dangling reference @id='" + id + "' at " + at
                        + ": target is not in this model; the SDK requires "
                        + "resolved models");
            }
        }
        if (raw instanceof Map<?, ?> m && m.get("@ref") instanceof String spelling)
            throw new UnresolvedReferenceException(
                "unresolved reference '" + spelling + "' at " + at
                    + " (the SysML Toolkit's @ref passthrough is non-standard "
                    + "best-effort); the SDK requires resolved models — use "
                    + "$raw() for the raw value");
        if (raw instanceof List<?> l) {
            List<Object> out = new ArrayList<>(l.size());
            for (Object v : l)
                out.add(wrap(v, at));
            return out;
        }
        return raw;
    }
}

/** Fallback for metaclasses missing from the generated registry. */
final class UnknownEl extends El {
    UnknownEl(Model model, String handle) {
        super(model, handle);
    }
}
