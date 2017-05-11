package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 */
public class CategoryInfoBean implements Serializable {


    /**
     * act_id : 分类id
     * adPlaceUrl : 广告图片地址
     * content : 吧名
     * img_url : 分类logo
     * introduce : 分类介绍
     * total : 2  ：活动总数
     */

    private int act_id;
    private String adPlaceUrl;
    private String class_code;
    private String code;
    private String content;
    private String img_url;
    private String introduce;
    private String parent_code;
    private int total;

    public CategoryInfoBean() {
    }

    public CategoryInfoBean(int act_id, String adPlaceUrl, String class_code,
                            String code, String content, String img_url, String introduce,
                            String parent_code, int total) {
        this.act_id = act_id;
        this.adPlaceUrl = adPlaceUrl;
        this.class_code = class_code;
        this.code = code;
        this.content = content;
        this.img_url = img_url;
        this.introduce = introduce;
        this.parent_code = parent_code;
        this.total = total;
    }

    public int getAct_id() {
        return act_id;
    }

    public void setAct_id(int act_id) {
        this.act_id = act_id;
    }

    public String getAdPlaceUrl() {
        return adPlaceUrl;
    }

    public void setAdPlaceUrl(String adPlaceUrl) {
        this.adPlaceUrl = adPlaceUrl;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
