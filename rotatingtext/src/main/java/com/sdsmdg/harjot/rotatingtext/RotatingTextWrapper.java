package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Harjot on 01-May-17.
 */

public class RotatingTextWrapper extends RelativeLayout {

    String text;
    ArrayList<Rotatable> rotatableList;

    boolean isContentSet = false;

    Context context;

    RelativeLayout.LayoutParams lp;

    int prevId;

    public RotatingTextWrapper(Context context) {
        super(context);
        this.context = context;
    }

    public RotatingTextWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RotatingTextWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setContent(String text, Rotatable... rotatables) {
        this.text = text;
        rotatableList = new ArrayList<>();
        Collections.addAll(rotatableList, rotatables);
        isContentSet = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (isContentSet) {
            String[] array = text.split("\\?");
            for (int i = 0; i < array.length; i++) {
                final TextView textView = new TextView(context);
                final RotatingTextSwitcher textSwitcher = new RotatingTextSwitcher(context);

                textView.setText(array[i]);
                textView.setId(View.generateViewId());
                textView.setTextSize(24);

                lp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (i == 0)
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                else
                    lp.addRule(RIGHT_OF, prevId);

                addView(textView, lp);

                if (i < rotatableList.size()) {
                    textSwitcher.setRotatable(rotatableList.get(i));

                    textSwitcher.setId(View.generateViewId());
                    prevId = textSwitcher.getId();

                    lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RIGHT_OF, textView.getId());

                    addView(textSwitcher, lp);
                }
            }
            isContentSet = false;
        }
    }
}
