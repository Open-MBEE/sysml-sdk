package org.openmbee.sysml;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SysML Toolkit backend: Java over the binding library ({@code abi/}) through the foreign
 * function API. Implements the same {@link Backend} contract as {@link PayloadBackend}, so the
 * generated interfaces and {@link Model} are untouched. Handles are the integers the library
 * mints, carried as decimal strings to fit the contract; references come back as
 * {@code {"@id": handle}} so {@link Model#wrap} mints typed elements as it does for payloads.
 *
 * <p>Dispatch: {@code get(handle, prop)} finds the declaring class of {@code prop} for the
 * element's metaclass through {@link ToolkitTable}, then calls that symbol. The per-member
 * static form follows once the generator emits symbols into the interfaces.
 *
 * <p>Operations: {@code call(handle, op, args)} finds the operation's row the same way and
 * passes each argument as the binding library's parameter C type says.
 *
 * <p>Status codes follow the ABI specification: NOT_IMPLEMENTED raises
 * {@link NotImplementedInToolkitException}, NOT_APPLICABLE is an absent value, INVALID_HANDLE
 * raises {@link GoneException}, UNRESOLVED raises {@link UnresolvedReferenceException}.
 *
 * <p>Needs JDK 22, or JDK 21 with {@code --enable-preview}.
 */
public final class ToolkitBackend implements Backend, AutoCloseable {

    // -- status codes (ABI section 3) --------------------------------------------------------
    private static final int OK = 0, NOT_IMPLEMENTED = 1, NOT_APPLICABLE = 2, INVALID_HANDLE = 3, UNRESOLVED = 7;

    // -- struct layouts (ABI section 4), x86-64 --------------------------------------------
    private static final long STR_SIZE = 32, STR_PTR = 0, STR_LEN = 8, STR_PRESENT = 16, STR_OWNER = 24;
    private static final long HANDLES_SIZE = 24, H_ITEMS = 0, H_LEN = 8, H_OWNER = 16;
    private static final long REF_SIZE = 16, BOOL_SIZE = 2, NUM_SIZE = 16;
    private static final long OPTS_SIZE = 72;

    /** One loaded library, shared by every session created from it. */
    public static final class Library {
        final Arena arena = Arena.ofShared();
        final Linker linker = Linker.nativeLinker();
        final SymbolLookup lookup;
        final MethodHandle free, sessionOpen, sessionClose, sessionRoots, lastError, metaclass, userElements, fullJson;
        private final Map<String, MethodHandle> members = new HashMap<>();

        public Library(Path path) {
            lookup = SymbolLookup.libraryLookup(path, arena);
            free = down("sysmlv2_free", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            sessionOpen = down("sysmlv2_session_open", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            sessionClose = down("sysmlv2_session_close", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            sessionRoots = down("sysmlv2_session_roots", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            lastError = down("sysmlv2_last_error", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            metaclass = down("sysmlv2_metaclass", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
            userElements = down("sysmlv2_user_elements", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            fullJson = down("sysmlv2_session_full_json", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            MethodHandle digest = down("sysmlv2_names_digest", FunctionDescriptor.of(ValueLayout.ADDRESS));
            MethodHandle digestLen = down("sysmlv2_names_digest_len", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
            try {
                MemorySegment p = (MemorySegment) digest.invokeExact();
                long n = (long) digestLen.invokeExact();
                String have = new String(p.reinterpret(n).toArray(ValueLayout.JAVA_BYTE), StandardCharsets.UTF_8);
                if (!have.equals(ToolkitTable.NAMES_DIGEST))
                    throw new SdkException("binding library " + path + " was generated from naming table "
                        + have.substring(0, 12) + ", this SDK from " + ToolkitTable.NAMES_DIGEST.substring(0, 12) + "; regenerate one side");
            } catch (SdkException e) {
                throw e;
            } catch (Throwable t) {
                throw new SdkException("cannot read the library's naming-table digest: " + t);
            }
        }

        private MethodHandle down(String symbol, FunctionDescriptor fd) {
            return linker.downcallHandle(lookup.find(symbol).orElseThrow(
                () -> new SdkException("binding library lacks " + symbol)), fd);
        }

        /** A per-member function: (session, handle, out*) -> status. */
        MethodHandle member(String symbol) {
            return members.computeIfAbsent(symbol, s -> down(s,
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)));
        }

        void free(MemorySegment owner) {
            if (owner.address() == 0) return;
            try {
                free.invokeExact(owner);
            } catch (Throwable t) {
                throw new SdkException("sysmlv2_free failed: " + t);
            }
        }

        /** SYSMLV2_ABI; else the library this jar carries for the running platform; else
         *  abi/target/release above the working directory (a build of the SDK's source). */
        public static Path defaultPath() {
            String env = System.getenv("SYSMLV2_ABI");
            if (env != null) return Path.of(env);
            String os = System.getProperty("os.name").toLowerCase();
            String name = os.contains("win") ? "sysmlv2_abi.dll" : os.contains("mac") ? "libsysmlv2_abi.dylib" : "libsysmlv2_abi.so";
            Path carried = carried(platform(os), name);
            if (carried != null) return carried;
            Path d = Path.of("").toAbsolutePath();
            while (d != null) {
                Path candidate = d.resolve("abi").resolve("target").resolve("release").resolve(name);
                if (java.nio.file.Files.exists(candidate)) return candidate;
                d = d.getParent();
            }
            throw new SdkException("binding library not found: this jar carries none for " + platform(os) + "; set SYSMLV2_ABI to the library file in the release's sysmlv2_abi archive for this platform (sysmlv2_abi.dll, libsysmlv2_abi.so or libsysmlv2_abi.dylib), or one built from abi/ in the SDK source");
        }

        /** The directory of the jar's libraries for the running platform: windows-x86_64,
         *  linux-x86_64, macos-aarch64 or macos-x86_64. */
        static String platform(String os) {
            String arch = System.getProperty("os.arch").toLowerCase();
            String a = arch.equals("amd64") || arch.equals("x86_64") ? "x86_64"
                : arch.equals("aarch64") || arch.equals("arm64") ? "aarch64" : arch;
            return (os.contains("win") ? "windows" : os.contains("mac") ? "macos" : "linux") + "-" + a;
        }

        /** The library this jar carries for {@code platform}, or null. A library cannot be loaded
         *  from inside a jar, so it is copied out once, into a directory of the temporary directory
         *  named after its digest. */
        static Path carried(String platform, String name) {
            String resource = "native/" + platform + "/" + name;
            try (java.io.InputStream in = ToolkitBackend.class.getResourceAsStream(resource)) {
                if (in == null) return null;
                byte[] bytes = in.readAllBytes();
                String digest = java.util.HexFormat.of().formatHex(
                    java.security.MessageDigest.getInstance("SHA-256").digest(bytes)).substring(0, 16);
                Path dir = Path.of(System.getProperty("java.io.tmpdir"), "sysmlv2-abi-" + digest);
                Path lib = dir.resolve(name);
                if (java.nio.file.Files.isRegularFile(lib) && java.nio.file.Files.size(lib) == bytes.length) return lib;
                java.nio.file.Files.createDirectories(dir);
                Path part = java.nio.file.Files.createTempFile(dir, name, ".part");
                java.nio.file.Files.write(part, bytes);
                try {
                    java.nio.file.Files.move(part, lib, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
                } catch (java.io.IOException raced) {   // another process copied it out first
                    java.nio.file.Files.deleteIfExists(part);
                    if (!java.nio.file.Files.isRegularFile(lib)) throw raced;
                }
                return lib;
            } catch (java.io.IOException | java.security.NoSuchAlgorithmException e) {
                throw new SdkException("cannot copy out the binding library " + resource + ": " + e);
            }
        }
    }

    private static Library defaultLibrary;

    public static synchronized Library library() {
        if (defaultLibrary == null) defaultLibrary = new Library(Library.defaultPath());
        return defaultLibrary;
    }

    // -- instance ------------------------------------------------------------------------------

    private final Library lib;
    private MemorySegment session;
    private final Map<String, String> metaclassCache = new HashMap<>();
    private final Map<String, String[]> declaringCache = new HashMap<>();

    private ToolkitBackend(Library lib, MemorySegment session) {
        this.lib = lib;
        this.session = session;
    }

    /** Open a session over files. */
    public static ToolkitBackend open(List<String> paths) {
        return open(library(), paths, null, null);
    }

    /** Open a session over in-memory sources: unit name to text. */
    public static ToolkitBackend openSources(Map<String, String> sources) {
        return open(library(), List.of(), sources, null);
    }

    public static ToolkitBackend open(Library lib, List<String> paths, Map<String, String> sources, String libraryDir) {
        if (sources == null || sources.isEmpty())
            for (String p : paths)
                if (!java.nio.file.Files.isRegularFile(Path.of(p)))
                    throw new SdkException("model file not found: " + Path.of(p).toAbsolutePath());
        if (libraryDir != null && !java.nio.file.Files.isDirectory(Path.of(libraryDir)))
            throw new SdkException("library directory not found: " + Path.of(libraryDir).toAbsolutePath());
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment opts = arena.allocate(OPTS_SIZE);
            if (sources != null && !sources.isEmpty()) {
                MemorySegment names = arena.allocate(STR_SIZE * sources.size());
                MemorySegment texts = arena.allocate(STR_SIZE * sources.size());
                int i = 0;
                for (Map.Entry<String, String> e : sources.entrySet()) {
                    writeStr(arena, names.asSlice(i * STR_SIZE, STR_SIZE), e.getKey());
                    writeStr(arena, texts.asSlice(i * STR_SIZE, STR_SIZE), e.getValue());
                    i++;
                }
                opts.set(ValueLayout.ADDRESS, 16, names);
                opts.set(ValueLayout.ADDRESS, 24, texts);
                opts.set(ValueLayout.JAVA_LONG, 32, sources.size());
            } else {
                MemorySegment arr = arena.allocate(STR_SIZE * Math.max(1, paths.size()));
                for (int i = 0; i < paths.size(); i++)
                    writeStr(arena, arr.asSlice(i * STR_SIZE, STR_SIZE), paths.get(i));
                opts.set(ValueLayout.ADDRESS, 0, arr);
                opts.set(ValueLayout.JAVA_LONG, 8, paths.size());
            }
            if (libraryDir != null)
                writeStr(arena, opts.asSlice(40, STR_SIZE), libraryDir);
            MemorySegment out = arena.allocate(ValueLayout.ADDRESS);
            int st = (int) lib.sessionOpen.invokeExact(opts, out);
            if (st != OK)
                throw new SdkException("the SysML Toolkit could not load the model: status " + st + " (the SysML Toolkit's reason is on stderr)");
            return new ToolkitBackend(lib, out.get(ValueLayout.ADDRESS, 0));
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("session open failed: " + t);
        }
    }

    private static void writeStr(Arena arena, MemorySegment str, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        MemorySegment buf = arena.allocate(Math.max(1, bytes.length));
        MemorySegment.copy(bytes, 0, buf, ValueLayout.JAVA_BYTE, 0, bytes.length);
        str.set(ValueLayout.ADDRESS, STR_PTR, buf);
        str.set(ValueLayout.JAVA_LONG, STR_LEN, bytes.length);
        str.set(ValueLayout.JAVA_BYTE, STR_PRESENT, (byte) 1);
        str.set(ValueLayout.ADDRESS, STR_OWNER, MemorySegment.NULL);
    }

    @Override
    public void close() {
        if (session != null) {
            try {
                lib.sessionClose.invokeExact(session);
            } catch (Throwable t) {
                throw new SdkException("session close failed: " + t);
            }
            session = null;
        }
    }

    /** The model as full-form interchange JSON, from the SysML Toolkit's own emitter, with the
     * closures. */
    public String fullJson() {
        return fullJson(true);
    }

    /**
     * The model as full-form interchange JSON, from the SysML Toolkit's own emitter. With
     * {@code closures} the inheritance-aware properties carry the specification's values,
     * inherited and imported members included; without, the owned side only, as the
     * SysML Toolkit's command-line export writes them.
     */
    public String fullJson(boolean closures) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(STR_SIZE);
            int st = (int) lib.fullJson.invokeExact(session, closures ? 1 : 0, out);
            check(st, "full_json");
            return takeStr(out);
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("full_json failed: " + t);
        }
    }

    /** The user-unit document roots. */
    public List<String> roots() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(HANDLES_SIZE);
            int st = (int) lib.sessionRoots.invokeExact(session, out);
            check(st, "roots");
            return takeHandles(out);
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("roots failed: " + t);
        }
    }

    // -- errors and result unpacking -----------------------------------------------------------

    private String lastError() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(STR_SIZE);
            int ignored = (int) lib.lastError.invokeExact(session, out);
            String s = takeStr(out);
            return s == null ? "" : s;
        } catch (Throwable t) {
            return "";
        }
    }

    /** True when a value is present; false for NOT_APPLICABLE; throws otherwise. */
    private boolean check(int st, String where) {
        if (st == OK) return true;
        if (st == NOT_APPLICABLE) return false;
        String msg = lastError();
        if (st == NOT_IMPLEMENTED)
            throw new NotImplementedInToolkitException(msg.isEmpty() ? where + ": not implemented by the SysML Toolkit" : msg);
        if (st == INVALID_HANDLE)
            throw new GoneException(msg.isEmpty() ? where + ": invalid handle" : msg);
        if (st == UNRESOLVED)
            throw new UnresolvedReferenceException(msg.isEmpty() ? where + ": a reference did not resolve" : msg);
        throw new SdkException(where + ": status " + st + ": " + msg);
    }

    private String takeStr(MemorySegment str) {
        boolean present = str.get(ValueLayout.JAVA_BYTE, STR_PRESENT) != 0;
        MemorySegment ptr = str.get(ValueLayout.ADDRESS, STR_PTR);
        long len = str.get(ValueLayout.JAVA_LONG, STR_LEN);
        String value = (present && ptr.address() != 0)
            ? new String(ptr.reinterpret(len).toArray(ValueLayout.JAVA_BYTE), StandardCharsets.UTF_8) : null;
        lib.free(str.get(ValueLayout.ADDRESS, STR_OWNER));
        return value;
    }

    private List<String> takeHandles(MemorySegment hs) {
        MemorySegment items = hs.get(ValueLayout.ADDRESS, H_ITEMS);
        long len = hs.get(ValueLayout.JAVA_LONG, H_LEN);
        List<String> out = new ArrayList<>((int) len);
        if (len > 0) {
            MemorySegment arr = items.reinterpret(len * 8);
            for (long i = 0; i < len; i++) out.add(Long.toString(arr.getAtIndex(ValueLayout.JAVA_LONG, i)));
        }
        lib.free(hs.get(ValueLayout.ADDRESS, H_OWNER));
        return out;
    }

    private static long outSize(String code) {
        return switch (code) { case "H" -> HANDLES_SIZE; case "S", "T" -> STR_SIZE; case "B" -> BOOL_SIZE; default -> NUM_SIZE; };
    }

    private Object call(String symbol, String code, long handle, String where) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(outSize(code));
            int st = (int) lib.member(symbol).invokeExact(session, handle, out);
            return unpack(code, out, st, where);
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException(where + " failed: " + t);
        }
    }

    /** The result in {@code out} as the backend contract carries it, after checking the status. */
    private Object unpack(String code, MemorySegment out, int st, String where) {
        if (!check(st, where)) return "H".equals(code) || "T".equals(code) ? List.of() : null;
        switch (code) {
            case "H": {
                List<Object> refs = new ArrayList<>();
                for (String h : takeHandles(out)) refs.add(Map.of("@id", h));
                return refs;
            }
            case "R":
                return out.get(ValueLayout.JAVA_BYTE, 0) != 0 ? Map.of("@id", Long.toString(out.get(ValueLayout.JAVA_LONG, 8))) : null;
            case "S":
                return takeStr(out);
            case "T": { // a list of strings (a requirement's `text`), as JSON text
                String text = takeStr(out);
                return text == null ? List.of() : Json.parse(text);
            }
            case "B":
                return out.get(ValueLayout.JAVA_BYTE, 0) != 0 ? Boolean.valueOf(out.get(ValueLayout.JAVA_BYTE, 1) != 0) : null;
            case "I":
                return out.get(ValueLayout.JAVA_BYTE, 0) != 0 ? Long.valueOf(out.get(ValueLayout.JAVA_LONG, 8)) : null;
            default:
                return out.get(ValueLayout.JAVA_BYTE, 0) != 0 ? Double.valueOf(out.get(ValueLayout.JAVA_DOUBLE, 8)) : null;
        }
    }

    // -- the backend contract ------------------------------------------------------------------

    private String[] declaring(String metaclass, String prop) {
        String key = metaclass + "::" + prop;
        if (declaringCache.containsKey(key)) return declaringCache.get(key);
        java.util.ArrayDeque<String> stack = new java.util.ArrayDeque<>();
        java.util.HashSet<String> seen = new java.util.HashSet<>();
        stack.push(metaclass);
        String[] found = null;
        while (!stack.isEmpty()) {
            String c = stack.pop();
            if (!seen.add(c)) continue;
            String[] e = ToolkitTable.FUNCS.get(c + "::" + prop);
            if (e != null) { found = e; break; }
            for (String b : ToolkitTable.BASES.getOrDefault(c, new String[0])) stack.push(b);
        }
        declaringCache.put(key, found);
        return found;
    }

    @Override
    public Object get(String handle, String prop) {
        String mc = metaclass(handle);
        String[] e = declaring(mc, prop);
        if (e == null) return null;  // not a member of this metaclass: absent, as the payload backend answers
        return call(e[0], e[1], Long.parseLong(handle), mc + "." + prop);
    }

    /**
     * A specification operation. Each argument is passed as the binding library's parameter C type
     * says: an element as its handle, a list of elements as a handle array, a Boolean as a C bool,
     * anything else as text. The library answers or refuses.
     */
    @Override
    public Object call(String handle, String op, List<Object> args) {
        String mc = metaclass(handle);
        String where = mc + "." + op + "()";
        String[] e = declaring(mc, op + "()");
        if (e == null) throw new SdkException(where + ": the binding library has no such operation");
        String[] types = e[2].isEmpty() ? new String[0] : e[2].split(";");
        try (Arena arena = Arena.ofConfined()) {
            List<MemoryLayout> layouts = new ArrayList<>(List.of(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            List<Object> values = new ArrayList<>(List.of(session, Long.parseLong(handle)));
            for (int i = 0; i < types.length; i++) {
                Object a = i < args.size() ? args.get(i) : null;
                switch (types[i]) {
                    case "Handle" -> {
                        // The binding library has no "no element": refuse it rather than pass a
                        // handle that names some other element.
                        if (!(a instanceof Element el))
                            throw new IllegalArgumentException(where + ": argument " + (i + 1) + " must be an element, not " + a);
                        layouts.add(ValueLayout.JAVA_LONG);
                        values.add(Long.parseLong(el.$handle()));
                    }
                    case "bool" -> {
                        layouts.add(ValueLayout.JAVA_BOOLEAN);
                        values.add(Boolean.TRUE.equals(a));
                    }
                    case "Handles" -> {
                        List<?> items = a instanceof List<?> l ? l : List.of();
                        MemorySegment arr = arena.allocate(8L * Math.max(1, items.size()));
                        for (int k = 0; k < items.size(); k++) {
                            if (!(items.get(k) instanceof Element x))
                                throw new IllegalArgumentException(where + ": argument " + (i + 1) + " must hold elements only, not " + items.get(k));
                            arr.setAtIndex(ValueLayout.JAVA_LONG, k, Long.parseLong(x.$handle()));
                        }
                        MemorySegment hs = arena.allocate(HANDLES_SIZE);
                        hs.set(ValueLayout.ADDRESS, H_ITEMS, arr);
                        hs.set(ValueLayout.JAVA_LONG, H_LEN, items.size());
                        hs.set(ValueLayout.ADDRESS, H_OWNER, MemorySegment.NULL);
                        layouts.add(ValueLayout.ADDRESS);
                        values.add(hs);
                    }
                    default -> {
                        MemorySegment s = arena.allocate(STR_SIZE);
                        writeStr(arena, s, a == null ? "" : a instanceof Element el ? el.$id() : String.valueOf(a));
                        layouts.add(ValueLayout.ADDRESS);
                        values.add(s);
                    }
                }
            }
            MemorySegment out = arena.allocate(outSize(e[1]));
            layouts.add(ValueLayout.ADDRESS);
            values.add(out);
            MethodHandle f = lib.linker.downcallHandle(lib.lookup.find(e[0]).orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, layouts.toArray(new MemoryLayout[0])));
            int st = (int) f.invokeWithArguments(values);
            return unpack(e[1], out, st, where);
        } catch (SdkException | IllegalArgumentException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new SdkException(where + " failed: " + t);
        }
    }

    @Override
    public String metaclass(String handle) {
        String mc = metaclassCache.get(handle);
        if (mc != null) return mc;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(STR_SIZE);
            int st = (int) lib.metaclass.invokeExact(session, Long.parseLong(handle), out);
            check(st, "metaclass(" + handle + ")");
            mc = takeStr(out);
            metaclassCache.put(handle, mc);
            return mc;
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("metaclass failed: " + t);
        }
    }

    @Override
    public String elementId(String handle) {
        Object v = call("sysmlv2_element_element_id", "S", Long.parseLong(handle), "elementId");
        return v == null ? "" : (String) v;
    }

    @Override
    public String resolve(String qualifiedName) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment arg = arena.allocate(STR_SIZE);
            writeStr(arena, arg, qualifiedName);
            MemorySegment out = arena.allocate(REF_SIZE);
            MethodHandle f = lib.linker.downcallHandle(lib.lookup.find("sysmlv2_session_resolve").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            int st = (int) f.invokeExact(session, arg, out);
            if (!check(st, "resolve(" + qualifiedName + ")")) return null;
            return out.get(ValueLayout.JAVA_BYTE, 0) != 0 ? Long.toString(out.get(ValueLayout.JAVA_LONG, 8)) : null;
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("resolve failed: " + t);
        }
    }

    @Override
    public Iterable<String> allHandles() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment out = arena.allocate(HANDLES_SIZE);
            int st = (int) lib.userElements.invokeExact(session, out);
            check(st, "user_elements");
            return takeHandles(out);
        } catch (SdkException e) {
            throw e;
        } catch (Throwable t) {
            throw new SdkException("user_elements failed: " + t);
        }
    }
}
