package com.vk.challenge.widget;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by nikita on 09.09.17.
 */

public class ResizeTransformation implements Transformation {

    private int mDesiredWidth;

    public ResizeTransformation(int desiredWidth) {
        mDesiredWidth = desiredWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = mDesiredWidth;
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "resizeTransformation" + mDesiredWidth;
    }
}
