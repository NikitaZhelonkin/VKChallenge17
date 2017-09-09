package com.vk.challenge.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by nikita on 08.09.17.
 */

public class KeyboardDetector {

    public interface Listener {
        void onKeyboardDetected(boolean visible);
    }

    private Listener mListener;

    private int mKeyboardHeight;

    private int mLastHeight = 0;

    private Context mContext;

    private boolean mIsKeyboardVisible;

    public KeyboardDetector(Activity activity) {
        mContext = activity;
        final View rootView = activity.findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mLastHeight != rootView.getHeight()) {
                    onSizeChanged(rootView);
                    mLastHeight = rootView.getHeight();
                }

            }
        });
    }

    public void setKeyboardListener(Listener listener) {
        mListener = listener;
    }

    private void onSizeChanged(View rootView) {
        int heightDiff = calculateKeyboardHeight(rootView);
        if (heightDiff > AndroidUtils.dpToPx(mContext, 200)) {
            mIsKeyboardVisible = true;
            mKeyboardHeight = heightDiff;
        }else{
            mIsKeyboardVisible = false;
        }
        if (mListener != null) {
            mListener.onKeyboardDetected(mIsKeyboardVisible);
        }
    }

    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    public boolean isKeyboardVisible() {
        return mIsKeyboardVisible;
    }


    private Rect mRect = new Rect();

    private int calculateKeyboardHeight(View rootView) {
        rootView.getWindowVisibleDisplayFrame(mRect);
        int usableViewHeight = rootView.getHeight();
        int statusBarHeight = mRect.top;
        int size = AndroidUtils.getDisplaySize((Activity) mContext).y - statusBarHeight - usableViewHeight;
        if (size <= statusBarHeight) {
            size = 0;
        }
        return size;
    }
}