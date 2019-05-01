package com.wanneeruay.wanneeruay.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanneeruay.wanneeruay.Menu;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class FirebaseHelper {

    private DatabaseReference dateDB, lottaryDB, mostnumDB, statisticDB;
    private ArrayList<String> dateArr, mostNumArr, statisticArr;
    private ArrayList<Spacecraft> lottaryArr = new ArrayList<>();
    private FirebaseDatabase FB;

    public FirebaseHelper(FirebaseDatabase FB) {
        this.FB = FB;
        dateDB = FB.getReference("lottary_date");
        mostnumDB = FB.getReference("mostNum");
        lottaryDB = FB.getReference("lottary_db");
        statisticDB = FB.getReference("statisticNum");
    }

    //READ
    public ArrayList<String> updateLottaryDate(Context context){
        final boolean[] gotResult = new boolean[1];
        gotResult[0] = false;
        dateArr = new ArrayList<>();
        AlertDialog al = loadinPopUp(context);
        ValueEventListener dataFetchEventListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot, dateArr);
                if(dateArr.isEmpty()) {
                    Toast.makeText(context, "ไม่สามารถอัพเดตDateได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    if(load(context, dateArr,"date")){
                        Menu.date_new.setText(dateArr.get(0));
                        gotResult[0] = true;
                    }
                }else{
                    Toast.makeText(context,"Date loaded",Toast.LENGTH_SHORT).show();
                    gotResult[0] = true;
                    Collections.reverse(dateArr);
                    Menu.date_new.setText(dateArr.get(0));
                    saved(context, dateArr,"date");
                }
                if(al.isShowing()){
                    al.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dateArr.clear();
                gotResult[0] = true;
                if(al.isShowing()){
                    al.cancel();
                }
            }
        };
        dateDB.addListenerForSingleValueEvent(dataFetchEventListener);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                if (gotResult[0] == false) { //  Timeout
                    dateDB.removeEventListener(dataFetchEventListener);
                    // Your timeout code goes here
                    Toast.makeText(context, "ไม่สามารถอัพเดตDateได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    if(load(context, dateArr,"date")){
                        Menu.date_new.setText(dateArr.get(0));
                        gotResult[0] = true;
                    }
                    if(al.isShowing()){
                        al.cancel();
                    }
                }
            }
        };
            // Setting timeout of 5 sec to the request
            timer.schedule(timerTask, 5000L);

        return dateArr;
    }

    public ArrayList<Spacecraft> updateLottaryDB(Context context){
        final boolean[] gotResult = new boolean[1];
        gotResult[0] = false;
        AlertDialog al = loadinPopUp(context);
        ValueEventListener dataFetchEventListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData2(dataSnapshot, lottaryArr);
                if(lottaryArr.isEmpty()){
                    Toast.makeText(context, "ไม่สามารถอัพเดตฐานข้อมูลได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    gotResult[0] = loadDB(context);
                }else{
                    Toast.makeText(context,"DB loaded",Toast.LENGTH_SHORT).show();
                    Collections.reverse(lottaryArr);
                    savedDB(context);
                    gotResult[0] = true;
                }
                if(al.isShowing()){
                    al.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lottaryArr.clear();
                gotResult[0] = true;
                if(al.isShowing()){
                    al.cancel();
                }
                Toast.makeText(context,"can't loaded",Toast.LENGTH_SHORT).show();
            }
        };
        lottaryDB.addListenerForSingleValueEvent(dataFetchEventListener);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                if (gotResult[0] == false) { //  Timeout
                    dateDB.removeEventListener(dataFetchEventListener);
                    // Your timeout code goes here
                    Toast.makeText(context, "ไม่สามารถอัพเดตฐานข้อมูลได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    loadDB(context);
                    if(al.isShowing()){
                        al.cancel();
                    }
                }
            }
        };
        // Setting timeout of 5 sec to the request
        timer.schedule(timerTask, 5000L);
        return lottaryArr;
    }

    public ArrayList<String> updateMostnum(Context context){
        final boolean[] gotResult = new boolean[1];
        gotResult[0] = false;
        mostNumArr = new ArrayList<>();
        AlertDialog al = loadinPopUp(context);
        ValueEventListener dataFetchEventListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot, mostNumArr);
                if(mostNumArr.isEmpty()) {
                    Toast.makeText(context, "ไม่สามารถอัพเดต mostNum ได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    gotResult[0] = load(context, mostNumArr,"mostNum");
                }else{
                    Toast.makeText(context,"MostNum loaded",Toast.LENGTH_SHORT).show();
                    saved(context, mostNumArr,"mostNum");
                    gotResult[0] = true;
                }
                if(al.isShowing()){
                    al.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mostNumArr.clear();
                gotResult[0] = true;
                if(al.isShowing()){
                    al.cancel();
                }
            }
        };
        mostnumDB.addListenerForSingleValueEvent(dataFetchEventListener);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                if (gotResult[0] == false) { //  Timeout
                    mostnumDB.removeEventListener(dataFetchEventListener);
                    // Your timeout code goes here
                    Toast.makeText(context, "ไม่สามารถอัพเดต mostNum ได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    gotResult[0] = load(context, mostNumArr,"mostNum");
                    if(al.isShowing()){
                        al.cancel();
                    }
                }
            }
        };
        // Setting timeout of 5 sec to the request
        timer.schedule(timerTask, 5000L);
        return mostNumArr;
    }

    public ArrayList<String> updateStatistic(Context context){
        statisticArr = new ArrayList<>();
        AlertDialog al = loadinPopUp(context);
        statisticDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot, statisticArr);
                if(statisticArr.isEmpty()) {
                    Toast.makeText(context, "ไม่สามารถอัพเดต mostNum ได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    load(context, statisticArr,"statistic");
                }else{
                    Toast.makeText(context,"statistic loaded",Toast.LENGTH_SHORT).show();
                    saved(context, statisticArr,"statistic");
                }
                al.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                statisticArr.clear();
                al.cancel();
            }
        });
        return statisticArr;
    }

    private void fetchData(DataSnapshot snapshot,ArrayList<String> sp){
        sp.clear();
        for (DataSnapshot ds:snapshot.getChildren()) {
            sp.add(ds.getValue(String.class));
        }

    }
    private void fetchData2(DataSnapshot snapshot,ArrayList<Spacecraft> sp){
        sp.clear();
        for (DataSnapshot ds:snapshot.child("62").getChildren()){
            Spacecraft x = new Spacecraft();
            x.setKey("62-"+ds.getKey());
            for (DataSnapshot dss :ds.getChildren()) {
                ArrayList<String> value = new ArrayList<>();
                for (DataSnapshot dsss:dss.getChildren()){
                    value.add(dsss.getValue(String.class));
                }
                x.addValue(value);
            }
            sp.add(x);
        }
    }


    private AlertDialog loadinPopUp(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("loading...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    private boolean loadDB(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("DB_file",null);
        Type type = new TypeToken<ArrayList<Spacecraft>>() {}.getType();
        lottaryArr = gson.fromJson(json,type);
        if(lottaryArr == null){
            Toast.makeText(context, "ไม่สามารถโหลดข้อมูลสำรองได้", Toast.LENGTH_SHORT).show();
            lottaryArr = new ArrayList<>();
            Spacecraft text = new Spacecraft();
            text.setKey("Error");
            lottaryArr.add(text);
            return false;
        }else{
            Toast.makeText(context, "LOAD ข้อมูล DB สำรอง", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void savedDB(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lottaryArr);
        editor.putString("DB_file",json);
        editor.apply();
        //Toast.makeText(context, "UPDATE FILE DB", Toast.LENGTH_SHORT).show();
    }

    private boolean load(Context context,ArrayList<String> arrStr,String key){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        arrStr = gson.fromJson(json,type);
        if(arrStr == null){
            Toast.makeText(context, "ไม่สามารถโหลดข้อมูลDateสำรองได้", Toast.LENGTH_SHORT).show();
            arrStr = new ArrayList<>();
            arrStr.add("Error");
            return false;
        }else{
            Toast.makeText(context, "LOAD ข้อมูล Date สำรอง", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void saved(Context context,ArrayList<String>arrStr,String key){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrStr);
        editor.putString(key,json);
        editor.apply();
        //Toast.makeText(context, "UPDATE FILE DATE", Toast.LENGTH_SHORT).show();
    }

}
