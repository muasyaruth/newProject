package com.example.realtimeschedule.Model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realtimeschedule.BookingsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    private ArrayList<User> users;
    private DatabaseReference usersRef;
    Context context;

    public FirebaseHelper(Context context){
        users = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    /**
     * Get all registered users
     */
    public ArrayList<User> getUsers(){
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return users;
    }

}
