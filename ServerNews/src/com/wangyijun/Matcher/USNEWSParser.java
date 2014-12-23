package com.wangyijun.Matcher;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class USNEWSParser {
	public USNEWSParser() {
		
	}
	// We now use the NBAnews schema because it is actually the same. However
	// The parsing might be different. We might extend the NBAnews later.
	public List<NBAnews> parseFinal(String url)
	{
		List<NBAnews> list = new ArrayList<NBAnews>();
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
		// This part is the part to get the important news.
		// The schema of the web site is pretty complicated.
		Elements h4tab = document.getElementsByClass("layout-section");
		if (h4tab.isEmpty())
			return null;
		h4tab = h4tab.get(0).getElementsByAttributeValue("data-grid-area", "left");
		if (h4tab.isEmpty())
			return null;
		Elements links = h4tab.get(0).getElementsByTag("h4");
		for (Element element : links) {
			Elements elements = element.getElementsByTag("a");
			if (elements.isEmpty())
				continue;
			if (elements.get(0).hasAttr("href")) {
				NBAnews news = new NBAnews(elements.get(0).attr("href"), elements.get(0).text(), null);				
				String tmp = elements.get(0).attr("href"); 
				news.setUrl("http://" + tmp.substring(2,tmp.length()));
				news.setText(trytoget(tmp));
				if (news.getText() != null)
					list.add(news);
			}
		}
		return list;
	}
	private String trytoget(String url)
	{
		Document document;
		url = "http://" + url.substring(2,url.length());
		try {
			document =  Jsoup.connect(url).get();
		} catch (Exception e) {
			return null;
		}
		Elements topelements = document.getElementsByClass("wrapper-global_inner");
		if (topelements.isEmpty())
			return null;
		Elements secondElements = topelements.get(0).getElementsByClass("layout-standard");
		if (secondElements.isEmpty())
			return null;
		Elements innerElements = secondElements.get(0).getElementsByAttributeValue("data-grid-area", "left");
		if (innerElements.isEmpty()) {
			return null;
		}
		//System.out.println(innerElements.toString());
		Elements article = innerElements.get(0).getElementsByTag("article");
		if (article.isEmpty())
			return null;
		for (Element element : article.select("footer")) {
			element.remove();
		}
		Elements paragraphs = article.get(0).getElementsByTag("p");
		String retString = new String();
		for (Element element : paragraphs) {
			retString = retString + element.toString();
		}
		retString = retString.replaceAll("</p>", "\n");
		retString = retString.replaceAll("<[^<>]*>", "");
		retString = retString.replaceAll("\\[[^\\[\\]]*\\]", "");
		//System.out.println(retString);
		//System.out.println(" ");
		return retString;		
	}
}
