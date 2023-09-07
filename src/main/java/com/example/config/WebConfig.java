package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** WebConfig.java 
 *  Simple configuration class 
 *  Used for forwarding to the index page by default. 
 */

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addViewControllers(ViewControllerRegistry linkCounter) {
		linkCounter.addViewController("/").setViewName("forward:/index.xhtml");
		linkCounter.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

}
