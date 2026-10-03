// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — KerML 20250201. */
public interface CollectExpression extends OperatorExpression {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default java.util.List<Expression> getArgument() { return $list($model().read(this, "argument")); }
    default java.util.List<Behavior> getBehavior() { return $list($model().read(this, "behavior")); }
    default java.util.List<Feature> getChainingFeature() { return $list($model().read(this, "chainingFeature")); }
    default Feature getCrossFeature() { return $ref($model().read(this, "crossFeature")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Type> getDifferencingType() { return $list($model().read(this, "differencingType")); }
    default java.util.List<Feature> getDirectedFeature() { return $list($model().read(this, "directedFeature")); }
    default String getDirection() { return $string($model().read(this, "direction")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default java.util.List<Feature> getEndFeature() { return $list($model().read(this, "endFeature")); }
    default Type getEndOwningType() { return $ref($model().read(this, "endOwningType")); }
    default java.util.List<Feature> getFeature() { return $list($model().read(this, "feature")); }
    default java.util.List<FeatureMembership> getFeatureMembership() { return $list($model().read(this, "featureMembership")); }
    default Feature getFeatureTarget() { return $ref($model().read(this, "featureTarget")); }
    default java.util.List<Type> getFeaturingType() { return $list($model().read(this, "featuringType")); }
    default Function getFunction() { return $ref($model().read(this, "function")); }
    default java.util.List<Membership> getImportedMembership() { return $list($model().read(this, "importedMembership")); }
    default java.util.List<Feature> getInheritedFeature() { return $list($model().read(this, "inheritedFeature")); }
    default java.util.List<Membership> getInheritedMembership() { return $list($model().read(this, "inheritedMembership")); }
    default java.util.List<Feature> getInput() { return $list($model().read(this, "input")); }
    default Type getInstantiatedType() { return $ref($model().read(this, "instantiatedType")); }
    default java.util.List<Type> getIntersectingType() { return $list($model().read(this, "intersectingType")); }
    default Boolean getIsAbstract() { return $bool($model().read(this, "isAbstract")); }
    default Boolean getIsComposite() { return $bool($model().read(this, "isComposite")); }
    default Boolean getIsConjugated() { return $bool($model().read(this, "isConjugated")); }
    default Boolean getIsConstant() { return $bool($model().read(this, "isConstant")); }
    default Boolean getIsDerived() { return $bool($model().read(this, "isDerived")); }
    default Boolean getIsEnd() { return $bool($model().read(this, "isEnd")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default Boolean getIsModelLevelEvaluable() { return $bool($model().read(this, "isModelLevelEvaluable")); }
    default Boolean getIsOrdered() { return $bool($model().read(this, "isOrdered")); }
    default Boolean getIsPortion() { return $bool($model().read(this, "isPortion")); }
    default Boolean getIsSufficient() { return $bool($model().read(this, "isSufficient")); }
    default Boolean getIsUnique() { return $bool($model().read(this, "isUnique")); }
    default Boolean getIsVariable() { return $bool($model().read(this, "isVariable")); }
    default java.util.List<org.openmbee.sysml.Element> getMember() { return $list($model().read(this, "member")); }
    default java.util.List<Membership> getMembership() { return $list($model().read(this, "membership")); }
    default Multiplicity getMultiplicity() { return $ref($model().read(this, "multiplicity")); }
    default String getName() { return $string($model().read(this, "name")); }
    default String getOperator() { return $string($model().read(this, "operator")); }
    default java.util.List<Feature> getOutput() { return $list($model().read(this, "output")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default Conjugation getOwnedConjugator() { return $ref($model().read(this, "ownedConjugator")); }
    default CrossSubsetting getOwnedCrossSubsetting() { return $ref($model().read(this, "ownedCrossSubsetting")); }
    default java.util.List<Differencing> getOwnedDifferencing() { return $list($model().read(this, "ownedDifferencing")); }
    default java.util.List<Disjoining> getOwnedDisjoining() { return $list($model().read(this, "ownedDisjoining")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<Feature> getOwnedEndFeature() { return $list($model().read(this, "ownedEndFeature")); }
    default java.util.List<Feature> getOwnedFeature() { return $list($model().read(this, "ownedFeature")); }
    default java.util.List<FeatureChaining> getOwnedFeatureChaining() { return $list($model().read(this, "ownedFeatureChaining")); }
    default java.util.List<FeatureInverting> getOwnedFeatureInverting() { return $list($model().read(this, "ownedFeatureInverting")); }
    default java.util.List<FeatureMembership> getOwnedFeatureMembership() { return $list($model().read(this, "ownedFeatureMembership")); }
    default java.util.List<Import> getOwnedImport() { return $list($model().read(this, "ownedImport")); }
    default java.util.List<Intersecting> getOwnedIntersecting() { return $list($model().read(this, "ownedIntersecting")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedMember() { return $list($model().read(this, "ownedMember")); }
    default java.util.List<Membership> getOwnedMembership() { return $list($model().read(this, "ownedMembership")); }
    default java.util.List<Redefinition> getOwnedRedefinition() { return $list($model().read(this, "ownedRedefinition")); }
    default ReferenceSubsetting getOwnedReferenceSubsetting() { return $ref($model().read(this, "ownedReferenceSubsetting")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default java.util.List<Specialization> getOwnedSpecialization() { return $list($model().read(this, "ownedSpecialization")); }
    default java.util.List<Subsetting> getOwnedSubsetting() { return $list($model().read(this, "ownedSubsetting")); }
    default java.util.List<TypeFeaturing> getOwnedTypeFeaturing() { return $list($model().read(this, "ownedTypeFeaturing")); }
    default java.util.List<FeatureTyping> getOwnedTyping() { return $list($model().read(this, "ownedTyping")); }
    default java.util.List<Unioning> getOwnedUnioning() { return $list($model().read(this, "ownedUnioning")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default FeatureMembership getOwningFeatureMembership() { return $ref($model().read(this, "owningFeatureMembership")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default Type getOwningType() { return $ref($model().read(this, "owningType")); }
    default java.util.List<Feature> getParameter() { return $list($model().read(this, "parameter")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default Feature getResult() { return $ref($model().read(this, "result")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default java.util.List<Type> getType() { return $list($model().read(this, "type")); }
    default java.util.List<Type> getUnioningType() { return $list($model().read(this, "unioningType")); }
    default java.util.List<Feature> allRedefinedFeatures() { return $list($model().call(this, "allRedefinedFeatures", new Object[] {})); }
    default java.util.List<Feature> allRedefinedFeaturesOf(Membership membership) { return $list($model().call(this, "allRedefinedFeaturesOf", new Object[] {membership})); }
    default java.util.List<Type> allSupertypes() { return $list($model().call(this, "allSupertypes", new Object[] {})); }
    default java.util.List<Type> asCartesianProduct() { return $list($model().call(this, "asCartesianProduct", new Object[] {})); }
    default Boolean canAccess(Feature feature) { return $bool($model().call(this, "canAccess", new Object[] {feature})); }
    default Boolean checkCondition(org.openmbee.sysml.Element target) { return $bool($model().call(this, "checkCondition", new Object[] {target})); }
    default String directionFor(Type type) { return $string($model().call(this, "directionFor", new Object[] {type})); }
    default String directionOf(Feature feature) { return $string($model().call(this, "directionOf", new Object[] {feature})); }
    default String directionOfExcluding(Feature feature, java.util.List<Type> excluded) { return $string($model().call(this, "directionOfExcluding", new Object[] {feature, excluded})); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default java.util.List<org.openmbee.sysml.Element> evaluate(org.openmbee.sysml.Element target) { return $list($model().call(this, "evaluate", new Object[] {target})); }
    default java.util.List<Membership> importedMemberships(java.util.List<Namespace> excluded) { return $list($model().call(this, "importedMemberships", new Object[] {excluded})); }
    default java.util.List<Membership> inheritableMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "inheritableMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default java.util.List<Membership> inheritedMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "inheritedMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default Type instantiatedType() { return $ref($model().call(this, "instantiatedType", new Object[] {})); }
    default Boolean isCartesianProduct() { return $bool($model().call(this, "isCartesianProduct", new Object[] {})); }
    default Boolean isCompatibleWith(Type otherType) { return $bool($model().call(this, "isCompatibleWith", new Object[] {otherType})); }
    default Boolean isFeaturedWithin(Type type) { return $bool($model().call(this, "isFeaturedWithin", new Object[] {type})); }
    default Boolean isFeaturingType(Type type) { return $bool($model().call(this, "isFeaturingType", new Object[] {type})); }
    default Boolean isOwnedCrossFeature() { return $bool($model().call(this, "isOwnedCrossFeature", new Object[] {})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default java.util.List<Membership> membershipsOfVisibility(String visibility, java.util.List<Namespace> excluded) { return $list($model().call(this, "membershipsOfVisibility", new Object[] {visibility, excluded})); }
    default Boolean modelLevelEvaluable(java.util.List<Feature> visited) { return $bool($model().call(this, "modelLevelEvaluable", new Object[] {visited})); }
    default java.util.List<Multiplicity> multiplicities() { return $list($model().call(this, "multiplicities", new Object[] {})); }
    default java.util.List<String> namesOf(org.openmbee.sysml.Element element) { return $list($model().call(this, "namesOf", new Object[] {element})); }
    default Feature namingFeature() { return $ref($model().call(this, "namingFeature", new Object[] {})); }
    default java.util.List<Membership> nonPrivateMemberships(java.util.List<Namespace> excludedNamespaces, java.util.List<Type> excludedTypes, Boolean excludeImplied) { return $list($model().call(this, "nonPrivateMemberships", new Object[] {excludedNamespaces, excludedTypes, excludeImplied})); }
    default Feature ownedCrossFeature() { return $ref($model().call(this, "ownedCrossFeature", new Object[] {})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
    default String qualificationOf(String qualifiedName) { return $string($model().call(this, "qualificationOf", new Object[] {qualifiedName})); }
    default Boolean redefines(Feature redefinedFeature) { return $bool($model().call(this, "redefines", new Object[] {redefinedFeature})); }
    default Boolean redefinesFromLibrary(String libraryFeatureName) { return $bool($model().call(this, "redefinesFromLibrary", new Object[] {libraryFeatureName})); }
    default java.util.List<Membership> removeRedefinedFeatures(java.util.List<Membership> memberships) { return $list($model().call(this, "removeRedefinedFeatures", new Object[] {memberships})); }
    default Membership resolve(String qualifiedName) { return $ref($model().call(this, "resolve", new Object[] {qualifiedName})); }
    default Membership resolveGlobal(String qualifiedName) { return $ref($model().call(this, "resolveGlobal", new Object[] {qualifiedName})); }
    default Membership resolveLocal(String name) { return $ref($model().call(this, "resolveLocal", new Object[] {name})); }
    default Membership resolveVisible(String name) { return $ref($model().call(this, "resolveVisible", new Object[] {name})); }
    default Boolean specializes(Type supertype) { return $bool($model().call(this, "specializes", new Object[] {supertype})); }
    default Boolean specializesFromLibrary(String libraryTypeName) { return $bool($model().call(this, "specializesFromLibrary", new Object[] {libraryTypeName})); }
    default Boolean subsetsChain(Feature first, Feature second) { return $bool($model().call(this, "subsetsChain", new Object[] {first, second})); }
    default java.util.List<Type> supertypes(Boolean excludeImplied) { return $list($model().call(this, "supertypes", new Object[] {excludeImplied})); }
    default java.util.List<Feature> typingFeatures() { return $list($model().call(this, "typingFeatures", new Object[] {})); }
    default String unqualifiedNameOf(String qualifiedName) { return $string($model().call(this, "unqualifiedNameOf", new Object[] {qualifiedName})); }
    default String visibilityOf(Membership mem) { return $string($model().call(this, "visibilityOf", new Object[] {mem})); }
    default java.util.List<Membership> visibleMemberships(java.util.List<Namespace> excluded, Boolean isRecursive, Boolean includeAll) { return $list($model().call(this, "visibleMemberships", new Object[] {excluded, isRecursive, includeAll})); }
}
