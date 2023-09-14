package com.example.validator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * LinkValidator.java Class for validation url Use REGEX and HttpURLConnection
 * for checking link is valid
 */

@Component
@Slf4j
public class LinkValidator {

	/**
	 * REGEX with pattern for check syntax input
	 */
	private static String REGEX_URL = "((http|https)://)(www.)?[-(a-zA-Z)|(а-яёА-ЯЁ)0-9@:%._\\+~#?&//=]"
			+ "{1,256}\\.[(a-z)|(а-яё)]{2,6}\\b([-(a-zA-Z)|(а-яёА-ЯЁ)0-9@:%._\\+~#?&//=]*)";

	/**
	 * REGEX with pattern for check latin input
	 */
	private static String REGEX_LAT = ".*\\p{IsLatin}.*";;

	/**
	 * Method for check syntax input
	 * 
	 * @param url string for analyze
	 * @return Boolean value
	 */
	private Boolean checkCorrectLink(String url) {
		log.info(" Check url: " + url);
		Pattern patt = Pattern.compile(REGEX_URL);
		Matcher matcher = patt.matcher(url);
		return matcher.matches();
	}

	/**
	 * Method for check latin input
	 * 
	 * @param host string for analyze
	 * @return Boolean value
	 */
	private Boolean checkLatinHost(String host) {
		log.info(" Check is Latin host: " + host);
		Pattern patt = Pattern.compile(REGEX_LAT);
		Matcher matcher = patt.matcher(host);
		return matcher.matches();
	}

	/**
	 * Method for convert cyrillic domain Use java.net.IDN
	 * 
	 * @param host string for convert
	 * @return String with converted domain
	 */
	private String convertCyrillicHost(String host) {
		List<String> labels = Arrays.asList(host.split("\\."));
		List<String> convertLabels = labels.stream()
				.map(s -> java.net.IDN.toASCII(s)).collect(Collectors.toList());
		String result = String.join(".", convertLabels);
		log.info(" Converted cyrilic host: {}", result);
		return result;
	}

	/**
	 * Method parse url, check cyrillic domain, build working url
	 * 
	 * @param url string for analyze
	 * @return url for connect
	 */
	private String getUrlForConnect(String url) {
		String connectUrl = "";
		try {
			URL u = new URL(url);
			String host = u.getHost();
			log.info(" Host Url : {} ", host);
			if (!checkLatinHost(host)) {
				log.info(" Non Latin host: {} ", host);
				String protocol = u.getProtocol();
				log.info(" Protocol Url : {}", protocol);
				String data = "";
				if ((url.lastIndexOf(host) + host.length()) < url.length()) {
					data = url.substring((url.lastIndexOf(host) + host.length()), url.length());
				}
				connectUrl = protocol + "://" + convertCyrillicHost(host) + data;
			} else {
				log.info(" Latin host: {}", host);
				connectUrl = url;
			}
		} catch (IOException e) {
			log.error(" Bad url: {}", url);
			log.error(" Issue in : ", e);
		}
		return connectUrl;
	}

	/**
	 * Method get url, try to send request check Response Code 
	 * 
	 * @param url string for connect
	 * @return Bollean value
	 */
	private Boolean checkLinkWorked(String url) {
		Boolean result = false;
		log.info(" Start check url : {}", url);
		String connectUrl = getUrlForConnect(url);
		log.info(" Try to connect Url : {}", connectUrl);
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection connection = (HttpURLConnection) new URL(connectUrl).openConnection();
			connection.setConnectTimeout(20000);
			connection.setRequestMethod("GET");
			connection.connect();
			log.info(" Responce code url : " + connection.getResponseCode());
			if (!(connection.getResponseCode() + "").startsWith("4")) {
				log.info(" Url works: {}", url);
				result = true;
			}
		} catch (IOException e) {
			log.error(" No connection : {}",  url);
			log.error(" Issue in : ", e);
		}
		return result;
	}

	/**
	 * Method for check input url Contain null check length check syntax check and
	 * connect check
	 * 
	 * @param url string for analyze
	 * @return Boolean value
	 */
	public Boolean checkLink(String url) {
		Boolean result = false;
		if (!(url == null) && !(url.length() > 2083)) {
			if (checkCorrectLink(url)) {
				result = checkLinkWorked(url);
			}
		}
		return result;
	}
}
