package com.vk.challenge.data;

import android.graphics.drawable.Drawable;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItem {

    int mThumbResId;

    Drawable mDrawable;

    FontStyle mFontStyle;

    public BackgroundItem(int thumbResId, Drawable drawable, FontStyle fontStyle) {
        mThumbResId = thumbResId;
        mDrawable = drawable;
        mFontStyle = fontStyle;
    }

    public int getThumbResId() {
        return mThumbResId;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public FontStyle getFontStyle() {
        return mFontStyle;
    }
}
