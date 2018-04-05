package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

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

    private static void lastRotatable(Rotatable toChange, String text) {
        RotatingTextSwitcher switcher = RotatingTextWrapper.switcherList.get(rotatablePosition);
        //provides space
        switcher.setText(text);
    }

    public void newWord(String... newList) {
        newWordArray = newList;
//        if (rotatablePosition == RotatingTextWrapper.rotatableList.size()-1) {
        toChange.setTextAt(2, "ritikkumaragarwal");
        lastRotatable(toChange, toChange.getLargestWord());
//        }
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
    }
}
