// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of TransitionUsage over a list of TransitionUsage elements (see org.openmbee.sysml.ElementList). */
public interface TransitionUsageListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * What a transition's triggers accept. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findAcceptedTypes() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACCEPTED_TYPES)); }
}
