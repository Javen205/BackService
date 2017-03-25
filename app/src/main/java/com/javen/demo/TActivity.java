package com.javen.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class TActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏应用图标
//	    getPackageManager().setComponentEnabledSetting(new ComponentName(this, MainActivity.class), 2, 1);
		System.out.println("start TActivity");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 2000);
	}
}
