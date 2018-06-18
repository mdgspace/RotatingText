package com.sdsmdg.harjot.rotatingtext;

public class AnimationInterface {

    private boolean animationRunning;

    private AnimationListener AnimationListener;

    /**
     * Construct a the boolean store.
     *
     * @param initialValue The initial value.
     */
    public AnimationInterface(boolean initialValue) {
        animationRunning = initialValue;

    }

    /**
     * Sets a listener on the store. The listener will be modified when the
     * value changes.
     */

    public void setAnimationListener(AnimationListener AnimationListener) {
        this.AnimationListener = AnimationListener;
    }

    /**
     * Set a new boolean value.
     *
     * @param newValue The new value.
     */
    public void setAnimationRunningValue(boolean newValue) {
        animationRunning = newValue;
        if (AnimationListener != null) {
            AnimationListener.onAnimationValueChanged(animationRunning);
        }
    }

    /**
     * Get the current value.
     *
     * @return The current boolean value.
     */
    public boolean getAnimationRunningValue() {
        return animationRunning;
    }

    public interface AnimationListener {
        /**
         * Called when the value of the animationRunning changes.
         *
         * @param newValue The new value.
         */
        void onAnimationValueChanged(boolean newValue);
    }
}