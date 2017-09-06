package com.vk.challenge.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vk.challenge.R;

/**
 * Created by nikita on 06.09.17.
 */

public class BeachDrawable extends Drawable {

    private Drawable mBackground;
    private Drawable mTopImage;
    private Drawable mBottomImage;

    public BeachDrawable(LayerDrawable layerDrawable) {
        mBackground = layerDrawable.findDrawableByLayerId(R.id.background_image);
        mTopImage = layerDrawable.findDrawableByLayerId(R.id.top_image);
        mBottomImage = layerDrawable.findDrawableByLayerId(R.id.bottom_image);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mBackground.draw(canvas);
        mTopImage.draw(canvas);
        mBottomImage.draw(canvas);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mBackground.setAlpha(alpha);
        mTopImage.setAlpha(alpha);
        mBottomImage.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mBackground.setColorFilter(colorFilter);
        mTopImage.setColorFilter(colorFilter);
        mBottomImage.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mBackground.setBounds(bounds);

        float topRatio = mTopImage.getIntrinsicWidth() / (float) mTopImage.getIntrinsicHeight();
        float botRatio = mBottomImage.getIntrinsicWidth() / (float) mBottomImage.getIntrinsicHeight();

        int topHeight = (int) (bounds.width() / topRatio);
        int botHeight = (int) (bounds.width() / botRatio);

        mTopImage.setBounds(bounds.left, bounds.top, bounds.right, bounds.top + topHeight);
        mBottomImage.setBounds(bounds.left, bounds.bottom - botHeight, bounds.right, bounds.bottom);
    }

}
