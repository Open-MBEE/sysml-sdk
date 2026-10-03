// C++ reading directly from the SysML Toolkit through the binding library, loaded at run time.
// Build and run from the repository root (add -static on MinGW):
//     g++ -std=c++17 -Wall -Werror -I cpp/include -I vendor cpp/checks/toolkit_checks.cpp -o cpp/out/toolkit_checks
//     cpp/out/toolkit_checks
// Exits 0 with a note when the library is not built, so payload checks still run everywhere; with
// SYSML_REQUIRE_TOOLKIT=1 a missing library is a failure instead.
#include <cstdlib>
#include <functional>
#include <iostream>
#include <stdexcept>
#include <string>
#include <unordered_set>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/toolkit.hpp>

using namespace sysml;

static void check(bool cond, const char* what) {
    if (!cond) throw std::runtime_error(std::string("FAILED: ") + what);
}

/// The member a SysML Toolkit refusal names (`Type::inheritedMemberships()` gives
/// `inheritedMemberships()`).
static std::string refused_member(const std::string& message) {
    const auto i = message.find(" is not answered by the SysML Toolkit");
    if (i == std::string::npos) return "";
    std::string head = message.substr(0, i);
    const auto j = head.rfind("::");
    return j == std::string::npos ? head : head.substr(j + 2);
}

/// A check that meets the SysML Toolkit's refusal of `member` first (not_implemented_in_toolkit
/// naming it): an expected failure, reported as one. It fails when it passes (the SysML Toolkit
/// answers now: remove the expectation) or when it stops on anything else.
static void expect_refusal(const std::string& member, const std::string& reason, const std::string& what,
                           const std::function<void()>& body) {
    try {
        body();
    } catch (const not_implemented_in_toolkit& e) {
        if (refused_member(e.what()) == member) {
            std::cout << "expected failure: " << what << ": the SysML Toolkit refuses " << member << " (" << reason << ")\n";
            return;
        }
        throw;
    }
    throw std::runtime_error("FAILED: " + what + ": the SysML Toolkit answers " + member + " now; remove the expected refusal");
}

int main() {
    std::string path;
    try {
        path = ToolkitBackend::Library::default_path();
    } catch (const sdk_error& e) {
        const char* require = std::getenv("SYSML_REQUIRE_TOOLKIT");
        if (require && std::string(require) == "1") {
            std::cerr << "SYSML_REQUIRE_TOOLKIT=1 but " << e.what() << "\n";
            return 1;
        }
        std::cout << "SysML Toolkit checks skipped: binding library not built\n";
        return 0;
    }
    const std::string source =
        "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }";
    auto be = ToolkitBackend::open_sources({{"inline.sysml", source}});
    ToolkitBackend* raw = be.get();
    Model m = Model::from_backend(std::move(be));

    Element car = m.resolve("Demo::Car").value();
    check(car.is_a<PartDefinition>(), "Car is a PartDefinition");
    auto car_def = car.as<PartDefinition>();
    check(car_def.getQualifiedName().value() == "Demo::Car", "qualifiedName");
    check(car_def.getOwner().value().is_a<Package>(), "owner is the package");
    auto feats = car_def.getOwnedFeature();
    check(feats.size() == 2 && feats[0].getDeclaredName().value() == "wheels", "ownedFeature");
    check(feats[0].is_a<PartUsage>(), "wheels is a PartUsage");
    expect_refusal("type", "it waits on implied relationships", "type", [&] {
        auto types = feats[0].getType();
        check(types.size() == 1 && types[0].getDeclaredName().value() == "Wheel", "type");
    });
    check(feats[0].getIsComposite().value() == true, "isComposite");
    auto sports = m.resolve("Demo::Sports").value().as<PartDefinition>();
    expect_refusal("inheritedFeature", "it waits on implied relationships", "inheritedFeature",
                   [&] { check(sports.getInheritedFeature().size() == 2, "inheritedFeature computed by the SysML Toolkit"); });
    check(car == m.resolve("Demo::Car").value(), "identity by elementId");
    check(car.element_id().size() == 36, "elementId is a UUID");
    // Operations, through the SysML Toolkit's operation entry point; it refuses what it cannot
    // answer as specified.
    check(car_def.effectiveName().value() == "Car", "effectiveName");
    expect_refusal("connectorEnd", "it waits on the implied end redefinitions", "an unnamed connection end is named by the end it redefines", [&] {
        std::optional<ConnectionUsage> connection;
        for (const Feature& f : m.resolve("Demo::Link").value().as<PartDefinition>().getOwnedFeature())
            if (f.is_a<ConnectionUsage>()) connection = f.as<ConnectionUsage>();
        Feature end = connection.value().getConnectorEnd().at(0);
        check(!end.getDeclaredName() && end.effectiveName().value() == "source",
              "an unnamed connection end is named by the end it redefines");
    });
    expect_refusal("inheritedMemberships()", "its evidence is incomplete", "inheritedMemberships", [&] {
        auto memberships = sports.inheritedMemberships({}, {}, false);
        check(memberships.size() == 2 && memberships[0].getMemberName().value() == "wheels", "inheritedMemberships");
    });
    bool op_refused = false;
    try {
        (void)sports.isCompatibleWith(car_def);
    } catch (const not_implemented_in_toolkit& e) {
        op_refused = std::string(e.what()).find("isCompatibleWith") != std::string::npos;
    }
    check(op_refused, "an operation the SysML Toolkit cannot answer as specified is refused");
    check(raw->full_json().rfind("[", 0) == 0, "full_json");

    // Text beyond ASCII, a requirement's text (a list of strings), the members every element has,
    // the top-level namespaces, the elements of a metaclass, and elements as keys.
    Model sizes = Model::from_backend(ToolkitBackend::open_sources({{"größe.sysml",
        "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }"}}));
    check(sizes.resolve("Sizes::Last").value().is_a<PartDefinition>(), "a source with text beyond ASCII loads whole");
    check(sizes.resolve("Sizes").value().getDocumentation().at(0).getBody().value_or("").find("Größe und Wärme") != std::string::npos,
          "text beyond ASCII comes back intact");
    auto light = sizes.resolve("Sizes::Light").value().as<RequirementDefinition>();
    std::vector<std::string> text = light.getText();
    check(text.size() == 1 && text[0].rfind("Keep it light.", 0) == 0, "a requirement's text is a list of strings");
    Element owner = light.getOwner().value();
    check(owner.getName() == std::optional<std::string>("Sizes"), "the members of Element, read on a plain Element");
    bool found = false;
    for (const Element& root : sizes.roots())
        for (const Element& member : root.as<Namespace>().getOwnedMember())
            found = found || member.getName() == std::optional<std::string>("Sizes");
    check(found, "roots");
    check(sizes.elements_of_type<PartDefinition>().size() == 1, "elements_of_type");
    std::unordered_set<Element> keys{owner, sizes.resolve("Sizes").value()};
    check(keys.size() == 1, "elements hash as they compare");
    std::cout << "ALL C++ SYSML TOOLKIT CHECKS PASSED\n";
    return 0;
}
