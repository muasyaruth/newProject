package com.example.realtimeschedule.Model;

import java.util.Comparator;

public class BookingComparator implements Comparator<Booking> {

    @Override
    public int compare(Booking booking, Booking t1) {
        return booking.getPriority() - t1.getPriority();
    }
}
