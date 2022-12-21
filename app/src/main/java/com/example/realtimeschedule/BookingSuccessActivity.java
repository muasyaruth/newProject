package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

//import com.example.realtimeschedule.ViewHolder.Day_TimeViewHolder;


public class BookingSuccessActivity extends AppCompatActivity  {
    Button okBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_success_activity);

        okBtn = findViewById(R.id.btnOk);
        okBtn.setOnClickListener(view->{
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}