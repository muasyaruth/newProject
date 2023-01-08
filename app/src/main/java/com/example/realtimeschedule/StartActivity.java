package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {
    CardView user, vc, secretary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        user = findViewById(R.id.vc);
        vc = findViewById(R.id.user);

        user.setOnClickListener(View -> {
            Intent intent = new Intent(StartActivity.this, UserLoginActivity.class);
            startActivity(intent);
        });
        vc.setOnClickListener(View -> {
            Intent intent = new Intent(StartActivity.this, VCLogin.class);
            startActivity(intent);
        });
    }
}