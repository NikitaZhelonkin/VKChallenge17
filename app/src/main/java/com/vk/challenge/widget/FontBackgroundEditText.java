package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by nikita on 07.09.17.
 */

public class FontBackgroundEditText extends TFEditText {

    private static final int STROKE_WIDTH = 8;//dp

    private int mBgColor = Color.TRANSPARENT;

    private Paint mBgPaint;

    private Path mBgPath;

    private RectF mLineBounds = new RectF();

    public FontBackgroundEditText(Context context) {
        super(context);
        init(context);
    }

    public FontBackgroundEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontBackgroundEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setStrokeWidth((int) (dm.density * STROKE_WIDTH));
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setStrokeJoin(Paint.Join.ROUND);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPath = new Path();

    }

    public void setFontBackgroundColor(int color) {
        mBgColor = color;
        invalidate();
    }

    public int getFontBackgroundColor() {
        return mBgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        Layout layout = getLayout();
        if (layout == null || getText() == null || getText().length() == 0) {
            return;
        }
        mBgPaint.setColor(mBgColor);
        mBgPath.reset();

        int lineCount = layout.getLineCount();

        RectF line0Bounds = getLineBounds(0);
        mBgPath.moveTo(line0Bounds.left, line0Bounds.top);

        for (int i = 0; i < lineCount; i++) {
            RectF lineBounds = getLineBounds(i);
            mBgPath.lineTo(lineBounds.right, lineBounds.top);
            mBgPath.lineTo(lineBounds.right, lineBounds.bottom);
        }

        for (int i = lineCount - 1; i >= 0; i--) {
            RectF lineBounds = getLineBounds(i);
            mBgPath.lineTo(lineBounds.left, lineBounds.bottom);
            mBgPath.lineTo(lineBounds.left, lineBounds.top);
        }

        canvas.drawPath(mBgPath, mBgPaint);
    }

    private RectF getLineBounds(int line) {
        float left = getLayout().getLineLeft(line) - getLineSpacingExtra();
        float right = getLayout().getLineRight(line) + getLineSpacingExtra();
        float top = getLayout().getLineTop(line);
        float bot = getLayout().getLineBottom(line);
        if (line == 0) {
            top += getPaddingTop();
        }
        mLineBounds.set(left, top, right, bot);
        return mLineBounds;
    }
}
