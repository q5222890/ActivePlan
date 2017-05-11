package com.xz.activeplan.entity;

/**
 * 广告Bean
 * Created by Administrator on 2016/6/4.
 */
public class AdvertisementBean {
    private int id;
    private String  url;
    private int type;
    private String title;
    private String targets;
    private int  param;
    private int displayorder;
    private int available;

    public AdvertisementBean() {
    }

    public AdvertisementBean(int id, String url, int type, String title, String targets, int param, int displayorder, int available) {
        this.id = id;
        this.url = url;
        this.type = type;
        this.title = title;
        this.targets = targets;
        this.param = param;
        this.displayorder = displayorder;
        this.available = available;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdvertisementBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", targets='" + targets + '\'' +
                ", param=" + param +
                ", displayorder=" + displayorder +
                ", available=" + available +
                '}';
    }
}
