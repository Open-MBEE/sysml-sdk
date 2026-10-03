//! Write the standard library as full-form interchange JSON, under its normative element ids, so
//! that a payload model's references into the library can be read without the SysML Toolkit.
//!
//! Usage: cargo run --release --example export_library -- <sysml.library directory> <out.json>
//!
//! The SysML Toolkit exports the library as a compact array (the ids KerML 9.1 and SysML 9.1
//! prescribe); its full-form completion is the one the SysML Toolkit offers for any compact
//! document, which writes the owned side of the inheritance-aware properties
//! (`inheritedMembership`, `inheritedFeature`, `importedMembership` and `featuringType` stay
//! empty). References inside the library that do not resolve keep the SysML Toolkit's recovery
//! annotations.

use std::collections::{HashMap, HashSet};
use std::path::Path;
use std::process::ExitCode;

use sysmlv2_model::full::{from_compact_value_with_policy, UnresolvedReferencePolicy};
use sysmlv2_model::json::{library_element_name_map, library_to_compact_json_with_units};
use sysmlv2_model::loader::load_document;
use sysmlv2_transform::{Library, Session};

/// The names of the files in `dir` and its subdirectories.
fn file_names(dir: &Path) -> HashSet<String> {
    let mut names = HashSet::new();
    let mut dirs = vec![dir.to_path_buf()];
    while let Some(dir) = dirs.pop() {
        for entry in std::fs::read_dir(&dir).into_iter().flatten().flatten() {
            let path = entry.path();
            if path.is_dir() {
                dirs.push(path);
            } else if let Some(name) = path.file_name() {
                names.insert(name.to_string_lossy().into_owned());
            }
        }
    }
    names
}

fn main() -> ExitCode {
    let args: Vec<String> = std::env::args().skip(1).collect();
    let [library, out] = args.as_slice() else {
        eprintln!("usage: export_library <sysml.library directory> <out.json>");
        return ExitCode::FAILURE;
    };
    // The SysML Toolkit adds its own ambient packages (TransformMeta) to every library it loads.
    // They are not part of the standard library, so they are kept out of its interchange.
    std::env::set_var("SYSMLV2_AMBIENT", "off");
    let session = match Session::from_sources_with_library(Vec::new(), Some(Library::dir(library))) {
        Ok(s) => s,
        Err(e) => {
            eprintln!("error: cannot load the library {library}: {e}");
            return ExitCode::FAILURE;
        }
    };
    let model = session.model();
    let (compact, units) = library_to_compact_json_with_units(model);

    // Every exported unit must be a file of the given library directory (units are named by file
    // name; the files sit in its subdirectories).
    let files = file_names(Path::new(library));
    let foreign: Vec<&str> =
        units.iter().map(|(_, name)| name.as_str()).filter(|name| !files.contains(*name)).collect();
    if !foreign.is_empty() {
        eprintln!("error: {} unit(s) not from {library}: {}", foreign.len(), foreign.join(", "));
        return ExitCode::FAILURE;
    }
    eprintln!("{} library units from {library}", units.len());

    // Qualified name -> id, for the implied relationships to library bases, as the SysML Toolkit's
    // own full-form export builds it.
    let by_name: HashMap<String, String> = library_element_name_map(model)
        .into_iter()
        .map(|(id, segments)| (segments.join("::"), id))
        .collect();

    // What the completion will derive from: the loaded document, or, when the loader warns, the
    // element array. Reported so that a change in the SysML Toolkit shows in the build log.
    let names: HashMap<String, Vec<String>> =
        by_name.iter().map(|(qn, id)| (id.clone(), qn.split("::").map(str::to_string).collect())).collect();
    if let Ok((_, _, _, warnings)) = load_document(&compact, &names) {
        for warning in &warnings {
            eprintln!("SysML Toolkit loader: {warning}");
        }
    }

    let full = match from_compact_value_with_policy(compact, &by_name, UnresolvedReferencePolicy::Preserve) {
        Ok(v) => v,
        Err(e) => {
            eprintln!("error: {} unresolved references", e.references.len());
            return ExitCode::FAILURE;
        }
    };
    let count = full.as_array().map_or(0, Vec::len);
    if let Err(e) = std::fs::write(out, full.to_string()) {
        eprintln!("error: cannot write {out}: {e}");
        return ExitCode::FAILURE;
    }
    eprintln!("{count} elements written to {out}");
    ExitCode::SUCCESS
}
