package com.vk.challenge.data;

import android.graphics.drawable.Drawable;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItem {

    int mThumbResId;

    Drawable mDrawable;

    int mTextColor;

    public BackgroundItem(int thumbResId, int textColor, Drawable drawable) {
        mThumbResId = thumbResId;
        mDrawable = drawable;
        mTextColor = textColor;
    }

    public int getThumbResId() {
        return mThumbResId;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public int getTextColor() {
        return mTextColor;
    }
}
