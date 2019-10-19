package com.juck.ioc.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ReflectionUtil {

    public static Object newInstance(Class<?> clazz) {
            Object obj = null;
        try {
            obj = clazz.newInstance();
            log.info("{} initialized.", clazz.getName());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("failed to initialize {}, due to: {}", clazz.getName(), e.getMessage());
            throw new RuntimeException(e);
        }

        return obj;
    }

    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
            log.info("set value: {} for {}'s field: {}", value, obj.getClass().getSimpleName(), field.getName());
        } catch (IllegalAccessException e) {
            log.error("failed to set value [{}] for Object: {}'s field: [{}].", value, obj.getClass().getSimpleName(), field.getName());
            throw new RuntimeException("failed to set value: " + value + "for " + field.getName());
        }
    }
}
