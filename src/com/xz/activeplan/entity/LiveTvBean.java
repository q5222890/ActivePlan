package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/2.
 */
public class LiveTvBean implements Serializable {
    private int liveid;					//直播ID
    private String title;				//标题
    private long startdate;		//开始时间
    private String seetype;				//谁都可以看  0 公开 1 付费 2 密码
    private int categoryid;			//分类
    private String gambit;			//话题
    private String coverurl;		//封面URL
    private int seenum;				//观看人数
    private String liveurl;			//直播URL
    private String backurl;			//会看URL
    private int status;				//状态 1预约、2直播、3回放
    private int userid;				//发布者ID
    private boolean isattend;   //是否关注

    private String headurl;
    private String fans;        //粉丝数量
    private String username;

    private String channelid;
    private String gcid;
    private String url;


    public LiveTvBean() {

    }

    public LiveTvBean(int liveid, String title, long startdate, String seetype, int categoryid, String gambit, String coverurl, int seenum, String liveurl, String backurl, int status, int userid, boolean isattend, String headurl, String fans, String username, String channelid, String gcid, String url) {
        this.liveid = liveid;
        this.title = title;
        this.startdate = startdate;
        this.seetype = seetype;
        this.categoryid = categoryid;
        this.gambit = gambit;
        this.coverurl = coverurl;
        this.seenum = seenum;
        this.liveurl = liveurl;
        this.backurl = backurl;
        this.status = status;
        this.userid = userid;
        this.isattend = isattend;
        this.headurl = headurl;
        this.fans = fans;
        this.username = username;
        this.channelid = channelid;
        this.gcid = gcid;
        this.url = url;
    }

    public boolean isIsattend() {
        return isattend;
    }

    public void setIsattend(boolean isattend) {
        this.isattend = isattend;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getGcid() {
        return gcid;
    }

    public void setGcid(String gcid) {
        this.gcid = gcid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLiveid() {
        return liveid;
    }

    public void setLiveid(int liveid) {
        this.liveid = liveid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public String getSeetype() {
        return seetype;
    }

    public void setSeetype(String seetype) {
        this.seetype = seetype;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getGambit() {
        return gambit;
    }

    public void setGambit(String gambit) {
        this.gambit = gambit;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public int getSeenum() {
        return seenum;
    }

    public void setSeenum(int seenum) {
        this.seenum = seenum;
    }

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String getStatus() {
        String statusstr="";
        if(status==1){
            statusstr ="预约";
        }else if(status==2){
            statusstr ="正在直播";
        }else if(status ==3){
            statusstr ="回放";
        }
        return statusstr;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LiveTvBean{" +
                "liveid=" + liveid +
                ", title='" + title + '\'' +
                ", startdate=" + startdate +
                ", seetype='" + seetype + '\'' +
                ", categoryid=" + categoryid +
                ", gambit='" + gambit + '\'' +
                ", coverurl='" + coverurl + '\'' +
                ", seenum=" + seenum +
                ", liveurl='" + liveurl + '\'' +
                ", backurl='" + backurl + '\'' +
                ", status=" + status +
                ", userid=" + userid +
                ", isattend=" + isattend +
                ", headurl='" + headurl + '\'' +
                ", fans='" + fans + '\'' +
                ", username='" + username + '\'' +
                ", channelid='" + channelid + '\'' +
                ", gcid='" + gcid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
