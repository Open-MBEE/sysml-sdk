// A backend over an engine that speaks the SDK's engine protocol (docs/engine-protocol.md): the
// engine runs as a process and exchanges one JSON request and one JSON answer per line over its
// standard input and output. EngineBackend.Start runs any such engine; the Pilot Implementation
// backend is one.
//
// Errors: a property the element's metaclass does not have reads as absent; a member the engine does
// not answer throws NotAnsweredException; a reference it could not resolve throws
// UnresolvedReferenceException; an element no longer known throws GoneException; a failure inside
// the engine, an operation the element's metaclass does not have, or an engine that cannot start
// throws BackendException.

using System.Diagnostics;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;

namespace OpenMBEE.SysML;

/// <summary>One way to reach an engine: a request line in, an answer line out, and the engine's
/// name and version as it announced them. <see cref="EngineProcess"/> is the engine in a process;
/// other transports reach an engine in this process.</summary>
public interface IEngineTransport : IDisposable
{
    string Engine { get; }
    string Version { get; }
    string Request(string line);
}

/// <summary>An engine in a process: one JSON request per line on its input, one answer per line on
/// its output. The first line it writes says whether it is ready.</summary>
public sealed class EngineProcess : IEngineTransport
{
    private readonly Process _process;

    public string Engine { get; }
    public string Version { get; }

    /// <summary>Start the engine <paramref name="command"/> (the program and its arguments) and read
    /// its first line.</summary>
    public EngineProcess(IReadOnlyList<string> command)
    {
        var start = new ProcessStartInfo(command[0])
        {
            RedirectStandardInput = true,
            RedirectStandardOutput = true,
            UseShellExecute = false,
            StandardInputEncoding = new UTF8Encoding(false),
            StandardOutputEncoding = new UTF8Encoding(false),
        };
        foreach (var a in command.Skip(1))
            start.ArgumentList.Add(a);
        try
        {
            _process = Process.Start(start) ?? throw new BackendException($"cannot start the engine {command[0]}");
        }
        catch (System.ComponentModel.Win32Exception e)
        {
            throw new BackendException($"cannot start the engine {command[0]}: {e.Message}");
        }
        var line = _process.StandardOutput.ReadLine();
        JsonElement ready;
        try
        {
            using var doc = JsonDocument.Parse(line ?? "");
            ready = doc.RootElement.Clone();
        }
        catch (JsonException)
        {
            if (!_process.HasExited) _process.Kill();
            throw new BackendException($"the engine {command[0]} did not start (it said '{line}')");
        }
        if (!(ready.TryGetProperty("ready", out var ok) && ok.ValueKind == JsonValueKind.True))
        {
            _process.WaitForExit(30_000);
            var why = ready.TryGetProperty("error", out var e) && e.TryGetProperty("message", out var m) ? m.GetString() : ready.ToString();
            throw new BackendException($"the engine is not available: {why}");
        }
        if (!(ready.TryGetProperty("protocol", out var p) && p.TryGetInt32(out var protocol) && protocol == EngineBackend.Protocol))
        {
            _process.Kill();
            throw new BackendException($"the engine speaks another protocol than this SDK ({EngineBackend.Protocol})");
        }
        Engine = ready.TryGetProperty("engine", out var en) ? en.GetString() ?? "the engine" : "the engine";
        Version = ready.TryGetProperty("version", out var v) ? v.GetString() ?? "" : "";
    }

    public string Request(string line)
    {
        if (_process.HasExited)
            throw new BackendException($"the {Engine} process has ended (status {_process.ExitCode})");
        _process.StandardInput.WriteLine(line);
        _process.StandardInput.Flush();
        return _process.StandardOutput.ReadLine()
            ?? throw new BackendException($"the {Engine} process ended while answering");
    }

    public void Dispose()
    {
        try
        {
            if (!_process.HasExited)
            {
                _process.StandardInput.Close();  // the end of its input: the engine stops
                if (!_process.WaitForExit(30_000)) _process.Kill();
            }
        }
        catch (Exception e) when (e is IOException or InvalidOperationException)
        {
            if (!_process.HasExited) _process.Kill();
        }
        _process.Dispose();
    }
}

/// <summary>An engine behind the backend contract, over an <see cref="IEngineTransport"/>: a process
/// (<see cref="Start"/>) or another transport (<see cref="Open"/>). Not thread-safe.</summary>
public class EngineBackend : IBackend, IDisposable
{
    public const int Protocol = 1;

    private readonly IEngineTransport _transport;
    private int _next;
    private bool _closed;

    /// <summary>The engine's name, as it announced it.</summary>
    public string Engine => _transport.Engine;

    /// <summary>The engine's version, as it announced it.</summary>
    public string Version => _transport.Version;

    /// <summary>What the engine said about opening the model (engine-specific).</summary>
    public JsonElement Stats { get; private set; }

    protected EngineBackend(IEngineTransport transport)
    {
        _transport = transport;
    }

    /// <summary>Start the engine <paramref name="command"/> (the program and its arguments) and
    /// read a model with it: files and directories of .sysml and .kerml files, and in-memory
    /// sources (unit name to text). <paramref name="elaborate"/> asks for the implied
    /// relationships; <paramref name="library"/> names a standard library directory (null: the
    /// engine's own).</summary>
    public static EngineBackend Start(IReadOnlyList<string> command, IEnumerable<string>? paths = null,
        IReadOnlyDictionary<string, string>? sources = null, bool elaborate = true, string? library = null) =>
        Open(new EngineProcess(command), paths, sources, elaborate, library);

    /// <summary>Read a model with the engine <paramref name="transport"/> reaches, as
    /// <see cref="Start"/> does with a process. The backend disposes of the transport.</summary>
    public static EngineBackend Open(IEngineTransport transport, IEnumerable<string>? paths = null,
        IReadOnlyDictionary<string, string>? sources = null, bool elaborate = true, string? library = null)
    {
        var backend = new EngineBackend(transport);
        backend.OpenModel(paths, sources, elaborate, library);
        return backend;
    }

    /// <summary>The <c>open</c> request: read the model.</summary>
    protected void OpenModel(IEnumerable<string>? paths, IReadOnlyDictionary<string, string>? sources, bool elaborate, string? library)
    {
        try
        {
            var request = new JsonObject
            {
                ["paths"] = new JsonArray((paths ?? Array.Empty<string>()).Select(x => (JsonNode?)JsonValue.Create(Path.GetFullPath(x))).ToArray()),
                ["sources"] = new JsonObject((sources ?? new Dictionary<string, string>())
                    .Select(kv => KeyValuePair.Create(kv.Key, (JsonNode?)JsonValue.Create(kv.Value)))),
                ["elaborate"] = elaborate,
                ["library"] = library is null ? null : JsonValue.Create(library),
            };
            Stats = Ask("open", request) ?? default;
        }
        catch
        {
            Dispose();
            throw;
        }
    }

    public void Dispose()
    {
        if (_closed) return;
        _closed = true;
        try
        {
            Send(new JsonObject { ["id"] = 0, ["cmd"] = "close" });
        }
        catch (Exception e) when (e is IOException or InvalidOperationException or BackendException or JsonException)
        {
            // ended already
        }
        _transport.Dispose();
        GC.SuppressFinalize(this);
    }

    private JsonElement Send(JsonObject request)
    {
        using var doc = JsonDocument.Parse(_transport.Request(request.ToJsonString()));
        return doc.RootElement.Clone();
    }

    private sealed class NotApplicable : SdkException
    {
        public NotApplicable(string message) : base(message) { }
    }

    private JsonElement? Ask(string cmd, JsonObject? fields = null)
    {
        if (_closed) throw new SdkException($"this {Engine} backend is closed");
        var request = new JsonObject { ["id"] = ++_next, ["cmd"] = cmd };
        if (fields is not null)
            foreach (var kv in fields.ToList())
            {
                fields.Remove(kv.Key);
                request[kv.Key] = kv.Value;
            }
        var answer = Send(request);
        if (answer.TryGetProperty("error", out var error))
        {
            var kind = error.TryGetProperty("kind", out var k) ? k.GetString() : null;
            var message = error.TryGetProperty("message", out var m) ? m.GetString() ?? "" : "";
            throw kind switch
            {
                "gone" => new GoneException(message),
                "notApplicable" => new NotApplicable(message),
                "refused" => new NotAnsweredException($"{Engine}: {message}"),
                "invalid" => new RefusedException($"{Engine}: {message}"),
                "unresolved" => new UnresolvedReferenceException($"{Engine}: {message}"),
                "badRequest" => new SdkException(message),
                _ => new BackendException($"{Engine}: {message}"),
            };
        }
        return answer.TryGetProperty("ok", out var value) && value.ValueKind != JsonValueKind.Null ? value : null;
    }

    // -- the backend contract -----------------------------------------------------------------

    public JsonElement? Get(string handle, string prop)
    {
        try
        {
            return Ask("get", new JsonObject { ["h"] = handle, ["prop"] = prop });
        }
        catch (NotApplicable)
        {
            return null;  // not a member of this metaclass: absent
        }
    }

    public string Metaclass(string handle) =>
        Ask("metaclass", new JsonObject { ["h"] = handle })?.GetString() ?? throw new BackendException($"{Engine} named no metaclass");

    public string ElementId(string handle) =>
        Ask("elementId", new JsonObject { ["h"] = handle })?.GetString() ?? "";

    public string? Resolve(string qualifiedName) =>
        Ask("resolve", new JsonObject { ["name"] = qualifiedName })?.GetString();

    public IEnumerable<string> AllHandles() =>
        Ask("allHandles")?.EnumerateArray().Select(h => h.GetString()!).ToList() ?? new List<string>();

    /// <summary>A specification operation, evaluated by the engine. Elements are passed as
    /// references, collections element by element.</summary>
    public JsonElement? Call(string handle, string op, IReadOnlyList<object?> args)
    {
        try
        {
            return Ask("call", new JsonObject
            {
                ["h"] = handle,
                ["op"] = op,
                ["args"] = new JsonArray(args.Select(Argument).ToArray()),
            });
        }
        catch (NotApplicable e)
        {
            throw new BackendException($"{Engine}: {e.Message}");
        }
    }

    /// <summary>A convenience member, by the protocol's <c>find</c>: its name and its arguments by
    /// name.</summary>
    public JsonElement? Find(string handle, Find member, IReadOnlyList<object?> args)
    {
        try
        {
            return Ask("find", new JsonObject { ["h"] = handle, ["member"] = member.Name, ["args"] = Named(member, args) });
        }
        catch (NotApplicable e)
        {
            throw new BackendException($"{Engine}: {e.Message}");
        }
    }

    /// <summary>A convenience member on each element, by the protocol's <c>findEach</c>: one request.</summary>
    public IReadOnlyList<JsonElement?> FindEach(IReadOnlyList<string> handles, Find member, IReadOnlyList<object?> args)
    {
        var hs = new JsonArray();
        foreach (var h in handles)
            hs.Add(JsonValue.Create(h));
        JsonElement? answer;
        try
        {
            answer = Ask("findEach", new JsonObject { ["hs"] = hs, ["member"] = member.Name, ["args"] = Named(member, args) });
        }
        catch (NotApplicable e)
        {
            throw new BackendException($"{Engine}: {e.Message}");
        }
        if (answer is not { ValueKind: JsonValueKind.Array } items)
            throw new BackendException($"{Engine}: findEach gave no list");
        return items.EnumerateArray()
            .Select(x => x.ValueKind == JsonValueKind.Null ? (JsonElement?)null : x.Clone())
            .ToList();
    }

    /// <summary>A convenience member's arguments by parameter name, as the protocol passes them.</summary>
    private static JsonObject Named(Find member, IReadOnlyList<object?> args)
    {
        var named = new JsonObject();
        for (var i = 0; i < member.ParamNames.Length; i++)
            named[member.ParamNames[i]] = args[i] switch
            {
                Scope s => JsonValue.Create(s.ToString().ToLowerInvariant()),
                VisibilityFilter v => JsonValue.Create(v.ToString().ToLowerInvariant()),
                var a => Argument(a),
            };
        return named;
    }

    private static JsonNode? Argument(object? a) => a switch
    {
        null => null,
        IElement el => new JsonObject { ["@id"] = el.Handle },
        string s => JsonValue.Create(s),
        bool b => JsonValue.Create(b),
        System.Collections.IEnumerable items => new JsonArray(items.Cast<object?>().Select(Argument).ToArray()),
        long l => JsonValue.Create(l),
        int i => JsonValue.Create(i),
        double d => JsonValue.Create(d),
        _ => JsonValue.Create(a.ToString()),
    };
}
