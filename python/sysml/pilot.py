"""The SysML v2 Pilot Implementation backend.

The Pilot reads the model and computes every value with its own code, behind the same backend
contract as the SysML Toolkit and payload backends, so the generated classes and `Model` are
unchanged:

    with PilotBackend.open(["vehicle.sysml"]) as pilot:
        model = Model(pilot)

The Pilot is Java. It runs in this process through JPype when JPype is installed
(``pip install sysml[pilot]``), else in a Java process this backend starts and talks to over its
standard input and output; both speak the same request protocol to the SDK's Java engine (the
``sysml-pilot-server.jar`` this package carries), which finds the Pilot: ``SYSML_PILOT_HOME`` (a
Pilot jar and its ``sysml.library``), else the copy in the user's cache, else the Pilot release the
SDK is pinned to, downloaded into that cache on the first use (about 130 MB) and checked against its
pinned SHA-256. A Java 21 or newer runtime is needed: ``java`` given here, else ``JAVA_HOME``, else
``java`` on the PATH. The SDK redistributes nothing of the Pilot (Eclipse Public License 2.0).

The Pilot reads the model with its own standard library and, by default, adds the implied
relationships and implicit elements of the specification's semantics. Its answers are the
Pilot's, as the SysML Toolkit backend's are the SysML Toolkit's: the two implementations differ in
places.

The Pilot reads its standard library once per Java runtime: when a backend is closed, its model is
taken out of the Pilot instance, which then serves the next model opened with the same Pilot and
library. Through JPype every backend of this process shares the runtime, so only the first model
waits for the library (about ten seconds); with ``transport="process"`` each backend has a runtime
of its own. The Pilot's elaboration of a model can, rarely, add an element inside a library feature,
which later models then see; ``SYSML_PILOT_KEEP_LIBRARY=0`` gives every model a fresh library.

It is an `EngineBackend` (`sysml.engine`): the Pilot engine speaks the SDK's engine protocol.
Errors: a property the element's metaclass does not have reads as absent; an element no longer
known raises `Gone`; a reference the Pilot could not resolve raises `UnresolvedReference`; a
failure inside the Pilot, an operation the Pilot does not have, or a Pilot that cannot be found,
downloaded or started raises `BackendError`.
"""

from __future__ import annotations

import json
import os
import shutil
from typing import Iterable, Optional

from .base import BackendError, SdkError
from .engine import EngineBackend, EngineProcess

ENGINE = "Pilot Implementation"
SERVER_CLASS = "org.openmbee.sysml.pilot.PilotServer"
ENGINE_CLASS = "org.openmbee.sysml.pilot.PilotEngine"


def server_jar() -> str:
    """The SDK's Pilot engine: ``SYSML_PILOT_SERVER`` (a jar or a class directory), else the jar this
    package carries."""
    env = os.environ.get("SYSML_PILOT_SERVER")
    if env:
        if not os.path.exists(env):
            raise BackendError(f"SYSML_PILOT_SERVER names {env}, which does not exist")
        return env
    here = os.path.join(os.path.dirname(os.path.abspath(__file__)), "_pilot", "sysml-pilot-server.jar")
    if os.path.exists(here):
        return here
    raise BackendError(
        "this copy of the SDK carries no Pilot engine (sysml-pilot-server.jar; the release's packages "
        "do); build it with python tools/package.py pilot-server and set SYSML_PILOT_SERVER to it")


def java_executable(java: "str | os.PathLike | None" = None) -> str:
    """The ``java`` to run the Pilot with: `java`, else ``JAVA_HOME``'s, else the PATH's."""
    if java:
        return os.fspath(java)
    home = os.environ.get("JAVA_HOME")
    if home:
        for name in ("java.exe", "java"):
            candidate = os.path.join(home, "bin", name)
            if os.path.isfile(candidate):
                return candidate
    found = shutil.which("java")
    if found:
        return found
    raise BackendError("the Pilot needs a Java 21 or newer runtime: none found (set JAVA_HOME, or put java on the PATH)")


def _pilot_args(home, jar, library) -> list[str]:
    if home is not None:
        return ["--home", os.fspath(home)]
    if jar is not None or library is not None:
        if jar is None or library is None:
            raise SdkError("name both pilot_jar and pilot_library, or neither")
        return ["--jar", os.fspath(jar), "--library", os.fspath(library)]
    return []


class _InProcess:
    """The engine in this process's JVM, through JPype. JPype starts one JVM per process, with the
    Pilot engine on its class path; a JVM already started without it cannot load it."""

    def __init__(self, heap: str, pilot_args: list[str]):
        import jpype  # optional: pip install sysml[pilot]

        jar = os.path.abspath(server_jar())
        if not jpype.isJVMStarted():
            jpype.startJVM(f"-Xmx{heap}", classpath=[jar], convertStrings=True)
        try:
            engine = jpype.JClass(ENGINE_CLASS)
        except Exception:
            raise BackendError(f"the JVM in this process was started without the Pilot engine ({jar}) on its "
                               f"class path; start it with that jar, or pass transport='process'") from None
        try:
            if pilot_args[:1] == ["--home"]:
                self._engine = engine.startIn(pilot_args[1])
            elif pilot_args:
                self._engine = engine.start(pilot_args[1], pilot_args[3])
            else:
                self._engine = engine.start(None, None)
        except jpype.JException as e:
            raise BackendError(f"the Pilot is not available: {e.getMessage()}") from None
        self.engine = ENGINE
        self.version = str(self._engine.pilot().version())

    def request(self, req: dict) -> dict:
        return json.loads(str(self._engine.request(json.dumps(req))))

    def close(self) -> None:
        self._engine.close()


class PilotBackend(EngineBackend):
    """The SysML v2 Pilot Implementation behind the SDK's backend contract (see the module)."""

    @classmethod
    def open(cls, paths: Iterable["str | os.PathLike"] = (), *, sources: dict[str, str] | None = None,
             elaborate: bool = True, transport: Optional[str] = None, java: "str | os.PathLike | None" = None,
             heap: str = "4g", pilot_home: "str | os.PathLike | None" = None,
             pilot_jar: "str | os.PathLike | None" = None,
             pilot_library: "str | os.PathLike | None" = None) -> "PilotBackend":
        """Read a model: files and directories of ``.sysml`` and ``.kerml`` files, and in-memory
        `sources` (unit name to text).

        With `elaborate` (the default), the Pilot adds the implied relationships and implicit
        elements, to the model and to its standard library. `transport` is ``"jpype"`` (in this process), ``"process"`` (a Java process), or
        None: JPype when it is installed, else a process. `java` and `heap` (the JVM's maximum heap)
        apply to a process, and `heap` to the JVM JPype starts. `pilot_home`, or `pilot_jar` and
        `pilot_library` together, name a Pilot instead of the one the engine finds.
        """
        args = _pilot_args(pilot_home, pilot_jar, pilot_library)
        if transport is None:
            try:
                import jpype  # noqa: F401
                transport = "jpype"
            except ImportError:
                transport = "process"
        if transport == "jpype":
            t = _InProcess(heap, args)
        elif transport == "process":
            t = EngineProcess([java_executable(java), f"-Xmx{heap}", "-cp", server_jar(), SERVER_CLASS, *args])
        else:
            raise SdkError(f"transport is 'jpype', 'process' or None, not {transport!r}")
        backend = cls(t)
        backend._open(paths, sources, elaborate, None)
        return backend

    @property
    def pilot_version(self) -> str:
        """The version of the Pilot that reads the model."""
        return self.version
