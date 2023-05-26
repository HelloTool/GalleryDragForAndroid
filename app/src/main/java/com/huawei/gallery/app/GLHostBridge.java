package com.huawei.gallery.app;

import android.app.Activity;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class GLHostBridge extends ObjectBridge {
    private final Method getGalleryContext;
    private final Method getActivity;

    public GLHostBridge(Object slotAlbumPage, ClassManager classManager) throws Throwable {
        super(slotAlbumPage, classManager);
        Class<?> clazz = classManager.getClass("com.huawei.gallery.app.GLHost");
        getGalleryContext = clazz.getMethod("getGalleryContext");
        getActivity = clazz.getMethod("getActivity");
    }

    public Object getGalleryContext() throws Throwable {
        return getGalleryContext.invoke(thisObj);
    }

    public Activity getActivity() throws Throwable {
        return (Activity) getActivity.invoke(thisObj);
    }
}
