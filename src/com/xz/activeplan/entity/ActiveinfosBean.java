package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 我的收藏数组 与类别
 * @author johnny
 */
public class ActiveinfosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int activeid;// int 是 活动ID
	private int userid;// int 是 发布者id
	private int hostid;// int 是 主办方id
	private String name;// 活动名称
	private String address;// 详细地址
	private long startdate;// long 开始时间
	private long enddate;// long 结束时间
	private String activeurl;// 海报URL
	private int personnum;// int 活动人数
	private int themeid;// int 活动主题
	private int formid;// int 活动形式
	private int categoryid;// int 类别id(见文档注释类别id)
	private int pushid;// int 推送类别id(见文档定义)
	private String label;// string 标签
	private String content;// string 内容
	private boolean charge;// bool 是否收费
	private double money;// int 参与费用
	private boolean isend;// bool 是否结束
	private int seenum;// int 查看人数
	private String groupid;// int 活动群ID
	private int collectnum;// int 收藏人数
	private boolean iscollect;// boolean 是否收藏(对应当前登录用户)
	private String hosthearurl	;//		主办方头像
	private String hostname	 ;//		主办方名称
	private boolean isapply ;//  是否报名过
	private boolean issellout ;// 票是否售完

	
	public ActiveinfosBean(){}
	
	public ActiveinfosBean(CollectBean bean){
		activeid = bean.getActiveid() ;
		activeurl = bean.getActiveurl() ;
		address = bean.getAddress() ;
		//bean.getCollectid();
		iscollect = true ;
		enddate = bean.getEnddate();
		money = bean.getMoney();
		name = bean.getName();
		seenum = bean.getSeenum();
		startdate = bean.getStartdate();
		userid = bean.getUserid();
		charge = bean.isCharge() ;
	}

	public int getActiveid() {
		return activeid;
	}

	public void setActiveid(int activeid) {
		this.activeid = activeid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getHostid() {
		return hostid;
	}

	public void setHostid(int hostid) {
		this.hostid = hostid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getStartdate() {
		return startdate;
	}

	public void setStartdate(long startdate) {
		this.startdate = startdate;
	}

	public long getEnddate() {
		return enddate;
	}

	public void setEnddate(long enddate) {
		this.enddate = enddate;
	}

	public String getActiveurl() {
		return activeurl;
	}

	public void setActiveurl(String activeurl) {
		this.activeurl = activeurl;
	}

	public int getPersonnum() {
		return personnum;
	}

	public void setPersonnum(int personnum) {
		this.personnum = personnum;
	}

	public int getThemeid() {
		return themeid;
	}

	public void setThemeid(int themeid) {
		this.themeid = themeid;
	}

	public int getFormid() {
		return formid;
	}

	public void setFormid(int formid) {
		this.formid = formid;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public int getPushid() {
		return pushid;
	}

	public void setPushid(int pushid) {
		this.pushid = pushid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCharge() {
		return charge;
	}

	public void setCharge(boolean charge) {
		this.charge = charge;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public boolean isIsend() {
		return isend;
	}

	public void setIsend(boolean isend) {
		this.isend = isend;
	}

	public int getSeenum() {
		return seenum;
	}

	public void setSeenum(int seenum) {
		this.seenum = seenum;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public int getCollectnum() {
		return collectnum;
	}

	public void setCollectnum(int collectnum) {
		this.collectnum = collectnum;
	}

	public boolean isIscollect() {
		return iscollect;
	}

	public void setIscollect(boolean iscollect) {
		this.iscollect = iscollect;
	}

	public String getHosthearurl() {
		return hosthearurl;
	}

	public void setHosthearurl(String hosthearurl) {
		this.hosthearurl = hosthearurl;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isIsapply() {
		return isapply;
	}

	public void setIsapply(boolean isapply) {
		this.isapply = isapply;
	}

	public boolean isIssellout() {
		return issellout;
	}

	public void setIssellout(boolean issellout) {
		this.issellout = issellout;
	}

	@Override
	public String toString() {
		return "ActiveinfosBean{" +
				"activeid=" + activeid +
				", userid=" + userid +
				", hostid=" + hostid +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", startdate=" + startdate +
				", enddate=" + enddate +
				", activeurl='" + activeurl + '\'' +
				", personnum=" + personnum +
				", themeid=" + themeid +
				", formid=" + formid +
				", categoryid=" + categoryid +
				", pushid=" + pushid +
				", label='" + label + '\'' +
				", content='" + content + '\'' +
				", charge=" + charge +
				", money=" + money +
				", isend=" + isend +
				", seenum=" + seenum +
				", groupid=" + groupid +
				", collectnum=" + collectnum +
				", iscollect=" + iscollect +
				", hosthearurl='" + hosthearurl + '\'' +
				", hostname='" + hostname + '\'' +
				", isapply=" + isapply +
				", issellout=" + issellout +
				'}';
	}
}
