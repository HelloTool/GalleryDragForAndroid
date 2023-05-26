package com.huawei.gallery.app;

import com.jesse205.module.gallerydrag.ClassManager;

import java.lang.reflect.Field;

public class SlotAlbumPageBridge extends AbsAlbumPageBridge {

    private final Class<?> clazz;
    private Field mAlbumDataAdapterField;
    private CommonAlbumDataLoaderBridge mAlbumDataAdapterBridge;

    public SlotAlbumPageBridge(Object slotAlbumPage, ClassManager classManager) throws Throwable {
        super(slotAlbumPage, classManager);
        clazz = classManager.getClass("com.huawei.gallery.app.SlotAlbumPage");
    }

    public Object getAlbumDataAdapter() throws NoSuchFieldException, IllegalAccessException {
        if (mAlbumDataAdapterField == null) {
            mAlbumDataAdapterField = clazz.getDeclaredField("mAlbumDataAdapter");
            mAlbumDataAdapterField.setAccessible(true);
        }
        return mAlbumDataAdapterField.get(thisObj);
    }

    public CommonAlbumDataLoaderBridge getAlbumDataAdapterBridge() throws Throwable {
        if (mAlbumDataAdapterBridge == null) {
            mAlbumDataAdapterBridge = new CommonAlbumDataLoaderBridge(getAlbumDataAdapter(), classManager);
        }
        return mAlbumDataAdapterBridge;
    }
}

