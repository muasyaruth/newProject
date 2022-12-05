package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.bumptech.glide.load.model.Model;
import com.example.realtimeschedule.Model.Day_Time;
//import com.example.realtimeschedule.ViewHolder.Day_TimeViewHolder;
import com.example.realtimeschedule.ViewHolder.Day_TimeAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    RecyclerView daysCards;
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
        daysList.add(new Day_Time("Monday"));
        daysList.add(new Day_Time("Tuesday"));
        daysList.add(new Day_Time("Wednesday"));
        daysList.add(new Day_Time("Thursday"));
        daysList.add(new Day_Time("Friday"));
        daysList.add(new Day_Time("Saturday"));
        daysList.add(new Day_Time("Sunday"));
    }

    private void initRecyclerView() {
        daysCards = findViewById(R.id.recyclerDays);

        linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        daysCards.setLayoutManager(linearLayoutManager);
        adapter= new Day_TimeAdapter(daysList);
        daysCards.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}