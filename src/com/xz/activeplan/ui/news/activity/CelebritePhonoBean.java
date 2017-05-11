package com.xz.activeplan.ui.news.activity;

/**
 * Created by Administrator on 2016/7/14.
 */
public class CelebritePhonoBean {
    private int gal_id;
    private String imgurl;
    private int tea_id;

    public CelebritePhonoBean() {
    }

    public CelebritePhonoBean(int gal_id, String imgurl, int tea_id) {
        this.gal_id = gal_id;
        this.imgurl = imgurl;
        this.tea_id = tea_id;
    }

    public int getGal_id() {
        return gal_id;
    }

    public void setGal_id(int gal_id) {
        this.gal_id = gal_id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    @Override
    public String toString() {
        return "CelebritePhonoBean{" +
                "gal_id=" + gal_id +
                ", imgurl='" + imgurl + '\'' +
                ", tea_id=" + tea_id +
                '}';
    }
}
