// The standard library helpers: the variables that name them, the cache the SDK languages share, and
// what they say when the library is not at hand. No network: C++ downloads nothing.

#include <cstdlib>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>

#include <sysml/library.hpp>

namespace fs = std::filesystem;

static int failures = 0;

static void check(bool cond, const std::string& what) {
    std::cout << (cond ? "ok: " : "FAIL: ") << what << "\n";
    if (!cond) ++failures;
}

static void set_env(const char* name, const std::string& value) {
#ifdef _WIN32
    _putenv_s(name, value.c_str());
#else
    if (value.empty()) unsetenv(name); else setenv(name, value.c_str(), 1);
#endif
}

template <class F>
static void rejects(F body, const std::string& pattern, const std::string& what) {
    try {
        body();
        check(false, what + " (no error)");
    } catch (const sysml::library_unavailable& e) {
        check(std::string(e.what()).find(pattern) != std::string::npos, what + ": " + std::string(e.what()).substr(0, 90));
    }
}

int main() {
    if (std::getenv("SYSML_LIBRARY_JSON") || std::getenv("SYSML_LIBRARY_DIR")) {
        std::cout << "skipped: SYSML_LIBRARY_JSON or SYSML_LIBRARY_DIR is set\n";
        return 0;
    }
    fs::path tmp = fs::temp_directory_path() / ("sysml-library-checks-" + std::to_string(std::rand()));
    fs::create_directories(tmp);

    // The JSON: the shared cache, then the variable.
    set_env("SYSML_CACHE_DIR", tmp.string());
    rejects([] { sysml::standard_library_json(); }, "SYSML_LIBRARY_JSON", "no JSON anywhere: says what to do");
    fs::path cached = tmp / (std::string("sysml_library-") + sysml::sdk_version) / "sysml.library.full.json";
    fs::create_directories(cached.parent_path());
    std::ofstream(cached) << "[]";
    check(fs::path(sysml::standard_library_json()) == cached, "the JSON another language downloaded into the shared cache");
    fs::path copy = tmp / "library.json";
    std::ofstream(copy) << "[]";
    set_env("SYSML_LIBRARY_JSON", copy.string());
    check(fs::path(sysml::standard_library_json()) == copy, "SYSML_LIBRARY_JSON names a copy");
    set_env("SYSML_LIBRARY_JSON", (tmp / "missing.json").string());
    rejects([] { sysml::standard_library_json(); }, "not a file", "SYSML_LIBRARY_JSON names nothing");
    set_env("SYSML_LIBRARY_JSON", "");

    // The models: the variable, then beside the executable.
    fs::path models = tmp / "models";
    fs::create_directories(models / "Kernel Libraries");
    set_env("SYSML_LIBRARY_DIR", models.string());
    rejects([] { sysml::standard_library(); }, "holds no library models", "SYSML_LIBRARY_DIR names an empty directory");
    std::ofstream(models / "Kernel Libraries" / "Base.kerml") << "standard library package Base;";
    check(fs::path(sysml::standard_library()) == models, "SYSML_LIBRARY_DIR names the models");
    set_env("SYSML_LIBRARY_DIR", "");
    try {
        std::string beside = sysml::standard_library();
        check(fs::exists(fs::path(beside) / "LICENSE"), "sysml.library beside the executable: " + beside);
    } catch (const sysml::library_unavailable& e) {
        check(std::string(e.what()).find("beside the executable") != std::string::npos,
              "no sysml.library beside the executable: says so");
    }

    set_env("SYSML_CACHE_DIR", "");
    fs::remove_all(tmp);
    if (failures) {
        std::cout << failures << " LIBRARY CHECK(S) FAILED\n";
        return 1;
    }
    std::cout << "ALL C++ LIBRARY CHECKS PASSED\n";
    return 0;
}
