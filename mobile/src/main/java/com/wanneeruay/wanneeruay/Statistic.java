package com.wanneeruay.wanneeruay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Statistic extends AppCompatActivity {

    ArrayList<TextView> settext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        settext = new ArrayList<>();
        settext.add(findViewById(R.id.up3));
        settext.add(findViewById(R.id.upnum3));
        settext.add(findViewById(R.id.back3));
        settext.add(findViewById(R.id.backnum3));
        settext.add(findViewById(R.id.front3));
        settext.add(findViewById(R.id.frontnum3));
        settext.add(findViewById(R.id.up2));
        settext.add(findViewById(R.id.upnum2));
        settext.add(findViewById(R.id.back2));
        settext.add(findViewById(R.id.backnum2));
        update();
    }

    private void update(){
        for (int i = 0; i < 10; i++) {
            settext.get(i).setText(Menu.statistic.get(i).replace(","," "));
        }
    }
}
