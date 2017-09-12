package com.vk.challenge.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.vk.challenge.R;

/**
 * Created by nikita on 12.09.17.
 */

public class TFButton extends AppCompatButton implements TypefaceView {

    public TFButton(Context context) {
        super(context);
        init(null);
    }

    public TFButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TFButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypefaceViewHelper.create(this).applyFromAttributes(attrs, R.attr.buttonStyle, 0);
    }
}
