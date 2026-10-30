package com.comicnews;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comicnews.generatecontent.ContentService;
import com.comicnews.newsdigest.NewsDigestService;
import com.comicnews.newsdigest.domain.News;
import com.comicnews.publishing.PostService;

@Service
public class NewServiceImpl implements NewsService{
	private final NewsDigestService newsDigestService;
	private final ContentService contentService;
	private final PostService postService;
	private static final Logger log = LoggerFactory.getLogger(NewServiceImpl.class);
	
	public NewServiceImpl(NewsDigestService newsDigestService, ContentService generateContentService, PostService postService) {
		this.newsDigestService = newsDigestService;
		this.contentService = generateContentService;
		this.postService = postService;
	}

	@Override
	public String generatePosts() throws Exception {
		List<News> newsList = newsDigestService.fetchNews();
		log.info("Got {} stories, sending over to caption writers", newsList.size());
		
		Map<News, String> captions = contentService.writeCaptions(newsList);
		log.info("Got {} captions, sending over to artists", captions.size());
		
		List<byte[]> images = contentService.draw(captions);
		log.info("Got {} images, sending over to publishers", images.size());
		
		String response = postService.createPosts(captions, images);
		return response;
	}
}
