//! Specification operations, every one through the SysML Toolkit's operation entry point,
//! `ResolvedModel::invoke_operation`, by the operation's declaration identity.
//!
//! The generated wrapper of an operation passes its arguments in the order the metamodel declares
//! them, with the operation's declaring class and name. This module finds that declaration in the
//! SysML Toolkit's catalog (`metamodel::operations`), converts each argument to the value the
//! SysML Toolkit's parameter type takes, orders them as the SysML Toolkit's execution signature
//! lists its inputs, and converts the result with the property readers' conversions. What the
//! SysML Toolkit cannot run as the specification defines it (no body yet, an unsupported
//! configuration, incomplete evidence), it refuses, and so does this module, with
//! `NOT_IMPLEMENTED` and the SysML Toolkit's reason.

use sysmlv2_model::json::{operation_execution_signature, DerivedValue, ElementRef, OperationError};
use sysmlv2_model::metamodel::{self, ParameterSpec};

use crate::read::{self, Answer};
use crate::{read_str, Handle, Handles, Session, Status, Str};

/// One argument as the C wrapper received it.
pub enum Arg<'a> {
    Str(&'a Str),
    Handle(Handle),
    Handles(&'a Handles),
    Bool(bool),
}

/// The declaration's parameters without its result. The metamodel marks no directions; the result
/// is the unnamed parameter, else the one named `result` (as the SDK's metamodel extraction reads
/// it), so that the count matches the wrapper's arguments.
fn inputs(parameters: &'static [ParameterSpec], wrapper_args: usize) -> Vec<&'static ParameterSpec> {
    let mut named: Vec<_> = parameters.iter().filter(|p| !p.name.is_empty()).collect();
    if named.len() == wrapper_args + 1 {
        if let Some(i) = named.iter().position(|p| p.name == "result") {
            named.remove(i);
        }
    }
    named
}

fn value(s: &mut Session, name: &str, arg: &Arg, param: &ParameterSpec) -> Result<DerivedValue, Status> {
    Ok(match arg {
        Arg::Bool(b) => DerivedValue::Bool(*b),
        Arg::Handle(h) => DerivedValue::Element(s.elem(*h)?),
        Arg::Handles(hs) => {
            let items: &[Handle] =
                if hs.items.is_null() || hs.len == 0 { &[] } else { unsafe { std::slice::from_raw_parts(hs.items, hs.len) } };
            let mut all: Vec<ElementRef> = Vec::with_capacity(items.len());
            for &h in items {
                all.push(s.elem(h)?);
            }
            DerivedValue::Elements(all)
        }
        Arg::Str(text) => {
            if !text.present {
                DerivedValue::Null
            } else {
                let t = match unsafe { read_str(text) } {
                    Ok(t) => t,
                    Err(st) => return Err(s.fail(st, format!("{name}: argument {} is not UTF-8", param.name))),
                };
                match param.target {
                    "Boolean" => match t.as_str() {
                        "true" | "True" => DerivedValue::Bool(true),
                        "false" | "False" => DerivedValue::Bool(false),
                        _ => return Err(s.fail(Status::InvalidArg, format!("{name}: argument {} is not a Boolean", param.name))),
                    },
                    _ => DerivedValue::Str(t),
                }
            }
        }
    })
}

/// Invoke the operation `member` declared on `class` on the element `e`.
pub fn call(s: &mut Session, e: ElementRef, class: &str, member: &str, args: &[Arg]) -> Result<Answer, Status> {
    let name = format!("{class}::{member}()");
    let Some(spec) = metamodel::operations().iter().find(|op| op.declaring_metaclass == class && op.name == member) else {
        return Err(s.fail(Status::NotImplemented, format!("{name}: the SysML Toolkit's catalog has no such declaration")));
    };
    let params = inputs(spec.parameters, args.len());
    if params.len() != args.len() {
        return Err(s.fail(
            Status::Internal,
            format!("{name}: the SysML Toolkit declares {} parameters, the wrapper passed {}", params.len(), args.len()),
        ));
    }
    let mut values = Vec::with_capacity(args.len());
    for (arg, param) in args.iter().zip(&params) {
        values.push((param.id, value(s, &name, arg, param)?));
    }
    // The SysML Toolkit takes the arguments in its execution signature's order, where it has one;
    // without one it will refuse the operation before reading them.
    if let Some(signature) = operation_execution_signature(spec.id) {
        let mut ordered = Vec::with_capacity(values.len());
        for input in signature.inputs {
            match values.iter().position(|(id, _)| id == input) {
                Some(i) => ordered.push(values.swap_remove(i)),
                None => return Err(s.fail(Status::Internal, format!("{name}: no argument for the SysML Toolkit's input {input}"))),
            }
        }
        values = ordered;
    }
    let values: Vec<DerivedValue> = values.into_iter().map(|(_, v)| v).collect();
    match s.model().invoke_operation(e, spec.id, &values) {
        Ok(result) => read::from_derived(s, &name, result.value),
        Err(err) => Err(match err {
            OperationError::InvalidArgument { .. } | OperationError::ArgumentCount { .. } => {
                s.fail(Status::InvalidArg, format!("{name}: {err}"))
            }
            OperationError::UnknownOperation | OperationError::InvalidReceiver => {
                s.fail(Status::Internal, format!("{name}: {err}"))
            }
            OperationError::WrongReceiver { .. } => s.fail(Status::NotApplicable, format!("{name}: {err}")),
            _ => s.fail(Status::NotImplemented, format!("{name} is not answered by the SysML Toolkit: {err}")),
        }),
    }
}
