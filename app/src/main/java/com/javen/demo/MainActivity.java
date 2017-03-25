package com.javen.demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.javen.backservice.R;
import com.javen.receiver.SystemEventReceiver;
import com.javen.sms.SMSMethod;
import com.javen.utils.Utils;

/**
 * 推荐参考文章
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startService(new Intent(this, FileService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getApplicationContext().registerReceiver(new SystemEventReceiver(), filter);

    }

    public void sendTextMessage(View view) {
        System.out.println("发送短信.....");
        SMSMethod.getInstance(this).SendMessage("10086", "测试短信.....Javen");
    }

    public void sendMultipartTextMessage(View view) {
        System.out.println("发送短信中.....");
        SMSMethod.getInstance(this).SendMessage2("10086", "测试短信M......Javen");
    }

    public void pmEnable(View view) {
        System.out.println("getPackageName()>" + getPackageName());
        System.out.println("result>" + Utils.exec(getPackageName()));
    }

    public void getAutoBoot(View view) {
        String[] autoBootAppInfo = Utils.getAutoBootAppInfo(this);
        System.out.println(">>" + autoBootAppInfo.length);
        for (int i = 0; i < autoBootAppInfo.length; i++) {
            String string = autoBootAppInfo[i];
            System.out.println(">>>" + string);
        }

//         startActivity(getPackageManager()
// 				.getLaunchIntentForPackage("com.javen.demo"));
        //隐士启动后台的service
        Intent startIntent = new Intent();
        startIntent.setAction("com.javen.android.service");
        startService(startIntent);


    }

    public void startActivity(View view) {
        //startActivity(new Intent(this, TActivity.class));

        Intent startActivity = new Intent();
        startActivity.setAction("com.javen.back.activity");
        startActivity(startActivity);

//        finish();
    }

    @Override
    protected void onPause() {
//        SMSMethod.getInstance(this).unregisterReceiver();
        super.onPause();
    }
}
