// Smoke test + conformance runner for the Java SDK backend.
// Mirrors ../../demo.py; any failure throws and exits non-zero.
package org.openmbee.sysml.checks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.openmbee.sysml.Element;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotImplementedInToolkitException;
import org.openmbee.sysml.PayloadLibrary;
import org.openmbee.sysml.UnresolvedReferenceException;
import org.openmbee.sysml.classes.AttributeUsage;
import org.openmbee.sysml.classes.CalculationDefinition;
import org.openmbee.sysml.classes.Classifier;
import org.openmbee.sysml.classes.Definition;
import org.openmbee.sysml.classes.Feature;
import org.openmbee.sysml.classes.MetadataUsage;
import org.openmbee.sysml.classes.Namespace;
import org.openmbee.sysml.classes.PartDefinition;
import org.openmbee.sysml.classes.PartUsage;
import org.openmbee.sysml.classes.Subclassification;
import org.openmbee.sysml.classes.Type;
import org.openmbee.sysml.classes.Usage;

public final class Checks {
    private Checks() { }

    private static void check(boolean cond, String what) {
        if (!cond)
            throw new AssertionError("FAILED: " + what);
    }

    private static Path findRepoRoot() {
        Path d = Path.of("").toAbsolutePath();
        while (d != null) {
            if (Files.exists(d.resolve("metamodel.json")))
                return d;
            d = d.getParent();
        }
        throw new IllegalStateException("metamodel.json not found above cwd");
    }

    public static void main(String[] args) throws Exception {
        Path root = findRepoRoot();
        Conformance.run(root.resolve("metamodel.json"));
        runSmoke(Files.readString(root.resolve("data").resolve("bigger.full.json")));
        System.out.println();
        System.out.println("ALL JAVA-BACKEND SMOKE TESTS PASSED");
    }

    private static void runSmoke(String payload) {
        Model m = Model.fromFullJson(payload);

        // --- typed resolution and hierarchy -------------------------------
        Element vehicle = m.resolve("Demo::Vehicle");
        System.out.println("resolve: " + vehicle);
        check(vehicle instanceof PartDefinition, "PartDefinition");
        check(vehicle instanceof Definition && vehicle instanceof Classifier
            && vehicle instanceof Type && vehicle instanceof Namespace,
            "hierarchy chain");
        check(!(vehicle instanceof Feature), "not Feature");
        System.out.println(
            "instanceof chain: PartDefinition < Definition < Classifier < Type < Namespace OK");

        // --- navigation ----------------------------------------------------
        List<?> feats = (List<?>) ((PartDefinition) vehicle).getOwnedFeature();
        StringBuilder fl = new StringBuilder();
        for (Object f : feats)
            fl.append(fl.isEmpty() ? "" : ", ")
              .append(((Element) f).$metaclass()).append(':')
              .append(((Feature) f).getDeclaredName());
        System.out.println("ownedFeature: " + fl);
        check(feats.size() == 7, "7 features");
        Feature wheel = null;
        for (Object f : feats)
            if ("wheel".equals(((Feature) f).getDeclaredName()))
                wheel = (Feature) f;
        check(wheel != null && wheel.$metaclass().equals("PartUsage"), "wheel");
        check(wheel.getOwner() == vehicle, "owner minted from cache");
        check(Boolean.TRUE.equals(wheel.getIsComposite()), "isComposite");
        check("Demo::Vehicle".equals(((PartDefinition) vehicle).getQualifiedName()),
            "qualifiedName");

        Element baseDef = m.resolve("Demo::Base");
        List<?> sub = (List<?>) ((Classifier) vehicle).getOwnedSubclassification();
        check(sub.size() == 1
            && ((Subclassification) sub.get(0)).getSuperclassifier() == baseDef,
            "ownedSubclassification -> Base");

        Usage vUsage = (Usage) m.resolve("Demo::v");
        List<?> defs = (List<?>) vUsage.getDefinition();
        check(defs.size() == 1 && defs.get(0) == vehicle, "v.definition");
        System.out.println("v.definition -> Vehicle OK");

        // --- reads pass the backend value through unchanged -----------------
        // (no client-side check: a member the SysML Toolkit does not implement says so itself)
        check(((PartDefinition) vehicle).getOwnedPart().isEmpty(), "ownedPart == []");
        check(((Type) vehicle).getInheritedFeature().isEmpty(), "inheritedFeature == []");
        System.out.println(
            "unimplemented derivations -> [] (backend value, no second-guessing)");

        // --- operation stubs are honest ------------------------------------
        try {
            ((Type) vehicle).specializes((Type) baseDef);
            check(false, "expected NotImplementedInToolkitException");
        } catch (NotImplementedInToolkitException e) {
            System.out.println("Type.specializes() -> NotImplementedInToolkit (as designed)");
        }

        // (read-only guard is compile-time in Java: no setters are generated)

        // --- model-wide queries --------------------------------------------
        int parts = m.elementsOfType(PartUsage.class).size();
        int usages = m.elementsOfType(Usage.class).size();
        System.out.println("PartUsage: " + parts + ", all Usage subtypes: " + usages);
        check(usages > parts, "subtype counts");

        Element calc = m.resolve("Demo::Vehicle::K");
        check(calc instanceof CalculationDefinition, "K is CalculationDefinition");
        List<?> inputs = (List<?>) ((CalculationDefinition) calc).getInput();
        check(inputs.size() == 1
            && "x".equals(((Feature) inputs.get(0)).getDeclaredName()), "K.input");
        System.out.println("K.input -> [x] OK");

        // --- collision rule: MetadataUsage.metaclass is the SPEC member ----
        String metaPayload = """
            [
              {"@id": "mc1", "@type": "Metaclass", "declaredName": "Safety",
               "qualifiedName": "M::Safety"},
              {"@id": "m1", "@type": "MetadataUsage", "qualifiedName": "M::stubbed",
               "metaclass": null},
              {"@id": "m2", "@type": "MetadataUsage", "qualifiedName": "M::filled",
               "metaclass": {"@id": "mc1"}}
            ]
            """;
        Model mm = Model.fromFullJson(metaPayload);
        MetadataUsage mu = (MetadataUsage) mm.element("m1");
        check(mu.$metaclass().equals("MetadataUsage"), "runtime accessor ($-prefixed)");
        check(mu.getMetaclass() == null, "spec member: backend value passed through");
        check(((MetadataUsage) mm.element("m2")).getMetaclass() == mm.element("mc1"),
            "filled metaclass re-check");
        System.out.println("MetadataUsage: .$metaclass() (runtime) vs .getMetaclass() (spec) OK");

        // Elements come in document order; an id given twice keeps its first place and its last
        // element.
        Model ordered = Model.fromFullJson("""
            [
              {"@id": "z", "@type": "Package", "declaredName": "first"},
              {"@id": "a", "@type": "Package", "declaredName": "A"},
              {"@id": "z", "@type": "Package", "declaredName": "second"}
            ]
            """);
        List<String> ids = new java.util.ArrayList<>();
        for (Element e : ordered.all())
            ids.add(e.$id());
        check(ids.equals(List.of("z", "a")), "document order: " + ids);
        check("second".equals(ordered.element("z").getDeclaredName()), "the last element of an id");
        System.out.println("payload elements in document order OK");

        checkPayloadLibrary();
    }

    /** A payload model with a library: references the model does not hold resolve in the library,
     * the library's elements are not the model's, the model wins an id or name both have, and a
     * reference in neither still throws. The same checks run in every language. */
    private static void checkPayloadLibrary() {
        PayloadLibrary library = PayloadLibrary.fromJson("""
            [
              {"@id": "lib", "@type": "LibraryPackage", "qualifiedName": "ScalarValues", "isLibraryElement": true},
              {"@id": "real", "@type": "DataType", "qualifiedName": "ScalarValues::Real", "declaredName": "Real",
               "owner": {"@id": "lib"}, "isLibraryElement": true},
              {"@id": "clash", "@type": "Package", "declaredName": "from the library"},
              {"@id": "libP", "@type": "Package", "qualifiedName": "P"}
            ]
            """);
        String model = """
            [
              {"@id": "pkg", "@type": "Package", "qualifiedName": "P", "declaredName": "P"},
              {"@id": "mass", "@type": "AttributeUsage", "qualifiedName": "P::mass", "owner": {"@id": "pkg"},
               "type": [{"@id": "real"}], "definition": [{"@id": "gone"}]},
              {"@id": "clash", "@type": "Package", "declaredName": "from the model"}
            ]
            """;
        Model withLibrary = Model.fromFullJson(model, library);
        AttributeUsage mass = (AttributeUsage) withLibrary.element("mass");
        Type real = mass.getType().get(0);
        check("Real".equals(real.getDeclaredName()) && Boolean.TRUE.equals(real.getIsLibraryElement()), "library element");
        check("ScalarValues".equals(real.getOwner().getQualifiedName()), "library owner");
        check(real.equals(withLibrary.resolve("ScalarValues::Real")), "resolve into the library");
        List<String> all = new java.util.ArrayList<>();
        for (Element e : withLibrary.all())
            all.add(e.$id());
        check(all.equals(List.of("pkg", "mass", "clash")), "all() lists the library: " + all);
        List<String> roots = new java.util.ArrayList<>();
        for (Element e : withLibrary.roots())
            roots.add(e.$id());
        check(roots.equals(List.of("pkg", "clash")), "roots() lists the library: " + roots);
        check("from the model".equals(withLibrary.element("clash").getDeclaredName()), "the model's id wins");
        check("pkg".equals(withLibrary.resolve("P").$id()), "the model's name wins");
        check(throwsUnresolved(() -> mass.getDefinition()), "a reference in neither");
        Model withoutLibrary = Model.fromFullJson(model);
        check(throwsUnresolved(() -> ((AttributeUsage) withoutLibrary.element("mass")).getType()), "no library, no resolution");
        check(withoutLibrary.resolve("ScalarValues::Real") == null, "no library, no name");
        System.out.println("payload model with a library OK");
    }

    private static boolean throwsUnresolved(Runnable read) {
        try {
            read.run();
        } catch (UnresolvedReferenceException e) {
            return true;
        }
        return false;
    }
}
