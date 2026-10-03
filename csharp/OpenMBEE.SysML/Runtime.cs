// SDK runtime (C# backend): Model, PayloadBackend, element core.
// Mirrors python/sysml/base.py — same read semantics, same identity
// rules. A property read returns whatever the backend gave: the
// SysML Toolkit reports its own unimplemented members, so the SDK
// never second-guesses a result.
//
// Collision rule: runtime members are PascalCase
// (ElementId, MetaclassName, GetRaw, Model); spec members are generated
// lowerCamelCase default interface members, so the first letter keeps
// the two namespaces disjoint. tools/gen_classes_cs.py enforces it.

#nullable enable

using System.Text.Json;

namespace OpenMBEE.SysML;

public class SdkException : Exception
{
    public SdkException(string message) : base(message) { }
}

/// <summary>A property the backend reports it does not compute. Reserved
/// for a backend that says so itself; never inferred from an empty
/// result.</summary>
public sealed class NotComputedException : SdkException
{
    public NotComputedException(string message) : base(message) { }
}

/// <summary>A specification member the SysML Toolkit does not answer:
/// thrown when the SysML Toolkit reports the member as not
/// implemented.</summary>
public sealed class NotImplementedInToolkitException : SdkException
{
    public NotImplementedInToolkitException(string message) : base(message) { }
}

/// <summary>The element no longer exists in the backend's current state.</summary>
public sealed class GoneException : SdkException
{
    public GoneException(string message) : base(message) { }
}

/// <summary>A reference that did not resolve — a SysML Toolkit @ref
/// spelling passthrough (non-standard, best-effort) or a dangling @id.
/// Typed navigation throws: the SDK requires resolved models. GetRaw()
/// still exposes the raw value.</summary>
public sealed class UnresolvedReferenceException : SdkException
{
    public UnresolvedReferenceException(string message) : base(message) { }
}

/// <summary>The backend contract: everything the generated surface
/// needs. The protocol is defined over opaque per-backend element
/// <em>handles</em>; an id-keyed backend (payload, REST) uses element ids
/// as its handles, which is why this payload-backed runtime types them as
/// string. <see cref="ElementId"/> maps a handle to the stable id.</summary>
public interface IBackend
{
    JsonElement? Get(string handle, string prop);
    string Metaclass(string handle);
    string ElementId(string handle);
    string? Resolve(string qualifiedName);
    IEnumerable<string> AllHandles();

    /// <summary>A specification operation on the element <paramref name="handle"/>, by its
    /// specification name, with its arguments in order (elements as wrappers, collections as
    /// lists). A backend that cannot evaluate the operation throws
    /// <see cref="NotImplementedInToolkitException"/>.</summary>
    JsonElement? Call(string handle, string op, IReadOnlyList<object?> args);
}

/// <summary>A standard library read from a full-form interchange element array, such as the
/// library JSON published with the SDK. Index it once and pass it to any number of payload models:
/// their references into the library then resolve by the library's normative ids.</summary>
public sealed class PayloadLibrary
{
    internal readonly Dictionary<string, JsonElement> ById = new();
    internal readonly Dictionary<string, string> ByQname = new();

    public PayloadLibrary(JsonElement elements)
    {
        PayloadBackend.Index(elements, ById, new List<string>(), ByQname);
    }

    public static PayloadLibrary FromJson(string json)
    {
        using var doc = JsonDocument.Parse(json);
        return new PayloadLibrary(doc.RootElement.Clone());
    }

    /// <summary>The number of library elements.</summary>
    public int Count => ById.Count;
}

/// <summary>Read-only backend over a full-form interchange element array. Handles are the
/// element ids, listed in document order; an id that occurs twice keeps its first position and
/// its last element. With a library, an id or qualified name the payload does not hold is looked
/// up in the library; the library's elements are not the model's, so <see cref="AllHandles"/>
/// lists the payload's only.</summary>
public sealed class PayloadBackend : IBackend
{
    private readonly Dictionary<string, JsonElement> _byId = new();
    private readonly List<string> _order = new();
    private readonly Dictionary<string, string> _byQname = new();
    private readonly PayloadLibrary? _library;

    public PayloadBackend(JsonElement elements, PayloadLibrary? library = null)
    {
        Index(elements, _byId, _order, _byQname);
        _library = library;
    }

    /// <summary>Elements by id (document order, the last of a repeated id) and ids by qualified
    /// name (the first).</summary>
    internal static void Index(JsonElement elements, Dictionary<string, JsonElement> byId, List<string> order,
        Dictionary<string, string> byQname)
    {
        foreach (var e in elements.EnumerateArray())
        {
            if (!e.TryGetProperty("@id", out var id) ||
                id.ValueKind != JsonValueKind.String)
                continue;
            var eid = id.GetString()!;
            if (!byId.ContainsKey(eid))
                order.Add(eid);
            byId[eid] = e;
            if (e.TryGetProperty("qualifiedName", out var qn) &&
                qn.ValueKind == JsonValueKind.String && qn.GetString()!.Length > 0)
                byQname.TryAdd(qn.GetString()!, eid);
        }
    }

    private JsonElement Require(string handle)
    {
        if (_byId.TryGetValue(handle, out var el))
            return el;
        if (_library != null && _library.ById.TryGetValue(handle, out el))
            return el;
        throw new GoneException($"no element {handle} in this payload");
    }

    public JsonElement? Get(string handle, string prop) =>
        Require(handle).TryGetProperty(prop, out var v) ? v : null;

    public string Metaclass(string handle) =>
        Require(handle).GetProperty("@type").GetString()!;

    public string ElementId(string handle)
    {
        Require(handle);
        return handle;
    }

    public string? Resolve(string qualifiedName) =>
        _byQname.TryGetValue(qualifiedName, out var id) ? id
        : _library != null && _library.ByQname.TryGetValue(qualifiedName, out id) ? id
        : null;

    public IEnumerable<string> AllHandles() => _order;

    /// <summary>A payload holds property values only; there is nothing to evaluate an operation with.</summary>
    public JsonElement? Call(string handle, string op, IReadOnlyList<object?> args) =>
        throw new NotImplementedInToolkitException(
            $"{op}(): operations are not available on payload models; load the model through the SysML Toolkit backend");
}

/// <summary>Runtime core of every element. Spec properties live on the
/// generated interfaces as default members; classes stay empty shells. The
/// members of the metaclass Element itself, which every element has, are the
/// generated part of this interface (Classes.g.cs).</summary>
public partial interface IElement
{
    /// <summary>The element's stable id, via the backend's ElementId(handle).</summary>
    string ElementId { get; }

    /// <summary>The backend handle this wrapper holds (= id for payload backends).</summary>
    string Handle { get; }

    string MetaclassName { get; }
    Model Model { get; }

    /// <summary>Escape hatch: raw backend value, no wrapping.</summary>
    JsonElement? GetRaw(string prop);
}

/// <summary>Base of every generated element class. A wrapper holds
/// (model, handle); identity is by the backend's stable element id
/// </summary>
public abstract class El : IElement
{
    private readonly Model _model;
    private readonly string _handle;

    protected El(Model model, string handle)
    {
        _model = model;
        _handle = handle;
    }

    public string ElementId => _model.Backend.ElementId(_handle);
    public string Handle => _handle;
    public string MetaclassName => _model.Backend.Metaclass(_handle);
    public Model Model => _model;
    public JsonElement? GetRaw(string prop) => _model.Backend.Get(_handle, prop);

    public override bool Equals(object? obj) =>
        obj is El other && ReferenceEquals(other._model, _model)
            && other.ElementId == ElementId;

    public override int GetHashCode() => ElementId.GetHashCode();

    public override string ToString()
    {
        var qn = _model.Backend.Get(_handle, "qualifiedName");
        var label = qn is { ValueKind: JsonValueKind.String } q
            ? q.GetString()
            : ElementId;
        return $"<{MetaclassName} {label}>";
    }
}

/// <summary>Fallback for metaclasses missing from the generated registry.</summary>
internal sealed class UnknownEl : El
{
    internal UnknownEl(Model model, string id) : base(model, id) { }
}

/// <summary>Entry point: wraps a backend and mints typed wrappers by metaclass.</summary>
public sealed class Model
{
    private readonly Dictionary<string, IElement> _cache = new();

    public IBackend Backend { get; }

    public Model(IBackend backend)
    {
        Backend = backend;
    }

    /// <summary>A model read from a full-form interchange element array. With
    /// <paramref name="library"/>, references into the standard library resolve against it; its
    /// elements are not the model's.</summary>
    public static Model FromFullJson(string json, PayloadLibrary? library = null)
    {
        using var doc = JsonDocument.Parse(json);
        return new Model(new PayloadBackend(doc.RootElement.Clone(), library));
    }

    public IElement Element(string handle)
    {
        if (_cache.TryGetValue(handle, out var w))
            return w;
        var mc = Backend.Metaclass(handle); // throws Gone if absent
        w = ClassMap.Factories.TryGetValue(mc, out var make)
            ? make(this, handle)
            : new UnknownEl(this, handle);
        _cache[handle] = w;
        return w;
    }

    public IElement? Resolve(string qualifiedName)
    {
        var handle = Backend.Resolve(qualifiedName);
        return handle is null ? null : Element(handle);
    }

    public IEnumerable<IElement> All()
    {
        foreach (var handle in Backend.AllHandles())
            yield return Element(handle);
    }

    public IEnumerable<T> ElementsOfType<T>() where T : class, IElement
    {
        foreach (var el in All())
            if (el is T t)
                yield return t;
    }

    /// <summary>The top-level namespaces: the elements without an owner that are namespaces (a
    /// membership may have no owner either).</summary>
    public IEnumerable<IElement> Roots()
    {
        foreach (var el in All())
            if (el is Namespace && el.owner is null)
                yield return el;
    }

    /// <summary>Every generated property member routes through here:
    /// the backend value, wrapped. Mirrors P.__get__ in base.py.</summary>
    public object? Read(IElement el, string prop)
    {
        var raw = Backend.Get(el.Handle, prop);
        return Wrap(raw, $"{el.MetaclassName}.{prop} (element {el.Handle})");
    }

    /// <summary>Every generated operation routes through here: the backend evaluates it (the
    /// SysML Toolkit answers or refuses; a payload refuses), and the result is wrapped like a
    /// property value.</summary>
    public object? Call(IElement el, string op, object?[] args)
    {
        var raw = Backend.Call(el.Handle, op, args);
        return Wrap(raw, $"{el.MetaclassName}.{op}() (element {el.Handle})");
    }

    // --- typed converters (generated members call these on Read and Call results) ----

    public static IReadOnlyList<T> AsList<T>(object? v)
    {
        if (v is null)
            return Array.Empty<T>(); // absent arrays normalize to empty
        var src = (List<object?>)v;
        var outp = new List<T>(src.Count);
        foreach (var item in src)
            outp.Add((T)item!);
        return outp;
    }

    public static T? AsRef<T>(object? v) where T : class, IElement => (T?)v;

    public static bool? AsBool(object? v) => (bool?)v;

    public static string? AsString(object? v) => (string?)v;

    public static long? AsInt(object? v) => v is null ? null : Convert.ToInt64(v);

    public static double? AsReal(object? v) => v is null ? null : Convert.ToDouble(v);

    private static string? IdRef(JsonElement v)
    {
        if (v.ValueKind != JsonValueKind.Object)
            return null;
        string? id = null;
        var count = 0;
        foreach (var p in v.EnumerateObject())
        {
            count++;
            if (p.Name == "@id" && p.Value.ValueKind == JsonValueKind.String)
                id = p.Value.GetString();
        }
        return count == 1 ? id : null;
    }

    public object? Wrap(JsonElement? raw, string at = "?")
    {
        if (raw is not { } v)
            return null;
        switch (v.ValueKind)
        {
            case JsonValueKind.Null:
            case JsonValueKind.Undefined:
                return null;
            case JsonValueKind.String:
                return v.GetString();
            case JsonValueKind.True:
                return true;
            case JsonValueKind.False:
                return false;
            case JsonValueKind.Number:
                return v.TryGetInt64(out var l) ? l : v.GetDouble();
            case JsonValueKind.Array:
            {
                var list = new List<object?>(v.GetArrayLength());
                foreach (var item in v.EnumerateArray())
                    list.Add(Wrap(item, at));
                return list;
            }
            case JsonValueKind.Object:
            {
                if (IdRef(v) is { } id)
                {
                    try
                    {
                        return Element(id);
                    }
                    catch (GoneException)
                    {
                        throw new UnresolvedReferenceException(
                            $"dangling reference @id='{id}' at {at}: target is "
                            + "not in this model; the SDK requires resolved models");
                    }
                }
                if (v.TryGetProperty("@ref", out var r) &&
                    r.ValueKind == JsonValueKind.String)
                    throw new UnresolvedReferenceException(
                        $"unresolved reference '{r.GetString()}' at {at} (the "
                        + "SysML Toolkit's @ref passthrough is non-standard best-effort); "
                        + "the SDK requires resolved models — use GetRaw() for "
                        + "the raw value");
                return v;
            }
            default:
                return v;
        }
    }
}
