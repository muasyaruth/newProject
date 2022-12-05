package com.example.realtimeschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AlarmActivity extends AppCompatActivity {

    ImageView imageView;

    TextView name;

    TextView email;

    TextView timeAndDate;

    Button closeButton;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

//        mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.notification);
//        mediaPlayer.start();

        name= findViewById(R.id.title);
        closeButton= (Button) findViewById(R.id.closeButton);
        email= findViewById(R.id.description);
        timeAndDate= findViewById(R.id.timeAndData);

        if(getIntent().getExtras() != null) {
            name.setText(getIntent().getStringExtra("TITLE"));
            email.setText(getIntent().getStringExtra("DESC"));
            timeAndDate.setText(getIntent().getStringExtra("DATE") + ", " + getIntent().getStringExtra("TIME"));
        }

        Glide.with(getApplicationContext()).load(R.drawable.clalendar).into(imageView);
        closeButton.setOnClickListener(view -> finish());




    }
    public static AlarmActivity instance() {
        return instance();
    }
}