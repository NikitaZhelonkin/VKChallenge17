package com.vk.challenge.data.provider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;

import com.vk.challenge.R;
import com.vk.challenge.data.model.BackgroundItem;
import com.vk.challenge.data.model.FontStyle;
import com.vk.challenge.data.model.NewBackgroundItem;
import com.vk.challenge.widget.CompositeDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItemsProvider {

    public static List<BackgroundItem> getItems(Context context) {
        List<BackgroundItem> items = new ArrayList<>();
        items.add(new BackgroundItem(
                R.drawable.thumb_white,
                new ColorDrawable(Color.WHITE)
        ));
        items.add(new BackgroundItem(
                R.drawable.bg_blue,
                ContextCompat.getDrawable(context, R.drawable.bg_blue)
        ));
        items.add(new BackgroundItem(
                R.drawable.bg_green,
                ContextCompat.getDrawable(context, R.drawable.bg_green)
        ));
        items.add(new BackgroundItem(
                R.drawable.bg_orange,
                ContextCompat.getDrawable(context, R.drawable.bg_orange)
        ));
        items.add(new BackgroundItem(
                R.drawable.bg_red,
                ContextCompat.getDrawable(context, R.drawable.bg_red)
        ));
        items.add(new BackgroundItem(
                R.drawable.bg_purple,
                ContextCompat.getDrawable(context, R.drawable.bg_purple)
        ));
        items.add(new BackgroundItem(
                R.drawable.thumb_beach,
                new CompositeDrawable((LayerDrawable) ContextCompat.getDrawable(context, R.drawable.bg_beach))
        ));
        items.add(new BackgroundItem(
                R.drawable.thumb_stars,
                ContextCompat.getDrawable(context, R.drawable.bg_stars)
        ));
        items.add(new NewBackgroundItem());
        return items;
    }
}
