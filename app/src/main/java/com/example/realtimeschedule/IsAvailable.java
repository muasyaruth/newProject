package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.bumptech.glide.load.model.Model;
import com.example.realtimeschedule.Model.Day_Time;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class IsAvailable extends AppCompatActivity{

    Spinner days;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference ref1;
    Button btn;

    TextInputLayout morning,afternoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_available);
//
//        morning = findViewById(R.id.mng);
//        afternoon = findViewById(R.id.noon);
//        days = findViewById(R.id.spinner);
//        btn = findViewById(R.id.confirm);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        days.setOnItemSelectedListener(this);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String morniing = morning.getEditText().getText().toString();
//                String afternon = afternoon.getEditText().getText().toString();
//
//                isAvailableModel isAvailableModel= new isAvailableModel(morniing, afternon);
//            }
//        });
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
    }
}