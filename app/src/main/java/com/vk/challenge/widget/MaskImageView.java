package com.vk.challenge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.vk.challenge.R;

/**
 * Created by nikita on 06.09.17.
 */

public class MaskImageView extends AppCompatImageView {

    private Rect mRect = new Rect();
    private Rect mImageRect = new Rect();
    private RectF mMaskRect = new RectF();

    private Paint mPaintSrc;
    private Paint mPaintDst;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Bitmap mBitmapDst;
    private Bitmap mBitmapSrc;
    private Canvas mCanvasSrc;
    private Canvas mCanvasDst;

    private Drawable mMaskDrawable;

    private PorterDuff.Mode mMode = PorterDuff.Mode.DST_IN;


    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaintDst = new Paint();
        mPaintSrc = new Paint();
        mPaintSrc.setXfermode(new PorterDuffXfermode(mMode));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskImageView, 0, 0);
        try {
            mMaskDrawable = a.getDrawable(R.styleable.MaskImageView_mask);
            if (mMaskDrawable == null) {
                throw new IllegalArgumentException("mask param required");
            }
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        //background
        if (getBackground() != null) {
            getBackground().setBounds(mRect);
            getBackground().draw(canvas);
        }
        //original bitmap
        if (mBitmapDst != null) {
            mCanvasDst.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            if (getDrawable() != null) {
                getDrawable().draw(mCanvasDst);
            }

            mCanvas.drawBitmap(mBitmapDst, 0, 0, mPaintDst);
        }

        // mask bitmap bitmap
        if (mBitmapSrc != null) {
            mCanvasSrc.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mMaskDrawable.setBounds(mImageRect);
            mMaskDrawable.draw(mCanvasSrc);
            mCanvas.drawBitmap(mBitmapSrc, 0, 0, mPaintSrc);
        }

        //result bitmap
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(0, 0, getWidth(), getHeight());
        mImageRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mMaskRect.set(mImageRect);

        if (mBitmapSrc != null) {
            mBitmapSrc.recycle();
        }
        mBitmapSrc = Bitmap.createBitmap(mRect.width(), mRect.height(),
                Bitmap.Config.ARGB_8888);
        mCanvasSrc = new Canvas(mBitmapSrc);

        if (mBitmapDst != null) {
            mBitmapDst.recycle();
        }
        mBitmapDst = Bitmap.createBitmap(mRect.width(), mRect.height(),
                Bitmap.Config.ARGB_8888);
        mCanvasDst = new Canvas(mBitmapDst);

        if (mBitmap != null) {
            mBitmap.recycle();
        }
        mBitmap = Bitmap.createBitmap(mRect.width(), mRect.height(),
                Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

}

