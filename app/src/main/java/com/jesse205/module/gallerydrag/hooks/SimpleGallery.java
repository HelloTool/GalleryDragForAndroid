package com.jesse205.module.gallerydrag.hooks;

import static com.jesse205.module.gallerydrag.HookInit.FLAG_DRAG;
import static com.jesse205.module.gallerydrag.HookInit.LABEL_GALLERY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewBridge;

import androidx.recyclerview.widget.RecyclerViewBridge;

import com.jesse205.module.gallerydrag.ClassManager;
import com.jesse205.module.gallerydrag.DrawableUtil;
import com.jesse205.module.gallerydrag.ImageDragShadowBuilder;
import com.simplemobiletools.commons.extensions.ActivityKtBridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SimpleGallery {
    private static final String TAG = "SimpleGallery";
    public static final String PACKAGE_SIMPLE_GALLERY = "com.simplemobiletools.gallery.pro";
    private final ClassManager classManager;
    private final ActivityKtBridge activityKtBridge;

    private SimpleGallery(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        classManager = new ClassManager(lpparam.classLoader);
        ClassLoader classLoader = lpparam.classLoader;
        Class<?> MediaAdapterClass = classLoader.loadClass("com.simplemobiletools.gallery.pro.adapters.MediaAdapter");
        Class<?> ViewHolderClass = classLoader.loadClass("com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter$ViewHolder");
        activityKtBridge = new ActivityKtBridge(null, classManager);

        Class MyRecyclerViewAdapterClass = classLoader.loadClass("com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter");
        XposedHelpers.findAndHookMethod(MediaAdapterClass, "onBindViewHolder", ViewHolderClass, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                classManager.setClass("androidx.recyclerview.widget.RecyclerView$ViewHolder", ViewHolderClass.getSuperclass());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                RecyclerViewBridge.ViewHolderBridge viewHolderBridge = new RecyclerViewBridge.ViewHolderBridge(param.args[0], classManager);

                View itemView = viewHolderBridge.getItemView();
                ViewBridge viewBridge = new ViewBridge(itemView, classManager);
                assert itemView != null;

                View.OnLongClickListener onLongClickListener = viewBridge.getListenerInfoBridge().getOnLongClickListener();

                itemView.setOnLongClickListener(v -> {

                    try {
                        Method getSelectedPaths = MediaAdapterClass.getDeclaredMethod("getSelectedPaths");
                        Field lastLongPressedItemField = MyRecyclerViewAdapterClass.getDeclaredField("lastLongPressedItem");
                        getSelectedPaths.setAccessible(true);
                        lastLongPressedItemField.setAccessible(true);
                        ArrayList<String> selectedPaths = (ArrayList<String>) getSelectedPaths.invoke(param.thisObject);
                        assert selectedPaths != null;
                        int oldSelectedCount = selectedPaths.size();
                        onLongClickListener.onLongClick(v);
                        Activity activity = (Activity) v.getContext();

                        selectedPaths = (ArrayList<String>) getSelectedPaths.invoke(param.thisObject);
                        assert selectedPaths != null;

                        if (oldSelectedCount > 0 && selectedPaths.size() > 0) {
                            startDragAndDrop(activity, itemView, selectedPaths);
                            return true;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    return true;
                });
            }
        });
    }

    @SuppressLint("PrivateApi")
    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!PACKAGE_SIMPLE_GALLERY.equals(lpparam.packageName))
            return;
        new SimpleGallery(lpparam);
    }

    /**
     * 开始拖放
     *
     * @param activity      当前活动
     * @param view          视图
     * @param selectedPaths 文件列表
     * @throws Throwable 错误
     */
    public void startDragAndDrop(Activity activity, View view, ArrayList<String> selectedPaths) throws Throwable {
        Uri uri = activityKtBridge.getFinalUriFromPath(activity, selectedPaths.get(0), activity.getPackageName());
        Uri coverUri = uri;
        ClipData data = ClipData.newUri(activity.getContentResolver(), LABEL_GALLERY, uri);
        for (int i = 1; i < selectedPaths.size(); i++) {
            uri = activityKtBridge.getFinalUriFromPath(activity, selectedPaths.get(i), activity.getPackageName());
            data.addItem(new ClipData.Item(uri));
        }

        // 设置拖放阴影
        ImageDragShadowBuilder dragShadowBuilder = new ImageDragShadowBuilder(activity);
        dragShadowBuilder.updateBadge(selectedPaths.size());


        try {
            Bitmap bitmap = DrawableUtil.getVideoThumb(activity, coverUri);
            dragShadowBuilder.updateImage(bitmap);
        } catch (Throwable ignored) {
            dragShadowBuilder.updateImage(coverUri);
        }
        view.startDragAndDrop(data, dragShadowBuilder, null, FLAG_DRAG);
    }


}
