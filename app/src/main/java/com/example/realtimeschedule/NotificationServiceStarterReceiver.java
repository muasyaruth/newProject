package com.example.realtimeschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long interval = intent.getLongExtra("alarm_interval", 0);
        NotificationEventReceiver.setupAlarm(context, interval);
    }
}