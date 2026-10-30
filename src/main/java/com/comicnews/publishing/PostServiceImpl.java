package com.comicnews.publishing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comicnews.newsdigest.domain.News;
import com.comicnews.publishing.domain.Post;

@Service
public class PostServiceImpl implements PostService {
	private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

	@Override
	public String createPosts(Map<News, String> captions, List<byte[]> images) {
		List<Post> posts = new ArrayList<>();
		int index = 0;
		for (Map.Entry<News, String> entry : captions.entrySet()) {
			byte[] image = images.get(index);
			String caption = entry.getValue();
			News newsStory = entry.getKey();
			Post post = new Post(image, caption, newsStory);
			posts.add(post);
			log.info("Created Post : " + post.toString());
			index++;
		}
		boolean isSaved;
		try {
			isSaved = savePosts(posts);
			if (isSaved)
				return "Posts created successfully";
			else
				return "Failed to create posts";
		}
		catch (IOException e) {
			e.printStackTrace();
			return "Failed to create posts";
		}
	}
	
	private boolean savePosts(List<Post> posts) throws IOException {
		try {
			String fileDate = LocalDate.now().toString();
			int count = 0;
			for(Post post : posts) {
				String postInformation = post.getNewsStory().toString() + "\n" + post.getCaption();
				Files.writeString(Path.of("caption-" + fileDate + String.valueOf(count) + ".txt"), postInformation);
				Files.write(Path.of("image-" + fileDate + String.valueOf(count) + ".png"), post.getImage());
				count++;
			}
			return true;
		}
		catch (Exception error) { 
			log.error("Failed to save generated posts {}", error);
			return false;
		}
	}

}
