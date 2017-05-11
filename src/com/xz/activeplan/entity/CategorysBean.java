package com.xz.activeplan.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 活动集合
 * 
 * @author johnny
 *
 */
public class CategorysBean implements Serializable {

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
