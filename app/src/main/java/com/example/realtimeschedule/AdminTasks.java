package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.os.Bundle;

import com.example.realtimeschedule.databinding.ActivityAdminTasksBinding;
import com.google.android.material.navigation.NavigationView;

public class AdminTasks extends MenuContainer {
    ActivityAdminTasksBinding activityAdminTasksBinding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminTasksBinding= ActivityAdminTasksBinding.inflate(getLayoutInflater());
        setContentView(activityAdminTasksBinding.getRoot());
    }
}