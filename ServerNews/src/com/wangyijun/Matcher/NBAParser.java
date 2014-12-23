package com.wangyijun.Matcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NBAParser {
	public NBAParser() {
		
	}
	public List<NBAnews> parseFinal(String url)
	{
		List<NBAnews> retlist = new ArrayList<NBAnews>();
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Element tabarea = document.getElementById("nbaNewsMainTabArea");
		Elements contents = tabarea.getElementsByClass("nbaEachNewsTbl");
		for (Element content : contents) {
			Elements links = content.getElementsByTag("a");
			Elements info = content.getElementsByClass("nbaNewsInfo");	
			if (links.isEmpty())
				continue;
			info.get(0).select("a").remove();
			info = info.get(0).getElementsByTag("p");
			NBAnews news = new NBAnews(links.get(0).attr("href"), links.get(0).text(), info.get(0).text());
			news.setText(trytoget(links.get(0).attr("href")));
			news.setUrl("http://www.nba.com"+links.get(0).attr("href"));
			if (news.getText() != null)
				retlist.add(news);
		}
		return retlist;
	}
	private String trytoget(String url)
	{
		url = "http://www.nba.com" + url;
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		Element area = document.getElementById("nbaArticleContent");
		String text = area.toString();
		text = text.replaceAll("</p>", "\n");
		text = text.replaceAll("<[^<>]*>", "");
		text = text.trim();
		text = text.substring(0, text.length()-9);
		text = text.trim();
		return text;
	}
}
