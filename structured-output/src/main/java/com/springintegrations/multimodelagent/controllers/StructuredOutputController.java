package com.springintegrations.multimodelagent.controllers;

import com.springintegrations.multimodelagent.controllers.dtos.StockRecordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class StructuredOutputController {

    private final ChatClient chatClient;

    public StructuredOutputController(
            @Qualifier("openAiChatClient")
            final ChatClient chatClient
    ) {
        this.chatClient = chatClient;
    }

    @GetMapping("/structured-response")
    public ResponseEntity<StockRecordResponse> getStockMetrics(@RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient
                .prompt()
                .user(message)
                .call()
                .entity(StockRecordResponse.class)
        );
    }
}
