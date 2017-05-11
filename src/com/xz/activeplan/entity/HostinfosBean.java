package com.xz.activeplan.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 关注的主办方集合
 * 
 * @author johnny
 *
 */
public class HostinfosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// hostinfos array 数组

	private ArrayList<OrganizersBean> hostinfos;

	public ArrayList<OrganizersBean> getHostinfos() {
		return hostinfos;
	}

	public void setHostinfos(ArrayList<OrganizersBean> hostinfos) {
		this.hostinfos = hostinfos;
	}

}
