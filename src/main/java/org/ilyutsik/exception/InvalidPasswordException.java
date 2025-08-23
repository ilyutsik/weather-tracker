package org.ilyutsik.exception;

public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
