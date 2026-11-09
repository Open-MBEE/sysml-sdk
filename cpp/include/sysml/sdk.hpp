// SDK runtime (C++ backend): Model, PayloadBackend, Element, Value.
// Mirrors python/sysml/base.py — same read semantics, same identity
// rules. A property read returns whatever the backend gave: the
// SysML Toolkit reports its own unimplemented members, so the SDK
// never second-guesses a result. Elements are safe, reference-counted
// value types over a shared model; JSON goes through the vendored
// nlohmann/json; as<T>() throws on the wrong metaclass.
//
// Collision rule: runtime members are multi-word
// snake_case (element_id, backend_handle, metaclass_name, get_raw,
// is_a, as, is_kind); spec members are generated lowerCamelCase
// methods and never contain an underscore. C++ keywords among spec
// names get a trailing underscore (`operator_`), enforced at
// generation time.
//
// Include <sysml/classes.g.hpp> (which includes this header); it
// supplies the generated hierarchy table Element::is_kind relies on.
#pragma once

#include <cstdint>
#include <memory>
#include <optional>
#include <stdexcept>
#include <string>
#include <string_view>
#include <unordered_map>
#include <utility>
#include <variant>
#include <vector>

#include <nlohmann/json.hpp>

namespace sysml {

// --- errors ----------------------------------------------------------------

class sdk_error : public std::runtime_error {
public:
    using std::runtime_error::runtime_error;
};

/// A property the backend reports it does not compute. Reserved for a
/// backend that says so itself; never inferred from an empty result.
class not_computed final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// A specification member the backend does not answer for this element:
/// the SysML Toolkit's refusals (not_implemented_in_toolkit), and an
/// engine's.
class not_answered : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// A specification member the SysML Toolkit does not answer: thrown
/// when the SysML Toolkit reports the member as not implemented.
class not_implemented_in_toolkit final : public not_answered {
public:
    using not_answered::not_answered;
};

/// A convenience member that gives no answer for this element by its contract (`invalid` in its
/// OCL): the literal of an expression that is not a literal, a bound that is an expression. Unlike
/// the other not_answered errors, it says what the answer is, not that the backend cannot give it.
class refused final : public not_answered {
public:
    using not_answered::not_answered;
};

/// The element no longer exists in the backend's current state.
class gone final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// The engine behind a backend could not answer: it failed while computing a value, or it is
/// not available (not installed, not downloadable, or no suitable runtime). The message says
/// which engine and why.
class backend_error : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// as<T>() on an element whose metaclass is not (a descendant of) T.
class wrong_kind final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// A reference that did not resolve — a SysML Toolkit @ref spelling
/// passthrough (non-standard, best-effort) or a dangling @id. Typed
/// navigation throws: the SDK requires resolved models.
/// get_raw() still exposes the reference as an Unresolved value.
class unresolved_reference final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

// --- leaf value types ------------------------------------------------------

/// A reference that did not resolve: carries the source spelling.
struct Unresolved {
    std::string spelling;

    friend bool operator==(const Unresolved& a, const Unresolved& b) {
        return a.spelling == b.spelling;
    }
    friend bool operator!=(const Unresolved& a, const Unresolved& b) {
        return !(a == b);
    }
};

namespace detail {
struct ModelCore;
}  // namespace detail

class Value;

// --- convenience members ----------------------------------------------------

/// Which members a convenience list reads: the element's own, those it inherits (for a namespace's
/// members, imported ones too), or both. A list of features gives its own first; a list of members or
/// definitions, outside the owned scope, is in the backend's order.
enum class Scope { Owned, Inherited, All };

/// Which visibilities a convenience list keeps: every one, public and protected, or public only.
enum class VisibilityFilter { Any, Protected, Public };

/// The contract's literal of a scope or visibility filter ("owned", "public").
inline const char* literal(Scope s) {
    return s == Scope::Owned ? "owned" : s == Scope::Inherited ? "inherited" : "all";
}
inline const char* literal(VisibilityFilter v) {
    return v == VisibilityFilter::Any ? "any" : v == VisibilityFilter::Protected ? "protected" : "public";
}

/// The unbounded `*` among a literal's values.
struct Unbounded {
    friend bool operator==(Unbounded, Unbounded) { return true; }
    friend bool operator!=(Unbounded, Unbounded) { return false; }
};

/// A literal's value (find_literal, and the bounds' numbers, find_lower_bound and find_upper_bound): an
/// integer, a real, a Boolean, a string, or the unbounded `*`. Read it with std::get or std::visit:
/// `std::get<std::int64_t>(expr.find_literal())`, `std::holds_alternative<Unbounded>(*bound)`.
using Literal = std::variant<std::int64_t, double, bool, std::string, Unbounded>;

/// A convenience member as the generated structs call it (tools/naming.py, convenience_runtime): its
/// name, its binding library symbol suffix, its parameters' names and kinds (scope, visibility, bool,
/// str, element), and its result (elements, element, bool, int, unlimited, real, strings, literal).
/// The int, unlimited and literal results answer a Literal.
struct Find {
    const char* name;
    const char* symbol;
    std::vector<const char*> param_names;
    std::vector<const char*> param_kinds;
    const char* result;
};

// The metaclass structs, declared ahead: Element's own members name them.
#include "forward.g.hpp"

/// A list of elements carrying the convenience members of its elements (defined in classes.g.hpp):
/// `xs.find_ports()` is the elements' `find_ports()` in list order, list results concatenated
/// (duplicates kept), absent single results dropped. A std::vector of the elements.
template <class T>
class ElementList;

/// The answer of a name-keyed convenience member (find_part_defs_by_name): names to elements in
/// membership order, as (name, element) pairs; find() and contains() look a name up.
template <class T>
class NameMap : public std::vector<std::pair<std::string, T>> {
public:
    /// The element under `name`, or std::nullopt.
    std::optional<T> find(const std::string& name) const {
        for (const auto& entry : *this)
            if (entry.first == name) return entry.second;
        return std::nullopt;
    }
    /// Whether some element is under `name`.
    bool contains(const std::string& name) const { return find(name).has_value(); }
};

// --- Element: the single state root ---------------------------------------
//
// All 174 generated metaclass structs virtually inherit this class and
// add methods only; every element object is {core, handle} regardless
// of its static type. Copy freely — the reference-counted core keeps
// the model alive as long as any element survives.

class Element {
public:
    Element() = default;  // detached; any access throws sdk_error

    // Copy-only, deliberately: under the virtual-inheritance diamonds a
    // defaulted MOVE assignment would move this shared base once per
    // inheritance path — the second move reads an already-moved-from
    // source and detaches the target (gcc -Wvirtual-move-assign).
    // Declaring the copy operations suppresses the implicit moves, so
    // "moves" fall back to cheap, idempotent copies.
    Element(const Element&) = default;
    Element& operator=(const Element&) = default;

    static constexpr std::string_view kind_name = "Element";

    std::string element_id() const;
    const std::string& backend_handle() const { return handle_; }
    std::string metaclass_name() const;

    /// Escape hatch: the property's backend value, wrapped but keeping
    /// unresolved references as Unresolved values. (Other backends
    /// return the unconverted raw value; here conversion is the only
    /// way to avoid vendored types in the public API.)
    Value get_raw(std::string_view prop) const;

    /// True if this element's metaclass is `kind` or a descendant of it
    /// per the spec hierarchy table (generated; never uses C++ RTTI).
    bool is_kind(std::string_view kind) const;

    template <class T>
    bool is_a() const {
        return is_kind(T::kind_name);
    }

    /// Typed view of the same element; throws wrong_kind on mismatch
    /// is_a<T>() never throws.
    template <class T>
    T as() const {
        if (!is_a<T>())
            throw wrong_kind(metaclass_name() + " is not a " +
                             std::string(T::kind_name) + " (element " +
                             element_id() + ")");
        T t;
        static_cast<Element&>(t) = *this;
        return t;
    }

    /// "<Metaclass qualifiedName-or-id>"
    std::string to_string() const;

    friend bool operator==(const Element& a, const Element& b);
    friend bool operator!=(const Element& a, const Element& b) {
        return !(a == b);
    }

    // The members of the metaclass Element, which every element has: getters
    // and operations, declared by the generated fragment and defined in
    // classes.g.hpp once every struct is complete.
#include "element_members.g.hpp"

protected:
    Value read_(std::string_view prop) const;

    /// Every generated operation routes through here: the backend evaluates it (the SysML Toolkit
    /// answers or refuses; a payload refuses). Arguments are made by detail::arg.
    Value call_(std::string_view op, std::vector<nlohmann::json> args) const;

    /// Every convenience member routes through here: the backend answers or refuses, and the result
    /// is wrapped (an unbounded `*` as INT64_MAX). Arguments are made by detail::arg, a scope or
    /// visibility filter as its literal.
    Value find_(const Find& member, std::vector<nlohmann::json> args) const;
    /// The backend's answer unwrapped (a literal's tagged JSON).
    nlohmann::json find_raw_(const Find& member, std::vector<nlohmann::json> args) const;
    /// A literal's value from its tagged JSON.
    static Literal as_literal_(const nlohmann::json& raw);
    /// A bound's number as a Literal from the backend's answer: none where it is null, unbounded where
    /// it is `*` (or, from the binding library, INT64_MAX, which only an upper bound can be).
    static std::optional<Literal> as_bound_(const nlohmann::json& raw, bool unlimited);

    /// A convenience member over a list of elements (the members of ElementList): list results
    /// concatenated in list order, single results with the absent ones dropped, as R; one backend call
    /// per model. If the member refuses for one element, the call refuses.
    template <class R, class T>
    static ElementList<R> find_all_(const std::vector<T>& xs, const Find& member, std::vector<nlohmann::json> args);
    /// The same for a member that answers strings.
    template <class T>
    static std::vector<std::string> find_all_strings_(const std::vector<T>& xs, const Find& member,
                                                      std::vector<nlohmann::json> args);
    /// The backend's answers for each element (one find_each per model), wrapped.
    template <class T>
    static std::vector<Value> find_each_(const std::vector<T>& xs, const Find& member, std::vector<nlohmann::json> args);

    // --- typed converters of a Value (generated operations call these) --
    template <class T>
    static std::vector<T> as_list_(const Value& v);
    template <class T>
    static std::optional<T> as_ref_(const Value& v);
    /// A name-keyed map from the backend's pairs of a name and an element, in their order.
    template <class T>
    static NameMap<T> as_map_(const Value& v);
    static std::optional<bool> as_bool_(const Value& v);
    static std::optional<std::string> as_string_(const Value& v);
    static std::optional<std::int64_t> as_int_(const Value& v);
    static std::optional<double> as_real_(const Value& v);
    static std::vector<std::string> as_string_list_(const Value& v);

    // --- typed-getter converters (generated members call these) ---------
    // Templates instantiate at use (after every struct is complete);
    // non-template converters are defined after Value below.

    template <class T>
    std::vector<T> read_list_(std::string_view prop) const;

    template <class T>
    std::optional<T> read_ref_(std::string_view prop) const;

    std::optional<bool> read_bool_(std::string_view prop) const;
    std::optional<std::string> read_string_(std::string_view prop) const;
    std::optional<std::int64_t> read_int_(std::string_view prop) const;
    std::optional<double> read_real_(std::string_view prop) const;
    std::vector<std::string> read_string_list_(std::string_view prop) const;

    const detail::ModelCore& core() const {
        if (!core_)
            throw sdk_error("detached element (default-constructed)");
        return *core_;
    }

private:
    Element(std::shared_ptr<const detail::ModelCore> core, std::string handle)
        : core_(std::move(core)), handle_(std::move(handle)) {}

    std::shared_ptr<const detail::ModelCore> core_;
    std::string handle_;

    friend struct detail::ModelCore;
    friend class Model;
    template <class>
    friend class ElementList;
};

// --- Value: one property result -------------------------------------------

using List = std::vector<Value>;
/// Fallback for plain JSON objects that are neither @id refs nor @ref
/// spellings (rare in full-form payloads). Pair-vector rather than map
/// so the recursion stays legal with an incomplete Value.
using Object = std::vector<std::pair<std::string, Value>>;

class Value {
public:
    using Var = std::variant<std::monostate, bool, std::int64_t, double,
                             std::string, Element, Unresolved, List, Object>;

    Value() = default;
    explicit Value(Var v) : v_(std::move(v)) {}

    bool is_null() const { return std::holds_alternative<std::monostate>(v_); }
    bool is_bool() const { return std::holds_alternative<bool>(v_); }
    bool is_int() const { return std::holds_alternative<std::int64_t>(v_); }
    bool is_double() const { return std::holds_alternative<double>(v_); }
    bool is_string() const { return std::holds_alternative<std::string>(v_); }
    bool is_element() const { return std::holds_alternative<Element>(v_); }
    bool is_unresolved() const { return std::holds_alternative<Unresolved>(v_); }
    bool is_list() const { return std::holds_alternative<List>(v_); }

    bool as_bool() const { return std::get<bool>(v_); }
    std::int64_t as_int() const { return std::get<std::int64_t>(v_); }
    double as_double() const { return std::get<double>(v_); }
    const std::string& as_string() const { return std::get<std::string>(v_); }
    const Element& as_element() const { return std::get<Element>(v_); }
    const Unresolved& as_unresolved() const { return std::get<Unresolved>(v_); }
    const List& as_list() const { return std::get<List>(v_); }
    const Object& as_object() const { return std::get<Object>(v_); }

    const Var& var() const { return v_; }

    friend bool operator==(const Value& a, const Value& b) { return a.v_ == b.v_; }
    friend bool operator!=(const Value& a, const Value& b) { return !(a == b); }

private:
    Var v_;
};

// --- Backend protocol (handle-based) --------------------------------------
//
// Raw values are nlohmann::json nodes (layer 1 is allowed to see the
// vendored type; layers 2/3 and Value are not). An id-keyed backend
// uses element ids as its handles.

class Backend {
public:
    virtual ~Backend() = default;

    /// nullptr = property absent. The node must stay valid while the
    /// backend lives.
    virtual const nlohmann::json* get(const std::string& handle,
                                      std::string_view prop) const = 0;
    virtual std::string metaclass(const std::string& handle) const = 0;
    virtual std::string element_id(const std::string& handle) const = 0;
    virtual std::optional<std::string> resolve(std::string_view qname) const = 0;
    virtual std::vector<std::string> all_handles() const = 0;

    /// A specification operation on the element `handle`, by its specification name, with its
    /// arguments in order (elements as {"@id": handle}). A backend that cannot evaluate it
    /// throws not_implemented_in_toolkit; a payload holds property values only.
    virtual nlohmann::json call(const std::string& handle, std::string_view op,
                                const nlohmann::json& args) const {
        (void)handle;
        (void)args;
        throw not_implemented_in_toolkit(std::string(op) + "(): operations are not available on "
                                        "payload models; load the model through the SysML Toolkit backend");
    }

    /// A convenience member on the element `handle`, with its arguments in order, defaults filled
    /// in (a scope or visibility filter as its literal, an element as {"@id": handle}). A backend that
    /// cannot answer it throws not_answered; where its contract gives no answer, refused. A payload
    /// holds property values only.
    virtual nlohmann::json find(const std::string& handle, const Find& member, const nlohmann::json& args) const {
        (void)handle;
        (void)args;
        throw not_answered(std::string(member.name) + ": the convenience members are not available on payload "
                           "models; load the model through the SysML Toolkit or the Pilot Implementation");
    }

    /// `find` on each element, an array with a result per handle in order, for the members of an
    /// ElementList. By default one `find` per element; a backend that can answer them together (in one
    /// request) overrides it.
    virtual nlohmann::json find_each(const std::vector<std::string>& handles, const Find& member,
                                     const nlohmann::json& args) const {
        nlohmann::json out = nlohmann::json::array();
        for (const auto& h : handles) out.push_back(find(h, member, args));
        return out;
    }
};

namespace detail {

/// Elements by id (document order, the last of a repeated id) and ids by qualified name (the
/// first). The index points into `doc`.
inline void index_payload(const nlohmann::json& doc,
                          std::unordered_map<std::string, const nlohmann::json*>& by_id,
                          std::vector<std::string>& order,
                          std::unordered_map<std::string, std::string>& by_qname) {
    for (const auto& e : doc) {
        if (!e.is_object())
            continue;
        auto id = e.find("@id");
        if (id == e.end() || !id->is_string())
            continue;
        if (by_id.insert_or_assign(id->get<std::string>(), &e).second)
            order.push_back(id->get<std::string>());
        auto qn = e.find("qualifiedName");
        if (qn != e.end() && qn->is_string() && !qn->get<std::string>().empty())
            by_qname.emplace(qn->get<std::string>(), id->get<std::string>());
    }
}

}  // namespace detail

/// A standard library read from a full-form interchange element array, such as the library
/// JSON published with the SDK. Parse it once and pass it to any number of payload models:
/// their references into the library then resolve by the library's normative ids.
/// Non-movable: the id index points into the owned document; share it through the shared_ptr
/// that parse() returns.
class PayloadLibrary {
public:
    explicit PayloadLibrary(nlohmann::json elements) : doc_(std::move(elements)) {
        std::vector<std::string> order;
        detail::index_payload(doc_, by_id_, order, by_qname_);
    }

    static std::shared_ptr<const PayloadLibrary> parse(std::string_view text) {
        return std::make_shared<const PayloadLibrary>(nlohmann::json::parse(text));
    }

    PayloadLibrary(const PayloadLibrary&) = delete;
    PayloadLibrary& operator=(const PayloadLibrary&) = delete;

    /// The number of library elements.
    std::size_t size() const { return by_id_.size(); }

private:
    friend class PayloadBackend;
    nlohmann::json doc_;
    std::unordered_map<std::string, const nlohmann::json*> by_id_;
    std::unordered_map<std::string, std::string> by_qname_;
};

/// Read-only backend over a full-form interchange element array.
/// Handles are the element ids themselves, listed in document order; an
/// id that occurs twice keeps its first position and its last element.
/// With a library, an id or qualified name the payload does not hold is
/// looked up in the library; the library's elements are not the model's, so
/// all_handles() lists the payload's only.
/// Non-movable: the id index points into the owned document.
class PayloadBackend final : public Backend {
public:
    explicit PayloadBackend(nlohmann::json elements,
                            std::shared_ptr<const PayloadLibrary> library = nullptr)
        : doc_(std::move(elements)), library_(std::move(library)) {
        detail::index_payload(doc_, by_id_, order_, by_qname_);
    }

    static std::unique_ptr<PayloadBackend> parse(
        std::string_view text, std::shared_ptr<const PayloadLibrary> library = nullptr) {
        return std::make_unique<PayloadBackend>(nlohmann::json::parse(text), std::move(library));
    }

    PayloadBackend(const PayloadBackend&) = delete;
    PayloadBackend& operator=(const PayloadBackend&) = delete;

    const nlohmann::json* get(const std::string& handle,
                              std::string_view prop) const override {
        const nlohmann::json& el = require(handle);
        auto it = el.find(prop);
        return it == el.end() ? nullptr : &*it;
    }

    std::string metaclass(const std::string& handle) const override {
        return require(handle).at("@type").get<std::string>();
    }

    std::string element_id(const std::string& handle) const override {
        require(handle);
        return handle;
    }

    std::optional<std::string> resolve(std::string_view qname) const override {
        std::string key(qname);
        auto it = by_qname_.find(key);
        if (it != by_qname_.end())
            return it->second;
        if (library_) {
            auto lib = library_->by_qname_.find(key);
            if (lib != library_->by_qname_.end())
                return lib->second;
        }
        return std::nullopt;
    }

    std::vector<std::string> all_handles() const override { return order_; }

private:
    const nlohmann::json& require(const std::string& handle) const {
        auto it = by_id_.find(handle);
        if (it != by_id_.end())
            return *it->second;
        if (library_) {
            auto lib = library_->by_id_.find(handle);
            if (lib != library_->by_id_.end())
                return *lib->second;
        }
        throw gone("no element " + handle + " in this payload");
    }

    nlohmann::json doc_;
    std::shared_ptr<const PayloadLibrary> library_;
    std::vector<std::string> order_;
    std::unordered_map<std::string, const nlohmann::json*> by_id_;
    std::unordered_map<std::string, std::string> by_qname_;
};

// --- ModelCore: the single owner ------------------------------------------

namespace detail {

struct ModelCore {
    std::unique_ptr<Backend> backend;

    explicit ModelCore(std::unique_ptr<Backend> b) : backend(std::move(b)) {}

    static Element mint(std::shared_ptr<const ModelCore> self, std::string handle) {
        return Element(std::move(self), std::move(handle));
    }

    static const std::string* id_ref(const nlohmann::json& v) {
        if (!v.is_object() || v.size() != 1)
            return nullptr;
        auto it = v.find("@id");
        return it != v.end() && it->is_string()
                   ? it->get_ptr<const nlohmann::json::string_t*>()
                   : nullptr;
    }

    /// throw_unresolved: property reads refuse unresolved models; get_raw
    /// keeps Unresolved values instead.
    Value wrap(const std::shared_ptr<const ModelCore>& self,
               const nlohmann::json* raw, bool throw_unresolved = false,
               std::string_view at = "?") const {
        if (raw == nullptr || raw->is_null())
            return Value();
        const nlohmann::json& v = *raw;
        if (const std::string* id = id_ref(v)) {
            try {
                backend->metaclass(*id);  // existence check (throws gone)
                return Value(Value::Var(mint(self, *id)));
            } catch (const gone&) {
                if (throw_unresolved)
                    throw unresolved_reference(
                        "dangling reference @id='" + *id + "' at " +
                        std::string(at) + ": target is not in this model; " +
                        "the SDK requires resolved models");
                return Value(Value::Var(Unresolved{*id}));
            }
        }
        if (v.is_object()) {
            auto ref = v.find("@ref");
            if (ref != v.end() && ref->is_string()) {
                if (throw_unresolved)
                    throw unresolved_reference(
                        "unresolved reference '" + ref->get<std::string>() +
                        "' at " + std::string(at) + " (the SysML Toolkit's @ref " +
                        "passthrough is non-standard best-effort); the SDK " +
                        "requires resolved models — use get_raw() for the " +
                        "raw value");
                return Value(Value::Var(Unresolved{ref->get<std::string>()}));
            }
            Object out;
            out.reserve(v.size());
            for (auto it = v.begin(); it != v.end(); ++it)
                out.emplace_back(it.key(),
                                 wrap(self, &it.value(), throw_unresolved, at));
            return Value(Value::Var(std::move(out)));
        }
        if (v.is_array()) {
            List out;
            out.reserve(v.size());
            for (const auto& item : v)
                out.push_back(wrap(self, &item, throw_unresolved, at));
            return Value(Value::Var(std::move(out)));
        }
        if (v.is_string())
            return Value(Value::Var(v.get<std::string>()));
        if (v.is_boolean())
            return Value(Value::Var(v.get<bool>()));
        if (v.is_number_integer())
            return Value(Value::Var(v.get<std::int64_t>()));
        return Value(Value::Var(v.get<double>()));
    }

    /// An operation's result, wrapped like a property value.
    Value call(const std::shared_ptr<const ModelCore>& self, const std::string& handle,
               std::string_view op, const nlohmann::json& args) const {
        const nlohmann::json raw = backend->call(handle, op, args);
        const std::string at = std::string(op) + "() (element " + handle + ")";
        return wrap(self, &raw, true, at);
    }

    /// The backend value, wrapped (this path refuses unresolved
    /// references). Mirrors P.__get__ in base.py.
    Value read(const std::shared_ptr<const ModelCore>& self,
               const std::string& handle, std::string_view prop) const {
        const nlohmann::json* raw = backend->get(handle, prop);
        const std::string at =
            std::string(prop) + " (element " + handle + ")";
        return wrap(self, raw, true, at);
    }
};

}  // namespace detail

// --- Model -----------------------------------------------------------------

class Model {
public:
    /// A model over any backend, for example ToolkitBackend (toolkit.hpp).
    static Model from_backend(std::unique_ptr<Backend> backend) {
        return Model(std::make_shared<const detail::ModelCore>(std::move(backend)));
    }

    /// A model read from a full-form interchange element array. With `library`, references
    /// into the standard library resolve against it; its elements are not the model's.
    static Model from_full_json(std::string_view text,
                                std::shared_ptr<const PayloadLibrary> library = nullptr) {
        return Model(std::make_shared<const detail::ModelCore>(
            PayloadBackend::parse(text, std::move(library))));
    }

    Element element(std::string handle) const {
        core_->backend->metaclass(handle);  // throws gone if absent
        return detail::ModelCore::mint(core_, std::move(handle));
    }

    /// Name resolution, with visibility (KerML 8.2.3.5), from the root: the backend's resolution. A
    /// payload model resolves its elements' own qualified names; the SysML Toolkit's lookup does not
    /// consider visibility yet. Only names the model gives count: an unnamed element is not found under
    /// the name of what it references. To get an element by its qualified name whatever its visibility,
    /// use find.
    std::optional<Element> resolve(std::string_view qname) const {
        auto handle = core_->backend->resolve(qname);
        if (!handle)
            return std::nullopt;
        return element(*std::move(handle));
    }

    /// Name resolution, with visibility, from a namespace: the specification's Namespace::resolve on
    /// `context` (its member element), as the backend answers it. Throws wrong_kind when `context` is not
    /// a namespace.
    std::optional<Element> resolve(std::string_view name, const Element& context) const;

    /// The element a qualified name names, whatever its visibility, the same on every backend: the first
    /// segment is a top-level name; each next one a member of the namespace before it, through one of its
    /// owned memberships (an alias's name included) or, with `inherited`, also through its inherited
    /// memberships (its supertypes' non-private ones). Empty if a segment names nothing. Segments may be
    /// quoted, with KerML's escapes ('~Plug').
    std::optional<Element> find(std::string_view qualified_name, bool inherited = false) const;

    /// A qualified name's segments as {written, name} (KerML 8.2.3.4.1): separated by `::`; an
    /// unrestricted name in single quotes, its escape sequences resolved (KerML 8.2.2.3); a leading
    /// global scope qualifier `$` dropped. Throws sdk_error on a malformed name.
    static std::vector<std::pair<std::string, std::string>> split_qualified_name(std::string_view qualified_name);

    std::vector<Element> all() const {
        std::vector<Element> out;
        for (auto& h : core_->backend->all_handles())
            out.push_back(detail::ModelCore::mint(core_, std::move(h)));
        return out;
    }

    /// Every element whose metaclass is T or a descendant of it, typed as T.
    template <class T>
    std::vector<T> elements_of_type() const {
        std::vector<T> out;
        for (const Element& e : all())
            if (e.is_a<T>())
                out.push_back(e.as<T>());
        return out;
    }

    /// The top-level namespaces: the elements without an owner that are
    /// namespaces (a membership may have no owner either).
    std::vector<Element> roots() const {
        std::vector<Element> out;
        for (const Element& e : all())
            if (e.is_kind("Namespace") && !e.getOwner())
                out.push_back(e);
        return out;
    }

    const Backend& backend() const { return *core_->backend; }

private:
    explicit Model(std::shared_ptr<const detail::ModelCore> core)
        : core_(std::move(core)) {}

    /// The member element of the first membership with this member name or short name.
    static std::optional<Element> member_named(const Value& memberships, const std::string& name);

    std::shared_ptr<const detail::ModelCore> core_;
};

// --- Element bodies needing complete ModelCore/Value -----------------------

inline std::string Element::element_id() const {
    return core().backend->element_id(handle_);
}

inline std::string Element::metaclass_name() const {
    return core().backend->metaclass(handle_);
}

inline Value Element::get_raw(std::string_view prop) const {
    return core().wrap(core_, core().backend->get(handle_, prop));
}

inline Value Element::read_(std::string_view prop) const {
    return core().read(core_, handle_, prop);
}

inline Value Element::call_(std::string_view op, std::vector<nlohmann::json> args) const {
    return core().call(core_, handle_, op, nlohmann::json(std::move(args)));
}

inline nlohmann::json Element::find_raw_(const Find& member, std::vector<nlohmann::json> args) const {
    return core().backend->find(handle_, member, nlohmann::json(std::move(args)));
}

inline Value Element::find_(const Find& member, std::vector<nlohmann::json> args) const {
    nlohmann::json raw = find_raw_(member, std::move(args));
    if (std::string_view(member.result) == "unlimited" && raw.is_string() && raw.get<std::string>() == "*")
        raw = INT64_MAX;
    const std::string at = std::string(member.name) + "() (element " + handle_ + ")";
    return core().wrap(core_, &raw, true, at);
}

template <class T>
std::vector<Value> Element::find_each_(const std::vector<T>& xs, const Find& member, std::vector<nlohmann::json> args) {
    std::vector<Value> out;
    if (xs.empty())
        return out;
    const nlohmann::json given(std::move(args));
    const Element& first = xs.front();
    bool one_model = true;
    for (const Element& x : xs) one_model = one_model && x.core_ == first.core_;
    nlohmann::json raws = nlohmann::json::array();
    if (one_model) {
        std::vector<std::string> handles;
        for (const Element& x : xs) handles.push_back(x.handle_);
        raws = first.core().backend->find_each(handles, member, given);
    } else {
        for (const Element& x : xs) raws.push_back(x.core().backend->find(x.handle_, member, given));
    }
    for (std::size_t i = 0; i < xs.size(); ++i) {
        const Element& x = xs[i];
        const std::string at = std::string(member.name) + "() (element " + x.handle_ + ")";
        out.push_back(x.core().wrap(x.core_, &raws.at(i), true, at));
    }
    return out;
}

template <class R, class T>
ElementList<R> Element::find_all_(const std::vector<T>& xs, const Find& member, std::vector<nlohmann::json> args) {
    ElementList<R> out;
    const bool single = std::string_view(member.result) == "element";
    for (const Value& v : find_each_(xs, member, std::move(args))) {
        if (single) {
            if (!v.is_null()) out.push_back(v.as_element().template as<R>());
        } else if (!v.is_null()) {
            for (const Value& item : v.as_list()) out.push_back(item.as_element().template as<R>());
        }
    }
    return out;
}

template <class T>
std::vector<std::string> Element::find_all_strings_(const std::vector<T>& xs, const Find& member,
                                                    std::vector<nlohmann::json> args) {
    std::vector<std::string> out;
    for (const Value& v : find_each_(xs, member, std::move(args)))
        for (const std::string& s : as_string_list_(v)) out.push_back(s);
    return out;
}

inline std::vector<std::pair<std::string, std::string>> Model::split_qualified_name(std::string_view qualified_name) {
    const std::string qn(qualified_name);
    std::vector<std::pair<std::string, std::string>> out;
    std::size_t i = 0;
    while (i < qn.size()) {
        if (qn[i] == '\'') {
            std::size_t j = i + 1;
            std::string name;
            while (j < qn.size() && qn[j] != '\'') {
                if (qn[j] == '\\' && j + 1 < qn.size()) {
                    const char c = qn[j + 1];
                    name += c == 'b' ? '\b' : c == 'f' ? '\f' : c == 't' ? '\t' : c == 'n' ? '\n' : c;
                    j += 2;
                } else {
                    name += qn[j++];
                }
            }
            if (j >= qn.size())
                throw sdk_error("an unterminated quoted name in " + qn);
            out.emplace_back(qn.substr(i, j + 1 - i), name);
            i = j + 1;
        } else {
            std::size_t j = qn.find("::", i);
            if (j == std::string::npos)
                j = qn.size();
            out.emplace_back(qn.substr(i, j - i), qn.substr(i, j - i));
            i = j;
        }
        if (i < qn.size()) {
            if (qn.compare(i, 2, "::") != 0)
                throw sdk_error("a name in " + qn + " is not followed by '::'");
            i += 2;
        }
    }
    if (!out.empty() && out.front().second == "$")
        out.erase(out.begin());
    return out;
}

inline std::optional<Element> Model::member_named(const Value& memberships, const std::string& name) {
    if (!memberships.is_list())
        return std::nullopt;
    for (const Value& v : memberships.as_list()) {
        if (!v.is_element())
            continue;
        const Element& m = v.as_element();
        const Value member_name = m.read_("memberName");
        const Value short_name = m.read_("memberShortName");
        if ((member_name.is_string() && member_name.as_string() == name) ||
            (short_name.is_string() && short_name.as_string() == name)) {
            const Value e = m.read_("memberElement");
            return e.is_element() ? std::optional<Element>(e.as_element()) : std::nullopt;
        }
    }
    return std::nullopt;
}

inline std::optional<Element> Model::find(std::string_view qualified_name, bool inherited) const {
    const std::string qn(qualified_name);
    if (auto handle = core_->backend->resolve(qn)) {
        Element el = element(*handle);
        const Value own = el.read_("qualifiedName");
        if (own.is_string() && own.as_string() == qn)
            return el;
    }
    const auto segments = split_qualified_name(qn);
    if (segments.empty())
        return std::nullopt;
    std::optional<Element> current;
    if (auto handle = core_->backend->resolve(segments.front().first))
        current = element(*handle);
    if (!current)
        for (const Element& root : roots()) {
            current = member_named(root.read_("ownedMembership"), segments.front().second);
            if (current)
                break;
        }
    for (std::size_t i = 1; i < segments.size(); ++i) {
        if (!current || !current->is_kind("Namespace"))
            return std::nullopt;
        auto found = member_named(current->read_("ownedMembership"), segments[i].second);
        if (!found && inherited && current->is_kind("Type"))
            found = member_named(current->read_("inheritedMembership"), segments[i].second);
        current = found;
    }
    return current;
}

inline std::optional<Element> Model::resolve(std::string_view name, const Element& context) const {
    if (!context.is_kind("Namespace"))
        throw wrong_kind(context.metaclass_name() + " is not a Namespace: resolve needs a namespace as its context");
    const Value m = context.call_("resolve", {nlohmann::json(std::string(name))});
    if (!m.is_element())
        return std::nullopt;
    const Value e = m.as_element().read_("memberElement");
    return e.is_element() ? std::optional<Element>(e.as_element()) : std::nullopt;
}

inline std::optional<Literal> Element::as_bound_(const nlohmann::json& raw, bool unlimited) {
    if (raw.is_null())
        return std::nullopt;
    if (raw.is_string() && raw.get<std::string>() == "*")
        return Literal{Unbounded{}};
    const std::int64_t n = raw.is_number_integer() ? raw.get<std::int64_t>()
                                                   : static_cast<std::int64_t>(raw.get<double>());
    if (unlimited && n == INT64_MAX)
        return Literal{Unbounded{}};
    return Literal{n};
}

inline Literal Element::as_literal_(const nlohmann::json& raw) {
    const nlohmann::json v = raw.is_string() ? nlohmann::json::parse(raw.get<std::string>()) : raw;
    if (v.is_object() && v.size() == 1) {
        const auto& [k, x] = *v.items().begin();
        if (k == "unbounded") return Unbounded{};
        if (k == "integer") return x.get<std::int64_t>();
        if (k == "real") return x.get<double>();
        if (k == "boolean") return x.get<bool>();
        if (k == "string") return x.get<std::string>();
    }
    throw sdk_error("a literal value was expected, not " + raw.dump());
}

namespace detail {

/// An operation argument as the backend contract carries it: an element as {"@id": handle}, a
/// list as an array, an absent value as null.
inline nlohmann::json arg(const Element& e) { return {{"@id", e.backend_handle()}}; }
inline nlohmann::json arg(Scope v) { return literal(v); }
inline nlohmann::json arg(VisibilityFilter v) { return literal(v); }
inline nlohmann::json arg(bool v) { return v; }
inline nlohmann::json arg(const std::string& v) { return v; }
inline nlohmann::json arg(std::int64_t v) { return v; }
inline nlohmann::json arg(double v) { return v; }

template <class T>
nlohmann::json arg(const std::optional<T>& v) {
    return v ? arg(*v) : nlohmann::json(nullptr);
}

template <class T>
nlohmann::json arg(const std::vector<T>& v) {
    nlohmann::json out = nlohmann::json::array();
    for (const T& item : v)
        out.push_back(arg(item));
    return out;
}

}  // namespace detail

inline std::string Element::to_string() const {
    const nlohmann::json* qn = core().backend->get(handle_, "qualifiedName");
    std::string label =
        (qn != nullptr && qn->is_string() && !qn->get<std::string>().empty())
            ? qn->get<std::string>()
            : element_id();
    return "<" + metaclass_name() + " " + label + ">";
}

inline bool operator==(const Element& a, const Element& b) {
    return a.core_ == b.core_ && a.core_ != nullptr &&
           a.element_id() == b.element_id();
}

// --- typed-getter converter bodies -----------------------------------------

template <class T>
std::vector<T> Element::read_list_(std::string_view prop) const {
    return as_list_<T>(read_(prop));
}

template <class T>
std::optional<T> Element::read_ref_(std::string_view prop) const {
    return as_ref_<T>(read_(prop));
}

inline std::optional<bool> Element::read_bool_(std::string_view prop) const {
    return as_bool_(read_(prop));
}

inline std::optional<std::string> Element::read_string_(std::string_view prop) const {
    return as_string_(read_(prop));
}

inline std::optional<std::int64_t> Element::read_int_(std::string_view prop) const {
    return as_int_(read_(prop));
}

inline std::optional<double> Element::read_real_(std::string_view prop) const {
    return as_real_(read_(prop));
}

inline std::vector<std::string> Element::read_string_list_(std::string_view prop) const {
    return as_string_list_(read_(prop));
}

template <class T>
std::vector<T> Element::as_list_(const Value& v) {
    std::vector<T> out;
    if (v.is_null())
        return out;  // absent arrays normalize to empty
    const List& l = v.as_list();
    out.reserve(l.size());
    for (const Value& item : l)
        out.push_back(item.as_element().as<T>());
    return out;
}

template <class T>
std::optional<T> Element::as_ref_(const Value& v) {
    if (v.is_null())
        return std::nullopt;
    return v.as_element().as<T>();
}

template <class T>
NameMap<T> Element::as_map_(const Value& v) {
    NameMap<T> out;
    if (v.is_null())
        return out;
    for (const Value& pair : v.as_list()) {
        const List& p = pair.as_list();
        out.emplace_back(p.at(0).as_string(), p.at(1).as_element().as<T>());
    }
    return out;
}

inline std::optional<bool> Element::as_bool_(const Value& v) {
    if (v.is_null())
        return std::nullopt;
    return v.as_bool();
}

inline std::optional<std::string> Element::as_string_(const Value& v) {
    if (v.is_null())
        return std::nullopt;
    return v.as_string();
}

inline std::optional<std::int64_t> Element::as_int_(const Value& v) {
    if (v.is_null())
        return std::nullopt;
    return v.is_int() ? v.as_int()
                      : static_cast<std::int64_t>(v.as_double());
}

inline std::optional<double> Element::as_real_(const Value& v) {
    if (v.is_null())
        return std::nullopt;
    return v.is_double() ? v.as_double()
                         : static_cast<double>(v.as_int());
}

inline std::vector<std::string> Element::as_string_list_(const Value& v) {
    std::vector<std::string> out;
    if (v.is_null())
        return out;
    const List& l = v.as_list();
    out.reserve(l.size());
    for (const Value& item : l)
        out.push_back(item.as_string());
    return out;
}

}  // namespace sysml

namespace std {

/// Elements hash by their element id, as they compare: an Element can key an
/// unordered container.
template <>
struct hash<sysml::Element> {
    std::size_t operator()(const sysml::Element& e) const {
        return std::hash<std::string>()(e.element_id());
    }
};

}  // namespace std
