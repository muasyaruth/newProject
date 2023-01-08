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

import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.User;
import com.example.realtimeschedule.ViewHolder.BookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

//How To Sort My Recyclerview According To Date And Time With Examples
public class BookingsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    User user;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        loader = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
        final  DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseRecyclerOptions<Booking> options = new FirebaseRecyclerOptions.Builder<Booking>()
                .setQuery(bookingsRef, Booking.class)
                .build();

        FirebaseRecyclerAdapter<Booking, BookingsViewHolder> adapter = new FirebaseRecyclerAdapter
                <Booking, BookingsViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull BookingsViewHolder bookingsViewHolder,
                                            int i, @NonNull Booking bookings) {

                Booking booking = getItem(i);
                loader.setMessage("Getting booking information...");
                loader.show();

                usersRef.child(booking.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loader.dismiss();
                        if(!snapshot.exists()){
                            Toast.makeText(BookingsActivity.this, "No user associated with this booking was found.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        user = snapshot.getValue(User.class);
                        // render UI
                        bookingsViewHolder.clientName.setText(user.getUsername());
                        bookingsViewHolder.clientEmail.setText(user.getEmail());
                        bookingsViewHolder.designation.setText(user.getUserType());
                        bookingsViewHolder.appointmentDate.setText(booking.getDate());
                        Picasso.get().load(user.getImage()).into(bookingsViewHolder.imageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BookingsActivity.this, "Error getting bookings "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




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
                
                 bookingsViewHolder.btnServed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(BookingsActivity.this, "You need to implement logic for scheduling", Toast.LENGTH_SHORT).show();
                    }
                });

                // user will not be served, and will be removed from the queue
                bookingsViewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(BookingsActivity.this, "Your logic for rejecting user booking will appear here", Toast.LENGTH_SHORT).show();
                    }
                });

                bookingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(BookingsActivity.this, "Booking item info clicked", Toast.LENGTH_SHORT).show();
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