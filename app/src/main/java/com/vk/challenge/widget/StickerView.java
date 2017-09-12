package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by nikita on 07.09.17.
 */

public class StickerView extends AppCompatImageView {

    public interface OnMoveListener{
        void onStartMove(StickerView stickerView);
        void onMove(StickerView stickerView);
        void onMoveEnd(StickerView stickerView);
    }

    private float mDownX;
    private float mDownY;
    private boolean mDragging;
    private int mTouchSlop;

    private Point mPosition = new Point();

    private OnMoveListener mOnMoveListener;

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setPosition(Point position) {
        mPosition = position;
        updatePosition();
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        mOnMoveListener = onMoveListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mDownX;
                float deltaY = y - mDownY;

                if (!mDragging && (Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop)) {
                    mDragging = true;
                    deltaX = 0;
                    deltaY = 0;
                    if (mOnMoveListener != null) {
                        mOnMoveListener.onStartMove(this);
                    }
                }
                if (mDragging) {
                    mPosition.offset((int) deltaX, (int) deltaY);
                    updatePosition();
                    if (mOnMoveListener != null) {
                        mOnMoveListener.onMove(this);
                    }
                }
                mDownX = x;
                mDownY = y;
                return mDragging;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                if (mOnMoveListener != null) {
                    mOnMoveListener.onMoveEnd(this);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updatePosition(){
        setX(mPosition.x);
        setY(mPosition.y);
    }
}
