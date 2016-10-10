package com.javen.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Javen on 2016/10/9.
 */

public class ShowService extends IntentService {
    private final static String TAG = ShowService.class.getSimpleName();

    public ShowService() {
        super("ShowService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"onHandleIntent...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand...");
        Toast.makeText(this,"show...",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate...");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy...");

        Intent intent = new Intent(this,AmService.class);
        startService(intent);

        super.onDestroy();
    }
}
