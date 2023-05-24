package com.huawei.gallery.app;

import com.android.gallery3d.ui.SelectionManagerBridge;

import java.lang.reflect.Field;

public class AbsAlbumPageBridge extends ActivityStateBridge {
    private final Class<?> clazz;
    private final ClassLoader classLoader;
    private final Field mSelectionManagerField;
    private final Object thisObj;
    private SelectionManagerBridge mSelectionManagerBridge;

    public AbsAlbumPageBridge(Object absAlbumPage, ClassLoader classLoader) throws Throwable {
        super(absAlbumPage,classLoader);
        this.classLoader = classLoader;
        this.thisObj = absAlbumPage;
        clazz = classLoader.loadClass("com.huawei.gallery.app.AbsAlbumPage");
        mSelectionManagerField = clazz.getDeclaredField("mSelectionManager");
        mSelectionManagerField.setAccessible(true);
    }


    public SelectionManagerBridge getSelectionManagerBridge() throws Throwable {
        if (mSelectionManagerBridge == null)
            mSelectionManagerBridge = new SelectionManagerBridge(getSelectionManagerField(), classLoader);
        return mSelectionManagerBridge;
    }


    public Object getSelectionManagerField() throws IllegalAccessException {
        mSelectionManagerField.setAccessible(true);
        return mSelectionManagerField.get(thisObj);
    }
}
