package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.9. 我关注的主办方(my_host)
 * 
 * @author johnny
 * 
 */
public class MyHostBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762797760557932347L;

	// id int 是 参与ID
	// userid int 是 用户名id
	// hostid int 是 主办方Id
	private int id;
	private int userid;
	private int hostid;

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

	public int getHostid() {
		return hostid;
	}

	public void setHostid(int hostid) {
		this.hostid = hostid;
	}

}
