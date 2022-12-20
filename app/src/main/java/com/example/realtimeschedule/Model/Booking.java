package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class Booking  {
    String id, date;
    boolean served = false;

    public Booking() {
        // required public constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }


    public Map<String , Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("date", date);
        params.put("served", served);
        return params;
    }
}
