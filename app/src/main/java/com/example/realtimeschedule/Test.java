package com.example.realtimeschedule;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Test {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        System.out.println("=====================Time here"+ LocalDateTime.now());
    }
}
