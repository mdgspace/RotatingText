package com.sdsmdg.harjot.rotatingtext;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
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

    Context context;
    Rotatable rotatable;

    Paint paint;

    float density;

    static boolean isRotatableSet = false;

    Path pathIn, pathOut;

    static Timer updateWordTimer;

    private Disposable disposable;

    String currentText = "";

    static boolean isPaused = false;

    static ValueAnimator animatorin;

    public RotatingTextSwitcher(Context context) {
        super(context);
        Log.i("point rts42", "reached");
        this.context = context;
    }

    public void setRotatable(Rotatable rotatable) {
        Log.i("point rts47", "reached");
        this.rotatable = rotatable;
        isRotatableSet = true;
        init();
    }

    void init() {
        Log.i("point rts54", "reached");
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
            Log.i("point rts67", "reached");
            paint.setTypeface(rotatable.getTypeface());
        }

        setText(rotatable.getLargestWord());
//        setText("ritik");
        currentText = rotatable.getNextWord();

        post(new Runnable() {
            @Override
            public void run() {
                pathIn = new Path();
                Log.i("point rts871", getHeight() + "");
                Log.i("point rts872", getHeight() - paint.getFontMetrics().bottom + "");
                Log.i("point rts873", getWidth() + "");

                pathIn.moveTo(0.0f, getHeight() - paint.getFontMetrics().bottom);
                pathIn.lineTo(getWidth(), getHeight() - paint.getFontMetrics().bottom);
                Log.i("point rts91", "reached");

                rotatable.setPathIn(pathIn);

                pathOut = new Path();
                pathOut.moveTo(0.0f, (2 * getHeight()) - paint.getFontMetrics().bottom);
                pathOut.lineTo(getWidth(), (2 * getHeight()) - paint.getFontMetrics().bottom);
                Log.i("point rts98", "reached");

                rotatable.setPathOut(pathOut);


            }
        });

        if (disposable == null) {
            Log.i("point rts109", "reached");
            // knocks every ~16 milisec
            disposable = Observable.interval(1000 / rotatable.getFPS(), TimeUnit.MILLISECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.d("OBSER", aLong + "");
                            Log.i("point 110 knock", aLong + "");
                            invalidate();
                            //calls on draw
                        }
                    });
        }

        invalidate();
        Log.i("point rts1111", "reached");

        updateWordTimer = new Timer();
        updateWordTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("point rts112", "reached");
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("point rts116", "reached");
                        if (isPaused) {
                            Log.i("point rts118", "reached");
                            pauseRender();
                        } else {
                            Log.i("point rts121", "reached");
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
        Log.i("point rts135", "onDraw");
        if (isRotatableSet) {
            Log.i("point rts137", "rotatableSet");
            if (rotatable.isUpdated()) {
                Log.i("point rts139", "reached");
                updatePaint();
                rotatable.setUpdated(false);
            }
            Log.i("point rts142", "reached");
            String text = currentText;
            if (rotatable.getPathIn() != null) {
                Log.i("point rts146", "reached");
                canvas.drawTextOnPath(text, rotatable.getPathIn(), 0.0f, 0.0f, paint);
            }
            if (rotatable.getPathOut() != null) {

                canvas.drawTextOnPath(rotatable.getPreviousWord(), rotatable.getPathOut(), 0.0f, 0.0f, paint);
            }
        }
    }

    void animateInHorizontal() {
        Log.i("point rts158", getHeight() + "");
        animatorin = ValueAnimator.ofFloat(0.0f, getHeight());
        animatorin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i("point rts1631", getWidth() + "");
                Log.i("point rts1632", valueAnimator.getAnimatedValue() + "");
                Log.i("point rts1633", paint.getFontMetrics().bottom + "");
                pathIn = new Path();
                pathIn.moveTo(0.0f, (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                pathIn.lineTo(getWidth(), (Float) valueAnimator.getAnimatedValue() - paint.getFontMetrics().bottom);
                rotatable.setPathIn(pathIn);
            }
        });
        animatorin.setInterpolator(rotatable.getInterpolator());
        animatorin.setDuration(rotatable.getAnimationDuration());
        Log.i("point rts172", "reached");
        animatorin.start();
    }

    public static void stopAnimationIn() {
        Log.i("point 172 stopanime", "reached");
//        if (updateWordTimer != null) {
//            Log.i("point rts318", "reached");
//            updateWordTimer.cancel();
//        }
//        animatorin.end();
//        isRotatableSet=false;
        isPaused=true;
    }

    public static void resumeAnimationIn() {
//        isRotatableSet=true;
        Log.i("point 173 resumeanime", "reached");

        isPaused=false;

    }

    void animateOutHorizontal() {
        ValueAnimator animator = ValueAnimator.ofFloat(getHeight(), getHeight() * 2.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i("point rts181", "reached");
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
        Log.i("point rts194", "reached");
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
        Log.i("point rts233", "reached");
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
        Log.i("point rts271", "reached");
        isPaused = true;
    }

    void resume() {
        Log.i("point rts276", "reached");
        isPaused = false;
    }

    void pauseRender() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable=null;
        }
    }

    void resumeRender() {
        if (disposable == null) {
            Log.i("point rts308", "reached");
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

    void updatePaint() {
        Log.i("point rts303", "reached");
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
                Log.i("point rts325", "reached");
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isPaused) {
                            Log.i("point rts330", "reached");
                            pauseRender();
                        } else {
                            Log.i("point rts333", "reached");
                            resumeRender();
                            animateInHorizontal();
//                            animateOutHorizontal();
                            currentText = rotatable.getNextWord();
                        }
                    }
                });
            }
        }, rotatable.getUpdateDuration(), rotatable.getUpdateDuration());

    }

}
