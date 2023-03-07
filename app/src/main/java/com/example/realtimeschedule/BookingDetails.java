package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookingDetails extends AppCompatActivity {
    ImageView image;
    TextView name, email, date;
    String userId;
    User user;
    Booking booking;
    SharedPreferences userPrefs, bookingPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        image = findViewById(R.id.imageViewUser);
        name = findViewById(R.id.tvUsername);
        email = findViewById(R.id.tvEmail);
        date = findViewById(R.id.tvDate);
        booking = new Booking();
        user = new User();
        userPrefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        bookingPrefs = getSharedPreferences("booking_details", Context.MODE_PRIVATE);

        userId = userPrefs.getString("uid", null);
        if (userId == null) {
            Toast.makeText(this, "You must be logged in to see this page", Toast.LENGTH_LONG).show();
            return;
        }

        // user info
        user.setUid(userPrefs.getString("uid", null));
        user.setEmail(userPrefs.getString("email", null));
        user.setImage(userPrefs.getString("image", null));
        user.setUsername(userPrefs.getString("username", null));
        // booking
        booking.setId(bookingPrefs.getString("id", null));
        booking.setDate(bookingPrefs.getString("date", null));
        booking.setPriority(bookingPrefs.getInt("priority", 1));

        // update UI with data
        updateUI();
    }


    /**
     * Populate UI with data
     */
    public void updateUI(){
        try {
            Picasso.get().load(user.getImage()).into(image);
            name.setText("Username: "+user.getUsername());
            email.setText("Email: "+user.getEmail());
            date.setText("Booking Date: "+booking.getDate());
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}