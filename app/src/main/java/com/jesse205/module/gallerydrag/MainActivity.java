package com.jesse205.module.gallerydrag;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(isModuleActivated() ? R.string.xposed_activated : R.string.xposed_unactivated);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitee.com/Jesse205/GalleryDrag"));
        startActivity(intent);
        finish();
    }

    public static boolean isModuleActivated() {
        return false;
    }
}
