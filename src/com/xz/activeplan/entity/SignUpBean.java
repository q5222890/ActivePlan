package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class SignUpBean implements Serializable{


    private int activeid;//活动id
    private String headurl;//报名人的头像
    private int id;
    private String ticket;
    private boolean ticketcheck;//是否检票
    private int userid;//报名人的id
    private String username;//报名人的名字
    /**
     * telphone : 电话号码
     */

    private String telphone;

    public SignUpBean() {
    }

    public SignUpBean(int activeid, String headurl, int id, String ticket, boolean ticketcheck, int userid, String username, String telphone) {
        this.activeid = activeid;
        this.headurl = headurl;
        this.id = id;
        this.ticket = ticket;
        this.ticketcheck = ticketcheck;
        this.userid = userid;
        this.username = username;
        this.telphone = telphone;
    }

    public int getActiveid() {
        return activeid;
    }

    public void setActiveid(int activeid) {
        this.activeid = activeid;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isTicketcheck() {
        return ticketcheck;
    }

    public void setTicketcheck(boolean ticketcheck) {
        this.ticketcheck = ticketcheck;
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


    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @Override
    public String toString() {
        return "SignUpBean{" +
                "activeid=" + activeid +
                ", headurl='" + headurl + '\'' +
                ", id=" + id +
                ", ticket='" + ticket + '\'' +
                ", ticketcheck=" + ticketcheck +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", telphone='" + telphone + '\'' +
                '}';
    }
}
