package com.vk.challenge.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vk.challenge.utils.AndroidUtils;
import com.vk.challenge.utils.MathUtils;
import com.vk.challenge.R;
import com.vk.challenge.data.model.FontStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nikita on 06.09.17.
 */

public class PostView extends FrameLayout implements
        ScaleGestureDetector.OnScaleGestureListener,
        RotationGestureDetector.OnRotationGestureListener,
        StickerView.OnMoveListener {

    private int mMaxHeight;

    private FontBackgroundEditText mEditText;
    private ImageView mTrashView;

    private FontStyle mFontStyle;

    private List<StickerView> mStickers = new ArrayList<>();

    private float mLastScale;
    private float mLastAngle;

    private ScaleGestureDetector mScaleGestureDetector;
    private RotationGestureDetector mRotationGestureDetector;

    private View mTargetSticker;

    private View mStickerToDelete;

    private boolean mAnimatingTrashHide;


    public PostView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mRotationGestureDetector = new RotationGestureDetector(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (FontBackgroundEditText) findViewById(R.id.post_edit_text);
        mTrashView = (ImageView) findViewById(R.id.trash);
    }

    public void setFontStyle(FontStyle fontStyle) {
        mEditText.setFontBackgroundColor(fontStyle.getFontBackgroundColor());
        mEditText.setTextColor(fontStyle.getTextColor());
        int shadowRadius = fontStyle.isShadow() ? 2 : 0;
        int shadowDy = fontStyle.isShadow() ? 2 : 0;
        int shadowColor = fontStyle.isShadow() ? Color.parseColor("#1e000000") : 0;
        mEditText.setShadowLayer(shadowRadius, 0, shadowDy, shadowColor);
        mFontStyle = fontStyle;
    }

    public FontStyle getFontStyle() {
        return mFontStyle;
    }

    public Bitmap createBitmap() {
        setEditTextFocusable(false);
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw(c);
        setEditTextFocusable(true);
        return b;
    }

    public void addSticker(final String sticker) {
        final StickerView stickerView = createStickerView(sticker);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                AndroidUtils.dpToPx(getContext(), 100),
                AndroidUtils.dpToPx(getContext(), 100)
        );
        stickerView.setOnMoveListener(this);
        addView(stickerView, params);
        bringStickerToFront(stickerView);
        AndroidUtils.onPreDraw(stickerView, new AndroidUtils.OnPreDraw() {
            @Override
            public void onPreDraw() {
                Rect rect = new Rect();
                rect.right = getWidth() - stickerView.getMeasuredWidth() / 2;
                rect.bottom = stickerView.getMeasuredHeight() / 2;
                stickerView.setPosition(randomPosition(rect));

                stickerView.setScaleX(0f);
                stickerView.setScaleY(0f);
                stickerView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .rotation(randomRotation())
                        .setDuration(300)
                        .start();
            }
        });

        mStickers.add(stickerView);
    }

    private void removeSticker(final StickerView stickerView) {
        if (mStickers.contains(stickerView)) {
            mStickers.remove(stickerView);
            Point trashPoint = getChildMidPoint(mTrashView);
            stickerView.animate()
                    .alpha(0)
                    .scaleY(0)
                    .scaleX(0)
                    .x(trashPoint.x - stickerView.getWidth() / 2)
                    .y(trashPoint.y - stickerView.getHeight() / 2)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeView(stickerView);
                        }
                    })
                    .start();
        }
    }

    private StickerView createStickerView(String sticker) {
        StickerView stickerView = new StickerView(getContext());
        Picasso.with(getContext()).load(sticker).into(stickerView);
        return stickerView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        if (height != 0) {
            mMaxHeight = mMaxHeight == 0 ? height : Math.min(height, mMaxHeight);
        }
        if (mMaxHeight != 0) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public void onStartMove(StickerView stickerView) {
        bringStickerToFront(stickerView);
    }

    @Override
    public void onMove(StickerView stickerView) {
        int deleteDistance = AndroidUtils.dpToPx(getContext(), 48);
        int showTrashDistance = AndroidUtils.dpToPx(getContext(), 96);

        Point stickerPoint = getChildMidPoint(stickerView);
        Point trashPoint = getChildMidPoint(mTrashView);

        int distance = MathUtils.distanceBetween(stickerPoint, trashPoint);

        if (distance <= deleteDistance) {
            showTrash();
            setTrashActive(true);
            setStickerToDelete(stickerView);
            float scale = (Float) stickerView.getTag();
            stickerView.setAlpha(distance / (float) deleteDistance);
            stickerView.setScaleX(scale * distance / (float) deleteDistance);
            stickerView.setScaleY(scale * distance / (float) deleteDistance);

        } else if (distance < showTrashDistance) {
            showTrash();
            setTrashActive(false);
            setStickerToDelete(null);
        } else {
            hideTrash();
            setStickerToDelete(null);
        }
    }

    private void setStickerToDelete(View stickerToDelete) {
        if (stickerToDelete != null) {
            if (mStickerToDelete == null) {
                mStickerToDelete = stickerToDelete;
                mStickerToDelete.setTag(stickerToDelete.getScaleX());
            }
        } else {
            if (mStickerToDelete != null) {
                float scale = (Float) mStickerToDelete.getTag();
                mStickerToDelete.animate()
                        .setListener(null)
                        .alpha(1)
                        .scaleX(scale)
                        .scaleY(scale)
                        .setDuration(100)
                        .start();
            }
            mStickerToDelete = null;
        }
    }

    @Override
    public void onMoveEnd(StickerView stickerView) {
        if (mStickerToDelete == stickerView) {
            removeSticker(stickerView);
            mStickerToDelete = null;
        }
        hideTrash();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return ev.getPointerCount() >= 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mStickers.size() == 0) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 2) {
                    mTargetSticker = findTargetSticker(event);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTargetSticker = null;
                break;
        }
        mScaleGestureDetector.onTouchEvent(event);
        mRotationGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mTargetSticker == null) {
            return false;
        }
        float sf = detector.getScaleFactor();
        float newScale = sf / mLastScale;
        mTargetSticker.setScaleX(mTargetSticker.getScaleX() * newScale);
        mTargetSticker.setScaleY(mTargetSticker.getScaleY() * newScale);

        mLastScale = sf;
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mLastScale = 1.0f;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        //do nothing
    }

    @Override
    public void onRotation(RotationGestureDetector rotationDetector) {
        if (mTargetSticker == null) {
            return;
        }
        float angle = rotationDetector.getAngle();
        float delta = mLastAngle - angle;
        mTargetSticker.setRotation(mTargetSticker.getRotation() + delta);
        mLastAngle = angle;
    }

    @Override
    public void onRotationBegin(RotationGestureDetector rotationDetector) {
        mLastAngle = rotationDetector.getStartAngle();

    }

    @Override
    public void onRotationEnd(RotationGestureDetector rotationDetector) {
        //do nothing
    }

    private View findTargetSticker(MotionEvent event) {
        Point midPoint = MathUtils.midPoint(
                event.getX(0),
                event.getY(0),
                event.getX(1),
                event.getY(1));
        Rect rect = new Rect();
        for (View sticker : mStickers) {
            sticker.getHitRect(rect);
            if (rect.contains(midPoint.x, midPoint.y)) {
                return sticker;
            }
        }
        return mStickers.size() != 0 ? mStickers.get(mStickers.size() - 1) : null;
    }

    private void bringStickerToFront(View v) {
        bringChildToFront(v);
        bringChildToFront(mEditText);
    }

    private void setEditTextFocusable(boolean focusable) {
        mEditText.setFocusable(focusable);
        mEditText.setFocusableInTouchMode(focusable);
    }

    private Point randomPosition(Rect rect) {
        Random random = new Random();
        Point point = new Point();
        point.x = random.nextInt(rect.width());
        point.y = random.nextInt(rect.height());
        return point;
    }

    private int randomRotation() {
        return new Random().nextInt(90) - 45;
    }

    private void showTrash() {
        if (mTrashView.getVisibility() == View.VISIBLE) {
            return;
        }
        mTrashView.setVisibility(View.VISIBLE);
        mTrashView.setAlpha(0f);
        mTrashView.setTranslationY(mTrashView.getHeight() / 2);
        mTrashView.animate().cancel();
        mTrashView.animate()
                .alpha(1)
                .translationY(0)
                .setListener(null)
                .setDuration(300)
                .start();
    }

    private void hideTrash() {
        if (mTrashView.getVisibility() == View.INVISIBLE || mAnimatingTrashHide) {
            return;
        }
        mAnimatingTrashHide = true;
        mTrashView.setVisibility(View.VISIBLE);
        mTrashView.animate().cancel();
        mTrashView.animate()
                .alpha(0)
                .translationY(mTrashView.getHeight() / 2)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mTrashView.setVisibility(View.INVISIBLE);
                        mAnimatingTrashHide = false;
                    }
                })
                .setDuration(300)
                .start();

    }

    private Point getChildMidPoint(View child) {
        int x = (int) (child.getX() + child.getWidth() / 2);
        int y = (int) (child.getY() + child.getHeight() / 2);
        return new Point(x, y);
    }

    private void setTrashActive(boolean active) {
        mTrashView.setScaleX(active ? 1.1f : 1.0f);
        mTrashView.setScaleY(active ? 1.1f : 1.0f);
        mTrashView.setSelected(active);
    }
}
