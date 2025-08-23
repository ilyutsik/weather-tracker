package org.ilyutsik.exception;

public class SessionExpiredException extends AuthorizationException {

    public SessionExpiredException() {
        super("Session expired");
    }
}
