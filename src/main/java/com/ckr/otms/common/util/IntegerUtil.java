package com.ckr.otms.common.util;

public class IntegerUtil {

    /**
     * Parse an string into Integer.
     *
     * @param str An String will be parsed.
     * @return If the string is a valid number, return an Integer object for that number. If not, return null.
     */
    public static Integer parse(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException numExp) {
            return null;
        }

    }
}
