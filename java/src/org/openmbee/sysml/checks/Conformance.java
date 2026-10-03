// Generated — do not edit. Source: metamodel.json via tools/gen_conformance.py.
//
// Anti-drift: TABLE_SHA256 pins the table this file was generated from;
// the checks verify the generated interfaces + runtime against the live
// table via reflection. Invoked by Checks.java.
package org.openmbee.sysml.checks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.TreeMap;

import org.openmbee.sysml.Element;
import org.openmbee.sysml.Json;
import org.openmbee.sysml.Model;
import org.openmbee.sysml.NotImplementedInToolkitException;
import org.openmbee.sysml.classes.ClassMap;

public final class Conformance {
    public static final String TABLE_SHA256 = "17644f3a5d70189d76b5ecb95c2089aeda1f93b6705f8bce5eeae1faf023758b";

    private Conformance() { }

    private static void check(boolean cond, String what) {
        if (!cond)
            throw new AssertionError("conformance FAILED: " + what);
    }

    private static void ancestors(Map<?, ?> mm, String name, Set<String> acc) {
        for (Object b : (List<?>) ((Map<?, ?>) mm.get(name)).get("bases"))
            if (acc.add((String) b))
                ancestors(mm, (String) b, acc);
    }

    // Every operation a class declares or inherits: name to parameter count.
    private static Map<String, Integer> flatOps(Map<?, ?> mm, String name) {
        Set<String> classes = new HashSet<>(List.of(name));
        ancestors(mm, name, classes);
        Map<String, Integer> ops = new TreeMap<>();
        for (String c : classes)
            for (Object opo : (List<?>) ((Map<?, ?>) mm.get(c)).get("ops"))
                ops.put((String) ((Map<?, ?>) opo).get("name"),
                    ((List<?>) ((Map<?, ?>) opo).get("params")).size());
        return ops;
    }

    // Properties are read through `get` + the specification name.
    private static String getter(String prop) {
        return "get" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
    }

    // Invoke a generated default method; unwrap reflection's
    // InvocationTargetException so SDK exceptions stay visible.
    private static void invoke(Method m, Object target, Object[] args)
            throws Exception {
        try {
            m.invoke(target, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception cause)
                throw cause;
            throw e;
        }
    }

    public static void run(Path metamodelPath) throws Exception {
        byte[] raw = Files.readAllBytes(metamodelPath);
        String digest = HexFormat.of()
            .formatHex(MessageDigest.getInstance("SHA-256").digest(raw));
        check(digest.equals(TABLE_SHA256),
            "metamodel.json changed since this harness was generated; "
            + "rerun tools/gen_conformance.py");
        Map<?, ?> mm = (Map<?, ?>) ((Map<?, ?>) Json
            .parse(new String(raw, StandardCharsets.UTF_8))).get("classes");

        List<String> names = new ArrayList<>();
        for (Object k : mm.keySet())
            names.add((String) k);
        names.sort(null);
        List<String> concrete = new ArrayList<>();
        for (String n : names)
            if (!(Boolean) ((Map<?, ?>) mm.get(n)).get("abstract"))
                concrete.add(n);

        // --- generated surface matches the table --------------------------
        for (String n : names) {
            Map<?, ?> info = (Map<?, ?>) mm.get(n);
            var t = ClassMap.TYPE_MAP.get(n);
            check(t != null, "TYPE_MAP[" + n + "] missing");
            check(ClassMap.FACTORIES.containsKey(n)
                == !(Boolean) info.get("abstract"), "factory presence for " + n);
            for (Object b : (List<?>) info.get("bases"))
                check(ClassMap.TYPE_MAP.get((String) b).isAssignableFrom(t),
                    n + " !< " + b);
            if (n.equals("Element"))
                continue; // spec root merges into the runtime Element
            Set<String> declared = new HashSet<>();
            for (Method m : t.getDeclaredMethods())
                declared.add(m.getName());
            Map<?, ?> props = (Map<?, ?>) info.get("props");
            for (Object p : props.keySet())
                check(declared.contains(getter((String) p)),
                    n + "." + getter((String) p) + "(): property getter missing");
            for (String o : flatOps(mm, n).keySet())
                check(declared.contains(o), n + "." + o + "(): operation missing");
        }

        // --- runtime honors the table -------------------------------------
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < concrete.size(); i++)
            sb.append(i == 0 ? "" : ",")
              .append("{\"@id\":\"e-").append(concrete.get(i))
              .append("\",\"@type\":\"").append(concrete.get(i)).append("\"}");
        String payload = sb.append(']').toString();
        Model model = Model.fromFullJson(payload);
        for (String n : concrete) {
            Element el = model.element("e-" + n);
            var t = ClassMap.TYPE_MAP.get(n);
            check(el.$metaclass().equals(n), "$metaclass " + n);
            check(el.$id().equals("e-" + n), "$id " + n);
            check(t.isInstance(el), n + " instance of own interface");
            Set<String> anc = new HashSet<>();
            ancestors(mm, n, anc);
            for (String a : anc)
                check(ClassMap.TYPE_MAP.get(a).isInstance(el),
                    n + " not instance of " + a);
            Map<?, ?> props = (Map<?, ?>) ((Map<?, ?>) mm.get(n)).get("props");
            for (Object po : props.keySet()) {
                Method read = t.getMethod(getter((String) po));
                // a property read never second-guesses the backend
                invoke(read, el, new Object[0]);
            }
            for (Map.Entry<String, Integer> entry : flatOps(mm, n).entrySet()) {
                String o = entry.getKey();
                int arity = entry.getValue();
                Method op = null;
                for (Method m : t.getMethods())
                    if (m.getName().equals(o) && m.getParameterCount() == arity) {
                        op = m;
                        break;
                    }
                check(op != null, n + "." + o + "(): not found");
                boolean threw = false;
                try {
                    invoke(op, el, new Object[arity]);
                } catch (NotImplementedInToolkitException e) {
                    threw = true;
                }
                check(threw, n + "." + o + "(): must throw NotImplementedInToolkit");
            }
        }

        System.out.println("conformance OK: " + names.size() + " classes, "
            + concrete.size() + " concrete, table " + TABLE_SHA256.substring(0, 12));
    }
}
