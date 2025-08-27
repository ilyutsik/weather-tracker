package org.ilyutsik.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ilyutsik.service.SessionService;
import org.ilyutsik.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/registration")
public class RegistrationController extends BaseController {

    public RegistrationController(SessionService sessionService, UserService userService) {
        super(sessionService, userService);
    }

    @GetMapping()
    public String registrationGet(Model model) {
        return "registration";
    }

    @PostMapping()
    public String registrationPost(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("repeatPassword") String repeatPassword, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (login.length() < 5) {
            model.addAttribute("apiError", "minimum login length is 5 characters");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "registration";
        }
        if (password.length() < 5) {
            model.addAttribute("apiError", "minimum password length is 5 characters");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "registration";
        }
        if (!password.equals(repeatPassword)) {
            model.addAttribute("login", login);
            model.addAttribute("apiError", "Passwords do not match");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "registration";
        }
            userService.register(login, password);

            return "redirect:/authorization";

    }

}
