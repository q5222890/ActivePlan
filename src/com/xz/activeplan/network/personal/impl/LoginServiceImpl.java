package com.xz.activeplan.network.personal.impl;

import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.Param;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.ILoginService;
import com.xz.activeplan.ui.UrlsManager;

/**
 * 登陆接口网络请求具体实现类
 * @author johnny
 *
 */
public class LoginServiceImpl implements ILoginService {

	
	private static ILoginService mILoginService ;
	
	
	private  LoginServiceImpl() {
	}
	
	public static ILoginService getInstance(){
		if(mILoginService ==null){
			synchronized (LoginServiceImpl.class) {
				if(mILoginService == null){
					mILoginService = new LoginServiceImpl() ;
				}
			}
		}
		return mILoginService ;
	}

	@Override
	public void login(String name, String password,StringCallback stringCallBack) {
		// 传参数修改key value
		Param paramName = new Param("phonenum", name) ;
		Param paramPWD = new Param("password", password) ;
		
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_LOGIN,stringCallBack, paramName,paramPWD);
	}

	@Override
	public void forgetPwd(String phone, String newPwd,String code,StringCallback stringCallBack) {
		Param paramPhone = new Param("phonenum",phone) ;
		Param paramNewPwd = new Param("password",newPwd) ;
		Param paramCode = new Param("code",code) ;
		OkHttpClientManager.postAsyn(UrlsManager.FORGET_PWD_URL, stringCallBack, paramPhone,paramNewPwd,paramCode) ;
		
	}

	@Override
	public void register(String name, String password,String code,StringCallback stringCallBack) {
		Param paraName = new Param("phonenum",name) ;
		Param paramPwd = new Param("password",password) ;
		Param paramCode = new Param("code",code) ;
		OkHttpClientManager.postAsyn(UrlsManager.REGISTER_URL,stringCallBack, paraName,paramPwd,paramCode) ;
	}

	@Override
	public void modifyPwd(String userName, String oldPwd, String newPwd,String code,StringCallback stringCallBack) {
		Param paraName = new Param("phonenum",userName) ;
		Param paramOldPwd = new Param("oldpwd",oldPwd) ;
		Param paramNewPwd = new Param("newpwd",newPwd) ;
		Param paramCode = new Param("code",code) ;
		OkHttpClientManager.postAsyn(UrlsManager.MODIFY_URL, stringCallBack, paraName,paramOldPwd,paramNewPwd,paramCode) ;
		
	}

	/**
	 * 获取验证码（带安全验证）
	 * @param phonenum 电话号码
	 * @param type 类型
	 * @param randomCode 随机验证码
	 * @param stringCallback 回调
     */
	@Override
	public void getCode(String phonenum,String type,String randomCode, StringCallback stringCallback) {
		Param paraName = new Param("phonenum",phonenum) ;
		Param paraType = new Param("type",type) ;
		Param valiCode = new Param("valiCode",randomCode) ;
		OkHttpClientManager.postAsyn(UrlsManager.GET_CODE_URL, stringCallback, paraName,paraType,valiCode) ;
	}

	@Override
	public void bingPhone(String type, String phonenum, String openid, String username,
						  int sex, String headurl, String code,StringCallback stringCallback) {

		Param paraType = new Param("type",type) ;
		Param phonenum1 = new Param("phonenum",phonenum) ;
		Param openid1 = new Param("openid",openid) ;
		Param username1 = new Param("username",username) ;
		Param sex1 = new Param("sex",sex+"") ;
		Param headurl1 = new Param("headurl",headurl) ;
		Param code1 = new Param("code",code) ;
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.THIRDLOGIN_BIND, stringCallback,
				paraType,phonenum1,openid1,username1,sex1,headurl1,code1) ;
	}
}
