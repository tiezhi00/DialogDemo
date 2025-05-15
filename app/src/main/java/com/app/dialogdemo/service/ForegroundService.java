package com.app.dialogdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.app.dialogdemo.floatingwindow.ServiceFloatingWindow;

public class ForegroundService extends Service {
    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceFloatingWindow.getInstance().init(getApplicationContext());
        ServiceFloatingWindow.getInstance().showFloatingWindow();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}