package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 *直播分类
 */
public class LiveCategoryBean implements Serializable {


    /**
     * coverurl : 封面
     * title : 标题
     * username : 用户名
     */

    private String backurl;
    private int categoryid;
    private String channelid;
    private String coverurl;
    private String fans;
    private String gambit;
    private String gcid;
    private String headurl;
    private int liveid;
    private String liveurl;
    private int seenum;
    private String seetype;
    private long startdate;
    private int status;
    private String title;
    private String url;
    private int userid;
    private String username;


    public LiveCategoryBean() {
    }

    public LiveCategoryBean(String backurl, int categoryid, String channelid,
                            String coverurl, String fans, String gambit, String gcid,
                            String headurl, int liveid, String liveurl, int seenum,
                            String seetype, long startdate, int status, String title,
                            String url, int userid, String username) {
        this.backurl = backurl;
        this.categoryid = categoryid;
        this.channelid = channelid;
        this.coverurl = coverurl;
        this.fans = fans;
        this.gambit = gambit;
        this.gcid = gcid;
        this.headurl = headurl;
        this.liveid = liveid;
        this.liveurl = liveurl;
        this.seenum = seenum;
        this.seetype = seetype;
        this.startdate = startdate;
        this.status = status;
        this.title = title;
        this.url = url;
        this.userid = userid;
        this.username = username;
    }

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getGambit() {
        return gambit;
    }

    public void setGambit(String gambit) {
        this.gambit = gambit;
    }

    public String getGcid() {
        return gcid;
    }

    public void setGcid(String gcid) {
        this.gcid = gcid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getLiveid() {
        return liveid;
    }

    public void setLiveid(int liveid) {
        this.liveid = liveid;
    }

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public int getSeenum() {
        return seenum;
    }

    public void setSeenum(int seenum) {
        this.seenum = seenum;
    }

    public String getSeetype() {
        return seetype;
    }

    public void setSeetype(String seetype) {
        this.seetype = seetype;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LiveCategoryBean{" +
                "backurl='" + backurl + '\'' +
                ", categoryid=" + categoryid +
                ", channelid='" + channelid + '\'' +
                ", coverurl='" + coverurl + '\'' +
                ", fans='" + fans + '\'' +
                ", gambit='" + gambit + '\'' +
                ", gcid='" + gcid + '\'' +
                ", headurl='" + headurl + '\'' +
                ", liveid=" + liveid +
                ", liveurl='" + liveurl + '\'' +
                ", seenum=" + seenum +
                ", seetype='" + seetype + '\'' +
                ", startdate=" + startdate +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                '}';
    }
}
