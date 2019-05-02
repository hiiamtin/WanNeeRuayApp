package com.wanneeruay.wanneeruay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.wanneeruay.wanneeruay.Firebase.FirebaseHelper;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;
import java.util.ArrayList;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    private static FirebaseHelper helper;
    public static ArrayList<String> date,mostNum,statistic;
    public static ArrayList<Spacecraft> lottary_data;
    public static Context context;
    public static TextView date_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btRandom = findViewById(R.id.bt_random);
        final Button btCheck = findViewById(R.id.bt_check);
        final Button btHistory = findViewById(R.id.bt_history);
        final Button btLektaided = findViewById(R.id.bt_lektaided);
        final Button btStatistic = findViewById(R.id.bt_statistic);
        context = this;
        date_new = findViewById(R.id.date_text_main);
        permiss();
        helper = new FirebaseHelper(FirebaseDatabase.getInstance());
        btRandom.setOnClickListener(this);
        btCheck.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btLektaided.setOnClickListener(this);
        btStatistic.setOnClickListener(this);
        update();
        if(date.isEmpty()||date==null){
            new Handler().postDelayed(() -> {
                //do something
                if(date!=null)date_new.setText(date.get(0).trim());
            }, 6000 );//time in milisecond
        }
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
                //Toast.makeText(this,mostNum.toString(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Mostnum.class));
                break;
        }
    }

    private void update(){
        date=helper.updateLottaryDate(this);
        lottary_data=helper.updateLottaryDB(this);
        statistic=helper.updateStatistic(this);
        mostNum=helper.updateMostnum(this);
    }


    public static void setDate(ArrayList<String> date) {
        Menu.date = date;
    }

    public static void setMostNum(ArrayList<String> mostNum) {
        Menu.mostNum = mostNum;
    }

    public static void setStatistic(ArrayList<String> statistic) {
        Menu.statistic = statistic;
    }

    public static void setLottary_data(ArrayList<Spacecraft> lottary_data) {
        Menu.lottary_data = lottary_data;
    }

    private void permiss(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
            }
        }
    }

}
