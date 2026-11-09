// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Expression over a list of Expression elements (see org.openmbee.sysml.ElementList). */
public interface ExpressionListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The unit a number literal is written in (1500 [kg], -5 [kg]: the element the reference names,
     * SI::kilogram); none for a literal without a unit; refused for anything else, whose unit needs
     * evaluation (3 [m/s], 2 * x). On a feature: findValue().findUnit(). Over the list's elements,
     * in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default org.openmbee.sysml.ElementList<org.openmbee.sysml.Element> findUnit() { return new org.openmbee.sysml.ElementList<>(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_UNIT)); }
    /**
     * The result expression of a calculation or constraint, owned or inherited; the specification
     * has resultExpression only on analysis cases. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ExpressionList findResultExpression() { return new ExpressionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RESULT_EXPRESSION)); }
}
