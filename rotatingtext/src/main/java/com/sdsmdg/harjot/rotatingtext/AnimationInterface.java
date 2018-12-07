package com.sdsmdg.harjot.rotatingtext;

class AnimationInterface {
    //class to check the current animation status
    private boolean animationRunning;

    private AnimationListener AnimationListener;

    AnimationInterface(boolean initialValue) {
        animationRunning = initialValue;
    }

    void setAnimationListener(AnimationListener AnimationListener) {
        this.AnimationListener = AnimationListener;
    }

    boolean isAnimationRunning() {
        return animationRunning;
    }

    void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
        if (AnimationListener != null) {
            AnimationListener.onAnimationValueChanged(animationRunning);
        }
    }

    public interface AnimationListener {
        //interface to report the current animation status
        void onAnimationValueChanged(boolean newValue);
    }
}