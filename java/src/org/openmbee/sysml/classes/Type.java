// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — KerML 20250201. */
public interface Type extends Namespace {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Type> getDifferencingType() { return $list($model().read(this, "differencingType")); }
    default java.util.List<Feature> getDirectedFeature() { return $list($model().read(this, "directedFeature")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default java.util.List<Feature> getEndFeature() { return $list($model().read(this, "endFeature")); }
    default java.util.List<Feature> getFeature() { return $list($model().read(this, "feature")); }
    default java.util.List<FeatureMembership> getFeatureMembership() { return $list($model().read(this, "featureMembership")); }
    default java.util.List<Membership> getImportedMembership() { return $list($model().read(this, "importedMembership")); }
    default java.util.List<Feature> getInheritedFeature() { return $list($model().read(this, "inheritedFeature")); }
    default java.util.List<Membership> getInheritedMembership() { return $list($model().read(this, "inheritedMembership")); }
    default java.util.List<Feature> getInput() { return $list($model().read(this, "input")); }
    default java.util.List<Type> getIntersectingType() { return $list($model().read(this, "intersectingType")); }
    default Boolean getIsAbstract() { return $bool($model().read(this, "isAbstract")); }
    default Boolean getIsConjugated() { return $bool($model().read(this, "isConjugated")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default Boolean getIsSufficient() { return $bool($model().read(this, "isSufficient")); }
    default java.util.List<org.openmbee.sysml.Element> getMember() { return $list($model().read(this, "member")); }
    default java.util.List<Membership> getMembership() { return $list($model().read(this, "membership")); }
    default Multiplicity getMultiplicity() { return $ref($model().read(this, "multiplicity")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<Feature> getOutput() { return $list($model().read(this, "output")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default Conjugation getOwnedConjugator() { return $ref($model().read(this, "ownedConjugator")); }
    default java.util.List<Differencing> getOwnedDifferencing() { return $list($model().read(this, "ownedDifferencing")); }
    default java.util.List<Disjoining> getOwnedDisjoining() { return $list($model().read(this, "ownedDisjoining")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<Feature> getOwnedEndFeature() { return $list($model().read(this, "ownedEndFeature")); }
    default java.util.List<Feature> getOwnedFeature() { return $list($model().read(this, "ownedFeature")); }
    default java.util.List<FeatureMembership> getOwnedFeatureMembership() { return $list($model().read(this, "ownedFeatureMembership")); }
    default java.util.List<Import> getOwnedImport() { return $list($model().read(this, "ownedImport")); }
    default java.util.List<Intersecting> getOwnedIntersecting() { return $list($model().read(this, "ownedIntersecting")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedMember() { return $list($model().read(this, "ownedMember")); }
    default java.util.List<Membership> getOwnedMembership() { return $list($model().read(this, "ownedMembership")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default java.util.List<Specialization> getOwnedSpecialization() { return $list($model().read(this, "ownedSpecialization")); }
    default java.util.List<Unioning> getOwnedUnioning() { return $list($model().read(this, "ownedUnioning")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default java.util.List<Type> getUnioningType() { return $list($model().read(this, "unioningType")); }
    default java.util.List<Feature> allRedefinedFeaturesOf(Membership membership) { return $list($model().call(this, "allRedefinedFeaturesOf", new Object[] {membership})); }
    default java.util.List<Type> allSupertypes() { return $list($model().call(this, "allSupertypes", new Object[] {})); }
    default String directionOf(Feature feature) { return $string($model().call(this, "directionOf", new Object[] {feature})); }
    default String directionOfExcluding(Feature feature, java.util.List<Type> excluded) { return $string($model().call(this, "directionOfExcluding", new Object[] {feature, excluded})); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default java.util.List<Membership> importedMemberships(java.util.List<Namespace> excluded) { return $list($model().call(this, "importedMemberships", new Object[] {excluded})); }
    default java.util.List<Membership> inheritableMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "inheritableMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default java.util.List<Membership> inheritedMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "inheritedMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default Boolean isCompatibleWith(Type otherType) { return $bool($model().call(this, "isCompatibleWith", new Object[] {otherType})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default java.util.List<Membership> membershipsOfVisibility(String visibility, java.util.List<Namespace> excluded) { return $list($model().call(this, "membershipsOfVisibility", new Object[] {visibility, excluded})); }
    default java.util.List<Multiplicity> multiplicities() { return $list($model().call(this, "multiplicities", new Object[] {})); }
    default java.util.List<String> namesOf(org.openmbee.sysml.Element element) { return $list($model().call(this, "namesOf", new Object[] {element})); }
    default java.util.List<Membership> nonPrivateMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "nonPrivateMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
    default String qualificationOf(String qualifiedName) { return $string($model().call(this, "qualificationOf", new Object[] {qualifiedName})); }
    default java.util.List<Membership> removeRedefinedFeatures(java.util.List<Membership> memberships) { return $list($model().call(this, "removeRedefinedFeatures", new Object[] {memberships})); }
    default Membership resolve(String qualifiedName) { return $ref($model().call(this, "resolve", new Object[] {qualifiedName})); }
    default Membership resolveGlobal(String qualifiedName) { return $ref($model().call(this, "resolveGlobal", new Object[] {qualifiedName})); }
    default Membership resolveLocal(String name) { return $ref($model().call(this, "resolveLocal", new Object[] {name})); }
    default Membership resolveVisible(String name) { return $ref($model().call(this, "resolveVisible", new Object[] {name})); }
    default Boolean specializes(Type supertype) { return $bool($model().call(this, "specializes", new Object[] {supertype})); }
    default Boolean specializesFromLibrary(String libraryTypeName) { return $bool($model().call(this, "specializesFromLibrary", new Object[] {libraryTypeName})); }
    default java.util.List<Type> supertypes(Boolean excludeImplied) { return $list($model().call(this, "supertypes", new Object[] {excludeImplied})); }
    default String unqualifiedNameOf(String qualifiedName) { return $string($model().call(this, "unqualifiedNameOf", new Object[] {qualifiedName})); }
    default String visibilityOf(Membership mem) { return $string($model().call(this, "visibilityOf", new Object[] {mem})); }
    default java.util.List<Membership> visibleMemberships(java.util.List<Namespace> excluded, Boolean isRecursive, Boolean includeAll) { return $list($model().call(this, "visibleMemberships", new Object[] {excluded, isRecursive, includeAll})); }
    /**
     * The features of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FEATURES, scope, visibility, library))); }
    /**
     * The features of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFeatures(scope, visibility, false); }
    /**
     * The features of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFeatures(org.openmbee.sysml.Scope scope) { return findFeatures(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFeatures() { return findFeatures(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Feature findFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FEATURE, name, scope, visibility, library)); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Feature findFeature(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFeature(name, scope, visibility, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Feature findFeature(String name, org.openmbee.sysml.Scope scope) { return findFeature(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The feature with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Feature findFeature(String name) { return findFeature(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UsageList findUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USAGES, scope, visibility, library))); }
    /**
     * The usages of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UsageList findUsages(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUsages(scope, visibility, false); }
    /**
     * The usages of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UsageList findUsages(org.openmbee.sysml.Scope scope) { return findUsages(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usages of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UsageList findUsages() { return findUsages(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Usage findUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USAGE, name, scope, visibility, library)); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Usage findUsage(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUsage(name, scope, visibility, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Usage findUsage(String name, org.openmbee.sysml.Scope scope) { return findUsage(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The usage with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Usage findUsage(String name) { return findUsage(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ACTIONS, scope, visibility, library))); }
    /**
     * The actions of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActions(scope, visibility, false); }
    /**
     * The actions of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActions(org.openmbee.sysml.Scope scope) { return findActions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActions() { return findActions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ActionUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ACTIONS_EXACT, scope, visibility, library))); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Leaves library at
     * its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActionsExact(scope, visibility, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Leaves visibility
     * and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActionsExact(org.openmbee.sysml.Scope scope) { return findActionsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The actions whose metaclass is exactly ActionUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsageList findActionsExact() { return findActionsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ACTIONS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Leaves library at
     * its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findActionsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Leaves visibility
     * and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findActionsSemantic(org.openmbee.sysml.Scope scope) { return findActionsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Actions::Action, as the Pilot navigation API's
     * semanticActions; refused where the model's library has no Actions::Action. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findActionsSemantic() { return findActionsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsage findAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ACTION, name, scope, visibility, library)); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsage findAction(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAction(name, scope, visibility, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsage findAction(String name, org.openmbee.sysml.Scope scope) { return findAction(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The action with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ActionUsage findAction(String name) { return findAction(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AllocationUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ALLOCATIONS, scope, visibility, library))); }
    /**
     * The allocations of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocations(scope, visibility, false); }
    /**
     * The allocations of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsageList findAllocations(org.openmbee.sysml.Scope scope) { return findAllocations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocations of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsageList findAllocations() { return findAllocations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ALLOCATIONS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocationsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAllocationsSemantic(org.openmbee.sysml.Scope scope) { return findAllocationsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Allocations::Allocation, as the Pilot navigation API's
     * semanticAllocations; refused where the model's library has no Allocations::Allocation. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAllocationsSemantic() { return findAllocationsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsage findAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ALLOCATION, name, scope, visibility, library)); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsage findAllocation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAllocation(name, scope, visibility, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsage findAllocation(String name, org.openmbee.sysml.Scope scope) { return findAllocation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The allocation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AllocationUsage findAllocation(String name) { return findAllocation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AnalysisCaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ANALYSIS_CASES, scope, visibility, library))); }
    /**
     * The analysisCases of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCases(scope, visibility, false); }
    /**
     * The analysisCases of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsageList findAnalysisCases(org.openmbee.sysml.Scope scope) { return findAnalysisCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCases of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsageList findAnalysisCases() { return findAnalysisCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ANALYSIS_CASES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAnalysisCasesSemantic(org.openmbee.sysml.Scope scope) { return findAnalysisCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to AnalysisCases::AnalysisCase, as the Pilot navigation
     * API's semanticAnalysisCases; refused where the model's library has no
     * AnalysisCases::AnalysisCase. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAnalysisCasesSemantic() { return findAnalysisCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsage findAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ANALYSIS_CASE, name, scope, visibility, library)); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsage findAnalysisCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAnalysisCase(name, scope, visibility, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsage findAnalysisCase(String name, org.openmbee.sysml.Scope scope) { return findAnalysisCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The analysisCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found
     * as wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AnalysisCaseUsage findAnalysisCase(String name) { return findAnalysisCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ATTRIBUTES, scope, visibility, library))); }
    /**
     * The attributes of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributes(scope, visibility, false); }
    /**
     * The attributes of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributes(org.openmbee.sysml.Scope scope) { return findAttributes(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributes() { return findAttributes(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new AttributeUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ATTRIBUTES_EXACT, scope, visibility, library))); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributesExact(scope, visibility, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributesExact(org.openmbee.sysml.Scope scope) { return findAttributesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attributes whose metaclass is exactly AttributeUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsageList findAttributesExact() { return findAttributesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ATTRIBUTES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttributesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAttributesSemantic(org.openmbee.sysml.Scope scope) { return findAttributesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Base::DataValue, as the Pilot navigation API's
     * semanticAttributes; refused where the model's library has no Base::DataValue. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findAttributesSemantic() { return findAttributesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsage findAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ATTRIBUTE, name, scope, visibility, library)); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsage findAttribute(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findAttribute(name, scope, visibility, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsage findAttribute(String name, org.openmbee.sysml.Scope scope) { return findAttribute(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The attribute with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default AttributeUsage findAttribute(String name) { return findAttribute(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CALCULATIONS, scope, visibility, library))); }
    /**
     * The calculations of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculations(scope, visibility, false); }
    /**
     * The calculations of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculations(org.openmbee.sysml.Scope scope) { return findCalculations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculations() { return findCalculations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CalculationUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CALCULATIONS_EXACT, scope, visibility, library))); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculationsExact(scope, visibility, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculationsExact(org.openmbee.sysml.Scope scope) { return findCalculationsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculations whose metaclass is exactly CalculationUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsageList findCalculationsExact() { return findCalculationsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CALCULATIONS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculationsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCalculationsSemantic(org.openmbee.sysml.Scope scope) { return findCalculationsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Calculations::Calculation, as the Pilot navigation API's
     * semanticCalculations; refused where the model's library has no Calculations::Calculation.
     * Leaves scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and
     * false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCalculationsSemantic() { return findCalculationsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsage findCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CALCULATION, name, scope, visibility, library)); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsage findCalculation(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCalculation(name, scope, visibility, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsage findCalculation(String name, org.openmbee.sysml.Scope scope) { return findCalculation(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The calculation with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CalculationUsage findCalculation(String name) { return findCalculation(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CASES, scope, visibility, library))); }
    /**
     * The cases of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCases(scope, visibility, false); }
    /**
     * The cases of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCases(org.openmbee.sysml.Scope scope) { return findCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCases() { return findCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new CaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CASES_EXACT, scope, visibility, library))); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Leaves library at its
     * default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCasesExact(scope, visibility, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Leaves visibility and
     * library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCasesExact(org.openmbee.sysml.Scope scope) { return findCasesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The cases whose metaclass is exactly CaseUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsageList findCasesExact() { return findCasesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CASES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCasesSemantic(org.openmbee.sysml.Scope scope) { return findCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Cases::Case, as the Pilot navigation API's semanticCases;
     * refused where the model's library has no Cases::Case. Leaves scope, visibility and library at
     * their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findCasesSemantic() { return findCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsage findCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CASE, name, scope, visibility, library)); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsage findCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findCase(name, scope, visibility, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsage findCase(String name, org.openmbee.sysml.Scope scope) { return findCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The case with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default CaseUsage findCase(String name) { return findCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConcernUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONCERNS, scope, visibility, library))); }
    /**
     * The concerns of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcerns(scope, visibility, false); }
    /**
     * The concerns of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsageList findConcerns(org.openmbee.sysml.Scope scope) { return findConcerns(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concerns of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsageList findConcerns() { return findConcerns(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONCERNS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcernsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConcernsSemantic(org.openmbee.sysml.Scope scope) { return findConcernsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::ConcernCheck, as the Pilot navigation API's
     * semanticConcerns; refused where the model's library has no Requirements::ConcernCheck. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConcernsSemantic() { return findConcernsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsage findConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONCERN, name, scope, visibility, library)); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsage findConcern(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConcern(name, scope, visibility, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsage findConcern(String name, org.openmbee.sysml.Scope scope) { return findConcern(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The concern with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConcernUsage findConcern(String name) { return findConcern(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectorAsUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONNECTIONS, scope, visibility, library))); }
    /**
     * The connections of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnections(scope, visibility, false); }
    /**
     * The connections of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsageList findConnections(org.openmbee.sysml.Scope scope) { return findConnections(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsageList findConnections() { return findConnections(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConnectionUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONNECTIONS_EXACT, scope, visibility, library))); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnectionsExact(scope, visibility, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectionUsageList findConnectionsExact(org.openmbee.sysml.Scope scope) { return findConnectionsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connections whose metaclass is exactly ConnectionUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectionUsageList findConnectionsExact() { return findConnectionsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONNECTIONS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnectionsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConnectionsSemantic(org.openmbee.sysml.Scope scope) { return findConnectionsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Connections::Connection, as the Pilot navigation API's
     * semanticConnections; refused where the model's library has no Connections::Connection. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConnectionsSemantic() { return findConnectionsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsage findConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONNECTION, name, scope, visibility, library)); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsage findConnection(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConnection(name, scope, visibility, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsage findConnection(String name, org.openmbee.sysml.Scope scope) { return findConnection(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The connection with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConnectorAsUsage findConnection(String name) { return findConnection(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONSTRAINTS, scope, visibility, library))); }
    /**
     * The constraints of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraints(scope, visibility, false); }
    /**
     * The constraints of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraints(org.openmbee.sysml.Scope scope) { return findConstraints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraints() { return findConstraints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ConstraintUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONSTRAINTS_EXACT, scope, visibility, library))); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraintsExact(scope, visibility, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraintsExact(org.openmbee.sysml.Scope scope) { return findConstraintsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraints whose metaclass is exactly ConstraintUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsageList findConstraintsExact() { return findConstraintsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONSTRAINTS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraintsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConstraintsSemantic(org.openmbee.sysml.Scope scope) { return findConstraintsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Constraints::ConstraintCheck, as the Pilot navigation
     * API's semanticConstraints; refused where the model's library has no
     * Constraints::ConstraintCheck. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findConstraintsSemantic() { return findConstraintsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsage findConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_CONSTRAINT, name, scope, visibility, library)); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsage findConstraint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findConstraint(name, scope, visibility, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsage findConstraint(String name, org.openmbee.sysml.Scope scope) { return findConstraint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The constraint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ConstraintUsage findConstraint(String name) { return findConstraint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new EnumerationUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ENUMERATIONS, scope, visibility, library))); }
    /**
     * The enumerations of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findEnumerations(scope, visibility, false); }
    /**
     * The enumerations of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsageList findEnumerations(org.openmbee.sysml.Scope scope) { return findEnumerations(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumerations of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsageList findEnumerations() { return findEnumerations(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsage findEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ENUMERATION, name, scope, visibility, library)); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsage findEnumeration(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findEnumeration(name, scope, visibility, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsage findEnumeration(String name, org.openmbee.sysml.Scope scope) { return findEnumeration(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The enumeration with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default EnumerationUsage findEnumeration(String name) { return findEnumeration(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FLOWS, scope, visibility, library))); }
    /**
     * The flows of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlows(scope, visibility, false); }
    /**
     * The flows of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlows(org.openmbee.sysml.Scope scope) { return findFlows(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlows() { return findFlows(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FlowUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FLOWS_EXACT, scope, visibility, library))); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Leaves library at its
     * default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlowsExact(scope, visibility, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Leaves visibility and
     * library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlowsExact(org.openmbee.sysml.Scope scope) { return findFlowsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flows whose metaclass is exactly FlowUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsageList findFlowsExact() { return findFlowsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FLOWS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlowsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFlowsSemantic(org.openmbee.sysml.Scope scope) { return findFlowsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Flows::MessageAction, as the Pilot navigation API's
     * semanticFlows; refused where the model's library has no Flows::MessageAction. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findFlowsSemantic() { return findFlowsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsage findFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_FLOW, name, scope, visibility, library)); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsage findFlow(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findFlow(name, scope, visibility, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsage findFlow(String name, org.openmbee.sysml.Scope scope) { return findFlow(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The flow with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FlowUsage findFlow(String name) { return findFlow(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new InterfaceUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_INTERFACES, scope, visibility, library))); }
    /**
     * The interfaces of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterfaces(scope, visibility, false); }
    /**
     * The interfaces of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsageList findInterfaces(org.openmbee.sysml.Scope scope) { return findInterfaces(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interfaces of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsageList findInterfaces() { return findInterfaces(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_INTERFACES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterfacesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInterfacesSemantic(org.openmbee.sysml.Scope scope) { return findInterfacesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Interfaces::Interface, as the Pilot navigation API's
     * semanticInterfaces; refused where the model's library has no Interfaces::Interface. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInterfacesSemantic() { return findInterfacesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsage findInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_INTERFACE, name, scope, visibility, library)); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsage findInterface(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInterface(name, scope, visibility, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsage findInterface(String name, org.openmbee.sysml.Scope scope) { return findInterface(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The interface with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default InterfaceUsage findInterface(String name) { return findInterface(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ITEMS, scope, visibility, library))); }
    /**
     * The items of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItems(scope, visibility, false); }
    /**
     * The items of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItems(org.openmbee.sysml.Scope scope) { return findItems(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItems() { return findItems(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ItemUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ITEMS_EXACT, scope, visibility, library))); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Leaves library at its
     * default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItemsExact(scope, visibility, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Leaves visibility and
     * library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItemsExact(org.openmbee.sysml.Scope scope) { return findItemsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The items whose metaclass is exactly ItemUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsageList findItemsExact() { return findItemsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ITEMS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItemsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findItemsSemantic(org.openmbee.sysml.Scope scope) { return findItemsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Items::Item, as the Pilot navigation API's semanticItems;
     * refused where the model's library has no Items::Item. Leaves scope, visibility and library at
     * their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findItemsSemantic() { return findItemsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsage findItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_ITEM, name, scope, visibility, library)); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsage findItem(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findItem(name, scope, visibility, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsage findItem(String name, org.openmbee.sysml.Scope scope) { return findItem(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The item with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ItemUsage findItem(String name) { return findItem(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_OCCURRENCES, scope, visibility, library))); }
    /**
     * The occurrences of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrences(scope, visibility, false); }
    /**
     * The occurrences of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrences(org.openmbee.sysml.Scope scope) { return findOccurrences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrences() { return findOccurrences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new OccurrenceUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_OCCURRENCES_EXACT, scope, visibility, library))); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrencesExact(scope, visibility, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrencesExact(org.openmbee.sysml.Scope scope) { return findOccurrencesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrences whose metaclass is exactly OccurrenceUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsageList findOccurrencesExact() { return findOccurrencesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_OCCURRENCES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrencesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOccurrencesSemantic(org.openmbee.sysml.Scope scope) { return findOccurrencesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Occurrences::Occurrence, as the Pilot navigation API's
     * semanticOccurrences; refused where the model's library has no Occurrences::Occurrence. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOccurrencesSemantic() { return findOccurrencesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsage findOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_OCCURRENCE, name, scope, visibility, library)); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsage findOccurrence(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOccurrence(name, scope, visibility, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsage findOccurrence(String name, org.openmbee.sysml.Scope scope) { return findOccurrence(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The occurrence with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default OccurrenceUsage findOccurrence(String name) { return findOccurrence(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PARTS, scope, visibility, library))); }
    /**
     * The parts of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findParts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findParts(scope, visibility, false); }
    /**
     * The parts of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findParts(org.openmbee.sysml.Scope scope) { return findParts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findParts() { return findParts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PartUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PARTS_EXACT, scope, visibility, library))); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Leaves library at its
     * default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPartsExact(scope, visibility, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Leaves visibility and
     * library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findPartsExact(org.openmbee.sysml.Scope scope) { return findPartsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The parts whose metaclass is exactly PartUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsageList findPartsExact() { return findPartsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PARTS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPartsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPartsSemantic(org.openmbee.sysml.Scope scope) { return findPartsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Parts::Part, as the Pilot navigation API's semanticParts;
     * refused where the model's library has no Parts::Part. Leaves scope, visibility and library at
     * their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPartsSemantic() { return findPartsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsage findPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PART, name, scope, visibility, library)); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsage findPart(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPart(name, scope, visibility, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsage findPart(String name, org.openmbee.sysml.Scope scope) { return findPart(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The part with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PartUsage findPart(String name) { return findPart(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new PortUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PORTS, scope, visibility, library))); }
    /**
     * The ports of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPorts(scope, visibility, false); }
    /**
     * The ports of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsageList findPorts(org.openmbee.sysml.Scope scope) { return findPorts(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The ports of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsageList findPorts() { return findPorts(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PORTS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPortsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPortsSemantic(org.openmbee.sysml.Scope scope) { return findPortsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Ports::Port, as the Pilot navigation API's semanticPorts;
     * refused where the model's library has no Ports::Port. Leaves scope, visibility and library at
     * their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findPortsSemantic() { return findPortsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsage findPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_PORT, name, scope, visibility, library)); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsage findPort(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findPort(name, scope, visibility, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsage findPort(String name, org.openmbee.sysml.Scope scope) { return findPort(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The port with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default PortUsage findPort(String name) { return findPort(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ReferenceUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REFERENCES, scope, visibility, library))); }
    /**
     * The references of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findReferences(scope, visibility, false); }
    /**
     * The references of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsageList findReferences(org.openmbee.sysml.Scope scope) { return findReferences(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The references of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsageList findReferences() { return findReferences(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsage findReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REFERENCE, name, scope, visibility, library)); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsage findReference(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findReference(name, scope, visibility, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsage findReference(String name, org.openmbee.sysml.Scope scope) { return findReference(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The reference with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ReferenceUsage findReference(String name) { return findReference(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RenderingUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_RENDERINGS, scope, visibility, library))); }
    /**
     * The renderings of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRenderings(scope, visibility, false); }
    /**
     * The renderings of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsageList findRenderings(org.openmbee.sysml.Scope scope) { return findRenderings(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The renderings of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsageList findRenderings() { return findRenderings(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_RENDERINGS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRenderingsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRenderingsSemantic(org.openmbee.sysml.Scope scope) { return findRenderingsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::Rendering, as the Pilot navigation API's
     * semanticRenderings; refused where the model's library has no Views::Rendering. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRenderingsSemantic() { return findRenderingsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsage findRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_RENDERING, name, scope, visibility, library)); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsage findRendering(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRendering(name, scope, visibility, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsage findRendering(String name, org.openmbee.sysml.Scope scope) { return findRendering(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The rendering with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RenderingUsage findRendering(String name) { return findRendering(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REQUIREMENTS, scope, visibility, library))); }
    /**
     * The requirements of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirements(scope, visibility, false); }
    /**
     * The requirements of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirements(org.openmbee.sysml.Scope scope) { return findRequirements(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirements() { return findRequirements(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new RequirementUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REQUIREMENTS_EXACT, scope, visibility, library))); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirementsExact(scope, visibility, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirementsExact(org.openmbee.sysml.Scope scope) { return findRequirementsExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirements whose metaclass is exactly RequirementUsage, its subkinds left out. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsageList findRequirementsExact() { return findRequirementsExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REQUIREMENTS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirementsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRequirementsSemantic(org.openmbee.sysml.Scope scope) { return findRequirementsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Requirements::RequirementCheck, as the Pilot navigation
     * API's semanticRequirements; refused where the model's library has no
     * Requirements::RequirementCheck. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findRequirementsSemantic() { return findRequirementsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsage findRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_REQUIREMENT, name, scope, visibility, library)); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsage findRequirement(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findRequirement(name, scope, visibility, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsage findRequirement(String name, org.openmbee.sysml.Scope scope) { return findRequirement(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The requirement with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default RequirementUsage findRequirement(String name) { return findRequirement(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_STATES, scope, visibility, library))); }
    /**
     * The states of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStates(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStates(scope, visibility, false); }
    /**
     * The states of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStates(org.openmbee.sysml.Scope scope) { return findStates(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStates() { return findStates(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new StateUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_STATES_EXACT, scope, visibility, library))); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Leaves library at
     * its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStatesExact(scope, visibility, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Leaves visibility
     * and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStatesExact(org.openmbee.sysml.Scope scope) { return findStatesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The states whose metaclass is exactly StateUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsageList findStatesExact() { return findStatesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_STATES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findStatesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findStatesSemantic(org.openmbee.sysml.Scope scope) { return findStatesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to States::StateAction, as the Pilot navigation API's
     * semanticStates; refused where the model's library has no States::StateAction. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findStatesSemantic() { return findStatesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsage findState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_STATE, name, scope, visibility, library)); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsage findState(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findState(name, scope, visibility, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsage findState(String name, org.openmbee.sysml.Scope scope) { return findState(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The state with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default StateUsage findState(String name) { return findState(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new TransitionUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_TRANSITIONS, scope, visibility, library))); }
    /**
     * The transitions of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findTransitions(scope, visibility, false); }
    /**
     * The transitions of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsageList findTransitions(org.openmbee.sysml.Scope scope) { return findTransitions(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transitions of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsageList findTransitions() { return findTransitions(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsage findTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_TRANSITION, name, scope, visibility, library)); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsage findTransition(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findTransition(name, scope, visibility, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsage findTransition(String name, org.openmbee.sysml.Scope scope) { return findTransition(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The transition with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TransitionUsage findTransition(String name) { return findTransition(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USE_CASES, scope, visibility, library))); }
    /**
     * The useCases of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCases(scope, visibility, false); }
    /**
     * The useCases of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCases(org.openmbee.sysml.Scope scope) { return findUseCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCases() { return findUseCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new UseCaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USE_CASES_EXACT, scope, visibility, library))); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCasesExact(scope, visibility, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCasesExact(org.openmbee.sysml.Scope scope) { return findUseCasesExact(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCases whose metaclass is exactly UseCaseUsage, its subkinds left out. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsageList findUseCasesExact() { return findUseCasesExact(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USE_CASES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Leaves library
     * at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findUseCasesSemantic(org.openmbee.sysml.Scope scope) { return findUseCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to UseCases::UseCase, as the Pilot navigation API's
     * semanticUseCases; refused where the model's library has no UseCases::UseCase. Leaves scope,
     * visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findUseCasesSemantic() { return findUseCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsage findUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_USE_CASE, name, scope, visibility, library)); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsage findUseCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findUseCase(name, scope, visibility, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsage findUseCase(String name, org.openmbee.sysml.Scope scope) { return findUseCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The useCase with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default UseCaseUsage findUseCase(String name) { return findUseCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new VerificationCaseUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VERIFICATION_CASES, scope, visibility, library))); }
    /**
     * The verificationCases of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCases(scope, visibility, false); }
    /**
     * The verificationCases of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsageList findVerificationCases(org.openmbee.sysml.Scope scope) { return findVerificationCases(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCases of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsageList findVerificationCases() { return findVerificationCases(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VERIFICATION_CASES_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCasesSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findVerificationCasesSemantic(org.openmbee.sysml.Scope scope) { return findVerificationCasesSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to VerificationCases::VerificationCase, as the Pilot
     * navigation API's semanticVerificationCases; refused where the model's library has no
     * VerificationCases::VerificationCase. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findVerificationCasesSemantic() { return findVerificationCasesSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsage findVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VERIFICATION_CASE, name, scope, visibility, library)); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsage findVerificationCase(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findVerificationCase(name, scope, visibility, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and
     * false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsage findVerificationCase(String name, org.openmbee.sysml.Scope scope) { return findVerificationCase(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The verificationCase with this simple (effective) name, or none: part :&gt;&gt; wheels is
     * found as wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default VerificationCaseUsage findVerificationCase(String name) { return findVerificationCase(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEWS, scope, visibility, library))); }
    /**
     * The views of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViews(scope, visibility, false); }
    /**
     * The views of this type. Leaves visibility and library at their defaults: VisibilityFilter.ANY
     * and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsageList findViews(org.openmbee.sysml.Scope scope) { return findViews(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The views of this type. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsageList findViews() { return findViews(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEWS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewsSemantic(org.openmbee.sysml.Scope scope) { return findViewsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::View, as the Pilot navigation API's semanticViews;
     * refused where the model's library has no Views::View. Leaves scope, visibility and library at
     * their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewsSemantic() { return findViewsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsage findView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEW, name, scope, visibility, library)); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsage findView(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findView(name, scope, visibility, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsage findView(String name, org.openmbee.sysml.Scope scope) { return findView(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The view with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewUsage findView(String name) { return findView(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints of this type.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new ViewpointUsageList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEWPOINTS, scope, visibility, library))); }
    /**
     * The viewpoints of this type. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpoints(scope, visibility, false); }
    /**
     * The viewpoints of this type. Leaves visibility and library at their defaults:
     * VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsageList findViewpoints(org.openmbee.sysml.Scope scope) { return findViewpoints(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoints of this type. Leaves scope, visibility and library at their defaults:
     * Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsageList findViewpoints() { return findViewpoints(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEWPOINTS_SEMANTIC, scope, visibility, library))); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Leaves
     * library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpointsSemantic(scope, visibility, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Leaves
     * visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewpointsSemantic(org.openmbee.sysml.Scope scope) { return findViewpointsSemantic(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features whose type conforms to Views::ViewpointCheck, as the Pilot navigation API's
     * semanticViewpoints; refused where the model's library has no Views::ViewpointCheck. Leaves
     * scope, visibility and library at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findViewpointsSemantic() { return findViewpointsSemantic(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsage findViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_VIEWPOINT, name, scope, visibility, library)); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves library at its default, false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsage findViewpoint(String name, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findViewpoint(name, scope, visibility, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves visibility and library at their defaults: VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsage findViewpoint(String name, org.openmbee.sysml.Scope scope) { return findViewpoint(name, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The viewpoint with this simple (effective) name, or none: part :&gt;&gt; wheels is found as
     * wheels. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param name The simple name to look for, a feature's effective name.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default ViewpointUsage findViewpoint(String name) { return findViewpoint(name, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * Direct supertypes as written: supertypes(true). A conjugated type's only supertype is its
     * original type; a feature chain's last feature is a supertype of the chain.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TypeList findDeclaredSupertypes() { return new TypeList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_DECLARED_SUPERTYPES))); }
    /**
     * Direct supertypes the backend adds by the language's rules (Parts::Part for a part
     * definition).
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TypeList findImpliedSupertypes() { return new TypeList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_IMPLIED_SUPERTYPES))); }
    /**
     * Direct supertypes, written and implied: supertypes(false).
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default TypeList findDeclaredAndImpliedSupertypes() { return new TypeList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_DECLARED_AND_IMPLIED_SUPERTYPES))); }
    /**
     * The feature in effect here for feature: itself where inherited as it is, else the one
     * redefining it; none where this type does not have it.
     *
     * @param feature The feature as declared, on this type or on a type it specializes.
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Feature findEffectiveFeature(Feature feature) { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_EFFECTIVE_FEATURE, feature)); }
    /**
     * The lower bound as a number; none where the type has no multiplicity; refused where the bound
     * is an expression ([rotorCount]).
     *
     * @return The number as a Literal, an integer; null where there is none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default org.openmbee.sysml.Literal findLowerBound() { return (org.openmbee.sysml.Literal) ($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_LOWER_BOUND)); }
    /**
     * The upper bound as a number or * (unbounded); none where the type has no multiplicity;
     * refused where the bound is an expression.
     *
     * @return The number as a Literal, an integer or unbounded (*); null where there is none.
     * @throws org.openmbee.sysml.RefusedException Where the definition gives no answer for this
     *     element, as described above.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default org.openmbee.sysml.Literal findUpperBound() { return (org.openmbee.sysml.Literal) ($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_UPPER_BOUND)); }
    /**
     * The lower bound's expression; none where there is none (no multiplicity, the lower bound of
     * [*], a multiplicity without a range).
     *
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Expression findLowerBoundExpr() { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_LOWER_BOUND_EXPR)); }
    /**
     * The upper bound's expression; none where there is none.
     *
     * @return The element, or null where there is none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default Expression findUpperBoundExpr() { return $ref($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_UPPER_BOUND_EXPR)); }
    /**
     * Whether the type has a multiplicity whose lower bound is a literal (findLowerBound then
     * answers with a number).
     *
     * @return true or false.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default boolean findHasLiteralLowerBound() { return Boolean.TRUE.equals($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_HAS_LITERAL_LOWER_BOUND)); }
    /**
     * Whether the type has a multiplicity whose upper bound is a literal.
     *
     * @return true or false.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default boolean findHasLiteralUpperBound() { return Boolean.TRUE.equals($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_HAS_LITERAL_UPPER_BOUND)); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_INPUTS, scope, visibility, library))); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findInputs(scope, visibility, false); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Leaves visibility and library at their
     * defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInputs(org.openmbee.sysml.Scope scope) { return findInputs(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed in or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's out items are its inputs), scoped like the kind lists. With
     * scope all and library true, the specification's input. Leaves scope, visibility and library
     * at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findInputs() { return findInputs(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_OUTPUTS, scope, visibility, library))); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Leaves library at its default, false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findOutputs(scope, visibility, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Leaves visibility and library at
     * their defaults: VisibilityFilter.ANY and false.
     *
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOutputs(org.openmbee.sysml.Scope scope) { return findOutputs(scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features directed out or inout relative to this type, directionOf taking conjugation into
     * account (a conjugated port's in items are its outputs), scoped like the kind lists. With
     * scope all and library true, the specification's output. Leaves scope, visibility and library
     * at their defaults: Scope.OWNED, VisibilityFilter.ANY and false.
     *
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findOutputs() { return findOutputs(org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped.
     *
     * @param feature The feature the answers are or specialize.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @param library Whether members inherited from the standard library's types are kept (a user's
     *     library package counts as model).
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility, boolean library) { return new FeatureList($list($model().findMember(this, org.openmbee.sysml.classes.Finds.FIND_SUBSETTING_FEATURES_OF, feature, scope, visibility, library))); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Leaves library at its default, false.
     *
     * @param feature The feature the answers are or specialize.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @param visibility Which visibilities a list keeps: every one, public and protected, or public
     *     only.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope, org.openmbee.sysml.VisibilityFilter visibility) { return findSubsettingFeaturesOf(feature, scope, visibility, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Leaves visibility and library at their defaults: VisibilityFilter.ANY and
     * false.
     *
     * @param feature The feature the answers are or specialize.
     * @param scope Which members a list reads: the type's own, those it inherits, or both. A list
     *     of features gives its own first; a list of members or definitions, outside the owned
     *     scope, is in the backend's order. For a namespace's members, inherited includes imported.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature, org.openmbee.sysml.Scope scope) { return findSubsettingFeaturesOf(feature, scope, org.openmbee.sysml.VisibilityFilter.ANY, false); }
    /**
     * The features of this type that are, or specialize, feature (the wheels among a vehicle's
     * parts), scoped. Leaves scope, visibility and library at their defaults: Scope.OWNED,
     * VisibilityFilter.ANY and false.
     *
     * @param feature The feature the answers are or specialize.
     * @return The elements, in order; an empty list where there are none.
     * @throws org.openmbee.sysml.NotAnsweredException Where the backend cannot answer a property or
     *     operation this member reads; the message names it.
     */
    default FeatureList findSubsettingFeaturesOf(Feature feature) { return findSubsettingFeaturesOf(feature, org.openmbee.sysml.Scope.OWNED, org.openmbee.sysml.VisibilityFilter.ANY, false); }
}
