package com.geovis.core.util;

import java.security.SecureRandom;
import java.util.Random;

public class YzmUtils {


    public static char[] charArray() {
        int i = 1234567890;
        String s = "qwertyuiopasdfghjklzxcvbnm";
        String S = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String word = s + S + i;
        char[] c = word.toCharArray();
        return c;
    }

    public static String verifyCode() {
        char[] c = charArray();
        Random random = new SecureRandom();
        String code = "";
        for (int i = 0; i < 18; i++) {
            code += c[random.nextInt(c.length)];
        }
        return code;
    }
}
