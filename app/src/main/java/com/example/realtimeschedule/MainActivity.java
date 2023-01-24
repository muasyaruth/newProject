package com.example.realtimeschedule;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Interface.OnHelperCompleteListener;
import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.Model.BookingHelper;
import com.example.realtimeschedule.Model.EmailSender;
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
    Button bookBtn, btnViewBookings, btnLogout;
    TextView name, email, phone, designation;
    ImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference usersRef, bookingsRef, slotsRef;
    User user;
    Scheduler scheduler;
    ProgressDialog loader;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    SharedPreferences userPrefs, bookingPrefs;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= findViewById(R.id.textViewUserNamePass);
        email= findViewById(R.id.textViewEmailPass);
        phone= findViewById(R.id.textViewPhone);
        imageView= findViewById(R.id.imageView2);
        bookBtn = findViewById(R.id.btnBook);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        designation = findViewById(R.id.tvDesignation);
        btnLogout = findViewById(R.id.btnLogout);
        loader = new ProgressDialog(this);
        user = new User();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        slotsRef = FirebaseDatabase.getInstance().getReference("Slots");
        userPrefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        bookingPrefs = getSharedPreferences("booking_details", Context.MODE_PRIVATE);

//        loader.setMessage("Getting your info...");
//        loader.setCancelable(false);
//        loader.show();

        // create a notification channel for reminders
        createNotificationChannel();
        // get user info from shared preferences if any
        if (userPrefs.getString("uid", null) != null || userPrefs.getString("email", null) != null){
            user.setUid(userPrefs.getString("uid", null));
            user.setUsername(userPrefs.getString("username", ""));
            user.setEmail(userPrefs.getString("email", ""));
            user.setImage(userPrefs.getString("image", ""));
            user.setPhone(userPrefs.getString("phone", ""));
            user.setPriority(userPrefs.getInt("priority", 1));
            // render data to views
            initViews();
        }

        //Get current user info from firebase and render profile
        usersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (loader.isShowing()) loader.dismiss();
                if(!snapshot.exists()){
                    // user not found in users collection
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                user = snapshot.getValue(User.class);
                // save user to shared preferences
                assert user != null;
                saveUser(user);
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
                    if(!snapshot.exists()){
                        if (loader.isShowing()) loader.dismiss();
                        // no data found
                        Toast.makeText(MainActivity.this, "Admin is currently accepting no more applications or is not available.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    scheduler = snapshot.getValue(Scheduler.class);
                    // make new Booking
                    Booking booking = new Booking();
                    booking.setId(user.getUid());
                    booking.setPriority(user.getPriority());
                    booking.setDate(scheduler.getBookedUntil());

                    //Here we need to implement logic for re-ordering time based on priority of the user.
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
        // view bookings
        btnViewBookings.setOnClickListener(view -> {
            // check if user has any booking information
            String bookingId = bookingPrefs.getString("id", null);
            String bookingDate = bookingPrefs.getString("date", null);
            if (bookingId == null || bookingDate == null){
                Toast.makeText(this, "You have not made any bookings. ", Toast.LENGTH_LONG).show();
                return;
            }
            // user has bookings
            startActivity(new Intent(MainActivity.this, BookingDetails.class));
        });
        // logout
        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            // clear user data from shared preferences
            userPrefs.edit().clear().apply();
            bookingPrefs.edit().clear().apply();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        });
    }

    /**
     * Listener for the progress of booking helper.
     */
    public OnHelperCompleteListener bookingHelperListener = new OnHelperCompleteListener() {
        @Override
        public void onSuccess(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            // Do activities after a booking e.g. sending success email to user, setting reminders etc.
            postBookingActions();
        }

        @Override
        public void onError(String errorMessage) {
            // there was an issue in making booking.
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    private void initViews(){
        name.setText(user.getUsername());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        designation.setText(user.getUserType());
        Picasso.get().load(user.getImage()).into(imageView);
    }

    /**
     * Create a notification channel
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNotificationChannel(){
        String name = "RealTimeScheduleReminders";
        String description = "Reminder Channel for schedules in RealTimeSchedule app";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("schedule_reminder", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Create an alarm reminder for the scheduled time
     */
    private void setReminder(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        // set alarm
        long triggerTime = System.currentTimeMillis()+2000; // set alarm to be triggered after 2 seconds
        long reminderInterval = 2*60*1000; // reminder after every 2 minutes
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, reminderInterval, pendingIntent);
        // use the cancel() method to stop alarmManager from firing.
        Toast.makeText(this, "Reminder set successfully", Toast.LENGTH_SHORT).show();
    }

    /**
     * Cancel alarm to prevent reminders from firing indefinitely
     */
    private void cancelReminders(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0);

        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Reminder cleared successfully.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Actions done after a user has made a booking e.g. sending booking success email
     *
     */
    private void postBookingActions(){
        bookingsRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // hide loader if showing
                if (loader.isShowing()) loader.dismiss();

                Booking booking = snapshot.getValue(Booking.class);
                // save booking info to shared preferences
                assert booking != null;
                saveBooking(booking);
                // set reminder
                setReminder();

                // send email to user
                EmailSender emailSender = new EmailSender();
                emailSender.setReceiver(user.getEmail())
                        .setSubject("RealTimeSchedule: Booking Success!")
                        .setMessage("Hello "+user.getUsername()+". You booking was successful and has been scheduled on "+booking.getDate()+"\n " +
                                "Thank you for making bookings with RealTimeSchedule. "+"\n" +
                                "Best Regards.")
                        .send();

                // you can forward email to vc too

                // Booking success. Show booking success activity
                Intent intent = new Intent(MainActivity.this, BookingSuccessActivity.class);
                intent.putExtra("bookingDate", booking.getDate());
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Save user info to shared preferences
     */
    public void saveUser(User user){
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("uid", user.getUid());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("image", user.getImage());
        editor.putString("userType", user.getUserType());
        editor.apply();
    }
    /**
     * Save booking info to shared preferences
     */
    public void saveBooking(Booking booking){
        SharedPreferences.Editor editor = bookingPrefs.edit();
        editor.putString("id", booking.getId());
        editor.putString("date", booking.getDate());
        editor.putInt("priority", booking.getPriority());
        editor.apply();
    }
}