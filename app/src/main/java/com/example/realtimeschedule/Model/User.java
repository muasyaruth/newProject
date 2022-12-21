package com.example.realtimeschedule.Model;

import java.util.Comparator;

public class User {

    private String uid,name, email, phone, password, image;
    private int priority = UserPriorities.PRIORITY_STUDENT;

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // get user type
    public String getUserType(){
        switch (priority){
            case 1:
                return "Student";
            case 2:
                return "School President";
            case 3:
                return "Lecturer";
            case 4:
                return "COD";
            case 5:
                return "Dean";
            case 6:
                return "Registrar";
            default:
                return "Unknown User type";
        }
    }
}
