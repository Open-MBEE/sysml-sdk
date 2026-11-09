// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Step over a list of Step elements (see org.openmbee.sysml.ElementList). */
public interface StepListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The directed features, scoped like the kind lists: those whose direction relative to this
     * behaviour or step, directionOf, is not null. With scope all and library true, the
     * specification's parameter. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findParameters(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PARAMETERS, scope, visibility, library)); }
    /**
     * The directed features, scoped like the kind lists: those whose direction relative to this
     * behaviour or step, directionOf, is not null. With scope all and library true, the
     * specification's parameter. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findParameters(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findParameters(scope, visibility, false); }
    /**
     * The directed features, scoped like the kind lists: those whose direction relative to this
     * behaviour or step, directionOf, is not null. With scope all and library true, the
     * specification's parameter. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findParameters(org.openmbee.sysml.Scope scope) { return findParameters(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The directed features, scoped like the kind lists: those whose direction relative to this
     * behaviour or step, directionOf, is not null. With scope all and library true, the
     * specification's parameter. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findParameters() { return findParameters(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
}
