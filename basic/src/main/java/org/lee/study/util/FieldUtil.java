package org.lee.study.util;

import java.lang.reflect.Field;

public class FieldUtil {
    public static <T, V, O> V getValue(Class<T> tClass, String fieldName, O object) {
        try {
            Field field = String.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            return (V)value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
