"""Verify every vendored input against its vendor/*/PIN.json.

Hashes are SHA-256 over newline-normalized bytes (CRLF -> LF), so the
pins are stable regardless of the platform or checkout that produced
the vendored copies. Run by CI before the regenerate-and-diff drift
check.
"""

import hashlib
import json
import sys
from pathlib import Path

VENDOR = Path(__file__).resolve().parent.parent / "vendor"


def normalized_sha256(path: Path) -> str:
    return hashlib.sha256(
        path.read_bytes().replace(b"\r\n", b"\n")
    ).hexdigest()


def main():
    bad, total = [], 0
    for pin_file in sorted(VENDOR.glob("*/PIN.json")):
        pin = json.loads(pin_file.read_text(encoding="utf-8"))
        for rel, want in pin["files"].items():
            total += 1
            got = normalized_sha256(pin_file.parent / rel)
            if got != want:
                bad.append(f"{pin_file.parent.name}/{rel}: {got} != pinned {want}")
        src = pin.get("sourceCommit", pin.get("version", "?"))
        print(f"PIN {pin_file.parent.name}: {len(pin['files'])} files @ {src[:12]}")
    if bad:
        sys.exit(
            "vendored inputs do not match their PIN.json (re-vendor "
            "deliberately and update the pin):\n  " + "\n  ".join(bad)
        )
    print(f"PIN OK: {total} vendored files match")


if __name__ == "__main__":
    main()
