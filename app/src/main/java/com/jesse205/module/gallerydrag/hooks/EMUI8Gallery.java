package com.jesse205.module.gallerydrag.hooks;

import static com.jesse205.module.gallerydrag.HookInit.FLAG_DRAG;
import static com.jesse205.module.gallerydrag.HookInit.LABEL_GALLERY;
import static com.jesse205.module.gallerydrag.HookInit.PACKAGE_GALLERY3D;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.gallery3d.data.MediaItemBridge;
import com.android.gallery3d.ui.SelectionManagerBridge;
import com.android.gallery3d.util.GalleryUtilsBridge;
import com.huawei.gallery.app.AbsAlbumPageBridge;
import com.huawei.gallery.app.GLHostBridge;
import com.huawei.gallery.app.SlotAlbumPageBridge;
import com.huawei.gallery.app.TimeBucketPageBridge;
import com.huawei.gallery.photoshare.utils.PhotoShareUtilsBridge;
import com.jesse205.module.gallerydrag.ClassManager;
import com.jesse205.module.gallerydrag.DrawableUtil;
import com.jesse205.module.gallerydrag.ImageDragShadowBuilder;

import java.io.File;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class EMUI8Gallery {
    private static final String TAG = "EMUI8Gallery";
    private final PhotoShareUtilsBridge photoShareUtilsBridge;
    private final GalleryUtilsBridge galleryUtilsBridge;
    private final ClassManager classManager;

    private EMUI8Gallery(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        ClassLoader classLoader = lpparam.classLoader;
        Class<?> SlotAlbumPageClass = classLoader.loadClass("com.huawei.gallery.app.SlotAlbumPage");
        Class<?> TimeBucketPageClass = classLoader.loadClass("com.huawei.gallery.app.TimeBucketPage");
        classManager = new ClassManager(classLoader);
        photoShareUtilsBridge = new PhotoShareUtilsBridge(null, classManager);
        galleryUtilsBridge = new GalleryUtilsBridge(null, classManager);

        XposedHelpers.findAndHookMethod(SlotAlbumPageClass, "onLongTap", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(TAG, "SlotAlbumPage beforeHookedMethod: ");
                SlotAlbumPageBridge thisBridge = new SlotAlbumPageBridge(param.thisObject, classManager);
                MediaItemBridge itemBridge = thisBridge.getAlbumDataAdapterBridge().getBridge((Integer) param.args[0]);
                onGalleryLongClick(param, thisBridge, itemBridge);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "SlotAlbumPage afterHookedMethod: ");
            }
        });


        XposedHelpers.findAndHookMethod(TimeBucketPageClass, "onLongTap", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(TAG, "TimeBucketPage beforeHookedMethod: ");
                TimeBucketPageBridge thisBridge = new TimeBucketPageBridge(param.thisObject, classManager);
                onGalleryLongClick(param, thisBridge, null);//不知道怎么获取实际长按的项目
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "TimeBucketPage afterHookedMethod: ");
            }
        });
    }

    @SuppressLint("PrivateApi")
    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!PACKAGE_GALLERY3D.equals(lpparam.packageName))
            return;
        new EMUI8Gallery(lpparam);
    }

    public void onGalleryLongClick(XC_MethodHook.MethodHookParam param, AbsAlbumPageBridge thisBridge, MediaItemBridge itemBridge) throws Throwable {
        SelectionManagerBridge selectionManagerBridge = thisBridge.getSelectionManagerBridge();
        // 获得总数
        int selectedCount = selectionManagerBridge.getSelectedCount();

        if (selectedCount > 0) {
            GLHostBridge mHostBridge = thisBridge.getHostBridge();
            Object galleryContext = mHostBridge.getGalleryContext();
            Activity activity = mHostBridge.getActivity();
            assert activity != null;

            ArrayList<Object> selectedItems = selectionManagerBridge.getSelected(true);
            ArrayList<String> fileList = photoShareUtilsBridge.getFilePathsFromPath(galleryContext, selectedItems);
            assert fileList != null;
            String coverPath = fileList.get(0);
            if (itemBridge != null) {
                ArrayList<Object> coverItemList = new ArrayList<>();
                coverItemList.add(itemBridge.getPath());
                coverPath = photoShareUtilsBridge.getFilePathsFromPath(galleryContext, coverItemList).get(0);
            }
            if (!fileList.contains(coverPath))
                return;//当前项目不包含在列表内，说明当前项目没有被选中，禁止启动拖放
            // 很难拿到当前视图，所以就拿根视图代替
            View view = activity.getWindow().getDecorView();
            startDragAndDrop(activity, view, fileList, coverPath);
            param.setResult(null);
        }
        Log.d(TAG, "onGalleryLongClick: selectedCount=" + selectedCount);
    }

    /**
     * 开始拖放
     *
     * @param activity  当前活动
     * @param view      视图
     * @param fileList  文件列表
     * @param coverPath 当前项目
     * @throws Throwable 错误
     */
    public void startDragAndDrop(Activity activity, View view, ArrayList<String> fileList, String coverPath) throws Throwable {
        Log.d(TAG, "startDragAndDrop: fileList=" + fileList);
        Uri uri = Uri.fromFile(new File(fileList.get(0)));
        Uri contentUri = galleryUtilsBridge.convertFileUriToContentUri(activity, uri);
        ClipData data = ClipData.newUri(activity.getContentResolver(), LABEL_GALLERY, contentUri);
        for (int i = 1; i < fileList.size(); i++) {
            uri = Uri.fromFile(new File(fileList.get(i)));
            contentUri = galleryUtilsBridge.convertFileUriToContentUri(activity, uri);
            data.addItem(new ClipData.Item(contentUri));
        }

        // 设置拖放阴影
        ImageDragShadowBuilder dragShadowBuilder = new ImageDragShadowBuilder(activity);
        dragShadowBuilder.updateBadge(fileList.size());

        uri = Uri.fromFile(new File(coverPath != null ? coverPath : fileList.get(0)));
        try {
            Bitmap bitmap = DrawableUtil.getVideoThumb(activity, uri);
            dragShadowBuilder.updateImage(bitmap);
        } catch (Throwable ignored) {
            dragShadowBuilder.updateImage(uri);
        }
        view.startDragAndDrop(data, dragShadowBuilder, null, FLAG_DRAG);
    }

    public ClassManager getClassManager() {
        return classManager;
    }
}
