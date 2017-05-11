package com.xz.activeplan.network.live;

import com.xz.activeplan.network.OkHttpClientManager.StringCallback;

/**
 * 直播信息网络请求接口
 */
public interface ILiveInfoService {


    /***
     * 获取直播分类
     * @param categoryid  分类id
     * @param currpage  当前页
     * @param pagesize   一页多少条
     * @param stringCallback  回调
     */
    public void getLiveCategory(int categoryid, int currpage, int pagesize, StringCallback stringCallback);

    /***
     *  关注的所有主播
     * @param userid  用户id
     * @param type   直播类型
     * @param currpage   当前页
     * @param pagesize   一夜几条
     * @param stringCallback  回调
     */
    public void getLiveFollow(int userid, int type, int currpage, int pagesize, StringCallback stringCallback);

    /**
     * 更多最新直播
     * @param currpage  当前页
     * @param pagesize  一页几条
     * @param stringCallback  回调
     */

    public void getNewFreshLive(int currpage, int pagesize, StringCallback stringCallback);

    /**
     * 更多回放视频
     * @param currpage  当前页
     * @param pagesize  一页几条
     * @param stringCallback  回调
     */
    public void getMorePlayback(int currpage, int pagesize, StringCallback stringCallback);


    /**
     * 获取视频的播放数量
     * @param liveid  视频id
     */
     public void getVideoPlayCount(int liveid,StringCallback stringCallback);

    /**
     * 点击头像的获取用户信息
     * @param liveuserid    主播userid
     * @param currentUserid  当前登录 userid
     */
     public void getCheckHeader(int liveuserid,int currentUserid,StringCallback stringCallback);


}
