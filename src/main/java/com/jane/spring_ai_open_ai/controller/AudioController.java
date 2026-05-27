package com.jane.spring_ai_open_ai.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model;

@RestController
public class AudioController {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    @GetMapping("audio-to-text")
    public String transcribeAudio(){
        OpenAiAudioTranscriptionOptions options =
                OpenAiAudioTranscriptionOptions
                        .builder()
                        .language("en")
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT) // for srt format output
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

    @GetMapping("/text-to-audio/{prompt}")
    public ResponseEntity<Resource> transcribeAudio(@PathVariable("prompt") String prompt){
        OpenAiAudioSpeechOptions options =
                OpenAiAudioSpeechOptions
                        .builder()
                        .model(OpenAiAudioApi.TtsModel.TTS_1.getValue())
                        .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                        .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY.getValue())
                        .speed(1.0f)
                        .build();

        SpeechPrompt speechPrompt =
                new SpeechPrompt(prompt, options);

        byte[] output = openAiAudioSpeechModel
                .call(speechPrompt)
                .getResult()
                .getOutput();
        ByteArrayResource byteArrayResource = new ByteArrayResource(output);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(byteArrayResource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("output.mp3")
                                .build().toString())
                .body(byteArrayResource);
    }
}
