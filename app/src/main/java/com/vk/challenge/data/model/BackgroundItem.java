package com.vk.challenge.data.model;

import android.graphics.drawable.Drawable;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItem {

    int mThumbResId;

    Drawable mDrawable;


    public BackgroundItem(int thumbResId, Drawable drawable) {
        mThumbResId = thumbResId;
        mDrawable = drawable;
    }

    public int getThumbResId() {
        return mThumbResId;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }
}
