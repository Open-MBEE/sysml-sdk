# sysmlv2-abi — the binding library

The C ABI over the OpenMBEE SysML Toolkit: one exported function per specification member, read
only. The contract is the status codes, the result structs and their ownership, as `src/lib.rs`
documents them; this directory is its implementation. Every SDK language reaches the SysML Toolkit
through this library and nothing else.

## Layout

| Path | What | Who writes it |
|---|---|---|
| `src/lib.rs` | sessions, the handle table, result buffers, diagnostics, versioning, the JSON export | by hand |
| `src/read.rs` | one generic property reader per result shape, over the SysML Toolkit's checked reader (`ResolvedModel::property`) | by hand |
| `src/ops.rs` | the specification *operations*, through the SysML Toolkit's operation entry point (`ResolvedModel::invoke_operation`), by declaration | by hand |
| `src/generated.rs` | one `extern "C"` wrapper per naming-table row | `tools/gen_abi.py`; never edit |
| `examples/export_library.rs` | the standard library as full-form JSON (`tools/export_example.py` runs it) | by hand |
| `smoke.py` | end-to-end check from Python through `ctypes`, including the refusal statuses | by hand |

Consumers: `python/sysml/toolkit.py` (ctypes),
`java/src/org/openmbee/sysml/ToolkitBackend.java` (foreign function API),
`ts/toolkit.mjs` (the WebAssembly build), `csharp/OpenMBEE.SysML/ToolkitBackend.cs` (P/Invoke)
and `cpp/include/sysml/toolkit.hpp` (run-time loading).

Every function first checks that the element's metaclass has the member, and answers
`NOT_APPLICABLE` when it does not. Every property row then goes through `read.rs` by its
specification name, so no property is hand-written: the SysML Toolkit's checked reader reads it,
owned or derived, following the metamodel's redefinitions, and answers only what it can derive
completely. Where it cannot, the wrapper returns `NOT_IMPLEMENTED` with the SysML Toolkit's reason.
Every operation row goes through `ops.rs`: the operation's declaration is found in the
SysML Toolkit's catalog, the arguments are converted to the parameter types it declares, and the
SysML Toolkit runs it or refuses it, again with `NOT_IMPLEMENTED` and the reason. A reference that
does not resolve, or points at an element the session has not loaded, is reported with
`UNRESOLVED`. Sessions open under the SysML Toolkit's full closure policy, so inherited members are
derived as the specification defines them, where the SysML Toolkit can.

## Building

The SysML Toolkit is a **sibling checkout** at `../../sysml-toolkit`. Releases of this library are
built and tested against the SysML Toolkit release pinned in `vendor/sysml-toolkit/PIN.json` under
`toolkit`: the public repository `Open-MBEE/sysml-toolkit` at the tag and commit recorded there. A
clone of that tag beside this repository, under the repository's own name, satisfies the path
dependency unchanged; continuous integration arranges its checkouts that way. A later SysML Toolkit
commit works as long as it keeps the checked reader (`ResolvedModel::property`) and the operation
entry point (`ResolvedModel::invoke_operation`), but only the pinned tag is tested. Nothing in the
SysML Toolkit repository is modified.

```sh
cd abi && cargo build --release                  # on Windows, the MSVC or the GNU toolchain
# -> target/release/sysmlv2_abi.dll  (.so / .dylib elsewhere)
python smoke.py ../examples/vehicle.sysml
```

The same crate builds for WebAssembly, which is how JavaScript reaches the SysML Toolkit in both
Node and the browser (no shared libraries there):

```sh
rustup target add wasm32-unknown-unknown          # once
cargo build --release --target wasm32-unknown-unknown
# -> target/wasm32-unknown-unknown/release/sysmlv2_abi.wasm  (~1.8 MB, the SysML Toolkit included)
```

The module needs three imports from wasm-bindgen glue that a dependency of the SysML Toolkit pulls
in; the JavaScript core satisfies them with no-op stubs. Pointers are 4 bytes there, so the result
structs have different offsets from the native build; `ts/toolkit.mjs` carries both layouts.
Callers write input strings into the module's memory through `sysmlv2_alloc` and release them with
`sysmlv2_dealloc`.

Regenerating the wrappers needs Python:

```sh
python tools/gen_abi.py            # reads vendor/naming-table/*.csv and metamodel.json, writes src/generated.rs
```

`generated.rs` is committed, so the crate builds without Python. Continuous integration builds the
crate on every platform it releases for, with the pinned SysML Toolkit release checked out beside
the SDK.

## Moving to a newer SysML Toolkit release

The SysML Toolkit pin is one entry; everything else follows from it.

1. Update `toolkit.tag` and `toolkit.commit` in `vendor/sysml-toolkit/PIN.json`. Continuous
   integration reads the tag from there, so the workflow needs no edit.
2. If the SysML Toolkit's specification inputs changed (the two XMI files and the two property
   tables listed under `files`), re-vendor them, update their hashes and `sourceCommit`, rerun
   `tools/extract_metamodel.py` with a table version bump, and rerun the generators.
3. Run `cargo update` here when the SysML Toolkit's own dependencies moved (the lock file is
   committed).
4. Build, then run the whole test matrix and the Annex A regression test
   (`python/tests/test_annex_a.py`).
5. Answers that changed because the SysML Toolkit now computes more are expected: update
   `python/tests/toolkit_gaps.py`, the only place a test records what the SysML Toolkit does not yet
   answer. `python tools/toolkit_figures.py` measures the Annex A reads by outcome and the
   operations the SysML Toolkit runs, and with `--write` rewrites those figures in
   `toolkit_gaps.py`, `STATUS.md` and `CHANGELOG.md`. Regenerate the example JSON with
   `python tools/export_example.py --library <sysml.library>`, and run the payload paths on it.
6. Update the known limitations in `CHANGELOG.md`.

## The handle table

`ElementRef` has a crate-private field, so this library cannot rebuild one from an integer it
receives from C. Each session therefore keeps a table of the element references it has handed
out, and a handle is an index into that table. The lookup is bounds-checked, which is where
`INVALID_HANDLE` comes from. This is also the natural place for a generation or session tag
when the SysML Toolkit settles identity across edits: the ABI signatures do not change.

## Threading

A session is single-threaded. Many SysML Toolkit accessors take a mutable receiver to fill lazy
caches; the library does not lock. Use one session per thread.

## Moving this into the SysML Toolkit later

Kept deliberately easy. The crate is self-contained, the generated code is committed, and the
only external dependencies are the two SysML Toolkit crates by path. To move it: copy this
directory to `crates/sysmlv2-abi` in the SysML Toolkit, set `version.workspace = true`, shorten the
two dependency paths to `../sysmlv2-model` and `../sysmlv2-transform`, and add it to the workspace
members. The generator can stay here or travel with it.
