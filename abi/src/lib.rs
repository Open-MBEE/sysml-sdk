//! C ABI over the SysML Toolkit, read-only.
//!
//! One exported function per specification member (see `generated.rs`, produced by
//! `tools/gen_abi.py` from the naming table), plus this hand-written core: sessions, the
//! handle table, result buffers, diagnostics and versioning. Every member function answers
//! `NOT_APPLICABLE` for an element whose metaclass does not have the member.
//!
//! Handles. `ElementRef` has a crate-private field, so this library cannot rebuild one from an
//! integer it receives from C. Every session therefore keeps a table of the `ElementRef`s it
//! has handed out; a handle is an index into that table. Looking a handle up is bounds-checked,
//! which is how `INVALID_HANDLE` is detected today. When the SysML Toolkit settles identity
//! across edits, this table is where a generation or session tag is added.

#![allow(clippy::missing_safety_doc)]

use std::collections::HashMap;
use std::ffi::c_void;
use std::path::PathBuf;

use sysmlv2_model::full::{resolved_to_full_json, EmissionPolicy};
use sysmlv2_model::json::{metaclass_conforms, ClosurePolicy, ElementRef, ResolvedModel};
use sysmlv2_transform::{Library, Session as ToolkitSession};

pub mod ops;
pub mod generated;
pub mod read;

pub const ABI_VERSION: u32 = 1;
/// SHA-256 of the naming table this build was generated from; set by the generator.
pub use generated::NAMES_DIGEST;

// ---------------------------------------------------------------- status and results

#[repr(C)]
#[derive(Clone, Copy, PartialEq, Eq, Debug)]
pub enum Status {
    Ok = 0,
    NotImplemented = 1,
    NotApplicable = 2,
    InvalidHandle = 3,
    InvalidArg = 4,
    LoadFailed = 5,
    Internal = 6,
    /// A reference in the model did not resolve; the message carries its spelling.
    Unresolved = 7,
}

pub type Handle = u64;

#[repr(C)]
pub struct Handles {
    pub items: *const Handle,
    pub len: usize,
    pub owner: *mut c_void,
}

#[repr(C)]
pub struct Str {
    pub ptr: *const u8,
    pub len: usize,
    pub present: bool,
    pub owner: *mut c_void,
}

#[repr(C)]
pub struct Bool {
    pub present: bool,
    pub value: bool,
}

#[repr(C)]
pub struct Int {
    pub present: bool,
    pub value: i64,
}

#[repr(C)]
pub struct Real {
    pub present: bool,
    pub value: f64,
}

#[repr(C)]
pub struct Ref {
    pub present: bool,
    pub value: Handle,
}

impl Handles {
    pub const EMPTY: Handles = Handles { items: std::ptr::null(), len: 0, owner: std::ptr::null_mut() };
}
impl Str {
    pub const ABSENT: Str = Str { ptr: std::ptr::null(), len: 0, present: false, owner: std::ptr::null_mut() };
}
impl Bool {
    pub const ABSENT: Bool = Bool { present: false, value: false };
}
impl Int {
    pub const ABSENT: Int = Int { present: false, value: 0 };
}
impl Real {
    pub const ABSENT: Real = Real { present: false, value: 0.0 };
}
impl Ref {
    pub const ABSENT: Ref = Ref { present: false, value: 0 };
}

/// Release a buffer handed out by any function in this library. Null is a no-op.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_free(owner: *mut c_void) {
    if owner.is_null() {
        return;
    }
    // Every allocation token is a leaked `Box<Vec<u8>>` or `Box<Vec<Handle>>`; both are
    // dropped through the same erased pointer via the length-prefixed layout below.
    drop(Box::from_raw(owner as *mut Owned));
}

/// The one allocation shape behind every `owner` token. The payload is only ever held so
/// that dropping the box frees it; nothing reads it back.
#[allow(dead_code)]
enum Owned {
    Bytes(Vec<u8>),
    Handles(Vec<Handle>),
}

fn own_bytes(v: Vec<u8>) -> (*const u8, usize, *mut c_void) {
    let ptr = v.as_ptr();
    let len = v.len();
    let owner = Box::into_raw(Box::new(Owned::Bytes(v))) as *mut c_void;
    (ptr, len, owner)
}

fn own_handles(v: Vec<Handle>) -> Handles {
    let items = v.as_ptr();
    let len = v.len();
    let owner = Box::into_raw(Box::new(Owned::Handles(v))) as *mut c_void;
    Handles { items, len, owner }
}

pub fn str_of<S: AsRef<str>>(s: S) -> Str {
    let (ptr, len, owner) = own_bytes(s.as_ref().as_bytes().to_vec());
    Str { ptr, len, present: true, owner }
}

pub fn str_opt<S: AsRef<str>>(s: Option<S>) -> Str {
    match s {
        Some(s) => str_of(s),
        None => Str::ABSENT,
    }
}

// ---------------------------------------------------------------- session

pub struct Session {
    inner: ToolkitSession,
    handles: Vec<ElementRef>,
    index: HashMap<ElementRef, Handle>,
    last_error: String,
}

impl Session {
    pub fn model(&mut self) -> &mut ResolvedModel {
        self.inner.resolved()
    }

    /// The handle for an element, minting one on first sight.
    pub fn mint(&mut self, e: ElementRef) -> Handle {
        if let Some(h) = self.index.get(&e) {
            return *h;
        }
        let h = self.handles.len() as Handle;
        self.handles.push(e);
        self.index.insert(e, h);
        h
    }

    pub fn mint_all(&mut self, es: Vec<ElementRef>) -> Handles {
        let v: Vec<Handle> = es.into_iter().map(|e| self.mint(e)).collect();
        own_handles(v)
    }

    pub fn mint_opt(&mut self, e: Option<ElementRef>) -> Ref {
        match e {
            Some(e) => Ref { present: true, value: self.mint(e) },
            None => Ref::ABSENT,
        }
    }

    /// Resolve a handle, or record the error and report `InvalidHandle`.
    pub fn elem(&mut self, h: Handle) -> Result<ElementRef, Status> {
        match self.handles.get(h as usize) {
            Some(e) => Ok(*e),
            None => {
                self.last_error = format!("invalid handle {h}: this session minted {} handles", self.handles.len());
                Err(Status::InvalidHandle)
            }
        }
    }

    pub fn fail(&mut self, status: Status, msg: impl Into<String>) -> Status {
        self.last_error = msg.into();
        status
    }
}

/// Load options: either file paths or in-memory `(unit name, text)` sources, plus an optional
/// standard-library directory. Strings are UTF-8 with explicit lengths.
#[repr(C)]
pub struct LoadOptions {
    pub paths: *const Str,
    pub paths_len: usize,
    pub source_names: *const Str,
    pub source_texts: *const Str,
    pub sources_len: usize,
    pub library_dir: Str,
}

pub(crate) unsafe fn read_str(s: &Str) -> Result<String, Status> {
    if s.ptr.is_null() {
        return Ok(String::new());
    }
    let bytes = std::slice::from_raw_parts(s.ptr, s.len);
    String::from_utf8(bytes.to_vec()).map_err(|_| Status::InvalidArg)
}

/// Whether `e`'s metaclass has the members of `class`; otherwise records why and gives the status
/// to return. Every generated member function calls this first.
pub fn applicable(s: &mut Session, e: ElementRef, class: &str, member: &str) -> Result<(), Status> {
    let mc = s.model().element_type(e).to_string();
    if metaclass_conforms(&mc, class) {
        Ok(())
    } else {
        Err(s.fail(Status::NotApplicable, format!("{class}::{member} does not apply to a {mc}")))
    }
}

/// Open a session. On failure `*out` is null and the return code says why; there is no session
/// to ask for the message, so it is written to stderr in that one case.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_open(opts: *const LoadOptions, out: *mut *mut Session) -> Status {
    open_session(opts, None, out)
}

/// Open a session whose standard library is given as in-memory `(unit name, text)` sources, for
/// hosts without a file system (the WebAssembly build): `library_len` units, in load order. The
/// library is a library, as if read from a directory: its elements are not among the user
/// elements and report `isLibraryElement`. `opts.library_dir` is ignored.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_open_with_library_sources(
    opts: *const LoadOptions,
    library_names: *const Str,
    library_texts: *const Str,
    library_len: usize,
    out: *mut *mut Session,
) -> Status {
    if library_len > 0 && (library_names.is_null() || library_texts.is_null()) {
        return Status::InvalidArg;
    }
    let units = match read_units(library_names, library_texts, library_len) {
        Ok(u) => u,
        Err(st) => return st,
    };
    open_session(opts, Some(Library::sources(units)), out)
}

unsafe fn read_units(names: *const Str, texts: *const Str, len: usize) -> Result<Vec<(String, String)>, Status> {
    let mut units = Vec::with_capacity(len);
    for i in 0..len {
        units.push((read_str(&*names.add(i))?, read_str(&*texts.add(i))?));
    }
    Ok(units)
}

unsafe fn open_session(opts: *const LoadOptions, library: Option<Library>, out: *mut *mut Session) -> Status {
    if opts.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    *out = std::ptr::null_mut();
    let o = &*opts;

    let inner = if o.sources_len > 0 {
        let sources = match read_units(o.source_names, o.source_texts, o.sources_len) {
            Ok(u) => u,
            Err(st) => return st,
        };
        // One build resolves the sources against the library together.
        ToolkitSession::from_sources_with_library(sources, library.clone())
    } else {
        let mut paths = Vec::with_capacity(o.paths_len);
        for i in 0..o.paths_len {
            match read_str(&*o.paths.add(i)) {
                Ok(p) => paths.push(PathBuf::from(p)),
                Err(st) => return st,
            }
        }
        if let Some(missing) = paths.iter().find(|p| !p.is_file()) {
            eprintln!("sysmlv2-abi: model file not found: {}", missing.display());
            return Status::LoadFailed;
        }
        ToolkitSession::open(&paths).and_then(|mut s| match library.clone() {
            Some(lib) => s.load_library_from(lib).map(|_| s),
            None => Ok(s),
        })
    };
    let mut inner = match inner {
        Ok(s) => s,
        Err(e) => {
            eprintln!("sysmlv2-abi: load failed: {e}");
            return Status::LoadFailed;
        }
    };
    if library.is_none() && o.library_dir.ptr != std::ptr::null() && o.library_dir.len > 0 {
        let dir = match read_str(&o.library_dir) { Ok(s) => s, Err(st) => return st };
        let dir = std::path::Path::new(&dir);
        if !dir.is_dir() {
            eprintln!("sysmlv2-abi: library directory not found: {}", dir.display());
            return Status::LoadFailed;
        }
        if let Err(e) = inner.load_library(dir) {
            eprintln!("sysmlv2-abi: library load failed ({}): {e}", dir.display());
            return Status::LoadFailed;
        }
    }
    // Answer derived properties as the specification defines them, over the inheritance and
    // import closures and the implied library heritage, rather than the owned side only.
    inner.resolved().set_closure_policy(ClosurePolicy::Closure { include_implied: true });
    let session = Box::new(Session { inner, handles: Vec::new(), index: HashMap::new(), last_error: String::new() });
    *out = Box::into_raw(session);
    Status::Ok
}

#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_close(s: *mut Session) {
    if !s.is_null() {
        drop(Box::from_raw(s));
    }
}

/// The user-unit document roots: elements with no owner, libraries excluded.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_roots(s: *mut Session, out: *mut Handles) -> Status {
    if s.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let all: Vec<ElementRef> = s.model().user_elements().collect();
    let mut roots = Vec::new();
    for e in all {
        if s.model().owner(e).is_none() {
            roots.push(e);
        }
    }
    *out = s.mint_all(roots);
    Status::Ok
}

/// Human-readable message for the last non-OK status on this session.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_last_error(s: *mut Session, out: *mut Str) -> Status {
    if s.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    *out = str_of(&(*s).last_error);
    Status::Ok
}

// ---------------------------------------------------------------- helpers the SDK needs

/// The element a `::`-separated qualified name (quoted segments allowed) names, looked up from
/// the model's root namespace; absent when nothing has that name. This is the SDK's own lookup,
/// not the specification operation `Namespace::resolve`, which answers a Membership relative to
/// a namespace.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_resolve(s: *mut Session, name: *const Str, out: *mut Ref) -> Status {
    if s.is_null() || name.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let name = match read_str(&*name) {
        Ok(n) => n,
        Err(st) => return s.fail(st, "qualified name is not UTF-8"),
    };
    let r = s.model().resolve_qualified(&name);
    *out = s.mint_opt(r);
    Status::Ok
}

#[no_mangle]
pub unsafe extern "C" fn sysmlv2_metaclass(s: *mut Session, h: Handle, out: *mut Str) -> Status {
    if s.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let e = match s.elem(h) { Ok(e) => e, Err(st) => return st };
    *out = str_of(s.model().element_type(e));
    Status::Ok
}

#[no_mangle]
pub unsafe extern "C" fn sysmlv2_all_of_metaclass(s: *mut Session, mc: *const u8, mc_len: usize, out: *mut Handles) -> Status {
    if s.is_null() || out.is_null() || mc.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let name = match std::str::from_utf8(std::slice::from_raw_parts(mc, mc_len)) {
        Ok(n) => n.to_string(),
        Err(_) => return s.fail(Status::InvalidArg, "metaclass name is not UTF-8"),
    };
    let es = s.model().elements_of_metaclass(&name);
    *out = s.mint_all(es);
    Status::Ok
}

#[no_mangle]
pub unsafe extern "C" fn sysmlv2_user_elements(s: *mut Session, out: *mut Handles) -> Status {
    if s.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let es: Vec<ElementRef> = s.model().user_elements().collect();
    *out = s.mint_all(es);
    Status::Ok
}

// ---------------------------------------------------------------- interchange

/// The session's model as full-form interchange JSON, from the SysML Toolkit's own emitter.
///
/// `level` 1 writes the specification's values of the inheritance-aware properties, over the
/// inherited and imported memberships and the implied library heritage, and the four closure
/// names (`inheritedMembership`, `inheritedFeature`, `importedMembership`, `featuringType`).
/// `level` 0 writes their owned side only and leaves the closure names empty, as the
/// SysML Toolkit's command-line export does. Unresolved references carry the SysML Toolkit's
/// recovery annotations at either level.
#[no_mangle]
pub unsafe extern "C" fn sysmlv2_session_full_json(s: *mut Session, level: u32, out: *mut Str) -> Status {
    if s.is_null() || out.is_null() {
        return Status::InvalidArg;
    }
    let s = &mut *s;
    let value = match level {
        0 => s.inner.to_full_json(),
        1 => {
            if s.inner.has_explicit_ids() {
                return s.fail(Status::NotImplemented, "a closure-level export of a model loaded with explicit ids");
            }
            // The session's resolved model is borrowed apart from its model, so the emission
            // resolves a second time; the ids agree, since both come from the same model.
            let result = {
                let model = s.inner.model();
                let mut resolved = ResolvedModel::build(model);
                let policy = EmissionPolicy { closures: ClosurePolicy::Closure { include_implied: true }, ..Default::default() };
                resolved_to_full_json(&mut resolved, model, policy)
            };
            match result {
                Ok(v) => v,
                Err(e) => return s.fail(Status::Internal, e.to_string()),
            }
        }
        _ => return s.fail(Status::InvalidArg, format!("export level {level}: 0 for the owned side, 1 for the closures")),
    };
    *out = str_of(value.to_string());
    Status::Ok
}

// ---------------------------------------------------------------- caller-side buffers

/// Allocate `size` bytes the caller may write into, for example to pass source text or a
/// qualified name. Needed by callers that share our address space but cannot allocate in it
/// themselves, such as JavaScript talking to the WebAssembly build. Release with
/// `sysmlv2_dealloc`; never with `sysmlv2_free`, which is for results we allocated.
#[no_mangle]
pub extern "C" fn sysmlv2_alloc(size: usize) -> *mut u8 {
    let mut v: Vec<u8> = Vec::with_capacity(size.max(1));
    let p = v.as_mut_ptr();
    std::mem::forget(v);
    p
}

#[no_mangle]
pub unsafe extern "C" fn sysmlv2_dealloc(ptr: *mut u8, size: usize) {
    if !ptr.is_null() {
        drop(Vec::from_raw_parts(ptr, 0, size.max(1)));
    }
}

// ---------------------------------------------------------------- versioning

#[no_mangle]
pub extern "C" fn sysmlv2_abi_version() -> u32 {
    ABI_VERSION
}

/// SHA-256 hex of the naming table this library was generated from. Static, never freed.
#[no_mangle]
pub extern "C" fn sysmlv2_names_digest() -> *const u8 {
    NAMES_DIGEST.as_ptr()
}

#[no_mangle]
pub extern "C" fn sysmlv2_names_digest_len() -> usize {
    NAMES_DIGEST.len()
}

#[cfg(test)]
mod tests {
    use super::*;

    /// The SysML Toolkit's metaclass hierarchy, which the applicability check relies on, agrees
    /// with the metamodel the SDK is generated from: every concrete metaclass conforms to itself
    /// and to each of its ancestors.
    #[test]
    fn toolkit_hierarchy_agrees_with_the_metamodel() {
        let mut missing = Vec::new();
        for (class, ancestors) in generated::HIERARCHY {
            for general in std::iter::once(class).chain(ancestors.iter()) {
                if !metaclass_conforms(class, general) {
                    missing.push(format!("{class} < {general}"));
                }
            }
        }
        assert!(missing.is_empty(), "the SysML Toolkit does not know: {missing:?}");
    }
}
