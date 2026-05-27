package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioController {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    public AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
    }

    @GetMapping("audio-to-text")
    public String transcribeAudio(){
        OpenAiAudioTranscriptionOptions options =
                OpenAiAudioTranscriptionOptions
                        .builder()
                        .language("en")
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                        .temperature(0.5f)
                        .build();
        AudioTranscriptionPrompt prompt =
                new AudioTranscriptionPrompt(
                        new ClassPathResource("/audio/sample_audio.mp3"),
                        options
                );
        return openAiAudioTranscriptionModel
                .call(prompt)
                .getResult()
                .getOutput();

    }
}
