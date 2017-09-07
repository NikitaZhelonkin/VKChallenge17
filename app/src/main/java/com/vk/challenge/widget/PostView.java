package com.vk.challenge.widget;

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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vk.challenge.AndroidUtils;
import com.vk.challenge.R;
import com.vk.challenge.data.model.FontStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nikita on 06.09.17.
 */

public class PostView extends FrameLayout {

    private FontBackgroundEditText mEditText;

    private FontStyle mFontStyle;

    private List<StickerView> mStickers = new ArrayList<>();

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

    }

    public void setFontStyle(FontStyle fontStyle) {
        mEditText.setFontBackgroundColor(fontStyle.getFontBackgroundColor());
        mEditText.setTextColor(fontStyle.getTextColor());
        int shadowRadius = fontStyle.isShadow() ? 2 : 0;
        int shadowDy = fontStyle.isShadow() ? 2 : 0;
        int shadowColor = fontStyle.isShadow() ? Color.parseColor("#1e000000") : 0;
        mEditText.setShadowLayer(shadowRadius, 0, shadowDy, shadowColor);
        mFontStyle  = fontStyle;
    }

    public void addSticker(final String sticker) {
        final StickerView stickerView = createStickerView(sticker);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        addView(stickerView, params);
        bringChildToFront(mEditText);
        mStickers.add(stickerView);

        AndroidUtils.onPreDraw(stickerView, new AndroidUtils.OnPreDraw() {
            @Override
            public void onPreDraw() {
                Rect rect = new Rect();
                rect.right = getWidth() - stickerView.getMeasuredWidth() / 2;
                rect.bottom = stickerView.getMeasuredHeight()/2;
                stickerView.setPosition(randomPosition(rect));
            }
        });
    }

    private StickerView createStickerView(String sticker) {
        StickerView stickerView = new StickerView(getContext());
        stickerView.setTag(mStickers.size());
        Picasso.with(getContext()).load(sticker).into(stickerView);
        return stickerView;
    }

    private Point randomPosition(Rect rect) {
        Random random = new Random();
        Point point = new Point();
        point.x = random.nextInt(rect.width());
        point.y = random.nextInt(rect.height());
        return point;
    }

    public FontStyle getFontStyle() {
        return mFontStyle;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (FontBackgroundEditText) findViewById(R.id.post_edit_text);
    }

    public Bitmap createBitmap() {
        setEditTextFocusable(false);
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw(c);
        setEditTextFocusable(true);
        return b;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredHeight() > getMeasuredWidth()) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }
    }

    private void setEditTextFocusable(boolean focusable) {
        mEditText.setFocusable(focusable);
        mEditText.setFocusableInTouchMode(focusable);
    }
}
