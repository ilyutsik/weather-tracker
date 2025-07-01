package org.ilyutsik.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordEncoder {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String hash(String password) {
        return ENCODER.encode(password);
    }

    public static boolean match(String password, String hashedPassword) {
        return ENCODER.matches(password, hashedPassword);
    }

}
