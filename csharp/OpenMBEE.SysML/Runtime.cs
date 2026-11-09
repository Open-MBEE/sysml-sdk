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

/// <summary>The answer of a name-keyed convenience member (<c>FindPartDefsByName</c>): names to
/// elements, read-only, enumerated in membership order.</summary>
public sealed class NameMap<T> : IReadOnlyDictionary<string, T>
{
    private readonly List<KeyValuePair<string, T>> entries = new();
    private readonly Dictionary<string, T> index = new();

    internal void Add(string key, T value)
    {
        index.Add(key, value);
        entries.Add(new KeyValuePair<string, T>(key, value));
    }

    public T this[string key] => index[key];
    public IEnumerable<string> Keys => entries.Select(e => e.Key);
    public IEnumerable<T> Values => entries.Select(e => e.Value);
    public int Count => entries.Count;
    public bool ContainsKey(string key) => index.ContainsKey(key);

    public bool TryGetValue(string key, [System.Diagnostics.CodeAnalysis.MaybeNullWhen(false)] out T value) =>
        index.TryGetValue(key, out value);

    public IEnumerator<KeyValuePair<string, T>> GetEnumerator() => entries.GetEnumerator();
    System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() => GetEnumerator();
}

/// <summary>A property the backend reports it does not compute. Reserved
/// for a backend that says so itself; never inferred from an empty
/// result.</summary>
public sealed class NotComputedException : SdkException
{
    public NotComputedException(string message) : base(message) { }
}

/// <summary>A specification member the backend does not answer for this
/// element: the SysML Toolkit's refusals
/// (<see cref="NotImplementedInToolkitException"/>), and an engine's.</summary>
public class NotAnsweredException : SdkException
{
    public NotAnsweredException(string message) : base(message) { }
}

/// <summary>A specification member the SysML Toolkit does not answer:
/// thrown when the SysML Toolkit reports the member as not
/// implemented.</summary>
/// <summary>A convenience member that gives no answer for this element by its contract
/// (<c>invalid</c> in its OCL): the literal of an expression that is not a literal, a bound that is
/// an expression. Unlike the other <see cref="NotAnsweredException"/>s, it says what the answer is,
/// not that the backend cannot give it.</summary>
public sealed class RefusedException : NotAnsweredException
{
    public RefusedException(string message) : base(message) { }
}

/// <summary>Which members a convenience list reads: the element's own, those it inherits (for a
/// namespace's members, imported ones too), or both. A list of features gives its own first; a list
/// of members or definitions, outside the owned scope, is in the backend's order.</summary>
public enum Scope { Owned, Inherited, All }

/// <summary>Which visibilities a convenience list keeps: every one, public and protected, or public
/// only.</summary>
public enum VisibilityFilter { Any, Protected, Public }

/// <summary>A convenience member as the generated interfaces call it (tools/naming.py,
/// convenience_runtime): its name, its binding library symbol suffix, its parameters' names and
/// kinds (scope, visibility, bool, str, element), and its result (elements, element, bool, int,
/// unlimited, real, strings, literal). The int, unlimited and literal results answer a
/// <see cref="Literal"/>.</summary>
public sealed record Find(string Name, string Symbol, string[] ParamNames, string[] ParamKinds, string Result);

/// <summary>A literal's value, as <c>FindLiteral()</c> answers it (and the bounds,
/// <c>FindLowerBound()</c> and <c>FindUpperBound()</c>): an integer, a real, a Boolean, a string, or the
/// unbounded <c>*</c>. Switch over its cases, or ask for the kind you expect:
/// <c>expr.FindLiteral().AsLong()</c>, <c>expr.FindLiteral&lt;long&gt;()</c>, or a switch over
/// <c>Literal.IntegerValue(var n)</c>, <c>Literal.Unbounded</c> and the other cases.</summary>
public abstract record Literal
{
    private Literal() { }

    /// <summary>An integer.</summary>
    public sealed record IntegerValue(long Value) : Literal;
    /// <summary>A real (a rational or a real literal).</summary>
    public sealed record RealValue(double Value) : Literal;
    /// <summary>A Boolean.</summary>
    public sealed record BooleanValue(bool Value) : Literal;
    /// <summary>A string.</summary>
    public sealed record StringValue(string Value) : Literal;
    /// <summary>The unbounded <c>*</c>.</summary>
    public sealed record Unbounded() : Literal;

    /// <summary>Whether this is an integer.</summary>
    public bool IsInteger => this is IntegerValue;
    /// <summary>Whether this is a real.</summary>
    public bool IsReal => this is RealValue;
    /// <summary>Whether this is a Boolean.</summary>
    public bool IsBoolean => this is BooleanValue;
    /// <summary>Whether this is a string.</summary>
    public bool IsString => this is StringValue;
    /// <summary>Whether this is the unbounded <c>*</c>.</summary>
    public bool IsUnbounded => this is Unbounded;

    /// <summary>The integer; an <see cref="InvalidOperationException"/> for any other kind.</summary>
    public long AsLong() => this is IntegerValue(var v) ? v : throw KindError("an integer");

    /// <summary>The number, an integer widened; an <see cref="InvalidOperationException"/> for any
    /// other kind.</summary>
    public double AsDouble() => this switch
    {
        RealValue(var v) => v,
        IntegerValue(var v) => v,
        _ => throw KindError("a number"),
    };

    /// <summary>The Boolean; an <see cref="InvalidOperationException"/> for any other kind.</summary>
    public bool AsBoolean() => this is BooleanValue(var v) ? v : throw KindError("a Boolean");

    /// <summary>The string; an <see cref="InvalidOperationException"/> for any other kind.</summary>
    public string AsString() => this is StringValue(var v) ? v : throw KindError("a string");

    /// <summary>The value as <typeparamref name="T"/>: long, int (an <see cref="OverflowException"/>
    /// where it does not fit), double (an integer widened), bool, string, or a case of this record; an
    /// <see cref="InvalidOperationException"/> where the literal is of another kind.</summary>
    public T As<T>()
    {
        object v = typeof(T) == typeof(long) ? AsLong()
            : typeof(T) == typeof(int) ? checked((int)AsLong())
            : typeof(T) == typeof(double) ? AsDouble()
            : typeof(T) == typeof(bool) ? AsBoolean()
            : typeof(T) == typeof(string) ? AsString()
            : this is T ? this
            : throw KindError("a " + typeof(T).Name);
        return (T)v;
    }

    private InvalidOperationException KindError(string expected) => new($"the literal is {this}, not {expected}");
}

public sealed class NotImplementedInToolkitException : NotAnsweredException
{
    public NotImplementedInToolkitException(string message) : base(message) { }
}

/// <summary>The engine behind a backend could not answer: it failed while computing a value,
/// or it is not available (not installed, not downloadable, or no suitable runtime). The
/// message says which engine and why.</summary>
public class BackendException : SdkException
{
    public BackendException(string message) : base(message) { }
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

    /// <summary>A convenience member on the element <paramref name="handle"/>, with its arguments
    /// in order, defaults filled in. A backend that cannot answer it throws
    /// <see cref="NotAnsweredException"/>; where its contract gives no answer,
    /// <see cref="RefusedException"/>.</summary>
    JsonElement? Find(string handle, Find member, IReadOnlyList<object?> args) =>
        throw new NotAnsweredException($"{member.Name}: this backend does not answer the convenience members");

    /// <summary><see cref="Find"/> on each element, a result per handle in order, for the convenience
    /// members over lists (<see cref="Model.FindAll{T}"/>). By default one <c>Find</c> per element; a
    /// backend that can answer them together (in one request) overrides it.</summary>
    IReadOnlyList<JsonElement?> FindEach(IReadOnlyList<string> handles, Find member, IReadOnlyList<object?> args)
    {
        var outp = new List<JsonElement?>(handles.Count);
        foreach (var h in handles)
            outp.Add(Find(h, member, args));
        return outp;
    }
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

    /// <summary>Nor a convenience member.</summary>
    public JsonElement? Find(string handle, Find member, IReadOnlyList<object?> args) =>
        throw new NotAnsweredException($"{member.Name}: the convenience members are not available on payload "
            + "models; load the model through the SysML Toolkit or the Pilot Implementation");
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

    /// <summary>Name resolution, with visibility (KerML 8.2.3.5), from the root: the backend's
    /// resolution. A payload model resolves its elements' own qualified names; the SysML Toolkit's lookup
    /// does not consider visibility yet. Only names the model gives count: an unnamed element is not
    /// found under the name of what it references. To get an element by its qualified name whatever its
    /// visibility, use <see cref="Find(string, bool)"/>.</summary>
    public IElement? Resolve(string qualifiedName)
    {
        var handle = Backend.Resolve(qualifiedName);
        return handle is null ? null : Element(handle);
    }

    /// <summary>Name resolution, with visibility, from a namespace: the specification's
    /// <c>Namespace::resolve</c> on <paramref name="context"/> (its member element), as the backend
    /// answers it.</summary>
    public IElement? Resolve(string name, Namespace context) => context.resolve(name)?.memberElement;

    /// <summary>The element a qualified name names, whatever its visibility, the same on every backend:
    /// the first segment is a top-level name; each next one a member of the namespace before it, through
    /// one of its owned memberships (an alias's name included) or, with <paramref name="inherited"/>, also
    /// through its inherited memberships (its supertypes' non-private ones). Null if a segment names
    /// nothing. Segments may be quoted, with KerML's escapes (<c>'~Plug'</c>).</summary>
    public IElement? Find(string qualifiedName, bool inherited = false)
    {
        var handle = Backend.Resolve(qualifiedName);
        if (handle is not null)
        {
            var el = Element(handle);
            if (el.qualifiedName == qualifiedName)
                return el;
        }
        var segments = SplitQualifiedName(qualifiedName);
        if (segments.Count == 0)
            return null;
        handle = Backend.Resolve(segments[0].Written);
        var current = handle is not null ? Element(handle) : null;
        if (current is null)
            foreach (var root in Roots())
            {
                current = MemberNamed(((Namespace)root).ownedMembership, segments[0].Name);
                if (current is not null)
                    break;
            }
        foreach (var (_, name) in segments.Skip(1))
        {
            if (current is not Namespace ns)
                return null;
            var found = MemberNamed(ns.ownedMembership, name);
            if (found is null && inherited && current is Type t)
                found = MemberNamed(t.inheritedMembership, name);
            current = found;
        }
        return current;
    }

    private static IElement? MemberNamed(IEnumerable<Membership> memberships, string name)
    {
        foreach (var m in memberships)
            if (m.memberName == name || m.memberShortName == name)
                return m.memberElement;
        return null;
    }

    private static readonly Dictionary<char, char> Escapes = new()
    {
        ['\''] = '\'', ['"'] = '"', ['b'] = '\b', ['f'] = '\f', ['t'] = '\t', ['n'] = '\n', ['\\'] = '\\',
    };

    /// <summary>A qualified name's segments, as written and as names (KerML 8.2.3.4.1): separated by
    /// <c>::</c>; an unrestricted name in single quotes, its escape sequences resolved (KerML 8.2.2.3); a
    /// leading global scope qualifier <c>$</c> dropped.</summary>
    public static IReadOnlyList<(string Written, string Name)> SplitQualifiedName(string qualifiedName)
    {
        var qn = qualifiedName;
        var outp = new List<(string, string)>();
        var i = 0;
        while (i < qn.Length)
        {
            if (qn[i] == '\'')
            {
                var j = i + 1;
                var name = new System.Text.StringBuilder();
                while (j < qn.Length && qn[j] != '\'')
                {
                    if (qn[j] == '\\' && j + 1 < qn.Length)
                    {
                        name.Append(Escapes.TryGetValue(qn[j + 1], out var c) ? c : qn[j + 1]);
                        j += 2;
                    }
                    else
                    {
                        name.Append(qn[j++]);
                    }
                }
                if (j >= qn.Length)
                    throw new SdkException($"an unterminated quoted name in {qualifiedName}");
                outp.Add((qn[i..(j + 1)], name.ToString()));
                i = j + 1;
            }
            else
            {
                var j = qn.IndexOf("::", i, StringComparison.Ordinal);
                if (j < 0)
                    j = qn.Length;
                outp.Add((qn[i..j], qn[i..j]));
                i = j;
            }
            if (i < qn.Length)
            {
                if (string.CompareOrdinal(qn, i, "::", 0, 2) != 0)
                    throw new SdkException($"a name in {qualifiedName} is not followed by '::'");
                i += 2;
            }
        }
        if (outp.Count > 0 && outp[0].Item2 == "$")
            outp.RemoveAt(0);
        return outp;
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

    /// <summary>Every convenience member routes through here: the backend answers or refuses, and
    /// the result is wrapped and converted to its C# shape (a literal, and the bounds' numbers, a
    /// <see cref="Literal"/>).</summary>
    public object? FindMember(IElement el, Find member, params object?[] args) =>
        ConvertFind(el, member, Backend.Find(el.Handle, member, args));

    /// <summary>A convenience member over a list of elements (the extension methods of
    /// <see cref="ElementLists"/>): list results concatenated in list order, single results with the
    /// absent ones dropped; one backend call per model. If the member refuses for one element, the call
    /// refuses.</summary>
    public static IReadOnlyList<T> FindAll<T>(IEnumerable<IElement> elements, Find member, params object?[] args)
    {
        var list = elements.ToList();
        var outp = new List<T>();
        if (list.Count == 0)
            return outp;
        var model = list[0].Model;
        IReadOnlyList<JsonElement?> raws;
        if (list.All(el => ReferenceEquals(el.Model, model)))
            raws = model.Backend.FindEach(list.Select(el => el.Handle).ToList(), member, args);
        else
            raws = list.Select(el => el.Model.Backend.Find(el.Handle, member, args)).ToList();
        for (var i = 0; i < list.Count; i++)
        {
            var v = list[i].Model.ConvertFind(list[i], member, raws[i]);
            if (member.Result == "element")
            {
                if (v is not null)
                    outp.Add((T)v);
            }
            else
            {
                foreach (var x in (List<object?>)v!)
                    outp.Add((T)x!);
            }
        }
        return outp;
    }

    /// <summary>A convenience member's answer from the backend's, in its C# shape.</summary>
    internal object? ConvertFind(IElement el, Find member, JsonElement? raw)
    {
        var at = $"{el.MetaclassName}.{member.Name}() (element {el.Handle})";
        var v = Wrap(raw, at);
        return member.Result switch
        {
            "elements" or "strings" => v ?? new List<object?>(),
            "unlimited" => v is null ? null
                : v is "*" || (v is long l && l == long.MaxValue) ? new Literal.Unbounded()
                : new Literal.IntegerValue(Convert.ToInt64(v)),
            "int" => v is null ? null : new Literal.IntegerValue(Convert.ToInt64(v)),
            "literal" => DecodeLiteral(v),
            "real" => v is null ? null : Convert.ToDouble(v),
            "map" => v ?? new List<object?>(),
            _ => v,
        };
    }

    /// <summary>A literal's value from its tagged JSON ({"integer": 4}, {"real": 0.5},
    /// {"boolean": true}, {"string": "a"}, {"unbounded": true}).</summary>
    public static Literal DecodeLiteral(object? raw)
    {
        JsonElement v = raw switch
        {
            string s => JsonDocument.Parse(s).RootElement.Clone(),
            JsonElement e => e,
            _ => throw new SdkException($"a literal value was expected, not {raw}"),
        };
        if (v.ValueKind == JsonValueKind.Object)
        {
            foreach (var p in v.EnumerateObject())
            {
                switch (p.Name)
                {
                    case "unbounded": return new Literal.Unbounded();
                    case "integer": return new Literal.IntegerValue(p.Value.GetInt64());
                    case "real": return new Literal.RealValue(p.Value.GetDouble());
                    case "boolean": return new Literal.BooleanValue(p.Value.GetBoolean());
                    case "string": return new Literal.StringValue(p.Value.GetString()!);
                }
            }
        }
        throw new SdkException($"a literal value was expected, not {raw}");
    }

    // --- typed converters (generated members call these on Read and Call results) ----

    public static bool AsFlag(object? v) => v is true;

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

    /// <summary>A name-keyed map from a convenience member's pairs of a name and an element, in
    /// their order.</summary>
    public static IReadOnlyDictionary<string, T> AsMap<T>(object? v) where T : class, IElement
    {
        var map = new NameMap<T>();
        if (v is List<object?> pairs)
            foreach (var pair in pairs)
            {
                var p = (List<object?>)pair!;
                map.Add((string)p[0]!, (T)p[1]!);
            }
        return map;
    }

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
                return v.TryGetInt64(out var l) ? (object)l : v.GetDouble(); // boxed apart: a long, not a double
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
