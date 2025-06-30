package org.ilyutsik.controller;

import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.InvalidPasswordException;
import org.ilyutsik.exeption.UserNotFoundException;
import org.ilyutsik.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final UserService userService;

    @GetMapping
    public String authorizationGet(Model model) {
        return "authorization";
    }

    @PostMapping
    public String authorizationPost(@RequestParam("login") String login, @RequestParam("password") String password, Model model) {
        try {
            userService.authenticateUser(login, password);
            return "redirect:/home";
        } catch (UserNotFoundException | InvalidPasswordException ex) {
            model.addAttribute("login", login);
            model.addAttribute("error", ex.getMessage());
            return "authorization";
        }
    }

}
