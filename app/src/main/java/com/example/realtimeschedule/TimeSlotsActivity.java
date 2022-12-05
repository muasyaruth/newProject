package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realtimeschedule.Adapter.SlotsViewHolder;
import com.example.realtimeschedule.Model.SlotsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeSlotsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView nav_name;

    ArrayList<String> slotTimes = new ArrayList<>();
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    BookAppointmentAdapter adapter;

    RecyclerView slotCards;
    CircleImageView img;
    TextView name, exp, degree, place;
    Button btn;
    String dr_name;
    EditText remarks;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

//    Spinner spinner;
//    List<String> timeslots;
//    private RecyclerView recyclerView;
//    DatabaseReference slotsRef;
//    RecyclerView.LayoutManager layoutManager;
//    FirebaseRecyclerOptions<SlotsModel> options;
//    FirebaseRecyclerAdapter<SlotsModel, BookAppointmentAdapter.ViewHolder.> SlotsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slots);


//        dr_name = getIntent().getExtras().getString("dr_app_name");
//        slotCards = findViewById(R.id.slotsRecycler);
//
//
//        mDatabase = FirebaseDatabase.getInstance();
//        mRef = mDatabase.getReference("Slots");
//
//
//        mListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                slotTimes.clear();
//                docTimings.clear();
//                docReserved.clear();
//                for (DataSnapshot slot: snapshot.getChildren()) {
//                    String stand = slot.getValue(String.class);
//
//                    slotTimes.add(slot.getKey());
//                    if (stand.equals("Available"))
//                        docTimings.add(slot.getKey());
//                    else if (stand.equals("Reserved"))
//                        docReserved.add(slot.getKey());
//                }
//                Collections.sort(slotTimes, new Comparator<String>() {
//                    @SuppressLint("SimpleDateFormat")
//                    @Override
//                    public int compare(String o1, String o2) {
//                        try {
//                            return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
//                        } catch (ParseException e) {
//                            return 0;
//                        }
//                    }
//                });
//                slotCards(slotTimes);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        mRef.child("Today").addValueEventListener(mListener);
//
//    }
//    private void slotCards(ArrayList<String> slotTimes) {
//        slotCards.setHasFixedSize(true);
//        slotCards.setLayoutManager(new LinearLayoutManager(this));
//
//        ArrayList<String> sectionList = new ArrayList<>();
//        sectionList.add("Today's Slots");
//        itemList.put(sectionList.get(0), slotTimes);
//        adapter = new BookAppointmentAdapter(this, sectionList, itemList, docTimings, docReserved, dr_name);
//        GridLayoutManager manager = new GridLayoutManager(this, 3);
//
//        slotCards.setLayoutManager(manager);
//        adapter.setLayoutManager(manager);
//        slotCards.setAdapter(adapter);
//
//    }

//        recyclerView= findViewById(R.id.slotsRecycler);
//        layoutManager= new GridLayoutManager(this, 4);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        //final DatabaseReference bookingsref = FirebaseDatabase.getInstance().getReference().child("Bookings");
//
//        timeslots= new ArrayList<>();
//
//        slotsRef= FirebaseDatabase.getInstance().getReference().child("Slots");
//        options= new FirebaseRecyclerOptions.Builder<SlotsModel>().
//                setQuery(slotsRef, SlotsModel.class).build();
//
//        SlotsAdapter= new FirebaseRecyclerAdapter<SlotsModel, SlotsViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull SlotsViewHolder holder,
//                                            int position, @NonNull SlotsModel model) {
//                holder.slot.setText(model.getSlot());
//
//            }
//
//            @NonNull
//            @Override
//            public SlotsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item,
//                        parent, false);
//                return new SlotsViewHolder(view);
//            }
//        };
//
//
//        SlotsAdapter.startListening();
//        recyclerView.setAdapter(SlotsAdapter);
//
    }

}