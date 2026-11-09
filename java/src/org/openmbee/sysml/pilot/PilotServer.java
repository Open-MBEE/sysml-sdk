package org.openmbee.sysml.pilot;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openmbee.sysml.Json;

/**
 * The Pilot engine as a process: one request of {@link PilotEngine}'s protocol per line on standard
 * input, one answer per line on standard output, until {@code close} or the end of the input. The
 * first line it writes says whether the Pilot is ready:
 * {@code {"ready": true, "engine": "Pilot Implementation", "version": version, "protocol": 1}}, or
 * {@code {"ready": false, "error": {"kind": "unavailable", "message": ...}}} before it exits with
 * status 1. Everything else (the Pilot's own output, download progress) goes to standard error.
 *
 * <pre>
 * java -cp sysml-pilot-server.jar org.openmbee.sysml.pilot.PilotServer [--jar JAR --library DIR | --home DIR]
 * </pre>
 * Without options the Pilot is {@link Pilot#locate()}'s.
 */
public final class PilotServer {
    private PilotServer() {}

    public static void main(String[] args) throws IOException {
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        System.setOut(System.err);  // nothing but answers on the protocol's stream
        String jar = null, library = null, home = null;
        for (int i = 0; i + 1 < args.length; i += 2) {
            switch (args[i]) {
                case "--jar" -> jar = args[i + 1];
                case "--library" -> library = args[i + 1];
                case "--home" -> home = args[i + 1];
                default -> {
                    fail(out, "badRequest", "unknown option " + args[i]);
                    return;
                }
            }
        }
        PilotEngine engine;
        try {
            engine = home != null ? PilotEngine.startIn(home) : PilotEngine.start(jar, library);
        } catch (PilotException e) {
            fail(out, e.kind(), e.getMessage());
            return;
        }
        Map<String, Object> ready = new LinkedHashMap<>();
        ready.put("ready", true);
        ready.put("engine", PilotEngine.ENGINE);
        ready.put("version", engine.pilot().version());
        ready.put("protocol", PilotEngine.PROTOCOL);
        out.println(Json.write(ready));
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        for (String line; (line = in.readLine()) != null; ) {
            if (line.isBlank()) continue;
            String answer = engine.request(line);
            out.println(answer);
            if (isClose(line)) break;
        }
        engine.close();
        out.flush();
        System.exit(0);  // the Pilot leaves non-daemon threads behind
    }

    private static boolean isClose(String line) {
        try {
            return Json.parse(line) instanceof Map<?, ?> m && "close".equals(m.get("cmd"));
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void fail(PrintStream out, String kind, String message) {
        Map<String, Object> answer = new LinkedHashMap<>();
        answer.put("ready", false);
        answer.put("error", PilotEngine.error(kind, message));
        out.println(Json.write(answer));
        out.flush();
        System.exit(1);
    }
}
