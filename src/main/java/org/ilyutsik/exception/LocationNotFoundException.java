package org.ilyutsik.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String city) {
        super("City: " + city + " not found");
    }
}
