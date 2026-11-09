// The SysML v2 Pilot Implementation backend for C++: the Pilot reads the model and computes every
// value with its own code, behind the same Backend contract as the SysML Toolkit and payload
// backends, so the generated structs and Model are untouched.
//
// The Pilot is Java. It runs in a Java process this backend starts, an EngineBackend (engine.hpp)
// over the engine protocol; or, compiled with
// SYSML_PILOT_JNI defined and the JDK's include directories on the include path
// (-I "$JAVA_HOME/include" -I "$JAVA_HOME/include/<os>"), in this process, in a JVM loaded from
// JAVA_HOME at run time. Both run the SDK's Pilot engine (sysml-pilot-server.jar: SYSML_PILOT_SERVER,
// else beside the executable, where to copy the one the release archive carries), which finds the
// Pilot: SYSML_PILOT_HOME (a Pilot jar and its sysml.library), else the copy in the user's cache,
// else the Pilot release the SDK is pinned to, downloaded into that cache on the first use (about
// 130 MB) and checked against its pinned SHA-256. A Java 21 or newer runtime is needed. The SDK
// redistributes nothing of the Pilot (Eclipse Public License 2.0).
//
// Errors: a property the element's metaclass does not have reads as absent; an element no longer
// known throws gone; a reference the Pilot could not resolve throws unresolved_reference; a failure
// inside the Pilot, an operation the Pilot does not have, or a Pilot that cannot be found,
// downloaded or started throws backend_error.
#pragma once

#include <memory>
#include <string>
#include <utility>
#include <vector>

#include "engine.hpp"

#ifdef SYSML_PILOT_JNI
#include <jni.h>
#endif

namespace sysml {

/// How PilotBackend runs the Pilot.
struct PilotOptions {
    /// Add the implied relationships and implicit elements of the specification's semantics, to the
    /// model and to its standard library.
    bool elaborate = true;
    /// "process", "jni" (compiled with SYSML_PILOT_JNI), or empty: JNI when compiled in, else a process.
    std::string transport;
    /// The java to run a process with; empty: JAVA_HOME's, else the PATH's.
    std::string java;
    /// The JVM's maximum heap (fixed when this process's JVM starts).
    std::string heap = "4g";
    /// A directory holding a Pilot jar and its sysml.library; or a jar and its library directory
    /// (both or neither); empty: the Pilot the engine finds.
    std::string pilot_home, pilot_jar, pilot_library;
    /// The Pilot engine's jar; empty: SYSML_PILOT_SERVER, else beside the executable.
    std::string server;
};

namespace detail {

/// The Pilot engine's jar: `server`, else SYSML_PILOT_SERVER, else beside the executable.
inline std::string pilot_server_jar(const std::string& server) {
    std::string named = server.empty() ? engine_env("SYSML_PILOT_SERVER") : server;
    if (!named.empty()) {
        if (!engine_file_exists(named)) throw backend_error("the Pilot engine " + named + " does not exist");
        return named;
    }
    std::string beside = ToolkitBackend::Library::executable_dir() + "sysml-pilot-server.jar";
    if (engine_file_exists(beside)) return beside;
    throw backend_error("no Pilot engine: copy sysml-pilot-server.jar from the release archive beside the executable, "
                        "or set SYSML_PILOT_SERVER to it");
}

/// The java to run: `java`, else JAVA_HOME's, else the PATH's.
inline std::string pilot_java(const std::string& java) {
    if (!java.empty()) return java;
    std::string home = engine_env("JAVA_HOME");
    if (!home.empty()) {
#ifdef _WIN32
        std::string candidate = home + "\\bin\\java.exe";
#else
        std::string candidate = home + "/bin/java";
#endif
        if (engine_file_exists(candidate)) return candidate;
    }
    return "java";
}

inline std::vector<std::string> pilot_args(const PilotOptions& o) {
    if (!o.pilot_home.empty()) return {"--home", o.pilot_home};
    if (!o.pilot_jar.empty() || !o.pilot_library.empty()) {
        if (o.pilot_jar.empty() || o.pilot_library.empty())
            throw sdk_error("name both pilot_jar and pilot_library, or neither");
        return {"--jar", o.pilot_jar, "--library", o.pilot_library};
    }
    return {};
}

#ifdef SYSML_PILOT_JNI

/// UTF-16 (a Java string) to UTF-8.
inline std::string pilot_utf8(const jchar* s, jsize n) {
    std::string out;
    out.reserve(static_cast<std::size_t>(n));
    for (jsize i = 0; i < n; ++i) {
        std::uint32_t c = s[i];
        if (c >= 0xD800 && c < 0xDC00 && i + 1 < n && s[i + 1] >= 0xDC00 && s[i + 1] < 0xE000)
            c = 0x10000 + ((c - 0xD800) << 10) + (s[++i] - 0xDC00);
        if (c < 0x80) {
            out += static_cast<char>(c);
        } else if (c < 0x800) {
            out += static_cast<char>(0xC0 | (c >> 6));
            out += static_cast<char>(0x80 | (c & 0x3F));
        } else if (c < 0x10000) {
            out += static_cast<char>(0xE0 | (c >> 12));
            out += static_cast<char>(0x80 | ((c >> 6) & 0x3F));
            out += static_cast<char>(0x80 | (c & 0x3F));
        } else {
            out += static_cast<char>(0xF0 | (c >> 18));
            out += static_cast<char>(0x80 | ((c >> 12) & 0x3F));
            out += static_cast<char>(0x80 | ((c >> 6) & 0x3F));
            out += static_cast<char>(0x80 | (c & 0x3F));
        }
    }
    return out;
}

/// The engine in this process's JVM, loaded from JAVA_HOME and started once per process with the
/// engine's jar on its class path (a JVM cannot be started twice in one process).
class PilotJni final : public EngineTransport {
public:
    PilotJni(const std::string& jar, const std::string& heap, const std::vector<std::string>& args) {
        JNIEnv* e = env(jar, heap);
        JniFrame frame(e);
        jclass cls = e->FindClass("org/openmbee/sysml/pilot/PilotEngine");
        if (!cls) {
            e->ExceptionClear();
            throw backend_error("the JVM in this process was started without the Pilot engine (" + jar
                                + ") on its class path; pass transport \"process\"");
        }
        jobject engine;
        if (!args.empty() && args[0] == "--home") {
            jmethodID start = e->GetStaticMethodID(cls, "startIn", "(Ljava/lang/String;)Lorg/openmbee/sysml/pilot/PilotEngine;");
            engine = e->CallStaticObjectMethod(cls, start, e->NewStringUTF(args[1].c_str()));
        } else {
            jmethodID start = e->GetStaticMethodID(cls, "start", "(Ljava/lang/String;Ljava/lang/String;)Lorg/openmbee/sysml/pilot/PilotEngine;");
            engine = e->CallStaticObjectMethod(cls, start, args.empty() ? nullptr : e->NewStringUTF(args[1].c_str()),
                                               args.empty() ? nullptr : e->NewStringUTF(args[3].c_str()));
        }
        if (std::string why = pending(e); !why.empty()) throw backend_error("the Pilot is not available: " + why);
        engine_ = e->NewGlobalRef(engine);
        request_ = e->GetMethodID(cls, "request", "(Ljava/lang/String;)Ljava/lang/String;");
        close_ = e->GetMethodID(cls, "close", "()V");
        jobject pilot = e->CallObjectMethod(engine, e->GetMethodID(cls, "pilot", "()Lorg/openmbee/sysml/pilot/Pilot;"));
        jclass pilot_cls = e->GetObjectClass(pilot);
        EngineTransport::engine = "Pilot Implementation";
        version = text(e, static_cast<jstring>(e->CallObjectMethod(pilot, e->GetMethodID(pilot_cls, "version", "()Ljava/lang/String;"))));
    }
    ~PilotJni() override { close(); }

    std::string request(const std::string& line) override {
        JNIEnv* e = env("", "");
        JniFrame frame(e);
        // The request is ASCII (the backend escapes everything else), which is also modified UTF-8.
        jobject answer = e->CallObjectMethod(engine_, request_, e->NewStringUTF(line.c_str()));
        if (std::string why = pending(e); !why.empty()) throw backend_error("the Pilot engine failed: " + why);
        return text(e, static_cast<jstring>(answer));
    }

    void close() override {
        if (!engine_) return;
        JNIEnv* e = env("", "");
        e->CallVoidMethod(engine_, close_);
        e->ExceptionClear();
        e->DeleteGlobalRef(engine_);
        engine_ = nullptr;
    }

private:
    jobject engine_ = nullptr;
    jmethodID request_ = nullptr, close_ = nullptr;

    /// Local references made outside a native method live until popped.
    struct JniFrame {
        JNIEnv* e;
        explicit JniFrame(JNIEnv* env) : e(env) { e->PushLocalFrame(32); }
        ~JniFrame() { e->PopLocalFrame(nullptr); }
    };

    static std::string text(JNIEnv* e, jstring s) {
        if (!s) return "";
        const jchar* chars = e->GetStringChars(s, nullptr);
        std::string out = pilot_utf8(chars, e->GetStringLength(s));
        e->ReleaseStringChars(s, chars);
        return out;
    }

    static std::string pending(JNIEnv* e) {
        jthrowable t = e->ExceptionOccurred();
        if (!t) return "";
        e->ExceptionClear();
        jclass cls = e->GetObjectClass(t);
        auto message = static_cast<jstring>(e->CallObjectMethod(t, e->GetMethodID(cls, "getMessage", "()Ljava/lang/String;")));
        e->ExceptionClear();
        std::string out = text(e, message);
        return out.empty() ? "a Java exception" : out;
    }

    /// This thread's JNI environment, starting the JVM on the first call.
    static JNIEnv* env(const std::string& jar, const std::string& heap) {
        static JavaVM* vm = nullptr;
        if (!vm) vm = start(jar, heap);
        JNIEnv* e = nullptr;
        if (vm->GetEnv(reinterpret_cast<void**>(&e), JNI_VERSION_1_8) == JNI_EDETACHED)
            vm->AttachCurrentThread(reinterpret_cast<void**>(&e), nullptr);
        if (!e) throw backend_error("cannot attach this thread to the JVM");
        return e;
    }

    static JavaVM* start(const std::string& jar, const std::string& heap) {
        std::string home = engine_env("JAVA_HOME");
        if (home.empty()) throw backend_error("the Pilot in this process needs JAVA_HOME (a Java 21 or newer runtime)");
#ifdef _WIN32
        std::string path = home + "\\bin\\server\\jvm.dll";
        void* lib = reinterpret_cast<void*>(LoadLibraryExA(path.c_str(), nullptr, LOAD_WITH_ALTERED_SEARCH_PATH));
        auto sym = [lib](const char* name) { return reinterpret_cast<void*>(GetProcAddress(static_cast<HMODULE>(lib), name)); };
#else
#ifdef __APPLE__
        std::string path = home + "/lib/server/libjvm.dylib";
#else
        std::string path = home + "/lib/server/libjvm.so";
#endif
        void* lib = dlopen(path.c_str(), RTLD_NOW | RTLD_GLOBAL);
        auto sym = [lib](const char* name) { return dlsym(lib, name); };
#endif
        if (!lib) throw backend_error("cannot load the JVM at " + path);
        using Created = jint(JNICALL*)(JavaVM**, jsize, jsize*);
        using Create = jint(JNICALL*)(JavaVM**, void**, void*);
        auto created = reinterpret_cast<Created>(sym("JNI_GetCreatedJavaVMs"));
        auto create = reinterpret_cast<Create>(sym("JNI_CreateJavaVM"));
        if (!created || !create) throw backend_error(path + " is not a JVM");
        JavaVM* vm = nullptr;
        jsize n = 0;
        if (created(&vm, 1, &n) == JNI_OK && n > 0) return vm;  // started by someone else
        std::string class_path = "-Djava.class.path=" + jar, max_heap = "-Xmx" + heap;
        JavaVMOption options[2];
        options[0].optionString = class_path.data();
        options[0].extraInfo = nullptr;
        options[1].optionString = max_heap.data();
        options[1].extraInfo = nullptr;
        JavaVMInitArgs init{};
        init.version = JNI_VERSION_1_8;
        init.nOptions = 2;
        init.options = options;
        init.ignoreUnrecognized = JNI_FALSE;
        JNIEnv* e = nullptr;
        if (create(&vm, reinterpret_cast<void**>(&e), &init) != JNI_OK)
            throw backend_error("cannot start the JVM at " + path);
        return vm;
    }
};

#endif

}  // namespace detail

/// The SysML v2 Pilot Implementation behind the backend contract: an EngineBackend over the SDK's
/// Pilot engine. Not thread-safe.
class PilotBackend final : public EngineBackend {
public:
    /// Read a model over files and directories of .sysml and .kerml files, and in-memory sources
    /// (unit name to text). stats() then says what opening took: the Pilot's version ("pilot"),
    /// "libraryMs", "modelMs", "resources".
    static std::unique_ptr<PilotBackend> open(const std::vector<std::string>& paths,
                                              const std::vector<std::pair<std::string, std::string>>& sources = {},
                                              const PilotOptions& options = {}) {
        std::unique_ptr<detail::EngineTransport> t;
        const std::vector<std::string> pilot = detail::pilot_args(options);
        const std::string jar = detail::pilot_server_jar(options.server);
#ifdef SYSML_PILOT_JNI
        if (options.transport.empty() || options.transport == "jni") {
            t = std::make_unique<detail::PilotJni>(jar, options.heap, pilot);
        }
#else
        if (options.transport == "jni")
            throw sdk_error("transport \"jni\" needs this program compiled with SYSML_PILOT_JNI defined");
#endif
        if (!t) {
            if (!options.transport.empty() && options.transport != "process" && options.transport != "jni")
                throw sdk_error("transport is \"jni\", \"process\" or empty, not \"" + options.transport + "\"");
            std::vector<std::string> command{detail::pilot_java(options.java), "-Xmx" + options.heap, "-cp", jar,
                                             "org.openmbee.sysml.pilot.PilotServer"};
            command.insert(command.end(), pilot.begin(), pilot.end());
            try {
                t = std::make_unique<detail::EngineProcess>(command);
            } catch (const backend_error& e) {
                if (std::string(e.what()).rfind("cannot start", 0) == 0)
                    throw backend_error(std::string(e.what()) + " (the Pilot needs a Java 21 or newer runtime: set JAVA_HOME, "
                                        "or put java on the PATH)");
                throw;
            }
        }
        std::unique_ptr<PilotBackend> b(new PilotBackend(std::move(t)));
        b->open_model(paths, sources, options.elaborate, "");
        return b;
    }

    /// Read a model over in-memory sources: unit name to text.
    static std::unique_ptr<PilotBackend> open_sources(const std::vector<std::pair<std::string, std::string>>& sources,
                                                      const PilotOptions& options = {}) {
        return open({}, sources, options);
    }

    /// The version of the Pilot that reads the model.
    const std::string& pilot_version() const { return version(); }

private:
    explicit PilotBackend(std::unique_ptr<detail::EngineTransport> t) : EngineBackend(std::move(t)) {}
};

}  // namespace sysml
