package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class AuthenticationInfoBean implements Serializable{


    private int userid;
    private String idimg;
    private String idotherimg;
    private String name;
    private long birthday;
    private String idcardno;
    private String alipayname;
    private String alipeyno;
    private long ctime;
    private int isadopt;

    public AuthenticationInfoBean() {
    }

    public AuthenticationInfoBean(String idimg, int userid, String idotherimg, String name,
                                  long birthday, String idcardno, String alipayname, String alipeyno,
                                  long ctime, int isadopt) {
        this.idimg = idimg;
        this.userid = userid;
        this.idotherimg = idotherimg;
        this.name = name;
        this.birthday = birthday;
        this.idcardno = idcardno;
        this.alipayname = alipayname;
        this.alipeyno = alipeyno;
        this.ctime = ctime;
        this.isadopt = isadopt;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getIdimg() {
        return idimg;
    }

    public void setIdimg(String idimg) {
        this.idimg = idimg;
    }

    public String getIdotherimg() {
        return idotherimg;
    }

    public void setIdotherimg(String idotherimg) {
        this.idotherimg = idotherimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getAlipayname() {
        return alipayname;
    }

    public void setAlipayname(String alipayname) {
        this.alipayname = alipayname;
    }

    public String getAlipeyno() {
        return alipeyno;
    }

    public void setAlipeyno(String alipeyno) {
        this.alipeyno = alipeyno;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getIsadopt() {
        return isadopt;
    }

    public void setIsadopt(int isadopt) {
        this.isadopt = isadopt;
    }


    @Override
    public String toString() {
        return "AuthenticationInfoBean{" +
                "userid=" + userid +
                ", idimg='" + idimg + '\'' +
                ", idotherimg='" + idotherimg + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", idcardno='" + idcardno + '\'' +
                ", alipayname='" + alipayname + '\'' +
                ", alipeyno='" + alipeyno + '\'' +
                ", ctime=" + ctime +
                ", isadopt=" + isadopt +
                '}';
    }
}
