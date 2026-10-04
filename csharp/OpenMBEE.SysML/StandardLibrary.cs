// The KerML and SysML standard library, in the two forms the backends read.
//
// StandardLibrary.Directory() is the directory of the library's models this package carries, for
// the SysML Toolkit backend: ToolkitBackend.Open(ToolkitBackend.DefaultLibrary, paths, null,
// StandardLibrary.Directory()). A session reads a directory, so the first call copies the models out
// of the assembly into the user's cache.
//
// StandardLibrary.Json() is the same library as full-form interchange JSON, for the payload backend
// (PayloadLibrary). It is too large for the package: the first call downloads
// sysml_library-<version>.zip from this version's GitHub release, checks it against the SHA-256
// GitHub states for that file, and keeps the JSON in the user's cache (the same place the other SDK
// languages keep it), where later calls find it. Nothing is downloaded unless it is called.
// SYSML_LIBRARY_JSON names a copy of the JSON to use instead.
//
// Both are the library of the SysML v2 release the SDK was built with, licensed under the Eclipse
// Public License 2.0, not the SDK's Apache-2.0: see the LICENSE and NOTICE beside them.

using System.IO.Compression;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;

namespace OpenMBEE.SysML;

/// <summary>The standard library is not at hand: this package carries no models, or the JSON could
/// not be downloaded and checked.</summary>
public sealed class LibraryUnavailableException : SdkException
{
    public LibraryUnavailableException(string message) : base(message) { }
}

public static class StandardLibrary
{
    const string Repository = "Open-MBEE/sysml-sdk";
    const string JsonVariable = "SYSML_LIBRARY_JSON";
    const string CacheVariable = "SYSML_CACHE_DIR";
    const string ResourcePrefix = "stdlib/";
    static readonly object Gate = new();

    /// <summary>The directory of the standard library models this package carries, copied out into
    /// the user's cache on the first call.</summary>
    public static string Directory()
    {
        lock (Gate)
        {
            var assembly = typeof(StandardLibrary).Assembly;
            var names = assembly.GetManifestResourceNames().Where(n => n.StartsWith(ResourcePrefix)).OrderBy(n => n, StringComparer.Ordinal).ToList();
            if (names.Count == 0)
                throw new LibraryUnavailableException("this copy of the SDK carries no standard library models (the NuGet package "
                    + "does); pass the sysml.library directory of sysml_library-<version>.zip from "
                    + $"https://github.com/{Repository}/releases as the library directory");
            var digest = Sha256(Encoding.UTF8.GetBytes(string.Join("\n", names)))[..16];
            var dir = Path.Combine(CacheRoot(), $"sysml-stdlib-{Version()}-{digest}");
            var complete = Path.Combine(dir, ".complete");
            if (File.Exists(complete)) return dir;
            try
            {
                foreach (var name in names)
                {
                    // MSBuild spells the directories of a resource's name with the separator of the
                    // machine that built the package.
                    var parts = name[ResourcePrefix.Length..].Split('/', '\\');
                    var file = Path.Combine([dir, .. parts]);
                    System.IO.Directory.CreateDirectory(Path.GetDirectoryName(file)!);
                    using var input = assembly.GetManifestResourceStream(name)!;
                    using var output = File.Create(file);
                    input.CopyTo(output);
                }
                File.WriteAllText(complete, string.Join("\n", names));
            }
            catch (IOException e)
            {
                throw new LibraryUnavailableException($"cannot copy the standard library models out to {dir}: {e.Message}");
            }
            return dir;
        }
    }

    /// <summary>The standard library as full-form JSON, for PayloadLibrary: the file
    /// SYSML_LIBRARY_JSON names, else the cached copy (<paramref name="cacheDir"/>, else
    /// SYSML_CACHE_DIR, else the user's cache directory), downloaded from the GitHub release of
    /// <paramref name="version"/> (default: this SDK's) on the first call and checked against the
    /// SHA-256 GitHub states for it. <paramref name="fetch"/> (URL, Accept header) to bytes replaces
    /// HTTPS, for a proxy or a test.</summary>
    public static string Json(string? version = null, string? cacheDir = null, Func<string, string, byte[]>? fetch = null)
    {
        lock (Gate)
        {
            var named = Environment.GetEnvironmentVariable(JsonVariable);
            if (!string.IsNullOrEmpty(named))
            {
                if (!File.Exists(named))
                    throw new LibraryUnavailableException($"{JsonVariable} names {named}, which is not a file");
                return named;
            }
            version ??= Version();
            fetch ??= Https;
            var stem = $"sysml_library-{version}";
            var root = cacheDir ?? CacheRoot();
            var target = Path.Combine(root, stem);
            var found = Path.Combine(target, "sysml.library.full.json");
            if (File.Exists(found)) return found;

            var page = $"https://github.com/{Repository}/releases/tag/v{version}";
            var byHand = $"download {stem}.zip from {page}, unpack it, and set {JsonVariable} to the sysml.library.full.json it holds";
            JsonElement release;
            try
            {
                var document = JsonDocument.Parse(fetch($"https://api.github.com/repos/{Repository}/releases/tags/v{version}",
                    "application/vnd.github+json"));
                release = document.RootElement;
            }
            catch (Exception e) when (e is IOException or HttpRequestException or JsonException or TaskCanceledException)
            {
                throw new LibraryUnavailableException($"cannot read the release v{version} of {Repository} ({e.Message}); {byHand}");
            }
            JsonElement? asset = null;
            if (release.ValueKind == JsonValueKind.Object && release.TryGetProperty("assets", out var assets) && assets.ValueKind == JsonValueKind.Array)
                foreach (var a in assets.EnumerateArray())
                    if (a.TryGetProperty("name", out var n) && n.GetString() == $"{stem}.zip")
                        asset = a;
            if (asset is not { } chosen)
                throw new LibraryUnavailableException($"the release v{version} of {Repository} has no {stem}.zip ({page})");
            var digest = chosen.TryGetProperty("digest", out var d) && d.ValueKind == JsonValueKind.String ? d.GetString()! : "";
            if (!digest.StartsWith("sha256:"))
                throw new LibraryUnavailableException($"GitHub states no SHA-256 for {stem}.zip, so it is not downloaded; {byHand}");
            var url = chosen.GetProperty("browser_download_url").GetString()!;
            byte[] zip;
            try
            {
                zip = fetch(url, "application/octet-stream");
            }
            catch (Exception e) when (e is IOException or HttpRequestException or TaskCanceledException)
            {
                throw new LibraryUnavailableException($"cannot download {url} ({e.Message}); {byHand}");
            }
            var sha256 = Sha256(zip);
            var want = digest["sha256:".Length..];
            if (sha256 != want)
                throw new LibraryUnavailableException($"{stem}.zip does not match the SHA-256 GitHub states for it ({sha256}, not {want}); nothing was kept");

            string[] wanted = ["sysml.library.full.json", "LICENSE", "NOTICE", "README.txt"];
            System.IO.Directory.CreateDirectory(root);
            var work = Path.Combine(root, $"{stem}-{Guid.NewGuid():N}");
            try
            {
                var staged = System.IO.Directory.CreateDirectory(Path.Combine(work, stem)).FullName;
                try
                {
                    using var archive = new ZipArchive(new MemoryStream(zip), ZipArchiveMode.Read);
                    foreach (var name in wanted)
                    {
                        var entry = archive.GetEntry($"{stem}/{name}")
                            ?? throw new InvalidDataException($"no {stem}/{name}");
                        entry.ExtractToFile(Path.Combine(staged, name));
                    }
                }
                catch (InvalidDataException e)
                {
                    throw new LibraryUnavailableException($"{stem}.zip is not the archive the SDK expects ({e.Message}); {byHand}");
                }
                if (System.IO.Directory.Exists(target)) System.IO.Directory.Delete(target, true);  // left incomplete earlier
                System.IO.Directory.Move(staged, target);
            }
            catch (IOException e)
            {
                throw new LibraryUnavailableException($"cannot keep {stem} in {root}: {e.Message}");
            }
            finally
            {
                if (System.IO.Directory.Exists(work)) System.IO.Directory.Delete(work, true);
            }
            return found;
        }
    }

    /// <summary>This SDK's version, from the assembly.</summary>
    static string Version()
    {
        var v = typeof(StandardLibrary).Assembly.GetCustomAttribute<AssemblyInformationalVersionAttribute>()?.InformationalVersion
            ?? throw new LibraryUnavailableException("cannot tell this SDK's version; pass the version");
        var plus = v.IndexOf('+');   // build metadata the SDK appends (+commit)
        return plus < 0 ? v : v[..plus];
    }

    static string CacheRoot()
    {
        var env = Environment.GetEnvironmentVariable(CacheVariable);
        if (!string.IsNullOrEmpty(env)) return env;
        var home = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
        if (OperatingSystem.IsWindows())
        {
            var local = Environment.GetEnvironmentVariable("LOCALAPPDATA");
            return Path.Combine(string.IsNullOrEmpty(local) ? Path.Combine(home, "AppData", "Local") : local, "sysml", "Cache");
        }
        if (OperatingSystem.IsMacOS()) return Path.Combine(home, "Library", "Caches", "sysml");
        var xdg = Environment.GetEnvironmentVariable("XDG_CACHE_HOME");
        return Path.Combine(string.IsNullOrEmpty(xdg) ? Path.Combine(home, ".cache") : xdg, "sysml");
    }

    static string Sha256(byte[] data) => Convert.ToHexString(SHA256.HashData(data)).ToLowerInvariant();

    static byte[] Https(string url, string accept)
    {
        using var client = new HttpClient { Timeout = TimeSpan.FromMinutes(5) };
        using var request = new HttpRequestMessage(HttpMethod.Get, url);
        request.Headers.UserAgent.ParseAdd("sysml-sdk");
        request.Headers.Accept.ParseAdd(accept);
        using var response = client.Send(request);
        if (!response.IsSuccessStatusCode) throw new HttpRequestException($"HTTP {(int)response.StatusCode}");
        using var body = response.Content.ReadAsStream();
        using var buffer = new MemoryStream();
        body.CopyTo(buffer);
        return buffer.ToArray();
    }
}
