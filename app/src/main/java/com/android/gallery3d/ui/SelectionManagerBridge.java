package com.android.gallery3d.ui;

import android.annotation.SuppressLint;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javabridge.lang.ObjectBridge;

public class SelectionManagerBridge extends ObjectBridge {
    private final Class<?> clazz;
    private final Method getSelectedCount;
    private final Method getSelected;


    @SuppressLint("PrivateApi")
    public SelectionManagerBridge(Object selectionManager, ClassManager classManager) throws Throwable {
        super(selectionManager, classManager);
        clazz = classManager.getClass("com.android.gallery3d.ui.SelectionManager");
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

    @SuppressWarnings("unchecked")
    public ArrayList<Object> getSelected(boolean expandSet) throws Throwable {
        return (ArrayList<Object>) getSelected.invoke(thisObj, true);
    }
}
