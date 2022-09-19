package com.example.realtimeschedule.Model;

public class Services {

    private String semail;
    private String sname;
    private String image;
    private String pid;
    private String date;
    private String time;

    public Services() {
    }

    public Services(String semail, String sname, String image, String pid, String date, String time) {
        this.semail = semail;
        this.sname = sname;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Services{" +
                "semail='" + semail + '\'' +
                ", sname='" + sname + '\'' +
                ", image='" + image + '\'' +
                ", pid='" + pid + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
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
}
