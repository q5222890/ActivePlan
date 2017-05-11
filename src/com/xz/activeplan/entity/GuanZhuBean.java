package com.xz.activeplan.entity;

/**
 * Created by Administrator on 2016/6/11.
 */
public class GuanZhuBean {
    private int id;
    private int userid;
    private String username;
    private String password;
    private String city;
    private String realname;
    private String signature;
    private String phonenum;
    private String sex;
    private String headurl;
    private String email;
    private String bankname;
    private String bankaccount;
    private String bankusername;
    private String bankcity;
    private String banksubsidiary;
    private String alipayaccount;
    private String alipayusername;
    private String qqkey;
    private String weibokey;
    private String token;

    public GuanZhuBean() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankusername() {
        return bankusername;
    }

    public void setBankusername(String bankusername) {
        this.bankusername = bankusername;
    }

    public String getBankcity() {
        return bankcity;
    }

    public void setBankcity(String bankcity) {
        this.bankcity = bankcity;
    }

    public String getBanksubsidiary() {
        return banksubsidiary;
    }

    public void setBanksubsidiary(String banksubsidiary) {
        this.banksubsidiary = banksubsidiary;
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

    public String getQqkey() {
        return qqkey;
    }

    public void setQqkey(String qqkey) {
        this.qqkey = qqkey;
    }

    public String getWeibokey() {
        return weibokey;
    }

    public void setWeibokey(String weibokey) {
        this.weibokey = weibokey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getThirdid() {
        return thirdid;
    }

    public void setThirdid(String thirdid) {
        this.thirdid = thirdid;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public GuanZhuBean(int id, int userid, String username, String password, String city, String realname, String signature, String phonenum, String sex, String headurl, String email, String bankname, String bankaccount, String bankusername, String bankcity, String banksubsidiary, String alipayaccount, String alipayusername, String qqkey, String weibokey, String token, String thirdid, int fans) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.city = city;
        this.realname = realname;
        this.signature = signature;
        this.phonenum = phonenum;
        this.sex = sex;
        this.headurl = headurl;
        this.email = email;
        this.bankname = bankname;
        this.bankaccount = bankaccount;
        this.bankusername = bankusername;
        this.bankcity = bankcity;
        this.banksubsidiary = banksubsidiary;
        this.alipayaccount = alipayaccount;
        this.alipayusername = alipayusername;
        this.qqkey = qqkey;
        this.weibokey = weibokey;
        this.token = token;
        this.thirdid = thirdid;
        this.fans = fans;
    }

    @Override
    public String toString() {
        return "GuanZhuBean{" +
                "id=" + id +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", realname='" + realname + '\'' +
                ", signature='" + signature + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", sex='" + sex + '\'' +
                ", headurl='" + headurl + '\'' +
                ", email='" + email + '\'' +
                ", bankname='" + bankname + '\'' +
                ", bankaccount='" + bankaccount + '\'' +
                ", bankusername='" + bankusername + '\'' +
                ", bankcity='" + bankcity + '\'' +
                ", banksubsidiary='" + banksubsidiary + '\'' +
                ", alipayaccount='" + alipayaccount + '\'' +
                ", alipayusername='" + alipayusername + '\'' +
                ", qqkey='" + qqkey + '\'' +
                ", weibokey='" + weibokey + '\'' +
                ", token='" + token + '\'' +
                ", thirdid='" + thirdid + '\'' +
                ", fans=" + fans +
                '}';
    }

    private String thirdid;
    private int fans;
}
