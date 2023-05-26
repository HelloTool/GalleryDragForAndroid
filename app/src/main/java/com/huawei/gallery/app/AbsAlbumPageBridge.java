package com.huawei.gallery.app;

import com.android.gallery3d.ui.SelectionManagerBridge;
import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Field;

public class AbsAlbumPageBridge extends ActivityStateBridge {
    private final Field mSelectionManagerField;
    protected final Object thisObj;
    private SelectionManagerBridge mSelectionManagerBridge;

    public AbsAlbumPageBridge(Object absAlbumPage, ClassManager classManager) throws Throwable {
        super(absAlbumPage, classManager);
        this.thisObj = absAlbumPage;
        Class<?> clazz = classManager.getClass("com.huawei.gallery.app.AbsAlbumPage");
        mSelectionManagerField = clazz.getDeclaredField("mSelectionManager");
        mSelectionManagerField.setAccessible(true);
    }


    public SelectionManagerBridge getSelectionManagerBridge() throws Throwable {
        if (mSelectionManagerBridge == null)
            mSelectionManagerBridge = new SelectionManagerBridge(getSelectionManagerField(), classManager);
        return mSelectionManagerBridge;
    }


    public Object getSelectionManagerField() throws IllegalAccessException {
        mSelectionManagerField.setAccessible(true);
        return mSelectionManagerField.get(thisObj);
    }
}
