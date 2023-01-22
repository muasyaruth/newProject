package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Model.AvailableTime;
import com.example.realtimeschedule.Model.Scheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GiveDayAndTimeAvailable extends AppCompatActivity {

    TextView timer1, timer2, tvHeader, tvDate;
    Button btn, btnSelectDate;

    int t1Hr, t1Min, t2Hr, t2Min;
    private DatabaseReference slotsRef;
    public static String from, to;
    private int dayIndex; // zero indexed based value of day of week from 0-6
    private static final String TAG = "GiveDayAndTime";
    private AvailableTime availableTime;
    ProgressDialog loader;
    private boolean schedulerInitialized = false;
    String day, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_day_and_time_available);

        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");
        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        tvHeader = findViewById(R.id.tvHeader);
        btn = findViewById(R.id.monday);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvDate = findViewById(R.id.tvDate);

        loader = new ProgressDialog(this);
        date=from=to = "";

        // get the day index of week selected
        dayIndex = getIntent().getIntExtra("dayIndex", 0);
        day = getIntent().getStringExtra("day");

        // update title
        tvHeader.setText("Edit VC Schedule for "+day);

        loader.setMessage("Getting saved time... ");
        loader.show();
        // get previously saved time from database
        slotsRef.child(String.valueOf(dayIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loader.dismiss();
                if(!snapshot.exists()){
                    Log.d(TAG, "No time has been set for day index "+dayIndex);
                    return;
                }
                availableTime = snapshot.getValue(AvailableTime.class);
                String onlineDate = availableTime.getDate();
                date = onlineDate == null ? "" : onlineDate;
                from = availableTime.getFrom();
                to = availableTime.getTo();
                if (date.isEmpty()) tvDate.setText(date);
                timer1.setText(from);
                timer2.setText(to);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.dismiss();
                Toast.makeText(GiveDayAndTimeAvailable.this, "Error getting saved time "+error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
        // check if scheduler has been initialized
        slotsRef.child("scheduler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    schedulerInitialized = false;
                    return;
                }
                schedulerInitialized = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get date
        btnSelectDate.setOnClickListener(view -> {
            // get current date
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay;
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH+1);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(GiveDayAndTimeAvailable.this, (datePicker, year, month, dayOfMonth) -> {
                date = (dayOfMonth<10? "0" : "")+dayOfMonth+"/"+(month+1<10 ? "0": "")+(month+1)+"/"+year;
                tvDate.setText(date);
            }, mYear, mMonth, mDay);
            // show dialog

            datePickerDialog.show();
        });

        // get from time
        timer1.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(GiveDayAndTimeAvailable.this,
                    (view, hourOfDay, minute) -> {
                        t1Hr = hourOfDay;
                        t1Min = minute;

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hr, t1Min);

                        from = android.text.format.DateFormat.format(
                                "hh:mm aa", calendar).toString();
                        timer1.setText(from);
                    }, t1Hr, t1Min, false);
            timePickerDialog.updateTime(t1Hr, t1Min);
            timePickerDialog.show();
        });
        // get to time
        timer2.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(GiveDayAndTimeAvailable.this,
                    (view, hourOfDay, minute) -> {
                        t2Hr = hourOfDay;
                        t2Min = minute;

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t2Hr, t2Min);

                        to = android.text.format.DateFormat.format(
                                "hh:mm aa", calendar).toString();

                        timer2.setText(to);
                    }, t2Hr, t2Min, false);

            timePickerDialog.updateTime(t2Hr, t2Min);
            timePickerDialog.show();
        });
        // save to db
        btn.setOnClickListener(v -> {
            loader.setMessage("Setting time...");
            loader.show();
            // validate correct time selected
            if(date.isEmpty() || from.isEmpty() || to.isEmpty()){
                loader.dismiss();
                Toast.makeText(getApplicationContext(), "Please select valid Time Slot", Toast.LENGTH_SHORT).show();
                return;
            }
            // proceed
            AvailableTime availableTime = new AvailableTime();
            availableTime.setDate(date);
            availableTime.setFrom(from);
            availableTime.setTo(to);
            // first initialize scheduler if it had no been initialized
            if(!schedulerInitialized){
                Scheduler scheduler = new Scheduler();
                scheduler.setBookedUntil(availableTime.getDate()+" "+availableTime.getFrom());
                scheduler.setEnd(availableTime.getDate()+" "+availableTime.getTo());
                scheduler.setDayOfWeek(dayIndex);
                slotsRef.child("scheduler").setValue(scheduler.toMap());
            }

            slotsRef.child(String.valueOf(dayIndex)).setValue(availableTime.toMap()).addOnCompleteListener(task -> {
                loader.dismiss();
                Toast.makeText(GiveDayAndTimeAvailable.this, "Available time updated successfully", Toast.LENGTH_SHORT).show();
            });

        });

    }
}