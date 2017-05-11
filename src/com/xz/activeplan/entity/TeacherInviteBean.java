package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.12. 邀请名师列表(teacher_invite)
 * 
 * @author johnny
 * 
 */
public class TeacherInviteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8354898314637091995L;

	// id int 是 列表id
	// userid int 用户名
	// teacherid int 名师id

	private int id;
	private int userid;
	private int teacherid;

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

	public int getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}

}
