package com.huawei.gallery.app;

import java.lang.reflect.Field;

public class ActivityStateBridge {
    private Object thisObj;
    private final Class clazz;
    private ClassLoader classLoader;
    private final Field mHostField;
    private GLHostBridge mHostBridge;

    public ActivityStateBridge(Object activityState, ClassLoader classLoader) throws Throwable {
        thisObj = activityState;

        clazz = classLoader.loadClass("com.huawei.gallery.app.ActivityState");
        this.classLoader = classLoader;
        mHostField = clazz.getDeclaredField("mHost");
        mHostField.setAccessible(true);
    }

    public GLHostBridge getHostBridge() throws Throwable {
        if (mHostBridge == null)
            mHostBridge = new GLHostBridge(getHostField(), classLoader);
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
