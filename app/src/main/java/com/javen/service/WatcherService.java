package com.javen.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.javen.backservice.StrongService;
import com.javen.utils.Utils;

@SuppressLint("HandlerLeak") 
public class WatcherService extends Service{
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startBackService();
				break;

			default:
				break;
			}

		}

		
	};
	/**
	 * 使用Aidl 启动BackService
	 */
	private StrongService backService = new StrongService.Stub() {
		@Override
		public void stopService() throws RemoteException {
			Intent intent = new Intent(getBaseContext(), BackService.class);
			getBaseContext().stopService(intent);
		}

		@Override
		public void startService() throws RemoteException {
			Intent intent = new Intent(getBaseContext(), BackService.class);
			getBaseContext().startService(intent);
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) backService;
	}
	@Override
	public void onCreate() {
		startBackService();
		
		/*
		 * 此线程用监听Service的状态
		 */
		new Thread() {
			public void run() {
				while (true) {
					if (!backServiceIsRun()) {
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
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	/**
	 * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service
	 */
	@Override
	public void onTrimMemory(int level) {
		startBackService();
	}

	private void startBackService() {
		boolean isRun = backServiceIsRun();
		if (isRun == false) {
			try {
				backService.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}		
	};
	private boolean backServiceIsRun() {
		boolean isRun = Utils.isServiceWork(WatcherService.this,
				BackService.class.getName());
		return isRun;
	}
}
