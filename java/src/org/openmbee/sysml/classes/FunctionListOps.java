// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Function over a list of Function elements (see org.openmbee.sysml.ElementList). */
public interface FunctionListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

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
