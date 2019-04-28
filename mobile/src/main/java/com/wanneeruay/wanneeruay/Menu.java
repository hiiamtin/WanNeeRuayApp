package com.wanneeruay.wanneeruay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanneeruay.wanneeruay.Firebase.FirebaseHelper;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    private static FirebaseHelper helper;
    public static ArrayList<String> date;
    public static ArrayList<Spacecraft> lottary_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btRandom = findViewById(R.id.bt_random);
        final Button btCheck = findViewById(R.id.bt_check);
        final Button btHistory = findViewById(R.id.bt_history);
        final Button btLektaided = findViewById(R.id.bt_lektaided);
        final Button btStatistic = findViewById(R.id.bt_statistic);
        helper = new FirebaseHelper(FirebaseDatabase.getInstance());
        boolean check = helper.checkConnection();
        Toast.makeText(this,String.valueOf(check),Toast.LENGTH_SHORT).show();
        loadDate();
        loadDB();
        btRandom.setOnClickListener(this);
        btCheck.setOnClickListener(this);
        btHistory.setOnClickListener(this);
        btLektaided.setOnClickListener(this);
        btStatistic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        helper.updateLottaryDB();
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

    private void saveDate(){
        SharedPreferences sp = getSharedPreferences("lottary_date", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(date);
        editor.putString("date_file",json);
        editor.apply();
        Toast.makeText(this, "UPDATE FILE DATE", Toast.LENGTH_SHORT).show();
    }

    private void loadDate(){
        date = helper.updateLottaryDate();
        if(date==null){
            Toast.makeText(this, "ไม่สามารถอัพเดตDateได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("lottary_date", this.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sp.getString("date_file",null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            date = gson.fromJson(json,type);
            if(date == null){
                    Toast.makeText(this, "ไม่สามารถโหลดข้อมูลสำรองได้", Toast.LENGTH_SHORT).show();
                    date.clear();
                    date.add("ไม่มีข้อมูล");
            }else{
                Toast.makeText(this, "LOAD ข้อมูล Date สำรอง", Toast.LENGTH_SHORT).show();
            }
        }else{saveDate();}
    }
    private void savedDB(){
        SharedPreferences sp = getSharedPreferences("lottary_db", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(date);
        editor.putString("DB_file",json);
        editor.apply();
        Toast.makeText(this, "UPDATE FILE DB", Toast.LENGTH_SHORT).show();
    }
    private void loadDB(){
        lottary_data = helper.updateLottaryDB();
        if(date==null){
            Toast.makeText(this, "ไม่สามารถอัพเดตฐานข้อมูลได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("lottary_db", this.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sp.getString("DB_file",null);
            Type type = new TypeToken<ArrayList<Spacecraft>>() {}.getType();
            lottary_data = gson.fromJson(json,type);
            if(lottary_data == null){
                Toast.makeText(this, "ไม่สามารถโหลดข้อมูลสำรองได้", Toast.LENGTH_SHORT).show();
                lottary_data.clear();
                Spacecraft text = new Spacecraft();
                text.setKey("Error");
                lottary_data.add(text);
            }else{
                Toast.makeText(this, "LOAD ข้อมูล DB สำรอง", Toast.LENGTH_SHORT).show();
            }
        }else{savedDB();}
    }

}
