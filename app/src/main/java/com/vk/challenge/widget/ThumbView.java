package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by nikita on 06.09.17.
 */

public class ThumbView extends MaskImageView {


    public ThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //TODO draw foreground for old api
    }

}
