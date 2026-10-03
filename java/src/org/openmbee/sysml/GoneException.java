package org.openmbee.sysml;

/** The element no longer exists in the backend's current state. */
public final class GoneException extends SdkException {
    public GoneException(String message) {
        super(message);
    }
}
