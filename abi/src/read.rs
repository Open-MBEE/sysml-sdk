//! Property reads, one generic reader per ABI result shape.
//!
//! Every property row of the naming table is answered the same way, so the generated wrappers
//! call these with the specification name and nothing is hand-written per member: the
//! SysML Toolkit's checked reader, `ResolvedModel::property(e, name)`, reads the property by its
//! specification name, owned or derived, following the metamodel's redefinitions (`general` on a
//! `FeatureTyping` is its `type`, `memberName` on an `OwningMembership` its `ownedMemberName`).
//! Its value is converted to the ABI shape.
//!
//! Where the SysML Toolkit cannot stand behind a value (not computed, an incomplete derivation,
//! an incomplete type projection, ...) it refuses, and so does this reader, with `NOT_IMPLEMENTED`
//! and the SysML Toolkit's reason. A reference that does not resolve, or resolves to an element
//! this session has not loaded, is reported with `UNRESOLVED`. Nothing is guessed or dropped
//! silently.

use serde_json::Value;
use sysmlv2_model::json::{DerivedValue, ElementRef, PropertyError, Reference};

use crate::{str_of, Bool, Handles, Int, Real, Ref, Session, Status, Str};

/// What a read produced before conversion to a specific ABI shape.
pub enum Answer {
    Absent,
    Elements(Vec<ElementRef>),
    Element(ElementRef),
    Text(String),
    Texts(Vec<String>),
    Flag(bool),
    Integer(i64),
    Number(f64),
}

fn unresolved(s: &mut Session, name: &str, spelling: &str) -> Status {
    s.fail(Status::Unresolved, format!("{name}: reference '{spelling}' did not resolve"))
}

fn outside(s: &mut Session, name: &str, id: &str) -> Status {
    s.fail(Status::Unresolved, format!("{name}: reference to element {id}, which this session has not loaded"))
}

/// The answer for `name` on `e`, or the status to return.
fn answer(s: &mut Session, e: ElementRef, name: &str) -> Result<Answer, Status> {
    match s.model().property(e, name) {
        Ok(v) => from_json(s, name, v),
        Err(PropertyError::UnresolvedReference(spelling)) => Err(unresolved(s, name, &spelling)),
        Err(why) => Err(s.fail(Status::NotImplemented, format!("{name} is not answered by the SysML Toolkit: {why}"))),
    }
}

fn from_json(s: &mut Session, name: &str, v: Value) -> Result<Answer, Status> {
    Ok(match v {
        Value::Null => Answer::Absent,
        Value::Bool(b) => Answer::Flag(b),
        Value::String(t) => Answer::Text(t),
        Value::Number(n) => match n.as_i64() {
            Some(i) => Answer::Integer(i),
            None => Answer::Number(n.as_f64().unwrap_or(f64::NAN)),
        },
        Value::Object(_) => match id_ref(s, name, &v)? {
            Some(x) => Answer::Element(x),
            None => Answer::Absent,
        },
        Value::Array(items) => {
            if items.iter().all(|i| i.is_string()) && !items.is_empty() {
                Answer::Texts(items.into_iter().filter_map(|i| i.as_str().map(str::to_string)).collect())
            } else {
                let mut all = Vec::with_capacity(items.len());
                for i in &items {
                    if let Some(x) = id_ref(s, name, i)? {
                        all.push(x);
                    }
                }
                Answer::Elements(all)
            }
        }
    })
}

/// `{"@id": uuid}` to an element of the model; `{"@ref": spelling}` is an unresolved reference,
/// and an id this session has not loaded is reported the same way.
fn id_ref(s: &mut Session, name: &str, v: &Value) -> Result<Option<ElementRef>, Status> {
    if let Some(id) = v.get("@id").and_then(Value::as_str) {
        return match s.model().element_by_id(id) {
            Some(x) => Ok(Some(x)),
            None => Err(outside(s, name, id)),
        };
    }
    if let Some(sp) = v.get("@ref").and_then(Value::as_str) {
        return Err(unresolved(s, name, sp));
    }
    Ok(None)
}

/// A value the SysML Toolkit computed in its own value shape (an operation's result), as an answer.
pub fn from_derived(s: &mut Session, name: &str, v: DerivedValue) -> Result<Answer, Status> {
    fn element(s: &mut Session, name: &str, r: Reference) -> Result<ElementRef, Status> {
        match r {
            Reference::Element(x) => Ok(x),
            Reference::Unresolved(sp) => Err(unresolved(s, name, &sp)),
            Reference::External(id) => Err(outside(s, name, &id.to_string())),
            _ => Err(s.fail(Status::Internal, format!("{name}: a kind of reference this library does not know"))),
        }
    }
    Ok(match v {
        DerivedValue::Null => Answer::Absent,
        DerivedValue::Bool(b) => Answer::Flag(b),
        DerivedValue::Str(t) => Answer::Text(t),
        DerivedValue::Strings(ts) => Answer::Texts(ts),
        DerivedValue::Element(x) => Answer::Element(x),
        DerivedValue::Elements(xs) => Answer::Elements(xs),
        DerivedValue::Reference(r) => Answer::Element(element(s, name, r)?),
        DerivedValue::References(rs) => {
            let mut all = Vec::with_capacity(rs.len());
            for r in rs {
                all.push(element(s, name, r)?);
            }
            Answer::Elements(all)
        }
        _ => return Err(s.fail(Status::Internal, format!("{name}: a kind of value this library does not know"))),
    })
}

// ---------------------------------------------------------------- one reader per ABI shape

pub fn handles(s: &mut Session, e: ElementRef, name: &str, out: &mut Handles) -> Status {
    let answer = answer(s, e, name);
    put_handles(s, name, answer, out)
}

pub fn put_handles(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Handles) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Elements(xs)) => { *out = s.mint_all(xs); Status::Ok }
        Ok(Answer::Element(x)) => { *out = s.mint_all(vec![x]); Status::Ok }
        Ok(Answer::Absent) => { *out = s.mint_all(Vec::new()); Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: a collection was expected")),
    }
}

pub fn reference(s: &mut Session, e: ElementRef, name: &str, out: &mut Ref) -> Status {
    let answer = answer(s, e, name);
    put_reference(s, name, answer, out)
}

pub fn put_reference(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Ref) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Element(x)) => { *out = s.mint_opt(Some(x)); Status::Ok }
        Ok(Answer::Elements(mut xs)) if xs.len() <= 1 => { *out = s.mint_opt(xs.pop()); Status::Ok }
        Ok(Answer::Absent) => { *out = Ref::ABSENT; Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: a single reference was expected")),
    }
}

pub fn text(s: &mut Session, e: ElementRef, name: &str, out: &mut Str) -> Status {
    let answer = answer(s, e, name);
    put_text(s, name, answer, out)
}

pub fn put_text(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Str) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Text(t)) => { *out = str_of(t); Status::Ok }
        Ok(Answer::Texts(ts)) => { *out = str_of(serde_json::to_string(&ts).unwrap_or_default()); Status::Ok }
        // An empty list of strings (aliasIds, text): the SysML Toolkit writes it as an empty array.
        Ok(Answer::Elements(xs)) if xs.is_empty() => { *out = str_of("[]".to_string()); Status::Ok }
        Ok(Answer::Integer(i)) => { *out = str_of(i.to_string()); Status::Ok }
        Ok(Answer::Number(n)) => { *out = str_of(n.to_string()); Status::Ok }
        Ok(Answer::Flag(b)) => { *out = str_of(b.to_string()); Status::Ok }
        Ok(Answer::Absent) => { *out = Str::ABSENT; Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: a text value was expected")),
    }
}

pub fn flag(s: &mut Session, e: ElementRef, name: &str, out: &mut Bool) -> Status {
    let answer = answer(s, e, name);
    put_flag(s, name, answer, out)
}

pub fn put_flag(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Bool) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Flag(b)) => { *out = Bool { present: true, value: b }; Status::Ok }
        Ok(Answer::Absent) => { *out = Bool::ABSENT; Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: a boolean was expected")),
    }
}

pub fn integer(s: &mut Session, e: ElementRef, name: &str, out: &mut Int) -> Status {
    let answer = answer(s, e, name);
    put_integer(s, name, answer, out)
}

pub fn put_integer(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Int) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Integer(i)) => { *out = Int { present: true, value: i }; Status::Ok }
        Ok(Answer::Absent) => { *out = Int::ABSENT; Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: an integer was expected")),
    }
}

pub fn real(s: &mut Session, e: ElementRef, name: &str, out: &mut Real) -> Status {
    let answer = answer(s, e, name);
    put_real(s, name, answer, out)
}

pub fn put_real(s: &mut Session, name: &str, answer: Result<Answer, Status>, out: &mut Real) -> Status {
    match answer {
        Err(st) => st,
        Ok(Answer::Number(n)) => { *out = Real { present: true, value: n }; Status::Ok }
        Ok(Answer::Integer(i)) => { *out = Real { present: true, value: i as f64 }; Status::Ok }
        Ok(Answer::Absent) => { *out = Real::ABSENT; Status::Ok }
        Ok(_) => s.fail(Status::Internal, format!("{name}: a number was expected")),
    }
}
