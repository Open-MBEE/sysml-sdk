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

    /**
     * A convenience member on the element {@code handle}, with its arguments in order, defaults filled
     * in. A backend that cannot answer it throws {@link NotAnsweredException}; where its contract gives
     * no answer, {@link RefusedException}.
     */
    default Object find(String handle, Find member, java.util.List<Object> args) {
        throw new NotAnsweredException(member.name() + ": this backend does not answer the convenience members");
    }

    /**
     * {@link #find} on each element, a result per handle in order, for the members of an
     * {@link ElementList}. By default one {@code find} per element; a backend that can answer them
     * together (in one request) overrides it.
     */
    default java.util.List<Object> findEach(java.util.List<String> handles, Find member, java.util.List<Object> args) {
        java.util.List<Object> out = new java.util.ArrayList<>(handles.size());
        for (String h : handles) out.add(find(h, member, args));
        return out;
    }
}
