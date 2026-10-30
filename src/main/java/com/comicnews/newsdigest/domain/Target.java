package com.comicnews.newsdigest.domain;

import java.util.EnumMap;
import java.util.Map;

import com.comicnews.newsdigest.enumeration.Audience;

public class Target {
	private Map<Audience, Double> audienceProportion = new EnumMap<>(Audience.class);
	

	public Target(Map<Audience, Double> audienceProportion) {
		for (Audience audience : Audience.values()) {
			audienceProportion.put(audience, null);
		}
	}

	public Map<Audience, Double> getAudienceProportion() {
		return audienceProportion;
	}

}
