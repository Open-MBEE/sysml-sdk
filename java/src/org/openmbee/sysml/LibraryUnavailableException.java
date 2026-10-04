package org.openmbee.sysml;

/** The standard library is not at hand: this jar carries no models, or the JSON could not be
 *  downloaded and checked. */
public class LibraryUnavailableException extends SdkException {
    public LibraryUnavailableException(String message) {
        super(message);
    }
}
