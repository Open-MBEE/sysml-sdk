"""Generated type stubs — do not edit. Source: metamodel.json."""

from .base import Element as _RuntimeElement

class Element(_RuntimeElement):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    def escapedName(self) -> str | None: ...
    def effectiveShortName(self) -> str | None: ...
    def effectiveName(self) -> str | None: ...
    def libraryNamespace(self) -> Namespace | None: ...
    def path(self) -> str | None: ...

class Namespace(Element):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importedMembership: list[Membership]
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    member: list[Element]
    membership: list[Membership]
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedImport: list[Import]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    def namesOf(self, element: Element | None) -> list[str]: ...
    def visibilityOf(self, mem: Membership | None) -> str | None: ...
    def visibleMemberships(self, excluded: list[Namespace], isRecursive: bool | None, includeAll: bool | None) -> list[Membership]: ...
    def importedMemberships(self, excluded: list[Namespace]) -> list[Membership]: ...
    def membershipsOfVisibility(self, visibility: str | None, excluded: list[Namespace]) -> list[Membership]: ...
    def resolve(self, qualifiedName: str | None) -> Membership | None: ...
    def resolveGlobal(self, qualifiedName: str | None) -> Membership | None: ...
    def resolveLocal(self, name: str | None) -> Membership | None: ...
    def resolveVisible(self, name: str | None) -> Membership | None: ...
    def qualificationOf(self, qualifiedName: str | None) -> str | None: ...
    def unqualifiedNameOf(self, qualifiedName: str | None) -> str | None: ...

class Type(Namespace):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    def visibleMemberships(self, excluded: list[Namespace], isRecursive: bool | None, includeAll: bool | None) -> list[Membership]: ...
    def inheritedMemberships(self, excludedNamespaces: list[Namespace], excludedTypes: list[Type], excludeImplied: bool | None) -> list[Membership]: ...
    def inheritableMemberships(self, excludedNamespaces: list[Namespace], excludedTypes: list[Type], excludeImplied: bool | None) -> list[Membership]: ...
    def nonPrivateMemberships(self, excludedNamespaces: list[Namespace], excludedTypes: list[Type], excludeImplied: bool | None) -> list[Membership]: ...
    def removeRedefinedFeatures(self, memberships: list[Membership]) -> list[Membership]: ...
    def allRedefinedFeaturesOf(self, membership: Membership | None) -> list[Feature]: ...
    def directionOf(self, feature: Feature | None) -> str | None: ...
    def directionOfExcluding(self, feature: Feature | None, excluded: list[Type]) -> str | None: ...
    def supertypes(self, excludeImplied: bool | None) -> list[Type]: ...
    def allSupertypes(self) -> list[Type]: ...
    def specializes(self, supertype: Type | None) -> bool | None: ...
    def specializesFromLibrary(self, libraryTypeName: str | None) -> bool | None: ...
    def isCompatibleWith(self, otherType: Type | None) -> bool | None: ...
    def multiplicities(self) -> list[Multiplicity]: ...

class Feature(Type):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def directionFor(self, type: Type | None) -> str | None: ...
    def effectiveShortName(self) -> str | None: ...
    def effectiveName(self) -> str | None: ...
    def namingFeature(self) -> Feature | None: ...
    def supertypes(self, excludeImplied: bool | None) -> list[Type]: ...
    def redefines(self, redefinedFeature: Feature | None) -> bool | None: ...
    def redefinesFromLibrary(self, libraryFeatureName: str | None) -> bool | None: ...
    def subsetsChain(self, first: Feature | None, second: Feature | None) -> bool | None: ...
    def isCompatibleWith(self, otherType: Type | None) -> bool | None: ...
    def typingFeatures(self) -> list[Feature]: ...
    def asCartesianProduct(self) -> list[Type]: ...
    def isCartesianProduct(self) -> bool | None: ...
    def isOwnedCrossFeature(self) -> bool | None: ...
    def ownedCrossFeature(self) -> Feature | None: ...
    def allRedefinedFeatures(self) -> list[Feature]: ...
    def isFeaturedWithin(self, type: Type | None) -> bool | None: ...
    def canAccess(self, feature: Feature | None) -> bool | None: ...
    def isFeaturingType(self, type: Type | None) -> bool | None: ...

class Step(Feature):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class Usage(Feature):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def namingFeature(self) -> Feature | None: ...
    def referencedFeatureTarget(self) -> Feature | None: ...

class OccurrenceUsage(Usage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ActionUsage(OccurrenceUsage, Step):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def inputParameters(self) -> list[Feature]: ...
    def inputParameter(self, i: int | None) -> Feature | None: ...
    def argument(self, i: int | None) -> Expression | None: ...
    def isSubactionUsage(self) -> bool | None: ...

class AcceptActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    payloadArgument: Expression | None
    payloadParameter: ReferenceUsage | None
    portionKind: str | None
    qualifiedName: str | None
    receiverArgument: Expression | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def isTriggerAction(self) -> bool | None: ...

class Classifier(Type):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class Definition(Classifier):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Class(Classifier):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class OccurrenceDefinition(Class, Definition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Behavior(Class):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class ActionDefinition(Behavior, OccurrenceDefinition):
    action: list[ActionUsage]
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Relationship(Element):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    def libraryNamespace(self) -> Namespace | None: ...
    def path(self) -> str | None: ...

class Membership(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def isDistinguishableFrom(self, other: Membership | None) -> bool | None: ...

class OwningMembership(Membership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def path(self) -> str | None: ...

class FeatureMembership(OwningMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class ParameterMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberParameter: Feature | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def parameterDirection(self) -> str | None: ...

class ActorMembership(ParameterMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedActorParameter: PartUsage | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberParameter: Feature | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class Structure(Class):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class Association(Relationship, Classifier):
    aliasIds: list[str]
    associationEnd: list[Feature]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class AssociationStructure(Association, Structure):
    aliasIds: list[str]
    associationEnd: list[Feature]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class ItemDefinition(Structure, OccurrenceDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class PartDefinition(ItemDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ConnectionDefinition(PartDefinition, AssociationStructure):
    aliasIds: list[str]
    associationEnd: list[Feature]
    connectionEnd: list[Usage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AllocationDefinition(ConnectionDefinition):
    aliasIds: list[str]
    allocation: list[AllocationUsage]
    associationEnd: list[Feature]
    connectionEnd: list[Usage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ItemUsage(OccurrenceUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class PartUsage(ItemUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Connector(Relationship, Feature):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class ConnectorAsUsage(Connector, Usage):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ConnectionUsage(ConnectorAsUsage, PartUsage):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectionDefinition: list[AssociationStructure]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AllocationUsage(ConnectionUsage):
    aliasIds: list[str]
    allocationDefinition: list[AllocationDefinition]
    association: list[Association]
    chainingFeature: list[Feature]
    connectionDefinition: list[AssociationStructure]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Function(Behavior):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class CalculationDefinition(Function, ActionDefinition):
    action: list[ActionUsage]
    aliasIds: list[str]
    calculation: list[CalculationUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class CaseDefinition(CalculationDefinition):
    action: list[ActionUsage]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    calculation: list[CalculationUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    objectiveRequirement: RequirementUsage | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AnalysisCaseDefinition(CaseDefinition):
    action: list[ActionUsage]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    calculation: list[CalculationUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    objectiveRequirement: RequirementUsage | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    resultExpression: Expression | None
    shortName: str | None
    step: list[Step]
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Expression(Step):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...
    def checkCondition(self, target: Element | None) -> bool | None: ...

class CalculationUsage(Expression, ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    calculationDefinition: Function | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...

class CaseUsage(CalculationUsage):
    actionDefinition: list[Behavior]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    behavior: list[Behavior]
    calculationDefinition: Function | None
    caseDefinition: CaseDefinition | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    objectiveRequirement: RequirementUsage | None
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AnalysisCaseUsage(CaseUsage):
    actionDefinition: list[Behavior]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    analysisCaseDefinition: AnalysisCaseDefinition | None
    behavior: list[Behavior]
    calculationDefinition: Function | None
    caseDefinition: CaseDefinition | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    objectiveRequirement: RequirementUsage | None
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    resultExpression: Expression | None
    shortName: str | None
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AnnotatingElement(Element):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]

class Annotation(Relationship):
    aliasIds: list[str]
    annotatedElement: Element | None
    annotatingElement: AnnotatingElement | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotatingElement: AnnotatingElement | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningAnnotatedElement: Element | None
    owningAnnotatingElement: AnnotatingElement | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class BooleanExpression(Expression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    predicate: Predicate | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class Invariant(BooleanExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isNegated: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    predicate: Predicate | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class ConstraintUsage(BooleanExpression, OccurrenceUsage):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def namingFeature(self) -> Feature | None: ...
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...

class AssertConstraintUsage(ConstraintUsage, Invariant):
    aliasIds: list[str]
    assertedConstraint: ConstraintUsage | None
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isNegated: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AssignmentActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    referent: Feature | None
    shortName: str | None
    targetArgument: Expression | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    valueExpression: Expression | None
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class DataType(Classifier):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class AttributeDefinition(DataType, Definition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class AttributeUsage(Usage):
    aliasIds: list[str]
    attributeDefinition: list[DataType]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class BindingConnector(Connector):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class BindingConnectorAsUsage(BindingConnector, ConnectorAsUsage):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class InstantiationExpression(Expression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def instantiatedType_op(self) -> Type | None: ...

class InvocationExpression(InstantiationExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...

class OperatorExpression(InvocationExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    operator: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def instantiatedType_op(self) -> Type | None: ...

class CollectExpression(OperatorExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    operator: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class Comment(AnnotatingElement):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    body: str | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    locale: str | None
    name: str | None
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]

class Predicate(Function):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class ConstraintDefinition(Predicate, OccurrenceDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class RequirementDefinition(ConstraintDefinition):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    framedConcern: list[ConcernUsage]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    step: list[Step]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ConcernDefinition(RequirementDefinition):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    framedConcern: list[ConcernUsage]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    step: list[Step]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class RequirementUsage(ConstraintUsage):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    framedConcern: list[ConcernUsage]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    requirementDefinition: RequirementDefinition | None
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ConcernUsage(RequirementUsage):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    concernDefinition: ConcernDefinition | None
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    framedConcern: list[ConcernUsage]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    requirementDefinition: RequirementDefinition | None
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class PortDefinition(Structure, OccurrenceDefinition):
    aliasIds: list[str]
    conjugatedPortDefinition: ConjugatedPortDefinition | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ConjugatedPortDefinition(PortDefinition):
    aliasIds: list[str]
    conjugatedPortDefinition: ConjugatedPortDefinition | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    originalPortDefinition: PortDefinition | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedPortConjugator: PortConjugation | None
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def effectiveName(self) -> str | None: ...

class Specialization(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class FeatureTyping(Specialization):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    type: Type | None
    typedFeature: Feature | None

class ConjugatedPortTyping(FeatureTyping):
    aliasIds: list[str]
    conjugatedPortDefinition: ConjugatedPortDefinition | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    portDefinition: PortDefinition | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    type: Type | None
    typedFeature: Feature | None

class Conjugation(Relationship):
    aliasIds: list[str]
    conjugatedType: Type | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    originalType: Type | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class ConstructorExpression(InstantiationExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...

class ControlNode(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def multiplicityHasBounds(self, mult: Multiplicity | None, lower: int | None, upper: int | None) -> bool | None: ...

class Subsetting(Specialization):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    subsettedFeature: Feature | None
    subsettingFeature: Feature | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class CrossSubsetting(Subsetting):
    aliasIds: list[str]
    crossedFeature: Feature | None
    crossingFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    subsettedFeature: Feature | None
    subsettingFeature: Feature | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class DecisionNode(ControlNode):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Dependency(Relationship):
    aliasIds: list[str]
    client: list[Element]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    supplier: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class Differencing(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: Type | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    typeDifferenced: Type | None

class Disjoining(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    disjoiningType: Type | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    typeDisjoined: Type | None

class Documentation(Comment):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    body: str | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    documentedElement: Element | None
    elementId: str | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    locale: str | None
    name: str | None
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]

class ElementFilterMembership(OwningMembership):
    aliasIds: list[str]
    condition: Expression | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class EndFeatureMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class EnumerationDefinition(AttributeDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    enumeratedValue: list[EnumerationUsage]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class EnumerationUsage(AttributeUsage):
    aliasIds: list[str]
    attributeDefinition: list[DataType]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    enumerationDefinition: EnumerationDefinition | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class EventOccurrenceUsage(OccurrenceUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    eventOccurrence: OccurrenceUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class PerformActionUsage(EventOccurrenceUsage, ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    eventOccurrence: OccurrenceUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    performedAction: ActionUsage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def namingFeature(self) -> Feature | None: ...

class StateUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    doAction: ActionUsage | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    entryAction: ActionUsage | None
    exitAction: ActionUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isParallel: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    stateDefinition: list[Behavior]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def isSubstateUsage(self, isParallel: bool | None) -> bool | None: ...

class ExhibitStateUsage(StateUsage, PerformActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    doAction: ActionUsage | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    entryAction: ActionUsage | None
    eventOccurrence: OccurrenceUsage | None
    exhibitedState: StateUsage | None
    exitAction: ActionUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isParallel: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    performedAction: ActionUsage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    stateDefinition: list[Behavior]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Import(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def importedMemberships(self, excluded: list[Namespace]) -> list[Membership]: ...

class Expose(Import):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class FeatureChainExpression(OperatorExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    operator: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    targetFeature: Feature | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def sourceTargetFeature(self) -> Feature | None: ...

class FeatureChaining(Relationship):
    aliasIds: list[str]
    chainingFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    featureChained: Feature | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class FeatureInverting(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    featureInverted: Feature | None
    invertingFeature: Feature | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class FeatureReferenceExpression(Expression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    referent: Feature | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...

class FeatureValue(OwningMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    featureWithValue: Feature | None
    isDefault: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isInitial: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    value: Expression | None
    visibility: str | None

class Flow(Connector, Step):
    aliasIds: list[str]
    association: list[Association]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    flowEnd: list[FlowEnd]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interaction: list[Interaction]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    payloadFeature: PayloadFeature | None
    payloadType: list[Classifier]
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    sourceOutputFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    targetInputFeature: Feature | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class Interaction(Association, Behavior):
    aliasIds: list[str]
    associationEnd: list[Feature]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    step: list[Step]
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class FlowDefinition(Interaction, ActionDefinition):
    action: list[ActionUsage]
    aliasIds: list[str]
    associationEnd: list[Feature]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    flowEnd: list[Usage]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    step: list[Step]
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class FlowEnd(Feature):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class FlowUsage(Flow, ConnectorAsUsage, ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    association: list[Association]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    flowDefinition: list[Interaction]
    flowEnd: list[FlowEnd]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interaction: list[Interaction]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    payloadFeature: PayloadFeature | None
    payloadType: list[Classifier]
    portionKind: str | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    sourceOutputFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    targetInputFeature: Feature | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class LoopActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    bodyAction: ActionUsage | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ForLoopActionUsage(LoopActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    bodyAction: ActionUsage | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    loopVariable: ReferenceUsage | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    seqArgument: Expression | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class ForkNode(ControlNode):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class RequirementConstraintMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    kind: str | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedConstraint: ConstraintUsage | None
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    referencedConstraint: ConstraintUsage | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class FramedConcernMembership(RequirementConstraintMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    kind: str | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedConcern: ConcernUsage | None
    ownedConstraint: ConstraintUsage | None
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    referencedConcern: ConcernUsage | None
    referencedConstraint: ConstraintUsage | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class IfActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    elseAction: ActionUsage | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    ifArgument: Expression | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    thenAction: ActionUsage | None
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class UseCaseUsage(CaseUsage):
    actionDefinition: list[Behavior]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    behavior: list[Behavior]
    calculationDefinition: Function | None
    caseDefinition: CaseDefinition | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    includedUseCase: list[UseCaseUsage]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    objectiveRequirement: RequirementUsage | None
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    useCaseDefinition: UseCaseDefinition | None
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class IncludeUseCaseUsage(UseCaseUsage, PerformActionUsage):
    actionDefinition: list[Behavior]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    behavior: list[Behavior]
    calculationDefinition: Function | None
    caseDefinition: CaseDefinition | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    eventOccurrence: OccurrenceUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    includedUseCase: list[UseCaseUsage]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    objectiveRequirement: RequirementUsage | None
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    performedAction: ActionUsage | None
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    useCaseDefinition: UseCaseDefinition | None
    useCaseIncluded: UseCaseUsage | None
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class IndexExpression(OperatorExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    operator: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class InterfaceDefinition(ConnectionDefinition):
    aliasIds: list[str]
    associationEnd: list[Feature]
    connectionEnd: list[Usage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interfaceEnd: list[PortUsage]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedType: list[Type]
    shortName: str | None
    source: list[Element]
    sourceType: Type | None
    target: list[Element]
    targetType: list[Type]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class InterfaceUsage(ConnectionUsage):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectionDefinition: list[AssociationStructure]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interfaceDefinition: list[InterfaceDefinition]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Intersecting(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    intersectingType: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    typeIntersected: Type | None

class JoinNode(ControlNode):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Package(Namespace):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    filterCondition: list[Expression]
    importedMembership: list[Membership]
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    member: list[Element]
    membership: list[Membership]
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedImport: list[Import]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    def importedMemberships(self, excluded: list[Namespace]) -> list[Membership]: ...
    def includeAsMember(self, element: Element | None) -> bool | None: ...

class LibraryPackage(Package):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    filterCondition: list[Expression]
    importedMembership: list[Membership]
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isStandard: bool | None
    member: list[Element]
    membership: list[Membership]
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedImport: list[Import]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    def libraryNamespace(self) -> Namespace | None: ...

class LiteralExpression(Expression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...

class LiteralBoolean(LiteralExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    value: bool | None

class LiteralInfinity(LiteralExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class LiteralInteger(LiteralExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    value: int | None

class LiteralRational(LiteralExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    value: float | None

class LiteralString(LiteralExpression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    value: str | None

class MembershipImport(Import):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    importedMembership: Membership | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def importedMemberships(self, excluded: list[Namespace]) -> list[Membership]: ...

class MembershipExpose(MembershipImport, Expose):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    importedMembership: Membership | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class MergeNode(ControlNode):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Metaclass(Structure):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubclassification: list[Subclassification]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]

class MetadataAccessExpression(Expression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    referencedElement: Element | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...
    def metaclassFeature(self) -> MetadataFeature | None: ...

class MetadataDefinition(Metaclass, ItemDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class MetadataFeature(AnnotatingElement, Feature):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    metaclass: Metaclass | None
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def evaluateFeature(self, baseFeature: Feature | None) -> list[Element]: ...
    def isSemantic(self) -> bool | None: ...
    def isSyntactic(self) -> bool | None: ...
    def syntaxElement(self) -> Element | None: ...

class MetadataUsage(MetadataFeature, ItemUsage):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    metaclass: Metaclass | None
    metadataDefinition: Metaclass | None
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Multiplicity(Feature):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class MultiplicityRange(Multiplicity):
    aliasIds: list[str]
    bound: list[Expression]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    lowerBound: Expression | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    upperBound: Expression | None
    def hasBounds(self, lower: int | None, upper: int | None) -> bool | None: ...
    def valueOf(self, bound: Expression | None) -> int | None: ...

class NamespaceImport(Import):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    importedNamespace: Namespace | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def importedMemberships(self, excluded: list[Namespace]) -> list[Membership]: ...

class NamespaceExpose(NamespaceImport, Expose):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    importOwningNamespace: Namespace | None
    importedElement: Element | None
    importedNamespace: Namespace | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isImportAll: bool | None
    isLibraryElement: bool | None
    isRecursive: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class NullExpression(Expression):
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def modelLevelEvaluable(self, visited: list[Feature]) -> bool | None: ...
    def evaluate(self, target: Element | None) -> list[Element]: ...

class ObjectiveMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedObjectiveRequirement: RequirementUsage | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class PayloadFeature(Feature):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class PortConjugation(Conjugation):
    aliasIds: list[str]
    conjugatedPortDefinition: ConjugatedPortDefinition | None
    conjugatedType: Type | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    originalPortDefinition: PortDefinition | None
    originalType: Type | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class PortUsage(OccurrenceUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    portDefinition: list[PortDefinition]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class Redefinition(Subsetting):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    redefinedFeature: Feature | None
    redefiningFeature: Feature | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    subsettedFeature: Feature | None
    subsettingFeature: Feature | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class ReferenceSubsetting(Subsetting):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeature: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    referencedFeature: Feature | None
    referencingFeature: Feature | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    subsettedFeature: Feature | None
    subsettingFeature: Feature | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class ReferenceUsage(Usage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def namingFeature(self) -> Feature | None: ...

class RenderingDefinition(PartDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    rendering: list[RenderingUsage]
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class RenderingUsage(PartUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    renderingDefinition: RenderingDefinition | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class RequirementVerificationMembership(RequirementConstraintMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    kind: str | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedConstraint: ConstraintUsage | None
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRequirement: RequirementUsage | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    referencedConstraint: ConstraintUsage | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    verifiedRequirement: RequirementUsage | None
    visibility: str | None

class ResultExpressionMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedResultExpression: Expression | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class ReturnParameterMembership(ParameterMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberParameter: Feature | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None
    def parameterDirection(self) -> str | None: ...

class SatisfyRequirementUsage(RequirementUsage, AssertConstraintUsage):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assertedConstraint: ConstraintUsage | None
    assumedConstraint: list[ConstraintUsage]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    framedConcern: list[ConcernUsage]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isNegated: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    requirementDefinition: RequirementDefinition | None
    result: Feature | None
    satisfiedRequirement: RequirementUsage | None
    satisfyingFeature: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class SelectExpression(OperatorExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    operator: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class SendActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    payloadArgument: Expression | None
    portionKind: str | None
    qualifiedName: str | None
    receiverArgument: Expression | None
    senderArgument: Expression | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class StakeholderMembership(ParameterMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberParameter: Feature | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedStakeholderParameter: PartUsage | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class StateDefinition(ActionDefinition):
    action: list[ActionUsage]
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    doAction: ActionUsage | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    entryAction: ActionUsage | None
    exitAction: ActionUsage | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isParallel: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    shortName: str | None
    state: list[StateUsage]
    step: list[Step]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class StateSubactionMembership(FeatureMembership):
    action: ActionUsage | None
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    kind: str | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class Subclassification(Specialization):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    general: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningClassifier: Classifier | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    specific: Type | None
    subclassifier: Classifier | None
    superclassifier: Classifier | None
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class SubjectMembership(ParameterMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberParameter: Feature | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSubjectParameter: Usage | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class Succession(Connector):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class SuccessionAsUsage(Succession, ConnectorAsUsage):
    aliasIds: list[str]
    association: list[Association]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class SuccessionFlow(Succession, Flow):
    aliasIds: list[str]
    association: list[Association]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    flowEnd: list[FlowEnd]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interaction: list[Interaction]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    payloadFeature: PayloadFeature | None
    payloadType: list[Classifier]
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    sourceOutputFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    targetInputFeature: Feature | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]

class SuccessionFlowUsage(SuccessionFlow, FlowUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    association: list[Association]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    connectorEnd: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    defaultFeaturingType: Type | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    flowDefinition: list[Interaction]
    flowEnd: list[FlowEnd]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    interaction: list[Interaction]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    payloadFeature: PayloadFeature | None
    payloadType: list[Classifier]
    portionKind: str | None
    qualifiedName: str | None
    relatedElement: list[Element]
    relatedFeature: list[Feature]
    shortName: str | None
    source: list[Element]
    sourceFeature: Feature | None
    sourceOutputFeature: Feature | None
    target: list[Element]
    targetFeature: list[Feature]
    targetInputFeature: Feature | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class TerminateActionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    terminatedOccurrenceArgument: Expression | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class TextualRepresentation(AnnotatingElement):
    aliasIds: list[str]
    annotatedElement: list[Element]
    annotation: list[Annotation]
    body: str | None
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    language: str | None
    name: str | None
    ownedAnnotatingRelationship: list[Annotation]
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningAnnotatingRelationship: Annotation | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    representedElement: Element | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]

class TransitionFeatureMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    kind: str | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    transitionFeature: Step | None
    visibility: str | None

class TransitionUsage(ActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    effectAction: list[ActionUsage]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    guardExpression: list[Expression]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    source: ActionUsage | None
    succession: Succession | None
    target: ActionUsage | None
    textualRepresentation: list[TextualRepresentation]
    triggerAction: list[AcceptActionUsage]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    def triggerPayloadParameter(self) -> ReferenceUsage | None: ...
    def sourceFeature(self) -> Feature | None: ...

class TriggerInvocationExpression(InvocationExpression):
    aliasIds: list[str]
    argument: list[Expression]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    instantiatedType: Type | None
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    kind: str | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    def instantiatedType_op(self) -> Type | None: ...

class TypeFeaturing(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    featureOfType: Feature | None
    featuringType: Type | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningFeatureOfType: Feature | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]

class Unioning(Relationship):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    typeUnioned: Type | None
    unioningType: Type | None

class UseCaseDefinition(CaseDefinition):
    action: list[ActionUsage]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    calculation: list[CalculationUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    includedUseCase: list[UseCaseUsage]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    objectiveRequirement: RequirementUsage | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]

class VariantMembership(OwningMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedVariantUsage: Usage | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class VerificationCaseDefinition(CaseDefinition):
    action: list[ActionUsage]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    calculation: list[CalculationUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    objectiveRequirement: RequirementUsage | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    step: list[Step]
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    verifiedRequirement: list[RequirementUsage]

class VerificationCaseUsage(CaseUsage):
    actionDefinition: list[Behavior]
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    behavior: list[Behavior]
    calculationDefinition: Function | None
    caseDefinition: CaseDefinition | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    objectiveRequirement: RequirementUsage | None
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    result: Feature | None
    shortName: str | None
    subjectParameter: Usage | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    verificationCaseDefinition: VerificationCaseDefinition | None
    verifiedRequirement: list[RequirementUsage]

class ViewDefinition(PartDefinition):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    qualifiedName: str | None
    satisfiedViewpoint: list[ViewpointUsage]
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    view: list[ViewUsage]
    viewCondition: list[Expression]
    viewRendering: RenderingUsage | None

class ViewRenderingMembership(FeatureMembership):
    aliasIds: list[str]
    declaredName: str | None
    declaredShortName: str | None
    documentation: list[Documentation]
    elementId: str | None
    isImplied: bool | None
    isImpliedIncluded: bool | None
    isLibraryElement: bool | None
    memberElement: Element | None
    memberElementId: str | None
    memberName: str | None
    memberShortName: str | None
    membershipOwningNamespace: Namespace | None
    name: str | None
    ownedAnnotation: list[Annotation]
    ownedElement: list[Element]
    ownedMemberElement: Element | None
    ownedMemberElementId: str | None
    ownedMemberFeature: Feature | None
    ownedMemberName: str | None
    ownedMemberShortName: str | None
    ownedRelatedElement: list[Element]
    ownedRelationship: list[Relationship]
    ownedRendering: RenderingUsage | None
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelatedElement: Element | None
    owningRelationship: Relationship | None
    owningType: Type | None
    qualifiedName: str | None
    referencedRendering: RenderingUsage | None
    relatedElement: list[Element]
    shortName: str | None
    source: list[Element]
    target: list[Element]
    textualRepresentation: list[TextualRepresentation]
    visibility: str | None

class ViewUsage(PartUsage):
    aliasIds: list[str]
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    exposedElement: list[Element]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    itemDefinition: list[Structure]
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    partDefinition: list[PartDefinition]
    portionKind: str | None
    qualifiedName: str | None
    satisfiedViewpoint: list[ViewpointUsage]
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    viewCondition: list[Expression]
    viewDefinition: ViewDefinition | None
    viewRendering: RenderingUsage | None
    def includeAsExposed(self, element: Element | None) -> bool | None: ...

class ViewpointDefinition(RequirementDefinition):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    declaredName: str | None
    declaredShortName: str | None
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    expression: list[Expression]
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    framedConcern: list[ConcernUsage]
    importedMembership: list[Membership]
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isConjugated: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isSufficient: bool | None
    isVariation: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    output: list[Feature]
    ownedAction: list[ActionUsage]
    ownedAllocation: list[AllocationUsage]
    ownedAnalysisCase: list[AnalysisCaseUsage]
    ownedAnnotation: list[Annotation]
    ownedAttribute: list[AttributeUsage]
    ownedCalculation: list[CalculationUsage]
    ownedCase: list[CaseUsage]
    ownedConcern: list[ConcernUsage]
    ownedConjugator: Conjugation | None
    ownedConnection: list[ConnectorAsUsage]
    ownedConstraint: list[ConstraintUsage]
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedEnumeration: list[EnumerationUsage]
    ownedFeature: list[Feature]
    ownedFeatureMembership: list[FeatureMembership]
    ownedFlow: list[FlowUsage]
    ownedImport: list[Import]
    ownedInterface: list[InterfaceUsage]
    ownedIntersecting: list[Intersecting]
    ownedItem: list[ItemUsage]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedMetadata: list[MetadataUsage]
    ownedOccurrence: list[OccurrenceUsage]
    ownedPart: list[PartUsage]
    ownedPort: list[PortUsage]
    ownedReference: list[ReferenceUsage]
    ownedRelationship: list[Relationship]
    ownedRendering: list[RenderingUsage]
    ownedRequirement: list[RequirementUsage]
    ownedSpecialization: list[Specialization]
    ownedState: list[StateUsage]
    ownedSubclassification: list[Subclassification]
    ownedTransition: list[TransitionUsage]
    ownedUnioning: list[Unioning]
    ownedUsage: list[Usage]
    ownedUseCase: list[UseCaseUsage]
    ownedVerificationCase: list[VerificationCaseUsage]
    ownedView: list[ViewUsage]
    ownedViewpoint: list[ViewpointUsage]
    owner: Element | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    parameter: list[Feature]
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    step: list[Step]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    viewpointStakeholder: list[PartUsage]

class ViewpointUsage(RequirementUsage):
    actorParameter: list[PartUsage]
    aliasIds: list[str]
    assumedConstraint: list[ConstraintUsage]
    behavior: list[Behavior]
    chainingFeature: list[Feature]
    constraintDefinition: Predicate | None
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    framedConcern: list[ConcernUsage]
    function: Function | None
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isModelLevelEvaluable: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    predicate: Predicate | None
    qualifiedName: str | None
    reqId: str | None
    requiredConstraint: list[ConstraintUsage]
    requirementDefinition: RequirementDefinition | None
    result: Feature | None
    shortName: str | None
    stakeholderParameter: list[PartUsage]
    subjectParameter: Usage | None
    text: list[str]
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    viewpointDefinition: ViewpointDefinition | None
    viewpointStakeholder: list[PartUsage]

class WhileLoopActionUsage(LoopActionUsage):
    actionDefinition: list[Behavior]
    aliasIds: list[str]
    behavior: list[Behavior]
    bodyAction: ActionUsage | None
    chainingFeature: list[Feature]
    crossFeature: Feature | None
    declaredName: str | None
    declaredShortName: str | None
    definition: list[Classifier]
    differencingType: list[Type]
    directedFeature: list[Feature]
    directedUsage: list[Usage]
    direction: str | None
    documentation: list[Documentation]
    elementId: str | None
    endFeature: list[Feature]
    endOwningType: Type | None
    feature: list[Feature]
    featureMembership: list[FeatureMembership]
    featureTarget: Feature | None
    featuringType: list[Type]
    importedMembership: list[Membership]
    individualDefinition: OccurrenceDefinition | None
    inheritedFeature: list[Feature]
    inheritedMembership: list[Membership]
    input: list[Feature]
    intersectingType: list[Type]
    isAbstract: bool | None
    isComposite: bool | None
    isConjugated: bool | None
    isConstant: bool | None
    isDerived: bool | None
    isEnd: bool | None
    isImpliedIncluded: bool | None
    isIndividual: bool | None
    isLibraryElement: bool | None
    isOrdered: bool | None
    isPortion: bool | None
    isReference: bool | None
    isSufficient: bool | None
    isUnique: bool | None
    isVariable: bool | None
    isVariation: bool | None
    mayTimeVary: bool | None
    member: list[Element]
    membership: list[Membership]
    multiplicity: Multiplicity | None
    name: str | None
    nestedAction: list[ActionUsage]
    nestedAllocation: list[AllocationUsage]
    nestedAnalysisCase: list[AnalysisCaseUsage]
    nestedAttribute: list[AttributeUsage]
    nestedCalculation: list[CalculationUsage]
    nestedCase: list[CaseUsage]
    nestedConcern: list[ConcernUsage]
    nestedConnection: list[ConnectorAsUsage]
    nestedConstraint: list[ConstraintUsage]
    nestedEnumeration: list[EnumerationUsage]
    nestedFlow: list[FlowUsage]
    nestedInterface: list[InterfaceUsage]
    nestedItem: list[ItemUsage]
    nestedMetadata: list[MetadataUsage]
    nestedOccurrence: list[OccurrenceUsage]
    nestedPart: list[PartUsage]
    nestedPort: list[PortUsage]
    nestedReference: list[ReferenceUsage]
    nestedRendering: list[RenderingUsage]
    nestedRequirement: list[RequirementUsage]
    nestedState: list[StateUsage]
    nestedTransition: list[TransitionUsage]
    nestedUsage: list[Usage]
    nestedUseCase: list[UseCaseUsage]
    nestedVerificationCase: list[VerificationCaseUsage]
    nestedView: list[ViewUsage]
    nestedViewpoint: list[ViewpointUsage]
    occurrenceDefinition: list[Class]
    output: list[Feature]
    ownedAnnotation: list[Annotation]
    ownedConjugator: Conjugation | None
    ownedCrossSubsetting: CrossSubsetting | None
    ownedDifferencing: list[Differencing]
    ownedDisjoining: list[Disjoining]
    ownedElement: list[Element]
    ownedEndFeature: list[Feature]
    ownedFeature: list[Feature]
    ownedFeatureChaining: list[FeatureChaining]
    ownedFeatureInverting: list[FeatureInverting]
    ownedFeatureMembership: list[FeatureMembership]
    ownedImport: list[Import]
    ownedIntersecting: list[Intersecting]
    ownedMember: list[Element]
    ownedMembership: list[Membership]
    ownedRedefinition: list[Redefinition]
    ownedReferenceSubsetting: ReferenceSubsetting | None
    ownedRelationship: list[Relationship]
    ownedSpecialization: list[Specialization]
    ownedSubsetting: list[Subsetting]
    ownedTypeFeaturing: list[TypeFeaturing]
    ownedTyping: list[FeatureTyping]
    ownedUnioning: list[Unioning]
    owner: Element | None
    owningDefinition: Definition | None
    owningFeatureMembership: FeatureMembership | None
    owningMembership: OwningMembership | None
    owningNamespace: Namespace | None
    owningRelationship: Relationship | None
    owningType: Type | None
    owningUsage: Usage | None
    parameter: list[Feature]
    portionKind: str | None
    qualifiedName: str | None
    shortName: str | None
    textualRepresentation: list[TextualRepresentation]
    type: list[Type]
    unioningType: list[Type]
    untilArgument: Expression | None
    usage: list[Usage]
    variant: list[Usage]
    variantMembership: list[VariantMembership]
    whileArgument: Expression | None

REGISTRY: dict[str, type[Element]]
