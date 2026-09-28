package com.springintegrations.multimodelagent.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient ollamaChatModel;
    private final ChatClient openAiChatModel;

    public ChatController(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatModel,
            @Qualifier("openAiChatClient") ChatClient openAiChatModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.openAiChatModel = openAiChatModel;
    }

    @GetMapping("/message")
    public String askAi(
            @RequestParam("message") String message,
            @RequestParam("model") String model
    ) {
        return switch (model) {
            case "CHATGPT" -> sendPromptToAi(message, openAiChatModel);
            case "OLLAMA" -> sendPromptToAi(message, ollamaChatModel);
            default -> throw new RuntimeException("Please choose a model.");
        };
    }

    private String sendPromptToAi(String message, ChatClient chatClient) {
        return chatClient.prompt(message).call().content();
    }
}
