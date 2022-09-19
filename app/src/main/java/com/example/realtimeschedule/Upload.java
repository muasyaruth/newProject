package com.example.realtimeschedule;
//contains name of service and image representing the service
public class Upload {
    //member variables
    private String sName;
    private String sImageUrl;

    //empty constructor for it to record to firebase database
    public Upload(){
        //empty constructor
    }

    //constructor
    public Upload(String name, String imageUrl){
        if (name.trim().equals("")){
            name="No name";
        }

        sName= name;
        sImageUrl= imageUrl;
    }
    //getters and setters

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsImageUrl() {
        return sImageUrl;
    }

    public void setsImageUrl(String sImageUrl) {
        this.sImageUrl = sImageUrl;
    }
}
