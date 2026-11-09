// C++ reading through the SysML v2 Pilot Implementation. Build and run from the repository root
// (add -static on MinGW; -ldl on Linux), in a Java process:
//     g++ -std=c++17 -Wall -Werror -I cpp/include -I vendor cpp/checks/pilot_checks.cpp -o cpp/out/pilot_checks
// or in this process, through JNI:
//     g++ -std=c++17 -Wall -Werror -DSYSML_PILOT_JNI -I cpp/include -I vendor -I "$JAVA_HOME/include"
//         -I "$JAVA_HOME/include/<win32|linux|darwin>" cpp/checks/pilot_checks.cpp -o cpp/out/pilot_checks_jni
// Exits 0 with a note when no Pilot is at hand (SYSML_PILOT_HOME unset and none in the user's
// cache) or no Pilot engine (SYSML_PILOT_SERVER unset and none beside the executable), so the other
// checks run everywhere without downloading the Pilot; with SYSML_REQUIRE_PILOT=1 nothing is a
// reason to skip.
#include <cstdlib>
#include <filesystem>
#include <iostream>
#include <stdexcept>
#include <string>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/library.hpp>
#include <sysml/pilot.hpp>

using namespace sysml;
namespace fs = std::filesystem;

static void check(bool cond, const std::string& what) {
    if (!cond) throw std::runtime_error("FAILED: " + what);
}

static std::string why_not() {
    if (detail::engine_env("SYSML_REQUIRE_PILOT") == "1") return "";
    try {
        detail::pilot_server_jar("");
    } catch (const backend_error& e) {
        return e.what();
    }
    if (!detail::engine_env("SYSML_PILOT_HOME").empty()) return "";
    std::error_code ec;
    fs::path cache = detail::library_cache_root();
    for (const auto& d : fs::directory_iterator(cache, ec))
        if (d.path().filename().string().rfind("pilot-", 0) == 0 && fs::exists(d.path() / ".complete")) return "";
    return "no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)";
}

int main() {
    if (std::string why = why_not(); !why.empty()) {
        std::cout << "Pilot checks skipped: " << why << "\n";
        return 0;
    }
    PilotOptions options;
    options.heap = "2g";
    auto be = PilotBackend::open_sources({
        {"demo.sysml", "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }"},
        {"größe.sysml", "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }"},
        {"bad.sysml", "package Bad { part p : Missing; }"},
    }, options);
    PilotBackend* raw = be.get();
    std::cout << "checking with the Pilot " << raw->pilot_version() << "\n";
    check(raw->stats().value("pilot", "") == raw->pilot_version(), "stats name the Pilot's version");
    Model m = Model::from_backend(std::move(be));

    Element car = m.resolve("Demo::Car").value();
    auto car_def = car.as<PartDefinition>();
    check(car_def.getQualifiedName().value() == "Demo::Car", "qualifiedName");
    check(car_def.getOwner().value().is_a<Package>(), "owner is the package");
    auto feats = car_def.getOwnedFeature();
    check(feats.size() == 2 && feats[0].getDeclaredName().value() == "wheels" && feats[0].is_a<PartUsage>(), "ownedFeature");
    auto types = feats[0].getType();
    check(types.size() == 1 && types[0].getDeclaredName().value() == "Wheel", "type");
    check(feats[0].getIsComposite().value() == true, "isComposite");
    auto sports = m.resolve("Demo::Sports").value().as<PartDefinition>();
    bool wheels = false;
    for (const Feature& f : sports.getInheritedFeature()) wheels = wheels || f.getDeclaredName() == std::optional<std::string>("wheels");
    check(wheels, "inheritedFeature includes Car's wheels");
    check(car == m.resolve("Demo::Car").value(), "identity by elementId");
    check(car.element_id().size() == 36, "elementId is a UUID");
    check(!m.resolve("Demo::Nothing"), "an unknown name resolves to nothing");
    check(m.resolve("ScalarValues::Real").has_value(), "library names resolve");

    check(car_def.effectiveName().value() == "Car", "effectiveName");
    check(sports.specializes(car_def).value() == true, "specializes");
    check(sports.isCompatibleWith(car_def).value() == true, "isCompatibleWith");
    bool membership = false;
    for (const Membership& ms : sports.inheritedMemberships({}, {}, false))
        membership = membership || ms.getMemberName() == std::optional<std::string>("wheels");
    check(membership, "inheritedMemberships");
    std::optional<ConnectionUsage> connection;
    for (const Feature& f : m.resolve("Demo::Link").value().as<PartDefinition>().getOwnedFeature())
        if (f.is_a<ConnectionUsage>()) connection = f.as<ConnectionUsage>();
    Feature end = connection.value().getConnectorEnd().at(0);
    check(!end.getDeclaredName() && end.effectiveName().value() == "source",
          "an unnamed connection end is named by the end it redefines");

    check(m.resolve("Sizes").value().getDocumentation().at(0).getBody().value_or("").find("Größe und Wärme") != std::string::npos,
          "text beyond ASCII comes back intact");
    std::vector<std::string> text = m.resolve("Sizes::Light").value().as<RequirementDefinition>().getText();
    check(text.size() == 1 && text[0].rfind("Keep it light.", 0) == 0, "a requirement's text is a list of strings");
    bool found = false;
    for (const Element& root : m.roots())
        for (const Element& member : root.as<Namespace>().getOwnedMember())
            found = found || member.getName() == std::optional<std::string>("Sizes");
    check(found, "roots");

    Element p = m.resolve("Bad::p").value();
    bool unresolved = false;
    try {
        (void)p.as<PartUsage>().getOwnedTyping().at(0).getType();
    } catch (const unresolved_reference&) {
        unresolved = true;
    }
    check(unresolved, "an unresolved reference throws unresolved_reference");
    bool is_gone = false;
    try {
        (void)raw->metaclass("999999999");
    } catch (const gone&) {
        is_gone = true;
    }
    check(is_gone, "an unknown handle is gone");
    check(raw->get(p.backend_handle(), "noSuchProperty") == nullptr, "a property the metaclass lacks reads as absent");
    bool refused = false;
    try {
        (void)raw->call(p.backend_handle(),"noSuchOperation", nlohmann::json::array());
    } catch (const backend_error& e) {
        refused = std::string(e.what()).find("noSuchOperation") != std::string::npos;
    }
    check(refused, "an operation the Pilot does not have throws backend_error");

    bool missing = false;
    try {
        PilotOptions elsewhere = options;
        elsewhere.pilot_home = fs::temp_directory_path().string();
        elsewhere.transport = "process";
        (void)PilotBackend::open_sources({{"a.sysml", "package A;"}}, elsewhere);
    } catch (const backend_error& e) {
        missing = std::string(e.what()).find("holds no Pilot") != std::string::npos;
    }
    check(missing, "a missing Pilot throws backend_error");
    std::cout << "ALL C++ PILOT CHECKS PASSED\n";
    return 0;
}
