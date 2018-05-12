package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.sdsmdg.harjot.rotatingtext.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Harjot on 01-May-17.
 */

public class RotatingTextWrapper extends RelativeLayout {

    String text;
    ArrayList<Rotatable> rotatableList;
    ArrayList<TextView> textViews;

    boolean isContentSet = false;

    Context context;

    RelativeLayout.LayoutParams lp;

    int prevId;

    Typeface typeface;
    int size = 24;

    List<RotatingTextSwitcher> switcherList;

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
        switcherList = new ArrayList<>();
        textViews = new ArrayList<>();
        Collections.addAll(rotatableList, rotatables);
        isContentSet = true;
    }

    public void setContent(String text, ArrayList<Rotatable> rotatables) {
        this.text = text;
        rotatableList = new ArrayList<>(rotatables);
        switcherList = new ArrayList<>();
        isContentSet = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if (isContentSet) {
            String[] array = text.split("\\?");

            if (array.length == 0) {
                final RotatingTextSwitcher textSwitcher = new RotatingTextSwitcher(context);
                switcherList.add(textSwitcher);

                textSwitcher.setRotatable(rotatableList.get(0));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textSwitcher.setId(Utils.generateViewId());
                } else {
                    textSwitcher.setId(View.generateViewId());
                }

                prevId = textSwitcher.getId();

                lp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                lp.addRule(CENTER_VERTICAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                addViewInLayout(textSwitcher, -1, lp);

            }

            for (int i = 0; i < array.length; i++) {
                final TextView textView = new TextView(context);


                textView.setText(array[i]);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textView.setId(Utils.generateViewId());
                } else {
                    textView.setId(View.generateViewId());
                }
                textView.setTextSize(size);
                textViews.add(textView);

                if (typeface != null)
                    textView.setTypeface(typeface);

                lp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(CENTER_VERTICAL);
                if (i == 0)
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                else
                    lp.addRule(RIGHT_OF, prevId);

                addViewInLayout(textView, -1, lp);

                if (i < rotatableList.size()) {
                    final RotatingTextSwitcher textSwitcher = new RotatingTextSwitcher(context);
                    switcherList.add(textSwitcher);
                    textSwitcher.setRotatable(rotatableList.get(i));

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textSwitcher.setId(Utils.generateViewId());
                    } else {
                        textSwitcher.setId(View.generateViewId());
                    }
                    prevId = textSwitcher.getId();

                    lp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(CENTER_VERTICAL);
                    lp.addRule(RIGHT_OF, textView.getId());

                    addViewInLayout(textSwitcher, -1, lp);
                }
            }
            isContentSet = false;
        }
        requestLayout();
    }

    public void shiftRotatable(int index) {

    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void pause(int position) {
        switcherList.get(position).pause();
    }

    public void resume(int position) {
        switcherList.get(position).resume();
    }

}
