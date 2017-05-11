package com.xz.activeplan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.xz.activeplan.entity.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

public class SharedUtil {
	private static  String Name = "activityplan";

	public static void saveAppVersion(Context context, String version) {
		Editor editor = getShared(context).edit();
		editor.putString("appVersion", version);
		editor.commit();
	}

	private static SharedPreferences getShared(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Name,
				Context.MODE_PRIVATE);
		return sp;
	}

	public static String getAppVersion(Context context) {
		return getShared(context).getString("appVersion", "");
	}

	public static void saveLogin(Context context, boolean isLogin,boolean isThird) {
		Editor editor = getShared(context).edit();
		editor.putBoolean("login", isLogin);
		editor.putBoolean("third", isThird);
		editor.commit();
	}

	public static boolean isLogin(Context context) {
		return getShared(context).getBoolean("login", false);
	}

	public static boolean isThirdLogin(Context context) {
		return getShared(context).getBoolean("third", false);
	}
	
	public static void saveLoginCount(Context context, int count) {
		Editor editor = getShared(context).edit();
		editor.putInt("loginCount", count);
		editor.commit();
	}
	
	public static void saveCity(Context context,String city){
		Editor editor = getShared(context).edit();
		editor.putString("city", city);
		editor.commit();
	}
	
	public static String getCity(Context context){
		return getShared(context).getString("city", "全国") ;
	}
	
	public static void saveLocationCity(Context context,String city){
		Editor editor = getShared(context).edit();
		editor.putString("locationcity", city);
		editor.commit();
	}
	
	public static String getLocationCity(Context context){
		return getShared(context).getString("locationcity", "全国") ;
	}
	
	public static void saveSplash(Context context){
		PackageInfo p = SystemUtil.getPackageInfo(context);
		Editor editor = getShared(context).edit();
		editor.putBoolean(p.versionName+"Splash", true);
		editor.commit();
	}
	
	public static boolean getSplash(Context context){
		PackageInfo p = SystemUtil.getPackageInfo(context);
		return getShared(context).getBoolean(p.versionName + "Splash", false) ;
	}

	public static int getLoginCount(Context context) {
		return getShared(context).getInt("loginCount", 0);
	}

	public static void saveUserAccount(Context context, String account) {
		Editor editor = getShared(context).edit();
		editor.putString("account", account);
		editor.commit();
	}

	public static String getUserAccount(Context context) {
		return getShared(context).getString("account", "");
	}
	
	public static void save(Context context, String key,String content) {
		Editor editor = getShared(context).edit();
		editor.putString(key, content);
		editor.commit();
	}

	public static String get(Context context,String key) {
		return getShared(context).getString(key, "");
	}
	
	public static void saveFloat(Context context, String key,double content) {
		Editor editor = getShared(context).edit();
		editor.putFloat(key, (float)content);
		editor.commit();
	}

	public static Float getFloat(Context context,String key) {
		return getShared(context).getFloat(key, 0);
	}
	public static void saveUserInfo(Context context,UserInfoBean userInfo){
		Editor editor = getShared(context).edit();
		editor.putInt("id",userInfo.getId());
		editor.putInt("userid", userInfo.getUserid());
		editor.putString("username", userInfo.getUsername());
		editor.putString("password", userInfo.getPassword());
		editor.putString("city", userInfo.getCity());
		editor.putString("realname", userInfo.getRealname());
		editor.putString("signature", userInfo.getSignature());
		editor.putString("phonenum", userInfo.getPhonenum());
		editor.putInt("sex",userInfo.getSex());
		editor.putString("headurl", userInfo.getHeadurl());
		editor.putString("email", userInfo.getEmail());
		editor.putString("bankname", userInfo.getBankname());
		editor.putString("bankaccount", userInfo.getBankaccount());
		editor.putString("bankusername", userInfo.getBankusername());
		editor.putString("bankcity", userInfo.getBankcity());
		editor.putString("banksubsidiary", userInfo.getBanksubsidiary());
		editor.putString("alipayaccount", userInfo.getAlipayaccount());
		editor.putString("alipayusername", userInfo.getAlipayusername());
		editor.putString("qqkey", userInfo.getQqkey());
		editor.putString("weixinkey",userInfo.getWeixinkey());
		editor.putString("weibokey", userInfo.getWeibokey());
		editor.putString("token", userInfo.getToken());
		editor.putString("thirdid", userInfo.getThirdid());
		editor.putInt("fans",userInfo.getFans());
		editor.putString("openid",userInfo.getOpenid());  //第三方登录openid
		editor.putString("loginID",userInfo.getLoginID());  //登录标识
		editor.commit();
	}

	public static void saveUserInfoId(Context context,String id,UserInfoBean userInfo){
		if (id!=null){
			Name=id;
		}
		Editor editor = getShared(context).edit();
		editor.putInt("userid", userInfo.getUserid());
		editor.putString("username", userInfo.getUsername());
		editor.putString("realname", userInfo.getRealname());
		editor.putString("phonenum", userInfo.getPhonenum());
		editor.putInt("sex",userInfo.getSex());
		editor.putString("headurl", userInfo.getHeadurl());
		editor.commit();
		Name= "activityplan";
	}
	
	public static UserInfoBean getUserInfoId(Context context,String id){
		if (TextUtils.isEmpty(id)) {
			return null;
		}
		Name=id;
			UserInfoBean userInfo = new UserInfoBean();
			userInfo.setHeadurl(getShared(context).getString("headurl", ""));
			userInfo.setRealname(getShared(context).getString("realname", ""));
			userInfo.setSex(getShared(context).getInt("sex", 0));
			userInfo.setUserid(getShared(context).getInt("userid", 0));
			userInfo.setUsername(getShared(context).getString("username", ""));
			userInfo.setPhonenum(getShared(context).getString("phonenum", ""));
		Name= "activityplan";
			return userInfo ;
	}
	public static UserInfoBean getUserInfo(Context context){
		UserInfoBean userInfo = new UserInfoBean() ;
		userInfo.setAlipayaccount(getShared(context).getString("alipayaccount", ""));
		userInfo.setAlipayusername(getShared(context).getString("alipayusername", "")) ;
		userInfo.setBankaccount(getShared(context).getString("bankaccount", ""));
		userInfo.setBankcity(getShared(context).getString("bankcity", ""));
		userInfo.setBankname(getShared(context).getString("bankname", ""));
		userInfo.setBanksubsidiary(getShared(context).getString("banksubsidiary", ""));
		userInfo.setBankusername(getShared(context).getString("bankusername", ""));
		userInfo.setCity(getShared(context).getString("city", ""));
		userInfo.setEmail(getShared(context).getString("email", ""));
		userInfo.setHeadurl(getShared(context).getString("headurl", ""));
		userInfo.setPassword(getShared(context).getString("password", ""));
		userInfo.setPhonenum(getShared(context).getString("phonenum", ""));
		userInfo.setQqkey(getShared(context).getString("qqkey", ""));
		userInfo.setRealname(getShared(context).getString("realname", ""));
		userInfo.setSex(getShared(context).getInt("sex",0));
		userInfo.setId(getShared(context).getInt("id",0));
		userInfo.setSignature(getShared(context).getString("signature", ""));
		userInfo.setUserid(getShared(context).getInt("userid", 0));
		userInfo.setUsername(getShared(context).getString("username", ""));
		userInfo.setWeibokey(getShared(context).getString("weibokey", ""));
		userInfo.setWeixinkey(getShared(context).getString("weixinkey",""));
		userInfo.setToken(getShared(context).getString("token", "")) ;
		userInfo.setThirdid(getShared(context).getString("thirdid", ""));
		userInfo.setOpenid(getShared(context).getString("openid",""));
		userInfo.setLoginID(getShared(context).getString("loginID",""));
		return userInfo ;
	}
	public static boolean saveArray(Context context, List<String>imglist) {
		Editor editor = getShared(context).edit();
		editor.putInt("list_size",imglist.size());

		for(int i=0;i<imglist.size();i++) {
			editor.remove("Status_" + i);
			editor.putString("Status_" + i, imglist.get(i));
		}
		return editor.commit();
	}

	public static List<String> loadArray(Context mContext) {
		int size = getShared(mContext).getInt("list_size", 0);

		List<String>list =new ArrayList<>();
		for(int i=0;i<size;i++) {
			list.add(getShared(mContext).getString("Status_" + i, ""));
		}
		return list;
	}


	public static void saveHostHeadUrl(Context context,String headurl){
		Editor editor = getShared(context).edit();
		editor.putString("hostheadurl", headurl);
		editor.commit();
	}

	public static String getHostHeadUrl(Context context){
		return getShared(context).getString("hostheadurl", "") ;
	}
}
