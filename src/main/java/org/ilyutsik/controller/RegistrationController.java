package org.ilyutsik.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ilyutsik.exeption.UserAlreadyExistsException;
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
        checkUserAuthorization(request, model);
        if (!password.equals(repeatPassword)) {
            model.addAttribute("login", login);
            model.addAttribute("error", "Passwords do not match");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "registration";
        }
        try {
            userService.register(login, password);
            return "redirect:/authorization";
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "registration";
        }
    }

}
