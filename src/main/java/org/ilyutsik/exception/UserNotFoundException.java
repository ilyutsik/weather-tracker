package org.ilyutsik.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("User with login '" + login + "' does not exist");
    }
}
