package com.ckr.otms.common.util;

public class ArrayUtil {

    public static String toString(Object[] objs) {

        if (objs == null) {
            return null;
        }

        StringBuffer buf = new StringBuffer("[");

        for (int i = 0; i < objs.length; i++) {
            Object obj = objs[i];

            if (obj instanceof Object[]) {
                buf.append(ArrayUtil.toString((Object[]) obj));
            }

            buf.append(obj.toString());

            if (i < objs.length - 1) {
                buf.append(", ");
            }
        }

        buf.append("]");

        return buf.toString();

    }

}
