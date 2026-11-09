// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Feature over a list of Feature elements (see org.openmbee.sysml.ElementList). */
public interface FeatureListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The types this feature is declared with. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findDeclaredTypes() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DECLARED_TYPES)); }
    /**
     * The features this feature subsets as written (redefinitions and reference subsettings left
     * out). Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findSubsettedFeatures() { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_SUBSETTED_FEATURES)); }
    /**
     * The features this feature redefines as written. Over the list's elements, in order: list
     * answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRedefinedFeatures() { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REDEFINED_FEATURES)); }
    /**
     * The feature this feature references (::&gt;), or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findReferencedFeature() { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REFERENCED_FEATURE)); }
    /**
     * The feature a chain of written redefinitions starts from, following the first at each step
     * (Truck::mass to Frame::mass); itself when it redefines nothing. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRedefinitionRoot() { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REDEFINITION_ROOT)); }
    /**
     * The value expression this feature is declared with (=, :=, default), or none. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ExpressionList findValue() { return new ExpressionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VALUE)); }
    /**
     * A feature chain as its features (battery.powerOut gives [battery, powerOut]); else [self].
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPath() { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PATH)); }
}
