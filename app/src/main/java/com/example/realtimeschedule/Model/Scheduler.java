package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of the current available time a user can make a booking.
 * Once a booking is made, the scheduler advances to the next stage.
 * Once available slots in a day are completely booked, the scheduler advances to the following day
 * Once a higher priority booking is made, the scheduler pointer is reset and booking re-scheduled again
 *
 */
public class Scheduler {
    private String bookedUntil, currentServing, end;
    int dayOfWeek = 0;

    public Scheduler() {
        // required default constructor
    }

    public String getBookedUntil() {
        return bookedUntil;
    }

    /**
     * The current time the scheduler is in. i.e. The upper limit of the booked time.
     * @param bookedUntil
     */
    public void setBookedUntil(String bookedUntil) {
        this.bookedUntil = bookedUntil;
    }

    /**
     *
     * @return String The time VC will be available until
     */
    public String getEnd() {
        return end;
    }

    /**
     * Set the time the time the VC will be available until
     * @param end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Working days ordered in zero-based indexing
     * 0 - Mon
     * 1- Tue
     * 2- Wed
     * 3- Thur
     * 4- Fri
     * 5- Sat
     * 6- Sun
     * @return
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     *
     * @return String - A pointer pointing the time the current user is being served.
     */
    public String getCurrentServing() {
        return currentServing;
    }

    /**
     * Set the time the current user is being served
     * @param currentServing the time the current user is being served
     */
    public void setCurrentServing(String currentServing) {
        this.currentServing = currentServing;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("bookedUntil", bookedUntil);
        params.put("currentServing", currentServing);
        params.put("end", end);
        params.put("dayOfWeek", dayOfWeek);
        return params;
    }
}
