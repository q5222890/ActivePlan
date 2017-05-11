package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * @author caoyong
 *
 */
public class AccountBean implements Serializable {
	
    private int id ;//	int	是	账户id
	private int userid	;//int		用户id
	private String phonenum	;//String		用户电话号码
	private double total ;//	double		               余额
	private double fronzen ;//	double		冻结的余额
	private double drawnum ;//	double		正在提现的金额
	private String updatetime ;//string		更新时间
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
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getFronzen() {
		return fronzen;
	}
	public void setFronzen(double fronzen) {
		this.fronzen = fronzen;
	}
	public double getDrawnum() {
		return drawnum;
	}
	public void setDrawnum(double drawnum) {
		this.drawnum = drawnum;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	

}
