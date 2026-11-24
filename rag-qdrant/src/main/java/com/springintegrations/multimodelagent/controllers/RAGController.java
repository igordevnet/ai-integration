package com.springintegrations.multimodelagent.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.ChatClient.PromptSystemSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RAGController {

    private final ChatClient openAiChatModel;
    private final VectorStore vectorStore;
    private final MessageChatMemoryAdvisor messageChatMemoryAdvisor;

    @Value("classPath:/promptTemplates/systemPromptRandomDataTemplate.st")
    private Resource promptTemplate;

    public RAGController(
            @Qualifier("openAiChatClient")
            ChatClient openAiChatModel,
            VectorStore vectorStore,
            MessageChatMemoryAdvisor messageChatMemoryAdvisor
    ) {
        this.openAiChatModel = openAiChatModel;
        this.vectorStore = vectorStore;
        this.messageChatMemoryAdvisor = messageChatMemoryAdvisor;
    }

    @GetMapping("/rag")
    public ResponseEntity<String> askQuestions(
            @RequestParam("message") String message,
            @RequestParam("chatId") String chatId
    ) {


        return ResponseEntity.ok(openAiChatModel.prompt()
                .advisors(messageChatMemoryAdvisor)
                .system(generateSystemPrompt(message))
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content()
        );
    }

    private Consumer<PromptSystemSpec> generateSystemPrompt(String message) {
        var searchQuery = buildSearchQuery(message);

        return promptTemplateSpec -> promptTemplateSpec
                .text(promptTemplate)
                .param("documents", search(searchQuery));
    }

    private SearchRequest buildSearchQuery(String message) {
        return SearchRequest.builder()
                .query(message)
                .topK(5)
                .similarityThreshold(0.5)
                .build();
    }

    private String search(SearchRequest searchQuery) {
        List<Document> response = vectorStore.similaritySearch(searchQuery);

        return response.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
