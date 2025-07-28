package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class AuthorizationController extends BaseController {

    public AuthorizationController(SessionService sessionService, UserService userService) {
        super(sessionService, userService);
    }

    @GetMapping
    public String authorizationGet(Model model) {
        return "authorization";
    }

    @PostMapping
    public String authorizationPost(@RequestParam("login") String login, @RequestParam("password") String password,
                                    Model model, HttpServletRequest request, HttpServletResponse response) {
        checkUserAuthorization(request, model);
        try {
            User user = userService.authenticate(login, password);
            Cookie cookie =  sessionService.startSession(user);
            response.addCookie(cookie);
            return "redirect:/home";
        } catch (UserNotFoundException | InvalidPasswordException ex) {
            if (ex instanceof InvalidPasswordException) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            if (ex instanceof UserNotFoundException) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            model.addAttribute("login", login);
            model.addAttribute("error", ex.getMessage());
            return "authorization";
        }
    }

}
