// The SysML Toolkit backend for C#: the binding library (abi/) through P/Invoke function pointers.
// Implements the same IBackend contract as PayloadBackend, so the generated interfaces and
// Model are untouched. Handles are the integers the library mints, carried as decimal strings
// to fit the contract; references come back as {"@id": handle} JSON so Model.Wrap mints
// typed elements exactly as it does for payloads.
//
// Dispatch: Get(handle, prop) finds the declaring class of prop for the element's metaclass
// through ToolkitTable and calls that export. The per-member static form follows once the
// generator emits symbols into the interfaces.
//
// Operations: Call(handle, op, args) finds the operation's row the same way and passes each
// argument as the binding library's parameter C type says.
//
// Status codes follow the ABI specification: NOT_IMPLEMENTED raises
// NotImplementedInToolkitException, NOT_APPLICABLE is an absent value, INVALID_HANDLE raises
// GoneException, UNRESOLVED raises UnresolvedReferenceException.

using System.Runtime.InteropServices;
using System.Text;
using System.Text.Json;

namespace OpenMBEE.SysML;

public sealed unsafe class ToolkitBackend : IBackend, IDisposable
{
    private const int Ok = 0, NotImplemented = 1, NotApplicable = 2, InvalidHandle = 3, Unresolved = 7;

    // -- struct layouts (ABI section 4), x86-64 -------------------------------------------
    [StructLayout(LayoutKind.Sequential)]
    private struct Str { public IntPtr Ptr; public nuint Len; public byte Present; public IntPtr Owner; }
    [StructLayout(LayoutKind.Sequential)]
    private struct Handles { public IntPtr Items; public nuint Len; public IntPtr Owner; }
    [StructLayout(LayoutKind.Sequential)]
    private struct Ref { public byte Present; public ulong Value; }
    [StructLayout(LayoutKind.Sequential)]
    private struct Bool { public byte Present; public byte Value; }
    [StructLayout(LayoutKind.Sequential)]
    private struct Int { public byte Present; public long Value; }
    [StructLayout(LayoutKind.Sequential)]
    private struct Real { public byte Present; public double Value; }
    [StructLayout(LayoutKind.Sequential)]
    private struct LoadOptions
    {
        public IntPtr Paths; public nuint PathsLen;
        public IntPtr SourceNames; public IntPtr SourceTexts; public nuint SourcesLen;
        public Str LibraryDir;
    }

    /// <summary>One loaded library, shared by every session created from it.</summary>
    public sealed class Library
    {
        internal readonly IntPtr Handle;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, void> Free;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int> SessionOpen;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, void> SessionClose;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int> SessionRoots, LastError, UserElements;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, uint, IntPtr, int> FullJson;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, int> Metaclass;
        internal readonly delegate* unmanaged[Cdecl]<IntPtr, IntPtr, IntPtr, int> SessionResolve;
        private readonly Dictionary<string, IntPtr> _members = new();

        public Library(string path)
        {
            Handle = NativeLibrary.Load(path);
            Free = (delegate* unmanaged[Cdecl]<IntPtr, void>)Export("sysmlv2_free");
            SessionOpen = (delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int>)Export("sysmlv2_session_open");
            SessionClose = (delegate* unmanaged[Cdecl]<IntPtr, void>)Export("sysmlv2_session_close");
            SessionRoots = (delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int>)Export("sysmlv2_session_roots");
            LastError = (delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int>)Export("sysmlv2_last_error");
            UserElements = (delegate* unmanaged[Cdecl]<IntPtr, IntPtr, int>)Export("sysmlv2_user_elements");
            FullJson = (delegate* unmanaged[Cdecl]<IntPtr, uint, IntPtr, int>)Export("sysmlv2_session_full_json");
            Metaclass = (delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, int>)Export("sysmlv2_metaclass");
            SessionResolve = (delegate* unmanaged[Cdecl]<IntPtr, IntPtr, IntPtr, int>)Export("sysmlv2_session_resolve");
            var digest = (delegate* unmanaged[Cdecl]<IntPtr>)Export("sysmlv2_names_digest");
            var digestLen = (delegate* unmanaged[Cdecl]<nuint>)Export("sysmlv2_names_digest_len");
            var have = Encoding.UTF8.GetString((byte*)digest(), (int)digestLen());
            if (have != ToolkitTable.NamesDigest)
                throw new SdkException($"binding library {path} was generated from naming table {have[..12]}, this SDK from {ToolkitTable.NamesDigest[..12]}; regenerate one side");
        }

        private IntPtr Export(string name) =>
            NativeLibrary.TryGetExport(Handle, name, out var p) ? p : throw new SdkException($"binding library lacks {name}");

        /// <summary>A per-member function: (session, handle, out*) -> status.</summary>
        internal delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, int> Member(string symbol) =>
            (delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, int>)Symbol(symbol);

        /// <summary>An export's address, for a caller that knows its signature.</summary>
        internal IntPtr Symbol(string symbol)
        {
            if (!_members.TryGetValue(symbol, out var p)) _members[symbol] = p = Export(symbol);
            return p;
        }

        /// <summary>SYSMLV2_ABI; else the library the NuGet package carries for this platform, where
        /// .NET puts it (beside the application, or under runtimes/&lt;rid&gt;/native); else
        /// abi/target/release above the application (a build of the SDK's source).</summary>
        public static string DefaultPath()
        {
            var env = Environment.GetEnvironmentVariable("SYSMLV2_ABI");
            if (env is not null) return env;
            var name = OperatingSystem.IsWindows() ? "sysmlv2_abi.dll" : OperatingSystem.IsMacOS() ? "libsysmlv2_abi.dylib" : "libsysmlv2_abi.so";
            var rid = (OperatingSystem.IsWindows() ? "win" : OperatingSystem.IsMacOS() ? "osx" : "linux")
                + (RuntimeInformation.ProcessArchitecture == Architecture.Arm64 ? "-arm64" : "-x64");
            foreach (var carried in new[] { Path.Combine(AppContext.BaseDirectory, name),
                                            Path.Combine(AppContext.BaseDirectory, "runtimes", rid, "native", name) })
                if (File.Exists(carried)) return carried;
            var d = new DirectoryInfo(AppContext.BaseDirectory);
            while (d is not null)
            {
                var candidate = Path.Combine(d.FullName, "abi", "target", "release", name);
                if (File.Exists(candidate)) return candidate;
                d = d.Parent;
            }
            throw new SdkException("binding library not found: set SYSMLV2_ABI to the library file in the release's sysmlv2_abi archive for this platform (sysmlv2_abi.dll, libsysmlv2_abi.so or libsysmlv2_abi.dylib), or one built from abi/ in the SDK source");
        }
    }

    private static Library? _default;
    public static Library DefaultLibrary => _default ??= new Library(Library.DefaultPath());

    private readonly Library _lib;
    private IntPtr _session;
    private readonly Dictionary<string, string> _metaclass = new();
    private readonly Dictionary<string, (string Symbol, char Code, string Params)?> _declaring = new();

    private ToolkitBackend(Library lib, IntPtr session) { _lib = lib; _session = session; }

    /// <summary>Open a session over files.</summary>
    public static ToolkitBackend Open(params string[] paths) => Open(DefaultLibrary, paths, null, null);

    /// <summary>Open a session over in-memory sources: unit name to text.</summary>
    public static ToolkitBackend OpenSources(IReadOnlyDictionary<string, string> sources) => Open(DefaultLibrary, Array.Empty<string>(), sources, null);

    public static ToolkitBackend Open(Library lib, string[] paths, IReadOnlyDictionary<string, string>? sources, string? libraryDir)
    {
        if (sources is not { Count: > 0 })
            foreach (var p in paths)
                if (!File.Exists(p)) throw new FileNotFoundException($"model file not found: {Path.GetFullPath(p)}", p);
        if (libraryDir is not null && !Directory.Exists(libraryDir))
            throw new DirectoryNotFoundException($"library directory not found: {Path.GetFullPath(libraryDir)}");
        var pins = new List<GCHandle>();
        try
        {
            var opts = new LoadOptions();
            Str[] names = Array.Empty<Str>(), texts = Array.Empty<Str>(), pathArr = Array.Empty<Str>();
            if (sources is { Count: > 0 })
            {
                names = sources.Keys.Select(k => MakeStr(k, pins)).ToArray();
                texts = sources.Values.Select(v => MakeStr(v, pins)).ToArray();
                opts.SourceNames = Pin(names, pins); opts.SourceTexts = Pin(texts, pins); opts.SourcesLen = (nuint)names.Length;
            }
            else
            {
                pathArr = paths.Select(p => MakeStr(p, pins)).ToArray();
                opts.Paths = Pin(pathArr, pins); opts.PathsLen = (nuint)pathArr.Length;
            }
            if (libraryDir is not null) opts.LibraryDir = MakeStr(libraryDir, pins);
            IntPtr session;
            int st = lib.SessionOpen((IntPtr)(&opts), (IntPtr)(&session));
            if (st != Ok) throw new SdkException($"the SysML Toolkit could not load the model: status {st} (the SysML Toolkit's reason is on stderr)");
            return new ToolkitBackend(lib, session);
        }
        finally { foreach (var h in pins) h.Free(); }
    }

    private static IntPtr Pin(object array, List<GCHandle> pins)
    {
        var h = GCHandle.Alloc(array, GCHandleType.Pinned); pins.Add(h); return h.AddrOfPinnedObject();
    }

    /// <summary>The text as UTF-8 in a pinned buffer. The length is the byte count, which exceeds the
    /// string's UTF-16 length for any text beyond ASCII. An empty string still gets a one-byte buffer,
    /// so the pointer is never null.</summary>
    private static Str MakeStr(string s, List<GCHandle> pins)
    {
        var bytes = Encoding.UTF8.GetBytes(s);
        var buffer = bytes.Length == 0 ? new byte[1] : bytes;
        return new Str { Ptr = Pin(buffer, pins), Len = (nuint)bytes.Length, Present = 1, Owner = IntPtr.Zero };
    }

    public void Dispose()
    {
        if (_session != IntPtr.Zero) { _lib.SessionClose(_session); _session = IntPtr.Zero; }
    }

    /// <summary>The model as full-form interchange JSON, from the SysML Toolkit's own emitter.
    /// With <paramref name="closures"/> (the default) the inheritance-aware properties carry the
    /// specification's values, inherited and imported members included; without, the owned side
    /// only, as the SysML Toolkit's command-line export writes them.</summary>
    public string FullJson(bool closures = true)
    {
        Str s; Check(_lib.FullJson(_session, closures ? 1u : 0u, (IntPtr)(&s)), "full_json"); return TakeStr(&s) ?? "[]";
    }

    public IReadOnlyList<string> Roots()
    {
        Handles h; Check(_lib.SessionRoots(_session, (IntPtr)(&h)), "roots"); return TakeHandles(&h);
    }

    // -- errors and result unpacking ----------------------------------------------------------

    private string LastError() { Str s; _lib.LastError(_session, (IntPtr)(&s)); return TakeStr(&s) ?? ""; }

    /// <summary>True when a value is present; false for NOT_APPLICABLE; throws otherwise.</summary>
    private bool Check(int st, string where)
    {
        if (st == Ok) return true;
        if (st == NotApplicable) return false;
        var msg = LastError();
        if (st == NotImplemented) throw new NotImplementedInToolkitException(msg.Length == 0 ? $"{where}: not implemented by the SysML Toolkit" : msg);
        if (st == InvalidHandle) throw new GoneException(msg.Length == 0 ? $"{where}: invalid handle" : msg);
        if (st == Unresolved) throw new UnresolvedReferenceException(msg.Length == 0 ? $"{where}: a reference did not resolve" : msg);
        throw new SdkException($"{where}: status {st}: {msg}");
    }

    private string? TakeStr(Str* s)
    {
        var v = s->Present != 0 && s->Ptr != IntPtr.Zero ? Encoding.UTF8.GetString((byte*)s->Ptr, (int)s->Len) : null;
        _lib.Free(s->Owner);
        return v;
    }

    private List<string> TakeHandles(Handles* h)
    {
        var outp = new List<string>((int)h->Len);
        var items = (ulong*)h->Items;
        for (nuint i = 0; i < h->Len; i++) outp.Add(items[i].ToString());
        _lib.Free(h->Owner);
        return outp;
    }

    private static JsonElement ToJson(object? value) => JsonSerializer.SerializeToElement(value);

    // Room for the largest result struct (Str: 32 bytes on 64-bit targets).
    private const int OutSize = 32;

    private JsonElement? Call(string symbol, char code, ulong handle, string where)
    {
        var o = stackalloc byte[OutSize];
        return Unpack(code, o, _lib.Member(symbol)(_session, handle, (IntPtr)o), where);
    }

    /// <summary>The result in <paramref name="o"/> as the backend contract carries it, after
    /// checking the status.</summary>
    private JsonElement? Unpack(char code, byte* o, int st, string where)
    {
        if (!Check(st, where)) return code is 'H' or 'T' ? ToJson(Array.Empty<object>()) : null;
        switch (code)
        {
            case 'H': return ToJson(TakeHandles((Handles*)o).Select(h => new Dictionary<string, string> { ["@id"] = h }).ToList());
            case 'R': { var r = (Ref*)o; return r->Present == 0 ? null : ToJson(new Dictionary<string, string> { ["@id"] = r->Value.ToString() }); }
            case 'S': { var s = TakeStr((Str*)o); return s is null ? null : ToJson(s); }
            case 'T':
            {
                // A list of strings (a requirement's `text`), as JSON text.
                var s = TakeStr((Str*)o);
                if (s is null) return ToJson(Array.Empty<object>());
                using var doc = JsonDocument.Parse(s);
                return doc.RootElement.Clone();
            }
            case 'B': { var b = (Bool*)o; return b->Present == 0 ? null : ToJson(b->Value != 0); }
            case 'I': { var i = (Int*)o; return i->Present == 0 ? null : ToJson(i->Value); }
            default:  { var d = (Real*)o; return d->Present == 0 ? null : ToJson(d->Value); }
        }
    }

    // -- the backend contract -----------------------------------------------------------------

    private (string Symbol, char Code, string Params)? Declaring(string metaclass, string prop)
    {
        var key = metaclass + "::" + prop;
        if (_declaring.TryGetValue(key, out var cached)) return cached;
        (string Symbol, char Code, string Params)? found = null;
        var stack = new Stack<string>(); var seen = new HashSet<string>();
        stack.Push(metaclass);
        while (stack.Count > 0)
        {
            var c = stack.Pop();
            if (!seen.Add(c)) continue;
            if (ToolkitTable.Funcs.TryGetValue(c + "::" + prop, out var e)) { found = e; break; }
            if (ToolkitTable.Bases.TryGetValue(c, out var bases)) foreach (var b in bases) stack.Push(b);
        }
        _declaring[key] = found;
        return found;
    }

    public JsonElement? Get(string handle, string prop)
    {
        var mc = Metaclass(handle);
        var e = Declaring(mc, prop);
        if (e is null) return null;  // not a member of this metaclass: absent, as the payload backend answers
        return Call(e.Value.Symbol, e.Value.Code, ulong.Parse(handle), $"{mc}.{prop}");
    }

    /// <summary>A specification operation. Each argument is passed as the binding library's
    /// parameter C type says: an element as its handle, a list of elements as a handle array, a
    /// Boolean as a C bool, anything else as text. The library answers or refuses.</summary>
    public JsonElement? Call(string handle, string op, IReadOnlyList<object?> args)
    {
        var mc = Metaclass(handle);
        var where = $"{mc}.{op}()";
        var e = Declaring(mc, op + "()") ?? throw new SdkException($"{where}: the binding library has no such operation");
        var types = e.Params.Length == 0 ? Array.Empty<string>() : e.Params.Split(';');
        var pins = new List<GCHandle>();
        try
        {
            // One machine word per argument: the address of a Str or Handles struct, or the value
            // of a Handle or bool. The structs live in pinned arrays until the call returns.
            var words = new ulong[types.Length];
            var strs = new Str[types.Length];
            var lists = new Handles[types.Length];
            var strBase = Pin(strs, pins);
            var listBase = Pin(lists, pins);
            var sig = new StringBuilder();
            for (var i = 0; i < types.Length; i++)
            {
                var a = i < args.Count ? args[i] : null;
                switch (types[i])
                {
                    case "Handle":
                        // The binding library has no "no element": refuse it rather than pass a
                        // handle that names some other element.
                        words[i] = a is IElement el ? ulong.Parse(el.Handle)
                            : throw new ArgumentException($"{where}: argument {i + 1} must be an element, not {a?.ToString() ?? "null"}");
                        sig.Append('U'); break;
                    case "bool":
                        words[i] = a is true ? 1UL : 0UL; sig.Append('B'); break;
                    case "Handles":
                    {
                        var n = i + 1;
                        var items = (a as System.Collections.IEnumerable)?.Cast<object?>()
                            .Select(x => x is IElement el ? ulong.Parse(el.Handle)
                                : throw new ArgumentException($"{where}: argument {n} must hold elements only, not {x?.ToString() ?? "null"}"))
                            .ToArray() ?? Array.Empty<ulong>();
                        lists[i] = new Handles { Items = items.Length == 0 ? IntPtr.Zero : Pin(items, pins), Len = (nuint)items.Length, Owner = IntPtr.Zero };
                        words[i] = (ulong)(listBase + i * sizeof(Handles)); sig.Append('P'); break;
                    }
                    default:
                        strs[i] = MakeStr(a switch { null => "", string s => s, IElement named => named.ElementId, _ => a.ToString() ?? "" }, pins);
                        words[i] = (ulong)(strBase + i * sizeof(Str)); sig.Append('P'); break;
                }
            }
            var fp = _lib.Symbol(e.Symbol);
            var o = stackalloc byte[OutSize];
            var h = ulong.Parse(handle);
            var outp = (IntPtr)o;
            int st = sig.ToString() switch
            {
                "" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, int>)fp)(_session, h, outp),
                "P" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, IntPtr, int>)fp)(_session, h, (IntPtr)words[0], outp),
                "PP" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, IntPtr, IntPtr, int>)fp)(_session, h, (IntPtr)words[0], (IntPtr)words[1], outp),
                "PPP" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, IntPtr, IntPtr, IntPtr, int>)fp)(_session, h, (IntPtr)words[0], (IntPtr)words[1], (IntPtr)words[2], outp),
                "U" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, ulong, IntPtr, int>)fp)(_session, h, words[0], outp),
                "B" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, byte, IntPtr, int>)fp)(_session, h, (byte)words[0], outp),
                "PPB" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, IntPtr, byte, IntPtr, int>)fp)(_session, h, (IntPtr)words[0], (IntPtr)words[1], (byte)words[2], outp),
                "PBB" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, IntPtr, byte, byte, IntPtr, int>)fp)(_session, h, (IntPtr)words[0], (byte)words[1], (byte)words[2], outp),
                "UU" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, ulong, ulong, IntPtr, int>)fp)(_session, h, words[0], words[1], outp),
                "UP" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, ulong, IntPtr, IntPtr, int>)fp)(_session, h, words[0], (IntPtr)words[1], outp),
                "UPP" => ((delegate* unmanaged[Cdecl]<IntPtr, ulong, ulong, IntPtr, IntPtr, IntPtr, int>)fp)(_session, h, words[0], (IntPtr)words[1], (IntPtr)words[2], outp),
                var other => throw new SdkException($"{where}: no call shape for parameters '{other}'"),
            };
            return Unpack(e.Code, o, st, where);
        }
        finally { foreach (var p in pins) p.Free(); }
    }

    public string Metaclass(string handle)
    {
        if (_metaclass.TryGetValue(handle, out var mc)) return mc;
        Str s; Check(_lib.Metaclass(_session, ulong.Parse(handle), (IntPtr)(&s)), $"metaclass({handle})");
        mc = TakeStr(&s) ?? "";
        _metaclass[handle] = mc;
        return mc;
    }

    public string ElementId(string handle) => Call("sysmlv2_element_element_id", 'S', ulong.Parse(handle), "elementId")?.GetString() ?? "";

    public string? Resolve(string qualifiedName)
    {
        var pins = new List<GCHandle>();
        try
        {
            var arg = MakeStr(qualifiedName, pins);
            Ref o;
            if (!Check(_lib.SessionResolve(_session, (IntPtr)(&arg), (IntPtr)(&o)), $"resolve({qualifiedName})")) return null;
            return o.Present != 0 ? o.Value.ToString() : null;
        }
        finally { foreach (var h in pins) h.Free(); }
    }

    public IEnumerable<string> AllHandles()
    {
        Handles h; Check(_lib.UserElements(_session, (IntPtr)(&h)), "user_elements"); return TakeHandles(&h);
    }
}
