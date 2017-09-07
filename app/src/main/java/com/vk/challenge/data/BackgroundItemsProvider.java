package com.vk.challenge.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;

import com.vk.challenge.R;
import com.vk.challenge.widget.BeachDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundItemsProvider {

    public static List<BackgroundItem> getItems(Context context){
        List<BackgroundItem> items = new ArrayList<>();
        items.add(new BackgroundItem(
                R.drawable.thumb_white,
                ContextCompat.getDrawable(context, R.drawable.bg_white),
                new FontStyle(ContextCompat.getColor(context, R.color.colorText)))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_blue,
                ContextCompat.getDrawable(context, R.drawable.bg_blue),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_green,
                ContextCompat.getDrawable(context, R.drawable.bg_green),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_orange,
                ContextCompat.getDrawable(context, R.drawable.bg_orange),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_red,
                ContextCompat.getDrawable(context, R.drawable.bg_red),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.bg_purple,
                ContextCompat.getDrawable(context, R.drawable.bg_purple),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.thumb_beach,
                new BeachDrawable((LayerDrawable) ContextCompat.getDrawable(context, R.drawable.bg_beach)),
                new FontStyle(Color.WHITE, true))
        );
        items.add(new BackgroundItem(
                R.drawable.thumb_stars,
                ContextCompat.getDrawable(context, R.drawable.bg_stars),
                new FontStyle(Color.WHITE, true))
        );
        return  items;
    }
}
