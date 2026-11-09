package org.openmbee.sysml.pilot;

import org.openmbee.sysml.BackendException;

/**
 * An answer the Pilot engine refuses to give, with the kind the request protocol names:
 * {@code gone} (no element has this handle), {@code notApplicable} (the element's metaclass has no
 * such property or operation), {@code unresolved} (the value refers to an element the Pilot could not
 * resolve), {@code refused} (a convenience member this engine does not answer), {@code invalid} (a
 * convenience member whose contract gives no answer here), {@code failed} (the Pilot raised an
 * exception), {@code unavailable} (the Pilot cannot be found, downloaded or loaded), {@code badRequest}
 * (a request the protocol does not define).
 */
public final class PilotException extends BackendException {
    private final String kind;

    public PilotException(String kind, String message) {
        super(message);
        this.kind = kind;
    }

    public String kind() {
        return kind;
    }
}
