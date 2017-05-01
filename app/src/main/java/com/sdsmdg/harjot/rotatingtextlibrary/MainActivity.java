package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    Rotatable rotatable, rotatable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatable = new Rotatable(Color.RED, 1000, "Word1", "Word2", "Word3");
        rotatable.setAnimationDuration(500);
        rotatable.setInterpolator(new DecelerateInterpolator());
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        rotatable.setTypeface(typeface);
        rotatable2 = new Rotatable(Color.BLUE, 1000, "Word4", "Word5", "Word6");
        rotatable2.setAnimationDuration(500);
        rotatable2.setInterpolator(new AccelerateDecelerateInterpolator());
        rotatingTextWrapper.setContent("This is ?", rotatable);
    }
}
