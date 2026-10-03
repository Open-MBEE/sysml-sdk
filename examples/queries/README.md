# Queries

Parametric queries over SysML v2 models, written against the SDK in all five languages. Each
language has the same query functions and a demo program that prints the same report, byte for
byte: [expected/report.txt](expected/report.txt), and [expected/annex-a.txt](expected/annex-a.txt)
for the vehicle model of the SysML specification's Annex A.

The tutorials in `examples/` show how to read one element. These show what it takes to answer a
question about a whole model: walking a state machine, a part tree, connections, and requirements,
and evaluating the model's own expressions along the way.

## The models

- [models/pumps.sysml](models/pumps.sysml): a pump controller whose behavior is a hierarchical
  state machine: composite states with their own initial states (`first start then idle;`),
  transitions that leave a composite state from any of its substates, guards over attributes, and a
  state that nothing leads to. One guard reads a limit with a default value
  (`maxTemperature default 80.0`), which a scenario may replace; a value bound with `=` could not
  be.
- [models/drones.sysml](models/drones.sysml): a family of drones. `Multicopter` leaves open how many
  rotors a drone has and how many cells its battery has (`rotors : Rotor[rotorCount]`,
  `cells : Cell[cellCount]`); `Quadcopter`, `Hexacopter` and `CargoHexacopter` specialize it side by
  side, each fixing both counts, and the cargo drone adds a payload bay. They are siblings because
  none of them is a kind of another: a hexacopter with an eight-cell battery is not a hexacopter
  whose battery has six. Around that: attribute values redefined along the part tree, connections
  and flows between nested parts, and requirements with constraints, satisfied by two drones.

`models/*.full.json` are the same models exported by the SysML Toolkit with the standard library
loaded, so every demo can run without the SysML Toolkit too: their references into the library
resolve against the library as JSON (`examples/sysml.library.full.json`). None of these files is
committed; generate them once with `python tools/export_example.py --library <sysml.library>`.

## The queries

| Query | Question | Parameters |
|---|---|---|
| `explore` | Which states can the machine reach, and what is a shortest sequence of triggers to each? | which triggers can occur; values of the attributes its guards read |
| `bill_of_materials` | How many parts of each definition does a definition consist of, at every depth? | the root definition |
| `rollup` | What is the total of an attribute over the part tree? | the root, the attribute |
| `reachable_parts` | Which parts does a part reach through connections and flows? | the source part; optionally an item definition, to follow only flows of it |
| `check_satisfactions` | Does every satisfied requirement hold for the part that satisfies it? | how to compute the attributes the model leaves to computation (a total mass is a roll-up) |

Guards, multiplicity bounds, attribute values and constraints are the model's own expressions,
evaluated by one small evaluator (`evaluate`) in three-valued logic: a value that the model does not
determine and the caller did not bind is unknown, and a guard that is unknown may hold. So
`selfTestPassed = false` makes the operating states unreachable, while leaving `selfTestPassed`
unbound keeps them reachable. On Annex A, "which states can the vehicle reach if the brake pedal is
never depressed?" is `explore(vehicleStates, bindings={"brakePedalDepressed": False})`: the guard
`ignitionCmd.ignitionOnOff == IgnitionOnOff::on and brakePedalDepressed` is false whatever the
ignition command says, so `starting` and `on` drop out.

The queries use specification properties only: `nestedState`, `ownedFeature`, `entryAction`,
`triggerAction`, `guardExpression`, `usage`, `multiplicity`, `ownedSubsetting`, `ownedRedefinition`,
`sourceFeature`, `targetFeature`, `featureMembership`, and the like. Three rules show up everywhere:

- **A value holds in a context.** A feature's value is the one given by the feature that stands for
  it in the element at hand: among the element's features, owned or inherited, the one that is the
  feature or redefines it (`feature_in`). That is how `Hexacopter` gets six rotors from a
  multiplicity declared in `Multicopter`, and how the cargo drone's battery, with eight cells, weighs
  more than the others.
- **A usage without a multiplicity takes the one it subsets.** `multiplicity_of` follows SysML's
  rule: a usage's own multiplicity; else that of the usages it subsets or redefines explicitly
  (`part :>> battery` has the multiplicity of `Multicopter::battery`); else `[1..1]` for an
  attribute, item, part or port usage owned by a definition or usage; else nothing constrains it.
  Implied subsettings, such as every part's subsetting of the library's `parts`, do not count.
- **Library features are not model features.** With the standard library loaded, `usage` also lists
  what every part inherits from the library (`self`, `start`, `subparts`, ...); `isLibraryElement`
  tells them apart.

A state's initial states are the targets of the successions it owns (`first start then off;`), as
SysML writes them. `explore` also follows the older form that Annex A uses: an entry action with a
transition from it (`entry action initial; transition initial then off;`).

### What the queries simplify, on purpose

- A bound value holds for a whole exploration. Guard inputs such as a temperature vary over time in
  SysML; binding one asks "what if it always had this value", and leaving it unbound lets every
  guard over it go either way, independently of the others.
- The regions of a parallel state are explored independently of each other.
- A state that no transition or initial succession leads to is unreachable. That is the
  conventional reading; the library's semantics do not state it formally.
- A requirement is checked by its required constraints. Its assumptions (`assume constraint`) and
  its nested requirements are not considered.
- Connections and flows are followed where the model declares them; reaching a part means it may be
  reached, at the level of the usages, not that every instance is.
- The totals the requirements read (`totalMass`, `totalCost`) are declared in the model without a
  value; `check_satisfactions` computes them by roll-up and hands them to the constraint, so a
  violation is the query's conclusion. A model could define the totals itself, with an expression
  over the subparts; then a satisfied requirement that fails makes the model inconsistent rather
  than reporting a violation, and the evaluator would need collection operations such as `sum`.

## Running

From the repository root. Each program takes `--payload` to read the JSON exports, with the library
JSON, instead of the SysML Toolkit; `--library DIR` to load the standard library with the models;
and `--annex-a FILE --library DIR` to report on the Annex A vehicle instead (the model and the
library are in the SysML v2 release repository).

| Language | Build | Run |
|---|---|---|
| Python | | `python examples/queries/python/demo.py` |
| JavaScript | | `node examples/queries/typescript/demo.mjs` |
| Java | `javac --enable-preview --release 21 -cp java/out -d examples/queries/java/out examples/queries/java/*.java` (after compiling the SDK into `java/out`) | `java --enable-preview -cp java/out:examples/queries/java/out QueriesDemo` (`;` on Windows) |
| C# | | `dotnet run --project examples/queries/csharp` (arguments after `--`) |
| C++ | `g++ -std=c++17 -I cpp/include -I vendor examples/queries/cpp/demo.cpp -o queries_demo` (`-ldl` on Linux, `-static` with MinGW) | `./queries_demo` |

Compare with the expected report:
`python examples/queries/python/demo.py | diff --strip-trailing-cr - examples/queries/expected/report.txt`.
The Python tests (`python/tests/test_queries.py`) check the answers, worked out by hand from the
models, and the report.

In JavaScript the SysML Toolkit runs in the WebAssembly build, which reads no files itself: the
demo's `--library` passes `libraryDir`, which reads the library's files in Node; a browser passes
them as `librarySources`.

## What the models work around

The SysML Toolkit does not yet pass unnamed members on to specializations, so the requirement
definitions name their constraints (`require constraint massBound { ... }`): the requirement usages
reach them by inheritance, through `featureMembership`. The derived `requiredConstraint` holds a
requirement's owned constraints only, as the specification defines it.

By the specification, a connection's own ends redefine the ends of the library's association, so
`connectorEnd` lists the connection's own ends. With the library loaded, the SysML Toolkit lists the
library's ends too; `links` reads `sourceFeature` and `targetFeature` instead, which are right
either way.

## With and without the standard library

The models resolve completely only with the standard library: `first start then off;` names the
library's `start` of a state (`States::StateAction::start`), and the models type their attributes
with `ScalarValues`. The JSON exports are made with the library loaded and read with the library
JSON, so there those references resolve. Without `--library`, the runs through the SysML Toolkit
load no library, and the references stay unresolved. The queries do not depend on them:
`initial_states` reads only the target of a succession, and attribute values are read from their expressions, so
every run gives the same report. Units are left out: quantities from the library's `ISQ`
(`mass = 0.045 [kg]`) would need unit arithmetic in the evaluator.
