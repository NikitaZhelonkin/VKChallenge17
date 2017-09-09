package com.vk.challenge.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by nikita on 09.09.17.
 */

//TODO draw foreground for old api
public class GalleryImageView extends AppCompatImageView {


    public GalleryImageView(Context context) {
        super(context);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() != getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        }
    }
}
