package com.example.realtimeschedule.Model;

public class Day_Time {
    private String title;
    private int dayIndex;

    public Day_Time(String title, int dayIndex){
        this.title= title;
        this.dayIndex = dayIndex;
    }

    public String getTitle() {
        return title;
    }

    public int getDayIndex(){
        return this.dayIndex;
    }
}
