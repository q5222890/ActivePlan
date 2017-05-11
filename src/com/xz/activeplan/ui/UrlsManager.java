package com.xz.activeplan.ui;

public class  UrlsManager {

	//分享活动的URL
	public static final String ACTIVEINFO_URL ="http://www.yueba.cc/activty/activtyinfo/index.html?activeid=";
	/**
	 * 服务器url
	 */                                   //139.196.152.82
	public static final String URL = "http://139.196.152.82/tryst" ;
	/*********微信支付常量********/
	public static final String APP_ID = "wx3f8723fb7da40f18";
	public static final String MCH_ID = "1345149801" ;
	public static final String API_KEY = "qq584968058qq584968058q584968058" ;
	//http:登陆url
	public static final String URL_LOGIN = URL +"/loginauth" ;
	//忘记密码url
	public static final String FORGET_PWD_URL = URL +"/userinfo?action=forgetpwd" ;
	// 注册url
	public static final String REGISTER_URL = URL +"/register" ;
	// 修改密码
	public static final String MODIFY_URL = URL + "/modifypwd" ;
	// 4.1.43.	获取验证码
	public static final String GET_CODE_URL = URL + "/register?action=code" ;
	// 个人信息保存
	public static final String SAVE_USER_URL = URL +"/saveuser" ;
	//票务
	public static final String MY_TICKET_URL = URL +"/myticket" ;
	//获取管理报名
	public static final String URL_MANAGER_SINGUP = URL+"/myticket?action=signSpManage";
	//我的收藏
	public static final String MY_COLLECT = URL +"/mycollect";
	// 4.1.7.	关注的主办方
	public static final String MY_HOST = URL +"/myhost";
	// 4.1.8.	同伴
	public static final String URL_MY_FRIEND = URL +"/myfriend";
	// 4.1.9.	活动列表(类别)
	public static final String MY_CATEGORY = URL +"/mycategory";//http://139.196.152.82/tryst/mycategory
	//   活动列表排序
	public static final String MY_CATEGORY_ORDER =URL+"/screen";
	// 4.1.10.	推送活动
	public static final String MY_PUSHACTIVE = URL +"/pushactive";
	//4.1.11.	名师
	public static final String URL_CELEBRITY = URL +"/teacher";
	//4.1.11.	名人清单
	public static final String URL_CELEBRITE_LIST = URL +"/type?action=class&class_code=C0002";
	//4.1.11.	场地清单
	public static final String URL_AREA = URL +"/type?action=class&class_code=C0003";
	// 4.1.12.	获取名师详情
	public static final String URL_CELBRITY_DETAILS = URL +  "/teacher" ;
	// 4.1.12.	我的名人堂粉丝
	public static final String URL_MY_CELBRITY_DETAILS = URL +  "/teacher?action=attention&param=fans" ;
	// 4.1.12.	获取名人视频
	public static final String URL_CELBRITY_VEDIO = URL +  "/teacher?action=video" ;
	// 4.1.12.	得到名人的评价记录
	public static final String URL_CELBRITY_EVALUATE = URL +  "/teacher?action=comment" ;
	//邀请名师---查看订单是否创建
	public static final String Url_INVITY_CELEBRITY_ESTABLISH=URL+"/userinfo?action=teacher&param=invite";
	//4.1.25.	邀请名师---创建订单
	public static final String URL_INVITY_CELEBRITY = URL + "/teacher?action=invite" ;
	//我的名人堂-收到的邀请:
	public static final String URL_MY_INVITY_CELEBRITY = URL + "/userinfo?action=teacher&param=accept" ;
	//我的名人堂-发起的邀请:
	public static final String URL_MY_SEND_INVITION = URL + "/userinfo?action=teacher&param=send" ;
	//我的名人堂-拒绝邀请:
	public static final String URL_MY_REFUSEINVITY_CELEBRITY = URL + "/teacher?action=refuse" ;
	//我的名人堂---接受邀请
	public static final String URL_MY_AGREE_CELEBRITY = URL + "/teacher?action=confirm" ;
	//评价提交
	public static final String URL_PUBLISHED_EVALUATION=URL+"/userinfo?action=teacher&param=comment";
	//退款提交
	public static final String URL_REFUND=URL+"/userinfo?action=teacher&param=refund";
	//拒绝退款
	public static final String URL_REFUND_REFUND=URL+"/teacher?action=refuse&param=refund";
	//同意退款
	public static final String URL_AgreeREFUND_REFUND=URL+"/teacher?action=confirm&param=refund";
	//邀请方确认出席
	public static final String URL_INVITION_CONFIRM=URL+"/userinfo?action=teacher&param=confirm";
	//提醒名人
	public static final String URL_REMIND_CELEBRITES=URL+"/teacher?action=remind&param=attend";
	//关注和取消关注
	public static final String URL_GUANZHU_CELEBRITES=URL+"/teacher?action=attention";
	//判断有没有关注
	public static final String URL_GUANZHU_YOU_CELEBRITES=URL+"/teacher?action=attention&param=follows";
	//跳过
	public static final String URL_SKIP=URL+"/userinfo?action=teacher&param=jump";
	//4.1.12.	获取用户信息
	public static final String GET_USERINFO_ID_URL = URL + "/userinfo?action=userid" ;
	// 4.1.12.	获取用户信息
	public static final String GET_USERINFO_USERNAME_URL = URL + "/userinfo?action=phonenum" ;
	// 4.1.13.	收藏
	public static final String COLLECT_ACTION_URL = URL + "/mycollect?action=collect" ;
	// 4.1.29.	取消收藏
	public static final String CANCEL_COLLECT_ACTION_URL = URL + "/mycollect?action=cancel_collect" ;
	//4.1.14.	保存活动
	public static final String ACTIVE_ACTION_SAVE_URL =  URL + "/active?action=save";
	//4.1.15.	获取活动
	public static final String ACTIVE_ACTION_GET = URL +  "/active?action=get" ;
	//4.1.16.	搜索活动
	public static final String ACTIVE_ACTION_SEARCH = URL +  "/active?action=search" ;
	//4.1.17.	上传用户头像
	public static final String USERINFO_UPLOAD_HEADIMG = URL +  "/userinfo?action=headurl" ;
	//4.1.18. 关注关注个人
	public static final String ATTEND_FRIEND = URL +  "/myfriend?action=attend" ;
	// 4.1.18.	取消关注个人
	public static final String CANCEL_FRIEND = URL +  "/myfriend?action=cancel" ;
	// 4.1.19.	关注关注主办方
	public static final String ATTEND_HOST = URL +  "/myhost?action=attend" ;
	//4.1.19.	取消关注主办方
	public static final String CANCEL_HOST = URL +   "/myhost?action=cancel" ;
	// 4.1.20.	获取主办方
	public static final String GET_HOST_INFO = URL +  "/myhost?action=get" ;
	// 4.1.22.	获取名师推荐
	public static final String URL_CELBRITY_RECOMMEND =  URL + "/teacher?action=push" ;
	// 4.1.26.	获取群成员
	public static final String GROUP_MEMBER = URL + "/saveuser?action=groupmember" ;
	//4.1.23.	修改主办方
	public static final String MODIFY_HOST = URL + "/myhost?action=modify" ;
	//.1.22.	根据userid获取主办方
	public static final String GET_USER_HOSTID = URL + "/myhost?action=getuserid";
	// 4.1.26.	搜索名师
	public static final String UTL_SEARCH_CELEBRITY = URL + "/teacher?action=search" ;
	// 4.1.27.	主办方(发布的活动)
	public static final String HOAST_POST_ACTIVE = URL + "/active?action=host" ;
	// 4.1.25.	邀请名师
	public static final String INVITE_TEACHER = URL + "/teacher?action=invite" ;
	// 4.1.32.	验票
	public static final String CHECK_TICKET = URL + "/myticket?action=check" ;
	// 4.1.31.	活动报名者列表
	public static final String TICKET_LIST = URL + "/myticket?action=activeid" ;
	// 4.1.30.	活动报名
	public static final String APPLY_ACITVE = URL + "/myticket?action=apply" ;
	//4.1.34.	获取个人账户
	public static final String GET_ACCOUNT_URL = URL + "/account?action=getaccount" ;
	//4.1.36.	增加账户流水
	public static final String ADD_DATAIL_URL = URL + "/account?action=adddetail" ;
	// 4.1.37.	查询账户流水
	public static final String GET_DATAIL_URL = URL + "/account?action=getdetail"  ;
	//4.1.38.	版本升级
	public static final String UPDATEVER_URL = URL + "/updatever" ;
	// 4.1.41.	意见反馈
	public static final String FEEDBACK_URL = URL +"/mycenter?action=feedback" ;
	// 4.1.40.	首页广告
	public static final String GETAD_URL = URL + "/mycenter?action=getad" ;
	// 4.1.42.	第三方登录
//	public static final String THIRDLOGIN_URL = URL + "/userinfo?action=thirdlogin" ;
	public static final String THIRDLOGIN_URL = URL + "/loginauth?action=thirdlogin";
	// 4.1.43.  第三方登入绑定系统账号
	public static final String THIRDLOGIN_BIND = URL + "/loginauth?action=binding";
	// 5.1.1  最新直播
	public  final static String URL_NEWLIVE = URL+"/live?action=new&currpage=0&pagesize=4";
	public final static String URL_NEWLIVE_ALL= URL +"/live?action=new";
	//  5.1.2  最新回放
	public final static String URL_NEWBLACKPLAY=URL+"/live?action=back";
	//  5.1.3  直播轮播广告
	public final static String URL_LIVETVPAGE_TITLEBANNER=URL+"/adPlace?action=getad&targets=2&type=";
	// 5.1.4  直播---关注
	//public final static String URL_LIVE_GUANZHU =URL+"/userinfo?action=friend&param=get&userid=1&currpage=0";
	//  5.1.5明日之星、主播
	public final static String URL_LIVE_TOMMORROW=URL+"/live?action=hot&currpage=";
	// 5.1 总播放量
	public final static String URL_LIVE_PLAYALL=URL+"h/live?action=plays&userid=1";
	// 5.2 粉丝数量
	public final static String URL_LIVE_PANSALL =URL+"/userinfo?action=friend&param=fans&userid=1";
	// 5.3 主办方排行榜
	public final static String URL_LIVE_SPONSOR =URL+"/myhost?action=hot";
	// 5.2 直播 -推荐页广告轮播
	public final static String URL_LIVE_RECOMMEND_AD =URL+"/adPlace?action=getad&param=0&type=2";
	// 直播---主播GridVeiw
	public final static String URL_LIVE_ANCHOR = URL+"/userinfo?action=users&param=hot&currpage=";
	// 直播 -本周排行榜
	public final static String URL_LIVE_RECOMMEND_WEEKCHARTS = URL+"/live?action=hotWeek&currpage=";
	//搜索好友http:
	public final static String URL_SEARCH_MYFRIEND = URL+"/userinfo?action=phonenum&phonenum=";
	//发起直播 http://192.168.1.110/tryst(本地测试)
	public final static String URL_SEND_LIVE = URL+"/live?action=add";
	//创建直播频道（使用暴风云）
	public final static String BF_CREATE_LIVE = "http://channelmgr.baofengcloud.com/createchannel";
	// 7.1.1活动粉丝
	public final static String URL_ACTIVEFANS = URL+"/myhost?action=activefans&userid=51&currpage=0&pagesize=3";
	//7.1.2 直播粉丝
	public final static String URL_LIVEFANS =URL+"/mylive?action=getmylivefans&userid=1&currpage=0&pagesize=3";
	//7.1.3 直播分类
	public final static String URL_LIVECATEGOTY =URL+"/live?action=categoryid";
    //视频关注量(含：视频量、粉丝量、关注量)
	public final static String URL_VIDEO_FANSCOUNT =URL+"/mylive?action=clickhead";
	//获取视频的播放数量
	public final static String CELEBRITE_END= URL+"/live?action=addSeenum";
	//获取视频的播放数量
	public final static String URL_VIDEO_PLAY_COUNT= URL+"/live?action=addSeenum";
    //直播模块--->首页分类 (活动现场1、户外现场2、全名直播3、全部分类0)    参数：   categoryid=0&currpage=0&pagesize=16
	public final static String URL_VIDEO_CLASSIFY =URL + "/live?action=categoryid";
	//活动分类获取活动信息和广告图片
	public final static String URL_CATEGORYINFO =URL + "/type?action=active&param=actid";
	//关注的主播
	public final static String URL_FOLLOWANCHOR =URL + "/mylive?action=getmylivefollows";
	//我的关注列表
	public final static String URL_FOLLOW_LIST = URL +"/live?action=myfollow&currpage=";

	//根据用户ID获取他的所有视频(参数：1.用户ID、2.当前页数、3.每页的数量)
	public final static String  URL_VIDEO_LIST = URL+"/live?action=mylivemanage&currpage=";

	//直播模块的所有轮播地址(参数：pushlivetype = 1、2、3   分别表示：直播首页、推介、主播)
	public final static String URL_LIVE_BANNER = URL +"/live?action=pushlive&pushlivetype=";

	//打赏页面(参数  频道ID)
	public final static String URL_REWARD =URL+"/live?action=getchannelid&channelid=";

	//统计聊天室的人数(传入房间id)
	public final static String URL_CHAT_ROOM_NUMBER =URL+"/live?action=queryChatroomUser&count=500&order=1&chatroomId=";

	//获取票务信息
	public final static String URL_TICKETINFO = URL+"/myticket?action=getTempTicket";
	//更新票务数据
	public final static String URL_UPDATA_TICKETINFO =URL+"/myticket?action=updateTempTicket";
	//关注主播(参数 userid(当前用户ID)    otheruserid(要关注的用户ID)
	public final static String URL_FOLLOW_ANCHOR =URL + "/mylive?action=addmylivefans";
	//取消关注主播 userid(当前用户ID)     otheruserid(要关注的用户ID)
	public final static String URL_CANCEL_FOLLOW_ANCHOR =URL + "/mylive?action=delmylivefans";
	//实名认证Authentication
	public final static String URL_AUTHENTICATION =URL + "/authentication?action=save";
	//获取认证状态
	public final static String URL_AUTHENTIC_STATUS = URL +"/authentication?action=isauthentication";
	//搜索主播
	public final static String URL_ANCHOR_SEARCH= URL+"/live?action=screenlive&currpage=";

	//搜索的 相关视频列表            0&pagesize=4&orderby=timeasc&key=神
    public final static String URL_SEARCH_VIDEO = URL +"/live?action=relevantlive&currpage=";
	//创建直播请求的回调接口
	public final static String URL_BF_CALLBACK="http://yueba.cc/tryst/callback";
    //加密视频密码校验  liveid=48&password=123456
	public final static String URL_VIDOE_PASSWORD= URL+"/live?action=checkpassword&liveid=";

	//获取网络图片地址
	public final static String URL_EDITOR_POHOT_CHANGE = URL+"/upload?action=simple";

	//打赏微信支付    参数 ：&userid=88&liveid=88&payamount=10&describe1=描述;
	public final static String URL_REWARD_WEIXIN_PAY ="http://yueba.cc/tryst/livered?action=addlivered";

	//购买视频（支付宝、微信） &userid=10&liveid=10&payamount=1000.0
	public final static String URL_BUY_VIDEO = URL +"/buylive?action=prebuylive";

    //判断付费视频是否已经购买过  &userid=88&liveid=88
	public final static String URL_Already_Buy=URL +"/buylive?action=isbuylived";

	//我的购买视频列表    &userid=1&currpage=0&pagesize=10
	public final static String URL_MY_BUYVIDEO =URL+ "/buylive?action=getliveinfobyuserid&currpage=";

	//举报视频 &userid=8&liveid=88&QQ=123456&describe1=888
	public final static String URL_REPORT_VIDEO= URL +"/livereport?action=addreport";

	//一登录入信息
	public final static String URL_SuperID_INFO = URL +"/face?action=addface";

	//获取随机数的验证码（4位数）
	public final static String URL_REG_YZM= URL+"/register?action=code&param=valiCode";
	//选择验票活动
	public final static String URL_SELECT_TICKET_ACTION = URL +"/checkTicketActiveList";
    //刷脸验票接口
    public final static String URL_CHECK_FACE =URL +"/face?action=checkface";



	public final static String END_ONE_DAY="one_day";
	public final static String END_SEVEN_DAY="seven_day";

	//默认Token
	public final static String RONG_DISCONNECT_TOKEN="JTu+o1B03NffcNjjdAFDqdyyXTy0AHd0wPNm02et4o80NUlp1a42qzzuco0N5YsfblUNoV2teIBNdqCKEqla2g==";
	/*******************************************************************/
	/*****************************************************************************************
	 *
	 * 支付宝常量
	 */
	// 商户PID
	public static final String PARTNER = "2088222576426703";
	// 商户收款账号
	public static final String SELLER = "admin@uxingzhe.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDmAFaAOsBgKcNhAZWFn1DnI7BPhYy39y5uHm0s/43Zbjpnevs8PWUhkWqzAVscdAe3MS/Ytb+AsW6KKMp1wU3yqLpZ+8GHovlRUWEt7frSDkXedpZ9SO1bkMzLAtXQbwGjGKiUtt2rb7cOv3dle+j2m0SOn7rYcZkj0loUXb8FUwIDAQABAoGACjAjpMR1dw0CiY5/aA7Zj5OGIM+inxeE5/3TCjY7u1Dlp2XMMSvrCeSeHFXICmedW+EC5I+QbwvtAJOz/ClW4X5WfxC4+sqDvSpFH7FEz2/52LWNEhSnq/CgltnmxzBqQPJeJTsKa/RXt1h0cO7khuCDlW66bkNS30oD9wbyyBECQQDz40VZG2BvgZZLuBfzMj3DrKg35Sv+wuveDTE3kmXgsRf/UL96fwxP376hbaX2jNsRxS5HFbGPaYG3IfZDRNsLAkEA8WyEnEu39OXmwsqC2mAbO5UBvW7ybV+PJ167WVyKdjN/qxqrETCW8alO3h9+2ichAD3cIUb4sko2AHoTcNOr2QJBAOmkkIsM7xXjz1g6xLb3KrSKc50Yr00g71WKzduvJGpdPeAaO1Xe4KykbLu5j4Ti6/vaKrtuzOvW563Jm2JWVIECQG6WSthdRtKDTQUFRpNJWrAcPUPMwnefQi/CkQcKANLMHDsAaEPjggEIkPvmaXD8Y3182IzVua3RmJL3WFhiXWECQQDEGa72r0R9Lv8+xZtEzeUw5jiuhu3gpK02mxizaq9aAaYHEZsDGeqH5lIGd3rhUkhMib448simR2otNxQfMHyJ";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static final int SDK_PAY_FLAG = 1;
	/*****************************************
	 *
	 * 暴风常量 appKey
	 *
	 */
	// 请填写您的sk ak  这两个key可以从   http://www.baofengcloud.com/user/userinfo 获取
	public static final String SecretKey = "ocddhokn1jLflQpZb667Qd09bXAUvKxt9i6hTfiT";
	public static final String AccessKey = "=X6dhokn1S09lQY51667QdQs9c7jPYnXWZQPd3Y-";
	/**
	 * 生产环境
	 */
	public static String key = "lmxuhwagxv3kd";   // 替换成您的appkey
	public static String secret = "810oUM0A6wTcA";// 替换成匹配上面key的secret
}

