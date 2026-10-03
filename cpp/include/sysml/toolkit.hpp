// The SysML Toolkit backend for C++: the binding library (abi/) loaded at run time. Implements
// the same Backend contract as PayloadBackend, so the generated structs and Model are untouched.
// Handles are the integers the library mints, carried as decimal strings to fit the contract;
// references come back as {"@id": handle} JSON so ModelCore::wrap mints elements exactly as it
// does for payloads.
//
// The Backend contract hands out pointers to JSON nodes that must stay valid while the backend
// lives, so every value read from the SysML Toolkit is kept in a per-backend cache keyed by
// handle and member; the pointer returned refers into that cache.
//
// Dispatch: get(handle, prop) finds the declaring class of prop for the element's metaclass
// through toolkit_table.g.hpp and calls that export; call(handle, op, args) finds the operation's
// row the same way and passes each argument as the binding library's parameter C type says.
// Status codes follow the ABI specification: NOT_IMPLEMENTED throws not_implemented_in_toolkit,
// NOT_APPLICABLE is an absent value, INVALID_HANDLE throws gone, UNRESOLVED throws
// unresolved_reference.
#pragma once

#include <cstdint>
#include <cstdlib>
#include <memory>
#include <string>
#include <string_view>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#ifdef _WIN32
#include <windows.h>
#else
#include <dlfcn.h>
#include <unistd.h>
#endif
#ifdef __APPLE__
#include <mach-o/dyld.h>
#endif

#include "sdk.hpp"
#include "toolkit_table.g.hpp"

namespace sysml {

class ToolkitBackend final : public Backend {
public:
    // -- ABI structs (section 4), native layout ------------------------------------------
    struct Str { const std::uint8_t* ptr; std::size_t len; bool present; void* owner; };
    struct Handles { const std::uint64_t* items; std::size_t len; void* owner; };
    struct Ref { bool present; std::uint64_t value; };
    struct Bool { bool present; bool value; };
    struct Int { bool present; std::int64_t value; };
    struct Real { bool present; double value; };
    struct LoadOptions {
        const Str* paths; std::size_t paths_len;
        const Str* source_names; const Str* source_texts; std::size_t sources_len;
        Str library_dir;
    };
    static constexpr int OK = 0, NOT_IMPLEMENTED = 1, NOT_APPLICABLE = 2, INVALID_HANDLE = 3, UNRESOLVED = 7;

    /// One loaded library, shared by every session created from it.
    class Library {
    public:
        explicit Library(const std::string& path) {
#ifdef _WIN32
            handle_ = static_cast<void*>(LoadLibraryA(path.c_str()));
#else
            handle_ = dlopen(path.c_str(), RTLD_NOW);
#endif
            if (!handle_) throw sdk_error("cannot load the binding library at " + path);
            auto digest = reinterpret_cast<const char* (*)()>(sym("sysmlv2_names_digest"));
            auto digest_len = reinterpret_cast<std::size_t (*)()>(sym("sysmlv2_names_digest_len"));
            std::string have(digest(), digest_len());
            if (have != toolkit_table::NAMES_DIGEST)
                throw sdk_error("binding library " + path + " was generated from naming table " + have.substr(0, 12)
                                + ", this SDK from " + std::string(toolkit_table::NAMES_DIGEST.substr(0, 12)) + "; regenerate one side");
        }
        Library(const Library&) = delete;
        Library& operator=(const Library&) = delete;

        void* sym(const char* name) {
            auto it = syms_.find(name);
            if (it != syms_.end()) return it->second;
#ifdef _WIN32
            void* p = reinterpret_cast<void*>(GetProcAddress(static_cast<HMODULE>(handle_), name));
#else
            void* p = dlsym(handle_, name);
#endif
            if (!p) throw sdk_error(std::string("binding library lacks ") + name);
            syms_[name] = p;
            return p;
        }

        /// SYSMLV2_ABI; else the library beside the running executable (where to copy the one the
        /// release archive carries in lib/<target>/); else abi/target/release above the working
        /// directory (a build of the SDK's source).
        static std::string default_path() {
            if (const char* env = std::getenv("SYSMLV2_ABI")) return env;
#ifdef _WIN32
            const char* name = "sysmlv2_abi.dll";
#elif defined(__APPLE__)
            const char* name = "libsysmlv2_abi.dylib";
#else
            const char* name = "libsysmlv2_abi.so";
#endif
            std::string beside = executable_dir() + name;
            if (FILE* f = std::fopen(beside.c_str(), "rb")) { std::fclose(f); return beside; }
            std::string prefix;
            for (int up = 0; up < 6; ++up) {
                std::string candidate = prefix + "abi/target/release/" + name;
                if (FILE* f = std::fopen(candidate.c_str(), "rb")) { std::fclose(f); return candidate; }
                prefix += "../";
            }
            throw sdk_error("binding library not found: set SYSMLV2_ABI to the library file in the release's sysmlv2_abi archive for this platform (sysmlv2_abi.dll, libsysmlv2_abi.so or libsysmlv2_abi.dylib), or one built from abi/ in the SDK source");
        }

    private:
        void* handle_ = nullptr;
        std::unordered_map<std::string, void*> syms_;

        /// The running executable's directory, with a trailing separator; empty if unknown.
        static std::string executable_dir() {
            std::string path;
#ifdef _WIN32
            char buf[MAX_PATH];
            DWORD n = GetModuleFileNameA(nullptr, buf, MAX_PATH);
            if (n == 0 || n == MAX_PATH) return "";
            path.assign(buf, n);
#elif defined(__APPLE__)
            char buf[4096];
            uint32_t size = sizeof buf;
            if (_NSGetExecutablePath(buf, &size) != 0) return "";
            path = buf;
#else
            char buf[4096];
            ssize_t n = readlink("/proc/self/exe", buf, sizeof buf - 1);
            if (n <= 0) return "";
            path.assign(buf, static_cast<std::size_t>(n));
#endif
            std::size_t slash = path.find_last_of("/\\");
            return slash == std::string::npos ? std::string() : path.substr(0, slash + 1);
        }
    };

    static std::shared_ptr<Library> default_library() {
        static std::shared_ptr<Library> lib = std::make_shared<Library>(Library::default_path());
        return lib;
    }

    /// Open a session over files.
    static std::unique_ptr<ToolkitBackend> open(const std::vector<std::string>& paths, std::shared_ptr<Library> lib = default_library()) {
        return open_impl(std::move(lib), paths, {}, "");
    }
    /// Open a session over in-memory sources: unit name to text.
    static std::unique_ptr<ToolkitBackend> open_sources(const std::vector<std::pair<std::string, std::string>>& sources,
                                                       std::shared_ptr<Library> lib = default_library()) {
        return open_impl(std::move(lib), {}, sources, "");
    }
    /// Open a session over files, or over in-memory sources when any are given, with the standard
    /// library read from the directory `library_dir` (empty for none).
    static std::unique_ptr<ToolkitBackend> open(const std::vector<std::string>& paths,
                                               const std::vector<std::pair<std::string, std::string>>& sources,
                                               const std::string& library_dir,
                                               std::shared_ptr<Library> lib = default_library()) {
        return open_impl(std::move(lib), paths, sources, library_dir);
    }

    ~ToolkitBackend() override {
        if (session_) fn<void (*)(void*)>("sysmlv2_session_close")(session_);
    }

    /// The model as full-form interchange JSON, from the SysML Toolkit's own emitter. With
    /// `closures` (the default) the inheritance-aware properties carry the specification's values,
    /// inherited and imported members included; without, the owned side only, as the
    /// SysML Toolkit's command-line export writes them.
    std::string full_json(bool closures = true) const {
        Str s{};
        check(fn<int (*)(void*, std::uint32_t, Str*)>("sysmlv2_session_full_json")(session_, closures ? 1u : 0u, &s), "full_json");
        return take_str(s).value_or("[]");
    }

    std::vector<std::string> roots() const { return handles_of("sysmlv2_session_roots", "roots"); }

    // -- the backend contract ---------------------------------------------------------------

    const nlohmann::json* get(const std::string& handle, std::string_view prop) const override {
        const std::string mc = metaclass(handle);
        const toolkit_table::Entry* e = declaring(mc, prop);
        if (!e) return nullptr;  // not a member of this metaclass: absent, as the payload backend answers
        const std::string key = handle + "|" + std::string(prop);
        auto it = values_.find(key);
        if (it != values_.end()) return it->second.is_null() ? nullptr : &it->second;
        nlohmann::json v = call(e->symbol, e->code, std::stoull(handle), mc + "." + std::string(prop));
        auto& slot = values_[key] = std::move(v);
        return slot.is_null() ? nullptr : &slot;
    }

    /// A specification operation. Each argument is passed as the binding library's parameter C
    /// type says: an element as its handle, a list of elements as a handle array, a Boolean as a
    /// C bool, anything else as text. The library answers or refuses.
    nlohmann::json call(const std::string& handle, std::string_view op, const nlohmann::json& args) const override {
        const std::string mc = metaclass(handle);
        const std::string where = mc + "." + std::string(op) + "()";
        const toolkit_table::Entry* e = declaring(mc, std::string(op) + "()");
        if (!e) throw sdk_error(where + ": the binding library has no such operation");
        std::vector<std::string> types;
        for (std::string_view rest = e->params; !rest.empty();) {
            auto semi = rest.find(';');
            types.emplace_back(rest.substr(0, semi));
            rest = semi == std::string_view::npos ? std::string_view() : rest.substr(semi + 1);
        }
        // One machine word per argument: the address of a Str or Handles struct, or the value of
        // a Handle or bool. The structs and their buffers live until the call returns.
        std::vector<std::string> texts(types.size());
        std::vector<std::vector<std::uint64_t>> items(types.size());
        std::vector<Str> strs(types.size());
        std::vector<Handles> lists(types.size());
        std::vector<std::uint64_t> words(types.size());
        std::string sig;
        // The binding library has no "no element": a missing element is refused, never passed as a
        // handle that names some other element.
        auto handle_of = [&where](const nlohmann::json& a, std::size_t i) -> std::uint64_t {
            if (a.is_object() && a.contains("@id")) return std::stoull(a["@id"].get<std::string>());
            throw std::invalid_argument(where + ": argument " + std::to_string(i + 1) + " must be an element");
        };
        for (std::size_t i = 0; i < types.size(); ++i) {
            const nlohmann::json a = i < args.size() ? args[i] : nlohmann::json(nullptr);
            if (types[i] == "Handle") {
                words[i] = handle_of(a, i); sig += 'U';
            } else if (types[i] == "bool") {
                words[i] = a.is_boolean() && a.get<bool>() ? 1 : 0; sig += 'B';
            } else if (types[i] == "Handles") {
                if (a.is_array()) for (const auto& x : a) items[i].push_back(handle_of(x, i));
                lists[i] = Handles{items[i].data(), items[i].size(), nullptr};
                words[i] = reinterpret_cast<std::uintptr_t>(&lists[i]); sig += 'P';
            } else {  // text; an element as its element id, as every binding passes it
                texts[i] = a.is_string() ? a.get<std::string>()
                         : a.is_null() ? std::string()
                         : a.is_object() && a.contains("@id") ? element_id(a["@id"].get<std::string>())
                         : a.dump();
                strs[i] = Str{reinterpret_cast<const std::uint8_t*>(texts[i].data()), texts[i].size(), true, nullptr};
                words[i] = reinterpret_cast<std::uintptr_t>(&strs[i]); sig += 'P';
            }
        }
        void* f = lib_->sym(e->symbol);
        const std::uint64_t h = std::stoull(handle);
        auto p = [&](std::size_t i) { return reinterpret_cast<void*>(static_cast<std::uintptr_t>(words[i])); };
        Out o{};
        int st;
        if (sig.empty()) st = reinterpret_cast<int (*)(void*, std::uint64_t, void*)>(f)(session_, h, &o);
        else if (sig == "P") st = reinterpret_cast<int (*)(void*, std::uint64_t, void*, void*)>(f)(session_, h, p(0), &o);
        else if (sig == "PP") st = reinterpret_cast<int (*)(void*, std::uint64_t, void*, void*, void*)>(f)(session_, h, p(0), p(1), &o);
        else if (sig == "PPP") st = reinterpret_cast<int (*)(void*, std::uint64_t, void*, void*, void*, void*)>(f)(session_, h, p(0), p(1), p(2), &o);
        else if (sig == "U") st = reinterpret_cast<int (*)(void*, std::uint64_t, std::uint64_t, void*)>(f)(session_, h, words[0], &o);
        else if (sig == "B") st = reinterpret_cast<int (*)(void*, std::uint64_t, bool, void*)>(f)(session_, h, words[0] != 0, &o);
        else if (sig == "PPB") st = reinterpret_cast<int (*)(void*, std::uint64_t, void*, void*, bool, void*)>(f)(session_, h, p(0), p(1), words[2] != 0, &o);
        else if (sig == "PBB") st = reinterpret_cast<int (*)(void*, std::uint64_t, void*, bool, bool, void*)>(f)(session_, h, p(0), words[1] != 0, words[2] != 0, &o);
        else if (sig == "UU") st = reinterpret_cast<int (*)(void*, std::uint64_t, std::uint64_t, std::uint64_t, void*)>(f)(session_, h, words[0], words[1], &o);
        else if (sig == "UP") st = reinterpret_cast<int (*)(void*, std::uint64_t, std::uint64_t, void*, void*)>(f)(session_, h, words[0], p(1), &o);
        else if (sig == "UPP") st = reinterpret_cast<int (*)(void*, std::uint64_t, std::uint64_t, void*, void*, void*)>(f)(session_, h, words[0], p(1), p(2), &o);
        else throw sdk_error(where + ": no call shape for parameters '" + sig + "'");
        return unpack(e->code, o, st, where);
    }

    std::string metaclass(const std::string& handle) const override {
        auto it = metaclass_.find(handle);
        if (it != metaclass_.end()) return it->second;
        Str s{};
        check(fn<int (*)(void*, std::uint64_t, Str*)>("sysmlv2_metaclass")(session_, std::stoull(handle), &s), "metaclass(" + handle + ")");
        std::string mc = take_str(s).value_or("");
        metaclass_[handle] = mc;
        return mc;
    }

    std::string element_id(const std::string& handle) const override {
        nlohmann::json v = call("sysmlv2_element_element_id", 'S', std::stoull(handle), "elementId");
        return v.is_string() ? v.get<std::string>() : "";
    }

    std::optional<std::string> resolve(std::string_view qname) const override {
        std::string q(qname);
        Str arg{reinterpret_cast<const std::uint8_t*>(q.data()), q.size(), true, nullptr};
        Ref out{};
        if (!check(fn<int (*)(void*, const Str*, Ref*)>("sysmlv2_session_resolve")(session_, &arg, &out),
                   "resolve(" + q + ")"))
            return std::nullopt;
        if (!out.present) return std::nullopt;
        return std::to_string(out.value);
    }

    std::vector<std::string> all_handles() const override { return handles_of("sysmlv2_user_elements", "user_elements"); }

private:
    std::shared_ptr<Library> lib_;
    void* session_ = nullptr;
    mutable std::unordered_map<std::string, std::string> metaclass_;
    mutable std::unordered_map<std::string, const toolkit_table::Entry*> declaring_;
    mutable std::unordered_map<std::string, nlohmann::json> values_;

    ToolkitBackend(std::shared_ptr<Library> lib, void* session) : lib_(std::move(lib)), session_(session) {}

    static std::unique_ptr<ToolkitBackend> open_impl(std::shared_ptr<Library> lib, const std::vector<std::string>& paths,
                                                    const std::vector<std::pair<std::string, std::string>>& sources, const std::string& library_dir) {
        auto as_str = [](const std::string& s) { return Str{reinterpret_cast<const std::uint8_t*>(s.data()), s.size(), true, nullptr}; };
        std::vector<Str> path_strs, names, texts;
        for (const auto& p : paths) path_strs.push_back(as_str(p));
        for (const auto& [n, t] : sources) { names.push_back(as_str(n)); texts.push_back(as_str(t)); }
        LoadOptions opts{};
        opts.paths = path_strs.data(); opts.paths_len = path_strs.size();
        opts.source_names = names.data(); opts.source_texts = texts.data(); opts.sources_len = names.size();
        opts.library_dir = library_dir.empty() ? Str{nullptr, 0, false, nullptr} : as_str(library_dir);
        void* session = nullptr;
        auto open = reinterpret_cast<int (*)(const LoadOptions*, void**)>(lib->sym("sysmlv2_session_open"));
        int st = open(&opts, &session);
        if (st != OK) throw sdk_error("the SysML Toolkit could not load the model: status " + std::to_string(st) + " (the SysML Toolkit's reason is on stderr)");
        return std::unique_ptr<ToolkitBackend>(new ToolkitBackend(std::move(lib), session));
    }

    template <typename F>
    F fn(const char* name) const { return reinterpret_cast<F>(lib_->sym(name)); }

    void free(void* owner) const { fn<void (*)(void*)>("sysmlv2_free")(owner); }

    std::string last_error() const {
        Str s{};
        fn<int (*)(void*, Str*)>("sysmlv2_last_error")(session_, &s);
        return take_str(s).value_or("");
    }

    /// True when a value is present; false for NOT_APPLICABLE; throws otherwise.
    bool check(int st, const std::string& where) const {
        if (st == OK) return true;
        if (st == NOT_APPLICABLE) return false;
        std::string msg = last_error();
        if (st == NOT_IMPLEMENTED) throw not_implemented_in_toolkit(msg.empty() ? where + ": not implemented by the SysML Toolkit" : msg);
        if (st == INVALID_HANDLE) throw gone(msg.empty() ? where + ": invalid handle" : msg);
        if (st == UNRESOLVED) throw unresolved_reference(msg.empty() ? where + ": a reference did not resolve" : msg);
        throw sdk_error(where + ": status " + std::to_string(st) + ": " + msg);
    }

    std::optional<std::string> take_str(const Str& s) const {
        std::optional<std::string> v;
        if (s.present && s.ptr) v = std::string(reinterpret_cast<const char*>(s.ptr), s.len);
        free(s.owner);
        return v;
    }

    std::vector<std::string> take_handles(const Handles& h) const {
        std::vector<std::string> out;
        out.reserve(h.len);
        for (std::size_t i = 0; i < h.len; ++i) out.push_back(std::to_string(h.items[i]));
        free(h.owner);
        return out;
    }

    std::vector<std::string> handles_of(const char* symbol, const char* where) const {
        Handles h{};
        check(fn<int (*)(void*, Handles*)>(symbol)(session_, &h), where);
        return take_handles(h);
    }

    /// Room for any result struct, aligned for all of them.
    union Out { Handles h; Ref r; Str s; Bool b; Int i; Real d; };

    nlohmann::json call(const char* symbol, char code, std::uint64_t handle, const std::string& where) const {
        using MemberFn = int (*)(void*, std::uint64_t, void*);
        Out o{};
        return unpack(code, o, fn<MemberFn>(symbol)(session_, handle, &o), where);
    }

    /// The result in `o` as the backend contract carries it, after checking the status.
    nlohmann::json unpack(char code, Out& o, int st, const std::string& where) const {
        if (!check(st, where)) return code == 'H' || code == 'T' ? nlohmann::json::array() : nlohmann::json(nullptr);
        switch (code) {
            case 'H': { nlohmann::json a = nlohmann::json::array(); for (auto& h : take_handles(o.h)) a.push_back({{"@id", h}}); return a; }
            case 'R': return o.r.present ? nlohmann::json{{"@id", std::to_string(o.r.value)}} : nlohmann::json(nullptr);
            case 'S': { auto s = take_str(o.s); return s ? nlohmann::json(*s) : nlohmann::json(nullptr); }
            case 'T': {  // a list of strings (a requirement's `text`), as JSON text
                auto s = take_str(o.s);
                return s ? nlohmann::json::parse(*s) : nlohmann::json::array();
            }
            case 'B': return o.b.present ? nlohmann::json(o.b.value) : nlohmann::json(nullptr);
            case 'I': return o.i.present ? nlohmann::json(o.i.value) : nlohmann::json(nullptr);
            default:  return o.d.present ? nlohmann::json(o.d.value) : nlohmann::json(nullptr);
        }
    }

    const toolkit_table::Entry* declaring(const std::string& mc, std::string_view prop) const {
        const std::string key = mc + "::" + std::string(prop);
        auto it = declaring_.find(key);
        if (it != declaring_.end()) return it->second;
        const toolkit_table::Entry* found = nullptr;
        std::vector<std::string_view> stack{mc};
        std::unordered_set<std::string_view> seen;
        while (!stack.empty()) {
            std::string_view c = stack.back(); stack.pop_back();
            if (!seen.insert(c).second) continue;
            std::string k = std::string(c) + "::" + std::string(prop);
            auto f = toolkit_table::funcs().find(k);
            if (f != toolkit_table::funcs().end()) { found = &f->second; break; }
            auto b = toolkit_table::bases().find(c);
            if (b != toolkit_table::bases().end()) for (auto base : b->second) stack.push_back(base);
        }
        declaring_[key] = found;
        return found;
    }
};

}  // namespace sysml
