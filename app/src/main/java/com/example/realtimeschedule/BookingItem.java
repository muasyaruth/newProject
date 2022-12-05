package com.example.realtimeschedule;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingItem implements Parcelable {
    private int bImage;
    private String bName;
    private String bEmail;
    private String bDate;
    private String bDesignation;
    private String bReason;

    public BookingItem(int image, String name, String email,
                       String date, String designation, String reason){
        bImage= image;
        bEmail= email;
        bName= name;
        bDate= date;
        bDesignation= designation;
        bReason= reason;
    }

    protected BookingItem(Parcel in) {
        bImage = in.readInt();
        bName = in.readString();
        bEmail = in.readString();
        bDate = in.readString();
        bDesignation = in.readString();
        bReason = in.readString();
    }

    public static final Creator<BookingItem> CREATOR = new Creator<BookingItem>() {
        @Override
        public BookingItem createFromParcel(Parcel in) {
            return new BookingItem(in);
        }

        @Override
        public BookingItem[] newArray(int size) {
            return new BookingItem[size];
        }
    };

    public int getbImage() {
        return bImage;
    }

    public String getbName() {
        return bName;
    }

    public String getbEmail() {
        return bEmail;
    }

    public String getbDate() {
        return bDate;
    }

    public String getbDesignation() {
        return bDesignation;
    }

    public String getbReason() {
        return bReason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bImage);
        parcel.writeString(bName);
        parcel.writeString(bEmail);
        parcel.writeString(bDate);
        parcel.writeString(bDesignation);
        parcel.writeString(bReason);
    }
}
