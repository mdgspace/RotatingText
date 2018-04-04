package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.util.Log;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.ArrayList;

public class UpdateRotatable extends RotatingTextWrapper {

    private static Rotatable toChange;
    private static int rotatablePosition;
    private static String[] newWordArray;
    private static ArrayList<String> rotatableList1;

    public UpdateRotatable(Context context, Rotatable rotatable) {
        super(context);
        if (!RotatingTextWrapper.rotatableList.contains(rotatable)) {
            throw new NullPointerException("rotatable not found");
        } else {
            rotatablePosition = RotatingTextWrapper.rotatableList.indexOf(rotatable);
            this.toChange = rotatable;
        }
    }

    public UpdateRotatable(Context context) {
        super(context);
    }

    public void update() {
        if (RotatingTextSwitcher.isRotatableSet) {
            updateRight();
        } else {
            throw new NullPointerException("rotatable not set");

        }
    }

    private void updateRight() {
//        if (rotatablePosition == RotatingTextWrapper.rotatableList.size()-1) {
//            lastRotatable(toChange,"zzzzzzzzzzzzzzzz");
//        }
    }

    private static void lastRotatable(Rotatable toChange, String text) {
        RotatingTextSwitcher switcher = RotatingTextWrapper.switcherList.get(rotatablePosition);
        //provides space
        switcher.setText(text);
    }

    public void newWord(String... newList) {
        Log.i("point ur76", "reached");
//        Log.i("point ur77", switcher.toString());
        newWordArray = newList;
//        if (rotatablePosition == RotatingTextWrapper.rotatableList.size()-1) {
        Log.i("point ur78", "reached");
        toChange.setTextAt(2, "ritikkumaragarwal");
        lastRotatable(toChange, toChange.getLargestWord());
//        }
    }
}
