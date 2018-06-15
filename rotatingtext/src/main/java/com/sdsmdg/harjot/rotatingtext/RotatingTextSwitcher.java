package com.sdsmdg.harjot.rotatingtext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Harjot on 01-May-17.
 */

public class RotatingTextSwitcher extends TextView {

    private Context context;
    private Rotatable rotatable;

    private Paint paint;

    private float density;

    private boolean isRotatableSet = false;

    private Path pathIn, pathOut;

    private Timer updateWordTimer;

    private Disposable disposable;

    private String currentText = "";
    private String oldText = "";

    boolean animationRunning=false;

    private boolean isPaused = false;

    public RotatingTextSwitcher(Context context) {
        super(context);
        this.context = context;
    }

    public void setRotatable(Rotatable rotatable) {
        this.rotatable = rotatable;
        isRotatableSet = true;
        init();
    }

    private void init() {
        paint = getPaint();
        density = getContext().getResources().getDisplayMetrics().density;
        paint.setAntiAlias(true);
        paint.setTextSize(rotatable.getSize() * density);
        paint.setColor(rotatable.getColor());

        if (rotatable.isCenter()) {
            //always false
            paint.setTextAlign(Paint.Align.CENTER);
        }

        if (rotatable.getTypeface() != null) {
            paint.setTypeface(rotatable.getTypeface());
        }

        setText(rotatable.getLargestWordWithSpace());
        currentText = rotatable.getNextWord();
        oldText = currentText;

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

        if (disposable == null) {
            // knocks every ~16 milisec
            disposable = Observable.interval(1000 / rotatable.getFPS(), TimeUnit.MILLISECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            invalidate();
                            //calls on draw
                        }
                    });
        }

        invalidate();

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
                            animationRunning=true;
                            resumeRender();
                            animateInHorizontal();
                            animateOutHorizontal();
                            oldText = currentText;
                            currentText = rotatable.getNextWord();
                        }
                    }
                });
            }
        }, rotatable.getUpdateDuration(), rotatable.getUpdateDuration());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("point", "onDraw");
        if (isRotatableSet) {
            if (rotatable.isUpdated()) {
                updatePaint();
                rotatable.setUpdated(false);
            }
            String text = currentText;
            if (rotatable.getPathIn() != null) {
                canvas.drawTextOnPath(text, rotatable.getPathIn(), 0.0f, 0.0f, paint);
            }
            if (rotatable.getPathOut() != null) {

                canvas.drawTextOnPath(oldText, rotatable.getPathOut(), 0.0f, 0.0f, paint);
            }
        }
    }

    private void animateInHorizontal() {
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
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                animationRunning=false;
            }
        });
        animator.setInterpolator(rotatable.getInterpolator());
        animator.setDuration(rotatable.getAnimationDuration());
        animator.start();
    }

    private void animateOutHorizontal() {
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

    private void animateInCurve() {
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

    private void animateOutCurve() {
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

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    private void pauseRender() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void resumeRender() {
        if (disposable == null) {
            disposable = Observable.interval(1000 / rotatable.getFPS(), TimeUnit.MILLISECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            invalidate();
                        }
                    });
        }
    }

    private void updatePaint() {
        paint.setTextSize(rotatable.getSize() * density);
        paint.setColor(rotatable.getColor());

        if (rotatable.isCenter()) {
            paint.setTextAlign(Paint.Align.CENTER);
        }

        if (rotatable.getTypeface() != null) {
            paint.setTypeface(rotatable.getTypeface());
        }

        if (updateWordTimer != null) {
            updateWordTimer.cancel();
        }
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
                            oldText = currentText;
                            currentText = rotatable.getNextWord();
                            animationRunning=true;
                            resumeRender();
                            animateInHorizontal();
                            animateOutHorizontal();

                        }
                    }
                });
            }
        }, rotatable.getUpdateDuration(), rotatable.getUpdateDuration());

    }

    public boolean isPaused() {
        return isPaused;
    }

}
