package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Model.AvailableTime;
import com.example.realtimeschedule.Model.Scheduler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class GiveDayAndTimeAvailable extends AppCompatActivity {

    TextView timer1, timer2, tvHeader;
    Button btn;

    int t1Hr, t1Min, t2Hr, t2Min;
    private DatabaseReference slotsRef;
    public static String from, to;
    private int dayIndex; // zero indexed based value of day of week from 0-6
    private static final String TAG = "GiveDayAndTime";
    private AvailableTime availableTime;
    ProgressDialog loader;
    private boolean schedulerInitialized = false;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_day_and_time_available);

        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");
        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        tvHeader = findViewById(R.id.tvHeader);
        btn = findViewById(R.id.monday);
        loader = new ProgressDialog(this);

        from=to = "";

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
                from = availableTime.getFrom();
                to = availableTime.getTo();
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
            if(from.isEmpty() || to.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please select valid Time Slot", Toast.LENGTH_SHORT).show();
                return;
            }

            // proceed
            AvailableTime availableTime = new AvailableTime();
            availableTime.setFrom(from);
            availableTime.setTo(to);
            // first initialize scheduler if it had no been initialized
            if(!schedulerInitialized){
                Scheduler scheduler = new Scheduler();
                scheduler.setCurrent(availableTime.getFrom());
                scheduler.setEnd(availableTime.getTo());
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