package com.jesse205.module.gallerydrag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.view.View;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class HookInit implements IXposedHookLoadPackage {
    public static final String TAG = "IXposedHookLoadPackage";
    public static final String LABEL_GALLERY = "gallery";
    public static final int FLAG_DRAG = View.DRAG_FLAG_OPAQUE | View.DRAG_FLAG_GLOBAL_URI_WRITE |
            View.DRAG_FLAG_GLOBAL_URI_READ | View.DRAG_FLAG_GLOBAL;
    public static final String PACKGE_GALLERY3D = "com.android.gallery3d";


    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (BuildConfig.APPLICATION_ID.equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(
                    MainActivity.class.getName(),
                    lpparam.classLoader,
                    "isModuleActivated",
                    XC_MethodReplacement.returnConstant(true));
        } else if (PACKGE_GALLERY3D.equals(lpparam.packageName)) {
            EMUI8Gallery.handleLoadPackage(lpparam);
        }

    }

}
