package com.example.duex;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "NotificationService started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
