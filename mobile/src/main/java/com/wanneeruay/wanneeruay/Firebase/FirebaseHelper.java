package com.wanneeruay.wanneeruay.Firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {
    DatabaseReference db;
    Boolean save = null;

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //SAVE
    public Boolean save(Spacecraft spacecraft){
        if(spacecraft==null){
            save = false;
        }else{
            try {
                db.child("lottary_date").setValue(spacecraft);
                save=true;
            }catch (DatabaseException e){
                e.printStackTrace();
                save=false;
            }
        }
        return save;
    }

    //READ
    public ArrayList<String> retrieve(){
        final ArrayList<String> spacecrafts = new ArrayList<>();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return spacecrafts;
    }

    private void fetchData(DataSnapshot snapshot,ArrayList<String> spacecrafts){
        spacecrafts.clear();
        for (DataSnapshot ds:snapshot.getChildren()) {
            String name = ds.getValue(Spacecraft.class).getValue();
            spacecrafts.add(name);
        }
    }
}
