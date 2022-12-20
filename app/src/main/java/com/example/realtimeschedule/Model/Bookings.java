package com.example.realtimeschedule.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Bookings {

    private String semail;
    private String sname;
    private String image;
    private String pid;
    private String date;
    private String time;
    private String currentDateTime;
    private String designation;

    public Bookings() {
    }


    public Bookings(String semail, String sname, String image, String pid, String date, String time, String currentDateTime, String designation) {
        this.semail = semail;
        this.sname = sname;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.currentDateTime = currentDateTime;
        this.designation = designation;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
