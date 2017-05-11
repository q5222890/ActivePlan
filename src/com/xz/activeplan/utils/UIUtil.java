package com.xz.activeplan.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UI帮助类
 * @author kenny
 */
public class UIUtil {

	/**
	 * 设置文本粗体
	 * @author kenny
	 * @param view
	 */
	public static void setTextBold(TextView view){
		TextPaint paint = view.getPaint();
		paint.setFakeBoldText(true); 
		paint.setAntiAlias(true); //使用抗锯齿功能
	}
	
	/**
	 * 在字符串指定位置查如字符串
	 * @param index
	 * @param text
	 * @param subText
	 * @return 
	 */
	public static String insertTextString(int index, String text, String subText){
		StringBuffer str = new StringBuffer(text);
		str.insert(index, subText);
		return str.toString();
	}
	/**
	 * 使字符串”FFFFFFFF“转化成int类型的资源文件ID
	 * @param color
	 * @return 
	 */
	public static int setCustomItemColor(String color) {
		int id = 0;
		try {
			Long l = Long.parseLong("FF"+color, 16);
			id = l.intValue();
		} catch (Exception e) {
			return 0;
		}
		return id;
	}
	
	/**
	 * 去掉标题栏
	 * 
	 * @param activity
	 */
	public static void clearTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	/**
	 * 设置屏幕全屏
	 * 
	 * @param activity
	 */
	public static void fullScreen(Activity activity) {
		clearTitleBar(activity);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 消息提示
	 * 
	 * @param context
	 * @param showText
	 */
	public static void showToast(Context context, String showText) {

		Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
	}
} 
