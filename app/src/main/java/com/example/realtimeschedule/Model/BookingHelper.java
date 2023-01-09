package com.example.realtimeschedule.Model;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realtimeschedule.BookingSuccessActivity;
import com.example.realtimeschedule.Interface.OnHelperCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable;

public class BookingHelper {
    private Booking booking;
    private Context context;
    OnHelperCompleteListener listener;
    DatabaseReference bookingsRef, slotsRef;
    private Scheduler scheduler;
    public BookingHelper(Context context){
        this.context = context;
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");
        scheduler = new Scheduler();
    }

    public BookingHelper book(Booking booking){
    try {
        // update to firebase
        bookingsRef.child(booking.getId()).setValue(booking.toMap())
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
//                        Toast.makeText(context, "Unable to reserve your booking. Please try again", Toast.LENGTH_LONG).show();
                        if (listener != null) listener.onError("Unable to reserve your booking. Please try again");
                        return;
                    }
                    // update scheduler
                    updateScheduler();
                    // booking successful. Move to booking success activity
                    if (listener != null ) listener.onSuccess("Booking success");
//
                });

        // error parsing next available time
    } catch (Exception e) {
        Toast.makeText(context, "Error parsing date "+e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    return this;
}

    /**
     * Set scheduler to use for this booking
     */
    public BookingHelper setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
        return this;
    }

    /**
     * Update scheduler information for subsequent bookings
     */
    private void updateScheduler() {
        // check if there is any time available this day
        if(scheduler.getCurrent().equals(scheduler.getEnd())){
            // update scheduler to use the following day
            // check also if the week has ended and start a fresh week
            if(scheduler.getDayOfWeek() >= 5){
                // Office is closed on weekends. Start a new week
                scheduler.setDayOfWeek(0);
            }else{
                // not a weekend. Update the next day
                scheduler.setDayOfWeek(scheduler.getDayOfWeek()+1);
            }
        }
        // update the scheduler's time for the next client
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            Date currentDate = sdf.parse(scheduler.getCurrent());
            calendar.setTime(currentDate);
            // next client to be served after 30 minutes
            calendar.add(Calendar.MINUTE, 30);

            scheduler.setCurrent(sdf.format(calendar.getTime()));
            Log.d("Scheduler", scheduler.toMap().toString());
            slotsRef.child("scheduler").setValue(scheduler.toMap());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a listener when booking helper has finished executing
     */
    public void setOnCompleteListener(OnHelperCompleteListener listener){
        this.listener = listener;
    }
}
