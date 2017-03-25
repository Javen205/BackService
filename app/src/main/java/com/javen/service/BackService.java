package com.javen.service;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import com.javen.backservice.StrongService;
import com.javen.utils.Utils;

@SuppressLint("HandlerLeak") 
public class BackService extends Service {
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startWatcherService();
				break;

			default:
				break;
			}

		};
	};
	
	
	public static void startService(Context context){
		Intent intent = new Intent(context, BackService.class);
		context.startService(intent);
	}
	
	
	/**
	 * 使用Aidl 启动watcherService
	 */
	private StrongService watcherService = new StrongService.Stub() {
		@Override
		public void stopService() throws RemoteException {
			Intent intent = new Intent(getBaseContext(), WatcherService.class);
			getBaseContext().stopService(intent);
		}

		@Override
		public void startService() throws RemoteException {
			Intent intent = new Intent(getBaseContext(), WatcherService.class);
			getBaseContext().startService(intent);
		}
	};
	

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) watcherService;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("service onStartCommand");
		Toast.makeText(this, "service onStartCommand", Toast.LENGTH_LONG).show();
		execut(this);
		return START_STICKY;
	}
	@Override
	public void onCreate() {
		
		System.out.println("service onCreate");
		Toast.makeText(this, "service onCreate", Toast.LENGTH_LONG).show();
		
		startWatcherService();
		
		/*
		 * 此线程用监听WatcherService的状态
		 */
		new Thread() {
			public void run() {
				while (true) {
					if (!watherServiceIsRun()) {
						Message msg = Message.obtain();
						msg.what = 1;
						handler.sendMessage(msg);
					}
					try {
						Thread.sleep(1000*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	/**
	 * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service
	 */
	@Override
	public void onTrimMemory(int level) {
		startWatcherService();
	}
	
	@Override
	public void onDestroy() {
		startService(new Intent(this, WatcherService.class));
		super.onDestroy();
	}
	
	private void execut(Context context){
		// 开启定时器，每隔xx秒刷新一次
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
        
        Intent intent=new Intent(context, TaskService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);  
        long firstime =System.currentTimeMillis();
        //绝对时间    
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstime,1000*30, pi);
        System.out.println("定时执行.....");
	}
	
	
	/**
	 * 判断WatcherService是否还在运行，如果不是则启动WatcherService
	 */
	private void startWatcherService() {
		boolean isRun = watherServiceIsRun();
		if (isRun == false) {
			try {
				watcherService.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean watherServiceIsRun() {
		boolean isRun = Utils.isServiceWork(BackService.this,
				WatcherService.class.getName());
		return isRun;
	}

}
