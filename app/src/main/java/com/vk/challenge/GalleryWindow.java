package com.vk.challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.vk.challenge.adapter.GalleryAdapter;
import com.vk.challenge.data.LoadGalleryTask;
import com.vk.challenge.data.model.GalleryItem;
import com.vk.challenge.widget.GalleryLayoutManager;

import java.util.List;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryWindow extends PopupWindow implements
        LoadGalleryTask.Callback,
        GalleryAdapter.Callback{


    private LoadGalleryTask mLoadGalleryTask;

    private GalleryAdapter mGalleryAdapter;

    private GalleryAdapter.Callback mCallback;

    @SuppressLint("InflateParams")
    public GalleryWindow(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_gallery, null), ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.keyboard_height), false);
        setAnimationStyle(R.style.Animation_Popup);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        mGalleryAdapter = new GalleryAdapter();
        mGalleryAdapter.setCallback(this);
        RecyclerView recyclerView = (RecyclerView) getContentView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GalleryLayoutManager(context));

        recyclerView.setAdapter(mGalleryAdapter);
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

    public void addAndSelect(GalleryItem galleryItem){
        mGalleryAdapter.addAndSelect(galleryItem);
    }

    public void clearSelection(){
        mGalleryAdapter.selectItem(RecyclerView.NO_POSITION, false);
    }

    public void load() {
        cancelLoading();
        mLoadGalleryTask = new LoadGalleryTask(getContentView().getContext());
        mLoadGalleryTask.setCallback(this);
        mLoadGalleryTask.execute();
    }

    public void release() {
        if (isShowing()) {
            dismiss();
        }
        cancelLoading();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        int selectPosition = mGalleryAdapter.getSelectedPosition() != RecyclerView.NO_POSITION ?
                mGalleryAdapter.getSelectedPosition() : 1;
        if (selectPosition < mGalleryAdapter.getItemCount()) {
            mGalleryAdapter.selectItem(selectPosition, true);
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

    private void selectItem() {
        if (isShowing()) {
            List<GalleryItem> items = mGalleryAdapter.getData();
            if (items != null && items.size() != 0) {
                mGalleryAdapter.selectItem(1, true);
            }
        }
    }

}
