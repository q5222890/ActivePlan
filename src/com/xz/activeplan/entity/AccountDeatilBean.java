package com.xz.activeplan.entity;

import java.io.Serializable;

public class AccountDeatilBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3729982156948461524L;


	private int id;// int 是 账户id
	private int userid;// int 用户id
	private String phonenum;// String 用户电话号码
	private double num;// float 交易额
	private int type;// int 流水类型: 1=收入, 2=支出, 3=提现
	private String intro;// String 备注
	private String createtime;// String 创建时间

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

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

}
