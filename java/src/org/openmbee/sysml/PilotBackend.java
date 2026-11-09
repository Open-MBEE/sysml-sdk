package org.openmbee.sysml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openmbee.sysml.pilot.Pilot;
import org.openmbee.sysml.pilot.PilotEngine;
import org.openmbee.sysml.pilot.PilotException;

/**
 * The SysML v2 Pilot Implementation backend: the Pilot reads the model in this process and computes
 * every value with its own code, behind the same {@link Backend} contract as the SysML Toolkit and
 * payload backends, so {@link Model} and the generated interfaces are unchanged.
 *
 * <pre>
 * try (PilotBackend pilot = PilotBackend.open(List.of("vehicle.sysml"))) {
 *     Model model = new Model(pilot);
 *     ...
 * }
 * </pre>
 *
 * <p>The Pilot is {@link Pilot#locate()}'s unless one is passed: {@code SYSML_PILOT_HOME}, else the
 * release the SDK is pinned to, downloaded into the user's cache on the first use (about 130 MB). It
 * reads the model with its own standard library and, by default, adds the implied relationships and
 * implicit elements of the specification's semantics, to the model and to the library. Its answers are the Pilot's, as the SysML
 * Toolkit backend's are the SysML Toolkit's: the two implementations differ in places.
 *
 * <p>Errors: a property the element's metaclass does not have reads as absent; an element no longer
 * known raises {@link GoneException}; a reference the Pilot could not resolve raises
 * {@link UnresolvedReferenceException}; a convenience member the backend does not answer raises
 * {@link NotAnsweredException}, and one whose contract gives no answer {@link RefusedException}; a
 * failure inside the Pilot, an operation the Pilot does not have, or a Pilot that cannot be found or
 * loaded raises {@link BackendException}.
 *
 * <p>The Pilot needs a Java 21 or newer runtime and a large heap for large models ({@code -Xmx4g}
 * reads the specification's examples). Not thread-safe.
 */
public final class PilotBackend implements Backend, AutoCloseable {
    private final PilotEngine engine;
    private final Map<String, Object> stats;

    private PilotBackend(PilotEngine engine, Map<String, Object> stats) {
        this.engine = engine;
        this.stats = stats;
    }

    /** Open a model over files and directories of {@code .sysml} and {@code .kerml} files. */
    public static PilotBackend open(List<String> paths) {
        return open(null, paths, null, true);
    }

    /** Open a model over in-memory sources: unit name to text. */
    public static PilotBackend openSources(Map<String, String> sources) {
        return open(null, List.of(), sources, true);
    }

    /** Open a model with {@code pilot} (null: {@link Pilot#locate()}), over files and in-memory
     *  sources; with {@code elaborate}, the Pilot adds the implied relationships and implicit
     *  elements, to the model and to its standard library. */
    public static PilotBackend open(Pilot pilot, List<String> paths, Map<String, String> sources, boolean elaborate) {
        PilotEngine engine;
        try {
            engine = new PilotEngine(pilot != null ? pilot : Pilot.locate());
        } catch (PilotException e) {
            throw translate(e);
        }
        try {
            return new PilotBackend(engine, engine.open(paths, sources, elaborate, null));
        } catch (PilotException e) {
            engine.close();
            throw translate(e);
        }
    }

    /** The Pilot this backend reads with. */
    public Pilot pilot() {
        return engine.pilot();
    }

    /** What opening took: the Pilot's version ({@code pilot}), {@code libraryMs}, {@code modelMs},
     *  and the number of {@code resources} read. */
    public Map<String, Object> stats() {
        return stats;
    }

    @Override
    public void close() {
        engine.close();
    }

    private static SdkException translate(PilotException e) {
        return switch (e.kind()) {
            case "gone" -> new GoneException(e.getMessage());
            case "unresolved" -> new UnresolvedReferenceException(e.getMessage());
            case "refused" -> new NotAnsweredException(e.getMessage());
            case "invalid" -> new RefusedException(e.getMessage());
            case "badRequest" -> new SdkException(e.getMessage());
            default -> e;  // already a BackendException
        };
    }

    // -- the backend contract --------------------------------------------------------------------

    @Override
    public Object get(String handle, String prop) {
        try {
            return engine.get(handle, prop);
        } catch (PilotException e) {
            if (e.kind().equals("notApplicable")) return null;  // not a member of this metaclass: absent
            throw translate(e);
        }
    }

    @Override
    public String metaclass(String handle) {
        try {
            return engine.metaclass(handle);
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    @Override
    public String elementId(String handle) {
        try {
            String id = engine.elementId(handle);
            return id == null ? "" : id;
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    @Override
    public String resolve(String qualifiedName) {
        try {
            return engine.resolve(qualifiedName);
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    @Override
    public Iterable<String> allHandles() {
        try {
            return engine.allHandles();
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    /** A specification operation, evaluated by the Pilot. Elements are passed as references, lists
     *  element by element; the Pilot converts numbers to its parameters' types. */
    @Override
    public Object call(String handle, String op, List<Object> args) {
        List<Object> given = new ArrayList<>(args.size());
        for (Object a : args) given.add(argument(a));
        try {
            return engine.call(handle, op, given);
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    /** A convenience member, answered by the Pilot engine's bodies over the Pilot's model. An element
     *  whose metaclass is not one of the member's contexts raises {@link BackendException}. */
    @Override
    public Object find(String handle, Find member, List<Object> args) {
        try {
            return engine.find(handle, member.name(), EngineBackend.named(member, args));
        } catch (PilotException e) {
            throw translate(e);
        }
    }

    /** A convenience member on each element, answered by the Pilot engine in one call. */
    @Override
    public List<Object> findEach(List<String> handles, Find member, List<Object> args) {
        try {
            return engine.findEach(handles, member.name(), EngineBackend.named(member, args));
        } catch (PilotException e) {
            throw translate(e);
        }
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
