package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * 票务bean
 * 
 * @author johnny
 * 
 */
public class TicketListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ticketcheck;// boolean 是否验票
	
	private String headurl ;
	private String username ;
	private String phonenum ;
	private String ticket;
	public boolean isTicketcheck() {
		return ticketcheck;
	}
	public void setTicketcheck(boolean ticketcheck) {
		this.ticketcheck = ticketcheck;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	

}
