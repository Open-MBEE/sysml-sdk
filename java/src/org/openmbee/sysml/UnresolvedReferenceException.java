package org.openmbee.sysml;

/** A reference that did not resolve — a SysML Toolkit {@code @ref}
 * spelling passthrough (non-standard, best-effort) or a dangling
 * {@code @id}. Typed navigation throws: the SDK requires resolved
 * models. {@code $raw()} still exposes the raw value. */
public final class UnresolvedReferenceException extends SdkException {
    public UnresolvedReferenceException(String message) {
        super(message);
    }
}
