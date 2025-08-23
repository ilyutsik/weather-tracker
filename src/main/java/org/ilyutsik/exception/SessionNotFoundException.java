package org.ilyutsik.exception;

public class SessionNotFoundException extends AuthorizationException {

    public SessionNotFoundException() {
        super("Session not found");
    }
}
