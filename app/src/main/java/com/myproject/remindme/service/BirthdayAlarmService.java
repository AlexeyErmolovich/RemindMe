package com.myproject.remindme.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.myproject.remindme.R;

/**
 * Created by Alecey Ermolovich on 1/26/2016.
 */
public class BirthdayAlarmService extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 127;

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("name_of_the_birthday");

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_birthday)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_birthday))
                .setWhen(System.currentTimeMillis())
                .setTicker(context.getString(R.string.birthday))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.birthday))
                .setContentText(context.getString(R.string.congratulation) + " " + name + ". "
                        + context.getString(R.string.notification));

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
