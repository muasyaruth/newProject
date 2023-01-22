package com.example.realtimeschedule.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BookingComparator implements Comparator<Booking> {

    @Override
    public int compare(Booking booking, Booking t1) {
        if(booking.getPriority() != t1.getPriority()){
            return t1.getPriority() - booking.getPriority();
        }else{
            // if same priority, compare based on time
            return  booking.getParsedDate().compareTo(t1.getParsedDate());
        }

    }
}
