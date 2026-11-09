// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Type over a list of Type elements (see org.openmbee.sysml.ElementList). */
public interface TypeListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The features of this type. Over the list's elements, in order: list answers concatenated,
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
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FEATURES, scope, visibility, library)); }
    /**
     * The features of this type. Over the list's elements, in order: list answers concatenated,
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
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFeatures(scope, visibility, false); }
    /**
     * The features of this type. Over the list's elements, in order: list answers concatenated,
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
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope) { return findFeatures(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFeatures() { return findFeatures(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default FeatureList findFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FEATURE, name, scope, visibility, library)); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default FeatureList findFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFeature(name, scope, visibility, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFeature(String name, org.openmbee.sysml.Scope scope) { return findFeature(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFeature(String name) { return findFeature(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages of this type. Over the list's elements, in order: list answers concatenated,
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
    default UsageList findUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USAGES, scope, visibility, library)); }
    /**
     * The usages of this type. Over the list's elements, in order: list answers concatenated,
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
    default UsageList findUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUsages(scope, visibility, false); }
    /**
     * The usages of this type. Over the list's elements, in order: list answers concatenated,
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
    default UsageList findUsages(org.openmbee.sysml.Scope scope) { return findUsages(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findUsages() { return findUsages(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default UsageList findUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USAGE, name, scope, visibility, library)); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default UsageList findUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUsage(name, scope, visibility, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findUsage(String name, org.openmbee.sysml.Scope scope) { return findUsage(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findUsage(String name) { return findUsage(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions of this type. Over the list's elements, in order: list answers concatenated,
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
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTIONS, scope, visibility, library)); }
    /**
     * The actions of this type. Over the list's elements, in order: list answers concatenated,
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
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActions(scope, visibility, false); }
    /**
     * The actions of this type. Over the list's elements, in order: list answers concatenated,
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
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope) { return findActions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findActions() { return findActions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTIONS_EXACT, scope, visibility, library)); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActionsExact(scope, visibility, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope) { return findActionsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findActionsExact() { return findActionsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTIONS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActionsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope) { return findActionsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findActionsSemantic() { return findActionsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ActionUsageList findAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTION, name, scope, visibility, library)); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ActionUsageList findAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAction(name, scope, visibility, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findAction(String name, org.openmbee.sysml.Scope scope) { return findAction(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findAction(String name) { return findAction(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations of this type. Over the list's elements, in order: list answers concatenated,
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
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AllocationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATIONS, scope, visibility, library)); }
    /**
     * The allocations of this type. Over the list's elements, in order: list answers concatenated,
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
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocations(scope, visibility, false); }
    /**
     * The allocations of this type. Over the list's elements, in order: list answers concatenated,
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
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope) { return findAllocations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findAllocations() { return findAllocations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATIONS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocationsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope) { return findAllocationsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAllocationsSemantic() { return findAllocationsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AllocationUsageList findAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AllocationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATION, name, scope, visibility, library)); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AllocationUsageList findAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocation(name, scope, visibility, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findAllocation(String name, org.openmbee.sysml.Scope scope) { return findAllocation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findAllocation(String name) { return findAllocation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
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
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AnalysisCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASES, scope, visibility, library)); }
    /**
     * The analysisCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves library at its default, false.
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
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCases(scope, visibility, false); }
    /**
     * The analysisCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves visibility and library at their defaults:
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
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope) { return findAnalysisCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findAnalysisCases() { return findAnalysisCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers concatenated,
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
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope) { return findAnalysisCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAnalysisCasesSemantic() { return findAnalysisCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AnalysisCaseUsageList findAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AnalysisCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASE, name, scope, visibility, library)); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AnalysisCaseUsageList findAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCase(name, scope, visibility, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and
     * false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findAnalysisCase(String name, org.openmbee.sysml.Scope scope) { return findAnalysisCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findAnalysisCase(String name) { return findAnalysisCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes of this type. Over the list's elements, in order: list answers concatenated,
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
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTES, scope, visibility, library)); }
    /**
     * The attributes of this type. Over the list's elements, in order: list answers concatenated,
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
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributes(scope, visibility, false); }
    /**
     * The attributes of this type. Over the list's elements, in order: list answers concatenated,
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
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope) { return findAttributes(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findAttributes() { return findAttributes(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTES_EXACT, scope, visibility, library)); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributesExact(scope, visibility, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope) { return findAttributesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findAttributesExact() { return findAttributesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope) { return findAttributesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findAttributesSemantic() { return findAttributesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AttributeUsageList findAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTE, name, scope, visibility, library)); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default AttributeUsageList findAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttribute(name, scope, visibility, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findAttribute(String name, org.openmbee.sysml.Scope scope) { return findAttribute(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findAttribute(String name) { return findAttribute(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations of this type. Over the list's elements, in order: list answers concatenated,
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
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATIONS, scope, visibility, library)); }
    /**
     * The calculations of this type. Over the list's elements, in order: list answers concatenated,
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
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculations(scope, visibility, false); }
    /**
     * The calculations of this type. Over the list's elements, in order: list answers concatenated,
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
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope) { return findCalculations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findCalculations() { return findCalculations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATIONS_EXACT, scope, visibility, library)); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculationsExact(scope, visibility, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope) { return findCalculationsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findCalculationsExact() { return findCalculationsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATIONS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculationsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope) { return findCalculationsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCalculationsSemantic() { return findCalculationsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default CalculationUsageList findCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATION, name, scope, visibility, library)); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default CalculationUsageList findCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculation(name, scope, visibility, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findCalculation(String name, org.openmbee.sysml.Scope scope) { return findCalculation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findCalculation(String name) { return findCalculation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASES, scope, visibility, library)); }
    /**
     * The cases of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCases(scope, visibility, false); }
    /**
     * The cases of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope) { return findCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCases() { return findCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASES_EXACT, scope, visibility, library)); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCasesExact(scope, visibility, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope) { return findCasesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCasesExact() { return findCasesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope) { return findCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findCasesSemantic() { return findCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default CaseUsageList findCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASE, name, scope, visibility, library)); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default CaseUsageList findCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCase(name, scope, visibility, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCase(String name, org.openmbee.sysml.Scope scope) { return findCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findCase(String name) { return findCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConcernUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERNS, scope, visibility, library)); }
    /**
     * The concerns of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcerns(scope, visibility, false); }
    /**
     * The concerns of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope) { return findConcerns(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findConcerns() { return findConcerns(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERNS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcernsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope) { return findConcernsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConcernsSemantic() { return findConcernsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConcernUsageList findConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConcernUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERN, name, scope, visibility, library)); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConcernUsageList findConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcern(name, scope, visibility, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findConcern(String name, org.openmbee.sysml.Scope scope) { return findConcern(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findConcern(String name) { return findConcern(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectorAsUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTIONS, scope, visibility, library)); }
    /**
     * The connections of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnections(scope, visibility, false); }
    /**
     * The connections of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope) { return findConnections(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findConnections() { return findConnections(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTIONS_EXACT, scope, visibility, library)); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnectionsExact(scope, visibility, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope) { return findConnectionsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionUsageList findConnectionsExact() { return findConnectionsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTIONS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnectionsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope) { return findConnectionsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConnectionsSemantic() { return findConnectionsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConnectorAsUsageList findConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectorAsUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTION, name, scope, visibility, library)); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConnectorAsUsageList findConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnection(name, scope, visibility, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findConnection(String name, org.openmbee.sysml.Scope scope) { return findConnection(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findConnection(String name) { return findConnection(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINTS, scope, visibility, library)); }
    /**
     * The constraints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraints(scope, visibility, false); }
    /**
     * The constraints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope) { return findConstraints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findConstraints() { return findConstraints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINTS_EXACT, scope, visibility, library)); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraintsExact(scope, visibility, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope) { return findConstraintsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findConstraintsExact() { return findConstraintsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Over the list's elements, in order: list answers concatenated,
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
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINTS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraintsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope) { return findConstraintsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findConstraintsSemantic() { return findConstraintsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConstraintUsageList findConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINT, name, scope, visibility, library)); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ConstraintUsageList findConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraint(name, scope, visibility, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findConstraint(String name, org.openmbee.sysml.Scope scope) { return findConstraint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findConstraint(String name) { return findConstraint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations of this type. Over the list's elements, in order: list answers concatenated,
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
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new EnumerationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ENUMERATIONS, scope, visibility, library)); }
    /**
     * The enumerations of this type. Over the list's elements, in order: list answers concatenated,
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
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findEnumerations(scope, visibility, false); }
    /**
     * The enumerations of this type. Over the list's elements, in order: list answers concatenated,
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
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope) { return findEnumerations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findEnumerations() { return findEnumerations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default EnumerationUsageList findEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new EnumerationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ENUMERATION, name, scope, visibility, library)); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default EnumerationUsageList findEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findEnumeration(name, scope, visibility, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findEnumeration(String name, org.openmbee.sysml.Scope scope) { return findEnumeration(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findEnumeration(String name) { return findEnumeration(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOWS, scope, visibility, library)); }
    /**
     * The flows of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlows(scope, visibility, false); }
    /**
     * The flows of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope) { return findFlows(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlows() { return findFlows(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOWS_EXACT, scope, visibility, library)); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlowsExact(scope, visibility, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope) { return findFlowsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlowsExact() { return findFlowsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOWS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlowsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope) { return findFlowsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findFlowsSemantic() { return findFlowsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default FlowUsageList findFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOW, name, scope, visibility, library)); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default FlowUsageList findFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlow(name, scope, visibility, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlow(String name, org.openmbee.sysml.Scope scope) { return findFlow(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findFlow(String name) { return findFlow(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces of this type. Over the list's elements, in order: list answers concatenated,
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
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new InterfaceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACES, scope, visibility, library)); }
    /**
     * The interfaces of this type. Over the list's elements, in order: list answers concatenated,
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
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterfaces(scope, visibility, false); }
    /**
     * The interfaces of this type. Over the list's elements, in order: list answers concatenated,
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
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope) { return findInterfaces(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findInterfaces() { return findInterfaces(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterfacesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope) { return findInterfacesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInterfacesSemantic() { return findInterfacesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default InterfaceUsageList findInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new InterfaceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACE, name, scope, visibility, library)); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default InterfaceUsageList findInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterface(name, scope, visibility, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findInterface(String name, org.openmbee.sysml.Scope scope) { return findInterface(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findInterface(String name) { return findInterface(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEMS, scope, visibility, library)); }
    /**
     * The items of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItems(scope, visibility, false); }
    /**
     * The items of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope) { return findItems(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItems() { return findItems(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEMS_EXACT, scope, visibility, library)); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItemsExact(scope, visibility, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope) { return findItemsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItemsExact() { return findItemsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEMS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItemsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope) { return findItemsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findItemsSemantic() { return findItemsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ItemUsageList findItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEM, name, scope, visibility, library)); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ItemUsageList findItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItem(name, scope, visibility, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItem(String name, org.openmbee.sysml.Scope scope) { return findItem(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findItem(String name) { return findItem(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences of this type. Over the list's elements, in order: list answers concatenated,
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
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCES, scope, visibility, library)); }
    /**
     * The occurrences of this type. Over the list's elements, in order: list answers concatenated,
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
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrences(scope, visibility, false); }
    /**
     * The occurrences of this type. Over the list's elements, in order: list answers concatenated,
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
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope) { return findOccurrences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findOccurrences() { return findOccurrences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCES_EXACT, scope, visibility, library)); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrencesExact(scope, visibility, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope) { return findOccurrencesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findOccurrencesExact() { return findOccurrencesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrencesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope) { return findOccurrencesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOccurrencesSemantic() { return findOccurrencesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default OccurrenceUsageList findOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCE, name, scope, visibility, library)); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default OccurrenceUsageList findOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrence(name, scope, visibility, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findOccurrence(String name, org.openmbee.sysml.Scope scope) { return findOccurrence(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findOccurrence(String name) { return findOccurrence(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default PartUsageList findParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PARTS, scope, visibility, library)); }
    /**
     * The parts of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default PartUsageList findParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findParts(scope, visibility, false); }
    /**
     * The parts of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findParts(org.openmbee.sysml.Scope scope) { return findParts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findParts() { return findParts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PARTS_EXACT, scope, visibility, library)); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPartsExact(scope, visibility, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope) { return findPartsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findPartsExact() { return findPartsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PARTS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPartsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope) { return findPartsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPartsSemantic() { return findPartsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default PartUsageList findPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PART, name, scope, visibility, library)); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default PartUsageList findPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPart(name, scope, visibility, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findPart(String name, org.openmbee.sysml.Scope scope) { return findPart(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findPart(String name) { return findPart(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PortUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORTS, scope, visibility, library)); }
    /**
     * The ports of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPorts(scope, visibility, false); }
    /**
     * The ports of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope) { return findPorts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findPorts() { return findPorts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORTS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPortsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope) { return findPortsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findPortsSemantic() { return findPortsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default PortUsageList findPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PortUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORT, name, scope, visibility, library)); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default PortUsageList findPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPort(name, scope, visibility, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findPort(String name, org.openmbee.sysml.Scope scope) { return findPort(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findPort(String name) { return findPort(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references of this type. Over the list's elements, in order: list answers concatenated,
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
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ReferenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REFERENCES, scope, visibility, library)); }
    /**
     * The references of this type. Over the list's elements, in order: list answers concatenated,
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
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findReferences(scope, visibility, false); }
    /**
     * The references of this type. Over the list's elements, in order: list answers concatenated,
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
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope) { return findReferences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findReferences() { return findReferences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ReferenceUsageList findReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ReferenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REFERENCE, name, scope, visibility, library)); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ReferenceUsageList findReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findReference(name, scope, visibility, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findReference(String name, org.openmbee.sysml.Scope scope) { return findReference(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findReference(String name) { return findReference(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings of this type. Over the list's elements, in order: list answers concatenated,
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
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RenderingUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERINGS, scope, visibility, library)); }
    /**
     * The renderings of this type. Over the list's elements, in order: list answers concatenated,
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
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRenderings(scope, visibility, false); }
    /**
     * The renderings of this type. Over the list's elements, in order: list answers concatenated,
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
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope) { return findRenderings(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findRenderings() { return findRenderings(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERINGS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRenderingsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope) { return findRenderingsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRenderingsSemantic() { return findRenderingsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default RenderingUsageList findRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RenderingUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERING, name, scope, visibility, library)); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default RenderingUsageList findRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRendering(name, scope, visibility, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findRendering(String name, org.openmbee.sysml.Scope scope) { return findRendering(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findRendering(String name) { return findRendering(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements of this type. Over the list's elements, in order: list answers concatenated,
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
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENTS, scope, visibility, library)); }
    /**
     * The requirements of this type. Over the list's elements, in order: list answers concatenated,
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
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirements(scope, visibility, false); }
    /**
     * The requirements of this type. Over the list's elements, in order: list answers concatenated,
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
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope) { return findRequirements(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findRequirements() { return findRequirements(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENTS_EXACT, scope, visibility, library)); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
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
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirementsExact(scope, visibility, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope) { return findRequirementsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findRequirementsExact() { return findRequirementsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENTS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirementsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope) { return findRequirementsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findRequirementsSemantic() { return findRequirementsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default RequirementUsageList findRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENT, name, scope, visibility, library)); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default RequirementUsageList findRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirement(name, scope, visibility, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findRequirement(String name, org.openmbee.sysml.Scope scope) { return findRequirement(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findRequirement(String name) { return findRequirement(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states of this type. Over the list's elements, in order: list answers concatenated,
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
    default StateUsageList findStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATES, scope, visibility, library)); }
    /**
     * The states of this type. Over the list's elements, in order: list answers concatenated,
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
    default StateUsageList findStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStates(scope, visibility, false); }
    /**
     * The states of this type. Over the list's elements, in order: list answers concatenated,
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
    default StateUsageList findStates(org.openmbee.sysml.Scope scope) { return findStates(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findStates() { return findStates(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATES_EXACT, scope, visibility, library)); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStatesExact(scope, visibility, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope) { return findStatesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findStatesExact() { return findStatesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStatesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope) { return findStatesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findStatesSemantic() { return findStatesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default StateUsageList findState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATE, name, scope, visibility, library)); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default StateUsageList findState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findState(name, scope, visibility, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findState(String name, org.openmbee.sysml.Scope scope) { return findState(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findState(String name) { return findState(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions of this type. Over the list's elements, in order: list answers concatenated,
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
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new TransitionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_TRANSITIONS, scope, visibility, library)); }
    /**
     * The transitions of this type. Over the list's elements, in order: list answers concatenated,
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
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findTransitions(scope, visibility, false); }
    /**
     * The transitions of this type. Over the list's elements, in order: list answers concatenated,
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
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope) { return findTransitions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findTransitions() { return findTransitions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default TransitionUsageList findTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new TransitionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_TRANSITION, name, scope, visibility, library)); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default TransitionUsageList findTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findTransition(name, scope, visibility, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findTransition(String name, org.openmbee.sysml.Scope scope) { return findTransition(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findTransition(String name) { return findTransition(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases of this type. Over the list's elements, in order: list answers concatenated,
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
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASES, scope, visibility, library)); }
    /**
     * The useCases of this type. Over the list's elements, in order: list answers concatenated,
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
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCases(scope, visibility, false); }
    /**
     * The useCases of this type. Over the list's elements, in order: list answers concatenated,
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
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope) { return findUseCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findUseCases() { return findUseCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
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
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASES_EXACT, scope, visibility, library)); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
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
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCasesExact(scope, visibility, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope) { return findUseCasesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findUseCasesExact() { return findUseCasesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope) { return findUseCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findUseCasesSemantic() { return findUseCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default UseCaseUsageList findUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASE, name, scope, visibility, library)); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default UseCaseUsageList findUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCase(name, scope, visibility, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findUseCase(String name, org.openmbee.sysml.Scope scope) { return findUseCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findUseCase(String name) { return findUseCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
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
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new VerificationCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASES, scope, visibility, library)); }
    /**
     * The verificationCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves library at its default, false.
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
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCases(scope, visibility, false); }
    /**
     * The verificationCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves visibility and library at their defaults:
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
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope) { return findVerificationCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases of this type. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findVerificationCases() { return findVerificationCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASES_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope) { return findVerificationCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findVerificationCasesSemantic() { return findVerificationCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default VerificationCaseUsageList findVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new VerificationCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASE, name, scope, visibility, library)); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default VerificationCaseUsageList findVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCase(name, scope, visibility, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and
     * false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findVerificationCase(String name, org.openmbee.sysml.Scope scope) { return findVerificationCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findVerificationCase(String name) { return findVerificationCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWS, scope, visibility, library)); }
    /**
     * The views of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves library at its default, false.
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
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViews(scope, visibility, false); }
    /**
     * The views of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope) { return findViews(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views of this type. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findViews() { return findViews(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope) { return findViewsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewsSemantic() { return findViewsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ViewUsageList findView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEW, name, scope, visibility, library)); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ViewUsageList findView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findView(name, scope, visibility, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findView(String name, org.openmbee.sysml.Scope scope) { return findView(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findView(String name) { return findView(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewpointUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINTS, scope, visibility, library)); }
    /**
     * The viewpoints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpoints(scope, visibility, false); }
    /**
     * The viewpoints of this type. Over the list's elements, in order: list answers concatenated,
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
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope) { return findViewpoints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints of this type. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findViewpoints() { return findViewpoints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINTS_SEMANTIC, scope, visibility, library)); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpointsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope) { return findViewpointsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findViewpointsSemantic() { return findViewpointsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ViewpointUsageList findViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewpointUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINT, name, scope, visibility, library)); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
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
    default ViewpointUsageList findViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpoint(name, scope, visibility, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findViewpoint(String name, org.openmbee.sysml.Scope scope) { return findViewpoint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findViewpoint(String name) { return findViewpoint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * Direct supertypes as written: supertypes(true). A conjugated type's only supertype is its
     * original type; a feature chain's last feature is a supertype of the chain. Over the list's
     * elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findDeclaredSupertypes() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DECLARED_SUPERTYPES)); }
    /**
     * Direct supertypes the backend adds by the language's rules (Parts::Part for a part
     * definition). Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findImpliedSupertypes() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_IMPLIED_SUPERTYPES)); }
    /**
     * Direct supertypes, written and implied: supertypes(false). Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findDeclaredAndImpliedSupertypes() { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DECLARED_AND_IMPLIED_SUPERTYPES)); }
    /**
     * The feature in effect here for feature: itself where inherited as it is, else the one
     * redefining it; none where this type does not have it. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param feature The feature as declared, on this type or on a type it specializes.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findEffectiveFeature(Feature feature) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_EFFECTIVE_FEATURE, feature)); }
    /**
     * The lower bound's expression; none where there is none (no multiplicity, the lower bound of
     * [*], a multiplicity without a range). Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ExpressionList findLowerBoundExpr() { return new ExpressionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_LOWER_BOUND_EXPR)); }
    /**
     * The upper bound's expression; none where there is none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ExpressionList findUpperBoundExpr() { return new ExpressionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_UPPER_BOUND_EXPR)); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
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
    default FeatureList findInputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INPUTS, scope, visibility, library)); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
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
    default FeatureList findInputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInputs(scope, visibility, false); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInputs(org.openmbee.sysml.Scope scope) { return findInputs(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findInputs() { return findInputs(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
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
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OUTPUTS, scope, visibility, library)); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves library at its default,
     * false.
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
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOutputs(scope, visibility, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope) { return findOutputs(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope, visibility and
     * library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findOutputs() { return findOutputs(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
     *
     * @param feature The feature the answers are or specialize.
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
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_SUBSETTING_FEATURES_OF, feature, scope, visibility, library)); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves library at its default, false.
     *
     * @param feature The feature the answers are or specialize.
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
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findSubsettingFeaturesOf(feature, scope, visibility, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and
     * false.
     *
     * @param feature The feature the answers are or specialize.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope) { return findSubsettingFeaturesOf(feature, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param feature The feature the answers are or specialize.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature) { return findSubsettingFeaturesOf(feature, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
}
