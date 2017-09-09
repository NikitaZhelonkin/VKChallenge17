package com.vk.challenge.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryLayoutManager extends GridLayoutManager {


    public GalleryLayoutManager(Context context) {
        super(context, 2, HORIZONTAL, false);
        setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
    }

}
