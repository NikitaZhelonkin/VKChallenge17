package com.vk.challenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.vk.challenge.GlideApp;
import com.vk.challenge.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikita on 07.09.17.
 */

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String sticker);
    }

    private List<String> mData;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public void setData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public StickersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.list_item_sticker, parent, false));
    }

    @Override
    public void onBindViewHolder(StickersAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemView)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            GlideApp.with(itemView.getContext())
                    .load(mData.get(position))
                    .placeholder(null)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mImageView);
        }

        @OnClick(R.id.itemView)
        public void onItemClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mData.get(position));
            }
        }
    }
}
