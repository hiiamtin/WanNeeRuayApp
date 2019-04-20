package com.wanneeruay.wanneeruay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btRandom = (Button)findViewById(R.id.random_bt);
        btRandom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.random_bt:
                Intent intent = new Intent(this, RandomNumber.class);
                startActivity(intent);
                break;
        }
    }
}
