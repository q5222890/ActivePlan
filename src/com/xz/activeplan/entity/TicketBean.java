package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 票务bean
 * 
 * @author johnny
 *
 */
public class TicketBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// int 票务ID
	private int userid;// int 发布者id
	private int activeid;// int 活动id
	private String ticket;// string 票号
	private String name;// string 活动名称
	private String address; // string 详细地址
	private long startdate;// long 开始时间
	private long enddate;// long 结束时间
	private String activeurl;// string 海报URL
	private boolean charge;// boolean 是否收费

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getActiveid() {
		return activeid;
	}

	public void setActiveid(int activeid) {
		this.activeid = activeid;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
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


	@Override
	public String toString() {
		return "TicketBean{" +
				"id=" + id +
				", userid=" + userid +
				", activeid=" + activeid +
				", ticket='" + ticket + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", startdate=" + startdate +
				", enddate=" + enddate +
				", activeurl='" + activeurl + '\'' +
				", charge=" + charge +
				'}';
	}
}
