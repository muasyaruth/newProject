package com.example.realtimeschedule;


import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.realtimeschedule.Adapter.BookingsAdapter;
import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.BookingComparator;
import com.example.realtimeschedule.Model.Scheduler;
import com.example.realtimeschedule.Model.User;
import com.example.realtimeschedule.ViewHolder.BookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bookings = new PriorityQueue<>(new BookingComparator());
        }

        bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new BookingsAdapter(this, bookings);
        recyclerView.setAdapter(adapter);

        loader.setMessage("Checking users information...");
        loader.show();

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(BookingsActivity.this, "No Users in the system currently.", Toast.LENGTH_LONG).show();
                    return;
                }
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
                Toast.makeText(BookingsActivity.this, "Error getting users "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



//You need to parse date in dd-MM-yyyy pattern first and then format it to the pattern of your choice.
//                List<String> str = Collections.singletonList(bookings.getDate());
//                List<String> str = new ArrayList<>();
//                str.add(bookings.getDate());
//
//                for(int j =0;j<str.size();j++){
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
////                    List<LocalDateTime> dateTime = Collections.singletonList(LocalDateTime.parse(str.get(j), formatter));
//
//                    List<LocalDateTime> dateTime = new ArrayList<>();
//                    dateTime.add(LocalDateTime.parse(str.get(j),formatter));
//                    dateTime.forEach(n->{
//                        long millis  = n.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                        long timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                        if (timeNow<millis){
//                            System.out.println("schedule reminder");
//
//                            long sendNotification = millis- LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//
//                            /*schedule reminder*/
//                            Timer timer = new Timer();
//                            TimerTask timerTask = new TimerTask() {
//                                @Override
//                                public void run() {
//                                    System.out.println("Scheduling reminder------------------------:)");
//                                    sendmail();
//                                }
//                            };
//
//                            try {
//                                timer.schedule(timerTask, sendNotification);
//                            }catch(IllegalArgumentException ex){
//                                ex.printStackTrace();
//                            }
////                            sendmail();
//                        }else {
//                            System.out.println("time past");
//                        }
//                        System.out.println("======================date list===="+n.toString());//connect simu tuone
//
//
//                    });
//
//                }


    /**
     * Get bookings. This should be after all users have been loaded
     */
    public void getBookings(){
        loader.setMessage("Getting users booking information...");
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loader.dismiss();

                if(!snapshot.exists()){
                    Toast.makeText(BookingsActivity.this, "No booking information found. ", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                // notify recycler adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingsActivity.this, "Unable to get bookings. "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}