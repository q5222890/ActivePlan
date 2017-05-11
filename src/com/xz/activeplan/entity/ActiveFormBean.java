package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.6.	活动形式(active_form)
 * @author johnny
 *
 */
public class ActiveFormBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// formid int 是 形式ID
	// name string 形式名字

	private int formid;
	private String name;

	public int getFormid() {
		return formid;
	}

	public void setFormid(int formid) {
		this.formid = formid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
