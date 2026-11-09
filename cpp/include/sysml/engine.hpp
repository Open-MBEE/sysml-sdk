// A backend over an engine that speaks the SDK's engine protocol (docs/engine-protocol.md), for C++:
// the engine runs as a process and exchanges one JSON request and one JSON answer per line over its
// standard input and output. EngineBackend::start runs any such engine; the Pilot Implementation
// backend (pilot.hpp) is one.
//
// The Backend contract hands out pointers to JSON nodes that must stay valid while the backend
// lives, so every value read is kept in a per-backend cache keyed by handle and property.
//
// Errors: a property the element's metaclass does not have reads as absent; a member the engine does
// not answer throws not_answered; a reference it could not resolve throws unresolved_reference; an
// element no longer known throws gone; a failure inside the engine, an operation the element's
// metaclass does not have, or an engine that cannot start throws backend_error.
#pragma once

#include <chrono>
#include <cstdio>
#include <cstdlib>
#include <filesystem>
#include <memory>
#include <string>
#include <string_view>
#include <thread>
#include <unordered_map>
#include <utility>
#include <vector>

#include "sdk.hpp"
#include "toolkit.hpp"  // ToolkitBackend::Library::executable_dir, and the platform headers

#ifndef _WIN32
#include <csignal>
#include <spawn.h>
#include <sys/socket.h>
#include <sys/wait.h>
extern char** environ;
#endif

namespace sysml {
namespace detail {

/// One way to reach an engine: a request line in, an answer line out, and the engine's name and
/// version as it announced them.
class EngineTransport {
public:
    virtual ~EngineTransport() = default;
    virtual std::string request(const std::string& line) = 0;
    virtual void close() = 0;
    std::string engine = "the engine", version;
};

inline std::string engine_env(const char* name) {
    const char* v = std::getenv(name);
    return v ? std::string(v) : std::string();
}

inline bool engine_file_exists(const std::string& path) {
    if (FILE* f = std::fopen(path.c_str(), "rb")) {
        std::fclose(f);
        return true;
    }
    return false;
}

/// Check the engine's first line, and take its name and version.
inline void engine_ready(const std::string& line, const std::string& program, EngineTransport& t) {
    nlohmann::json ready;
    try {
        ready = nlohmann::json::parse(line);
    } catch (const nlohmann::json::exception&) {
        throw backend_error("the engine " + program + " did not start (it said '" + line + "')");
    }
    if (!ready.value("ready", false)) {
        std::string why = ready.contains("error") ? ready["error"].value("message", line) : line;
        throw backend_error("the engine is not available: " + why);
    }
    if (ready.value("protocol", 0) != 1) throw backend_error("the engine speaks another protocol than this SDK (1)");
    t.engine = ready.value("engine", std::string("the engine"));
    t.version = ready.value("version", std::string());
}

#ifdef _WIN32

inline std::wstring engine_wide(const std::string& s) {
    if (s.empty()) return std::wstring();
    int n = MultiByteToWideChar(CP_UTF8, 0, s.data(), static_cast<int>(s.size()), nullptr, 0);
    std::wstring w(static_cast<std::size_t>(n), L'\0');
    MultiByteToWideChar(CP_UTF8, 0, s.data(), static_cast<int>(s.size()), w.data(), n);
    return w;
}

/// One argument of a Windows command line, quoted as CommandLineToArgvW reads it.
inline std::wstring engine_quote(const std::wstring& arg) {
    if (!arg.empty() && arg.find_first_of(L" \t\n\v\"") == std::wstring::npos) return arg;
    std::wstring out = L"\"";
    std::size_t backslashes = 0;
    for (wchar_t c : arg) {
        if (c == L'\\') {
            ++backslashes;
        } else if (c == L'"') {
            out.append(backslashes * 2 + 1, L'\\');
            out += c;
            backslashes = 0;
        } else {
            out.append(backslashes, L'\\');
            out += c;
            backslashes = 0;
        }
    }
    out.append(backslashes * 2, L'\\');
    return out + L"\"";
}

/// The engine in a process.
class EngineProcess final : public EngineTransport {
public:
    explicit EngineProcess(const std::vector<std::string>& command) {
        SECURITY_ATTRIBUTES sa{sizeof(sa), nullptr, TRUE};
        HANDLE child_in = nullptr, child_out = nullptr;
        if (!CreatePipe(&out_, &child_out, &sa, 0) || !CreatePipe(&child_in, &in_, &sa, 0))
            throw backend_error("cannot create the pipes to the engine's process");
        SetHandleInformation(out_, HANDLE_FLAG_INHERIT, 0);
        SetHandleInformation(in_, HANDLE_FLAG_INHERIT, 0);
        std::wstring cmd = engine_quote(engine_wide(command[0]));
        for (std::size_t i = 1; i < command.size(); ++i) cmd += L" " + engine_quote(engine_wide(command[i]));
        STARTUPINFOW si{};
        si.cb = sizeof(si);
        si.dwFlags = STARTF_USESTDHANDLES;
        si.hStdInput = child_in;
        si.hStdOutput = child_out;
        si.hStdError = GetStdHandle(STD_ERROR_HANDLE);
        PROCESS_INFORMATION pi{};
        BOOL ok = CreateProcessW(nullptr, cmd.data(), nullptr, nullptr, TRUE, CREATE_NO_WINDOW, nullptr, nullptr, &si, &pi);
        CloseHandle(child_in);
        CloseHandle(child_out);
        if (!ok) {
            CloseHandle(in_);
            CloseHandle(out_);
            throw backend_error("cannot start the engine " + command[0] + " (error " + std::to_string(GetLastError()) + ")");
        }
        CloseHandle(pi.hThread);
        process_ = pi.hProcess;
        engine_ready(read_line(), command[0], *this);
    }
    ~EngineProcess() override { close(); }

    std::string request(const std::string& line) override {
        std::string out = line + "\n";
        for (std::size_t done = 0; done < out.size();) {
            DWORD n = 0;
            if (!WriteFile(in_, out.data() + done, static_cast<DWORD>(out.size() - done), &n, nullptr))
                throw backend_error("the engine's process has ended");
            done += n;
        }
        return read_line();
    }

    void close() override {
        if (!process_) return;
        CloseHandle(in_);  // the end of its input: the engine stops
        if (WaitForSingleObject(process_, 30000) != WAIT_OBJECT_0) TerminateProcess(process_, 1);
        CloseHandle(out_);
        CloseHandle(process_);
        process_ = nullptr;
    }

private:
    HANDLE in_ = nullptr, out_ = nullptr, process_ = nullptr;
    std::string buffer_;

    std::string read_line() {
        for (;;) {
            auto nl = buffer_.find('\n');
            if (nl != std::string::npos) {
                std::string line = buffer_.substr(0, nl);
                buffer_.erase(0, nl + 1);
                if (!line.empty() && line.back() == '\r') line.pop_back();
                return line;
            }
            char chunk[65536];
            DWORD n = 0;
            if (!ReadFile(out_, chunk, sizeof chunk, &n, nullptr) || n == 0)
                throw backend_error("the engine's process ended while answering");
            buffer_.append(chunk, n);
        }
    }
};

#else  // POSIX

/// The engine in a process. Its input is a socket, so that writing to a process that has
/// ended fails with an error instead of raising SIGPIPE.
class EngineProcess final : public EngineTransport {
public:
    explicit EngineProcess(const std::vector<std::string>& command) {
        int in[2], out[2];
        if (socketpair(AF_UNIX, SOCK_STREAM, 0, in) != 0) throw backend_error("cannot create the socket to the engine's process");
        if (pipe(out) != 0) {
            ::close(in[0]);
            ::close(in[1]);
            throw backend_error("cannot create the pipe from the engine's process");
        }
#ifdef SO_NOSIGPIPE
        int one = 1;
        setsockopt(in[0], SOL_SOCKET, SO_NOSIGPIPE, &one, sizeof one);
#endif
        posix_spawn_file_actions_t fa;
        posix_spawn_file_actions_init(&fa);
        posix_spawn_file_actions_adddup2(&fa, in[1], 0);
        posix_spawn_file_actions_adddup2(&fa, out[1], 1);
        posix_spawn_file_actions_addclose(&fa, in[0]);
        posix_spawn_file_actions_addclose(&fa, out[0]);
        std::vector<std::string> all(command);
        std::vector<char*> argv;
        for (auto& a : all) argv.push_back(a.data());
        argv.push_back(nullptr);
        int st = posix_spawnp(&pid_, command[0].c_str(), &fa, nullptr, argv.data(), environ);
        posix_spawn_file_actions_destroy(&fa);
        ::close(in[1]);
        ::close(out[1]);
        in_ = in[0];
        out_ = out[0];
        if (st != 0) {
            ::close(in_);
            ::close(out_);
            pid_ = 0;
            throw backend_error("cannot start the engine " + command[0]);
        }
        std::string first;
        try {
            first = read_line();
        } catch (const backend_error&) {
            close();
            throw backend_error("cannot start the engine " + command[0]);
        }
        engine_ready(first, command[0], *this);
    }
    ~EngineProcess() override { close(); }

    std::string request(const std::string& line) override {
        std::string out = line + "\n";
#ifdef MSG_NOSIGNAL
        const int flags = MSG_NOSIGNAL;
#else
        const int flags = 0;
#endif
        for (std::size_t done = 0; done < out.size();) {
            ssize_t n = send(in_, out.data() + done, out.size() - done, flags);
            if (n <= 0) throw backend_error("the engine's process has ended");
            done += static_cast<std::size_t>(n);
        }
        return read_line();
    }

    void close() override {
        if (!pid_) return;
        ::close(in_);  // the end of its input: the engine stops
        int status = 0;
        for (int i = 0; i < 300 && waitpid(pid_, &status, WNOHANG) == 0; ++i)
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        if (waitpid(pid_, &status, WNOHANG) == 0) {
            kill(pid_, SIGKILL);
            waitpid(pid_, &status, 0);
        }
        ::close(out_);
        pid_ = 0;
    }

private:
    int in_ = -1, out_ = -1;
    pid_t pid_ = 0;
    std::string buffer_;

    std::string read_line() {
        for (;;) {
            auto nl = buffer_.find('\n');
            if (nl != std::string::npos) {
                std::string line = buffer_.substr(0, nl);
                buffer_.erase(0, nl + 1);
                return line;
            }
            char chunk[65536];
            ssize_t n = read(out_, chunk, sizeof chunk);
            if (n <= 0) throw backend_error("the engine's process ended while answering");
            buffer_.append(chunk, static_cast<std::size_t>(n));
        }
    }
};

#endif

}  // namespace detail

/// An engine behind the backend contract. Not thread-safe.
class EngineBackend : public Backend {
public:
    /// Start the engine `command` (the program and its arguments) and read a model with it: files
    /// and directories of .sysml and .kerml files, and in-memory sources (unit name to text).
    /// `elaborate` asks for the implied relationships; `library` names a standard library directory
    /// (empty: the engine's own).
    static std::unique_ptr<EngineBackend> start(const std::vector<std::string>& command,
                                                const std::vector<std::string>& paths = {},
                                                const std::vector<std::pair<std::string, std::string>>& sources = {},
                                                bool elaborate = true, const std::string& library = "") {
        std::unique_ptr<EngineBackend> b(new EngineBackend(std::make_unique<detail::EngineProcess>(command)));
        b->open_model(paths, sources, elaborate, library);
        return b;
    }

    ~EngineBackend() override { close(); }

    /// The engine's name and version, as it announced them.
    const std::string& engine() const { return t_->engine; }
    const std::string& version() const { return t_->version; }

    /// What the engine said about opening the model (engine-specific).
    const nlohmann::json& stats() const { return stats_; }

    /// Stop the engine (and its process).
    void close() {
        if (!t_ || closed_) return;
        closed_ = true;
        try {
            ask_raw("close", nlohmann::json::object());
        } catch (const sdk_error&) {
            // ended already
        }
        t_->close();
    }

    // -- the backend contract ---------------------------------------------------------------

    const nlohmann::json* get(const std::string& handle, std::string_view prop) const override {
        const std::string key = handle + "|" + std::string(prop);
        auto it = values_.find(key);
        if (it == values_.end()) {
            nlohmann::json v;
            try {
                v = ask("get", {{"h", handle}, {"prop", std::string(prop)}});
            } catch (const not_applicable&) {
                v = nullptr;  // not a member of this metaclass: absent
            }
            it = values_.emplace(key, std::move(v)).first;
        }
        return it->second.is_null() ? nullptr : &it->second;
    }

    std::string metaclass(const std::string& handle) const override {
        auto it = metaclass_.find(handle);
        if (it != metaclass_.end()) return it->second;
        std::string mc = ask("metaclass", {{"h", handle}}).get<std::string>();
        metaclass_[handle] = mc;
        return mc;
    }

    std::string element_id(const std::string& handle) const override {
        nlohmann::json v = ask("elementId", {{"h", handle}});
        return v.is_string() ? v.get<std::string>() : "";
    }

    std::optional<std::string> resolve(std::string_view qname) const override {
        nlohmann::json v = ask("resolve", {{"name", std::string(qname)}});
        if (!v.is_string()) return std::nullopt;
        return v.get<std::string>();
    }

    std::vector<std::string> all_handles() const override {
        return ask("allHandles", nlohmann::json::object()).get<std::vector<std::string>>();
    }

    /// A specification operation, evaluated by the engine. Elements are passed as references, lists
    /// element by element.
    nlohmann::json call(const std::string& handle, std::string_view op, const nlohmann::json& args) const override {
        try {
            return ask("call", {{"h", handle}, {"op", std::string(op)}, {"args", args}});
        } catch (const not_applicable& e) {
            throw backend_error(t_->engine + ": " + e.what());
        }
    }

    /// A convenience member, by the protocol's `find`: its name and its arguments by name.
    nlohmann::json find(const std::string& handle, const Find& member, const nlohmann::json& args) const override {
        nlohmann::json named = nlohmann::json::object();
        for (std::size_t i = 0; i < member.param_names.size(); ++i) named[member.param_names[i]] = args[i];
        try {
            return ask("find", {{"h", handle}, {"member", std::string(member.name)}, {"args", named}});
        } catch (const not_applicable& e) {
            throw backend_error(t_->engine + ": " + e.what());
        }
    }

    /// A convenience member on each element, by the protocol's `findEach`: one request.
    nlohmann::json find_each(const std::vector<std::string>& handles, const Find& member,
                             const nlohmann::json& args) const override {
        nlohmann::json named = nlohmann::json::object();
        for (std::size_t i = 0; i < member.param_names.size(); ++i) named[member.param_names[i]] = args[i];
        try {
            return ask("findEach", {{"hs", handles}, {"member", std::string(member.name)}, {"args", named}});
        } catch (const not_applicable& e) {
            throw backend_error(t_->engine + ": " + e.what());
        }
    }

protected:
    explicit EngineBackend(std::unique_ptr<detail::EngineTransport> t) : t_(std::move(t)) {}

    /// The `open` request: read the model.
    void open_model(const std::vector<std::string>& paths, const std::vector<std::pair<std::string, std::string>>& sources,
                    bool elaborate, const std::string& library) {
        nlohmann::json absolute = nlohmann::json::array();
        for (const auto& p : paths) absolute.push_back(std::filesystem::absolute(std::filesystem::u8path(p)).u8string());
        nlohmann::json src = nlohmann::json::object();
        for (const auto& [name, text] : sources) src[name] = text;
        stats_ = ask("open", {{"paths", absolute}, {"sources", src}, {"elaborate", elaborate},
                              {"library", library.empty() ? nlohmann::json(nullptr) : nlohmann::json(library)}});
    }

private:
    class not_applicable final : public sdk_error {
    public:
        using sdk_error::sdk_error;
    };

    std::unique_ptr<detail::EngineTransport> t_;
    bool closed_ = false;
    mutable long next_ = 0;
    nlohmann::json stats_;
    mutable std::unordered_map<std::string, nlohmann::json> values_;
    mutable std::unordered_map<std::string, std::string> metaclass_;

    nlohmann::json ask_raw(const std::string& cmd, nlohmann::json fields) const {
        fields["id"] = ++next_;
        fields["cmd"] = cmd;
        // ensure_ascii: the request crosses as ASCII, whichever way the engine is reached.
        nlohmann::json answer = nlohmann::json::parse(t_->request(fields.dump(-1, ' ', true)));
        if (!answer.contains("error")) return answer.contains("ok") ? answer["ok"] : nlohmann::json(nullptr);
        const auto& error = answer["error"];
        const std::string kind = error.value("kind", ""), message = error.value("message", "");
        if (kind == "gone") throw gone(message);
        if (kind == "notApplicable") throw not_applicable(message);
        if (kind == "refused") throw not_answered(t_->engine + ": " + message);
        if (kind == "invalid") throw refused(t_->engine + ": " + message);
        if (kind == "unresolved") throw unresolved_reference(t_->engine + ": " + message);
        if (kind == "badRequest") throw sdk_error(message);
        throw backend_error(t_->engine + ": " + message);
    }

    nlohmann::json ask(const std::string& cmd, nlohmann::json fields) const {
        if (closed_) throw sdk_error("this " + t_->engine + " backend is closed");
        return ask_raw(cmd, std::move(fields));
    }
};

}  // namespace sysml
