package org.openmbee.sysml;

/** Which visibilities a convenience list keeps: every one, public and protected, or public only. */
public enum VisibilityFilter {
    ANY("any"), PROTECTED("protected"), PUBLIC("public");

    private final String literal;

    VisibilityFilter(String literal) {
        this.literal = literal;
    }

    /** The contract's literal ({@code "any"}, {@code "protected"}, {@code "public"}). */
    public String literal() {
        return literal;
    }
}
