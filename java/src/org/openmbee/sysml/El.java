package org.openmbee.sysml;

/** Base of every generated element class. A wrapper holds (model, handle);
 * identity is by the backend's stable element id. */
public abstract class El implements Element {
    private final Model model;
    private final String handle;

    protected El(Model model, String handle) {
        this.model = model;
        this.handle = handle;
    }

    @Override
    public final String $id() {
        return model.backend().elementId(handle);
    }

    @Override
    public final String $metaclass() {
        return model.backend().metaclass(handle);
    }

    @Override
    public final Model $model() {
        return model;
    }

    @Override
    public final Object $raw(String prop) {
        return model.backend().get(handle, prop);
    }

    /** The backend handle (= element id for payload backends). */
    public final String $handle() {
        return handle;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof El other && other.model == model && other.$id().equals($id());
    }

    @Override
    public final int hashCode() {
        return $id().hashCode();
    }

    @Override
    public final String toString() {
        Object qn = model.backend().get(handle, "qualifiedName");
        String label = qn instanceof String s && !s.isEmpty() ? s : $id();
        return "<" + $metaclass() + " " + label + ">";
    }
}
