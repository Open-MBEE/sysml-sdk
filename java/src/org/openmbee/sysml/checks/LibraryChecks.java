// The standard library helpers, offline: GitHub is replaced by a fake fetcher that serves the
// release metadata and the library archive from memory.
package org.openmbee.sysml.checks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openmbee.sysml.LibraryUnavailableException;
import org.openmbee.sysml.StandardLibrary;

public final class LibraryChecks {
    static final String VERSION = "9.9.9", STEM = "sysml_library-" + VERSION;
    static final String API = "https://api.github.com/repos/Open-MBEE/sysml-sdk/releases/tags/v" + VERSION;
    static final String ASSET = "https://github.com/Open-MBEE/sysml-sdk/releases/download/v" + VERSION + "/" + STEM + ".zip";
    static int failures = 0;

    static void check(boolean cond, String what) {
        System.out.println((cond ? "ok: " : "FAIL: ") + what);
        if (!cond) failures++;
    }

    static void rejects(Runnable body, String pattern, String what) {
        try {
            body.run();
            check(false, what + " (no error)");
        } catch (LibraryUnavailableException e) {
            check(e.getMessage().contains(pattern), what + ": " + e.getMessage().substring(0, Math.min(90, e.getMessage().length())));
        }
    }

    static byte[] zip(String[][] entries) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (ZipOutputStream z = new ZipOutputStream(buf)) {
            for (String[] e : entries) {
                z.putNextEntry(new ZipEntry(e[0]));
                z.write(e[1].getBytes(StandardCharsets.UTF_8));
                z.closeEntry();
            }
        }
        return buf.toByteArray();
    }

    static byte[] archive() throws IOException {
        return zip(new String[][] {{STEM + "/sysml.library.full.json", "[{\"@id\": \"lib\"}]"}, {STEM + "/LICENSE", "EPL"},
            {STEM + "/NOTICE", "notice"}, {STEM + "/README.txt", "readme"}});
    }

    static String sha256(byte[] data) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
    }

    /** A fake GitHub: URL to bytes; every URL asked is recorded. */
    static final class GitHub implements StandardLibrary.Fetcher {
        final Map<String, byte[]> served;
        final List<String> asked = new ArrayList<>();
        GitHub(Map<String, byte[]> served) { this.served = served; }
        public byte[] get(String url, String accept) throws IOException {
            asked.add(url);
            byte[] b = served.get(url);
            if (b == null) throw new IOException("no route to host");
            return b;
        }
    }

    static GitHub release(byte[] data, String digest) {
        String api = "{\"assets\": [{\"name\": \"" + STEM + ".zip\", \"browser_download_url\": \"" + ASSET + "\", \"digest\": \"" + digest + "\"}]}";
        return new GitHub(Map.of(API, api.getBytes(StandardCharsets.UTF_8), ASSET, data));
    }

    public static void main(String[] args) throws Exception {
        if (System.getenv("SYSML_LIBRARY_JSON") != null) {
            System.out.println("skipped: SYSML_LIBRARY_JSON is set");
            return;
        }
        byte[] good = archive();
        {
            Path dir = Files.createTempDirectory("sysml-library-");
            GitHub g = release(good, "sha256:" + sha256(good));
            Path path = StandardLibrary.json(VERSION, dir, g);
            check(path.equals(dir.resolve(STEM).resolve("sysml.library.full.json")), "the JSON lands in the cache");
            check(Files.readString(path).equals("[{\"@id\": \"lib\"}]"), "the JSON is the archive's");
            try (var s = Files.list(dir.resolve(STEM))) {
                check(s.map(p -> p.getFileName().toString()).sorted().toList()
                    .equals(List.of("LICENSE", "NOTICE", "README.txt", "sysml.library.full.json")), "with its license and notice");
            }
            try (var s = Files.list(dir)) { check(s.count() == 1, "no work files left behind"); }
            GitHub again = new GitHub(Map.of());
            check(StandardLibrary.json(VERSION, dir, again).equals(path) && again.asked.isEmpty(),
                "the second call reads the cache without asking GitHub");
        }
        {
            Path dir = Files.createTempDirectory("sysml-library-");
            rejects(() -> StandardLibrary.json(VERSION, dir, release(good, "sha256:" + "0".repeat(64))),
                "does not match the SHA-256", "a digest mismatch");
            try (var s = Files.list(dir)) { check(s.count() == 0, "a digest mismatch keeps nothing"); }
            GitHub g = release(good, "");
            rejects(() -> StandardLibrary.json(VERSION, dir, g), "states no SHA-256", "no digest");
            check(g.asked.size() == 1, "no digest, no download");
            rejects(() -> StandardLibrary.json(VERSION, dir, new GitHub(Map.of(API, "{\"assets\": []}".getBytes()))),
                "has no " + STEM + ".zip", "no such asset");
            rejects(() -> StandardLibrary.json(VERSION, dir, new GitHub(Map.of())), "SYSML_LIBRARY_JSON", "offline: says what to do");
            byte[] other = zip(new String[][] {{"other.txt", "x"}});
            rejects(() -> StandardLibrary.json(VERSION, dir, release(other, "sha256:" + sha256OrThrow(other))),
                "not the archive", "not the expected archive");
        }
        {
            Path dir = Files.createTempDirectory("sysml-library-");
            Files.createDirectories(dir.resolve(STEM));
            Files.writeString(dir.resolve(STEM).resolve("LICENSE"), "left over");
            StandardLibrary.json(VERSION, dir, release(good, "sha256:" + sha256(good)));
            check(Files.readString(dir.resolve(STEM).resolve("LICENSE")).equals("EPL"), "an incomplete copy is replaced");
        }
        try {
            Path lib = StandardLibrary.directory();
            check(Files.isRegularFile(lib.resolve("LICENSE")), "StandardLibrary.directory(): " + lib);
        } catch (LibraryUnavailableException e) {
            check(e.getMessage().contains("carries no standard library models"),
                "StandardLibrary.directory() without the models in the class path says so");
        }
        if (failures > 0) {
            System.out.println(failures + " LIBRARY CHECK(S) FAILED");
            System.exit(1);
        }
        System.out.println("ALL JAVA LIBRARY CHECKS PASSED");
    }

    static String sha256OrThrow(byte[] data) {
        try { return sha256(data); } catch (Exception e) { throw new IllegalStateException(e); }
    }
}
