package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryImageView extends AppCompatImageView {

    private ForegroundCompatHelper mForegroundHelper;

    public GalleryImageView(Context context) {
        super(context);
        init(context, null);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mForegroundHelper = ForegroundCompatHelper.create(this, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable dr) {
        return super.verifyDrawable(dr) || mForegroundHelper.verifyDrawable(dr);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        mForegroundHelper.jumpDrawablesToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mForegroundHelper.drawableStateChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundHelper.onSizeChanged();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mForegroundHelper.draw(canvas);
    }
}
