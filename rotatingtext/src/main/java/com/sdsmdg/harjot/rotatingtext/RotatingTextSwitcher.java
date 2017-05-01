package com.sdsmdg.harjot.rotatingtext;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.Log;
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
        paint.setTextAlign(Paint.Align.CENTER);

        setText(rotatable.getNextWord());

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
                        animateIn();
                        animateOut();
                        setText(rotatable.getNextWord());
                    }
                });
            }
        }, rotatable.getUpdateDuration(), rotatable.getUpdateDuration());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isRotatableSet) {
            String text = (String) getText();
            if (rotatable.getPathIn() != null)
                canvas.drawTextOnPath(text, rotatable.getPathIn(), 0.0f, 0.0f, paint);
            if (rotatable.getPathOut() != null)
                canvas.drawTextOnPath(rotatable.getPreviousWord(), rotatable.getPathOut(), 0.0f, 0.0f, paint);
        }
    }

    void animateIn() {
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

    void animateOut() {
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
}
