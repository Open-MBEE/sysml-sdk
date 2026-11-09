package org.openmbee.sysml;

/** A specification member the backend does not answer for this element: the SysML Toolkit's refusals
 * ({@link NotImplementedInToolkitException}), and an engine's. Catch it to handle every backend's
 * refusals alike. */
public class NotAnsweredException extends SdkException {
    public NotAnsweredException(String message) {
        super(message);
    }
}
