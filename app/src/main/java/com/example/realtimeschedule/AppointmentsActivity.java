package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realtimeschedule.Model.Appointments;
import com.example.realtimeschedule.ViewHolder.AppointmentsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String newDate;
    String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        recyclerView = findViewById(R.id.appointmentsRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        SharedPreferences sharedPreference;
        sharedPreference = getSharedPreferences("user",MODE_PRIVATE);
        newName = sharedPreference.getString("NameOfClient", null);
        newDate = sharedPreference.getString("DateOfAppointment", null);



    }
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference appointmentsref = FirebaseDatabase.getInstance().getReference().child("Appointments");

        FirebaseRecyclerOptions<Appointments> options = new FirebaseRecyclerOptions.Builder<Appointments>()
                .setQuery(appointmentsref,Appointments.class)
                .build();

        FirebaseRecyclerAdapter<Appointments, AppointmentsViewHolder> adapter = new FirebaseRecyclerAdapter
                <Appointments, AppointmentsViewHolder>(options) {
            @NonNull
            @Override
            public AppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.appointment_item, parent, false);
                AppointmentsViewHolder appointmentsViewHolder= new AppointmentsViewHolder(view);
                return appointmentsViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull AppointmentsViewHolder holder, int position,
                                            @NonNull Appointments model) {

                Appointments appointments= getItem(position);


                holder.clientFinalName.setText(model.getPassedsname());
                holder.appointmentFinalDate.setText(model.getPasseddate());
                holder.appointmentTime.setText(model.getTime());
//                holder.clientName.setText(model.getDesignation());
                Intent i=getIntent();
                newName= i.getStringExtra("NameOfClient");
                newDate= i.getStringExtra("DateOfAppointment");


                holder.clientFinalName.setText(newName);
                holder.appointmentFinalDate.setText(newDate);

            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}