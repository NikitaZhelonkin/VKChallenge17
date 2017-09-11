package com.vk.challenge.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.vk.challenge.R;

/**
 * Created by nikita on 08.09.17.
 */

public class KeyboardDetector {

    public interface Listener {
        void onKeyboardDetected(boolean visible);
    }

    private Listener mListener;

    private static int keyboardHeight;

    private int mLastHeight = 0;

    private Context mContext;

    private boolean mIsKeyboardVisible;

    public KeyboardDetector(Activity activity) {
        mContext = activity;
        final View rootView = activity.findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Math.abs(mLastHeight - rootView.getHeight()) > AndroidUtils.dpToPx(mContext, 100)) {
                    onSizeChanged(rootView);
                    mLastHeight = rootView.getHeight();
                }

            }
        });
        keyboardHeight = mContext.getResources().getDimensionPixelSize(R.dimen.keyboard_height);
    }

    public void setKeyboardListener(Listener listener) {
        mListener = listener;
    }

    private void onSizeChanged(View rootView) {
        int heightDiff = calculateKeyboardHeight(rootView);
        if (heightDiff > AndroidUtils.dpToPx(mContext, 200)) {
            mIsKeyboardVisible = true;
            keyboardHeight = heightDiff;
        }else{
            mIsKeyboardVisible = false;
        }
        if (mListener != null) {
            mListener.onKeyboardDetected(mIsKeyboardVisible);
        }
    }

    public static int getKeyboardHeight() {
        return keyboardHeight;
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