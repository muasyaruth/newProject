package com.example.realtimeschedule.Model;

public class Appointments {
    private String passedsname;
    private String passedpid;
    private String passeddate;
    private String time;
//    private String designation;

    public Appointments() {

    }

    @Override
    public String toString() {
        return "Appointments{" +
                "sname='" + passedsname + '\'' +
                ", pid='" + passedpid + '\'' +
                ", date='" + passeddate + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Appointments(String time) {
        this.time = time;
    }

    public Appointments(String sname, String pid, String date, String time) {
        this.passedsname = sname;
        this.passedpid = pid;
        this.passeddate = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassedsname() {
        return passedsname;
    }

    public void setPassedsname(String passedsname) {
        this.passedsname = passedsname;
        return;
    }

    public String getPassedpid() {
        return passedpid;
    }

    public void setPassedpid(String passedpid) {
        this.passedpid = passedpid;
    }

    public String getPasseddate() {
        return passeddate;
    }

    public void setPasseddate(String passeddate) {
        this.passeddate = passeddate;
    }
}
