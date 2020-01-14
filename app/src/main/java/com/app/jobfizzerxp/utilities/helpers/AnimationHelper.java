package com.app.jobfizzerxp.utilities.helpers;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.jobfizzerxp.R;

public class AnimationHelper {

    public static void expand(final View v, ImageView changeThemIcon) {
        Context context = changeThemIcon.getContext();
        changeThemIcon.startAnimation(rotate(context, true));
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / context.getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static Animation rotate(Context activity, boolean up) {
        Animation anim = android.view.animation.AnimationUtils.loadAnimation(activity,
                up ? R.anim.rotate_up : R.anim.rotate_down);
        anim.setInterpolator(new LinearInterpolator()); // for smooth animation
        return anim;
    }

    public static void collapse(final View v, ImageView changeThemIcon) {
        Context context = changeThemIcon.getContext();
        changeThemIcon.startAnimation(rotate(context, false));
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / context.getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void animateUp(LinearLayout commentSection, Context context,
                                 final LinearLayout topLayout) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topLayout.getLayoutParams();
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slidefrombottomm);
        commentSection.setVisibility(View.VISIBLE);
        commentSection.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slidefrombottom));

        topLayout.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topLayout.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void buildAndStartAnimation(final View view) {
        ViewPropertyAnimator animator = view.animate();

        if (view.getAlpha() == 0f) {
            float alphaValue = view.getAlpha() == 0f ? 1f : 0f;
            animator.alpha(alphaValue);
        }

        float scalingValue = view.getScaleY() == 0f ? 1f : 0f;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float translationZValue = view.getTranslationZ() != 25f ? 25f : 2f;
            animator.translationZ(translationZValue);
        }

        animator.scaleX(scalingValue).scaleY(scalingValue);
        animator.setDuration(500);
        animator.setInterpolator(null);
        animator.start();
    }

}