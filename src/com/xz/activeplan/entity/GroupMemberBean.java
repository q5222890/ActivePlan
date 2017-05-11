package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.14. 群成员(group_member)
 * 
 * @author johnny
 * 
 */
public class GroupMemberBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9054500469296062939L;

	// id int 是 群成员ID
	// groupid int 群ID
	// userid int 用户id
	// username string 用户名
	// headurl string 用户头像

	private int id;
	private int groupid;
	private int userid;
	private String username;
	private String headurl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadurl() {
		return headurl;
	}

	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}

}
