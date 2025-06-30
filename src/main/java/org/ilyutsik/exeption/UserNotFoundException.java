package org.ilyutsik.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("User with login '" + login + "' does not exist");
    }
}
