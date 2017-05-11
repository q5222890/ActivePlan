package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/29.
 */
public class SponsorsSupportBean implements Serializable{


    private int sponsorscounts;  //赞助人
    private String supporttype;  //赞助
    private String supportprice;
    private String sponsorcontent;
    private String contactname;
    private String contactphone;


    public SponsorsSupportBean() {
    }

    public SponsorsSupportBean(int sponsorscounts, String supporttype, String supportprice, String sponsorcontent,
                               String contactname, String contactphone) {
        this.sponsorscounts = sponsorscounts;
        this.supporttype = supporttype;
        this.supportprice = supportprice;
        this.sponsorcontent = sponsorcontent;
        this.contactname = contactname;
        this.contactphone = contactphone;
    }

    public int getSponsorscounts() {
        return sponsorscounts;
    }

    public void setSponsorscounts(int sponsorscounts) {
        this.sponsorscounts = sponsorscounts;
    }

    public String getSupporttype() {
        return supporttype;
    }

    public void setSupporttype(String supportname) {
        this.supporttype = supportname;
    }

    public String getSupportprice() {
        return supportprice;
    }

    public void setSupportprice(String supportprice) {
        this.supportprice = supportprice;
    }

    public String getSponsorcontent() {
        return sponsorcontent;
    }

    public void setSponsorcontent(String sponsorcontent) {
        this.sponsorcontent = sponsorcontent;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }
}
