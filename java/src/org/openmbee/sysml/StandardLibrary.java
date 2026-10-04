package org.openmbee.sysml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The KerML and SysML standard library, in the two forms the backends read.
 *
 * <p>{@link #directory()} is the directory of the library's models this jar carries, for the SysML
 * Toolkit backend: {@code ToolkitBackend.open(ToolkitBackend.library(), paths, null,
 * StandardLibrary.directory().toString())}. A session reads a directory, so the first call copies
 * the models out of the jar into the user's cache.
 *
 * <p>{@link #json()} is the same library as full-form interchange JSON, for the payload backend
 * ({@link PayloadLibrary}). It is too large for the jar: the first call downloads
 * {@code sysml_library-<version>.zip} from this version's GitHub release, checks it against the
 * SHA-256 GitHub states for that file, and keeps the JSON in the user's cache (the same place the
 * other SDK languages keep it), where later calls find it. Nothing is downloaded unless it is called.
 * {@code SYSML_LIBRARY_JSON} names a copy of the JSON to use instead.
 *
 * <p>Both are the library of the SysML v2 release the SDK was built with, licensed under the Eclipse
 * Public License 2.0, not the SDK's Apache-2.0: see the LICENSE and NOTICE beside them.
 */
public final class StandardLibrary {
    private StandardLibrary() {}

    static final String REPOSITORY = "Open-MBEE/sysml-sdk";
    static final String JSON_VARIABLE = "SYSML_LIBRARY_JSON";
    static final String CACHE_VARIABLE = "SYSML_CACHE_DIR";

    /** Fetches a URL's bytes; a substitute (a proxy, a test) can be passed to {@link #json(String, Path, Fetcher)}. */
    @FunctionalInterface
    public interface Fetcher {
        byte[] get(String url, String accept) throws IOException;
    }

    /** The directory of the standard library models this jar carries, copied out into the user's
     *  cache on the first call. */
    public static synchronized Path directory() {
        String index = resource("stdlib/INDEX");
        if (index == null)
            throw new LibraryUnavailableException("this copy of the SDK carries no standard library models (the "
                + "release's jar does); pass the sysml.library directory of sysml_library-<version>.zip from "
                + "https://github.com/" + REPOSITORY + "/releases as the library directory");
        String digest = sha256(index.getBytes(StandardCharsets.UTF_8)).substring(0, 16);
        Path dir = cacheRoot().resolve("sysml-stdlib-" + version() + "-" + digest);
        Path complete = dir.resolve(".complete");
        if (Files.isRegularFile(complete)) return dir;
        try {
            for (String rel : index.split("\n")) {
                if (rel.isBlank()) continue;
                try (InputStream in = StandardLibrary.class.getResourceAsStream("stdlib/" + rel)) {
                    if (in == null) throw new IOException("the jar lists stdlib/" + rel + " but carries no such file");
                    Path file = dir.resolve(rel);
                    Files.createDirectories(file.getParent());
                    Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            Files.writeString(complete, index);
        } catch (IOException e) {
            throw new LibraryUnavailableException("cannot copy the standard library models out to " + dir + ": " + e);
        }
        return dir;
    }

    /** The standard library as full-form JSON, for {@link PayloadLibrary}: the file
     *  {@code SYSML_LIBRARY_JSON} names, else the cached copy, downloaded from this version's GitHub
     *  release on the first call and checked against the SHA-256 GitHub states for it. */
    public static Path json() {
        return json(null, null, null);
    }

    /** {@link #json()} for another {@code version} (null: this SDK's), cache directory (null:
     *  {@code SYSML_CACHE_DIR}, else the user's cache directory) and way of fetching (null: HTTPS). */
    public static synchronized Path json(String version, Path cacheDir, Fetcher fetch) {
        String named = System.getenv(JSON_VARIABLE);
        if (named != null && !named.isEmpty()) {
            if (!Files.isRegularFile(Path.of(named)))
                throw new LibraryUnavailableException(JSON_VARIABLE + " names " + named + ", which is not a file");
            return Path.of(named);
        }
        if (version == null) version = version();
        if (fetch == null) fetch = StandardLibrary::https;
        String stem = "sysml_library-" + version;
        Path root = cacheDir != null ? cacheDir : cacheRoot();
        Path target = root.resolve(stem);
        Path found = target.resolve("sysml.library.full.json");
        if (Files.isRegularFile(found)) return found;

        String page = "https://github.com/" + REPOSITORY + "/releases/tag/v" + version;
        String byHand = "download " + stem + ".zip from " + page + ", unpack it, and set " + JSON_VARIABLE
            + " to the sysml.library.full.json it holds";
        Map<?, ?> release;
        try {
            byte[] body = fetch.get("https://api.github.com/repos/" + REPOSITORY + "/releases/tags/v" + version,
                "application/vnd.github+json");
            release = (Map<?, ?>) Json.parse(new String(body, StandardCharsets.UTF_8));
        } catch (IOException | RuntimeException e) {
            throw new LibraryUnavailableException("cannot read the release v" + version + " of " + REPOSITORY
                + " (" + e.getMessage() + "); " + byHand);
        }
        Map<?, ?> asset = null;
        if (release.get("assets") instanceof List<?> assets)
            for (Object a : assets)
                if (a instanceof Map<?, ?> m && (stem + ".zip").equals(m.get("name"))) asset = m;
        if (asset == null)
            throw new LibraryUnavailableException("the release v" + version + " of " + REPOSITORY + " has no "
                + stem + ".zip (" + page + ")");
        String digest = asset.get("digest") instanceof String s ? s : "";
        if (!digest.startsWith("sha256:"))
            throw new LibraryUnavailableException("GitHub states no SHA-256 for " + stem + ".zip, so it is not downloaded; " + byHand);
        String url = String.valueOf(asset.get("browser_download_url"));
        byte[] zip;
        try {
            zip = fetch.get(url, "application/octet-stream");
        } catch (IOException | RuntimeException e) {
            throw new LibraryUnavailableException("cannot download " + url + " (" + e.getMessage() + "); " + byHand);
        }
        String sha256 = sha256(zip), want = digest.substring("sha256:".length());
        if (!sha256.equals(want))
            throw new LibraryUnavailableException(stem + ".zip does not match the SHA-256 GitHub states for it ("
                + sha256 + ", not " + want + "); nothing was kept");

        Set<String> wanted = Set.of("sysml.library.full.json", "LICENSE", "NOTICE", "README.txt");
        Path work = null;
        try {
            Files.createDirectories(root);
            work = Files.createTempDirectory(root, stem + "-");
            Path staged = Files.createDirectories(work.resolve(stem));
            int extracted = 0;
            try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
                for (ZipEntry e; (e = in.getNextEntry()) != null; ) {
                    String name = e.getName();
                    if (name.startsWith(stem + "/") && wanted.contains(name.substring(stem.length() + 1))) {
                        Files.copy(in, staged.resolve(name.substring(stem.length() + 1)));
                        extracted++;
                    }
                }
            }
            if (extracted != wanted.size())
                throw new LibraryUnavailableException(stem + ".zip is not the archive the SDK expects (it lacks some of "
                    + wanted + "); " + byHand);
            if (Files.exists(target)) deleteTree(target);  // a copy left incomplete by an earlier call
            Files.move(staged, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new LibraryUnavailableException("cannot keep " + stem + " in " + root + ": " + e);
        } finally {
            if (work != null) try { deleteTree(work); } catch (IOException ignored) {}
        }
        return found;
    }

    /** This SDK's version, from the jar's manifest. */
    static String version() {
        String v = StandardLibrary.class.getPackage().getImplementationVersion();
        if (v == null)
            throw new LibraryUnavailableException("cannot tell this SDK's version outside its jar; pass the version");
        return v;
    }

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

    private static String resource(String name) {
        try (InputStream in = StandardLibrary.class.getResourceAsStream(name)) {
            return in == null ? null : new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    private static String sha256(byte[] data) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] https(String url, String accept) throws IOException {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(30)).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofMinutes(5))
            .header("User-Agent", "sysml-sdk").header("Accept", accept).build();
        try {
            HttpResponse<byte[]> r = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (r.statusCode() != 200) throw new IOException("HTTP " + r.statusCode());
            return r.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted");
        }
    }

    private static void deleteTree(Path p) throws IOException {
        if (!Files.exists(p)) return;
        try (var walk = Files.walk(p)) {
            for (Path q : walk.sorted(java.util.Comparator.reverseOrder()).toList()) Files.delete(q);
        }
    }
}
