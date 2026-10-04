"""The standard library helpers, offline: GitHub is replaced by a fake that serves the release
metadata and the library archive from memory."""

import hashlib
import io
import json
import urllib.error
import zipfile

import pytest

import sysml.library as L
from sysml import LibraryUnavailable, standard_library, standard_library_json

VERSION = "9.9.9"
STEM = f"sysml_library-{VERSION}"
ASSET_URL = f"https://github.com/{L.REPOSITORY}/releases/download/v{VERSION}/{STEM}.zip"
API_URL = f"https://api.github.com/repos/{L.REPOSITORY}/releases/tags/v{VERSION}"


def archive(json_text: str = '[{"@id": "lib"}]') -> bytes:
    buf = io.BytesIO()
    with zipfile.ZipFile(buf, "w") as z:
        for name, text in (("sysml.library.full.json", json_text), ("LICENSE", "EPL"), ("NOTICE", "notice"),
                           ("README.txt", "readme")):
            z.writestr(f"{STEM}/{name}", text)
    return buf.getvalue()


@pytest.fixture
def github(monkeypatch):
    """A fake GitHub: `served` maps URL to bytes (or an exception to raise); `asked` lists the URLs."""
    state = {"served": {}, "asked": []}

    def urlopen(request, timeout=None):
        url = request.full_url
        state["asked"].append(url)
        answer = state["served"].get(url, urllib.error.URLError("no route to host"))
        if isinstance(answer, Exception):
            raise answer
        return io.BytesIO(answer)

    monkeypatch.setattr(L.urllib.request, "urlopen", urlopen)
    monkeypatch.delenv(L.JSON_VARIABLE, raising=False)
    return state


def publish(github, data: bytes, digest: str | None = None) -> None:
    asset = {"name": f"{STEM}.zip", "browser_download_url": ASSET_URL}
    asset["digest"] = digest if digest is not None else "sha256:" + hashlib.sha256(data).hexdigest()
    github["served"][API_URL] = json.dumps({"assets": [asset]}).encode()
    github["served"][ASSET_URL] = data


def test_download_checks_and_caches(github, tmp_path):
    publish(github, archive())
    path = standard_library_json(version=VERSION, cache_dir=tmp_path)
    assert path == tmp_path / STEM / "sysml.library.full.json"
    assert json.loads(path.read_text(encoding="utf-8")) == [{"@id": "lib"}]
    assert sorted(p.name for p in path.parent.iterdir()) == ["LICENSE", "NOTICE", "README.txt",
                                                            "sysml.library.full.json"]
    assert sorted(p.name for p in tmp_path.iterdir()) == [STEM]    # no work files left behind
    github["served"].clear()                                       # the second call never asks GitHub
    assert standard_library_json(version=VERSION, cache_dir=tmp_path) == path
    assert github["asked"] == [API_URL, ASSET_URL]


def test_a_digest_mismatch_keeps_nothing(github, tmp_path):
    publish(github, archive(), digest="sha256:" + "0" * 64)
    with pytest.raises(LibraryUnavailable, match="does not match the SHA-256"):
        standard_library_json(version=VERSION, cache_dir=tmp_path)
    assert list(tmp_path.iterdir()) == []


def test_no_digest_no_download(github, tmp_path):
    publish(github, archive(), digest="")
    with pytest.raises(LibraryUnavailable, match="states no SHA-256"):
        standard_library_json(version=VERSION, cache_dir=tmp_path)
    assert github["asked"] == [API_URL]


def test_no_such_asset(github, tmp_path):
    github["served"][API_URL] = json.dumps({"assets": [{"name": "other.zip"}]}).encode()
    with pytest.raises(LibraryUnavailable, match=f"has no {STEM}.zip"):
        standard_library_json(version=VERSION, cache_dir=tmp_path)


def test_offline_says_what_to_do(github, tmp_path):
    with pytest.raises(LibraryUnavailable) as e:
        standard_library_json(version=VERSION, cache_dir=tmp_path)
    assert L.JSON_VARIABLE in str(e.value) and f"releases/tag/v{VERSION}" in str(e.value)


def test_not_the_expected_archive(github, tmp_path):
    buf = io.BytesIO()
    with zipfile.ZipFile(buf, "w") as z:
        z.writestr("something/else.txt", "x")
    publish(github, buf.getvalue())
    with pytest.raises(LibraryUnavailable, match="not the archive"):
        standard_library_json(version=VERSION, cache_dir=tmp_path)


def test_an_incomplete_copy_is_replaced(github, tmp_path):
    (tmp_path / STEM).mkdir()
    (tmp_path / STEM / "LICENSE").write_text("left over", encoding="utf-8")
    publish(github, archive())
    path = standard_library_json(version=VERSION, cache_dir=tmp_path)
    assert path.is_file() and (path.parent / "LICENSE").read_text(encoding="utf-8") == "EPL"


def test_the_variable_names_a_copy(github, tmp_path, monkeypatch):
    copy = tmp_path / "library.json"
    copy.write_text("[]", encoding="utf-8")
    monkeypatch.setenv(L.JSON_VARIABLE, str(copy))
    assert standard_library_json() == copy
    monkeypatch.setenv(L.JSON_VARIABLE, str(tmp_path / "missing.json"))
    with pytest.raises(LibraryUnavailable, match="not a file"):
        standard_library_json()
    assert github["asked"] == []


def test_the_cache_directory_variable(github, tmp_path, monkeypatch):
    monkeypatch.setenv(L.CACHE_VARIABLE, str(tmp_path))
    publish(github, archive())
    assert standard_library_json(version=VERSION) == tmp_path / STEM / "sysml.library.full.json"


def test_the_models_need_a_wheel(monkeypatch, tmp_path):
    monkeypatch.setattr(L, "_PACKAGE", tmp_path)          # a checkout or the sdist: no stdlib/
    with pytest.raises(LibraryUnavailable, match="only the platform wheels"):
        standard_library()
    (tmp_path / "stdlib" / "Kernel Libraries").mkdir(parents=True)
    (tmp_path / "stdlib" / "Kernel Libraries" / "Base.kerml").write_text("standard library package Base;",
                                                                        encoding="utf-8")
    assert standard_library() == tmp_path / "stdlib"


def test_the_version_comes_from_the_package():
    assert L._version() == L._version().strip() and L._version()[0].isdigit()
