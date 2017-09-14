package com.vk.challenge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.challenge.utils.AndroidUtils;
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

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikita on 10.09.17.
 */

public class PostDialogFragment extends DialogFragment {

    public interface Callback {
        void onCreateMoreClick();
    }

    @BindView(R.id.cancel_btn)
    Button mCancelButton;
    @BindView(R.id.create_btn)
    Button mCreateButton;
    @BindView(R.id.loader_success_image)
    ImageView mSuccessImage;
    @BindView(R.id.progressBar)
    View mProgressBar;
    @BindView(R.id.posting_text)
    TextView mPostingText;

    private static final String EXTRA_FILE = "extra_file";

    private VKRequest mRequest;

    private Callback mCallback;

    public static PostDialogFragment create(File file) {
        PostDialogFragment fragment = new PostDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FILE, file.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Post);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            String path = getArguments().getString(EXTRA_FILE);
            if (path != null) {
                loadPhotoToMyWall(new File(path));
                showCancelView();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @OnClick(R.id.cancel_btn)
    public void onCancelClick(View v) {
        if (mRequest != null) {
            mRequest.cancel();
        }
        dismiss();
    }

    @OnClick(R.id.create_btn)
    public void onCreateMoreClick(View v) {
        if (mCallback != null) {
            mCallback.onCreateMoreClick();
        }
        dismiss();
    }

    private void showCancelView() {
        mCancelButton.setAlpha(0);
        mCancelButton.setTranslationY(AndroidUtils.dpToPx(getContext(), 48));
        mCancelButton.animate().alpha(1)
                .translationY(0)
                .setStartDelay(250)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .start();
    }

    private void makePost(VKAttachments att, String msg, final int ownerId) {
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.OWNER_ID, String.valueOf(ownerId));
        parameters.put(VKApiConst.ATTACHMENTS, att);
        parameters.put(VKApiConst.MESSAGE, msg);
        mRequest = VKApi.wall().post(parameters);
        mRequest.setModelClass(VKWallPostResult.class);
        mRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                fadeOut(mProgressBar);
                fadeIn(mCreateButton);
                fadeIn(mSuccessImage);
                mPostingText.setText(R.string.posting_success);
            }

            @Override
            public void onError(VKError error) {
                dismiss();
                if (error.errorCode != VKError.VK_CANCELED) showError(error.toString());
            }
        });
    }

    private void loadPhotoToMyWall(final File image) {
        mRequest = VKApi.uploadWallPhotoRequest(image, getMyId(), 0);
        mRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel), null, getMyId());
            }

            @Override
            public void onError(VKError error) {
                dismiss();
                if (error.errorCode != VKError.VK_CANCELED) showError(error.toString());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }

    public static void fadeIn(View view) {
        view.animate().setListener(null).cancel();
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1).setDuration(300).start();
    }

    public static void fadeOut(final View view) {
        view.animate().setListener(null).cancel();
        view.setAlpha(1);
        view.animate().alpha(0).setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                }).start();
    }

}
