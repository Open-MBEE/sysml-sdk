"""Print the release notes for a tag, after checking that every package has the tag's version.

Usage: python tools/release_notes.py <tag>

The tag is v<version> or v<version>-<suffix> (a rehearsal such as v0.1.0-rc1). The notes are the
section of CHANGELOG.md headed with the version. The versions checked are the ones each package
carries: pyproject.toml (the Python packages, the jar and the C++ archive take theirs from it),
ts/package.json, the C# project, and the C++ headers' sdk_version (which the standard library
helpers name).
"""

import json
import re
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from naming import CS_PROJECT  # noqa: E402


def main() -> None:
    tag = sys.argv[1]
    m = re.fullmatch(r"v(\d+\.\d+\.\d+)(-[0-9A-Za-z.]+)?", tag)
    if not m:
        sys.exit(f"tag {tag!r} is not v<major>.<minor>.<patch>[-<suffix>]")
    version = m.group(1)
    found = {
        "pyproject.toml": tomllib.loads((ROOT / "pyproject.toml").read_text(encoding="utf-8"))["project"]["version"],
        "ts/package.json": json.loads((ROOT / "ts" / "package.json").read_text(encoding="utf-8"))["version"],
    }
    [csproj] = CS_PROJECT.glob("*.csproj")
    cs = re.search(r"<Version>([^<]+)</Version>", csproj.read_text(encoding="utf-8"))
    found[csproj.relative_to(ROOT).as_posix()] = cs.group(1) if cs else None
    cpp = re.search(r'sdk_version = "([^"]+)"', (ROOT / "cpp" / "include" / "sysml" / "library.hpp").read_text(encoding="utf-8"))
    found["cpp/include/sysml/library.hpp"] = cpp.group(1) if cpp else None
    wrong = {k: v for k, v in found.items() if v != version}
    if wrong:
        sys.exit(f"tag {tag} wants version {version}; these differ: {wrong}")
    changelog = (ROOT / "CHANGELOG.md").read_text(encoding="utf-8")
    section = re.search(rf"^## {re.escape(version)}\b.*?$(.*?)(?=^## |\Z)", changelog, re.M | re.S)
    if not section:
        sys.exit(f"CHANGELOG.md has no section for {version}")
    print(section.group(1).strip())


if __name__ == "__main__":
    main()
