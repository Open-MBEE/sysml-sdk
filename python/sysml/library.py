"""The KerML and SysML standard library, in the two forms the backends read.

`standard_library()` is the directory of the library's models that a platform wheel carries, for the
SysML Toolkit backend: ``Model.from_toolkit("model.sysml", library_dir=standard_library())``.

`standard_library_json()` is the same library as full-form interchange JSON, for the payload backend
(`PayloadLibrary`). It is too large for a wheel: the first call downloads ``sysml_library-<version>.zip``
from this version's GitHub release, checks it against the SHA-256 that GitHub states for that file,
and keeps the JSON in the user's cache, where later calls find it. Nothing is downloaded unless the
function is called. ``SYSML_LIBRARY_JSON`` names a copy of the JSON to use instead, for machines
without access to GitHub.

Both are the library of the SysML v2 release the SDK was built with, licensed under the Eclipse
Public License 2.0, not the SDK's Apache-2.0: see the LICENSE and NOTICE beside them.
"""

from __future__ import annotations

import hashlib
import json
import os
import re
import shutil
import sys
import tempfile
import urllib.request
import zipfile
from pathlib import Path

from .base import SdkError

REPOSITORY = "Open-MBEE/sysml-sdk"
JSON_VARIABLE = "SYSML_LIBRARY_JSON"
CACHE_VARIABLE = "SYSML_CACHE_DIR"
_PACKAGE = Path(__file__).resolve().parent
_TIMEOUT = 60


class LibraryUnavailable(SdkError):
    """The standard library is not at hand: this installation does not carry it, or it could not be
    downloaded and checked."""


def standard_library() -> Path:
    """The directory of the standard library models a platform wheel carries, to pass as
    `library_dir`. The source distribution and a repository checkout carry none."""
    directory = _PACKAGE / "stdlib"
    if not directory.is_dir() or next((p for p in directory.rglob("*") if p.suffix in (".kerml", ".sysml")),
                                      None) is None:
        raise LibraryUnavailable(
            "this installation of the SDK carries no standard library models (only the platform "
            "wheels do); pass the sysml.library directory of sysml_library-<version>.zip from "
            f"https://github.com/{REPOSITORY}/releases as library_dir")
    return directory


def standard_library_json(*, version: str | None = None, cache_dir: str | os.PathLike | None = None) -> Path:
    """The standard library as full-form JSON, for `PayloadLibrary`: the file ``SYSML_LIBRARY_JSON``
    names, else the copy in the cache (``cache_dir``, else ``SYSML_CACHE_DIR``, else the user's cache
    directory), downloaded from the GitHub release of ``version`` (default: this SDK's) on the first
    call and checked against the SHA-256 GitHub states for it."""
    named = os.environ.get(JSON_VARIABLE)
    if named:
        if not Path(named).is_file():
            raise LibraryUnavailable(f"{JSON_VARIABLE} names {named}, which is not a file")
        return Path(named)
    version = version or _version()
    stem = f"sysml_library-{version}"
    target = Path(cache_dir) if cache_dir is not None else _cache_root()
    target = target / stem
    found = target / "sysml.library.full.json"
    if found.is_file():
        return found

    page = f"https://github.com/{REPOSITORY}/releases/tag/v{version}"
    by_hand = (f"download {stem}.zip from {page}, unpack it, and set {JSON_VARIABLE} to the "
               "sysml.library.full.json it holds")
    try:
        release = _get_json(f"https://api.github.com/repos/{REPOSITORY}/releases/tags/v{version}")
    except (OSError, ValueError) as e:
        raise LibraryUnavailable(f"cannot read the release v{version} of {REPOSITORY} ({e}); {by_hand}") from e
    asset = next((a for a in release.get("assets", []) if a.get("name") == f"{stem}.zip"), None)
    if asset is None:
        raise LibraryUnavailable(f"the release v{version} of {REPOSITORY} has no {stem}.zip ({page})")
    digest = asset.get("digest") or ""
    if not digest.startswith("sha256:"):
        raise LibraryUnavailable(f"GitHub states no SHA-256 for {stem}.zip, so it is not downloaded; {by_hand}")

    target.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(dir=target.parent) as work:
        archive = Path(work) / f"{stem}.zip"
        try:
            sha256 = _download(asset["browser_download_url"], archive)
        except OSError as e:
            raise LibraryUnavailable(f"cannot download {asset['browser_download_url']} ({e}); {by_hand}") from e
        if sha256 != digest.removeprefix("sha256:"):
            raise LibraryUnavailable(f"{stem}.zip does not match the SHA-256 GitHub states for it "
                                     f"({sha256}, not {digest.removeprefix('sha256:')}); nothing was kept")
        try:
            with zipfile.ZipFile(archive) as z:
                for name in ("sysml.library.full.json", "LICENSE", "NOTICE", "README.txt"):
                    z.extract(f"{stem}/{name}", work)
        except (KeyError, zipfile.BadZipFile) as e:
            raise LibraryUnavailable(f"{stem}.zip is not the archive the SDK expects ({e}); {by_hand}") from e
        if target.exists():
            shutil.rmtree(target)  # a copy left incomplete by an earlier call
        os.replace(Path(work) / stem, target)
    return found


def _version() -> str:
    """This SDK's version: its installed metadata, else (in a repository checkout) pyproject.toml's."""
    from importlib import metadata
    try:
        return metadata.version(__package__)
    except metadata.PackageNotFoundError:
        pyproject = _PACKAGE.parent.parent / "pyproject.toml"
        m = re.search(r'^version = "([^"]+)"', pyproject.read_text(encoding="utf-8"), re.M)
        if m is None:
            raise LibraryUnavailable("cannot tell this SDK's version; pass version=")
        return m.group(1)


def _cache_root() -> Path:
    if os.environ.get(CACHE_VARIABLE):
        return Path(os.environ[CACHE_VARIABLE])
    if sys.platform == "win32":
        return Path(os.environ.get("LOCALAPPDATA") or Path.home() / "AppData" / "Local") / "sysml" / "Cache"
    if sys.platform == "darwin":
        return Path.home() / "Library" / "Caches" / "sysml"
    return Path(os.environ.get("XDG_CACHE_HOME") or Path.home() / ".cache") / "sysml"


def _request(url: str, accept: str) -> urllib.request.Request:
    return urllib.request.Request(url, headers={"User-Agent": f"{__package__}-sdk", "Accept": accept})


def _get_json(url: str) -> dict:
    with urllib.request.urlopen(_request(url, "application/vnd.github+json"), timeout=_TIMEOUT) as response:
        return json.load(response)


def _download(url: str, path: Path) -> str:
    """Write the file at `url` to `path`; its SHA-256, as hex."""
    h = hashlib.sha256()
    with urllib.request.urlopen(_request(url, "application/octet-stream"), timeout=_TIMEOUT) as response, \
            open(path, "wb") as out:
        while chunk := response.read(1 << 20):
            h.update(chunk)
            out.write(chunk)
    return h.hexdigest()
