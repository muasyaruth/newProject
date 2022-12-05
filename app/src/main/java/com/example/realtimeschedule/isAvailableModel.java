package com.example.realtimeschedule;

public class isAvailableModel {
    public String morning, afternoonn, days;

    public isAvailableModel(String morning, String afternoonn, String days) {
        this.morning = morning;
        this.afternoonn = afternoonn;
        this.days = days;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoonn;
    }

    public void setAfternoon(String afternoon) {
        this.afternoonn = afternoon;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
