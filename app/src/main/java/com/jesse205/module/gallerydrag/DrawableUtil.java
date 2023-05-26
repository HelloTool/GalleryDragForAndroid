package com.jesse205.module.gallerydrag;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import java.io.IOException;

public class DrawableUtil {

    /**
     * 获取视频封面
     *
     * @param context 上下文
     * @param uri     文件 Uri
     * @return 封面 Bitmap
     * @throws IOException              When an IOException is thrown while closing a MediaDataSource passed to setDataSource(MediaDataSource). This throws clause exists since API Build.VERSION_CODES.TIRAMISU, but this method can throw in earlier API versions where the exception is not declared.
     * @throws IllegalArgumentException if the Uri is invalid
     * @throws SecurityException        if the Uri cannot be used due to lack of permission.
     */
    public static Bitmap getVideoThumb(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException {

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
