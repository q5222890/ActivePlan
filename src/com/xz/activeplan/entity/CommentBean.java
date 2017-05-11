package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 */
public class CommentBean implements Serializable {
    private int com_id;
    private String content;
    private String gam_title;
    private int grade;
    private int inv_id;
    private int tea_id;
    private long time;
    private int usr_id;
    private UserInfoBean userInfo;

    public CommentBean(int com_id, String content, String gam_title, int grade, int inv_id, int tea_id, long time,
                       int usr_id, UserInfoBean userInfo) {
        this.com_id = com_id;
        this.content = content;
        this.gam_title = gam_title;
        this.grade = grade;
        this.inv_id = inv_id;
        this.tea_id = tea_id;
        this.time = time;
        this.usr_id = usr_id;
        this.userInfo = userInfo;
    }

    public CommentBean() {

    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGam_title() {
        return gam_title;
    }

    public void setGam_title(String gam_title) {
        this.gam_title = gam_title;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "com_id=" + com_id +
                ", content='" + content + '\'' +
                ", gam_title='" + gam_title + '\'' +
                ", grade=" + grade +
                ", inv_id=" + inv_id +
                ", tea_id=" + tea_id +
                ", time=" + time +
                ", usr_id=" + usr_id +
                ", userInfo=" + userInfo +
                '}';
    }
}