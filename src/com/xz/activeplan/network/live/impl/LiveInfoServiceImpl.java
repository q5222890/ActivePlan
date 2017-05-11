package com.xz.activeplan.network.live.impl;

import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.live.ILiveInfoService;
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.ui.UrlsManager;

/**
 * 直播信息网络请求接口实现类
 */
public class LiveInfoServiceImpl implements ILiveInfoService {

    private static ILiveInfoService mILiveInfoService;

    private LiveInfoServiceImpl() {
    }

    public static ILiveInfoService getInstance() {
        if (mILiveInfoService == null) {
            synchronized (LoginServiceImpl.class) {
                if (mILiveInfoService == null) {
                    mILiveInfoService = new LiveInfoServiceImpl();
                }
            }
        }
        return mILiveInfoService;
    }

    /**
     * 获取直播分类
     * @param categoryid  分类id
     * @param currpage  当前页
     * @param pagesize   一页多少条
     * @param stringCallback  回调
     */
    @Override
    public void getLiveCategory(int categoryid, int currpage, int pagesize, StringCallback stringCallback) {

        OkHttpClientManager.Param paramcategoryid = new OkHttpClientManager.Param("categoryid", categoryid + "");
        OkHttpClientManager.Param paramcurpage = new OkHttpClientManager.Param("currpage", currpage + "");
        OkHttpClientManager.Param parampagesize = new OkHttpClientManager.Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_LIVECATEGOTY, stringCallback,
                 paramcategoryid, paramcurpage, parampagesize);
    }

    /**
     * 关注的主播
     * @param userid  用户id
     * @param type   直播类型
     * @param currpage   当前页
     * @param pagesize   一夜几条
     * @param stringCallback  回调
     */
    @Override
    public void getLiveFollow(int userid,int type, int currpage, int pagesize, StringCallback stringCallback) {
        OkHttpClientManager.Param paramuserid = new OkHttpClientManager.Param("userid", userid + "");
        OkHttpClientManager.Param paramtype = new OkHttpClientManager.Param("type", type + "");
        OkHttpClientManager.Param paramcurpage = new OkHttpClientManager.Param("currpage", currpage + "");
        OkHttpClientManager.Param parampagesize = new OkHttpClientManager.Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_FOLLOWANCHOR, stringCallback,
                paramuserid, paramtype, paramcurpage, parampagesize);
    }

    /**
     *  更多最新直播
     * @param currpage  当前页
     * @param pagesize  一页几条
     * @param stringCallback  回调
     */
    @Override
    public void getNewFreshLive( int currpage, int pagesize, StringCallback stringCallback) {
        OkHttpClientManager.Param paramcurpage = new OkHttpClientManager.Param("currpage", currpage + "");
        OkHttpClientManager.Param parampagesize = new OkHttpClientManager.Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_NEWLIVE_ALL, stringCallback,
                paramcurpage, parampagesize);

    }

    /**
     * 更多回放视频
     * @param currpage  当前页
     * @param pagesize  一页几条
     * @param stringCallback  回调
     */
    @Override
    public void getMorePlayback(int currpage, int pagesize, StringCallback stringCallback) {
        OkHttpClientManager.Param paramcurpage = new OkHttpClientManager.Param("currpage", currpage + "");
        OkHttpClientManager.Param parampagesize = new OkHttpClientManager.Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_NEWBLACKPLAY, stringCallback,
                paramcurpage, parampagesize);
    }

    /**
     * 获取视频的播放数量
     * @param liveid  视频id
     */
    @Override
    public void getVideoPlayCount(int liveid,StringCallback stringCallback) {
        OkHttpClientManager.Param liveID = new OkHttpClientManager.Param("liveid", liveid+"");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_PLAY_COUNT, stringCallback,liveID);
    }

    /**
     * 点击头像获取主播信息 (头像，姓名，视频量，粉丝量，关注量)
     * @param liveuserid    主播userid
     * @param currentUserid  当前登录 userid
     */
    @Override
    public void getCheckHeader(int liveuserid, int currentUserid,StringCallback stringCallback) {
        OkHttpClientManager.Param liveuserid1 = new OkHttpClientManager.Param("liveuserid", liveuserid+ "");
        OkHttpClientManager.Param  userid1 = new OkHttpClientManager.Param("userid", currentUserid+ "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_VIDEO_FANSCOUNT, stringCallback,liveuserid1, userid1);
    }


}
