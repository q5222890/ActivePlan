package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.8.	我的参与,票务(my_attend)
 * @author johnny
 *
 */
public class MyAttendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1608968975809198023L;
	
//    id	int			是	参与ID
//	userid	int			是	用户名id
//	activeid	int			是	活动Id
//	ticket	String				票号

	private int id ;
	private int userid ;
	private int activeid ;
	private String ticket ;
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
	

}
