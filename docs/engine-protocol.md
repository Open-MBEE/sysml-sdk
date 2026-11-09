# The engine protocol

An engine is a program that reads SysML v2 and KerML models and answers questions about them. One
that speaks this protocol is a backend of the SDK in every language: `EngineBackend` starts it as a
process and exchanges JSON lines with it, and the generated classes read through it as through any
other backend. The Pilot Implementation backend is built this way (its engine is
`sysml-pilot-server.jar`); other engines can be added without changing the SDK.

The protocol carries the backend contract the generated classes use: read a property by its
specification name, evaluate an operation, name an element's metaclass and element id, resolve a
qualified name, and list the model's elements.

## The process

The SDK starts the engine with a command line and talks to it over its standard input and output,
one JSON object per line, UTF-8. Standard error is the engine's own (progress, diagnostics).

1. The engine writes one line saying whether it is ready:
   `{"ready": true, "engine": "<name>", "version": "<version>", "protocol": 1}`, or
   `{"ready": false, "error": {"kind": "unavailable", "message": "<why>"}}` and exits with a nonzero
   status.
2. The SDK sends a request per line; the engine answers each with one line, in order.
3. The exchange ends with a `close` request, which the engine answers before exiting, or with the
   end of its input.

## Requests

Every request has an `id` (echoed in the answer) and a `cmd`:

| `cmd` | Fields | Answer (`ok`) |
|---|---|---|
| `open` | `paths` (files and directories), `sources` (unit name to text), `elaborate` (Boolean: add the implied relationships), `library` (a library directory, or null for the engine's own) | an object of facts about the opening (engine-specific, such as timings) |
| `metaclass` | `h` | the element's metaclass name |
| `elementId` | `h` | the element's id, or null |
| `get` | `h`, `prop` (a property's specification name) | the value |
| `call` | `h`, `op` (an operation's specification name), `args` (in the declaration's order) | the result |
| `find` | `h`, `member` (a convenience member's name, as in `convenience.json`), `args` (by parameter name; a scope or visibility filter as its literal, `"owned"`, `"public"`) | the result: a literal as `{"integer": 4}`, `{"real": 0.5}`, `{"boolean": true}`, `{"string": "a"}` or `{"unbounded": true}`; an unbounded upper bound as `"*"`; a name-keyed map as a list of `[name, element]` pairs, in order |
| `findEach` | `hs` (handles), `member`, `args` (as for `find`) | a list with `find`'s result for each element, in order; the first element that fails fails the request, its handle in the message |
| `resolve` | `name` (a qualified name) | a handle, or null |
| `allHandles` | | the handles of the model's elements (not the library's) |
| `diagnostics` | | an object of engine-specific facts about how the engine has answered so far; optional (an engine without it answers `badRequest`). The Pilot engine's: how it answers the convenience members (`mode`), and, when it checks one body against another, counts per member |
| `close` | | null |

An engine reads one model: `open` comes once, first.

## Values

A value is JSON: null for none, a Boolean, a number, a string, a list, or a reference to an element,
`{"@id": "<handle>"}`. A handle is a string the engine mints for an element and keeps for as long as
it runs. Enumeration literals are the specification's, in lowercase (`"public"`, `"inout"`). Arguments
of `call` take the same forms.

## Answers and errors

An answer is `{"id": <id>, "ok": <value>}` or `{"id": <id>, "error": {"kind": "<kind>", "message":
"<text>"}}`. The kinds, and what the SDK makes of them in every language:

| `kind` | Meaning | The SDK |
|---|---|---|
| `notApplicable` | the element's metaclass has no such property or operation, or is not one of a convenience member's metaclasses | a read is absent (null); a call or a convenience member raises `BackendError` |
| `refused` | the engine does not answer this member | raises `NotAnswered` |
| `invalid` | a convenience member gives no answer here by its contract (the literal of an expression that is not one) | raises `Refused`, a `NotAnswered` |
| `unresolved` | the value refers to an element the engine could not resolve | raises `UnresolvedReference` |
| `gone` | no element has this handle | raises `Gone` |
| `failed` | the engine failed while answering | raises `BackendError` |
| `unavailable` | the engine cannot run (not installed, no runtime) | raises `BackendError` |
| `badRequest` | a request the protocol does not define, or out of order | raises the SDK's base error |

In Java and C# the errors end in `Exception`; in C++ they are `not_answered`, `unresolved_reference`,
`gone`, `backend_error` and `sdk_error`.
