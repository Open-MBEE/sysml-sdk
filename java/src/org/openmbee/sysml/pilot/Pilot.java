package org.openmbee.sysml.pilot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * One installation of the SysML v2 Pilot Implementation: its jar and the standard library of the
 * same version (the library's qualified names change between versions, so the two always travel
 * together).
 *
 * <p>{@link #locate()} finds the Pilot: the directory {@code SYSML_PILOT_HOME} names (a Pilot jar
 * and its {@code sysml.library} beside it, or under {@code sysml/}, as the Pilot's release archive
 * unpacks), else the copy in the user's cache, else the release the SDK is pinned to, downloaded
 * from the Pilot's GitHub releases on the first call, checked against its pinned SHA-256 and kept in
 * the cache for later calls (the cache the SDK's other languages share). {@link #at} names a jar
 * and a library directly, such as a Pilot built from source.
 *
 * <p>The SDK redistributes nothing of the Pilot, which is licensed under the Eclipse Public License
 * 2.0. The Pilot needs a Java 21 or newer runtime.
 */
public final class Pilot {
    /** The Pilot release the SDK is tested with and downloads. */
    public static final String RELEASE = "2026-08";
    public static final String VERSION = "0.62.0";
    static final String REPOSITORY = "Systems-Modeling/SysML-v2-Pilot-Implementation";
    static final String ASSET = "jupyter-sysml-kernel-" + VERSION + ".zip";
    static final String ASSET_SHA256 = "5a015ed3f2b3d2dae14e9c3fb9602d9e1fef5f9ce33cf396c422329405cc3cb2";
    static final String JAR_ENTRY = "sysml/jupyter-sysml-kernel-" + VERSION + "-all.jar";
    static final String LIBRARY_ENTRY = "sysml/sysml.library/";

    static final String HOME_VARIABLE = "SYSML_PILOT_HOME";
    static final String CACHE_VARIABLE = "SYSML_CACHE_DIR";

    private static final Pattern JAR_VERSION = Pattern.compile("-(\\d+\\.\\d+\\.\\d+(?:-[A-Za-z0-9.]+)?)-all\\.jar$");
    private static final Map<Path, Pilot> LOCATED = new HashMap<>();

    private final Path jar;
    private final Path library;
    private final String version;
    private ClassLoader loader;

    private Pilot(Path jar, Path library, String version) {
        this.jar = jar;
        this.library = library;
        this.version = version;
    }

    /** The Pilot jar. */
    public Path jar() {
        return jar;
    }

    /** The standard library directory of the same Pilot version. */
    public Path library() {
        return library;
    }

    /** The Pilot's version, from its jar's name ({@code "unknown"} when the name does not say). */
    public String version() {
        return version;
    }

    @Override
    public String toString() {
        return "Pilot " + version + " (" + jar + ")";
    }

    /** A Pilot jar and the standard library directory of the same version. */
    public static synchronized Pilot at(Path jar, Path library) {
        jar = jar.toAbsolutePath().normalize();
        if (!Files.isRegularFile(jar))
            throw new PilotException("unavailable", "the Pilot jar " + jar + " is not a file");
        if (!Files.isDirectory(library))
            throw new PilotException("unavailable", "the Pilot's library directory " + library.toAbsolutePath() + " is not a directory");
        Pilot known = LOCATED.get(jar);
        if (known != null) return known;
        Matcher m = JAR_VERSION.matcher(jar.getFileName().toString());
        Pilot p = new Pilot(jar, library.toAbsolutePath().normalize(), m.find() ? m.group(1) : "unknown");
        LOCATED.put(jar, p);
        return p;
    }

    /** The Pilot in {@code home}: a jar and {@code sysml.library} beside it, or both under {@code sysml/}. */
    public static Pilot in(Path home) {
        for (Path dir : List.of(home, home.resolve("sysml"))) {
            if (!Files.isDirectory(dir.resolve("sysml.library"))) continue;
            try (Stream<Path> files = Files.list(dir)) {
                List<Path> jars = files.filter(f -> f.getFileName().toString().endsWith(".jar")).sorted().toList();
                if (jars.size() == 1) return at(jars.get(0), dir.resolve("sysml.library"));
                if (jars.size() > 1)
                    throw new PilotException("unavailable", dir + " holds more than one jar (" + jars + "); name the Pilot's jar with Pilot.at");
            } catch (IOException e) {
                throw new PilotException("unavailable", "cannot list " + dir + ": " + e);
            }
        }
        throw new PilotException("unavailable", home.toAbsolutePath() + " holds no Pilot: a jar and a sysml.library directory beside it, or both under sysml/");
    }

    /** {@code SYSML_PILOT_HOME}, else the cached copy of the pinned release, else that release downloaded. */
    public static Pilot locate() {
        String home = System.getenv(HOME_VARIABLE);
        if (home != null && !home.isEmpty()) return in(Path.of(home));
        return download(cacheRoot());
    }

    /** {@code SYSML_PILOT_HOME}, else the cached copy of the pinned release, else null: {@link #locate()}
     *  without downloading. */
    public static Pilot find() {
        String home = System.getenv(HOME_VARIABLE);
        if (home != null && !home.isEmpty()) return in(Path.of(home));
        Path dir = cacheRoot().resolve("pilot-" + VERSION);
        if (!Files.isRegularFile(dir.resolve(".complete"))) return null;
        return at(dir.resolve(JAR_ENTRY.substring("sysml/".length())), dir.resolve("sysml.library"));
    }

    /** The pinned release in {@code cacheRoot}, downloaded there first when absent. */
    public static synchronized Pilot download(Path cacheRoot) {
        Path dir = cacheRoot.resolve("pilot-" + VERSION);
        Path jarFile = dir.resolve(JAR_ENTRY.substring("sysml/".length()));
        if (Files.isRegularFile(dir.resolve(".complete"))) return at(jarFile, dir.resolve("sysml.library"));

        String url = "https://github.com/" + REPOSITORY + "/releases/download/" + RELEASE + "/" + ASSET;
        String byHand = "download " + url + ", unpack it, and set " + HOME_VARIABLE + " to the directory it unpacks to";
        Path work = null;
        try {
            Files.createDirectories(cacheRoot);
            work = Files.createTempDirectory(cacheRoot, "pilot-" + VERSION + "-");
            Path zip = work.resolve(ASSET);
            System.err.println("Downloading the SysML v2 Pilot Implementation " + VERSION + " (about 130 MB) from " + url);
            fetch(url, zip);
            String sha256 = sha256(zip);
            if (!sha256.equals(ASSET_SHA256))
                throw new PilotException("unavailable", ASSET + " does not match its pinned SHA-256 (" + sha256 + ", not "
                    + ASSET_SHA256 + "); nothing was kept");
            Path staged = Files.createDirectories(work.resolve("pilot-" + VERSION));
            boolean jarFound = false, libraryFound = false;
            try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zip))) {
                for (ZipEntry e; (e = in.getNextEntry()) != null; ) {
                    String name = e.getName();
                    Path target = null;
                    if (name.equals(JAR_ENTRY)) {
                        target = staged.resolve(name.substring("sysml/".length()));
                        jarFound = true;
                    } else if (name.equals("LICENSE")) {
                        target = staged.resolve("LICENSE");
                    } else if (name.startsWith(LIBRARY_ENTRY) && !e.isDirectory()) {
                        target = staged.resolve("sysml.library").resolve(name.substring(LIBRARY_ENTRY.length())).normalize();
                        if (!target.startsWith(staged)) throw new IOException("the archive's entry " + name + " points outside it");
                        libraryFound = true;
                    }
                    if (target == null) continue;
                    Files.createDirectories(target.getParent());
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            if (!jarFound || !libraryFound)
                throw new PilotException("unavailable", ASSET + " is not the archive the SDK expects (it lacks "
                    + (jarFound ? LIBRARY_ENTRY : JAR_ENTRY) + "); " + byHand);
            Files.writeString(staged.resolve(".complete"), RELEASE + "\n" + ASSET_SHA256 + "\n");
            if (Files.exists(dir)) deleteTree(dir);  // a copy left incomplete by an earlier call
            Files.move(staged, dir, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            if (Files.isRegularFile(dir.resolve(".complete")))  // another process kept it first
                return at(jarFile, dir.resolve("sysml.library"));
            throw new PilotException("unavailable", "cannot download the Pilot into " + cacheRoot + " (" + e + "); " + byHand);
        } finally {
            if (work != null) try { deleteTree(work); } catch (IOException ignored) {}
        }
        return at(jarFile, dir.resolve("sysml.library"));
    }

    /** The class loader of this Pilot's jar, created once. Its parent is the platform class loader,
     *  so the Pilot's own libraries (EMF, Xtext, Guice, Gson, logging) never meet the application's. */
    synchronized ClassLoader loader() {
        if (loader == null) {
            try {
                loader = new URLClassLoader("sysml-pilot-" + version, new URL[] {jar.toUri().toURL()},
                    ClassLoader.getPlatformClassLoader());
            } catch (IOException e) {
                throw new PilotException("unavailable", "cannot load " + jar + ": " + e);
            }
            quietLogging(loader);
        }
        return loader;
    }

    /** The Pilot logs through log4j 1.2 without configuring it, which warns on standard error on the
     *  first message; a root logger that discards keeps the warning and the log out of the
     *  application's output (the Pilot's failures reach the SDK as exceptions). */
    private static void quietLogging(ClassLoader loader) {
        Thread t = Thread.currentThread();
        ClassLoader previous = t.getContextClassLoader();
        t.setContextClassLoader(loader);
        try {
            Class<?> logger = loader.loadClass("org.apache.log4j.Logger");
            Class<?> appender = loader.loadClass("org.apache.log4j.Appender");
            Object root = logger.getMethod("getRootLogger").invoke(null);
            if (!((java.util.Enumeration<?>) logger.getMethod("getAllAppenders").invoke(root)).hasMoreElements())
                logger.getMethod("addAppender", appender).invoke(root,
                    loader.loadClass("org.apache.log4j.varia.NullAppender").getConstructor().newInstance());
        } catch (ReflectiveOperationException | LinkageError ignored) {
            // another Pilot build, logging some other way: its output stays as it is
        } finally {
            t.setContextClassLoader(previous);
        }
    }

    /** The user's cache directory, the one the SDK's other languages use: {@code SYSML_CACHE_DIR},
     *  else the platform's per-user cache directory. */
    static Path cacheRoot() {
        String env = System.getenv(CACHE_VARIABLE);
        if (env != null && !env.isEmpty()) return Path.of(env);
        String os = System.getProperty("os.name").toLowerCase();
        Path home = Path.of(System.getProperty("user.home"));
        if (os.contains("win")) {
            String local = System.getenv("LOCALAPPDATA");
            return (local != null && !local.isEmpty() ? Path.of(local) : home.resolve("AppData").resolve("Local"))
                .resolve("sysml").resolve("Cache");
        }
        if (os.contains("mac")) return home.resolve("Library").resolve("Caches").resolve("sysml");
        String xdg = System.getenv("XDG_CACHE_HOME");
        return (xdg != null && !xdg.isEmpty() ? Path.of(xdg) : home.resolve(".cache")).resolve("sysml");
    }

    private static void fetch(String url, Path to) throws IOException {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(30)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofMinutes(30))
            .header("User-Agent", "sysml-sdk").build();
        try {
            HttpResponse<Path> r = client.send(request, HttpResponse.BodyHandlers.ofFile(to));
            if (r.statusCode() != 200) throw new IOException("HTTP " + r.statusCode());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted");
        }
    }

    private static String sha256(Path file) throws IOException {
        try (InputStream in = new DigestInputStream(Files.newInputStream(file), MessageDigest.getInstance("SHA-256"))) {
            in.transferTo(java.io.OutputStream.nullOutputStream());
            return HexFormat.of().formatHex(((DigestInputStream) in).getMessageDigest().digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void deleteTree(Path p) throws IOException {
        if (!Files.exists(p)) return;
        try (var walk = Files.walk(p)) {
            for (Path q : walk.sorted(java.util.Comparator.reverseOrder()).toList()) Files.delete(q);
        }
    }
}
