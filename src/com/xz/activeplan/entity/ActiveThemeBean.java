package com.xz.activeplan.entity;

import java.io.Serializable;


/**
 * 2.1.5.	活动主题(active_theme)
 * @author johnny
 *
 */
public class ActiveThemeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3943099681184822778L;

	// themeid int 是 主题ID
	// name string 主题名字

	private int themeid;
	private String name;

	public int getThemeid() {
		return themeid;
	}

	public void setThemeid(int themeid) {
		this.themeid = themeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
