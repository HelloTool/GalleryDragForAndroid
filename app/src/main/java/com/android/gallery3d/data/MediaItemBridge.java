package com.android.gallery3d.data;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class MediaItemBridge extends ObjectBridge {

    private final Class<?> clazz;
    private Method getPath;

    public MediaItemBridge(Object mediaItem, ClassManager classManager) throws ClassNotFoundException {
        super(mediaItem, classManager);
        clazz = classManager.getClass("com.android.gallery3d.data.MediaItem");
    }

    public Object getPath() throws Throwable {
        if (getPath == null) {
            getPath = clazz.getMethod("getPath");
        }
        return getPath.invoke(thisObj);
    }
}
