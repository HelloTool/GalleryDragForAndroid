package com.jesse205.module.gallerydrag;

import static com.jesse205.module.gallerydrag.HookInit.FLAG_DRAG;
import static com.jesse205.module.gallerydrag.HookInit.LABEL_GALLERY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.android.gallery3d.ui.SelectionManagerBridge;
import com.android.gallery3d.util.GalleryUtilsBridge;
import com.huawei.gallery.app.AbsAlbumPageBridge;
import com.huawei.gallery.app.GLHostBridge;
import com.huawei.gallery.app.SlotAlbumPageBridge;
import com.huawei.gallery.app.TimeBucketPageBridge;
import com.huawei.gallery.photoshare.utils.PhotoShareUtilsBridge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class EMUI8Gallery {
    private static final String TAG = "EMUI8Gallery";

    @SuppressLint("PrivateApi")
    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        ClassLoader classLoader = lpparam.classLoader;
        Class<?> SlotAlbumPageClass = classLoader.loadClass("com.huawei.gallery.app.SlotAlbumPage");
        Class<?> TimeBucketPageClass = classLoader.loadClass("com.huawei.gallery.app.TimeBucketPage");

        PhotoShareUtilsBridge photoShareUtilsBridge = new PhotoShareUtilsBridge(null, classLoader);
        GalleryUtilsBridge galleryUtilsClass = new GalleryUtilsBridge(null, classLoader);

        XposedHelpers.findAndHookMethod(SlotAlbumPageClass, "onLongTap", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(TAG, "SlotAlbumPage beforeHookedMethod: ");
                // 当前实例桥
                SlotAlbumPageBridge thisBridge = new SlotAlbumPageBridge(param.thisObject, classLoader);
                onGalleryLongClick(param, thisBridge, photoShareUtilsBridge, galleryUtilsClass);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "SlotAlbumPage afterHookedMethod: ");
            }
        });


        XposedHelpers.findAndHookMethod(TimeBucketPageClass, "onLongTap", android.view.MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(TAG, "TimeBucketPage beforeHookedMethod: ");
                TimeBucketPageBridge thisBridge = new TimeBucketPageBridge(param.thisObject, classLoader);
                onGalleryLongClick(param, thisBridge, photoShareUtilsBridge, galleryUtilsClass);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "TimeBucketPage afterHookedMethod: ");
            }
        });
    }

    public static void onGalleryLongClick(XC_MethodHook.MethodHookParam param, AbsAlbumPageBridge thisBridge,
                                          PhotoShareUtilsBridge photoShareUtilsBridge, GalleryUtilsBridge galleryUtilsClass) throws Throwable {
        SelectionManagerBridge selectionManagerBridge = thisBridge.getSelectionManagerBridge();
        // 获得总数
        int selectedCount = selectionManagerBridge.getSelectedCount();

        if (selectedCount > 0) {
            GLHostBridge mHostBridge = thisBridge.getHostBridge();
            Object galleryContext = mHostBridge.getGalleryContext();
            Activity activity = mHostBridge.getActivity();

            ArrayList<Object> selectedItems = selectionManagerBridge.getSelected(true);
            ArrayList<String> fileList = photoShareUtilsBridge.getFilePathsFromPath(galleryContext, selectedItems);

            assert fileList != null;
            assert activity != null;
            Log.d(TAG, "onGalleryLongClick: fileList=" + fileList);
            Uri uri = Uri.fromFile(new File(fileList.get(0)));
            Uri contentUri = galleryUtilsClass.convertFileUriToContentUri(activity, uri);
            ImageDragShadowBuilder dragShadowBuilder = new ImageDragShadowBuilder(activity);
            dragShadowBuilder.updateBagde(fileList.size());
            try {
                Bitmap bitmap = DrawableUtil.getVideoThumb(activity,uri);
                BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                dragShadowBuilder.updateImage(drawable);
            } catch (IOException | RuntimeException ignored) {
                dragShadowBuilder.updateImage(uri);
            }

            ClipData data = ClipData.newUri(activity.getContentResolver(), LABEL_GALLERY, contentUri);
            for (int i = 1; i < selectedCount; i++) {
                uri = Uri.fromFile(new File(fileList.get(i)));
                contentUri = galleryUtilsClass.convertFileUriToContentUri(activity, uri);
                data.addItem(new ClipData.Item(contentUri));
            }
            View view = activity.getWindow().getDecorView();
            view.startDragAndDrop(data, dragShadowBuilder, null, FLAG_DRAG);
            param.setResult(null);
        }
        Log.d(TAG, "onGalleryLongClick: selectedCount=" + selectedCount);
    }
}
