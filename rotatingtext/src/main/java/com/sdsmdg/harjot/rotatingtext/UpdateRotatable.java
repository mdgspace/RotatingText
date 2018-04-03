package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.sdsmdg.harjot.rotatingtext.utils.Utils;

public class UpdateRotatable extends RotatingTextWrapper {

    private Rotatable toChange;

    public UpdateRotatable(Context context, Rotatable rotatable) {
        super(context);
        this.toChange=rotatable;
    }

    public UpdateRotatable(Context context) {
        super(context);
    }

    public void update(int direction){
        if(RotatingTextSwitcher.isRotatableSet) {
            if (direction == Utils.LEFT) {
                updateLeft();
            } else if (direction == Utils.RIGHT) {
                updateRight();
            }
        }else {
            throw new NullPointerException("rotatable not set");

        }
    }

    private void updateLeft(){

    }

    private void updateRight() {

    }
}
