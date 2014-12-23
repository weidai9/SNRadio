package com.wangyijun.Matcher;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// The news mostly comes from the web site techcrunch
// We plan to mix up different tech related web sites later

public class techParser {
	public techParser() {
		
	}
	public List<NBAnews> parseFinal(String url) {
		List<NBAnews> list = new ArrayList<NBAnews>();
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (Exception e) {
			return null;
		}
		Elements elements = document.getElementsByAttributeValue("role", "main");
		if (elements.isEmpty()) {
			return null;
		}
		Elements info = elements.get(0).getElementsByClass("l-main");
		if (info.isEmpty()) {
			return null;
		}
		Elements lastlevel = info.get(0).getElementsByClass("river-block");
		for (Element element : lastlevel) {
			Elements postTitle = element.getElementsByClass("post-title");
			if (postTitle.isEmpty()) {
				continue;
			}
			Elements links = postTitle.get(0).getElementsByTag("a");
			if (links.isEmpty()) {
				continue;
			}
			NBAnews technews = new NBAnews(links.get(0).attr("href"),links.get(0).text(), null);
			technews.setUrl(links.get(0).attr("href"));
			technews.setText(trytoget(links.get(0).attr("href")));
			if (technews.getText() != null) {
				list.add(technews);
			}
		}
		return list;
	}
	private String trytoget(String url) {
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (Exception e) {
			return null;
		}
		Elements elements = document.getElementsByClass("l-main-container");
		if (elements.isEmpty()) {
			return null;
		}
		Elements articleEntry = elements.get(0).getElementsByClass("l-main");
		if (articleEntry.isEmpty()) {
			return null;
		}
		Elements article = articleEntry.get(0).getElementsByTag("p");
		String retString = "";
		for (Element element : article) {
			retString = retString + element.text();
		}
		return retString;
	}
}
