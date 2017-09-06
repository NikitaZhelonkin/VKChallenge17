package com.vk.challenge.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.vk.challenge.R;

/**
 * Created by nikita on 06.09.17.
 */

public class PostView extends FrameLayout {

    private EditText mEditText;

    private int mMaxHeight;

    public PostView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(R.id.post_edit_text);
    }

    public void setTextColor(int color){
        mEditText.setTextColor(ColorStateList.valueOf(color));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        if (height != 0) {
            mMaxHeight = mMaxHeight == 0 ? height : Math.min(height, mMaxHeight);
        }
        if (mMaxHeight != 0) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY));
        }
    }
}
