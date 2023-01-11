package com.example.realtimeschedule;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimeschedule.Adapter.BookingsAdapter;
import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.BookingComparator;
import com.example.realtimeschedule.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.PriorityQueue;

//How To Sort My Recyclerview According To Date And Time With Examples
public class BookingsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference bookingsRef, usersRef;
    FirebaseUser firebaseUser;
    User user;
    ProgressDialog loader;
    ArrayList<User> users;
    PriorityQueue<Booking> bookings;
    ArrayList<Booking> bookingsArr;
    BookingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        loader = new ProgressDialog(this);
        users = new ArrayList<>();
        bookingsArr = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bookings = new PriorityQueue<>(new BookingComparator());
        }

        bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new BookingsAdapter(this, bookingsArr);
        recyclerView.setAdapter(adapter);

        loader.setMessage("Checking users information...");
        loader.show();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(BookingsActivity.this, "No Users in the system currently.", Toast.LENGTH_LONG).show();
                    loader.dismiss();
                    return;
                }
                users.clear(); // required to empty arrayList as it will be populated again
                // get all registered users
                for(DataSnapshot userSnapshot: snapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    // add user to all users list, to avoid quadratic network calls
                    users.add(user);
                }
                // get bookings
                getBookings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingsActivity.this, "Error getting users. "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get bookings. This should be after all users have been loaded
     */
    public void getBookings(){
        loader.setMessage("Getting users booking information...");
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loader.dismiss();

                if(!snapshot.exists()){
                    Toast.makeText(BookingsActivity.this, "No booking information found. ", Toast.LENGTH_SHORT).show();
                    return;
                }
                bookings.clear(); // remove any bookings data as it will be populated afresh
                for(DataSnapshot ds: snapshot.getChildren()){
                    Booking booking = ds.getValue(Booking.class);
                    // associate it with a user
                    for (User user: users){
                        if(user.getUid().equals(booking.getId())){
                            booking.setUser(user);
                            break;
                        }
                    }
                    // add to bookings array
                    bookings.add(booking);
                }

                // get items based on priority
                sortBookings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingsActivity.this, "Unable to get bookings. "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Get bookings based on priority and render them in the recyclerview
     */
    public void sortBookings(){
        bookingsArr.clear();
        Booking booking;
        while ((booking = bookings.poll()) != null){
            bookingsArr.add(booking);
        }

        // notify recycler adapter
        adapter.notifyDataSetChanged();
    }
}