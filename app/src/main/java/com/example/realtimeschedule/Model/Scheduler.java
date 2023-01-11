package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class Scheduler {
    private String current, end;
    int dayOfWeek = 0;

    public Scheduler() {
        // required default constructor
    }

    public String getCurrent() {
        return current;
    }

    /**
     * The current time the scheduler is in
     * @param current
     */
    public void setCurrent(String current) {
        this.current = current;
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

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("current", current);
        params.put("end", end);
        params.put("dayOfWeek", dayOfWeek);
        return params;
    }
}
