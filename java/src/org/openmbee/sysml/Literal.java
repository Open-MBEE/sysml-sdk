package org.openmbee.sysml;

/**
 * A literal's value, as {@code findLiteral()} answers it (and the bounds, {@code findLowerBound()} and
 * {@code findUpperBound()}): an integer, a real, a Boolean, a string, or the unbounded {@code *}. Switch
 * over its cases, or ask for the kind you expect.
 *
 * <pre>{@code
 * long count = expr.findLiteral().asLong();
 * long same = expr.findLiteral(Long.class);
 * String text = switch (expr.findLiteral()) {
 *     case Literal.IntegerValue(long n) -> "integer " + n;
 *     case Literal.RealValue(double x) -> "real " + x;
 *     case Literal.BooleanValue(boolean b) -> "Boolean " + b;
 *     case Literal.StringValue(String s) -> "string " + s;
 *     case Literal.Unbounded u -> "*";
 * };
 * }</pre>
 */
public sealed interface Literal {
    /** An integer. */
    record IntegerValue(long value) implements Literal {}
    /** A real (a rational or a real literal). */
    record RealValue(double value) implements Literal {}
    /** A Boolean. */
    record BooleanValue(boolean value) implements Literal {}
    /** A string. */
    record StringValue(String value) implements Literal {}
    /** The unbounded {@code *}. */
    record Unbounded() implements Literal {}

    /** Whether this is an integer. */
    default boolean isInteger() { return this instanceof IntegerValue; }
    /** Whether this is a real. */
    default boolean isReal() { return this instanceof RealValue; }
    /** Whether this is a Boolean. */
    default boolean isBoolean() { return this instanceof BooleanValue; }
    /** Whether this is a string. */
    default boolean isString() { return this instanceof StringValue; }
    /** Whether this is the unbounded {@code *}. */
    default boolean isUnbounded() { return this instanceof Unbounded; }

    /** The integer; an {@link IllegalStateException} for any other kind. */
    default long asLong() {
        if (this instanceof IntegerValue(long v)) return v;
        throw kindError("an integer");
    }

    /** The number, an integer widened; an {@link IllegalStateException} for any other kind. */
    default double asDouble() {
        return switch (this) {
            case RealValue(double v) -> v;
            case IntegerValue(long v) -> v;
            default -> throw kindError("a number");
        };
    }

    /** The Boolean; an {@link IllegalStateException} for any other kind. */
    default boolean asBoolean() {
        if (this instanceof BooleanValue(boolean v)) return v;
        throw kindError("a Boolean");
    }

    /** The string; an {@link IllegalStateException} for any other kind. */
    default String asString() {
        if (this instanceof StringValue(String v)) return v;
        throw kindError("a string");
    }

    /**
     * The value as {@code type}: {@code Long} (or {@code long}), {@code Integer} (or {@code int}, an
     * {@link ArithmeticException} where it does not fit), {@code Double} (or {@code double}, an integer
     * widened), {@code Boolean} (or {@code boolean}), {@code String}, or a case of this interface; an
     * {@link IllegalStateException} where the literal is of another kind.
     */
    @SuppressWarnings("unchecked")
    default <T> T as(Class<T> type) {
        Object v;
        if (type == Long.class || type == long.class) v = asLong();
        else if (type == Integer.class || type == int.class) v = Math.toIntExact(asLong());
        else if (type == Double.class || type == double.class) v = asDouble();
        else if (type == Boolean.class || type == boolean.class) v = asBoolean();
        else if (type == String.class) v = asString();
        else if (type.isInstance(this)) v = this;
        else throw kindError("a " + type.getSimpleName());
        return (T) v;
    }

    private IllegalStateException kindError(String expected) {
        return new IllegalStateException("the literal is " + this + ", not " + expected);
    }
}
