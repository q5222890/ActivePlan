package com.xz.activeplan.network.personal;

import com.xz.activeplan.network.OkHttpClientManager.StringCallback;

/**
 * 登陆网络请求接口
 * @author johnny
 */
public interface ILoginService {
	/**
	 * 登陆请求方法
	 * @param name 用户名
	 * @param password 密码
	 */
	public void login(String phone,String password,StringCallback stringCallBack);
	
	/**
	 * 忘记密码
	 * @param phone 电话号码
	 * @param newPwd 新密码
	 */
	public void forgetPwd(String phone,String newPwd,String code,StringCallback stringCallBack) ;
	
	/**
	 * 注册账号 
	 * @param name 用户名 
	 * @param password 密码
	 */
	public void register(String name,String password,String code,StringCallback stringCallBack) ;
	
	/**
	 * 修改密码
	 * @param userName 用户名
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 */
	public void modifyPwd(String userName,String oldPwd,String newPwd,String code,StringCallback stringCallBack) ;
	
	/**
	 * 4.1.43.	获取验证码
	 * @param phonenum 电话号码
	 * @param type 类型
	 * @param randomCode 随机验证码
	 * @param stringCallback 回调
	 */
	public void getCode(String phonenum,String type,String randomCode,StringCallback stringCallback);


	/**
	 * 第三方登录——绑定手机号码
	 *
	 * @param type    第三方登录标识（type=qq 或者 weixin）
	 * @param phonenum  手机号
	 * @param openid    openid
	 * @param username  姓名
	 * @param sex       性别
	 * @param headurl  头像
     * @param code     验证码
     */
	public void bingPhone(String type,String phonenum,String openid,
						  String username,int sex,String headurl,String code,StringCallback stringCallback);

}
