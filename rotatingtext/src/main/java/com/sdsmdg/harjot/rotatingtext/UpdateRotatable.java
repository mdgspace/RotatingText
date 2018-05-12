package com.sdsmdg.harjot.rotatingtext;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class UpdateRotatable {

    private Rotatable toChange;
    private int rotatablePosition;
    private RotatingTextWrapper wrapper;

    public UpdateRotatable(Rotatable rotatable, RotatingTextWrapper wrapper) {
        this.wrapper = wrapper;
        if (!wrapper.rotatableList.contains(rotatable)) {
            throw new NullPointerException("rotatable not found");
        } else {
            rotatablePosition = wrapper.rotatableList.indexOf(rotatable);
            this.toChange = rotatable;
        }

    }

    public void newWord(String newWord, int index) {
        if (!TextUtils.isEmpty(newWord) && (!newWord.contains("\n"))) {
            RotatingTextSwitcher switcher = wrapper.switcherList.get(rotatablePosition);

            Paint paint = new Paint();
            paint.setTextSize(20);
            paint.setTypeface(toChange.getTypeface());
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            Rect result = new Rect();

            paint.getTextBounds(toChange.getLargestWord(), 0, toChange.getLargestWord().length(), result);

            float originalSize = result.width();

            String toDeleteWord = toChange.getTextAt(0);

            paint = new Paint();
            paint.setTextSize(20);
            paint.setTypeface(toChange.getTypeface());
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            result = new Rect();
            paint.getTextBounds(toChange.peekLargestWord(index, newWord), 0, toChange.peekLargestWord(index, newWord).length(), result);
            float finalSize = result.width();

            Log.i("toDeleteWord", toDeleteWord);
            Log.i("PreviousWord", toChange.getPreviousWord());
            Log.i("CurrentWord", toChange.getCurrentWord());
            if (finalSize < originalSize) {

                //we are replacing the largest word with a smaller new word
                if (toChange.getPreviousWord().equals(toDeleteWord)) {
                    waitForAnimationComplete(toChange.getAnimationDuration(), toChange.getLargestWord(), false, switcher, newWord, index);
                } else if (toChange.getCurrentWord().equals(toDeleteWord)) {
                    waitForAnimationComplete(toChange.getAnimationDuration() + toChange.getUpdateDuration(), toChange.getLargestWord(), true, switcher, newWord, index);

                } else {
                    toChange.setTextAt(index, newWord);
                    //provides space
                    switcher.setText(toChange.getLargestWordWithSpace());
                }
            } else {
                toChange.setTextAt(index, newWord);
                //provides space
                switcher.setText(toChange.getLargestWordWithSpace());
            }
        }
    }

    private void waitForAnimationComplete(int totalTime, final String oldLargestWord, boolean positionEntering, final RotatingTextSwitcher switcher, final String newWord, final int index) {

        if (positionEntering) {
            new CountDownTimer(totalTime + 23, 22) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (!switcher.animationRunning && !toChange.getCurrentWord().equals(oldLargestWord)) {
                        toChange.setTextAt(index, newWord);
                        //provides space
                        switcher.setText(toChange.getLargestWordWithSpace());
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        } else {
            new CountDownTimer(totalTime + 23, 22) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (!switcher.animationRunning) {
                        toChange.setTextAt(index, newWord);
                        //provides space
                        switcher.setText(toChange.getLargestWordWithSpace());
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
    }

    public void fitScreen(RotatingTextWrapper rotator) {
        boolean crushed = false;
        float totalPixel = 0;
        float actualPixel = 0;
        MarginLayoutParams margins;

        for (RotatingTextSwitcher rotatable : wrapper.switcherList) {

            totalPixel += rotatable.getWidth();
            rotatable.measure(0, 0);
            actualPixel += rotatable.getMeasuredWidth();


        }
        for (TextView id : wrapper.textViews) {
            totalPixel += id.getWidth();

            id.measure(0, 0);
            actualPixel += id.getMeasuredWidth();

        }
        margins = MarginLayoutParams.class.cast(rotator.getLayoutParams());

        totalPixel += margins.leftMargin; // pixels as density is 1.5
        totalPixel += margins.rightMargin;
        totalPixel += rotator.getPaddingLeft();
        totalPixel += rotator.getPaddingRight();

        actualPixel += margins.leftMargin;
        actualPixel += margins.rightMargin;
        actualPixel += rotator.getPaddingLeft();
        actualPixel += rotator.getPaddingRight();

        if (actualPixel != totalPixel) {
            crushed = true;
            reduceSize(actualPixel / totalPixel, rotator);
        }
        Log.i("crushed", crushed + "");


    }

    public void reduceSize(float factor, RotatingTextWrapper wrapper) {
        int initialSizeWrapper = wrapper.getSize();

        float newWrapperSize = initialSizeWrapper / factor;

        for (RotatingTextSwitcher rotatable : wrapper.switcherList) {
            float initialSizeRotatable = rotatable.getTextSize();
            float newRotatableSize = initialSizeRotatable / factor;

            rotatable.setTextSize(TypedValue.COMPLEX_UNIT_PX, newRotatableSize);

        }
        for (TextView id : wrapper.textViews) {
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
