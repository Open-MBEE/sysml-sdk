"""Shared helpers of the convenience reference (tools/gen_reference.py) and its outcomes run
(tools/convenience_outcomes.py)."""
from __future__ import annotations

import hashlib
import json


def example_id(e: dict) -> str:
    """A stable id for an example: its member, model, element, path and arguments."""
    key = json.dumps({k: e.get(k) for k in ("member", "model", "at", "via", "args")}, sort_keys=True,
                     ensure_ascii=False)
    return "x" + hashlib.sha1(key.encode("utf-8")).hexdigest()[:10]
