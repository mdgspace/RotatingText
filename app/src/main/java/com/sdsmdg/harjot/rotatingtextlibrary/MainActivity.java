package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.sdsmdg.harjot.rotatingtext.RotatingTextSwitcher;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.UpdateRotatable;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.sdsmdg.harjot.rotatingtext.utils.Utils;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    Rotatable rotatable, rotatable2, rotatable3;
//    Rotatable rotatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("point ma29", "reached");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        rotatingTextWrapper = findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(30);
        rotatingTextWrapper.setTypeface(typeface2);

//        rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word00", "Word01", "Word02");
        rotatable = new Rotatable(Color.parseColor("#FFA036"), 2500, "Word", "1", "2");
        rotatable.setSize(25);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new AccelerateInterpolator());
        rotatable.setAnimationDuration(1500);

        rotatable2 = new Rotatable(Color.parseColor("#123456"), 1000, "cat", "dog", "hen");
        rotatable2.setSize(25);
        rotatable2.setTypeface(typeface);
        rotatable2.setInterpolator(new DecelerateInterpolator());
        rotatable2.setAnimationDuration(500);


        rotatable3 = new Rotatable(Color.parseColor("#123456"), 1000, "4", "5", "6");
        rotatable3.setSize(25);
        rotatable3.setTypeface(typeface);
        rotatable3.setInterpolator(new DecelerateInterpolator());
        rotatable3.setAnimationDuration(500);

        rotatingTextWrapper.setContent("?abc ? abc ?", rotatable, rotatable2,rotatable3);
//        rotatingTextWrapper.setContent("? abc", rotatable);

    }

    public void editRotatable(View view) {
        UpdateRotatable updateRotatable = new UpdateRotatable(this, rotatable);
        updateRotatable.newWord("Word00000", "a", "b");
//        updateRotatable.update();
    }

    public void resize(View view) {
        UpdateRotatable updateRotatable = new UpdateRotatable(this);
        updateRotatable.reduceSize(2f, rotatingTextWrapper);
    }

//    public void pushRight(View view) {
//        UpdateRotatable updateRotatable = new UpdateRotatable(this, rotatable);
//        UpdateRotatable.newWord("Word00000", "a", "b");
//        updateRotatable.update(Utils.RIGHT);
//    }
//
//    public void pushLeft(View view) {
//        UpdateRotatable updateRotatable = new UpdateRotatable(this, rotatable);
//        UpdateRotatable.newWord("Word00000", "a", "b");
//        updateRotatable.update(Utils.LEFT);
//    }

    public void cancelRotation(View view) {
        RotatingTextSwitcher.stopAnimationIn();

    }

    public void fitScreen(View view) {
        UpdateRotatable updateRotatable = new UpdateRotatable(this);
        updateRotatable.fitScreen(rotatingTextWrapper);
    }

    public void resumeRotation(View view) {
        RotatingTextSwitcher.resumeAnimationIn();

    }

}
