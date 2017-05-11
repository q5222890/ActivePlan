package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.10.	我关注的个人(my_friend)
 * @author johnny
 *
 */
public class MyFriendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7683027505009862466L;
	
//    id	int			是	参与ID
//    userid	int			是	用户名id
//   friendid	int			是	用户名Id
	
	private int id ;
	private int userid ;
	private int friendid ;
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
	public int getFriendid() {
		return friendid;
	}
	public void setFriendid(int friendid) {
		this.friendid = friendid;
	}
	
	

}
