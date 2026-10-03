// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — KerML 20250201. */
public interface ReturnParameterMembership extends ParameterMembership {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default Boolean getIsImplied() { return $bool($model().read(this, "isImplied")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default org.openmbee.sysml.Element getMemberElement() { return $ref($model().read(this, "memberElement")); }
    default String getMemberElementId() { return $string($model().read(this, "memberElementId")); }
    default String getMemberName() { return $string($model().read(this, "memberName")); }
    default String getMemberShortName() { return $string($model().read(this, "memberShortName")); }
    default Namespace getMembershipOwningNamespace() { return $ref($model().read(this, "membershipOwningNamespace")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default org.openmbee.sysml.Element getOwnedMemberElement() { return $ref($model().read(this, "ownedMemberElement")); }
    default String getOwnedMemberElementId() { return $string($model().read(this, "ownedMemberElementId")); }
    default Feature getOwnedMemberFeature() { return $ref($model().read(this, "ownedMemberFeature")); }
    default String getOwnedMemberName() { return $string($model().read(this, "ownedMemberName")); }
    default Feature getOwnedMemberParameter() { return $ref($model().read(this, "ownedMemberParameter")); }
    default String getOwnedMemberShortName() { return $string($model().read(this, "ownedMemberShortName")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedRelatedElement() { return $list($model().read(this, "ownedRelatedElement")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default org.openmbee.sysml.Element getOwningRelatedElement() { return $ref($model().read(this, "owningRelatedElement")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default Type getOwningType() { return $ref($model().read(this, "owningType")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default java.util.List<org.openmbee.sysml.Element> getRelatedElement() { return $list($model().read(this, "relatedElement")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<org.openmbee.sysml.Element> getSource() { return $list($model().read(this, "source")); }
    default java.util.List<org.openmbee.sysml.Element> getTarget() { return $list($model().read(this, "target")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default String getVisibility() { return $string($model().read(this, "visibility")); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default Boolean isDistinguishableFrom(Membership other) { return $bool($model().call(this, "isDistinguishableFrom", new Object[] {other})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default String parameterDirection() { return $string($model().call(this, "parameterDirection", new Object[] {})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
}
