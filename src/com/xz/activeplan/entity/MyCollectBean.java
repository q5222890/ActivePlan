package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.7.	我的收藏(my_collect)
 * @author johnny
 *
 */
public class MyCollectBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8352798617020994356L;

	// collectid int 是 收藏ID
	// userid int 是 用户名id
	// activeid int 是 活动Id

	private int collectid;
	private int userid;
	private int activeid;

	public int getCollectid() {
		return collectid;
	}

	public void setCollectid(int collectid) {
		this.collectid = collectid;
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

}
