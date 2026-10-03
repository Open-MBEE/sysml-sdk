package org.openmbee.sysml;

/**
 * The backend contract: everything the generated surface needs.
 *
 * The protocol is defined over opaque per-backend element <em>handles</em>;
 * an id-keyed backend (payload, REST) simply uses element ids as its
 * handles, which is why this payload-backed runtime types them as
 * String. {@link #elementId} maps a handle to the element's stable id.
 */
public interface Backend {
    Object get(String handle, String prop);

    String metaclass(String handle);

    String elementId(String handle);

    /** Qualified name to handle, or null if absent. */
    String resolve(String qualifiedName);

    Iterable<String> allHandles();

    /**
     * A specification operation on the element {@code handle}, by its specification name, with
     * its arguments in order (elements as wrappers, collections as lists). A backend that cannot
     * evaluate the operation throws {@link NotImplementedInToolkitException}.
     */
    Object call(String handle, String op, java.util.List<Object> args);
}
