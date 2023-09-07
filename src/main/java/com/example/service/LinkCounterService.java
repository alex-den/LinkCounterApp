package com.example.service;

import java.util.List;

import com.example.entity.LinkItem;

/** LinkCounterService.java 
 *  Simple interface with method contract
 */

public interface LinkCounterService {

	public List<LinkItem> getLinksData(String url);

}
