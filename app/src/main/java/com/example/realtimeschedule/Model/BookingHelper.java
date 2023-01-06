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

public class BookingHelper {
    private Booking booking;
    private Context context;
    OnHelperCompleteListener listener;
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
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
//                        Toast.makeText(context, "Unable to reserve your booking. Please try again", Toast.LENGTH_LONG).show();
                        if (listener != null) listener.onError("Unable to reserve your booking. Please try again");
                        return;
                    }
                    // update available time for bookings for subsequent bookings
                    updateAvailableTime(booking.getDate());
                    // booking successful. Move to booking success activity
                    if (listener != null ) listener.onSuccess("Booking success");
//                    (context).startActivity(new Intent(context, BookingSuccessActivity.class));
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
    private void updateAvailableTime(String currentBookingDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            Date currentDate = sdf.parse(currentBookingDate);
            calendar.setTime(currentDate);
            // next client to be served after 30 minutes
            calendar.add(Calendar.MINUTE, 30);

            slotsRef.child("bookedUntil").setValue(sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            Log.d("BookedUntil", "Error formatting booking date. Error: "+e.getMessage());
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
