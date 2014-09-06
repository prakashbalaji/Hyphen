package com.hyphen;

import java.lang.reflect.Field;

public class FieldHelper {
    public static <O> Object accessField (String fieldName, O o) {
        Field field = null;
        try {
            field = o.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        field.setAccessible(true);
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("The field not accessible");
        }
    }
}
