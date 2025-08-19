package org.ilyutsik.exception;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException(String city) {
        super("City: " + city + " not found");
    }
}
