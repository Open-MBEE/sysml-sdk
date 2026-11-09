// The SysML v2 Pilot Implementation backend for JavaScript (Node): the Pilot reads the model and
// computes every value with its own code, behind the same Backend contract as the SysML Toolkit
// and payload backends, so Model and the generated interfaces are untouched.
//
// The Pilot is Java. This backend starts the SDK's Pilot engine (sysml-pilot-server.jar, which the
// package carries) in a Java process: an EngineBackend (engine.mjs) over the engine protocol. The
// engine finds the Pilot: SYSML_PILOT_HOME (a Pilot jar and its sysml.library), else the copy in
// the user's cache, else the Pilot release the SDK is pinned to, downloaded into that cache on the
// first use (about 130 MB) and checked against its pinned SHA-256. A Java 21 or newer runtime is
// needed. The SDK redistributes nothing of the Pilot (Eclipse Public License 2.0).
//
// Errors: a property the element's metaclass does not have reads as absent; an element no longer
// known throws Gone; a reference the Pilot could not resolve throws UnresolvedReference; a failure
// inside the Pilot, an operation the Pilot does not have, or a Pilot that cannot be found,
// downloaded or started throws BackendError.

import { BackendError } from "./runtime.mjs";
import { EngineBackend, nodeModules } from "./engine.mjs";

const SERVER_CLASS = "org.openmbee.sysml.pilot.PilotServer";

/** The Pilot engine's jar: `server`, else SYSML_PILOT_SERVER, else the one the package carries. */
function pilotServerJar(n, server) {
  server ??= process.env.SYSML_PILOT_SERVER;
  if (server) {
    if (!n.existsSync(server)) throw new BackendError(`the Pilot engine ${server} does not exist`);
    return server;
  }
  const carried = n.join(n.dirname(n.fileURLToPath(import.meta.url)), "pilot", "sysml-pilot-server.jar");
  if (n.existsSync(carried)) return carried;
  throw new BackendError(
    "this copy of the SDK carries no Pilot engine (sysml-pilot-server.jar; the release's packages do); " +
    "build it with python tools/package.py pilot-server and set SYSML_PILOT_SERVER to it");
}

/** The `java` to run the Pilot with: `java`, else JAVA_HOME's, else the PATH's. */
function javaExecutable(n, java) {
  if (java) return java;
  const home = process.env.JAVA_HOME;
  if (home) {
    for (const name of ["java.exe", "java"]) {
      const candidate = n.join(home, "bin", name);
      if (n.existsSync(candidate)) return candidate;
    }
  }
  return "java";  // the PATH's; a missing one fails to start, with that reason
}

export class PilotBackend extends EngineBackend {
  /** The version of the Pilot that reads the model. */
  get pilotVersion() { return this.version; }

  /**
   * Read a model: files and directories of .sysml and .kerml files (`paths`), and in-memory
   * `sources` (unit name to text). With `elaborate` (the default), the Pilot adds the implied
   * relationships and implicit elements, to the model and to its standard library. `java` and `heap` (the JVM's maximum heap) run the
   * process; `pilotHome`, or `pilotJar` and `pilotLibrary` together, name a Pilot instead of the
   * one the engine finds; `server` names the engine's jar.
   */
  static async open({ paths = [], sources = {}, elaborate = true, java, heap = "4g", pilotHome, pilotJar, pilotLibrary,
                      server } = {}) {
    const n = await nodeModules();
    const command = [javaExecutable(n, java), `-Xmx${heap}`, "-cp", pilotServerJar(n, server), SERVER_CLASS];
    if (pilotHome != null) command.push("--home", pilotHome);
    else if (pilotJar != null || pilotLibrary != null) {
      if (pilotJar == null || pilotLibrary == null) throw new Error("name both pilotJar and pilotLibrary, or neither");
      command.push("--jar", pilotJar, "--library", pilotLibrary);
    }
    try {
      return await super.start(command, { paths, sources, elaborate });
    } catch (e) {
      if (e instanceof BackendError && e.message.startsWith("cannot start"))
        throw new BackendError(`${e.message} (the Pilot needs a Java 21 or newer runtime: set JAVA_HOME, or put java on the PATH)`);
      throw e;
    }
  }
}
