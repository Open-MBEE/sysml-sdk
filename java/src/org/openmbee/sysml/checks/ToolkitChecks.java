// Java reading directly from the SysML Toolkit through the binding library.
// Skips itself when the library is not built, so the payload checks still run everywhere; with
// SYSML_REQUIRE_TOOLKIT=1 a missing library is a failure instead.
package org.openmbee.sysml.checks;

import java.util.List;
import java.util.Map;

import org.openmbee.sysml.Element;
import org.openmbee.sysml.ToolkitBackend;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotImplementedInToolkitException;
import org.openmbee.sysml.SdkException;
import org.openmbee.sysml.classes.ConnectionUsage;
import org.openmbee.sysml.classes.Feature;
import org.openmbee.sysml.classes.Membership;
import org.openmbee.sysml.classes.Package;
import org.openmbee.sysml.classes.PartDefinition;
import org.openmbee.sysml.classes.PartUsage;
import org.openmbee.sysml.classes.RequirementDefinition;
import org.openmbee.sysml.classes.Type;

public final class ToolkitChecks {
    private static void check(boolean cond, String what) {
        if (!cond) throw new AssertionError("FAILED: " + what);
    }

    /** A check that meets the SysML Toolkit's refusal of `member` first
     *  (NotImplementedInToolkitException naming it): an expected failure, reported as one. It
     *  fails when it passes (the SysML Toolkit answers now: remove the expectation) or when it
     *  stops on anything else. */
    private static void expectRefusal(String member, String reason, String what, Runnable body) {
        try {
            body.run();
        } catch (NotImplementedInToolkitException e) {
            if (refusedMember(e.getMessage()).equals(member)) {
                System.out.println("expected failure: " + what + ": the SysML Toolkit refuses " + member + " (" + reason + ")");
                return;
            }
            throw e;
        }
        throw new AssertionError("FAILED: " + what + ": the SysML Toolkit answers " + member + " now; remove the expected refusal");
    }

    /** The member a SysML Toolkit refusal names (`Type::inheritedMemberships()` gives
     *  `inheritedMemberships()`). */
    private static String refusedMember(String message) {
        int i = message.indexOf(" is not answered by the SysML Toolkit");
        if (i < 0) return "";
        String head = message.substring(0, i).trim();
        int j = head.lastIndexOf("::");
        return j < 0 ? head : head.substring(j + 2);
    }

    public static void main(String[] args) throws Exception {
        try {
            ToolkitBackend.Library.defaultPath();
        } catch (SdkException e) {
            if ("1".equals(System.getenv("SYSML_REQUIRE_TOOLKIT")))
                throw new AssertionError("SYSML_REQUIRE_TOOLKIT=1 but " + e.getMessage());
            System.out.println("SysML Toolkit checks skipped: binding library not built");
            return;
        }
        String source = "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass; } part def Sports :> Car { part spoiler; } part def Link { part a; part b; connect a to b; } }";
        try (ToolkitBackend be = ToolkitBackend.openSources(Map.of("inline.sysml", source))) {
            Model m = new Model(be);

            Element car = m.resolve("Demo::Car");
            check(car instanceof PartDefinition, "Car is a PartDefinition");
            PartDefinition carDef = (PartDefinition) car;
            check("Demo::Car".equals(carDef.getQualifiedName()), "qualifiedName");
            check(carDef.getOwner() instanceof Package, "owner is the package");

            List<Feature> feats = carDef.getOwnedFeature();
            check(feats.size() == 2 && "wheels".equals(feats.get(0).getDeclaredName()), "ownedFeature");
            check(feats.get(0) instanceof PartUsage, "wheels is a PartUsage");
            expectRefusal("type", "it waits on implied relationships", "type", () -> {
                List<Type> types = feats.get(0).getType();
                check(types.size() == 1 && "Wheel".equals(types.get(0).getDeclaredName()), "type");
            });
            check(Boolean.TRUE.equals(feats.get(0).getIsComposite()), "isComposite");

            PartDefinition sports = (PartDefinition) m.resolve("Demo::Sports");
            expectRefusal("inheritedFeature", "it waits on implied relationships", "inheritedFeature", () -> {
                List<Feature> inherited = sports.getInheritedFeature();
                check(inherited.size() == 2, "inheritedFeature computed by the SysML Toolkit: " + inherited.size());
            });

            check(car == m.resolve("Demo::Car"), "wrappers are cached by handle");
            check(carDef.$id().length() == 36, "elementId is a UUID");


            // Operations, through the SysML Toolkit's operation entry point; it refuses what it
            // cannot answer as specified.
            check("Car".equals(carDef.effectiveName()), "effectiveName");
            expectRefusal("connectorEnd", "it waits on the implied end redefinitions", "an unnamed connection end is named by the end it redefines", () -> {
                ConnectionUsage connection = null;
                for (Feature f : ((PartDefinition) m.resolve("Demo::Link")).getOwnedFeature())
                    if (f instanceof ConnectionUsage c) connection = c;
                Feature end = connection.getConnectorEnd().get(0);
                check(end.getDeclaredName() == null && "source".equals(end.effectiveName()),
                    "an unnamed connection end is named by the end it redefines: " + end.effectiveName());
            });
            expectRefusal("inheritedMemberships()", "its evidence is incomplete", "inheritedMemberships", () -> {
                List<Membership> memberships = sports.inheritedMemberships(List.of(), List.of(), false);
                check(memberships.size() == 2 && "wheels".equals(memberships.get(0).getMemberName()), "inheritedMemberships");
            });
            boolean opRefused = false;
            try {
                sports.isCompatibleWith(carDef);
            } catch (NotImplementedInToolkitException e) {
                opRefused = e.getMessage().contains("isCompatibleWith");
            }
            check(opRefused, "an operation the SysML Toolkit cannot answer as specified is refused");

            check(be.fullJson().startsWith("["), "fullJson");
        }

        // Text beyond ASCII, a requirement's text (a list of strings), the members every element
        // has, and the top-level namespaces.
        String sizesSource = "package Sizes { doc /* Größe und Wärme */ requirement def Light { doc /* Keep it light. */ } part def Last; }";
        try (ToolkitBackend be = ToolkitBackend.openSources(Map.of("größe.sysml", sizesSource))) {
            Model sizes = new Model(be);
            check(sizes.resolve("Sizes::Last") instanceof PartDefinition, "a source with text beyond ASCII loads whole");
            check(sizes.resolve("Sizes").getDocumentation().get(0).getBody().contains("Größe und Wärme"),
                "text beyond ASCII comes back intact");
            RequirementDefinition light = (RequirementDefinition) sizes.resolve("Sizes::Light");
            List<String> text = light.getText();
            check(text.size() == 1 && text.get(0).startsWith("Keep it light."), "a requirement's text is a list of strings: " + text);
            Element owner = light.getOwner();
            check("Sizes".equals(owner.getName()), "the members of Element, read on a plain Element");
            boolean found = false;
            for (Element root : sizes.roots())
                for (Element member : ((org.openmbee.sysml.classes.Namespace) root).getOwnedMember())
                    found |= "Sizes".equals(member.getName());
            check(found, "roots");
        }
        System.out.println("ALL JAVA SYSML TOOLKIT CHECKS PASSED");
    }
}
