package com.xz.activeplan.network.personal;

import com.xz.activeplan.network.OkHttpClientManager.StringCallback;

/**
 * 用户信息网络请求具体实现类
 *
 * @author johnny
 */
public interface IUserInfoService {

    /**
     * 保存用户信息
     *
     * @param userId    用户id
     * @param userName  用户姓名
     * @param city      城市
     * @param realName  真实姓名
     * @param signature 个性签名
     * @param phonenum  电话号码
     * @param sex       性别1男，0女
     * @param email     邮箱
     */
    void saveUserInfo(int userId, String userName, String city,
                      String realName, String signature, String phonenum, String sex,
                      String email, String alipayaccount, String alipayusername, StringCallback stringCallback);

    /**
     * 4.1.5. 票务
     *
     * @param userId   用户id
     * @param type     =未过期, 0=已过期
     * @param currpage 当前页数
     *                 , 第几页, 默认从0开始
     * @param pagesize 一页几条
     *                 (默认20条)
     */
    void myTicket(int userId, int type, int currpage, int pagesize,
                  StringCallback stringCallback);

    /**
     * 4.1.6. 我的收藏
     *
     * @param userId   用户id
     * @param currpage 当前页数
     *                 , 第几页
     * @param pagesize 一页几条
     *                 (默认20条)
     */
    void myCollect(int userId, int currpage, int pagesize,
                   StringCallback stringCallback);

    /**
     * 4.1.7. 关注的主办方
     *
     * @param userId   用户id
     * @param currpage 当前页数
     *                 , 第几页
     * @param pagesize 一页几条
     *                 (默认20条)
     */
    void myHost(int userId, int currpage, int pagesize,
                StringCallback stringCallback);

    void myHostList(int userId, int type, int currpage, int pagesize,
                    StringCallback stringCallback);
    /**
     * 4.1.8. 同伴
     *
     * @param userId   用户id
     * @param currpage 当前页数
     *                 , 第几页
     * @param pagesize 一页几条
     *                 (默认20条)
     */
    void myFriend(int userId, int currpage, int pagesize,
                  StringCallback stringCallback);

    /**
     * 4.1.9. 活动列表(类别)
     *
     * @param addr       城市, 比如深圳市
     * @param categoryid 类别ID(最新=0爱心吧= 1
     *                   相亲吧=2商务吧=3老乡吧=4创业吧=5运动吧=6学习吧=7旅游吧=8生活吧=9校园吧=10群星吧=11同性吧=12其它吧=
     *                   13)
     * @param currpage   当前页数
     *                   , 第几页
     * @param pagesize   一页几条
     *                   (默认20条)
     */
    void myCategory(String addr, int categoryid, int currpage,
                    int pagesize, StringCallback stringCallback);

    /***
     * @param categoryid     分类id
     * @param stringCallback
     */
    void getActiveCategoryInfo(int categoryid, StringCallback stringCallback);

    /***
     * @param addr           城市
     * @param categoryid     分类id
     * @param currpage       当前页数，第几页
     * @param pagesize       一页几条 默认20条
     * @param comprehensive  综合热门排序
     * @param startdate      时间排序
     * @param charge         是否收费
     * @param stringCallback 回调接口
     */
    void myCategoryOrder(String addr, int categoryid, int currpage,
                         int pagesize, String comprehensive, int startdate,
                         int charge, StringCallback stringCallback);


    /**
     * 4.1.10. 推送活动
     *
     * @param addr     城市, 比如深圳市
     * @param pushid   tab轮播 = 1精彩推送 = 2
     * @param currpage 当前页数
     *                 , 第几页
     * @param pagesize 一页几条
     *                 (默认20条)
     */
    void pushactive(String addr, int pushid, int currpage, int pagesize,
                    StringCallback stringCallback);

    /**
     * 4.1.12. 获取用户信息
     *
     * @param username       userid/username
     * @param stringCallback 回调接口
     */
    void getUserinfo(String username, StringCallback stringCallback);

    /**
     * 4.1.12. 获取用户信息
     *
     * @param userid         userid/username
     * @param stringCallback 回调接口
     */
    void getUserinfo(int userid, StringCallback stringCallback);

    /**
     * 4.1.13. 收藏
     *
     * @param userid         用户ID
     * @param activeid       活动id
     * @param stringCallback 回调接口
     */
    void collectAction(int userid, int activeid,
                       StringCallback stringCallback);

    /**
     * 4.1.29.	取消收藏
     *
     * @param userid
     * @param activeid
     * @param stringCallback
     */
    void cancelCollect(int userid, int activeid,
                       StringCallback stringCallback);

    /**
     * 4.1.17. 上传用户头像
     *
     * @param userid         用户id
     * @param path           头像路径
     * @param stringCallback 回调接口
     */
    void uploadHeadImg(int userid, String path,
                       StringCallback stringCallback);

    /**
     * 4.1.18. 关注/取消关注个人
     *
     * @param userid         用户id
     * @param friendid       被关注用户id
     * @param stringCallback 回调接口
     */
    void attendFriend(int userid, int friendid,
                      StringCallback stringCallback);

    /**
     * 4.1.18. 关注/取消关注个人
     *
     * @param userid   用户id
     * @param friendid 被关注用户的id
     */
    void cancelFriend(int userid, int friendid,
                      StringCallback stringCallback);

    /**
     * 4.1.19. 关注关注主办方
     *
     * @param userid         用户id
     * @param hostid         主办方id
     * @param stringCallback 回调接口
     */
    void attendHost(int userid, int hostid, StringCallback stringCallback);

    /**
     * 4.1.19. 取消关注主办方
     *
     * @param userid         用户id
     * @param hostid         主办方id
     * @param stringCallback 回调接口
     */
    void cancelHost(int userid, int hostid, StringCallback stringCallback);

    /**
     * 4.1.20. 获取主办方
     *
     * @param userid         用户id
     * @param hostid         主办方id
     * @param stringCallback 回调接口
     */
    void getHostInfo(int userid, int hostid,
                     StringCallback stringCallback);

    /**
     * 4.1.26. 获取群成员
     *
     * @param groupid
     * @param stringCallback
     */
    void groupmember(String groupid, StringCallback stringCallback);

    /**
     * 4.1.23.	修改主办方
     *
     * @param userid         用户名id(一个用户对应一个主办方)
     * @param hostname       主办方单位
     * @param hostcontact    主办方联系人
     * @param hostphone      主办方联系号码
     * @param hostmail       主办方邮箱
     * @param hostintro      主办方简介
     * @param hosthearurl    主办方头像
     * @param hosturl        主办方网址
     * @param stringCallback 回调接口
     */
    void modifyHost(int userid, String hostname, String hostcontact,
                    String hostphone, String hostmail, String hostintro,
                    String hosthearurl, String hosturl, StringCallback stringCallback);

    /**
     * 4.1.22.	根据userid获取主办方
     * 功能描述：获取主办方信息
     * 接口地址：http://localhost:8080/tryst/myhost?action=getuserid
     *
     * @param userid
     * @param stringCallback 回调接口
     */
    void getUserHostId(int userid, StringCallback stringCallback);


    /**
     * 4.1.27.	主办方(发布的活动)
     *
     * @param hostid         主办方id
     * @param type           1=未过期, 0=已过期
     * @param currpage       当前页数, 第几页, 默认从0开始
     * @param pagesize       一页几条(默认20条)
     * @param stringCallback 回调接口
     */
    void hostPostActive(int hostid, int type, int currpage, int pagesize, StringCallback stringCallback);

    /**
     * 4.1.32.	验票
     *
     * @param ticket         票号
     * @param stringCallback 回调接口
     */
    void checkTicket(String ticket, int activeid,StringCallback stringCallback);

    /**
     * 4.1.31.	活动报名者列表
     *
     * @param activeid       活动id
     * @param stringCallback 回调接口
     */
    void ticketList(int activeid, StringCallback stringCallback);

    /**
     * 4.1.34.	获取个人账户
     *
     * @param userid         用户id
     * @param phonenum       电话号码
     * @param stringCallback 回调接口
     */
    void getMoneyAccount(String userid, String phonenum, StringCallback stringCallback);

    /**
     * 4.1.36.	增加账户流水
     *
     * @param userid         用户id
     * @param phonenum       电话号码
     * @param num            金额
     * @param type           流水类型: 1=收入, 2=支出, 3=提现
     * @param intro          备注
     * @param stringCallback 回调接口
     */
    void adddetail(String userid, String phonenum, double num, int type, String intro, StringCallback stringCallback);

    /**
     * 4.1.37.	查询账户流水
     *
     * @param userid         用户id
     * @param currpage       当前页,从0开始
     * @param pagesize       页数
     * @param stringCallback 回调接口
     */
    void getDetail(String userid, int currpage, int pagesize, StringCallback stringCallback);

    /**
     * 4.1.38.版本升级
     *
     * @param versioncode    版本序列号
     * @param stringCallback 回调接口
     */
    void updatever(String versioncode, StringCallback stringCallback);

    /**
     * 4.1.41.	意见反馈
     *
     * @param content
     * @param num
     * @param stringCallback
     */
    void feedback(String content, String num, StringCallback stringCallback);

    /**
     * 4.1.40.	首页广告
     *
     * @param type           广告类型, 1=启动页广告
     * @param stringCallback
     */
    void getad(int type, StringCallback stringCallback);

    /**
     * 4.1.42.	第三方登录
     *
     * @param type
     * @param openid
     * @param username
     * @param sex
     * @param headurl
     */
    void thirdlogin(String type,String openid,String username, String sex,
                    String headurl, StringCallback stringCallback);


    /**
     * 4.1.43.  第三方登入绑定系统账号
     * @param type
     * @param phonenum
     * @param openid
     * @param username
     * @param sex
     * @param headurl
     * @param code
     */
    void bindLogin(String type, String phonenum, String openid, String username, String sex,
                   String headurl,String code, StringCallback stringCallback);
    /**
     * 获取报名管理
     *
     * @param userId
     * @param currpage
     * @param pagesize
     * @param stringCallback
     */
    void getSignUp(int userId, int currpage, int pagesize, StringCallback stringCallback);


    /**
     * 获取活动粉丝
     *
     * @param userId
     * @param currpage
     * @param pagesize
     * @param stringCallback
     */
    void getMyFans(int userId, int currpage, int pagesize, StringCallback stringCallback);

    /***
     * 获取直播粉丝
     *
     * @param userId
     * @param currpage
     * @param pagesize
     * @param stringCallback
     */
    void getLiveFans(int userId, int currpage, int pagesize, StringCallback stringCallback);


    void getHostOrderInfo(int userid, int hostid, int currpage, int pagesize, StringCallback stringCallback);

    /**
     * 关注  （新版）
     */
    void followPerson(int userid, int orther, StringCallback stringCallback);

    /**
     * 取消关注
     */
    void cancelFollowPerson(int userid, int orther, StringCallback stringCallback);

    /**
     * 提交个人认证信息 authentication
     *
     * @param userid         用户id
     * @param idimg          身份证正面照片
     * @param idotherimg     身份证反面照片
     * @param name           姓名
     * @param birthday       出生日期
     * @param idcardno       身份证号码
     * @param alipayname     支付宝帐号姓名
     * @param alipeyno       支付宝帐号
     * @param stringCallback 回调
     */

    void submitAnthentication(int userid, String idimg, String idotherimg, String name,
                              long birthday, String idcardno, String alipayname,
                              String alipeyno, StringCallback stringCallback);

    /**
     * 名人堂粉丝
     * @param userid
     * @param stringCallback
     */
    void myCelebriteFans(int userid, StringCallback stringCallback);
    /**
     * 我的关注获取---获取某一个关注的主播
     * @param userid
     * @param stringCallback
     */
    void getGuanZhuZhuBoItem(int userid, StringCallback stringCallback);


    void getAuthenticStatus(int userid, StringCallback stringCallback);
}
