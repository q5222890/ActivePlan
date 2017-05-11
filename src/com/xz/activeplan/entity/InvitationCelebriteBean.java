package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/9.
 */
public class InvitationCelebriteBean implements Serializable {
    private 	String	amount	;	//	订单总金额
    private     String	inv_palce	;	//	邀约地点
    private 	String	starttime	;	//	开始时间
    private 	String	linkman	;	//	联系人
    private 	String	phone	;	//	手机号码
    private 	String	gam_title	;	//	话题标题
    private 	String	gam_content	;	//	话题内容
    private 	String	inv_title	;	//	邀约主题
    private 	String	inv_content	;	//	邀约内容
    private 	String	tea_id	;	//	被邀人ID（名人）
    private 	String	usr_id	;	//	邀请人ID（用户）
    private 	String	duration ;	//	时长

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInv_palce() {
        return inv_palce;
    }

    public void setInv_palce(String inv_palce) {
        this.inv_palce = inv_palce;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGam_title() {
        return gam_title;
    }

    public void setGam_title(String gam_title) {
        this.gam_title = gam_title;
    }

    public String getGam_content() {
        return gam_content;
    }

    public void setGam_content(String gam_content) {
        this.gam_content = gam_content;
    }

    public String getInv_title() {
        return inv_title;
    }

    public void setInv_title(String inv_title) {
        this.inv_title = inv_title;
    }

    public String getInv_content() {
        return inv_content;
    }

    public void setInv_content(String inv_content) {
        this.inv_content = inv_content;
    }

    public String getTea_id() {
        return tea_id;
    }

    public void setTea_id(String tea_id) {
        this.tea_id = tea_id;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }
}
