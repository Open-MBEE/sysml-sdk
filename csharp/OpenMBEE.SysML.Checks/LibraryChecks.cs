// The standard library helpers, offline: GitHub is replaced by a fake fetch that serves the release
// metadata and the library archive from memory.

using System.IO.Compression;
using System.Security.Cryptography;
using System.Text;

namespace OpenMBEE.SysML.Checks;

public static class LibraryChecks
{
    const string Version = "9.9.9";
    const string Stem = "sysml_library-" + Version;
    const string Api = "https://api.github.com/repos/Open-MBEE/sysml-sdk/releases/tags/v" + Version;
    const string Asset = "https://github.com/Open-MBEE/sysml-sdk/releases/download/v" + Version + "/" + Stem + ".zip";

    static void Check(bool cond, string what)
    {
        if (!cond) throw new Exception("FAILED: " + what);
        Console.WriteLine("ok: " + what);
    }

    static void Rejects(Action body, string pattern, string what)
    {
        try
        {
            body();
        }
        catch (LibraryUnavailableException e)
        {
            Check(e.Message.Contains(pattern), what + ": " + e.Message[..Math.Min(90, e.Message.Length)]);
            return;
        }
        Check(false, what + " (no error)");
    }

    static byte[] Zip(params (string Name, string Text)[] entries)
    {
        var buffer = new MemoryStream();
        using (var z = new ZipArchive(buffer, ZipArchiveMode.Create, true))
            foreach (var (name, text) in entries)
            {
                using var w = new StreamWriter(z.CreateEntry(name).Open());
                w.Write(text);
            }
        return buffer.ToArray();
    }

    static byte[] Archive() => Zip(($"{Stem}/sysml.library.full.json", "[{\"@id\": \"lib\"}]"), ($"{Stem}/LICENSE", "EPL"),
        ($"{Stem}/NOTICE", "notice"), ($"{Stem}/README.txt", "readme"));

    static string Sha256(byte[] data) => Convert.ToHexString(SHA256.HashData(data)).ToLowerInvariant();

    /// <summary>A fake GitHub: URL to bytes; every URL asked is recorded.</summary>
    sealed class GitHub(Dictionary<string, byte[]> served)
    {
        public readonly List<string> Asked = [];
        public byte[] Fetch(string url, string accept)
        {
            Asked.Add(url);
            return served.TryGetValue(url, out var b) ? b : throw new IOException("no route to host");
        }
    }

    static GitHub Release(byte[] data, string digest) => new(new()
    {
        [Api] = Encoding.UTF8.GetBytes($"{{\"assets\": [{{\"name\": \"{Stem}.zip\", \"browser_download_url\": \"{Asset}\", \"digest\": \"{digest}\"}}]}}"),
        [Asset] = data,
    });

    static string TempDir() => Directory.CreateTempSubdirectory("sysml-library-").FullName;

    public static void Run()
    {
        if (Environment.GetEnvironmentVariable("SYSML_LIBRARY_JSON") is not null)
        {
            Console.WriteLine("library checks skipped: SYSML_LIBRARY_JSON is set");
            return;
        }
        var good = Archive();
        {
            var dir = TempDir();
            var path = StandardLibrary.Json(Version, dir, Release(good, "sha256:" + Sha256(good)).Fetch);
            Check(path == Path.Combine(dir, Stem, "sysml.library.full.json"), "the JSON lands in the cache");
            Check(File.ReadAllText(path) == "[{\"@id\": \"lib\"}]", "the JSON is the archive's");
            Check(string.Join(",", Directory.GetFiles(Path.Combine(dir, Stem)).Select(Path.GetFileName).Order(StringComparer.Ordinal))
                == "LICENSE,NOTICE,README.txt,sysml.library.full.json", "with its license and notice");
            Check(Directory.GetFileSystemEntries(dir).Length == 1, "no work files left behind");
            var again = new GitHub(new());
            Check(StandardLibrary.Json(Version, dir, again.Fetch) == path && again.Asked.Count == 0,
                "the second call reads the cache without asking GitHub");
        }
        {
            var dir = TempDir();
            Rejects(() => StandardLibrary.Json(Version, dir, Release(good, "sha256:" + new string('0', 64)).Fetch),
                "does not match the SHA-256", "a digest mismatch");
            Check(Directory.GetFileSystemEntries(dir).Length == 0, "a digest mismatch keeps nothing");
            var g = Release(good, "");
            Rejects(() => StandardLibrary.Json(Version, dir, g.Fetch), "states no SHA-256", "no digest");
            Check(g.Asked.Count == 1, "no digest, no download");
            Rejects(() => StandardLibrary.Json(Version, dir, new GitHub(new() { [Api] = "{\"assets\": []}"u8.ToArray() }).Fetch),
                $"has no {Stem}.zip", "no such asset");
            Rejects(() => StandardLibrary.Json(Version, dir, new GitHub(new()).Fetch), "SYSML_LIBRARY_JSON", "offline: says what to do");
            var other = Zip(("other.txt", "x"));
            Rejects(() => StandardLibrary.Json(Version, dir, Release(other, "sha256:" + Sha256(other)).Fetch),
                "not the archive", "not the expected archive");
        }
        {
            var dir = TempDir();
            Directory.CreateDirectory(Path.Combine(dir, Stem));
            File.WriteAllText(Path.Combine(dir, Stem, "LICENSE"), "left over");
            StandardLibrary.Json(Version, dir, Release(good, "sha256:" + Sha256(good)).Fetch);
            Check(File.ReadAllText(Path.Combine(dir, Stem, "LICENSE")) == "EPL", "an incomplete copy is replaced");
        }
        try
        {
            var lib = StandardLibrary.Directory();
            Check(File.Exists(Path.Combine(lib, "LICENSE")), "StandardLibrary.Directory(): " + lib);
        }
        catch (LibraryUnavailableException e)
        {
            Check(e.Message.Contains("carries no standard library models"),
                "StandardLibrary.Directory() without the models in the assembly says so");
        }
        Console.WriteLine("ALL C# LIBRARY CHECKS PASSED");
    }
}
