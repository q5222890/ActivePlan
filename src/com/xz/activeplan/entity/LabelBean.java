package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 2.1.4.	标签(label)
 * @author johnny
 *
 */
public class LabelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7990848358461229452L;
	// labelid int 是 标签ID
	// name string 标签名字

	private int labelid;
	private String name;

	public int getLabelid() {
		return labelid;
	}

	public void setLabelid(int labelid) {
		this.labelid = labelid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
