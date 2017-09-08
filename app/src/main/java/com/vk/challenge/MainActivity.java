package com.vk.challenge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vk.challenge.adapter.BackgroundThumbAdapter;
import com.vk.challenge.data.model.BackgroundItem;
import com.vk.challenge.data.model.NewBackgroundItem;
import com.vk.challenge.data.provider.BackgroundItemsProvider;
import com.vk.challenge.data.model.FontStyle;
import com.vk.challenge.widget.PostView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity implements
        BackgroundThumbAdapter.OnItemClickListener,
        StickerDialogFragment.Callback{

    @BindView(R.id.post_view)
    PostView mPostView;
    @BindView(R.id.sendButton)
    Button mButton;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    BackgroundThumbAdapter mThumbsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VKAccessToken token = VKAccessToken.currentToken();
        if (token == null || token.isExpired()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mThumbsAdapter = new BackgroundThumbAdapter();
        mThumbsAdapter.setOnItemClickListener(this);
        mThumbsAdapter.setItems(BackgroundItemsProvider.getItems(this));

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mThumbsAdapter);
    }

    @OnTextChanged(R.id.post_edit_text)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mButton.setEnabled(s.length() != 0);
    }

    @OnClick(R.id.action_font_btn)
    public void onFontClick(View v) {
        if (mPostView.getFontStyle() == FontStyle.LIGHT) {
            mPostView.setFontStyle(FontStyle.DARK);
        } else {
            mPostView.setFontStyle(FontStyle.LIGHT);
        }
    }

    @OnClick(R.id.action_sticker_btn)
    public void onStickerClick(View v) {
        StickerDialogFragment.create().show(getSupportFragmentManager(), "strickers");
    }

    @OnClick(R.id.sendButton)
    public void onSendClick(View v) {
        loadPhotoToMyWall(mPostView.createBitmap(), null);
    }

    @Override
    public void onStickerSelected(String sticker) {
        mPostView.addSticker(sticker);
    }

    @Override
    public void onItemClick(int position, BackgroundItem item) {
        mThumbsAdapter.setCurrentItemPosition(position);
        if (item instanceof NewBackgroundItem) {
            Toast.makeText(this, "Open gallery preview", Toast.LENGTH_SHORT).show();
        } else {
            mPostView.setBackground(item.getDrawable());
            mPostView.setFontStyle(item.getFontStyle());
        }
    }


    private void makePost(VKAttachments att, String msg, final int ownerId) {
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.OWNER_ID, String.valueOf(ownerId));
        parameters.put(VKApiConst.ATTACHMENTS, att);
        parameters.put(VKApiConst.MESSAGE, msg);
        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPhotoToMyWall(final Bitmap photo, final String message) {
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo,
                VKImageParameters.jpgImage(0.9f)), getMyId(), 0);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                if (!photo.isRecycled()) {
                    photo.recycle();
                }
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel), message, getMyId());
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }
}
