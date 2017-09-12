package com.vk.challenge.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by nikita on 12.09.17.
 */

public class TFTextView extends AppCompatTextView implements TypefaceView {

    public TFTextView(Context context) {
        super(context);
    }

    public TFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypefaceViewHelper.create(this).applyFromAttributes(attrs, android.R.attr.textViewStyle, 0);
    }
}