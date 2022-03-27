package org.lee.util;

public class StringUtil {
    public static boolean isEmpty(String o){
        return o == null || "".equals(o) || "".equals(o.trim());
    }
}
