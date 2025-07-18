package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.SessionExpiredException;
import org.ilyutsik.exeption.SessionNotFoundException;
import org.ilyutsik.model.User;
import org.ilyutsik.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.Optional;


@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController extends BaseController {

    private final SessionService sessionService;

    @GetMapping
    public String home(HttpServletRequest request, Model model) {
        isLoginUser(request, model);
        if (request.getCookies() == null) {
            return "redirect:/authorization";
        }
        Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "session-token".equals(cookie.getName())).findFirst();

        if (tokenCookie.isEmpty()) {
            return "redirect:/authorization";
        }
        String token = tokenCookie.get().getValue();
        try {
            User user = sessionService.getUser(token);
            model.addAttribute("user", user);
            return "home";
        } catch (SessionNotFoundException | SessionExpiredException ex) {
            return "redirect:/authorization";
        }

    }

}
