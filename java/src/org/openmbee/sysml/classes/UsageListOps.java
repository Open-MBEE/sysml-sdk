// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Usage over a list of Usage elements (see org.openmbee.sysml.ElementList). */
public interface UsageListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The variation a variant belongs to (variation part def V { variant part a; }: a gives V; an
     * enumeration's values give the enumeration); none for a usage that is not a variant. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findVariation() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VARIATION)); }
}
