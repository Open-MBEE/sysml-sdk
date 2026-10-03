// Generated — do not edit. Source: metamodel.json via tools/gen_classes_cpp.py.
// Included by sdk.hpp inside class Element: the members of the metaclass Element, which
// every element has. Defined in classes.g.hpp.
std::vector<std::string> getAliasIds() const;
std::optional<std::string> getDeclaredName() const;
std::optional<std::string> getDeclaredShortName() const;
std::vector<Documentation> getDocumentation() const;
std::optional<std::string> getElementId() const;
std::optional<bool> getIsImpliedIncluded() const;
std::optional<bool> getIsLibraryElement() const;
std::optional<std::string> getName() const;
std::vector<Annotation> getOwnedAnnotation() const;
std::vector<Element> getOwnedElement() const;
std::vector<Relationship> getOwnedRelationship() const;
std::optional<Element> getOwner() const;
std::optional<OwningMembership> getOwningMembership() const;
std::optional<Namespace> getOwningNamespace() const;
std::optional<Relationship> getOwningRelationship() const;
std::optional<std::string> getQualifiedName() const;
std::optional<std::string> getShortName() const;
std::vector<TextualRepresentation> getTextualRepresentation() const;
std::optional<std::string> effectiveName() const;
std::optional<std::string> effectiveShortName() const;
std::optional<std::string> escapedName() const;
std::optional<Namespace> libraryNamespace() const;
std::optional<std::string> path() const;
