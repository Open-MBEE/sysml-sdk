// The worker thread behind EngineBackend (engine.mjs): it runs the engine's process and relays
// lines both ways, so that the main thread can wait for each answer synchronously. Every
// line the process writes, and its exit, is posted to `port`; then the counter in `signal` is
// incremented and the main thread woken.
import { spawn } from "node:child_process";
import { createInterface } from "node:readline";
import { parentPort, workerData } from "node:worker_threads";

const { command, args, port, signal } = workerData;
const counter = new Int32Array(signal);

function post(message) {
  port.postMessage(message);
  Atomics.add(counter, 0, 1);
  Atomics.notify(counter, 0);
}

const child = spawn(command, args, { stdio: ["pipe", "pipe", "inherit"], windowsHide: true });
child.on("error", (e) => post({ failed: String(e.message ?? e) }));
child.on("exit", (code) => post({ exit: code }));
createInterface({ input: child.stdout, crlfDelay: Infinity }).on("line", (line) => post({ line }));
child.stdin.on("error", () => {});  // the process ended: its exit is reported
parentPort.on("message", (line) => {
  if (line === null) child.stdin.end();
  else child.stdin.write(line + "\n");
});
