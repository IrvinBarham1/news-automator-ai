package com.comicnews.newsdigest;

import java.util.List;

import com.comicnews.newsdigest.domain.News;

public interface NewsDigestService {

	List<News> fetchNews() throws Exception;
	
}
