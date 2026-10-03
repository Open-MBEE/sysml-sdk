"""Package the standard library, as JSON and as its models, as a release archive.

Usage: python tools/package_library.py <sysml.library.full.json> --library-dir DIR [--out DIR]

<sysml.library.full.json> is the output of abi/examples/export_library.rs (python
tools/export_example.py writes it to examples/sysml.library.full.json); DIR is the sysml.library
directory it was made from, inside a git checkout of the SysML v2 release. The archive,
sysml_library-<version>.zip, holds one directory with the JSON (for the payload backend), the
library models themselves as released (sysml.library/: the .kerml and .sysml files the release
tracks, with their project metadata, for the SysML Toolkit backend), the Eclipse Public License
2.0 under which both are licensed, a NOTICE that says where the library's source is and who holds
its copyright (read from the release's README), and a README on what each part is for. The
version is the SDK's (pyproject.toml); the SysML Toolkit release comes from
vendor/sysml-toolkit/PIN.json.

The JSON is a Modified Work of the library models in the sense of the EPL 2.0 and is distributed
under it, not under the SDK's Apache-2.0; the models are distributed unmodified. Section 3.1 asks
that they be accompanied by a statement that the Source Code is available under the EPL and how to
obtain it, and section 3.3 that the models' notices be kept: the NOTICE carries both. The tool stops
rather than write an archive without them.
"""

from __future__ import annotations

import argparse
import json
import re
import subprocess
import sys
import tomllib
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

NOTICE = """The KerML and SysML standard library, as its models and as full-form interchange JSON

sysml.library/ holds the standard library models of the SysML v2 release named below, unmodified:
the .kerml and .sysml files the release tracks in its directory sysml.library, with their project
metadata.

sysml.library.full.json is a Modified Work of those models (the directory sysml.library) of the
SysML v2 release

  {release}, {url}
  commit {release_commit}

produced from those models by the SysML Toolkit, with sysml-sdk {version}:

  {repository} {tag}
  commit {commit}

The elements are the models' own, under the element ids KerML 9.1 and SysML 9.1 prescribe, with the
properties the full form derives, and with annotations that mark the references the SysML Toolkit
could not resolve.

Both are licensed under the Eclipse Public License 2.0, as the library models are; see LICENSE. They
are not covered by the SDK's Apache-2.0 license.

Source Code: the library models are available under the Eclipse Public License 2.0 at
{url}/tree/{release_commit}/sysml.library

The release licenses its models under the EPL 2.0 by the copyright holders it lists:

{holders}
"""

README = """sysml_library {version}

The KerML and SysML standard library for the SysML v2 SDK, in the two forms its backends read:

  sysml.library/            the library models themselves, for the SysML Toolkit backend: pass
                            this directory as the library directory when the session opens
                            (library_dir in Python, libraryDir in JavaScript, the last argument
                            of ToolkitBackend.open in Java, C# and C++)
  sysml.library.full.json   the same library as full-form interchange JSON, for the payload
                            backend: a payload model's references into the standard library
                            resolve against it; read it once as a PayloadLibrary and pass it to
                            Model.from_full_json (the same in every SDK language)

Every element of the JSON carries the element id that KerML 9.1 and SysML 9.1 prescribe for the
library, the one other tools use too.

Library: the SysML v2 release {release} (commit {release_commit}), library version {library_version}.
The JSON was written by the SysML Toolkit {repository} at {tag} (commit {commit}), with
sysml-sdk {version}.

In the JSON, the inheritance-aware properties hold each element's own side: inheritedMembership,
inheritedFeature, importedMembership and featuringType are empty, and feature, membership and the
like list owned members only. References inside the library that do not resolve carry the
SysML Toolkit's recovery annotations (a TextualRepresentation in the language
x-sysmlv2-unresolved-reference).

License: Eclipse Public License 2.0, see LICENSE; the source and the copyright holders are in NOTICE.
"""


def git(directory: Path, *args: str) -> str:
    r = subprocess.run(["git", "-C", str(directory), *args], capture_output=True, text=True)
    if r.returncode != 0 or not r.stdout.strip():
        sys.exit(f"git {' '.join(args)} in {directory} failed; the NOTICE needs it:\n{r.stderr}")
    return r.stdout.strip()


def copyright_holders(readme: str) -> list[str]:
    """The copyright lines the release's README gives for its EPL-licensed models."""
    marker = "licensed by the respective copyright holders listed below"
    start = readme.find(marker)
    if start < 0:
        sys.exit(f"the release README no longer says {marker!r}; check its licensing section by hand")
    holders = []
    for line in readme[start:].splitlines()[1:]:
        line = re.sub(r"<br\s*/?>", "", line).strip()
        if line.startswith("Copyright"):
            holders.append(line)
        elif holders and line:
            break
    if not holders:
        sys.exit("no copyright lines follow the licensing statement of the release README")
    return holders


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("json", type=Path)
    ap.add_argument("--library-dir", type=Path, required=True)
    ap.add_argument("--out", type=Path, default=ROOT / "dist")
    a = ap.parse_args()

    version = tomllib.loads((ROOT / "pyproject.toml").read_text(encoding="utf-8"))["project"]["version"]
    toolkit = json.loads((ROOT / "vendor" / "sysml-toolkit" / "PIN.json").read_text(encoding="utf-8"))["toolkit"]
    release = a.library_dir.resolve().parent
    project = json.loads((a.library_dir / "Systems Library" / ".project.json").read_text(encoding="utf-8"))
    fields = dict(
        version=version, release=git(release, "describe", "--tags", "--always"),
        release_commit=git(release, "rev-parse", "HEAD"),
        url=git(release, "remote", "get-url", "origin").removesuffix(".git"),
        library_version=project["version"],
        repository=toolkit["repository"], tag=toolkit["tag"], commit=toolkit["commit"])
    holders = copyright_holders((release / "README.md").read_text(encoding="utf-8"))
    notice = NOTICE.format(**fields, holders="\n".join(holders))

    name = f"sysml_library-{version}"
    a.out.mkdir(parents=True, exist_ok=True)
    archive = a.out / f"{name}.zip"
    # The models as the release tracks them; its IDE files (.project, .settings, .gitignore) stay out.
    library = a.library_dir.resolve()
    models = [p for p in git(release, "ls-files", "-z", "--", library.name).split("\0") if p]
    models = [p for p in models if p.endswith((".kerml", ".sysml", ".project.json", ".meta.json"))]
    if not any(p.endswith((".kerml", ".sysml")) for p in models):
        sys.exit(f"no library models tracked under {library}")
    with zipfile.ZipFile(archive, "w", zipfile.ZIP_DEFLATED, compresslevel=9) as z:
        z.write(a.json, f"{name}/sysml.library.full.json")
        for rel in sorted(models):
            z.write(release / rel, f"{name}/{rel}")
        z.write(release / "LICENSE", f"{name}/LICENSE")
        z.writestr(f"{name}/NOTICE", notice)
        z.writestr(f"{name}/README.txt", README.format(**fields))
    print(f"wrote {archive} ({archive.stat().st_size} bytes)")


if __name__ == "__main__":
    main()
