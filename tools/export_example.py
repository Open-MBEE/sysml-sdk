"""Generate the JSON the examples read on their payload path: each example model exported by the
SysML Toolkit, beside the model, and the standard library as JSON. None of these files is
committed.

Usage: python tools/export_example.py --library <sysml.library directory> [--skip-library-json]

Writes examples/vehicle.full.json, examples/queries/models/pumps.full.json and drones.full.json, and
examples/sysml.library.full.json.

The exports are the SysML Toolkit's own full-form emitter at the closure level: the
inheritance-aware properties carry the specification's values, inherited members included. The
models are loaded with the standard library, so their references into it carry the library's
normative element ids, and a payload model reads them from the library JSON (PayloadLibrary). Each
model is loaded under its file name, so the element ids are the same on every machine.

The library JSON comes from abi/examples/export_library.rs, run with cargo. The exports need the
binding library (abi/target/release, or SYSMLV2_ABI).
"""

import argparse
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT / "python"))

from sysml.toolkit import ToolkitBackend  # noqa: E402

MODELS = [
    ROOT / "examples" / "vehicle.sysml",
    ROOT / "examples" / "queries" / "models" / "pumps.sysml",
    ROOT / "examples" / "queries" / "models" / "drones.sysml",
]
LIBRARY_JSON = ROOT / "examples" / "sysml.library.full.json"


def export(model: Path, library: str) -> None:
    out = model.with_suffix(".full.json")
    be = ToolkitBackend.open(sources={model.name: model.read_text(encoding="utf-8")}, library_dir=library)
    try:
        text = be.full_json(closures=True)
    finally:
        be.close()
    out.write_text(text + "\n", encoding="utf-8", newline="\n")
    print(f"wrote {out.relative_to(ROOT).as_posix()} ({len(text)} bytes)")


def export_library(library: str) -> None:
    # cargo runs in abi/, so a path relative to where this was started must be made absolute first.
    subprocess.run(["cargo", "run", "--release", "--locked", "--quiet", "--example", "export_library", "--",
                    str(Path(library).resolve()), str(LIBRARY_JSON)], cwd=ROOT / "abi", check=True)
    print(f"wrote {LIBRARY_JSON.relative_to(ROOT).as_posix()} ({LIBRARY_JSON.stat().st_size} bytes)")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    parser.add_argument("--library", required=True, help="the standard library's directory (sysml.library)")
    parser.add_argument("--skip-library-json", action="store_true", help="write the model exports only")
    args = parser.parse_args()
    for model in MODELS:
        export(model, args.library)
    if not args.skip_library_json:
        export_library(args.library)


if __name__ == "__main__":
    main()
