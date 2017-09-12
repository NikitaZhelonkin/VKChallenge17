package com.vk.challenge.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by nikita on 07.09.17.
 */

public class AndroidUtils {

    private static Point sDisplaySize;

    public static void onPreDraw(final View view, final Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                runnable.run();;
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

    public static void  hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Point getDisplaySize(Activity activity) {
        if (sDisplaySize != null && sDisplaySize.x != 0 && sDisplaySize.y != 0) {
            return sDisplaySize;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return sDisplaySize = size;
    }


}
