package com.comicnews.newsdigest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.AnthropicWebSearchResult;
import org.springframework.ai.anthropic.AnthropicWebSearchTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.comicnews.newsdigest.domain.News;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.WebSearchResultBlock;
import com.anthropic.models.messages.WebSearchTool20250305;

@Service
public class NewsDigestServiceImpl implements NewsDigestService {
	
	private final AnthropicClient anthropic;
	private final ChatClient chatClient;   // plain client for step 2
	private static final Logger log =LoggerFactory.getLogger(NewsDigestServiceImpl.class);
	
	@Value("classpath:/promptTemplates/fetchNewsTemplate.st")
	Resource fetchNewsTemplate;
	
	public NewsDigestServiceImpl(ChatClient.Builder chatClientBuilder, AnthropicChatModel chatModel) {
	    this.anthropic = chatModel.getAnthropicClient();
	    this.chatClient = chatClientBuilder
	            .defaultOptions(AnthropicChatOptions.builder().thinkingDisabled())
	            .build();
	}

	// TODO
	// Add Evaluator for Factual Checking of the news, which will also help add important details to the shortSummary
	@Override
	public List<News> fetchNews() throws Exception {
		LocalDate todaysDate = LocalDate.now();
		String systemPrompt = new PromptTemplate(fetchNewsTemplate).render(Map.of("todaysDate", todaysDate.toString()));		
		Message message = anthropic.messages().create(MessageCreateParams.builder()
		        .model("claude-haiku-5-5")
		        .maxTokens(4096L)
				.system(systemPrompt)
		        .addUserMessage("What are today's top 3 news headlines?")
		        .addTool(WebSearchTool20250305.builder().maxUses(3L).build())
		        .build());

		StringBuilder findings = new StringBuilder();
		List<WebSearchResultBlock> sources = new ArrayList<>();

		for (ContentBlock block : message.content()) {
			// Claude chat response
		    if (block.isText()) {
		        findings.append(block.asText().text());
		    } 
		    // Claude search results
		    else if (block.isWebSearchToolResult()) {
		        var content = block.asWebSearchToolResult().content();
		        if (content.isResultBlocks())
		            sources.addAll(content.asResultBlocks());
		        else if (content.isError())
		            log.warn("Search failed: {}", content.asError().errorCode());
		    }
		}
		if (sources.isEmpty()) {
		    // retry or fail here
			log.error("Failed");
			throw new Exception("Failed to Provide News Stories");
		}
		sources.forEach(s -> log.info("{} | {} | {}", s.pageAge().orElse("?"), s.title(), s.url()));
		log.info("findings={}", findings);
		
		
		
		List<News> newsStories = new ArrayList<>();
		newsStories = chatClient.prompt()
		            .user(u -> u.text("Convert these news findings into the requested format:\n\n{findings}")
		                        .param("findings", findings))
		            .call()
		            .entity(new ParameterizedTypeReference<List<News>>() {});
		
		if (newsStories.size() > 3) {
			log.info("Trimming news stories list from {} to 3", newsStories.size());
			newsStories = newsStories.subList(0, 2);
		}
		return newsStories;
	}

	private void logUsage(Usage usage) {
		log.info("Token usage: prompt={}, generation={}, total={}",
		usage.getPromptTokens(),
		usage.getCompletionTokens(),
		usage.getTotalTokens());
	}
	

	private void auditPostHistory(List<News> news) {
		// TODO Auto-generated method stub
		
	}

}
