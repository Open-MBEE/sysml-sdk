// Java reading through the SysML v2 Pilot Implementation, in this process.
// Skips itself when no Pilot is at hand (SYSML_PILOT_HOME unset and none in the user's cache), so
// the other checks run everywhere without downloading it; with SYSML_REQUIRE_PILOT=1 the Pilot is
// downloaded when absent and a missing Pilot is a failure.
package org.openmbee.sysml.checks;

import java.util.List;
import java.util.Map;

import org.openmbee.sysml.BackendException;
import org.openmbee.sysml.Element;
import org.openmbee.sysml.GoneException;
import org.openmbee.sysml.Json;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.PilotBackend;
import org.openmbee.sysml.UnresolvedReferenceException;
import org.openmbee.sysml.classes.ConnectionUsage;
import org.openmbee.sysml.classes.Feature;
import org.openmbee.sysml.classes.Membership;
import org.openmbee.sysml.classes.Namespace;
import org.openmbee.sysml.classes.Package;
import org.openmbee.sysml.classes.PartDefinition;
import org.openmbee.sysml.classes.PartUsage;
import org.openmbee.sysml.classes.RequirementDefinition;
import org.openmbee.sysml.classes.Type;
import org.openmbee.sysml.pilot.Pilot;
import org.openmbee.sysml.pilot.PilotEngine;

public final class PilotChecks {
    private static void check(boolean cond, String what) {
        if (!cond) throw new AssertionError("FAILED: " + what);
    }

    public static void main(String[] args) throws Exception {
        boolean required = "1".equals(System.getenv("SYSML_REQUIRE_PILOT"));
        Pilot pilot = required ? Pilot.locate() : Pilot.find();
        if (pilot == null) {
            System.out.println("Pilot checks skipped: no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)");
            return;
        }
        System.out.println("checking with " + pilot);

        String source = "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }";
        try (PilotBackend be = PilotBackend.open(pilot, List.of(), Map.of("inline.sysml", source), true)) {
            Model m = new Model(be);
            check(pilot.version().equals(be.stats().get("pilot")), "stats name the Pilot's version");

            Element car = m.resolve("Demo::Car");
            check(car instanceof PartDefinition, "Car is a PartDefinition");
            PartDefinition carDef = (PartDefinition) car;
            check("Demo::Car".equals(carDef.getQualifiedName()), "qualifiedName");
            check(carDef.getOwner() instanceof Package, "owner is the package");

            List<Feature> feats = carDef.getOwnedFeature();
            check(feats.size() == 2 && "wheels".equals(feats.get(0).getDeclaredName()), "ownedFeature");
            check(feats.get(0) instanceof PartUsage, "wheels is a PartUsage");
            List<Type> types = feats.get(0).getType();
            check(types.size() == 1 && "Wheel".equals(types.get(0).getDeclaredName()), "type: " + types);
            check(Boolean.TRUE.equals(feats.get(0).getIsComposite()), "isComposite");

            PartDefinition sports = (PartDefinition) m.resolve("Demo::Sports");
            List<Feature> inherited = sports.getInheritedFeature();
            boolean hasWheels = false;
            for (Feature f : inherited) hasWheels |= "wheels".equals(f.getDeclaredName());
            check(hasWheels, "inheritedFeature includes Car's wheels: " + inherited);
            check(sports.specializes(carDef), "specializes (an operation)");

            check(car == m.resolve("Demo::Car"), "wrappers are cached by handle");
            check(carDef.$id().length() == 36, "elementId is a UUID: " + carDef.$id());
            check(m.resolve("Demo::Nothing") == null, "an unknown name resolves to null");
            check(m.resolve("ScalarValues::Real") != null, "library names resolve");

            check("Car".equals(carDef.effectiveName()), "effectiveName");
            ConnectionUsage connection = null;
            for (Feature f : ((PartDefinition) m.resolve("Demo::Link")).getOwnedFeature())
                if (f instanceof ConnectionUsage c) connection = c;
            Feature end = connection.getConnectorEnd().get(0);
            check(end.getDeclaredName() == null && "source".equals(end.effectiveName()),
                "an unnamed connection end is named by the end it redefines: " + end.effectiveName());
            List<Membership> memberships = sports.inheritedMemberships(List.of(), List.of(), false);
            boolean wheelsMembership = false;
            for (Membership ms : memberships) wheelsMembership |= "wheels".equals(ms.getMemberName());
            check(wheelsMembership, "inheritedMemberships");
            check(sports.isCompatibleWith(carDef), "isCompatibleWith");

            int all = m.all().size();
            check(all > 20, "allHandles covers the elaborated model: " + all);
            boolean gone = false;
            try {
                be.metaclass("999999999");
            } catch (GoneException e) {
                gone = true;
            }
            check(gone, "an unknown handle is gone");
            check(be.get(m.resolve("Demo::Car").$handle(), "noSuchProperty") == null, "a property the metaclass lacks reads as absent");
            boolean refused = false;
            try {
                be.call(carDef.$handle(), "noSuchOperation", List.of());
            } catch (BackendException e) {
                refused = e.getMessage().contains("noSuchOperation");
            }
            check(refused, "an operation the Pilot does not have raises BackendException");
        }

        // Without elaboration: no implied relationships.
        try (PilotBackend be = PilotBackend.open(pilot, List.of(), Map.of("inline.sysml", source), false)) {
            Model m = new Model(be);
            PartDefinition car = (PartDefinition) m.resolve("Demo::Car");
            check(car.getOwnedSpecialization().isEmpty(), "no implied specialization without elaboration");
        }

        // An unresolved reference.
        try (PilotBackend be = PilotBackend.openSources(Map.of("bad.sysml", "package Bad { part p : Missing; }"))) {
            Model m = new Model(be);
            PartUsage p = (PartUsage) m.resolve("Bad::p");
            boolean unresolved = false;
            try {
                p.getOwnedTyping().get(0).getType();
            } catch (UnresolvedReferenceException e) {
                unresolved = true;
            }
            check(unresolved, "an unresolved reference raises UnresolvedReferenceException");
        }

        // Text beyond ASCII, a requirement's text, the top-level namespaces.
        String sizesSource = "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }";
        try (PilotBackend be = PilotBackend.openSources(Map.of("größe.sysml", sizesSource))) {
            Model sizes = new Model(be);
            check(sizes.resolve("Sizes::Last") instanceof PartDefinition, "a source with text beyond ASCII loads whole");
            check(sizes.resolve("Sizes").getDocumentation().get(0).getBody().contains("Größe und Wärme"),
                "text beyond ASCII comes back intact");
            RequirementDefinition light = (RequirementDefinition) sizes.resolve("Sizes::Light");
            List<String> text = light.getText();
            check(text.size() == 1 && text.get(0).startsWith("Keep it light."), "a requirement's text is a list of strings: " + text);
            boolean found = false;
            for (Element root : sizes.roots())
                for (Element member : ((Namespace) root).getOwnedMember())
                    found |= "Sizes".equals(member.getName());
            check(found, "roots");
        }

        // The protocol the other languages speak, on the same engine.
        try (PilotEngine engine = new PilotEngine(pilot)) {
            Map<?, ?> opened = (Map<?, ?>) Json.parse(engine.request(
                "{\"id\": 1, \"cmd\": \"open\", \"sources\": {\"a.sysml\": \"package P { part def A; }\"}}"));
            check(Long.valueOf(1).equals(opened.get("id")) && opened.get("ok") instanceof Map, "open answers: " + opened);
            Map<?, ?> resolved = (Map<?, ?>) Json.parse(engine.request("{\"id\": 2, \"cmd\": \"resolve\", \"name\": \"P::A\"}"));
            String h = (String) resolved.get("ok");
            Map<?, ?> mc = (Map<?, ?>) Json.parse(engine.request("{\"id\": 3, \"cmd\": \"metaclass\", \"h\": \"" + h + "\"}"));
            check("PartDefinition".equals(mc.get("ok")), "metaclass over the protocol: " + mc);
            Map<?, ?> owner = (Map<?, ?>) Json.parse(engine.request("{\"id\": 4, \"cmd\": \"get\", \"h\": \"" + h + "\", \"prop\": \"owningNamespace\"}"));
            check(owner.get("ok") instanceof Map<?, ?> ref && ref.get("@id") instanceof String, "references are {\"@id\": handle}: " + owner);
            Map<?, ?> bad = (Map<?, ?>) Json.parse(engine.request("{\"id\": 5, \"cmd\": \"metaclass\", \"h\": \"424242\"}"));
            check(bad.get("error") instanceof Map<?, ?> e && "gone".equals(e.get("kind")), "errors carry a kind: " + bad);
            Map<?, ?> unknown = (Map<?, ?>) Json.parse(engine.request("{\"id\": 6, \"cmd\": \"frobnicate\"}"));
            check(unknown.get("error") instanceof Map<?, ?> e && "badRequest".equals(e.get("kind")), "an unknown command is a bad request");
        }
        System.out.println("ALL JAVA PILOT CHECKS PASSED");
    }
}
