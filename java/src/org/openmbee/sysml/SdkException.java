package org.openmbee.sysml;

/** Base of all SDK-specific errors. */
public class SdkException extends RuntimeException {
    public SdkException(String message) {
        super(message);
    }
}
