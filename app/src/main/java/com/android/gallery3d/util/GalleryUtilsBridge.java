package com.android.gallery3d.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class GalleryUtilsBridge extends ObjectBridge {
    private final Class<?> clazz;
    private final Method convertFileUriToContentUri;

    @SuppressLint("PrivateApi")
    public GalleryUtilsBridge(Object galleryUtils, ClassManager classManager) throws Throwable {
        super(galleryUtils, classManager);
        this.clazz = classManager.getClass("com.android.gallery3d.util.GalleryUtils");
        convertFileUriToContentUri = clazz.getMethod("convertFileUriToContentUri", Context.class, Uri.class);
    }

    public Uri convertFileUriToContentUri(Context context, Uri fileUri) throws Throwable {
        return (Uri) convertFileUriToContentUri.invoke(thisObj, context, fileUri);
    }
}
