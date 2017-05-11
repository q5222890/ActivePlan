package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/18.
 */
public class FollowAnchorBean implements Serializable {
    private int attendcount;
    private String city;
    private int fanscount;
    private String headurl;
    private int id;
    private boolean isAttend;
    private int livecount;
    private int otheruserid;
    private String province;
    private int userid;
    private String username;

    public FollowAnchorBean(int attendcount, String city, int fanscount, String headurl,
                            int id, boolean isAttend, int livecount, int otheruserid, String province,
                            int userid, String username) {
        this.attendcount = attendcount;
        this.city = city;
        this.fanscount = fanscount;
        this.headurl = headurl;
        this.id = id;
        this.isAttend = isAttend;
        this.livecount = livecount;
        this.otheruserid = otheruserid;
        this.province = province;
        this.userid = userid;
        this.username = username;
    }

    public FollowAnchorBean() {

    }

    public int getAttendcount() {
        return attendcount;
    }

    public void setAttendcount(int attendcount) {
        this.attendcount = attendcount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFanscount() {
        return fanscount;
    }

    public void setFanscount(int fanscount) {
        this.fanscount = fanscount;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsAttend() {
        return isAttend;
    }

    public void setIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    public int getLivecount() {
        return livecount;
    }

    public void setLivecount(int livecount) {
        this.livecount = livecount;
    }

    public int getOtheruserid() {
        return otheruserid;
    }

    public void setOtheruserid(int otheruserid) {
        this.otheruserid = otheruserid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
        return "FollowAnchorBean{" +
                "attendcount=" + attendcount +
                ", city='" + city + '\'' +
                ", fanscount=" + fanscount +
                ", headurl='" + headurl + '\'' +
                ", id=" + id +
                ", isAttend=" + isAttend +
                ", livecount=" + livecount +
                ", otheruserid=" + otheruserid +
                ", province='" + province + '\'' +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                '}';
    }
}
