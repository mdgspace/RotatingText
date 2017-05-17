package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper, rotatingTextWrapper1, rotatingTextWrapper2;
    Rotatable rotatable, rotatable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setTypeface(typeface);
        rotatingTextWrapper.setSize(30);

        rotatable = new Rotatable(Color.RED, 1000, "Word", "Word02", "Word03");
        rotatable.setTypeface(typeface);
        rotatable.setSize(30);
        rotatable.setAnimationDuration(500);
        rotatable.setInterpolator(new DecelerateInterpolator());

        rotatingTextWrapper.setContent("This is ?", rotatable);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotatingTextWrapper.pause(0);
            }
        }, 4500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotatingTextWrapper.resume(0);
            }
        }, 8500);
    }
}
