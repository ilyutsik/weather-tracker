package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.service.SessionService;
import org.ilyutsik.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.Optional;


@Controller
@RequestMapping("/logout")
public class LogoutController extends BaseController{

    public LogoutController(SessionService sessionService, UserService userService) {
        super(sessionService, userService);
    }

    @GetMapping
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) {
            return "redirect:/authorization";
        }
        Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "session-token".equals(cookie.getName())).findFirst();

        if (tokenCookie.isPresent()) {
            Cookie emptyCookie = sessionService.logout(tokenCookie.get().getValue());
            response.addCookie(emptyCookie);
        }
        return "redirect:/authorization";
    }

}
