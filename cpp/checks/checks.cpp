// Smoke test + conformance runner for the C++ SDK backend.
// Mirrors ../../demo.py; any failure throws and exits non-zero.

#include <filesystem>
#include <fstream>
#include <iostream>
#include <sstream>
#include <stdexcept>
#include <string>

#include <sysml/classes.g.hpp>

namespace checks {
void run_conformance(const std::string& metamodel_path);
}

namespace {

using namespace sysml;
namespace fs = std::filesystem;

void check(bool cond, const std::string& what) {
    if (!cond)
        throw std::runtime_error("FAILED: " + what);
}

fs::path find_repo_root() {
    fs::path d = fs::current_path();
    while (true) {
        if (fs::exists(d / "metamodel.json"))
            return d;
        if (d == d.root_path())
            throw std::runtime_error("metamodel.json not found above cwd");
        d = d.parent_path();
    }
}

std::string read_file(const fs::path& p) {
    std::ifstream in(p, std::ios::binary);
    check(in.good(), "cannot open " + p.string());
    std::ostringstream buf;
    buf << in.rdbuf();
    return buf.str();
}

Feature find_feature(const std::vector<Feature>& feats, const std::string& name) {
    for (const Feature& f : feats)
        if (f.getDeclaredName() == name)
            return f;
    throw std::runtime_error("FAILED: no feature named " + name);
}

void run_smoke(const std::string& payload) {
    Model m = Model::from_full_json(payload);

    // --- typed resolution and hierarchy -----------------------------------
    Element vehicle = m.resolve("Demo::Vehicle").value();
    std::cout << "resolve: " << vehicle.to_string() << "\n";
    check(vehicle.is_a<PartDefinition>(), "PartDefinition");
    check(vehicle.is_a<Definition>() && vehicle.is_a<Classifier>() &&
              vehicle.is_a<Type>() && vehicle.is_a<Namespace>(),
          "hierarchy chain");
    check(!vehicle.is_a<Feature>(), "not Feature");
    std::cout << "is_a chain: PartDefinition < Definition < Classifier < Type "
                 "< Namespace OK\n";

    // wrong-kind cast throws
    try {
        (void)vehicle.as<Feature>();
        check(false, "expected wrong_kind");
    } catch (const wrong_kind&) {
        std::cout << "as<Feature>() on a PartDefinition -> wrong_kind (as designed)\n";
    }

    // --- navigation (typed getters, table 0.5.0) ---------------------------
    auto pd = vehicle.as<PartDefinition>();
    const std::vector<Feature> feats = pd.getOwnedFeature();
    std::cout << "ownedFeature:";
    for (const Feature& f : feats)
        std::cout << " " << f.metaclass_name() << ":"
                  << f.getDeclaredName().value_or("?");
    std::cout << "\n";
    check(feats.size() == 7, "7 features");
    Feature wheel = find_feature(feats, "wheel");
    check(wheel.metaclass_name() == "PartUsage", "wheel is PartUsage");
    check(wheel.getOwner().value() == vehicle, "owner identity");
    check(wheel.getIsComposite().value(), "isComposite");
    check(pd.getQualifiedName() == "Demo::Vehicle", "qualifiedName");

    Element base_def = m.resolve("Demo::Base").value();
    const std::vector<Subclassification> sub = pd.getOwnedSubclassification();
    check(sub.size() == 1 && sub[0].getSuperclassifier().value() == base_def,
          "ownedSubclassification -> Base");

    auto v_usage = m.resolve("Demo::v").value().as<Usage>();
    const std::vector<Classifier> defs = v_usage.getDefinition();
    check(defs.size() == 1 && defs[0] == vehicle, "v.definition");
    std::cout << "v.definition -> Vehicle OK\n";

    // --- reads pass the backend value through unchanged ---------------------
    // (no client-side check: a member the SysML Toolkit does not implement says so itself)
    check(pd.getOwnedPart().empty(), "ownedPart == []");
    check(vehicle.as<Type>().getInheritedFeature().empty(), "inheritedFeature == []");
    std::cout << "unimplemented derivations -> [] (backend value, no "
                 "second-guessing)\n";

    // --- operation stubs are honest ----------------------------------------
    try {
        (void)vehicle.as<Type>().specializes(base_def.as<Type>());
        check(false, "expected not_implemented_in_toolkit");
    } catch (const not_implemented_in_toolkit&) {
        std::cout << "Type.specializes() -> not_implemented_in_toolkit (as designed)\n";
    }

    // (read-only guard is compile-time in C++: no setters are generated)

    // --- model-wide queries -------------------------------------------------
    int parts = 0, usages = 0;
    for (const Element& el : m.all()) {
        if (el.is_a<PartUsage>())
            parts++;
        if (el.is_a<Usage>())
            usages++;
    }
    std::cout << "PartUsage: " << parts << ", all Usage subtypes: " << usages
              << "\n";
    check(usages > parts, "subtype counts");

    Element calc = m.resolve("Demo::Vehicle::K").value();
    check(calc.is_a<CalculationDefinition>(), "K is CalculationDefinition");
    const std::vector<Feature> inputs = calc.as<CalculationDefinition>().getInput();
    check(inputs.size() == 1 && inputs[0].getDeclaredName() == "x", "K.input");
    std::cout << "K.input -> [x] OK\n";

    // --- collision rule: MetadataUsage.metaclass is the SPEC member --------
    const std::string meta_payload = R"json([
      {"@id": "mc1", "@type": "Metaclass", "declaredName": "Safety",
       "qualifiedName": "M::Safety"},
      {"@id": "m1", "@type": "MetadataUsage", "qualifiedName": "M::stubbed",
       "metaclass": null},
      {"@id": "m2", "@type": "MetadataUsage", "qualifiedName": "M::filled",
       "metaclass": {"@id": "mc1"}}
    ])json";
    Model mm = Model::from_full_json(meta_payload);
    auto mu = mm.element("m1").as<MetadataUsage>();
    check(mu.metaclass_name() == "MetadataUsage", "runtime accessor (snake_case)");
    check(!mu.getMetaclass().has_value(), "spec member: backend value passed through");
    check(mm.element("m2").as<MetadataUsage>().getMetaclass().value() ==
              mm.element("mc1"),
          "filled metaclass re-check");
    std::cout << "MetadataUsage: .metaclass_name() (runtime) vs .getMetaclass() "
                 "(spec) OK\n";

    // Elements come in document order; an id given twice keeps its first place
    // and its last element.
    Model ordered = Model::from_full_json(R"json([
      {"@id": "z", "@type": "Package", "declaredName": "first"},
      {"@id": "a", "@type": "Package", "declaredName": "A"},
      {"@id": "z", "@type": "Package", "declaredName": "second"}
    ])json");
    std::vector<std::string> ids;
    for (const Element& e : ordered.all())
        ids.push_back(e.element_id());
    check(ids == std::vector<std::string>{"z", "a"}, "document order");
    check(ordered.element("z").getDeclaredName() == std::optional<std::string>("second"),
          "the last element of an id");
    std::cout << "payload elements in document order OK\n";
}

template <typename Read>
bool throws_unresolved(Read read) {
    try {
        read();
    } catch (const unresolved_reference&) {
        return true;
    }
    return false;
}

std::vector<std::string> ids_of(const std::vector<Element>& elements) {
    std::vector<std::string> ids;
    for (const Element& e : elements)
        ids.push_back(e.element_id());
    return ids;
}

// A payload model with a library: references the model does not hold resolve in the library,
// the library's elements are not the model's, the model wins an id or name both have, and a
// reference in neither still throws. The same checks run in every language.
void check_payload_library() {
    auto library = PayloadLibrary::parse(R"json([
      {"@id": "lib", "@type": "LibraryPackage", "qualifiedName": "ScalarValues", "isLibraryElement": true},
      {"@id": "real", "@type": "DataType", "qualifiedName": "ScalarValues::Real", "declaredName": "Real",
       "owner": {"@id": "lib"}, "isLibraryElement": true},
      {"@id": "clash", "@type": "Package", "declaredName": "from the library"},
      {"@id": "libP", "@type": "Package", "qualifiedName": "P"}
    ])json");
    const std::string model = R"json([
      {"@id": "pkg", "@type": "Package", "qualifiedName": "P", "declaredName": "P"},
      {"@id": "mass", "@type": "AttributeUsage", "qualifiedName": "P::mass", "owner": {"@id": "pkg"},
       "type": [{"@id": "real"}], "definition": [{"@id": "gone"}]},
      {"@id": "clash", "@type": "Package", "declaredName": "from the model"}
    ])json";
    Model with_library = Model::from_full_json(model, library);
    AttributeUsage mass = with_library.element("mass").as<AttributeUsage>();
    Type real = mass.getType().at(0);
    check(real.getDeclaredName() == std::optional<std::string>("Real") && real.getIsLibraryElement() == true,
          "library element");
    check(real.getOwner()->getQualifiedName() == std::optional<std::string>("ScalarValues"), "library owner");
    check(with_library.resolve("ScalarValues::Real") == std::optional<Element>(real), "resolve into the library");
    check(ids_of(with_library.all()) == std::vector<std::string>{"pkg", "mass", "clash"}, "all() lists the library");
    check(ids_of(with_library.roots()) == std::vector<std::string>{"pkg", "clash"}, "roots() lists the library");
    check(with_library.element("clash").getDeclaredName() == std::optional<std::string>("from the model"),
          "the model's id wins");
    check(with_library.resolve("P")->element_id() == "pkg", "the model's name wins");
    check(throws_unresolved([&] { (void)mass.getDefinition(); }), "a reference in neither");
    Model without_library = Model::from_full_json(model);
    check(throws_unresolved([&] { (void)without_library.element("mass").as<AttributeUsage>().getType(); }),
          "no library, no resolution");
    check(!without_library.resolve("ScalarValues::Real"), "no library, no name");
    std::cout << "payload model with a library OK\n";
}

}  // namespace

int main() {
    try {
        const fs::path root = find_repo_root();
        checks::run_conformance((root / "metamodel.json").string());
        run_smoke(read_file(root / "data" / "bigger.full.json"));
        check_payload_library();
        std::cout << "\nALL C++-BACKEND SMOKE TESTS PASSED\n";
        return 0;
    } catch (const std::exception& e) {
        std::cerr << e.what() << "\n";
        return 1;
    }
}
