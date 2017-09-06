package com.vk.challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vk.challenge.data.BackgroundItem;
import com.vk.challenge.data.BackgroundItemsProvider;
import com.vk.challenge.widget.PostView;
import com.vk.sdk.VKAccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity implements BackgroundThumbAdapter.OnItemClickListener {

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
        Toast.makeText(this, "TODO: change font", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.action_sticker_btn)
    public void onStickerClick(View v) {
        Toast.makeText(this, "TODO: open stickers", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.sendButton)
    public void onSendClick(View v) {
        Toast.makeText(this, "TODO: send", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, BackgroundItem item) {
        mThumbsAdapter.setCurrentItemPosition(position);
        mPostView.setBackground(item.getDrawable());
        mPostView.setTextColor(item.getTextColor());
    }
}
