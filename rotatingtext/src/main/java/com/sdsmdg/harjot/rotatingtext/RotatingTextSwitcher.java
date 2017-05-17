package com.sdsmdg.harjot.rotatingtext;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harjot on 01-May-17.
 */

public class RotatingTextSwitcher extends TextView {

    Context context;
    Rotatable rotatable;

    Paint paint;

    boolean isRotatableSet = false;

    Path pathIn, pathOut;

    Timer updateWordTimer, renderTimer;

    String currentText = "";

    boolean isPaused = false;

    public RotatingTextSwitcher(Context context) {
        super(context);
        this.context = context;
    }

    public void setRotatable(Rotatable rotatable) {
        this.rotatable = rotatable;
        isRotatableSet = true;
        init();
    }

    void init() {
        paint = getPaint();
        float density = getContext().getResources().getDisplayMetrics().density;
        paint.setTextSize(rotatable.getSize() * density);
        paint.setAntiAlias(true);
        paint.setColor(rotatable.getColor());

        if (rotatable.getTypeface() != null) {
            paint.setTypeface(rotatable.getTypeface());
        }

        setText(rotatable.getLargestWord());
        currentText = rotatable.getNextWord();

        post(new Runnable() {
            @Override
            public void run() {
                pathIn = new Path();
                pathIn.moveTo(0.0f, getHeight() - paint.getFontMetrics().bottom);
                pathIn.lineTo(getWidth(), getHeight() - paint.getFontMetrics().bottom);

                rotatable.setPathIn(pathIn);

                pathOut = new Path();
                pathOut.moveTo(0.0f, (2 * getHeight()) - paint.getFontMetrics().bottom);
                pathOut.lineTo(getWidth(), (2 * getHeight()) - paint.getFontMetrics().bottom);

                rotatable.setPathOut(pathOut);
            }
        });

        renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }, 0, 1000 / 60);

        updateWordTimer = new Timer();
        updateWordTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isPaused) {
                            pauseRender();
                        } else {
                            resumeRender();
                            animateInHorizontal();
                            animateOutHorizontal();
                            currentText = rotatable.getNextWord();
                        }
                    }
                });
            }
        }, rotatable.getUpdateDuration(), rotatable.getUpdateDuration());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isRotatableSet) {
            String text = currentText;
            if (rotatable.getPathIn() != null)
                canvas.drawTextOnPath(text, rotatable.getPathIn(), 0.0f, 0.0f, paint);
            if (rotatable.getPathOut() != null)
                canvas.drawTextOnPath(rotatable.getPreviousWord(), rotatable.getPathOut(), 0.0f, 0.0f, paint);
        }
    }

    void animateInHorizontal() {
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pathIn = new Path();
                pathIn.moveTo(0.0f, (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                pathIn.lineTo(getWidth(), (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                rotatable.setPathIn(pathIn);
            }
        });
        animator.setInterpolator(rotatable.getInterpolator());
        animator.setDuration(rotatable.getAnimationDuration());
        animator.start();
    }

    void animateOutHorizontal() {
        ValueAnimator animator = ValueAnimator.ofFloat(getHeight(), getHeight() * 2.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pathOut = new Path();
                pathOut.moveTo(0.0f, (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                pathOut.lineTo(getWidth(), (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                rotatable.setPathOut(pathOut);
            }
        });
        animator.setInterpolator(rotatable.getInterpolator());
        animator.setDuration(rotatable.getAnimationDuration());
        animator.start();
    }

    void animateInCurve() {
        final int stringLength = rotatable.peekNextWord().length();
//        long perCharacterAnimDuration = rotatable.getAnimationDuration() / stringLength;

        final float[] yValues = new float[stringLength];
        for (int i = 0; i < stringLength; i++) {
            yValues[i] = 0.0f;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                yValues[0] = (float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom;

                for (int i = 1; i < stringLength; i++) {
                    if (valueAnimator.getAnimatedFraction() > (float) i / (float) (stringLength)) {
                        yValues[i] = (valueAnimator.getAnimatedFraction() - (float) i / (float) (stringLength)) * yValues[0];
                    }
                }

                pathIn = new Path();

                pathIn.moveTo(0.0f, yValues[0]);
                for (int i = 1; i < stringLength; i++) {
                    pathIn.lineTo((getWidth() * ((float) i / (float) stringLength)), yValues[i]);
                    pathIn.moveTo((getWidth() * ((float) i / (float) stringLength)), yValues[i]);
                }
                rotatable.setPathIn(pathIn);
            }
        });
        animator.setInterpolator(rotatable.getInterpolator());
        animator.setDuration(rotatable.getAnimationDuration());
        animator.start();

    }

    void animateOutCurve() {
        final int stringLength = getText().length();
//        long perCharacterAnimDuration = rotatable.getAnimationDuration() / stringLength;

        final float[] yValues = new float[stringLength];
        for (int i = 0; i < stringLength; i++) {
            yValues[i] = getHeight();
        }

        ValueAnimator animator = ValueAnimator.ofFloat(getHeight(), 2 * getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                yValues[0] = (float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom;

                for (int i = 1; i < stringLength; i++) {
                    if (valueAnimator.getAnimatedFraction() > (float) i / (float) (stringLength)) {
                        yValues[i] = (valueAnimator.getAnimatedFraction() - (float) i / (float) (stringLength)) * yValues[0];
                    }
                }

                pathIn = new Path();

                pathIn.moveTo(0.0f, yValues[0]);
                for (int i = 1; i < stringLength; i++) {
                    pathIn.lineTo((getWidth() * ((float) i / (float) stringLength)), yValues[i]);
                    pathIn.moveTo((getWidth() * ((float) i / (float) stringLength)), yValues[i]);
                }
                rotatable.setPathIn(pathIn);
            }
        });
        animator.setInterpolator(rotatable.getInterpolator());
        animator.setDuration(rotatable.getAnimationDuration());
        animator.start();
    }

    void pause() {
        isPaused = true;
    }

    void resume() {
        isPaused = false;
    }

    void pauseRender() {
        renderTimer.cancel();
    }

    void resumeRender() {
        renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }, 0, 1000 / 60);
    }

}
