package com.xz.activeplan.entity;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author johnny
 */
public class UserInfoBean implements Serializable {


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
    private Object teacher;
    private String thirdid;
    private String token;
    private int userid;
    private String username;
    private String weibokey;
    private String weixinkey;
    private String openid;    //第三方登录回调openid
    private String loginID;  //登录标识  （微信、QQ）

    public UserInfoBean() {
    }

    public UserInfoBean(String alipayaccount, String alipayusername, String bankaccount, String bankcity, String bankname, String banksubsidiary, String bankusername, String city, String email, int fans, String headurl, int id, String password, String phonenum, String qqkey, String realname, int sex, String signature, Object teacher, String thirdid, String token, int userid, String username, String weibokey, String weixinkey, String openid, String loginID) {
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
        this.teacher = teacher;
        this.thirdid = thirdid;
        this.token = token;
        this.userid = userid;
        this.username = username;
        this.weibokey = weibokey;
        this.weixinkey = weixinkey;
        this.openid = openid;
        this.loginID = loginID;
    }

    public static UserInfoBean objectFromData(String str) {

        return new Gson().fromJson(str, UserInfoBean.class);
    }

    /**
     * @return 第三方登录标识（qq / weixin）
     */
    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public Object getTeacher() {
        return teacher;
    }

    public void setTeacher(Object teacher) {
        this.teacher = teacher;
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

    public String getWeixinkey() {
        return weixinkey;
    }

    public void setWeixinkey(String weixinkey) {
        this.weixinkey = weixinkey;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "alipayaccount='" + alipayaccount + '\'' +
                ", alipayusername='" + alipayusername + '\'' +
                ", bankaccount='" + bankaccount + '\'' +
                ", bankcity='" + bankcity + '\'' +
                ", bankname='" + bankname + '\'' +
                ", banksubsidiary='" + banksubsidiary + '\'' +
                ", bankusername='" + bankusername + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", fans=" + fans +
                ", headurl='" + headurl + '\'' +
                ", id=" + id +
                ", password='" + password + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", qqkey='" + qqkey + '\'' +
                ", realname='" + realname + '\'' +
                ", sex=" + sex +
                ", signature='" + signature + '\'' +
                ", teacher=" + teacher +
                ", thirdid='" + thirdid + '\'' +
                ", token='" + token + '\'' +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", weibokey='" + weibokey + '\'' +
                ", weixinkey='" + weixinkey + '\'' +
                ", openid='" + openid + '\'' +
                ", loginID='" + loginID + '\'' +
                '}';
    }
}
