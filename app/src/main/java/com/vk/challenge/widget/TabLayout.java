package com.vk.challenge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.vk.challenge.R;

/**
 * Created by nikita on 10.09.17.
 */

public class TabLayout extends android.support.design.widget.TabLayout {

    private int mCustomViewResId;

    public TabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setScaleY(-1);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        mCustomViewResId = a.getResourceId(R.styleable.TabLayout_customView, -1);
        a.recycle();
    }

    @Override
    public void addTab(@NonNull Tab tab) {
        super.addTab(tab);
        View tabView = getTabView(tab.getPosition());
        if (tabView != null) {
            tabView.setScaleY(-1);
        }
    }

    @NonNull
    @Override
    public Tab newTab() {
        if (mCustomViewResId != -1) {
            return super.newTab().setCustomView(mCustomViewResId);
        } else {
            return super.newTab();
        }
    }

    View getTabView(int position) {
        Tab tab = getTabAt(position);
        if (tab == null) {
            return null;
        }
        if (tab.getCustomView() != null) {
            return tab.getCustomView();
        }
        return ((ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(position)).getChildAt(1);
    }

}
