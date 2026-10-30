package com.comicnews.newsdigest.domain;

import com.comicnews.newsdigest.enumeration.Place;
import com.comicnews.newsdigest.enumeration.Subject;
import com.comicnews.newsdigest.enumeration.Topic;

public class News {
	
	private String headlineTitle;
	private String sourceLink;
	private String shortSummary;
	private Topic topic;
	private Place place;
	private Subject subject;
	private Target target;
	
	public News(String headlineTitle, String sourceLink, String shortSummary, Topic topic, Place place, Subject subject, Target target) {
		this.headlineTitle = headlineTitle;
		this.sourceLink = sourceLink;
		this.shortSummary = shortSummary;
		this.topic = topic;
		this.place = place;
		this.subject = subject;
		this.target = target;
	}

	public String getHeadlineTitle() {
		return headlineTitle;
	}

	public String getSourceLink() {
		return sourceLink;
	}
	
	public String getShortSummary() {
		return shortSummary;
	}

	public Topic getTopic() {
		return topic;
	}

	public Place getPlace() {
		return place;
	}

	public Subject getSubject() {
		return subject;
	}

	public Target getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return "News [headlineTitle=" + headlineTitle + ", sourceLink=" + sourceLink + ", shortSummary=" + shortSummary
				+ ", topic=" + topic + ", place=" + place + ", subject=" + subject + ", target=" + target + "]";
	}
	
}
