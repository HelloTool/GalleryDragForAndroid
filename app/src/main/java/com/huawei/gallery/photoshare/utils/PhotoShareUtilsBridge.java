package com.huawei.gallery.photoshare.utils;

import android.annotation.SuppressLint;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PhotoShareUtilsBridge {
    private final Object thisObj;
    private final Class<?> clazz;
    private final Class<?> GalleryContextClass;
    private final Method getFilePathsFromPath;

    @SuppressLint("PrivateApi")
    public PhotoShareUtilsBridge(Object photoShareUtils, ClassLoader classLoader) throws Throwable {
        this.thisObj = photoShareUtils;
        clazz = classLoader.loadClass("com.huawei.gallery.photoshare.utils.PhotoShareUtils");
        GalleryContextClass = classLoader.loadClass("com.android.gallery3d.app.GalleryContext");
        getFilePathsFromPath = clazz.getMethod("getFilePathsFromPath", GalleryContextClass, ArrayList.class);
    }

    public ArrayList<String> getFilePathsFromPath(Object galleryContext, ArrayList<Object> selectedItems) throws Throwable {
        return (ArrayList<String>) getFilePathsFromPath.invoke(thisObj, galleryContext, selectedItems);
    }
}
