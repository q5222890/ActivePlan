package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 活动列表类别
 * 
 * @author johnny
 *
 */
public class CategoryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int activeid;// int 活动ID
	private int userid;// int 发布者id
	private String name;// 活动名称
	private String address;// 详细地址
	private long startdate;// 开始时间
	private long enddate;// 结束时间
	private String activeurl;// 海报URL
	private boolean charge;// bool 是否收费
	private int money;// int 参与费用
	private boolean isend;// boolean 是否结束
	private int seenum;// int 查看

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

	public boolean isCharge() {
		return charge;
	}

	public void setCharge(boolean charge) {
		this.charge = charge;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
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

}
