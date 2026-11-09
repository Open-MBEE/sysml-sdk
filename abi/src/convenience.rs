//! The bodies of the SDK's convenience members (the `find` members of `convenience.json`), over the
//! SysML Toolkit.
//!
//! Each body follows its member's OCL and reads the specification properties and operations that OCL
//! reads, through the same checked reader and operation entry point as the specification members
//! (`read::answer`, `ops::call`). So a member is refused with `NOT_IMPLEMENTED` and the SysML Toolkit's
//! reason exactly when the SysML Toolkit refuses something its OCL reads, and with `REFUSED` where its
//! OCL gives `invalid`. Where the OCL's `and` or `or` decides on its left operand, the right one is not
//! read, as OCL evaluates it. The generated wrappers (`generated_convenience.rs`, from
//! `tools/gen_abi_convenience.py`) check the element's metaclass, convert the arguments and call these.

use std::collections::HashSet;

use sysmlv2_model::json::{metaclass_conforms, ElementRef};

use crate::ops::{self, Arg};
use crate::read::{self, Answer};
use crate::{Session, Status, Str};

pub type R<T> = Result<T, Status>;

/// The upper bound `*` as an integer: greater than every bound.
pub const UNBOUNDED: i64 = i64::MAX;

#[derive(Clone, Copy, PartialEq, Eq)]
pub enum Scope {
    Owned,
    Inherited,
    All,
}

#[derive(Clone, Copy, PartialEq, Eq)]
pub enum Visibility {
    Any,
    Protected,
    Public,
}

/// A `Scope` argument as the wrappers receive it: the literal's index (owned, inherited, all).
pub fn scope(s: &mut Session, v: u32) -> R<Scope> {
    match v {
        0 => Ok(Scope::Owned),
        1 => Ok(Scope::Inherited),
        2 => Ok(Scope::All),
        _ => Err(s.fail(Status::InvalidArg, format!("scope {v}: 0 owned, 1 inherited, 2 all"))),
    }
}

/// A `VisibilityFilter` argument: the literal's index (any, protected, public).
pub fn visibility(s: &mut Session, v: u32) -> R<Visibility> {
    match v {
        0 => Ok(Visibility::Any),
        1 => Ok(Visibility::Protected),
        2 => Ok(Visibility::Public),
        _ => Err(s.fail(Status::InvalidArg, format!("visibility {v}: 0 any, 1 protected, 2 public"))),
    }
}

/// A literal's value (`findLiteral`), passed as tagged JSON so that every language tells an integer
/// from a real and `*` from a string: {"integer": 4}, {"real": 0.5}, {"boolean": true},
/// {"string": "front"}, {"unbounded": true}.
pub enum Literal {
    Integer(i64),
    Real(f64),
    Boolean(bool),
    String(String),
    Unbounded,
}

impl Literal {
    pub fn json(&self) -> String {
        match self {
            Literal::Integer(i) => serde_json::json!({ "integer": i }),
            Literal::Real(r) => serde_json::json!({ "real": r }),
            Literal::Boolean(b) => serde_json::json!({ "boolean": b }),
            Literal::String(t) => serde_json::json!({ "string": t }),
            Literal::Unbounded => serde_json::json!({ "unbounded": true }),
        }
        .to_string()
    }
}

/// A body's result in the shape the wrappers put out.
pub trait IntoAnswer {
    fn into_answer(self) -> Answer;
}

impl IntoAnswer for Vec<ElementRef> {
    fn into_answer(self) -> Answer {
        Answer::Elements(self)
    }
}
impl IntoAnswer for Option<ElementRef> {
    fn into_answer(self) -> Answer {
        self.map_or(Answer::Absent, Answer::Element)
    }
}
impl IntoAnswer for ElementRef {
    fn into_answer(self) -> Answer {
        Answer::Element(self)
    }
}
impl IntoAnswer for bool {
    fn into_answer(self) -> Answer {
        Answer::Flag(self)
    }
}
impl IntoAnswer for Option<i64> {
    fn into_answer(self) -> Answer {
        self.map_or(Answer::Absent, Answer::Integer)
    }
}
impl IntoAnswer for Option<f64> {
    fn into_answer(self) -> Answer {
        self.map_or(Answer::Absent, Answer::Number)
    }
}
impl IntoAnswer for Vec<String> {
    fn into_answer(self) -> Answer {
        Answer::Texts(self)
    }
}
impl IntoAnswer for Literal {
    fn into_answer(self) -> Answer {
        Answer::Text(self.json())
    }
}

/// A body's result as an answer, its failure's message led by the member's name.
pub fn answer<T: IntoAnswer>(s: &mut Session, member: &str, r: R<T>) -> R<Answer> {
    match r {
        Ok(v) => Ok(v.into_answer()),
        Err(st) => {
            s.prefix_error(member);
            Err(st)
        }
    }
}

/// Whether the element's metaclass is one of the member's contexts.
pub fn applicable(s: &mut Session, e: ElementRef, member: &str, contexts: &[&str]) -> R<()> {
    let mc = s.model().element_type(e).to_string();
    if contexts.iter().any(|c| metaclass_conforms(&mc, c)) {
        Ok(())
    } else {
        Err(s.fail(Status::NotApplicable, format!("{member} does not apply to a {mc}")))
    }
}

fn refuse<T>(s: &mut Session, why: impl Into<String>) -> R<T> {
    Err(s.fail(Status::Refused, why))
}

// ---------------------------------------------------------------- reads

fn elems(s: &mut Session, e: ElementRef, prop: &str) -> R<Vec<ElementRef>> {
    match read::answer(s, e, prop)? {
        Answer::Elements(xs) => Ok(xs),
        Answer::Element(x) => Ok(vec![x]),
        Answer::Absent => Ok(Vec::new()),
        _ => Err(s.fail(Status::Internal, format!("{prop}: elements were expected"))),
    }
}

fn elem(s: &mut Session, e: ElementRef, prop: &str) -> R<Option<ElementRef>> {
    match read::answer(s, e, prop)? {
        Answer::Element(x) => Ok(Some(x)),
        Answer::Elements(mut xs) if xs.len() <= 1 => Ok(xs.pop()),
        Answer::Absent => Ok(None),
        _ => Err(s.fail(Status::Internal, format!("{prop}: one element was expected"))),
    }
}

fn flag(s: &mut Session, e: ElementRef, prop: &str) -> R<bool> {
    match read::answer(s, e, prop)? {
        Answer::Flag(b) => Ok(b),
        Answer::Absent => Ok(false),
        _ => Err(s.fail(Status::Internal, format!("{prop}: a Boolean was expected"))),
    }
}

fn text(s: &mut Session, e: ElementRef, prop: &str) -> R<Option<String>> {
    match read::answer(s, e, prop)? {
        Answer::Text(t) => Ok(Some(t)),
        Answer::Absent => Ok(None),
        _ => Err(s.fail(Status::Internal, format!("{prop}: a text was expected"))),
    }
}

fn integer(s: &mut Session, e: ElementRef, prop: &str) -> R<Option<i64>> {
    match read::answer(s, e, prop)? {
        Answer::Integer(i) => Ok(Some(i)),
        Answer::Absent => Ok(None),
        _ => Err(s.fail(Status::Internal, format!("{prop}: an integer was expected"))),
    }
}

fn number(s: &mut Session, e: ElementRef, prop: &str) -> R<Option<f64>> {
    match read::answer(s, e, prop)? {
        Answer::Number(n) => Ok(Some(n)),
        Answer::Integer(i) => Ok(Some(i as f64)),
        Answer::Absent => Ok(None),
        _ => Err(s.fail(Status::Internal, format!("{prop}: a number was expected"))),
    }
}

fn metaclass(s: &mut Session, e: ElementRef) -> String {
    s.model().element_type(e).to_string()
}

/// `oclIsKindOf(class)`.
fn is_kind(s: &mut Session, e: ElementRef, class: &str) -> bool {
    metaclass_conforms(&metaclass(s, e), class)
}

/// `oclIsTypeOf(class)`.
fn is_type(s: &mut Session, e: ElementRef, class: &str) -> bool {
    metaclass(s, e) == class
}

fn kinds(s: &mut Session, es: Vec<ElementRef>, class: &str) -> Vec<ElementRef> {
    let mut out = Vec::with_capacity(es.len());
    for e in es {
        if is_kind(s, e, class) {
            out.push(e);
        }
    }
    out
}

fn name_of(s: &mut Session, e: ElementRef) -> R<Option<String>> {
    text(s, e, "name")
}

fn distinct(es: Vec<ElementRef>) -> Vec<ElementRef> {
    let mut seen = HashSet::new();
    es.into_iter().filter(|e| seen.insert(*e)).collect()
}

// ---------------------------------------------------------------- operations

fn str_arg(t: &str) -> Str {
    Str { ptr: t.as_ptr(), len: t.len(), present: true, owner: std::ptr::null_mut() }
}

/// `Type::specializes(general)`.
fn specializes(s: &mut Session, t: ElementRef, general: ElementRef) -> R<bool> {
    let h = s.mint(general);
    match ops::call(s, t, "Type", "specializes", &[Arg::Handle(h)])? {
        Answer::Flag(b) => Ok(b),
        Answer::Absent => Ok(false),
        _ => Err(s.fail(Status::Internal, "specializes(): a Boolean was expected")),
    }
}

/// `Feature::allRedefinedFeatures()`.
fn all_redefined_features(s: &mut Session, f: ElementRef) -> R<Vec<ElementRef>> {
    match ops::call(s, f, "Feature", "allRedefinedFeatures", &[])? {
        Answer::Elements(xs) => Ok(xs),
        Answer::Element(x) => Ok(vec![x]),
        Answer::Absent => Ok(Vec::new()),
        _ => Err(s.fail(Status::Internal, "allRedefinedFeatures(): elements were expected")),
    }
}

/// `Namespace::visibilityOf(m)`, lowercase.
fn visibility_of(s: &mut Session, ns: ElementRef, m: ElementRef) -> R<String> {
    let h = s.mint(m);
    match ops::call(s, ns, "Namespace", "visibilityOf", &[Arg::Handle(h)])? {
        Answer::Text(t) => Ok(t.to_ascii_lowercase()),
        _ => Err(s.fail(Status::Internal, "visibilityOf(): a visibility was expected")),
    }
}

/// The contract's `libraryType(qualifiedName)`: the type `resolveGlobal` finds, else none.
fn library_type(s: &mut Session, ns: ElementRef, qualified_name: &str) -> R<Option<ElementRef>> {
    let arg = str_arg(qualified_name);
    let m = match ops::call(s, ns, "Namespace", "resolveGlobal", &[Arg::Str(&arg)])? {
        Answer::Element(m) => Some(m),
        Answer::Elements(mut ms) if ms.len() <= 1 => ms.pop(),
        Answer::Absent => None,
        _ => return Err(s.fail(Status::Internal, "resolveGlobal(): a membership was expected")),
    };
    let Some(m) = m else { return Ok(None) };
    match elem(s, m, "memberElement")? {
        Some(t) if is_kind(s, t, "Type") => Ok(Some(t)),
        _ => Ok(None),
    }
}

/// The library type of a semantic member, refused when the library does not have it.
fn required_library_type(s: &mut Session, ns: ElementRef, qualified_name: &str) -> R<ElementRef> {
    match library_type(s, ns, qualified_name)? {
        Some(t) => Ok(t),
        None => refuse(s, format!("the library type {qualified_name} is not in this model's library")),
    }
}

// ---------------------------------------------------------------- helpers of the contract

/// `isStandardLibraryElement()`: over `owningNamespace` for now, as the SysML Toolkit does not implement
/// `libraryNamespace()` yet.
fn is_standard_library_element(s: &mut Session, e: ElementRef) -> R<bool> {
    if !flag(s, e, "isLibraryElement")? {
        return Ok(false);
    }
    let Some(ns) = elem(s, e, "owningNamespace")? else { return Ok(false) };
    if is_kind(s, ns, "LibraryPackage") {
        flag(s, ns, "isStandard")
    } else {
        is_standard_library_element(s, ns)
    }
}

fn keeps(visibility: Visibility, v: &str) -> bool {
    match visibility {
        Visibility::Any => true,
        Visibility::Public => v == "public",
        Visibility::Protected => v == "public" || v == "protected",
    }
}

/// The `reject` of the scope helpers: without `library`, the memberships of standard library
/// namespaces leave, unless the element is itself in the standard library.
fn drop_library(s: &mut Session, e: ElementRef, ms: Vec<ElementRef>, library: bool) -> R<Vec<ElementRef>> {
    if library || is_standard_library_element(s, e)? {
        return Ok(ms);
    }
    let mut out = Vec::with_capacity(ms.len());
    for m in ms {
        let from_library = match elem(s, m, "membershipOwningNamespace")? {
            Some(ns) => is_standard_library_element(s, ns)?,
            None => false,
        };
        if !from_library {
            out.push(m);
        }
    }
    Ok(out)
}

/// The inherited feature memberships whose owning type this type specializes
/// (`deriveTypeFeatureMembership` since KERML11-191): a feature that a supertype imports is inherited
/// by name only.
fn inherited_feature_memberships(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let all = elems(s, e, "inheritedMembership")?;
    let ms = kinds(s, all, "FeatureMembership");
    let mut out = Vec::with_capacity(ms.len());
    for m in ms {
        if let Some(owner) = elem(s, m, "owningType")? {
            if specializes(s, e, owner)? {
                out.push(m);
            }
        }
    }
    Ok(out)
}

/// `scopedFeatureMemberships(scope, visibility, library)`.
fn scoped_feature_memberships(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    let ms = match scope {
        Scope::Owned => elems(s, e, "ownedFeatureMembership")?,
        Scope::Inherited => inherited_feature_memberships(s, e)?,
        Scope::All => {
            let mut all = elems(s, e, "ownedFeatureMembership")?;
            all.extend(inherited_feature_memberships(s, e)?);
            all
        }
    };
    let mut visible = Vec::with_capacity(ms.len());
    for m in ms {
        if vis == Visibility::Any {
            visible.push(m);
            continue;
        }
        let v = text(s, m, "visibility")?.unwrap_or_default().to_ascii_lowercase();
        if keeps(vis, &v) {
            visible.push(m);
        }
    }
    drop_library(s, e, visible, library)
}

fn scoped_features(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for m in scoped_feature_memberships(s, e, scope, vis, library)? {
        if let Some(f) = elem(s, m, "ownedMemberFeature")? {
            out.push(f);
        }
    }
    Ok(out)
}

/// `scopedMemberships(scope, visibility, library)`: outside the owned scope, `membership` in the
/// backend's order.
fn scoped_memberships(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    let ms = match scope {
        Scope::Owned => elems(s, e, "ownedMembership")?,
        Scope::All => elems(s, e, "membership")?,
        Scope::Inherited => {
            let owned: HashSet<ElementRef> = elems(s, e, "ownedMembership")?.into_iter().collect();
            elems(s, e, "membership")?.into_iter().filter(|m| !owned.contains(m)).collect()
        }
    };
    let mut visible = Vec::with_capacity(ms.len());
    for m in ms {
        if vis == Visibility::Any {
            visible.push(m);
            continue;
        }
        let v = visibility_of(s, e, m)?;
        if keeps(vis, &v) {
            visible.push(m);
        }
    }
    drop_library(s, e, visible, library)
}

/// `scopedMembers(scope)`: outside the owned scope, `member` in the backend's order.
fn scoped_members(s: &mut Session, e: ElementRef, scope: Scope) -> R<Vec<ElementRef>> {
    if scope == Scope::Owned {
        return elems(s, e, "ownedMember");
    }
    // Each element once: `member` is a set.
    let all = distinct(elems(s, e, "member")?);
    if scope == Scope::All {
        return Ok(all);
    }
    let owned: HashSet<ElementRef> = elems(s, e, "ownedMember")?.into_iter().collect();
    Ok(all.into_iter().filter(|x| !owned.contains(x)).collect())
}

fn conforms_to(s: &mut Session, f: ElementRef, general: ElementRef) -> R<bool> {
    for t in elems(s, f, "type")? {
        if specializes(s, t, general)? {
            return Ok(true);
        }
    }
    Ok(false)
}

// ---------------------------------------------------------------- the families

/// Kind lists: `scopedFeatureMemberships(...).ownedMemberFeature->selectByKind(M)`.
pub fn kind_list(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool, mc: &str) -> R<Vec<ElementRef>> {
    let fs = scoped_features(s, e, scope, vis, library)?;
    Ok(kinds(s, fs, mc))
}

/// `...->select(oclIsTypeOf(X))`.
pub fn kind_exact(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool, mc: &str) -> R<Vec<ElementRef>> {
    let fs = scoped_features(s, e, scope, vis, library)?;
    Ok(fs.into_iter().filter(|f| is_type(s, *f, mc)).collect())
}

/// The features whose type conforms to the library type; refused when the library has none.
pub fn kind_semantic(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool, lib_type: &str) -> R<Vec<ElementRef>> {
    let general = required_library_type(s, e, lib_type)?;
    let fs = scoped_features(s, e, scope, vis, library)?;
    let mut out = Vec::new();
    for f in fs {
        if conforms_to(s, f, general)? {
            out.push(f);
        }
    }
    Ok(out)
}

/// `type->exists(t | t.specializes(libraryType(L)))`.
pub fn semantic_test(s: &mut Session, e: ElementRef, lib_type: &str) -> R<bool> {
    let general = required_library_type(s, e, lib_type)?;
    conforms_to(s, e, general)
}

pub fn kind_by_name(s: &mut Session, e: ElementRef, name: &str, scope: Scope, vis: Visibility, library: bool, mc: &str) -> R<Option<ElementRef>> {
    for f in kind_list(s, e, scope, vis, library, mc)? {
        if name_of(s, f)?.as_deref() == Some(name) {
            return Ok(Some(f));
        }
    }
    Ok(None)
}

/// Member lists: `scopedMemberships(...).memberElement->selectByKind(M)`, each once.
pub fn member_list(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool, mc: &str) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for m in scoped_memberships(s, e, scope, vis, library)? {
        if let Some(x) = elem(s, m, "memberElement")? {
            if is_kind(s, x, mc) {
                out.push(x);
            }
        }
    }
    Ok(distinct(out))
}

/// The member under `name` (a membership's `memberName`, an alias's included).
pub fn member_by_name(s: &mut Session, e: ElementRef, name: &str, scope: Scope, vis: Visibility, library: bool, mc: &str) -> R<Option<ElementRef>> {
    for m in scoped_memberships(s, e, scope, vis, library)? {
        if text(s, m, "memberName")?.as_deref() != Some(name) {
            continue;
        }
        if let Some(x) = elem(s, m, "memberElement")? {
            if is_kind(s, x, mc) {
                return Ok(Some(x));
            }
        }
    }
    Ok(None)
}

/// Definition lists: `scopedMembers(scope)->selectByKind(MD)`.
pub fn def_list(s: &mut Session, e: ElementRef, scope: Scope, mc: &str) -> R<Vec<ElementRef>> {
    let ms = scoped_members(s, e, scope)?;
    Ok(kinds(s, ms, mc))
}

pub fn def_exact(s: &mut Session, e: ElementRef, scope: Scope, mc: &str) -> R<Vec<ElementRef>> {
    let ms = scoped_members(s, e, scope)?;
    Ok(ms.into_iter().filter(|x| is_type(s, *x, mc)).collect())
}

pub fn def_semantic(s: &mut Session, e: ElementRef, scope: Scope, lib_type: &str) -> R<Vec<ElementRef>> {
    let general = required_library_type(s, e, lib_type)?;
    let ms = scoped_members(s, e, scope)?;
    let types = kinds(s, ms, "Type");
    let mut out = Vec::new();
    for t in types {
        if specializes(s, t, general)? {
            out.push(t);
        }
    }
    Ok(out)
}

pub fn def_by_name(s: &mut Session, e: ElementRef, name: &str, scope: Scope, mc: &str) -> R<Option<ElementRef>> {
    for d in def_list(s, e, scope, mc)? {
        if name_of(s, d)?.as_deref() == Some(name) {
            return Ok(Some(d));
        }
    }
    Ok(None)
}

/// A name-keyed map, as the wrappers put it out: JSON pairs of a name and an element's handle (a
/// decimal string, as the bindings hold handles), in membership order.
pub struct NameMap(String);

impl IntoAnswer for NameMap {
    fn into_answer(self) -> Answer {
        Answer::Text(self.0)
    }
}

/// Name-keyed definition maps, `find{Ks}ByName(scope)`: the definitions of the list under every name and
/// short name of their memberships (`membership`, as `Namespace::namesOf` reads it), in the backend's
/// order. Refused where one name names two different elements, naming both.
pub fn def_name_map(s: &mut Session, e: ElementRef, scope: Scope, mc: &str) -> R<NameMap> {
    let ds: HashSet<ElementRef> = def_list(s, e, scope, mc)?.into_iter().collect();
    let ms = elems(s, e, "membership")?;
    let mut entries: Vec<(String, ElementRef)> = Vec::new();
    for m in ms {
        let Some(x) = elem(s, m, "memberElement")? else { continue };
        if !ds.contains(&x) {
            continue;
        }
        for prop in ["memberName", "memberShortName"] {
            let Some(name) = text(s, m, prop)? else { continue };
            match entries.iter().find(|(n, _)| *n == name).map(|(_, y)| *y) {
                Some(y) if y == x => {}
                Some(y) => {
                    let (a, b) = (described(s, y), described(s, x));
                    return refuse(s, format!("the name '{name}' names two elements: {a} and {b}"));
                }
                None => entries.push((name, x)),
            }
        }
    }
    let pairs: Vec<serde_json::Value> =
        entries.into_iter().map(|(n, x)| serde_json::json!([n, s.mint(x).to_string()])).collect();
    Ok(NameMap(serde_json::Value::Array(pairs).to_string()))
}

/// An element as a refusal names it: its name, its metaclass and the namespace that owns it (not its
/// qualified name, which KerML leaves null for a member whose name an earlier member has).
fn described(s: &mut Session, x: ElementRef) -> String {
    let name = text(s, x, "name").ok().flatten().unwrap_or_else(|| "an unnamed element".to_string());
    let owner = match elem(s, x, "owningNamespace").ok().flatten() {
        Some(ns) => text(s, ns, "qualifiedName").ok().flatten(),
        None => None,
    };
    let owner = owner.unwrap_or_else(|| "the root namespace".to_string());
    format!("{name} ({}) in {owner}", metaclass(s, x))
}

// ---------------------------------------------------------------- packages

pub fn find_packages(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let ms = elems(s, e, "ownedMember")?;
    Ok(kinds(s, ms, "Package"))
}

pub fn find_package(s: &mut Session, e: ElementRef, name: &str) -> R<Option<ElementRef>> {
    for p in find_packages(s, e)? {
        if name_of(s, p)?.as_deref() == Some(name) {
            return Ok(Some(p));
        }
    }
    Ok(None)
}

// ---------------------------------------------------------------- typing and specialization

pub fn find_declared_types(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for t in elems(s, e, "ownedTyping")? {
        if !flag(s, t, "isImplied")? {
            if let Some(x) = elem(s, t, "type")? {
                out.push(x);
            }
        }
    }
    Ok(out)
}

fn generals(s: &mut Session, e: ElementRef, implied: Option<bool>) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for sp in elems(s, e, "ownedSpecialization")? {
        if let Some(want) = implied {
            if flag(s, sp, "isImplied")? != want {
                continue;
            }
        }
        if let Some(g) = elem(s, sp, "general")? {
            out.push(g);
        }
    }
    Ok(out)
}

/// `Type::supertypes(excludeImplied)`, `Feature::supertypes` on a feature.
fn supertypes(s: &mut Session, e: ElementRef, exclude_implied: bool) -> R<Vec<ElementRef>> {
    let class = if is_kind(s, e, "Feature") { "Feature" } else { "Type" };
    match ops::call(s, e, class, "supertypes", &[Arg::Bool(exclude_implied)])? {
        Answer::Elements(xs) => Ok(xs),
        Answer::Element(x) => Ok(vec![x]),
        Answer::Absent => Ok(Vec::new()),
        _ => Err(s.fail(Status::Internal, "supertypes(): types were expected")),
    }
}

pub fn find_declared_supertypes(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    supertypes(s, e, true)
}

pub fn find_implied_supertypes(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let declared = supertypes(s, e, true)?;
    Ok(supertypes(s, e, false)?.into_iter().filter(|t| !declared.contains(t)).collect())
}

pub fn find_declared_and_implied_supertypes(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    supertypes(s, e, false)
}

pub fn find_subsetted_features(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for sb in elems(s, e, "ownedSubsetting")? {
        if flag(s, sb, "isImplied")? || !is_type(s, sb, "Subsetting") {
            continue;
        }
        if let Some(f) = elem(s, sb, "subsettedFeature")? {
            out.push(f);
        }
    }
    Ok(out)
}

pub fn find_redefined_features(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for r in elems(s, e, "ownedRedefinition")? {
        if flag(s, r, "isImplied")? {
            continue;
        }
        if let Some(f) = elem(s, r, "redefinedFeature")? {
            out.push(f);
        }
    }
    Ok(out)
}

pub fn find_referenced_feature(s: &mut Session, e: ElementRef) -> R<Option<ElementRef>> {
    match elem(s, e, "ownedReferenceSubsetting")? {
        Some(r) => elem(s, r, "referencedFeature"),
        None => Ok(None),
    }
}

pub fn find_redefinition_root(s: &mut Session, e: ElementRef) -> R<ElementRef> {
    let mut f = e;
    let mut seen = HashSet::new();
    while seen.insert(f) {
        match find_redefined_features(s, f)?.first() {
            Some(g) => f = *g,
            None => return Ok(f),
        }
    }
    refuse(s, "the written redefinitions form a cycle")
}

pub fn find_effective_feature(s: &mut Session, e: ElementRef, feature: ElementRef) -> R<Option<ElementRef>> {
    for f in elems(s, e, "feature")? {
        if f == feature || all_redefined_features(s, f)?.contains(&feature) {
            return Ok(Some(f));
        }
    }
    Ok(None)
}

// ---------------------------------------------------------------- values

fn feature_values(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let ms = elems(s, e, "ownedMembership")?;
    Ok(kinds(s, ms, "FeatureValue"))
}

pub fn find_value(s: &mut Session, e: ElementRef) -> R<Option<ElementRef>> {
    match feature_values(s, e)?.first() {
        Some(fv) => elem(s, *fv, "value"),
        None => Ok(None),
    }
}

pub fn find_has_default_value(s: &mut Session, e: ElementRef) -> R<bool> {
    for fv in feature_values(s, e)? {
        if flag(s, fv, "isDefault")? {
            return Ok(true);
        }
    }
    Ok(false)
}

pub fn find_has_initial_value(s: &mut Session, e: ElementRef) -> R<bool> {
    for fv in feature_values(s, e)? {
        if flag(s, fv, "isInitial")? {
            return Ok(true);
        }
    }
    Ok(false)
}

/// `operands()`: an operator expression's `argument`.
fn operands(s: &mut Session, x: ElementRef) -> R<Vec<ElementRef>> {
    elems(s, x, "argument")
}

fn operator(s: &mut Session, x: ElementRef) -> R<Option<String>> {
    text(s, x, "operator")
}

/// `unsigned()`: a sign taken off.
fn unsigned(s: &mut Session, x: ElementRef) -> R<ElementRef> {
    if is_kind(s, x, "OperatorExpression") {
        if let Some(op) = operator(s, x)? {
            if op == "-" || op == "+" {
                let args = operands(s, x)?;
                if args.len() == 1 {
                    return Ok(args[0]);
                }
            }
        }
    }
    Ok(x)
}

fn is_number_literal(s: &mut Session, x: ElementRef) -> bool {
    is_kind(s, x, "LiteralInteger") || is_kind(s, x, "LiteralRational")
}

pub fn find_has_unit(s: &mut Session, x: ElementRef) -> R<bool> {
    let u = unsigned(s, x)?;
    Ok(is_kind(s, u, "OperatorExpression") && operator(s, u)?.as_deref() == Some("["))
}

pub fn find_is_literal(s: &mut Session, x: ElementRef) -> R<bool> {
    if is_kind(s, x, "LiteralExpression") {
        return Ok(true);
    }
    let u = unsigned(s, x)?;
    Ok(u != x && is_number_literal(s, u))
}

pub fn find_literal(s: &mut Session, x: ElementRef) -> R<Literal> {
    if !find_is_literal(s, x)? {
        return refuse(s, "the value is not a literal (a value with a unit, an expression or a reference)");
    }
    if is_kind(s, x, "LiteralInteger") {
        return match integer(s, x, "value")? {
            Some(i) => Ok(Literal::Integer(i)),
            None => refuse(s, "the integer literal has no value"),
        };
    }
    if is_kind(s, x, "LiteralRational") {
        return match number(s, x, "value")? {
            Some(r) => Ok(Literal::Real(r)),
            None => refuse(s, "the rational literal has no value"),
        };
    }
    if is_kind(s, x, "LiteralBoolean") {
        return Ok(Literal::Boolean(flag(s, x, "value")?));
    }
    if is_kind(s, x, "LiteralString") {
        return Ok(Literal::String(text(s, x, "value")?.unwrap_or_default()));
    }
    if is_kind(s, x, "LiteralInfinity") {
        return Ok(Literal::Unbounded);
    }
    // A sign on a number literal.
    let negative = operator(s, x)?.as_deref() == Some("-");
    let a = operands(s, x)?[0];
    if is_kind(s, a, "LiteralInteger") {
        match integer(s, a, "value")? {
            Some(i) => Ok(Literal::Integer(if negative { -i } else { i })),
            None => refuse(s, "the integer literal has no value"),
        }
    } else {
        match number(s, a, "value")? {
            Some(r) => Ok(Literal::Real(if negative { -r } else { r })),
            None => refuse(s, "the rational literal has no value"),
        }
    }
}

/// The parts of `<number> [<unit reference>]` (a sign allowed): the number and the reference.
fn number_with_unit(s: &mut Session, x: ElementRef) -> R<Option<(ElementRef, ElementRef)>> {
    if !find_has_unit(s, x)? {
        return Ok(None);
    }
    let u = unsigned(s, x)?;
    let parts = operands(s, u)?;
    if parts.len() != 2 {
        return Ok(None);
    }
    let first = unsigned(s, parts[0])?;
    if is_number_literal(s, first) && is_kind(s, parts[1], "FeatureReferenceExpression") {
        Ok(Some((parts[0], parts[1])))
    } else {
        Ok(None)
    }
}

pub fn find_unit(s: &mut Session, x: ElementRef) -> R<Option<ElementRef>> {
    if !find_has_unit(s, x)? {
        let u = unsigned(s, x)?;
        return if is_kind(s, u, "LiteralExpression") {
            Ok(None)
        } else {
            refuse(s, "the value is not a literal: its unit needs evaluation")
        };
    }
    match number_with_unit(s, x)? {
        Some((_, reference)) => elem(s, reference, "referent"),
        None => refuse(s, "the unit is not a single unit reference on a number: it needs evaluation"),
    }
}

// ---------------------------------------------------------------- multiplicity and order

/// `boundsRange()`: a multiplicity's own range, else the first range among what it specializes.
fn bounds_range(s: &mut Session, m: ElementRef) -> R<Option<ElementRef>> {
    if is_kind(s, m, "MultiplicityRange") {
        return Ok(Some(m));
    }
    // OCL closure, depth first from the multiplicity's generals, not past a range.
    let mut seen = HashSet::new();
    let mut stack: Vec<ElementRef> = generals(s, m, None)?.into_iter().rev().collect();
    while let Some(t) = stack.pop() {
        if !seen.insert(t) {
            continue;
        }
        if is_kind(s, t, "MultiplicityRange") {
            return Ok(Some(t));
        }
        stack.extend(generals(s, t, None)?.into_iter().rev());
    }
    Ok(None)
}

fn own_range(s: &mut Session, t: ElementRef) -> R<Option<ElementRef>> {
    match elem(s, t, "multiplicity")? {
        Some(m) => bounds_range(s, m),
        None => Ok(None),
    }
}

fn lower_bound_expression(s: &mut Session, t: ElementRef) -> R<Option<ElementRef>> {
    let Some(r) = own_range(s, t)? else { return Ok(None) };
    if let Some(l) = elem(s, r, "lowerBound")? {
        return Ok(Some(l));
    }
    match elem(s, r, "upperBound")? {
        Some(u) if is_kind(s, u, "LiteralInfinity") => Ok(None),
        u => Ok(u),
    }
}

fn has_multiplicity(s: &mut Session, t: ElementRef) -> R<bool> {
    Ok(elem(s, t, "multiplicity")?.is_some())
}

pub fn find_lower_bound(s: &mut Session, t: ElementRef) -> R<Option<i64>> {
    if !has_multiplicity(s, t)? {
        return Ok(None);
    }
    match lower_bound_expression(s, t)? {
        None => Ok(Some(0)),
        Some(e) => match find_literal(s, e)? {
            Literal::Integer(i) => Ok(Some(i)),
            _ => refuse(s, "the lower bound is not an integer"),
        },
    }
}

pub fn find_upper_bound(s: &mut Session, t: ElementRef) -> R<Option<i64>> {
    if !has_multiplicity(s, t)? {
        return Ok(None);
    }
    let Some(r) = own_range(s, t)? else { return Ok(Some(UNBOUNDED)) };
    let Some(u) = elem(s, r, "upperBound")? else { return refuse(s, "the multiplicity range has no upper bound") };
    match find_literal(s, u)? {
        Literal::Integer(i) => Ok(Some(i)),
        Literal::Unbounded => Ok(Some(UNBOUNDED)),
        _ => refuse(s, "the upper bound is not an integer or *"),
    }
}

pub fn find_lower_bound_expr(s: &mut Session, t: ElementRef) -> R<Option<ElementRef>> {
    lower_bound_expression(s, t)
}

pub fn find_upper_bound_expr(s: &mut Session, t: ElementRef) -> R<Option<ElementRef>> {
    match own_range(s, t)? {
        Some(r) => elem(s, r, "upperBound"),
        None => Ok(None),
    }
}

pub fn find_has_literal_lower_bound(s: &mut Session, t: ElementRef) -> R<bool> {
    if !has_multiplicity(s, t)? {
        return Ok(false);
    }
    match lower_bound_expression(s, t)? {
        None => Ok(true),
        Some(e) => find_is_literal(s, e),
    }
}

pub fn find_has_literal_upper_bound(s: &mut Session, t: ElementRef) -> R<bool> {
    if !has_multiplicity(s, t)? {
        return Ok(false);
    }
    match find_upper_bound_expr(s, t)? {
        None => Ok(true),
        Some(e) => find_is_literal(s, e),
    }
}

pub fn find_is_effectively_ordered(s: &mut Session, e: ElementRef) -> R<bool> {
    let mut seen = HashSet::new();
    let mut stack = vec![e];
    while let Some(f) = stack.pop() {
        if !seen.insert(f) {
            continue;
        }
        if flag(s, f, "isOrdered")? {
            return Ok(true);
        }
        for sb in elems(s, f, "ownedSubsetting")? {
            if let Some(g) = elem(s, sb, "subsettedFeature")? {
                stack.push(g);
            }
        }
    }
    Ok(false)
}

// ---------------------------------------------------------------- behaviour

/// `Type::directionOf(feature)`, lowercase, or none.
fn direction_of(s: &mut Session, t: ElementRef, f: ElementRef) -> R<Option<String>> {
    let h = s.mint(f);
    match ops::call(s, t, "Type", "directionOf", &[Arg::Handle(h)])? {
        Answer::Text(d) => Ok(Some(d.to_ascii_lowercase())),
        Answer::Absent => Ok(None),
        _ => Err(s.fail(Status::Internal, "directionOf(): a direction was expected")),
    }
}

pub fn find_parameters(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for f in scoped_features(s, e, scope, vis, library)? {
        if direction_of(s, e, f)?.is_some() {
            out.push(f);
        }
    }
    Ok(out)
}

/// The scoped features whose direction relative to `e` is `one` or inout.
fn directed(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool, one: &str) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for f in scoped_features(s, e, scope, vis, library)? {
        if matches!(direction_of(s, e, f)?.as_deref(), Some(d) if d == one || d == "inout") {
            out.push(f);
        }
    }
    Ok(out)
}

pub fn find_inputs(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    directed(s, e, scope, vis, library, "in")
}

pub fn find_outputs(s: &mut Session, e: ElementRef, scope: Scope, vis: Visibility, library: bool) -> R<Vec<ElementRef>> {
    directed(s, e, scope, vis, library, "out")
}

pub fn find_subsetting_features_of(
    s: &mut Session,
    e: ElementRef,
    feature: ElementRef,
    scope: Scope,
    vis: Visibility,
    library: bool,
) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for f in scoped_features(s, e, scope, vis, library)? {
        if specializes(s, f, feature)? {
            out.push(f);
        }
    }
    Ok(out)
}

pub fn find_result_expression(s: &mut Session, e: ElementRef) -> R<Option<ElementRef>> {
    let ms = elems(s, e, "featureMembership")?;
    match kinds(s, ms, "ResultExpressionMembership").first() {
        Some(m) => elem(s, *m, "ownedResultExpression"),
        None => Ok(None),
    }
}

pub fn find_type_argument(s: &mut Session, e: ElementRef) -> R<Option<ElementRef>> {
    let op = operator(s, e)?.unwrap_or_default();
    if !["istype", "hastype", "@", "@@", "as", "meta"].contains(&op.as_str()) {
        return Ok(None);
    }
    let fs = elems(s, e, "ownedFeature")?;
    if fs.len() < 2 {
        return Ok(None);
    }
    match elems(s, fs[1], "ownedTyping")?.first() {
        Some(t) => elem(s, *t, "type"),
        None => Ok(None),
    }
}

fn feature_target(s: &mut Session, f: ElementRef) -> R<Option<ElementRef>> {
    elem(s, f, "featureTarget")
}

pub fn find_initial_states(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let entry = elem(s, e, "entryAction")?;
    // The successions to exactly one state.
    let mut successions = Vec::new();
    let fs = elems(s, e, "ownedFeature")?;
    for su in kinds(s, fs, "SuccessionAsUsage") {
        let targets = elems(s, su, "targetFeature")?;
        if targets.len() != 1 {
            continue;
        }
        if let Some(target) = feature_target(s, targets[0])? {
            if is_kind(s, target, "StateUsage") {
                successions.push((su, target));
            }
        }
    }
    // 1. `first start then off;`
    let mut from_start = Vec::new();
    for (su, target) in &successions {
        let Some(source) = elem(s, *su, "sourceFeature")? else { continue };
        let Some(source) = feature_target(s, source)? else { continue };
        if flag(s, source, "isLibraryElement")? && name_of(s, source)?.as_deref() == Some("start") {
            from_start.push(*target);
        }
    }
    if !from_start.is_empty() {
        return Ok(from_start);
    }
    // 2. `entry; then off;`
    let Some(entry) = entry else { return Ok(Vec::new()) };
    let mut from_entry = Vec::new();
    for (su, target) in &successions {
        let Some(source) = elem(s, *su, "sourceFeature")? else { continue };
        if feature_target(s, source)? == Some(entry) {
            from_entry.push(*target);
        }
    }
    Ok(from_entry)
}

pub fn find_accepted_types(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    if is_kind(s, e, "AcceptActionUsage") {
        return match elem(s, e, "payloadParameter")? {
            Some(p) => elems(s, p, "type"),
            None => Ok(Vec::new()),
        };
    }
    let mut out = Vec::new();
    for a in elems(s, e, "triggerAction")? {
        out.extend(find_accepted_types(s, a)?);
    }
    Ok(out)
}

pub fn find_is_accepted_type(s: &mut Session, e: ElementRef, ty: ElementRef) -> R<bool> {
    for a in find_accepted_types(s, e)? {
        if specializes(s, ty, a)? {
            return Ok(true);
        }
    }
    Ok(false)
}

// ---------------------------------------------------------------- structure and annotations

pub fn find_path(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let chain = elems(s, e, "chainingFeature")?;
    Ok(if chain.is_empty() { vec![e] } else { chain })
}

pub fn find_variation(s: &mut Session, e: ElementRef) -> R<Option<ElementRef>> {
    match elem(s, e, "owningMembership")? {
        Some(m) if is_kind(s, m, "VariantMembership") => match elem(s, e, "owningNamespace")? {
            Some(ns) if is_kind(s, ns, "Type") => Ok(Some(ns)),
            Some(_) => refuse(s, "the variant's owning namespace is not a type"),
            None => Ok(None),
        },
        _ => Ok(None),
    }
}

pub fn find_applied_metadata(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let mut out = Vec::new();
    for a in elems(s, e, "ownedAnnotation")? {
        if let Some(x) = elem(s, a, "annotatingElement")? {
            if is_kind(s, x, "MetadataFeature") {
                out.push(x);
            }
        }
    }
    if is_kind(s, e, "Namespace") {
        let ms = elems(s, e, "ownedMember")?;
        for f in kinds(s, ms, "MetadataFeature") {
            if elems(s, f, "annotatedElement")?.contains(&e) {
                out.push(f);
            }
        }
    }
    Ok(out)
}

pub fn find_member_metadata(s: &mut Session, e: ElementRef) -> R<Vec<ElementRef>> {
    let ms = elems(s, e, "ownedMember")?;
    Ok(kinds(s, ms, "MetadataUsage"))
}

pub fn find_doc_text(s: &mut Session, e: ElementRef) -> R<Vec<String>> {
    let mut out = Vec::new();
    for d in elems(s, e, "documentation")? {
        out.push(text(s, d, "body")?.unwrap_or_default());
    }
    Ok(out)
}
