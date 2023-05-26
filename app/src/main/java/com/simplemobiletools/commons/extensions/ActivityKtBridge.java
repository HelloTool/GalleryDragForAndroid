package com.simplemobiletools.commons.extensions;

import android.app.Activity;
import android.net.Uri;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class ActivityKtBridge extends ObjectBridge {

    private final Class<?> clazz;
    private Method getFinalUriFromPath;

    public ActivityKtBridge(Object activityKt, ClassManager classManager) throws Throwable {
        super(activityKt, classManager);
        clazz = classManager.getClass("com.simplemobiletools.commons.extensions.ActivityKt");
    }

    public Uri getFinalUriFromPath(Activity activity, String path, String applicationId) throws Throwable {
        if (getFinalUriFromPath == null) {
            getFinalUriFromPath = clazz.getMethod("getFinalUriFromPath", Activity.class, String.class, String.class);
        }
        return (Uri) getFinalUriFromPath.invoke(thisObj, activity, path, applicationId);
    }
}
