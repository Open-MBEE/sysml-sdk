"""The Python SDK reading directly from the SysML Toolkit through the binding library.

Skips when the library is not built (it needs a sibling sysml-toolkit checkout and cargo).
"""

from __future__ import annotations

import os
from pathlib import Path

import pytest
from conftest import toolkit_library

ROOT = Path(__file__).resolve().parents[2]
MODEL = ROOT.parent / "sysml-shell" / "models" / "example_model.sysml"

pytestmark = pytest.mark.skipif(toolkit_library() is None, reason="binding library not built (abi/)")


@pytest.fixture(scope="module")
def model():
    from sysml.base import Model
    from sysml.toolkit import ToolkitBackend

    src = {"inline.sysml": "package Demo { part def Wheel; part def Car { part wheels : Wheel[4]; attribute mass : Real; } }"}
    be = ToolkitBackend.open(sources=src)
    yield Model(be)
    be.close()


def test_roots_and_names(model):
    from sysml.classes import Namespace, Package

    roots = model.roots
    assert roots and all(isinstance(r, Namespace) for r in roots)
    pkgs = [m for r in roots for m in r.ownedMember if isinstance(m, Package)]
    assert [p.declaredName for p in pkgs] == ["Demo"]


def test_ownership_spine(model):
    from sysml.classes import PartDefinition, PartUsage

    car = model.resolve("Demo::Car")
    assert isinstance(car, PartDefinition)
    assert car.qualifiedName == "Demo::Car"
    assert car.owner.declaredName == "Demo"
    feats = car.ownedFeature
    assert [f.declaredName for f in feats] == ["wheels", "mass"]
    wheels = feats[0]
    assert isinstance(wheels, PartUsage)
    assert wheels.isComposite is True
    assert [t.declaredName for t in wheels.type] == ["Wheel"]


def test_identity_and_caching(model):
    car = model.resolve("Demo::Car")
    again = model.resolve("Demo::Car")
    assert car is again
    assert car.element_id == again.element_id and len(car.element_id) == 36


def test_unimplemented_member_raises(model):
    from sysml.base import NotImplementedInToolkit

    car = model.resolve("Demo::Car")
    wheels = car.ownedFeature[0]
    with pytest.raises(NotImplementedInToolkit) as ei:
        _ = wheels.mayTimeVary
    assert "mayTimeVary" in str(ei.value)


def test_an_element_argument_must_be_an_element(model):
    # The binding library has no "no element"; a missing one must not become some other element.
    car = model.resolve("Demo::Car")
    with pytest.raises(TypeError, match="must be an element"):
        car.specializes(None)
    with pytest.raises(TypeError, match="must be an element"):
        car.inheritedMemberships([None], [], False)


def test_not_applicable_is_absent(model):
    pkg = model.resolve("Demo")
    assert pkg.get_raw("isComposite") is None


def test_digest_guard_message():
    from sysml import toolkit_table

    assert len(toolkit_table.NAMES_DIGEST) == 64


@pytest.mark.skipif(not MODEL.exists(), reason="shell example model not beside this repo")
def test_loads_a_file(model):
    from sysml.base import Model
    from sysml.toolkit import ToolkitBackend

    be = ToolkitBackend.open([str(MODEL)])
    try:
        m = Model(be)
        assert m.roots
    finally:
        be.close()


def test_paths_may_be_path_objects(tmp_path):
    from sysml import Model

    (tmp_path / "lib").mkdir()
    (tmp_path / "lib" / "Base.sysml").write_text("package Base { part def Thing; }", encoding="utf-8")
    (tmp_path / "model.sysml").write_text("package M { part def Car :> Base::Thing; }", encoding="utf-8")
    m = Model.from_toolkit(tmp_path / "model.sysml", library_dir=tmp_path / "lib")
    assert m.resolve("M::Car").qualifiedName == "M::Car"
    assert m.resolve("Base::Thing").qualifiedName == "Base::Thing"


def test_a_missing_path_is_named(tmp_path):
    from sysml import Model

    with pytest.raises(FileNotFoundError, match="model file not found.*absent.sysml"):
        Model.from_toolkit(tmp_path / "absent.sysml")
    (tmp_path / "model.sysml").write_text("package M;", encoding="utf-8")
    with pytest.raises(FileNotFoundError, match="library directory not found.*nolib"):
        Model.from_toolkit(tmp_path / "model.sysml", library_dir=tmp_path / "nolib")


@pytest.fixture(scope="module")
def sizes():
    from sysml import Model

    source = ("package Sizes { doc /* Größe und Wärme */ "
              "requirement def Light { doc /* Keep it light. */ } part def Last; }")
    return Model.from_toolkit(sources={"größe.sysml": source})


def test_text_beyond_ascii(sizes):
    from sysml.classes import PartDefinition

    assert isinstance(sizes.resolve("Sizes::Last"), PartDefinition)  # the source arrived whole
    assert "Größe und Wärme" in sizes.resolve("Sizes").documentation[0].body


def test_a_list_of_strings_is_a_list(sizes):
    light = sizes.resolve("Sizes::Light")
    assert isinstance(light.text, list) and len(light.text) == 1
    assert light.text[0].startswith("Keep it light.")
    assert light.get_raw("text") == light.text  # the name-based read decodes it too


def test_members_of_element_on_the_runtime_base(sizes):
    from sysml import Element

    assert "name" in vars(Element) and "owner" in vars(Element)  # declared on the base itself
    assert sizes.resolve("Sizes::Light").owner.name == "Sizes"
