package com.ckr.otms.common.util;

/**
 * A util class that handle processing about array.
 */
public class ArrayUtil {

    /**
     * Print all elements in an array into a String. If an element is an array as well, this element will be
     * printed recursively.
     * @param array An array that should be printed.
     * @return An String that can show all objects in an array.
     */
    public static String toString(Object[] array) {

        if (array == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder("[");

        for (int i = 0; i < array.length; i++) {
            Object obj = array[i];

            if (obj instanceof Object[]) {
                buf.append(ArrayUtil.toString((Object[]) obj));
            }

            buf.append(obj.toString());

            if (i < array.length - 1) {
                buf.append(", ");
            }
        }

        buf.append("]");

        return buf.toString();

    }

}
