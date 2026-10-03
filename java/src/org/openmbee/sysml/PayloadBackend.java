package org.openmbee.sysml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Read-only backend over a full-form interchange element array.
 * Handles are the element ids themselves. With a library, an id or qualified name the payload does
 * not hold is looked up in the library; the library's elements are not the model's, so
 * {@link #allHandles()} lists the payload's only. */
public final class PayloadBackend implements Backend {
    private final Map<String, Map<?, ?>> byId = new LinkedHashMap<>();
    private final Map<String, String> byQname = new LinkedHashMap<>();
    private final PayloadLibrary library;

    public PayloadBackend(List<?> elements) {
        this(elements, null);
    }

    public PayloadBackend(List<?> elements, PayloadLibrary library) {
        index(elements, byId, byQname);
        this.library = library;
    }

    /** Elements by id (document order, the last of a repeated id) and ids by qualified name (the
     * first). */
    static void index(List<?> elements, Map<String, Map<?, ?>> byId, Map<String, String> byQname) {
        for (Object o : elements) {
            if (!(o instanceof Map<?, ?> e) || !(e.get("@id") instanceof String id))
                continue;
            byId.put(id, e);
            if (e.get("qualifiedName") instanceof String qn && !qn.isEmpty())
                byQname.putIfAbsent(qn, id);
        }
    }

    public static PayloadBackend fromJson(String json) {
        return new PayloadBackend((List<?>) Json.parse(json));
    }

    public static PayloadBackend fromJson(String json, PayloadLibrary library) {
        return new PayloadBackend((List<?>) Json.parse(json), library);
    }

    private Map<?, ?> require(String handle) {
        Map<?, ?> el = byId.get(handle);
        if (el == null && library != null)
            el = library.byId.get(handle);
        if (el == null)
            throw new GoneException("no element " + handle + " in this payload");
        return el;
    }

    @Override
    public Object get(String handle, String prop) {
        return require(handle).get(prop);
    }

    @Override
    public String metaclass(String handle) {
        return (String) require(handle).get("@type");
    }

    @Override
    public String elementId(String handle) {
        require(handle);
        return handle;
    }

    @Override
    public String resolve(String qualifiedName) {
        String handle = byQname.get(qualifiedName);
        if (handle == null && library != null)
            handle = library.byQname.get(qualifiedName);
        return handle;
    }

    @Override
    public Iterable<String> allHandles() {
        return byId.keySet();
    }

    /** A payload holds property values only; there is nothing to evaluate an operation with. */
    @Override
    public Object call(String handle, String op, List<Object> args) {
        throw new NotImplementedInToolkitException(op + "(): operations are not available on "
            + "payload models; load the model through the SysML Toolkit backend");
    }
}
