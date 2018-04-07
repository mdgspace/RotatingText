package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.sdsmdg.harjot.rotatingtext.utils.Utils.POSITION_ENTERING;
import static com.sdsmdg.harjot.rotatingtext.utils.Utils.POSITION_LEAVING;

public class UpdateRotatable extends RotatingTextWrapper {

    private Rotatable toChange;
    private static int rotatablePosition;
    private static String[] newWordArray;
    Context context;

    public UpdateRotatable(Context context, Rotatable rotatable) {
        super(context);
        this.context = context;
        if (!RotatingTextWrapper.rotatableList.contains(rotatable)) {
            throw new NullPointerException("rotatable not found");
        } else {
            rotatablePosition = RotatingTextWrapper.rotatableList.indexOf(rotatable);
            this.toChange = rotatable;
        }

    }

    public UpdateRotatable(Context context) {
        super(context);
        this.context = context;
    }


    public void newWord(String... newList) {
        newWordArray = newList;
        RotatingTextSwitcher switcher = RotatingTextWrapper.switcherList.get(rotatablePosition);

        Paint paint = new Paint();
        paint.setTextSize(20);
        paint.setTypeface(toChange.getTypeface());
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        Rect result = new Rect();

        paint.getTextBounds(toChange.getLargestWord(), 0, toChange.getLargestWord().length(), result);

        Log.i("point ur56", "initial: " + result.width());
        float originalSize = result.width();

        String toDeleteWord = toChange.getTextAt(0);

        paint = new Paint();
        paint.setTextSize(20);
        paint.setTypeface(toChange.getTypeface());
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        result = new Rect();
        paint.getTextBounds(toChange.peekLargestWord(0, "cse"), 0, toChange.peekLargestWord(0, "cse").length(), result);
        Log.i("point ur56", "final: " + result.width());
        float finalSize = result.width();

        Log.i("point ur69", toDeleteWord);
        Log.i("point ur70", toChange.getPreviousWord());
        Log.i("point ur71", toChange.getCurrentWord());
        if (finalSize < originalSize) {

//we are replacing the largest word with a smaller new word
            if (toChange.getPreviousWord().equals(toDeleteWord)) {
                waitForAnimationComplete(toChange.getAnimationDuration(), toChange.getLargestWord(), POSITION_LEAVING, switcher);
            } else if (toChange.getCurrentWord().equals(toDeleteWord)) {
                waitForAnimationComplete(toChange.getAnimationDuration() + toChange.getUpdateDuration(), toChange.getLargestWord(), POSITION_ENTERING, switcher);

            } else {
                toChange.setTextAt(0, "cse");
                //provides space
                switcher.setText(toChange.getLargestWordWithSpace());
            }
        } else {
            toChange.setTextAt(0, "cse");
            //provides space
            switcher.setText(toChange.getLargestWordWithSpace());
        }
    }

    private void waitForAnimationComplete(int totalTime, final String oldLargestWord, boolean position, final RotatingTextSwitcher switcher) {

        if (position == POSITION_ENTERING) {
            new CountDownTimer(totalTime + 23, 22) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("point ur142", switcher.animationRunning + "");
                    Log.i("point ur143", oldLargestWord + "");
                    Log.i("point ur143", toChange.getCurrentWord() + "");
                    if (!switcher.animationRunning && !toChange.getCurrentWord().equals(oldLargestWord)) {
                        toChange.setTextAt(0, "cse");
//                //provides space
                        switcher.setText(toChange.getLargestWordWithSpace());
                    }
                }

                @Override
                public void onFinish() {
                    Log.i("point ur154", "finished");
                }
            }.start();
        } else {
            new CountDownTimer(totalTime + 23, 22) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("point ur162", switcher.animationRunning + "");
                    Log.i("point ur163", oldLargestWord + "");
                    Log.i("point ur164", toChange.getPreviousWord() + "");
                    if (!switcher.animationRunning) {
                        toChange.setTextAt(0, "cse");
//                //provides space
                        switcher.setText(toChange.getLargestWordWithSpace());
                    }
                }

                @Override
                public void onFinish() {
                    Log.i("point ur174", "finished");
                }
            }.start();
        }
    }

    public void test() {

    }

    public void fitScreen(RotatingTextWrapper rotator) {
        boolean crushed = false;
        float totalPixel = 0;
        float actualPixel = 0;
        MarginLayoutParams margins;

        for (RotatingTextSwitcher rotatable : RotatingTextWrapper.switcherList) {

            Log.i("point ur721", rotatable.getText().toString());
            Log.i("point ur78", rotatable.getWidth() + "");
            totalPixel += rotatable.getWidth();
            rotatable.measure(0, 0);
            Log.i("point ur781", rotatable.getMeasuredWidth() + "");
            actualPixel += rotatable.getMeasuredWidth();


        }
        for (TextView id : RotatingTextWrapper.textViews) {
            totalPixel += id.getWidth();
            Log.i("point ur75", id.getWidth() + "");

            id.measure(0, 0);
            Log.i("point ur781", id.getMeasuredWidth() + "");
            actualPixel += id.getMeasuredWidth();

        }
        margins = MarginLayoutParams.class.cast(rotator.getLayoutParams());

        Log.i("point ur114", rotator.getPaddingRight() + "");
        Log.i("point ur114", rotator.getPaddingLeft() + "");
        Log.i("point ur115", margins.leftMargin + "");
        Log.i("point ur1151", margins.rightMargin + "");

        totalPixel += margins.leftMargin; // pixels as density is 1.5
        totalPixel += margins.rightMargin;
        totalPixel += rotator.getPaddingLeft();
        totalPixel += rotator.getPaddingRight();

        actualPixel += margins.leftMargin;
        actualPixel += margins.rightMargin;
        actualPixel += rotator.getPaddingLeft();
        actualPixel += rotator.getPaddingRight();

        Log.i("point ur79", totalPixel + "");
        Log.i("point ur791", actualPixel + "");
        if (actualPixel != totalPixel) {
            crushed = true;
            reduceSize(actualPixel / totalPixel, rotator);
        }
        Log.i("point ur105", crushed + "");


    }

    public void reduceSize(float factor, RotatingTextWrapper wrapper) {
        int initialSizeWrapper = wrapper.getSize();

        float newWrapperSize = initialSizeWrapper / factor;
        Log.i("point ur129", initialSizeWrapper + "");
        Log.i("point ur130", newWrapperSize + "");
        Log.i("point ur130", factor + "");

        for (RotatingTextSwitcher rotatable : RotatingTextWrapper.switcherList) {
            float initialSizeRotatable = rotatable.getTextSize();
            float newRotatableSize = initialSizeRotatable / factor;
            Log.i("point ur134", initialSizeRotatable + "");
            Log.i("point ur135", newRotatableSize + "");

            rotatable.setTextSize(TypedValue.COMPLEX_UNIT_PX, newRotatableSize);

        }
        for (TextView id : RotatingTextWrapper.textViews) {
            id.setTextSize(newWrapperSize);

        }
        MarginLayoutParams margins = MarginLayoutParams.class.cast(wrapper.getLayoutParams());

        margins.leftMargin = (int) (margins.leftMargin / factor);
        margins.rightMargin = (int) (margins.rightMargin / factor);
        int paddingLeft = wrapper.getPaddingLeft();
        int paddingRight = wrapper.getPaddingRight();

        if (paddingLeft != 0) {
            if (paddingRight == 0)
                wrapper.setPadding((int) (paddingLeft / factor), 0, 0, 0);
            else
                wrapper.setPadding((int) (paddingLeft / factor), 0, (int) (paddingRight / factor), 0);

        } else if (paddingRight != 0) {
            wrapper.setPadding(0, 0, (int) (paddingRight / factor), 0);
        }

        wrapper.setSize((int) (wrapper.getSize() / factor));
    }
}
