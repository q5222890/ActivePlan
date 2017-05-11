package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.13.	活动群(groupinfo)
 * @author jonhnny
 *
 */
public class GroupinfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8751177047788290568L;
	
//    groupid	int			是	群ID
//	groupname	string				群名
//	notice	string				群公告
//	isdisturb	boolean				是否免打扰(1=不打扰,0=默认)
	
	private int groupid;
	private String groupname;
	private String notice ;
	private boolean isdisturb ;
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public boolean isIsdisturb() {
		return isdisturb;
	}
	public void setIsdisturb(boolean isdisturb) {
		this.isdisturb = isdisturb;
	}
	
	


}
