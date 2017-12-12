package com.less.tplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CoreService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
