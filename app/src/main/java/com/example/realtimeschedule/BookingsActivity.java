package com.example.realtimeschedule;


import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realtimeschedule.Model.Bookings;
import com.example.realtimeschedule.ViewHolder.BookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

//How To Sort My Recyclerview According To Date And Time With Examples
public class BookingsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);



        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        //Query db = FirebaseDatabase.getInstance().getReference().child("Email").child(mCurrentUser.getUid()).child("Appointment").orderByChild("date");


    }

    private void DateTimePair() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference bookingsref = FirebaseDatabase.getInstance().getReference().child("Bookings");

        FirebaseRecyclerOptions<Bookings> options = new FirebaseRecyclerOptions.Builder<Bookings>()
                .setQuery(bookingsref, Bookings.class)
                .build();

        FirebaseRecyclerAdapter<Bookings, BookingsViewHolder> adapter = new FirebaseRecyclerAdapter
                <Bookings, BookingsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookingsViewHolder bookingsViewHolder,
                                            int i, @NonNull Bookings bookings) {

                Bookings item= getItem(i);

                bookingsViewHolder.clientName.setText(bookings.getSname());
                bookingsViewHolder.clientEmail.setText(bookings.getSemail());
                bookingsViewHolder.designation.setText(bookings.getDesignation());
                bookingsViewHolder.appointmentDate.setText(bookings.getDate());
                bookingsViewHolder.appointmentTime.setText(bookings.getTime());
                bookingsViewHolder.designation.setText(bookings.getDesignation());
                Picasso.get().load(bookings.getImage()).into(bookingsViewHolder.imageView);

                bookingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(BookingsActivity.this, BookingDetails.class);
                        intent.putExtra("Name", item. getSname());
                        intent.putExtra("Image", item. getImage());
                        intent.putExtra("Email", item. getSemail());
                        intent.putExtra("Date", item. getDate());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.booking_items_layout, parent, false);
                BookingsViewHolder holder = new BookingsViewHolder(view);
                return holder;


            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }


}