package com.xz.activeplan.network.active;

import com.xz.activeplan.network.OkHttpClientManager.StringCallback;

import java.util.List;

/**
 *  活动网络请求接口
 *
 * @author johnny
 */
public interface IActiveService {

    /**
     * 4.1.14.	保存活动
     *
     * @param userid         发布者id
     * @param hostid         主办方id
     * @param name           活动名称
     * @param address        详细地址
     * @param startdate      开始时间
     * @param enddate        结束时间
     * @param activeurl      海报URL(File文件)
     * @param personnum      活动人数
     * @param themeid        活动主题
     * @param formid         活动形式
     * @param categoryid     类别id(见文档注释类别id)
     * @param label          标签
     * @param content        内容
     * @param charge         是否收费
     * @param money          参与费用
     * @param stringCallback
     */
    public void pushActive(int userid, int hostid, String name, String address, long startdate, long enddate,
                           String activeurl, int personnum, int themeid, int formid, int categoryid, String label, String content,
                           boolean charge, double money, String json, StringCallback stringCallback);

    /**
     * 4.1.15.	获取活动
     *
     * @param activeid       活动id
     * @param stringCallback 回调接口
     */
    public void getActive(int userid, int activeid, StringCallback stringCallback);

    /**
     * 4.1.16.	搜索活动
     *
     * @param search         搜索字段
     * @param stringCallback 回调函数
     */
    public void searchActive(String search, StringCallback stringCallback);

    /***
     * 4.1.30.	活动报名(免费票)
     *
     * @param userid         用户ID
     * @param activeid       活动id
     * @param paynum         支付流水号
     * @param payamount      支付金额
     * @param paytype        支付类型(支付宝=1,微信=2)
     * @param stringCallback 回调函数
     */
    public void applyActive(int userid, int activeid, String paynum, int payamount, int paytype, String telphone, String weixin, String company, String position, String tickettypeid, String realname, StringCallback stringCallback);


    /***
     * 4.1.30.	活动报名(收费票)
     *
     * @param userid         用户ID
     * @param activeid       活动id
     * @param paynum         支付流水号
     * @param payamount      支付金额
     * @param paytype        支付类型(支付宝=1,微信=2)
     * @param telphone       电话号码
     * @param weixin         微信号
     * @param company        公司
     * @param position       职业
     * @param tickettypeid   票务类型
     * @param realname       姓名
     * @param stringCallback
     */
    public void applychargeActive(int userid, int activeid, String paynum, double payamount, int paytype, String telphone, String weixin, String company, String position, String tickettypeid, String realname, StringCallback stringCallback);

    /***
     * 获取票务信息
     *
     * @param userid
     * @param activeid
     * @param stringCallback
     */
    public void Ticketinfo(int userid, int activeid, StringCallback stringCallback);


    /***
     * @param num            票务数量
     * @param payamount      总金额
     * @param ticket         票务号
     * @param stringCallback 回调
     */
    public void updateTicketNumInfo(int activeid, int num, float payamount, String ticket, StringCallback stringCallback);


    public void updateImage( List<String> strings, StringCallback stringCallback);


    /**
     * 选择验票活动
     * @param userid   当前登录用户id
     * @param stringCallback 回调
     */
    public void selectTicketAction(int userid,StringCallback stringCallback);

    /**
     * 刷脸验票接口
     * @param activeid    活动ID
     * @param group_uid    报名活动组成员的用户Id
     * @param stringCallback  回调
     */
    public void checkFace(int activeid,String group_uid,StringCallback stringCallback);


}
