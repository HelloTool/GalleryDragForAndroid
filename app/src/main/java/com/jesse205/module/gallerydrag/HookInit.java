package com.jesse205.module.gallerydrag;

import android.os.Build;
import android.view.View;

import com.jesse205.module.gallerydrag.hooks.EMUI8Gallery;
import com.jesse205.module.gallerydrag.hooks.SimpleGallery;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookInit implements IXposedHookLoadPackage {
    public static final String TAG = "IXposedHookLoadPackage";
    public static final String LABEL_GALLERY = "gallery";
    public static final int FLAG_DRAG = View.DRAG_FLAG_OPAQUE | View.DRAG_FLAG_GLOBAL_URI_WRITE |
            View.DRAG_FLAG_GLOBAL_URI_READ | View.DRAG_FLAG_GLOBAL;
    public static final String PACKAGE_GALLERY3D = "com.android.gallery3d";
    public static final String PACKAGE_SIMPLE_GALLERY = "com.simplemobiletools.gallery.pro";


    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("");
        }
        if (BuildConfig.APPLICATION_ID.equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(
                    MainActivity.class.getName(),
                    lpparam.classLoader,
                    "isModuleActivated",
                    XC_MethodReplacement.returnConstant(true));
        } else if (PACKAGE_GALLERY3D.equals(lpparam.packageName)) {
            EMUI8Gallery.handleLoadPackage(lpparam);
        } else if (PACKAGE_SIMPLE_GALLERY.equals(lpparam.packageName)) {
            SimpleGallery.handleLoadPackage(lpparam);
        }

    }

}
