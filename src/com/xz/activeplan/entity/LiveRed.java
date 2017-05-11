package com.xz.activeplan.entity;

/**
 * 直播，点播中的打赏红包实体类
 */
public class LiveRed {

	private int id;
	private int userid; // 打赏者用户id
	private int liveid;
	private String paynum; // 支付流水号
	private float payamount; // 支付金额
	private int paytype; // 支付类型
	private long createdate; // 时间
	private String describe1; // 描述
	private int state; // 状态：0为付款；1已付款
	private String number; // 订单号

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

	public int getLiveid() {
		return liveid;
	}

	public void setLiveid(int liveid) {
		this.liveid = liveid;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}

	public float getPayamount() {
		return payamount;
	}

	public void setPayamount(float payamount) {
		this.payamount = payamount;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public long getCreatedate() {
		return createdate;
	}

	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}

	public String getDescribe1() {
		return describe1;
	}

	public void setDescribe1(String describe1) {
		this.describe1 = describe1;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	

}
