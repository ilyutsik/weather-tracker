package org.ilyutsik.service;

import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.InvalidPasswordException;
import org.ilyutsik.exeption.UserAlreadyExistsException;
import org.ilyutsik.exeption.UserNotFoundException;
import org.ilyutsik.model.User;
import org.ilyutsik.repository.UserRepository;
import org.ilyutsik.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(String login, String password) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.getUserByLogin(login);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(login);
        }
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        userRepository.save(newUser);
    }

    public void authenticateUser(String login, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<User> optionalUser = userRepository.getUserByLogin(login);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(login);
        }
        if (!PasswordUtil.matchPassword(password, optionalUser.get().getPassword())) {
            throw new InvalidPasswordException();
        }
    }

}
