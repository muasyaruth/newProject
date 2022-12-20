package com.example.realtimeschedule.Model;

import java.util.Comparator;

public class User {

    public String name, email, phone, password, uid;
    private String image;

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

    public User(String name, String image, String email, String phone, String password, String uid) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.uid = uid;
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
}
