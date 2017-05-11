package com.xz.activeplan.utils;

import android.util.Log;

/**
 * 提供统一的log打印工具类
 * 
 * @author kenny
 */
public class LogUtil {
	
	public static final boolean LOGON = true;		        // false 表示关闭log
	/** 五种Log日志类型 */
	
	/** 调试日志类型 */
	public static final int DEBUG = 111;
	/** 错误日志类型 */
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告日志类型 */
	public static final int WARN = 115;

	public static void show(Class cl, String message){
		if(LOGON){
			Log.i(cl.getSimpleName(), message);
		}
	}
	public static void show(String Tag, String Message) {
		if (LOGON) {
			Log.i(Tag, Message);
		}
	}

	public static void show(String Tag, String Message, int Style) {
		if (LOGON) {
			switch (Style) {
			case DEBUG: {
				Log.d(Tag, Message);
			}
				break;
			case ERROR: {
				Log.e(Tag, Message);
			}
				break;
			case INFO: {
				Log.i(Tag, Message);
			}
				break;
			case VERBOSE: {
				Log.v(Tag, Message);
			}
				break;
			case WARN: {
				Log.w(Tag, Message);
			}
				break;
			default:
				Log.i(Tag, Message);
				break;
			}
		}
	}
}
