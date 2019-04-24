package com.wanneeruay.wanneeruay.Firebase;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    DatabaseReference db,db2;
    Boolean save = null;
    ArrayList<String> spacecrafts = new ArrayList<>();
    ArrayList<String> spacecrafts2 = new ArrayList<>();
    FirebaseDatabase FB;

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
    public ArrayList<String> updateLottaryDate(){
        db = FB.getReference("lottary_date");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                spacecrafts.clear();
            }
        });
        return spacecrafts;
    }

    public ArrayList<String> updateLottaryDB(){
        db2 = FB.getReference("lottary_db").child("TYPE");
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot,spacecrafts2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                spacecrafts2.clear();
            }
        });
        return spacecrafts2;
    }

    private void fetchData(DataSnapshot snapshot,ArrayList<String> sp){
        sp.clear();
        for (DataSnapshot ds:snapshot.getChildren()) {
            sp.add(ds.getValue(String.class));
        }

    }
}
