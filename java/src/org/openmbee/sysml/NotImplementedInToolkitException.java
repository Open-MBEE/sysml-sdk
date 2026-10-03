package org.openmbee.sysml;

/** A specification member the SysML Toolkit does not answer: thrown when the SysML Toolkit reports
 * the member as not implemented, and for every operation on a payload model. */
public final class NotImplementedInToolkitException extends SdkException {
    public NotImplementedInToolkitException(String message) {
        super(message);
    }
}
