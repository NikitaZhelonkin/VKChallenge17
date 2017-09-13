package com.vk.challenge.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
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
import com.vk.challenge.utils.KeyboardDetector;
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

    public static final int MODE_POST = 0;
    public static final int MODE_HISTORY = 1;

    private int mPostHeight;

    private ImageView mImageView;
    private FontBackgroundEditText mEditText;
    private ImageView mTrashView;

    private List<StickerView> mStickers = new ArrayList<>();

    private float mLastScale;
    private float mLastAngle;

    private ScaleGestureDetector mScaleGestureDetector;
    private RotationGestureDetector mRotationGestureDetector;

    private View mTargetSticker;

    private View mStickerToDelete;

    private boolean mAnimatingTrashHide;

    private int mMode;

    private int mPostTopInset;
    private int mPostBottomInset;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mRotationGestureDetector = new RotationGestureDetector(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PostView);
        mPostTopInset = a.getDimensionPixelSize(R.styleable.PostView_postTopInset, 0);
        mPostBottomInset = a.getDimensionPixelSize(R.styleable.PostView_postBottomInset, 0);
        a.recycle();

        setMode(MODE_POST);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImageView = (ImageView) findViewById(R.id.post_image_view);
        mEditText = (FontBackgroundEditText) findViewById(R.id.post_edit_text);
        mTrashView = (ImageView) findViewById(R.id.trash_fab);
    }

    public void setFontStyle(FontStyle fontStyle) {
        mEditText.setFontBackgroundColor(fontStyle.getFontBackgroundColor());
        mEditText.setTextColor(fontStyle.getTextColor());
        int shadowRadius = fontStyle.isShadow() ? 2 : 0;
        int shadowDy = fontStyle.isShadow() ? 2 : 0;
        int shadowColor = fontStyle.isShadow() ? Color.parseColor("#1e000000") : 0;
        mEditText.setShadowLayer(shadowRadius, 0, shadowDy, shadowColor);
    }

    public void setMode(int mode) {
        mMode = mode;
        if (mMode == MODE_POST) {
            int insetDiff = Math.abs(mPostBottomInset - mPostTopInset);
            setPadding(0, mPostTopInset > mPostBottomInset ? insetDiff : 0, 0,
                    mPostBottomInset > mPostTopInset ? insetDiff : 0);
        } else {
            setPadding(0, 0, 0, 0);
        }
        requestLayout();
    }

    public Bitmap createBitmap() {
        setEditTextFocusable(false);
        Bitmap b = createBitmapInternal(1080);
        setEditTextFocusable(true);
        return b;
    }

    private Bitmap createBitmapInternal(int width) {
        int imageWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int imageHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float ratio = imageWidth / (float) imageHeight;
        int height = (int) (width / ratio);
        float scaleX = width / (float) imageWidth;
        float scaleY = height / (float) imageHeight;
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.clipRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + imageWidth, getPaddingTop() + imageHeight);
        c.scale(scaleX, scaleY);
        draw(c);
        return b;
    }

    public void setTrashWithBorder(boolean border) {
        if (border) {
            mTrashView.setBackgroundResource(R.drawable.bg_trash_with_border);
        } else {
            mTrashView.setBackgroundResource(R.drawable.bg_trash);
        }
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
        AndroidUtils.onPreDraw(stickerView, new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                rect.top = mPostTopInset;
                rect.right = getWidth();
                rect.bottom = getHeight() - mPostBottomInset;
                Point point = randomPosition(rect);
                stickerView.setX(point.x);
                stickerView.setY(point.y);

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
        if (mMode == MODE_POST) {
            if (getMeasuredHeight() != 0) {
                mPostHeight = Math.min(calculatePostHeight(), getMeasuredWidth());
            }
            if (mPostHeight != 0) {
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mPostHeight, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw != 0 && oldh != 0) {
            for (final StickerView stickerView : getStickers()) {
                float endX = stickerView.getX() / oldw * (float) w;
                float endY = stickerView.getY() / oldh * (float) h;
                stickerView.setX(endX);
                stickerView.setY(endY);
            }
        }
    }

    private int calculatePostHeight() {
        Rect rect = new Rect();
        int insetDiff = Math.abs(mPostBottomInset - mPostTopInset);
        getRootView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        return AndroidUtils.getDisplaySize((Activity) getContext()).y -
                statusBarHeight - KeyboardDetector.getKeyboardHeight() - mPostBottomInset - mPostTopInset + insetDiff;
    }

    @Override
    public void onStartMove(StickerView stickerView) {
        bringStickerToFront(stickerView);
        mHandler.postDelayed(mShowTrashRunnable, 300);
    }

    @Override
    public void onMove(StickerView stickerView) {
        int deleteDistance = AndroidUtils.dpToPx(getContext(), 48);

        Point stickerPoint = new Point((int) stickerView.getX(), (int) stickerView.getY());
        Point trashPoint = getChildMidPoint(mTrashView);

        int distance = MathUtils.distanceBetween(stickerPoint, trashPoint);

        if (distance <= deleteDistance && isTrashVisible()) {
            setTrashActive(true);
            setStickerToDelete(stickerView, distance / (float) deleteDistance);
        }  else {
            setTrashActive(false);
            setStickerToDelete(null, 0);
        }
    }

    protected List<StickerView> getStickers() {
        return mStickers;
    }


    private void setStickerToDelete(View stickerToDelete, float distanceFraction) {
        if (stickerToDelete != null) {
            if (mStickerToDelete == null) {
                float scale = stickerToDelete.getScaleX();
                mStickerToDelete = stickerToDelete;
                mStickerToDelete.setTag(scale);
            }
            float scale = (Float) stickerToDelete.getTag();
            stickerToDelete.setAlpha(distanceFraction);
            stickerToDelete.setScaleX(scale * distanceFraction);
            stickerToDelete.setScaleY(scale * distanceFraction);

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
        mHandler.removeCallbacks(mShowTrashRunnable);
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

    public void setImage(Drawable drawable) {
        Picasso.with(getContext()).cancelRequest(mImageView);
        mImageView.setImageDrawable(drawable);
    }

    public void setImage(Uri uri) {
        Picasso.with(getContext())
                .load(uri)
                .transform(new ResizeTransformation(1024))
                .into(mImageView);
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

    private Runnable mShowTrashRunnable =new Runnable() {
        @Override
        public void run() {
            showTrash();
            setTrashActive(false);
        }
    };

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
                .translationY(mMode == MODE_POST ? 0 : -mPostBottomInset)
                .setListener(null)
                .setDuration(300)
                .start();
    }

    private boolean isTrashVisible(){
        return mTrashView.getVisibility() == View.VISIBLE;
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
                .setStartDelay(200)
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
