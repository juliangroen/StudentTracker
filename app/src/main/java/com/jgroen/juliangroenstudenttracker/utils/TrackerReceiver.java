package com.jgroen.juliangroenstudenttracker.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.jgroen.juliangroenstudenttracker.R;

public class TrackerReceiver extends BroadcastReceiver {

    public static final String EXTRA_NOTIFICATION_CONTENT = "com.jgroen.juliangroenstudenttracker.EXTRA_NOTIFICATION_CONTENT";

    static int notificationID;
    String channelID = "trackerNotifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context, channelID);
        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_sentiment_very_satisfied)
                .setContentText(intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT))
                .setContentTitle(context.getString(R.string.app_name))
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, notification);
    }

    private void createNotificationChannel(Context context, String channelID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
