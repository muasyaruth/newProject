package com.example.realtimeschedule.Model;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realtimeschedule.BookingSuccessActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class BookingHelper {
    private Booking booking;
    private Context context;
    DatabaseReference bookingsRef, slotsRef;
    public BookingHelper(Context context){
        this.context = context;
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots").child("Today");
    }

public void book(Booking booking){
    try {
        // update to firebase
        bookingsRef.child(booking.getId()).setValue(booking.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(context, "Unable to reserve your booking. Please try again", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // update available time for bookings for subsequent bookings
                        updateAvailableTime();
                        // booking successful. Move to booking success activity
                        (context).startActivity(new Intent(context, BookingSuccessActivity.class));
                    }
                });

        // error parsing next available time
    } catch (Exception e) {
        Toast.makeText(context, "Error parsing date "+e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
}

    /**
     * Update available time so that next users will be scheduled after this time.
     *
     */
    public void updateAvailableTime(){
        try {
            slotsRef.child("bookedUntil").setValue(availableTime.getNextAvailableTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
