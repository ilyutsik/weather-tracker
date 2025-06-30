package org.ilyutsik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Optional;

public abstract class BaseController {

    public void isLoginUser(HttpServletRequest request, Model model) {
        Optional<Cookie> tokenCookie = Optional.empty();
        if (request.getCookies() != null) {
            tokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("session-token")).findFirst();

        }
        if (tokenCookie.isPresent()) {
            model.addAttribute("isLogin", true);
        }
    }
}
