package com.xz.activeplan.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SystemUtil {

	/**
	 * description: 启动apk的
	 * 
	 * @param context：
	 *            应用上下文
	 * @param pkg
	 *            ：包名
	 */
	public static void ExcuteApk(Context context, String pkg) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent();
		intent = packageManager.getLaunchIntentForPackage(pkg);
		if (intent != null)
			context.startActivity(intent);
	}

	/**
	 * description: 安装apk
	 * 
	 * @param context：应用上下文
	 * @param apkPath：应用包名
	 */
	public static void InstallApk(Context context, String apkPath) {
		if (null == apkPath)
			return;
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 功能：获得打开分享列表的Intent 参数：info 表示需要分享的信息 返回值：intent
	 */
	public static Intent getShareIntent(String info) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, info);
		return intent;
	}

	/**
	 * 功能：获得安装程序的Intent 参数：strApkPath 安装程序路径 返回：成功-intent,失败null
	 */
	public static Intent getApkInstallIntent(String strApkPath) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + strApkPath), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	/**
	 * 判断程序是否在后台运行
	 * 
	 * @author kenny
	 */
	public static boolean isBackgroundRunning(Context context) {
		String processName = "um.lbs.chat";

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

		if (activityManager == null)
			return false;
		// get running application processes
		List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo process : processList) {
			if (process.processName.startsWith(processName)) {
				boolean isBackground = process.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& process.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
				boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
				if (isBackground || isLockedState)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/**
	 * 获取软件信息
	 * 
	 * @param context
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo packInfo = null;
		try {
			// 获取PackageManager
			PackageManager packageManager = context.getPackageManager();
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return packInfo;
	}

	/** 获得屏幕的宽度 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/** 获取屏幕的高度 */
	public static int getScreenHeight(Activity context) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/** 获取屏幕的分辨率 */
	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/** 获取系统版本 */
	public static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/** 隐藏软件盘 */
	public static void hideSoftKeyborad(Activity context) {
		final View v = context.getWindow().peekDecorView(); // Retrieve the
															// current decor
															// view
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context // 获得输入方法的Manager
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * 获取android当前可用内存大小
	 */
	public static String getAvailMemory(Context context) {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 获取android当前可用内存大小
	 */
	public static long getmem_UNUSED(Context mContext) {
		long MEM_UNUSED;
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		MEM_UNUSED = mi.availMem / 1024;
		return MEM_UNUSED;
	}

	/**
	 * 消耗的内存大小
	 */
	@SuppressLint("NewApi")
	public static long getmem_used(Context mContext) {
		long MEM_USED;
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		MEM_USED = (mi.totalMem - mi.availMem) / 1024;
		return MEM_USED;
	}

	/**
	 * 系统内存
	 * 
	 * @return
	 */
	public static long getmem_TOLAL() {
		long mTotal;
		// 系统内存
		String path = "/proc/meminfo";
		// 存储器内容
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				// 采集内存信息
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// beginIndex
		int begin = content.indexOf(':');
		// endIndex
		int end = content.indexOf('k');
		// 采集数量的内存
		content = content.substring(begin + 1, end).trim();
		// 转换为Int型
		mTotal = Integer.parseInt(content);
		return mTotal;
	}

	/** s杀进程，清手机内存，不是自己的APP */
	@SuppressWarnings("deprecation")
	public static void cleanMemory(Context context) {
		ActivityManager activityManger = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);

				System.out.println("pid            " + apinfo.pid);
				System.out.println("processName              " + apinfo.processName);
				System.out.println("importance            " + apinfo.importance);
				String[] pkgList = apinfo.pkgList;

				if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
					// Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						// 2.2以上是过时的,请用killBackgroundProcesses代替
						if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO) {
							activityManger.killBackgroundProcesses(pkgList[j]);
						} else {
							activityManger.restartPackage(pkgList[j]);
						}
					}
				}

			}
	}

	/**
	 * 检查是否有SIM卡 1表示没有
	 * 
	 * @return int
	 */
	public static int checkSIMcard(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);// 取得相关系统服务
		// StringBuffer sb = new StringBuffer();
		int state = 5;
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
			// sb.append("无卡");
			state = TelephonyManager.SIM_STATE_ABSENT;
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			// sb.append("未知状态");
			state = TelephonyManager.SIM_STATE_UNKNOWN;
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			// sb.append("需要NetworkPIN解锁");
			state = TelephonyManager.SIM_STATE_NETWORK_LOCKED;
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			// sb.append("需要PIN解锁");break;
			state = TelephonyManager.SIM_STATE_PIN_REQUIRED;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			// sb.append("需要PUK解锁");
			state = TelephonyManager.SIM_STATE_PUK_REQUIRED;
			break;
		case TelephonyManager.SIM_STATE_READY:
			// sb.append("良好");
			state = TelephonyManager.SIM_STATE_READY;
			break;
		}
		return state;
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	/**
	 设置状态栏
	 */
	public static  void setStatusBar(Context context,ViewGroup viewGroup)
	{
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
		{
			final ViewGroup linear_bar = (ViewGroup)viewGroup;

			final int statusHeight= SystemUtil.getStatusBarHeight(context);
			linear_bar.post(new Runnable() {
				@Override
				public void run() {
					int titleHeight= linear_bar.getHeight();
					ViewGroup.LayoutParams layoutParams = linear_bar.getLayoutParams();
					layoutParams.height= statusHeight + titleHeight;
					linear_bar.setLayoutParams(layoutParams);
				}
			});
		}
	}
	/**
	 * 设置沉浸式状态栏位 自定义颜色
	 */
	public static void setTranslucentStatus(Activity activity,int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
           /* getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
			SystemStatusManager tintManager = new SystemStatusManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(color);
			activity.getWindow().getDecorView().setFitsSystemWindows(true);
		}
	}

}
