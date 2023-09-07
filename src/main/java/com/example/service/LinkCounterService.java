package com.example.service;

import java.util.List;

import com.example.entity.LinkItem;

public interface LinkCounterService {

	public List<LinkItem> getLinksData(String url);

}
