package com.juck.ioc.utils;

import com.juck.ioc.Constants;
import com.juck.ioc.annotation.Controller;
import com.juck.ioc.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassHelper {
    private static Set<Class<?>> CLASS_SET = new HashSet<>();

    static {
        String basePackage = System.getProperty(Constants.SYS_PROP_KEY);
        CLASS_SET = ClassUtil.getClassUnderPackage(basePackage);
    }

    public static Set<Class<?>> getServiceClassSet() {
        return getClassByType(Service.class);
    }

    public static Set<Class<?>> getControllerClassSet() {
        return getClassByType(Controller.class);
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> result = new HashSet<>();

        result.addAll(getControllerClassSet());
        result.addAll(getServiceClassSet());

        return result;
    }

    private static Set<Class<?>> getClassByType(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> result = new HashSet<>();

        if (CLASS_SET.isEmpty()) {
            return result;
        }

        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                result.add(clazz);
            }
        }

        return result;
    }
}
