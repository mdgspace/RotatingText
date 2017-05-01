package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    Rotatable rotatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatable = new Rotatable(Color.RED, 1000, "Word1", "Word2", "Word3");
        rotatable.setAnimationDuration(500);
        rotatingTextWrapper.setContent("This is ?", rotatable);

    }
}
