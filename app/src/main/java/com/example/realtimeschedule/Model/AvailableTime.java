package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class AvailableTime {
    private String day, from, to, bookedUntil;

    public AvailableTime(){
        // required constructor
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public void setBookedUntil(String bookedUntil) {
        this.bookedUntil = bookedUntil;
    }

    public String getBookedUntil() {
        return bookedUntil;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("day", day);
        params.put("from", from);
        params.put("to", to);
        params.put("bookedUntil", bookedUntil);
        return params;
    }


}
