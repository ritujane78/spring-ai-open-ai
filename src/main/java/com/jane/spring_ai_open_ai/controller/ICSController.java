package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ICSController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ICSController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/ics")
    public String icsQuestion(@RequestParam String question) {
        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }
}