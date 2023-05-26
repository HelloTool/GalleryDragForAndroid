package com.jesse205.module.gallerydrag;

import java.util.HashMap;

public class ClassManager {
    private final HashMap<String, Class<?>> classHashMap = new HashMap<>();
    private final ClassLoader classLoader;


    public ClassManager(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClass(String className, Class<?> clazz) {
        classHashMap.put(className, clazz);
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
        Class<?> clazz = classHashMap.get(className);
        if (clazz == null) {
            clazz = classLoader.loadClass(className);
            setClass(className, clazz);
        }
        return clazz;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
