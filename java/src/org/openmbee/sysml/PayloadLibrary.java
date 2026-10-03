package org.openmbee.sysml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A standard library read from a full-form interchange element array, such as the library JSON
 * published with the SDK. Index it once and pass it to any number of payload models: their
 * references into the library then resolve by the library's normative ids. */
public final class PayloadLibrary {
    final Map<String, Map<?, ?>> byId = new LinkedHashMap<>();
    final Map<String, String> byQname = new LinkedHashMap<>();

    public PayloadLibrary(List<?> elements) {
        PayloadBackend.index(elements, byId, byQname);
    }

    public static PayloadLibrary fromJson(String json) {
        return new PayloadLibrary((List<?>) Json.parse(json));
    }

    /** The number of library elements. */
    public int size() {
        return byId.size();
    }
}
