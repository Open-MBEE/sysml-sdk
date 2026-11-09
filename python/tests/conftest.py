"""Make the in-repo package importable without installation."""

import os
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))


def toolkit_library():
    """Path of the binding library the SysML Toolkit backend would load, or None when it is not
    built.

    With SYSML_REQUIRE_TOOLKIT=1 a missing library is an error rather than a reason to skip, so a
    run that is meant to exercise the SysML Toolkit cannot pass by skipping it.
    """
    from sysml.toolkit import _default_library_path

    path = _default_library_path()
    if os.path.exists(path):
        return path
    if os.environ.get("SYSML_REQUIRE_TOOLKIT") == "1":
        raise RuntimeError(f"SYSML_REQUIRE_TOOLKIT=1 but the binding library is not at {path}")
    return None


ROOT = Path(__file__).resolve().parents[2]
LIBRARY_JSON = ROOT / "examples" / "sysml.library.full.json"


def example_json():
    """Whether the generated JSON of the examples is present: the SysML Toolkit's exports of the
    example models and the standard library as JSON (python tools/export_example.py).

    With SYSML_REQUIRE_EXAMPLE_JSON=1 missing files are an error rather than a reason to skip.
    """
    if LIBRARY_JSON.exists():
        return True
    if os.environ.get("SYSML_REQUIRE_EXAMPLE_JSON") == "1":
        raise RuntimeError(f"SYSML_REQUIRE_EXAMPLE_JSON=1 but {LIBRARY_JSON} is missing; "
                           f"run python tools/export_example.py")
    return False


_library = None


def payload_library():
    """The library JSON as a PayloadLibrary, read once per test session."""
    global _library
    if _library is None:
        import json

        from sysml import PayloadLibrary

        _library = PayloadLibrary(json.loads(LIBRARY_JSON.read_text(encoding="utf-8")))
    return _library


# ---------------------------------------------------------------- the Pilot Implementation

def pilot_at_hand():
    """Why the Pilot backend's live tests cannot run, or None when they can: they need a Java
    runtime and a Pilot (SYSML_PILOT_HOME, or one in the user's cache), so that the tests never
    download the Pilot by themselves.

    With SYSML_REQUIRE_PILOT=1 nothing is a reason to skip: the engine downloads the Pilot when it is
    absent, and a missing Java runtime fails the tests.
    """
    if os.environ.get("SYSML_REQUIRE_PILOT") == "1":
        return None
    from sysml.library import _cache_root
    from sysml.pilot import BackendError, java_executable

    try:
        java_executable()
    except BackendError as e:
        return str(e)
    if os.environ.get("SYSML_PILOT_HOME"):
        return None
    if any(_cache_root().glob("pilot-*/.complete")):
        return None
    return "no Pilot at hand (set SYSML_PILOT_HOME, or SYSML_REQUIRE_PILOT=1 to download it)"


def pilot_server(tmp_dir: Path) -> str:
    """The Pilot engine's jar: SYSML_PILOT_SERVER, the one an installed package carries, else built
    from the Java sources into `tmp_dir` (a JDK's javac on the PATH or under JAVA_HOME)."""
    from sysml.pilot import BackendError, server_jar

    try:
        return server_jar()
    except BackendError:
        pass
    import subprocess

    sys.path.insert(0, str(ROOT / "tools"))
    from package import pilot_server as build  # noqa: E402

    with open(os.devnull, "w") as quiet:
        saved = sys.stdout
        sys.stdout = quiet
        try:
            return str(build(tmp_dir))
        except (OSError, subprocess.CalledProcessError) as e:
            raise RuntimeError(f"cannot build the Pilot engine's jar: {e}") from None
        finally:
            sys.stdout = saved


# ---------------------------------------------------------------- expected SysML Toolkit refusals

import pytest  # noqa: E402

TOOLKIT_REFUSAL = " is not answered by the SysML Toolkit"


def _refusal_row(nodeid: str):
    from toolkit_gaps import TOOLKIT_REFUSALS

    test = nodeid.split("python/tests/")[-1]
    for pattern, row in TOOLKIT_REFUSALS.items():
        if test == pattern or (pattern.endswith("*") and test.startswith(pattern[:-1])):
            return row
    return None


def refused_member(message: str):
    """The member a SysML Toolkit refusal names ("type", "Type::inheritedMemberships()" -> the
    member "inheritedMemberships()"), or None when the message is not a SysML Toolkit refusal."""
    if TOOLKIT_REFUSAL not in message:
        return None
    head = message.split(TOOLKIT_REFUSAL)[0].strip()
    return head.split("::")[-1]


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """Apply TOOLKIT_REFUSALS (toolkit_gaps.py): a listed test that stops with exactly its listed
    SysML Toolkit refusal is an expected failure; one that passes, or stops otherwise, fails."""
    outcome = yield
    report = outcome.get_result()
    if report.when != "call":
        return
    row = _refusal_row(item.nodeid)
    if row is None:
        return
    member, reason = row
    from sysml import NotImplementedInToolkit

    if call.excinfo is None:
        report.outcome = "failed"
        report.longrepr = (f"TOOLKIT_REFUSALS expects the SysML Toolkit to refuse {member} ({reason}), "
                           f"but the test passed: the SysML Toolkit answers now; remove the row from "
                           f"toolkit_gaps.py")
    elif call.excinfo.errisinstance(NotImplementedInToolkit) and refused_member(str(call.excinfo.value)) == member:
        report.outcome = "skipped"
        report.wasxfail = f"the SysML Toolkit refuses {member} ({reason})"
