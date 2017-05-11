package com.xz.activeplan.entity;

import java.io.Serializable;

public class TicketAddBean implements Serializable {

	private static final long serialVersionUID = -6303146608379785407L;
	private String name;// string 票务名称
	private int type; // int 1=免费,2=收费
	private double money;// float 收费金额(元)
	private String intro;// string 票务说明
	private boolean ischeck;// boolean 是否要审核(默认false)
	private int personnum;// int 人数限制
	private boolean issellout ;  //是否售完
	private int surpluspersonnum;  //剩余票数
	private int id;
	private int activeid;


	public int getSurpluspersonnum() {
		return surpluspersonnum;
	}

	public void setSurpluspersonnum(int surpluspersonnum) {
		this.surpluspersonnum = surpluspersonnum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActiveid() {
		return activeid;
	}

	public void setActiveid(int activeid) {
		this.activeid = activeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public int getPersonnum() {
		return personnum;
	}

	public void setPersonnum(int personnum) {
		this.personnum = personnum;
	}

	public boolean isIssellout() {
		return issellout;
	}

	public void setIssellout(boolean issellout) {
		this.issellout = issellout;
	}


	@Override
	public String toString() {
		return "TicketAddBean{" +
				"name='" + name + '\'' +
				", type=" + type +
				", money=" + money +
				", intro='" + intro + '\'' +
				", ischeck=" + ischeck +
				", personnum=" + personnum +
				", issellout=" + issellout +
				", surpluspersonnum=" + surpluspersonnum +
				", id=" + id +
				", activeid=" + activeid +
				'}';
	}
}
