package com.springintegrations.multimodelagent.controllers;

import com.springintegrations.multimodelagent.controllers.dtos.OutputType;
import com.springintegrations.multimodelagent.controllers.dtos.StockRecordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/structured-response-list")
    public ResponseEntity<List<StockRecordResponse>> getStockMetricsList(@RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<StockRecordResponse>>() {})
        );
    }

    @GetMapping("structured-list-map")
    public ResponseEntity<Object> getListMap(
            @RequestParam("message") String message,
            @RequestParam("type") OutputType type
    ) {
        if(!message.isBlank()) {
            Object response = switch (type) {
                case MAP -> getResponse(message, new MapOutputConverter());
                case LIST -> getResponse(message, new ListOutputConverter());
            };

            return ResponseEntity.ok(response);
        } else {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }

    private <T> T getResponse(
            String message,
            StructuredOutputConverter<T> typeConverter
    ) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(typeConverter);
    }
}
