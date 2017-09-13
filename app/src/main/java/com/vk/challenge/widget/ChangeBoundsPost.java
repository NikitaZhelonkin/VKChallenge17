package com.vk.challenge.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikita on 13.09.17.
 */

public class ChangeBoundsPost extends ChangeBounds {

    private static final String PROPNAME_STICKERS = "challenge:changeBounds:stickers";

    private PostView mPostView;

    public ChangeBoundsPost(PostView postView) {
        mPostView = postView;
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        if (transitionValues.view == mPostView) {
            HashMap<View, PointF> values = new HashMap<>();
            for (StickerView stickerView : mPostView.getStickers()) {
                values.put(stickerView, new PointF(stickerView.getX(), stickerView.getY()));
            }
            transitionValues.values.put(PROPNAME_STICKERS, values);
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        if (transitionValues.view == mPostView) {
            HashMap<View, PointF> values = new HashMap<>();
            for (StickerView stickerView : mPostView.getStickers()) {
                values.put(stickerView, new PointF(stickerView.getX(), stickerView.getY()));
            }
            transitionValues.values.put(PROPNAME_STICKERS, values);
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        Animator animator = super.createAnimator(sceneRoot, startValues, endValues);
        Animator stickersAnimator = createStickersAnimator(startValues, endValues);
        if (stickersAnimator == null) {
            return animator;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, stickersAnimator);
        return animatorSet;
    }

    @SuppressWarnings("unchecked")
    private Animator createStickersAnimator(TransitionValues startValues, TransitionValues endValues) {
        AnimatorSet animatorSet = null;
        if (mPostView == startValues.view && mPostView == endValues.view) {
            animatorSet = new AnimatorSet();
            List<StickerView> stickerViews = mPostView.getStickers();
            List<Animator> animatorList = new ArrayList<>(stickerViews.size());
            HashMap<View, PointF> startPoints = (HashMap<View, PointF>) startValues.values.get(PROPNAME_STICKERS);
            HashMap<View, PointF> endPoints = (HashMap<View, PointF>) endValues.values.get(PROPNAME_STICKERS);
            for (StickerView stickerView : stickerViews) {
                PointF startPoint = startPoints.get(stickerView);
                PointF endPoint = endPoints.get(stickerView);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", startPoint.x, endPoint.x);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", startPoint.y, endPoint.y);
                Animator animator = ObjectAnimator.ofPropertyValuesHolder(stickerView, pvhX, pvhY);
                animatorList.add(animator);
            }
            animatorSet.playTogether(animatorList);

            startValues.values.put(PROPNAME_STICKERS, null);
            endValues.values.put(PROPNAME_STICKERS, null);
        }
        return animatorSet;
    }

}
