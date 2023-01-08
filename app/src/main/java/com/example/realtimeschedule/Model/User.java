package com.example.realtimeschedule.Model;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid, username, email, phone, image;
    private int priority = UserPriorities.PRIORITY_STUDENT;
    private boolean isAdmin = false;

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("username", username);
        params.put("email", email);
        params.put("phone", phone);
        params.put("image", image);
        params.put("isAdmin", isAdmin);
        return  params;
    }
}
