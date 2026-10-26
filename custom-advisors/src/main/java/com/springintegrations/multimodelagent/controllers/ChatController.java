package com.springintegrations.multimodelagent.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.PromptUserSpec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient ollamaChatModel;
    private final ChatClient openAiChatModel;

    @Value("classPath:/promptTemplates/emailPromptTemplate.st")
    private Resource promptTemplate;

    public ChatController(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatModel,
            @Qualifier("openAiChatClient") ChatClient openAiChatModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.openAiChatModel = openAiChatModel;
    }

    @GetMapping("/email")
    public String askAi(
            @RequestParam("customerName") String customerName,
            @RequestParam("message") String message,
            @RequestParam("model") String model
    ) {
        model = model.toUpperCase();

        var userMessage = buildUserMessage(message, customerName);

        return switch (model) {
            case "CHATGPT" -> sendPromptToAi(userMessage, openAiChatModel);
            case "OLLAMA" -> sendPromptToAi(userMessage, ollamaChatModel);
            default -> throw new RuntimeException("Please choose a model.");
        };
    }

    private String sendPromptToAi(
            Consumer<PromptUserSpec> userMessage,
            ChatClient chatClient
    ) {
        log.info("Prompt sent to agent using model: " + chatClient.getClass().getName());

        return chatClient
                .prompt()
                .system("""
                        You are a professional customer service assistant which helps drafting emails/s
                        responses to improve productivity of the customer support team
                        """)
                .user(userMessage)
                .call()
                .content();
    }

    private Consumer<PromptUserSpec> buildUserMessage(String message, String customerName) {
        return promptUserSpec -> promptUserSpec
                .text(promptTemplate)
                .param("customerName", customerName)
                .param("customerMessage", message);
    }
}
