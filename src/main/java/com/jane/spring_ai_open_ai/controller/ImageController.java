package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class ImageController {

    private final ChatModel chatModel;

    private final ImageModel  imageModel;

    public ImageController(ChatModel chatModel, ImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
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
    @GetMapping(value = "/image/{prompt}", produces = "image/png")
    public byte[] generateImage(@PathVariable String prompt) {

        ImageResponse response = imageModel.call(
                new ImagePrompt(
                        prompt,
                        OpenAiImageOptions.builder()
                                .N(1)
                                .height(1024)
                                .width(1024)
                                .quality("medium")
                                .build()
                )
        );

        var output = response.getResult().getOutput();

        if (output.getB64Json() != null) {
            return Base64.getDecoder().decode(output.getB64Json());
        }

        throw new RuntimeException("No image generated");
    }
}