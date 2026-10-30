package com.comicnews.publishing;

import java.util.List;
import java.util.Map;

import com.comicnews.newsdigest.domain.News;

public interface PostService {

	public String createPosts(Map<News, String> captions, List<byte[]> images);
	
}
