// Generated — do not edit. Source: metamodel.json via tools/gen_classes_java.py.
package org.openmbee.sysml.classes;

/** concrete — KerML 20250201. */
public interface LibraryPackage extends Package {
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default java.util.List<Expression> getFilterCondition() { return $list($model().read(this, "filterCondition")); }
    default java.util.List<Membership> getImportedMembership() { return $list($model().read(this, "importedMembership")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default Boolean getIsStandard() { return $bool($model().read(this, "isStandard")); }
    default java.util.List<org.openmbee.sysml.Element> getMember() { return $list($model().read(this, "member")); }
    default java.util.List<Membership> getMembership() { return $list($model().read(this, "membership")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<Import> getOwnedImport() { return $list($model().read(this, "ownedImport")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedMember() { return $list($model().read(this, "ownedMember")); }
    default java.util.List<Membership> getOwnedMembership() { return $list($model().read(this, "ownedMembership")); }
    default java.util.List<Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default java.util.List<Membership> importedMemberships(java.util.List<Namespace> excluded) { return $list($model().call(this, "importedMemberships", new Object[] {excluded})); }
    default Boolean includeAsMember(org.openmbee.sysml.Element element) { return $bool($model().call(this, "includeAsMember", new Object[] {element})); }
    default Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default java.util.List<Membership> membershipsOfVisibility(String visibility, java.util.List<Namespace> excluded) { return $list($model().call(this, "membershipsOfVisibility", new Object[] {visibility, excluded})); }
    default java.util.List<String> namesOf(org.openmbee.sysml.Element element) { return $list($model().call(this, "namesOf", new Object[] {element})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
    default String qualificationOf(String qualifiedName) { return $string($model().call(this, "qualificationOf", new Object[] {qualifiedName})); }
    default Membership resolve(String qualifiedName) { return $ref($model().call(this, "resolve", new Object[] {qualifiedName})); }
    default Membership resolveGlobal(String qualifiedName) { return $ref($model().call(this, "resolveGlobal", new Object[] {qualifiedName})); }
    default Membership resolveLocal(String name) { return $ref($model().call(this, "resolveLocal", new Object[] {name})); }
    default Membership resolveVisible(String name) { return $ref($model().call(this, "resolveVisible", new Object[] {name})); }
    default String unqualifiedNameOf(String qualifiedName) { return $string($model().call(this, "unqualifiedNameOf", new Object[] {qualifiedName})); }
    default String visibilityOf(Membership mem) { return $string($model().call(this, "visibilityOf", new Object[] {mem})); }
    default java.util.List<Membership> visibleMemberships(java.util.List<Namespace> excluded, Boolean isRecursive, Boolean includeAll) { return $list($model().call(this, "visibleMemberships", new Object[] {excluded, isRecursive, includeAll})); }
}
