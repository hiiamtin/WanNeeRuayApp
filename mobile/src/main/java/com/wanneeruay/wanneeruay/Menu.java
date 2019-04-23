package com.wanneeruay.wanneeruay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanneeruay.wanneeruay.Firebase.FirebaseHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    public static FirebaseHelper helper;
    public static DatabaseReference db;
    public static ArrayList<String> date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btRandom = findViewById(R.id.bt_random);
        final Button btCheck = findViewById(R.id.bt_check);
        final Button btHistory = findViewById(R.id.bt_history);
        final Button btLektaided = findViewById(R.id.bt_lektaided);
        final Button btStatistic = findViewById(R.id.bt_statistic);
        db = FirebaseDatabase.getInstance().getReference("lottary_date");
        helper = new FirebaseHelper(db);
        loadata();
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

    private void savedata(){
        SharedPreferences sp = getSharedPreferences("lottary_date", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(date);
        editor.putString("date_file",json);
        editor.apply();
    }

    private void loadata(){
        date = helper.retrieve();
        if(date==null){
            Toast.makeText(this, "ไม่สามารถอัพเดตฐานข้อมูลได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_LONG).show();
            SharedPreferences sp = getSharedPreferences("lottary_date", this.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sp.getString("date_file",null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            date = gson.fromJson(json,type);
            if(date == null){
                if(date==null) {
                    Toast.makeText(this, "ไม่สามารถโหลดข้อมูลสำรองได้", Toast.LENGTH_LONG).show();
                    date.clear();
                    date.add("ไม่มีข้อมูล");
                }
            }else{savedata();}
        }

    }

}
