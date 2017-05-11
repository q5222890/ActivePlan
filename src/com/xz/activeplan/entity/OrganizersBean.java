package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 我关注的主办方
 * 
 * @author johnny
 *
 */
public class OrganizersBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;// int 我的关注的主办方列表ID
	private int hostid;// int 主办方id
	private int userid; // int 用户名id(一个用户有多个主办方)
	private String hostname;// string 主办方单位
	private String hostcontact;// string 主办方联系人
	private String hostphone;// string 主办方联系号码
	private String hostmail;// string 主办方邮箱
	private String hostintro;// string 主办方简介
	private String hosthearurl;// string 主办方头像
	private String hosturl;// string 主办方网址
	private int follownum; // int 关注人数
	private boolean isattend ;//是否关注
	private int fans; //粉丝数
	private String intro;  //介绍
	private int releaseNum; //发布活动数

	public boolean isattend() {
		return isattend;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getReleaseNum() {
		return releaseNum;
	}

	public void setReleaseNum(int releaseNum) {
		this.releaseNum = releaseNum;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHostid() {
		return hostid;
	}

	public void setHostid(int hostid) {
		this.hostid = hostid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHostcontact() {
		return hostcontact;
	}

	public void setHostcontact(String hostcontact) {
		this.hostcontact = hostcontact;
	}

	public String getHostphone() {
		return hostphone;
	}

	public void setHostphone(String hostphone) {
		this.hostphone = hostphone;
	}

	public String getHostmail() {
		return hostmail;
	}

	public void setHostmail(String hostmail) {
		this.hostmail = hostmail;
	}

	public String getHostintro() {
		return hostintro;
	}

	public void setHostintro(String hostintro) {
		this.hostintro = hostintro;
	}

	public String getHosthearurl() {
		return hosthearurl;
	}

	public void setHosthearurl(String hosthearurl) {
		this.hosthearurl = hosthearurl;
	}

	public String getHosturl() {
		return hosturl;
	}

	public void setHosturl(String hosturl) {
		this.hosturl = hosturl;
	}

	public int getFollownum() {
		return follownum;
	}

	public void setFollownum(int follownum) {
		this.follownum = follownum;
	}

	public boolean isIsattend() {
		return isattend;
	}

	public void setIsattend(boolean isattend) {
		this.isattend = isattend;
	}

}
