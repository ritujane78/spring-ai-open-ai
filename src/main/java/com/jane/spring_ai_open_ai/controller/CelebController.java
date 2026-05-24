package com.jane.spring_ai_open_ai.controller;

import com.jane.spring_ai_open_ai.model.Player;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping
public class CelebController {

    private final ChatClient  chatClient;

    @Value("classpath:/prompts/celeb-details.st")
    private Resource celebrityPrompt;

    public CelebController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/popular")
    public List<Player> getInfo(@RequestParam String name){
        BeanOutputConverter<List<Player>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<Player>>() {});

        PromptTemplate template = new PromptTemplate(celebrityPrompt);

        Prompt prompt = template.create(
                Map.of("sport", name, "format", converter.getFormat())
        );
        Generation result =  chatClient
                .prompt(prompt)
                .call()
                .chatResponse()
                .getResult();

        return converter.convert(result.getOutput().getText());
    }
}
