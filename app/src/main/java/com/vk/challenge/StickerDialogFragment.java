package com.vk.challenge;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.challenge.adapter.StickersAdapter;
import com.vk.challenge.data.provider.StickersProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nikita on 07.09.17.
 */

public class StickerDialogFragment extends BottomSheetDialogFragment implements
        StickersAdapter.OnItemClickListener {

    public interface Callback {
        void onStickerSelected(String sticker);
    }

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.divider)
    View mDivider;

    private Callback mCallback;

    public static StickerDialogFragment create() {
        return new StickerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.Theme_BottomSheetDialog_Stickers);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stickers, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        StickersAdapter stickersAdapter = new StickersAdapter();
        stickersAdapter.setOnItemClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setAdapter(stickersAdapter);
        stickersAdapter.setData(StickersProvider.getStickers());
    }

    @Override
    public void onItemClick(String sticker) {
        if (mCallback != null) {
            mCallback.onStickerSelected(sticker);
        }
        dismiss();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int offset = recyclerView.computeVerticalScrollOffset();
            mDivider.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
        }
    };
}
