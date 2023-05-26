/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jesse205.module.gallerydrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;

public class ImageDragShadowBuilder extends View.DragShadowBuilder {
    private static final String TAG = "DragShadowBuilder";
    private final RelativeLayout mShadowView;
    private final int mWidth;
    private final Context context;
    private final int mHeight;
    private final ImageView mImage;
    // private final TextView mBadge;

    @SuppressLint("InflateParams")
    public ImageDragShadowBuilder(Context context) {
        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 104, context.getResources().getDisplayMetrics());
        this.context = context;
        mHeight = mWidth;
        Log.d(TAG, "DragShadowBuilder: mWidth=" + mWidth);
        // 无法成功获取到资源
        // context.getResources().addLoaders();
        // mShadowView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.drag_shadow_layout, null);
        // mImage = mShadowView.findViewById(android.R.id.icon);
        // mBagde = mShadowView.findViewById(R.id.badge);
        mShadowView = new RelativeLayout(context);
        mImage = new ImageView(context);
        // mBadge = new TextView(context);


        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mShadowView.setLayoutParams(layoutParams);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImage.setLayoutParams(layoutParams);

        mShadowView.addView(mImage);
        // mShadowView.addView(mBadge);

        mImage.setBackgroundColor(Color.WHITE);
        mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onProvideShadowMetrics(
            Point shadowSize, Point shadowTouchPoint) {
        shadowSize.set(mWidth, mHeight);
        shadowTouchPoint.set(mWidth / 2, mHeight / 2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        Rect r = canvas.getClipBounds();
        // Calling measure is necessary in order for all child views to get correctly laid out.
        mShadowView.measure(
                View.MeasureSpec.makeMeasureSpec(r.right - r.left, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(r.bottom - r.top, View.MeasureSpec.EXACTLY));
        mShadowView.layout(r.left, r.top, r.right, r.bottom);
        mShadowView.draw(canvas);
    }

    public void updateImage(Drawable icon) {
        mImage.setImageDrawable(icon);
    }

    public void updateImage(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            updateImage(context.getContentResolver().loadThumbnail(uri, new Size(mWidth, mHeight),
                    null));

        } else {
            mImage.setImageURI(uri);
        }
    }

    public void updateImage(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    public void updateBadge(int num) {
        /*if (num <= 1) {
            mBadge.setVisibility(View.INVISIBLE);
        } else {
            mBadge.setVisibility(View.VISIBLE);
            if (num > 99)
                mBadge.setText("∞");
            else
                mBadge.setText(String.valueOf(num));
        }*/
    }
}
