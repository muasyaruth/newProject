package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class Scheduler {
    private String current, end, nextDay;

    public Scheduler() {
        // required default constructor
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getNextDay() {
        return nextDay;
    }

    public void setNextDay(String nextDay) {
        this.nextDay = nextDay;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("current", current);
        params.put("end", end);
        params.put("nextDay", nextDay);
        return params;
    }
}
