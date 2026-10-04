// The standard library helpers, offline: GitHub is replaced by a fake fetch that serves the release
// metadata and the library archive from memory. Run with `node ts/library_checks.mjs`.

import { createHash } from "node:crypto";
import { mkdirSync, mkdtempSync, readFileSync, readdirSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { deflateRawSync } from "node:zlib";
import { LibraryUnavailable, standardLibrary, standardLibraryJson } from "./library.mjs";

const VERSION = "9.9.9";
const STEM = `sysml_library-${VERSION}`;
const API = `https://api.github.com/repos/Open-MBEE/sysml-sdk/releases/tags/v${VERSION}`;
const ASSET = `https://github.com/Open-MBEE/sysml-sdk/releases/download/v${VERSION}/${STEM}.zip`;
let failures = 0;

function check(cond, what) {
  if (!cond) { failures++; console.log(`FAIL: ${what}`); } else console.log(`ok: ${what}`);
}

async function rejects(promise, pattern, what) {
  try { await promise; check(false, `${what} (no error)`); }
  catch (e) { check(e instanceof LibraryUnavailable && pattern.test(e.message), `${what}: ${e.message.slice(0, 90)}`); }
}

/** A zip of deflated entries, as Python's zipfile writes them (CRCs left 0: the reader does not check them). */
function zip(entries) {
  const locals = [], central = [];
  let offset = 0;
  for (const [name, text] of entries) {
    const n = Buffer.from(name), data = deflateRawSync(Buffer.from(text));
    const local = Buffer.alloc(30);
    local.writeUInt32LE(0x04034b50, 0); local.writeUInt16LE(8, 8);
    local.writeUInt32LE(data.length, 18); local.writeUInt32LE(text.length, 22); local.writeUInt16LE(n.length, 26);
    const dir = Buffer.alloc(46);
    dir.writeUInt32LE(0x02014b50, 0); dir.writeUInt16LE(8, 10);
    dir.writeUInt32LE(data.length, 20); dir.writeUInt32LE(text.length, 24); dir.writeUInt16LE(n.length, 28);
    dir.writeUInt32LE(offset, 42);
    locals.push(local, n, data); central.push(dir, n);
    offset += 30 + n.length + data.length;
  }
  const dir = Buffer.concat(central), end = Buffer.alloc(22);
  end.writeUInt32LE(0x06054b50, 0); end.writeUInt16LE(entries.length, 8); end.writeUInt16LE(entries.length, 10);
  end.writeUInt32LE(dir.length, 12); end.writeUInt32LE(offset, 16);
  return Buffer.concat([...locals, dir, end]);
}

const archive = (json = '[{"@id": "lib"}]') =>
  zip([["sysml.library.full.json", json], ["LICENSE", "EPL"], ["NOTICE", "notice"], ["README.txt", "readme"]]
    .map(([n, t]) => [`${STEM}/${n}`, t]));

/** A fake GitHub serving `served` (URL to bytes, or an Error to throw); `asked` lists the URLs. */
function github(served) {
  const asked = [];
  const fetch = async (url) => {
    asked.push(url);
    const answer = served[url];
    if (answer === undefined || answer instanceof Error) throw answer ?? new TypeError("fetch failed");
    return new Response(answer, { status: 200 });
  };
  return { fetch, asked };
}

function release(data, digest = "sha256:" + createHash("sha256").update(data).digest("hex")) {
  return { [API]: JSON.stringify({ assets: [{ name: `${STEM}.zip`, browser_download_url: ASSET, digest }] }), [ASSET]: data };
}

const tmp = () => mkdtempSync(join(tmpdir(), "sysml-library-"));
delete process.env.SYSML_LIBRARY_JSON;

{
  const dir = tmp(), g = github(release(archive()));
  const path = await standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: g.fetch });
  check(path === join(dir, STEM, "sysml.library.full.json"), "the JSON lands in the cache");
  check(readFileSync(path, "utf8") === '[{"@id": "lib"}]', "the JSON is the archive's");
  check(readdirSync(join(dir, STEM)).sort().join() === "LICENSE,NOTICE,README.txt,sysml.library.full.json", "with its license and notice");
  check(readdirSync(dir).join() === STEM, "no work files left behind");
  const again = github({});
  check(await standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: again.fetch }) === path && again.asked.length === 0,
    "the second call reads the cache without asking GitHub");
  rmSync(dir, { recursive: true });
}
{
  const dir = tmp();
  await rejects(standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: github(release(archive(), "sha256:" + "0".repeat(64))).fetch }),
    /does not match the SHA-256/, "a digest mismatch");
  check(readdirSync(dir).length === 0, "a digest mismatch keeps nothing");
  rmSync(dir, { recursive: true });
}
{
  const dir = tmp(), g = github(release(archive(), ""));
  await rejects(standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: g.fetch }), /states no SHA-256/, "no digest");
  check(g.asked.length === 1, "no digest, no download");
  await rejects(standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: github({ [API]: '{"assets": []}' }).fetch }),
    new RegExp(`has no ${STEM}\\.zip`), "no such asset");
  await rejects(standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: github({}).fetch }),
    /SYSML_LIBRARY_JSON.*releases\/tag\/v9\.9\.9|releases\/tag\/v9\.9\.9.*SYSML_LIBRARY_JSON/, "offline: says what to do");
  await rejects(standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: github(release(zip([["other.txt", "x"]]))).fetch }),
    /not the archive/, "not the expected archive");
  rmSync(dir, { recursive: true });
}
{
  const dir = tmp();
  mkdirSync(join(dir, STEM));
  writeFileSync(join(dir, STEM, "LICENSE"), "left over");
  const path = await standardLibraryJson({ version: VERSION, cacheDir: dir, fetch: github(release(archive())).fetch });
  check(readFileSync(join(dir, STEM, "LICENSE"), "utf8") === "EPL" && readFileSync(path, "utf8").length > 0, "an incomplete copy is replaced");
  rmSync(dir, { recursive: true });
}
{
  const dir = tmp(), copy = join(dir, "library.json");
  writeFileSync(copy, "[]");
  process.env.SYSML_LIBRARY_JSON = copy;
  const g = github({});
  check(await standardLibraryJson({ fetch: g.fetch }) === copy && g.asked.length === 0, "SYSML_LIBRARY_JSON names a copy");
  process.env.SYSML_LIBRARY_JSON = join(dir, "missing.json");
  await rejects(standardLibraryJson({ fetch: g.fetch }), /not a file/, "SYSML_LIBRARY_JSON names nothing");
  delete process.env.SYSML_LIBRARY_JSON;
  process.env.SYSML_CACHE_DIR = dir;
  check(await standardLibraryJson({ version: VERSION, fetch: github(release(archive())).fetch }) === join(dir, STEM, "sysml.library.full.json"),
    "SYSML_CACHE_DIR names the cache");
  delete process.env.SYSML_CACHE_DIR;
  rmSync(dir, { recursive: true });
}
try {
  const dir = await standardLibrary();
  check(readdirSync(dir).includes("LICENSE"), `standardLibrary(): ${dir}`);
} catch (e) {
  check(e instanceof LibraryUnavailable && /carries no standard library models/.test(e.message),
    "standardLibrary() in a checkout without the models says so");
}

if (failures) { console.log(`${failures} LIBRARY CHECK(S) FAILED`); process.exit(1); }
console.log("ALL JS LIBRARY CHECKS PASSED");
