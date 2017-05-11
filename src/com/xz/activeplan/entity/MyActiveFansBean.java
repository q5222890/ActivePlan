package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 活动粉丝
 */
public class MyActiveFansBean implements Serializable {

    /**
     * @param city 城市
     * @param headurl 头像地址
     * @param username : 用户名
     */

    private String alipayaccount;
    private String alipayusername;
    private String bankaccount;
    private String bankcity;
    private String bankname;
    private String banksubsidiary;
    private String bankusername;
    private String city;
    private String email;
    private int fans;
    private String headurl;
    private int id;
    private String password;
    private String phonenum;
    private String qqkey;
    private String realname;
    private int sex;
    private String signature;
    private String thirdid;
    private String token;
    private int userid;
    private String username;
    private String weibokey;


    public MyActiveFansBean() {
    }

    public MyActiveFansBean(String alipayaccount, String alipayusername,
                            String bankaccount, String bankcity, String bankname,
                            String banksubsidiary, String bankusername, String city,
                            String email, int fans, String headurl, int id, String password,
                            String phonenum, String qqkey, String realname, int sex, String signature,
                            String thirdid, String token, int userid, String username, String weibokey) {
        this.alipayaccount = alipayaccount;
        this.alipayusername = alipayusername;
        this.bankaccount = bankaccount;
        this.bankcity = bankcity;
        this.bankname = bankname;
        this.banksubsidiary = banksubsidiary;
        this.bankusername = bankusername;
        this.city = city;
        this.email = email;
        this.fans = fans;
        this.headurl = headurl;
        this.id = id;
        this.password = password;
        this.phonenum = phonenum;
        this.qqkey = qqkey;
        this.realname = realname;
        this.sex = sex;
        this.signature = signature;
        this.thirdid = thirdid;
        this.token = token;
        this.userid = userid;
        this.username = username;
        this.weibokey = weibokey;
    }

    public String getAlipayaccount() {
        return alipayaccount;
    }

    public void setAlipayaccount(String alipayaccount) {
        this.alipayaccount = alipayaccount;
    }

    public String getAlipayusername() {
        return alipayusername;
    }

    public void setAlipayusername(String alipayusername) {
        this.alipayusername = alipayusername;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankcity() {
        return bankcity;
    }

    public void setBankcity(String bankcity) {
        this.bankcity = bankcity;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBanksubsidiary() {
        return banksubsidiary;
    }

    public void setBanksubsidiary(String banksubsidiary) {
        this.banksubsidiary = banksubsidiary;
    }

    public String getBankusername() {
        return bankusername;
    }

    public void setBankusername(String bankusername) {
        this.bankusername = bankusername;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getQqkey() {
        return qqkey;
    }

    public void setQqkey(String qqkey) {
        this.qqkey = qqkey;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getThirdid() {
        return thirdid;
    }

    public void setThirdid(String thirdid) {
        this.thirdid = thirdid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getWeibokey() {
        return weibokey;
    }

    public void setWeibokey(String weibokey) {
        this.weibokey = weibokey;
    }
}
