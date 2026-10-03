"""The names users type to install or import the SDK agree everywhere they are declared.

`tools/naming.py` holds them (NAMES) and `tools/rename.py` changes them; this checks the files the
package managers and the build read.
"""

import json
import re
import sys
from pathlib import Path

import pytest

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools"))

import naming  # noqa: E402

tomllib = pytest.importorskip("tomllib")


def test_python_distribution_and_module():
    project = tomllib.loads((ROOT / "pyproject.toml").read_text(encoding="utf-8"))
    assert project["project"]["name"] == naming.S["python_dist"]
    assert project["tool"]["setuptools"]["packages"] == [naming.PYTHON_MODULE]
    assert (ROOT / "python" / naming.PYTHON_MODULE / "__init__.py").is_file()


def test_npm_package():
    package = json.loads((ROOT / "ts" / "package.json").read_text(encoding="utf-8"))
    assert package["name"] == naming.NPM_PACKAGE


def test_java_package_and_csharp_projects():
    assert (naming.JAVA_SRC / "Model.java").is_file()
    assert (naming.CS_PROJECT / f"{naming.CS_NAMESPACE}.csproj").is_file()
    assert (naming.CS_CHECKS_PROJECT / f"{naming.CS_NAMESPACE}.Checks.csproj").is_file()


def test_csharp_names_the_whole_namespace():
    # A type named by the namespace's last segment (`SysML.Type` inside OpenMBEE.SysML.Checks)
    # compiles only while the namespace ends in that segment; rename.py rewrites whole namespaces, so C# code names the
    # whole one (or a using alias of it), in the sources and in the documentation's examples.
    if "." not in naming.CS_NAMESPACE:
        return
    last = re.escape(naming.CS_NAMESPACE.rsplit(".", 1)[1])
    partial = re.compile(rf"(?<![\w.]){last}\.(?=[A-Z])")
    files = [p for p in ROOT.rglob("*") if p.suffix in (".cs", ".md") and p.is_file()
             and not {"bin", "obj", "node_modules", "target", ".git", "vendor"} & set(p.relative_to(ROOT).parts)]
    found = [f"{p.relative_to(ROOT)}:{n}" for p in files
             for n, line in enumerate(p.read_text(encoding="utf-8").splitlines(), 1) if partial.search(line)]
    assert not found, found


def test_skills_are_named_after_the_product():
    skills = sorted(p for p in (ROOT / "skills").iterdir() if p.is_dir())
    assert skills
    for d in skills:
        name = re.search(r"^name: *(\S+)", (d / "SKILL.md").read_text(encoding="utf-8"), re.M).group(1)
        assert name == d.name, d
        assert name.startswith(naming.S["product"] + "-"), d
