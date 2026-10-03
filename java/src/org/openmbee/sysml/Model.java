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

    public Element resolve(String qualifiedName) {
        String handle = backend.resolve(qualifiedName);
        return handle == null ? null : element(handle);
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
