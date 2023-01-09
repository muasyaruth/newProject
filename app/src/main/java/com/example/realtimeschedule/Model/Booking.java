package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class Booking  {
    private String id, date;
    private int priority = 1;
    private boolean served = false;
    private User user;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * User object will no be stored in firebase but just
     * to create the relationship between a user and a booking
     * @return
     */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String , Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("priority", priority);
        params.put("date", date);
        params.put("served", served);
        return params;
    }
}
