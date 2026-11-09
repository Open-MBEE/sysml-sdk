package org.openmbee.sysml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A backend over an engine that speaks the SDK's engine protocol (docs/engine-protocol.md): the engine
 * runs as a process and exchanges one JSON request and one JSON answer per line over its standard
 * input and output.
 *
 * <pre>
 * try (EngineBackend engine = EngineBackend.start(List.of("my-engine", "--serve"), List.of("vehicle.sysml"), null, true, null)) {
 *     Model model = new Model(engine);
 * }
 * </pre>
 *
 * <p>Errors: a property the element's metaclass does not have reads as absent; a member the engine
 * does not answer raises {@link NotAnsweredException}; a reference it could not resolve raises
 * {@link UnresolvedReferenceException}; an element no longer known raises {@link GoneException}; a
 * failure inside the engine, an operation the element's metaclass does not have, or an engine that
 * cannot start raises {@link BackendException}. Not thread-safe.
 */
public class EngineBackend implements Backend, AutoCloseable {
    /** The version of the engine protocol this SDK speaks. */
    public static final int PROTOCOL = 1;

    private final Process process;
    private final BufferedReader out;
    private final Writer in;
    private final String engine;
    private final String version;
    private Object stats;
    private long next;
    private boolean closed;

    protected EngineBackend(Process process, BufferedReader out, Writer in, Map<?, ?> ready) {
        this.process = process;
        this.out = out;
        this.in = in;
        this.engine = ready.get("engine") instanceof String s ? s : "the engine";
        this.version = ready.get("version") instanceof String s ? s : "";
    }

    /**
     * Start the engine {@code command} (the program and its arguments) and read a model with it: files
     * and directories of {@code .sysml} and {@code .kerml} files, and in-memory sources (unit name to
     * text). {@code elaborate} asks for the implied relationships; {@code library} names a standard
     * library directory (null: the engine's own).
     */
    public static EngineBackend start(List<String> command, List<String> paths, Map<String, String> sources,
                                      boolean elaborate, String library) {
        Launched l = launch(command);
        EngineBackend backend = new EngineBackend(l.process, l.out, l.in, l.ready);
        backend.openModel(paths, sources, elaborate, library);
        return backend;
    }

    /** A started engine, ready. */
    protected record Launched(Process process, BufferedReader out, Writer in, Map<?, ?> ready) {}

    /** Start the engine and read its first line, which says whether it is ready. */
    protected static Launched launch(List<String> command) {
        Process process;
        try {
            process = new ProcessBuilder(command).redirectError(ProcessBuilder.Redirect.INHERIT).start();
        } catch (IOException e) {
            throw new BackendException("cannot start the engine " + command.get(0) + ": " + e.getMessage());
        }
        BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        Writer in = new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8);
        String line;
        try {
            line = out.readLine();
        } catch (IOException e) {
            process.destroyForcibly();
            throw new BackendException("the engine " + command.get(0) + " did not start: " + e.getMessage());
        }
        Object parsed;
        try {
            parsed = Json.parse(line == null ? "" : line);
        } catch (RuntimeException e) {
            process.destroyForcibly();
            throw new BackendException("the engine " + command.get(0) + " did not start (it said '" + line + "')");
        }
        if (!(parsed instanceof Map<?, ?> ready) || !Boolean.TRUE.equals(ready.get("ready"))) {
            String why = parsed instanceof Map<?, ?> m && m.get("error") instanceof Map<?, ?> e ? String.valueOf(e.get("message")) : line;
            waitFor(process);
            throw new BackendException("the engine is not available: " + why);
        }
        if (!(ready.get("protocol") instanceof Number n) || n.intValue() != PROTOCOL) {
            process.destroyForcibly();
            throw new BackendException("the engine speaks protocol " + ready.get("protocol") + ", this SDK " + PROTOCOL);
        }
        return new Launched(process, out, in, ready);
    }

    /** The {@code open} request: read the model. */
    protected void openModel(List<String> paths, Map<String, String> sources, boolean elaborate, String library) {
        List<Object> absolute = new ArrayList<>();
        for (String p : paths == null ? List.<String>of() : paths) absolute.add(Path.of(p).toAbsolutePath().toString());
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("paths", absolute);
        fields.put("sources", sources == null ? Map.of() : sources);
        fields.put("elaborate", elaborate);
        fields.put("library", library);
        try {
            stats = ask("open", fields);
        } catch (RuntimeException e) {
            close();
            throw e;
        }
    }

    /** The engine's name, as it announced it. */
    public String engine() {
        return engine;
    }

    /** The engine's version, as it announced it. */
    public String version() {
        return version;
    }

    /** What the engine said about opening the model (engine-specific). */
    public Object stats() {
        return stats;
    }

    @Override
    public void close() {
        if (closed) return;
        try {
            if (process.isAlive()) send(Map.of("id", 0, "cmd", "close"));
        } catch (RuntimeException ignored) {
            // ended already
        }
        closed = true;
        waitFor(process);
    }

    private static void waitFor(Process process) {
        try {
            if (!process.waitFor(30, TimeUnit.SECONDS)) process.destroyForcibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
        }
    }

    private Map<?, ?> send(Map<String, Object> request) {
        if (!process.isAlive()) throw new BackendException("the " + engine + " process has ended (status " + process.exitValue() + ")");
        String line;
        try {
            in.write(Json.write(request));
            in.write('\n');
            in.flush();
            line = out.readLine();
        } catch (IOException e) {
            throw new BackendException("the " + engine + " process failed: " + e.getMessage());
        }
        if (line == null) throw new BackendException("the " + engine + " process ended while answering");
        return (Map<?, ?>) Json.parse(line);
    }

    private static final class NotApplicable extends SdkException {
        NotApplicable(String message) {
            super(message);
        }
    }

    private Object ask(String cmd, Map<String, Object> fields) {
        if (closed) throw new SdkException("this " + engine + " backend is closed");
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("id", ++next);
        request.put("cmd", cmd);
        request.putAll(fields);
        Map<?, ?> answer = send(request);
        if (!(answer.get("error") instanceof Map<?, ?> error)) return answer.get("ok");
        String kind = String.valueOf(error.get("kind"));
        String message = String.valueOf(error.get("message"));
        throw switch (kind) {
            case "gone" -> new GoneException(message);
            case "notApplicable" -> new NotApplicable(message);
            case "refused" -> new NotAnsweredException(engine + ": " + message);
            case "invalid" -> new RefusedException(engine + ": " + message);
            case "unresolved" -> new UnresolvedReferenceException(engine + ": " + message);
            case "badRequest" -> new SdkException(message);
            default -> new BackendException(engine + ": " + message);
        };
    }

    // -- the backend contract --------------------------------------------------------------------

    @Override
    public Object get(String handle, String prop) {
        try {
            return ask("get", Map.of("h", handle, "prop", prop));
        } catch (NotApplicable e) {
            return null;  // not a member of this metaclass: absent
        }
    }

    @Override
    public String metaclass(String handle) {
        return (String) ask("metaclass", Map.of("h", handle));
    }

    @Override
    public String elementId(String handle) {
        Object id = ask("elementId", Map.of("h", handle));
        return id == null ? "" : (String) id;
    }

    @Override
    public String resolve(String qualifiedName) {
        return (String) ask("resolve", Map.of("name", qualifiedName));
    }

    @Override
    public Iterable<String> allHandles() {
        List<String> out = new ArrayList<>();
        if (ask("allHandles", Map.of()) instanceof List<?> l) for (Object h : l) out.add((String) h);
        return out;
    }

    /** A specification operation, evaluated by the engine. Elements are passed as references, lists
     *  element by element. */
    @Override
    public Object call(String handle, String op, List<Object> args) {
        List<Object> given = new ArrayList<>(args.size());
        for (Object a : args) given.add(argument(a));
        try {
            return ask("call", Map.of("h", handle, "op", op, "args", given));
        } catch (NotApplicable e) {
            throw new BackendException(engine + ": " + e.getMessage());
        }
    }

    /** A convenience member, by the protocol's {@code find}: its name and its arguments by name. */
    @Override
    public Object find(String handle, Find member, List<Object> args) {
        try {
            return ask("find", Map.of("h", handle, "member", member.name(), "args", named(member, args)));
        } catch (NotApplicable e) {
            throw new BackendException(engine + ": " + e.getMessage());
        }
    }

    /** A convenience member on each element, by the protocol's {@code findEach}: one request. */
    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findEach(List<String> handles, Find member, List<Object> args) {
        try {
            return (List<Object>) ask("findEach", Map.of("hs", handles, "member", member.name(), "args", named(member, args)));
        } catch (NotApplicable e) {
            throw new BackendException(engine + ": " + e.getMessage());
        }
    }

    /** A convenience member's arguments by parameter name, as the protocol passes them. */
    static Map<String, Object> named(Find member, List<Object> args) {
        Map<String, Object> named = new java.util.LinkedHashMap<>();
        for (int i = 0; i < member.paramNames().length; i++) {
            Object a = args.get(i);
            named.put(member.paramNames()[i], a instanceof Scope s ? s.literal()
                : a instanceof VisibilityFilter v ? v.literal() : argument(a));
        }
        return named;
    }

    private static Object argument(Object a) {
        if (a instanceof Element el) return Map.of("@id", el.$handle());
        if (a instanceof List<?> l) {
            List<Object> out = new ArrayList<>(l.size());
            for (Object x : l) out.add(argument(x));
            return out;
        }
        return a;
    }
}
