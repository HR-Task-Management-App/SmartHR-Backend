package com.hrms.backend.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class CodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateBase64Code() {
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return base64.length() >= 7 ? base64.substring(0, 7) : generateBase64Code();
    }
}