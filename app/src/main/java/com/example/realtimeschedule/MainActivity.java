package com.example.realtimeschedule;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Model.AvailableTime;
import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.BookingHelper;
import com.example.realtimeschedule.Model.FirebaseHelper;
import com.example.realtimeschedule.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;


public class MainActivity extends AppCompatActivity {
    Button boookBtn;
    TextView name, email, phone;
    ImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference usersRef, bookingsRef, slotsRef;
    User currentUser;
    AvailableTime availableTime;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= findViewById(R.id.textViewUserNamePass);
        email= findViewById(R.id.textViewEmailPass);
        phone= findViewById(R.id.textViewPhone);
        imageView= findViewById(R.id.imageView2);
        boookBtn = findViewById(R.id.btnBook);
        loader = new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots").child("Today");

        loader.setMessage("Getting your info...");
        loader.setCancelable(false);
        loader.show();
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

        boookBtn.setOnClickListener(view-> {
            // get next available time for booking
            loader.setMessage("Getting available time...");
            loader.setCancelable(true);
            loader.show();
            slotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loader.dismiss();
                    if(!snapshot.exists()){
                        // no data found
                        Toast.makeText(MainActivity.this, "Could not find any booking information by Admin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    availableTime = snapshot.getValue(AvailableTime.class);
                    // make new Booking
                    Booking booking = new Booking();
                    booking.setId(currentUser.getUid());
                    booking.setDate(availableTime.getBookedUntil());
                    /**
                     *  Here we need to implement logic for re-ordering time based on priority
                     *  of the user.
                     */
                    BookingHelper bHelper = new BookingHelper(MainActivity.this);
                    bHelper.book(booking);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error getting available time "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public  void initViews(){
        name.setText(currentUser.getName());
        email.setText(currentUser.getEmail());
        phone.setText(currentUser.getPhone());
        Picasso.get().load(currentUser.getImage()).into(imageView);
    }
}