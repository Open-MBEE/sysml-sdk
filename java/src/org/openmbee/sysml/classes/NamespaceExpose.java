// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — SysML 20250201. */
public interface NamespaceExpose extends Expose, NamespaceImport {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default Namespace getImportOwningNamespace() { return $ref($model().read(this, "importOwningNamespace")); }
    default org.openmbee.sysml.Element getImportedElement() { return $ref($model().read(this, "importedElement")); }
    default Namespace getImportedNamespace() { return $ref($model().read(this, "importedNamespace")); }
    default Boolean getIsImplied() { return $bool($model().read(this, "isImplied")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsImportAll() { return $bool($model().read(this, "isImportAll")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default Boolean getIsRecursive() { return $bool($model().read(this, "isRecursive")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedRelatedElement() { return $list($model().read(this, "ownedRelatedElement")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default org.openmbee.sysml.Element getOwningRelatedElement() { return $ref($model().read(this, "owningRelatedElement")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
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
    default java.util.List<Membership> importedMemberships(java.util.List<Namespace> excluded) { return $list($model().call(this, "importedMemberships", new Object[] {excluded})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
}
