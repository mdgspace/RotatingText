package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    Rotatable rotatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setTypeface(typeface);
        rotatingTextWrapper.setSize(30);

        rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word02", "Word03");
        rotatable.setTypeface(typeface);
        rotatable.setSize(30);
        rotatable.setAnimationDuration(500);
        rotatable.setInterpolator(new DecelerateInterpolator());

        rotatingTextWrapper.setContent("This is ?", rotatable);
    }
}
