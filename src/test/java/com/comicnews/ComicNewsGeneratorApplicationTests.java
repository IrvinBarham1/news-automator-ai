package com.comicnews;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Chat model disabled and in-memory DB so the context test runs without API keys.
@SpringBootTest(properties = {
		"spring.ai.model.chat=none",
		"spring.datasource.url=jdbc:h2:mem:test"
})
class ComicNewsGeneratorApplicationTests {

	@Test
	void contextLoads() {
	}

}
