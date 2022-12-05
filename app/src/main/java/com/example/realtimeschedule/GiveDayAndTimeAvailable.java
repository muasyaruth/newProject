package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    SimpleDateFormat sdf;
    private DatabaseReference ref;
    public static String UserName;
    String t1, t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_day_and_time_available);

        ref = FirebaseDatabase.getInstance().getReference("Slots").child("Today");

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

                                String dateAndroid = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();

                                timer1.setText(dateAndroid);
                            }
                        }, 12, 0, false);
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

                                String dateAndroid = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();

                                timer2.setText(dateAndroid);
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(t2Hr, t2Min);
                timePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                int min = findMinutes();

                Calendar calendar = Calendar.getInstance();
                ArrayList<String> results = new ArrayList<>();
                sdf = new SimpleDateFormat("hh:mm aaa");

                calendar.set(0, 0, 0, t1Hr, t1Min);

                for (int i = 0; i < min; i++) {
                    String day1 = sdf.format(calendar.getTime());

                    // add 15 minutes to the current time; the hour adjusts automatically!
                    calendar.add(Calendar.MINUTE, 15);
                    String day2 = sdf.format(calendar.getTime());

                    //String day = day1 + " - " + day2;
                    results.add(day1);
                }

                if (results.size() != 0) {
                    ref.setValue(null);
                    for (String slot : results) {
                        ref.child(slot).setValue("Available");
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        MainActivity.redirectActivity(GiveDayAndTimeAvailable.this, MainActivity.class);
                        finish();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please select valid Time slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private int findMinutes() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(0, 0, 0, t1Hr, t1Min);
        String d1 = android.text.format.DateFormat.format(
                "kk:mm", calendar).toString();

        calendar.set(0, 0, 0, t2Hr, t2Min);
        String d2 = android.text.format.DateFormat.format(
                "kk:mm", calendar).toString();

        sdf = new SimpleDateFormat("hh:mm");

        Date date1 = null;
        try {
            date1 = sdf.parse(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = sdf.parse(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - date1.getTime();
        int min = (int) (diff / (1000 * 60));
        min = min / 120;
        return (min);
    }
}