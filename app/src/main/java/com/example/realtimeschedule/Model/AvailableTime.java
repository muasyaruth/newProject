package com.example.realtimeschedule.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public String getNextAvailableTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = sdf.parse(this.getBookedUntil());
        calendar.setTime(currentDate);
        updateNextAvailableTime(calendar);
        return sdf.format(calendar.getTime());
    }

    private void updateNextAvailableTime(Calendar calendar) throws ParseException {
        // next user should be served after 20 minutes
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        calendar.add(Calendar.MINUTE, 20);
        this.setBookedUntil(sdf.format(calendar.getTime()));
    }
}
