package com.wangyijun.Matcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class EntryMain {
	public static void main(String[] args) {
		
		NBAParser nbaParser = new NBAParser();
		List<NBAnews> nbalist = nbaParser.parseFinal("http://www.nba.com/news/?ls=iref:nba:gnav");
		
		USNEWSParser usnewsParser = new USNEWSParser();
		List<NBAnews> usnewslist = usnewsParser.parseFinal("http://www.usnews.com/opinion");
		
		techParser tech = new techParser();
		List<NBAnews> techList = tech.parseFinal("http://techcrunch.com/");
		
		listIntoTable(nbalist, "nbaNewsTable");
		listIntoTable(usnewslist, "usNewsTable");
		listIntoTable(techList,"techNewsTable");
		
	}
	private static void listIntoTable(List<NBAnews> list,String tableName) {
		DBhelper dBhelper = new DBhelper();
		for (NBAnews nbAnews : list) {
			Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
			map.put("title", new AttributeValue(nbAnews.getTitle()));
			map.put("url", new AttributeValue(nbAnews.getUrl()));
			map.put("date", new AttributeValue(nbAnews.getTime().toString()));
			map.put("text", new AttributeValue(nbAnews.getText()));
			if (nbAnews.getInfo() != null) {
				map.put("info", new AttributeValue(nbAnews.getInfo()));
			} else {
				map.put("info", new AttributeValue("null"));
			}
			dBhelper.addItem(tableName, map);
		}
	}
}
