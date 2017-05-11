package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 数据返回code
 * 
 * @author johnny
 *
 */
public class StatusBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;
	private String data;
	private int total_size;

	public int getTotal_size() {
		return total_size;
	}

	public void setTotal_size(int total_size) {
		this.total_size = total_size;
	}

	public StatusBean() {
	}

	public StatusBean(int code, String msg, String data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public StatusBean(int code, String msg, String data, int total_size) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.total_size = total_size;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "StatusBean{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data='" + data + '\'' +
				", total_size=" + total_size +
				'}';
	}
}
