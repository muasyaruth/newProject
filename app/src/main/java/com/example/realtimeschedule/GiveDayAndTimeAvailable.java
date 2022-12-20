package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.realtimeschedule.Model.AvailableTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GiveDayAndTimeAvailable extends AppCompatActivity {


    TextView timer1, timer2;
    Button btn;

    int t1Hr, t1Min, t2Hr, t2Min;
    private DatabaseReference ref;
    public static String from, to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_day_and_time_available);

        ref = FirebaseDatabase.getInstance().getReference("slots").child("today");
        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        btn = findViewById(R.id.monday);

        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(GiveDayAndTimeAvailable.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hr = hourOfDay;
                                t1Min = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1Hr, t1Min);

                                from = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();
                                timer1.setText(from);
                            }
                        }, t1Hr, t1Min, false);
                timePickerDialog.updateTime(t1Hr, t1Min);
                timePickerDialog.show();
            }
        });

        timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(GiveDayAndTimeAvailable.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t2Hr = hourOfDay;
                                t2Min = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t2Hr, t2Min);

                                to = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();

                                timer2.setText(to);
                            }
                        }, t2Hr, t2Min, false);

                timePickerDialog.updateTime(t2Hr, t2Min);
                timePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                // validate correct time selected
                if(from.isEmpty() || to.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select valid Time slot", Toast.LENGTH_SHORT).show();
                    return;
                }

                // proceed
                AvailableTime availableTime = new AvailableTime();
                availableTime.setDay("today");
                availableTime.setFrom(from);
                availableTime.setTo(to);
                availableTime.setBookedUntil(from); // no bookings as for now

                ref.setValue(availableTime.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GiveDayAndTimeAvailable.this, "Available time updated successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}