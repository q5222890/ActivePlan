package com.xz.activeplan.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/7/23.
 */
public class CelebriteAndAreaBean {
    /**
     * class_code : C0003
     * code : 30001
     * content : 北京
     * img_url :
     * introduce :
     * parent_code :
     * typ_id : 24
     */
    private String class_code;
    private String code;
    private String content;
    private String img_url;
    private String introduce;
    private String parent_code;
    private int typ_id;
    public CelebriteAndAreaBean() {
    }
    public CelebriteAndAreaBean(String class_code, String code, String content, String img_url, String introduce, String parent_code, int typ_id) {
        this.class_code = class_code;
        this.code = code;
        this.content = content;
        this.img_url = img_url;
        this.introduce = introduce;
        this.parent_code = parent_code;
        this.typ_id = typ_id;
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

    public int getTyp_id() {
        return typ_id;
    }

    public void setTyp_id(int typ_id) {
        this.typ_id = typ_id;
    }

    @Override
    public String toString() {
        return "CelebriteAndAreaBean{" +
                "class_code='" + class_code + '\'' +
                ", code='" + code + '\'' +
                ", content='" + content + '\'' +
                ", img_url='" + img_url + '\'' +
                ", introduce='" + introduce + '\'' +
                ", parent_code='" + parent_code + '\'' +
                ", typ_id=" + typ_id +
                '}';
    }
}
