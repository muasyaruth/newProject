package com.example.realtimeschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    String name, email, date, time;
    @Override
    public void onReceive(Context context, Intent intent) {

        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");

        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra("TITLE", name);
        i.putExtra("DESC", email);
        i.putExtra("DATE", date);
        i.putExtra("TIME", time);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
