package com.xz.activeplan.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PhoneInfoUtil {

	// IMEI
	public static final String getIMEI(Context context) {
		String imei = null;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();
		if (imei == null || imei.length() == 0) {
			imei = Settings.System.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);
			if (imei == null || imei.length() == 0) {
				imei = "0000000000000000";
			}
		}
		if (imei.length() < 16) {
			imei = imei + "0";
		} else if (imei.length() > 16) {
			imei = imei.substring(0, 15);
		}
		return imei;
	}
	
	// IMSI
	public static final String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		if (TextUtils.isEmpty(imsi)) {
			return "";
		}
		return imsi;
	}

	// cpuid 做为移动设备标识
	public static final String getCPUId() {
		String str = "", strCPU = "", cpuid = null;
		try {
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			str = input.readLine();
			while(! TextUtils.isEmpty(str)) {
				if (str.indexOf("Serial") > -1) {
					// get cpu serial
					strCPU = str.substring(str.indexOf(":") + 1, str.length());
					//get rid of the spaces
					cpuid = strCPU.trim();
					break;
				}
				str = input.readLine();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if(TextUtils.isEmpty(cpuid)) {
			cpuid = "0000000000000000";
		}
		return cpuid;
	}
	
	// we call it "ver",应用版本号
	public static String GetAppVer(Context context){
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	// we call it "pf", 手机平台
	public static final String getSDKVersion() {
		return "android_" + String.valueOf(Build.VERSION.SDK_INT);
	}
	
	// we call it "md", 机型
	public static final String getMachineType() {
		return Build.MODEL.replaceAll(" ", "").toString();
	}
	
	// we call it "rhv", 子机型
	public static final String getMachineSubType() {
		return Build.MODEL.replaceAll(" ", "").toString();
	}
	
	// we call it "sw", 屏宽
	public static final int getResolutionX(Context context) {
		DisplayMetrics dm=new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
	}
	
	// we call it "sh", 屏高
	public static final int getResolutionY(Context context) {
		DisplayMetrics dm=new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
	}
	
	// we call it "op", 运营商标识
	public static final int getMobileServiceProvider(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// op 运营商标识 -1=其它，0=移动，1=联通，2=电信
		String opName = tm.getNetworkOperator();
		int op = -1;
		if (opName.equals("46000") || opName.equals("46002") || opName.equals("46007")) {
			op = 0;
		} else if (opName.equals("46001")) {
			op = 1;
		} else if (opName.equals("46003") || opName.equals("460003")) {
			op = 2;
		}
		return op;
	}
	
	public static final String getCountryCode(Context context) {
		String countryCode;
		try {
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			countryCode = telephonyManager.getSimCountryIso();
			if(countryCode.equalsIgnoreCase("cn")) {
				countryCode = "86";
			} else {
				countryCode = "00";
			}
		} catch(Exception e) {
			countryCode = "00";
		}
		return countryCode;
	}
}
