package com.huawei.gallery.app;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Field;

import javabridge.lang.ObjectBridge;

public class ActivityStateBridge extends ObjectBridge {
    private final Field mHostField;
    private GLHostBridge mHostBridge;

    public ActivityStateBridge(Object activityState, ClassManager classManager) throws Throwable {
        super(activityState,classManager);

        Class<?> clazz = classManager.getClass("com.huawei.gallery.app.ActivityState");
        mHostField = clazz.getDeclaredField("mHost");
        mHostField.setAccessible(true);
    }

    public GLHostBridge getHostBridge() throws Throwable {
        if (mHostBridge == null)
            mHostBridge = new GLHostBridge(getHostField(), classManager);
        return mHostBridge;
    }

    public Object getHostField() throws IllegalAccessException {
        mHostField.setAccessible(true);
        return mHostField.get(thisObj);
    }

    public Object getThisObj() {
        return thisObj;
    }
}
