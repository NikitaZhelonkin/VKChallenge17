package com.vk.challenge.data.model;

import android.graphics.Color;

/**
 * Created by nikita on 07.09.17.
 */

public class FontStyle {

    public static final FontStyle DEFAULT_BLANK = new FontStyle(Color.parseColor("#2c2d2e"));
    public static final FontStyle LIGHT_BLANK = new FontStyle(Color.parseColor("#2c2d2e"), false,
            Color.parseColor("#dddddd"));
    public static final FontStyle DARK_BLANK = new FontStyle(Color.WHITE, false, Color.BLACK);

    public static final FontStyle DEFAULT_IMAGE = new FontStyle(Color.WHITE, true);
    public static final FontStyle LIGHT_IMAGE = new FontStyle(Color.WHITE, true, Color.parseColor("#5bffffff"));
    public static final FontStyle DARK_IMAGE = new FontStyle(Color.parseColor("#2c2d2e"), false, Color.WHITE);


    private int mTextColor;

    private boolean mShadow;

    private int mFontBackgroundColor;

    public FontStyle(int textColor) {
        this(textColor, false, Color.TRANSPARENT);
    }

    public FontStyle(int textColor, boolean shadow) {
        this(textColor, shadow, Color.TRANSPARENT);
    }

    public FontStyle(int textColor, boolean shadow, int fontBackgroundColor) {
        mTextColor = textColor;
        mShadow = shadow;
        mFontBackgroundColor = fontBackgroundColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public boolean isShadow() {
        return mShadow;
    }

    public int getFontBackgroundColor() {
        return mFontBackgroundColor;
    }
}
