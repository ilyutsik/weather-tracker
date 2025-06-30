package org.ilyutsik.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.UserAlreadyExistsException;
import org.ilyutsik.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController extends BaseController {

    private final UserService userService;

    @GetMapping()
    public String registrationGet(Model model) {
        return "registration";
    }

    @PostMapping()
    public String registrationPost(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("repeatPassword") String repeatPassword, HttpServletRequest request, Model model) {
        isLoginUser(request, model);
        if (!password.equals(repeatPassword)) {
            model.addAttribute("login", login);
            model.addAttribute("error", "Passwords do not match");
            return "registration";
        }
        try {
            userService.register(login, password);
            return "redirect:/authorization";
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registration";
        }
    }

}
