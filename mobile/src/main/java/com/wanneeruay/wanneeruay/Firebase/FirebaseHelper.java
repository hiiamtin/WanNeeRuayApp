package com.wanneeruay.wanneeruay.Firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    DatabaseReference db;
    Boolean save = null;
    ArrayList<String> spacecrafts = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    public DatabaseReference getDb() {
        return db;
    }

    public void setDb(DatabaseReference db) {
        this.db = db;
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
    public ArrayList<String> retrieve(){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return spacecrafts;
    }

    private void fetchData(DataSnapshot snapshot){
        spacecrafts.clear();
        for (DataSnapshot ds:snapshot.getChildren()) {
            spacecrafts.add(ds.getValue(String.class));
        }

    }
}
