package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final ChatModel chatModel;

    public ImageController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/image-to-text")
    public String describeImage() {

        String response =  ChatClient.create(chatModel)
                .prompt()
                .user(u -> u
                        .text("Explain what you see in this image.")
                        .media(MimeTypeUtils.IMAGE_JPEG,
                                new ClassPathResource("/images/horse-8209533_1280.jpg"))
                )
                .call()
                .content();
        return response;
    }
}