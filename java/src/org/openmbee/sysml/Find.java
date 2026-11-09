package org.openmbee.sysml;

/**
 * A convenience member as the generated classes call it (tools/naming.py, convenience_runtime):
 * its name, its binding library symbol suffix, its parameters' names and kinds (scope, visibility,
 * bool, str, element), and its result (elements, element, bool, int, unlimited, real, strings,
 * literal). The int, unlimited and literal results answer a {@link Literal}.
 */
public record Find(String name, String symbol, String[] paramNames, String[] paramKinds, String result) {
}
