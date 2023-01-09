package com.example.realtimeschedule.Model;

import com.google.android.gms.tasks.OnCompleteListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AvailableTime {
    private String from, to;

    public AvailableTime() {
        // required constructor
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

    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        return params;
    }
}
