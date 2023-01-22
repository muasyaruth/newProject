package com.example.realtimeschedule.Model;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realtimeschedule.Interface.OnHelperCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;

public class BookingHelper {
    private Booking booking;
    private Context context;
    OnHelperCompleteListener listener;
    DatabaseReference bookingsRef, slotsRef;
    private Scheduler scheduler;
    private PriorityQueue<Booking> bookings;
    public BookingHelper(Context context){
        this.context = context;
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bookings = new PriorityQueue<>(new BookingComparator());
        }
    }

    public BookingHelper book(Booking booking){
        // validate all required params
        if (scheduler == null){
            throw new RuntimeException("Scheduler must be specified in order to make a booking!");
        }

    try {
        // update to firebase
        bookingsRef.child(booking.getId()).setValue(booking.toMap())
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
//                        Toast.makeText(context, "Unable to reserve your booking. Please try again", Toast.LENGTH_LONG).show();
                        if (listener != null) listener.onError("Unable to reserve your booking. Please try again");
                        return;
                    }
                    // re-schedule bookings while updating scheduler
                    reScheduleBookings();
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
     * Synchronize scheduler to make sure time updated as required
     * and the scheduler skips to the next day once current day's time
     * is over
     */
    private void synchronizeScheduler(){
        // check if there is any time available this day
        if(scheduler.getBookedUntil().equals(scheduler.getEnd())){
            listener.onError("Day Fully Booked");
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
        listener.onError("Day not fully booked: "+scheduler.getBookedUntil()+" vs "+scheduler.getEnd());
    }

    /**
     * Add a listener when booking helper has finished executing
     */
    public void setOnCompleteListener(OnHelperCompleteListener listener){
        this.listener = listener;
    }

    /**
     * Get all bookings and re-schedule them based on priority, updating time for each
     */
    public void reScheduleBookings(){
        // get all bookings from database
        bookingsRef.addListenerForSingleValueEvent(bookingsListener);
    }

    /**
     * Single listener when getting bookings
     */
    public ValueEventListener bookingsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // add all bookings to a priority queue
            bookings.clear();
            listener.onError("Got new Booking info. Processing...");
            for (DataSnapshot ds: snapshot.getChildren()){
                Booking booking = ds.getValue(Booking.class);
                if(booking.isServed()) continue; // consider only bookings that are not served
                bookings.add(booking);
            }
            // Do not re-schedule user being served currently.
            Booking currentServing = bookings.poll();
            // reset scheduler pointer
            scheduler.setBookedUntil(currentServing.getDate());

            HashMap<String, Object> bookingsMap = new HashMap<>();
            Booking booking;
            while ((booking = bookings.poll()) != null){
                try {
                    booking.setDate(getNextTime());
                    scheduler.setBookedUntil(getNextTime());
                    bookingsMap.put(booking.getId(), booking);
                    // synchronize with offline scheduler
                    synchronizeScheduler();
                } catch (ParseException e) {
                    listener.onError("Error parsing booking time");
                }
            }

            // update bookings to firebase
            bookingsRef.updateChildren(bookingsMap);
            // update scheduler
            slotsRef.child("scheduler").setValue(scheduler.toMap());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            if (listener != null) listener.onError("Error getting booking information "+error.getMessage());
        }
    };

    // get next time the scheduler will
    private String getNextTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = sdf.parse(scheduler.getBookedUntil());
        calendar.setTime(currentDate);
        // next client to be served after 30 minutes
        calendar.add(Calendar.MINUTE, 30);
        return sdf.format(calendar.getTime());
    }
}
