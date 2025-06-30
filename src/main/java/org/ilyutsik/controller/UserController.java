package org.ilyutsik.controller;

import lombok.RequiredArgsConstructor;
import org.ilyutsik.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller()
@RequestMapping("/")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "Привет, Thymeleaf!");
        return "registration";
    }

}
