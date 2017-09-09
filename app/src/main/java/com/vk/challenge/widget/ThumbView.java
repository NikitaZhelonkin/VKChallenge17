package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by nikita on 06.09.17.
 */

public class ThumbView extends RoundedImageView {


    public ThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //TODO draw foreground for old api
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        }
    }
}
