package com.vk.challenge;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.vk.challenge.adapter.GalleryAdapter;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryWindow extends PopupWindow {

    private RecyclerView mRecyclerView;


    public GalleryWindow(Context context){
        super(LayoutInflater.from(context).inflate(R.layout.layout_gallery, null), ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.keyboard_height), false);
        setAnimationStyle(R.style.Animation_Popup);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        mRecyclerView = (RecyclerView) getContentView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
        GalleryAdapter galleryAdapter = new GalleryAdapter();
        mRecyclerView.setAdapter(galleryAdapter);
    }

}
