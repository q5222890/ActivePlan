package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 *名人碎片根据---根据类型查找相对应的名人
 */
public class CelebriteSimpleBean implements Serializable {
       private int tea_id;
       private String realname;
       private String cover;

    public CelebriteSimpleBean() {
    }

    public CelebriteSimpleBean(int tea_id, String realname, String cover) {
        this.tea_id = tea_id;
        this.realname = realname;
        this.cover = cover;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "CelebriteSimpleBean{" +
                "tea_id=" + tea_id +
                ", realname='" + realname + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
