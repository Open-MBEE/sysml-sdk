package org.openmbee.sysml;

/** A property the backend reports it does not compute. Reserved for a
 * backend that says so itself; never inferred from an empty result. */
public final class NotComputedException extends SdkException {
    public NotComputedException(String message) {
        super(message);
    }
}
