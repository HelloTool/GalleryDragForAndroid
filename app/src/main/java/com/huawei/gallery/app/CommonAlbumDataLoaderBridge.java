package com.huawei.gallery.app;

import com.android.gallery3d.data.MediaItemBridge;
import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class CommonAlbumDataLoaderBridge extends ObjectBridge {
    private final Class<?> clazz;
    private Method get;

    public CommonAlbumDataLoaderBridge(Object commonAlbumDataLoader, ClassManager classManager) throws ClassNotFoundException {
        super(commonAlbumDataLoader, classManager);
        clazz = classManager.getClass("com.huawei.gallery.app.CommonAlbumDataLoader");
    }

    public Object get(int index) throws Throwable {
        if (get == null) {
            get = clazz.getMethod("get", int.class);
        }
        return get.invoke(thisObj, index);
    }

    public MediaItemBridge getBridge(int index) throws Throwable {
        return new MediaItemBridge(get(index), classManager);
    }
}
