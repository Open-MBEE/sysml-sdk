// The KerML and SysML standard library, in the two forms the backends read. Node only: a browser
// has no file system to hold them (fetch the files and pass `librarySources`, or a library JSON you
// host yourself, instead).
//
// standardLibrary() is the directory of the library's models this package carries, for the SysML
// Toolkit backend: ToolkitBackend.open({ sources, libraryDir: await standardLibrary() }).
//
// standardLibraryJson() is the same library as full-form interchange JSON, for the payload backend
// (PayloadLibrary). It is too large for the package: the first call downloads
// sysml_library-<version>.zip from this version's GitHub release, checks it against the SHA-256
// GitHub states for that file, and keeps the JSON in the user's cache (the same place the other
// SDK languages keep it), where later calls find it. Nothing is downloaded unless it is called.
// SYSML_LIBRARY_JSON names a copy of the JSON to use instead.
//
// Both are the library of the SysML v2 release the SDK was built with, licensed under the Eclipse
// Public License 2.0, not the SDK's Apache-2.0: see the LICENSE and NOTICE beside them.

const REPOSITORY = "Open-MBEE/sysml-sdk";
const JSON_VARIABLE = "SYSML_LIBRARY_JSON";
const CACHE_VARIABLE = "SYSML_CACHE_DIR";

export class LibraryUnavailable extends Error {}

function nodeOnly(what) {
  if (typeof process === "undefined" || !process.versions?.node)
    throw new LibraryUnavailable(`${what} needs Node: a browser has no file system to keep the library in`);
}

async function here() {
  const { dirname } = await import("node:path");
  const { fileURLToPath } = await import("node:url");
  return dirname(fileURLToPath(import.meta.url));
}

/** The directory of the standard library models this package carries, for `libraryDir`. */
export async function standardLibrary() {
  nodeOnly("standardLibrary()");
  const { join } = await import("node:path");
  const { existsSync, readdirSync } = await import("node:fs");
  const dir = join(await here(), "stdlib");
  const models = existsSync(dir) &&
    readdirSync(dir, { recursive: true }).some((f) => /\.(sysml|kerml)$/.test(String(f)));
  if (!models)
    throw new LibraryUnavailable(
      "this copy of the SDK carries no standard library models (the npm package does); pass the " +
      `sysml.library directory of sysml_library-<version>.zip from https://github.com/${REPOSITORY}/releases as libraryDir`);
  return dir;
}

/**
 * The standard library as full-form JSON, for PayloadLibrary: the file SYSML_LIBRARY_JSON names,
 * else the copy in the cache (`cacheDir`, else SYSML_CACHE_DIR, else the user's cache directory),
 * downloaded from the GitHub release of `version` (default: this SDK's) on the first call and checked
 * against the SHA-256 GitHub states for it. Resolves to the file's path.
 */
export async function standardLibraryJson({ version, cacheDir, fetch: get = globalThis.fetch } = {}) {
  nodeOnly("standardLibraryJson()");
  const { existsSync, statSync, mkdirSync, mkdtempSync, rmSync, renameSync, writeFileSync, readFileSync } =
    await import("node:fs");
  const { join } = await import("node:path");
  const named = process.env[JSON_VARIABLE];
  if (named) {
    if (!existsSync(named) || !statSync(named).isFile())
      throw new LibraryUnavailable(`${JSON_VARIABLE} names ${named}, which is not a file`);
    return named;
  }
  version ??= JSON.parse(readFileSync(join(await here(), "package.json"), "utf8")).version;
  const stem = `sysml_library-${version}`;
  const root = cacheDir ?? (await cacheRoot());
  const target = join(root, stem);
  const found = join(target, "sysml.library.full.json");
  if (existsSync(found)) return found;

  const page = `https://github.com/${REPOSITORY}/releases/tag/v${version}`;
  const byHand = `download ${stem}.zip from ${page}, unpack it, and set ${JSON_VARIABLE} to the sysml.library.full.json it holds`;
  const headers = { "User-Agent": "sysml-sdk" };
  let release;
  try {
    const r = await get(`https://api.github.com/repos/${REPOSITORY}/releases/tags/v${version}`,
      { headers: { ...headers, Accept: "application/vnd.github+json" } });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
    release = await r.json();
  } catch (e) {
    throw new LibraryUnavailable(`cannot read the release v${version} of ${REPOSITORY} (${e.message}); ${byHand}`);
  }
  const asset = (release.assets ?? []).find((a) => a.name === `${stem}.zip`);
  if (!asset) throw new LibraryUnavailable(`the release v${version} of ${REPOSITORY} has no ${stem}.zip (${page})`);
  const digest = asset.digest ?? "";
  if (!digest.startsWith("sha256:"))
    throw new LibraryUnavailable(`GitHub states no SHA-256 for ${stem}.zip, so it is not downloaded; ${byHand}`);

  let bytes;
  try {
    const r = await get(asset.browser_download_url, { headers: { ...headers, Accept: "application/octet-stream" } });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
    bytes = Buffer.from(await r.arrayBuffer());
  } catch (e) {
    throw new LibraryUnavailable(`cannot download ${asset.browser_download_url} (${e.message}); ${byHand}`);
  }
  const { createHash } = await import("node:crypto");
  const sha256 = createHash("sha256").update(bytes).digest("hex");
  const want = digest.slice("sha256:".length);
  if (sha256 !== want)
    throw new LibraryUnavailable(`${stem}.zip does not match the SHA-256 GitHub states for it (${sha256}, not ${want}); nothing was kept`);

  let entries;
  try {
    entries = await unzip(bytes, ["sysml.library.full.json", "LICENSE", "NOTICE", "README.txt"].map((n) => `${stem}/${n}`));
  } catch (e) {
    throw new LibraryUnavailable(`${stem}.zip is not the archive the SDK expects (${e.message}); ${byHand}`);
  }
  mkdirSync(root, { recursive: true });
  const work = mkdtempSync(join(root, `${stem}-`));
  try {
    mkdirSync(join(work, stem));
    for (const [name, data] of entries) writeFileSync(join(work, name), data);
    if (existsSync(target)) rmSync(target, { recursive: true, force: true }); // left incomplete earlier
    renameSync(join(work, stem), target);
  } finally {
    rmSync(work, { recursive: true, force: true });
  }
  return found;
}

async function cacheRoot() {
  const { join } = await import("node:path");
  const { homedir } = await import("node:os");
  const env = process.env;
  if (env[CACHE_VARIABLE]) return env[CACHE_VARIABLE];
  if (process.platform === "win32") return join(env.LOCALAPPDATA || join(homedir(), "AppData", "Local"), "sysml", "Cache");
  if (process.platform === "darwin") return join(homedir(), "Library", "Caches", "sysml");
  return join(env.XDG_CACHE_HOME || join(homedir(), ".cache"), "sysml");
}

/** The named entries of a zip archive (stored or deflated, as Python's zipfile writes them), as
 * [name, Buffer] pairs; throws if one is missing. */
async function unzip(zip, names) {
  const { inflateRawSync } = await import("node:zlib");
  let end = zip.length - 22;
  while (end >= 0 && zip.readUInt32LE(end) !== 0x06054b50) end--;
  if (end < 0) throw new Error("no end of central directory");
  const count = zip.readUInt16LE(end + 10);
  let p = zip.readUInt32LE(end + 16);
  const found = new Map();
  for (let i = 0; i < count; i++) {
    if (zip.readUInt32LE(p) !== 0x02014b50) throw new Error("bad central directory");
    const method = zip.readUInt16LE(p + 10), size = zip.readUInt32LE(p + 20);
    const nameLen = zip.readUInt16LE(p + 28), extraLen = zip.readUInt16LE(p + 30), commentLen = zip.readUInt16LE(p + 32);
    const local = zip.readUInt32LE(p + 42);
    const name = zip.toString("utf8", p + 46, p + 46 + nameLen);
    if (names.includes(name)) {
      const start = local + 30 + zip.readUInt16LE(local + 26) + zip.readUInt16LE(local + 28);
      const raw = zip.subarray(start, start + size);
      if (method === 0) found.set(name, raw);
      else if (method === 8) found.set(name, inflateRawSync(raw));
      else throw new Error(`${name}: compression method ${method}`);
    }
    p += 46 + nameLen + extraLen + commentLen;
  }
  const missing = names.filter((n) => !found.has(n));
  if (missing.length) throw new Error(`no ${missing.join(", ")}`);
  return names.map((n) => [n, found.get(n)]);
}
