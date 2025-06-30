package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.InvalidPasswordException;
import org.ilyutsik.exeption.UserNotFoundException;
import org.ilyutsik.model.User;
import org.ilyutsik.service.SessionService;
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
public class AuthorizationController extends BaseController {

    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping
    public String authorizationGet(Model model) {
        return "authorization";
    }

    @PostMapping
    public String authorizationPost(@RequestParam("login") String login, @RequestParam("password") String password,
                                    Model model, HttpServletRequest request, HttpServletResponse response) {
        isLoginUser(request, model);
        try {
            User user = userService.authenticate(login, password);
            Cookie cookie =  sessionService.startSession(user);
            response.addCookie(cookie);
            return "redirect:/home";
        } catch (UserNotFoundException | InvalidPasswordException ex) {
            model.addAttribute("login", login);
            model.addAttribute("error", ex.getMessage());
            return "authorization";
        }
    }

}
