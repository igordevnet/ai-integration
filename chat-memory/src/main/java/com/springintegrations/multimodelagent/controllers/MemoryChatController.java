package com.springintegrations.multimodelagent.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MemoryChatController {

    private final ChatClient chatClient;
    private final MessageChatMemoryAdvisor messageChatMemoryAdvisor;

    public MemoryChatController(
            @Qualifier("openAiChatClient") ChatClient openAiChatModel,
            MessageChatMemoryAdvisor messageChatMemoryAdvisor
    ) {
        this.chatClient = openAiChatModel;
        this.messageChatMemoryAdvisor = messageChatMemoryAdvisor;
    }

    @GetMapping("/memory/chat")
    public ResponseEntity<String> sendMessage(
            @RequestParam("message") String message,
            @RequestParam("chatId") String chatId
    ) {
        return ResponseEntity.ok(chatClient.prompt()
                .advisors(messageChatMemoryAdvisor)
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content()
        );
    }
}
