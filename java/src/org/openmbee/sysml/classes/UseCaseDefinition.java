// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — SysML 20250201. */
public interface UseCaseDefinition extends CaseDefinition {
    default java.util.List<ActionUsage> getAction() { return $list($model().read(this, "action")); }
    default java.util.List<PartUsage> getActorParameter() { return $list($model().read(this, "actorParameter")); }
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default java.util.List<CalculationUsage> getCalculation() { return $list($model().read(this, "calculation")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Type> getDifferencingType() { return $list($model().read(this, "differencingType")); }
    default java.util.List<Feature> getDirectedFeature() { return $list($model().read(this, "directedFeature")); }
    default java.util.List<Usage> getDirectedUsage() { return $list($model().read(this, "directedUsage")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default java.util.List<Feature> getEndFeature() { return $list($model().read(this, "endFeature")); }
    default java.util.List<Expression> getExpression() { return $list($model().read(this, "expression")); }
    default java.util.List<Feature> getFeature() { return $list($model().read(this, "feature")); }
    default java.util.List<FeatureMembership> getFeatureMembership() { return $list($model().read(this, "featureMembership")); }
    default java.util.List<Membership> getImportedMembership() { return $list($model().read(this, "importedMembership")); }
    default java.util.List<UseCaseUsage> getIncludedUseCase() { return $list($model().read(this, "includedUseCase")); }
    default java.util.List<Feature> getInheritedFeature() { return $list($model().read(this, "inheritedFeature")); }
    default java.util.List<Membership> getInheritedMembership() { return $list($model().read(this, "inheritedMembership")); }
    default java.util.List<Feature> getInput() { return $list($model().read(this, "input")); }
    default java.util.List<Type> getIntersectingType() { return $list($model().read(this, "intersectingType")); }
    default Boolean getIsAbstract() { return $bool($model().read(this, "isAbstract")); }
    default Boolean getIsConjugated() { return $bool($model().read(this, "isConjugated")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsIndividual() { return $bool($model().read(this, "isIndividual")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default Boolean getIsModelLevelEvaluable() { return $bool($model().read(this, "isModelLevelEvaluable")); }
    default Boolean getIsSufficient() { return $bool($model().read(this, "isSufficient")); }
    default Boolean getIsVariation() { return $bool($model().read(this, "isVariation")); }
    default java.util.List<org.openmbee.sysml.Element> getMember() { return $list($model().read(this, "member")); }
    default java.util.List<Membership> getMembership() { return $list($model().read(this, "membership")); }
    default Multiplicity getMultiplicity() { return $ref($model().read(this, "multiplicity")); }
    default String getName() { return $string($model().read(this, "name")); }
    default RequirementUsage getObjectiveRequirement() { return $ref($model().read(this, "objectiveRequirement")); }
    default java.util.List<Feature> getOutput() { return $list($model().read(this, "output")); }
    default java.util.List<ActionUsage> getOwnedAction() { return $list($model().read(this, "ownedAction")); }
    default java.util.List<AllocationUsage> getOwnedAllocation() { return $list($model().read(this, "ownedAllocation")); }
    default java.util.List<AnalysisCaseUsage> getOwnedAnalysisCase() { return $list($model().read(this, "ownedAnalysisCase")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<AttributeUsage> getOwnedAttribute() { return $list($model().read(this, "ownedAttribute")); }
    default java.util.List<CalculationUsage> getOwnedCalculation() { return $list($model().read(this, "ownedCalculation")); }
    default java.util.List<CaseUsage> getOwnedCase() { return $list($model().read(this, "ownedCase")); }
    default java.util.List<ConcernUsage> getOwnedConcern() { return $list($model().read(this, "ownedConcern")); }
    default Conjugation getOwnedConjugator() { return $ref($model().read(this, "ownedConjugator")); }
    default java.util.List<ConnectorAsUsage> getOwnedConnection() { return $list($model().read(this, "ownedConnection")); }
    default java.util.List<ConstraintUsage> getOwnedConstraint() { return $list($model().read(this, "ownedConstraint")); }
    default java.util.List<Differencing> getOwnedDifferencing() { return $list($model().read(this, "ownedDifferencing")); }
    default java.util.List<Disjoining> getOwnedDisjoining() { return $list($model().read(this, "ownedDisjoining")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<Feature> getOwnedEndFeature() { return $list($model().read(this, "ownedEndFeature")); }
    default java.util.List<EnumerationUsage> getOwnedEnumeration() { return $list($model().read(this, "ownedEnumeration")); }
    default java.util.List<Feature> getOwnedFeature() { return $list($model().read(this, "ownedFeature")); }
    default java.util.List<FeatureMembership> getOwnedFeatureMembership() { return $list($model().read(this, "ownedFeatureMembership")); }
    default java.util.List<FlowUsage> getOwnedFlow() { return $list($model().read(this, "ownedFlow")); }
    default java.util.List<Import> getOwnedImport() { return $list($model().read(this, "ownedImport")); }
    default java.util.List<InterfaceUsage> getOwnedInterface() { return $list($model().read(this, "ownedInterface")); }
    default java.util.List<Intersecting> getOwnedIntersecting() { return $list($model().read(this, "ownedIntersecting")); }
    default java.util.List<ItemUsage> getOwnedItem() { return $list($model().read(this, "ownedItem")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedMember() { return $list($model().read(this, "ownedMember")); }
    default java.util.List<Membership> getOwnedMembership() { return $list($model().read(this, "ownedMembership")); }
    default java.util.List<MetadataUsage> getOwnedMetadata() { return $list($model().read(this, "ownedMetadata")); }
    default java.util.List<OccurrenceUsage> getOwnedOccurrence() { return $list($model().read(this, "ownedOccurrence")); }
    default java.util.List<PartUsage> getOwnedPart() { return $list($model().read(this, "ownedPart")); }
    default java.util.List<PortUsage> getOwnedPort() { return $list($model().read(this, "ownedPort")); }
    default java.util.List<ReferenceUsage> getOwnedReference() { return $list($model().read(this, "ownedReference")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default java.util.List<RenderingUsage> getOwnedRendering() { return $list($model().read(this, "ownedRendering")); }
    default java.util.List<RequirementUsage> getOwnedRequirement() { return $list($model().read(this, "ownedRequirement")); }
    default java.util.List<Specialization> getOwnedSpecialization() { return $list($model().read(this, "ownedSpecialization")); }
    default java.util.List<StateUsage> getOwnedState() { return $list($model().read(this, "ownedState")); }
    default java.util.List<Subclassification> getOwnedSubclassification() { return $list($model().read(this, "ownedSubclassification")); }
    default java.util.List<TransitionUsage> getOwnedTransition() { return $list($model().read(this, "ownedTransition")); }
    default java.util.List<Unioning> getOwnedUnioning() { return $list($model().read(this, "ownedUnioning")); }
    default java.util.List<Usage> getOwnedUsage() { return $list($model().read(this, "ownedUsage")); }
    default java.util.List<UseCaseUsage> getOwnedUseCase() { return $list($model().read(this, "ownedUseCase")); }
    default java.util.List<VerificationCaseUsage> getOwnedVerificationCase() { return $list($model().read(this, "ownedVerificationCase")); }
    default java.util.List<ViewUsage> getOwnedView() { return $list($model().read(this, "ownedView")); }
    default java.util.List<ViewpointUsage> getOwnedViewpoint() { return $list($model().read(this, "ownedViewpoint")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default java.util.List<Feature> getParameter() { return $list($model().read(this, "parameter")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default Feature getResult() { return $ref($model().read(this, "result")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<Step> getStep() { return $list($model().read(this, "step")); }
    default Usage getSubjectParameter() { return $ref($model().read(this, "subjectParameter")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default java.util.List<Type> getUnioningType() { return $list($model().read(this, "unioningType")); }
    default java.util.List<Usage> getUsage() { return $list($model().read(this, "usage")); }
    default java.util.List<Usage> getVariant() { return $list($model().read(this, "variant")); }
    default java.util.List<VariantMembership> getVariantMembership() { return $list($model().read(this, "variantMembership")); }
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
}
