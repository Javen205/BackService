package com.javen.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Javen on 2016/10/9.
 */

public class AmService extends Service {
    private final static  String TAG=AmService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand...");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate...");
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long currentTime= System.currentTimeMillis();
        Intent intent= new Intent(this,ShowService.class);
        PendingIntent pi = PendingIntent.getService(this,0,intent,0) ;
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,currentTime,1000*10,pi);


        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentText("已连接");
        builder.setSubText("触摸可以运行");
        builder.setOngoing(true);//不能滑动删除
        builder.setAutoCancel(false);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_share));
        builder.setSmallIcon(android.R.drawable.ic_menu_share);
        Intent notificationIntent = new Intent(this, ShowService.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getService(this, requestCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONGOING_EVENT;
        startForeground(0x001,notification);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //强杀之后onDestroy 方法不执行
        Log.e(TAG,"onDestroy...");
        stopForeground(true);

        Intent intent = new Intent("com.javen.AmService.destroy");
        sendBroadcast(intent);

        Intent sevice = new Intent(this, ShowService.class);
        this.startService(sevice);

        super.onDestroy();
    }
}
