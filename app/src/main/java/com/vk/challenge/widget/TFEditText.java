package com.vk.challenge.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by nikita on 12.09.17.
 */

public class TFEditText extends AppCompatEditText implements TypefaceView {

    public TFEditText(Context context) {
        super(context);
    }

    public TFEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TFEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypefaceViewHelper.create(this).applyFromAttributes(attrs, 0, 0);
    }
}
