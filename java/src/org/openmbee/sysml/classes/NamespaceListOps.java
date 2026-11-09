// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** The convenience members of Namespace over a list of Namespace elements (see org.openmbee.sysml.ElementList). */
public interface NamespaceListOps {
    /** The list's elements. */
    java.util.List<? extends org.openmbee.sysml.Element> $elements();

    /**
     * The features that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FeatureList findMemberFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_FEATURES, scope, visibility, library)); }
    /**
     * The features that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FeatureList findMemberFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberFeatures(scope, visibility, false); }
    /**
     * The features that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FeatureList findMemberFeatures(org.openmbee.sysml.Scope scope) { return findMemberFeatures(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findMemberFeatures() { return findMemberFeatures(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default FeatureList findMemberFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_FEATURE, name, scope, visibility, library)); }
    /**
     * The feature that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default FeatureList findMemberFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberFeature(name, scope, visibility, false); }
    /**
     * The feature that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findMemberFeature(String name, org.openmbee.sysml.Scope scope) { return findMemberFeature(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FeatureList findMemberFeature(String name) { return findMemberFeature(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UsageList findMemberUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_USAGES, scope, visibility, library)); }
    /**
     * The usages that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UsageList findMemberUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberUsages(scope, visibility, false); }
    /**
     * The usages that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UsageList findMemberUsages(org.openmbee.sysml.Scope scope) { return findMemberUsages(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findMemberUsages() { return findMemberUsages(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default UsageList findMemberUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_USAGE, name, scope, visibility, library)); }
    /**
     * The usage that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default UsageList findMemberUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberUsage(name, scope, visibility, false); }
    /**
     * The usage that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findMemberUsage(String name, org.openmbee.sysml.Scope scope) { return findMemberUsage(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UsageList findMemberUsage(String name) { return findMemberUsage(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ActionUsageList findMemberActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ACTIONS, scope, visibility, library)); }
    /**
     * The actions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ActionUsageList findMemberActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberActions(scope, visibility, false); }
    /**
     * The actions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ActionUsageList findMemberActions(org.openmbee.sysml.Scope scope) { return findMemberActions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findMemberActions() { return findMemberActions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ActionUsageList findMemberAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ACTION, name, scope, visibility, library)); }
    /**
     * The action that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ActionUsageList findMemberAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAction(name, scope, visibility, false); }
    /**
     * The action that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findMemberAction(String name, org.openmbee.sysml.Scope scope) { return findMemberAction(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionUsageList findMemberAction(String name) { return findMemberAction(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AllocationUsageList findMemberAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AllocationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ALLOCATIONS, scope, visibility, library)); }
    /**
     * The allocations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AllocationUsageList findMemberAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAllocations(scope, visibility, false); }
    /**
     * The allocations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AllocationUsageList findMemberAllocations(org.openmbee.sysml.Scope scope) { return findMemberAllocations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findMemberAllocations() { return findMemberAllocations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AllocationUsageList findMemberAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AllocationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ALLOCATION, name, scope, visibility, library)); }
    /**
     * The allocation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AllocationUsageList findMemberAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAllocation(name, scope, visibility, false); }
    /**
     * The allocation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findMemberAllocation(String name, org.openmbee.sysml.Scope scope) { return findMemberAllocation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationUsageList findMemberAllocation(String name) { return findMemberAllocation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AnalysisCaseUsageList findMemberAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AnalysisCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ANALYSIS_CASES, scope, visibility, library)); }
    /**
     * The analysisCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AnalysisCaseUsageList findMemberAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAnalysisCases(scope, visibility, false); }
    /**
     * The analysisCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AnalysisCaseUsageList findMemberAnalysisCases(org.openmbee.sysml.Scope scope) { return findMemberAnalysisCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findMemberAnalysisCases() { return findMemberAnalysisCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AnalysisCaseUsageList findMemberAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AnalysisCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ANALYSIS_CASE, name, scope, visibility, library)); }
    /**
     * The analysisCase that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AnalysisCaseUsageList findMemberAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAnalysisCase(name, scope, visibility, false); }
    /**
     * The analysisCase that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findMemberAnalysisCase(String name, org.openmbee.sysml.Scope scope) { return findMemberAnalysisCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseUsageList findMemberAnalysisCase(String name) { return findMemberAnalysisCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AttributeUsageList findMemberAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ATTRIBUTES, scope, visibility, library)); }
    /**
     * The attributes that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AttributeUsageList findMemberAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAttributes(scope, visibility, false); }
    /**
     * The attributes that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default AttributeUsageList findMemberAttributes(org.openmbee.sysml.Scope scope) { return findMemberAttributes(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findMemberAttributes() { return findMemberAttributes(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AttributeUsageList findMemberAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ATTRIBUTE, name, scope, visibility, library)); }
    /**
     * The attribute that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default AttributeUsageList findMemberAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberAttribute(name, scope, visibility, false); }
    /**
     * The attribute that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findMemberAttribute(String name, org.openmbee.sysml.Scope scope) { return findMemberAttribute(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeUsageList findMemberAttribute(String name) { return findMemberAttribute(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CalculationUsageList findMemberCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CALCULATIONS, scope, visibility, library)); }
    /**
     * The calculations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CalculationUsageList findMemberCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberCalculations(scope, visibility, false); }
    /**
     * The calculations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CalculationUsageList findMemberCalculations(org.openmbee.sysml.Scope scope) { return findMemberCalculations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findMemberCalculations() { return findMemberCalculations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default CalculationUsageList findMemberCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CALCULATION, name, scope, visibility, library)); }
    /**
     * The calculation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default CalculationUsageList findMemberCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberCalculation(name, scope, visibility, false); }
    /**
     * The calculation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findMemberCalculation(String name, org.openmbee.sysml.Scope scope) { return findMemberCalculation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationUsageList findMemberCalculation(String name) { return findMemberCalculation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CaseUsageList findMemberCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CASES, scope, visibility, library)); }
    /**
     * The cases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CaseUsageList findMemberCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberCases(scope, visibility, false); }
    /**
     * The cases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default CaseUsageList findMemberCases(org.openmbee.sysml.Scope scope) { return findMemberCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findMemberCases() { return findMemberCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default CaseUsageList findMemberCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CASE, name, scope, visibility, library)); }
    /**
     * The case that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default CaseUsageList findMemberCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberCase(name, scope, visibility, false); }
    /**
     * The case that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findMemberCase(String name, org.openmbee.sysml.Scope scope) { return findMemberCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseUsageList findMemberCase(String name) { return findMemberCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConcernUsageList findMemberConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConcernUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONCERNS, scope, visibility, library)); }
    /**
     * The concerns that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConcernUsageList findMemberConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConcerns(scope, visibility, false); }
    /**
     * The concerns that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConcernUsageList findMemberConcerns(org.openmbee.sysml.Scope scope) { return findMemberConcerns(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findMemberConcerns() { return findMemberConcerns(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConcernUsageList findMemberConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConcernUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONCERN, name, scope, visibility, library)); }
    /**
     * The concern that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConcernUsageList findMemberConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConcern(name, scope, visibility, false); }
    /**
     * The concern that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findMemberConcern(String name, org.openmbee.sysml.Scope scope) { return findMemberConcern(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernUsageList findMemberConcern(String name) { return findMemberConcern(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConnectorAsUsageList findMemberConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectorAsUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONNECTIONS, scope, visibility, library)); }
    /**
     * The connections that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConnectorAsUsageList findMemberConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConnections(scope, visibility, false); }
    /**
     * The connections that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConnectorAsUsageList findMemberConnections(org.openmbee.sysml.Scope scope) { return findMemberConnections(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findMemberConnections() { return findMemberConnections(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConnectorAsUsageList findMemberConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectorAsUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONNECTION, name, scope, visibility, library)); }
    /**
     * The connection that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConnectorAsUsageList findMemberConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConnection(name, scope, visibility, false); }
    /**
     * The connection that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findMemberConnection(String name, org.openmbee.sysml.Scope scope) { return findMemberConnection(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectorAsUsageList findMemberConnection(String name) { return findMemberConnection(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConstraintUsageList findMemberConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONSTRAINTS, scope, visibility, library)); }
    /**
     * The constraints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConstraintUsageList findMemberConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConstraints(scope, visibility, false); }
    /**
     * The constraints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ConstraintUsageList findMemberConstraints(org.openmbee.sysml.Scope scope) { return findMemberConstraints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findMemberConstraints() { return findMemberConstraints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConstraintUsageList findMemberConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_CONSTRAINT, name, scope, visibility, library)); }
    /**
     * The constraint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ConstraintUsageList findMemberConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberConstraint(name, scope, visibility, false); }
    /**
     * The constraint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findMemberConstraint(String name, org.openmbee.sysml.Scope scope) { return findMemberConstraint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintUsageList findMemberConstraint(String name) { return findMemberConstraint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default EnumerationUsageList findMemberEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new EnumerationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ENUMERATIONS, scope, visibility, library)); }
    /**
     * The enumerations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default EnumerationUsageList findMemberEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberEnumerations(scope, visibility, false); }
    /**
     * The enumerations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default EnumerationUsageList findMemberEnumerations(org.openmbee.sysml.Scope scope) { return findMemberEnumerations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findMemberEnumerations() { return findMemberEnumerations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default EnumerationUsageList findMemberEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new EnumerationUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ENUMERATION, name, scope, visibility, library)); }
    /**
     * The enumeration that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default EnumerationUsageList findMemberEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberEnumeration(name, scope, visibility, false); }
    /**
     * The enumeration that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findMemberEnumeration(String name, org.openmbee.sysml.Scope scope) { return findMemberEnumeration(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationUsageList findMemberEnumeration(String name) { return findMemberEnumeration(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FlowUsageList findMemberFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_FLOWS, scope, visibility, library)); }
    /**
     * The flows that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FlowUsageList findMemberFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberFlows(scope, visibility, false); }
    /**
     * The flows that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default FlowUsageList findMemberFlows(org.openmbee.sysml.Scope scope) { return findMemberFlows(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findMemberFlows() { return findMemberFlows(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default FlowUsageList findMemberFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_FLOW, name, scope, visibility, library)); }
    /**
     * The flow that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default FlowUsageList findMemberFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberFlow(name, scope, visibility, false); }
    /**
     * The flow that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findMemberFlow(String name, org.openmbee.sysml.Scope scope) { return findMemberFlow(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowUsageList findMemberFlow(String name) { return findMemberFlow(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default InterfaceUsageList findMemberInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new InterfaceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_INTERFACES, scope, visibility, library)); }
    /**
     * The interfaces that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default InterfaceUsageList findMemberInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberInterfaces(scope, visibility, false); }
    /**
     * The interfaces that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default InterfaceUsageList findMemberInterfaces(org.openmbee.sysml.Scope scope) { return findMemberInterfaces(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findMemberInterfaces() { return findMemberInterfaces(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default InterfaceUsageList findMemberInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new InterfaceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_INTERFACE, name, scope, visibility, library)); }
    /**
     * The interface that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default InterfaceUsageList findMemberInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberInterface(name, scope, visibility, false); }
    /**
     * The interface that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findMemberInterface(String name, org.openmbee.sysml.Scope scope) { return findMemberInterface(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceUsageList findMemberInterface(String name) { return findMemberInterface(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ItemUsageList findMemberItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ITEMS, scope, visibility, library)); }
    /**
     * The items that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ItemUsageList findMemberItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberItems(scope, visibility, false); }
    /**
     * The items that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ItemUsageList findMemberItems(org.openmbee.sysml.Scope scope) { return findMemberItems(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findMemberItems() { return findMemberItems(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ItemUsageList findMemberItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_ITEM, name, scope, visibility, library)); }
    /**
     * The item that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ItemUsageList findMemberItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberItem(name, scope, visibility, false); }
    /**
     * The item that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findMemberItem(String name, org.openmbee.sysml.Scope scope) { return findMemberItem(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemUsageList findMemberItem(String name) { return findMemberItem(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default OccurrenceUsageList findMemberOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_OCCURRENCES, scope, visibility, library)); }
    /**
     * The occurrences that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default OccurrenceUsageList findMemberOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberOccurrences(scope, visibility, false); }
    /**
     * The occurrences that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default OccurrenceUsageList findMemberOccurrences(org.openmbee.sysml.Scope scope) { return findMemberOccurrences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findMemberOccurrences() { return findMemberOccurrences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default OccurrenceUsageList findMemberOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_OCCURRENCE, name, scope, visibility, library)); }
    /**
     * The occurrence that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default OccurrenceUsageList findMemberOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberOccurrence(name, scope, visibility, false); }
    /**
     * The occurrence that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findMemberOccurrence(String name, org.openmbee.sysml.Scope scope) { return findMemberOccurrence(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceUsageList findMemberOccurrence(String name) { return findMemberOccurrence(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PartUsageList findMemberParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_PARTS, scope, visibility, library)); }
    /**
     * The parts that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PartUsageList findMemberParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberParts(scope, visibility, false); }
    /**
     * The parts that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PartUsageList findMemberParts(org.openmbee.sysml.Scope scope) { return findMemberParts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findMemberParts() { return findMemberParts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default PartUsageList findMemberPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_PART, name, scope, visibility, library)); }
    /**
     * The part that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default PartUsageList findMemberPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberPart(name, scope, visibility, false); }
    /**
     * The part that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findMemberPart(String name, org.openmbee.sysml.Scope scope) { return findMemberPart(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartUsageList findMemberPart(String name) { return findMemberPart(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PortUsageList findMemberPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PortUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_PORTS, scope, visibility, library)); }
    /**
     * The ports that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PortUsageList findMemberPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberPorts(scope, visibility, false); }
    /**
     * The ports that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default PortUsageList findMemberPorts(org.openmbee.sysml.Scope scope) { return findMemberPorts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findMemberPorts() { return findMemberPorts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default PortUsageList findMemberPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PortUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_PORT, name, scope, visibility, library)); }
    /**
     * The port that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default PortUsageList findMemberPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberPort(name, scope, visibility, false); }
    /**
     * The port that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findMemberPort(String name, org.openmbee.sysml.Scope scope) { return findMemberPort(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortUsageList findMemberPort(String name) { return findMemberPort(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ReferenceUsageList findMemberReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ReferenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_REFERENCES, scope, visibility, library)); }
    /**
     * The references that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ReferenceUsageList findMemberReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberReferences(scope, visibility, false); }
    /**
     * The references that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ReferenceUsageList findMemberReferences(org.openmbee.sysml.Scope scope) { return findMemberReferences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findMemberReferences() { return findMemberReferences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ReferenceUsageList findMemberReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ReferenceUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_REFERENCE, name, scope, visibility, library)); }
    /**
     * The reference that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ReferenceUsageList findMemberReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberReference(name, scope, visibility, false); }
    /**
     * The reference that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findMemberReference(String name, org.openmbee.sysml.Scope scope) { return findMemberReference(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ReferenceUsageList findMemberReference(String name) { return findMemberReference(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RenderingUsageList findMemberRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RenderingUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_RENDERINGS, scope, visibility, library)); }
    /**
     * The renderings that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RenderingUsageList findMemberRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberRenderings(scope, visibility, false); }
    /**
     * The renderings that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RenderingUsageList findMemberRenderings(org.openmbee.sysml.Scope scope) { return findMemberRenderings(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findMemberRenderings() { return findMemberRenderings(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default RenderingUsageList findMemberRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RenderingUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_RENDERING, name, scope, visibility, library)); }
    /**
     * The rendering that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default RenderingUsageList findMemberRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberRendering(name, scope, visibility, false); }
    /**
     * The rendering that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findMemberRendering(String name, org.openmbee.sysml.Scope scope) { return findMemberRendering(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingUsageList findMemberRendering(String name) { return findMemberRendering(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RequirementUsageList findMemberRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_REQUIREMENTS, scope, visibility, library)); }
    /**
     * The requirements that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RequirementUsageList findMemberRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberRequirements(scope, visibility, false); }
    /**
     * The requirements that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default RequirementUsageList findMemberRequirements(org.openmbee.sysml.Scope scope) { return findMemberRequirements(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findMemberRequirements() { return findMemberRequirements(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default RequirementUsageList findMemberRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_REQUIREMENT, name, scope, visibility, library)); }
    /**
     * The requirement that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default RequirementUsageList findMemberRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberRequirement(name, scope, visibility, false); }
    /**
     * The requirement that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findMemberRequirement(String name, org.openmbee.sysml.Scope scope) { return findMemberRequirement(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementUsageList findMemberRequirement(String name) { return findMemberRequirement(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default StateUsageList findMemberStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_STATES, scope, visibility, library)); }
    /**
     * The states that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default StateUsageList findMemberStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberStates(scope, visibility, false); }
    /**
     * The states that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default StateUsageList findMemberStates(org.openmbee.sysml.Scope scope) { return findMemberStates(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findMemberStates() { return findMemberStates(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default StateUsageList findMemberState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_STATE, name, scope, visibility, library)); }
    /**
     * The state that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default StateUsageList findMemberState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberState(name, scope, visibility, false); }
    /**
     * The state that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findMemberState(String name, org.openmbee.sysml.Scope scope) { return findMemberState(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateUsageList findMemberState(String name) { return findMemberState(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default TransitionUsageList findMemberTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new TransitionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_TRANSITIONS, scope, visibility, library)); }
    /**
     * The transitions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default TransitionUsageList findMemberTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberTransitions(scope, visibility, false); }
    /**
     * The transitions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default TransitionUsageList findMemberTransitions(org.openmbee.sysml.Scope scope) { return findMemberTransitions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findMemberTransitions() { return findMemberTransitions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default TransitionUsageList findMemberTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new TransitionUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_TRANSITION, name, scope, visibility, library)); }
    /**
     * The transition that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default TransitionUsageList findMemberTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberTransition(name, scope, visibility, false); }
    /**
     * The transition that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findMemberTransition(String name, org.openmbee.sysml.Scope scope) { return findMemberTransition(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TransitionUsageList findMemberTransition(String name) { return findMemberTransition(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UseCaseUsageList findMemberUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_USE_CASES, scope, visibility, library)); }
    /**
     * The useCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UseCaseUsageList findMemberUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberUseCases(scope, visibility, false); }
    /**
     * The useCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default UseCaseUsageList findMemberUseCases(org.openmbee.sysml.Scope scope) { return findMemberUseCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findMemberUseCases() { return findMemberUseCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default UseCaseUsageList findMemberUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_USE_CASE, name, scope, visibility, library)); }
    /**
     * The useCase that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default UseCaseUsageList findMemberUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberUseCase(name, scope, visibility, false); }
    /**
     * The useCase that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findMemberUseCase(String name, org.openmbee.sysml.Scope scope) { return findMemberUseCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseUsageList findMemberUseCase(String name) { return findMemberUseCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases that are members of this namespace through any membership (owned,
     * aliased, imported, inherited), each once. Over the list's elements, in order: list answers
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
    default VerificationCaseUsageList findMemberVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new VerificationCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VERIFICATION_CASES, scope, visibility, library)); }
    /**
     * The verificationCases that are members of this namespace through any membership (owned,
     * aliased, imported, inherited), each once. Over the list's elements, in order: list answers
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
    default VerificationCaseUsageList findMemberVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberVerificationCases(scope, visibility, false); }
    /**
     * The verificationCases that are members of this namespace through any membership (owned,
     * aliased, imported, inherited), each once. Over the list's elements, in order: list answers
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
    default VerificationCaseUsageList findMemberVerificationCases(org.openmbee.sysml.Scope scope) { return findMemberVerificationCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases that are members of this namespace through any membership (owned,
     * aliased, imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findMemberVerificationCases() { return findMemberVerificationCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase that is a member under this simple name (an alias's name included), or
     * none. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default VerificationCaseUsageList findMemberVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new VerificationCaseUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VERIFICATION_CASE, name, scope, visibility, library)); }
    /**
     * The verificationCase that is a member under this simple name (an alias's name included), or
     * none. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default VerificationCaseUsageList findMemberVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberVerificationCase(name, scope, visibility, false); }
    /**
     * The verificationCase that is a member under this simple name (an alias's name included), or
     * none. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findMemberVerificationCase(String name, org.openmbee.sysml.Scope scope) { return findMemberVerificationCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase that is a member under this simple name (an alias's name included), or
     * none. Over the list's elements, in order: list answers concatenated, absent single answers
     * dropped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseUsageList findMemberVerificationCase(String name) { return findMemberVerificationCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewUsageList findMemberViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VIEWS, scope, visibility, library)); }
    /**
     * The views that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewUsageList findMemberViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberViews(scope, visibility, false); }
    /**
     * The views that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewUsageList findMemberViews(org.openmbee.sysml.Scope scope) { return findMemberViews(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findMemberViews() { return findMemberViews(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ViewUsageList findMemberView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VIEW, name, scope, visibility, library)); }
    /**
     * The view that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ViewUsageList findMemberView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberView(name, scope, visibility, false); }
    /**
     * The view that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findMemberView(String name, org.openmbee.sysml.Scope scope) { return findMemberView(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view that is a member under this simple name (an alias's name included), or none. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewUsageList findMemberView(String name) { return findMemberView(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewpointUsageList findMemberViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewpointUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VIEWPOINTS, scope, visibility, library)); }
    /**
     * The viewpoints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewpointUsageList findMemberViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberViewpoints(scope, visibility, false); }
    /**
     * The viewpoints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
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
    default ViewpointUsageList findMemberViewpoints(org.openmbee.sysml.Scope scope) { return findMemberViewpoints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints that are members of this namespace through any membership (owned, aliased,
     * imported, inherited), each once. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope, visibility and library at their
     * defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findMemberViewpoints() { return findMemberViewpoints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ViewpointUsageList findMemberViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewpointUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_VIEWPOINT, name, scope, visibility, library)); }
    /**
     * The viewpoint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves library at its default, false.
     *
     * @param name The member name to look for, an alias's name included.
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
    default ViewpointUsageList findMemberViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findMemberViewpoint(name, scope, visibility, false); }
    /**
     * The viewpoint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The member name to look for, an alias's name included.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findMemberViewpoint(String name, org.openmbee.sysml.Scope scope) { return findMemberViewpoint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint that is a member under this simple name (an alias's name included), or none.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @param name The member name to look for, an alias's name included.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointUsageList findMemberViewpoint(String name) { return findMemberViewpoint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The definitions of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default DefinitionList findDefinitions(org.openmbee.sysml.Scope scope) { return new DefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DEFINITIONS, scope)); }
    /**
     * The definitions of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default DefinitionList findDefinitions() { return findDefinitions(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The definition with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default DefinitionList findDefinition(String name, org.openmbee.sysml.Scope scope) { return new DefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_DEFINITION, name, scope)); }
    /**
     * The definition with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default DefinitionList findDefinition(String name) { return findDefinition(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The actionDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDefs(org.openmbee.sysml.Scope scope) { return new ActionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTION_DEFS, scope)); }
    /**
     * The actionDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDefs() { return findActionDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The actionDefs whose metaclass is exactly ActionDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDefsExact(org.openmbee.sysml.Scope scope) { return new ActionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTION_DEFS_EXACT, scope)); }
    /**
     * The actionDefs whose metaclass is exactly ActionDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDefsExact() { return findActionDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Actions::Action; refused where the model's library has no
     * Actions::Action. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
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
    default TypeList findActionDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTION_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Actions::Action; refused where the model's library has no
     * Actions::Action. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findActionDefsSemantic() { return findActionDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The actionDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDef(String name, org.openmbee.sysml.Scope scope) { return new ActionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ACTION_DEF, name, scope)); }
    /**
     * The actionDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ActionDefinitionList findActionDef(String name) { return findActionDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The allocationDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationDefinitionList findAllocationDefs(org.openmbee.sysml.Scope scope) { return new AllocationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATION_DEFS, scope)); }
    /**
     * The allocationDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationDefinitionList findAllocationDefs() { return findAllocationDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Allocations::Allocation; refused where the model's library has no
     * Allocations::Allocation. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
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
    default TypeList findAllocationDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATION_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Allocations::Allocation; refused where the model's library has no
     * Allocations::Allocation. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findAllocationDefsSemantic() { return findAllocationDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The allocationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationDefinitionList findAllocationDef(String name, org.openmbee.sysml.Scope scope) { return new AllocationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ALLOCATION_DEF, name, scope)); }
    /**
     * The allocationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AllocationDefinitionList findAllocationDef(String name) { return findAllocationDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The analysisCaseDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseDefinitionList findAnalysisCaseDefs(org.openmbee.sysml.Scope scope) { return new AnalysisCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASE_DEFS, scope)); }
    /**
     * The analysisCaseDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseDefinitionList findAnalysisCaseDefs() { return findAnalysisCaseDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize AnalysisCases::AnalysisCase; refused where the model's library has
     * no AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
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
    default TypeList findAnalysisCaseDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize AnalysisCases::AnalysisCase; refused where the model's library has
     * no AnalysisCases::AnalysisCase. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findAnalysisCaseDefsSemantic() { return findAnalysisCaseDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The analysisCaseDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseDefinitionList findAnalysisCaseDef(String name, org.openmbee.sysml.Scope scope) { return new AnalysisCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ANALYSIS_CASE_DEF, name, scope)); }
    /**
     * The analysisCaseDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AnalysisCaseDefinitionList findAnalysisCaseDef(String name) { return findAnalysisCaseDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The attributeDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDefs(org.openmbee.sysml.Scope scope) { return new AttributeDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTE_DEFS, scope)); }
    /**
     * The attributeDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDefs() { return findAttributeDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The attributeDefs whose metaclass is exactly AttributeDefinition, its subkinds left out. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDefsExact(org.openmbee.sysml.Scope scope) { return new AttributeDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTE_DEFS_EXACT, scope)); }
    /**
     * The attributeDefs whose metaclass is exactly AttributeDefinition, its subkinds left out. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDefsExact() { return findAttributeDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Base::DataValue; refused where the model's library has no
     * Base::DataValue. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped.
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
    default TypeList findAttributeDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Base::DataValue; refused where the model's library has no
     * Base::DataValue. Over the list's elements, in order: list answers concatenated, absent single
     * answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findAttributeDefsSemantic() { return findAttributeDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The attributeDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDef(String name, org.openmbee.sysml.Scope scope) { return new AttributeDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ATTRIBUTE_DEF, name, scope)); }
    /**
     * The attributeDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default AttributeDefinitionList findAttributeDef(String name) { return findAttributeDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The calculationDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDefs(org.openmbee.sysml.Scope scope) { return new CalculationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATION_DEFS, scope)); }
    /**
     * The calculationDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDefs() { return findCalculationDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The calculationDefs whose metaclass is exactly CalculationDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDefsExact(org.openmbee.sysml.Scope scope) { return new CalculationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATION_DEFS_EXACT, scope)); }
    /**
     * The calculationDefs whose metaclass is exactly CalculationDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDefsExact() { return findCalculationDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Calculations::Calculation; refused where the model's library has no
     * Calculations::Calculation. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
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
    default TypeList findCalculationDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATION_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Calculations::Calculation; refused where the model's library has no
     * Calculations::Calculation. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findCalculationDefsSemantic() { return findCalculationDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The calculationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDef(String name, org.openmbee.sysml.Scope scope) { return new CalculationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CALCULATION_DEF, name, scope)); }
    /**
     * The calculationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CalculationDefinitionList findCalculationDef(String name) { return findCalculationDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The caseDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDefs(org.openmbee.sysml.Scope scope) { return new CaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASE_DEFS, scope)); }
    /**
     * The caseDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDefs() { return findCaseDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The caseDefs whose metaclass is exactly CaseDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDefsExact(org.openmbee.sysml.Scope scope) { return new CaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASE_DEFS_EXACT, scope)); }
    /**
     * The caseDefs whose metaclass is exactly CaseDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDefsExact() { return findCaseDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Cases::Case; refused where the model's library has no Cases::Case.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default TypeList findCaseDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Cases::Case; refused where the model's library has no Cases::Case.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findCaseDefsSemantic() { return findCaseDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The caseDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDef(String name, org.openmbee.sysml.Scope scope) { return new CaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CASE_DEF, name, scope)); }
    /**
     * The caseDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default CaseDefinitionList findCaseDef(String name) { return findCaseDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The concernDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernDefinitionList findConcernDefs(org.openmbee.sysml.Scope scope) { return new ConcernDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERN_DEFS, scope)); }
    /**
     * The concernDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernDefinitionList findConcernDefs() { return findConcernDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Requirements::ConcernCheck; refused where the model's library has
     * no Requirements::ConcernCheck. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
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
    default TypeList findConcernDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERN_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Requirements::ConcernCheck; refused where the model's library has
     * no Requirements::ConcernCheck. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findConcernDefsSemantic() { return findConcernDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The concernDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernDefinitionList findConcernDef(String name, org.openmbee.sysml.Scope scope) { return new ConcernDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONCERN_DEF, name, scope)); }
    /**
     * The concernDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConcernDefinitionList findConcernDef(String name) { return findConcernDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The connectionDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDefs(org.openmbee.sysml.Scope scope) { return new ConnectionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTION_DEFS, scope)); }
    /**
     * The connectionDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDefs() { return findConnectionDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The connectionDefs whose metaclass is exactly ConnectionDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDefsExact(org.openmbee.sysml.Scope scope) { return new ConnectionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTION_DEFS_EXACT, scope)); }
    /**
     * The connectionDefs whose metaclass is exactly ConnectionDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDefsExact() { return findConnectionDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Connections::Connection; refused where the model's library has no
     * Connections::Connection. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
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
    default TypeList findConnectionDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTION_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Connections::Connection; refused where the model's library has no
     * Connections::Connection. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findConnectionDefsSemantic() { return findConnectionDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The connectionDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDef(String name, org.openmbee.sysml.Scope scope) { return new ConnectionDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONNECTION_DEF, name, scope)); }
    /**
     * The connectionDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConnectionDefinitionList findConnectionDef(String name) { return findConnectionDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The constraintDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDefs(org.openmbee.sysml.Scope scope) { return new ConstraintDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINT_DEFS, scope)); }
    /**
     * The constraintDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDefs() { return findConstraintDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The constraintDefs whose metaclass is exactly ConstraintDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDefsExact(org.openmbee.sysml.Scope scope) { return new ConstraintDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINT_DEFS_EXACT, scope)); }
    /**
     * The constraintDefs whose metaclass is exactly ConstraintDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDefsExact() { return findConstraintDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Constraints::ConstraintCheck; refused where the model's library has
     * no Constraints::ConstraintCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
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
    default TypeList findConstraintDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINT_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Constraints::ConstraintCheck; refused where the model's library has
     * no Constraints::ConstraintCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findConstraintDefsSemantic() { return findConstraintDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The constraintDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDef(String name, org.openmbee.sysml.Scope scope) { return new ConstraintDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_CONSTRAINT_DEF, name, scope)); }
    /**
     * The constraintDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ConstraintDefinitionList findConstraintDef(String name) { return findConstraintDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The enumerationDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationDefinitionList findEnumerationDefs(org.openmbee.sysml.Scope scope) { return new EnumerationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ENUMERATION_DEFS, scope)); }
    /**
     * The enumerationDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationDefinitionList findEnumerationDefs() { return findEnumerationDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The enumerationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationDefinitionList findEnumerationDef(String name, org.openmbee.sysml.Scope scope) { return new EnumerationDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ENUMERATION_DEF, name, scope)); }
    /**
     * The enumerationDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default EnumerationDefinitionList findEnumerationDef(String name) { return findEnumerationDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The flowDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowDefinitionList findFlowDefs(org.openmbee.sysml.Scope scope) { return new FlowDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOW_DEFS, scope)); }
    /**
     * The flowDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowDefinitionList findFlowDefs() { return findFlowDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Flows::MessageAction; refused where the model's library has no
     * Flows::MessageAction. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findFlowDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOW_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Flows::MessageAction; refused where the model's library has no
     * Flows::MessageAction. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findFlowDefsSemantic() { return findFlowDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The flowDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowDefinitionList findFlowDef(String name, org.openmbee.sysml.Scope scope) { return new FlowDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_FLOW_DEF, name, scope)); }
    /**
     * The flowDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default FlowDefinitionList findFlowDef(String name) { return findFlowDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The interfaceDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceDefinitionList findInterfaceDefs(org.openmbee.sysml.Scope scope) { return new InterfaceDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACE_DEFS, scope)); }
    /**
     * The interfaceDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceDefinitionList findInterfaceDefs() { return findInterfaceDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Interfaces::Interface; refused where the model's library has no
     * Interfaces::Interface. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findInterfaceDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Interfaces::Interface; refused where the model's library has no
     * Interfaces::Interface. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findInterfaceDefsSemantic() { return findInterfaceDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The interfaceDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceDefinitionList findInterfaceDef(String name, org.openmbee.sysml.Scope scope) { return new InterfaceDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_INTERFACE_DEF, name, scope)); }
    /**
     * The interfaceDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default InterfaceDefinitionList findInterfaceDef(String name) { return findInterfaceDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The itemDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDefs(org.openmbee.sysml.Scope scope) { return new ItemDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEM_DEFS, scope)); }
    /**
     * The itemDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDefs() { return findItemDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The itemDefs whose metaclass is exactly ItemDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDefsExact(org.openmbee.sysml.Scope scope) { return new ItemDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEM_DEFS_EXACT, scope)); }
    /**
     * The itemDefs whose metaclass is exactly ItemDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDefsExact() { return findItemDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Items::Item; refused where the model's library has no Items::Item.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default TypeList findItemDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEM_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Items::Item; refused where the model's library has no Items::Item.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findItemDefsSemantic() { return findItemDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The itemDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDef(String name, org.openmbee.sysml.Scope scope) { return new ItemDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_ITEM_DEF, name, scope)); }
    /**
     * The itemDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ItemDefinitionList findItemDef(String name) { return findItemDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The metadataDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataDefinitionList findMetadataDefs(org.openmbee.sysml.Scope scope) { return new MetadataDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_METADATA_DEFS, scope)); }
    /**
     * The metadataDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataDefinitionList findMetadataDefs() { return findMetadataDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The metadataDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataDefinitionList findMetadataDef(String name, org.openmbee.sysml.Scope scope) { return new MetadataDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_METADATA_DEF, name, scope)); }
    /**
     * The metadataDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataDefinitionList findMetadataDef(String name) { return findMetadataDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The occurrenceDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDefs(org.openmbee.sysml.Scope scope) { return new OccurrenceDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCE_DEFS, scope)); }
    /**
     * The occurrenceDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDefs() { return findOccurrenceDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The occurrenceDefs whose metaclass is exactly OccurrenceDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDefsExact(org.openmbee.sysml.Scope scope) { return new OccurrenceDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCE_DEFS_EXACT, scope)); }
    /**
     * The occurrenceDefs whose metaclass is exactly OccurrenceDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDefsExact() { return findOccurrenceDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Occurrences::Occurrence; refused where the model's library has no
     * Occurrences::Occurrence. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped.
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
    default TypeList findOccurrenceDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Occurrences::Occurrence; refused where the model's library has no
     * Occurrences::Occurrence. Over the list's elements, in order: list answers concatenated,
     * absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findOccurrenceDefsSemantic() { return findOccurrenceDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The occurrenceDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDef(String name, org.openmbee.sysml.Scope scope) { return new OccurrenceDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_OCCURRENCE_DEF, name, scope)); }
    /**
     * The occurrenceDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default OccurrenceDefinitionList findOccurrenceDef(String name) { return findOccurrenceDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The partDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDefs(org.openmbee.sysml.Scope scope) { return new PartDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PART_DEFS, scope)); }
    /**
     * The partDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDefs() { return findPartDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The partDefs whose metaclass is exactly PartDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDefsExact(org.openmbee.sysml.Scope scope) { return new PartDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PART_DEFS_EXACT, scope)); }
    /**
     * The partDefs whose metaclass is exactly PartDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDefsExact() { return findPartDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Parts::Part; refused where the model's library has no Parts::Part.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default TypeList findPartDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PART_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Parts::Part; refused where the model's library has no Parts::Part.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findPartDefsSemantic() { return findPartDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The partDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDef(String name, org.openmbee.sysml.Scope scope) { return new PartDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PART_DEF, name, scope)); }
    /**
     * The partDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PartDefinitionList findPartDef(String name) { return findPartDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The portDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDefs(org.openmbee.sysml.Scope scope) { return new PortDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORT_DEFS, scope)); }
    /**
     * The portDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDefs() { return findPortDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The portDefs whose metaclass is exactly PortDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDefsExact(org.openmbee.sysml.Scope scope) { return new PortDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORT_DEFS_EXACT, scope)); }
    /**
     * The portDefs whose metaclass is exactly PortDefinition, its subkinds left out. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDefsExact() { return findPortDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Ports::Port; refused where the model's library has no Ports::Port.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default TypeList findPortDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORT_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Ports::Port; refused where the model's library has no Ports::Port.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findPortDefsSemantic() { return findPortDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The portDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDef(String name, org.openmbee.sysml.Scope scope) { return new PortDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PORT_DEF, name, scope)); }
    /**
     * The portDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PortDefinitionList findPortDef(String name) { return findPortDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The renderingDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingDefinitionList findRenderingDefs(org.openmbee.sysml.Scope scope) { return new RenderingDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERING_DEFS, scope)); }
    /**
     * The renderingDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingDefinitionList findRenderingDefs() { return findRenderingDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Views::Rendering; refused where the model's library has no
     * Views::Rendering. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findRenderingDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERING_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Views::Rendering; refused where the model's library has no
     * Views::Rendering. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findRenderingDefsSemantic() { return findRenderingDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The renderingDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingDefinitionList findRenderingDef(String name, org.openmbee.sysml.Scope scope) { return new RenderingDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_RENDERING_DEF, name, scope)); }
    /**
     * The renderingDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RenderingDefinitionList findRenderingDef(String name) { return findRenderingDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The requirementDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDefs(org.openmbee.sysml.Scope scope) { return new RequirementDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENT_DEFS, scope)); }
    /**
     * The requirementDefs of this namespace: owned, imported (and inherited) only, or both. Over
     * the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDefs() { return findRequirementDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The requirementDefs whose metaclass is exactly RequirementDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDefsExact(org.openmbee.sysml.Scope scope) { return new RequirementDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENT_DEFS_EXACT, scope)); }
    /**
     * The requirementDefs whose metaclass is exactly RequirementDefinition, its subkinds left out.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDefsExact() { return findRequirementDefsExact(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Requirements::RequirementCheck; refused where the model's library
     * has no Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
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
    default TypeList findRequirementDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENT_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Requirements::RequirementCheck; refused where the model's library
     * has no Requirements::RequirementCheck. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findRequirementDefsSemantic() { return findRequirementDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The requirementDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDef(String name, org.openmbee.sysml.Scope scope) { return new RequirementDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_REQUIREMENT_DEF, name, scope)); }
    /**
     * The requirementDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default RequirementDefinitionList findRequirementDef(String name) { return findRequirementDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The stateDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateDefinitionList findStateDefs(org.openmbee.sysml.Scope scope) { return new StateDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATE_DEFS, scope)); }
    /**
     * The stateDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateDefinitionList findStateDefs() { return findStateDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize States::StateAction; refused where the model's library has no
     * States::StateAction. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findStateDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize States::StateAction; refused where the model's library has no
     * States::StateAction. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findStateDefsSemantic() { return findStateDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The stateDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateDefinitionList findStateDef(String name, org.openmbee.sysml.Scope scope) { return new StateDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_STATE_DEF, name, scope)); }
    /**
     * The stateDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default StateDefinitionList findStateDef(String name) { return findStateDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The useCaseDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseDefinitionList findUseCaseDefs(org.openmbee.sysml.Scope scope) { return new UseCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASE_DEFS, scope)); }
    /**
     * The useCaseDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseDefinitionList findUseCaseDefs() { return findUseCaseDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize UseCases::UseCase; refused where the model's library has no
     * UseCases::UseCase. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findUseCaseDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize UseCases::UseCase; refused where the model's library has no
     * UseCases::UseCase. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findUseCaseDefsSemantic() { return findUseCaseDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The useCaseDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseDefinitionList findUseCaseDef(String name, org.openmbee.sysml.Scope scope) { return new UseCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_USE_CASE_DEF, name, scope)); }
    /**
     * The useCaseDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default UseCaseDefinitionList findUseCaseDef(String name) { return findUseCaseDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The verificationCaseDefs of this namespace: owned, imported (and inherited) only, or both.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseDefinitionList findVerificationCaseDefs(org.openmbee.sysml.Scope scope) { return new VerificationCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASE_DEFS, scope)); }
    /**
     * The verificationCaseDefs of this namespace: owned, imported (and inherited) only, or both.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseDefinitionList findVerificationCaseDefs() { return findVerificationCaseDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize VerificationCases::VerificationCase; refused where the model's
     * library has no VerificationCases::VerificationCase. Over the list's elements, in order: list
     * answers concatenated, absent single answers dropped.
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
    default TypeList findVerificationCaseDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASE_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize VerificationCases::VerificationCase; refused where the model's
     * library has no VerificationCases::VerificationCase. Over the list's elements, in order: list
     * answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findVerificationCaseDefsSemantic() { return findVerificationCaseDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The verificationCaseDef with this simple (effective) name, or none. Over the list's elements,
     * in order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseDefinitionList findVerificationCaseDef(String name, org.openmbee.sysml.Scope scope) { return new VerificationCaseDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VERIFICATION_CASE_DEF, name, scope)); }
    /**
     * The verificationCaseDef with this simple (effective) name, or none. Over the list's elements,
     * in order: list answers concatenated, absent single answers dropped. Leaves scope at its
     * default, Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default VerificationCaseDefinitionList findVerificationCaseDef(String name) { return findVerificationCaseDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The viewDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewDefinitionList findViewDefs(org.openmbee.sysml.Scope scope) { return new ViewDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEW_DEFS, scope)); }
    /**
     * The viewDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewDefinitionList findViewDefs() { return findViewDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Views::View; refused where the model's library has no Views::View.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
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
    default TypeList findViewDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEW_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Views::View; refused where the model's library has no Views::View.
     * Over the list's elements, in order: list answers concatenated, absent single answers dropped.
     * Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findViewDefsSemantic() { return findViewDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The viewDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewDefinitionList findViewDef(String name, org.openmbee.sysml.Scope scope) { return new ViewDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEW_DEF, name, scope)); }
    /**
     * The viewDef with this simple (effective) name, or none. Over the list's elements, in order:
     * list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewDefinitionList findViewDef(String name) { return findViewDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The viewpointDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointDefinitionList findViewpointDefs(org.openmbee.sysml.Scope scope) { return new ViewpointDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINT_DEFS, scope)); }
    /**
     * The viewpointDefs of this namespace: owned, imported (and inherited) only, or both. Over the
     * list's elements, in order: list answers concatenated, absent single answers dropped. Leaves
     * scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointDefinitionList findViewpointDefs() { return findViewpointDefs(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The types that specialize Views::ViewpointCheck; refused where the model's library has no
     * Views::ViewpointCheck. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped.
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
    default TypeList findViewpointDefsSemantic(org.openmbee.sysml.Scope scope) { return new TypeList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINT_DEFS_SEMANTIC, scope)); }
    /**
     * The types that specialize Views::ViewpointCheck; refused where the model's library has no
     * Views::ViewpointCheck. Over the list's elements, in order: list answers concatenated, absent
     * single answers dropped. Leaves scope at its default, Scope.OWNED.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default TypeList findViewpointDefsSemantic() { return findViewpointDefsSemantic(org.openmbee.sysml.Scope.OWNED); }
    /**
     * The viewpointDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointDefinitionList findViewpointDef(String name, org.openmbee.sysml.Scope scope) { return new ViewpointDefinitionList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_VIEWPOINT_DEF, name, scope)); }
    /**
     * The viewpointDef with this simple (effective) name, or none. Over the list's elements, in
     * order: list answers concatenated, absent single answers dropped. Leaves scope at its default,
     * Scope.OWNED.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default ViewpointDefinitionList findViewpointDef(String name) { return findViewpointDef(name, org.openmbee.sysml.Scope.OWNED); }
    /**
     * The packages this namespace owns. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PackageList findPackages() { return new PackageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PACKAGES)); }
    /**
     * The owned package with this simple name, or none. Over the list's elements, in order: list
     * answers concatenated, absent single answers dropped.
     *
     * @param name The simple name to look for.
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default PackageList findPackage(String name) { return new PackageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_PACKAGE, name)); }
    /**
     * The metadata usages this namespace owns, whatever they annotate (metadata M about other;
     * included). Owned only: metadata is about the element that owns it, so it is never read
     * through inheritance or imports. Over the list's elements, in order: list answers
     * concatenated, absent single answers dropped.
     *
     * @return The elements, in order.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it. If the member refuses for one element,
     *     the call refuses.
     */
    default MetadataUsageList findMemberMetadata() { return new MetadataUsageList(org.openmbee.sysml.Model.findAll($elements(), Finds.FIND_MEMBER_METADATA)); }
}
