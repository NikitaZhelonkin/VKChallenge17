package com.vk.challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.vk.challenge.adapter.GalleryAdapter;
import com.vk.challenge.data.LoadGalleryTask;
import com.vk.challenge.data.model.GalleryItem;
import com.vk.challenge.data.provider.GalleryPhotosProvider;
import com.vk.challenge.widget.GalleryLayoutManager;

import java.util.List;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryWindow extends PopupWindow implements
        LoadGalleryTask.Callback,
        GalleryAdapter.Callback,
        GalleryPhotosProvider.OnContentChangeListener{


    private LoadGalleryTask mLoadGalleryTask;

    private GalleryAdapter mGalleryAdapter;

    private GalleryAdapter.Callback mCallback;

    private GalleryPhotosProvider mPhotosProvider;

    @SuppressLint("InflateParams")
    public GalleryWindow(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_gallery, null), ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.keyboard_height), false);
        setAnimationStyle(R.style.Animation_Slide);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        mGalleryAdapter = new GalleryAdapter();
        mGalleryAdapter.setCallback(this);
        RecyclerView recyclerView = (RecyclerView) getContentView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GalleryLayoutManager(context));

        recyclerView.setAdapter(mGalleryAdapter);

        mPhotosProvider = GalleryPhotosProvider.getInstance(context);
        mPhotosProvider.setContentObserver(this);
    }

    public void setCallback(GalleryAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onGalleryItemSelected(GalleryItem galleryItem) {
        if (mCallback != null) {
            mCallback.onGalleryItemSelected(galleryItem);
        }
    }

    @Override
    public void onTakePhotoClick() {
        if (mCallback != null) {
            mCallback.onTakePhotoClick();
        }
    }

    @Override
    public void onOpenExternalGalleryClick() {
        if (mCallback != null) {
            mCallback.onOpenExternalGalleryClick();
        }
    }

    public void select(Uri uri){
        mGalleryAdapter.selectItem(new GalleryItem(uri), false);
    }

    public void load() {
        cancelLoading();
        mLoadGalleryTask = new LoadGalleryTask(getContentView().getContext());
        mLoadGalleryTask.setCallback(this);
        mLoadGalleryTask.execute();
    }

    public void release() {
        mPhotosProvider.setContentObserver(null);
        if (isShowing()) {
            dismiss();
        }
        cancelLoading();

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (mGalleryAdapter.getItemCount() > 1) {
            mGalleryAdapter.selectItem(mGalleryAdapter.getSelected() == null ?
                    mGalleryAdapter.getItem(1) : mGalleryAdapter.getSelected(), true);
        }
    }

    private void cancelLoading() {
        if (mLoadGalleryTask != null) {
            mLoadGalleryTask.setCallback(null);
            mLoadGalleryTask.cancel(false);
            mLoadGalleryTask = null;
        }
    }


    @Override
    public void onGalleryLoaded(List<GalleryItem> items) {
        List<GalleryItem> oldItems = mGalleryAdapter.getData();
        mGalleryAdapter.setData(items);
        if (oldItems == null || oldItems.size() == 0) {
            selectItem();
        }
    }

    @Override
    public void onChange() {
        load();
    }

    private void selectItem() {
        if (isShowing()) {
            List<GalleryItem> items = mGalleryAdapter.getData();
            if (items != null && items.size() != 0) {
                mGalleryAdapter.selectItem(items.get(0), true);
            }
        }
    }

}
