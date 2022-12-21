package com.example.realtimeschedule.Model;

import android.content.Context;

public class BookingHelper {
    private Booking booking;
    private Context context;
    public BookingHelper(Context context){
        this.context = context;
    }

public void book(Booking booking){
        this.booking = booking;
}
}
