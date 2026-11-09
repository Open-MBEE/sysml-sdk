// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of StateUsage over a list of StateUsage elements (see org.openmbee.sysml.ElementList). */
public interface StateUsageListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * Initial states, by a priority chain over the notation's forms: first start then off; and,
     * only if that gives none, entry; then off; (the Annex A form, an entry action with a
     * transition from it, is not recognized). Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findInitialStates() { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INITIAL_STATES)); }
}
