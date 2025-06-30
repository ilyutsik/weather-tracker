package org.ilyutsik.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return ENCODER.encode(password);
    }

    public static boolean matchPassword(String password, String hashedPassword) {
        return ENCODER.matches(password, hashedPassword);
    }

}
