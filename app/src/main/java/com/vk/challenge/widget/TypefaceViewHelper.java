package com.vk.challenge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.vk.challenge.R;

/**
 * Created by nikita on 12.09.17.
 */

public class TypefaceViewHelper {

    private TypefaceView mView;

    private Context mContext;

    public static TypefaceViewHelper create(TypefaceView typefaceView){
        return new TypefaceViewHelper(typefaceView);
    }

    private TypefaceViewHelper(TypefaceView typefaceView) {
        mView = typefaceView;
        mContext = typefaceView.getContext();
    }

    public void applyFromAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TFView, defStyleAttr, defStyleRes);

        try {
            String typefaceName = a.getString(R.styleable.TFView_font);
            Typeface typeface = Typefaces.getTypeface(mContext, typefaceName);
            if (typeface != null) {
                mView.setTypeface(typeface);
            }
        } finally {
            a.recycle();
        }
    }
}
