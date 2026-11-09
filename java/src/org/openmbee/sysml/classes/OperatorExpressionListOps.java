// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of OperatorExpression over a list of OperatorExpression elements (see org.openmbee.sysml.ElementList). */
public interface OperatorExpressionListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The type operand of a classification or cast (x istype T, x hastype T, x &#64; T, m
     * &#64;&#64; T, x as T, m meta T give T): the grammar puts it in the second owned feature. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findTypeArgument() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_TYPE_ARGUMENT)); }
}
