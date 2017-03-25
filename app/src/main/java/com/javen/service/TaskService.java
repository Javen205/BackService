package com.javen.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TaskService extends IntentService {
	private Context mContext;

	public TaskService() {
		super("TaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mContext = this;
		System.out.println("TaskService execut");
		Toast.makeText(mContext, "TaskService execut", Toast.LENGTH_LONG).show();

	}

}
