package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.example.entity.LinkItem;

import lombok.extern.slf4j.Slf4j;

/** LinkCounterServiceImpl.java 
 *  Implementation @LinkCounterService
 *  Use Jsoup for parse html page
 */

@Service
@Slf4j 
public class LinkCounterServiceImpl implements LinkCounterService {

	/**  
	 * Method get url for analyze parse page and return found links 
	 * @param url page for analyze
	 * @return list with found links
	 */
	@Override
	public List<LinkItem> getLinksData(String url) {
		List<LinkItem> result = new ArrayList<>();
		try {
			result = Jsoup.connect(url).get().select("a[href]").stream()
					.peek(link -> log.info(" Found : {}", link.attr("href")))
					.map(link -> new LinkItem(link.text(), link.absUrl("href"))).collect(Collectors.toList());
		} catch (IOException e) {
			log.error(" Bad url: {}", url);
			log.error(" Issue in : ", e);
		}
		return result;
		
	}
}
