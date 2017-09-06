package com.vk.challenge.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.vk.challenge.R;
import com.vk.challenge.widget.BeachDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItemsProvider {

    public static List<BackgroundItem> getItems(Context context){
        int lightTextColor = Color.WHITE;
        int darkTextColor = ContextCompat.getColor(context, R.color.colorText);
        List<BackgroundItem> items = new ArrayList<>();
        items.add(new BackgroundItem(
                R.drawable.thumb_white,
                darkTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_white))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_blue,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_blue))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_green,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_green))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_orange,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_orange))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_red,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_red))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_purple,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_purple))
        );
        items.add(new BackgroundItem(
                R.drawable.thumb_beach,
                lightTextColor,
                new BeachDrawable((LayerDrawable) ContextCompat.getDrawable(context, R.drawable.bg_beach)))
        );
        items.add(new BackgroundItem(
                R.drawable.thumb_stars,
                lightTextColor,
                ContextCompat.getDrawable(context, R.drawable.bg_stars))
        );
        return  items;
    }
}
