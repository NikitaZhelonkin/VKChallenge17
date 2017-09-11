package com.vk.challenge.widget;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nikita on 11.09.17.
 */

public class ForegroundCompatHelper {

    private View mView;
    private Drawable mForeground;
    private boolean mForegroundBoundsChanged = false;

    public static ForegroundCompatHelper create(View view, AttributeSet attributeSet) {
        return new ForegroundCompatHelper(view, attributeSet);
    }

    private ForegroundCompatHelper(View view, AttributeSet attributeSet) {
        mView = view;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            TypedArray a = view.getContext().obtainStyledAttributes(attributeSet, new int[]{android.R.attr.foreground});
            setForeground(a.getDrawable(0));
            a.recycle();
        }
    }

    public boolean verifyDrawable(Drawable who) {
        return who == mForeground;
    }

    public void jumpDrawablesToCurrentState() {
        if (mForeground != null) mForeground.jumpToCurrentState();
    }

    public void drawableStateChanged() {
        if (mForeground != null && mForeground.isStateful()) {
            mForeground.setState(mView.getDrawableState());
            mView.invalidateDrawable(mForeground);
        }
    }

    public void setForeground(Drawable drawable) {
        if (mForeground != drawable) {
            if (mForeground != null) {
                mForeground.setCallback(null);
                mView.unscheduleDrawable(mForeground);
            }

            mForeground = drawable;

            if (drawable != null) {
                mView.setWillNotDraw(false);
                drawable.setCallback(mView);
                if (drawable.isStateful()) {
                    drawable.setState(mView.getDrawableState());
                }
            } else {
                mView.setWillNotDraw(true);
            }
            mView.requestLayout();
            mView.invalidate();
        }
    }

    public void onSizeChanged() {
        mForegroundBoundsChanged = true;
    }

    public void draw(Canvas canvas) {

        if (mForeground != null) {
            final Drawable foreground = mForeground;

            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;

                final int w = mView.getRight() - mView.getLeft();
                final int h = mView.getBottom() - mView.getTop();
                foreground.setBounds(0, 0, w, h);
            }

            foreground.draw(canvas);
        }
    }
}
