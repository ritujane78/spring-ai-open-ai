package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ICSController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ICSController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
//                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
         .build(); // QuestionAnswerAdvisor available in SpringAI versions <= 1.0.0
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

    private String prompt = """
            Your task is to answer the questions about Indian Constitution. Use the information from the DOCUMENTS
            section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section, 
            simply state that you don't know the answer.
                        
            QUESTION:
            {input}
                        
            DOCUMENTS:
            {documents}
                        
            """;

    @GetMapping("/ic")
    public String icQuestion(@RequestParam String question) {
        PromptTemplate template = new PromptTemplate(prompt);

        Map<String , Object> promptParams = new HashMap<>();

        promptParams.put("input", question);
        promptParams.put("documents", findSimilarData(question));

        return chatClient
                .prompt(template.create(promptParams))
                .call()
                .content();

    }

    private String findSimilarData(String question) {
       List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(5)
                        .build()
        );
               return documents
                       .stream()
                       .map(d ->
                               d.getFormattedContent()
                                       .toString())
                       .collect(Collectors.joining());
    }
}