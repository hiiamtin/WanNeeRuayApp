package com.wanneeruay.wanneeruay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class RandomNumber extends AppCompatActivity implements View.OnClickListener{

    ImageView picture;
    ConstraintLayout numberLayout;
    TextView number;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        picture = (ImageView)findViewById(R.id.p1);
        numberLayout = (ConstraintLayout)findViewById(R.id.lektaided_layout);
        final Button btRandom = (Button)findViewById(R.id.random_bt);


        clear();
        btRandom.setOnClickListener(this);
    }
    private void clear(){
        picture.setVisibility(View.VISIBLE);
        numberLayout.setVisibility(View.INVISIBLE);
        status = false;
    }
    private void shack(){
        random();
        picture.setVisibility(View.INVISIBLE);
        numberLayout.setVisibility(View.VISIBLE);
        status = true;
    }
    private void random(){
        number = (TextView)findViewById(R.id.two0);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = (TextView)findViewById(R.id.two1);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = (TextView)findViewById(R.id.three0);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = (TextView)findViewById(R.id.three1);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = (TextView)findViewById(R.id.three2);
        number.setText(String.valueOf(new Random().nextInt(10)));
    }

    @Override
    public void onClick(View v) {
        if(status){
            clear();
        }
        else{
            shack();
        }
    }
}