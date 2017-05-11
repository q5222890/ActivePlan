package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/27.
 */
public class TicketInfoBean implements Serializable{


    /**
     * activeid : 1140
     * activeurl :
     * address :
     * charge : false
     * company : null
     * createdate : 1473227346805
     * enddate : 0
     * headurl :
     * id : 436
     * isend : false
     * name : 人脸测试
     * num : 0
     * payamount : 0
     * paynum : null
     * paytype : 0
     * phonenum :
     * position : null
     * realname : 哈佛
     * startdate : 0
     * telphone : 15012984485
     * ticket : 80511403105
     * ticketcheck : 1
     * tickettypeid : 1197
     * userid : 21
     * username :
     * weixin : null
     */

    private int activeid;
    private String activeurl;
    private String address;
    private boolean charge;
    private Object company;
    private long createdate;
    private int enddate;
    private String headurl;
    private int id;
    private boolean isend;
    private String name;
    private int num;
    private int payamount;
    private Object paynum;
    private int paytype;
    private String phonenum;
    private Object position;
    private String realname;
    private int startdate;
    private String telphone;
    private String ticket;
    private int ticketcheck;
    private int tickettypeid;
    private int userid;
    private String username;
    private Object weixin;

    public TicketInfoBean() {
    }

    public TicketInfoBean(int activeid, String activeurl, String address, boolean charge, Object company, long createdate, int enddate, String headurl, int id, boolean isend, String name, int num, int payamount, Object paynum, int paytype, String phonenum, Object position, String realname, int startdate, String telphone, String ticket, int ticketcheck, int tickettypeid, int userid, String username, Object weixin) {
        this.activeid = activeid;
        this.activeurl = activeurl;
        this.address = address;
        this.charge = charge;
        this.company = company;
        this.createdate = createdate;
        this.enddate = enddate;
        this.headurl = headurl;
        this.id = id;
        this.isend = isend;
        this.name = name;
        this.num = num;
        this.payamount = payamount;
        this.paynum = paynum;
        this.paytype = paytype;
        this.phonenum = phonenum;
        this.position = position;
        this.realname = realname;
        this.startdate = startdate;
        this.telphone = telphone;
        this.ticket = ticket;
        this.ticketcheck = ticketcheck;
        this.tickettypeid = tickettypeid;
        this.userid = userid;
        this.username = username;
        this.weixin = weixin;
    }

    public int getActiveid() {
        return activeid;
    }

    public void setActiveid(int activeid) {
        this.activeid = activeid;
    }

    public String getActiveurl() {
        return activeurl;
    }

    public void setActiveurl(String activeurl) {
        this.activeurl = activeurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(long createdate) {
        this.createdate = createdate;
    }

    public int getEnddate() {
        return enddate;
    }

    public void setEnddate(int enddate) {
        this.enddate = enddate;
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

    public boolean isIsend() {
        return isend;
    }

    public void setIsend(boolean isend) {
        this.isend = isend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPayamount() {
        return payamount;
    }

    public void setPayamount(int payamount) {
        this.payamount = payamount;
    }

    public Object getPaynum() {
        return paynum;
    }

    public void setPaynum(Object paynum) {
        this.paynum = paynum;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getStartdate() {
        return startdate;
    }

    public void setStartdate(int startdate) {
        this.startdate = startdate;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getTicketcheck() {
        return ticketcheck;
    }

    public void setTicketcheck(int ticketcheck) {
        this.ticketcheck = ticketcheck;
    }

    public int getTickettypeid() {
        return tickettypeid;
    }

    public void setTickettypeid(int tickettypeid) {
        this.tickettypeid = tickettypeid;
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

    public Object getWeixin() {
        return weixin;
    }

    public void setWeixin(Object weixin) {
        this.weixin = weixin;
    }

    @Override
    public String toString() {
        return "TicketInfoBean{" +
                "activeid=" + activeid +
                ", activeurl='" + activeurl + '\'' +
                ", address='" + address + '\'' +
                ", charge=" + charge +
                ", company=" + company +
                ", createdate=" + createdate +
                ", enddate=" + enddate +
                ", headurl='" + headurl + '\'' +
                ", id=" + id +
                ", isend=" + isend +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", payamount=" + payamount +
                ", paynum=" + paynum +
                ", paytype=" + paytype +
                ", phonenum='" + phonenum + '\'' +
                ", position=" + position +
                ", realname='" + realname + '\'' +
                ", startdate=" + startdate +
                ", telphone='" + telphone + '\'' +
                ", ticket='" + ticket + '\'' +
                ", ticketcheck=" + ticketcheck +
                ", tickettypeid=" + tickettypeid +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", weixin=" + weixin +
                '}';
    }
}
