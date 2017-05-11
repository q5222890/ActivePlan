package com.xz.activeplan.utils.observer;

import java.io.Serializable;

import io.rong.imlib.model.Conversation;

/**
 * 事件type
 * 
 */
public class EventType implements Serializable {
	
	public static final int CLICK_TYPE = 0x01;	//点击事件
	public static final int NOTIFY_TYPE = 0x02;	//通知
	public static final int LOGIN_NOTIFI_TYPE = 0x03 ;//登录通知
	public static final int CITY_NOTIFI_TYPE = 0x04 ;//城市通知
	public static final int LOGOUT_NOTIFI_TYPE = 0x05; //退出登录通知
	public static final int QUITE_CHART_TYPE = 0x06 ;//退出聊天界面
	public static final int MAP_ADDRESS_TYPE = 0x07 ;//百度地图地址
	public static final int ACCOUNT_CHANGE_TYPE = 0x08 ;//支付宝账号更改
	public static final int ACCOUNT_TIXIANE_TYPE = 0x09 ;//提现
	public static final int ACCOUNT_BAOMING_TYPE = 1001 ;//报名
	public static final int MAIN_HEAD_CITY = 1002 ; //城市
	public static final int MAIN_HEAD_SEARCH = 1003 ;// 搜索
	public static final int MAIN_HEAD_ADD = 1004 ;// 发布活动
	public static final int MAIN_HEAD_TEACHER_SEARCH = 1005 ;//
	public static final int ACTIVITE_TICKET_ADD = 1006 ;// 添加票务
	public static final int TEACHER_INVITE_TYPE = 1007 ;// 教师邀请
	public static final int WEIXIN_PAY_SUCCESS = 1008 ; //微信支付成功
	public static final int WEIXIN_PAY_ERROR = 1009 ; //微信支付失败
	public static final int ALI_PAY_SUCCESS = 1008 ; //支付宝支付成功
	public static final int ALI_PAY_ERROR = 1009 ; //支付宝支付失败
	public static final int LAUNCH_LIVE = 1010 ; // 发起直播
	public static  final int ORDER_PAYMENT=1011;//消息类型区分
	public static final int A_REWARD =1000; //打赏
	public static final int INPUT_FACE_SUCCESS = 0x1012;// 脸部录入成功消息
	private static final long serialVersionUID = 3160563426497038631L;
	public static  Conversation.ConversationType CONVERSATION_TYPE;//消息类型区分
}
