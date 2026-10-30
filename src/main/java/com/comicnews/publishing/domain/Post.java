package com.comicnews.publishing.domain;

import com.comicnews.newsdigest.domain.News;

public class Post {
	private byte[] image;
	private String caption;
	private News newsStory;
	
	public Post(byte[] image, String caption, News newsStory) {
		this.image = image;
		this.caption = caption;
		this.newsStory = newsStory;
	}

	public byte[] getImage() {
		return image;
	}

	public String getCaption() {
		return caption;
	}

	public News getNewsStory() {
		return newsStory;
	}

	@Override
	public String toString() {
		return "Post [caption=" + caption + ", newsStory=" + newsStory + "]";
	}

	
}
