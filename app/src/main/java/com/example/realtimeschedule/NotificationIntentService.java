package com.example.realtimeschedule;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class NotificationIntentService extends IntentService {

    private static final String ACTION_START = "ACTION_START";
    private static final int NOTIFICATION_ID = 1;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action))
                processNotification();

        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processNotification() {
        Intent resultIntent=new Intent(this, MyBookingDetails.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        Notification nBuilder = new Notification.Builder(this)
                .setContentTitle("Don't miss! ")
                .setTicker("Notification!")
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.clalendar)
                .setContentText("7 days left till your appointment...")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nBuilder.flags |=Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, nBuilder);
    }
}
