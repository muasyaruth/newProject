package com.example.realtimeschedule;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Interface.OnHelperCompleteListener;
import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.BookingHelper;
import com.example.realtimeschedule.Model.Scheduler;
import com.example.realtimeschedule.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    Button bookBtn;
    TextView name, email, phone;
    ImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference usersRef, bookingsRef, slotsRef;
    User currentUser;
    Scheduler scheduler;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= findViewById(R.id.textViewUserNamePass);
        email= findViewById(R.id.textViewEmailPass);
        phone= findViewById(R.id.textViewPhone);
        imageView= findViewById(R.id.imageView2);
        bookBtn = findViewById(R.id.btnBook);
        loader = new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");

        loader.setMessage("Getting your info...");
        loader.setCancelable(false);
        loader.show();

         //Get current user info from firebase and render profile
        usersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loader.dismiss();
                if(!snapshot.exists()){
                    // user not found in users collection
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentUser = snapshot.getValue(User.class);
                initViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // make a booking
        bookBtn.setOnClickListener(view-> {
            // get next available time for booking
            loader.setMessage("Getting available time...");
            loader.show();
            // load information saved in scheduler
            slotsRef.child("scheduler").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loader.dismiss();
                    if(!snapshot.exists()){
                        // no data found
                        Toast.makeText(MainActivity.this, "Admin is currently accepting no more applications or is not available.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    scheduler = snapshot.getValue(Scheduler.class);
                    // make new Booking
                    Booking booking = new Booking();
                    booking.setId(currentUser.getUid());
                    booking.setPriority(currentUser.getPriority());
                    booking.setDate(scheduler.getBookedUntil());

                    /**
                     *  Here we need to implement logic for re-ordering time based on priority
                     *  of the user.
                     */
                    BookingHelper bHelper = new BookingHelper(MainActivity.this);
                    bHelper.setScheduler(scheduler) // for updating scheduling time
                            .book(booking) // booking over network request
                            .setOnCompleteListener(bookingHelperListener);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error getting available time "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * Listener for the progress of booking helper.
     */
    public OnHelperCompleteListener bookingHelperListener = new OnHelperCompleteListener() {
        @Override
        public void onSuccess(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            // Booking success. Show booking success activity
            startActivity(new Intent(MainActivity.this, BookingSuccessActivity.class));
        }

        @Override
        public void onError(String errorMessage) {
            // there was an issue in making booking.
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    private void initViews(){
        name.setText(currentUser.getUsername());
        email.setText(currentUser.getEmail());
        phone.setText(currentUser.getPhone());
        Picasso.get().load(currentUser.getImage()).into(imageView);
    }
}