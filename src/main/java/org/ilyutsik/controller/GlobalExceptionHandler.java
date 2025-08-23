package org.ilyutsik.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.ilyutsik.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public String handleAuthError(AuthorizationException e, Model model,  HttpServletResponse response) {
        model.addAttribute("apiError", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "authorization";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundError(UserNotFoundException e, Model model, HttpServletResponse response) {
        model.addAttribute("apiError", e.getMessage());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "authorization";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPasswordError(InvalidPasswordException e, Model model, HttpServletResponse response) {
        model.addAttribute("apiError", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "authorization";
    }

    @ExceptionHandler(RegistrationException.class)
    public String handleRegistrationError(RegistrationException e, Model model, HttpServletResponse response) {
        model.addAttribute("apiError", e.getMessage());
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return "registration";
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public String handleWeatherNotFoundError(WeatherNotFoundException e, Model model) {
        model.addAttribute("apiError", e.getMessage());
        return "home";
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public String handleLocationNotFoundError(LocationNotFoundException e, Model model) {
        model.addAttribute("apiError", e.getMessage());
        return "home";
    }


}
