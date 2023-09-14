package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.entity.LinkItem;
import com.example.service.LinkCounterService;
import com.example.validator.LinkValidator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * LinkCounterController.java Works with form request and save state generate
 * message
 */

@Component
@ManagedBean
@SessionScope
@RequiredArgsConstructor
@Getter
@Setter
@Log4j2
public class LinkCounterController {

	private final LinkCounterService dataService;

	/**
	 * Result list with found links
	 */
	private List<LinkItem> linkList;

	/**
	 * Link which user select in table
	 */
	private LinkItem selectedItem;

	/**
	 * Url chosen for analyze
	 */
	private String analyzePage;

	/**
	 * Method for initialization reset all state variables
	 */
	@PostConstruct
	public void init() {
		selectedItem = null;
		linkList = new ArrayList<LinkItem>();
		analyzePage = "";
	}

	/**
	 * Method reset previous result @selectedItem, validate new input @analyzePage,
	 * and if it is correct send to service for analyze
	 */
	public void getDataForPage() {
		log.info(" Start count for page : {}", getAnalyzePage());
		setSelectedItem(null);
		// cleareTable();
		LinkValidator validator = new LinkValidator();
		if (validator.checkLink(getAnalyzePage())) {
			log.info(" Link is valid : {} ", getAnalyzePage());
			linkList = dataService.getLinksData(getAnalyzePage());
			log.info(" Service return for this page " + getAnalyzePage() + " links : " + linkList.size());
		} else {
			log.info(" Link is not valid : {}", getAnalyzePage());
			displayBadUrlMsg();
		}
	}

	/**
	 * Method change @selectedItem and refresh @analyzePage
	 */
	public void selectLinkRow(SelectEvent<LinkItem> event) {
		log.info("Selected link : {}", event.getObject().getUrl());
		setSelectedItem(event.getObject());
		log.info("Change analyze page to : {}", event.getObject().getUrl());
		setAnalyzePage(getSelectedItem().getUrl());
		displayChangeUrlMsg(event.getObject().getUrl());
	}

	/**
	 * Method delete data from @selectedItem
	 */
	public void cleareTable() {
		log.info("Cleare Table ");
		setSelectedItem(null);
		setLinkList(new ArrayList<LinkItem>());
	}

	/**
	 * Method send message for user with new @selectedItem
	 */
	public void displayChangeUrlMsg(String newUrl) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Инфо", "Выбрана ссылка : " + newUrl);
		log.info("Set change link msg : {}", msg.getDetail().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Method send message for use if @analyzePage is not valid
	 */
	public void displayBadUrlMsg() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Невалидная ссылка!!!");
		log.info("Set bad link : {}", msg.getDetail().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
