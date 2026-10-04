// The KerML and SysML standard library, in the two forms the backends read.
//
// sysml::standard_library() is the directory of the library's models, for the SysML Toolkit
// backend: ToolkitBackend::open({"model.sysml"}, {}, sysml::standard_library()). The release archive
// carries them in sysml.library/: copy that directory beside your executable (as you do the binding
// library), or name it in SYSML_LIBRARY_DIR.
//
// sysml::standard_library_json() is the same library as full-form interchange JSON, for the payload
// backend (PayloadLibrary). C++ downloads nothing: it is the file SYSML_LIBRARY_JSON names, else the
// copy in the user's cache that the other SDK languages download (sysml_library-<version>/ in the
// cache directory). Get it once with any of them, or download sysml_library-<version>.zip from the
// release and set SYSML_LIBRARY_JSON to the sysml.library.full.json it holds.
//
// Both are the library of the SysML v2 release the SDK was built with, licensed under the Eclipse
// Public License 2.0, not the SDK's Apache-2.0: see the LICENSE and NOTICE beside them.

#pragma once

#include <cstdlib>
#include <filesystem>
#include <string>
#include <system_error>

#include "toolkit.hpp"

namespace sysml {

/// This SDK's version, whose release the standard library helpers name.
inline constexpr const char* sdk_version = "0.1.0";

/// The standard library is not at hand: no models beside the executable, or no JSON.
class library_unavailable final : public sdk_error {
public:
    using sdk_error::sdk_error;
};

namespace detail {

inline std::string environment(const char* name) {
    const char* v = std::getenv(name);
    return v ? v : "";
}

/// The cache directory the SDK languages share: SYSML_CACHE_DIR, else the user's cache directory.
inline std::filesystem::path library_cache_root() {
    namespace fs = std::filesystem;
    if (std::string env = environment("SYSML_CACHE_DIR"); !env.empty()) return env;
#ifdef _WIN32
    std::string local = environment("LOCALAPPDATA");
    fs::path base = !local.empty() ? fs::path(local) : fs::path(environment("USERPROFILE")) / "AppData" / "Local";
    return base / "sysml" / "Cache";
#elif defined(__APPLE__)
    return fs::path(environment("HOME")) / "Library" / "Caches" / "sysml";
#else
    std::string xdg = environment("XDG_CACHE_HOME");
    return (!xdg.empty() ? fs::path(xdg) : fs::path(environment("HOME")) / ".cache") / "sysml";
#endif
}

inline bool holds_models(const std::filesystem::path& dir) {
    namespace fs = std::filesystem;
    std::error_code ec;
    if (!fs::is_directory(dir, ec)) return false;
    for (fs::recursive_directory_iterator it(dir, ec), end; !ec && it != end; it.increment(ec)) {
        auto ext = it->path().extension();
        if (ext == ".sysml" || ext == ".kerml") return true;
    }
    return false;
}

}  // namespace detail

/// The directory of the standard library models, for `library_dir`: SYSML_LIBRARY_DIR, else
/// sysml.library beside the running executable.
inline std::string standard_library() {
    namespace fs = std::filesystem;
    if (std::string named = detail::environment("SYSML_LIBRARY_DIR"); !named.empty()) {
        if (!detail::holds_models(named))
            throw library_unavailable("SYSML_LIBRARY_DIR names " + named + ", which holds no library models");
        return named;
    }
    fs::path beside = fs::path(ToolkitBackend::Library::executable_dir()) / "sysml.library";
    if (detail::holds_models(beside)) return beside.string();
    throw library_unavailable(std::string("no standard library models beside the executable: copy the sysml.library "
        "directory of the release's sysml-sdk-cpp-") + sdk_version + ".zip there, or set SYSML_LIBRARY_DIR to it");
}

/// The standard library as full-form JSON, for PayloadLibrary: the file SYSML_LIBRARY_JSON names,
/// else the copy in the cache the SDK languages share (which C++ reads but does not download).
inline std::string standard_library_json() {
    namespace fs = std::filesystem;
    std::error_code ec;
    if (std::string named = detail::environment("SYSML_LIBRARY_JSON"); !named.empty()) {
        if (!fs::is_regular_file(named, ec))
            throw library_unavailable("SYSML_LIBRARY_JSON names " + named + ", which is not a file");
        return named;
    }
    std::string stem = std::string("sysml_library-") + sdk_version;
    fs::path cached = detail::library_cache_root() / stem / "sysml.library.full.json";
    if (fs::is_regular_file(cached, ec)) return cached.string();
    throw library_unavailable("no standard library JSON: download " + stem + ".zip from https://github.com/Open-MBEE/"
        "sysml-sdk/releases/tag/v" + sdk_version + ", unpack it, and set SYSML_LIBRARY_JSON to the "
        "sysml.library.full.json it holds (or let another SDK language download it once into " +
        cached.parent_path().string() + ")");
}

}  // namespace sysml
