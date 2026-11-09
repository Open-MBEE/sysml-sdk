// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Element over a list of Element elements (see org.openmbee.sysml.ElementList). */
public interface ElementListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * Metadata applied to an element by an owned annotation, or as an owned member annotating it
     * (#M part p;, part p { &#64;M; }). Metadata placed elsewhere (metadata M about p;) is not
     * found. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataFeatureList findAppliedMetadata() { return new MetadataFeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_APPLIED_METADATA)); }
    /**
     * The bodies of the element's documentation comments, in order. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @return The texts, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default java.util.List<String> findDocText() { return org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DOC_TEXT); }
}
