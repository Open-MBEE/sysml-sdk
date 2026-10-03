package org.openmbee.sysml;

/**
 * Runtime core of every element. Spec properties live on the generated
 * interfaces (org.openmbee.sysml.classes) as default methods; identity and
 * escape hatches live here.
 *
 * Collision rule: runtime members are {@code $}-prefixed —
 * spec camelCase can never start with {@code $}, so a generated member
 * can never shadow a runtime one. tools/gen_classes_java.py enforces
 * the invariant at generation time.
 */
public interface Element {
    /** The element's stable id, via the backend's elementId(handle). */
    String $id();

    /** The backend handle this wrapper holds (= id for payload backends). */
    String $handle();

    String $metaclass();

    Model $model();

    /** Escape hatch: raw backend value, no wrapping. */
    Object $raw(String prop);

    // --- typed-getter converters (generated members call these) ---------
    // Lists are erased, so the blind cast is safe: the backend mints
    // wrappers by @type, and the table types match the mint (table 0.5.0).

    @SuppressWarnings("unchecked")
    default <T> java.util.List<T> $list(Object v) {
        return v == null ? java.util.List.of() : (java.util.List<T>) v;
    }

    @SuppressWarnings("unchecked")
    default <T> T $ref(Object v) {
        return (T) v;
    }

    default Boolean $bool(Object v) {
        return (Boolean) v;
    }

    default String $string(Object v) {
        return (String) v;
    }

    default Long $int(Object v) {
        return v == null ? null : ((Number) v).longValue();
    }

    default Double $real(Object v) {
        return v == null ? null : ((Number) v).doubleValue();
    }

    // --- the members of the metaclass Element, which every element has ------
    // Written by tools/gen_classes_java.py between the two marker lines.

    // BEGIN GENERATED
    default java.util.List<String> getAliasIds() { return $list($model().read(this, "aliasIds")); }
    default String getDeclaredName() { return $string($model().read(this, "declaredName")); }
    default String getDeclaredShortName() { return $string($model().read(this, "declaredShortName")); }
    default java.util.List<org.openmbee.sysml.classes.Documentation> getDocumentation() { return $list($model().read(this, "documentation")); }
    default String getElementId() { return $string($model().read(this, "elementId")); }
    default Boolean getIsImpliedIncluded() { return $bool($model().read(this, "isImpliedIncluded")); }
    default Boolean getIsLibraryElement() { return $bool($model().read(this, "isLibraryElement")); }
    default String getName() { return $string($model().read(this, "name")); }
    default java.util.List<org.openmbee.sysml.classes.Annotation> getOwnedAnnotation() { return $list($model().read(this, "ownedAnnotation")); }
    default java.util.List<org.openmbee.sysml.Element> getOwnedElement() { return $list($model().read(this, "ownedElement")); }
    default java.util.List<org.openmbee.sysml.classes.Relationship> getOwnedRelationship() { return $list($model().read(this, "ownedRelationship")); }
    default org.openmbee.sysml.Element getOwner() { return $ref($model().read(this, "owner")); }
    default org.openmbee.sysml.classes.OwningMembership getOwningMembership() { return $ref($model().read(this, "owningMembership")); }
    default org.openmbee.sysml.classes.Namespace getOwningNamespace() { return $ref($model().read(this, "owningNamespace")); }
    default org.openmbee.sysml.classes.Relationship getOwningRelationship() { return $ref($model().read(this, "owningRelationship")); }
    default String getQualifiedName() { return $string($model().read(this, "qualifiedName")); }
    default String getShortName() { return $string($model().read(this, "shortName")); }
    default java.util.List<org.openmbee.sysml.classes.TextualRepresentation> getTextualRepresentation() { return $list($model().read(this, "textualRepresentation")); }
    default String effectiveName() { return $string($model().call(this, "effectiveName", new Object[] {})); }
    default String effectiveShortName() { return $string($model().call(this, "effectiveShortName", new Object[] {})); }
    default String escapedName() { return $string($model().call(this, "escapedName", new Object[] {})); }
    default org.openmbee.sysml.classes.Namespace libraryNamespace() { return $ref($model().call(this, "libraryNamespace", new Object[] {})); }
    default String path() { return $string($model().call(this, "path", new Object[] {})); }
    // END GENERATED
}
