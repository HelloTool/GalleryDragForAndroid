package com.android.gallery3d.ui;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SelectionManagerBridge {
    private final Class<?> clazz;
    private final ClassLoader classLoader;
    private final Method getSelectedCount;
    private final Method getSelected;
    private final Object thisObj;


    @SuppressLint("PrivateApi")
    public SelectionManagerBridge(Object selectionManager, ClassLoader classLoader) throws Throwable {
        this.thisObj = selectionManager;
        this.classLoader = classLoader;
        clazz = classLoader.loadClass("com.android.gallery3d.ui.SelectionManager");
        getSelectedCount = clazz.getMethod("getSelectedCount");
        getSelected = clazz.getMethod("getSelected", boolean.class);
    }

    public Object getThisObj() {
        return thisObj;
    }

    public int getSelectedCount() throws Throwable {
        Integer selectedCount = (Integer) getSelectedCount.invoke(thisObj);
        if (selectedCount != null)
            return selectedCount;
        else
            return 0;
    }

    public ArrayList<Object> getSelected(boolean expandSet) throws Throwable {
        return (ArrayList<Object>) getSelected.invoke(thisObj, true);
    }
}
