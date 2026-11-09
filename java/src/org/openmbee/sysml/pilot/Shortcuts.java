package org.openmbee.sysml.pilot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarFile;

import org.openmbee.sysml.Json;

/**
 * Shortcut bodies for the convenience members: a plugin jar, compiled against one Pilot version, whose
 * bodies answer members through the Pilot's own properties and utilities where those give the
 * member's answer. The bodies of {@link Convenience}, which read what each member's OCL reads, are the
 * reference they are checked against.
 *
 * <p>{@code SYSML_PILOT_PLUGIN} names the plugin jar, and {@code SYSML_PILOT_CONVENIENCE} chooses how
 * members are answered:
 * <ul>
 * <li>{@code reference}: by the reference bodies only (the default without a plugin);</li>
 * <li>{@code shortcut}: by the plugin's body where it has one for the member and its arguments, else
 *     by the reference (the default with a plugin);</li>
 * <li>{@code compare}: by both, where the plugin has a body. Equal answers are given; where they
 *     differ, or one side refuses, the call is refused ({@code refused}) with both outcomes named, so a
 *     difference is never silent.</li>
 * </ul>
 * The counts per member (agreed, diverged, refused by both, refused by one, no shortcut) are answered
 * by the protocol's {@code diagnostics} request and, where {@code SYSML_PILOT_DIAGNOSTICS} names a
 * file, written to it as JSON whenever an engine closes and when the JVM exits.
 *
 * <p>The plugin's classes are loaded by a class loader whose parent is the Pilot's, as its bodies are
 * compiled against the Pilot; the engine reaches its entry point by name, passing only Java's own
 * types. The plugin names the Pilot version it was compiled against ({@code Pilot-Version} in its
 * manifest); a different Pilot refuses it.
 */
final class Shortcuts {
    enum Mode { REFERENCE, SHORTCUT, COMPARE }

    static final String PLUGIN_VARIABLE = "SYSML_PILOT_PLUGIN";
    static final String MODE_VARIABLE = "SYSML_PILOT_CONVENIENCE";
    static final String DIAGNOSTICS_VARIABLE = "SYSML_PILOT_DIAGNOSTICS";
    static final String ENTRY_CLASS = "org.openmbee.sysml.pilot.plugin.ConvenienceShortcuts";

    /** Counts per member, for every engine in this JVM. */
    private static final Map<String, long[]> COUNTS = new TreeMap<>();
    private static final String[] OUTCOMES = {"agreed", "diverged", "refusedByBoth", "refusedByOne", "noShortcut"};
    private static final int AGREED = 0, DIVERGED = 1, REFUSED_BY_BOTH = 2, REFUSED_BY_ONE = 3, NO_SHORTCUT = 4;
    private static final Map<Path, Shortcuts> LOADED = new HashMap<>();
    private static Mode lastMode = Mode.REFERENCE;
    private static String lastPlugin;
    private static boolean hooked;

    /** How the engine's value conversion is reached: the protocol's shape of a body's answer. */
    interface Conversion {
        Object json(Object v) throws ReflectiveOperationException;
    }

    /** A body that answers in the protocol's shapes or throws {@link PilotException}. */
    interface Body {
        Object call() throws ReflectiveOperationException;
    }

    private final Mode mode;
    private final Path jar;
    private final Method find;
    private final Object none;

    private Shortcuts(Mode mode, Path jar, Method find, Object none) {
        this.mode = mode;
        this.jar = jar;
        this.find = find;
        this.none = none;
    }

    /** The shortcuts the environment asks for over {@code pilot}, or null to answer by the reference only. */
    static Shortcuts fromEnvironment(Pilot pilot) {
        String plugin = System.getenv(PLUGIN_VARIABLE);
        String chosen = System.getenv(MODE_VARIABLE);
        Mode mode;
        if (chosen == null || chosen.isEmpty()) {
            mode = plugin == null || plugin.isEmpty() ? Mode.REFERENCE : Mode.SHORTCUT;
        } else {
            switch (chosen) {
                case "reference": mode = Mode.REFERENCE; break;
                case "shortcut": mode = Mode.SHORTCUT; break;
                case "compare": mode = Mode.COMPARE; break;
                default: throw new PilotException("unavailable", MODE_VARIABLE + "=" + chosen + ": reference, shortcut or compare");
            }
        }
        synchronized (Shortcuts.class) {
            lastMode = mode;
            lastPlugin = mode == Mode.REFERENCE ? null : plugin;
        }
        if (mode == Mode.REFERENCE) return null;
        if (plugin == null || plugin.isEmpty())
            throw new PilotException("unavailable", MODE_VARIABLE + "=" + chosen + " needs the plugin jar in " + PLUGIN_VARIABLE);
        return load(pilot, Path.of(plugin).toAbsolutePath().normalize(), mode);
    }

    private static synchronized Shortcuts load(Pilot pilot, Path jar, Mode mode) {
        Shortcuts known = LOADED.get(jar);
        if (known != null && known.mode == mode) return known;
        if (!Files.isRegularFile(jar)) throw new PilotException("unavailable", "the plugin jar " + jar + " is not a file");
        String compiledFor;
        try (JarFile f = new JarFile(jar.toFile())) {
            compiledFor = f.getManifest() == null ? null : f.getManifest().getMainAttributes().getValue("Pilot-Version");
        } catch (IOException e) {
            throw new PilotException("unavailable", "cannot read the plugin jar " + jar + ": " + e);
        }
        if (compiledFor == null)
            throw new PilotException("unavailable", "the plugin jar " + jar + " does not name the Pilot it was compiled against (Pilot-Version)");
        if (!compiledFor.equals(pilot.version()))
            throw new PilotException("unavailable", "the plugin jar " + jar + " was compiled against the Pilot " + compiledFor
                + ", not " + pilot.version() + ": build it against this Pilot");
        try {
            ClassLoader loader = new URLClassLoader("sysml-pilot-plugin", new URL[] {jar.toUri().toURL()}, pilot.loader());
            Class<?> entry = loader.loadClass(ENTRY_CLASS);
            Method find = entry.getMethod("find", String.class, Object.class, Map.class);
            Object none = entry.getField("NONE").get(null);
            Shortcuts s = new Shortcuts(mode, jar, find, none);
            LOADED.put(jar, s);
            hookDiagnostics();
            return s;
        } catch (ReflectiveOperationException | LinkageError | IOException e) {
            throw new PilotException("unavailable", "the plugin jar " + jar + " cannot be loaded: " + e);
        }
    }

    /**
     * The member's answer in this mode: the plugin's ({@code args} are the request's, an element as the
     * Pilot's object), the reference's, or in compare mode both, checked against each other.
     */
    Object find(String member, Object element, Map<String, Object> args, Body reference, Conversion conversion)
            throws ReflectiveOperationException {
        Object raw;
        try {
            raw = find.invoke(null, member, element, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            PilotException failed = new PilotException("failed", member + ": the shortcut raised " + cause);
            if (mode == Mode.SHORTCUT) throw failed;
            return compared(member, null, failed, reference);
        }
        if (raw == none) {
            if (mode == Mode.COMPARE) count(member, NO_SHORTCUT);
            return reference.call();
        }
        Object answer = null;
        PilotException refused = null;
        try {
            answer = conversion.json(raw);
        } catch (PilotException e) {
            refused = new PilotException(e.kind(), member + ": " + e.getMessage());
        }
        if (mode == Mode.SHORTCUT) {
            if (refused != null) throw refused;
            return answer;
        }
        return compared(member, answer, refused, reference);
    }

    private Object compared(String member, Object shortcut, PilotException shortcutRefused, Body reference)
            throws ReflectiveOperationException {
        Object answer = null;
        PilotException refused = null;
        try {
            answer = reference.call();
        } catch (PilotException e) {
            if (e.kind().equals("badRequest") || e.kind().equals("notApplicable")) throw e;
            refused = e;
        }
        if (refused != null && shortcutRefused != null) {
            count(member, REFUSED_BY_BOTH);
            throw refused;
        }
        if (refused == null && shortcutRefused == null) {
            if (answer == null ? shortcut == null : answer.equals(shortcut)) {
                count(member, AGREED);
                return answer;
            }
            count(member, DIVERGED);
            throw new PilotException("refused", "diverged: the shortcut answers " + brief(shortcut) + ", the reference "
                + brief(answer));
        }
        count(member, REFUSED_BY_ONE);
        throw new PilotException("refused", "diverged: "
            + (refused == null ? "the shortcut refuses (" + shortcutRefused.kind() + ": " + shortcutRefused.getMessage()
                + "), the reference answers " + brief(answer)
            : "the shortcut answers " + brief(shortcut) + ", the reference refuses (" + refused.kind() + ": "
                + refused.getMessage() + ")"));
    }

    /** An answer in the protocol's shapes, cut short for a message. */
    private static String brief(Object v) {
        String s = Json.write(v);
        return s.length() > 300 ? s.substring(0, 297) + "..." : s;
    }

    private static synchronized void count(String member, int outcome) {
        COUNTS.computeIfAbsent(member, k -> new long[OUTCOMES.length])[outcome]++;
    }

    /** The mode, the plugin, and the counts per member so far in this JVM (compare mode counts). */
    static synchronized Map<String, Object> diagnostics() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("mode", lastMode.name().toLowerCase(java.util.Locale.ROOT));
        out.put("plugin", lastPlugin);
        Map<String, Object> members = new LinkedHashMap<>();
        long[] totals = new long[OUTCOMES.length];
        for (Map.Entry<String, long[]> e : COUNTS.entrySet()) {
            Map<String, Object> m = new LinkedHashMap<>();
            for (int i = 0; i < OUTCOMES.length; i++) {
                m.put(OUTCOMES[i], e.getValue()[i]);
                totals[i] += e.getValue()[i];
            }
            members.put(e.getKey(), m);
        }
        Map<String, Object> total = new LinkedHashMap<>();
        for (int i = 0; i < OUTCOMES.length; i++) total.put(OUTCOMES[i], totals[i]);
        out.put("total", total);
        out.put("members", members);
        return out;
    }

    /** Write {@link #diagnostics} to the file {@code SYSML_PILOT_DIAGNOSTICS} names, if any. */
    static void writeDiagnostics() {
        String file = System.getenv(DIAGNOSTICS_VARIABLE);
        if (file == null || file.isEmpty()) return;
        try {
            Files.writeString(Path.of(file), Json.write(diagnostics()) + "\n", StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("sysml-pilot: cannot write the diagnostics to " + file + ": " + e);
        }
    }

    private static void hookDiagnostics() {
        if (hooked) return;
        hooked = true;
        Runtime.getRuntime().addShutdownHook(new Thread(Shortcuts::writeDiagnostics, "sysml-pilot-diagnostics"));
    }
}
