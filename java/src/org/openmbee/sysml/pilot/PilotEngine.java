package org.openmbee.sysml.pilot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.openmbee.sysml.Json;

/**
 * The SysML v2 Pilot Implementation behind the SDK's backend contract: one model, read through the
 * Pilot's own API ({@code org.omg.sysml.interactive.SysMLInteractive} and EMF reflection), with every
 * value in the shapes the SDK's backends exchange: strings, Booleans, numbers ({@code Long},
 * {@code Double}), enumeration literals as strings, lists, and references to elements as
 * {@code {"@id": handle}}. A handle is a decimal string this engine mints for an element.
 *
 * <p>The Pilot's classes are reached by reflection through the class loader of {@link Pilot}, so the
 * SDK neither compiles against the Pilot nor shares its libraries with the application.
 *
 * <p>The same operations as text: {@link #request} takes one request of the SDK's backend protocol
 * as JSON and answers it as JSON, which is how the SDK's other languages reach the Pilot (in their
 * own process through an embedded JVM, or through {@link PilotServer}). Requests:
 * <pre>
 * {"id": n, "cmd": "open", "paths": [...], "sources": {name: text}, "elaborate": true, "library": dir}
 * {"id": n, "cmd": "metaclass" | "elementId", "h": handle}
 * {"id": n, "cmd": "get", "h": handle, "prop": name}
 * {"id": n, "cmd": "call", "h": handle, "op": name, "args": [...]}
 * {"id": n, "cmd": "find", "h": handle, "member": name, "args": {parameter: value}}
 * {"id": n, "cmd": "findEach", "hs": [handle, ...], "member": name, "args": {parameter: value}}
 * {"id": n, "cmd": "resolve", "name": qualifiedName}
 * {"id": n, "cmd": "allHandles"}
 * {"id": n, "cmd": "diagnostics"}
 * {"id": n, "cmd": "close"}
 * </pre>
 * Answers: {@code {"id": n, "ok": value}}, or {@code {"id": n, "error": {"kind": k, "message": m}}}
 * with a kind of {@link PilotException}.
 *
 * <p>Not thread-safe: one engine serves one caller at a time.
 */
public final class PilotEngine implements AutoCloseable {
    /** The version of the request protocol {@link #request} speaks (docs/engine-protocol.md). */
    public static final int PROTOCOL = 1;
    /** The engine's name in the protocol. */
    public static final String ENGINE = "Pilot Implementation";

    private final Pilot pilot;
    private final ClassLoader loader;
    private final Class<?> eObject, enumerator, element, basicEList;
    private final Method createInstance, setVerbose, loadLibrary, readAll, resolveAllInputResources, transformAll,
        transformResource, resolveName, getInputResources, getResourceSet, addResourceToIndex;
    private final Method eClass, eGet, eInvoke, eIsProxy, eProxyURI, getName, getEStructuralFeature, getEAllOperations,
        getEParameters, getEType, getInstanceClass, getLiteral, getElementId, getAllContents;
    private final Method getResources, getURI, unload, findIndex, removeDescription;

    private Object si;
    /** {@link #si} with its library, when {@link #close} hands the library on to the next engine. */
    private Kept kept;
    private Path sourceDir;

    /**
     * Whether a Pilot instance outlives its engine with its standard library loaded, for the next
     * engine in this JVM over the same Pilot and library: reading, resolving and elaborating the
     * library then happens once per JVM rather than once per model (about ten seconds a model).
     * Closing an engine takes its model out of the instance (out of the resource set, the Xtext index
     * and the input resources) before handing the instance on; a library directory whose files change
     * gets a new instance. The Pilot's elaboration of a model can, rarely, add an element inside a
     * library feature, which later models then see. {@code SYSML_PILOT_KEEP_LIBRARY=0} turns it off.
     */
    public static final boolean KEEP_LIBRARY = !"0".equals(System.getenv("SYSML_PILOT_KEEP_LIBRARY"));
    /** A Pilot instance whose library is loaded, and the library's resources. */
    private record Kept(String key, Object instance, List<Object> libraryResources) {}
    /** Instances with nothing but their library, by Pilot, library directory and its files. */
    private static final Map<String, Kept> KEPT = new HashMap<>();
    private final List<Object> objects = new ArrayList<>();
    private final Map<Object, String> handles = new IdentityHashMap<>();
    private final Map<Object, Map<String, Object>> features = new IdentityHashMap<>();
    private final Map<Object, Map<String, Object>> operations = new IdentityHashMap<>();
    /** The Pilot's metamodel interfaces by metaclass name (null where it has none), for {@link #isKind}. */
    private final Map<String, Class<?>> interfaces = new HashMap<>();
    private final Convenience convenience = new Convenience(this);
    /** The plugin's shortcut bodies and the mode they answer in, or null to answer by {@link Convenience}. */
    private final Shortcuts shortcuts;
    private static final Object NONE = new Object();

    /** An engine over {@code pilot}; {@link #open} reads the model. */
    public PilotEngine(Pilot pilot) {
        this.pilot = pilot;
        this.loader = pilot.loader();
        try {
            Class<?> interactive = loader.loadClass("org.omg.sysml.interactive.SysMLInteractive");
            createInstance = interactive.getMethod("createInstance");
            setVerbose = interactive.getMethod("setVerbose", boolean.class);
            loadLibrary = interactive.getMethod("loadLibrary", String.class);
            readAll = interactive.getMethod("readAll", String.class, boolean.class, String.class);
            resolveAllInputResources = interactive.getMethod("resolveAllInputResources");
            transformAll = interactive.getMethod("transformAll", boolean.class);
            resolveName = interactive.getMethod("resolve", String.class);
            getInputResources = interactive.getMethod("getInputResources");
            getResourceSet = interactive.getMethod("getResourceSet");
            Class<?> resourceSet = loader.loadClass("org.eclipse.emf.ecore.resource.ResourceSet");
            getResources = resourceSet.getMethod("getResources");
            addResourceToIndex = interactive.getMethod("addResourceToIndex", loader.loadClass("org.eclipse.emf.ecore.resource.Resource"));
            findIndex = loader.loadClass("org.eclipse.xtext.resource.impl.ResourceDescriptionsData$ResourceSetAdapter")
                .getMethod("findResourceDescriptionsData", resourceSet);
            removeDescription = loader.loadClass("org.eclipse.xtext.resource.impl.ResourceDescriptionsData")
                .getMethod("removeDescription", loader.loadClass("org.eclipse.emf.common.util.URI"));
            eObject = loader.loadClass("org.eclipse.emf.ecore.EObject");
            Class<?> internal = loader.loadClass("org.eclipse.emf.ecore.InternalEObject");
            Class<?> feature = loader.loadClass("org.eclipse.emf.ecore.EStructuralFeature");
            Class<?> operation = loader.loadClass("org.eclipse.emf.ecore.EOperation");
            Class<?> eList = loader.loadClass("org.eclipse.emf.common.util.EList");
            Class<?> eClassClass = loader.loadClass("org.eclipse.emf.ecore.EClass");
            Class<?> named = loader.loadClass("org.eclipse.emf.ecore.ENamedElement");
            Class<?> typed = loader.loadClass("org.eclipse.emf.ecore.ETypedElement");
            Class<?> classifier = loader.loadClass("org.eclipse.emf.ecore.EClassifier");
            Class<?> resource = loader.loadClass("org.eclipse.emf.ecore.resource.Resource");
            transformResource = loader.loadClass("org.omg.sysml.util.ElementUtil").getMethod("transformAll", resource, boolean.class);
            enumerator = loader.loadClass("org.eclipse.emf.common.util.Enumerator");
            element = loader.loadClass("org.omg.sysml.lang.sysml.Element");
            basicEList = loader.loadClass("org.eclipse.emf.common.util.BasicEList");
            eClass = eObject.getMethod("eClass");
            eGet = eObject.getMethod("eGet", feature);
            eInvoke = eObject.getMethod("eInvoke", operation, eList);
            eIsProxy = eObject.getMethod("eIsProxy");
            eProxyURI = internal.getMethod("eProxyURI");
            getName = named.getMethod("getName");
            getEStructuralFeature = eClassClass.getMethod("getEStructuralFeature", String.class);
            getEAllOperations = eClassClass.getMethod("getEAllOperations");
            getEParameters = operation.getMethod("getEParameters");
            getEType = typed.getMethod("getEType");
            getInstanceClass = classifier.getMethod("getInstanceClass");
            getLiteral = enumerator.getMethod("getLiteral");
            getElementId = element.getMethod("getElementId");
            getAllContents = resource.getMethod("getAllContents");
            getURI = resource.getMethod("getURI");
            unload = resource.getMethod("unload");
        } catch (ReflectiveOperationException | LinkageError e) {
            throw new PilotException("unavailable", pilot + " lacks the API the SDK uses (" + e + "); the SDK is tested with the Pilot "
                + Pilot.VERSION + (e instanceof UnsupportedClassVersionError ? ", which needs a Java 21 or newer runtime" : ""));
        }
        shortcuts = Shortcuts.fromEnvironment(pilot);
    }

    /** An engine over the Pilot that {@code jar} and {@code library} name, or over {@link Pilot#locate()}
     *  when both are null. For callers that reach Java by name (an embedded JVM). */
    public static PilotEngine start(String jar, String library) {
        if (jar == null && library == null) return new PilotEngine(Pilot.locate());
        if (jar == null || library == null)
            throw new PilotException("badRequest", "name both the Pilot's jar and its library directory, or neither");
        return new PilotEngine(Pilot.at(Path.of(jar), Path.of(library)));
    }

    /** An engine over the Pilot in {@code home} ({@link Pilot#in}). For callers that reach Java by name. */
    public static PilotEngine startIn(String home) {
        return new PilotEngine(Pilot.in(Path.of(home)));
    }

    public Pilot pilot() {
        return pilot;
    }

    // -- the model -------------------------------------------------------------------------------

    /**
     * Read the model: files and directories ({@code .sysml} and {@code .kerml}) and in-memory
     * sources (unit name to text), with the Pilot's standard library (or {@code library}, when not
     * null). With {@code elaborate}, the Pilot adds the implied relationships and implicit elements
     * of the specification's semantics (its {@code transformAll}) to the model and to the library, as
     * the SysML Toolkit does; the Pilot's own {@code transformAll} adds them to the model only, so the
     * library's resources are elaborated here, once per library loaded. Answers timings:
     * {@code libraryMs} (reading and, with {@code elaborate}, elaborating the library), {@code modelMs},
     * and the number of {@code resources} read; {@code libraryReused} says whether the library was one
     * an earlier engine left loaded (see {@link #KEEP_LIBRARY}).
     */
    public Map<String, Object> open(List<String> paths, Map<String, String> sources, boolean elaborate, String library) {
        if (si != null) throw new PilotException("badRequest", "this engine already holds a model; open another engine");
        List<String> inputs = new ArrayList<>();
        for (String p : paths == null ? List.<String>of() : paths) {
            Path path = Path.of(p).toAbsolutePath();
            if (!Files.exists(path)) throw new PilotException("badRequest", "model file not found: " + path);
            inputs.add(path.toString());
        }
        if (sources != null && !sources.isEmpty()) {
            try {
                sourceDir = Files.createTempDirectory("sysml-pilot-");
                int i = 0;
                for (Map.Entry<String, String> s : sources.entrySet()) {
                    String name = s.getKey().replaceAll("[^A-Za-z0-9._-]", "_");
                    if (!name.endsWith(".sysml") && !name.endsWith(".kerml")) name += ".sysml";
                    Path file = sourceDir.resolve(String.format("%03d-%s", i++, name));
                    Files.writeString(file, s.getValue());
                    inputs.add(file.toString());
                }
            } catch (IOException e) {
                throw new PilotException("failed", "cannot write the sources for the Pilot: " + e);
            }
        }
        String lib = library != null ? library : pilot.library().toString();
        String key = KEEP_LIBRARY ? pilot.jar() + "\n" + Path.of(lib).toAbsolutePath().normalize() + "\n" + fingerprint(lib)
            + (elaborate ? "\nelaborated" : "\nas read") : null;
        return inPilot(() -> {
            long t0 = System.nanoTime();
            Kept reusing = null;
            if (key != null) synchronized (KEPT) { reusing = KEPT.remove(key); }
            boolean reused = reusing != null;
            Object instance;
            List<Object> libraryResources;
            if (reused) {
                instance = reusing.instance();
                libraryResources = reusing.libraryResources();
            } else {
                instance = createInstance.invoke(null);
                setVerbose.invoke(instance, false);
                loadLibrary.invoke(instance, lib);
                // The Pilot resolves names through a resource it adds to the resource set on first use
                // and keeps for later calls. Created now, it counts among the library's resources, so
                // taking a model out (removeModel) leaves it in place for the next model.
                resolveName.invoke(instance, "Base::Anything");
                libraryResources = new ArrayList<>((List<?>) getResources.invoke(getResourceSet.invoke(instance)));
                if (elaborate) for (Object r : libraryResources) transformResource.invoke(null, r, true);
            }
            long t1 = System.nanoTime();
            for (String input : inputs) {
                if (Files.isDirectory(Path.of(input))) {
                    readAll.invoke(instance, input, true, ".sysml");
                    readAll.invoke(instance, input, true, ".kerml");
                } else {
                    readAll.invoke(instance, input, true, input.endsWith(".kerml") ? ".kerml" : ".sysml");
                }
            }
            resolveAllInputResources.invoke(instance);
            if (elaborate) transformAll.invoke(instance, true);
            long t2 = System.nanoTime();
            si = instance;
            // Only an instance that read its model is handed on; one the Pilot failed in is dropped.
            kept = key == null ? null : new Kept(key, instance, libraryResources);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("pilot", pilot.version());
            r.put("libraryReused", reused);
            r.put("libraryMs", (t1 - t0) / 1_000_000);
            r.put("modelMs", (t2 - t1) / 1_000_000);
            r.put("resources", (long) ((List<?>) getInputResources.invoke(instance)).size());
            return r;
        });
    }

    /**
     * Take {@code k}'s model out of its instance: every resource but the library's leaves the resource
     * set, the Xtext index and the input resources, unloaded. Not {@code Resource.delete}, which
     * deletes the file a resource was read from.
     *
     * <p>The Pilot's index keeps one resource per qualified name, the one added last, and removing a
     * resource drops its names from the index without bringing back what they replaced. A model that
     * declares a name its library also declares (a package {@code SI}, say) would leave that library
     * name unresolvable for every later model, so the library's resources are indexed again.
     */
    private boolean removeModel(Kept k) throws ReflectiveOperationException {
        Object resourceSet = getResourceSet.invoke(k.instance());
        Object index = findIndex.invoke(null, resourceSet);
        @SuppressWarnings("unchecked")
        List<Object> resources = (List<Object>) getResources.invoke(resourceSet);
        java.util.Set<Object> library = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        library.addAll(k.libraryResources());
        for (Object r : new ArrayList<>(resources)) {
            if (library.contains(r)) continue;
            if (index != null) removeDescription.invoke(index, getURI.invoke(r));
            unload.invoke(r);
            resources.remove(r);
        }
        ((List<?>) getInputResources.invoke(k.instance())).clear();
        for (Object r : k.libraryResources()) addResourceToIndex.invoke(k.instance(), r);
        return true;
    }

    /** The files of the library directory {@code lib}, by path, size and modification time, as a digest. */
    private static String fingerprint(String lib) {
        try (var walk = Files.walk(Path.of(lib))) {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            for (Path p : walk.filter(Files::isRegularFile).sorted().toList())
                digest.update((p + "\t" + Files.size(p) + "\t" + Files.getLastModifiedTime(p).toMillis() + "\n")
                    .getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest.digest());
        } catch (IOException | java.security.NoSuchAlgorithmException e) {
            return "unread " + System.nanoTime();  // matches no kept instance
        }
    }

    @Override
    public void close() {
        if (kept != null) {
            Kept k = kept;
            boolean cleared;
            try {
                cleared = inPilot(() -> removeModel(k));
            } catch (PilotException e) {
                cleared = false;
            }
            if (cleared) synchronized (KEPT) { KEPT.putIfAbsent(k.key(), k); }
        }
        si = null;
        kept = null;
        objects.clear();
        handles.clear();
        if (shortcuts != null) Shortcuts.writeDiagnostics();
        if (sourceDir != null) {
            try (var walk = Files.walk(sourceDir)) {
                for (Path q : walk.sorted(java.util.Comparator.reverseOrder()).toList()) Files.deleteIfExists(q);
            } catch (IOException ignored) {
                // a temporary directory; the system cleans it up eventually
            }
            sourceDir = null;
        }
    }

    // -- the backend contract --------------------------------------------------------------------

    public String metaclass(String h) {
        Object o = object(h);
        return inPilot(() -> (String) getName.invoke(eClass.invoke(o)));
    }

    public String elementId(String h) {
        Object o = object(h);
        return inPilot(() -> element.isInstance(o) ? (String) getElementId.invoke(o) : null);
    }

    /** A property by its specification name; {@code notApplicable} when the element's metaclass has none. */
    public Object get(String h, String prop) {
        Object o = object(h);
        return inPilot(() -> {
            Object ec = eClass.invoke(o);
            Object f = feature(ec, prop);
            if (f == NONE) throw new PilotException("notApplicable", getName.invoke(ec) + " has no property " + prop);
            try {
                return json(eGet.invoke(o, f));
            } catch (PilotException e) {
                if (!e.kind().equals("unresolved")) throw e;
                throw new PilotException("unresolved", getName.invoke(ec) + "." + prop + ": " + e.getMessage());
            }
        });
    }

    /** An operation by its specification name, with as many arguments as it has parameters. */
    public Object call(String h, String op, List<Object> args) {
        Object o = object(h);
        List<Object> given = args == null ? List.of() : args;
        return inPilot(() -> {
            Object ec = eClass.invoke(o);
            Object found = operation(ec, op, given.size());
            if (found == NONE)
                throw new PilotException("notApplicable", getName.invoke(ec) + " has no operation " + op + " with " + given.size() + " parameter(s)");
            List<?> params = (List<?>) getEParameters.invoke(found);
            @SuppressWarnings("unchecked")
            List<Object> values = (List<Object>) basicEList.getConstructor().newInstance();
            for (int i = 0; i < given.size(); i++) values.add(argument(given.get(i), params.get(i)));
            return json(eInvoke.invoke(o, found, values));
        });
    }

    /** The structural feature of the EClass {@code ec} by name, or {@link #NONE}. */
    private Object feature(Object ec, String prop) throws ReflectiveOperationException {
        Map<String, Object> byName = features.computeIfAbsent(ec, k -> new HashMap<>());
        Object f = byName.get(prop);
        if (f == null) {
            Object found = getEStructuralFeature.invoke(ec, prop);
            f = found == null ? NONE : found;
            byName.put(prop, f);
        }
        return f;
    }

    /** The operation of the EClass {@code ec} by name and number of parameters, or {@link #NONE}. */
    private Object operation(Object ec, String op, int arity) throws ReflectiveOperationException {
        String key = op + "/" + arity;
        Map<String, Object> byName = operations.computeIfAbsent(ec, k -> new HashMap<>());
        Object found = byName.get(key);
        if (found == null) {
            found = NONE;
            for (Object candidate : (List<?>) getEAllOperations.invoke(ec))
                if (op.equals(getName.invoke(candidate)) && ((List<?>) getEParameters.invoke(candidate)).size() == arity) {
                    found = candidate;
                    break;
                }
            byName.put(key, found);
        }
        return found;
    }

    /**
     * A convenience member ({@code convenience.json}) on the element, its arguments by parameter name
     * (a scope or visibility filter as its literal's name, an element as {@code {"@id": handle}}),
     * answered by the bodies of {@link Convenience} over the Pilot's model. The result is in the
     * protocol's shapes: a literal tagged ({@code {"integer": 4}}), an unbounded upper bound as
     * {@code "*"}. Errors: {@code notApplicable} when the element's metaclass is not one of the member's
     * contexts, {@code invalid} where the member's contract gives no answer, {@code refused} where this
     * engine does not answer it. With a plugin of shortcut bodies ({@link Shortcuts}), the member may be
     * answered by the plugin, or by both and checked.
     */
    public Object find(String h, String member, Map<?, ?> args) {
        Object o = object(h);
        Map<?, ?> given = args == null ? Map.of() : args;
        return inPilot(() -> shortcuts == null ? referenceFind(o, member, given)
            : shortcuts.find(member, o, pluginArguments(given), () -> referenceFind(o, member, given), this::json));
    }

    /** The member answered by its body in {@link Convenience}, in the protocol's shapes. */
    private Object referenceFind(Object o, String member, Map<?, ?> given) throws ReflectiveOperationException {
        try {
            return json(ConvenienceMembers.find(convenience, o, member, given));
        } catch (PilotException e) {
            if (e.kind().equals("badRequest") || e.kind().equals("notApplicable")) throw e;
            throw new PilotException(e.kind(), member + ": " + e.getMessage());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof PilotException p) throw new PilotException(p.kind(), member + ": " + p.getMessage());
            throw new PilotException("failed", member + ": the Pilot raised " + describe(cause));
        }
    }

    /** A request's arguments for the plugin: as given, an element as the Pilot's object. */
    private Map<String, Object> pluginArguments(Map<?, ?> given) {
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : given.entrySet()) {
            Object v = e.getValue();
            if (v instanceof Map<?, ?> m && m.get("@id") != null) {
                Object h = m.get("@id");
                v = object(h instanceof Number n ? Long.toString(n.longValue()) : String.valueOf(h));
            }
            out.put(String.valueOf(e.getKey()), v);
        }
        return out;
    }

    /** How members are answered in this JVM (reference, shortcut or compare), and the counts of compare
     *  mode per member: {@link Shortcuts#diagnostics}. */
    public Map<String, Object> diagnostics() {
        return Shortcuts.diagnostics();
    }

    /** {@link #find} on each element, a result per handle in order; the first error ends it, with the
     *  element's handle in its message. */
    public List<Object> findEach(List<String> hs, String member, Map<?, ?> args) {
        List<Object> out = new ArrayList<>(hs.size());
        for (String h : hs) {
            try {
                out.add(find(h, member, args));
            } catch (PilotException e) {
                if (e.kind().equals("badRequest")) throw e;
                throw new PilotException(e.kind(), "element " + h + ": " + e.getMessage());
            }
        }
        return out;
    }

    /** The handle of the element with this qualified name (the model's or the library's), or null. */
    public String resolve(String qualifiedName) {
        Object instance = model();
        return inPilot(() -> {
            Object e = resolveName.invoke(instance, qualifiedName);
            return e == null ? null : handle(e);
        });
    }

    /** Every element of the model (not the library's), in containment order. */
    public List<String> allHandles() {
        Object instance = model();
        return inPilot(() -> {
            List<String> out = new ArrayList<>();
            for (Object r : (List<?>) getInputResources.invoke(instance))
                for (Iterator<?> it = (Iterator<?>) getAllContents.invoke(r); it.hasNext(); ) {
                    Object o = it.next();
                    if (element.isInstance(o)) out.add(handle(o));
                }
            return out;
        });
    }

    // -- the protocol ----------------------------------------------------------------------------

    /** Answer one request of the protocol (see the class comment), as JSON text. Never throws. */
    public String request(String json) {
        Object id = null;
        Map<String, Object> answer = new LinkedHashMap<>();
        try {
            Object parsed = Json.parse(json);
            if (!(parsed instanceof Map<?, ?> req)) throw new PilotException("badRequest", "a request is a JSON object");
            id = req.get("id");
            answer.put("id", id);
            answer.put("ok", handle(req));
        } catch (PilotException e) {
            answer.put("id", id);
            answer.put("error", error(e.kind(), e.getMessage()));
        } catch (RuntimeException e) {
            answer.put("id", id);
            answer.put("error", error("badRequest", e.toString()));
        }
        return Json.write(answer);
    }

    static Map<String, Object> error(String kind, String message) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("kind", kind);
        e.put("message", message);
        return e;
    }

    /** Answer one parsed request: the value of its {@code ok}. */
    public Object handle(Map<?, ?> req) {
        String cmd = String.valueOf(req.get("cmd"));
        switch (cmd) {
            case "open": {
                List<String> paths = new ArrayList<>();
                if (req.get("paths") instanceof List<?> l) for (Object p : l) paths.add(String.valueOf(p));
                Map<String, String> sources = new LinkedHashMap<>();
                if (req.get("sources") instanceof Map<?, ?> m) for (Map.Entry<?, ?> e : m.entrySet()) sources.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
                boolean elaborate = !Boolean.FALSE.equals(req.get("elaborate"));
                Object library = req.get("library");
                return open(paths, sources, elaborate, library == null ? null : String.valueOf(library));
            }
            case "metaclass": return metaclass(handleOf(req));
            case "elementId": return elementId(handleOf(req));
            case "get": return get(handleOf(req), String.valueOf(req.get("prop")));
            case "call": {
                List<Object> args = new ArrayList<>();
                if (req.get("args") instanceof List<?> l) args.addAll(l);
                return call(handleOf(req), String.valueOf(req.get("op")), args);
            }
            case "find": {
                Map<?, ?> args = req.get("args") instanceof Map<?, ?> m ? m : Map.of();
                return find(handleOf(req), String.valueOf(req.get("member")), args);
            }
            case "findEach": {
                Map<?, ?> args = req.get("args") instanceof Map<?, ?> m ? m : Map.of();
                List<String> hs = new ArrayList<>();
                if (!(req.get("hs") instanceof List<?> l)) throw new PilotException("badRequest", "findEach names no elements (hs)");
                for (Object h : l) hs.add(h instanceof Number n ? Long.toString(n.longValue()) : String.valueOf(h));
                return findEach(hs, String.valueOf(req.get("member")), args);
            }
            case "resolve": return resolve(String.valueOf(req.get("name")));
            case "allHandles": return allHandles();
            case "diagnostics": return diagnostics();
            case "close": close(); return null;
            default: throw new PilotException("badRequest", "unknown command " + cmd);
        }
    }

    private static String handleOf(Map<?, ?> req) {
        Object h = req.get("h");
        if (h == null) throw new PilotException("badRequest", "the request names no element (h)");
        return h instanceof Number n ? Long.toString(n.longValue()) : String.valueOf(h);
    }

    // -- values ----------------------------------------------------------------------------------

    private Object model() {
        if (si == null) throw new PilotException("badRequest", "no model is open");
        return si;
    }

    Object object(String h) {
        model();
        int i;
        try {
            i = Integer.parseInt(h);
        } catch (NumberFormatException e) {
            throw new PilotException("gone", "no element has the handle " + h);
        }
        if (i < 0 || i >= objects.size()) throw new PilotException("gone", "no element has the handle " + h);
        return objects.get(i);
    }

    private String handle(Object o) {
        String h = handles.get(o);
        if (h == null) {
            h = Integer.toString(objects.size());
            objects.add(o);
            handles.put(o, h);
        }
        return h;
    }

    private Object json(Object v) throws ReflectiveOperationException {
        if (v == null || v instanceof String || v instanceof Boolean) return v;
        if (eObject.isInstance(v)) {
            if ((Boolean) eIsProxy.invoke(v))
                throw new PilotException("unresolved", "a reference did not resolve (" + eProxyURI.invoke(v) + ")");
            return Map.of("@id", handle(v));
        }
        if (v instanceof Iterable<?> it) {
            List<Object> out = new ArrayList<>();
            for (Object x : it) out.add(json(x));
            return out;
        }
        if (v instanceof Map<?, ?> m) {
            Map<String, Object> out = new LinkedHashMap<>();
            for (Map.Entry<?, ?> e : m.entrySet()) out.put(String.valueOf(e.getKey()), json(e.getValue()));
            return out;
        }
        if (enumerator.isInstance(v)) return getLiteral.invoke(v);
        if (v instanceof Integer || v instanceof Long || v instanceof Short || v instanceof Byte) return ((Number) v).longValue();
        if (v instanceof java.math.BigInteger b) return b.bitLength() < 64 ? (Object) b.longValue() : b.toString();
        if (v instanceof Number n) return n.doubleValue();
        return String.valueOf(v);
    }

    private Object argument(Object a, Object param) throws ReflectiveOperationException {
        if (a == null) return null;
        if (a instanceof Map<?, ?> m && m.containsKey("@id")) {
            Object h = m.get("@id");
            return object(h instanceof Number n ? Long.toString(n.longValue()) : String.valueOf(h));
        }
        if (a instanceof List<?> l) {
            @SuppressWarnings("unchecked")
            List<Object> out = (List<Object>) basicEList.getConstructor().newInstance();
            for (Object x : l) out.add(argument(x, null));
            return out;
        }
        if (a instanceof Number n && param != null) {
            Object type = getEType.invoke(param);
            Class<?> c = type == null ? null : (Class<?>) getInstanceClass.invoke(type);
            if (c == int.class || c == Integer.class) return n.intValue();
            if (c == long.class || c == Long.class) return n.longValue();
            if (c == double.class || c == Double.class) return n.doubleValue();
            if (c == float.class || c == Float.class) return n.floatValue();
        }
        return a;
    }

    // -- raw reads, for the convenience members' bodies ------------------------------------------

    /** A property's value as the Pilot has it (EObjects, lists, enumerators); {@code failed} when the
     *  element's metaclass has no such property: a body reads only what its OCL's types allow. */
    Object read(Object o, String prop) throws ReflectiveOperationException {
        Object ec = eClass.invoke(o);
        Object f = feature(ec, prop);
        if (f == NONE) throw new PilotException("failed", getName.invoke(ec) + " has no property " + prop);
        return eGet.invoke(o, f);
    }

    /** An operation's result as the Pilot has it, the arguments passed as they are. */
    Object invoke(Object o, String op, Object... args) throws ReflectiveOperationException {
        Object ec = eClass.invoke(o);
        Object found = operation(ec, op, args.length);
        if (found == NONE)
            throw new PilotException("failed", getName.invoke(ec) + " has no operation " + op + " with " + args.length + " parameter(s)");
        @SuppressWarnings("unchecked")
        List<Object> values = (List<Object>) basicEList.getConstructor().newInstance();
        java.util.Collections.addAll(values, args);
        return eInvoke.invoke(o, found, values);
    }

    String metaclassOf(Object o) throws ReflectiveOperationException {
        return (String) getName.invoke(eClass.invoke(o));
    }

    /** {@code oclIsKindOf}: whether the object is an instance of the Pilot's interface of that metaclass. */
    boolean isKind(Object o, String metaclass) {
        Class<?> c = interfaces.computeIfAbsent(metaclass, n -> {
            try {
                return loader.loadClass("org.omg.sysml.lang.sysml." + n);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
        if (c == null) throw new PilotException("failed", "the Pilot has no metaclass " + metaclass);
        return c.isInstance(o);
    }

    boolean isProxy(Object o) throws ReflectiveOperationException {
        return eObject.isInstance(o) && (Boolean) eIsProxy.invoke(o);
    }

    boolean isEnumerator(Object v) {
        return enumerator.isInstance(v);
    }

    String literalOf(Object enumeratorValue) throws ReflectiveOperationException {
        return (String) getLiteral.invoke(enumeratorValue);
    }

    /** Run {@code body} with the Pilot's class loader as the thread's context class loader (Xtext and
     *  Guice look classes up through it), turning what the Pilot throws into a {@code failed} answer. */
    private <T> T inPilot(Callable<T> body) {
        Thread t = Thread.currentThread();
        ClassLoader previous = t.getContextClassLoader();
        t.setContextClassLoader(loader);
        try {
            return body.call();
        } catch (PilotException e) {
            throw e;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof PilotException p) throw p;
            throw new PilotException("failed", "the Pilot raised " + describe(cause));
        } catch (LinkageError e) {
            throw new PilotException("unavailable", "the Pilot could not be loaded: " + e);
        } catch (Exception e) {
            throw new PilotException("failed", "the Pilot could not be called: " + describe(e));
        } finally {
            t.setContextClassLoader(previous);
        }
    }

    private static String describe(Throwable e) {
        StackTraceElement[] at = e.getStackTrace();
        return e.getClass().getName() + (e.getMessage() == null ? "" : ": " + e.getMessage())
            + (at.length > 0 ? " (at " + at[0] + ")" : "");
    }
}
