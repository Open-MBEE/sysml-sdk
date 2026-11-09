package org.openmbee.sysml;

/** Which members a convenience list reads: the element's own, those it inherits (for a namespace's
 * members, imported ones too), or both. A list of features gives its own first; a list of members or
 * definitions, outside the owned scope, is in the backend's order. */
public enum Scope {
    OWNED("owned"), INHERITED("inherited"), ALL("all");

    private final String literal;

    Scope(String literal) {
        this.literal = literal;
    }

    /** The contract's literal ({@code "owned"}, {@code "inherited"}, {@code "all"}). */
    public String literal() {
        return literal;
    }
}
