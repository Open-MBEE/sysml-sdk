// The convenience members through C++, on the SysML Toolkit and on the Pilot Implementation (in a Java
// process): every argument shape and every result shape the SDK has for them, called on the examples'
// models (convenience.json) and compared with the examples' answers. The other languages run every
// example; the bodies are the same binding library's and Pilot engine's, so C++ checks what it adds,
// the marshalling. Build and run from the repository root (add -static on MinGW):
//     g++ -std=c++17 -Wall -Werror -I cpp/include -I vendor cpp/checks/convenience_checks.cpp -o cpp/out/convenience_checks
//     cpp/out/convenience_checks
// Each backend is skipped with a note when it is not at hand (the binding library and the release
// library; a Pilot and its engine, as cpp/checks/pilot_checks.cpp decides); SYSML_REQUIRE_TOOLKIT=1
// makes the SysML Toolkit's skip a failure, SYSML_REQUIRE_PILOT=1 the Pilot's.
#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <sstream>
#include <stdexcept>
#include <string>
#include <vector>

#include <sysml/sdk.hpp>
#include <sysml/classes.g.hpp>
#include <sysml/library.hpp>
#include <sysml/pilot.hpp>
#include <sysml/toolkit.hpp>

using namespace sysml;
namespace fs = std::filesystem;

static void check(bool cond, const std::string& what) {
    if (!cond) throw std::runtime_error("FAILED: " + what);
}

template <class F>
static bool throws_refused(F&& f) {
    try {
        f();
    } catch (const refused&) {
        return true;
    }
    return false;
}

/// A call's answer as qualified names (or strings), or a marker of how it refused.
template <class F>
static std::vector<std::string> outcome(F&& f) {
    try {
        return f();
    } catch (const refused&) {
        return {"<refused>"};
    } catch (const not_answered&) {
        return {"<not answered>"};
    }
}

static std::string name_of(const Element& e) { return e.getQualifiedName().value_or("<" + e.metaclass_name() + ">"); }

template <class Xs>
static std::vector<std::string> names_of(const Xs& xs) {
    std::vector<std::string> out;
    for (const auto& x : xs) out.push_back(name_of(x));
    return out;
}

static int lists_answered = 0, lists_refused = 0;

/// A member over a list against the member on each element: list results concatenated, absent single
/// results dropped; or both refuse alike. `call` gives the answer's names, on the list or on an element.
template <class L, class Call>
static void same_as_each(const std::string& what, const L& list, Call call) {
    auto from_list = outcome([&] { return call(list); });
    auto from_each = outcome([&] {
        std::vector<std::string> out;
        for (const auto& x : list)
            for (const auto& y : call(x)) out.push_back(y);
        return out;
    });
    check(from_list == from_each, what + ": over the list as over each element");
    ++(from_list.size() == 1 && from_list[0].front() == '<' && from_list[0].back() == '>' ? lists_refused : lists_answered);
}

/// The members over lists (ElementList), one of each shape: a list result with scope, visibility and
/// library; a lookup by name; a definition list; an element argument; an Element result; strings;
/// members with two contexts; Element's own.
static void check_lists(Model& m) {
    lists_answered = lists_refused = 0;
    auto at = [&](const char* qn) {
        auto e = m.resolve(qn);
        check(e.has_value(), std::string(qn) + " resolves");
        return *e;
    };
    auto as_names = [](const auto& v) { return names_of(v); };
    auto opt = [](const auto& o) {
        std::vector<std::string> out;
        if (o) out.push_back(name_of(*o));
        return out;
    };
    // A chain: the part definitions of a package, then their parts.
    auto defs = at("DefinitionLists::Imports").as<Package>().find_part_defs(Scope::All);
    check(!defs.empty(), "find_part_defs gives an ElementList");
    same_as_each("find_parts", defs, [&](const auto& x) { return names_of(x.find_parts(Scope::All)); });
    ElementList<PartDefinition> frames(std::vector<PartDefinition>{at("Typing::Frame").as<PartDefinition>(),
                                                                    at("Typing::Body").as<PartDefinition>(),
                                                                    at("Typing::Truck").as<PartDefinition>()});
    same_as_each("find_part (by name)", frames, [&](const auto& x) {
        if constexpr (std::is_base_of_v<std::vector<PartDefinition>, std::decay_t<decltype(x)>>) return as_names(x.find_part("wheels", Scope::All));
        else return opt(x.find_part("wheels", Scope::All));
    });
    same_as_each("find_part_defs (a definition list)", ElementList<Package>(std::vector<Package>{at("DefinitionLists::Imports").as<Package>()}),
                 [&](const auto& x) { return names_of(x.find_part_defs(Scope::All)); });
    auto mass = at("Typing::Frame::mass").as<Feature>();
    same_as_each("find_effective_feature (an element argument)", frames, [&](const auto& x) {
        if constexpr (std::is_base_of_v<std::vector<PartDefinition>, std::decay_t<decltype(x)>>) return as_names(x.find_effective_feature(mass));
        else return opt(x.find_effective_feature(mass));
    });
    // An Element result: the units of value expressions.
    auto value_of = [&](const char* feature) {
        for (const auto& mem : at(feature).as<Feature>().getOwnedMembership())
            if (mem.is_kind("FeatureValue")) return *mem.as<FeatureValue>().getValue();
        throw std::runtime_error(std::string("FAILED: ") + feature + " has a value");
    };
    ElementList<Expression> values(std::vector<Expression>{value_of("Values::Settings::mass"), value_of("Values::Settings::drag")});
    same_as_each("find_unit (an Element result)", values, [&](const auto& x) {
        if constexpr (std::is_base_of_v<std::vector<Expression>, std::decay_t<decltype(x)>>) return as_names(x.find_unit());
        else return opt(x.find_unit());
    });
    // Strings, and Element's own members.
    ElementList<Element> cars(std::vector<Element>{at("Structure::Car"), at("Structure::Truck"), at("Structure::Brake")});
    same_as_each("find_doc_text (strings)", cars, [&](const auto& x) { return x.find_doc_text(); });
    same_as_each("find_applied_metadata (Element's)", cars, [&](const auto& x) { return names_of(x.find_applied_metadata()); });
    // Two contexts: Behavior and Step, StateDefinition and StateUsage.
    same_as_each("find_parameters (a behavior)", ElementList<ActionDefinition>(std::vector<ActionDefinition>{at("Behaviour::Pump").as<ActionDefinition>()}),
                 [&](const auto& x) { return names_of(x.find_parameters()); });
    same_as_each("find_parameters (a step)", ElementList<ActionUsage>(std::vector<ActionUsage>{at("Behaviour::pump").as<ActionUsage>()}),
                 [&](const auto& x) { return names_of(x.find_parameters()); });
    same_as_each("find_initial_states (state definitions)",
                 ElementList<StateDefinition>(std::vector<StateDefinition>{at("Behaviour::Machine").as<StateDefinition>(),
                                                                           at("Behaviour::Machine2").as<StateDefinition>()}),
                 [&](const auto& x) { return names_of(x.find_initial_states()); });
    same_as_each("find_initial_states (a state usage)", ElementList<StateUsage>(std::vector<StateUsage>{at("Behaviour::lamp").as<StateUsage>()}),
                 [&](const auto& x) { return names_of(x.find_initial_states()); });
    std::cout << "convenience lists (C++): " << lists_answered << " answered alike, " << lists_refused << " refused alike\n";
}

/// Model::find (whatever the visibility; through inheritance when asked; quoted names) and
/// Model::resolve from a context (the specification's Namespace::resolve, which the SysML Toolkit
/// refuses in places).
static void check_find(Model& m) {
    auto qn = [](const std::optional<Element>& e) { return e ? e->getQualifiedName().value_or("") : std::string("<none>"); };
    check(qn(m.find("Scopes::Base::b")) == "Scopes::Base::b", "find Base::b");
    check(qn(m.find("Scopes::Base::c")) == "Scopes::Base::c", "find Base::c");
    check(!m.find("Scopes::Derived::b"), "find Derived::b: none");
    auto plug = m.find("Typing::Plug::'~Plug'");
    check(plug && plug->metaclass_name() == "ConjugatedPortDefinition", "find '~Plug'");
    try {
        check(qn(m.find("Scopes::Derived::b", true)) == "Scopes::Base::b", "find Derived::b, inherited");
        check(!m.find("Scopes::Derived::c", true), "find Derived::c, inherited: none");
    } catch (const not_answered&) {
        // the backend does not answer inheritedMembership here
    }
    try {
        check(qn(m.resolve("b", *m.find("Scopes::Base"))) == "Scopes::Base::b", "resolve b from Base");
        check(!m.resolve("Base::b", *m.find("Scopes::Derived")), "resolve Base::b from Derived: none");
    } catch (const not_answered&) {
        // the SysML Toolkit refuses Namespace::resolve in places
    }
    check(Model::split_qualified_name("A::'x\\'y'::B")[1].second == "x'y", "split a quoted name");
}

/// Every shape on `m`, the examples' models; both backends answer each of these alike.
static void check_shapes(Model& m) {
    check_lists(m);
    check_find(m);
    auto at = [&](const char* qn) {
        auto e = m.resolve(qn);
        check(e.has_value(), std::string(qn) + " resolves");
        return *e;
    };
    auto qn = [](const Element& e) { return e.getQualifiedName().value_or(""); };
    auto names = [&](const auto& list) {
        std::vector<std::string> out;
        for (const auto& e : list) out.push_back(qn(e));
        return out;
    };
    // The value expression of a feature (ownedMembership[FeatureValue].value).
    auto value_of = [&](const char* feature) {
        for (const auto& mem : at(feature).as<Feature>().getOwnedMembership())
            if (mem.is_kind("FeatureValue")) return *mem.as<FeatureValue>().getValue();
        throw std::runtime_error(std::string("FAILED: ") + feature + " has a value");
    };
    using V = std::vector<std::string>;

    // (scope, visibility, library) -> elements
    auto derived = at("Scopes::Derived").as<PartDefinition>();
    check(names(derived.find_parts()) == V{"Scopes::Derived::d", "Scopes::Derived::a", "Scopes::Derived::e"}, "find_parts()");
    // the inherited scope keeps what the type specializes the owner of
    check(names(derived.find_parts(Scope::Inherited)) == V{"Scopes::Base::b"}, "find_parts(Scope::Inherited)");
    check(names(derived.find_parts(Scope::All, VisibilityFilter::Public)) == V{"Scopes::Derived::d", "Scopes::Derived::a"},
          "find_parts(Scope::All, VisibilityFilter::Public)");
    // (name, scope, visibility, library) -> element
    check(qn(*derived.find_part("a")) == "Scopes::Derived::a", "find_part(\"a\")");
    check(!derived.find_part("b"), "find_part(\"b\"): none");
    // (scope) -> elements; (name, scope) -> element
    auto imports = at("DefinitionLists::Imports").as<Package>();
    // outside the owned scope the order is the backend's: the specification leaves membership's order open
    auto sorted = [](V v) { std::sort(v.begin(), v.end()); return v; };
    check(sorted(names(imports.find_part_defs(Scope::Inherited))) == sorted(V{"DefinitionLists::ForPartDefs::P", "DefinitionLists::ForPartDefs::Cn"}),
          "find_part_defs(Scope::Inherited)");
    check(qn(*imports.find_part_def("P", Scope::All)) == "DefinitionLists::ForPartDefs::P", "find_part_def(\"P\", Scope::All)");
    // (scope) -> a name-keyed map: the owned definition under its name and its alias's, and the imported
    // ones, in the backend's order
    auto keyed = [&](const auto& map) {
        V out;
        for (const auto& entry : map) out.push_back(entry.first + "=" + qn(entry.second));
        return out;
    };
    const V all_names{"Local=DefinitionLists::Imports::Local", "LocalAlias=DefinitionLists::Imports::Local",
                      "P=DefinitionLists::ForPartDefs::P", "Cn=DefinitionLists::ForPartDefs::Cn"};
    const auto map = imports.find_part_defs_by_name(Scope::All);
    check(sorted(keyed(map)) == sorted(all_names), "find_part_defs_by_name(Scope::All)");
    check(map.find("LocalAlias") && qn(*map.find("LocalAlias")) == "DefinitionLists::Imports::Local", "NameMap::find");
    check(!map.contains("It"), "NameMap::contains");
    // A name that names two definitions (a connection and a view definition): the contract refuses
    const auto clash = at("DefinitionLists::Clash").as<Package>();
    check(outcome([&] { return keyed(clash.find_part_defs_by_name()); }) == V{"<refused>"}, "find_part_defs_by_name: a clash is refused");
    // (name) -> element
    check(qn(*at("Structure::Outer").as<Package>().find_package("Inner")) == "Structure::Outer::Inner", "find_package");
    // (element) -> element
    auto body = at("Typing::Body").as<PartDefinition>();
    auto frame_mass = at("Typing::Frame::mass").as<Feature>();
    check(qn(*body.find_effective_feature(frame_mass)) == "Typing::Body::mass", "find_effective_feature");
    // (element, scope, visibility, library) -> elements
    auto vehicle = at("Behaviour::Vehicle").as<PartDefinition>();
    auto wheels = at("Behaviour::Vehicle::wheels").as<Feature>();
    check(names(vehicle.find_subsetting_features_of(wheels)) == V{"Behaviour::Vehicle::wheels", "Behaviour::Vehicle::frontWheels"},
          "find_subsetting_features_of");
    // () -> element, the end of a walk: answered on both
    check(qn(*at("Typing::Truck::mass").as<Feature>().find_redefinition_root()) == "Typing::Frame::mass", "find_redefinition_root");
    // (type) -> Boolean and directed features
    auto acc = at("Behaviour::Listen::acc").as<AcceptActionUsage>();
    auto sub_sig = at("Behaviour::SubSig").as<Type>();
    auto fuel = at("Behaviour::Fuel").as<Type>();
    auto socket = at("Behaviour::Socket").as<PortDefinition>();
    check(acc.find_is_accepted_type(sub_sig), "find_is_accepted_type: a specialization");
    check(!acc.find_is_accepted_type(fuel), "find_is_accepted_type: another type");
    check(names(socket.find_inputs()) == V{"Behaviour::Socket::plugIn", "Behaviour::Socket::exchange"}, "find_inputs");
    check(names(socket.find_outputs()) == V{"Behaviour::Socket::status", "Behaviour::Socket::exchange"}, "find_outputs");
    // () -> literal
    check(std::get<std::int64_t>(value_of("Values::Settings::count").find_literal()) == 4, "find_literal: 4");
    check(std::get<double>(value_of("Values::Settings::ratio").find_literal()) == 0.5, "find_literal: 0.5");
    check(std::get<bool>(value_of("Values::Settings::enabled").find_literal()), "find_literal: true");
    check(std::get<std::string>(value_of("Values::Settings::label").find_literal()) == "front", "find_literal: \"front\"");
    check(throws_refused([&] { (void)value_of("Values::Settings::mass").find_literal(); }), "find_literal of 1500 [kg]: refused");
    check(value_of("Values::Settings::mass").find_has_unit(), "find_has_unit");
    // a sign or a unit is read through the operator expression's argument
    check(std::get<std::int64_t>(value_of("Values::Settings::offset").find_literal()) == -5, "find_literal: -5");
    check(qn(*value_of("Values::Settings::mass").find_unit()) == "SI::kilogram", "find_unit");
    // () -> UnlimitedNatural, Integer, as a Literal
    check(std::holds_alternative<Unbounded>(*at("Bounds::V::atLeastOne").as<PartUsage>().find_upper_bound()), "find_upper_bound: *");
    check(std::get<std::int64_t>(*at("Bounds::V::upToThree").as<PartUsage>().find_upper_bound()) == 3, "find_upper_bound: 3");
    check(!at("Bounds::W").as<PartDefinition>().find_upper_bound(), "find_upper_bound: none");
    check(std::get<std::int64_t>(*at("Bounds::V::two").as<PartUsage>().find_lower_bound()) == 2, "find_lower_bound: 2");
    // () -> strings, on every element
    check(at("Structure::Car").find_doc_text() == V{"A car. "}, "find_doc_text");
}

/// Why the Pilot's run is skipped, or empty.
static std::string pilot_why_not() {
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

/// A note that the SysML Toolkit's run is skipped; false, a failure, with SYSML_REQUIRE_TOOLKIT=1.
static bool skip_toolkit(const std::string& what) {
    const char* require = std::getenv("SYSML_REQUIRE_TOOLKIT");
    if (require && std::string(require) == "1") {
        std::cerr << "SYSML_REQUIRE_TOOLKIT=1 but " << what << " is missing\n";
        return false;
    }
    std::cout << "convenience checks on the SysML Toolkit skipped: " << what << " is missing\n";
    return true;
}

int main() {
    fs::path root = fs::current_path();
    while (!fs::exists(root / "metamodel.json") && root.has_parent_path() && root != root.parent_path())
        root = root.parent_path();
    std::ifstream in(root / "convenience.json");
    std::stringstream text;
    text << in.rdbuf();
    const nlohmann::json table = nlohmann::json::parse(text.str());
    std::vector<std::pair<std::string, std::string>> sources;
    for (const auto& [name, m] : table["models"].items()) sources.emplace_back(name + ".sysml", m["text"].get<std::string>());

    const fs::path library = root.parent_path() / "sysml-toolkit" / "spec-refs" / "SysML-v2-Release" / "sysml.library";
    std::string missing;
    try {
        (void)ToolkitBackend::Library::default_path();
    } catch (const sdk_error& e) {
        missing = std::string("the binding library (") + e.what() + ")";
    }
    if (missing.empty() && !fs::is_directory(library)) missing = "the release library at " + library.string();
    if (!missing.empty()) {
        if (!skip_toolkit(missing)) return 1;
    } else {
        Model m = Model::from_backend(ToolkitBackend::open({}, sources, library.string()));
        check_shapes(m);
        std::cout << "convenience shapes on the SysML Toolkit (C++): passed\n";
    }

    if (std::string why = pilot_why_not(); !why.empty()) {
        std::cout << "convenience checks on the Pilot Implementation skipped: " << why << "\n";
    } else {
        PilotOptions options;
        options.heap = "2g";
        Model m = Model::from_backend(PilotBackend::open_sources(sources, options));
        check_shapes(m);
        std::cout << "convenience shapes on the Pilot Implementation (C++): passed\n";
    }
    std::cout << "ALL C++ CONVENIENCE CHECKS PASSED\n";
    return 0;
}
