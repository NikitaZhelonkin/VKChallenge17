package com.vk.challenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.challenge.data.BackgroundItem;
import com.vk.challenge.widget.ThumbView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikita on 06.09.17.
 */

public class BackgroundThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, BackgroundItem item);
    }

    private List<BackgroundItem> mData;

    private OnItemClickListener mOnItemClickListener;

    private int mCurrentItemPosition;

    public void setItems(List<BackgroundItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setCurrentItemPosition(int currentItemPosition) {
        mCurrentItemPosition = currentItemPosition;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_thumb, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(mData.get(position), position == mCurrentItemPosition);
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
        }

        @OnClick(R.id.itemView)
        public void onItemClick(View v) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, mData.get(position));
            }
        }
    }
}
