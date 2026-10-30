package com.comicnews.shared;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Domain event and the only contract between News Digest and Generate Art.
 * The script writer may quote only what is in {@code verifiedQuotes}.
 */
public record StorySelected(
		String storyId,
		String headline,
		List<URI> sources,
		List<String> keyFacts,
		List<String> verifiedQuotes,
		String category,
		StoryMode mode,
		Instant selectedAt) {

	public StorySelected {
		sources = List.copyOf(sources);
		keyFacts = List.copyOf(keyFacts);
		verifiedQuotes = List.copyOf(verifiedQuotes);
	}

}
