package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    Rotatable rotatable, rotatable2;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        rotatingTextWrapper = findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(30);
        rotatingTextWrapper.setTypeface(typeface2);

        rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word00", "Word01", "Word02");
        rotatable.setSize(25);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new AccelerateInterpolator());
        rotatable.setAnimationDuration(500);

        rotatable2 = new Rotatable(Color.parseColor("#123456"), 1000, "Word03", "Word04", "Word05");
        rotatable2.setSize(25);
        rotatable2.setTypeface(typeface);
        rotatable2.setInterpolator(new DecelerateInterpolator());
        rotatable2.setAnimationDuration(500);

        rotatingTextWrapper.setContent("abc ? abc ?", rotatable, rotatable2);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rotatingTextWrapper.getSwitcherList().get(0).isPaused()) {
                    rotatingTextWrapper.pause(0);
                } else {
                    rotatingTextWrapper.resume(0);
                }
            }
        });
    }
}
