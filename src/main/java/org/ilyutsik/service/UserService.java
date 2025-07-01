package org.ilyutsik.service;

import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.InvalidPasswordException;
import org.ilyutsik.exeption.UserAlreadyExistsException;
import org.ilyutsik.exeption.UserNotFoundException;
import org.ilyutsik.model.User;
import org.ilyutsik.repository.UserRepository;
import org.ilyutsik.util.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(String login, String password) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.getUserByLogin(login);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(login);
        }
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        userRepository.save(newUser);
    }

    public User authenticate(String login, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<User> optionalUser = userRepository.getUserByLogin(login);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(login);
        }
        if (!PasswordEncoder.match(password, optionalUser.get().getPassword())) {
            throw new InvalidPasswordException();
        }
        return optionalUser.get();
    }

}
