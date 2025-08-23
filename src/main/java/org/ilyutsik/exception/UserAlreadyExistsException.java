package org.ilyutsik.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RegistrationException {
    public UserAlreadyExistsException(String login) {
        super("User " + login + " already exists");
    }
}
