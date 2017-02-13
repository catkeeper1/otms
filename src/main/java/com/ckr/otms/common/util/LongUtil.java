package com.ckr.otms.common.util;

/**
 * An util class that handle Long object processing.
 */
public class LongUtil {

    /**
     * Parse an string into a Long object.
     * @param str An string that is parsed.
     * @return If str is a valid number, return the corresponding Long object. Otherwise, return null.
     */
    public static Long parse(String str) {

        try {
            return Long.valueOf(str);
        } catch (NumberFormatException numExp) {
            return null;
        }

    }
}
