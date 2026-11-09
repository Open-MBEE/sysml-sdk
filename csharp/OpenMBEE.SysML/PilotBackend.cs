// The SysML v2 Pilot Implementation backend for C#: the Pilot reads the model and computes every
// value with its own code, behind the same IBackend contract as the SysML Toolkit and payload
// backends, so the generated interfaces and Model are untouched.
//
// The Pilot is Java. This backend starts the SDK's Pilot engine (sysml-pilot-server.jar, which this
// package carries) in a Java process: an EngineBackend over the engine protocol. The engine finds
// the Pilot: SYSML_PILOT_HOME (a Pilot jar and its sysml.library), else the copy in the user's
// cache, else the Pilot release the SDK is pinned to, downloaded into that cache on the first use
// (about 130 MB) and checked against its pinned SHA-256. A Java 21 or newer runtime is needed. The
// SDK redistributes nothing of the Pilot (Eclipse Public License 2.0).
//
// Errors: a property the element's metaclass does not have reads as absent; an element no longer
// known throws GoneException; a reference the Pilot could not resolve throws
// UnresolvedReferenceException; a failure inside the Pilot, an operation the Pilot does not have, or a
// Pilot that cannot be found, downloaded or started throws BackendException.

using System.Diagnostics;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;

namespace OpenMBEE.SysML;

/// <summary>How <see cref="PilotBackend"/> runs the Pilot.</summary>
public sealed record PilotOptions
{
    /// <summary>Add the implied relationships and implicit elements of the specification's
    /// semantics (the Pilot's elaboration), to the model and to its standard library.</summary>
    public bool Elaborate { get; init; } = true;
    /// <summary>The <c>java</c> to run; null: JAVA_HOME's, else the PATH's.</summary>
    public string? Java { get; init; }
    /// <summary>The JVM's maximum heap.</summary>
    public string Heap { get; init; } = "4g";
    /// <summary>A directory holding a Pilot jar and its sysml.library, instead of the one the engine finds.</summary>
    public string? PilotHome { get; init; }
    /// <summary>A Pilot jar and its library directory (both or neither), instead of the one the engine finds.</summary>
    public string? PilotJar { get; init; }
    public string? PilotLibrary { get; init; }
    /// <summary>The Pilot engine's jar; null: SYSML_PILOT_SERVER, else the one this package carries.</summary>
    public string? Server { get; init; }
}

/// <summary>The SysML v2 Pilot Implementation behind the backend contract, in a Java process: an
/// <see cref="EngineBackend"/> over the SDK's Pilot engine. Not thread-safe.</summary>
public sealed class PilotBackend : EngineBackend
{
    private const string ServerClass = "org.openmbee.sysml.pilot.PilotServer";
    private const string ServerResource = "pilot/sysml-pilot-server.jar";

    private PilotBackend(IEngineTransport transport) : base(transport) { }

    /// <summary>The version of the Pilot that reads the model.</summary>
    public string PilotVersion => Version;

    /// <summary>Read a model over files and directories of .sysml and .kerml files.</summary>
    public static PilotBackend Open(IEnumerable<string> paths, PilotOptions? options = null) =>
        Open(paths, null, options);

    /// <summary>Read a model over in-memory sources: unit name to text.</summary>
    public static PilotBackend OpenSources(IReadOnlyDictionary<string, string> sources, PilotOptions? options = null) =>
        Open(Array.Empty<string>(), sources, options);

    /// <summary>Read a model over files and in-memory sources. <c>Stats</c> then says what opening
    /// took: the Pilot's version (<c>pilot</c>), <c>libraryMs</c>, <c>modelMs</c>, and the number of
    /// <c>resources</c> read.</summary>
    public static PilotBackend Open(IEnumerable<string> paths, IReadOnlyDictionary<string, string>? sources, PilotOptions? options)
    {
        options ??= new PilotOptions();
        var command = new List<string> { JavaExecutable(options.Java), $"-Xmx{options.Heap}", "-cp", ServerJar(options.Server), ServerClass };
        command.AddRange(PilotArgs(options));
        var backend = new PilotBackend(new EngineProcess(command));
        backend.OpenModel(paths, sources, options.Elaborate, null);
        return backend;
    }

    private static IEnumerable<string> PilotArgs(PilotOptions o)
    {
        if (o.PilotHome is not null)
            return new[] { "--home", o.PilotHome };
        if (o.PilotJar is not null || o.PilotLibrary is not null)
        {
            if (o.PilotJar is null || o.PilotLibrary is null)
                throw new SdkException("name both PilotJar and PilotLibrary, or neither");
            return new[] { "--jar", o.PilotJar, "--library", o.PilotLibrary };
        }
        return Array.Empty<string>();
    }

    /// <summary>The <c>java</c> to run the Pilot with: <paramref name="java"/>, else JAVA_HOME's,
    /// else the PATH's.</summary>
    public static string JavaExecutable(string? java = null)
    {
        if (!string.IsNullOrEmpty(java)) return java;
        var home = Environment.GetEnvironmentVariable("JAVA_HOME");
        if (!string.IsNullOrEmpty(home))
            foreach (var name in new[] { "java.exe", "java" })
            {
                var candidate = Path.Combine(home, "bin", name);
                if (File.Exists(candidate)) return candidate;
            }
        var exe = OperatingSystem.IsWindows() ? "java.exe" : "java";
        foreach (var dir in (Environment.GetEnvironmentVariable("PATH") ?? "").Split(Path.PathSeparator))
        {
            if (dir.Length == 0) continue;
            var candidate = Path.Combine(dir, exe);
            if (File.Exists(candidate)) return candidate;
        }
        throw new BackendException("the Pilot needs a Java 21 or newer runtime: none found (set JAVA_HOME, or put java on the PATH)");
    }

    /// <summary>The Pilot engine's jar: <paramref name="server"/>, else SYSML_PILOT_SERVER, else
    /// the one this package carries, copied out into the user's cache on the first call.</summary>
    public static string ServerJar(string? server = null)
    {
        server ??= Environment.GetEnvironmentVariable("SYSML_PILOT_SERVER");
        if (!string.IsNullOrEmpty(server))
        {
            if (!File.Exists(server) && !System.IO.Directory.Exists(server))
                throw new BackendException($"the Pilot engine {server} does not exist");
            return server;
        }
        var assembly = typeof(PilotBackend).Assembly;
        using var input = assembly.GetManifestResourceStream(ServerResource)
            ?? throw new BackendException("this copy of the SDK carries no Pilot engine (sysml-pilot-server.jar; the release's "
                + "packages do); build it with python tools/package.py pilot-server and set SYSML_PILOT_SERVER to it");
        using var buffer = new MemoryStream();
        input.CopyTo(buffer);
        var bytes = buffer.ToArray();
        var digest = Convert.ToHexString(SHA256.HashData(bytes)).ToLowerInvariant()[..16];
        var jar = Path.Combine(StandardLibrary.CacheRoot(), $"sysml-pilot-server-{digest}.jar");
        if (File.Exists(jar) && new FileInfo(jar).Length == bytes.Length) return jar;
        try
        {
            System.IO.Directory.CreateDirectory(Path.GetDirectoryName(jar)!);
            var part = jar + "." + Environment.ProcessId + ".part";
            File.WriteAllBytes(part, bytes);
            File.Move(part, jar, true);
        }
        catch (IOException) when (File.Exists(jar) && new FileInfo(jar).Length == bytes.Length)
        {
            // another process copied it out first
        }
        catch (IOException e)
        {
            throw new BackendException($"cannot copy the Pilot engine out to {jar}: {e.Message}");
        }
        return jar;
    }

}
