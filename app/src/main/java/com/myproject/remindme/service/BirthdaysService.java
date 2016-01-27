package com.myproject.remindme.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Alecey Ermolovich on 1/26/2016.
 */
public class BirthdaysService extends Service {

    private StartServiceThread startServiceThread;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startServiceThread == null) {
            startServiceThread = new StartServiceThread(getApplicationContext());
            startServiceThread.start();
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
