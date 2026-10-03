// Parametric queries over two example models, and optionally over the vehicle model of SysML
// Annex A, printed as a report. The program in every language prints the same report, which is
// kept in examples/queries/expected/.
//
// Build and run from the repository root (header-only, C++17):
//
//     g++ -std=c++17 -I cpp/include -I vendor examples/queries/cpp/demo.cpp -o queries_demo
//     ./queries_demo                          reads the models through the SysML Toolkit
//     ./queries_demo --payload                reads their JSON exports instead, with the standard
//                                             library as JSON
//     ./queries_demo --library DIR            loads the standard library with them
//     ./queries_demo --annex-a FILE --library DIR
//                                             reports on the Annex A vehicle instead
//
// (On Windows with MinGW add -static; on Linux add -ldl.) The JSON exports and the library JSON
// are generated, not committed: python tools/export_example.py. The queries themselves are in
// queries.hpp; this file loads the models and asks the questions.

#include <algorithm>
#include <fstream>
#include <iostream>
#include <sstream>

#include <sysml/toolkit.hpp>

#include "queries.hpp"

using namespace sysml;
namespace q = queries;

static const std::string MODELS = "examples/queries/models/";

static std::string read_file(const std::string& path) {
    std::ifstream in(path, std::ios::binary);
    if (!in) throw std::runtime_error("cannot open " + path + " (run from the repository root)");
    std::stringstream ss;
    ss << in.rdbuf();
    return ss.str();
}

/// The standard library as JSON, read once and shared by every payload model.
static std::shared_ptr<const PayloadLibrary> payload_library() {
    static std::shared_ptr<const PayloadLibrary> library =
        PayloadLibrary::parse(read_file("examples/sysml.library.full.json"));
    return library;
}

/// A model from examples/queries/models: `name`.sysml through the SysML Toolkit, or
/// `name`.full.json with the library JSON.
static Model load(const std::string& name, bool payload, const std::string& library) {
    if (payload) return Model::from_full_json(read_file(MODELS + name + ".full.json"), payload_library());
    return Model::from_backend(ToolkitBackend::open({}, {{name + ".sysml", read_file(MODELS + name + ".sysml")}}, library));
}

static void heading(const std::string& title) { std::cout << "== " << title << " ==\n"; }

/// Run one report. Where the SysML Toolkit cannot vouch for a value yet it refuses the read
/// (not_implemented_in_toolkit); the report stops there, says so, and the next one runs.
static void section(const std::function<void()>& report) {
    try {
        report();
    } catch (const not_implemented_in_toolkit& e) {
        std::cout << "  report stopped: " << e.what() << "\n";
    }
}

using Bindings = std::vector<std::pair<std::string, q::Value>>;

/// A scenario: its title, the triggers that can occur (nullopt for all), and the values bound.
struct Scenario {
    std::string title;
    std::optional<std::vector<std::string>> triggers;
    Bindings bindings;
};

static std::map<std::string, q::Value> as_map(const Bindings& bindings) { return {bindings.begin(), bindings.end()}; }

/// A scenario in words: the triggers that can occur and the values bound.
static std::string describe(const Scenario& scenario) {
    std::vector<std::string> parts;
    if (scenario.triggers) parts.push_back("only " + q::join(*scenario.triggers, ", "));
    if (!scenario.bindings.empty()) {
        std::vector<std::string> values;
        for (const auto& [name, value] : scenario.bindings) values.push_back(name + " = " + q::fmt(value));
        parts.push_back(q::join(values, ", "));
    }
    return parts.empty() ? "nothing bound, every trigger" : q::join(parts, "; ");
}

// ---------------------------------------------------------------- the reports

/// Reachable states of `machine` under each scenario, then the shortest trigger sequences under the
/// scenario `paths_for`.
static void report_states(const StateUsage& machine, const std::vector<Scenario>& scenarios, const Scenario& paths_for) {
    heading("Reachable states: " + machine.getQualifiedName().value_or(""));
    std::vector<StateUsage> states = q::states_of(machine);
    std::vector<TransitionUsage> transitions = q::transitions_of(machine);
    std::cout << states.size() << " states, " << transitions.size() << " transitions:\n";
    std::vector<StateUsage> with_machine{machine};
    with_machine.insert(with_machine.end(), states.begin(), states.end());
    for (const StateUsage& state : with_machine)
        for (const StateUsage& initial : q::initial_states(state))
            std::cout << "  " << q::describe_initial(state, initial, machine) << "\n";
    for (const TransitionUsage& transition : transitions) std::cout << "  " << q::describe_transition(transition, machine) << "\n";
    for (const Scenario& scenario : scenarios) {
        q::Paths reached = q::explore(machine, scenario.triggers, as_map(scenario.bindings));
        std::cout << scenario.title << ": " << describe(scenario) << "\n";
        std::vector<std::string> reachable, unreachable;
        for (const StateUsage& state : states)
            (reached.count(state.element_id()) ? reachable : unreachable).push_back(q::path_below(state, machine));
        std::cout << "  reachable (" << reachable.size() << "): " << q::join(reachable, ", ") << "\n";
        std::cout << "  unreachable (" << unreachable.size() << "): " << (unreachable.empty() ? "-" : q::join(unreachable, ", ")) << "\n";
    }
    q::Paths reached = q::explore(machine, paths_for.triggers, as_map(paths_for.bindings));
    std::cout << "shortest trigger sequences (" << describe(paths_for) << "):\n";
    std::size_t width = 0;
    for (const StateUsage& state : states) width = std::max(width, q::path_below(state, machine).size());
    for (const StateUsage& state : states) {
        auto path = reached.find(state.element_id());
        std::string text = path == reached.end() ? "unreachable" : path->second.empty() ? "(no trigger)" : q::join(path->second, ", ");
        std::string name = q::path_below(state, machine);
        std::cout << "  " << name << std::string(width - name.size(), ' ') << "  " << text << "\n";
    }
}

static void report_structure(const std::vector<Type>& roots, const std::vector<Feature>& attributes) {
    heading("Structure: bills of materials and roll-ups");
    for (const Type& root : roots) {
        std::cout << root.getQualifiedName().value_or("") << "\n";
        std::vector<std::string> parts, totals;
        for (const auto& [name, count] : q::bill_of_materials(root)) parts.push_back(name + " " + std::to_string(count));
        std::cout << "  parts: " << q::join(parts, ", ") << "\n";
        for (const Feature& attribute : attributes) totals.push_back(q::name_of(attribute) + " " + q::fmt(q::rollup(root, attribute)));
        std::cout << "  " << q::join(totals, ", ") << "\n";
    }
}

static void report_connectivity(const Type& root, const std::vector<std::pair<std::string, std::optional<std::string>>>& questions) {
    heading("Connectivity: " + root.getQualifiedName().value_or(""));
    for (const q::Link& link : q::links(root)) std::cout << "  " << link.describe() << "\n";
    for (const auto& [source, item] : questions) {
        std::string via = item ? ", through " + *item + " flows only" : "";
        std::vector<std::string> reached = q::reachable_parts(root, source, item);
        std::cout << "from " << source << via << ": " << (reached.empty() ? "-" : q::join(reached, ", ")) << "\n";
    }
}

static void report_requirements(const Model& model, const std::map<std::string, std::function<q::Value(const Type&)>>& derived) {
    heading("Requirements");
    for (const q::Verdict& v : q::check_satisfactions(model, derived)) {
        std::optional<RequirementDefinition> definition = v.requirement.getRequirementDefinition();
        std::string kind = definition ? " : " + q::name_of(*definition) : "";
        std::string verb = v.negated ? "not satisfied" : "satisfied";
        Expression expr = q::result_expression(v.constraint).value();
        std::string op = expr.is_a<OperatorExpression>() ? expr.as<OperatorExpression>().getOperator().value_or("") : "";
        std::vector<std::string> values;
        for (const q::Value& operand : v.operands) values.push_back(q::fmt(operand));
        std::string by = v.satisfied_by ? q::name_of(*v.satisfied_by) : "(nothing)";
        std::cout << q::name_of(v.requirement) << kind << ", " << verb << " by " << by << "\n";
        std::cout << "  " << q::name_of(v.constraint) << ": " << q::render(expr) << "  [" << q::join(values, " " + op + " ")
                  << "]  " << v.outcome() << "\n";
    }
}

// ---------------------------------------------------------------- the questions

static void examples(bool payload, const std::string& library) {
    const Bindings temperate{{"selfTestPassed", true}, {"temperature", 25.0}};
    Bindings hot_limit = temperate;
    hot_limit.emplace_back("maxTemperature", 20.0);
    Model pumps = load("pumps", payload, library);
    section([&] {
        report_states(pumps.resolve("Pumps::PumpController::modes").value().as<StateUsage>(),
                      {
                          {"A", std::nullopt, {}},
                          {"B", std::nullopt, temperate},
                          {"C", std::nullopt, {{"selfTestPassed", false}}},
                          {"D", std::nullopt, {{"selfTestPassed", true}, {"temperature", 95.0}}},
                          {"E", std::nullopt, hot_limit},
                          {"F", std::vector<std::string>{"PowerOn", "SelfTestDone", "Start"}, temperate},
                      },
                      {"", std::nullopt, temperate});
    });
    std::cout << "\n";

    Model drones = load("drones", payload, library);
    Feature mass = drones.resolve("Drones::Component::mass").value().as<Feature>();
    Feature cost = drones.resolve("Drones::Component::cost").value().as<Feature>();
    std::vector<Type> roots;
    for (const char* name : {"Quadcopter", "Hexacopter", "CargoHexacopter"})
        roots.push_back(drones.resolve(std::string("Drones::") + name).value().as<Type>());
    section([&] { report_structure(roots, {mass, cost}); });
    std::cout << "\n";
    section([&] {
        report_connectivity(drones.resolve("Drones::Quadcopter").value().as<Type>(),
                            {{"battery", std::nullopt}, {"battery", "Power"}, {"controller", std::nullopt},
                             {"controller", "Command"}, {"rotors.propeller", std::nullopt}});
    });
    std::cout << "\n";
    section([&] {
        report_requirements(drones, {
                                        {"totalMass", [&](const Type& part) { return q::Value(q::rollup(part, mass)); }},
                                        {"totalCost", [&](const Type& part) { return q::Value(q::rollup(part, cost)); }},
                                    });
    });
}

static void annex_a(const std::string& path, const std::string& library) {
    const std::string name = path.substr(path.find_last_of("/\\") + 1);
    Model vehicle = Model::from_backend(ToolkitBackend::open({}, {{name, read_file(path)}}, library));
    auto machine = vehicle.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates").value().as<StateUsage>();
    section([&] {
        std::vector<std::string> keys;
        for (const TransitionUsage& transition : q::transitions_of(machine)) {
            std::optional<q::Trigger> trigger = q::trigger_of(transition);
            if (trigger && trigger->key != "at" && std::find(keys.begin(), keys.end(), trigger->key) == keys.end())
                keys.push_back(trigger->key);
        }
        report_states(machine,
                      {
                          {"A", std::nullopt, {}},
                          {"B", std::nullopt, {{"brakePedalDepressed", false}}},
                          {"C", keys, {}},
                      },
                      {"", std::nullopt, {}});
    });
}

int main(int argc, char** argv) {
    std::vector<std::string> args(argv + 1, argv + argc);
    auto option = [&](const std::string& name) {
        auto it = std::find(args.begin(), args.end(), name);
        return it != args.end() && it + 1 != args.end() ? *(it + 1) : std::string();
    };
    const std::string library = option("--library"), annex = option("--annex-a");
    if (!annex.empty()) annex_a(annex, library);
    else examples(std::find(args.begin(), args.end(), "--payload") != args.end(), library);
    return 0;
}
