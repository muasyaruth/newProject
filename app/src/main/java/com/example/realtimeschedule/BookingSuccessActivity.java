package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class BookingSuccessActivity extends AppCompatActivity  {
    Button okBtn;
    TextView tvBookingInfo, tvBookingDate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_success_activity);

        okBtn = findViewById(R.id.btnOk);
        tvBookingInfo = findViewById(R.id.tvBookingInfo);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        String bookingDate = getIntent().getStringExtra("bookingDate");
        tvBookingDate.setText("Booking Date: "+bookingDate);

        okBtn.setOnClickListener(view-> finish());

        tvBookingInfo.setOnClickListener(view -> {
            startActivity(new Intent(this, BookingDetails.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}