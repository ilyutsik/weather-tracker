package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.model.User;
import org.ilyutsik.service.LocationService;
import org.ilyutsik.service.SessionService;
import org.ilyutsik.service.UserService;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseController {

    public final SessionService sessionService;
    public final UserService userService;

    public Optional<User> checkUserAuthorization(HttpServletRequest request, Model model) {
        Optional<Cookie> tokenCookie = Optional.empty();
        if (request.getCookies() != null) {
            tokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("session-token")).findFirst();

        }
        if (tokenCookie.isPresent()) {
            model.addAttribute("isLogin", true);
            User user = sessionService.getUser(tokenCookie.get().getValue());
            model.addAttribute("user", user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

}
