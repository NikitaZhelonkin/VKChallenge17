package com.vk.challenge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by nikita on 13.09.17.
 */

public class ResizeTransformation extends BitmapTransformation {

    private static final String ID = "com.vk.challenge.ResizeTransformation";

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
        // Create a Canvas backed by the result Bitmap.
        Canvas canvas = new Canvas(result);
        canvas.scale(targetWidth/(float)toTransform.getWidth(),targetHeight/(float)toTransform.getHeight() );
        Paint paint = new Paint();
        // Draw the original Bitmap onto the result Bitmap with a transformation.
        canvas.drawBitmap(toTransform, 0, 0, paint);
        // Since we've replaced our original Bitmap, we return our new Bitmap here. Glide will
        // will take care of returning our original Bitmap to the BitmapPool for us.
        return result;
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update((ID + mDesiredWidth).getBytes(CHARSET));
    }

}
