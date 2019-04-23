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

        final Button btRandom = findViewById(R.id.bt_random);
        final Button btCheck = findViewById(R.id.bt_check);
        final Button btHistory = findViewById(R.id.bt_history);
        final Button btLektaided = findViewById(R.id.bt_lektaided);
        final Button btStatistic = findViewById(R.id.bt_statistic);
        btRandom.setOnClickListener(this);
        btCheck.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btLektaided.setOnClickListener(this);
        btStatistic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_random:
                startActivity(new Intent(this, RandomNumber.class));
                break;

            case R.id.bt_check:
                startActivity(new Intent(this, CheckNumber.class));
                break;

            case R.id.bt_history:
                startActivity(new Intent(this, History.class));
                break;

            case R.id.bt_statistic:
                startActivity(new Intent(this, Statistic.class));
                break;
            case R.id.bt_lektaided:
                startActivity(new Intent(this,Mostnum.class));
                break;
        }
    }
}
