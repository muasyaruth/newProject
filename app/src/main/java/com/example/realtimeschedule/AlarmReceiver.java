package com.example.realtimeschedule;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Priority;

public class AlarmReceiver extends BroadcastReceiver {
    private static int REMINDER_NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Open a new activity when user clicks on this notification
        Intent i = new Intent(context, BookingDetails.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // make a pending intent to wrap this intent and send it to the broadcast receiver
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        // make a notification builder
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "schedule_reminder")
                    .setContentTitle("Appointment Reminder!")
                    .setTicker("Don't miss! ")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND) // use default notification sound
                    .setSmallIcon(R.drawable.clalendar)
                    .setContentText("Check your pending appointment");
            // show notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(REMINDER_NOTIFICATION_ID,notificationBuilder.build());
    }
}
