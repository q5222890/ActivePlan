/**
 *  Copyright (c) 2012, SXH COMPUTER TECH LTD.
 *  All rights reserved.
 *
 *  File name  :  PrefManager.java
 *  Description:  Preferences管理类
 *
 *  ========================================================================
 *  DATE     	AUTHOR   		VERSION  	REMARKS
 *  ----------------------------------------------------------------------
 *  2012-12-14  zhaoxufeng     	1.0   		 Create
 *  ========================================================================
 *
 */

package com.xz.activeplan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences管理类
 *
 */
public class PrefManager {
	
	/**
	 * 统一的SharedPreferences接口
	 * @param context
	 * @return SharedPreferences
	 */
	public static SharedPreferences getSharedPreferences(Context context){
		
		return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
	}
	
	/**
	 * 读取Preferences的int型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param nDefault 默认值
	 * @return 返回读取的数据
	 */
	public static int getPref_Int(Context context, String strKey, int nDefault) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		int nRet = preference.getInt(strKey, nDefault);
		return nRet;
	}
	
	/**
	 * 设置Preferences的int型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param nValue 值
	 */
	public static void setPref_Int(Context context, String strKey, int nValue) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference.edit().putInt(strKey, nValue).commit();
	}
	
	/**
	 * 读取Preferences的String型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param strDefault 默认值
	 * @return 返回读取的数据
	 */
	public static String getPref_String(Context context, String strKey, String strDefault) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		String strRet = preference.getString(strKey, strDefault);
		return strRet;
	}
	
	/**
	 * 设置Preferences的String型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param strValue 值
	 */
	public static void setPref_String(Context context, String strKey, String strValue) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference.edit().putString(strKey, strValue).commit();
	}
	
	/**
	 * 读取Preferences的boolean型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param bDefault 默认值
	 * @return 返回读取的数据
	 */
	public static boolean getPref_Boolean(Context context, String strKey, boolean bDefault) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		boolean bRet = preference.getBoolean(strKey, bDefault);
		return bRet;
	}
	
	/**
	 * 设置Preferences的boolean型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param bValue 值
	 */
	public static void setPref_Boolean(Context context, String strKey, boolean bValue) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference.edit().putBoolean(strKey, bValue).commit();
	}
	
	/**
	 * 读取Preferences的float型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param fDefault 默认值
	 * @return 返回读取的数据
	 */
	public static float getPref_Float(Context context, String strKey, float fDefault) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		float fRet = preference.getFloat(strKey, fDefault);
		return fRet;
	}
	
	/**
	 * 设置Preferences的float型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param fValue 值
	 */
	public static void setPref_Float(Context context, String strKey, float fValue) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference.edit().putFloat(strKey, fValue).commit();
	}
	
	/**
	 * 读取Preferences的long型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param lDefault 默认值
	 * @return 返回读取的数据
	 */
	public static long getPref_Long(Context context, String strKey, long lDefault) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		long lRet = preference.getLong(strKey, lDefault);
		return lRet;
	}
	
	/**
	 * 设置Preferences的long型数据
	 * @param context 上下文
	 * @param strKey 关键字
	 * @param lValue 值
	 */
	public static void setPref_Long(Context context, String strKey, long lValue) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
		preference.edit().putLong(strKey, lValue).commit();
	}
	
}
