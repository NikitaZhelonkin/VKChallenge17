package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by nikita on 06.09.17.
 */

public class ThumbView extends RoundedImageView {

    private ForegroundCompatHelper mForegroundHelper;

    public ThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mForegroundHelper = ForegroundCompatHelper.create(this, attrs);
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
