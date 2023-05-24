package com.android.gallery3d.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GalleryUtilsBridge {
    private final Object thisObj;
    private final Class<?> clazz;
    private final Method convertFileUriToContentUri;

    @SuppressLint("PrivateApi")
    public GalleryUtilsBridge(Object galleryUtils, ClassLoader classLoader) throws Throwable {
        this.thisObj = galleryUtils;
        this.clazz = classLoader.loadClass("com.android.gallery3d.util.GalleryUtils");
        convertFileUriToContentUri = clazz.getMethod("convertFileUriToContentUri", Context.class, Uri.class);
    }

    public Uri convertFileUriToContentUri(Context context, Uri fileUri) throws Throwable {
        return (Uri) convertFileUriToContentUri.invoke(thisObj, context, fileUri);
    }
}
