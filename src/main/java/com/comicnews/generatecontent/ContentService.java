package com.comicnews.generatecontent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.comicnews.newsdigest.domain.News;

public interface ContentService {
	
	Map<News, String> writeCaptions(List<News> newsStories);
	
	List<byte[]> draw(Map<News, String> captions) throws IOException;

}