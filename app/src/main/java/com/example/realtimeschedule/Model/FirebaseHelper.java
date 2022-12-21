package com.example.realtimeschedule.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    DatabaseReference ref;
    public FirebaseHelper(){
        ref = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference child(String path){
        return this.ref.child(path);
    }

    // get next available time
    public String getNextAvailableTime(){
    return  null;
    }

    public void getBookedUntil(){
        this.child("slots").child("today").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AvailableTime availableTime = snapshot.getValue(AvailableTime.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
