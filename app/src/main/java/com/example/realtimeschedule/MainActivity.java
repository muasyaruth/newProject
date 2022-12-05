package com.example.realtimeschedule;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button viewSchedule, viewUsers, about, tuesday, wed;
    TextView name, email, phone;
    String UName, UEmail, UPhone;

    public static void redirectActivity(GiveDayAndTimeAvailable giveDayAndTimeAvailable,
                                        Class<MainActivity> mainActivityClass) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= findViewById(R.id.textViewUserNamePass);
        email= findViewById(R.id.textViewEmailPass);
        phone= findViewById(R.id.textViewPhone);

        Intent i = getIntent();
        UName= i.getStringExtra("currentName");
        UEmail= i.getStringExtra("currentEmail");
        UPhone= i.getStringExtra("currentPhone");

        name.setText(UName);
        email.setText(UEmail);
        phone.setText(UPhone);


        tuesday= findViewById(R.id.Tuesday);
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GiveDayAndTimeAvailable.class));
            }
        });
        wed= findViewById(R.id.Wednesday);



        viewUsers= findViewById(R.id.bookingForm);
        about= findViewById(R.id.About);
        viewSchedule= findViewById(R.id.viewVcSchedule);

        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TimeSlotsActivity.class));
            }
        });

        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BookingFormActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GiveDayAndTimeAvailable.class));
            }
        });
    }
}