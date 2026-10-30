package com.comicnews.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comicnews.NewsService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/news")
public class NewsResource {
	private final NewsService newsService;
	private static final Logger log = LoggerFactory.getLogger(NewsResource.class);
	
	public NewsResource(NewsService newsService) {
		this.newsService = newsService;
	}

	@GetMapping("/create-posts")
	public String createPosts() throws Exception {
		return newsService.generatePosts();
	}
	
}
