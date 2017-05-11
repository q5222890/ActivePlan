package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 保存获取富文本图片
 */
public class imageUrlBean implements Serializable{
    private String name;
    private String url;

    public imageUrlBean() {
    }

    public imageUrlBean(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "imageUrlBean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
