package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 视频粉丝实体类
 */
public class MyVideoFansBean implements Serializable{
    private int id;
    private int userid;
    //private int liveid;
    private int otheruserid;
    private boolean isAttend;  //是否已经关注
    private int livecount;  //视频数量
    private int attendcount;  //关注数量
    private int fanscount;  //粉丝数量
    private String username; //用户头像
    private String headurl;  //用户头像
    private String city;
    private String province;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getFanscount() {
        return fanscount;
    }
    public void setFanscount(int fanscount) {
        this.fanscount = fanscount;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public boolean getIsAttend(){
        return isAttend;
    }
    public void setIsAttend(boolean isAttend){
        this.isAttend = isAttend;
    }

    public String getHeadurl() {
        return headurl;
    }
    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }
    public int getLivecount() {
        return livecount;
    }
    public void setLivecount(int livecount) {
        this.livecount = livecount;
    }
    public int getAttendcount() {
        return attendcount;
    }
    public void setAttendcount(int attendcount) {
        this.attendcount = attendcount;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public int getOtheruserid() {
        return otheruserid;
    }
    public void setOtheruserid(int otheruserid) {
        this.otheruserid = otheruserid;
    }
    public void setAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    @Override
    public String toString() {
        return "MyVideoFansBean{" +
                "id=" + id +
                ", userid=" + userid +
                ", otheruserid=" + otheruserid +
                ", isAttend=" + isAttend +
                ", livecount=" + livecount +
                ", attendcount=" + attendcount +
                ", fanscount=" + fanscount +
                ", username='" + username + '\'' +
                ", headurl='" + headurl + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
