package com.vk.challenge.widget;

import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by nikita on 11.09.17.
 */

public class ThumbsLayoutManager extends LinearLayoutManager {


    public ThumbsLayoutManager(Context context) {
        super(context);
    }

    public ThumbsLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ThumbsLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller {

        private static final int DEFAULT_SNAP_DURATION = 300;

        private Interpolator mSnapInterpolator = new FastOutSlowInInterpolator();

        private int mSnapDuration = DEFAULT_SNAP_DURATION;

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected void onTargetFound(View targetView, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
            final int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
            final int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
            action.update(-dx, -dy, mSnapDuration, mSnapInterpolator);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }
    }
}
