package org.ilyutsik.exeption;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String login) {
        super("User " + login + " already exists");
    }
}
