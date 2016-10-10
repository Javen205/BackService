package com.javen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.javen.service.AmService;

/**
 * Created by Javen on 2016/10/9.
 */

public class SystemEventReceiver extends BroadcastReceiver {
    private final static String TAG = SystemEventReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG,"收到广播:"+ action);
        startAmService(context);
        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            Toast.makeText(context, "解锁", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"ACTION_USER_PRESENT...");
        }
        if (action.equals("com.javen.AmService.destroy")){
            startAmService(context);
            Toast.makeText(context, "AmService.destroy", Toast.LENGTH_SHORT).show();
        }

    }


    private void startAmService(Context context){
        Intent startAmService = new Intent(context, AmService.class);
        context.startService(startAmService);
    }
}
