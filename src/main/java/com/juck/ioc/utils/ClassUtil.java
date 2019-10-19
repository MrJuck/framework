package com.juck.ioc.utils;

import com.juck.ioc.annotation.Controller;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final Set<Class<?>> CLASS_SET = new HashSet<>();

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static Class<?> loadClass(String qualifyPath) throws ClassNotFoundException {
        return Class.forName(qualifyPath, false, getClassLoader());
    }

    public static Set<Class<?>> getClassUnderPackage(String basePackage) {
        if (StringUtils.isBlank(basePackage)) {
            return CLASS_SET;
        }

        URL resource = Thread.currentThread().getContextClassLoader().getResource(package2Filepath(basePackage));
        File parentFolder = new File(resource.getPath());

        if (!parentFolder.exists()) {
            return CLASS_SET;
        }

        File[] files = parentFolder.listFiles((file) -> isClassFile(file) || file.isDirectory());
        if (files == null || files.length < 1) {
            return CLASS_SET;
        }

        String qualifyPath = "";
        try {
            for(File file : files) {
                if (isClassFile(file)) {
                    qualifyPath = getQualifyClassPath(basePackage, file.getName());
                    CLASS_SET.add(loadClass(qualifyPath));
                } else {
                    getClassUnderPackage(basePackage + "." + file.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class: {} failed due to: {}", qualifyPath, e.getMessage());
            throw new RuntimeException(e);
        }

        return CLASS_SET;
    }

    private static String package2Filepath(String basePackage) {
        return StringUtils.isBlank(basePackage) ? "" : basePackage.replaceAll("\\.", "/");
    }

    private static boolean isClassFile(File file) {
        return file.exists() && file.getName().endsWith(CLASS_FILE_SUFFIX);
    }

    private static String getQualifyClassPath(String basePackage, String filename) {
        return basePackage + "." + filename.substring(0, filename.lastIndexOf("."));
    }
}
