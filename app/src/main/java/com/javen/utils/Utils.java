package com.javen.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BOOT_PERMISSION="android.permission.RECEIVE_BOOT_COMPLETED";

	/**
	 * 获取开机自启动应用的包名
	 * @param context
	 * @return
	 */
	public static String[] getAutoBootAppInfo(Context context){
		List<String> autoBoot = new ArrayList<String>();
		List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		
		for (ApplicationInfo applicationInfo : installedApplications) {
			System.out.println(">>"+applicationInfo.packageName);
			
			if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().
					checkPermission(BOOT_PERMISSION, applicationInfo.packageName)) {
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)<=0) {
					autoBoot.add(applicationInfo.packageName);
				}
			}
		}
		return autoBoot.toArray(new String[autoBoot.size()]);
	}
	

	/**
	 * 判断某个服务是否正在运行的方法
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名(例如：com.javen.demo.service.BackService)
	 * @return
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(100);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 判断进程是否运行
	 * 
	 * @return
	 */
	public static boolean isProessRunning(Context context, String proessName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				isRunning = true;
			}
		}

		return isRunning;
	}

	public static boolean exec(String pkgName) {
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.println("pm enable " + pkgName);
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();
			return returnResult(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}

	private static boolean returnResult(int value) {
		// 代表成功
		if (value == 0) {
			return true;
		} else if (value == 1) { // 失败
			return false;
		} else { // 未知情况
			return false;
		}
	}
}
