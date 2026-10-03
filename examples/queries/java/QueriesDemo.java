// Parametric queries over two example models, and optionally over the vehicle model of SysML
// Annex A, printed as a report. The program in every language prints the same report, which is
// kept in examples/queries/expected/.
//
// Compile and run from the repository root (JDK 21 or newer, no build tool needed):
//
//     javac --enable-preview --release 21 -d java/out $(find java/src -name '*.java')
//     javac --enable-preview --release 21 -cp java/out -d examples/queries/java/out examples/queries/java/*.java
//     java --enable-preview -cp java/out:examples/queries/java/out QueriesDemo      (; instead of : on Windows)
//
// With no arguments the models are read through the SysML Toolkit; --payload reads their JSON
// exports instead, with the standard library as JSON, --library DIR loads the standard library
// with them, and --annex-a FILE --library DIR reports on the Annex A vehicle instead. The JSON
// exports and the library JSON are generated, not committed: python tools/export_example.py. The
// queries themselves are in Queries.java; this file loads the models and asks the questions.

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.openmbee.sysml.ToolkitBackend;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotImplementedInToolkitException;
import org.openmbee.sysml.PayloadLibrary;
import org.openmbee.sysml.classes.Expression;
import org.openmbee.sysml.classes.Feature;
import org.openmbee.sysml.classes.RequirementDefinition;
import org.openmbee.sysml.classes.StateUsage;
import org.openmbee.sysml.classes.TransitionUsage;
import org.openmbee.sysml.classes.Type;

public class QueriesDemo {
    static final Path MODELS = Path.of("examples", "queries", "models");
    static final Path LIBRARY_JSON = Path.of("examples", "sysml.library.full.json");
    static PayloadLibrary payloadLibrary;

    /** The standard library as JSON, read once and shared by every payload model. */
    static PayloadLibrary payloadLibrary() throws IOException {
        if (payloadLibrary == null)
            payloadLibrary = PayloadLibrary.fromJson(Files.readString(LIBRARY_JSON));
        return payloadLibrary;
    }

    /** A model from examples/queries/models: `name`.sysml through the SysML Toolkit, or
     * `name`.full.json with the library JSON. */
    static Model load(String name, boolean payload, String library) throws IOException {
        if (payload)
            return Model.fromFullJson(Files.readString(MODELS.resolve(name + ".full.json")), payloadLibrary());
        Map<String, String> sources = Map.of(name + ".sysml", Files.readString(MODELS.resolve(name + ".sysml")));
        return new Model(ToolkitBackend.open(ToolkitBackend.library(), List.of(), sources, library));
    }

    static void heading(String title) {
        System.out.println("== " + title + " ==");
    }

    /** Run one report. Where the SysML Toolkit cannot vouch for a value yet it refuses the read
     *  (NotImplementedInToolkitException); the report stops there, says so, and the next one runs. */
    static void section(Runnable report) {
        try {
            report.run();
        } catch (NotImplementedInToolkitException e) {
            System.out.println("  report stopped: " + e.getMessage());
        }
    }

    /** A scenario: its title, the triggers that can occur (null for all), and the values bound. */
    record Scenario(String title, List<String> triggers, Map<String, Object> bindings) {
    }

    /** Values bound by name, in the order given: name, value, name, value, ... */
    static Map<String, Object> bind(Object... pairs) {
        Map<String, Object> bindings = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2)
            bindings.put((String) pairs[i], pairs[i + 1]);
        return bindings;
    }

    /** A scenario in words: the triggers that can occur and the values bound. */
    static String describe(List<String> triggers, Map<String, Object> bindings) {
        List<String> parts = new ArrayList<>();
        if (triggers != null)
            parts.add("only " + String.join(", ", triggers));
        if (!bindings.isEmpty()) {
            List<String> values = new ArrayList<>();
            bindings.forEach((name, value) -> values.add(name + " = " + Queries.fmt(value)));
            parts.add(String.join(", ", values));
        }
        return parts.isEmpty() ? "nothing bound, every trigger" : String.join("; ", parts);
    }

    // ---------------------------------------------------------------- the reports

    /** Reachable states of `machine` under each scenario, then the shortest trigger sequences under
     * the scenario `pathsFor`. */
    static void reportStates(StateUsage machine, List<Scenario> scenarios, Scenario pathsFor) {
        heading("Reachable states: " + machine.getQualifiedName());
        List<StateUsage> states = Queries.statesOf(machine);
        List<TransitionUsage> transitions = Queries.transitionsOf(machine);
        System.out.println(states.size() + " states, " + transitions.size() + " transitions:");
        List<StateUsage> withMachine = new ArrayList<>(List.of(machine));
        withMachine.addAll(states);
        for (StateUsage state : withMachine)
            for (StateUsage initial : Queries.initialStates(state))
                System.out.println("  " + Queries.describeInitial(state, initial, machine));
        for (TransitionUsage transition : transitions)
            System.out.println("  " + Queries.describeTransition(transition, machine));
        for (Scenario scenario : scenarios) {
            Map<StateUsage, List<String>> reached = Queries.explore(machine, scenario.triggers(), scenario.bindings());
            System.out.println(scenario.title() + ": " + describe(scenario.triggers(), scenario.bindings()));
            List<String> reachable = new ArrayList<>(), unreachable = new ArrayList<>();
            for (StateUsage state : states)
                (reached.containsKey(state) ? reachable : unreachable).add(Queries.pathBelow(state, machine));
            System.out.println("  reachable (" + reachable.size() + "): " + String.join(", ", reachable));
            System.out.println("  unreachable (" + unreachable.size() + "): "
                + (unreachable.isEmpty() ? "-" : String.join(", ", unreachable)));
        }
        Map<StateUsage, List<String>> reached = Queries.explore(machine, pathsFor.triggers(), pathsFor.bindings());
        System.out.println("shortest trigger sequences (" + describe(pathsFor.triggers(), pathsFor.bindings()) + "):");
        int width = 0;
        for (StateUsage state : states)
            width = Math.max(width, Queries.pathBelow(state, machine).length());
        for (StateUsage state : states) {
            List<String> path = reached.get(state);
            String text = path == null ? "unreachable" : path.isEmpty() ? "(no trigger)" : String.join(", ", path);
            System.out.println("  " + String.format("%-" + width + "s", Queries.pathBelow(state, machine)) + "  " + text);
        }
    }

    static void reportStructure(List<Type> roots, List<Feature> attributes) {
        heading("Structure: bills of materials and roll-ups");
        for (Type root : roots) {
            System.out.println(root.getQualifiedName());
            List<String> parts = new ArrayList<>();
            Queries.billOfMaterials(root).forEach((name, count) -> parts.add(name + " " + count));
            System.out.println("  parts: " + String.join(", ", parts));
            List<String> totals = new ArrayList<>();
            for (Feature attribute : attributes)
                totals.add(Queries.nameOf(attribute) + " " + Queries.fmt(Queries.rollup(root, attribute)));
            System.out.println("  " + String.join(", ", totals));
        }
    }

    record Question(String source, String item) {
    }

    static void reportConnectivity(Type root, List<Question> questions) {
        heading("Connectivity: " + root.getQualifiedName());
        for (Queries.Link link : Queries.links(root))
            System.out.println("  " + link.describe());
        for (Question question : questions) {
            String via = question.item() != null ? ", through " + question.item() + " flows only" : "";
            List<String> reached = Queries.reachableParts(root, question.source(), question.item());
            System.out.println("from " + question.source() + via + ": " + (reached.isEmpty() ? "-" : String.join(", ", reached)));
        }
    }

    static void reportRequirements(Model model, Map<String, Function<Type, Object>> derived) {
        heading("Requirements");
        for (Queries.Verdict v : Queries.checkSatisfactions(model, derived)) {
            RequirementDefinition definition = v.requirement().getRequirementDefinition();
            String kind = definition != null ? " : " + Queries.nameOf(definition) : "";
            String verb = v.negated() ? "not satisfied" : "satisfied";
            Expression expr = Queries.resultExpression(v.constraint());
            List<String> values = new ArrayList<>();
            for (Object operand : v.operands())
                values.add(Queries.fmt(operand));
            String op = expr instanceof org.openmbee.sysml.classes.OperatorExpression o ? o.getOperator() : "";
            System.out.println(Queries.nameOf(v.requirement()) + kind + ", " + verb + " by " + Queries.nameOf(v.satisfiedBy()));
            System.out.println("  " + Queries.nameOf(v.constraint()) + ": " + Queries.render(expr) + "  ["
                + String.join(" " + op + " ", values) + "]  " + v.outcome());
        }
    }

    // ---------------------------------------------------------------- the questions

    static final Map<String, Object> TEMPERATE = bind("selfTestPassed", true, "temperature", 25.0);

    static final List<Scenario> PUMP_SCENARIOS = List.of(
        new Scenario("A", null, bind()),
        new Scenario("B", null, TEMPERATE),
        new Scenario("C", null, bind("selfTestPassed", false)),
        new Scenario("D", null, bind("selfTestPassed", true, "temperature", 95.0)),
        new Scenario("E", null, bind("selfTestPassed", true, "temperature", 25.0, "maxTemperature", 20.0)),
        new Scenario("F", List.of("PowerOn", "SelfTestDone", "Start"), TEMPERATE));

    static void examples(boolean payload, String library) throws IOException {
        Model pumps = load("pumps", payload, library);
        section(() -> reportStates((StateUsage) pumps.resolve("Pumps::PumpController::modes"), PUMP_SCENARIOS,
            new Scenario("", null, TEMPERATE)));
        System.out.println();

        Model drones = load("drones", payload, library);
        Feature mass = (Feature) drones.resolve("Drones::Component::mass");
        Feature cost = (Feature) drones.resolve("Drones::Component::cost");
        List<Type> roots = new ArrayList<>();
        for (String name : List.of("Quadcopter", "Hexacopter", "CargoHexacopter"))
            roots.add((Type) drones.resolve("Drones::" + name));
        section(() -> reportStructure(roots, List.of(mass, cost)));
        System.out.println();
        section(() -> reportConnectivity((Type) drones.resolve("Drones::Quadcopter"), List.of(
            new Question("battery", null), new Question("battery", "Power"), new Question("controller", null),
            new Question("controller", "Command"), new Question("rotors.propeller", null))));
        System.out.println();
        Map<String, Function<Type, Object>> derived = new LinkedHashMap<>();
        derived.put("totalMass", part -> Queries.rollup(part, mass));
        derived.put("totalCost", part -> Queries.rollup(part, cost));
        section(() -> reportRequirements(drones, derived));
    }

    static void annexA(String path, String library) throws IOException {
        Path file = Path.of(path);
        Map<String, String> sources = Map.of(file.getFileName().toString(), Files.readString(file));
        Model vehicle = new Model(ToolkitBackend.open(ToolkitBackend.library(), List.of(), sources, library));
        StateUsage machine = (StateUsage) vehicle.resolve("SimpleVehicleModel::Definitions::PartDefinitions::Vehicle::vehicleStates");
        section(() -> {
            List<String> keys = new ArrayList<>();
            for (TransitionUsage transition : Queries.transitionsOf(machine)) {
                Queries.Trigger trigger = Queries.triggerOf(transition);
                if (trigger != null && !trigger.key().equals("at") && !keys.contains(trigger.key()))
                    keys.add(trigger.key());
            }
            reportStates(machine, List.of(
                new Scenario("A", null, bind()),
                new Scenario("B", null, bind("brakePedalDepressed", false)),
                new Scenario("C", keys, bind())), new Scenario("", null, bind()));
        });
    }

    static String option(String[] args, String name) {
        for (int i = 0; i + 1 < args.length; i++)
            if (args[i].equals(name))
                return args[i + 1];
        return null;
    }

    public static void main(String[] args) throws IOException {
        String library = option(args, "--library");
        String annex = option(args, "--annex-a");
        if (annex != null)
            annexA(annex, library);
        else
            examples(List.of(args).contains("--payload"), library);
    }
}
