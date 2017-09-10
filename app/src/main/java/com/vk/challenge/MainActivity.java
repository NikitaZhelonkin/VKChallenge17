package com.vk.challenge;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.vk.challenge.adapter.BackgroundThumbAdapter;
import com.vk.challenge.adapter.GalleryAdapter;
import com.vk.challenge.data.model.BackgroundItem;
import com.vk.challenge.data.model.GalleryItem;
import com.vk.challenge.data.model.NewBackgroundItem;
import com.vk.challenge.data.provider.BackgroundItemsProvider;
import com.vk.challenge.data.model.FontStyle;
import com.vk.challenge.utils.AndroidUtils;
import com.vk.challenge.utils.KeyboardDetector;
import com.vk.challenge.widget.PostView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity implements
        BackgroundThumbAdapter.OnItemSelectedListener,
        GalleryAdapter.Callback,
        StickerDialogFragment.Callback,
        PostDialogFragment.Callback,
        KeyboardDetector.Listener,
        ImagePicker.Callback{

    private static final int PERM_REQ_CODE = 1;

    @BindView(R.id.root_layout)
    View mRootLayout;
    @BindView(R.id.post_view)
    PostView mPostView;
    @BindView(R.id.post_edit_text)
    EditText mEditText;
    @BindView(R.id.sendButton)
    Button mButton;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.gallery_cover)
    View mGalleryCover;

    private GalleryWindow mGalleryWindow;

    private KeyboardDetector mKeyboardDetector;

    private BackgroundThumbAdapter mThumbsAdapter;

    private ImagePicker mImagePicker;

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

        mKeyboardDetector = new KeyboardDetector(this);
        mKeyboardDetector.setKeyboardListener(this);

        mImagePicker = new ImagePicker(this);
        mImagePicker.setCallback(this);

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGalleryVisible()) {
                    hideGallery();
                }
            }
        });

        mGalleryWindow = new GalleryWindow(this);
        mGalleryWindow.setCallback(this);
        mGalleryWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mGalleryCover.setVisibility(LinearLayout.GONE);
            }
        });
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            mGalleryWindow.load();
        }

        mThumbsAdapter = new BackgroundThumbAdapter();
        mThumbsAdapter.setOnItemSelectedListener(this);
        mThumbsAdapter.setItems(BackgroundItemsProvider.getItems(this));
        mThumbsAdapter.selectItem(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mThumbsAdapter);
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!mImagePicker.onActivityResult(requestCode, resultCode, data)){
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showGallery();
            } else {
                mGalleryWindow.clearSelection();
                mThumbsAdapter.backSelection();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isGalleryVisible()) {
            hideGallery();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGalleryWindow != null) {
            mGalleryWindow.release();
        }
        super.onDestroy();
    }

    @Override
    public void onKeyboardDetected(boolean visible) {
        if (visible) {
            if (isGalleryVisible()) {
                hideGallery();
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    mKeyboardDetector.getKeyboardHeight());
            mGalleryCover.setLayoutParams(params);
        } else {
            hideGallery();
        }
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
        Bitmap bitmap = mPostView.createBitmap();
        try {
            final File file = new VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)).getTmpFile();
            if (file != null) {
                AndroidUtils.hideKeyboard(mEditText);
                mEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        post(file);
                    }
                }, 150);
            } else {
                showError("Unknown Error");
            }
        } finally {
            bitmap.recycle();
        }
    }

    @Override
    public void onStickerSelected(String sticker) {
        mPostView.addSticker(sticker);
    }

    @Override
    public void onImagePicked(Uri uri, boolean fromCamera) {
        mPostView.setImage(uri);
    }

    @Override
    public void onImagePickError(String errorMessage, boolean fromCamera) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(BackgroundItem item) {
        if (item instanceof NewBackgroundItem) {
            showGallery();
        } else {
            hideGallery();
            mPostView.setImage(item.getDrawable());
            mPostView.setFontStyle(item.getFontStyle());
            mGalleryWindow.clearSelection();
        }
        boolean white = item.getDrawable() instanceof ColorDrawable &&
                ((ColorDrawable) item.getDrawable()).getColor() == Color.WHITE;
        mPostView.setTrashWithBorder(white);
    }

    @Override
    public void onGalleryItemSelected(GalleryItem galleryItem) {
        mPostView.setImage(galleryItem.getUri());
    }

    @Override
    public void onTakePhotoClick() {
        AndroidUtils.hideKeyboard(mEditText);
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImagePicker.takePhoto();
            }
        }, 150);

    }

    @Override
    public void onOpenExternalGalleryClick() {
        AndroidUtils.hideKeyboard(mEditText);
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImagePicker.pickFromGallery();
            }
        }, 150);

    }

    @Override
    public void onCreateMoreClick() {
        Toast.makeText(this, "OnMore", Toast.LENGTH_SHORT).show();
        //TODO reset all
    }

    private void showGallery() {
        if (!requestMediaPermission()) {
            return;
        }
        mGalleryWindow.load();
        mGalleryWindow.setHeight((mKeyboardDetector.getKeyboardHeight()));
        if (mKeyboardDetector.isKeyboardVisible()) {
            mGalleryCover.setVisibility(LinearLayout.GONE);
        } else {
            mGalleryCover.setVisibility(LinearLayout.VISIBLE);
        }
        mGalleryWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);

    }

    private void hideGallery() {
        mGalleryWindow.dismiss();
    }

    private boolean isGalleryVisible() {
        return mGalleryWindow.isShowing();
    }

    private boolean requestMediaPermission() {
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (shouldShowPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.permission_rationale)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERM_REQ_CODE);
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mThumbsAdapter.backSelection();
                                mGalleryWindow.clearSelection();
                            }
                        })
                        .create()
                        .show();
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERM_REQ_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean shouldShowPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    private void post(File file){
        PostDialogFragment.create(file).show(getSupportFragmentManager(), "post");
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
