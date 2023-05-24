package com.huawei.gallery.app;

import android.app.Activity;

import java.lang.reflect.Method;

public class GLHostBridge {
    private final Class<?> clazz;
    private final Object thisObj;
    private final Method getGalleryContext;
    private final Method getActivity;
    private final ClassLoader classLoader;

    public GLHostBridge(Object slotAlbumPage, ClassLoader classLoader) throws Throwable {
        this.thisObj = slotAlbumPage;
        this.classLoader = classLoader;
        clazz = classLoader.loadClass("com.huawei.gallery.app.GLHost");
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
