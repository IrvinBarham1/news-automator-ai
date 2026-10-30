# Comic News Generator

Turns live news into comic strips. Spring Boot 4.1.1, Spring AI 2.0.1, Java 21.

## Run

Set your keys as environment variables (never in the repo):

    ANTHROPIC_API_KEY=...
    CLOUDFLARE_ACCOUNT_ID=...   (once the image adapter exists)
    CLOUDFLARE_API_TOKEN=...

Then:

    ./mvnw spring-boot:run        (macOS/Linux)
    mvnw.cmd spring-boot:run      (Windows)

The MCP server listens at http://localhost:8080/mcp

## Bounded contexts

| Package       | Context      | Owns                                              |
|---------------|--------------|---------------------------------------------------|
| newsdigest    | News Digest  | fetch, worthiness, post-history audit, category   |
| generateart   | Generate Art | script, review, draw, revise, compose             |
| publishing    | Publishing   | content, post, share, post history                |
| shared        | Contract     | StorySelected event, StoryMode policy             |
