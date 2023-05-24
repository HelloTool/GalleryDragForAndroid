package com.jesse205.module.gallerydrag;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import java.io.IOException;

public class DrawableUtil {
    public static Bitmap getVideoThumb(String path) throws IOException, RuntimeException {

        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        Bitmap bitmap = media.getFrameAtTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            media.close();
        else
            media.release();
        return bitmap;
    }

    public static Bitmap getVideoThumb(Context context, Uri uri) throws IOException, RuntimeException {

        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(context, uri);
        Bitmap bitmap = media.getFrameAtTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            media.close();
        else
            media.release();
        return bitmap;
    }

}
