package com.sdsmdg.harjot.rotatingtextlibrary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.harjot.rotatingtext.RotatingTextSwitcher;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RotatingTextWrapper rotatingTextWrapper;
    RotatingTextSwitcher rotatingTextSwitcher;
    Rotatable rotatable, rotatable2;
    Spinner s1;
    String word;
    int nCycles;
    EditText enterCycles;
    EditText e1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotatingTextSwitcher = new RotatingTextSwitcher(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        rotatingTextWrapper = findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(30);
        rotatingTextWrapper.setTypeface(typeface2);

//        rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word00", "Word01", "Word02");
        rotatable2 = new Rotatable(Color.parseColor("#123456"), 1000, "word", "word01", "word02");
        rotatable2.setSize(25);
        rotatable2.setTypeface(typeface);
        rotatable2.setInterpolator(new DecelerateInterpolator());
        rotatable2.setAnimationDuration(500);


        rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "rotating", "text", "library");
        rotatable.setSize(25);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new AccelerateInterpolator());
        rotatable.setAnimationDuration(500);

        word = rotatable.getTextAt(0);

        rotatingTextWrapper.setContent("?abc ? abc", rotatable, rotatable2);
//        rotatingTextWrapper.setContent("? abc", rotatable);

        s1 = (Spinner) findViewById(R.id.spinner);



        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(dataAdapter);
        s1.setSelection(0);

        e1 = (EditText) findViewById(R.id.replaceWord);

        button = findViewById(R.id.pause_button);
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

        Button button2 = findViewById(R.id.set_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterCycles = (EditText) findViewById(R.id.enterCycles);


                if(!enterCycles.getText().toString().equals("") && enterCycles.getText() != null) {
                    nCycles = Integer.parseInt(enterCycles.getText().toString());
                }

                rotatingTextSwitcher.cycles(nCycles,word);

                if (rotatingTextWrapper.getSwitcherList().get(0).isPaused()) {
                    rotatingTextWrapper.resume(0);
                }
            }
        });
    }

    public void replaceWord(View view) {
        String newWord = e1.getText().toString();
        if (TextUtils.isEmpty(newWord)) e1.setText("can't be left empty");
        else if (newWord.contains("\n")) e1.setText("one line only");
        else {
            rotatingTextWrapper.setAdaptable(true);
            rotatingTextWrapper.addWord(0, (int) s1.getSelectedItem() - 1, newWord);
        }

    }
}

