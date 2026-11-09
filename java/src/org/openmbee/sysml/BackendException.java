package org.openmbee.sysml;

/** The engine behind a backend could not answer: it failed while computing a value, or it is not
 * available (not installed, not downloadable, or no suitable runtime). The message says which
 * engine and why. */
public class BackendException extends SdkException {
    public BackendException(String message) {
        super(message);
    }
}
