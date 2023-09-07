package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.example.entity.LinkItem;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j 
public class LinkCounterServiceImpl implements LinkCounterService {

	@Override
	public List<LinkItem> getLinksData(String url) {
		List<LinkItem> result = new ArrayList<>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			log.info(" Found Links : " + links.size());
			int count = 1;
			for (Element link : links) {
				
				log.info(" Found : " + link.attr("href"));
				LinkItem li = new LinkItem();
				li.setId(count);
				li.setName(link.text());
				li.setUrl(link.absUrl("href"));
				result.add(li);
				count++;
			}
		} catch (IOException e) {
			log.error(" Can not to connect : " + url);
			log.error(" Issue in : ", e);
		}
		return result;
	}
}
