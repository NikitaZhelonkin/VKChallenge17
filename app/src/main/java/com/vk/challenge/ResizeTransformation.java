package com.vk.challenge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Created by nikita on 13.09.17.
 */

public class ResizeTransformation extends BitmapTransformation {

    private static final String ID = "com.vk.challenge.load.resource.bitmap.ResizeTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int mDesiredWidth;

    public ResizeTransformation(int desiredWidth) {
        mDesiredWidth = desiredWidth;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int targetWidth = mDesiredWidth;
        double aspectRatio = (double) toTransform.getHeight() / (double) toTransform.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap result = pool.get(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.scale(targetWidth / (float) toTransform.getWidth(), targetHeight / (float) toTransform.getHeight());
        Paint paint = new Paint();
        canvas.drawBitmap(toTransform, 0, 0, paint);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ResizeTransformation) && ((ResizeTransformation) o).mDesiredWidth == mDesiredWidth;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + mDesiredWidth;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(mDesiredWidth).array();
        messageDigest.update(radiusData);
    }

}
