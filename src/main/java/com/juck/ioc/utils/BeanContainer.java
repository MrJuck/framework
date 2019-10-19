package com.juck.ioc.utils;

import com.juck.ioc.Constants;
import com.juck.ioc.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BeanContainer {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    public BeanContainer(String basePackage) {
        System.setProperty(Constants.SYS_PROP_KEY, basePackage);

        // 1. 获取类
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();

        // 2.实例化类
        initializeBean(beanClassSet);

        // 3. 依赖注入
        dependencyInjection();
    }

    private void dependencyInjection() {
        if (BEAN_MAP.isEmpty()) {
            return ;
        }

        for (Map.Entry<Class<?>, Object> entry : BEAN_MAP.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();

            doInjection(value);
        }
    }

    private void doInjection(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length < 1) {
            return ;
        }

        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                ReflectionUtil.setField(obj, field, BEAN_MAP.get(field.getType()));
            }
        }
    }

    private void initializeBean(Set<Class<?>> beanClassSet) {
        if (beanClassSet == null || beanClassSet.isEmpty()) {
            return ;
        }

        for (Class<?> clazz : beanClassSet) {
            BEAN_MAP.put(clazz, ReflectionUtil.newInstance(clazz));
        }
    }

    public <T> T getBean(Class<T> clazz) throws RuntimeException {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("no bean found for class: " + clazz.getName());
        }
        return (T) BEAN_MAP.get(clazz);
    }
}
