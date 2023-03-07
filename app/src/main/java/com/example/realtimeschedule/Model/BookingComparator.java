package com.example.realtimeschedule.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BookingComparator implements Comparator<Booking> {

    @Override
    public int compare(Booking booking, Booking t1) {
        return t1.getPriority() - booking.getPriority();
    }
}
