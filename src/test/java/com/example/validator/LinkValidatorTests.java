package com.example.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LinkValidatorTests{
		
	@Test
	void checkCorrectLinkTest() {
		LinkValidator linkValidator = new LinkValidator();
		assertEquals(true,linkValidator.checkLink("https://www.google.com/"));
		assertEquals(true,linkValidator.checkLink("https://www.x.com/"));
		assertEquals(true,linkValidator.checkLink("https://www.primefaces.org/showcase/ui/data/datatable/paginator.xhtml?jfwid=fe991"));
		assertEquals(true,linkValidator.checkLink("https://объясняем.рф/"));
		assertEquals(true,linkValidator.checkLink("https://объясняем.рф/articles/news/"));
		assertEquals(true,linkValidator.checkLink("https://розы.бел"));
	}
	
	@Test
	void checkIncorrectLinkTest() {
		LinkValidator linkValidator = new LinkValidator();
		assertEquals(false,linkValidator.checkLink("google.com"));
		assertEquals(false,linkValidator.checkLink("om"));
		assertEquals(false,linkValidator.checkLink(".com"));
		assertEquals(false,linkValidator.checkLink("//www.primefaces.org/showcase/index.xhtml"));
		assertEquals(false,linkValidator.checkLink("объясняем.рф"));
	}

}
