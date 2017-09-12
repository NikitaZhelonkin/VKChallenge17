package com.vk.challenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vk.challenge.R;
import com.vk.challenge.data.model.GalleryItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Callback {
        void onGalleryItemSelected(GalleryItem galleryItem);

        void onTakePhotoClick();

        void onOpenExternalGalleryClick();
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<GalleryItem> mData;

    public Callback mCallback;

    private int mSelectedPosition = -1;
    private int mLastSelectedPosition = -1;

    public void setData(List<GalleryItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<GalleryItem> getData() {
        return mData;
    }

    public void selectItem(int adapterPosition, boolean notify) {
        if (adapterPosition == mSelectedPosition) {
            return;
        }
        if (mCallback != null && notify) {
            mCallback.onGalleryItemSelected(mData.get(adapterPosition - 1));
        }
        mLastSelectedPosition = mSelectedPosition;
        mSelectedPosition = adapterPosition;

        if (mLastSelectedPosition != -1) {
            notifyItemChanged(mLastSelectedPosition, "selection");
        }
        if (mSelectedPosition != -1) {
            notifyItemChanged(mSelectedPosition, "selection");
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.header_gallery, parent, false));
        } else {
            return new ViewHolder(inflater.inflate(R.layout.list_item_gallery, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (holder instanceof ViewHolder) {
            if (payloads != null && payloads.size() > 0) {
                ((ViewHolder) holder).mImageView.setSelected(position == mSelectedPosition);
                return;
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(mData.get(position - 1), position == mSelectedPosition);
        }
    }

    @Override
    public int getItemCount() {
        int dataCount = mData != null ? mData.size() : 0;
        return dataCount + 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.photo_btn)
        public void onTakePhotoClick(View v) {
            if (mCallback != null) {
                mCallback.onTakePhotoClick();
            }
        }

        @OnClick(R.id.gallery_btn)
        public void onOpenGalleryClick(View v) {
            if (mCallback != null) {
                mCallback.onOpenExternalGalleryClick();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemView)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final GalleryItem galleryItem, boolean selected) {
            mImageView.setSelected(selected);
            if (galleryItem.getUri() != null) {
                Picasso.with(itemView.getContext())
                        .load(galleryItem.getUri())
                        .centerCrop()
                        .transform(new RoundedTransformationBuilder().cornerRadiusDp(4).build())
                        .resizeDimen(R.dimen.gallery_item_approx_size, R.dimen.gallery_item_approx_size)
                        .into(mImageView);
            } else {
                mImageView.setImageDrawable(null);
            }
        }

        @OnClick(R.id.itemView)
        public void onItemClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                selectItem(adapterPosition, true);
            }
        }

    }
}
