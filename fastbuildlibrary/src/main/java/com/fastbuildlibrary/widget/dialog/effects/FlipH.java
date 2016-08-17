package com.fastbuildlibrary.widget.dialog.effects;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
public class FlipH  extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", -90, 0).setDuration(mDuration)

        );
    }
}
