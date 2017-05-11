package com.xz.activeplan.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 推送活动
 * 
 * @author johnny
 *
 */
public class PushActiveBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<CategoryBean> activeinfos;

	public ArrayList<CategoryBean> getActiveinfos() {
		return activeinfos;
	}

	public void setActiveinfos(ArrayList<CategoryBean> activeinfos) {
		this.activeinfos = activeinfos;
	}

}
