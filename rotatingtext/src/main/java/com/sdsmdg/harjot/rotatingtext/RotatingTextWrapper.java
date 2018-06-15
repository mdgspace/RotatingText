package com.sdsmdg.harjot.rotatingtext;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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


    private String text;
    private ArrayList<Rotatable> rotatableList;
    private List<RotatingTextSwitcher> switcherList;
    ArrayList<TextView> textViews;

    private boolean isContentSet = false;

    private Context context;

    private RelativeLayout.LayoutParams lp;

    private int prevId;

    private Typeface typeface;
    private int size = 24;

    private double changedSize = 0;
    private boolean adaptable = false;

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
        requestLayout();
    }

    public void setContent(String text, ArrayList<Rotatable> rotatables) {
        this.text = text;
        rotatableList = new ArrayList<>(rotatables);
        switcherList = new ArrayList<>();
        isContentSet = true;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isContentSet) {
            removeAllViews();
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

                addView(textSwitcher, lp);

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

                addView(textView, lp);

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

                    addView(textSwitcher, lp);
                }
            }
            isContentSet = false;
        }
    }

    public void replaceWord(int rotatableIndex, int wordIndex, String newWord) {
        if (!TextUtils.isEmpty(newWord) && (!newWord.contains("\n"))) {

            RotatingTextSwitcher switcher = switcherList.get(rotatableIndex);
            Rotatable toChange = rotatableList.get(rotatableIndex);

            Paint paint = new Paint();
            paint.setTextSize(toChange.getSize() * getContext().getResources().getDisplayMetrics().density);
            paint.setTypeface(toChange.getTypeface());
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            Rect result = new Rect();

            paint.getTextBounds(toChange.getLargestWord(), 0, toChange.getLargestWord().length(), result);

            double originalSize = result.width();

            String toDeleteWord = toChange.getTextAt(wordIndex);

            paint = new Paint();
            paint.setTextSize(toChange.getSize() * getContext().getResources().getDisplayMetrics().density);
            paint.setTypeface(toChange.getTypeface());
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            result = new Rect();
            paint.getTextBounds(toChange.peekLargestWord(wordIndex, newWord), 0, toChange.peekLargestWord(wordIndex, newWord).length(), result);
            double finalSize = result.width();

            if (finalSize < originalSize) {
                //we are replacing the largest word with a smaller new word

                if (toChange.getCurrentWord().equals(toDeleteWord) && switcher.animationRunning) {
                    //largest word is entering
                    singleAnimationComplete(toChange.getAnimationDuration() + toChange.getUpdateDuration(), toChange.getUpdateDuration(), toChange, switcher, wordIndex, newWord);
                } else if (toChange.getCurrentWord().equals(toDeleteWord) && !switcher.animationRunning) {
                    //largest word is the screen waiting for going out
                    singleAnimationComplete(toChange.getUpdateDuration(), toChange.getUpdateDuration() - toChange.getAnimationDuration(), toChange, switcher, wordIndex, newWord);
                } else if (toChange.getPreviousWord().equals(toDeleteWord)) {
                    // largest word is leaving
                    singleAnimationComplete(toChange.getAnimationDuration(), 0, toChange, switcher, wordIndex, newWord);

                } else {
                    //largest word is not in the screen
                    toChange.setTextAt(wordIndex, newWord);
                    switcher.setText(toChange.getLargestWordWithSpace()); //provides space

                    if (adaptable && getSize() != (int) changedSize && changedSize != 0) {

                        if ((double) availablePixels() / (double) findRequiredPixel() < getSize() / changedSize)
                            reduceSize((double) findRequiredPixel() / (double) availablePixels());
                        else reduceSize(changedSize / getSize());
                    }
                }
            } else {
                toChange.setTextAt(wordIndex, newWord);

                switcher.setText(toChange.getLargestWordWithSpace());//provides space
                if (adaptable && finalSize != originalSize) {
                    int actualPixel = findRequiredPixel();

                    if (adaptable && actualPixel > availablePixels()) {
                        reduceSize((double) actualPixel / (double) availablePixels());
                    }
                }
            }
        }
    }

    private void singleAnimationComplete(final int totalTime, final int startTime, final Rotatable toChange, final RotatingTextSwitcher switcher, final int index, final String newWord) {
        toChange.setTextAt(index, newWord);

        // here 40 take care for the average delay from the request to the time we get the first Tick
        // and 21 is just a perfect time-period to make a tick (and can be safely assumed to be assumed to be
        // smaller than AnimationDuration)
        // and 43 is added to get 2 extra ticks to safely counter terminal cases!

        //indeed:
        /**
         * There is so much number hackery in here.
         * Number fishing is HOW YOU WIN AT LIFE. -- paullewis :)
         * **/

        new CountDownTimer(totalTime + 43, 21) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (totalTime - millisUntilFinished > startTime - 40 && !switcher.animationRunning) {
                    switcher.setText(toChange.getLargestWordWithSpace());
                    if (adaptable && getSize() != (int) changedSize && changedSize != 0) {
                        if ((double) availablePixels() / (double) findRequiredPixel() < getSize() / changedSize)
                            reduceSize((double) findRequiredPixel() / (double) availablePixels());
                        else reduceSize(changedSize / getSize());
                    }
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private int availablePixels() {
        //returns total pixel available with parent
        View parent = (View) getParent();
        return parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
    }

    private int findRequiredPixel() {
        //returns observed wrapper size on screen including padding and margin in pixels
        int actualPixel = 0;
        MarginLayoutParams margins;

        for (RotatingTextSwitcher switcher : switcherList) {
            switcher.measure(0, 0);
            actualPixel += switcher.getMeasuredWidth();
        }

        for (TextView id : textViews) {
            id.measure(0, 0);
            actualPixel += id.getMeasuredWidth();
        }

        margins = MarginLayoutParams.class.cast(getLayoutParams());
        actualPixel += margins.leftMargin;
        actualPixel += margins.rightMargin;
        actualPixel += getPaddingLeft();
        actualPixel += getPaddingRight();
        return actualPixel;
    }

    public void reduceSize(double factor) {
        double initialSizeWrapper = (changedSize == 0) ? getSize() : changedSize;

        double newWrapperSize = (double) initialSizeWrapper / factor;

        for (RotatingTextSwitcher switcher : switcherList) {
            double initialSizeRotatable = switcher.getTextSize();

            double newRotatableSize = initialSizeRotatable / factor;
            switcher.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) newRotatableSize);

        }
        for (TextView id : textViews) {
            id.setTextSize((float) newWrapperSize);

        }
        MarginLayoutParams margins = MarginLayoutParams.class.cast(getLayoutParams());

        margins.leftMargin = (int) (margins.leftMargin / factor);
        margins.rightMargin = (int) (margins.rightMargin / factor);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (paddingLeft != 0) {
            if (paddingRight == 0)
                setPadding((int) (paddingLeft / factor), 0, 0, 0);
            else
                setPadding((int) ((paddingLeft / factor)), 0, (int) ((paddingRight / factor)), 0);

        } else if (paddingRight != 0) {
            setPadding(0, 0, (int) (paddingRight / factor), 0);
        }

        changedSize = (changedSize == 0) ? getSize() / factor : changedSize / factor;

        if (adaptable && findRequiredPixel() > availablePixels()) {
            reduceSize((double) findRequiredPixel() / (double) availablePixels());
        }
    }

    public void setAdaptable(boolean adaptable) {
        this.adaptable = adaptable;
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

    public List<RotatingTextSwitcher> getSwitcherList() {
        return switcherList;
    }
}
