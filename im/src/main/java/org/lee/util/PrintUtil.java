package org.lee.util;

import org.lee.domain.MessageProto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrintUtil {

    public static <T> void printProtoObject(T out) throws Exception {
        System.out.println(toString(out));
    }

    public static <T> String toString(T out) throws Exception {

        if (out == null) {
            System.out.println();
            return "";
        }
        Class<?> clazz = out.getClass();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder outStr = new StringBuilder();
        outStr.append(clazz.getSimpleName()).append("{");
        int i = 0;

        for (; i < fields.length; i++) {
            try {
                Field field = fields[i];
                outStr.append(generateStr(field, out)).append(",");
            } catch (Exception e) {
                continue;
            }
        }
        outStr.replace(outStr.length()-1,outStr.length(),"}");
        return outStr.toString();
    }

    public static <T> String generateStr(Field field, T out) throws Exception {
        String name = field.getName();
        String firstWord = name.substring(0, 1).toUpperCase();
        Method method = out.getClass().getMethod("get" + firstWord + name.substring(1, name.length() - 1));
        return name.substring(0, name.length() - 1) + " :" + method.invoke(out);
    }
}
