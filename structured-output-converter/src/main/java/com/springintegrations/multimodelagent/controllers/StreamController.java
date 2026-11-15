package com.springintegrations.multimodelagent.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(@Qualifier("openAiChatClient") ChatClient openAiChatModel) {
        this.chatClient = openAiChatModel;
    }

    @GetMapping("/stream")
    public Flux<String> chat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }
}
