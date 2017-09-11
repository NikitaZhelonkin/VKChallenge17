package com.vk.challenge.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by nikita on 10.09.17.
 */

public class TabLayout extends android.support.design.widget.TabLayout {

    public TabLayout(Context context) {
        super(context);
        init(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setScaleY(-1);
    }

    @Override
    public void addTab(@NonNull Tab tab) {
        super.addTab(tab);
        getTabTextView(tab.getPosition()).setScaleY(-1);
    }

    private LinearLayout tabStrip(){
        return (LinearLayout)getChildAt(0);
    }

    TextView getTabTextView(int position){
       return (TextView) ((ViewGroup)((ViewGroup)getChildAt(0)).getChildAt(position)).getChildAt(1);
    }

}
