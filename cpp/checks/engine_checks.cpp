// EngineBackend against a small engine written for the check (docs/engine-protocol.md): this program
// itself, started with --fake-engine. No Java and no engine install needed. Build and run from the
// repository root (add -static on MinGW):
//     g++ -std=c++17 -Wall -Werror -I cpp/include -I vendor cpp/checks/engine_checks.cpp -o cpp/out/engine_checks
//     cpp/out/engine_checks
#include <iostream>
#include <map>
#include <stdexcept>
#include <string>
#include <tuple>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/engine.hpp>

using namespace sysml;
using nlohmann::json;

static void check(bool cond, const std::string& what) {
    if (!cond) throw std::runtime_error("FAILED: " + what);
}

/// The fake engine: a package P owning a part definition A, answered from a table.
static int fake_engine() {
    const std::map<std::string, std::tuple<std::string, std::string, std::string>> elements{
        {"1", {"Package", "P", "P"}}, {"2", {"PartDefinition", "A", "P::A"}}};
    auto out = [](const json& j) { std::cout << j.dump() << std::endl; };
    out({{"ready", true}, {"engine", "Test engine"}, {"version", "1.0"}, {"protocol", 1}});
    for (std::string line; std::getline(std::cin, line);) {
        if (!line.empty() && line.back() == '\r') line.pop_back();
        json req = json::parse(line);
        json id = req["id"];
        const std::string cmd = req["cmd"];
        auto ok = [&](const json& v) { out({{"id", id}, {"ok", v}}); };
        auto err = [&](const std::string& kind, const std::string& message) {
            out({{"id", id}, {"error", {{"kind", kind}, {"message", message}}}});
        };
        const std::string h = req.value("h", "");
        if (cmd == "open") { ok({{"sources", req["sources"].size()}, {"elaborate", req["elaborate"]}}); continue; }
        if (cmd == "close") { ok(nullptr); return 0; }
        if (cmd == "allHandles") { ok(json::array({"1", "2"})); continue; }
        if (cmd == "resolve") {
            const std::string name = req["name"];
            ok(name == "P" ? json("1") : name == "P::A" ? json("2") : json(nullptr));
            continue;
        }
        auto it = elements.find(h);
        if (it == elements.end()) { err("gone", "no element " + h); continue; }
        const auto& [mc, name, qn] = it->second;
        if (cmd == "metaclass") { ok(mc); continue; }
        if (cmd == "elementId") { ok("id-" + h); continue; }
        if (cmd == "call") {
            if (req["op"] == "effectiveName") ok(name);
            else err("notApplicable", "no operation " + req["op"].get<std::string>());
            continue;
        }
        const std::string prop = req["prop"];
        if (prop == "declaredName") ok(name);
        else if (prop == "qualifiedName") ok(qn);
        else if (prop == "owner") ok(h == "2" ? json{{"@id", "1"}} : json(nullptr));
        else if (prop == "ownedMember") ok(h == "1" ? json::array({{{"@id", "2"}}}) : json::array());
        else if (prop == "isAbstract") err("refused", "isAbstract is not answered");
        else if (prop == "ownedRelationship") err("unresolved", "a reference did not resolve");
        else if (prop == "boom") err("failed", "the engine broke");
        else err("notApplicable", mc + " has no property " + prop);
    }
    return 0;
}

int main(int argc, char** argv) {
    if (argc > 1 && std::string(argv[1]) == "--fake-engine") return fake_engine();
    auto be = EngineBackend::start({argv[0], "--fake-engine"}, {}, {{"a.sysml", "package P { part def A; }"}});
    EngineBackend* raw = be.get();
    check(raw->engine() == "Test engine" && raw->version() == "1.0", "the engine announces itself");
    check(raw->stats().value("sources", 0) == 1 && raw->stats().value("elaborate", false), "open answers");
    Model m = Model::from_backend(std::move(be));
    auto a = m.resolve("P::A").value().as<PartDefinition>();
    check(a.getDeclaredName().value() == "A" && a.getQualifiedName().value() == "P::A", "reads");
    check(a.getOwner().value().is_a<Package>(), "references");
    check(a.getOwner().value().as<Package>().getOwnedMember().at(0) == a, "lists of references");
    check(a.effectiveName().value() == "A", "operations");
    check(m.all().size() == 2 && !m.resolve("Q"), "all and resolve");
    bool refused = false;
    try {
        (void)a.getIsAbstract();
    } catch (const not_answered& e) {
        refused = std::string(e.what()).rfind("Test engine: ", 0) == 0;
    }
    check(refused, "refused is not_answered");
    bool unresolved = false;
    try {
        (void)a.getOwnedRelationship();
    } catch (const unresolved_reference&) {
        unresolved = true;
    }
    check(unresolved, "unresolved is unresolved_reference");
    bool failed = false;
    try {
        (void)raw->get("2", "boom");
    } catch (const backend_error& e) {
        failed = std::string(e.what()).find("the engine broke") != std::string::npos;
    }
    check(failed, "failed is backend_error");
    check(raw->get("2", "noSuchProperty") == nullptr, "notApplicable reads as absent");
    bool is_gone = false;
    try {
        (void)raw->metaclass("9");
    } catch (const gone&) {
        is_gone = true;
    }
    check(is_gone, "gone is gone");
    bool missing = false;
    try {
        (void)EngineBackend::start({"no-such-engine-for-the-check"});
    } catch (const backend_error& e) {
        missing = std::string(e.what()).find("cannot start") != std::string::npos;
    }
    check(missing, "a missing engine is a backend_error");
    std::cout << "ALL C++ ENGINE CHECKS PASSED\n";
    return 0;
}
