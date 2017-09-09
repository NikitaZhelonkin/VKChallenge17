package com.vk.challenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vk.challenge.R;
import com.vk.challenge.data.model.BackgroundItem;
import com.vk.challenge.data.model.NewBackgroundItem;
import com.vk.challenge.widget.ThumbView;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemSelectedListener {
        void onItemSelected(BackgroundItem item);
    }

    private List<BackgroundItem> mData;

    private OnItemSelectedListener mOnItemSelectedListener;

    private int mLastSelectedPosition;
    private int mSelectedPosition;

    public void setItems(List<BackgroundItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void selectItem(int position) {
        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(mData.get(position));
        }
        mLastSelectedPosition = mSelectedPosition;
        mSelectedPosition = position;

        if (mLastSelectedPosition != -1) {
            notifyItemChanged(mLastSelectedPosition, "selection");
        }
        if (mSelectedPosition != -1) {
            notifyItemChanged(mSelectedPosition, "selection");
        }
    }

    public void backSelection(){
        selectItem(mLastSelectedPosition);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_thumb, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (holder instanceof ViewHolder) {
            if (payloads != null && payloads.size() > 0) {
                ((ViewHolder) holder).mThumbView.setSelected(position == mSelectedPosition);
                return;
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(mData.get(position), position == mSelectedPosition);
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ThumbView mThumbView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mThumbView = (ThumbView) itemView;
        }

        public void bind(BackgroundItem item, boolean current) {
            mThumbView.setSelected(current);
            mThumbView.setImageResource(item.getThumbResId());
            mThumbView.setScaleType(item instanceof NewBackgroundItem ?
                    ImageView.ScaleType.CENTER :
                    ImageView.ScaleType.FIT_CENTER);
        }

        @OnClick(R.id.itemView)
        public void onItemClick(View v) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            selectItem(position);
        }
    }
}
