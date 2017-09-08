package com.vk.challenge.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by nikita on 07.09.17.
 */

public class AndroidUtils {

    public interface OnPreDraw {
        void onPreDraw();
    }

    public static void onPreDraw(final View view, final OnPreDraw onPreDraw) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width > 0 && height > 0) {
            onPreDraw.onPreDraw();
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                onPreDraw.onPreDraw();
                return true;
            }
        });
    }

    public static int dpToPx(Context context, float value) {
        if (value == 0) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(dm.density * value);
    }


}
