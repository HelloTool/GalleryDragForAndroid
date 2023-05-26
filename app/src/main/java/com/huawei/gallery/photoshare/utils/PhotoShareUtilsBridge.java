package com.huawei.gallery.photoshare.utils;

import android.annotation.SuppressLint;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javabridge.lang.ObjectBridge;

public class PhotoShareUtilsBridge extends ObjectBridge {
    private final Method getFilePathsFromPath;

    @SuppressLint("PrivateApi")
    public PhotoShareUtilsBridge(Object photoShareUtils, ClassManager classManager) throws Throwable {
        super(photoShareUtils, classManager);
        Class<?> clazz = classManager.getClass("com.huawei.gallery.photoshare.utils.PhotoShareUtils");
        Class<?> galleryContextClass = classManager.getClass("com.android.gallery3d.app.GalleryContext");
        getFilePathsFromPath = clazz.getMethod("getFilePathsFromPath", galleryContextClass, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getFilePathsFromPath(Object galleryContext, ArrayList<Object> selectedItems) throws Throwable {
        return (ArrayList<String>) getFilePathsFromPath.invoke(thisObj, galleryContext, selectedItems);
    }
}
