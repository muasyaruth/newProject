package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.realtimeschedule.Model.Day_Time;
//import com.example.realtimeschedule.ViewHolder.Day_TimeViewHolder;
import com.example.realtimeschedule.ViewHolder.Day_TimeAdapter;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    RecyclerView daysCardsRecyclerView;
    Day_TimeAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Day_Time> daysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initData();
        initRecyclerView();
    }

    private void initData() {
        daysList= new ArrayList<>();
        daysList.add(new Day_Time("Monday",0));
        daysList.add(new Day_Time("Tuesday", 1));
        daysList.add(new Day_Time("Wednesday", 2));
        daysList.add(new Day_Time("Thursday", 3));
        daysList.add(new Day_Time("Friday", 4));
        // since it is a school setup, only working days are allowed.
    }

    private void initRecyclerView() {
        daysCardsRecyclerView = findViewById(R.id.recyclerDays);

        linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        daysCardsRecyclerView.setLayoutManager(linearLayoutManager);
        adapter= new Day_TimeAdapter(daysList);
        daysCardsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}