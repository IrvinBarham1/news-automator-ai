# Comic News Generator

An automated news-reporting pipeline built with **Spring Boot** and **Spring AI**. It researches current news with Anthropic's web search tool, turns stories into comic-style posts with captions, and publishes them to Instagram.

**See it live:** [@whatsgoingon464 Instagram](https://instagram.com/whatsgoingon464)

## Sample output

<!-- Add 1–2 example posts here, e.g. docs/images/sample-1.png -->
<img width="1538" height="910" alt="image" src="https://github.com/user-attachments/assets/f70681bf-16f2-478a-8c12-ef7af380c1e2" />
<img width="1532" height="910" alt="image" src="https://github.com/user-attachments/assets/523eefef-f3c0-4f5c-8fdc-24e1c149ac66" />


## Why I built it

A research project in automated news reporting: can an LLM-driven pipeline reliably gather real news, check what it found, and turn it into shareable content without manual steps in between?

## How it works

```mermaid
flowchart LR
    A[Fetch news<br/>Anthropic web search tool] --> B{Results usable?}
    B -- no, retry --> A
    B -- yes --> C[Analyze & select stories]
    C --> D[Generate comic + caption]
    D --> E[Publish to Instagram]
```

1. **Fetch** — Calls Claude with Anthropic's web search tool to gather current stories and their sources.
2. **Validate & retry** — Checks the returned results and re-queries when they come back thin or off-target.
3. **Analyze** — TODO: how stories are selected and summarized.
4. **Generate** — Captions are generated depending on the topic of the news that is reported; Different news topics are reported uniquely. Images are generated based upon the caption and the News object which gets created when the news is researched.
5. **Publish** — TODO: automated via API, or prepared for manual upload.

## Design decisions

<img width="1417" height="679" alt="image" src="https://github.com/user-attachments/assets/e5f7acdc-1b4e-468f-a0fa-7f486c65e2ee" />
<img width="682" height="569" alt="image" src="https://github.com/user-attachments/assets/9127e774-18ce-4a3e-a4f9-6a2a4c6d5c1a" />
<img width="1178" height="372" alt="image" src="https://github.com/user-attachments/assets/4f1f79c7-6843-46b2-bf00-2cbbeeb540b1" />


## Tech stack

| Area | Technology |
|---|---|
| Language | Java TODO |
| Framework | Spring Boot TODO, Spring AI TODO |
| LLM & search | Anthropic Claude API + web search tool |
| Build | Maven |
| Publishing | Instagram |

## Getting started

**Prerequisites:** Java TODO+, an Anthropic API key.

```bash
git clone https://github.com/TODO-username/comic-news-generator.git
cd comic-news-generator
export ANTHROPIC_API_KEY=your-key
./mvnw spring-boot:run
```
