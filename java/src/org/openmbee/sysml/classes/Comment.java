// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — KerML 20250201. */
public interface Comment extends AnnotatingElement {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default java.util.List<org.openmbee.sysml.Element> getAnnotatedElement() { return $list($model().read(this, "annotatedElement")); }
    default java.util.List<Annotation> getAnnotation() { return $list($model().read(this, "annotation")); }
    default String getBody() { return $string($model().read(this, "body")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default String getLocale() { return $string($model().read(this, "locale")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<Annotation> getOwnedAnnotatingRelationship() { return $list($model().read(this, "ownedAnnotatingRelationship")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default Annotation getOwningAnnotatingRelationship() { return $ref($model().read(this, "owningAnnotatingRelationship")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
}
