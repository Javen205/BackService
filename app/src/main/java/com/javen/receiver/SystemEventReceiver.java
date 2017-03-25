package com.javen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.javen.service.BackService;

/**
 * Created by Javen on 2016/10/9.
 *但是Android API Level8以上的时候，程序可以安装在SD卡上。
 *如果程序安装在SD卡上，那么在BOOT_COMPLETED广播发送之后， SD卡才会挂载，因此程序无法监听到该广播。
 *解决办法：同时监听开机和sd卡挂载。（也不能只监听挂载就认为开机了，因为有的手机没有sd卡）
 *
 */

public class SystemEventReceiver extends BroadcastReceiver {
    private final static String TAG = SystemEventReceiver.class.getSimpleName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG,"收到广播:"+ action);

        startAmService(context);

        // 按电源键解锁监听 识别用户进入home界面
        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            Toast.makeText(context, "解锁", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"ACTION_USER_PRESENT...");
        }
        // 接收安装广播
        else if (action.equals(Intent.ACTION_PACKAGE_ADDED)||action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            System.out.println("ACTION_PACKAGE_ADDED packageName>"+packageName);
            Toast.makeText(context, "ACTION_PACKAGE_ADDED packageName>"+packageName, Toast.LENGTH_LONG).show();
        }
        //网络改变
        else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            System.out.println("CONNECTIVITY_CHANGE");
            Toast.makeText(context, "CONNECTIVITY_CHANGE", Toast.LENGTH_LONG).show();
        }
        //每分钟一次 只能用过代码动态注册 http://www.ithtw.com/2276.html
        else if (Intent.ACTION_TIME_TICK.equals(action)) {
            System.out.println("ACTION_TIME_TICK");
            Toast.makeText(context, "ACTION_TIME_TICK", Toast.LENGTH_LONG).show();
        }
    }


    private void startAmService(Context context){
        Intent startAmService = new Intent(context, BackService.class);
        context.startService(startAmService);
    }
}
