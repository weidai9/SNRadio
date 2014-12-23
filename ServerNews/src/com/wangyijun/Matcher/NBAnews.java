package com.wangyijun.Matcher;

import java.util.Date;

// currently this is used for both nba news and also usnews, the name could be a little misleading
public class NBAnews {
	String link;
	String title;
	String info;
	String text;
	String url;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	Date time;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public NBAnews(String link, String title, String info) {
		super();
		this.link = link;
		this.title = title;
		this.info = info;
		this.time = new Date();
	}
	
 }
