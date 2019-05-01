package com.wanneeruay.wanneeruay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Mostnum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView date_new = findViewById(R.id.date_text_mostnum);
        date_new.setText(Menu.date_new.getText());
        setContentView(R.layout.activity_mostnum);
        ArrayList<TextView> settext = new ArrayList<>();
        settext.add(findViewById(R.id.acadrank1));
        settext.add(findViewById(R.id.percentrank1));
        settext.add(findViewById(R.id.numrank1));
        settext.add(findViewById(R.id.acadrank2));
        settext.add(findViewById(R.id.percentrank2));
        settext.add(findViewById(R.id.numrank2));
        settext.add(findViewById(R.id.acadrank3));
        settext.add(findViewById(R.id.percentrank3));
        settext.add(findViewById(R.id.numrank3));
        settext.add(findViewById(R.id.acadrank4));
        settext.add(findViewById(R.id.percentrank4));
        settext.add(findViewById(R.id.numrank4));
        settext.add(findViewById(R.id.acadrank5));
        settext.add(findViewById(R.id.percentrank5));
        settext.add(findViewById(R.id.numrank5));
        settext.add(findViewById(R.id.acadrank6));
        settext.add(findViewById(R.id.percentrank6));
        settext.add(findViewById(R.id.numrank6));
        settext.add(findViewById(R.id.acadrank7));
        settext.add(findViewById(R.id.percentrank7));
        settext.add(findViewById(R.id.numrank7));
        settext.add(findViewById(R.id.acadrank8));
        settext.add(findViewById(R.id.percentrank8));
        settext.add(findViewById(R.id.numrank8));
        settext.add(findViewById(R.id.acadrank9));
        settext.add(findViewById(R.id.percentrank9));
        settext.add(findViewById(R.id.numrank9));
        settext.add(findViewById(R.id.acadrank10));
        settext.add(findViewById(R.id.percentrank10));
        settext.add(findViewById(R.id.numrank10));
    }
}
