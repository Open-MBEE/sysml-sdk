package org.openmbee.sysml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal JSON parser (zero dependencies): objects become LinkedHashMap,
 * arrays ArrayList, numbers Long (integral) or Double, plus String,
 * Boolean, and null. Sufficient for full-form interchange payloads and
 * metamodel.json; not a general validating parser.
 */
public final class Json {
    private final String s;
    private int i;

    private Json(String s) {
        this.s = s;
    }

    public static Object parse(String text) {
        Json p = new Json(text);
        p.ws();
        Object v = p.value();
        p.ws();
        if (p.i != text.length())
            throw p.err("trailing data");
        return v;
    }

    private RuntimeException err(String what) {
        return new IllegalArgumentException("JSON: " + what + " at offset " + i);
    }

    private void ws() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i)))
            i++;
    }

    private char peek() {
        if (i >= s.length())
            throw err("unexpected end of input");
        return s.charAt(i);
    }

    private void expect(char c) {
        if (peek() != c)
            throw err("expected '" + c + "'");
        i++;
    }

    private Object value() {
        return switch (peek()) {
            case '{' -> object();
            case '[' -> array();
            case '"' -> string();
            case 't' -> literal("true", Boolean.TRUE);
            case 'f' -> literal("false", Boolean.FALSE);
            case 'n' -> literal("null", null);
            default -> number();
        };
    }

    private Object literal(String lit, Object value) {
        if (!s.startsWith(lit, i))
            throw err("bad literal");
        i += lit.length();
        return value;
    }

    private Map<String, Object> object() {
        expect('{');
        ws();
        Map<String, Object> m = new LinkedHashMap<>();
        if (peek() == '}') {
            i++;
            return m;
        }
        while (true) {
            String k = string();
            ws();
            expect(':');
            ws();
            m.put(k, value());
            ws();
            if (peek() == ',') {
                i++;
                ws();
                continue;
            }
            expect('}');
            return m;
        }
    }

    private List<Object> array() {
        expect('[');
        ws();
        List<Object> a = new ArrayList<>();
        if (peek() == ']') {
            i++;
            return a;
        }
        while (true) {
            a.add(value());
            ws();
            if (peek() == ',') {
                i++;
                ws();
                continue;
            }
            expect(']');
            return a;
        }
    }

    private String string() {
        expect('"');
        StringBuilder b = new StringBuilder();
        while (true) {
            char c = peek();
            i++;
            if (c == '"')
                return b.toString();
            if (c != '\\') {
                b.append(c);
                continue;
            }
            char e = peek();
            i++;
            switch (e) {
                case '"' -> b.append('"');
                case '\\' -> b.append('\\');
                case '/' -> b.append('/');
                case 'b' -> b.append('\b');
                case 'f' -> b.append('\f');
                case 'n' -> b.append('\n');
                case 'r' -> b.append('\r');
                case 't' -> b.append('\t');
                case 'u' -> {
                    if (i + 4 > s.length())
                        throw err("truncated \\u escape");
                    b.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                    i += 4;
                }
                default -> throw err("bad escape '\\" + e + "'");
            }
        }
    }

    private Object number() {
        int start = i;
        if (peek() == '-')
            i++;
        while (i < s.length() && "0123456789+-.eE".indexOf(s.charAt(i)) >= 0)
            i++;
        String t = s.substring(start, i);
        if (t.indexOf('.') < 0 && t.indexOf('e') < 0 && t.indexOf('E') < 0) {
            try {
                return Long.parseLong(t);
            } catch (NumberFormatException ignore) {
                // fall through to double
            }
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw err("bad number '" + t + "'");
        }
    }
}
