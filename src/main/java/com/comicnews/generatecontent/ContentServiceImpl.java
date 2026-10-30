package com.comicnews.generatecontent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.comicnews.newsdigest.domain.News;
import com.comicnews.newsdigest.enumeration.Place;
import com.comicnews.newsdigest.enumeration.Topic;

@Service
public class ContentServiceImpl implements ContentService {
	private final ChatClient chatClient;
	private final ImageModel imageModel;
	private final PromptTemplate comicTemplate;
	private static final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Value("classpath:/promptTemplates/businessWriterTemplate.st")
	Resource businessWriterTemplate;
	
	@Value("classpath:/promptTemplates/sportsWriterTemplate.st")
	Resource sportsWriterTemplate;
	
	@Value("classpath:/promptTemplates/weatherWriterTemplate.st")
	Resource weatherWriterTemplate;
	
	@Value("classpath:/promptTemplates/popCultureWriterTemplate.st")
	Resource popCultureWriterTemplate;
	
	@Value("classpath:/promptTemplates/tragicWriterTemplate.st")
	Resource tragicWriterTemplate;	

	public ContentServiceImpl(ChatClient.Builder chatClientBuilder, ImageModel imageModel, 
			@Value("classpath:/promptTemplates/comicArtistTemplate.st") Resource comicArtistTemplate) {
		this.chatClient = chatClientBuilder.build();
		this.imageModel = imageModel;
		this.comicTemplate =  new PromptTemplate(comicArtistTemplate);
	}
	
	@Override
	public Map<News, String> writeCaptions(List<News> newsStories) {	
		Map<News, String> captions = new HashMap<>();
		for (News newsStory : newsStories) {
			Resource writerTemplate = pickWritingStyle(newsStory);
			var responseEntity = chatClient.prompt()
					.system(systemSpec -> systemSpec
							.text(writerTemplate)
							.param("headlineTitle", newsStory.getHeadlineTitle())
							.param("shortSummary", newsStory.getShortSummary()))
					.user("Set the value, caption=")
					.call()
					.responseEntity(Caption.class);
			
			captions.put(newsStory, responseEntity.entity().toString());
		}
		return captions;
	}
	
	private Resource pickWritingStyle(News newsStory) {
		if (newsStory.getPlace().equals(Place.FOREIGN)) { 
			if (newsStory.getTopic().equals(Topic.BUSINESS))
				return businessWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.POP_CULTURE))
				return popCultureWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.SPORTS))
				return sportsWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.TRAGIC))
				return tragicWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.WEATHER))
				return weatherWriterTemplate;
			else
				log.error("topic={}, can't be mapped to writing style", newsStory.getTopic());
				return null;
		}
		// US_DOMESTIC NEWS
		else {
			if (newsStory.getTopic().equals(Topic.BUSINESS))
				return businessWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.POP_CULTURE))
				return popCultureWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.SPORTS))
				return sportsWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.TRAGIC))
				return tragicWriterTemplate;
			else if (newsStory.getTopic().equals(Topic.WEATHER))
				return weatherWriterTemplate;
			else
				log.error("topic={}, can't be mapped to writing style", newsStory.getTopic());
				return null;
		}
	}

	@Override
	public List<byte[]> draw(Map<News, String> captions) throws IOException {
		List<byte[]> images = new ArrayList<>();
		int count = 0;
		for (Map.Entry<News, String> element : captions.entrySet()) {
			String instructions = comicTemplate.render(Map.of(
					"newsHeadlineTitle", element.getKey().getHeadlineTitle(),
					"caption", element.getValue()));
			log.info("Drawing image for headline={}", element.getKey().getHeadlineTitle());
			String base64Image = generateImage(instructions)
					.getResult()
					.getOutput()
					.getB64Json();
			byte [] imageBytes = Base64.getDecoder().decode(base64Image);
			images.add(imageBytes);
			count++;
		}
		return images;
	}
	
	private ImageResponse generateImage(String instructions) {
		var options = ImageOptionsBuilder.builder()
				.width(1024)
				.height(1024)
				.build();
		var imagePrompt = new ImagePrompt(instructions, options);
		return imageModel.call(imagePrompt);
	}

}
