package org.cu.socialnetworkradio.getdata;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "usNewsTable")
public class UsNewsInfo {
	private String title;
	private String date;
	private String info;
	private String text;
	private String url;
	
	@DynamoDBHashKey(attributeName = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@DynamoDBAttribute(attributeName = "date")
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@DynamoDBAttribute(attributeName = "info")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@DynamoDBAttribute(attributeName = "text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@DynamoDBAttribute(attributeName = "url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}