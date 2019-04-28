package com.wanneeruay.wanneeruay.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference db,db2;
    Boolean save = null;
    ArrayList<String> spacecrafts;
    ArrayList<Spacecraft> spacecraftDB = new ArrayList<>();
    FirebaseDatabase FB;
    boolean status=false;

    public FirebaseHelper(FirebaseDatabase FB) {
        this.FB = FB;
    }


    //SAVE
    public Boolean save(Spacecraft spacecraft){
        if(spacecraft==null){
            save = false;
        }else{
            try {
                db.child(spacecraft.getKey()).setValue(spacecraft.value);
                save=true;
            }catch (DatabaseException e){
                e.printStackTrace();
                save=false;
            }
        }
        return save;
    }

    //READ
    public ArrayList<String> updateLottaryDate(Context context){
        db = FB.getReference("lottary_date");
        spacecrafts = new ArrayList<>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot,spacecrafts);
                if(spacecrafts.isEmpty()) {
                    Toast.makeText(context, "ไม่สามารถอัพเดตDateได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    loadDate(context);
                }else{
                    savedDate(context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                spacecrafts=null;
            }
        });
        return spacecrafts;
    }

    public ArrayList<Spacecraft> updateLottaryDB(Context context){
        db2 = FB.getReference("lottary_db");
        status = false;
        AlertDialog al = loadinPopUp(context);
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData2(dataSnapshot,spacecraftDB);
                status = true;
                al.cancel();
                if(spacecraftDB.isEmpty()){
                    Toast.makeText(context, "ไม่สามารถอัพเดตฐานข้อมูลได้\nโปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
                    loadDB(context);
                }else{
                    Toast.makeText(context,"loaded",Toast.LENGTH_SHORT).show();
                    savedDB(context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                spacecraftDB.clear();
                status=false;
                al.cancel();
                Toast.makeText(context,"can't loaded",Toast.LENGTH_SHORT).show();
            }
        });
        return spacecraftDB;
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

    public boolean isStatus() {
        return status;
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

    private void loadDB(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("DB_file",null);
        Type type = new TypeToken<ArrayList<Spacecraft>>() {}.getType();
        spacecraftDB = gson.fromJson(json,type);
        if(spacecraftDB == null){
            Toast.makeText(context, "ไม่สามารถโหลดข้อมูลสำรองได้", Toast.LENGTH_SHORT).show();
            spacecraftDB = new ArrayList<>();
            Spacecraft text = new Spacecraft();
            text.setKey("Error");
            spacecraftDB.add(text);
        }else{
            Toast.makeText(context, "LOAD ข้อมูล DB สำรอง", Toast.LENGTH_SHORT).show();
        }
    }

    private void savedDB(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(spacecraftDB);
        editor.putString("DB_file",json);
        editor.apply();
        Toast.makeText(context, "UPDATE FILE DB", Toast.LENGTH_SHORT).show();
    }

    private void loadDate(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("DB_file",null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        spacecrafts = gson.fromJson(json,type);
        if(spacecrafts == null){
            Toast.makeText(context, "ไม่สามารถโหลดข้อมูลDateสำรองได้", Toast.LENGTH_SHORT).show();
            spacecrafts = new ArrayList<>();
            spacecrafts.add("Error");
        }else{
            Toast.makeText(context, "LOAD ข้อมูล Date สำรอง", Toast.LENGTH_SHORT).show();
        }
    }

    private void savedDate(Context context){
        SharedPreferences sp = context.getSharedPreferences("lottary_db", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(spacecrafts);
        editor.putString("DB_file",json);
        editor.apply();
        Toast.makeText(context, "UPDATE FILE DATE", Toast.LENGTH_SHORT).show();
    }

}
