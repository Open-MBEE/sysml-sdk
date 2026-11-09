"""A backend over an engine that speaks the SDK's engine protocol (docs/engine-protocol.md).

An engine is a program that reads a model and answers the backend contract's requests: started as a
process, it exchanges one JSON request and one JSON answer per line over its standard input and
output. `EngineBackend.start` runs any such engine:

    with EngineBackend.start(["my-engine", "--serve"], ["vehicle.sysml"]) as engine:
        model = Model(engine)

The Pilot Implementation backend (`sysml.pilot`) is one, with a transport of its own besides.

Errors: a property the element's metaclass does not have reads as absent; a member the engine does
not answer raises `NotAnswered`; a reference it could not resolve raises `UnresolvedReference`; an
element no longer known raises `Gone`; a failure inside the engine, an operation the element's
metaclass does not have, or an engine that cannot start raises `BackendError`.
"""

from __future__ import annotations

import json
import os
import subprocess
import threading
from typing import Any, Iterable, Optional, Sequence

from .base import BackendError, Element, Gone, NotAnswered, Refused, SdkError, UnresolvedReference

PROTOCOL = 1


class EngineProcess:
    """An engine in a process: one JSON request per line on its input, one answer per line on its
    output. The first line it writes says whether it is ready."""

    def __init__(self, command: Sequence[str]):
        self.command = [os.fspath(c) for c in command]
        try:
            self._p = subprocess.Popen(self.command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, text=True,
                                       encoding="utf-8")
        except OSError as e:
            raise BackendError(f"cannot start the engine {self.command[0]}: {e}") from None
        self._lock = threading.Lock()
        line = self._p.stdout.readline()
        try:
            ready = json.loads(line)
        except ValueError:
            self._p.kill()
            raise BackendError(f"the engine {self.command[0]} did not start (it said {line.strip()!r})") from None
        if not ready.get("ready"):
            self._p.wait(timeout=30)
            raise BackendError(f"the engine is not available: {ready.get('error', {}).get('message', ready)}")
        if ready.get("protocol") != PROTOCOL:
            self._p.kill()
            raise BackendError(f"the engine speaks protocol {ready.get('protocol')}, this SDK {PROTOCOL}")
        self.engine: str = ready.get("engine", "the engine")
        self.version: str = ready.get("version", "")

    def request(self, req: dict) -> dict:
        with self._lock:
            if self._p.poll() is not None:
                raise BackendError(f"the {self.engine} process has ended (status {self._p.returncode})")
            self._p.stdin.write(json.dumps(req) + "\n")
            self._p.stdin.flush()
            line = self._p.stdout.readline()
        if not line:
            raise BackendError(f"the {self.engine} process ended while answering (status {self._p.wait(timeout=30)})")
        return json.loads(line)

    def close(self) -> None:
        if self._p.poll() is None:
            try:
                self.request({"id": 0, "cmd": "close"})
                self._p.wait(timeout=30)
            except (BackendError, OSError, ValueError, subprocess.TimeoutExpired):
                self._p.kill()
        for stream in (self._p.stdin, self._p.stdout):
            try:
                stream.close()
            except OSError:
                pass


class _NotApplicable(SdkError):
    pass


class EngineBackend:
    """The backend contract over an engine transport: an object with `request(dict) -> dict`,
    `close()`, and the `engine` and `version` it announced."""

    def __init__(self, transport):
        self._t = transport
        self.engine: str = transport.engine
        self.version: str = transport.version
        self._next = 0
        self.stats: dict = {}

    @classmethod
    def start(cls, command: Sequence[str], paths: Iterable["str | os.PathLike"] = (), *,
              sources: dict[str, str] | None = None, elaborate: bool = True,
              library: "str | os.PathLike | None" = None) -> "EngineBackend":
        """Start the engine `command` and read a model with it: files and directories of ``.sysml``
        and ``.kerml`` files, and in-memory `sources` (unit name to text). `elaborate` asks for the
        implied relationships; `library` names a standard library directory (default: the engine's
        own)."""
        backend = cls(EngineProcess(command))
        backend._open(paths, sources, elaborate, library)
        return backend

    def _open(self, paths, sources, elaborate, library) -> None:
        try:
            self.stats = self._ask("open", paths=[os.path.abspath(os.fspath(p)) for p in paths],
                                   sources=dict(sources or {}), elaborate=bool(elaborate),
                                   library=None if library is None else os.fspath(library))
        except BaseException:
            self.close()
            raise

    def close(self) -> None:
        if self._t is not None:
            self._t.close()
            self._t = None

    def __enter__(self):
        return self

    def __exit__(self, *exc) -> None:
        self.close()

    def __del__(self):
        try:
            self.close()
        except Exception:
            pass

    def _ask(self, cmd: str, **kw) -> Any:
        if self._t is None:
            raise SdkError(f"this {self.engine} backend is closed")
        self._next += 1
        answer = self._t.request({"id": self._next, "cmd": cmd, **kw})
        err = answer.get("error")
        if err is None:
            return answer.get("ok")
        kind, msg = err.get("kind"), err.get("message", "")
        if kind == "gone":
            raise Gone(msg)
        if kind == "notApplicable":
            raise _NotApplicable(msg)
        if kind == "refused":
            raise NotAnswered(f"{self.engine}: {msg}")
        if kind == "invalid":
            raise Refused(f"{self.engine}: {msg}")
        if kind == "unresolved":
            raise UnresolvedReference(f"{self.engine}: {msg}")
        if kind == "badRequest":
            raise SdkError(msg)
        raise BackendError(f"{self.engine}: {msg}")

    # ---------------------------------------------------------------- the backend contract

    def get(self, handle: str, prop: str) -> Any:
        try:
            return self._ask("get", h=handle, prop=prop)
        except _NotApplicable:
            return None  # not a member of this metaclass: absent

    def read(self, handle: str, prop) -> Any:
        return self.get(handle, prop.name)

    def call(self, handle: str, op, args: tuple) -> Any:
        try:
            return self._ask("call", h=handle, op=op.name, args=[_argument(a) for a in args])
        except _NotApplicable as e:
            raise BackendError(f"{self.engine}: {e}") from None

    def find(self, handle: str, member, args: list) -> Any:
        """A convenience member, by the protocol's `find`: its name and its arguments by name."""
        try:
            return self._ask("find", h=handle, member=member.name, args=_named(member, args))
        except _NotApplicable as e:
            raise BackendError(f"{self.engine}: {e}") from None

    def find_each(self, handles: list, member, args: list) -> list:
        """A convenience member on each element, by the protocol's `findEach`: one request."""
        try:
            return self._ask("findEach", hs=list(handles), member=member.name, args=_named(member, args))
        except _NotApplicable as e:
            raise BackendError(f"{self.engine}: {e}") from None

    def metaclass(self, handle: str) -> str:
        return self._ask("metaclass", h=handle)

    def element_id(self, handle: str) -> str:
        return self._ask("elementId", h=handle) or ""

    def resolve(self, qualified_name: str) -> Optional[str]:
        return self._ask("resolve", name=qualified_name)

    def all_handles(self) -> Iterable[str]:
        return self._ask("allHandles")


def _named(member, args: list) -> dict:
    """A convenience member's arguments by parameter name, as the protocol passes them."""
    return {pname: _argument(v.value if hasattr(v, "value") and not isinstance(v, Element) else v)
            for (pname, _, _), v in zip(member.params, args)}


def _argument(a: Any) -> Any:
    if isinstance(a, Element):
        return {"@id": a.backend_handle}
    if isinstance(a, (list, tuple)):
        return [_argument(x) for x in a]
    return a
