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

/// A specification member the SysML Toolkit does not answer: thrown
/// when the SysML Toolkit reports the member as not implemented.
class not_implemented_in_toolkit final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

/// The element no longer exists in the backend's current state.
class gone final : public sdk_error {
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

// The metaclass structs, declared ahead: Element's own members name them.
#include "forward.g.hpp"

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

    // --- typed converters of a Value (generated operations call these) --
    template <class T>
    static std::vector<T> as_list_(const Value& v);
    template <class T>
    static std::optional<T> as_ref_(const Value& v);
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

    std::optional<Element> resolve(std::string_view qname) const {
        auto handle = core_->backend->resolve(qname);
        if (!handle)
            return std::nullopt;
        return element(*std::move(handle));
    }

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

namespace detail {

/// An operation argument as the backend contract carries it: an element as {"@id": handle}, a
/// list as an array, an absent value as null.
inline nlohmann::json arg(const Element& e) { return {{"@id", e.backend_handle()}}; }
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
