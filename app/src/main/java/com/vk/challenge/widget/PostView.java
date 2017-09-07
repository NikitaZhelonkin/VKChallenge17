package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.vk.challenge.R;
import com.vk.challenge.data.FontStyle;

/**
 * Created by nikita on 06.09.17.
 */

public class PostView extends FrameLayout {

    private FontBackgroundEditText mEditText;

    private FontStyle mFontStyle;

    public PostView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    public void setFontStyle(FontStyle fontStyle) {
        mEditText.setFontBackgroundColor(fontStyle.getFontBackgroundColor());
        mEditText.setTextColor(fontStyle.getTextColor());
        int shadowRadius = fontStyle.isShadow() ? 2 : 0;
        int shadowDy = fontStyle.isShadow() ? 2 : 0;
        int shadowColor = fontStyle.isShadow() ? Color.parseColor("#1e000000") : 0;
        mEditText.setShadowLayer(shadowRadius, 0, shadowDy, shadowColor);
        mFontStyle  = fontStyle;
    }

    public FontStyle getFontStyle() {
        return mFontStyle;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (FontBackgroundEditText) findViewById(R.id.post_edit_text);
    }

    public Bitmap createBitmap() {
        setEditTextFocusable(false);
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw(c);
        setEditTextFocusable(true);
        return b;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredHeight() > getMeasuredWidth()) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }
    }

    private void setEditTextFocusable(boolean focusable) {
        mEditText.setFocusable(focusable);
        mEditText.setFocusableInTouchMode(focusable);
    }
}
