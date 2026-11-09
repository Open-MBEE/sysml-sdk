// EngineBackend against a small engine written for the check (docs/engine-protocol.md): this class,
// run again with --fake-engine. No engine install needed.
package org.openmbee.sysml.checks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmbee.sysml.BackendException;
import org.openmbee.sysml.EngineBackend;
import org.openmbee.sysml.GoneException;
import org.openmbee.sysml.Json;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotAnsweredException;
import org.openmbee.sysml.NotImplementedInToolkitException;
import org.openmbee.sysml.UnresolvedReferenceException;
import org.openmbee.sysml.classes.Package;
import org.openmbee.sysml.classes.PartDefinition;

public final class EngineChecks {
    private static void check(boolean cond, String what) {
        if (!cond) throw new AssertionError("FAILED: " + what);
    }

    /** The fake engine: a package P owning a part definition A, answered from a table. */
    static void fakeEngine() throws Exception {
        Map<String, String[]> elements = Map.of("1", new String[] {"Package", "P", "P"}, "2", new String[] {"PartDefinition", "A", "P::A"});
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(Json.write(map("ready", true, "engine", "Test engine", "version", "1.0", "protocol", 1)));
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        for (String line; (line = in.readLine()) != null; ) {
            Map<?, ?> req = (Map<?, ?>) Json.parse(line);
            Object id = req.get("id");
            String cmd = (String) req.get("cmd");
            Object h = req.get("h");
            Object ok = null;
            String kind = null, message = null;
            if (cmd.equals("open")) ok = map("sources", ((Map<?, ?>) req.get("sources")).size(), "elaborate", req.get("elaborate"));
            else if (cmd.equals("close")) { out.println(Json.write(map("id", id, "ok", null))); return; }
            else if (cmd.equals("allHandles")) ok = List.of("1", "2");
            else if (cmd.equals("resolve")) ok = Map.of("P", "1", "P::A", "2").get(req.get("name"));
            else if (!elements.containsKey(h)) { kind = "gone"; message = "no element " + h; }
            else {
                String[] e = elements.get(h);
                switch (cmd) {
                    case "metaclass" -> ok = e[0];
                    case "elementId" -> ok = "id-" + h;
                    case "call" -> {
                        if ("effectiveName".equals(req.get("op"))) ok = e[1];
                        else { kind = "notApplicable"; message = "no operation " + req.get("op"); }
                    }
                    default -> {
                        switch (String.valueOf(req.get("prop"))) {
                            case "declaredName" -> ok = e[1];
                            case "qualifiedName" -> ok = e[2];
                            case "owner" -> ok = "2".equals(h) ? Map.of("@id", "1") : null;
                            case "ownedMember" -> ok = "1".equals(h) ? List.of(Map.of("@id", "2")) : List.of();
                            case "isAbstract" -> { kind = "refused"; message = "isAbstract is not answered"; }
                            case "ownedRelationship" -> { kind = "unresolved"; message = "a reference did not resolve"; }
                            case "boom" -> { kind = "failed"; message = "the engine broke"; }
                            default -> { kind = "notApplicable"; message = e[0] + " has no property " + req.get("prop"); }
                        }
                    }
                }
            }
            out.println(Json.write(kind == null ? map("id", id, "ok", ok) : map("id", id, "error", map("kind", kind, "message", message))));
        }
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("--fake-engine")) {
            fakeEngine();
            return;
        }
        String java = ProcessHandle.current().info().command().orElse("java");
        List<String> command = new ArrayList<>(List.of(java, "-cp", System.getProperty("java.class.path"),
            EngineChecks.class.getName(), "--fake-engine"));
        try (EngineBackend be = EngineBackend.start(command, List.of(), Map.of("a.sysml", "package P { part def A; }"), true, null)) {
            check(be.engine().equals("Test engine") && be.version().equals("1.0"), "the engine announces itself");
            check(be.stats() instanceof Map<?, ?> s && Long.valueOf(1).equals(s.get("sources")), "open answers: " + be.stats());
            Model m = new Model(be);
            PartDefinition a = (PartDefinition) m.resolve("P::A");
            check("A".equals(a.getDeclaredName()) && "P::A".equals(a.getQualifiedName()), "reads");
            check(a.getOwner() instanceof Package p && p.getOwnedMember().get(0) == a, "references");
            check("A".equals(a.effectiveName()), "operations");
            check(m.all().size() == 2 && m.resolve("Q") == null, "all and resolve");
            boolean refused = false;
            try {
                a.getIsAbstract();
            } catch (NotAnsweredException e) {
                refused = e.getMessage().startsWith("Test engine: ");
            }
            check(refused, "refused is NotAnsweredException");
            boolean unresolved = false;
            try {
                a.getOwnedRelationship();
            } catch (UnresolvedReferenceException e) {
                unresolved = true;
            }
            check(unresolved, "unresolved is UnresolvedReferenceException");
            boolean failed = false;
            try {
                be.get("2", "boom");
            } catch (BackendException e) {
                failed = e.getMessage().contains("the engine broke");
            }
            check(failed, "failed is BackendException");
            check(be.get("2", "noSuchProperty") == null, "notApplicable reads as absent");
            boolean gone = false;
            try {
                be.metaclass("9");
            } catch (GoneException e) {
                gone = true;
            }
            check(gone, "gone is GoneException");
        }
        boolean missing = false;
        try {
            EngineBackend.start(List.of("no-such-engine-for-the-check"), List.of(), null, true, null);
        } catch (BackendException e) {
            missing = e.getMessage().contains("cannot start");
        }
        check(missing, "a missing engine is a BackendException");
        check(new NotImplementedInToolkitException("x") instanceof NotAnsweredException, "the SysML Toolkit's refusals are NotAnswered");
        System.out.println("ALL JAVA ENGINE CHECKS PASSED");
    }
}
