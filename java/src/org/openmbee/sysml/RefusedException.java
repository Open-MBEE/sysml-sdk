package org.openmbee.sysml;

/** A convenience member that gives no answer for this element by its contract ({@code invalid} in its
 * OCL): the literal of an expression that is not a literal, a bound that is an expression. Unlike the
 * other {@link NotAnsweredException}s, it says what the answer is, not that the backend cannot give it. */
public class RefusedException extends NotAnsweredException {
    public RefusedException(String message) {
        super(message);
    }
}
