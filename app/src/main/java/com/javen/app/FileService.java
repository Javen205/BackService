package com.javen.app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * https://www.oschina.net/code/snippet_661133_13075
 * @author Javen
注意： 
1、必须保持FileObserver的引用才能监听文件，因此最好放到service中，并且把引用都保存起来； 
2、FileObserver需要给每个文件夹设置监听器，因为它自己不能递归监听。
 */
public class FileService extends Service {
     
    private final static String TAG = "FileService";
     
    private List<SDCardListener > mFileObserverList = new ArrayList<SDCardListener>();
    private Context mContext;
     
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	mContext = this;
    	return super.onStartCommand(intent, flags, startId);
    }
 
    @SuppressLint("SdCardPath") 
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        System.out.println("启动监听.....");
        SDCardListener fileObserver = new SDCardListener(Environment.getExternalStorageDirectory().getPath());
        mFileObserverList.add(fileObserver);
        setFileObserver(Environment.getExternalStorageDirectory().getPath());
         
        //监听应用是否被卸载  监听不到啊
        SDCardListener fileObserver2 = new SDCardListener("/data/data/com.javen.demo");
        mFileObserverList.add(fileObserver2);
        SDCardListener fileObserver3 = new SDCardListener("/data/data/com.gold");
        mFileObserverList.add(fileObserver3);
        
        for(SDCardListener listener : mFileObserverList) {
            listener.startWatching();
        }
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        for(SDCardListener listener : mFileObserverList) {
            listener.stopWatching();
        }
    }   
     
    private List<File> setFileObserver(String dir) {
        ArrayList<File> fileList = new ArrayList<File>();
        LinkedList<File> list=new LinkedList<File>();
        File fileDir = new File(dir);
        File file[]=fileDir.listFiles();
        for(int i=0;i<file.length;i++){
            if(file[i].isDirectory()) {
                list.add(file[i]);
                SDCardListener fileObserver = new SDCardListener(file[i].getAbsolutePath());
                mFileObserverList.add(fileObserver);
            }
        }
        File tmp;
        while(!list.isEmpty()){
            tmp=list.removeFirst();
            if(tmp.isDirectory()){
                SDCardListener fileObserver = new SDCardListener(tmp.getAbsolutePath());
                mFileObserverList.add(fileObserver);
                file=tmp.listFiles();
                if(file==null)continue;
                for(int i=0;i<file.length;i++){
                    if(file[i].isDirectory()) {
                        list.add(file[i]);
                        SDCardListener fileObserver1 = new SDCardListener(file[i].getAbsolutePath());
                        mFileObserverList.add(fileObserver1);
                    }
                }
            }
        }
        return fileList;
    }
    
    
    //应用卸载后打开一个网页的方法
    protected void openBrowser() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
     
    class SDCardListener extends FileObserver {
         
        private String mAbsolutePath;
         
        public SDCardListener(String path, int mask) {
            super(path, mask);
        }
 
        public SDCardListener(String path) {
            super(path);
            mAbsolutePath = path;
        }
 
        @Override
        public void onEvent(int event, String path) {
            final int action = event & FileObserver.ALL_EVENTS;
            switch (action) {
            /*case FileObserver.ACCESS:
            	System.out.println("event: 文件或目录被访问, path: " + path);
                Log.d(TAG, "event: 文件或目录被访问, path: " + path);
                break;*/
                 
            case FileObserver.DELETE:
            	System.out.println("event: 文件或目录被删除, path: " + mAbsolutePath+"/" + path);
                Log.d(TAG, "event: 文件或目录被删除, path: " + mAbsolutePath + path);
                openBrowser();
                break;
                 
            /*case FileObserver.OPEN:
            	System.out.println("event: 文件或目录被打开, path: " + path);
                Log.d(TAG, "event: 文件或目录被打开, path: " + path);
                break;*/
                 
            case FileObserver.MODIFY:
            	System.out.println("event: 文件或目录被修改, path: " + path);
                Log.d(TAG, "event: 文件或目录被修改, path: " + path);
                break;
            case FileObserver.CREATE:
//                if(path.endsWith(".pdf")) {
//                    Log.d(TAG, "event: 文件或目录被创建, path: " + mAbsolutePath + path);
//                }
            	System.out.println("event: 文件或目录被创建, path: " + mAbsolutePath + "/" + path);
            	Log.d(TAG, "event: 文件或目录被创建, path: " + mAbsolutePath + path);
                break;
            }
        }
    }
}
