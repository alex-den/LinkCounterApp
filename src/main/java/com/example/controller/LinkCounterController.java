package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.stereotype.Component;

import com.example.entity.LinkItem;
import com.example.service.LinkCounterService;
import com.example.validator.LinkValidator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ManagedBean
@ViewScoped
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j 
public class LinkCounterController {

	private final LinkCounterService dataService;
	
	private List<LinkItem> linkList;
	private LinkItem selectedItem;
	private String analyzePage;
	private String linkCounter;

    @PostConstruct
    public void init() {
    	selectedItem = null;
    	linkList = new ArrayList<LinkItem>();
    	analyzePage = "";
    }
	
	public void getDataForPage() {
		log.info(" Start count for page : " + getAnalyzePage());
		setSelectedItem(null);
		//cleareTable(); 
		LinkValidator validator = new LinkValidator();
		if(validator.checkLink(getAnalyzePage())) {
			log.info(" Link is valid : " + getAnalyzePage());
			linkList = dataService.getLinksData(getAnalyzePage());
			log.info(" Service return for this page " + getAnalyzePage() + " links : " + linkList.size());
		} else {
			log.info(" Link is not valid : " + getAnalyzePage());
			displayBadUrlMsg();
		}
	}
	
	public void selectLinkRow(SelectEvent<LinkItem> event) {
		log.info("Selected link : " + event.getObject().getUrl());
		setSelectedItem(event.getObject());
		log.info("Change analyze page to : " + event.getObject().getUrl());
		setAnalyzePage(getSelectedItem().getUrl());
		displayChangeUrlMsg(event.getObject().getUrl()); 
	}
	
	public void cleareTable() {
		log.info("Cleare Table ");
		setSelectedItem(null);
		setLinkList(new ArrayList<LinkItem>());
	}
	
	public void displayChangeUrlMsg(String newUrl) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Инфо", "Выбрана ссылка : " + newUrl);   
		log.info("Set change link msg : " + msg.getDetail().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
	
	public void displayBadUrlMsg() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Невалидная ссылка!!!");   
		log.info("Set bad link : " + msg.getDetail().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
}
