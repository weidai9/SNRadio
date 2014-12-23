package org.cu.socialnetworkradio.getdata;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NewsFeedInfo implements Serializable {
	private String userid;
	private String message;
	private String userName;
	private String date;
	private String story;
	private String description;
	private boolean nowread;
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public void setNowread(boolean nowread) {
		this.nowread = nowread;
	}
	public boolean isNowread() {
		// TODO Auto-generated method stub
		return nowread;
	}
	
	
	
}
